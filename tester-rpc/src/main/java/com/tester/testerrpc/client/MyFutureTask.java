package com.tester.testerrpc.client;


import java.lang.reflect.Field;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.locks.LockSupport;


/**
 * 模仿futuretask，用来在netty channel外获取channel的返回值。供外部调用的方法有3个：{@linkplain MyFutureTask#putMySelf() putMySelf()}、
 * {@linkplain MyFutureTask#set(long, V) set(long,V)}、{@linkplain MyFutureTask#myGet(long, TimeUnit) myGet(long,TimeUnit)}
 * <p>建议单例。</p>
 * <p>MyFutureTask 应该是 DubboServiceClient 的一部分，不应该在外部初始化。应该设计成DubboServiceClient的内部类</p>
 *
 * @author cv
 * Aug 13, 2019
 */
public class MyFutureTask<V> {

    // main for testing
    public static void main(String[] args) {
        FutureTask<Object> f;
        // ConcurrentHashMap 不能存 null 的key
        ConcurrentHashMap<String, String> StrMap = new ConcurrentHashMap<String, String>();
    }

    // 用来实现 MyFutureTask 的在单例情况下处理多个线程的请求。
    private static Map<Long, MyWaitNode> waitNodes = new ConcurrentHashMap<Long, MyWaitNode>();
    private static final int NEW = 0;
    private static final int COMPLETING = 1;
    private static final int NORMAL = 2;
    private static final int CANCELLED = 4;

    // ***************************************** 弃用 **************************************************************
    private volatile int state;
    /**
     * The result to return or exception to throw from get()
     */
    private Object outcome; // non-volatile, protected by state reads/writes
    // 保存所有正在等待 get() 返回值的线程。
    /**
     * Treiber stack of waiting threads
     */
    private volatile WaitNode waiters;
    //暂时没有作用
    /**
     * The thread running the callable; CASed during run()
     */
    private volatile Thread runner;
    // ***************************************** 弃用 **************************************************************

    public MyFutureTask() {
        this.state = NEW;       // ensure visibility of callable
    }

    /**
     * 调用该方法，将当前线程放到MyFutureTask的waitNodes map里。在netty client调用set是会从这个map
     * 中找到对于的线程唤醒。
     *
     * @author Wen, Changying
     * @date 2019年8月19日
     */
    public void putMySelf() {
        putMyWaitNode(Thread.currentThread().getId(), new MyWaitNode());
    }

    /**
     * 调用 map 的 putIfAbsent 来插入线程。因为线程id不会重复，所以暂时不清楚会有什么坏处。
     *
     * @param threadID
     * @param myWaitNode
     * @return
     * @author Wen, Changying
     * @date 2019年8月20日
     */
    private boolean putMyWaitNode(long threadID, MyWaitNode myWaitNode) {
        // 如果putIfAbsent返回值为null，说明对应的key原来已经有值，返回false
        if (null == waitNodes.putIfAbsent(threadID, myWaitNode)) return false;
        return true;
    }

    /**
     * 设置返回值，并调用 LockSupport.unpark() 唤醒所有因为调用 get() 而阻塞着的线程<br/>
     *
     * @param threadID 线程ID
     * @param v        返回值
     * @author Wen, Changying
     * @date 2019年8月15日
     */
    public void set(long threadID, V v) {
        // set outcome， 然后再set state
        MyWaitNode node = waitNodes.get(threadID);
        if (node == null) return;
        node.outcome = v;
        node.state = NORMAL;
        if (node.thread != null) {
            LockSupport.unpark(node.thread);
        }
    }

    /**
     * 获取netty的回写数据，数据没有回来时挂起等待，等待超时抛出异常
     *
     * @param timeout 超时时间
     * @param unit    超时时间单位
     * @return
     * @throws TimeoutException, Exception
     * @author Wen, Changying
     * @date 2019年8月19日
     */
    public V myGet(long timeout, TimeUnit unit) throws TimeoutException, Exception {
        if (unit == null)
            throw new NullPointerException();
        MyWaitNode node;
        long threadID = Thread.currentThread().getId();
        if ((node = waitNodes.get(threadID)) == null)
            throw new Exception("调用putMySelf设置 MyWaitNode 失败!");

        final long deadline = System.nanoTime() + unit.toNanos(timeout);
        for (; ; ) {
            // 如果状态大于COMPLETING，返回
            if (node.state > COMPLETING) {
                waitNodes.remove(threadID);
                return (V) node.outcome;
            }
            // 超时抛异常。
            if (deadline - System.nanoTime() <= 0L) {
                waitNodes.remove(threadID);
                throw new TimeoutException();
            }
            // 放弃CPU时间片，有意义吗？
            Thread.yield();
            // 等待
            LockSupport.parkNanos(this, unit.toNanos(timeout));
        }
    }

    /**
     * 获取netty的回写数据，数据没有回来时挂起等待。线程可能会永远沉睡下去，所以尽量使用有等待时间的另一个myGet
     *
     * @return
     * @throws Exception
     * @author Wen, Changying
     * @date 2019年8月19日
     */
    @Deprecated
    private V myGet() throws Exception {
        MyWaitNode node;

        long threadID = Thread.currentThread().getId();
        if ((node = waitNodes.get(threadID)) == null)
            throw new Exception("调用putMySelf设置 MyWaitNode 失败!");

        for (; ; ) {
            if (node.state > COMPLETING) {
                waitNodes.remove(threadID);
                return (V) node.outcome;
            }
            LockSupport.park(this);
        }
    }

    /**
     * FutureTask原有方法
     *
     * @throws CancellationException {@inheritDoc}
     */
    @Deprecated
    public V get() throws InterruptedException, ExecutionException {
        int s = state;
        if (s <= COMPLETING)
            s = awaitDone(false, 0L);
        return report(s);
    }

    /**
     * FutureTask原有方法。我注释掉了两行 removeWaiter(q);</br>
     * Awaits completion or aborts on interrupt or timeout.
     *
     * @param timed true if use timed waits
     * @param nanos time to wait, if timed
     * @return state upon completion
     */
    @Deprecated
    private int awaitDone(boolean timed, long nanos)
            throws InterruptedException {
        final long deadline = timed ? System.nanoTime() + nanos : 0L;
        WaitNode q = null;
        boolean queued = false;
        for (; ; ) {
            if (Thread.interrupted()) {
//                removeWaiter(q);
                throw new InterruptedException();
            }

            int s = state;
            if (s > COMPLETING) {
                if (q != null)
                    q.thread = null;
                return s;
            } else if (s == COMPLETING) // cannot time out yet
                Thread.yield();
            else if (q == null)
                q = new WaitNode();
            else if (!queued) {
                queued = UNSAFE.compareAndSwapObject(this, waitersOffset,
                        q.next = waiters, q);
                waiters = q;
            } else if (timed) {
                nanos = deadline - System.nanoTime();
                if (nanos <= 0L) {
//                    removeWaiter(q);
                    return state;
                }
                LockSupport.parkNanos(this, nanos);
            } else
                LockSupport.park(this);
        }
    }

    /**
     * FutureTask原有方法</br>
     * Returns result or throws exception for completed task.
     *
     * @param s completed state value
     */
    @SuppressWarnings("unchecked")
    @Deprecated
    private V report(int s) throws ExecutionException {
        Object x = outcome;
        if (s == NORMAL)
            return (V) x;
        if (s >= CANCELLED)
            throw new CancellationException();
        throw new ExecutionException((Throwable) x);
    }

    static final class MyWaitNode {
        volatile long ThreadID;
        volatile Thread thread;
        volatile Object outcome;
        volatile int state = NEW;

        MyWaitNode() {
            ThreadID = Thread.currentThread().getId();
            thread = Thread.currentThread();
        }
    }

    /**
     * FutureTask原有内部类
     *
     * @author Wen, Changying
     * 2019年8月15日
     */
    @Deprecated
    static final class WaitNode {
        volatile Thread thread;
        volatile WaitNode next;

        WaitNode() {
            thread = Thread.currentThread();
        }
    }
    // Unsafe mechanics
    /**
     * Unsafe机制
     */
    private static final sun.misc.Unsafe UNSAFE;
    private static final long stateOffset;
    private static final long runnerOffset;
    private static final long waitersOffset;

    static {
        try {
            //FutureTask原有方法，jdk1.9之前不可用
            //UNSAFE = sun.misc.Unsafe.getUnsafe();

            //使用反射的方式，强行使用Unsafe API。在jdk1.9之后开源了
            Field f = sun.misc.Unsafe.class.getDeclaredField("theUnsafe");
            f.setAccessible(true);
            UNSAFE = (sun.misc.Unsafe) f.get(null);


            Class<?> k = MyFutureTask.class;
            stateOffset = UNSAFE.objectFieldOffset
                    (k.getDeclaredField("state"));
            runnerOffset = UNSAFE.objectFieldOffset
                    (k.getDeclaredField("runner"));
            waitersOffset = UNSAFE.objectFieldOffset
                    (k.getDeclaredField("waiters"));
        } catch (Exception e) {
            throw new Error(e);
        }
    }

}

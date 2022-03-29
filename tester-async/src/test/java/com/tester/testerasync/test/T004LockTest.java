package com.tester.testerasync.test;

import org.junit.Test;

import java.util.concurrent.*;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class T004LockTest {

    ReentrantLock lock = new ReentrantLock();

    ReentrantReadWriteLock rwlock = new ReentrantReadWriteLock();


    @Test
    public void test_reentrantLock(){
        boolean b = lock.tryLock();

        lock.lock();
        // ReentrantLock 默认为非公平锁
        // tryLock做了什么？
        //  1. 如果没有锁（state = 0），设置state标志锁已经被获取，并设置线程为当前线程
        //  2. 如果锁已被获取（state != 0），检查获取持有锁的线程，判断是否是当前线程
        //    2.1 如果是，state+1，获取锁成功（如果state+1 > int.maxvalue 抛异常）
        //    2.2 如果否，获取锁失败

        lock.unlock();
    }

    @Test
    public void test_rwlock(){
        ReentrantReadWriteLock.ReadLock readLock = rwlock.readLock();
        readLock.lock();

        ReentrantReadWriteLock.WriteLock writeLock = rwlock.writeLock();
        writeLock.lock();

    }


    /**
     * 不同线程的同步控制。
     * <br/>
     *
     * org.apache.rocketmq.common.CountDownLatch2.reset()
     * @param  
     * @return void 
     * @Date 17:56 2021/5/28
     * @Author 温昌营 
     **/
    @Test
    public void test_countDownLatch() throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(3);
        for (int i = 0; i < 6; i++) {
            int finalI = i;
            new Thread(() -> {
                try {
                    Thread.sleep(finalI *20);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(Thread.currentThread().getName()+": out");
                countDownLatch.countDown();
            },String.valueOf(i)).start();
        }
        countDownLatch.await();
    }


    /**
     * 栅栏。等待所有线程进入相同位置再往下走
     * @param
     * @return void
     * @Date 9:36 2021/5/28
     * @Author 温昌营
     **/
    @Test
    public void test_cyclicBarrier() throws Exception{
        CyclicBarrier cyclicBarrier = new CyclicBarrier(6,()-> System.out.println("all done"));
        for (int i = 0; i < 6; i++) {
            int finalI = i+1;
            new Thread(() -> {
                try {
                    Thread.sleep(finalI *20);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(Thread.currentThread().getName()+": out");
                try {
                    cyclicBarrier.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (BrokenBarrierException e) {
                    e.printStackTrace();
                }
            },String.valueOf(i)).start();
        }
        Thread.sleep(4000L);
    }

    /**
     * 信号量。信号量消耗完后就停止。可以用来控制并发数量（rocketMQ）
     * org.apache.rocketmq.remoting.netty.NettyRemotingAbstract#invokeOnewayImpl(io.netty.channel.Channel, org.apache.rocketmq.remoting.protocol.RemotingCommand, long)
     * @param
     * @return void
     * @Date 9:35 2021/5/28
     * @Author 温昌营
     **/
    @Test
    public void test_semaphore() throws Exception{
        Semaphore semaphore = new Semaphore(3);
        for (int i = 0; i < 6; i++) {
            new Thread(() -> {
                try {
                    semaphore.acquire();
                    System.out.println(Thread.currentThread().getName()+": in");
                    TimeUnit.SECONDS.sleep(10);
                    System.out.println(Thread.currentThread().getName()+": out");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }finally {
                    semaphore.release();
                }
            },String.valueOf(i)).start();
        }
        Thread.sleep(40000L);
    }
}

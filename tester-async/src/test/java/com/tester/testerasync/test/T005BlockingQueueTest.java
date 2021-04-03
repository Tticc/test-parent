package com.tester.testerasync.test;

import org.junit.Test;

import java.util.concurrent.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.LockSupport;
import java.util.concurrent.locks.ReentrantLock;

public class T005BlockingQueueTest {

    ReentrantLock lock = new ReentrantLock();
    Condition condition = lock.newCondition();


    @Test
    public void test_park() throws InterruptedException {
        Thread thread_start = new Thread(() -> {
            boolean b = lock.tryLock();
            System.out.println("lock status:"+b);
            System.out.println("thread start");
            System.out.println("i will park myself");
            try {
                Thread.sleep(3000);
                boolean b1 = lock.tryLock();
                condition.await();
                boolean locked = lock.isLocked();
                System.out.println("islocked:"+locked);
            } catch (InterruptedException e) {
                System.out.println("interruptedExcepion");
            }
            LockSupport.park(Thread.currentThread());
            System.out.println("continue");
            lock.unlock();
            lock.unlock();
        });
        thread_start.start();
        Thread.sleep(1000);
        boolean b = lock.tryLock(5, TimeUnit.SECONDS);
        System.out.println("main lock status:"+b);
        Thread.sleep(1000);
        condition.signal();
        lock.unlock();
        Thread.sleep(1000);
        LockSupport.unpark(thread_start);
        System.out.println("main: unparked");
    }

    @Test
    public void test_queue() throws InterruptedException {
        BlockingQueue<Integer> blockingQueue = new ArrayBlockingQueue<>(3);
//        System.out.println(blockingQueue.add(4));
//        System.out.println(blockingQueue.add(2));
//        System.out.println(blockingQueue.add(3));
//        System.out.println(blockingQueue.element());
//        System.out.println(blockingQueue.remove());
//        System.out.println(blockingQueue.remove());
//        System.out.println(blockingQueue.remove());
//        System.out.println(blockingQueue.remove());

//        System.out.println(blockingQueue.offer(4));
//        System.out.println(blockingQueue.offer(2));
//        System.out.println(blockingQueue.offer(3));
//        System.out.println(blockingQueue.peek());
//        System.out.println(blockingQueue.poll());
//        System.out.println(blockingQueue.poll());
//        System.out.println(blockingQueue.poll());
//        System.out.println(blockingQueue.poll());


        blockingQueue.put(4);
        blockingQueue.put(2);
        blockingQueue.put(3);

        Object obj = new Object();
        obj.wait();
        BlockingQueue<Integer> objects = new LinkedBlockingDeque<>();
        BlockingQueue<Integer> objects1 = new SynchronousQueue<>();
    }

}

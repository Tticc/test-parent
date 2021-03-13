package com.tester.testerasync;

import org.junit.Test;

import java.util.concurrent.*;

public class NormalTest_Async {

    private ExecutorService threadPool = Executors.newFixedThreadPool(1);


    @Test
    public void test_break(){
        int count = 0;
        int countj = 0;
        aa:
        for (int i=0;i<100;i++){
            ++countj;
            System.out.println("i is:"+i);
            for(int j=0;j<100;j++) {
                System.out.println(j);
                if(j == 10){
                    continue aa;
                }
                ++count;
                if (count % 1000000000 == 0) {
                    System.out.println("xxx:" + count);
                    break aa;
                }
            }
        }
    }

    @Test
    public void test_thread() throws ExecutionException, InterruptedException {
        threadPool.execute(() -> System.out.println("xx"));
        Future<Integer> submit = threadPool.submit(() -> {
            System.out.println("threadId is:"+Thread.currentThread().getId());
            Thread.sleep(5000);
            return 1;
        });
        System.out.println("threadId is:"+Thread.currentThread().getId());
        submit.get();
    }

    @Test
    public void test_queue() throws InterruptedException {

        BlockingQueue<Integer> blockingQueue = new ArrayBlockingQueue<>(Integer.MAX_VALUE);
        blockingQueue.put(1);
    }


    @Test
    public void test_interrupted() throws InterruptedException {
        Thread xxx = new Thread(() -> {
            int count = 0;
            for(;;) {
                ++count;
                if(count % 1000 == 0 && !Thread.currentThread().isInterrupted()){
                    System.out.println("xxx");
//                    Thread.currentThread().interrupt();
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }
        });
        xxx.start();
        Thread.sleep(1000);
        System.out.println(xxx.isInterrupted());
        xxx.interrupt();
        System.out.println(xxx.isInterrupted());
        System.out.println(xxx.interrupted());
        System.out.println(xxx.isInterrupted());
    }

}

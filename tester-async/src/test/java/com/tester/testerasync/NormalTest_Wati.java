package com.tester.testerasync;

import com.tester.testercommon.util.DateUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

@Slf4j
@Data
public class NormalTest_Wati {

    private boolean isDone = false;

    @Test
    public void test_identity() throws InterruptedException {
        boolean javaIdentifierPart = Character.isJavaIdentifierPart(9990);
        System.out.println("javaIdentifierPart = " + javaIdentifierPart);

        /**
         * 1. 关键字(50个)
         * abstract   continue   for          new         switch
         * assert     default    if           package     synchronized
         * boolean    do         goto         private     this
         * break      double     implements   protected   throw
         * byte       else       import       public      throws
         * case       enum       instanceof   return      transient
         * catch      extends    int          short       try
         * char       final      interface    static      void
         * class      finally    long         strictfp    volatile
         * const      float      native       super       while
         *
         * 2. 保留关键字：const、goto。虽然java不适用，但是避免在C++中出现问题，不允许使用作为变量名。
         *
         * 3. 非关键字：true、false、null。
         */

    }

    /**
     * 检查一个线程修改了非volatile变量后，其他线程是否能立刻取到数据。
     * 从结果上来看，是可以立刻读取到的。还是说休眠1ms还是太长了？
     * @throws InterruptedException
     */
    @Test
    public void test_sleep() throws InterruptedException {
        NormalTest_Wati normalTest_wati = new NormalTest_Wati();
        Thread t1 = new Thread(() -> {
            try {
                TimeUnit.MILLISECONDS.sleep(50);
                System.out.println("t1开始修改");
                normalTest_wati.setDone(true);
                int i = 0;
                while(++i < 10000000){
                    Thread.yield();
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println("t1结束");
        });
        Thread t2 = new Thread(() -> {
            while (!normalTest_wati.isDone()){
                try {
                    Thread.yield();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
            System.out.println("t2状态已完成，结束");
        });
        t1.start();
        t2.start();
        t1.join();
        t2.join();
    }
    @Test
    public void test_wait() throws InterruptedException {
        NormalTest_Wati normalTest_wati = new NormalTest_Wati();
        Thread wThread = new Thread(() -> waitMethod(normalTest_wati));
        Thread nThread = new Thread(() -> notifyMethod(normalTest_wati,wThread));
        wThread.start();
        nThread.start();
        wThread.join();
        nThread.join();
    }

    private void waitMethod(NormalTest_Wati normalTest_wati) {
        try {
            synchronized (normalTest_wati) {
                log.info("{}-waitMethod进入同步块成功，{}",Thread.currentThread().getId(),DateUtil.dateFormat(DateUtil.getNow()));
                log.info("{}-开始sleep 2s，{}",Thread.currentThread().getId(),DateUtil.dateFormat(DateUtil.getNow()));
                TimeUnit.SECONDS.sleep(2L);
                log.info("{}-开始wait，{}",Thread.currentThread().getId(),DateUtil.dateFormat(DateUtil.getNow()));
                normalTest_wati.wait();
                log.info("{}-wait结束，{}",Thread.currentThread().getId(),DateUtil.dateFormat(DateUtil.getNow()));
            }
        } catch (Exception e) {
            log.error("error:", e);
        }
    }

    private void notifyMethod(NormalTest_Wati normalTest_wati,Thread nThread) {
        try {
            TimeUnit.MILLISECONDS.sleep(300L);
//            nThread.interrupt();
            TimeUnit.MILLISECONDS.sleep(200L);
            synchronized (normalTest_wati) {
                log.info("{}-notifyMethod进入同步块成功，{}",Thread.currentThread().getId(),DateUtil.dateFormat(DateUtil.getNow()));
                log.info("{}-开始唤醒，{}",Thread.currentThread().getId(),DateUtil.dateFormat(DateUtil.getNow()));
                normalTest_wati.notify();
            }
        } catch (Exception e) {
            log.error("error:", e);
        }
    }




}


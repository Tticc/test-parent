package com.tester.testerasync;

import lombok.Data;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

@Data
public class NormalTest_Volatile {

    private int A = 0;
    private int B = 0;

//    private int r1 = 0, r2 = 0;

    /**
     * 测试指令重排序
     */
    @Test
    public void test_change() throws InterruptedException {
        for (int i = 0; i < 50000; i++) {
            int k = i;
            NormalTest_Volatile normalTest_volatile = new NormalTest_Volatile();
            Thread a = new Thread(() -> normalTest_volatile.threadA(k));
            Thread b = new Thread(() -> normalTest_volatile.threadB(k));
            a.start();
            b.start();
            a.join();
            b.join();
        }
        /**
         * 打印结果有时候包含：
         * ...
         * 5749	r1 = 1
         * 5750	r1 = 1
         * 5750	r2 = 2
         * 5751	r2 = 2
         * 5752	r1 = 1
         * ...
         * 这意味着当i=5750时，不再严格按照步骤1->步骤2   步骤3->步骤4 的顺序执行，而是重排序了
         */
    }

    public void threadA(int i) {
        try {
            TimeUnit.MILLISECONDS.sleep(5);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        // 步骤1
        int r2 = A;
        // 步骤2
        B = 1;
        if(r2 == 2){
            System.out.println(i+"\tr2 = 2");
        }
    }

    public void threadB(int i) {
        try {
            TimeUnit.MILLISECONDS.sleep(5);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        // 步骤3
        int r1 = B;
        // 步骤4
        A = 2;
        if(r1 == 1){
            System.out.println(i+"\tr1 = 1");
        }
    }


}


package com.tester.testerasync.test;

import org.junit.Test;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class T005BlockingQueueTest {

    @Test
    public void test_queue(){
        BlockingQueue<Integer> blockingQueue = new ArrayBlockingQueue<>(Integer.MAX_VALUE);
    }

}

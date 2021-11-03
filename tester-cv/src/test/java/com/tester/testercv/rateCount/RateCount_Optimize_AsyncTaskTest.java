package com.tester.testercv.rateCount;

import com.tester.testercommon.util.MySupplier;

import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class RateCount_Optimize_AsyncTaskTest {
    private static ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(8,8,60,TimeUnit.SECONDS,new LinkedBlockingQueue<>(3000));


    public static <T> Future<T> doAsync(MySupplier<T> mySupplier){
        Future<T> submit = threadPoolExecutor.submit(() -> mySupplier.get());
        return submit;
    }


}

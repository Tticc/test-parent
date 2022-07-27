package com.tester.testernormaltest;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.util.StopWatch;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * @Author 温昌营
 * @Date 2022-7-1 14:37:43
 */
@Slf4j
public class NormalTest_test {
    public static void main(String[] args) throws IOException {

    }


    @Test
    public void test_watch() throws InterruptedException {
        StopWatch stopWatch = new StopWatch("test StopWatch");
        stopWatch.start("时间消耗1");
        TimeUnit.SECONDS.sleep(1);
        stopWatch.stop();
        stopWatch.start("时间消耗2");
        TimeUnit.SECONDS.sleep(2);
        stopWatch.stop();
        stopWatch.start("时间消耗3");
        TimeUnit.SECONDS.sleep(3);
        stopWatch.stop();
//        log.info("【StopWatch 测试】:详情 {}; 总耗时:{} ", stopWatch.prettyPrint(), stopWatch.getTotalTimeMillis());
        log.info("\n{}", stopWatch.prettyPrint());
    }

}

package com.tester.testerfuncprogram;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * ReactorTest的补充
 * @Date 18:00 2021/9/22
 * @Author 温昌营
 **/
@Slf4j
public class ReactorQueueTest {


    private static final BlockingQueue<String> queue = new LinkedBlockingQueue<>(20);

    @Test
    public void test_monoCreate_create1() throws InterruptedException {
        Thread thread = new Thread(() -> putEle());
        thread.start();

        Mono<String> objectMono = Mono.create(sink ->
                {
                    String str = null;
                    try {
                        str = queue.take();
                    } catch (InterruptedException e) {
                        log.error("take error.",e);
                    }
                    System.out.println(str);
                    sink.success(str);
                }
        ).map(e -> "now the ele is "+e.toString());
        while (true){

            String block = objectMono.block(Duration.ofSeconds(4));
            System.out.println("block = " + block);
        }

    }


    private void putEle() {
        for (int i = 0; i < 14; i++) {
            try {
                Thread.sleep(2000);
                queue.put(""+i);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}

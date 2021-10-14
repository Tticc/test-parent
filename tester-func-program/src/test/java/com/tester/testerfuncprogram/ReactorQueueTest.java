package com.tester.testerfuncprogram;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import reactor.core.publisher.Flux;
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


    /**
     * 这里，不管queue会不停的放入多少数据，flux只subscribe一次，所以create里的lambda里的逻辑只执行一次<br/>
     * 这就意味着只会从queue里take一次，故而仅取到了一个元素。<br/>
     * 需要变更为create的lambda定义并启动一个线程，线程里面循环take并发送<br/>
     * <br/>
     *
     * @see ReactorQueueTest#test_flux_thread()
     * @Date 11:43 2021/9/28
     * @Author 温昌营
     **/
    @Test
    public void test_flux() throws InterruptedException {
        putEle();
        Flux.create(sink -> {
            String str = null;
            try {
                str = queue.take();
            } catch (InterruptedException e) {
                log.error("take error.", e);
            }
            System.out.println("the ele to send: "+str);
            sink.next(str);
        })
                .map(e -> "now the ele is " + e.toString())
                .then()
                .subscribe(e -> System.out.println("e = " + e));
        Thread.sleep(3000L);
    }


    @Test
    public void test_flux_thread() throws InterruptedException {
        putEle();
        Flux.create(sink -> {
            new Thread(() -> {
                while (true) {
                    String str = null;
                    try {
                        str = queue.take();
                    } catch (InterruptedException e) {
                        log.error("take error.", e);
                    }
                    System.out.println("the ele to send: " + str);
                    sink.next(str);
                }
            }).start();
        })
                .map(e -> "now the ele is " + e.toString())
                .then()
                .subscribe(e -> System.out.println("e = " + e));
        Thread.sleep(19000L);
    }


    @Test
    public void test_monoCreate_create1() throws InterruptedException {
        putEle();

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
        Thread thread = new Thread(() -> {
            int count = 0;
            do{
                try {
                    Thread.sleep(2000);
                    queue.put(""+ (++count));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }while (true);
        });
        thread.setDaemon(true);
        thread.start();
    }

}

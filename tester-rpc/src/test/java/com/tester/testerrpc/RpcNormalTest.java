package com.tester.testerrpc;

import com.tester.testercommon.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @Author 温昌营
 * @Date 2021-9-24 11:19:35
 */
@Slf4j
public class RpcNormalTest {


    @Test
    public void test_DiscardServer() throws Exception{
        int port = 8080;
        new DiscardServer(port).run();
    }

    @Test
    public void test_flux_flatMap(){
        Disposable subscribe = Flux.just("one", "twi")
                .map(e -> e.split(""))
                .flatMap(e -> Mono.just(e))
                .subscribe(e-> System.out.println(Arrays.asList(e)));
    }

    @Test
    public void test_stream_flatMap(){
        List<String> collect = Stream.of("one", "twi")
                .map(e -> e.split(""))
                .flatMap(Arrays::stream)
                .collect(Collectors.toList());
        System.out.println(collect);
    }

    @Test
    public void test_err(){
        Flux.fromArray(new Integer[]{1,2,3,4,5,6})
                .map(e -> {
                    if(e == 2){
                        return 8;
                    }
                    return e;
                })
                .map(e -> {
                    if(e == 6){
                        throw new RuntimeException();
                    }
                    return e;
                })
                .switchIfEmpty(Flux.error(new BusinessException(1)))
                .switchMap(e -> {
                    if(e == 8){
                        return Flux.empty();
                    }
                    return Flux.just(e);
                })
                .doOnError(e ->log.error("ex1 = ",e))
                .subscribe(e -> System.out.println("e = " + e), error -> System.out.println(error));
    }

    @Test
    public void test_next(){
        Object block = Flux.fromArray(new Integer[]{1, 2, 3, 4})
                .flatMap(e -> {
                    if (e == 3) {
                        return Mono.just(e);
                    }
                    return Mono.empty();
                })
                .next()
                .switchIfEmpty(createNotFoundError())
//                .doOnSuccess(System.out::println)
                // 异常处理
                .doOnError(e -> log.error("error thrown.",e))

                // 将原来的流废弃，重新建立一个新的流
                .then(Mono.just(-1))
                .block();
        System.out.println(block);
    }

    private Mono<? extends Integer> createNotFoundError() {
        return Mono.defer(() -> {
            Exception ex = new ResponseStatusException(HttpStatus.NOT_FOUND, "No matching handler");
            return Mono.error(ex);
        });
    }


    // 并没有看到Flux.create和Flux.push的区别在哪里
    @Test
    public void test_create(){
        Flux.create(fluxSink -> {
            for (int i = 0; i < 3; i++) {
                new Thread(() -> {
                    for (int j = 0; j < 5; j++) {
                        fluxSink.next("element "+j+" from tid: "+Thread.currentThread().getId());
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        fluxSink.next("element!!!!!!!!!! "+j+" from tid: "+Thread.currentThread().getId());
                    }
                }).start();
            }
        }).subscribe(e -> {
            System.out.println("print tid:"+Thread.currentThread().getId()+", value: "+e);
        });
        try {
            Thread.sleep(9000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test_push(){
        Flux.push(fluxSink -> {
            for (int i = 0; i < 3; i++) {
                new Thread(() -> {
                    for (int j = 0; j < 5; j++) {
                        fluxSink.next("element "+j+" from tid: "+Thread.currentThread().getId());
                    }
                }).start();
            }
        }).subscribe(e -> {
            System.out.println("print tid:"+Thread.currentThread().getId()+", value: "+e);
        });
        try {
            Thread.sleep(3000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


}

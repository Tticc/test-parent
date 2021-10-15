package com.tester.testerrpc;

import com.tester.testercommon.exception.BusinessException;
import com.tester.testercommon.model.request.MemberRequest;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.*;
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


    /**
     * 对象 -> obj输出流<br/>
     * obj输出流 -> byte数组<br/>
     * byte数组 -> piped输出流<br/>
     * piped输出流 -> piped输入流<br/>
     * piped输入流 -> byte数组<br/>
     * byte数组 -> obj输入流<br/>
     * obj输入流 -> 对象<br/>
     * @Date 17:11 2021/10/14
     * @Author 温昌营
     **/
    @Test
    public void test_out2in() throws Exception {
        // 创建对象
        MemberRequest memberRequest = new MemberRequest();
        memberRequest.setMemberId(12382948L);

        // 创建byte输出流
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        // 基于byte输出流，创建obj输出流
        ObjectOutputStream o = new ObjectOutputStream(byteArrayOutputStream);
        // 通过obj输出流，将object输出到byte输出流
        o.writeObject(memberRequest);
        o.flush();

        // 创建piped输入流
        PipedInputStream in = new PipedInputStream();
        // 基于piped输入流，创建piped输出流
        PipedOutputStream out = new PipedOutputStream(in);

        // 将byte输出流内容输出到piped输出流。这是就会将byte输出流输到 piped输入流的缓存中
        out.write(byteArrayOutputStream.toByteArray());
        out.flush();

        // 读取piped输入流到byte数组
        byte[] output = new byte[1024];
        in.read(output);

        // byte数组转换为obj输入流
        ByteArrayInputStream bais = new ByteArrayInputStream(output);
        ObjectInputStream ois = null;

        try {
            ois = new ObjectInputStream(bais);
            // obj输入流 读取对象
            Object o1 = ois.readObject();
            System.out.println("o1 = " + o1);
        }finally {
            if (ois != null) {
                ois.close();
            } else {
                bais.close();
            }
        }
    }

    /**
     * 对象 -> obj输出流<br/>
     * obj输出流 -> byte数组<br/>
     * byte数组 -> obj输入流<br/>
     * obj输入流 -> 对象<br/>
     * @Date 17:10 2021/10/14
     * @Author 温昌营
     **/
    @Test
    public void test_objInputStream() throws Exception {

        // 创建对象
        MemberRequest memberRequest = new MemberRequest();
        memberRequest.setMemberId(12382948L);

        // 创建byte输出流
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        // 基于byte输出流，创建obj输出流
        ObjectOutputStream oos = new ObjectOutputStream(byteArrayOutputStream);
        // 通过obj输出流，将object输出到byte输出流
        oos.writeObject(memberRequest);
//        oos.flush();

        byte[] bytes = byteArrayOutputStream.toByteArray();

        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
        ObjectInputStream ois = new ObjectInputStream(byteArrayInputStream);
        Object o = ois.readObject();
        System.out.println("o = " + o);
    }

    @Test
    public void test_DiscardServer() throws Exception{
        int port = 8080;
//        new DiscardServer(port).run();
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

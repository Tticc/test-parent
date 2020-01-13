package com.tester.testerfuncprogram;

import com.tester.testerfuncprogram.interfaces.AddAllFunction;
import org.junit.Test;
import org.reactivestreams.Subscription;
import org.springframework.util.StopWatch;
import reactor.core.Disposable;
import reactor.core.publisher.*;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.in;

/**
 * <a href = "https://projectreactor.io/docs/core/release/reference/#core-features">reactor url</a>
 * <ol>
 *     <li>Publisher - Flux</li>
 *     <li>Subscriber - LambdaSubscriber</li>
 *     <li>Subscription - </li>
 * </ol>
 * @author 温昌营
 * @date 2020/1/3
 */
public class ReactorTest {

    @Test
    public void test_getCPUProcessNumber(){
        System.out.println(Runtime.getRuntime().availableProcessors());
    }
    @Test
    public void test_Parallel() throws InterruptedException {
        Scheduler s = Schedulers.newParallel("parallel-scheduler", 4);

        final Flux<String> flux = Flux
                .range(1, 7)
                .map(i -> 10 + i)
                .publishOn(s)
                .map(i -> "value " + i);

        new Thread(() -> flux.subscribe(e -> System.out.println("tid:"+Thread.currentThread().getId()+", e:"+e))).start();
        Thread.currentThread().join(1000);
    }
    @Test
    public void test_publish_subscribe_On() throws InterruptedException {
        System.out.println("parent tid:"+Thread.currentThread().getId());
        CountDownLatch countDownLatch = new CountDownLatch(2);
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        Scheduler parallel = Schedulers.parallel();
        Flux.range(1,3)
                .limitRate(2)
                .publishOn(parallel)
                .map(e->{
                    try {
                        System.out.println("tid:"+Thread.currentThread().getId());
                        Thread.sleep(2000);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                    return e;
                })
                .publishOn(parallel)
                .map(e->{
                    try {
                        System.out.println("tid:"+Thread.currentThread().getId());
                        Thread.sleep(2000);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                    return e;
                })
                .subscribe(System.out::println,null,()->countDownLatch.countDown());
        countDownLatch.countDown();
        countDownLatch.await();
        stopWatch.stop();
        System.out.println("total time:"+stopWatch.getTotalTimeMillis());
    }

    @Test
    public void test_fork_join2() throws InterruptedException {
        System.out.println("parent tid:"+Thread.currentThread().getId());
        CountDownLatch countDownLatch = new CountDownLatch(4);
        Scheduler parallel = Schedulers.parallel();
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        Flux.range(1, 4)
                .parallel()
                .runOn(parallel)
//                .publishOn(parallel)

                .map(e -> {
                    try {
                        System.out.println("map1 tid:" + Thread.currentThread().getId());
                        Thread.sleep(2000);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                    return e;
                })
                .map(e -> {
                    try {
                        System.out.println("map2 tid:" + Thread.currentThread().getId());
                        Thread.sleep(2000);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                    return e;
                })
                .subscribe(e -> System.out.println("result tid:"+Thread.currentThread().getId()+", e:"+e),
                null,
                ()->countDownLatch.countDown());
//                .subscribe(getMySubscriber(countDownLatch));
//        sequential.subscribe(getMySubscriber(countDownLatch));
        countDownLatch.await();
        stopWatch.stop();
//        Thread.sleep(5000);
        System.out.println("total time:"+stopWatch.getTotalTimeMillis());
    }

    @Test
    public void test_fork_join1() throws InterruptedException {
        System.out.println("parent tid:"+Thread.currentThread().getId());
        CountDownLatch countDownLatch = new CountDownLatch(1);
        Scheduler parallel = Schedulers.parallel();
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        Mono<List<Integer>> sequential = Flux.range(1, 4)
                .parallel()
                .runOn(parallel)
//                .publishOn(parallel)
                .map(e -> {
                    try {
                        System.out.println("map1 tid:" + Thread.currentThread().getId());
                        Thread.sleep(2000);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                    return e;
                })
                .map(e -> {
                    try {
                        System.out.println("map2 tid:" + Thread.currentThread().getId());
                        Thread.sleep(2000);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                    return e;
                })
                .sequential()
                .collectList();
        sequential.subscribe(e -> System.out.println("result tid:"+Thread.currentThread().getId()+", e:"+e),
                        null,
                        ()->countDownLatch.countDown());
//        sequential.subscribe(getMySubscriber(countDownLatch));
//        countDownLatch.await();
        stopWatch.stop();
//        Thread.sleep(5000);
        System.out.println("total time:"+stopWatch.getTotalTimeMillis());
    }

    private <T> BaseSubscriber<T> getMySubscriber(CountDownLatch countDownLatch){
        BaseSubscriber<T> mysub = new BaseSubscriber<T>() {
            @Override
            public void hookOnSubscribe(Subscription subscription) {
                System.out.println("Subscribed");
//                request(1);
                requestUnbounded();
            }
            @Override
            public void hookOnNext(T value) {
                System.out.println("result tid:"+Thread.currentThread().getId()+", e:"+value);
                requestUnbounded();
//                request(1);
            }
            @Override
            public void hookOnComplete(){
                countDownLatch.countDown();
            }

        };
        return mysub;
    }

    @Test
    public void test_property_bufferSize(){
        int i = Integer.parseInt(System.getProperty("reactor.bufferSize.small", "256"));
        System.out.println(i);
    }

    @Test
    public void test_HotPublisher() {
        DirectProcessor<String> hotSource = DirectProcessor.create();
        Flux<String> hotFlux = hotSource;
        hotFlux.subscribe(d -> System.out.println("Subscriber 1 to Hot Source: " + d));
        hotSource.onNext("blue");
        hotSource.onNext("green");
        hotFlux.subscribe(d -> System.out.println("Subscriber 2 to Hot Source: " + d));
        hotSource.onNext("orange");
        hotSource.onNext("purple");
        hotSource.onComplete();
    }

    @Test
    public void test_Flux() {
        Flux.just(getIntList())
                .timeout(Duration.ofMillis(1))
                .take(3)
                .subscribe(System.out::println);
    }
    @Test
    public void test_FluxToMono(){
        Mono<Void> then = Flux.just(1, 2, 3, 4).then();
        // 怎么处理Mono
    }

    private List<Integer> getIntList() {
        List<Integer> list = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8));
        return list;
    }

    @Test
    public void test_limitRate(){
        Flux.generate(
                () -> 0,
                (state, sink) -> {
                    // sink.next时，会直接调用flux.subscribe注册的Subscriber的onNext方法，
                    // 也就是 System.out::println、真的吗
                    sink.next("3 x " + state + " = " + 3 * state);
                    if (state == 10) sink.complete();
                    return state + 1;
                },
                (state) -> {
                    // 最后最后会被调用。可以用来清理state，比如state是数据库连接时，可以在这里关闭
                    state = null;
                    System.out.println("finally, state is: "+state);
                })
                .doOnRequest((e) -> System.out.println("requested amount "+ e))
                .limitRate(2)
                .subscribe(e -> System.out.println("tid: " + Thread.currentThread().getId() + ", value: "+ e));
    }


    @Test
    public void test_fluxCreate_interval() throws InterruptedException {
        Thread t = new Thread(()->test_flux_newSingle());
        Thread t2 = new Thread(()->test_flux_newSingle());
        Thread t3 = new Thread(()->test_flux_newSingle());
        Thread t4 = new Thread(()->test_flux_newSingle());
        Thread t5 = new Thread(()->test_flux_newSingle());
        t.start();
//        t2.start();
//        t3.start();
//        t4.start();
//        t5.start();
//        test_flux_newSingle();
        Thread.currentThread().join(200);

    }
    private void test_flux_newSingle(){

        System.out.println("currentThreadId: "+Thread.currentThread().getId());
        // 默认就是 Schedulers.parallel()，数量为CPU核心数
        Flux<Long> interval = Flux.interval(Duration.ofMillis(2),Schedulers.single()/*,Schedulers.parallel()*/);
        interval.subscribe(e -> System.out.println("tid1: " + Thread.currentThread().getId() + ", value: "+ e));
        Flux<Long> interval1 = Flux.interval(Duration.ofMillis(2),Schedulers.single());
        interval1.subscribe(e -> System.out.println("tid2: " + Thread.currentThread().getId() + ", value: "+ e));
        Flux<Long> interval2 = Flux.interval(Duration.ofMillis(2),Schedulers.single());
        interval2.subscribe(e -> System.out.println("tid3: " + Thread.currentThread().getId() + ", value: "+ e));

    }

    @Test
    public void test_fluxCreate_just() throws InterruptedException {
        System.out.println("father threadId:"+Thread.currentThread().getId());
        Flux<String> seq1 = Flux.just("foo", "bar", "foobar");
        List<String> list = new ArrayList<>(Arrays.asList("foo1", "bar1", "foobar1"));
        Flux<String> seq2 = Flux.fromIterable(list);
        seq1.subscribeOn(Schedulers.parallel()).subscribe(e -> System.out.println("tid:"+Thread.currentThread().getId()+", e:"+e));
        Thread.currentThread().join(20);

    }

    /**
     * <ol>
     *     <li>subscribeOn - 往下到subscribe，往上到源头都受到这个subscribeOn语句影响。
     *     range的执行也会放到线程池中！！！</li>
     *     <li>publishOn - 影响部分流程，在publishOn往下的操作都受到这个subscribeOn语句影响，直到遇到下一个publishOn语句。
     *     无法影响到range！！！</li>
     *     <li>代码从下往上组装。所以先碰到subscribeOn，所有操作受到影响，会在parallel的某个线程中执行，
     *     接着遇到publishOn，publishOn语句以后的操作都受到publishOn的影响，会在parallel的另一个线程中执行</li>
     * </ol>
     * @author 温昌营
     * @date 2020/1/8
     */
    @Test
    public void test_subscribeOn_publishOn() throws InterruptedException{
        CountDownLatch countDownLatch = new CountDownLatch(1);
        StopWatch sw = new StopWatch();
        sw.start();
        System.out.println("parent tid:"+Thread.currentThread().getId());
        Scheduler s = Schedulers.newParallel("parallel-scheduler", 4);

//        s = Schedulers.single();
        Flux.range(1, 3)
                .doOnRequest((e) -> System.out.println("request amount: "+ e))
                .limitRate(2)
                .publishOn(s)
                .map(e ->
                {
                    if(e == null){
                        return null;
                    }
                    return e;}
                )
                .handle((BiConsumer<? super Integer, SynchronousSink<Integer>>)(e,sink) -> {
                    System.out.println("handle1 tid:"+Thread.currentThread().getId()+",e:"+e);
                    sink.next(e+10);
                })
                .handle((BiConsumer<? super Integer, SynchronousSink<Integer>>)(e,sink) -> {
                    System.out.println("handle2 tid:"+Thread.currentThread().getId()+",e:"+e);
//                    Integer a = (Integer)e;
                    sink.next(e+10);
                })
                .handle((e,sink) -> {
                    System.out.println("handle3 tid:"+Thread.currentThread().getId()+",e:"+e);
                    Integer a = (Integer)e;
                    // 最后一个handle返回的是String类型即可
                    sink.next("jid");
                })
                .publishOn(s)
                .doOnComplete(() -> countDownLatch.countDown())
                .subscribe(e->System.out.println("result tid:"+Thread.currentThread().getId()+",e:"+e));
        countDownLatch.await();
        sw.stop();
        System.out.println("time:"+sw.getTotalTimeMillis());
    }

    @Test
    public void test_watch_countDownLatch() throws InterruptedException {
        System.out.println("parent tid:"+Thread.currentThread().getId());
        CountDownLatch countDownLatch = new CountDownLatch(2);
        StopWatch sw = new StopWatch();
        sw.start();
        Flux.range(1,3)
                .handle((e, sink) -> {
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                    sink.next(e);
                })
                .subscribeOn(Schedulers.parallel())
                .doOnComplete(() -> countDownLatch.countDown())
                .subscribe(e -> System.out.println("result tid:"+Thread.currentThread().getId()+",e:"+e));
        Flux.range(31,3)
                .handle((e, sink) -> {
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                    sink.next(e);
                })
                .subscribeOn(Schedulers.parallel())
                .doOnComplete(() -> countDownLatch.countDown())
                .subscribe(e -> System.out.println("result tid:"+Thread.currentThread().getId()+",e:"+e));
        countDownLatch.await();
        sw.stop();
        System.out.println("total time:"+sw.getTotalTimeMillis());

    }

    @Test
    public void test_fluxCreate_generate(){
        Flux<String> flux = Flux.generate(
                () -> 0,
                (state, sink) -> {
                    // sink.next时，会直接调用flux.subscribe注册的Subscriber的onNext方法，
                    // 也就是 System.out::println、真的吗
                    sink.next("3 x " + state + " = " + 3*state);
                    if (state == 10) sink.complete();
                    return state + 1;
                },
                (state) -> {
                    // 最后最后会被调用。可以用来清理state，比如state是数据库连接时，可以在这里关闭
                    state = null;
                    System.out.println(state);
                });
        Flux<String> flux1 = Flux.generate(
                AtomicLong::new,
                (state, sink) -> {
                    long i = state.getAndIncrement();
                    sink.next("3 x " + i + " = " + 3*i);
                    if (i == 10) sink.complete();
                    return state;
                });
        flux.subscribe(System.out::println);
    }

    @Test
    public void test_fluxCreate_create(){
        List<ActionListener> listeners = new ArrayList<>();

        Flux.create(sink -> {
            listeners.add(evt -> sink.next(evt));
        })
                .subscribe(System.out::println);

        listeners.forEach(action -> action.actionPerformed(
                new ActionEvent(ReactorTest.class, 0, "shit happens"))
        );
    }


    @Test
    public void test_fluxToMonoToList(){
        Mono<List<Integer>> listMono = Flux.range(1, 7)
                .collectList();
        // process list within Mono
        listMono.subscribe(System.out::println);
        // get list from Mono
        List<Integer> block = listMono.block();
        System.out.println(block);
    }

    @Test
    public void test_handle() throws InterruptedException {
        System.out.println("parent tid:"+Thread.currentThread().getId());
        Flux<String> alphabet = Flux.just(-1, 30, 13, 9, 20)
                .handle((i, sink) -> {
                    String letter = alphabet(i);
                    if (letter != null) {
                        sink.next(i);
                    }
                }).handle((i, sink) -> {
                    String letter = alphabet((int)i);
                    if (letter != null) {
                        sink.next(letter);
                    }
                });

        alphabet.subscribeOn(Schedulers.single()).subscribe(e -> System.out.println("tid:"+Thread.currentThread().getId()+",e:"+e));
        Thread.currentThread().join(100);
    }
    private String alphabet(int letterNumber) {
        if (letterNumber < 1 || letterNumber > 26) {
            return null;
        }
        int letterIndexAscii = 'A' + letterNumber - 1;
        return "" + (char) letterIndexAscii;
    }




    @Test
    public void test_flux_subscriber() {
        Flux<String> seq1 = Flux.just("foo", "bar", "foobar", "end").map(e -> {
            if (Objects.equals("end", e)) {
                throw new RuntimeException("end it");
            }
            return e;
        });
//        seq1.subscribe(System.out::println, err -> System.out.println("error msg:" + err.getMessage()));
        Flux<Integer> ints = Flux.range(1, 40);
        ints.subscribe(i -> System.out.println(i),
                error -> System.err.println("Error " + error),
                () -> System.out.println("Done"),
                sub -> sub.request(10));
    }

    @Test
    public void test_mySubscriber(){
        BaseSubscriber<Integer> mysub = new BaseSubscriber<Integer>() {
            public void hookOnSubscribe(Subscription subscription) {
                System.out.println("Subscribed");
                request(1);
            }
            public void hookOnNext(Integer value) {
                System.out.println(value);
                request(1);
            }
        };
        Flux<Integer> ints = Flux.range(1, 4);
        // lambda subscribe
//        ints.subscribe(i -> System.out.println(i),
//                error -> System.err.println("Error " + error),
//                () -> {System.out.println("Done");},
//                s -> s.request(10));
        // custom subscribe
        ints.doOnRequest((l)->System.out.println("request amount:"+l))
//                .limitRequest(2)
                .take(2)
                .subscribe(mysub);
//        ints.subscribe(mysub);

    }

    @Test
    public void test_request_lambda(){
        Flux.range(1, 7)
                .doOnRequest(n -> System.out.println("request amount:"+n))
                .limitRequest(7)
                // limitRate是用来限制每次请求的数量的，与mySubscriber的request一样作用
                .limitRate(2)
                // subscribe 里的request是指定总共获取的数量，
                // 如果publisher发布的元素<=，那么所有的元素都会被处理，
                // 否则， 多余发布的元素会被丢弃。
//                .subscribe(System.out::println,(e)->{},()->{}, sub->sub.request(1));
                .subscribe(System.out::println,(e)->{},()->{});
    }


    @Test
    public void test_request_mySubscriber() {
        BaseSubscriber<Integer> mysub = new BaseSubscriber<Integer>() {
            public void hookOnSubscribe(Subscription subscription) {
                System.out.println("Subscribed");
                request(1);
//                requestUnbounded();
            }

            public void hookOnNext(Integer value) {
                System.out.println(value);
                request(1);
//                requestUnbounded();
            }
        };
        Flux.range(1, 7)
                .doOnRequest(n -> System.out.println("request amount:"+n))
                .limitRequest(4)
                .subscribe(mysub);
    }


    private void test_BaseSubscriber(){
//        BaseSubscriber
    }

    @Test
    public void test_stream() {
        Stream.of(1, 2, 3, 4, 5).map(e -> {
            if(e >=5 ){
                throw new RuntimeException();
            }
            return e;
        }).forEach(e -> System.out.println(e));
    }


    @Test
    public void test_monoCreate() {
        Mono<String> foo = Mono.just("foo");
        Mono<String> empty = Mono.empty();
        Flux<Integer> numbersFromFiveToSeven = Flux.range(0, 3);
        numbersFromFiveToSeven.subscribe(System.out::println);
    }


    @Test
    public void test_ids() {
        CompletableFuture<List<String>> ids = CompletableFuture.supplyAsync(() -> {
            return new ArrayList<>(Arrays.asList("NameJoe - 103", "b", "c", "d", "e"));
        });
        CompletableFuture<List<String>> result = ids.thenComposeAsync(l -> {
            Stream<CompletableFuture<String>> zip =
                    l.stream().map(i -> {
                        CompletableFuture<String> nameTask = ifhName(i);
                        CompletableFuture<Integer> statTask = ifhStat(i);
                        return nameTask.thenCombineAsync(statTask, (name, stat) -> "Name " + name + " has stats " + stat);
                    });
            List<CompletableFuture<String>> combinationList = zip.collect(Collectors.toList());
            CompletableFuture<String>[] combinationArray = combinationList.toArray(new CompletableFuture[combinationList.size()]);

            CompletableFuture<Void> allDone = CompletableFuture.allOf(combinationArray);
            return allDone.thenApply(v -> combinationList.stream()
                    .map(CompletableFuture::join)
                    .collect(Collectors.toList()));
        });
        List<String> results = result.join();
        System.out.println(results);
//        assertThat(results).contains(
//                "Name NameJoe has stats 103",
//                "Name NameBart has stats 104",
//                "Name NameHenry has stats 105",
//                "Name NameNicole has stats 106",
//                "Name NameABSLAJNFOAJNFOANFANSF has stats 121");
    }

    private CompletableFuture<Integer> ifhStat(String i) {
        return CompletableFuture.supplyAsync(() -> {
            if (i.contains("103")) {
                return 103;
            }
            return null;
        });
    }

    private CompletableFuture<String> ifhName(String i) {
        return CompletableFuture.supplyAsync(() -> {
            if (i.contains("NameJoe")) {
                return "NameJoe";
            }
            return null;
        });
    }
}

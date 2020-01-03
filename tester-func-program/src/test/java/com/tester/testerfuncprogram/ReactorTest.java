package com.tester.testerfuncprogram;

import org.junit.Test;
import reactor.core.publisher.DirectProcessor;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * <a href = "https://projectreactor.io/docs/core/release/reference/#core-features">reactor</a>
 *
 * @author 温昌营
 * @date 2020/1/3
 */
public class ReactorTest {

    @Test
    public void test_HotPublisher(){
        DirectProcessor<String> hotSource = DirectProcessor.create();
        Flux<String> hotFlux = hotSource;
        hotFlux.subscribe(d -> System.out.println("Subscriber 1 to Hot Source: "+d));
        hotSource.onNext("blue");
        hotSource.onNext("green");
        hotFlux.subscribe(d -> System.out.println("Subscriber 2 to Hot Source: "+d));
        hotSource.onNext("orange");
        hotSource.onNext("purple");
        hotSource.onComplete();
    }

    @Test
    public void test_Flux(){
        Flux.just(getIntList())
                .timeout(Duration.ofMillis(1))
                .take(3)
                .subscribe(System.out::println);
    }
    private List<Integer> getIntList(){
        List<Integer> list = new ArrayList<>(Arrays.asList(1,2,3,4,5,6,7,8));
        return list;
    }



    @Test
    public void test_ids(){
        CompletableFuture<List<String>> ids = CompletableFuture.supplyAsync(() -> {
            return new ArrayList<>(Arrays.asList("NameJoe - 103","b","c","d","e"));
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
            if(i.contains("103")){
                return 103;
            }
            return null;
        });
    }

    private CompletableFuture<String> ifhName(String i) {
        return CompletableFuture.supplyAsync(() -> {
            if(i.contains("NameJoe")){
                return "NameJoe";
            }
            return null;
        });
    }
}

package com.tester.testerasync;

import com.tester.testercommon.util.stream.ByteToInputStream;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class NormalTest_Scanner {

    @Test
    public void test_distinct(){

        int[] a = new int[]{1,2,3,4,5};
        int[] b = new int[]{1,2,3,4,5,6};

        int index = 0;
        for (int i = 0; i < 3; i++) {
            b[index++] = a[index];
        }
        for (int i = 0; i < b.length; i++) {
            System.out.println(b[i]);
        }

//        List<Integer> list = Arrays.asList(1, 2);
//        list.add(111);
//        Scanner sc = new Scanner(System.in);
//        int str = sc.nextInt();
//        int[] xx = new int[str];
        Integer[] vv = new Integer[]{10, 20,40, 32, 67, 40, 20, 89, 300, 400, 15};
        Arrays.sort(vv);

        List<Integer> integers = Arrays.asList(vv);
        System.out.println(integers);
    }

    @Test
    public void test_len(){
        String str = "hello nowcoder";
//        Scanner sc = new Scanner(System.in);
//        String str = sc.nextLine();
        int i = str.lastIndexOf(" ")+1;
        System.out.println(str.length() - i);
    }

    @Test
    public void test_count(){
        Scanner sc = new Scanner(System.in);
        String str = "ABCabc".toLowerCase();
        String s = "A".toLowerCase();
        System.out.print(str.length()-str.replaceAll(s,"").length());
    }

    @Test
    public void test_str(){
        String str = "c d a bb e";
        String[] s = str.split(" ");
        List<String> collect = Stream.of(s).sorted().collect(Collectors.toList());
        System.out.println(collect);

        Optional<String> reduce = Stream.of(s).sorted().reduce((a, b) -> a + "," + b);
        String s1 = reduce.get();
    }
    @Test
    public void test_scan(){
        Scanner sc = new Scanner(System.in);
        String s = sc.nextLine();
        String[] s1 = s.split(" ");
        int count = 0;
        for (String s2 : s1) {
            count+=Integer.valueOf(s2);
        }
        System.out.println(count);
        try(InputStream in = System.in) {
            InputStreamReader inputStreamReader = new InputStreamReader(in);
            byte[] bytes = ByteToInputStream.toByteArray(in);
            bytes.toString();
        }catch (IOException ex){

        }
    }


}

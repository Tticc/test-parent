package com.tester.testerwebapp;

import com.tester.testercommon.util.file.MyFileReader;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.*;
import java.util.stream.Collectors;

public class NormalTest {

    @Test
    public void test_equal(){
        int a = 125577;
        Integer b = new Integer(125577);
        System.out.println(b.equals(a));
    }

    @Test
    public void test_readTxt(){
        Set<String> ppSet = new HashSet<>(Arrays.asList(getPp()));
        System.out.println(ppSet.size());
        Set<String> pisSet = new HashSet<>(Arrays.asList(getPis()));
        System.out.println(pisSet.size());
        System.out.println(pisSet.contains("02000355"));
        List<String> collect = pisSet.stream().filter(e -> ppSet.contains(e)).collect(Collectors.toList());
        List<String> collect2 = pisSet.stream().filter(e -> !ppSet.contains(e)).collect(Collectors.toList());
        System.out.println("有效人员："+collect.size());
        System.out.println("无效人员："+collect2.size());
        for (String e:collect2) {
            System.out.println(e);
        }
    }

    /**
     * person in person
     * @return
     */
    private String[] getPp(){
        File file = new File("C:\\Users\\wenc\\Desktop\\ppa.txt");
        String[] text = MyFileReader.txt2String(file,";").split(";");
        System.out.println("pp.size:"+text.length);
        return text;
    }
    /**
     * person in station
     * @return
     */
    private String[] getPis(){
        File file = new File("C:\\Users\\wenc\\Desktop\\pisa.txt");
        String[] text = MyFileReader.txt2String(file,";").split(";");
        System.out.println("pis.size:"+text.length);
        return text;
    }

}

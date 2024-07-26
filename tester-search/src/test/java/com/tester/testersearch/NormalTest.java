package com.tester.testersearch;

import com.alibaba.fastjson.JSON;
import com.tester.testercommon.util.file.TxtWrite;
import com.tester.testersearch.model.Knowledge;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class NormalTest {
    @Test
    public void test() throws IOException {
        String s = TxtWrite.file2String("D:\\desktop\\test_knowledge_new.json");
        String[] split = s.split("\n");
        List<String> sList = new ArrayList<>();
        List<Knowledge> kList = new ArrayList<>();
        for (String s1 : split) {
            if(s1.startsWith("{\"index")){
                continue;
            }
            sList.add(s1);
//            System.out.println(s1);
            Knowledge knowledge = JSON.parseObject(s1, Knowledge.class);
            kList.add(knowledge);
        }
        System.out.println("kList.size() = " + kList.size());
    }
}

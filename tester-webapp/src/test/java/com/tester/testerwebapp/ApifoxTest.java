package com.tester.testerwebapp;

import com.alibaba.fastjson.JSON;
import com.tester.testercommon.util.file.TxtWrite;
import lombok.Data;
import lombok.experimental.Accessors;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author: wenc
 * @date: 2023/7/19 13:58
 */
public class ApifoxTest {


    /**
     * 生成apifox自动化测试的测试json数据
     *
     * @throws IOException
     */
    @Test
    public void test_generateApifoxTextFile() throws IOException {
        String tokenStr = TxtWrite.file2String(new File("D:\\desktop\\压测\\token.txt"));
        String[] split = tokenStr.split("\r\n");
        Set<String> collect = Stream.of(split)
                .limit(5)
                .collect(Collectors.toSet());
        List<TokenTestData> dataList = new ArrayList<>();
        for (String s : collect) {
            dataList.add(new TokenTestData().setToken(s));
        }
        String s = JSON.toJSONString(dataList);
        TxtWrite.string2File(s, "D:\\desktop\\压测\\sithk_token.json");
    }

    @Data
    @Accessors(chain = true)
    public static class TokenTestData {
        private String token;
    }
}

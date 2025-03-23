package com.tester.testersearch;

import com.alibaba.fastjson.JSON;
import com.tester.testercommon.util.file.TxtWrite;
import com.tester.testersearch.model.Knowledge;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class NormalTest {
    @Test
    public void test() throws IOException {
        Date date = new Date(1742037060000L);
        System.out.println("date = " + date);
    }
}

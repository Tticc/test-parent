package com.tester.testersearch;

import com.alibaba.fastjson.JSON;
import com.tester.testercommon.util.DateUtil;
import com.tester.testercommon.util.file.TxtWrite;
import com.tester.testersearch.model.Knowledge;
import org.junit.Test;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class NormalTest {
    @Test
    public void test() throws IOException {
        Date date = new Date(1743521995098L);
        System.out.println("date = " + date);

        LocalDateTime localDateTime = DateUtil.getLocalDateTime("20250101000000");
        Date dateFromLocalDateTime = DateUtil.getDateFromLocalDateTime(localDateTime);
        System.out.println(dateFromLocalDateTime.getTime());

    }
}

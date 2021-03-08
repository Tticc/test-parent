package com.tester.testercommon.utils;

import com.tester.testercommon.util.DateUtil;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * @Author 温昌营
 * @Date
 */
public class DateTest {

    @Test
    public void test_parseStr(){
        LocalDateTime localDateTime = DateUtil.getLocalDateTime("20210308101107");
        System.out.println(localDateTime);
        Date dateFromLocalDateTime = DateUtil.getDateFromLocalDateTime(localDateTime);
        System.out.println(dateFromLocalDateTime);
    }
}

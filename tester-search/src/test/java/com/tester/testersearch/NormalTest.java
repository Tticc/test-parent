package com.tester.testersearch;

import com.tester.testercommon.util.DateUtil;
import org.junit.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Objects;

public class NormalTest {

    public static void main(String[] args) {
        Long a = new Long("1");
        long b = 1L;
        System.out.println("(a==b = " + (a.equals(b)));

        BigDecimal bigDecimal = new BigDecimal("0.1");
        BigDecimal negate = bigDecimal.negate();
        System.out.println("b = " + negate);
    }

    @Test
    public void test() throws IOException {
        Date date = new Date(1743521995098L);
        System.out.println("date = " + date);

        LocalDateTime localDateTime = DateUtil.getLocalDateTime("20250101000000");
        Date dateFromLocalDateTime = DateUtil.getDateFromLocalDateTime(localDateTime);
        System.out.println(dateFromLocalDateTime.getTime());

    }
}

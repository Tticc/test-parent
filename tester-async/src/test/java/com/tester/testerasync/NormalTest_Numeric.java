package com.tester.testerasync;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

@Slf4j
@Data
public class NormalTest_Numeric {


    @Test
    public void test_Numeric() throws InterruptedException {
        float f1 = 0.000001f;
        float f2 = 1.000001f;
        System.out.println(f1);
        System.out.println(f2);
        System.out.println(f1+f2);

        double d1 = 0.000001d;
        double d2 = 1.000001d;
        System.out.println(d1);
        System.out.println(d2);
        System.out.println(d1+d2);


    }

    @Test
    public void test_NaN() {
        Boolean test = null;
        if (test!=null && test) { // null会直接报错，需要判断null
            System.out.println("test = " + test);
        }
        float naN = Float.NaN;
        System.out.println("naN = " + naN); // NaN
        float naN1 = 0.0f/0.0f;
        System.out.println("naN1 = " + naN1); // NaN
        float naN2 = 0/0.0f;
        System.out.println("naN2 = " + naN2); // NaN
        float f3 = 1.0f/0.0f;
        System.out.println("f3 = " + f3); // Infinity
        float f4 = 1.0f/-0.0f;
        System.out.println("f4 = " + f4); // -Infinity

        System.out.println(naN>naN); // false
        System.out.println(naN==naN); // false
        System.out.println(naN!=naN); // true
    }


}


package com.tester.testerasync;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;


/**
 * 1. 关键字(50个，含保留关键字)
 * abstract   continue   for          new         switch
 * assert     default    if           package     synchronized
 * boolean    do         goto         private     this
 * break      double     implements   protected   throw
 * byte       else       import       public      throws
 * case       enum       instanceof   return      transient
 * catch      extends    int          short       try
 * char       final      interface    static      void
 * class      finally    long         strictfp    volatile
 * const      float      native       super       while
 *
 * 2. 保留关键字：const、goto。虽然java不适用，但是避免在C++中出现问题，不允许使用作为变量名。
 *
 * 3. 非关键字：true、false、null。
 */
@Slf4j
@Data
public class NormalTest_IdentityAndKeyword {

    @Test
    public void test_identity() throws InterruptedException {
        char _c = '我';
        int codePointAt = Character.codePointAt(_c+"", 0);
        // 调用Character.isJavaIdentifierPart返回true的字符都可以用作变量名。
        // 可见，中文文字是可以作为变量名的。
        boolean javaIdentifierPart = Character.isJavaIdentifierPart(codePointAt);
        log.info("codePointAt:{}, javaIdentifierPart:{}, char_to_int:{}", codePointAt, javaIdentifierPart,(int)_c);

        int numbers = 1_000_000;
        System.out.println("numbers = " + numbers);

        int _2_binary = -0b0000_0011; // 以 0b 开头为二进制数
        int _8_octal = -00000_0011; // 以 0 开头为八进制数
        int _16_hexadecimal = -0x0000_0011; // 以 0x 开头为十六进制数
        System.out.println("_2_binary = " + _2_binary);
        System.out.println("_8_octal = " + _8_octal);
        System.out.println("_16_hexadecimal = " + _16_hexadecimal);


        int _2 = 0b1000_0000_0000_0000_0000_0000_0000_0000;
        System.out.println("_2 = " + _2);

        short _s = (short)0b1000_0000_0000_0000;
        System.out.println("_s = " + _s);



        char c = '\u2122';
        System.out.println("c = " + c);

        String s = System.lineSeparator();
        System.out.println("s = " + s);

    }

    @Test
    public void test_String() {
        String lo = "lo";
        String hello = "Hello";
        String hello1 = "Hel"+lo;
        String hello2 = "Hel"+"lo";
        String hello3 = ("Hel"+lo).intern();
        String hello4 = "Hel"+lo;
        System.out.println(hello == hello1); // false
        System.out.println(hello == hello2); // true
        System.out.println(hello == hello3); // true
        System.out.println(hello == hello4); // false
        System.out.println(hello1 == hello4); // false
    }


    @Test
    public void test_float() {
        float floatValue = 0.000001f;
        double doubleValue = 0.000001;

        System.out.println("Float value: " + floatValue);
        System.out.println("Double value: " + doubleValue);

        // 使用 Float 和 Double 类的方法来获取二进制表示
        String floatBinary = Integer.toBinaryString(Float.floatToIntBits(floatValue));
        String doubleBinary = Long.toBinaryString(Double.doubleToLongBits(doubleValue));

        System.out.println("Float binary: " + floatBinary);
        System.out.println("Double binary: " + doubleBinary);
    }

}


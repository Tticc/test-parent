package com.tester.testerrpc;

import com.tester.testercommon.util.UnsafeUtil;
import org.junit.Test;
import sun.misc.Unsafe;

import java.io.UnsupportedEncodingException;

/**
 * @Author 温昌营
 * @Date 2021-10-26 14:36:23
 */
public class NormalTest_Unsafe {
    static final Unsafe UNSAFE = UnsafeUtil.UNSAFE;

    @Test
    public void test_intMax(){
        int maxValue = Integer.MAX_VALUE;
        System.out.println("maxValue = " + maxValue);

        int left = 3;
        int right = 1;
        int res = left ^ right;
        System.out.println("res = " + res);

        int r = 2048 << 11;
        System.out.println("r = " + r);
    }

    @Test
    public void test_unsafe_byte() throws Exception{
        long size = 1;
        byte[] aByte = getByte();

        long addr1 = allocateMemory(size);
        System.out.println("addr1 = " + addr1);
//        UNSAFE.setMemory(addr1,size,(byte)0);
        for (int i = 0; i < aByte.length; i++) {
            UNSAFE.putByte(addr1+i,aByte[i]);
            UNSAFE.putInt(addr1,256);
        }

        byte aByte1 = UNSAFE.getByte(addr1);
        System.out.println("aByte1 = " + aByte1);
        int anInt = UNSAFE.getInt(addr1);
        System.out.println("anInt = " + anInt);
        System.out.println("anInt.reverseBytes = " + Integer.reverseBytes(anInt));
        freeMemery(addr1);

    }

    private byte[] getByte() throws UnsupportedEncodingException {
        String str = "开";
        byte[] bytes = str.getBytes("utf-8");
        System.out.println("bytes = " + bytes.length);
        return bytes;
    }

    /**
     * 申请一个size大小的内存，返回内存地址
     * @param size 内存大小
     * @return long 内存地址
     * @Date 13:44 2021/10/20
     * @Author 温昌营
     **/
    private long allocateMemory(long size){
        long addr = UNSAFE.allocateMemory(size);
        return addr;
    }

    /**
     * 释放内存
     * @param addr 需要释放的内存地址
     * @return void
     * @Date 13:44 2021/10/20
     * @Author 温昌营
     **/
    private void freeMemery(long addr){
        UNSAFE.freeMemory(addr);
    }


}

package com.tester.testercommon.util.stream;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Class 流字节数组互转工具类.
 * @Date 16:08 2021/3/9
 * @Author 温昌营
 **/

public class ByteToInputStream {

    /**
     * 字节数组转流
     * @Date 16:09 2021/3/9
     * @Author 温昌营
     **/
    public static final InputStream byte2Input(byte[] buf) {
        return new ByteArrayInputStream(buf);
    }

    /**
     * 流转字节数组
     * @Date 16:09 2021/3/9
     * @Author 温昌营
     **/
    public static byte[] toByteArray(InputStream input) throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024 * 4];
        int n = 0;
        while (-1 != (n = input.read(buffer))) {
            output.write(buffer, 0, n);
        }
        return output.toByteArray();
    }

}

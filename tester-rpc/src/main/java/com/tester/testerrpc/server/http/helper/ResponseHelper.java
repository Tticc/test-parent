package com.tester.testerrpc.server.http.helper;

import com.tester.testercommon.util.endecrypt.SHA1Encrypt;
import org.apache.commons.codec.binary.Base64;
import org.apache.coyote.http11.Constants;
import org.apache.tomcat.util.buf.HexUtils;

import java.util.Arrays;

/**
 * @Author 温昌营
 * @Date 2021-10-27 11:47:38
 */
public class ResponseHelper {

    public static final String SOCKET_KEY_SIGN_POSTFIX = "258EAFA5-E914-47DA-95CA-C5AB0DC85B11";


    private static final byte[] ENTER_BYTES = new byte[]{Constants.CR,Constants.LF};

    /**
     * 可用
     * @Date 11:56 2021/10/27
     * @Author 温昌营
     **/
    public static byte[] getHttpResponseHeader(){
        String str = "HTTP/1.1 200\n" +
                "Content-Type: text/html;charset=UTF-8\n" +
                "Transfer-Encoding: chunked\n" +
                "Date: Wed, 27 Oct 2021 01:38:35 GMT";
        byte[] bytes = str.getBytes();
        byte[] res = appendEnter(bytes,2);
        return res;
    }

    public static byte[] getHttpResponseBody(String str){
        // 内容bytes
        byte[] bytes = str.getBytes();
        // 内容长度行
        byte[] lengthLine = getLengthLine(bytes.length);
        // 长度+内容
        byte[] bytes1 = contactByteArray(lengthLine, bytes);
        // 长度+内容+换行符
        byte[] res = appendEnter(bytes1,1);
        return res;
    }

    public static byte[] getEndLine(){
        byte[] b = new byte[]{(byte) '0'};
        return appendEnter(b,2);
    }



    /**
     * @param key
     * @return byte[]
     * @Date 11:55 2021/10/27
     * @Author 温昌营
     **/
    public static byte[] getSocketResponseHeader(String key){
        String str = "HTTP/1.1 101 Switching Protocols\n" +
                "Upgrade: websocket\n" +
                "Connection: Upgrade\n" +
                "Sec-WebSocket-Accept: "+getResSign(key)+"\n" +
                "Sec-WebSocket-Protocol: chat";
        byte[] bytes = str.getBytes();
        byte[] res = appendEnter(bytes,1);
        return res;
    }

    public static String getResSign(String key){
        byte[] bytes = key.getBytes();
        String sha1 = SHA1Encrypt.getSha1(key + SOCKET_KEY_SIGN_POSTFIX);
        byte[] bytes1 = hexStr2Byte(sha1);
        String s = Base64.encodeBase64String(bytes1);
        return s;
    }

    public static byte[] hexStr2Byte(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }

    /**
     *
     * @Date 11:34 2021/10/27
     * @Author 温昌营
     * @see org.apache.coyote.http11.filters.ChunkedOutputFilter#calculateChunkHeader
     **/
    public static byte[] getLengthLine(int len){
        int maxLen = 10;
        byte[] chunkHeader = new byte[maxLen];
        chunkHeader[7] = (byte)0;
        chunkHeader[8] = Constants.CR;
        chunkHeader[9] = Constants.LF;
        int pos = 8;
        int current = len;
        while (current > 0) {
            int digit = current % 16;
            current = current / 16;
            chunkHeader[--pos] = HexUtils.getHex(digit);
        }
        byte[] res = new byte[maxLen-pos];
        for (int i = 0; i < res.length; i++) {
            res[i] = chunkHeader[pos+i];
        }
        return res;
    }

    /**
     * 追加换行符
     * @Date 13:54 2021/10/27
     * @Author 温昌营
     **/
    public static byte[] appendEnter(byte[] bytes, Integer times){
        assert times != null : "times should not null";
        assert times > 0 : "times should bigger then 0";
        byte[] res = bytes;
        for (int i = 0; i < times; i++) {
            res = contactByteArray(res, ENTER_BYTES);
        }
        return res;
    }

    /**
     * byte类型数组拼接
     * @Date 13:55 2021/10/27
     * @Author 温昌营
     **/
    public static  byte[] contactByteArray(byte[] first, byte[] second){
        byte[] res = Arrays.copyOf(first, first.length+second.length);
        System.arraycopy(second,0,res,first.length,second.length);
        return res;
    }

    /**
     * 数组拼接
     * @Date 13:55 2021/10/27
     * @Author 温昌营
     **/
    public static <T> T[] contactArray(T[] first, T[] second){
        T[] res = Arrays.copyOf(first, first.length+second.length);
        System.arraycopy(second,0,res,first.length,second.length);
        return res;
    }

}

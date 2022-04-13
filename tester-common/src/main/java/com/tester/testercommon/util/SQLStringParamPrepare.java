package com.tester.testercommon.util;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharsetEncoder;

/**
 * @Author 温昌营
 * @Date 2022-4-12 16:48:36
 */
public class SQLStringParamPrepare {

    public static void main(String[] args) {
        SQLStringParamPrepare stringParamPrepare = new SQLStringParamPrepare();
        String s = stringParamPrepare.setString("1';delete * from user;--");
        System.out.println("s = " + s);
    }

    private static CharsetEncoder charsetEncoder;

    /**
     * 转义特殊字符。<br/>
     * 默认使用 单引号 作为字符串起始符号
     *
     * @param x
     * @return java.lang.String
     * @Date 16:45 2022/4/12
     * @Author 温昌营
     * @see com.mysql.cj.ClientPreparedQueryBindings#setString(int, java.lang.String)
     **/
    public static String setString(String x) {
        if (x == null) {
            return x;
        } else {
            int stringLength = x.length();

            String parameterAsString = x;
            if (isEscapeNeededForString(x, stringLength)) {
                StringBuilder buf = new StringBuilder((int) (x.length() * 1.1));
                buf.append('\'');
                //
                // Note: buf.append(char) is _faster_ than appending in blocks, because the block append requires a System.arraycopy().... go figure...
                //

                for (int i = 0; i < stringLength; ++i) {
                    char c = x.charAt(i);

                    switch (c) {
                        case 0: /* Must be escaped for 'mysql' */
                            buf.append('\\');
                            buf.append('0');
                            break;
                        case '\n': /* Must be escaped for logs */
                            buf.append('\\');
                            buf.append('n');
                            break;
                        case '\r':
                            buf.append('\\');
                            buf.append('r');
                            break;
                        case '\\':
                            buf.append('\\');
                            buf.append('\\');
                            break;
                        case '\'':
                            buf.append('\\');
                            buf.append('\'');
                            break;
                        case '"':
                            // influxdb 使用单引号包字符串
                            buf.append('"');
                            break;
                        case '\032': /* This gives problems on Win32 */
                            buf.append('\\');
                            buf.append('Z');
                            break;
                        case '\u00a5':
                        case '\u20a9':
                            // escape characters interpreted as backslash by mysql
                            if (charsetEncoder != null) {
                                CharBuffer cbuf = CharBuffer.allocate(1);
                                ByteBuffer bbuf = ByteBuffer.allocate(1);
                                cbuf.put(c);
                                cbuf.position(0);
                                charsetEncoder.encode(cbuf, bbuf, true);
                                if (bbuf.get(0) == '\\') {
                                    buf.append('\\');
                                }
                            }
                            buf.append(c);
                            break;

                        default:
                            buf.append(c);
                    }
                }

                buf.append('\'');

                parameterAsString = buf.toString();
            }

            return parameterAsString;
        }
    }

    private static boolean isEscapeNeededForString(String x, int stringLength) {
        boolean needsHexEscape = false;

        for (int i = 0; i < stringLength; ++i) {
            char c = x.charAt(i);

            switch (c) {
                case 0: /* Must be escaped for 'mysql' */
                case '\n': /* Must be escaped for logs */
                case '\r':
                case '\\':
                case '\'':
                case '"': /* Better safe than sorry */
                case '\032': /* This gives problems on Win32 */
                    needsHexEscape = true;
                    break;
            }

            if (needsHexEscape) {
                break; // no need to scan more
            }
        }
        return needsHexEscape;
    }

}

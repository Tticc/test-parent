package com.tester.testerswing.capture.opencv;

import org.opencv.core.Core;

/**
 * 在这里加载 opencv dll
 * 其他工具类都应该在static代码块里调用 com.tester.testercv.utils.opencv.OpenCVBaseHelper#init() 方法
 * @Author 温昌营
 * @Date 2022-8-8 15:15:12
 */
public class OpenCVBaseHelper {

    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    public static void init() {
    }
}

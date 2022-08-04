package com.tester.testerswing.loadlibrary;

import org.springframework.util.ResourceUtils;

import java.io.File;

public class LibraryLoader {

    public static void loadLibrary(String libName) {
        try {
            String property = System.getProperty("java.library.path");
            System.out.println("property = " + property);
            // 直接使用系统加载方式
            System.loadLibrary(libName);
        } catch (Exception e) {
            System.err.println(" error! 加载dll失败");
            e.printStackTrace();
        }
    }

    /**
     * 未成功
     *
     * @Date 17:01 2022/8/4
     * @Author 温昌营
     **/
    public static void loadLibraryByAbsolutePath(String libName) {
        try {
            String property = System.getProperty("java.library.path");
            System.out.println("property = " + property);
            File dll = ResourceUtils.getFile("classpath:dll/" + libName + ".dll");
            System.load(dll.getAbsolutePath());
        } catch (Exception e) {
            System.err.println(" error! 加载dll失败");
            e.printStackTrace();
        }
    }
}

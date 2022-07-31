package com.tester.testerswing.loadlibrary;

import org.springframework.util.ResourceUtils;

import java.io.File;

public class LibraryLoader {

    public static void loadLibrary(String libName) {
        try {
            File dll = ResourceUtils.getFile("classpath:dll/" + libName + ".dll");
            System.load(dll.getAbsolutePath());
            dll.deleteOnExit();
        } catch (Exception e) {
            System.err.println(" error! 加载dll失败");
            e.printStackTrace();
        }
    }
}

package com.tester.testermytomcat.server;


import java.io.File;

/**
 * 常量
 */
public class Constants {

    /**
     * 资源目录
     */
    public static final String WEB_ROOT = System.getProperty("user.dir")+ File.separator+"webroot";

    /**
     * 关闭命令
     */
    public static final String SHUTDOWN_COMMAND = "/SHUTDOWN";
    /**
     * 文件读取size
     */
    public static final int BUFFER_SIZE = 1024;
}

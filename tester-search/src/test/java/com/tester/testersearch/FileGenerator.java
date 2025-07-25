package com.tester.testersearch;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;

public class FileGenerator {

    public static void main(String[] args) {
        // 目标文件的大小 (1024KB = 1024 * 1024 bytes = 1048576 bytes)
        long targetSize = 1024 * 1024;

        // 创建文件
        File file = new File("generated_file.dat");

        // 使用随机数据生成文件
        try (FileOutputStream fos = new FileOutputStream(file)) {
            byte[] buffer = new byte[1024]; // 每次写入1024字节
            long writtenBytes = 0;

            // 写入数据直到文件大小为1024KB
            while (writtenBytes < targetSize) {
                // 填充 buffer 数组为随机字节
                new Random().nextBytes(buffer);

                // 写入数据到文件
                fos.write(buffer);

                // 增加已写入字节数
                writtenBytes += buffer.length;
            }

            System.out.println("File generated: " + file.getAbsolutePath());
            System.out.println("File size: " + file.length() + " bytes");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

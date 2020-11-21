package com.tester.testercommon.util.file;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class MyFileReader {

    public static String txt2String(File file) throws IOException {
        return txt2String(file,"");
    }

    public static String txt2String(File file, String separator) throws IOException {
        StringBuilder result = new StringBuilder();
        //构造一个BufferedReader类来读取文件)，并实时close。不在finally close是因为如果有多个stream，只有最后一个被close
        try(BufferedReader br = new BufferedReader(new java.io.FileReader(file))){
            String s = null;
            while((s = br.readLine())!=null){//使用readLine方法，一次读一行
                result.append(s.trim()).append(separator);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return result.toString();
    }

    public static byte[] readFileToByte(File file){
        try {
            int length = (int) file.length();
            byte[] data = new byte[length];
            new FileInputStream(file).read(data);
            return data;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}

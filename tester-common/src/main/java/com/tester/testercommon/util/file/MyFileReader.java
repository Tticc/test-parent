package com.tester.testercommon.util.file;

import java.io.BufferedReader;
import java.io.File;

public class MyFileReader {

    public static String txt2String(File file){
        return txt2String(file,"");
    }

    public static String txt2String(File file, String separator){
        StringBuilder result = new StringBuilder();
        try{
            BufferedReader br = new BufferedReader(new java.io.FileReader(file));//构造一个BufferedReader类来读取文件
            String s = null;
            while((s = br.readLine())!=null){//使用readLine方法，一次读一行
                result.append(s.trim()).append(separator);
            }
            br.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        return result.toString();
    }
}

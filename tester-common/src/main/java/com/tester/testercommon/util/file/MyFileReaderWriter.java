package com.tester.testercommon.util.file;

import java.io.*;

public class MyFileReaderWriter {

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

    public static byte[] file2Byte(File file){
        byte[] bytes = new byte[(int)file.length()];
//        try(FileInputStream buf = new FileInputStream(file)){
        try(BufferedInputStream buf = new BufferedInputStream(new FileInputStream(file))){
            buf.read(bytes);
        }catch(Exception e){
            e.printStackTrace();
        }
        return bytes;
    }

    public static void byte2File(byte[] data,File file){
        try(BufferedInputStream in = new BufferedInputStream(new ByteArrayInputStream(data))){
            try(BufferedOutputStream fos = new BufferedOutputStream(new FileOutputStream(file))){
                int len = 0;
                byte[] buf = new byte[1024];
                while((len = in.read(buf)) != -1){
                    fos.write(buf,0,len);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}

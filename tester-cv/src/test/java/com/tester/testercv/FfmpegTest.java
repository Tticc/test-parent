package com.tester.testercv;

import org.junit.Test;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class FfmpegTest {

    @Test
    public void test_command(){
        String folderUrl = "C:\\Users\\wenc\\Desktop\\captureImg\\img\\img\\path0\\pic7_r_逆天清纯颜值小姐姐\\";
        String fileName = "逆天清纯颜值小姐姐.mp4";
        String fileFullName = "逆天清纯颜值小姐姐";
        List<String> command = new ArrayList<>();
        command.add("C:\\Users\\wenc\\Desktop\\ffmpeg\\bin\\ffmpeg.exe");
        command.add("-i");
        command.add(folderUrl+fileName);
        command.add("-c:v");
        command.add("libx264");
        command.add("-hls_time");
        command.add("20");
        command.add("-hls_list_size");
        command.add("0");
        command.add("-c:a");
        command.add("aac");
        command.add("-strict");
        command.add("-2");
        command.add("-f");
        command.add("hls");
        command.add(folderUrl+fileFullName+".m3u8");
        try{
            ProcessBuilder pb = new ProcessBuilder();
            pb.command(command);
            Process p = pb.start();
            int i = doWaitFor(p);

        }catch (Exception e){
            
        }
    }

    private int doWaitFor(Process p) {
        InputStream in = null;
        InputStream err = null;
        int exitValue = -1;
        try{
            in = p.getInputStream();
            err = p.getErrorStream();
            boolean finish = false;
            while (!finish){
                try{
                    while (in.available() > 0){
                        Character c = new Character((char)in.read());
                        System.out.print(c);
                    }
                    while (err.available() > 0){
                        Character c = new Character((char)err.read());
                        System.out.print(c);
                    }
                    exitValue = p.exitValue();
                    finish = true;
                }catch (IllegalThreadStateException itse){
                    Thread.currentThread().sleep(500);
                }
            }
        }catch (Exception e){

        }
        return exitValue;
    }
}

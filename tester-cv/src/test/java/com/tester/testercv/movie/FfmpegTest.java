package com.tester.testercv.movie;

import org.junit.Test;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class FfmpegTest {

    @Test
    public void test_command() {
        String folderUrl = "C:\\Users\\18883\\Downloads\\slice\\";
        String fileName = "xxa.mp4";
        String fileFullName = "index";
        List<String> command = new ArrayList<>();
        command.add("C:\\Users\\18883\\Desktop\\ffmpeg-master-latest-win64-gpl\\bin\\ffmpeg.exe");
        command.add("-i");
        command.add(folderUrl + fileName);
        command.add("-c:v");
        command.add("libx264");
        command.add("-hls_time");
        command.add("1");
        command.add("-hls_list_size");
        command.add("0");
        command.add("-c:a");
        command.add("aac");
        command.add("-strict");
        command.add("-2");
        command.add("-f");
        command.add("hls");
        // 出现这个问题：Frame rate very high for a muxer not efficiently supporting it. Please consider specifying a lower framerate, a different muxer or setting vsync/fps_mode to vfr
        // 无法切片
        // 添加下面的参数即可解决
        command.add("-vsync");
        command.add("2");
        command.add(folderUrl + fileFullName + ".m3u8");
        try {
            ProcessBuilder pb = new ProcessBuilder();
            pb.command(command);
            Process p = pb.start();
            int i = doWaitFor(p);

        } catch (Exception e) {

        }
    }

    private int doWaitFor(Process p) {
        InputStream in = null;
        InputStream err = null;
        int exitValue = -1;
        try {
            in = p.getInputStream();
            err = p.getErrorStream();
            boolean finish = false;
            while (!finish) {
                try {
                    while (in.available() > 0) {
                        Character c = new Character((char) in.read());
                        System.out.print(c);
                    }
                    while (err.available() > 0) {
                        Character c = new Character((char) err.read());
                        System.out.print(c);
                    }
                    exitValue = p.exitValue();
                    finish = true;
                } catch (IllegalThreadStateException itse) {
                    Thread.currentThread().sleep(500);
                }
            }
        } catch (Exception e) {

        }
        return exitValue;
    }
}

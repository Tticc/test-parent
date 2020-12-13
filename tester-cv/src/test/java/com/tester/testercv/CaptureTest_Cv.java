package com.tester.testercv;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamPanel;
import com.github.sarxos.webcam.WebcamResolution;
import org.junit.Test;
import org.springframework.util.StopWatch;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicInteger;

public class CaptureTest_Cv {

    private AtomicInteger atomicInteger = new AtomicInteger(0);
    private static Webcam webcam = Webcam.getDefault();

    private static double rate = 0.05;
    private static long intervalMS = 1000L;

    @Test
    public void test_main() throws Exception {
        WebcamResolution size = WebcamResolution.VGA;
        int widthNum = (int)Math.floor(size.getWidth()*rate);
        int heightNum = (int)Math.floor(size.getHeight()*rate);
        int widthInterval = size.getWidth()/widthNum;
        int heightInterval = size.getHeight()/heightNum;
        webcam.setViewSize(size.getSize());
        if (!webcam.isOpen()) {
            webcam.open();
        }
        viewImg();

        java.util.List<Float> changeList = new LinkedList<>();
        float currentRate = 0.0f;
        BufferedImage previous = webcam.getImage();
        BufferedImage current;
//        for(;;) {
        for(int k = 0; k < 50; k++) {
            Thread.sleep(intervalMS);
            int absMax = 0;
            current = webcam.getImage();
            for (int i = 0; i < widthNum; i++) {
                for (int j = 0; j < heightNum; j++) {
                    int currentPixel = current.getRGB(i*widthInterval, j*heightInterval);
                    int previousPixel = previous.getRGB(i*widthInterval, j*heightInterval);
                    int currentRed = (currentPixel >> 16) & 0xFF;
                    int previousRed = (previousPixel >> 16) & 0xFF;
                    int len = Math.abs(previousRed - currentRed);
                    absMax = absMax > len ? absMax : len;
                }
            }
            float changeNum = absMax;
            changeList.add(changeNum);
            System.out.println("changeNum: "+changeNum);
            if(changeNum > 100){
                writeImageFile(current);
            }
            // previous移位
            previous = current;
        }
        for (Float aFloat : changeList) {
            System.out.println(aFloat);
        }
        changeList.sort((a,b)->{
            if(a>b){
                return -1;
            }else if(a<b){
                return 1;
            }else {
                return 0;
            }
        });
        System.out.println(changeList);
    }

    private void viewImg(){
        WebcamPanel panel = new WebcamPanel(webcam);

        panel.setFPSDisplayed(true);

        panel.setDisplayDebugInfo(true);

        panel.setImageSizeDisplayed(true);

        panel.setMirrored(true);


        JFrame window = new JFrame("Test webcam panel");

        Component add = window.add(panel, -1);

        window.setResizable(true);

        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        window.pack();
        window.setVisible(true);
    }

    @Deprecated
    @Test
    public void getFromImgStream() throws InterruptedException {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        webcam.setViewSize(WebcamResolution.VGA.getSize());
//        String filePath = "C:\\Users\\wenc\\Desktop\\captureImg\\";
        for(int i = 0; i < 100; i++) {
//            WebcamUtils.capture(webcam, filePath+i+"_saved.png", ImageUtils.FORMAT_PNG);
            Thread.sleep(500L);
            if (!webcam.isOpen()) {
                webcam.open();
            }
            BufferedImage image = webcam.getImage();
//            System.out.println(image.getHeight());// 480
//            System.out.println(image.getWidth());// 640
            int pixel = image.getRGB(4, 4);
            int alpha = (pixel >> 24) & 0xFF;
            int red = (pixel >> 16) & 0xFF;
            int green = (pixel >> 8) & 0xFF;
            int blue = pixel & 0xFF;
            System.out.print("pixel: "+pixel);
            System.out.print("  alpha: "+alpha);
            System.out.print("  red: "+red);
            System.out.print("  green: "+green);
            System.out.print("  blue: "+blue);
            System.out.println();
        }
        stopWatch.stop();
        long totalTimeMillis = stopWatch.getTotalTimeMillis();
        System.out.println("total cost:"+totalTimeMillis);
    }

    @Test
    public void test_save_capture() throws Exception{

        webcam.setViewSize(WebcamResolution.VGA.getSize());
//        String filePath = "C:\\Users\\wenc\\Desktop\\captureImg\\";
        for(int i = 0; i < 10; i++){
//            WebcamUtils.capture(webcam, filePath+i+"_saved.png", ImageUtils.FORMAT_PNG);
            Thread.sleep(500L);
            if(!webcam.isOpen()){
                webcam.open();
            }
            BufferedImage image = webcam.getImage();
            toGrey(image);
            writeImageFile(image);
        }
        Thread.sleep(5000L);
    }


    @Test
    public void test_view_capture() throws InterruptedException, IOException {

        Webcam webcam = Webcam.getDefault();
        if(null == webcam){
            System.out.println("fatal error - 机器可能没有驱动/没有摄像头");
            return;
        }

        webcam.setViewSize(WebcamResolution.VGA.getSize());

        WebcamPanel panel = new WebcamPanel(webcam);

        panel.setFPSDisplayed(true);

        panel.setDisplayDebugInfo(true);

        panel.setImageSizeDisplayed(true);

        panel.setMirrored(true);


        JFrame window = new JFrame("Test webcam panel");

        Component add = window.add(panel, -1);

        window.setResizable(true);

        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        window.pack();
        window.setVisible(true);

        Thread.sleep(15000L);
    }


    public void writeImageFile(BufferedImage image) throws IOException {
        atomicInteger.incrementAndGet();
        String filePath = "C:\\Users\\wenc\\Desktop\\captureImg\\capture\\";
        File outputfile = new File(filePath+atomicInteger.get()+"saved.png");
        ImageIO.write(image, "png", outputfile);
    }

    public static void toGrey(BufferedImage image) throws IOException {
        int iw = image.getWidth();
        int ih = image.getHeight();
        for (int y = 0; y < ih; y++) {
            for (int x = 0; x < iw; x++) {
                int pixel = image.getRGB(x, y);
                int alpha = (pixel >> 24) & 0xFF;
                int red = (pixel >> 16) & 0xFF;
                int green = (pixel >> 8) & 0xFF;
                int blue = pixel & 0xFF;
//                System.out.println("["+alpha+","+red+","+green+","+blue+"]");

                // 转为灰度图
                int avg = (red + green + blue) / 3;
                pixel = (alpha << 24) | (avg << 16) | (avg << 8) | avg;
                image.setRGB(x, y, pixel);
            }
        }
    }
}

package com.tester.testercv;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamPanel;
import com.github.sarxos.webcam.WebcamResolution;
import org.junit.Test;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

public class NormalTest_Cv {

    private AtomicInteger atomicInteger = new AtomicInteger(0);


    @Test
    public void test_save_capture() throws Exception{


        Webcam webcam = Webcam.getDefault();

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
        String filePath = "C:\\Users\\wenc\\Desktop\\captureImg\\";
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

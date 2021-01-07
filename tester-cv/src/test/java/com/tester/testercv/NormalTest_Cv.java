package com.tester.testercv;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamPanel;
import com.github.sarxos.webcam.WebcamResolution;
import com.tester.testercommon.exception.BusinessException;
import com.tester.testercommon.util.MyConsumer;
import org.junit.Test;
import org.springframework.transaction.annotation.Transactional;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class NormalTest_Cv {

    private AtomicInteger atomicInteger = new AtomicInteger(0);


    class LRUMap<K,V> extends LinkedHashMap<K,V>{
        public LRUMap(){
            super(4, 0.75f, true);
        }
        @Override
        protected boolean removeEldestEntry(Map.Entry<K,V> eldest){
            return size() > 4;
        }
    }


    @Test
    public void test_hashMap(){
        String aaStr = "Aa";
        String bbStr = "BB";
        HashMap<String,Integer> map = new HashMap<>();
        map.put(aaStr,1 );
        map.put(bbStr,2);
        int aa = System.identityHashCode(aaStr);
        int bb = System.identityHashCode(bbStr);
        System.out.println(aa);
        System.out.println(bb);
        System.out.println(aaStr.hashCode());
        System.out.println(bbStr.hashCode());

        HashMap<String,Integer> map1 = new HashMap<>();
        map1.put(bbStr,2);
        HashMap<String,Integer> map3;
//        System.out.println(map3);
        System.out.println(map1);
        System.out.println(map);
        System.out.println();
        if((map3 = map1 = map) != null){
            System.out.println(map3);
            System.out.println(map1);
            System.out.println(map);
        }

    }
    @Test
    public void test_mq_tag(){
        String tags = "sjid||jkdfios";
        String s = "||";
        System.out.println(s);
        String[] split = tags.split(s);
        for (String s1 : split) {
            System.out.println(s1);
        }
    }


    @Test
    public void test_consumer_proxy() throws BusinessException {
        consumer((e)->{
            System.out.println(e);
        });
    }

    private void consumer(MyConsumer<Object> consumer) throws BusinessException {
        String path = consumer.getClass().getResource("").getPath();
        System.out.println(path);
        Method[] declaredMethods = consumer.getClass().getDeclaredMethods();
        consumer.accept(0);
    }

    @Test
    public void test_lru(){
        LRUMap<String,String> map = new LRUMap<>();
        map.put("1","1");
        map.put("2","2");
        map.put("3","3");
        System.out.println(map);
        map.get("1");
        map.put("4","4");
        System.out.println(map);
        map.put("5","5");
        System.out.println(map);
        map.put("6","6 ");
        System.out.println(map);
    }



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

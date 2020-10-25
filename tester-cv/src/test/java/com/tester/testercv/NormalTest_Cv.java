package com.tester.testercv;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamResolution;
import com.github.sarxos.webcam.WebcamUtils;
import com.github.sarxos.webcam.util.ImageUtils;
import org.junit.Test;

import java.io.IOException;

public class NormalTest_Cv {

    @Test
    public void test_capture() throws InterruptedException, IOException {

        Webcam webcam = Webcam.getDefault();

        webcam.setViewSize(WebcamResolution.VGA.getSize());

//        WebcamPanel panel = new WebcamPanel(webcam);
//
//        panel.setFPSDisplayed(true);
//
//        panel.setDisplayDebugInfo(true);
//
//        panel.setImageSizeDisplayed(true);
//
//        panel.setMirrored(true);
//
//
//        JFrame window = new JFrame("Test webcam panel");
//
//        Component add = window.add(panel, -1);
//
//        window.setResizable(true);
//
//        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//
//        window.pack();
//        window.setVisible(true);

        String filePath = "C:\\Users\\wenc\\Desktop\\captureImg\\";
        for(int i = 0; i < 10; i++){
            WebcamUtils.capture(webcam, filePath+i+"saved.png", ImageUtils.FORMAT_PNG);
            Thread.sleep(500L);
        }
        Thread.sleep(5000L);
    }


}

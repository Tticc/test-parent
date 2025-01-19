package com.tester.testerswing.capture;

import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

import java.io.File;

public class TessTest {
    public static void main(String[] args) throws TesseractException {
        ITesseract iTesseract = new Tesseract();
        iTesseract.setDatapath("C:\\Users\\18883\\Desktop\\near2\\MyDetect\\tessdata");// 语言包放置文件夹
        iTesseract.setLanguage("eng"); // 使用英文语言包
//        File img = new File("C:\\Users\\18883\\Desktop\\captureImg\\test_cap2.png");// 待识别文件
        File img = new File("C:\\Users\\18883\\Desktop\\near2\\MyDetect\\tessdata\\screenshot.png"); // 使用 PNG 格式保存截图

        String s = iTesseract.doOCR(img);
        System.out.println(s);

    }
}

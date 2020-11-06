package com.tester.testercv;

import com.tester.testercv.utils.MatrixImgTool;
import org.junit.Test;

import java.util.Random;

public class Img_GenerateImageTest {

    public static Random random = new Random();

    public static final String BASE_PATH = "C:\\Users\\wenc\\Desktop\\captureImg\\";

    public static int backgroundPixel =  255;
    public static int pointPixel =  0;
    public static int times = 4;

    @Test
    public void test_buildImg() throws Exception {
        int pixNum = 0;
        int width = 240;
        int height = 160;
        int[][] dataArr = new int[height][width];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                dataArr[i][j] = backgroundPixel;
                int i1 = random.nextInt(2000);
                if(i1 == 0){
                    dataArr[i][j] = pointPixel;
                    ++pixNum;
                }
            }
        }
        System.out.println("像素点："+pixNum);
        String filePath = BASE_PATH+"saved.png";
        int[][] imgArr = MatrixImgTool.transToImgArr(dataArr,times);
        MatrixImgTool.generateImgByImgArr(imgArr,filePath);
    }





}

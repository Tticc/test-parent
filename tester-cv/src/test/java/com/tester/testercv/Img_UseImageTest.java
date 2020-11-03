package com.tester.testercv;

import com.tester.testercv.utils.MatrixImgTool;
import org.junit.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;


public class Img_UseImageTest {

    private static Random random = new Random();
    private AtomicInteger atomicInteger = new AtomicInteger(0);
    public static final String BASE_PATH = Img_GenerateImageTest.BASE_PATH;

    private static int pointPixel =  Img_GenerateImageTest.pointPixel;
    private static int times = Img_GenerateImageTest.times;


    @Test
    public void test_dou(){
        int i1 = -16777216; // 数据
        int i2 = -11184811; // 背景
        System.out.println((i1 << 8) >> 8);
        System.out.println(((i2 << 8) >> 8)%256);

        int fei = ~-526345;
        System.out.println(fei);
        System.out.println(16777000 % 256);
    }
    @Test
    public void test_readImg() throws Exception {
        String filePath = BASE_PATH+"p1_saved.png";
        BufferedImage img = ImageIO.read(new File(filePath));
        int[][] pointArr = getDataArrayFromImg(img);
//        printDataArray(pointArr);
        reWriteImg(pointArr,times);
    }

    /**
     * 数据矩阵转为图片存盘。<br/>
     * <ol>
     *     <li>数据矩阵转为图片矩阵</li>
     *     <li>根据图片矩阵生成图片</li>
     * </ol>
     * @param dataArr
     * @param times
     * @return void
     * @Date 14:36 2020/11/3
     * @Author 温昌营
     **/
    public void reWriteImg(int[][] dataArr,int times) throws Exception {
        int[][] imgArr = MatrixImgTool.transToImgArr(dataArr, times);
        int i = atomicInteger.incrementAndGet();
        String filePath = BASE_PATH+"regen_"+i+"_saved.png";
        MatrixImgTool.generateImgByImgArr(imgArr,filePath);
    }

    /**
     * 从图片中获取数据矩阵
     * @param img
     * @return int[][]
     * @Date 14:37 2020/11/3
     * @Author 温昌营
     **/
    public int[][] getDataArrayFromImg(BufferedImage img){
        int height = img.getHeight();
        int width = img.getWidth();
        int[][] pointArr = new int[height/times][width/times];
        int imgPix;
        for (int i = 0; i < pointArr.length; i++) {
            for (int j = 0; j < pointArr[0].length; j++) {
                imgPix = img.getRGB(j*times,i*times);
                pointArr[i][j] = ((imgPix << 8) >> 8)%256;
            }
        }
        return pointArr;
    }

    /**
     * 打印数据矩阵
     * @param dataArr
     * @return void
     * @Date 14:37 2020/11/3
     * @Author 温昌营
     **/
    public void printDataArray(int[][] dataArr){
        for (int i = 0; i < dataArr.length; i++) {
            for (int j = 0; j < dataArr[0].length; j++) {
                if(dataArr[i][j] == pointPixel){
                    System.out.print(256+",");
                }else {
                    System.out.print(0+",");
                }
            }
            System.out.println();
        }

    }


}

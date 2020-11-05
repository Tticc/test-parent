package com.tester.testercv;

import com.tester.testercv.utils.MatrixImgTool;
import org.junit.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Arrays;
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

        System.out.println((float)5/6);


    }
    @Test
    public void test_readImg() throws Exception {
        String filePath = BASE_PATH+"p1_saved.png";
        BufferedImage img = ImageIO.read(new File(filePath));
        // 从图片获取数据矩阵
        int[][] dataArr = MatrixImgTool.getDataArrayFromImg(img,times);
        // 从数据矩阵获取点
        int[][] points = MatrixImgTool.getPointsFromDataArr(dataArr,pointPixel);
        int[][] pointsCopy = MatrixImgTool.getPointsFromDataArr(dataArr,pointPixel);
        // 计算连接顺序
        int[] order = doCal(pointsCopy);
        int lineNum = order.length-1;
        int[][] imgArr = MatrixImgTool.transToImgArr(dataArr, times);
        for (int i = 0; i < lineNum; i++) {
            MatrixImgTool.drawLine(points[order[i]],points[order[i+1]],imgArr,times);
        }
        MatrixImgTool.generateImgByImgArr(imgArr, BASE_PATH+"p1_saved_lined.png");

    }



    /**
     * 计算距离
     **/
    private int[] doCal(int[][] points){
        int length = points.length;
        int[] order = new int[length];
        // 当前起始点从0开始，不做随机
//        int length = points.length;
//        int startPoint = random.nextInt(length);
        int startPoint = 0;
        int[][] tempPoints = Arrays.copyOf(points, length);
        calSub(startPoint,tempPoints,order,0);
        for (int i = 0; i < length; i++) {
            System.out.print(order[i]+",");
        }
        return order;
    }
    private void calSub(int currPointIndex,int[][] tempPoints,int[] order,int currOrderIndex){
        int length = tempPoints.length;
        if(currOrderIndex >= length){
            return;
        }
        order[currOrderIndex] = currPointIndex;
        int nextPointIndex = currPointIndex;
        int dis = Integer.MAX_VALUE;
        for (int i = 0; i < length; i++) {
            if(i==currPointIndex || tempPoints[i][0] == -1){
                continue;
            }
            int newDis = calDis(tempPoints, currPointIndex, i);
            if(dis <= newDis){
                continue;
            }
            dis = newDis;
            nextPointIndex = i;

        }
        tempPoints[currPointIndex][0]=tempPoints[currPointIndex][1] = -1;
        calSub(nextPointIndex,tempPoints,order,++currOrderIndex);
    }

    private int calDis(int[][] points,int startIndex,int endIndex){
        int[] startPoint = points[startIndex];
        int[] endPoint = points[endIndex];
        double i = Math.sqrt(Math.pow(endPoint[0] - startPoint[0],2) + Math.pow(endPoint[1] - startPoint[1],2));
        return (int)Math.round(i);
    }

    private void regenImg(int[][] pointArr) throws Exception {
        int[][] imgArr = MatrixImgTool.transToImgArr(pointArr, times);
//        MatrixImgTool.drawLine(4,31,imgArr,154,120,times);
        int i = atomicInteger.incrementAndGet();
        String outputPath = BASE_PATH+"regen_"+i+"_saved.png";
        MatrixImgTool.generateImgByImgArr(imgArr,outputPath);
//        reWriteImg(pointArr,times);
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
     * 打印数据矩阵
     * @param dataArr
     * @return void
     * @Date 14:37 2020/11/3
     * @Author 温昌营
     **/
    private void printDataArray(int[][] dataArr){
        for (int i = 0; i < dataArr.length; i++) {
            for (int j = 0; j < dataArr[0].length; j++) {
                if(dataArr[i][j] == pointPixel){
                    // 数据点
//                    System.out.print(256+",");
                    System.out.println("["+i+","+j+"]");
                }else {
                    // 背景点
//                    System.out.print(0+",");
                }
            }
//            System.out.println();
        }

    }


}

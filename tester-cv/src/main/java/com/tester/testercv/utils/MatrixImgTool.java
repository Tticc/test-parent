package com.tester.testercv.utils;

import junit.framework.Assert;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * @Author 温昌营
 * @Date
 */
public class MatrixImgTool {

    /**
     * 将数据矩阵转为图片矩阵
     * @param dataArr 数据矩阵
     * @param times 放大倍数。
     * @return int[][]
     * @Date 14:39 2020/11/3
     * @Author 温昌营
     **/
    public static int[][] transToImgArr(int[][] dataArr, int times) throws Exception {
        Assert.assertNotNull("数据矩阵不能为空",dataArr);
        Assert.assertNotSame("数据矩阵不能为空",dataArr.length,0);
        Assert.assertNotSame("数据矩阵不能为空",dataArr[0].length,0);

        int width = dataArr.length*times;
        int height = dataArr[0].length*times;
        int[][] imgArr = new int[width][height];
        for(int i = 0; i < dataArr.length; i++){
            for(int j = 0; j < dataArr[0].length; j++){
                setImgArr(i,j,imgArr,times,dataArr[i][j]);
            }
        }
        return imgArr;
    }


    /**
     * 根据图片矩阵生成图片，并保存
     * @param imgArr 图片矩阵
     * @param outputfile 图片地址
     * @return void
     * @Date 14:39 2020/11/3
     * @Author 温昌营
     **/
    public static void generateImgByImgArr(int[][] imgArr, String outputfile) throws IOException {
        int width = imgArr[0].length; // 240
        int height = imgArr.length; // 160
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                image.setRGB(j,i,imgArr[i][j]);
            }
        }
        ImageIO.write(image, "png", new File(outputfile));
    }


    /**
     * 从图片中获取数据矩阵
     * @param img
     * @return int[][]
     * @Date 14:37 2020/11/3
     * @Author 温昌营
     **/
    public static int[][] getDataArrayFromImg(BufferedImage img, int times){
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

    public static int[][] getPointsFromDataArr(int[][] pointArr, int pointPixel){
        int pointCount = 0;
        int height = pointArr.length;
        int width = pointArr[0].length;
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if(pointArr[i][j] == pointPixel){
                    ++pointCount;
                }
            }
        }
        int[][] points = new int[pointCount][2];
        int count=0;
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if(pointArr[i][j] == pointPixel){
                    points[count][0] = i;
                    points[count][1] = j;
                    count++;
                }
            }
        }
        return points;
    }


    public static void drawLine(int[] startPoint,int[] endPoint,int[][] imgArr,int times){
        int x1 = startPoint[0]*times;
        int x2 = endPoint[0]*times;
        int y1 = startPoint[1]*times;
        int y2 = endPoint[1]*times;
        float k =  ((float)y2 - y1) / ((float)x2 - x1);
        int b = ((y1+y2) - Math.round(k*(x1+x2)))/2;
        int j;
        int xDis = Math.abs(x1-x2);
        int yDis = Math.abs(y1-y2);
        if(xDis >= yDis) {
            if (x1 < x2) {
                for (int i = x1; i < x2; i++) {
                    j = Math.round(k * i) + b;
                    imgArr[i][j] = imgArr[x1][y1];
                }

            } else {
                for (int i = x1; i >= x2; i--) {
                    j = Math.round(k * i) + b;
                    imgArr[i][j] = imgArr[x1][y1];
                }
            }
        }else {
            if (y1 < y2) {
                for (int i = y1; i < y2; i++) {
                    j = Math.round((i-b)/k);
                    imgArr[j][i] = imgArr[x1][y1];
                }

            } else {
                for (int i = y1; i >= y2; i--) {
                    j = Math.round((i-b)/k);
                    imgArr[j][i] = imgArr[x1][y1];
                }
            }
        }
    }


    private static void setImgArr(int i,int j,int imgArr[][],int times, int pixel){
        for (int x = 0; x < times; x++) {
            for (int y = 0; y < times; y++) {
                imgArr[i*times+x][j*times+y] = getByPixel(pixel);
            }
        }

    }

    private static int getByPixel(int pixel){
        int alpha = (pixel >> 24) & 0xFF;
        int red = (pixel >> 16) & 0xFF;
        int green = (pixel >> 8) & 0xFF;
        int blue = pixel & 0xFF;
//                System.out.println("["+alpha+","+red+","+green+","+blue+"]");

        // 转为灰度图
        int avg = (red + green + blue) / 3;
        pixel = (alpha << 24) | (avg << 16) | (avg << 8) | avg;
//        if(pixel == 5592405) {
////            System.out.println(pixel % 256);
//            return 16777000;
//        }
        if(pixel != 0){
            return 16777000;
        }
        return pixel;
    }
}

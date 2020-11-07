package com.tester.testercv;

import com.tester.testercv.utils.MatrixImgTool;
import org.junit.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.*;
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
    public void test_redrawImg() throws Exception {
        String filePath = BASE_PATH+"p1_saved.png";
        BufferedImage img = ImageIO.read(new File(filePath));
        // 从图片获取数据矩阵
        int[][] dataArr = MatrixImgTool.getDataArrayFromImg(img,times);
//        minInNextStep(dataArr);
        minInAllStep(dataArr);
    }

    /**
     * 整体最小算法
     * @param dataArr
     * @throws Exception
     */
    private void minInAllStep(int[][] dataArr) throws Exception {
        // 从数据矩阵获取点
        int[][] points = MatrixImgTool.getPointsFromDataArr(dataArr,pointPixel);
        // 计算连接顺序
        int[] order = calculateOrder_MinInAllStep(points);
//        int lineNum = order.length-1;
//        int[][] imgArr = MatrixImgTool.transToImgArr(dataArr, times);
//        for (int i = 0; i < lineNum; i++) {
//            MatrixImgTool.drawLine(points[order[i]],points[order[i+1]],imgArr,times);
//        }
//        MatrixImgTool.generateImgByImgArr(imgArr, BASE_PATH+"saved_minInAll_lined.png");
    }

    private int[] calculateOrder_MinInAllStep(int[][] points){
        ArrayList<Integer> minDisList = new ArrayList<>(Arrays.asList(Integer.MAX_VALUE));
        LinkedHashSet<Integer> hisPointIndex = new LinkedHashSet<>();
        hisPointIndex.add(0);
        ArrayList<Integer> minPointIndex = new ArrayList<>();
        calculateLine_MinInAllStep(minDisList,minPointIndex,0,points,0,hisPointIndex);
        System.out.println(minPointIndex);
        return null;
    }

    /**
     * <ol>
     *     <li>当前最小距离。初始值为：Integer.MAX_VALUE;</li>
     *     <li>当前最小距离对应的点 int[]</li>
     *     <li>当前距离</li>
     *     <li>所有点</li>
     *     <li>已遍历的点 LinkedHashSet</li>
     *     <li>当前点</li>
     *     <li></li>
     * </ol>
     * @return
     */
    private void calculateLine_MinInAllStep(ArrayList<Integer> minDisList, ArrayList<Integer> minPointIndex, int dis, int[][] points, int currPointIndex, LinkedHashSet<Integer> hisPointIndex){
        int length = points.length;
        if(hisPointIndex.size() >= length){
            minDisList.add(dis);
//            System.out.println(hisPointIndex);
            Iterator<Integer> iterator = hisPointIndex.iterator();
            if(iterator.hasNext()){
                minPointIndex.add(iterator.next());
            }
            return;
        }
        for (int i = 0; i < length; i++) {
            int size = minDisList.size();
            int minDis = minDisList.get(size-1);
            if(hisPointIndex.contains(i)){
                continue;
            }
            int newDis = dis + calDis(points, currPointIndex, i);
            if(newDis >= minDis){
                // 此时未到达最终点，如果距离已经大于现有的最小值，放弃
                continue;
            }
            System.out.println(hisPointIndex);
            hisPointIndex.add(i);
            calculateLine_MinInAllStep(minDisList,minPointIndex,newDis,points,i,hisPointIndex);
            int newSize = minDisList.size();
//            if(size >= newSize){
                // 这时，意味着最短路径没有更新，删去当前节点的历史记录
                hisPointIndex.remove(i);
//            }
        }
    }


    /**
     * 下一步最小算法
     * @param dataArr
     * @throws Exception
     */
    private void minInNextStep(int[][] dataArr) throws Exception {
        // 从数据矩阵获取点
        int[][] points = MatrixImgTool.getPointsFromDataArr(dataArr,pointPixel);
        // 计算连接顺序
        int[] order = calculateOrder_MinInNextStep(points);
        int lineNum = order.length-1;
        int[][] imgArr = MatrixImgTool.transToImgArr(dataArr, times);
        for (int i = 0; i < lineNum; i++) {
            MatrixImgTool.drawLine(points[order[i]],points[order[i+1]],imgArr,times);
        }
        MatrixImgTool.generateImgByImgArr(imgArr, BASE_PATH+"saved_minInNextStep_lined.png");
    }



    /**
     * 计算距离
     **/
    private int[] calculateOrder_MinInNextStep(int[][] points){
        int length = points.length;
        int[] order = new int[length];
        // 当前起始点从第0个节点开始，不做随机
//        int startPoint = random.nextInt(length);
        int startPoint = 0;
        calculateLine_MinInNextStep(startPoint,points,order,0, new HashSet<>());
        for (int i = 0; i < length; i++) {
            System.out.print(order[i]+",");
        }
        return order;
    }

    /**
     *
     * @param currPointIndex 当前点
     * @param points 所有点
     * @param order 点顺序
     * @param currOrderIndex 点顺序序号
     * @param hisPointIndex 已到达的点
     */
    private void calculateLine_MinInNextStep(int currPointIndex, int[][] points, int[] order, int currOrderIndex, Set<Integer> hisPointIndex){
        int length = points.length;
        if(currOrderIndex >= length){
            return;
        }
        hisPointIndex.add(currPointIndex);
        order[currOrderIndex] = currPointIndex;
        int nextPointIndex = currPointIndex;
        // 距离，初始设置为int最大值
        int dis = Integer.MAX_VALUE;
        for (int i = 0; i < length; i++) {
            if(hisPointIndex.contains(i)){
                continue;
            }
            int newDis = calDis(points, currPointIndex, i);
            if(dis <= newDis){
                continue;
            }
            dis = newDis;
            nextPointIndex = i;
        }
        calculateLine_MinInNextStep(nextPointIndex,points,order,++currOrderIndex,hisPointIndex);
    }

    private int calDis(int[][] points,int startIndex,int endIndex){
        int[] startPoint = points[startIndex];
        int[] endPoint = points[endIndex];
        double i = Math.sqrt(Math.pow(endPoint[0] - startPoint[0],2) + Math.pow(endPoint[1] - startPoint[1],2));
        return (int)Math.round(i);
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

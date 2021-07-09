package com.tester.testercv.rateCount;

import com.alibaba.fastjson.JSON;
import com.tester.testercommon.util.DateUtil;
import org.junit.Test;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @Author 温昌营
 * @Date
 */
public class RateCountImgTest {

    public static final String BASE_PATH = "C:\\Users\\Admin\\Desktop\\captureImg\\rateCount\\";
    public static final Scalar DEFAULT_LINE_POINT = new Scalar(255);
    public static final Scalar DEFAULT_TEXT_POINT = new Scalar(255);
    private static Mat mat;
    private static int row_y_height = 5000;
    private static int col_x_width = 3000;
    static{
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        mat = new Mat(row_y_height, col_x_width, CvType.CV_8UC1, new Scalar(0));
    }


    // VMOption = -Djava.library.path=E:\opencv\opencv\build\java\x64
    @Test
    public void test_draw(){
        draw();
    }

    public void draw(){
        String pointStr = getDefaultPointStr();
        Map<Integer, Double> points = readFromStr(pointStr);
        draw(points);
    }


    public void draw(Map<Integer, Double> points){
        Set<Map.Entry<Integer, Double>> entries = points.entrySet();
        Map.Entry<Integer, Double> integerDoubleEntry = entries.stream()
//                .sorted((a, b) -> b.getKey().compareTo(a.getKey())) // 逆序排序
                .sorted((a, b) -> a.getKey().compareTo(b.getKey())) // 正序排序
                .reduce((a, b) -> {
                    int startX = a.getKey()*100;
                    int startY = row_y_height - (int) (a.getValue() * 10000);
                    int endX = b.getKey()*100;
                    int endY = row_y_height - (int) (b.getValue() * 10000);
                    for (int i = -4; i <= 4; i++) {
                        drawLine(mat, startX, startY+i, endX, endY+i);
                    }
                    drawText(mat,startX+28,startY+28, "["+startX+","+(row_y_height - startY)+"]");
                    return b;
                }).get();
        writeImg("rateImg"+ DateUtil.dateFormat(new Date(),"MMddHHmmss"),mat);
    }
    private void writeImg(String name,Mat mat){
        Imgcodecs.imwrite(BASE_PATH+name+".png",mat);
    }
    private void drawLine(Mat mat, int startX, int startY, int endX, int endY){
        if(null == mat){
            return;
        }
        Point startPoint = new Point(startX, startY);
        Point endPoint = new Point(endX, endY);
        lineNode(mat,startPoint,endPoint);
    }
    void lineNode(Mat mat,Point start,Point end){
        Imgproc.line(mat,start,end,DEFAULT_LINE_POINT);
    }

    private void drawText(Mat mat, int startX, int startY, String text){
        if(null == mat){
            return;
        }
        Point point = new Point(startX, startY);
        imgproc_text(mat,point,text);
    }
    private void imgproc_text(Mat mat, Point point, String text){
        Point tempPoint = new Point(point.x,point.y);
        Imgproc.putText(mat,text,tempPoint,Imgproc.FONT_HERSHEY_SIMPLEX,0.5, DEFAULT_TEXT_POINT);
    }









    private Map<Integer, Double> readFromStr(String str){
        Map<Integer, Double> res = new HashMap<>();
        Map map = JSON.parseObject(str, Map.class);
        Set<Map.Entry<Integer,BigDecimal>> set = map.entrySet();
        if(CollectionUtils.isEmpty(set)){
            return res;
        }
        set.forEach(e -> res.put(e.getKey(), e.getValue().doubleValue()));
        return res;
    }

    private String getDefaultPointStr(){
        return "{1:0.09780000000000001,2:0.12988116000000005,3:0.15051597715200024,4:0.16444052018062252,5:0.17306365982022928,6:0.19207354878143293,7:0.21589159429739702,8:0.23271711519244626,9:0.24630882913137106,10:0.25827572848631397,11:0.27145148747442027,12:0.28345016086709107,13:0.29456102806544426,14:0.30709797330539584,15:0.31751551225231367,16:0.3259423495507898}";
    }
}

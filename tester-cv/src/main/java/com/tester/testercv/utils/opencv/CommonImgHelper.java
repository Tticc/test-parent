package com.tester.testercv.utils.opencv;

import org.opencv.core.*;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.io.File;

/**
 * @Author 温昌营
 * @Date 2022-8-9 15:35:59
 */
@Deprecated
public class CommonImgHelper {

    public static final String BASE_PATH = "C:\\Users\\Admin\\Desktop\\captureImg\\";
    public static final Scalar DEFAULT_LINE_POINT = new Scalar(255);
    public static final Scalar DEFAULT_TEXT_POINT = new Scalar(255);

    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    public static Mat getMat(int row_y_height, int col_x_width) {
        return new Mat(row_y_height, col_x_width, CvType.CV_8UC1, new Scalar(0));
    }

    public static Point getPoint(int x, int y) {
        return new Point(x, y);
    }

    public static void drawLine(Mat mat, int startX, int startY, int endX, int endY) {
        if (null == mat) {
            return;
        }
        Point startPoint = new Point(startX, startY);
        Point endPoint = new Point(endX, endY);
        Imgproc.line(mat, startPoint, endPoint, DEFAULT_LINE_POINT);
    }

    public static void writeImg(String name, Mat mat) {
        writeImg(name, mat, "img");
    }

    public static void drawEllipse(Mat mat, Point point,int height,int width){
        Imgproc.ellipse(mat,point,new Size((double)width/2,(double) height/2),360,0,360,DEFAULT_LINE_POINT);
    }

    public static void drawCircle(Mat img, Point center, int radius){
        Imgproc.circle(img,center,radius,DEFAULT_LINE_POINT,2);
    }

    public static void writeImg(String name, Mat mat, String folderName) {
        String path = BASE_PATH + folderName;
        File file1 = new File(path);
        if (!file1.exists())
            file1.mkdirs();
        Imgcodecs.imwrite(path + "\\" + name + ".png", mat);
    }

    public static void showImg(Mat mat,String ...name){
        String picName = "picX";
        int defaultShowTime = 10;// second
        if (name.length>0) {
            picName = name[0];
        }
        HighGui.imshow(picName,mat);
        HighGui.waitKey();
//        try {
//            Thread.sleep(defaultShowTime*1000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
    }
}

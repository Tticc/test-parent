package com.tester.testercv.utils.opencv;

import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;

/**
 * @Author 温昌营
 * @Date 2022-8-8 15:15:12
 * @see CommonImgHelper
 */
public class OpenCVDrawHelper {

    public static final Scalar DEFAULT_LINE_POINT = new Scalar(255);
    public static final Scalar DEFAULT_TEXT_POINT = new Scalar(255);

    static {
        OpenCVBaseHelper.init();
    }

    /**
     * 根据宽高创建 mat
     * @Date 15:34 2022/8/9
     * @Author 温昌营
     **/
    public static Mat getMat(int row_y_height, int col_x_width) {
        return new Mat(row_y_height, col_x_width, CvType.CV_8UC1, new Scalar(0));
    }

    /**
     * 创建point
     * @Date 15:34 2022/8/9
     * @Author 温昌营
     **/
    public static Point getPoint(int x, int y) {
        return new Point(x, y);
    }

    /**
     * 划线
     * @Date 15:34 2022/8/9
     * @Author 温昌营
     **/
    public static void drawLine(Mat mat, int startX, int startY, int endX, int endY) {
        if (null == mat) {
            return;
        }
        Point startPoint = new Point(startX, startY);
        Point endPoint = new Point(endX, endY);
        Imgproc.line(mat, startPoint, endPoint, DEFAULT_LINE_POINT);
    }

    /**
     * 绘制椭圆
     * @Date 15:34 2022/8/9
     * @Author 温昌营
     **/
    public static void drawEllipse(Mat mat, Point point,int height,int width){
        Imgproc.ellipse(mat,point,new Size((double)width/2,(double) height/2),360,0,360,DEFAULT_LINE_POINT);
    }

    /**
     * 绘制圆
     * @Date 15:34 2022/8/9
     * @Author 温昌营
     **/
    public static void drawCircle(Mat img, Point center, int radius){
        Imgproc.circle(img,center,radius,DEFAULT_LINE_POINT,2);
    }


}

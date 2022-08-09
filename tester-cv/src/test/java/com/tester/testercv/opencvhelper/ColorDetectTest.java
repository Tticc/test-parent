package com.tester.testercv.opencvhelper;

import com.tester.testercv.utils.opencv.OpenCVHelper;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author 温昌营
 * @Date 2022-8-8 15:19:41
 */
public class ColorDetectTest {

    public static void main(String[] args) throws InterruptedException {
//        Mat src = OpenCVHelper.readImgToMat("C:\\Users\\Admin\\Desktop\\captureImg\\color\\blue_line.png");
        Mat src = OpenCVHelper.readImgToMat("C:\\Users\\Admin\\Desktop\\captureImg\\color\\normal.PNG");
//        Mat src = OpenCVHelper.readImgToMat("C:\\Users\\Admin\\Desktop\\captureImg\\color\\warning.PNG");
//        Mat src = OpenCVHelper.readImgToMat("C:\\Users\\Admin\\Desktop\\captureImg\\color\\warning2.PNG");

        Mat blurredImage = OpenCVHelper.newMat(src);
        // remove some noise
        Imgproc.blur(src, blurredImage, new Size(7, 7));
//        OpenCVHelper.showImg(blurredImage,"dest_hsv");

        Mat dest = OpenCVHelper.newMat(blurredImage);
        Imgproc.cvtColor(blurredImage, dest, Imgproc.COLOR_BGR2HSV);
//        OpenCVHelper.showImg(dest,"dest_hsv");

        /**
         * OpenCV中H∈ [0, 180）， S ∈ [0, 255]， V ∈ [0, 255]。
         * 我们知道H分量基本能表示一个物体的颜色，但是S和V的取值也要在一定范围内，
         * 因为S代表的是H所表示的那个颜色和白色的混合程度，也就说S越小，颜色越发白，也就是越浅；
         * V代表的是H所表示的那个颜色和黑色的混合程度，也就说V越小，颜色越发黑
         * @param args
         * @return void
         * @Date 10:30 2022/8/9
         * @Author 温昌营
         **/
        int minH = 0;
        int maxH = 180;

        int minS = 0;
        int maxS = 20;

        int minV = 46;
        int maxV = 220;
        Scalar minValues = new Scalar(minH, minS, minV);
        Scalar maxValues = new Scalar(maxH, maxS, maxV);


        Mat finalMat = OpenCVHelper.newMat(dest);
        Core.inRange(dest, minValues, maxValues, finalMat);
//        OpenCVHelper.showImg(finalMat,"dest_hsv");

        List<Mat> list = new ArrayList<>();
        list.add(src);
        list.add(blurredImage);
        list.add(dest);
        list.add(finalMat);
        OpenCVHelper.playImg(list, false);
//        testingImg(dest);
    }


}

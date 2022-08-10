package com.tester.testercv.opencvhelper;

import com.tester.base.dto.exception.BusinessException;
import com.tester.testercommon.util.MyConsumer;
import com.tester.testercv.utils.opencv.OpenCVHelper;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.springframework.util.StopWatch;

/**
 * @Author 温昌营
 * @Date 2022-8-8 15:19:41
 */
public class ColorDetectDemo {


    public static void main(String[] args) throws Exception {
        String picPath = "E:\\Development\\Projects_backup\\test-parent\\tester-swing\\src\\main\\resources\\normal2.PNG";

        boolean b = detectWhite(picPath, (mat) -> {
//            new Thread(() -> {OpenCVHelper.showImg(mat, "name", 50000);}).start();
            new Thread(() -> {OpenCVHelper.showImg(OpenCVHelper.readImgToMat(picPath), "name", 500000000);}).start();
        });
        if(b){
            System.out.println("warning... ");
        }
//        detectRed("C:\\Users\\Admin\\Desktop\\captureImg\\color\\warning2.PNG");
//        detectYellow("C:\\Users\\Admin\\Desktop\\captureImg\\color\\normal_1.PNG");
    }

    /**
     * OpenCV中H∈ [0, 180）， S ∈ [0, 255]， V ∈ [0, 255]。
     * 我们知道H分量基本能表示一个物体的颜色，但是S和V的取值也要在一定范围内，
     * 因为S代表的是H所表示的那个颜色和白色的混合程度，也就说S越小，颜色越发白，也就是越浅；
     * V代表的是H所表示的那个颜色和黑色的混合程度，也就说V越小，颜色越发黑
     *
     * 具体颜色对应的HSV数值看：onenote -> miscellaneous -> 脚本尝试 -> opencv -> HSV颜色空间
     * @Date 10:30 2022/8/9
     * @Author 温昌营
     **/
    public static boolean detectWhite(String currentImgPath, MyConsumer<Mat> consumer) throws BusinessException {
//        Mat src = OpenCVHelper.readImgToMat("C:\\Users\\Admin\\Desktop\\captureImg\\color\\normal.PNG");
        Mat src = OpenCVHelper.readImgToMat(currentImgPath);
        int minH = 0;
        int maxH = 180;
        int minS = 0;
        int maxS = 20;
        int minV = 46;
        int maxV = 220;
        Scalar minValues = new Scalar(minH, minS, minV);
        Scalar maxValues = new Scalar(maxH, maxS, maxV);

        Mat resultMat = detect(src, minValues, maxValues);
        return doCheckIfNeedWarning(resultMat, consumer);
    }
    public static boolean detectRed(String currentImgPath, MyConsumer<Mat> consumer) throws BusinessException {
        Mat src = OpenCVHelper.readImgToMat(currentImgPath);
        int minH = 0;
        int maxH = 10;
        int minS = 43;
        int maxS = 255;
        int minV = 46;
        int maxV = 255;
        Scalar minValues = new Scalar(minH, minS, minV);
        Scalar maxValues = new Scalar(maxH, maxS, maxV);

        Mat resultMat = detect(src, minValues, maxValues);
        return doCheckIfNeedWarning(resultMat, consumer);
    }
    public static boolean detectYellow(String currentImgPath, MyConsumer<Mat> consumer) throws BusinessException {
//        Mat src = OpenCVHelper.readImgToMat("C:\\Users\\Admin\\Desktop\\captureImg\\color\\warning2.PNG");
        Mat src = OpenCVHelper.readImgToMat(currentImgPath);
        int minH = 26;
        int maxH = 34;
        int minS = 43;
        int maxS = 255;
        int minV = 46;
        int maxV = 255;
        Scalar minValues = new Scalar(minH, minS, minV);
        Scalar maxValues = new Scalar(maxH, maxS, maxV);

        Mat resultMat = detect(src, minValues, maxValues);
        return doCheckIfNeedWarning(resultMat, consumer);
    }

    private static Mat detect(Mat src, Scalar minValues, Scalar maxValues){
        Mat blurredImage = OpenCVHelper.newMat(src);
        // remove some noise
        Imgproc.blur(src, blurredImage, new Size(7, 7));
        Mat dest = OpenCVHelper.newMat(blurredImage);
        Imgproc.cvtColor(blurredImage, dest, Imgproc.COLOR_BGR2HSV);
        Mat resultMat = OpenCVHelper.newMat(dest);
        Core.inRange(dest, minValues, maxValues, resultMat);
        return resultMat;
    }

    private static boolean doCheckIfNeedWarning(Mat src, MyConsumer<Mat> consumer) throws BusinessException {
        consumer.accept(src);
        int count = 0;
        int rows = src.rows();
        int cols = src.cols();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                double[] doubles = src.get(i, j);
                double aDouble = doubles[0];
                if (aDouble > 0) {
                    ++count;
                }
                if(count > 5){
                    return true;
                }
            }
        }
        return false;
    }

}

package com.tester.testercv.utils.detectColor;

import com.tester.base.dto.exception.BusinessException;
import com.tester.testercv.utils.opencv.OpenCVHelper;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.highgui.HighGui;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * @Author 温昌营
 * @Date 2022-8-8 15:19:41
 */
public class ColorDetectTool {

    // 在static代码块中初始化
    public static List<Scalar> defaultMinValues = new ArrayList<>();
    public static List<Scalar> defaultMaxValues = new ArrayList<>();



    public static void main(String[] args) throws Exception {
        String picPath = "D:\\desktop\\captureImg\\warn\\001_hisImg_warn_exception.png";
        Mat src = OpenCVHelper.readImg2Mat(picPath);
        List<Scalar> minList = new ArrayList<>();
        List<Scalar> maxList = new ArrayList<>();
        addGray(minList,maxList);
//        addLightRed(minList,maxList);
        addDeepRed(minList,maxList);
        addOrange(minList,maxList);

        boolean b = doDetect(src,minList,maxList, (mat) -> {
            new Thread(() -> {
                // 将单通道转换为三通道
                Mat mat_tmp = new Mat();
                Imgproc.cvtColor(mat, mat_tmp, Imgproc.COLOR_GRAY2RGB);
                // 拼接展示
                Mat dest = OpenCVHelper.hconcatMat(OpenCVHelper.readImg2Mat(picPath), mat_tmp);
                HighGui.imshow("after11", dest);
                HighGui.waitKey(60000);
            }).start();
        });
        if (b) {
            System.out.println("warning... ");
        }
    }

    /**
     * OpenCV中H∈ [0, 180）， S ∈ [0, 255]， V ∈ [0, 255]。
     * 我们知道H分量基本能表示一个物体的颜色，但是S和V的取值也要在一定范围内，
     * 因为S代表的是H所表示的那个颜色和白色的混合程度，也就说S越小，颜色越发白，也就是越浅；
     * V代表的是H所表示的那个颜色和黑色的混合程度，也就说V越小，颜色越发黑
     * <p>
     * 具体颜色对应的HSV数值看：onenote -> miscellaneous -> 脚本尝试 -> opencv -> HSV颜色空间
     *
     * @Date 10:30 2022/8/9
     * @Author 温昌营
     **/
    @Deprecated
    public static boolean detectGray(Mat src, Consumer<Mat> consumer) throws BusinessException {
        int minH = 0;
        int maxH = 180;
        int minS = 0;
        int maxS = 20;
        int minV = 46;
        int maxV = 220;
        Scalar minValues = new Scalar(minH, minS, minV);
        Scalar maxValues = new Scalar(maxH, maxS, maxV);

        Mat resultMat = doDetect(src, minValues, maxValues);
        return doCheckIfNeedWarning(resultMat, consumer);
    }

    @Deprecated
    public static boolean detectRed(Mat src, Consumer<Mat> consumer) throws BusinessException {
        int minH = 156;
        int maxH = 180;
        int minS = 43;
        int maxS = 255;
        int minV = 46;
        int maxV = 255;
        Scalar minValues = new Scalar(minH, minS, minV);
        Scalar maxValues = new Scalar(maxH, maxS, maxV);

        Mat resultMat = doDetect(src, minValues, maxValues);
        return doCheckIfNeedWarning(resultMat, consumer);
    }

    @Deprecated
    public static boolean detectYellow(Mat src, Consumer<Mat> consumer) throws BusinessException {
        int minH = 26;
        int maxH = 34;
        int minS = 43;
        int maxS = 255;
        int minV = 46;
        int maxV = 255;
        Scalar minValues = new Scalar(minH, minS, minV);
        Scalar maxValues = new Scalar(maxH, maxS, maxV);

        Mat resultMat = doDetect(src, minValues, maxValues);
        return doCheckIfNeedWarning(resultMat, consumer);
    }

    @Deprecated
    private static Mat doDetect(Mat src, Scalar minValues, Scalar maxValues) {
        Mat blurredImage = OpenCVHelper.newMat(src);
        // remove some noise
        Imgproc.blur(src, blurredImage, new Size(7, 7));
        Mat dest = OpenCVHelper.newMat(blurredImage);
        Imgproc.cvtColor(blurredImage, dest, Imgproc.COLOR_BGR2HSV);
        Mat resultMat = OpenCVHelper.newMat(dest);
        Core.inRange(dest, minValues, maxValues, resultMat);
        return resultMat;
    }
    public static boolean doDetect(Mat src,
                                   List<Scalar> minValues,
                                   List<Scalar> maxValues,
                                   Consumer<Mat> consumer) throws BusinessException {
        // remove some noise
        Mat blurredImage = blur(src);
        Mat dest = cvtColor(blurredImage);
        for (int i = 0; i < minValues.size(); i++) {
            Mat resultMat = OpenCVHelper.newMat(dest);
            Scalar minValue = minValues.get(i);
            Scalar maxValue = maxValues.get(i);
            Core.inRange(dest, minValue, maxValue, resultMat);
            boolean b = doCheckIfNeedWarning(resultMat, consumer);
            if (b) {
                return b;
            }
        }
        return false;
    }


    /**
     * remove some noise
     * @param src
     * @return
     */
    public static Mat blur(Mat src){
        Mat blurredImage = OpenCVHelper.newMat(src);
        // remove some noise
        Imgproc.blur(src, blurredImage, new Size(7, 7));
        return blurredImage;
    }

    /**
     * 转为HSV颜色空间
     * @param blurredImage
     * @return
     */
    public static Mat cvtColor(Mat blurredImage){
        Mat dest = OpenCVHelper.newMat(blurredImage);
        Imgproc.cvtColor(blurredImage, dest, Imgproc.COLOR_BGR2HSV);
        return dest;
    }


    private static boolean doCheckIfNeedWarning(Mat src, Consumer<Mat> consumer) throws BusinessException {
        boolean needWarn = false;
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
                if (count > 5) {
                    needWarn = true;
                    break;
                }
            }
            if (count > 5) {
                needWarn = true;
                break;
            }
        }
        if (needWarn) {
            consumer.accept(src);
        }
        return needWarn;
    }


    static{
        addGray(defaultMinValues, defaultMaxValues);
        addDeepRed(defaultMinValues, defaultMaxValues);
        addLightRed(defaultMinValues, defaultMaxValues);
        addYellow(defaultMinValues, defaultMaxValues);
        addOrange(defaultMinValues, defaultMaxValues);
    }

    public static void addGray(List<Scalar> defaultMinValues, List<Scalar> defaultMaxValues){
        int gray_minH = 0;
        int gray_maxH = 180;
        int gray_minS = 0;
        int gray_maxS = 20;
        int gray_minV = 46;
        int gray_maxV = 220;
        Scalar gray_minValue = new Scalar(gray_minH, gray_minS, gray_minV);
        Scalar gray_maxValue = new Scalar(gray_maxH, gray_maxS, gray_maxV);
        defaultMinValues.add(gray_minValue);
        defaultMaxValues.add(gray_maxValue);
    }
    public static void addDeepRed(List<Scalar> defaultMinValues, List<Scalar> defaultMaxValues){
        int deepRed_minH = 156;
        int deepRed_maxH = 180;
        int deepRed_minS = 43;
        int deepRed_maxS = 255;
        int deepRed_minV = 46;
        int deepRed_maxV = 255;
        Scalar deepRed_minValue = new Scalar(deepRed_minH, deepRed_minS, deepRed_minV);
        Scalar deepRed_maxValue = new Scalar(deepRed_maxH, deepRed_maxS, deepRed_maxV);
        defaultMinValues.add(deepRed_minValue);
        defaultMaxValues.add(deepRed_maxValue);
    }
    public static void addLightRed(List<Scalar> defaultMinValues, List<Scalar> defaultMaxValues){
        int lightRed_minH = 0;
        int lightRed_maxH = 10;
        int lightRed_minS = 43;
        int lightRed_maxS = 255;
        int lightRed_minV = 46;
        int lightRed_maxV = 255;
        Scalar lightRed_minValue = new Scalar(lightRed_minH, lightRed_minS, lightRed_minV);
        Scalar lightRed_maxValue = new Scalar(lightRed_maxH, lightRed_maxS, lightRed_maxV);
        defaultMinValues.add(lightRed_minValue);
        defaultMaxValues.add(lightRed_maxValue);
    }
    public static void addYellow(List<Scalar> defaultMinValues, List<Scalar> defaultMaxValues){
        int yellow_minH = 26;
        int yellow_maxH = 34;
        int yellow_minS = 43;
        int yellow_maxS = 255;
        int yellow_minV = 46;
        int yellow_maxV = 255;
        Scalar yellow_minValues = new Scalar(yellow_minH, yellow_minS, yellow_minV);
        Scalar yellow_maxValues = new Scalar(yellow_maxH, yellow_maxS, yellow_maxV);
        defaultMinValues.add(yellow_minValues);
        defaultMaxValues.add(yellow_maxValues);
    }
    public static void addOrange(List<Scalar> defaultMinValues, List<Scalar> defaultMaxValues){
        int orange_minH = 11;
        int orange_maxH = 25;
        int orange_minS = 43;
        int orange_maxS = 255;
        int orange_minV = 46;
        int orange_maxV = 255;
        Scalar orange_minValues = new Scalar(orange_minH, orange_minS, orange_minV);
        Scalar orange_maxValues = new Scalar(orange_maxH, orange_maxS, orange_maxV);
        defaultMinValues.add(orange_minValues);
        defaultMaxValues.add(orange_maxValues);
    }
}

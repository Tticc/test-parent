package com.tester.testerswing.capture;

import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.List;

/**
 * 图片处理比较类
 * @Author 温昌营
 * @Date 2022-7-31 14:17:36
 */
public class ImgComparator {
    private static Mat mat;

    static {
        // 可用
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        // 测试中
//        LibraryLoader.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        mat = new Mat(618, 2000, CvType.CV_8UC1, new Scalar(0));
    }


    public static void main(String[] args) {
        String srcPath = "C:\\Users\\18883\\Desktop\\jacob\\test.png";
        String destPath = "C:\\Users\\18883\\Desktop\\jacob\\test1.png";
        boolean b = doCompareIfTheSame(srcPath, destPath);
        System.out.println("比较结果：" + b);
    }

    public static boolean doCompareIfTheSame(String hisImgPath, String targetImgPath) {
        String srcPath = hisImgPath;
        String destPath = targetImgPath;
        Mat src = Imgcodecs.imread(srcPath);
        Mat dest = Imgcodecs.imread(destPath);
        Mat srcHist = processImgForCompare(src);
        Mat destHist = processImgForCompare(dest);
        double res = compareImg(srcHist, destHist, Imgproc.HISTCMP_CORREL);
        if (res < 1.0) {
            System.out.println("比较结果：" + res);
            return false;
        }
        return true;
    }


    public static Mat processImgForCompare(Mat mat) {
        MatOfFloat mfloat = new MatOfFloat(0, 256);
        MatOfInt mint = new MatOfInt(255);

        List<Mat> matList = new ArrayList<Mat>();
        matList.add(mat);
        Mat hist = new Mat();
        //直方图计算 详见 https://blog.csdn.net/ren365880/article/details/103957456
        Imgproc.calcHist(matList, new MatOfInt(0), new Mat(), hist, mint, mfloat);
        //图片归一化 详见 https://blog.csdn.net/ren365880/article/details/103923813
        Core.normalize(hist, hist, 1, hist.rows(), Core.NORM_MINMAX, -1, new Mat());
        return hist;
    }


    /*
     * 函数cv :: compareHist使用指定的方法比较两个稠密或两个稀疏直方图。 该函数返回（d（H_1，H_2））。
     * 虽然此功能可以很好地处理1、2、3维密集直方图，但它可能不适用于高维稀疏直方图。 在这种直方图中，由于混叠和采样问题，非零直方图块的坐标可能会略有偏移。
     * 要比较此类直方图或加权点的更一般的稀疏配置，请考虑使用#EMD函数。
     * @param H1首先比较直方图。
     * @param H2第二个比较的直方图，大小与H1相同。
     * @param方法比较方法，请参见#HistCompMethods
     * Imgproc.HISTCMP_CORREL = 0, 相关性比较
     * Imgproc.HISTCMP_CHISQR = 1, 卡方比较
     * Imgproc.HISTCMP_INTERSECT = 2, 十字交叉性
     * Imgproc.HISTCMP_BHATTACHARYYA = 3, 巴氏距离
     * Imgproc.HISTCMP_HELLINGER = HISTCMP_BHATTACHARYYA,
     * Imgproc.HISTCMP_CHISQR_ALT = 4,
     * Imgproc.HISTCMP_KL_DIV = 5;
     */
    public static double compareImg(Mat H1, Mat H2, int method) {
        return Imgproc.compareHist(H1, H2, method);
    }

}

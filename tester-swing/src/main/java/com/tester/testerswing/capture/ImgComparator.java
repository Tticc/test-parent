package com.tester.testerswing.capture;

import com.tester.testercv.utils.opencv.OpenCVHelper;
import com.tester.testerswing.boot.AccountInfo;
import com.tester.testerswing.loadlibrary.LibraryLoader;
import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 图片处理比较类
 *
 * @Author 温昌营
 * @Date 2022-7-31 14:17:36
 */
public class ImgComparator {
    private static Mat mat;

    static {
        // 可用
//        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        // 可用
        LibraryLoader.loadLibrary(Core.NATIVE_LIBRARY_NAME);
//        LibraryLoader.loadLibraryByAbsolutePath(Core.NATIVE_LIBRARY_NAME);
        mat = new Mat(618, 2000, CvType.CV_8UC1, new Scalar(0));
    }


    public static void main(String[] args) {
        String srcPath = "C:\\Users\\18883\\Desktop\\jacob\\test.png";
        String destPath = "C:\\Users\\18883\\Desktop\\jacob\\test1.png";
        Mat src = OpenCVHelper.readImg2Mat(srcPath);
        Mat dest = OpenCVHelper.readImg2Mat(destPath);
        boolean b = doCompareIfTheSame(src, dest, null);
        System.out.println("比较的结果：" + b);
    }

    public static boolean doCompareIfTheSame(Mat src, Mat dest, AccountInfo accountInfo) {
        // 确保图像大小一致
        Imgproc.resize(dest, dest, new Size(src.cols(), src.rows()));
        Mat srcHist = processImgForCompare(src);
        Mat destHist = processImgForCompare(dest);
        double res = Imgproc.compareHist(srcHist, destHist, Imgproc.HISTCMP_INTERSECT);
        if (res < 0.99) {
            int i = accountInfo.getRefreshCount().get();
            System.out.println(accountInfo.getAccount()+"_"+(i+1)+"比较结果：" + res);
            // 记录图像
            int count = accountInfo.getRefreshCount().incrementAndGet();
            String basePath = ImgBoot.getBasePath(accountInfo.getAccount());
            OpenCVHelper.saveMat2Img(basePath, destHist, ImgBoot.getHisImgName(count, "number"));
            OpenCVHelper.saveMat2Img(basePath, srcHist, ImgBoot.getHisImgName(count, "number_his"));
            return false;
        }
        return true;
    }

    public static Mat processImgForCompare(Mat mat) {
        System.out.println("mat = " + mat.channels());
        if (mat.channels() == 3) {
            Imgproc.cvtColor(mat, mat, Imgproc.COLOR_BGR2GRAY);
        } else if (mat.channels() == 4) {
            Imgproc.cvtColor(mat, mat, Imgproc.COLOR_BGRA2GRAY);
        }
        MatOfFloat mfloat = new MatOfFloat(0, 256);
        MatOfInt mint = new MatOfInt(255);

        List<Mat> matList = new ArrayList<Mat>();
        matList.add(mat);
        Mat hist = new Mat();
        //直方图计算 详见 https://blog.csdn.net/ren365880/article/details/103957456
        Imgproc.calcHist(matList, new MatOfInt(0), new Mat(), hist, mint, mfloat);
        //图片归一化 详见 https://blog.csdn.net/ren365880/article/details/103923813
        Core.normalize(hist, hist, 0, 1, Core.NORM_MINMAX);
//        Core.normalize(hist, hist, 1, 0, Core.NORM_L1);
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

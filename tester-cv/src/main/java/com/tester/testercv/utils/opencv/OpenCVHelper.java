package com.tester.testercv.utils.opencv;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

/**
 * @Author 温昌营
 * @Date 2022-8-8 15:15:12
 */
public class OpenCVHelper {

    static {
        OpenCVBaseHelper.init();
    }

    /**
     * 将图片读取为mat
     *
     * @Date 15:24 2022/8/8
     * @Author 温昌营
     **/
    public static Mat readImg2Mat(String imgPath) {
        return Imgcodecs.imread(imgPath);
    }

    /**
     * 将 BufferedImage 转换为 Mat。 默认 UNCHANGED
     *
     * @Date 16:26 2022/8/10
     * @Author 温昌营
     **/
    public static Mat BufferedImage2Mat(BufferedImage image) throws IOException {
        return BufferedImage2Mat(image, Imgcodecs.IMREAD_UNCHANGED);
    }

    /**
     * 将 BufferedImage 转换为 Mat。 默认彩色
     * type： org.opencv.imgcodecs.Imgcodecs
     *
     * @Date 16:26 2022/8/10
     * @Author 温昌营
     **/
    public static Mat BufferedImage2Mat(BufferedImage image, int type) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ImageIO.write(image, "png", byteArrayOutputStream);
        byteArrayOutputStream.flush();
        return Imgcodecs.imdecode(new MatOfByte(byteArrayOutputStream.toByteArray()), type);

    }

    /**
     * mat 保存为图片
     *
     * @Date 15:26 2022/8/9
     * @Author 温昌营
     **/
    public static void saveMat2Img(String folderPath, Mat mat, String name) {
        File file1 = new File(folderPath);
        if (!file1.exists())
            file1.mkdirs();
        Imgcodecs.imwrite(folderPath + File.separator + name, mat);
    }

    /**
     * 创建一个同等大小的空mat
     *
     * @Date 15:24 2022/8/8
     * @Author 温昌营
     **/
    public static Mat newMat(Mat srcMat) {
        return new Mat(srcMat.size(), srcMat.type());
    }

    /**
     * @param rows height y轴方位
     * @param cols width x轴方位
     * @return
     */
    public static Mat newMat(int rows, int cols) {
        return new Mat(rows, cols, CvType.CV_8UC3);
    }

    /**
     * 展示 mat
     *
     * @Date 15:29 2022/8/8
     * @Author 温昌营
     **/
    public static void showImg(Mat mat, String name) {
        showImg(mat, name, 2000);
    }

    public static void showImg(Mat mat, String name, int delay) {
        HighGui.imshow(name, mat);
        HighGui.waitKey(delay);
        System.exit(0);
    }

    /**
     * 播放图片
     *
     * @Date 10:20 2022/8/9
     * @Author 温昌营
     **/
    public static void playImg(List<Mat> matList, boolean autoExit) {
        playImg(matList, "default_name", 500, autoExit);
    }

    public static void playImg(List<Mat> matList, String name) {
        playImg(matList, name, 500);
    }

    public static void playImg(List<Mat> matList, String name, int delay) {
        playImg(matList, name, delay, true);
    }

    public static void playImg(List<Mat> matList, String name, int delay, boolean autoExit) {
        Iterator<Mat> iterator = matList.iterator();
        while (iterator.hasNext()) {
            Mat next = iterator.next();
            HighGui.imshow(name, next);
            HighGui.waitKey(delay);
        }
        if (autoExit) {
            System.exit(0);
        }
    }

}

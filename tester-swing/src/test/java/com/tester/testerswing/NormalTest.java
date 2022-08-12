package com.tester.testerswing;

import com.tester.testercv.utils.opencv.OpenCVBaseHelper;
import com.tester.testercv.utils.opencv.OpenCVHelper;
import com.tester.testerswing.capture.PointInfoDTO;
import com.tester.testerswing.robot.RobotHelper;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * @Author 温昌营
 * @Date 2022-8-3 09:18:45
 */
public class NormalTest {
    static {
        OpenCVBaseHelper.init();
    }

    public static void main(String[] args) throws Exception {
//        Point point = RobotHelper.getPoint();
//        for (int i = 0; i < 15; i++) {
//            int x = GaussianHelper.getGaussianInt(1438, 1547);
//            int y = GaussianHelper.getGaussianInt(34, 51);
//            RobotHelper.move(x, y);
//            RobotHelper.delay(1000);
//            RobotHelper.move((int)point.getX(),(int)point.getY());
//            RobotHelper.delay(1000);
//        }


//        captureImgAnd2Mat();

        RobotHelper.move(264, 203);


    }

    public static void captureImgAnd2Mat() throws IOException {
        PointInfoDTO st = new PointInfoDTO().setX(0).setY(0);
        PointInfoDTO ed = new PointInfoDTO().setX(410).setY(410);
        BufferedImage image = RobotHelper.createScreenCapture(st, ed);
        Mat mat1 = OpenCVHelper.BufferedImage2Mat(image, Imgcodecs.IMREAD_GRAYSCALE);
        OpenCVHelper.showImg(mat1, "name", 10000);
    }
}

package com.tester.testerswing.capture;

import org.opencv.core.*;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

/**
 * @Author 温昌营
 * @Date
 */
public class OpenCVTest {

    public static final String BASE_PATH = Img_GenerateImageTest.BASE_PATH;

    private static final int modelHeight = 100;
    private static final int modelWidth = 200;

    public static final Scalar DEFAULT_GRAY_POINT = new Scalar(255);
    private static Mat mat;



    static{
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        mat = new Mat(618, 2000, CvType.CV_8UC1, new Scalar(0));
    }


    public static void main(String[] args) {
        String path = "C:\\Users\\18883\\Desktop\\jacob\\test.png";
        OpenCVTest openCVTest = new OpenCVTest();
        openCVTest.topHat(path);
    }

    public void test_opencv(){
//        demoPrint();
//        readImg();
//        writeBlackImg();
//        topHat();
//        blackHat();
        Mat m = mat;
        showImg(m);
    }


    private void imgproc_text(Mat mat, Point point, String text){
        Point tempPoint = new Point(point.x,point.y);
        Imgproc.putText(mat,text,tempPoint,Imgproc.FONT_HERSHEY_SIMPLEX,1,DEFAULT_GRAY_POINT);
    }

    private void imgproc_ellipse(Mat mat, Point point){
        Imgproc.ellipse(mat,point,new Size((double)modelWidth/2,(double) modelHeight/2),360,0,360,DEFAULT_GRAY_POINT);
    }


    public void topHat(String path){
        Mat src = Imgcodecs.imread(path);
        Mat image = new Mat(src.size(), src.type());
        Mat kernel = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(15, 15), new Point(-1, -1));
        Imgproc.morphologyEx(src, image, Imgproc.MORPH_TOPHAT, kernel);
        showImg(image,"图像形态学 顶帽 (原图像与开操作之间的差值图像)" );
    }
    private void blackHat(){
        Mat src = Imgcodecs.imread(BASE_PATH+"xxx.png");
        Mat image = new Mat(src.size(), src.type());
        Mat kernel = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(15, 15), new Point(-1, -1));
        Imgproc.morphologyEx(src, image, Imgproc.MORPH_BLACKHAT, kernel);
        showImg(image,"图像形态学 黑帽 (原图像与开操作之间的差值图像)" );
    }

    private void writeBlackImg(){
        Mat mat = new Mat(250, 300, CvType.CV_8UC1, new Scalar(0));
        Imgproc.line(mat,new Point(0,0),new Point(3,250),new Scalar(255));
        Imgproc.line(mat,new Point(3,250),new Point(69,80),new Scalar(255));
        Imgproc.line(mat,new Point(69,80),new Point(69,200),new Scalar(255));
        Imgproc.line(mat,new Point(69,200),new Point(150,200),new Scalar(255));
//        Imgcodecs.imwrite(BASE_PATH+"writeBlackImg.png",mat);
        showImg(mat,"writeBlackImg");
    }

    private void readImg(){
        Mat img = Imgcodecs.imread(BASE_PATH+"xxx.png",Imgcodecs.IMREAD_GRAYSCALE);
        System.out.println(img.channels());
        long ll = img.nativeObj;
        System.out.println(ll);
        System.out.println(img.height());
        System.out.println(img.width());
//        System.out.println("OpenCV Mat data:\n" + img.dump());
        Imgcodecs.imwrite(BASE_PATH+"BASE_WRITE.png",img);
        reRangeImg(img);
        Mat mat = img.rowRange(0, 240).colRange(0, 340);
        Imgproc.line(mat,new Point(0,0),new Point(340,240),new Scalar(255));
        writeImg("BASE_WRITE_part",mat);
    }

    private void reRangeImg(Mat mat){
        Mat newMat = mat.rowRange(0, mat.height()/2).colRange(0, mat.width()/2);
        showImg(newMat);
    }





    private void demoPrint(){
        System.out.println("Welcome to OpenCV " + Core.VERSION);
        Mat m = new Mat(50, 100, CvType.CV_8UC3, new Scalar(10));
        System.out.println("OpenCV Mat: " + m);
        Mat mr1 = m.row(1);
        mr1.setTo(new Scalar(new double[]{21,221,13}));
        Mat mr11 = m.rowRange(2,3);
        mr11.setTo(new Scalar(255));
        Mat mc5 = m.col(5);
        mc5.setTo(new Scalar(255));
        Mat mc51 = m.colRange(6,7);
        mc51.setTo(new Scalar(255));
        System.out.println("OpenCV Mat data:\n" + m.dump());
        writeImg("BASE_WRITE_m1",m);
    }

    private void showImg(Mat mat,String ...name){
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
    private void writeImg(String name,Mat mat){
        Imgcodecs.imwrite(BASE_PATH+"writeImg_"+name+".png",mat);
    }
}

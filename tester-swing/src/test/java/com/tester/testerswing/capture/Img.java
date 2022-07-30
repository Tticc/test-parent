package com.tester.testerswing.capture;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
public class Img {
    //图片存放目录
    public static final String BUFFER_IMAGE_AREA="C:\\Users\\18883\\Desktop\\jacob\\";
    public static void main(String[] args) throws IOException, AWTException {
        //{左上角的横坐标(x),左上角的纵坐标(y),右下角的横坐标(x),右下角的纵坐标(y)}
        int[] area={40,276,86,581};
        long l = System.currentTimeMillis();
        createScreen(area,"test.png");
        long l2 = System.currentTimeMillis();
        System.out.println("(l2-l) = " + (l2-l));
    }
    /**
     *
     * @param area 截图区域，即给定的截图范围：{左上角的横坐标,左上角的纵坐标,右下角的横坐标,右下角的纵坐标}
     * @param imageName 给截取的图片命名
     * @return
     * @throws AWTException
     * @throws IOException
     */
    public static String createScreen(int[] area,String imageName)throws AWTException, IOException {
        Dimension screen = null;    //电脑屏幕大小
        Rectangle screenRect = null;//截图的宽高
        BufferedImage image = null; //暂存图片的缓存
        Robot robot = null;         //负责截屏的操作者
        screen = Toolkit.getDefaultToolkit().getScreenSize();
        //截图尺寸
        screen.height=area[3]-area[1];
        screen.width=area[2]-area[0];
        screenRect = new Rectangle(screen);
        //左上角得坐标
        screenRect.x=area[0];
        screenRect.y=area[1];
        robot = new Robot();
        //将得到的屏幕信息存放在缓存里面
        image = robot.createScreenCapture(screenRect);
        //将缓存里面的屏幕信息以图片的格式存在制定的磁盘位置
        ImageIO.write(image, getFileSuffix(imageName), new File(BUFFER_IMAGE_AREA, imageName));
        return BUFFER_IMAGE_AREA+imageName;
    }
    public static String getFileSuffix(String path){
        String[] split = path.split("\\.");
        return split[split.length-1];
    }
}
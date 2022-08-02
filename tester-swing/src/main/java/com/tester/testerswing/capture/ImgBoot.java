package com.tester.testerswing.capture;

import com.tester.base.dto.exception.BusinessException;
import com.tester.testerswing.boot.AccountInfo;
import com.tester.testerswing.swing.EasyScript;
import com.tester.testerswing.voice.BeepSoundProcessor;
import com.tester.testerswing.voice.BeepSoundTaskDTO;
import org.springframework.util.CollectionUtils;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * 图片处理启动类
 * @Date 9:54 2022/8/2
 * @Author 温昌营
 **/
public class ImgBoot {


    public static final String HIS_IMG_NAME = "hisImg%d.png";

    //图片存放目录
    public static final String BUFFER_IMAGE_AREA = "C:\\Users\\18883\\Desktop\\captureImg\\eve\\";

    /**
     * 启动
     *
     * @Date 9:50 2022/8/2
     * @Author 温昌营
     **/
    public static void start(List<AccountInfo> accounts) throws BusinessException {
        if (CollectionUtils.isEmpty(accounts)) {
            throw new BusinessException(500L, "启动失败，账号为空！");
        }
        for (AccountInfo account : accounts) {
            try {
                deleteDir(getBasePath(account.getAccount()));
                refreshHisImg(account);
            } catch (Exception e) {
                throw new BusinessException(500, "创建初始图像失败", e);
            }
        }
    }

    /**
     * 比较图片并通知后续处理
     *
     * @Date 9:49 2022/8/2
     * @Author 温昌营
     **/
    public static void checkIfNormal(AccountInfo accountInfo) throws Exception {
        boolean normal = createAndCompareImg(accountInfo, "test_temp.png");
        if (!normal && accountInfo.isNeedWarn()) {
            EasyScript.getFrame().setExtendedState(JFrame.NORMAL);
            EasyScript.getFrame().toFront();
            BeepSoundTaskDTO beepSoundTaskDTO = BeepSoundProcessor.generateTask(accountInfo.getWarnMsg(), 100, 2);
            BeepSoundProcessor.putTask(beepSoundTaskDTO);
        }
    }


    /**
     * 刷新原始图片
     *
     * @Date 9:49 2022/8/2
     * @Author 温昌营
     **/
    public static void refreshHisImg(AccountInfo account) throws BusinessException {
        try {
            int count = account.getRefreshCount().incrementAndGet();
            String imgPath = createScreen(account.getSt(), account.getEd(), account.getAccount(), getHisImgName(count));
        } catch (Exception e) {
            throw new BusinessException(500, "刷新初始图像失败", e);
        }
    }


    private static boolean deleteDir(String path) {
        File dir = new File(path);
        if (dir.isDirectory()) {
            String[] children = dir.list();
            //递归删除目录中的子目录下
            for (String child : children) {
                deleteDir(path + "/" + child);
            }
        }
        // 目录此时为空，可以删除
        return dir.delete();
    }

    private static boolean createAndCompareImg(AccountInfo accountInfo, String imgName) throws Exception {
        String newImgPath = createScreen(accountInfo.getSt(), accountInfo.getEd(), accountInfo.getAccount(), imgName);
        String hisImgPath = getHisImgPath(accountInfo.getAccount(), accountInfo.getRefreshCount().get());
        return ImgComparator.doCompareIfTheSame(hisImgPath, newImgPath);
    }

    private static String createScreen(PointInfoDTO st, PointInfoDTO ed, String folderName, String imgName) throws AWTException, IOException {
        //{左上角的横坐标(x),左上角的纵坐标(y),右下角的横坐标(x),右下角的纵坐标(y)}
        int[] area = {st.getX(), st.getY(), ed.getX(), ed.getY()};
        String imgPath = doCreateScreen(area, getBasePath(folderName), imgName);
        return imgPath;
    }

    /**
     * @param area      截图区域，即给定的截图范围：{左上角的横坐标,左上角的纵坐标,右下角的横坐标,右下角的纵坐标}
     * @param imageName 给截取的图片命名
     * @return
     * @throws AWTException
     * @throws IOException
     */
    private static String doCreateScreen(int[] area, String basePath, String imageName) throws AWTException, IOException {
        Dimension screen = null;    //电脑屏幕大小
        Rectangle screenRect = null;//截图的宽高
        BufferedImage image = null; //暂存图片的缓存
        Robot robot = null;         //负责截屏的操作者
        screen = Toolkit.getDefaultToolkit().getScreenSize();
        //截图尺寸
        screen.height = area[3] - area[1];
        screen.width = area[2] - area[0];
        screenRect = new Rectangle(screen);
        //左上角得坐标
        screenRect.x = area[0];
        screenRect.y = area[1];
        robot = new Robot();
        //将得到的屏幕信息存放在缓存里面
        image = robot.createScreenCapture(screenRect);
        //将缓存里面的屏幕信息以图片的格式存在制定的磁盘位置
        File dir = new File(basePath);
        if (!dir.exists()) {
            if (dir.mkdirs()) {
                System.out.println("dir create success. --" + basePath);
            } else {
                System.out.println("dir create failed! --" + basePath);
            }
        }
        ImageIO.write(image, getFileSuffix(imageName), new File(basePath, imageName));
        return basePath + imageName;
    }

    private static String getFileSuffix(String path) {
        String[] split = path.split("\\.");
        return split[split.length - 1];
    }

    private static String getBasePath(String folderName) {
        return BUFFER_IMAGE_AREA + folderName + File.separator;
    }

    private static String getHisImgPath(String folderName, int count) {
        return BUFFER_IMAGE_AREA + folderName + File.separator + getHisImgName(count);
    }

    private static String getHisImgName(int count) {
        return String.format(HIS_IMG_NAME, count);
    }
}
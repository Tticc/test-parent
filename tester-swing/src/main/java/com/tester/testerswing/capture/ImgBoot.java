package com.tester.testerswing.capture;

import com.tester.base.dto.exception.BusinessException;
import com.tester.testercv.utils.detectColor.ColorDetectTool;
import com.tester.testercv.utils.opencv.OpenCVHelper;
import com.tester.testerswing.boot.AccountInfo;
import com.tester.testerswing.robot.RobotHelper;
import com.tester.testerswing.swing.EasyScript;
import com.tester.testerswing.voice.BeepSoundProcessor;
import com.tester.testerswing.voice.BeepSoundTaskDTO;
import org.opencv.core.Mat;
import org.springframework.util.CollectionUtils;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.Date;
import java.util.List;
import java.util.function.Consumer;

/**
 * 图片处理启动类
 *
 * @Date 9:54 2022/8/2
 * @Author 温昌营
 **/
public class ImgBoot {


    public static final String HIS_IMG_NAME = "hisImg%d.png";

    //图片存放目录
    public static final String BUFFER_IMAGE_AREA = "C:\\Users\\Admin\\Desktop\\deliv\\";

    public static long lastActiveTime = 0;
    // 至少每隔1分钟弹起窗口
    public static long activeInterval = 60 * 1000;

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
    public static void checkNumber(AccountInfo accountInfo) throws Exception {
        if (!accountInfo.isNeedWarn()) {
            // 无需告警，直接返回
            return;
        }
        boolean normal = createAndCompareImg(accountInfo, "test_temp.png");
        if (!normal) {
            sendVoice(accountInfo.getInfoMsg());
        }


    }

    public static void checkIfNeedWarning(AccountInfo accountInfo) throws Exception {
        if (!accountInfo.isNeedWarn()) {
            // 无需告警，直接返回
            return;
        }
        boolean warning = doCheckIfNeedWarning(accountInfo);
        if (warning) {
            sendVoice(accountInfo.getWarnMsg());
        }

    }

    public static void sendVoice(String msg) {
        long time = new Date().getTime();
        if (time > lastActiveTime + activeInterval) {
            lastActiveTime = time;
            JFrame frame = EasyScript.getFrame();
            frame.setExtendedState(JFrame.NORMAL);
            frame.toFront();
            Point location = frame.getLocation();
            // 鼠标移动到指定位置
            RobotHelper.move((int) location.getX() + 103, (int) location.getY() + 108);
        }
        BeepSoundTaskDTO beepSoundTaskDTO = BeepSoundProcessor.generateTask(msg, 100, 2);
        BeepSoundProcessor.putTask(beepSoundTaskDTO);
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
            String imgPath = createScreenAndSave(account.getSt(), account.getEd(), account.getAccount(), getHisImgName(count));
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

    private static boolean doCheckIfNeedWarning(AccountInfo accountInfo) throws Exception {
        String imgName = "warning_tmp.png";
        String warningImgPath = createScreenAndSave(accountInfo.getRedSt(), accountInfo.getRedEd(), accountInfo.getAccount(), imgName);
        Mat src = OpenCVHelper.readImgToMat(warningImgPath);
        if (src == null) {
            System.out.println("异常，无法获取警告mat");
            return true;
        }
        String basePath = getBasePath(accountInfo.getAccount());
        return ColorDetectTool.detectGray(src, (targetMat) -> {
            new Thread(() -> {
                OpenCVHelper.saveImgFromMat(basePath, targetMat, "warning.png");
            }).start();
        });
    }

    private static boolean createAndCompareImg(AccountInfo accountInfo, String imgName) throws Exception {
        String newImgPath = createScreenAndSave(accountInfo.getSt(), accountInfo.getEd(), accountInfo.getAccount(), imgName);
        String hisImgPath = getHisImgPath(accountInfo.getAccount(), accountInfo.getRefreshCount().get());
        return ImgComparator.doCompareIfTheSame(hisImgPath, newImgPath);
    }

    private static String createScreenAndSave(PointInfoDTO st, PointInfoDTO ed, String folderName, String imgName) throws AWTException {
        String basePath = getBasePath(folderName);
        doCreateScreen(st, ed, (bufferedImage) -> {
            //将缓存里面的屏幕信息以图片的格式存在制定的磁盘位置
            File dir = new File(basePath);
            if (!dir.exists()) {
                if (dir.mkdirs()) {
                    System.out.println("dir create success. --" + basePath);
                } else {
                    System.out.println("dir create failed! --" + basePath);
                }
            }
            try {
                ImageIO.write(bufferedImage, getFileSuffix(imgName), new File(basePath, imgName));
            } catch (IOException e) {
                System.out.println("异常，保存图片失败" + basePath);
            }
        });
        return basePath + imgName;
    }

    /**
     * 截图，并处理
     *
     * @Date 16:01 2022/8/10
     * @Author 温昌营
     **/
    private static void doCreateScreen(PointInfoDTO st, PointInfoDTO ed, Consumer<BufferedImage> consumer) throws AWTException {
        BufferedImage image = RobotHelper.createScreenCapture(st, ed);
        consumer.accept(image);
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

    /**
     * 将 BufferedImage 转换为 Mat
     *
     * @Date 16:26 2022/8/10
     * @Author 温昌营
     **/
    public static Mat bufferedImageToMat(BufferedImage bi) {
        Mat mat = OpenCVHelper.newMat(bi.getHeight(), bi.getWidth());
        int[] data = ((DataBufferInt) bi.getRaster().getDataBuffer()).getData();
        ByteBuffer byteBuffer = ByteBuffer.allocate(data.length * 4);
        IntBuffer intBuffer = byteBuffer.asIntBuffer();
        intBuffer.put(data);
        mat.put(0, 0, byteBuffer.array());
        return mat;
    }
}
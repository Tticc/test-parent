package com.tester.testerswing.capture;

import com.tester.base.dto.exception.BusinessException;
import com.tester.testercommon.util.DateUtil;
import com.tester.testercv.utils.detectColor.ColorDetectTool;
import com.tester.testercv.utils.opencv.OpenCVHelper;
import com.tester.testerswing.boot.AccountInfo;
import com.tester.testerswing.robot.RobotHelper;
import com.tester.testerswing.swing.EasyScript;
import com.tester.testerswing.voice.BeepSoundProcessor;
import com.tester.testerswing.voice.dto.BeepSoundTaskDTO;
import com.tester.testerswing.voice.dto.QywxMessageTaskDTO;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.imgcodecs.Imgcodecs;
import org.springframework.util.CollectionUtils;

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
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

/**
 * 图片处理启动类
 *
 * @Date 9:54 2022/8/2
 * @Author 温昌营
 **/
public class ImgBoot {


    public static final String HIS_IMG_NAME = "%s_hisImg_%s.png";

    //图片存放目录
    public static final String BUFFER_IMAGE_AREA = "C:\\Users\\Admin\\Desktop\\deliv\\";

    public static long lastActiveTime = 0;
    // 至少每隔1分钟弹起窗口
    public static long activeInterval = 60 * 1000;

    // 有白时，至少每隔1分钟自动跑路一次
    public static long auto_activeInterval = 5*60 * 1000;

    /**
     * 启动
     * 1. 删除原有目录。
     * 2. 初始化图片
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
     * 通过图片比较本地数量变化，并通知后续处理
     * <br/>已废弃
     * @Date 9:49 2022/8/2
     * @Author 温昌营
     **/
    @Deprecated
    public static void checkNumber(AccountInfo accountInfo, int imgType) throws Exception {
        if (!accountInfo.isNeedInfo()) {
            // 无需通知，直接返回
            return;
        }
        Mat newMat = createScreenAnd2Mat(accountInfo.getSt(), accountInfo.getEd(), imgType, accountInfo.getAccount());
        Mat hisMat = null;
        if (accountInfo.getHisMat() != null) {
            hisMat = accountInfo.getHisMat();
        } else {
            hisMat = OpenCVHelper.readImg2Mat(getHisImgPath(accountInfo.getAccount(), accountInfo.getRefreshCount().get(), "number"));
        }
        boolean normal = ImgComparator.doCompareIfTheSame(hisMat, newMat);
        if (!normal) {
            // 提示通知
            sendVoice(accountInfo.getInfoMsg(), false);
            // 最多提醒1次
            AtomicInteger noticeTime = accountInfo.getNoticeTime();
            int andIncrement = noticeTime.incrementAndGet();
            if(andIncrement >= 1){
                noticeTime.getAndSet(0);
                refreshHisImg(accountInfo);
//                sendVoice("已刷新", false);
            }
        }


    }
    /**
     * 通过图片检查本地进红状态。后续通知或直接跑路处理
     * @Date 9:49 2022/8/11
     * @Author 温昌营
     **/
    public static void checkIfNeedWarning(AccountInfo accountInfo, int imgType) throws Exception {
        if (!accountInfo.isNeedWarn()) {
            // 无需告警，直接返回
            return;
        }
        boolean warning = false;
        Mat src = createScreenAnd2Mat(accountInfo.getRedSt(), accountInfo.getRedEd(), imgType, accountInfo.getAccount());
        if (src == null) {
            System.out.println("异常，无法获取警告mat");
            warning = true;
        }
        if (warning || doCheckIfNeedWarning(src, accountInfo)) {
            long time = new Date().getTime();
            if(accountInfo.isIfAuto()){
                if(time > accountInfo.getLastQuickRunTime().getTime() + auto_activeInterval) {
                    accountInfo.setLastQuickRunTime(new Date());
                    accountInfo.getConsumer().accept(null);

                    // 发消息 - 企业微信消息不可靠，取消 2023-9-21 14:58:58
//                    QywxMessageTaskDTO qywxMessageTaskDTO = new QywxMessageTaskDTO(accountInfo.getAccount()+"_"+DateUtil.dateFormat(new Date()));
//                    BeepSoundProcessor.putTask(qywxMessageTaskDTO);
                }
            }else {
                sendVoice(accountInfo.getWarnMsg(), false);
            }
        }

    }

    private static boolean doCheckIfNeedWarning_ori(Mat src, AccountInfo accountInfo) throws BusinessException {
        String basePath = getBasePath(accountInfo.getAccount());
        return ColorDetectTool.detectGray(src, (targetMat) -> OpenCVHelper.saveMat2Img(basePath, targetMat, getCountPrefix(accountInfo.getRefreshCount().get()) + "_warning_gray.png"))
                || ColorDetectTool.detectRed(src, (targetMat) -> OpenCVHelper.saveMat2Img(basePath, targetMat, getCountPrefix(accountInfo.getRefreshCount().get()) + "warning_red.png"))
                || ColorDetectTool.detectYellow(src, (targetMat) -> OpenCVHelper.saveMat2Img(basePath, targetMat, getCountPrefix(accountInfo.getRefreshCount().get()) + "warning_yellow.png"));
    }

    /**
     * 判断是否本地进红
     * @param src
     * @param accountInfo
     * @return
     * @throws BusinessException
     */
    private static boolean doCheckIfNeedWarning(Mat src, AccountInfo accountInfo) throws BusinessException {
        String basePath = getBasePath(accountInfo.getAccount());
        return ColorDetectTool.doDetect(src,ColorDetectTool.defaultMinValues,ColorDetectTool.defaultMaxValues,(targetMat) -> OpenCVHelper.saveMat2Img(basePath, targetMat, getCountPrefix(accountInfo.getRefreshCount().get()) + "_warning.png"));
    }

    public static void sendVoice(String msg, boolean needFront) {
        long time = new Date().getTime();
        if (needFront && time > lastActiveTime + activeInterval) {
            lastActiveTime = time;
            JFrame frame = EasyScript.getFrame();
            frame.setExtendedState(JFrame.NORMAL);
            frame.toFront();
            // 鼠标移动到指定位置
//            Point location = frame.getLocation();
//            RobotHelper.move((int) location.getX() + 103, (int) location.getY() + 108);
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
            Mat numberCheckImage = createScreenAnd2Mat(account.getSt(), account.getEd(), Imgcodecs.IMREAD_GRAYSCALE, account.getAccount());
            account.setHisMat(numberCheckImage);
            Mat warnCheckImage = createScreenAnd2Mat(account.getRedSt(), account.getRedEd(), Imgcodecs.IMREAD_UNCHANGED, account.getAccount());
            //将缓存里面的屏幕信息以图片的格式存在制定的磁盘位置
            String basePath = getBasePath(account.getAccount());
            File dir = new File(basePath);
            if (!dir.exists()) {
                if (dir.mkdirs()) {
                    System.out.println("dir create success. --" + basePath);
                } else {
                    System.out.println("dir create failed! --" + basePath);
                }
            }
            OpenCVHelper.saveMat2Img(basePath, numberCheckImage, getHisImgName(count, "number"));
            OpenCVHelper.saveMat2Img(basePath, warnCheckImage, getHisImgName(count, "warn"));
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


    /**
     * 截屏并转换为mat
     *
     * @Date 17:02 2022/8/11
     * @Author 温昌营
     **/
    private static Mat createScreenAnd2Mat(PointInfoDTO st, PointInfoDTO ed, int imgType, String account) throws AWTException {
        AtomicReference<Mat> imgRef = new AtomicReference<>();
        doCreateScreen(st, ed, (bufferedImage) -> {
            try {
                imgRef.set(OpenCVHelper.BufferedImage2Mat(bufferedImage, imgType));
            } catch (IOException e) {
                System.out.println("异常，截图转换失败. account:" + account);
            }
        });
        return imgRef.get();
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

    private static String getHisImgPath(String folderName, int count, String type) {
        return BUFFER_IMAGE_AREA + folderName + File.separator + getHisImgName(count, type);
    }

    private static String getHisImgName(int count, String type) {
        return String.format(HIS_IMG_NAME, getCountPrefix(count), type);
    }

    public static String getCountPrefix(int count) {
        String countStr = "";
        if (count < 10) {
            countStr = "00" + count;
        } else if (count < 100) {
            countStr = "0" + count;
        } else {
            countStr = "" + count;
        }
        return countStr;
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
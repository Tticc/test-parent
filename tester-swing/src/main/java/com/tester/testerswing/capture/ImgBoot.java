package com.tester.testerswing.capture;

import com.tester.base.dto.exception.BusinessException;
import com.tester.testercommon.util.DateUtil;
import com.tester.testercv.utils.detectColor.ColorDetectTool;
import com.tester.testercv.utils.opencv.OpenCVHelper;
import com.tester.testerswing.boot.AccountInfo;
import com.tester.testerswing.mail.SendMailText;
import com.tester.testerswing.robot.RobotHelper;
import com.tester.testerswing.swing.EasyScript;
import com.tester.testerswing.voice.BeepSoundProcessor;
import com.tester.testerswing.voice.dto.BeepSoundTaskDTO;
import lombok.extern.slf4j.Slf4j;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.springframework.util.CollectionUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.File;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

/**
 * 图片处理启动类
 *
 * @Date 9:54 2022/8/2
 * @Author 温昌营
 **/
@Slf4j
public class ImgBoot {


    public static final String HIS_IMG_NAME = "%s_hisImg_%s.png";

    //图片存放目录
    public static final String BUFFER_IMAGE_AREA = "C:\\Users\\18883\\Desktop\\captureImg\\eve\\";

    public static long lastActiveTime = 0;
    // 至少每隔1分钟弹起窗口
    public static long activeInterval = 60 * 1000;

    // 有白时，至少每隔5分钟自动跑路一次
    public static long auto_activeInterval = 5 * 60 * 1000;

    // 警戒结束后保留时间
    public static long guarding2standByInterval = 2 * 60 * 1000;

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
     *
     * @Date 9:49 2022/8/2
     * @Author 温昌营
     **/
    public static void checkNumber(AccountInfo accountInfo, int imgType, List<AccountInfo> accountInfoList) throws Exception {
        if ((!Objects.equals(AccountInfo.GuardStatusEnum.WATCHING_OUT.getCode(), accountInfo.getGuardStatus())
                && !Objects.equals(AccountInfo.GuardStatusEnum.BEFORE_STAND_BY.getCode(), accountInfo.getGuardStatus()))
                || accountInfo.getHisMat() == null) {
            // 无需通知，直接返回
            return;
        }
        Mat newMat = createScreenAnd2Mat(accountInfo.getEnemySt(), accountInfo.getEnemyEd(), imgType, accountInfo.getAccount());
        boolean needWarning = doCheckIfNeedWarning(newMat, accountInfo);
        if (needWarning) {
            if (accountInfo.checkIfLeader()) {
                doEscapeWithFollows(accountInfo);
                setGuardStatusWithFollows(accountInfo, AccountInfo.GuardStatusEnum.RUN.getCode());
            } else {
                doEscape(accountInfo);
                for (AccountInfo leader : accountInfoList) {
                    if (Objects.equals(leader.getSerialNo(), accountInfo.getLeaderSerialNo())) {
                        doEscapeWithFollows(leader);
                        setGuardStatusWithFollows(leader, AccountInfo.GuardStatusEnum.RUN.getCode());
                        break;
                    }
                }
            }
            System.out.println("【"+accountInfo.getAccount()+"-"+accountInfo.getGuardingCount().get()+"】 -> RUN 【"+DateUtil.dateFormat(new Date())+"】");
            // 记录图像
            int count = accountInfo.getRefreshCount().incrementAndGet();
            String basePath = ImgBoot.getBasePath(accountInfo.getAccount());
            OpenCVHelper.saveMat2Img(basePath, newMat, ImgBoot.getHisImgName(count, "number"));
        }
    }

    /**
     * 通过图片检查本地进红状态。后续通知或直接跑路处理
     *
     * @Date 9:49 2022/8/11
     * @Author 温昌营
     **/
    public static void checkIfNeedWarning(AccountInfo accountInfo, int imgType, Map<Integer, AccountInfo> serialNoAccountInfoMap) throws Exception {
        // red 截图起点
        PointInfoDTO redSt = accountInfo.getRedSt();
        // red 截图终点
        PointInfoDTO redEd = accountInfo.getRedEd();
        if (!accountInfo.isNeedWarn()) {
            // 无需告警，直接返回
            return;
        }
        boolean warning = false;
        Mat src = createScreenAnd2Mat(redSt, redEd, imgType, accountInfo.getAccount());
        if (src == null) {
            System.out.println("异常，无法获取警告mat");
            warning = true;
        }
        if (warning || doCheckIfNeedWarning(src, accountInfo)) {
            // 如果来敌人，且启用了自动化
            if (accountInfo.isIfAuto()) {
                if (accountInfo.isHasGuard()) {
                    // 如果有警卫，且警卫处于待命状态，命令其警戒
                    if (Objects.equals(AccountInfo.GuardStatusEnum.STAND_BY.getCode(), accountInfo.getGuardStatus())) {
                        int count = accountInfo.getGuardingCount().incrementAndGet();
                        System.out.println("【"+accountInfo.getAccount()+"-"+count+"】 -> WATCHING_OUT 【"+DateUtil.dateFormat(new Date())+"】");
                        doWatchWithFollows(accountInfo);
                        setGuardStatusWithFollowsWithGuardTime(accountInfo, AccountInfo.GuardStatusEnum.WATCHING_OUT.getCode(), new Date());
                    }
                } else {
                    // 如果没有警卫，直接跑路
                    doEscapeWithFollows(accountInfo);
                }
            } else {
                // 如果没有启用自动化，发音提醒
                sendVoice(accountInfo.getWarnMsg(), false);
                for (AccountInfo value : accountInfo.getFollows().values()) {
                    sendVoice(value.getWarnMsg(), false);
                }
            }
        } else {
            // 否则如果安全，且处于警戒状态。解除警戒
            if (accountInfo.isHasGuard()) {
                if (Objects.equals(AccountInfo.GuardStatusEnum.WATCHING_OUT.getCode(), accountInfo.getGuardStatus())) {
                    System.out.println("【"+accountInfo.getAccount()+"-"+accountInfo.getGuardingCount().get()+"】 -> BEFORE_STAND_BY 【"+DateUtil.dateFormat(new Date())+"】");
                    // 重置为伪待命状态
                    setGuardStatusWithFollows(accountInfo, AccountInfo.GuardStatusEnum.BEFORE_STAND_BY.getCode());
                } else if (Objects.equals(AccountInfo.GuardStatusEnum.BEFORE_STAND_BY.getCode(), accountInfo.getGuardStatus())) {
                    // 否则如果安全，且处于伪待命状态，且处于伪待命状态一段时间后。解除警戒
                    long time = new Date().getTime();
                    if (time > accountInfo.getGuardTime().getTime() + guarding2standByInterval) {
                        System.out.println("【"+accountInfo.getAccount()+"-"+accountInfo.getGuardingCount().get()+"】 -> STAND_BY 【"+DateUtil.dateFormat(new Date())+"】");
                        // 重置为待命状态
                        setGuardStatusWithFollows(accountInfo, AccountInfo.GuardStatusEnum.STAND_BY.getCode());
                        doStandByWithFollows(accountInfo);
                    }
                }
            }
        }
    }

    /**
     * 开始警戒
     *
     * @param accountInfo
     * @throws Exception
     */
    private static void doWatchWithFollows(AccountInfo accountInfo) throws Exception {
        accountInfo.getToWatch().accept(null);
        for (AccountInfo value : accountInfo.getFollows().values()) {
            value.getToWatch().accept(null);
        }
        // 设置初始mat
        accountInfo.setHisMat(createScreenAnd2Mat(accountInfo.getEnemySt(), accountInfo.getEnemyEd(), Imgcodecs.IMREAD_GRAYSCALE, accountInfo.getAccount()));
        for (AccountInfo value : accountInfo.getFollows().values()) {
            value.setHisMat(createScreenAnd2Mat(value.getEnemySt(), value.getEnemyEd(), Imgcodecs.IMREAD_GRAYSCALE, value.getAccount()));
        }
    }

    /**
     * 解除警戒
     *
     * @param accountInfo
     * @throws Exception
     */
    private static void doStandByWithFollows(AccountInfo accountInfo) throws Exception {
        accountInfo.getToStandBy().accept(null);
        for (AccountInfo value : accountInfo.getFollows().values()) {
            value.getToStandBy().accept(null);
        }
    }

    private static void setGuardStatusWithFollows(AccountInfo accountInfo, Integer statusCode) {
        setGuardStatusWithFollowsWithGuardTime(accountInfo, statusCode, null);
    }

    private static void setGuardStatusWithFollowsWithGuardTime(AccountInfo accountInfo, Integer statusCode, Date guardTime) {
        accountInfo.setGuardStatus(statusCode);
        if (null != guardTime) {
            accountInfo.setGuardTime(guardTime);
        }
        for (AccountInfo value : accountInfo.getFollows().values()) {
            value.setGuardStatus(statusCode);
            if (null != guardTime) {
                value.setGuardTime(guardTime);
            }
        }
    }


    private static void doEscapeWithFollows(AccountInfo accountInfo) throws Exception {
        doEscape(accountInfo);
        for (AccountInfo value : accountInfo.getFollows().values()) {
            doEscape(value);
        }
    }

    private static void doEscape(AccountInfo accountInfo) throws Exception {
        long time = new Date().getTime();
        if (time > accountInfo.getLastQuickRunTime().getTime() + auto_activeInterval) {
            accountInfo.setLastQuickRunTime(new Date());
            accountInfo.getConsumer().accept(null);
            try {
                // 发送电子邮件
                SendMailText.sendMsg(accountInfo.getAccount());
            } catch (Exception e) {
                log.info("发送邮件失败", e);
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
     *
     * @param src
     * @param accountInfo
     * @return
     * @throws BusinessException
     */
    private static boolean doCheckIfNeedWarning(Mat src, AccountInfo accountInfo) throws BusinessException {
        String basePath = getBasePath(accountInfo.getAccount());
        return ColorDetectTool.doDetect(src,
                ColorDetectTool.defaultMinValues,
                ColorDetectTool.defaultMaxValues,
                // 不需要保存，去掉2025-1-4 22:40:41
                // (targetMat) -> OpenCVHelper.saveMat2Img(basePath, targetMat, getCountPrefix(accountInfo.getRefreshCount().get()) + "_warning.png"));
                (targetMat) -> {
                });
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
            Mat hisMat = account.getHisMat();
            Mat numberCheckImage = createScreenAnd2Mat(account.getEnemySt(), account.getEnemyEd(), Imgcodecs.IMREAD_GRAYSCALE, account.getAccount());
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
            if (null != hisMat) {
                OpenCVHelper.saveMat2Img(basePath, hisMat, getHisImgName(count, "number_his"));
            }
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
                imgRef.set(OpenCVHelper.BufferedImage2Mat(bufferedImage));
            } catch (Exception e) {
                log.error("异常，截图转换失败. account:{}", account, e);
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

    public static String getBasePath(String folderName) {
        return BUFFER_IMAGE_AREA + folderName + File.separator;
    }

    private static String getHisImgPath(String folderName, int count, String type) {
        return BUFFER_IMAGE_AREA + folderName + File.separator + getHisImgName(count, type);
    }

    public static String getHisImgName(int count, String type) {
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
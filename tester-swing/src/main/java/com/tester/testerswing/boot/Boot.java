package com.tester.testerswing.boot;

import com.tester.base.dto.exception.BusinessException;
import com.tester.testerswing.capture.ImgBoot;
import com.tester.testerswing.capture.PointInfoDTO;
import com.tester.testerswing.voice.BeepSoundProcessor;
import org.opencv.imgcodecs.Imgcodecs;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 启动检测
 * @Date 11:50 2022/8/12
 * @Author 温昌营
 **/
public class Boot {

    private boolean started = false;

    private String silot = "silot";
    private String sailinna = "sailinna";
    private String colos = "colos";

    // todo 所有监控点
    private PointInfoDTO st_silot = new PointInfoDTO().setX(51).setY(70);
    private PointInfoDTO ed_silot = new PointInfoDTO().setX(74).setY(90);

    private PointInfoDTO st_sai = new PointInfoDTO().setX(127).setY(62);
    private PointInfoDTO ed_sai = new PointInfoDTO().setX(146).setY(85);

    private PointInfoDTO st_colos = new PointInfoDTO().setX(203).setY(61);
    private PointInfoDTO ed_colos = new PointInfoDTO().setX(226).setY(81);


    private PointInfoDTO red_st_silot = new PointInfoDTO().setX(55).setY(127);
    private PointInfoDTO red_ed_silot = new PointInfoDTO().setX(71).setY(697);

    private PointInfoDTO red_st_sai = new PointInfoDTO().setX(132).setY(118);
    private PointInfoDTO red_ed_sai = new PointInfoDTO().setX(145).setY(696);

    private PointInfoDTO red_st_colos = new PointInfoDTO().setX(209).setY(117);
    private PointInfoDTO red_ed_colos = new PointInfoDTO().setX(225).setY(697);

    private final ScheduledExecutorService checkerExecutorService = Executors.newSingleThreadScheduledExecutor(new SwingThreadFactoryImpl("warn-checker"));


    private final List<AccountInfo> accountInfoList = new ArrayList<>();

    public Boot() {
        accountInfoList.add(new AccountInfo()
                .setWarnMsg("左警告")
                .setInfoMsg("左")
                .setAccount(silot)
                .setSt(st_silot)
                .setEd(ed_silot)
                .setRedSt(red_st_silot)
                .setRedEd(red_ed_silot));
        accountInfoList.add(new AccountInfo()
                .setWarnMsg("中警告")
                .setInfoMsg("中")
                .setAccount(sailinna)
                .setSt(st_sai)
                .setEd(ed_sai)
                .setRedSt(red_st_sai)
                .setRedEd(red_ed_sai));
        accountInfoList.add(new AccountInfo()
                .setWarnMsg("右警告")
                .setInfoMsg("右")
                .setAccount(colos)
                .setSt(st_colos)
                .setEd(ed_colos)
                .setRedSt(red_st_colos)
                .setRedEd(red_ed_colos));
    }


    public void start() throws BusinessException {
        if (started) {
            return;
        }
        started = true;
        // 重建目录，初始化图片
        ImgBoot.start(accountInfoList);
        // 启动警告音监听
        BeepSoundProcessor.start();
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // 最后启动监听任务
        startMySelf();
    }

    public String refresh() {
        for (AccountInfo accountInfo : accountInfoList) {
            try {
                ImgBoot.refreshHisImg(accountInfo);
            } catch (BusinessException e) {
                e.printStackTrace();
                return "刷新失败。account:" + accountInfo.getAccount();
            }
        }
        return new Date().toString();
    }

    private void startMySelf() {
        int delay = 2;
        int period = 1;
        checkerExecutorService.scheduleAtFixedRate(() -> {
                    try {
                        for (AccountInfo accountInfo : accountInfoList) {
                            // 监控数量取原图
                            ImgBoot.checkIfNeedWarning(accountInfo, Imgcodecs.IMREAD_UNCHANGED);
                        }
                        for (AccountInfo accountInfo : accountInfoList) {
                            // 监控数量取灰度图
                            ImgBoot.checkNumber(accountInfo, Imgcodecs.IMREAD_GRAYSCALE);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
                , delay
                , period
                , TimeUnit.SECONDS);
    }

    public boolean isStarted() {
        return started;
    }

    public List<AccountInfo> getAccountInfoList() {
        return accountInfoList;
    }
}

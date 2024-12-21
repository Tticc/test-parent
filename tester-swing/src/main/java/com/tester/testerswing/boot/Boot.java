package com.tester.testerswing.boot;

import com.tester.base.dto.exception.BusinessException;
import com.tester.testercommon.util.DateUtil;
import com.tester.testerswing.capture.ImgBoot;
import com.tester.testerswing.capture.PointInfoDTO;
import com.tester.testerswing.swing.eventHandler.EventHandle_Colos;
import com.tester.testerswing.swing.eventHandler.EventHandle_Sai;
import com.tester.testerswing.swing.eventHandler.EventHandle_Silot;
import com.tester.testerswing.swing.eventHandler.PointHelper;
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
    private String four = "four";


    private PointInfoDTO red_st_silot = new PointInfoDTO().setX(37).setY(87);
    private PointInfoDTO red_ed_silot = new PointInfoDTO().setX(58).setY(688);

    private PointInfoDTO red_st_sai = new PointInfoDTO().setX(94).setY(131);
    private PointInfoDTO red_ed_sai = new PointInfoDTO().setX(118).setY(688);

    private PointInfoDTO red_st_colos = new PointInfoDTO().setX(151).setY(131);
    private PointInfoDTO red_ed_colos = new PointInfoDTO().setX(172).setY(688);

    private final ScheduledExecutorService checkerExecutorService = Executors.newSingleThreadScheduledExecutor(new SwingThreadFactoryImpl("warn-checker"));


    private final List<AccountInfo> accountInfoList = new ArrayList<>();

    public Boot() {
        List<PointHelper.AccountPoint> list = PointHelper.getList();
        for (int i = 1; i <= list.size(); i++) {
            Integer finalI = i;
            PointHelper.AccountPoint accountPoint = list.get(finalI - 1);
            accountInfoList.add(new AccountInfo()
                    .setWarnMsg(accountPoint.getWarnMsg())
                    .setAccount(accountPoint.getName())
                    .setRedSt(accountPoint.getRedSt())
                    .setRedEd(accountPoint.getRedEd())
                    .setLastQuickRunTime(DateUtil.getYesterdayStart())
                    .setConsumer((e) -> PointHelper.eveEscapeAll(PointHelper.getList(),finalI)));
        }
//        accountInfoList.add(new AccountInfo()
//                .setWarnMsg("左警告")
//                .setAccount(silot)
////                .setSt(st_silot)
////                .setEd(ed_silot)
//                .setRedSt(red_st_silot)
//                .setRedEd(red_ed_silot)
//                .setLastQuickRunTime(DateUtil.getYesterdayStart())
//                .setConsumer((e) -> EventHandle_Silot.quick_run()));
//        accountInfoList.add(new AccountInfo()
//                .setWarnMsg("中警告")
//                .setAccount(sailinna)
////                .setSt(st_sai)
////                .setEd(ed_sai)
//                .setRedSt(red_st_sai)
//                .setRedEd(red_ed_sai)
//                .setLastQuickRunTime(DateUtil.getYesterdayStart())
//                .setConsumer((e) -> EventHandle_Sai.quick_run()));
//        accountInfoList.add(new AccountInfo()
//                .setWarnMsg("右警告")
//                .setAccount(colos)
////                .setSt(st_colos)
////                .setEd(ed_colos)
//                .setRedSt(red_st_colos)
//                .setRedEd(red_ed_colos)
//                .setLastQuickRunTime(DateUtil.getYesterdayStart())
//                .setConsumer((e) -> EventHandle_Colos.quick_run()));
//        accountInfoList.add(new AccountInfo()
//                .setWarnMsg("4警告")
//                .setAccount(four)
////                .setSt(st_colos)
////                .setEd(ed_colos)
//                .setRedSt(red_st_colos)
//                .setRedEd(red_ed_colos)
//                .setLastQuickRunTime(DateUtil.getYesterdayStart())
//                .setConsumer((e) -> EventHandle_Colos.quick_run()));
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
        int delay = 2000;
        int period = 500;
        checkerExecutorService.scheduleAtFixedRate(() -> {
                    try {
                        for (AccountInfo accountInfo : accountInfoList) {
                            // 监控数量取原图
                            ImgBoot.checkIfNeedWarning(accountInfo, Imgcodecs.IMREAD_UNCHANGED);
                        }
//                        for (AccountInfo accountInfo : accountInfoList) {
//                            // 监控数量取灰度图，比较本地数量变化 - 已废弃
//                            ImgBoot.checkNumber(accountInfo, Imgcodecs.IMREAD_GRAYSCALE);
//                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
                , delay
                , period
                , TimeUnit.MICROSECONDS);
    }

    public boolean isStarted() {
        return started;
    }

    public List<AccountInfo> getAccountInfoList() {
        return accountInfoList;
    }
}

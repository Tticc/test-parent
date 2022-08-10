package com.tester.testerswing.boot;

import com.tester.base.dto.exception.BusinessException;
import com.tester.testerswing.capture.ImgBoot;
import com.tester.testerswing.capture.PointInfoDTO;
import com.tester.testerswing.voice.BeepSoundProcessor;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Boot {

    private boolean started = false;

    private String silot = "silot";
    private String sailinna = "sailinna";

    private PointInfoDTO st_silot = new PointInfoDTO().setX(56).setY(118);
    private PointInfoDTO ed_silot = new PointInfoDTO().setX(153).setY(572);

    private PointInfoDTO st_sai = new PointInfoDTO().setX(56).setY(118);
    private PointInfoDTO ed_sai = new PointInfoDTO().setX(153).setY(572);


    private PointInfoDTO red_st_silot = new PointInfoDTO().setX(56).setY(118);
    private PointInfoDTO red_ed_silot = new PointInfoDTO().setX(153).setY(572);

    private PointInfoDTO red_st_sai = new PointInfoDTO().setX(56).setY(118);
    private PointInfoDTO red_ed_sai = new PointInfoDTO().setX(153).setY(572);

    private final ScheduledExecutorService checkerExecutorService = Executors.newSingleThreadScheduledExecutor(new SwingThreadFactoryImpl("warn-checker"));


    private final List<AccountInfo> accountInfoList = new ArrayList<>();

    public Boot() {
        accountInfoList.add(new AccountInfo()
                .setWarnMsg("左警告")
                .setInfoMsg("左提醒")
                .setAccount(silot)
                .setSt(st_silot)
                .setEd(ed_silot)
                .setRedSt(red_st_silot)
                .setRedEd(red_ed_silot));
        accountInfoList.add(new AccountInfo()
                .setWarnMsg("右警告")
                .setInfoMsg("左提醒")
                .setAccount(sailinna)
                .setSt(st_sai)
                .setEd(ed_sai)
                .setRedSt(red_st_sai)
                .setRedEd(red_ed_sai));
    }


    public void start() throws BusinessException {
        if (started) {
            return;
        }
        started = true;
        ImgBoot.start(accountInfoList);
        BeepSoundProcessor.start();

        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // 最后启动自己
        startMySelf();
    }

    public String refresh() {
        for (AccountInfo accountInfo : accountInfoList) {
            try {
                ImgBoot.refreshHisImg(accountInfo);
            } catch (BusinessException e) {
                return "刷新失败。account:" + accountInfo.getAccount();
            }
        }
        SimpleDateFormat formatter = new SimpleDateFormat("MM-dd HH:mm:ss");
//        return formatter.format(new Date());
        return new Date().toString();
    }

    public void startMySelf() {
        int delay = 2;
        int period = 1;
        checkerExecutorService.scheduleAtFixedRate(() -> {
                    try {
                        for (AccountInfo accountInfo : accountInfoList) {
                            ImgBoot.checkWarning(accountInfo);
                        }
                        for (AccountInfo accountInfo : accountInfoList) {
                            ImgBoot.checkNumber(accountInfo);
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

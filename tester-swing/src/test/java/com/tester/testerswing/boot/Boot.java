package com.tester.testerswing.boot;

import com.tester.base.dto.exception.BusinessException;
import com.tester.testerswing.capture.Img;
import com.tester.testerswing.capture.PointInfo;
import com.tester.testerswing.voice.BeepSoundProcessor;

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

    private PointInfo st_silot = new PointInfo().setX(56).setY(118);
    private PointInfo ed_silot = new PointInfo().setX(153).setY(572);

    private PointInfo st_sai = new PointInfo().setX(56).setY(118);
    private PointInfo ed_sai = new PointInfo().setX(153).setY(572);

    private final ScheduledExecutorService checkerExecutorService = Executors.newSingleThreadScheduledExecutor(new SwingThreadFactoryImpl("warn-checker"));


    private final List<AccountInfo> accountInfoList = new ArrayList<>();

    public Boot() {
        accountInfoList.add(new AccountInfo().setAccount(silot).setSt(st_silot).setEd(ed_silot));
        accountInfoList.add(new AccountInfo().setAccount(sailinna).setSt(st_sai).setEd(ed_sai));
    }


    public void start() throws BusinessException {
        if (started) {
            return;
        }
        started = true;
        Img.start(accountInfoList);
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
                Img.refreshHisImg(accountInfo);
            } catch (BusinessException e) {
                return "刷新失败。account:" + accountInfo.getAccount();
            }
        }
        return new Date().toString();
    }

    public void startMySelf() {
        int delay = 5;
        int period = 1;
        checkerExecutorService.scheduleAtFixedRate(() -> {
                    try {
                        for (AccountInfo accountInfo : accountInfoList) {
                            Img.checkIfNormal(accountInfo.getSt(), accountInfo.getEd(), accountInfo.getAccount());
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
}

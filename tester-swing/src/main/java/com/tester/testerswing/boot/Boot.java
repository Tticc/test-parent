package com.tester.testerswing.boot;

import com.tester.base.dto.exception.BusinessException;
import com.tester.testercommon.util.DateUtil;
import com.tester.testerswing.capture.ImgBoot;
import com.tester.testerswing.capture.PointInfoDTO;
import com.tester.testerswing.swing.eventHandler.*;
import com.tester.testerswing.voice.BeepSoundProcessor;
import org.opencv.imgcodecs.Imgcodecs;

import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 启动检测
 * @Date 11:50 2022/8/12
 * @Author 温昌营
 **/
public class Boot {

    private boolean started = false;

    private static Integer accountNum = 0;


    private final ScheduledExecutorService checkerExecutorService = Executors.newSingleThreadScheduledExecutor(new SwingThreadFactoryImpl("warn-checker"));


    private final List<AccountInfo> accountInfoList = new ArrayList<>();
    private final Map<Integer, AccountInfo> serialNoAccountInfoMap = new HashMap<>();

    public Boot() {
        PointHelper.CommonEvePoint commonEvePoint = new PointHelper.CommonEvePoint();
        List<PointHelper.AccountPoint> list = PointHelper.getListAll();
        Boot.setAccountNum(list.size());
        for (int i = 1; i <= list.size(); i++) {
            Integer finalI = i;
            PointHelper.AccountPoint accountPoint = list.get(finalI - 1);
            AccountInfo accountInfo = new AccountInfo()
                    .setSerialNo(finalI)
                    .setLeaderSerialNo(finalI)
                    .setWarnMsg(accountPoint.getWarnMsg())
                    .setAccount(accountPoint.getName())
                    .setRedSt(accountPoint.getRedSt())
                    .setRedEd(accountPoint.getRedEd())
                    .setEnemySt(accountPoint.getEnemySt())
                    .setEnemyEd(accountPoint.getEnemyEd())
                    .setLocalSt(commonEvePoint.getEve_localStPoint())
                    .setLocalEd(commonEvePoint.getEve_localEdPoint())
                    .setLastQuickRunTime(DateUtil.getYesterdayStart())
                    .setGuardTime(DateUtil.getYesterdayStart())
                    .setLastOcrTime(DateUtil.getYesterdayStart())
                    .setConsumer((e) -> PointHelper.eveEscapeAll(PointHelper.getList(), finalI))
                    .setToWatch((e) -> PointHelper.toWatchAll(PointHelper.getList(), finalI))
                    .setToStandBy((e) -> PointHelper.toStandByAll(PointHelper.getList(), finalI))
                    .setOpenConsumer((e) -> EventHandle_Main.openOpeNew(commonEvePoint.getEve_openSelectPoint(), accountPoint.getEve_selectPoint()))
                    .setReturnConsumer((e) -> PointHelper.eveEndWorkAll(PointHelper.getList(), finalI));
            accountPoint.setAccountInfo(accountInfo);
            accountInfoList.add(accountInfo);
            serialNoAccountInfoMap.put(finalI, accountInfo);
        }
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
        int period = 1000;
        checkerExecutorService.scheduleAtFixedRate(() -> {
                    try {
                        List<AccountInfo> accountInfoList = getAccountInfoList();
                        for (AccountInfo accountInfo : accountInfoList) {
                            if(!Objects.equals(accountInfo.getSerialNo(), accountInfo.getLeaderSerialNo())){
                                continue;
                            }
                            // 监控数量取原图
                            ImgBoot.checkIfNeedWarning(accountInfo, Imgcodecs.IMREAD_UNCHANGED, serialNoAccountInfoMap);
                        }
                        for (AccountInfo accountInfo : accountInfoList) {
                            // 监控数量取灰度图，比较本地数量变化 - 已废弃
                            ImgBoot.checkNumber(accountInfo, Imgcodecs.IMREAD_GRAYSCALE, accountInfoList);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
                , delay
                , period
                , TimeUnit.MILLISECONDS);
    }

    public boolean isStarted() {
        return started;
    }

    public List<AccountInfo> getAccountInfoList() {
        if(Boot.getAccountNum() > 0){
            return accountInfoList.stream().limit(accountNum).collect(Collectors.toList());
        }
        return accountInfoList;
    }

    public Map<Integer, AccountInfo> getSerialNoAccountInfoMap() {
        return serialNoAccountInfoMap;
    }


    public static Integer getAccountNum() {
        return accountNum;
    }

    public static void setAccountNum(Integer newNum) {
        accountNum = newNum;
    }


    public static boolean checkIfReturn(Integer num) {
        // 如果最大账号数小于当前账号序号，那么return true
        if(getAccountNum() < num){
            return true;
        }
        return false;
    }
}

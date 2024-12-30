package com.tester.testerswing.onTopReplica;

import com.tester.testerswing.robot.RobotHelper;
import com.tester.testerswing.swing.eventHandler.EventHandle_Main;
import com.tester.testerswing.swing.eventHandler.PointHelper;

import java.awt.event.InputEvent;
import java.util.List;

public class TestForOnTopReplica {

    public static void main(String[] args) {
        PointHelper.CommonReplicaPoint commonReplicaPoint = new PointHelper.CommonReplicaPoint();
        PointHelper.CommonEvePoint commonEvePoint = new PointHelper.CommonEvePoint();
        List<PointHelper.AccountPoint> list = PointHelper.getList();
        PointHelper.AccountPoint accountPoint = list.get(0);
        EventHandle_Main.openOpeNew(commonEvePoint.getEve_openSelectPoint(), accountPoint.getEve_selectPoint());
        RobotHelper.delay(200);


        RobotHelper.move(commonReplicaPoint.getReplica_searchPoint1());
        RobotHelper.mouseLeftPress();
        RobotHelper.delay(456);
        RobotHelper.move(commonReplicaPoint.getReplica_searchPoint2());
        RobotHelper.keyPress("OnTopReplica");
        RobotHelper.delay(456);
        RobotHelper.move(commonReplicaPoint.getReplica_startPoint());
        RobotHelper.mouseLeftPress();
        RobotHelper.delay(656);
        RobotHelper.move(commonReplicaPoint.getReplica_rightClickEmptyPoint());
        RobotHelper.delay(456);
        RobotHelper.mouseRightPress();
        RobotHelper.move(commonReplicaPoint.getReplica_lockMenuPoint());
        RobotHelper.mouseLeftPress();
        RobotHelper.move(commonReplicaPoint.getReplica_lockLeftPoint());
        RobotHelper.mouseLeftPress();
        // 移动到起始位置
        RobotHelper.move(commonReplicaPoint.getReplica_lockedStartPoint());
        RobotHelper.mouseRightPress();
        // 选择窗口
        RobotHelper.move(commonReplicaPoint.getReplica_selectWindowMenuPoint());
        RobotHelper.mouseLeftPress();
        // 选择 a
        RobotHelper.move(accountPoint.getReplica_selectPoint());
        RobotHelper.mouseLeftPress();
//
        // 移动到起始位置
        RobotHelper.move(commonReplicaPoint.getReplica_lockedStartPoint());
        RobotHelper.mouseRightPress();
        RobotHelper.move(commonReplicaPoint.getReplica_percentageWindowMenuPoint());
        RobotHelper.mouseLeftPress();
        RobotHelper.move(commonReplicaPoint.getReplica_50PercentageWindowMenuPoint());
        RobotHelper.mouseLeftPress();

        // 移动到起始位置
        RobotHelper.move(commonReplicaPoint.getReplica_lockedStartPoint());
        RobotHelper.mouseRightPress();
        RobotHelper.move(commonReplicaPoint.getReplica_selectAreaPoint());
        RobotHelper.mouseLeftPress();

        // 移动到区域起始点
        RobotHelper.move(commonReplicaPoint.getReplica_areaStartPoint());
        // 按下鼠标左键
        RobotHelper.r.mousePress(InputEvent.BUTTON1_MASK);
        // 移动到区域终止点
        RobotHelper.move(commonReplicaPoint.getReplica_areaEndPoint());
        // 弹起鼠标左键
        RobotHelper.r.mouseRelease(InputEvent.BUTTON1_MASK);
        // 区域选择完成
        RobotHelper.move(commonReplicaPoint.getReplica_areaDoneButtonPoint());
        RobotHelper.mouseLeftPress();
//
////        // 拖动放缩分屏窗口
        RobotHelper.move(commonReplicaPoint.getReplica_scrollStartPoint());
        RobotHelper.r.mousePress(InputEvent.BUTTON1_MASK);
        RobotHelper.move(commonReplicaPoint.getReplica_scrollEndPoint());
        RobotHelper.delay(400);
        RobotHelper.r.mouseRelease(InputEvent.BUTTON1_MASK);
        RobotHelper.delay(500);
//
//        // 解除锁定
        RobotHelper.move(commonReplicaPoint.getReplica_beforeUnlockMenuPoint());
        RobotHelper.mouseRightPress();
        RobotHelper.move(commonReplicaPoint.getReplica_unlockMenuPoint());
        RobotHelper.mouseLeftPress();
        RobotHelper.move(commonReplicaPoint.getReplica_unlockPoint());
        RobotHelper.mouseLeftPress();
        // 拖动1
        RobotHelper.move(commonReplicaPoint.getReplica_commonMoveStartPoint());
        RobotHelper.r.mousePress(InputEvent.BUTTON1_MASK);
        RobotHelper.move(accountPoint.getReplica_moveEndPoint());
        RobotHelper.delay(500);
        RobotHelper.r.mouseRelease(InputEvent.BUTTON1_MASK);
        RobotHelper.delay(500);


    }
}

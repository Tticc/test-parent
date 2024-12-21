package com.tester.testerswing.swing.eventHandler;

import com.google.common.collect.Lists;
import com.tester.base.dto.exception.BusinessException;
import com.tester.testercommon.util.MyConsumer;
import com.tester.testerswing.capture.GaussianPointInfoDTO;
import com.tester.testerswing.capture.GaussianStrPointInfoDTO;
import com.tester.testerswing.capture.PointInfoDTO;
import com.tester.testerswing.gaussian.GaussianHelper;
import com.tester.testerswing.robot.RobotHelper;
import lombok.Data;
import lombok.Getter;

import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class PointHelper {

    public static List<AccountPoint> list = new ArrayList<>();

    public static List<AccountPoint> getList() {
        return list.stream().collect(Collectors.toList());
    }


    static {
        AccountPoint silot = new AccountPoint();
        silot.setEve_selectPoint(new GaussianStrPointInfoDTO("811,955"));
        silot.setReplica_selectPoint(new GaussianStrPointInfoDTO("304,99"));
        silot.setReplica_moveEndPoint(new GaussianStrPointInfoDTO("80,57"));
        silot.setEve_escapeTabPoint(new GaussianStrPointInfoDTO("1850,231", "1862,238"));
        silot.setEve_dockingBuildingPoint(new GaussianStrPointInfoDTO("1622,290", "1650,298"));
        silot.setEve_alignToPoint(new GaussianStrPointInfoDTO("1588,153", "1602,163"));
        silot.setEve_dockPoint(new GaussianStrPointInfoDTO("1663,153", "1676,162"));
        silot.setRedSt(new PointInfoDTO(37, 133));
        silot.setRedEd(new PointInfoDTO(55, 651));
        silot.setName("silot");
        silot.setWarnMsg("左警告");


        AccountPoint sailinaa = new AccountPoint();
        sailinaa.setEve_selectPoint(new GaussianStrPointInfoDTO("1068,958"));
        sailinaa.setReplica_selectPoint(new GaussianStrPointInfoDTO("362,123"));
        sailinaa.setReplica_moveEndPoint(new GaussianStrPointInfoDTO("137,58"));
        sailinaa.setEve_escapeTabPoint(new GaussianStrPointInfoDTO("1846,230", "1865,239"));
        sailinaa.setEve_dockingBuildingPoint(new GaussianStrPointInfoDTO("1625,288", "1644,299"));
        sailinaa.setEve_alignToPoint(new GaussianStrPointInfoDTO("1590,154", "1601,163"));
        sailinaa.setEve_dockPoint(new GaussianStrPointInfoDTO("1664,151", "1678,165"));
        sailinaa.setRedSt(new PointInfoDTO(96, 141));
        sailinaa.setRedEd(new PointInfoDTO(116, 651));
        sailinaa.setName("sailinna");
        sailinaa.setWarnMsg("中警告");


        AccountPoint colos = new AccountPoint();
        colos.setEve_selectPoint(new GaussianStrPointInfoDTO("1344,948"));
        colos.setReplica_selectPoint(new GaussianStrPointInfoDTO("361,152"));
        colos.setReplica_moveEndPoint(new GaussianStrPointInfoDTO("194,58"));
        colos.setEve_escapeTabPoint(new GaussianStrPointInfoDTO("1843,227", "1862,238"));
        colos.setEve_dockingBuildingPoint(new GaussianStrPointInfoDTO("1616,288", "1634,299"));
        colos.setEve_alignToPoint(new GaussianStrPointInfoDTO("1591,150", "1600,161"));
        colos.setEve_dockPoint(new GaussianStrPointInfoDTO("1665,151", "1678,163"));
        colos.setRedSt(new PointInfoDTO(151, 139));
        colos.setRedEd(new PointInfoDTO(173, 651));
        colos.setName("colos");
        colos.setWarnMsg("右警告");


        AccountPoint four = new AccountPoint();
        four.setEve_selectPoint(new GaussianStrPointInfoDTO("1527,948"));
        four.setReplica_selectPoint(new GaussianStrPointInfoDTO("327,173"));
        four.setReplica_moveEndPoint(new GaussianStrPointInfoDTO("251,58"));
        four.setEve_escapeTabPoint(new GaussianStrPointInfoDTO("1840,228", "1865,235"));
        four.setEve_dockingBuildingPoint(new GaussianStrPointInfoDTO("1627,289", "1653,294"));
        four.setEve_alignToPoint(new GaussianStrPointInfoDTO("1581,159", "1598,168"));
        four.setEve_dockPoint(new GaussianStrPointInfoDTO("1661,154", "1675,161"));
        four.setRedSt(new PointInfoDTO(211, 139));
        four.setRedEd(new PointInfoDTO(231, 651));
        four.setName("four");
        four.setWarnMsg("four警告");


        list.add(silot);
        list.add(sailinaa);
        list.add(colos);
        list.add(four);
    }


    @Data
    public static class AccountPoint {
        // 点击任务栏选择账号窗口
        public GaussianStrPointInfoDTO eve_selectPoint;
        // onTopReplica 窗口选择账号点
        public GaussianStrPointInfoDTO replica_selectPoint;
        // onTopReplica 窗口选择账号点
        public GaussianStrPointInfoDTO replica_moveEndPoint;
        // 特殊tab，跑路用
        public GaussianStrPointInfoDTO eve_escapeTabPoint;
        // 停靠建筑
        public GaussianStrPointInfoDTO eve_dockingBuildingPoint;
        // 朝向按钮
        public GaussianStrPointInfoDTO eve_alignToPoint;
        // 停靠按钮
        public GaussianStrPointInfoDTO eve_dockPoint;

        // red 截图起点
        private PointInfoDTO redSt;

        // red 截图终点
        private PointInfoDTO redEd;

        private String name;

        private String warnMsg;

    }


    @Data
    public static class EveHarvestPoint {
        // 货仓内mtu位置点
        public GaussianStrPointInfoDTO eve_mutPoint = new GaussianStrPointInfoDTO("485,180", "499,180");
        // 释放mtu位置点
        public GaussianStrPointInfoDTO eve_releaseMutPoint = new GaussianStrPointInfoDTO("570,404", "594,408");
        // 堆叠货仓内mtu位置点
        public GaussianStrPointInfoDTO eve_stackMutPoint = new GaussianStrPointInfoDTO("472,120", "472,120");
        // 太空mtu位置点
        public GaussianStrPointInfoDTO eve_spaceMutPoint = new GaussianStrPointInfoDTO("1622,292", "1638,293");
        // 保存mtu位置点
        public GaussianStrPointInfoDTO eve_saveMutPointPoint = new GaussianStrPointInfoDTO("1695,508", "1711,507");
    }


    @Data
    public static class EveToWorkPoint {
        // 建筑tab点
        public GaussianStrPointInfoDTO eve_buildingTabPoint = new GaussianStrPointInfoDTO("1706,230", "1730,233");
        // 建筑点
        public GaussianStrPointInfoDTO eve_buildingPoint = new GaussianStrPointInfoDTO("1686,289", "1738,298");
        // 作战tab点
        public GaussianStrPointInfoDTO eve_fightingTabPoint = new GaussianStrPointInfoDTO("1633,226", "1655,236");
        // 环绕点
        public GaussianStrPointInfoDTO eve_aroundPoint = new GaussianStrPointInfoDTO("1662,159", "1674,168");
        // 启动加速点
        public GaussianStrPointInfoDTO eve_speedUpPoint = new GaussianStrPointInfoDTO("1107,878", "1122,891");
        // 释放无人机点
        public GaussianStrPointInfoDTO eve_releaseDronePoint = new GaussianStrPointInfoDTO("1892,770", "1900,780");
        // 接近建筑点
        public GaussianStrPointInfoDTO eve_approachPoint = new GaussianStrPointInfoDTO("1701,161", "1709,171");

    }


    @Data
    public static class CommonEvePoint {
        // 任务栏打开窗口位置
        public GaussianStrPointInfoDTO eve_openSelectPoint = new GaussianStrPointInfoDTO("1212,1051");
    }


    @Data
    public static class PrepareEvePoint {
        // 太空空点
        public GaussianStrPointInfoDTO eve_emptySpacePoint = new GaussianStrPointInfoDTO("908,659", "993,713");
        // 星图按钮点
        public GaussianStrPointInfoDTO eve_spaceMapPoint = new GaussianStrPointInfoDTO("13,456", "21,462");
        // 维修装备点
        public GaussianStrPointInfoDTO eve_reparePoint = new GaussianStrPointInfoDTO("1414,971", "1429,980");
    }


    @Data
    public static class EveEscapePoint {
        // 特殊tab，跑路用
        public GaussianStrPointInfoDTO eve_escapeTabPoint_1 = new GaussianStrPointInfoDTO("1850,231", "1862,238");
        public GaussianStrPointInfoDTO eve_escapeTabPoint_2 = new GaussianStrPointInfoDTO("1846,230", "1865,239");
        public GaussianStrPointInfoDTO eve_escapeTabPoint_3 = new GaussianStrPointInfoDTO("1843,227", "1862,238");
        // 停靠建筑
        public GaussianStrPointInfoDTO eve_dockingBuildingPoint_1 = new GaussianStrPointInfoDTO("1622,290", "1650,298");
        public GaussianStrPointInfoDTO eve_dockingBuildingPoint_2 = new GaussianStrPointInfoDTO("1625,288", "1644,299");
        public GaussianStrPointInfoDTO eve_dockingBuildingPoint_3 = new GaussianStrPointInfoDTO("1616,288", "1634,299");
        // 朝向按钮
        public GaussianStrPointInfoDTO eve_alignToPoint_1 = new GaussianStrPointInfoDTO("1588,153", "1602,163");
        public GaussianStrPointInfoDTO eve_alignToPoint_2 = new GaussianStrPointInfoDTO("1590,154", "1601,163");
        public GaussianStrPointInfoDTO eve_alignToPoint_3 = new GaussianStrPointInfoDTO("1591,150", "1600,161");
        // 停靠按钮
        public GaussianStrPointInfoDTO eve_dockPoint_1 = new GaussianStrPointInfoDTO("1663,153", "1676,162");
        public GaussianStrPointInfoDTO eve_dockPoint_2 = new GaussianStrPointInfoDTO("1664,151", "1678,165");
        public GaussianStrPointInfoDTO eve_dockPoint_3 = new GaussianStrPointInfoDTO("1665,151", "1678,163");
    }

    public static void eveEscapeAll(List<AccountPoint> accountPoints, Integer num) {
        CommonEvePoint commonEvePoint = new CommonEvePoint();
        for (int i = 1; i <= accountPoints.size(); i++) {
            if (num != null && num != i) {
                continue;
            }
            AccountPoint accountPoint = accountPoints.get(i - 1);
            EventHandle_Main.openOpeNew(commonEvePoint.getEve_openSelectPoint(), accountPoint.getEve_selectPoint());
            eveEscape_sub(accountPoint);
        }
    }

    public static void eveEscape_sub(AccountPoint accountPoint) {
        // 选中跑路tab
        RobotHelper.move(accountPoint.getEve_escapeTabPoint());
        RobotHelper.mouseLeftPress();
        // 选中建筑
        RobotHelper.move(accountPoint.getEve_dockingBuildingPoint());
        RobotHelper.mouseLeftPress();
        // 朝向建筑
        RobotHelper.move(accountPoint.getEve_alignToPoint());
        RobotHelper.mouseLeftPress();
        RobotHelper.delay(GaussianHelper.getGaussianInt(130, 170));
        RobotHelper.mouseLeftPress();
        // 跃迁进入建筑
        RobotHelper.move(accountPoint.getEve_dockPoint(), 1534);
        RobotHelper.mouseLeftPress();
        RobotHelper.delay(GaussianHelper.getGaussianInt(1530, 1770));
        RobotHelper.mouseLeftPress();
    }


    public static void openAll(List<AccountPoint> accountPoints, Integer num) {
        CommonEvePoint commonEvePoint = new CommonEvePoint();
        for (int i = 1; i <= accountPoints.size(); i++) {
            if (num != null && num != i) {
                continue;
            }
            AccountPoint accountPoint = accountPoints.get(i - 1);
            EventHandle_Main.openOpeNew(commonEvePoint.getEve_openSelectPoint(), accountPoint.getEve_selectPoint());
        }
    }

    public static void doHarvestAll(List<AccountPoint> accountPoints, Integer num) {
        CommonEvePoint commonEvePoint = new CommonEvePoint();
        for (int i = 1; i <= accountPoints.size(); i++) {
            if (num != null && num != i) {
                continue;
            }
            AccountPoint accountPoint = accountPoints.get(i - 1);
            EventHandle_Main.openOpeNew(commonEvePoint.getEve_openSelectPoint(), accountPoint.getEve_selectPoint());
            EveToWorkPoint eveToWorkPoint = new EveToWorkPoint();
            // release 释放无人机
            RobotHelper.move(eveToWorkPoint.getEve_releaseDronePoint(), 94);
            RobotHelper.mouseLeftPress();
            RobotHelper.delay(GaussianHelper.getGaussianInt(615, 770));
            // 释放箱子并存点
            doHarvest_sub(eveToWorkPoint);
            // 环绕建筑
            doHarvest_sub2(eveToWorkPoint);
            // speedUp 加速
            RobotHelper.move(eveToWorkPoint.getEve_speedUpPoint(), 94);
            RobotHelper.mouseLeftPress();
            RobotHelper.delay(GaussianHelper.getGaussianInt(815, 970));
        }
    }

    private static void doHarvest_sub(EveToWorkPoint eveToWorkPoint) {
        EveHarvestPoint eveHarvestPoint = new EveHarvestPoint();
        // 牵引箱子点
        RobotHelper.move(eveHarvestPoint.getEve_mutPoint());
        RobotHelper.mouseRightPress();
        RobotHelper.delay(GaussianHelper.getGaussianInt(1060, 1100));
        // 释放牵引箱子点
        RobotHelper.move(eveHarvestPoint.getEve_releaseMutPoint());
        RobotHelper.mouseLeftPress();
        RobotHelper.delay(GaussianHelper.getGaussianInt(510, 600));

        // 堆叠所有
        RobotHelper.move(eveHarvestPoint.getEve_stackMutPoint());
        RobotHelper.mouseLeftPress();
        RobotHelper.delay(GaussianHelper.getGaussianInt(810, 1000));


        // tab1 切换 作战tab
        RobotHelper.move(eveToWorkPoint.getEve_fightingTabPoint());
        RobotHelper.mouseLeftPress();
        RobotHelper.delay(GaussianHelper.getGaussianInt(1060, 1100));

        // 选中牵引箱子
        RobotHelper.move(eveHarvestPoint.getEve_spaceMutPoint());
        RobotHelper.mouseRightPress();
        RobotHelper.delay(GaussianHelper.getGaussianInt(1060, 1100));

        // 保存牵引箱子点
        RobotHelper.move(eveHarvestPoint.getEve_saveMutPointPoint());
        RobotHelper.mouseLeftPress();
        RobotHelper.delay(GaussianHelper.getGaussianInt(540, 570));
        RobotHelper.keyPress(KeyEvent.VK_ENTER);


        RobotHelper.delay(GaussianHelper.getGaussianInt(1060, 1100));
        // tab0 切换 生产tab
        RobotHelper.move(eveToWorkPoint.getEve_buildingTabPoint());
        RobotHelper.mouseLeftPress();
        RobotHelper.delay(GaussianHelper.getGaussianInt(260, 500));
    }

    private static void doHarvest_sub2(EveToWorkPoint eveToWorkPoint) {
        // building 选中建筑
        RobotHelper.move(eveToWorkPoint.getEve_buildingPoint(), 88);
        RobotHelper.mouseLeftPress();
        RobotHelper.delay(GaussianHelper.getGaussianInt(330, 370));

        try {
            Collections.shuffle(MIDDLE_ACTION_LIST);
            for (MyConsumer myConsumer : MIDDLE_ACTION_LIST) {
                myConsumer.accept(null);
            }
        } catch (Exception e) {

        }
    }


    @Data
    public static class CommonReplicaPoint {
        // 任务栏搜索按钮
        public GaussianStrPointInfoDTO replica_searchPoint1 = new GaussianStrPointInfoDTO("89,1056");
        // 输入OnTopReplica位置
        public GaussianStrPointInfoDTO replica_searchPoint2 = new GaussianStrPointInfoDTO("126,1003");
        // 点击启动OnTopReplica位置
        public GaussianStrPointInfoDTO replica_startPoint = new GaussianStrPointInfoDTO("198,372");
        // 右键点击空白OnTopReplica位置
        public GaussianStrPointInfoDTO replica_rightClickEmptyPoint = new GaussianStrPointInfoDTO("357,297");
        // OnTopReplica锁定位置
        public GaussianStrPointInfoDTO replica_lockMenuPoint = new GaussianStrPointInfoDTO("440,468");
        // OnTopReplica锁定左上位置
        public GaussianStrPointInfoDTO replica_lockLeftPoint = new GaussianStrPointInfoDTO("589,495");
        // OnTopReplica锁定后标准起始位置
        public GaussianStrPointInfoDTO replica_lockedStartPoint = new GaussianStrPointInfoDTO("61,55");
        // OnTopReplica选择窗口菜单
        public GaussianStrPointInfoDTO replica_selectWindowMenuPoint = new GaussianStrPointInfoDTO("136,72");
        // OnTopReplica选择放缩菜单
        public GaussianStrPointInfoDTO replica_percentageWindowMenuPoint = new GaussianStrPointInfoDTO("162,202");
        // OnTopReplica选择放缩1:2菜单
        public GaussianStrPointInfoDTO replica_50PercentageWindowMenuPoint = new GaussianStrPointInfoDTO("322,254");
        // OnTopReplica选择区域
        public GaussianStrPointInfoDTO replica_selectAreaPoint = new GaussianStrPointInfoDTO("132,123");
        // OnTopReplica区域起点
        public GaussianStrPointInfoDTO replica_areaStartPoint = new GaussianStrPointInfoDTO("770,68");
        // OnTopReplica区域终点
        public GaussianStrPointInfoDTO replica_areaEndPoint = new GaussianStrPointInfoDTO("793,346");
        // OnTopReplica区域选择完成按钮
        public GaussianStrPointInfoDTO replica_areaDoneButtonPoint = new GaussianStrPointInfoDTO("1252,278");
        // OnTopReplica拖动放缩起点
        public GaussianStrPointInfoDTO replica_scrollStartPoint = new GaussianStrPointInfoDTO("86,999");
        // OnTopReplica拖动放缩终点
        public GaussianStrPointInfoDTO replica_scrollEndPoint = new GaussianStrPointInfoDTO("58,888");
        // OnTopReplica准备解除锁定右键位置
        public GaussianStrPointInfoDTO replica_beforeUnlockMenuPoint = new GaussianStrPointInfoDTO("51,55");
        // OnTopReplica解除锁定菜单位置
        public GaussianStrPointInfoDTO replica_unlockMenuPoint = new GaussianStrPointInfoDTO("127,224");
        // OnTopReplica解除锁定
        public GaussianStrPointInfoDTO replica_unlockPoint = new GaussianStrPointInfoDTO("291,229");
        // OnTopReplica解除锁定
        public GaussianStrPointInfoDTO replica_commonMoveStartPoint = new GaussianStrPointInfoDTO("45,55");

    }

    public static void eveEndWorkAll(List<AccountPoint> accountPoints, Integer num) {
        CommonEvePoint commonEvePoint = new CommonEvePoint();
        for (int i = 1; i <= accountPoints.size(); i++) {
            if (num != null && num != i) {
                continue;
            }
            AccountPoint accountPoint = accountPoints.get(i - 1);
            EventHandle_Main.openOpeNew(commonEvePoint.getEve_openSelectPoint(), accountPoint.getEve_selectPoint());
            eveEndWork_sub();
        }
    }

    private static void eveEndWork_sub() {

        EveToWorkPoint eveToWorkPoint = new EveToWorkPoint();
        // 靠近建筑
        RobotHelper.move(eveToWorkPoint.getEve_approachPoint());
        RobotHelper.mouseLeftPress();
        RobotHelper.delay(GaussianHelper.getGaussianInt(330, 370));

        // tab0 切换 生产tab
        RobotHelper.move(eveToWorkPoint.getEve_buildingTabPoint());
        RobotHelper.mouseLeftPress();
        RobotHelper.delay(GaussianHelper.getGaussianInt(600, 870));

        // 回收无人机
        RobotHelper.keyPress(KeyEvent.VK_R);
        RobotHelper.delay(GaussianHelper.getGaussianInt(900, 970));
    }


    public static void eveToWorkAll(List<AccountPoint> accountPoints, Integer num) throws BusinessException {
        CommonEvePoint commonEvePoint = new CommonEvePoint();
        for (int i = 1; i <= accountPoints.size(); i++) {
            if (num != null && num != i) {
                continue;
            }
            AccountPoint accountPoint = accountPoints.get(i - 1);
            EventHandle_Main.openOpeNew(commonEvePoint.getEve_openSelectPoint(), accountPoint.getEve_selectPoint());
            RobotHelper.delay(200);
            eveToWork_sub();
            speedUpAndDroneAndSavePoint();
        }
    }

    // 中间两项操作。切回作战tab、环绕
    public static List<MyConsumer> MIDDLE_ACTION_LIST = middleActionList();

    private static void eveToWork_sub() throws BusinessException {
        EveToWorkPoint eveToWorkPoint = new EveToWorkPoint();
        // 选中环绕
        RobotHelper.delay(GaussianHelper.getGaussianInt(400, 470));
        // tab0 切换 生产tab
        RobotHelper.move(eveToWorkPoint.getEve_buildingTabPoint(), 94);
        RobotHelper.mouseLeftPress();
        RobotHelper.delay(GaussianHelper.getGaussianInt(600, 870));

        // building 选中建筑
        RobotHelper.move(eveToWorkPoint.getEve_buildingPoint(), 88);
        RobotHelper.mouseLeftPress();
        RobotHelper.delay(GaussianHelper.getGaussianInt(330, 370));

        Collections.shuffle(MIDDLE_ACTION_LIST);
        for (MyConsumer myConsumer : MIDDLE_ACTION_LIST) {
            myConsumer.accept(null);
        }
    }

    private static List<MyConsumer> middleActionList() {
        EveToWorkPoint eveToWorkPoint = new EveToWorkPoint();
        List<MyConsumer> list = new ArrayList<>();
        list.add((e) -> {
            // tab1 切换 作战tab
            RobotHelper.move(eveToWorkPoint.getEve_fightingTabPoint(), 100);
            RobotHelper.mouseLeftPress();
            RobotHelper.delay(GaussianHelper.getGaussianInt(1060, 1100));
        });
        list.add((e) -> {
            // around 环绕建筑
            RobotHelper.move(eveToWorkPoint.getEve_aroundPoint(), 100);
            RobotHelper.mouseLeftPress();
            RobotHelper.delay(GaussianHelper.getGaussianInt(30, 70));
            RobotHelper.mouseLeftPress();
            RobotHelper.delay(GaussianHelper.getGaussianInt(450, 600));
        });
        return list;
    }


    // 最后三项操作。加速、环绕、存点
    public static List<MyConsumer> FINAL_ACTION_LIST = finalActionList();

    private static void speedUpAndDroneAndSavePoint() throws BusinessException {
        List<MyConsumer> executeList = FINAL_ACTION_LIST;
        boolean shuffle = Math.random() * 10 < 1;
        if (shuffle) {
            List<MyConsumer> tempList = new ArrayList<>(FINAL_ACTION_LIST);
            Collections.shuffle(tempList);
            executeList = tempList;
        }
        for (MyConsumer myConsumer : executeList) {
            myConsumer.accept(null);
        }
        RobotHelper.delay(GaussianHelper.getGaussianInt(200, 270));
    }

    private static List<MyConsumer> finalActionList() {
        EveToWorkPoint eveToWorkPoint = new EveToWorkPoint();
        List<MyConsumer> list = new ArrayList<>();
        list.add((e) -> {
            // speedUp 加速
            RobotHelper.move(eveToWorkPoint.getEve_speedUpPoint(), 94);
            RobotHelper.mouseLeftPress();
            RobotHelper.delay(GaussianHelper.getGaussianInt(815, 970));
        });
        list.add((e) -> {
            // release 释放无人机
            RobotHelper.move(eveToWorkPoint.getEve_releaseDronePoint(), 94);
            RobotHelper.mouseLeftPress();
            RobotHelper.delay(GaussianHelper.getGaussianInt(615, 770));
        });
        list.add((e) -> {
            // 保存点
            savePoint();
        });
        return list;
    }

    private static void savePoint() {
        RobotHelper.keyPress(KeyEvent.VK_CONTROL, KeyEvent.VK_B);
        RobotHelper.delay(GaussianHelper.getGaussianInt(240, 370));
        RobotHelper.keyPress(KeyEvent.VK_ENTER);
    }

    public static void evePrepareAll(List<AccountPoint> accountPoints, Integer num) {
        CommonEvePoint commonEvePoint = new CommonEvePoint();
        for (int i = 1; i <= accountPoints.size(); i++) {
            if (num != null && num != i) {
                continue;
            }
            AccountPoint accountPoint = accountPoints.get(i - 1);
            EventHandle_Main.openOpeNew(commonEvePoint.getEve_openSelectPoint(), accountPoint.getEve_selectPoint());
            RobotHelper.delay(200);
            evePrepare_sub();
        }

    }

    private static void evePrepare_sub() {
        PrepareEvePoint prepareEvePoint = new PrepareEvePoint();

        // 单击太空
        RobotHelper.move(prepareEvePoint.getEve_emptySpacePoint());
        RobotHelper.mouseLeftPress();

        // 滑动鼠标，拉远舰船
        RobotHelper.delay(GaussianHelper.getGaussianInt(424, 470));
        for (int i = 0; i < 5; i++) {
            RobotHelper.mouseWheel(-4);
        }

        // ctrl+shift+f9
        RobotHelper.delay(GaussianHelper.getGaussianInt(524, 670));
        RobotHelper.keyPress(KeyEvent.VK_CONTROL, KeyEvent.VK_SHIFT, KeyEvent.VK_F9);

        // 打开星图
        RobotHelper.move(prepareEvePoint.getEve_spaceMapPoint());
        RobotHelper.mouseLeftPress();
        RobotHelper.delay(GaussianHelper.getGaussianInt(3024, 3370));

        // 单击太空
        RobotHelper.move(prepareEvePoint.getEve_emptySpacePoint());
        RobotHelper.mouseLeftPress();
        // 滑动鼠标，拉近星图
        RobotHelper.delay(GaussianHelper.getGaussianInt(424, 470));
        RobotHelper.mouseWheel(3);
        RobotHelper.mouseWheel(2);
        RobotHelper.delay(GaussianHelper.getGaussianInt(424, 470));

        // 启动维修
        RobotHelper.move(prepareEvePoint.getEve_reparePoint());
        RobotHelper.mouseLeftPress();
        RobotHelper.delay(GaussianHelper.getGaussianInt(424, 470));

    }

    public static void onTopReplicaPrepare(List<AccountPoint> accountPoints, Integer num) {
        CommonReplicaPoint commonReplicaPoint = new CommonReplicaPoint();
        CommonEvePoint commonEvePoint = new CommonEvePoint();
        List<AccountPoint> reverse = Lists.reverse(accountPoints);
        for (AccountPoint accountPoint : reverse) {
            EventHandle_Main.openOpeNew(commonEvePoint.getEve_openSelectPoint(), accountPoint.getEve_selectPoint());
            RobotHelper.delay(200);
        }
        for (int i = 1; i <= accountPoints.size(); i++) {
            if (num != null && num != i) {
                continue;
            }
            AccountPoint accountPoint = accountPoints.get(i - 1);
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

//        // 拖动放缩分屏窗口
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
        RobotHelper.delay(200);
        RobotHelper.move(555, 555);


    }

    public static void linkDrone(List<AccountPoint> accountPoints, Integer num) throws BusinessException {
        CommonEvePoint commonEvePoint = new CommonEvePoint();
        for (int i = 1; i <= accountPoints.size(); i++) {
            if (num != null && num != i) {
                continue;
            }
            AccountPoint accountPoint = accountPoints.get(i - 1);
            EventHandle_Main.openOpeNew(commonEvePoint.getEve_openSelectPoint(), accountPoint.getEve_selectPoint());
            linkDrone_sub();
        }
    }

    private static void linkDrone_sub() throws BusinessException {
        EveToWorkPoint eveToWorkPoint = new EveToWorkPoint();

        RobotHelper.delay(GaussianHelper.getGaussianInt(400, 470));

        // tab0 切换 生产tab
        RobotHelper.move(eveToWorkPoint.getEve_buildingTabPoint());
        RobotHelper.mouseLeftPress();

        RobotHelper.delay(GaussianHelper.getGaussianInt(400, 470));
        // 联系无人机
        RobotHelper.keyPress(KeyEvent.VK_N);
        RobotHelper.delay(GaussianHelper.getGaussianInt(2500, 2770));

        // 回收无人机
        RobotHelper.keyPress(KeyEvent.VK_R);
        RobotHelper.delay(GaussianHelper.getGaussianInt(200, 270));


        // building 选中建筑
        RobotHelper.move(eveToWorkPoint.getEve_buildingPoint());
        RobotHelper.mouseLeftPress();
        RobotHelper.delay(GaussianHelper.getGaussianInt(330, 370));


        // 环绕+切回作战tab
        for (MyConsumer myConsumer : MIDDLE_ACTION_LIST) {
            myConsumer.accept(null);
        }
    }


}

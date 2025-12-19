package com.tester.testerswing.swing.eventHandler;

import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener;
import com.google.common.collect.Lists;
import com.tester.base.dto.exception.BusinessException;
import com.tester.testercommon.util.MyConsumer;
import com.tester.testerswing.boot.AccountInfo;
import com.tester.testerswing.boot.Boot;
import com.tester.testerswing.capture.GaussianStrPointInfoDTO;
import com.tester.testerswing.capture.PointInfoDTO;
import com.tester.testerswing.gaussian.GaussianHelper;
import com.tester.testerswing.robot.RobotHelper;
import lombok.Data;

import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

@Data
public class PointHelper {

    public static List<AccountPoint> list = new ArrayList<>();

    public static Map<Integer, List<GaussianStrPointInfoDTO>> spListMap = new HashMap<>();

    public static List<AccountPoint> getListAll() {
        return list;
    }

    public static List<AccountPoint> getList() {
        return list.stream().limit(Boot.getAccountNum()).collect(Collectors.toList());
    }

    private static final AtomicBoolean shouldStop = new AtomicBoolean(false);




    static {
        spListMap = initPointMap();
        AccountPoint silot = new AccountPoint();
        silot.setSerialNo(1);
        silot.setEve_selectPoint(new GaussianStrPointInfoDTO("445,953"));
        silot.setReplica_selectPoint(new GaussianStrPointInfoDTO("304,99"));
        silot.setReplica_moveEndPoint(new GaussianStrPointInfoDTO("80,58"));//1
        silot.setRedSt(new PointInfoDTO(37,20));
        silot.setRedEd(new PointInfoDTO(54,503));
        silot.setEnemySt(new PointInfoDTO(80,35));
        silot.setEnemyEd(new PointInfoDTO(99,56));
        silot.setName("one");
        silot.setWarnMsg("1警告");


        AccountPoint sailinaa = new AccountPoint();
        sailinaa.setSerialNo(2);
        sailinaa.setEve_selectPoint(new GaussianStrPointInfoDTO("702,960"));
        sailinaa.setReplica_selectPoint(new GaussianStrPointInfoDTO("362,123"));
        sailinaa.setReplica_moveEndPoint(new GaussianStrPointInfoDTO("80,574"));//2
        sailinaa.setRedSt(new PointInfoDTO(37,533));
        sailinaa.setRedEd(new PointInfoDTO(54,1005));
        sailinaa.setEnemySt(new PointInfoDTO(80,548));
        sailinaa.setEnemyEd(new PointInfoDTO(99,569));
        sailinaa.setName("two");
        sailinaa.setWarnMsg("2警告");


        AccountPoint colos = new AccountPoint();
        colos.setSerialNo(3);
        colos.setEve_selectPoint(new GaussianStrPointInfoDTO("975,936"));
        colos.setReplica_selectPoint(new GaussianStrPointInfoDTO("361,152"));
        colos.setReplica_moveEndPoint(new GaussianStrPointInfoDTO("147,58"));//3
        colos.setRedSt(new PointInfoDTO(105,20));
        colos.setRedEd(new PointInfoDTO(123,503));
        colos.setEnemySt(new PointInfoDTO(148,35));
        colos.setEnemyEd(new PointInfoDTO(167,56));
        colos.setName("three");
        colos.setWarnMsg("3警告");


        AccountPoint four = new AccountPoint();
        four.setSerialNo(4);
        four.setEve_selectPoint(new GaussianStrPointInfoDTO("1240,950"));
        four.setReplica_selectPoint(new GaussianStrPointInfoDTO("327,173"));
        four.setReplica_moveEndPoint(new GaussianStrPointInfoDTO("147,574"));//4
        four.setRedSt(new PointInfoDTO(105,533));
        four.setRedEd(new PointInfoDTO(123,1005));
        four.setEnemySt(new PointInfoDTO(148,548));
        four.setEnemyEd(new PointInfoDTO(167,569));
        four.setName("four");
        four.setWarnMsg("4警告");



        AccountPoint five = new AccountPoint();
        five.setSerialNo(5);
        five.setEve_selectPoint(new GaussianStrPointInfoDTO("1476,947"));
        five.setReplica_selectPoint(new GaussianStrPointInfoDTO("327,200"));
        five.setReplica_moveEndPoint(new GaussianStrPointInfoDTO("214,58"));//5
        five.setRedSt(new PointInfoDTO(170,20));
        five.setRedEd(new PointInfoDTO(189,503));
        five.setEnemySt(new PointInfoDTO(213,35));
        five.setEnemyEd(new PointInfoDTO(232,56));
        five.setName("five");
        five.setWarnMsg("5警告");



        AccountPoint six = new AccountPoint();
        six.setSerialNo(6);
        six.setEve_selectPoint(new GaussianStrPointInfoDTO("1735,942"));
        six.setReplica_selectPoint(new GaussianStrPointInfoDTO("327,225"));
        six.setReplica_moveEndPoint(new GaussianStrPointInfoDTO("214,574"));//6
        six.setRedSt(new PointInfoDTO(170,533));
        six.setRedEd(new PointInfoDTO(189,1005));
        six.setEnemySt(new PointInfoDTO(213,548));
        six.setEnemyEd(new PointInfoDTO(232,569));
        six.setName("six");
        six.setWarnMsg("6警告");


//        silot.setReplica_moveEndPoint(new GaussianStrPointInfoDTO("245,58"));//7
//        silot.setReplica_moveEndPoint(new GaussianStrPointInfoDTO("245,574"));//8


        list.add(silot);
        list.add(sailinaa);
        list.add(colos);
        list.add(four);
        list.add(five);
        list.add(six);
    }


    @Data
    public static class AccountPoint {
        private Integer serialNo;
        // 点击任务栏选择账号窗口
        public GaussianStrPointInfoDTO eve_selectPoint;
        // onTopReplica 窗口选择账号点
        public GaussianStrPointInfoDTO replica_selectPoint;
        // onTopReplica 窗口选择账号点
        public GaussianStrPointInfoDTO replica_moveEndPoint;

        // red 截图起点
        private PointInfoDTO redSt;
        // red 截图终点
        private PointInfoDTO redEd;
        // 敌对 截图起点
        private PointInfoDTO enemySt;
        // 敌对 截图终点
        private PointInfoDTO enemyEd;

        private String name;

        private String warnMsg;

        private AccountInfo accountInfo;

        public GaussianStrPointInfoDTO getEve_selectPoint() {
            List<GaussianStrPointInfoDTO> gaussianStrPointInfoDTOS = spListMap.get(Boot.getAccountNum());
            GaussianStrPointInfoDTO gaussianStrPointInfoDTO = gaussianStrPointInfoDTOS.get(this.serialNo - 1);
            return gaussianStrPointInfoDTO;
        }

        public void setEve_selectPoint(GaussianStrPointInfoDTO eve_selectPoint) {
            this.eve_selectPoint = eve_selectPoint;
        }
    }

    private static Map<Integer, List<GaussianStrPointInfoDTO>> initPointMap(){
        Map<Integer, List<GaussianStrPointInfoDTO>> spMap = new HashMap<>();
        ArrayList<GaussianStrPointInfoDTO> _3Account = new ArrayList<>();
        ArrayList<GaussianStrPointInfoDTO> _4Account = new ArrayList<>();
        ArrayList<GaussianStrPointInfoDTO> _5Account = new ArrayList<>();
        ArrayList<GaussianStrPointInfoDTO> _6Account = new ArrayList<>();
        spMap.put(3, _3Account);
        spMap.put(4, _4Account);
        spMap.put(5, _5Account);
        spMap.put(6, _6Account);

        _3Account.add(new GaussianStrPointInfoDTO("958,924"));
        _3Account.add(new GaussianStrPointInfoDTO("1186,935"));
        _3Account.add(new GaussianStrPointInfoDTO("1449,946"));

        _4Account.add(new GaussianStrPointInfoDTO("801,940"));
        _4Account.add(new GaussianStrPointInfoDTO("1087,941"));
        _4Account.add(new GaussianStrPointInfoDTO("1338,939"));
        _4Account.add(new GaussianStrPointInfoDTO("1535,951"));

        _5Account.add(new GaussianStrPointInfoDTO("686,937"));
        _5Account.add(new GaussianStrPointInfoDTO("924,944"));
        _5Account.add(new GaussianStrPointInfoDTO("1202,936"));
        _5Account.add(new GaussianStrPointInfoDTO("1458,939"));
        _5Account.add(new GaussianStrPointInfoDTO("1674,953"));

        _6Account.add(new GaussianStrPointInfoDTO("445,953"));
        _6Account.add(new GaussianStrPointInfoDTO("702,960"));
        _6Account.add(new GaussianStrPointInfoDTO("975,936"));
        _6Account.add(new GaussianStrPointInfoDTO("1240,950"));
        _6Account.add(new GaussianStrPointInfoDTO("1476,947"));
        _6Account.add(new GaussianStrPointInfoDTO("1735,942"));

        return spMap;
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
        // 残骸环绕点
        public GaussianStrPointInfoDTO eve_aroundTrashPoint = new GaussianStrPointInfoDTO("1696,161", "1708,171");
        // 启动加速点
        public GaussianStrPointInfoDTO eve_speedUpPoint = new GaussianStrPointInfoDTO("1107,878", "1122,891");
        // 释放无人机点
        public GaussianStrPointInfoDTO eve_releaseDronePoint = new GaussianStrPointInfoDTO("1892,745", "1902,751");
        public GaussianStrPointInfoDTO eve_releaseDronePoint_new = new GaussianStrPointInfoDTO("1892,768", "1901,774");
        // 接近建筑点
        public GaussianStrPointInfoDTO eve_approachPoint = new GaussianStrPointInfoDTO("1701,161", "1709,171");

    }


    @Data
    public static class CommonEvePoint {
        // 任务栏打开窗口位置
        public GaussianStrPointInfoDTO eve_openSelectPoint = new GaussianStrPointInfoDTO("1212,1051");
        // 最小化
        public GaussianStrPointInfoDTO eve_minimize = new GaussianStrPointInfoDTO("1755,6","1779,18");

        // 特殊tab，跑路用
        public GaussianStrPointInfoDTO eve_escapeTabPoint = new GaussianStrPointInfoDTO("1840,228", "1865,235");
        // 停靠建筑
        public GaussianStrPointInfoDTO eve_dockingBuildingPoint = new GaussianStrPointInfoDTO("1627,289", "1653,294");
        // 朝向按钮
        public GaussianStrPointInfoDTO eve_alignToPoint = new GaussianStrPointInfoDTO("1581,159", "1598,168");
        // 停靠按钮
        public GaussianStrPointInfoDTO eve_dockPoint = new GaussianStrPointInfoDTO("1661,154", "1675,161");
        // 首tab
        public GaussianStrPointInfoDTO eve_firstTabPoint = new GaussianStrPointInfoDTO("1590,229","1605,237");

        // 本地账号截图起点
        public PointInfoDTO eve_localStPoint = new PointInfoDTO(1409,260);

        // 本地账号截图终点
        public PointInfoDTO eve_localEdPoint = new PointInfoDTO(1536,678);


    }


    @Data
    public static class PrepareEvePoint {
        // 太空空点
        public GaussianStrPointInfoDTO eve_emptySpacePoint = new GaussianStrPointInfoDTO("908,659", "993,713");
        // 星图按钮点
        public GaussianStrPointInfoDTO eve_spaceMapPoint = new GaussianStrPointInfoDTO("13,456", "21,462");
        // 维修装备点
        public GaussianStrPointInfoDTO eve_reparePoint = new GaussianStrPointInfoDTO("1414,971", "1429,980");
        // 脚本装备点
        public GaussianStrPointInfoDTO eve_scriptPoint = new GaussianStrPointInfoDTO("1363,971", "1380,984");


        // 装备位置：第2行，第1位
        public GaussianStrPointInfoDTO eve_reparePoint_2_1 = new GaussianStrPointInfoDTO("1084,921", "1099,935");

        // 装备位置：第2行，第2位
        public GaussianStrPointInfoDTO eve_scriptPoint_2_2 = new GaussianStrPointInfoDTO("1132,919", "1150,936");

        // 装备位置：第2行，第3位
        public GaussianStrPointInfoDTO eve_scriptPoint_2_3 = new GaussianStrPointInfoDTO("1185,922", "1202,937");

        // 装备位置：第2行，第4位
        public GaussianStrPointInfoDTO eve_scriptPoint_2_4 = new GaussianStrPointInfoDTO("1237,919", "1250,936");

        // 装备位置：第2行，第5位
        public GaussianStrPointInfoDTO eve_scriptPoint_2_5 = new GaussianStrPointInfoDTO("1291,919", "1302,934");

        // 装备位置：第2行，第6位
        public GaussianStrPointInfoDTO eve_scriptPoint_2_6 = new GaussianStrPointInfoDTO("1339,918", "1356,936");

        // 装备位置：第2行，第7位
        public GaussianStrPointInfoDTO eve_scriptPoint_2_7 = new GaussianStrPointInfoDTO("1391,919", "1405,935");

        // 装备位置：第2行，第8位
        public GaussianStrPointInfoDTO eve_scriptPoint_2_8 = new GaussianStrPointInfoDTO("1439,921", "1453,937");



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


    public static void toStandByAll(List<AccountPoint> accountPoints, Integer num) {
        CommonEvePoint commonEvePoint = new CommonEvePoint();
        EveToWorkPoint eveToWorkPoint = new EveToWorkPoint();
        for (int i = 1; i <= accountPoints.size(); i++) {
            if (num != null && num != i) {
                continue;
            }
            AccountPoint accountPoint = accountPoints.get(i - 1);
            EventHandle_Main.openOpeNew(commonEvePoint.getEve_openSelectPoint(), accountPoint.getEve_selectPoint());

            // tab1 切换 作战tab
            RobotHelper.move(eveToWorkPoint.getEve_fightingTabPoint(), 100);
            RobotHelper.mouseLeftPress();
            RobotHelper.delay(GaussianHelper.getGaussianInt(360, 450));
            AccountInfo accountInfo = accountPoint.getAccountInfo();
            if(!Objects.equals(accountInfo.getSerialNo(),accountInfo.getLeaderSerialNo())) {
                // 最小化
                minimize(commonEvePoint.getEve_minimize());
            }
        }
    }
    public static void toWatchAll(List<AccountPoint> accountPoints, Integer num) {
        CommonEvePoint commonEvePoint = new CommonEvePoint();
        PrepareEvePoint prepareEvePoint = new PrepareEvePoint();
        for (int i = 1; i <= accountPoints.size(); i++) {
            if (num != null && num != i) {
                continue;
            }
            AccountPoint accountPoint = accountPoints.get(i - 1);
            EventHandle_Main.openOpeNew(commonEvePoint.getEve_openSelectPoint(), accountPoint.getEve_selectPoint());

            // 打开警戒tab
            RobotHelper.move(commonEvePoint.getEve_firstTabPoint(),203);
            RobotHelper.mouseLeftPress();
            RobotHelper.delay(GaussianHelper.getGaussianInt(200, 270));
        }

    }

    public static void eveEscapeAll(List<AccountPoint> accountPoints, Integer num) {
        CommonEvePoint commonEvePoint = new CommonEvePoint();
        for (int i = 1; i <= accountPoints.size(); i++) {
            if (num != null && num != i) {
                continue;
            }
            AccountPoint accountPoint = accountPoints.get(i - 1);
            EventHandle_Main.openOpeNew(commonEvePoint.getEve_openSelectPoint(), accountPoint.getEve_selectPoint());
            eveEscape_sub(commonEvePoint);

            // 最小化
            minimize(commonEvePoint.getEve_minimize());
        }
    }

    private static void eveEscape_sub(CommonEvePoint commonEvePoint) {
        // 选中跑路tab
        RobotHelper.move(commonEvePoint.getEve_escapeTabPoint());
        RobotHelper.mouseLeftPress();
        // 选中建筑
        RobotHelper.move(commonEvePoint.getEve_dockingBuildingPoint());
        RobotHelper.mouseLeftPress();
        // 朝向建筑
        RobotHelper.move(commonEvePoint.getEve_alignToPoint());
        RobotHelper.mouseLeftPress();
        RobotHelper.delay(GaussianHelper.getGaussianInt(130, 170));
        RobotHelper.mouseLeftPress();
        // 跃迁进入建筑
        RobotHelper.move(commonEvePoint.getEve_dockPoint(), 1534);
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

    public static void doPureHarvestAll(List<AccountPoint> accountPoints, Integer num) {
        CommonEvePoint commonEvePoint = new CommonEvePoint();
        for (int i = 1; i <= accountPoints.size(); i++) {
            if (num != null && num != i) {
                continue;
            }
            AccountPoint accountPoint = accountPoints.get(i - 1);
            EventHandle_Main.openOpeNew(commonEvePoint.getEve_openSelectPoint(), accountPoint.getEve_selectPoint());
            EveToWorkPoint eveToWorkPoint = new EveToWorkPoint();
            // 释放箱子并存点
            doHarvest_sub(eveToWorkPoint);
            // 环绕建筑
            doHarvest_sub2(eveToWorkPoint);
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
            releaseDrone_ori(eveToWorkPoint);
            // 释放箱子并存点
            doHarvest_sub(eveToWorkPoint);
            // 环绕建筑
            doHarvest_sub2(eveToWorkPoint);
            // speedUp 加速
            RobotHelper.move(eveToWorkPoint.getEve_speedUpPoint(), 94);
            RobotHelper.mouseLeftPress();
            RobotHelper.delay(GaussianHelper.getGaussianInt(615, 770));

            // 最小化
            minimize(commonEvePoint.getEve_minimize());
        }
    }

    private static void doHarvest_sub(EveToWorkPoint eveToWorkPoint) {
        EveHarvestPoint eveHarvestPoint = new EveHarvestPoint();
        // 牵引箱子点
        RobotHelper.move(eveHarvestPoint.getEve_mutPoint(),85);
        RobotHelper.mouseRightPress();
        RobotHelper.delay(GaussianHelper.getGaussianInt(310, 500));
        // 释放牵引箱子点
        RobotHelper.move(eveHarvestPoint.getEve_releaseMutPoint(),85);
        RobotHelper.mouseLeftPress();
        RobotHelper.delay(GaussianHelper.getGaussianInt(110, 200));

        // 堆叠所有
        RobotHelper.move(eveHarvestPoint.getEve_stackMutPoint(),85);
        RobotHelper.mouseLeftPress();
        RobotHelper.delay(GaussianHelper.getGaussianInt(310, 500));


        // tab1 切换 作战tab
        RobotHelper.move(eveToWorkPoint.getEve_fightingTabPoint(),85);
        RobotHelper.mouseLeftPress();
        RobotHelper.delay(GaussianHelper.getGaussianInt(1560, 1600));

        // 选中牵引箱子
        RobotHelper.move(eveHarvestPoint.getEve_spaceMutPoint(),85);
        RobotHelper.mouseRightPress();
        RobotHelper.delay(GaussianHelper.getGaussianInt(260, 300));

        // 保存牵引箱子点
        RobotHelper.move(eveHarvestPoint.getEve_saveMutPointPoint(),85);
        RobotHelper.mouseLeftPress();
        RobotHelper.delay(GaussianHelper.getGaussianInt(840, 970));
        RobotHelper.keyPress(KeyEvent.VK_ENTER);
        RobotHelper.delay(GaussianHelper.getGaussianInt(100, 170));
        RobotHelper.keyPress(KeyEvent.VK_ENTER);


        RobotHelper.delay(GaussianHelper.getGaussianInt(260, 300));
        // tab0 切换 生产tab
        RobotHelper.move(eveToWorkPoint.getEve_buildingTabPoint(),85);
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
        public GaussianStrPointInfoDTO replica_areaStartPoint = new GaussianStrPointInfoDTO("770,115");
        // OnTopReplica区域终点
        public GaussianStrPointInfoDTO replica_areaEndPoint = new GaussianStrPointInfoDTO("798,323");
        // OnTopReplica区域选择完成按钮
        public GaussianStrPointInfoDTO replica_areaDoneButtonPoint = new GaussianStrPointInfoDTO("1252,278");
        // OnTopReplica拖动放缩起点
        public GaussianStrPointInfoDTO replica_scrollStartPoint = new GaussianStrPointInfoDTO("138,1005");
        // OnTopReplica拖动放缩终点
        public GaussianStrPointInfoDTO replica_scrollEndPoint = new GaussianStrPointInfoDTO("67,516");
        // OnTopReplica准备解除锁定右键位置
        public GaussianStrPointInfoDTO replica_beforeUnlockMenuPoint = new GaussianStrPointInfoDTO("51,55");
        // OnTopReplica解除锁定菜单位置
        public GaussianStrPointInfoDTO replica_unlockMenuPoint = new GaussianStrPointInfoDTO("127,224");
        // OnTopReplica解除锁定
        public GaussianStrPointInfoDTO replica_unlockPoint = new GaussianStrPointInfoDTO("291,229");
        // OnTopReplica解除锁定
        public GaussianStrPointInfoDTO replica_commonMoveStartPoint = new GaussianStrPointInfoDTO("45,55");

    }

    // button 回收
    // button 回收all
    public static void eveEndWorkAll(List<AccountPoint> accountPoints, Integer num) {
        CommonEvePoint commonEvePoint = new CommonEvePoint();
        for (int i = 1; i <= accountPoints.size(); i++) {
            if (num != null && num != i) {
                continue;
            }
            AccountPoint accountPoint = accountPoints.get(i - 1);
            EventHandle_Main.openOpeNew(commonEvePoint.getEve_openSelectPoint(), accountPoint.getEve_selectPoint());
            eveEndWork_sub(commonEvePoint);
        }
    }

    private static void eveEndWork_sub(CommonEvePoint commonEvePoint) {

        EveToWorkPoint eveToWorkPoint = new EveToWorkPoint();
//        // 靠近建筑
//        RobotHelper.move(eveToWorkPoint.getEve_approachPoint());
//        RobotHelper.mouseLeftPress();
//        RobotHelper.delay(GaussianHelper.getGaussianInt(330, 370));

        // 停止加速
//        RobotHelper.move(eveToWorkPoint.getEve_speedUpPoint(), 194);
//        RobotHelper.mouseLeftPress();
//        RobotHelper.delay(GaussianHelper.getGaussianInt(315, 470));

        // 回收无人机
        RobotHelper.keyPress(KeyEvent.VK_R);
        RobotHelper.delay(GaussianHelper.getGaussianInt(900, 970));

        // tab0 切换 生产tab
        RobotHelper.move(eveToWorkPoint.getEve_buildingTabPoint());
        RobotHelper.mouseLeftPress();
        RobotHelper.delay(GaussianHelper.getGaussianInt(300, 470));

        // 最小化
        minimize(commonEvePoint.getEve_minimize());
    }


    /**
     * button 开工
     * @param accountPoints
     * @param num
     * @param accountInfoList
     * @throws BusinessException
     */
    public static void eveToWorkAll(List<AccountPoint> accountPoints, Integer num, List<AccountInfo> accountInfoList) throws BusinessException {
        CommonEvePoint commonEvePoint = new CommonEvePoint();
        for (int i = 1; i <= accountPoints.size(); i++) {
            if (num != null && num != i) {
                continue;
            }
            AccountInfo accountInfo = accountInfoList.get(i - 1);
            AccountPoint accountPoint = accountPoints.get(i - 1);
            EventHandle_Main.openOpeNew(commonEvePoint.getEve_openSelectPoint(), accountPoint.getEve_selectPoint());
            RobotHelper.delay(200);
            eveToWork_sub(accountInfo);
            speedUpAndDroneAndSavePoint(accountInfo.getArroundTrash());

            if(!Objects.equals(accountInfo.getSerialNo(),accountInfo.getLeaderSerialNo())) {
//                EventHandle_Main.openOpeNew(commonEvePoint.getEve_openSelectPoint(), accountPoint.getEve_selectPoint());
                // 最小化
                minimize(commonEvePoint.getEve_minimize());
            }else{
                accountInfo.setAutoReturnTime(System.currentTimeMillis());
            }
        }
    }

    // 中间两项操作。切回作战tab、环绕
    public static List<MyConsumer> MIDDLE_ACTION_LIST = middleActionList();

    private static void eveToWork_sub(AccountInfo accountInfo) throws BusinessException {
        EveToWorkPoint eveToWorkPoint = new EveToWorkPoint();
        if (shouldStop.get()) {
            return;
        }

        // 选中环绕
        RobotHelper.delay(GaussianHelper.getGaussianInt(400, 470));
        // tab0 切换 生产tab
        RobotHelper.move(eveToWorkPoint.getEve_buildingTabPoint(), 94);
        RobotHelper.mouseLeftPress();
        RobotHelper.delay(GaussianHelper.getGaussianInt(500, 660));

        // building 选中建筑
        RobotHelper.move(eveToWorkPoint.getEve_buildingPoint(), 88);
        RobotHelper.mouseLeftPress();
        RobotHelper.delay(GaussianHelper.getGaussianInt(330, 370));
        if (shouldStop.get()) {
            return;
        }

        Collections.shuffle(MIDDLE_ACTION_LIST);
        for (MyConsumer myConsumer : MIDDLE_ACTION_LIST) {
            if (shouldStop.get()) {
                return;
            }
            myConsumer.accept(accountInfo.getArroundTrash());
        }
    }

    private static List<MyConsumer> middleActionList() {
        EveToWorkPoint eveToWorkPoint = new EveToWorkPoint();
        List<MyConsumer> list = new ArrayList<>();
        list.add((e) -> {
            // tab1 切换 作战tab
            RobotHelper.move(eveToWorkPoint.getEve_fightingTabPoint(), 100);
            RobotHelper.mouseLeftPress();
            RobotHelper.delay(GaussianHelper.getGaussianInt(660, 750));
        });
        list.add((e) -> {
            // around 环绕建筑/残骸
            if(e!=null && e instanceof Boolean && (Boolean)e){
                RobotHelper.move(eveToWorkPoint.getEve_aroundTrashPoint(), 100);
            }else {
                RobotHelper.move(eveToWorkPoint.getEve_aroundPoint(), 100);
            }
            RobotHelper.mouseLeftPress();
            RobotHelper.delay(GaussianHelper.getGaussianInt(30, 70));
            RobotHelper.mouseLeftPress();
            RobotHelper.delay(GaussianHelper.getGaussianInt(450, 600));
        });
        return list;
    }


    // 最后三项操作。1=加速、2=释放无人机、3=存点
    public static Map<Integer, MyConsumer> FINAL_ACTION_LIST = finalActionList();

    private static void speedUpAndDroneAndSavePoint(Boolean arroundTrash) throws BusinessException {
        Map<Integer, MyConsumer> executeList = FINAL_ACTION_LIST;
        List<Integer> randomList = new ArrayList<>(Arrays.asList(1, 2, 3));
        Collections.shuffle(randomList);
        if (arroundTrash == null) {
            arroundTrash = false;
        }
        for (Integer index : randomList) {
            // 没有加速，直接跳过
            if(index == 1){
                continue;
            }
            // 如果环绕垃圾，不需要释放无人机
            if(arroundTrash && index == 2){
                continue;
            }
            MyConsumer myConsumer = executeList.get(index);
            if(null == myConsumer){
                continue;
            }
            myConsumer.accept(null);
        }
        RobotHelper.delay(GaussianHelper.getGaussianInt(200, 270));
    }

    private static Map<Integer, MyConsumer> finalActionList() {
        EveToWorkPoint eveToWorkPoint = new EveToWorkPoint();
        Map<Integer, MyConsumer> list = new HashMap<>();
        list.put(1,(e) -> {
            // speedUp 加速
            RobotHelper.move(eveToWorkPoint.getEve_speedUpPoint(), 94);
            RobotHelper.mouseLeftPress();
            RobotHelper.delay(GaussianHelper.getGaussianInt(415, 530));
        });
        list.put(2,(e) -> {
            releaseDrone_ori(eveToWorkPoint);
        });
        list.put(3,(e) -> {
            // 保存点
            savePoint();
        });
        return list;
    }

    private static void releaseDrone_ori(EveToWorkPoint eveToWorkPoint){
        // release 释放无人机
        RobotHelper.move(eveToWorkPoint.getEve_releaseDronePoint(), 94);
        RobotHelper.mouseLeftPress();
        RobotHelper.delay(GaussianHelper.getGaussianInt(415, 550));
    }

    private static void releaseDone_new(EveToWorkPoint eveToWorkPoint){
        // release 释放无人机
        RobotHelper.move(eveToWorkPoint.getEve_releaseDronePoint_new(), 94);
        RobotHelper.mouseLeftPress();
        RobotHelper.delay(GaussianHelper.getGaussianInt(415, 550));
    }

    private static void savePoint() {
        RobotHelper.keyPress(KeyEvent.VK_CONTROL, KeyEvent.VK_B);
        RobotHelper.delay(GaussianHelper.getGaussianInt(640, 770));
        RobotHelper.keyPress(KeyEvent.VK_ENTER);
        RobotHelper.delay(GaussianHelper.getGaussianInt(140, 170));
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
            minimize(commonEvePoint.getEve_minimize());
        }

    }

    /**
     * button 全准备
     */
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

        RobotHelper.delay(GaussianHelper.getGaussianInt(324, 470));
        // 打开星图
        RobotHelper.move(prepareEvePoint.getEve_spaceMapPoint());
        RobotHelper.delay(GaussianHelper.getGaussianInt(224, 370));
        RobotHelper.mouseLeftPress();
        RobotHelper.delay(GaussianHelper.getGaussianInt(4524, 5370));

        RobotHelper.move(prepareEvePoint.getEve_emptySpacePoint());
        RobotHelper.delay(GaussianHelper.getGaussianInt(424, 570));

        // 启动维修1
        RobotHelper.move(prepareEvePoint.getEve_scriptPoint_2_2());
        RobotHelper.mouseLeftPress();
        RobotHelper.delay(GaussianHelper.getGaussianInt(424, 470));

        // 启动维修2
        RobotHelper.move(prepareEvePoint.getEve_scriptPoint_2_3());
        RobotHelper.mouseLeftPress();
        RobotHelper.delay(GaussianHelper.getGaussianInt(424, 470));

        // 启动维修3
        RobotHelper.move(prepareEvePoint.getEve_scriptPoint_2_4());
        RobotHelper.mouseLeftPress();
        RobotHelper.delay(GaussianHelper.getGaussianInt(424, 470));

        // 单击太空
        RobotHelper.move(prepareEvePoint.getEve_emptySpacePoint());
        RobotHelper.mouseLeftPress();
        // 滑动鼠标，拉近星图
        RobotHelper.delay(GaussianHelper.getGaussianInt(424, 470));
        RobotHelper.mouseWheel(3);
        RobotHelper.mouseWheel(2);
        RobotHelper.delay(GaussianHelper.getGaussianInt(424, 470));

    }

    /**
     * button 全投屏
     * @param accountPoints
     * @param num
     */
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
            if (shouldStop.get()) {
                return;
            }
            RobotHelper.move(commonReplicaPoint.getReplica_searchPoint2());
            RobotHelper.keyPress("OnTopReplica");
            RobotHelper.delay(856);
            RobotHelper.move(commonReplicaPoint.getReplica_startPoint());
            RobotHelper.delay(356);
            RobotHelper.mouseLeftPress();
            RobotHelper.delay(856);
            RobotHelper.move(commonReplicaPoint.getReplica_rightClickEmptyPoint());
            RobotHelper.delay(456);
            RobotHelper.mouseRightPress();
            RobotHelper.delay(236);
            RobotHelper.move(commonReplicaPoint.getReplica_lockMenuPoint());
            RobotHelper.delay(256);
            RobotHelper.mouseLeftPress();
            RobotHelper.delay(256);
            RobotHelper.move(commonReplicaPoint.getReplica_lockLeftPoint());
            RobotHelper.delay(256);
            RobotHelper.mouseLeftPress();
            RobotHelper.delay(256);
            // 移动到起始位置
            RobotHelper.move(commonReplicaPoint.getReplica_lockedStartPoint());
            RobotHelper.delay(256);
            RobotHelper.mouseRightPress();
            RobotHelper.delay(256);
            if (shouldStop.get()) {
                return;
            }
            // 选择窗口
            RobotHelper.move(commonReplicaPoint.getReplica_selectWindowMenuPoint());
            RobotHelper.delay(256);
            RobotHelper.mouseLeftPress();
            RobotHelper.delay(256);
            // 选择 a
            RobotHelper.move(accountPoint.getReplica_selectPoint());
            RobotHelper.delay(256);
            RobotHelper.mouseLeftPress();
            RobotHelper.delay(256);

            // 移动到起始位置
            RobotHelper.move(commonReplicaPoint.getReplica_lockedStartPoint());
            RobotHelper.delay(256);
            RobotHelper.mouseRightPress();
            RobotHelper.delay(256);
            RobotHelper.move(commonReplicaPoint.getReplica_percentageWindowMenuPoint());
            RobotHelper.delay(256);
            RobotHelper.mouseLeftPress();
            RobotHelper.delay(256);
            RobotHelper.move(commonReplicaPoint.getReplica_50PercentageWindowMenuPoint());
            RobotHelper.delay(256);
            RobotHelper.mouseLeftPress();
            RobotHelper.delay(256);
            if (shouldStop.get()) {
                return;
            }

            // 移动到起始位置
            RobotHelper.move(commonReplicaPoint.getReplica_lockedStartPoint());
            RobotHelper.delay(256);
            RobotHelper.mouseRightPress();
            RobotHelper.delay(256);
            RobotHelper.move(commonReplicaPoint.getReplica_selectAreaPoint());
            RobotHelper.delay(256);
            RobotHelper.mouseLeftPress();
            RobotHelper.delay(256);

            // 移动到区域起始点
            RobotHelper.move(commonReplicaPoint.getReplica_areaStartPoint());
            RobotHelper.delay(256);
            // 按下鼠标左键
            RobotHelper.r.mousePress(InputEvent.BUTTON1_MASK);
            RobotHelper.delay(256);
            // 移动到区域终止点
            RobotHelper.move(commonReplicaPoint.getReplica_areaEndPoint());
            RobotHelper.delay(256);
            // 弹起鼠标左键
            RobotHelper.r.mouseRelease(InputEvent.BUTTON1_MASK);
            RobotHelper.delay(256);
            // 区域选择完成
            RobotHelper.move(commonReplicaPoint.getReplica_areaDoneButtonPoint());
            RobotHelper.delay(256);
            RobotHelper.mouseLeftPress();
            RobotHelper.delay(256);
            if (shouldStop.get()) {
                return;
            }

//        // 拖动放缩分屏窗口
            RobotHelper.move(commonReplicaPoint.getReplica_scrollStartPoint());
            RobotHelper.delay(256);
            RobotHelper.r.mousePress(InputEvent.BUTTON1_MASK);
            RobotHelper.delay(256);
            RobotHelper.move(commonReplicaPoint.getReplica_scrollEndPoint());
            RobotHelper.delay(400);
            RobotHelper.r.mouseRelease(InputEvent.BUTTON1_MASK);
            RobotHelper.delay(500);
//
//        // 解除锁定
            RobotHelper.move(commonReplicaPoint.getReplica_beforeUnlockMenuPoint());
            RobotHelper.delay(256);
            RobotHelper.mouseRightPress();
            RobotHelper.delay(256);
            RobotHelper.move(commonReplicaPoint.getReplica_unlockMenuPoint());
            RobotHelper.delay(256);
            RobotHelper.mouseLeftPress();
            RobotHelper.delay(256);
            RobotHelper.move(commonReplicaPoint.getReplica_unlockPoint());
            RobotHelper.delay(256);
            RobotHelper.mouseLeftPress();
            RobotHelper.delay(256);
            if (shouldStop.get()) {
                return;
            }
            // 拖动1
            RobotHelper.move(commonReplicaPoint.getReplica_commonMoveStartPoint());
            RobotHelper.delay(256);
            RobotHelper.r.mousePress(InputEvent.BUTTON1_MASK);
            RobotHelper.delay(256);
            RobotHelper.move(accountPoint.getReplica_moveEndPoint());
            RobotHelper.delay(500);
            RobotHelper.r.mouseRelease(InputEvent.BUTTON1_MASK);
            RobotHelper.delay(500);
            if (shouldStop.get()) {
                return;
            }
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
            linkDrone_sub(accountPoint);
        }
    }

    private static void linkDrone_sub(AccountPoint accountPoint) throws BusinessException {
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

        AccountInfo accountInfo = accountPoint.getAccountInfo();
        Boolean aroundTrash = false;
        if(accountInfo != null){
            aroundTrash =accountInfo.getArroundTrash();
        }
        // 环绕+切回作战tab
        for (MyConsumer myConsumer : MIDDLE_ACTION_LIST) {
            myConsumer.accept(aroundTrash);
        }
    }

    public static void minimize(GaussianStrPointInfoDTO p1) {
        // 最小化 - 停用
        /**
        RobotHelper.move(p1, 394);
        RobotHelper.mouseLeftPress();
        RobotHelper.delay(GaussianHelper.getGaussianInt(315, 470));
         */
    }


    // 提取 DLL 文件
    private static void extractNativeLibrary() {
        try {
            String dllPath = System.getProperty("java.io.tmpdir") + "JNativeHook.dll"; // 临时目录路径
            if (!new File(dllPath).exists()) {
                try (InputStream is = PointHelper.class.getResourceAsStream("/native/JNativeHook-2.2.2.x86.dll");
                     OutputStream os = new FileOutputStream(dllPath)) {
                    byte[] buffer = new byte[1024];
                    int length;
                    while ((length = is.read(buffer)) > 0) {
                        os.write(buffer, 0, length);
                    }
                }
            }
            // 加载 DLL
            System.load(dllPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void startListeningForEsc() {
//        extractNativeLibrary(); // 提取并加载 DLL
        try {
            GlobalScreen.registerNativeHook();
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        GlobalScreen.addNativeKeyListener(new NativeKeyListener() {
            @Override
            public void nativeKeyPressed(NativeKeyEvent nativeEvent) {
                if (nativeEvent.getKeyCode() == NativeKeyEvent.VC_ESCAPE) {
                    shouldStop.set(true);
                    System.out.println("ESC key detected globally. Stopping operation...");
                }
            }
            @Override
            public void nativeKeyReleased(NativeKeyEvent nativeEvent) {
                // Not needed
            }
            @Override
            public void nativeKeyTyped(NativeKeyEvent nativeEvent) {
                // Not needed
            }
        });
    }

    public static void cleanEsc() {
        shouldStop.set(false);
    }


}

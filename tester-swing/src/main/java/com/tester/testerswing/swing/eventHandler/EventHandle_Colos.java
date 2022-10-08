package com.tester.testerswing.swing.eventHandler;

import com.tester.testerswing.capture.GaussianPointInfoDTO;
import com.tester.testerswing.capture.PointInfoDTO;
import com.tester.testerswing.robot.RobotHelper;

//import com.tester.testerswing.swing.Colos_Input;

/**
 * colos 初始化
 *
 * @Author 温昌营
 * @Date 2022-8-4 13:47:36F
 */
public class EventHandle_Colos {

    public static String pointStr = "(%d,%d)";

    public static PointInfoDTO colos_open_p1_st = new PointInfoDTO().setX(1335).setY(1051);
    public static GaussianPointInfoDTO colos_open_p1 = new GaussianPointInfoDTO().setSt(colos_open_p1_st).setEd(colos_open_p1_st);
    public static PointInfoDTO colos_open_p2_st = new PointInfoDTO().setX(1567).setY(946);
    public static GaussianPointInfoDTO colos_open_p2 = new GaussianPointInfoDTO().setSt(colos_open_p2_st).setEd(colos_open_p2_st);

    public static PointInfoDTO colos_return_p1_st = new PointInfoDTO().setX(1677).setY(735);
    public static PointInfoDTO colos_return_p1_ed = new PointInfoDTO().setX(1702).setY(745);
    public static GaussianPointInfoDTO colos_return_p1 = new GaussianPointInfoDTO().setSt(colos_return_p1_st).setEd(colos_return_p1_ed);

    public static PointInfoDTO colos_return_p2_st = new PointInfoDTO().setX(1649).setY(808);
    public static PointInfoDTO colos_return_p2_ed = new PointInfoDTO().setX(1649).setY(808);
    public static GaussianPointInfoDTO colos_return_p2 = new GaussianPointInfoDTO().setSt(colos_return_p2_st).setEd(colos_return_p2_ed);

    public static PointInfoDTO colos_return_p3_st = new PointInfoDTO().setX(1713).setY(875);
    public static PointInfoDTO colos_return_p3_ed = new PointInfoDTO().setX(1738).setY(882);
    public static GaussianPointInfoDTO colos_return_p3 = new GaussianPointInfoDTO().setSt(colos_return_p3_st).setEd(colos_return_p3_ed);

    public static PointInfoDTO colos_align_p1_st = new PointInfoDTO().setX(1843).setY(227);
    public static PointInfoDTO colos_align_p1_ed = new PointInfoDTO().setX(1862).setY(238);
    public static GaussianPointInfoDTO colos_align_p1 = new GaussianPointInfoDTO().setSt(colos_align_p1_st).setEd(colos_align_p1_ed);

    public static PointInfoDTO colos_align_p2_st = new PointInfoDTO().setX(1616).setY(288);
    public static PointInfoDTO colos_align_p2_ed = new PointInfoDTO().setX(1634).setY(299);
    public static GaussianPointInfoDTO colos_align_p2 = new GaussianPointInfoDTO().setSt(colos_align_p2_st).setEd(colos_align_p2_ed);

    public static PointInfoDTO colos_align_p3_st = new PointInfoDTO().setX(1591).setY(150);
    public static PointInfoDTO colos_align_p3_ed = new PointInfoDTO().setX(1600).setY(161);
    public static GaussianPointInfoDTO colos_align_p3 = new GaussianPointInfoDTO().setSt(colos_align_p3_st).setEd(colos_align_p3_ed);

    public static PointInfoDTO colos_align_p3_quick_st = new PointInfoDTO().setX(1665).setY(151);
    public static PointInfoDTO colos_align_p3_quick_ed = new PointInfoDTO().setX(1678).setY(163);
    public static GaussianPointInfoDTO colos_align_p3_quick = new GaussianPointInfoDTO().setSt(colos_align_p3_quick_st).setEd(colos_align_p3_quick_ed);


    public static PointInfoDTO colos_main_view_p1_st = new PointInfoDTO().setX(1632).setY(229);
    public static PointInfoDTO colos_main_view_p1_ed = new PointInfoDTO().setX(1648).setY(240);
    public static GaussianPointInfoDTO colos_main_view_p1 = new GaussianPointInfoDTO().setSt(colos_main_view_p1_st).setEd(colos_main_view_p1_ed);

    public static void quick_run(){
        try {
            EventHandle_Main.openOpe(EventHandle_Colos.colos_open_p1, EventHandle_Colos.colos_open_p2);
//            EventHandle_Main.runOpe(EventHandle_Colos.colos_align_p1, EventHandle_Colos.colos_align_p2, EventHandle_Colos.colos_align_p3_quick, EventHandle_Colos.colos_main_view_p1);
            EventHandle_Main.quickRunOpe(EventHandle_Colos.colos_align_p1,
                    EventHandle_Colos.colos_align_p2,
                    EventHandle_Colos.colos_align_p3,
                    EventHandle_Silot.silot_align_p4_stop,
                    EventHandle_Silot.silot_align_p5_use,
                    EventHandle_Silot.silot_align_p6_up,
                    EventHandle_Silot.silot_align_p7_up,
                    EventHandle_Colos.colos_align_p3_quick);
            // 回收无人机
//            EventHandle_Main.returnOpe(EventHandle_Colos.colos_return_p1, EventHandle_Colos.colos_return_p2, EventHandle_Colos.colos_return_p3, EventHandle_Colos.colos_main_view_p1);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }


//
//    public static void handle_colos(Colos_Input script) {
//        // 设置输入事件
//        JTextField colos_open_p1_input = script.getColos_open_p1_input();
//        setTextField(colos_open_p1_input, colos_open_p1_st);
//
//        JTextField colos_open_p2_input = script.getColos_open_p2_input();
//        setTextField(colos_open_p2_input, colos_open_p2_st);
//
//        // 返回6个点
//        JTextField colos_return_p1_input = script.getColos_return_p1_input();
//        setTextField(colos_return_p1_input, colos_return_p1_st);
//        JTextField colos_return_p12_input = script.getColos_return_p12_input();
//        setTextField(colos_return_p12_input, colos_return_p1_ed);
//
//        JTextField colos_return_p2_input = script.getColos_return_p2_input();
//        setTextField(colos_return_p2_input, colos_return_p2_st);
//        JTextField colos_return_p22_input = script.getColos_return_p22_input();
//        setTextField(colos_return_p22_input, colos_return_p2_ed);
//
//        JTextField colos_return_p3_input = script.getColos_return_p3_input();
//        setTextField(colos_return_p3_input, colos_return_p3_st);
//        JTextField colos_return_p32_input = script.getColos_return_p32_input();
//        setTextField(colos_return_p32_input, colos_return_p3_ed);
//
//
//        // 跑路6个点
//        JTextField colos_align_p1_input = script.getColos_align_p1_input();
//        setTextField(colos_align_p1_input, colos_align_p1_st);
//        JTextField colos_align_p12_input = script.getColos_align_p12_input();
//        setTextField(colos_align_p12_input, colos_align_p1_ed);
//
//        JTextField colos_align_p2_input = script.getColos_align_p2_input();
//        setTextField(colos_align_p2_input, colos_align_p2_st);
//        JTextField colos_align_p22_input = script.getColos_align_p22_input();
//        setTextField(colos_align_p22_input, colos_align_p2_ed);
//
//        JTextField colos_align_p3_input = script.getColos_align_p3_input();
//        setTextField(colos_align_p3_input, colos_align_p3_st);
//        JTextField colos_align_p32_input = script.getColos_align_p32_input();
//        setTextField(colos_align_p32_input, colos_align_p3_ed);
//
//        // 主览2个点
//        JTextField colos_main_view_input = script.getColos_main_view_input();
//        setTextField(colos_main_view_input, colos_main_view_p1_st);
//        JTextField colos_main_view2_input = script.getColos_main_view2_input();
//        setTextField(colos_main_view2_input, colos_main_view_p1_ed);
//
//    }
//
//
//    private static void setTextField(JTextField field, PointInfoDTO sPoint) {
//        field.addKeyListener(new KeyListener() {
//            @Override
//            public void keyTyped(KeyEvent e) {
//                // keyChar： 0=48, 1=49, 2=50
//                // keyChar： a=97, b=98
//                char keyChar = e.getKeyChar();
//                if (keyChar == 97) {
//                    Point point = RobotHelper.getPoint();
//                    sPoint.setX((int) point.getX());
//                    sPoint.setY((int) point.getY());
//                    field.setText(String.format(pointStr, sPoint.getX(), sPoint.getY()));
//                }
//            }
//
//            @Override
//            public void keyPressed(KeyEvent e) {
//            }
//
//            @Override
//            public void keyReleased(KeyEvent e) {
//            }
//        });
//    }


}

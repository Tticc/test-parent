package com.tester.testerswing.swing.eventHandler;

import com.tester.testerswing.capture.GaussianPointInfoDTO;
import com.tester.testerswing.capture.PointInfoDTO;

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

    public static PointInfoDTO colos_return_p1_st = new PointInfoDTO().setX(1719).setY(761);
    public static PointInfoDTO colos_return_p1_ed = new PointInfoDTO().setX(1746).setY(767);
    public static GaussianPointInfoDTO colos_return_p1 = new GaussianPointInfoDTO().setSt(colos_return_p1_st).setEd(colos_return_p1_ed);

    public static PointInfoDTO colos_return_p2_st = new PointInfoDTO().setX(1724).setY(862);
    public static PointInfoDTO colos_return_p2_ed = new PointInfoDTO().setX(1724).setY(862);
    public static GaussianPointInfoDTO colos_return_p2 = new GaussianPointInfoDTO().setSt(colos_return_p2_st).setEd(colos_return_p2_ed);

    public static PointInfoDTO colos_return_p3_st = new PointInfoDTO().setX(1775).setY(894);
    public static PointInfoDTO colos_return_p3_ed = new PointInfoDTO().setX(1823).setY(903);
    public static GaussianPointInfoDTO colos_return_p3 = new GaussianPointInfoDTO().setSt(colos_return_p3_st).setEd(colos_return_p3_ed);

    public static PointInfoDTO colos_align_p1_st = new PointInfoDTO().setX(1863).setY(202);
    public static PointInfoDTO colos_align_p1_ed = new PointInfoDTO().setX(1882).setY(211);
    public static GaussianPointInfoDTO colos_align_p1 = new GaussianPointInfoDTO().setSt(colos_align_p1_st).setEd(colos_align_p1_ed);

    public static PointInfoDTO colos_align_p2_st = new PointInfoDTO().setX(1621).setY(253);
    public static PointInfoDTO colos_align_p2_ed = new PointInfoDTO().setX(1653).setY(260);
    public static GaussianPointInfoDTO colos_align_p2 = new GaussianPointInfoDTO().setSt(colos_align_p2_st).setEd(colos_align_p2_ed);

    public static PointInfoDTO colos_align_p3_st = new PointInfoDTO().setX(1619).setY(104);
    public static PointInfoDTO colos_align_p3_ed = new PointInfoDTO().setX(1630).setY(113);
    public static GaussianPointInfoDTO colos_align_p3 = new GaussianPointInfoDTO().setSt(colos_align_p3_st).setEd(colos_align_p3_ed);


    public static PointInfoDTO colos_main_view_p1_st = new PointInfoDTO().setX(1662).setY(205);
    public static PointInfoDTO colos_main_view_p1_ed = new PointInfoDTO().setX(1679).setY(213);
    public static GaussianPointInfoDTO colos_main_view_p1 = new GaussianPointInfoDTO().setSt(colos_main_view_p1_st).setEd(colos_main_view_p1_ed);

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

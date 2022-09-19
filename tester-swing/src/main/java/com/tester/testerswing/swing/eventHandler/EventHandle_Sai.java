package com.tester.testerswing.swing.eventHandler;

import com.tester.testerswing.capture.GaussianPointInfoDTO;
import com.tester.testerswing.capture.PointInfoDTO;
import com.tester.testerswing.robot.RobotHelper;
import com.tester.testerswing.swing.Sai_Input;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * sai 初始化
 *
 * @Author 温昌营
 * @Date 2022-8-4 13:47:36
 */
public class EventHandle_Sai {

    public static String pointStr = "(%d,%d)";

    public static PointInfoDTO sai_open_p1_st = new PointInfoDTO().setX(1338).setY(1048);
    public static GaussianPointInfoDTO sai_open_p1 = new GaussianPointInfoDTO().setSt(sai_open_p1_st).setEd(sai_open_p1_st);
    public static PointInfoDTO sai_open_p2_st = new PointInfoDTO().setX(1353).setY(961);
    public static GaussianPointInfoDTO sai_open_p2 = new GaussianPointInfoDTO().setSt(sai_open_p2_st).setEd(sai_open_p2_st);


    public static PointInfoDTO sai_return_p1_st = new PointInfoDTO().setX(1718).setY(764);
    public static PointInfoDTO sai_return_p1_ed = new PointInfoDTO().setX(1746).setY(773);
    public static GaussianPointInfoDTO sai_return_p1 = new GaussianPointInfoDTO().setSt(sai_return_p1_st).setEd(sai_return_p1_ed);

    public static PointInfoDTO sai_return_p2_st = new PointInfoDTO().setX(1722).setY(864);
    public static PointInfoDTO sai_return_p2_ed = new PointInfoDTO().setX(1722).setY(864);
    public static GaussianPointInfoDTO sai_return_p2 = new GaussianPointInfoDTO().setSt(sai_return_p2_st).setEd(sai_return_p2_ed);

    public static PointInfoDTO sai_return_p3_st = new PointInfoDTO().setX(1770).setY(914);
    public static PointInfoDTO sai_return_p3_ed = new PointInfoDTO().setX(1792).setY(918);
    public static GaussianPointInfoDTO sai_return_p3 = new GaussianPointInfoDTO().setSt(sai_return_p3_st).setEd(sai_return_p3_ed);

    public static PointInfoDTO sai_align_p1_st = new PointInfoDTO().setX(1871).setY(203);
    public static PointInfoDTO sai_align_p1_ed = new PointInfoDTO().setX(1890).setY(214);
    public static GaussianPointInfoDTO sai_align_p1 = new GaussianPointInfoDTO().setSt(sai_align_p1_st).setEd(sai_align_p1_ed);

    public static PointInfoDTO sai_align_p2_st = new PointInfoDTO().setX(1659).setY(251);
    public static PointInfoDTO sai_align_p2_ed = new PointInfoDTO().setX(1682).setY(258);
    public static GaussianPointInfoDTO sai_align_p2 = new GaussianPointInfoDTO().setSt(sai_align_p2_st).setEd(sai_align_p2_ed);

    public static PointInfoDTO sai_align_p3_st = new PointInfoDTO().setX(1624).setY(101);
    public static PointInfoDTO sai_align_p3_ed = new PointInfoDTO().setX(1640).setY(115);
    public static GaussianPointInfoDTO sai_align_p3 = new GaussianPointInfoDTO().setSt(sai_align_p3_st).setEd(sai_align_p3_ed);


    public static PointInfoDTO sai_main_view_p1_st = new PointInfoDTO().setX(1667).setY(203);
    public static PointInfoDTO sai_main_view_p1_ed = new PointInfoDTO().setX(1697).setY(214);
    public static GaussianPointInfoDTO sai_main_view_p1 = new GaussianPointInfoDTO().setSt(sai_main_view_p1_st).setEd(sai_main_view_p1_ed);


    public static void handle_sai(Sai_Input script) {
        // 设置输入事件
        JTextField sai_open_p1_input = script.getSai_open_p1_input();
        setTextField(sai_open_p1_input, sai_open_p1_st);

        JTextField sai_open_p2_input = script.getSai_open_p2_input();
        setTextField(sai_open_p2_input, sai_open_p2_st);


        // 返回6个点
        JTextField sai_return_p1_input = script.getSai_return_p1_input();
        setTextField(sai_return_p1_input, sai_return_p1_st);
        JTextField sai_return_p12_input = script.getSai_return_p12_input();
        setTextField(sai_return_p12_input, sai_return_p1_ed);

        JTextField sai_return_p2_input = script.getSai_return_p2_input();
        setTextField(sai_return_p2_input, sai_return_p2_st);
        JTextField sai_return_p22_input = script.getSai_return_p22_input();
        setTextField(sai_return_p22_input, sai_return_p2_ed);

        JTextField sai_return_p3_input = script.getSai_return_p3_input();
        setTextField(sai_return_p3_input, sai_return_p3_st);
        JTextField sai_return_p32_input = script.getSai_return_p32_input();
        setTextField(sai_return_p32_input, sai_return_p3_ed);


        // 跑路6个点
        JTextField sai_align_p1_input = script.getSai_align_p1_input();
        setTextField(sai_align_p1_input, sai_align_p1_st);
        JTextField sai_align_p12_input = script.getSai_align_p12_input();
        setTextField(sai_align_p12_input, sai_align_p1_ed);

        JTextField sai_align_p2_input = script.getSai_align_p2_input();
        setTextField(sai_align_p2_input, sai_align_p2_st);
        JTextField sai_align_p22_input = script.getSai_align_p22_input();
        setTextField(sai_align_p22_input, sai_align_p2_ed);

        JTextField sai_align_p3_input = script.getSai_align_p3_input();
        setTextField(sai_align_p3_input, sai_align_p3_st);
        JTextField sai_align_p32_input = script.getSai_align_p32_input();
        setTextField(sai_align_p32_input, sai_align_p3_ed);

        // 主览2个点
        JTextField sai_main_view_input = script.getSai_main_view_input();
        setTextField(sai_main_view_input, sai_main_view_p1_st);
        JTextField sai_main_view2_input = script.getSai_main_view2_input();
        setTextField(sai_main_view2_input, sai_main_view_p1_ed);

    }


    private static void setTextField(JTextField field, PointInfoDTO sPoint) {
        field.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                // keyChar： 0=48, 1=49, 2=50
                // keyChar： a=97, b=98
                char keyChar = e.getKeyChar();
                if (keyChar == 97) {
                    Point point = RobotHelper.getPoint();
                    sPoint.setX((int) point.getX());
                    sPoint.setY((int) point.getY());
                    field.setText(String.format(pointStr, sPoint.getX(), sPoint.getY()));
                }
            }

            @Override
            public void keyPressed(KeyEvent e) {
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }
        });
    }


}

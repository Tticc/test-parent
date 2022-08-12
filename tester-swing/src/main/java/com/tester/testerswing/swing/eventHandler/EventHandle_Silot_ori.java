package com.tester.testerswing.swing.eventHandler;

import com.tester.testerswing.capture.PointInfoDTO;
import com.tester.testerswing.robot.RobotHelper;
import com.tester.testerswing.swing.Silot_Input;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * silot 初始化
 *
 * @Author 温昌营
 * @Date 2022-8-4 13:47:36
 */
public class EventHandle_Silot_ori {

    public static String pointStr = "(%d,%d)";

    public static PointInfoDTO silot_open_p1 = new PointInfoDTO().setX(1335).setY(1051);
    public static PointInfoDTO silot_open_p2 = new PointInfoDTO().setX(1197).setY(953);

    public static PointInfoDTO silot_return_p1 = new PointInfoDTO().setX(1717).setY(748);
    public static PointInfoDTO silot_return_p2 = new PointInfoDTO().setX(1723).setY(845);
    public static PointInfoDTO silot_return_p3 = new PointInfoDTO().setX(1787).setY(900);

    public static PointInfoDTO silot_align_p1 = new PointInfoDTO().setX(1882).setY(213);
    public static PointInfoDTO silot_align_p2 = new PointInfoDTO().setX(1667).setY(261);
    public static PointInfoDTO silot_align_p3 = new PointInfoDTO().setX(1630).setY(114);

    public static PointInfoDTO silot_main_view_p1 = new PointInfoDTO().setX(1673).setY(212);


    public static void handle_silot(Silot_Input script) {
        // 设置输入事件
        JTextField silot_open_p1_input = script.getSilot_open_p1_input();
        setTextField(silot_open_p1_input, silot_open_p1);

        JTextField silot_open_p2_input = script.getSilot_open_p2_input();
        setTextField(silot_open_p2_input, silot_open_p2);

        JTextField silot_return_p1_input = script.getSilot_return_p1_input();
        setTextField(silot_return_p1_input, silot_return_p1);

        JTextField silot_return_p2_input = script.getSilot_return_p2_input();
        setTextField(silot_return_p2_input, silot_return_p2);

        JTextField silot_return_p3_input = script.getSilot_return_p3_input();
        setTextField(silot_return_p3_input, silot_return_p3);

        JTextField silot_align_p1_input = script.getSilot_align_p1_input();
        setTextField(silot_align_p1_input, silot_align_p1);

        JTextField silot_align_p2_input = script.getSilot_align_p2_input();
        setTextField(silot_align_p2_input, silot_align_p2);

        JTextField silot_align_p3_input = script.getSilot_align_p3_input();
        setTextField(silot_align_p3_input, silot_align_p3);

        JTextField silot_main_view_input = script.getSilot_main_view_input();
        setTextField(silot_main_view_input, silot_main_view_p1);

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

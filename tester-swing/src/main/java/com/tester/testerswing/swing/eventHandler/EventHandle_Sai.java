package com.tester.testerswing.swing.eventHandler;

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

    public static PointInfoDTO sai_open_p1 = new PointInfoDTO().setX(1271).setY(1054);
    public static PointInfoDTO sai_open_p2 = new PointInfoDTO().setX(1400).setY(949);

    public static PointInfoDTO sai_return_p1 = new PointInfoDTO().setX(1726).setY(867);
    public static PointInfoDTO sai_return_p2 = new PointInfoDTO().setX(1776).setY(917);

    public static PointInfoDTO sai_align_p1 = new PointInfoDTO().setX(1881).setY(213);
    public static PointInfoDTO sai_align_p2 = new PointInfoDTO().setX(1696).setY(254);
    public static PointInfoDTO sai_align_p3 = new PointInfoDTO().setX(1618).setY(117);

    public static PointInfoDTO sai_main_view_p1 = new PointInfoDTO().setX(1667).setY(209);


    public static void handle_sai(Sai_Input script) {
        // 设置输入事件
        JTextField sai_open_p1_input = script.getSai_open_p1_input();
        setTextField(sai_open_p1_input, sai_open_p1);

        JTextField sai_open_p2_input = script.getSai_open_p2_input();
        setTextField(sai_open_p2_input, sai_open_p2);

        JTextField sai_return_p1_input = script.getSai_return_p1_input();
        setTextField(sai_return_p1_input, sai_return_p1);

        JTextField sai_return_p2_input = script.getSai_return_p2_input();
        setTextField(sai_return_p2_input, sai_return_p2);

        JTextField sai_align_p1_input = script.getSai_align_p1_input();
        setTextField(sai_align_p1_input, sai_align_p1);

        JTextField sai_align_p2_input = script.getSai_align_p2_input();
        setTextField(sai_align_p2_input, sai_align_p2);

        JTextField sai_align_p3_input = script.getSai_align_p3_input();
        setTextField(sai_align_p3_input, sai_align_p3);


        JTextField sai_main_view_input = script.getSai_main_view_input();
        setTextField(sai_main_view_input, sai_main_view_p1);

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
package com.tester.testerswing.swing;

import com.tester.testerswing.capture.PointInfoDTO;
import com.tester.testerswing.robot.RobotHelper;

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
public class EventHandle_Silot {

    public static String pointStr = "(%d,%d)";

    public static PointInfoDTO silot_open_p1 = new PointInfoDTO();
    public static PointInfoDTO silot_open_p2 = new PointInfoDTO();
    public static PointInfoDTO silot_return_p1 = new PointInfoDTO();
    public static PointInfoDTO silot_return_p2 = new PointInfoDTO();


    public static void handle_silot(EasyScript_UI script) {
        // 设置输入事件
        JTextField silot_open_p1_input = script.getSilot_open_p1_input();
        setTextField(silot_open_p1_input, silot_open_p1);

        JTextField silot_open_p2_input = script.getSilot_open_p2_input();
        setTextField(silot_open_p2_input, silot_open_p2);

        JTextField silot_return_p1_input = script.getSilot_return_p1_input();
        setTextField(silot_return_p1_input, silot_return_p1);

        JTextField silot_return_p2_input = script.getSilot_return_p2_input();
        setTextField(silot_return_p2_input, silot_return_p2);

        // 设置按钮事件
        JButton open_silot = script.getOpen_silot();
        open_silot.addActionListener((e) -> {
            try {
                openOpe(silot_open_p1, silot_open_p2);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });

        JButton open_return_silot = script.getOpen_return_silot();
        open_return_silot.addActionListener((e) -> {
            try {
                openOpe(silot_open_p1, silot_open_p2);
                returnOpe(silot_return_p1, silot_return_p2);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });
    }

    public static void openOpe(PointInfoDTO p1, PointInfoDTO p2) {
        RobotHelper.move(p1.getX(), p1.getY(), 100);
        RobotHelper.mouseLeftPress();
        RobotHelper.move(p2.getX(), p2.getY(), 226);
        RobotHelper.mouseLeftPress();
    }

    public static void returnOpe(PointInfoDTO p1, PointInfoDTO p2) {
        RobotHelper.move(p1.getX(), p1.getY(), 237);
        RobotHelper.mouseRightPress();
        RobotHelper.move(p2.getX(), p2.getY(), 113);
        RobotHelper.mouseLeftPress();
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

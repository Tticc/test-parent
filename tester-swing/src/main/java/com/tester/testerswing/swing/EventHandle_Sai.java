package com.tester.testerswing.swing;

import com.tester.testerswing.capture.PointInfoDTO;
import com.tester.testerswing.robot.RobotHelper;

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

    public static PointInfoDTO sai_open_p1 = new PointInfoDTO().setX(1337).setY(1054);
    public static PointInfoDTO sai_open_p2 = new PointInfoDTO().setX(1473).setY(957);
    public static PointInfoDTO sai_return_p1 = new PointInfoDTO().setX(1725).setY(875);
    public static PointInfoDTO sai_return_p2 = new PointInfoDTO().setX(1782).setY(917);


    public static void handle_sai(EasyScript_UI script) {
        // 设置输入事件
        JTextField sai_open_p1_input = script.getSai_open_p1_input();
        setTextField(sai_open_p1_input, sai_open_p1);

        JTextField sai_open_p2_input = script.getSai_open_p2_input();
        setTextField(sai_open_p2_input, sai_open_p2);

        JTextField sai_return_p1_input = script.getSai_return_p1_input();
        setTextField(sai_return_p1_input, sai_return_p1);

        JTextField sai_return_p2_input = script.getSai_return_p2_input();
        setTextField(sai_return_p2_input, sai_return_p2);

        // 设置按钮事件
        JButton open_sai = script.getOpen_sai();
        open_sai.addActionListener((e) -> {
            try {
                openOpe(sai_open_p1, sai_open_p2);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });

        JButton open_return_sai = script.getOpen_return_sai();
        open_return_sai.addActionListener((e) -> {
            try {
                openOpe(sai_open_p1, sai_open_p2);
                returnOpe(sai_return_p1, sai_return_p2);
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

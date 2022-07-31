package com.tester.testerswing.swing;

import com.tester.testerswing.boot.AccountInfo;
import com.tester.testerswing.boot.Boot;
import com.tester.testerswing.capture.PointInfo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.List;

public class EasyScript {
    public void start() {
        // 创建 JFrame 实例
        JFrame frame = new JFrame("Login Example");
        // Setting the width and height of frame
        frame.setSize(350, 300);
        frame.setLocation(755, 455);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        /* 创建面板，这个类似于 HTML 的 div 标签
         * 我们可以创建多个面板并在 JFrame 中指定位置
         * 面板中我们可以添加文本字段，按钮及其他组件。
         */
        JPanel panel = new JPanel();
        // 添加面板
        frame.add(panel);
        /*
         * 调用用户定义的方法并添加组件到面板
         */
        placeComponents(panel);

        // 设置界面可见
        frame.setVisible(true);
    }

    private static void placeComponents(JPanel panel) {

        /* 布局部分我们这边不多做介绍
         * 这边设置布局为 null
         */
        panel.setLayout(null);
        Boot boot = new Boot();
        List<AccountInfo> accountInfoList = boot.getAccountInfoList();

        // 创建 JLabel
        JLabel bootInfoLabel = new JLabel("");
        /* 这个方法定义了组件的位置。
         * setBounds(x, y, width, height)
         * x 和 y 指定左上角的新位置，由 width 和 height 指定新的大小。
         */
        bootInfoLabel.setBounds(100, 20, 165, 25);
        panel.add(bootInfoLabel);


        // 创建检测按钮
        JButton loginButton = new JButton("检测");
        loginButton.setBounds(10, 20, 80, 25);
        loginButton.addActionListener((e) -> {
            try {
                boot.start();
                bootInfoLabel.setText("检测中");
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });
        panel.add(loginButton);


        // 创建刷新 JLabel
        JLabel refreshInfoLabel = new JLabel("");
        /* 这个方法定义了组件的位置。
         * setBounds(x, y, width, height)
         * x 和 y 指定左上角的新位置，由 width 和 height 指定新的大小。
         */
        refreshInfoLabel.setBounds(100, 50, 165, 25);
        panel.add(refreshInfoLabel);
        // 创建刷新按钮
        JButton refreshButton = new JButton("刷新");
        refreshButton.setBounds(10, 50, 80, 25);
        refreshButton.addActionListener((e) -> {
            try {
                String refresh = boot.refresh();
                refreshInfoLabel.setText(refresh);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });
        panel.add(refreshButton);



        // 创建刷新 JLabel
        JLabel localInfoLabel = new JLabel("");
        /* 这个方法定义了组件的位置。
         * setBounds(x, y, width, height)
         * x 和 y 指定左上角的新位置，由 width 和 height 指定新的大小。
         */
        localInfoLabel.setBounds(10, 80, 165, 25);
        panel.add(localInfoLabel);



        /*
         * 创建文本域用于用户输入
         */
        JTextField userText = new JTextField(20);
        userText.setBounds(10,110,165,25);
        userText.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                // keyChar： 0=48, 1=49, 2=50
                char keyChar = e.getKeyChar();
                String s = setPoint(keyChar, accountInfoList);
                localInfoLabel.setText(s);
            }
            @Override
            public void keyPressed(KeyEvent e) {

            }
            @Override
            public void keyReleased(KeyEvent e) {

            }
        });
        panel.add(userText);

        // 创建刷新按钮
        JButton cleanButton = new JButton("清理");
        cleanButton.setBounds(10, 140, 80, 25);
        cleanButton.addActionListener((e) -> {
            localInfoLabel.setText("");
        });
        panel.add(cleanButton);
    }


    private static String setPoint(char keyChar, List<AccountInfo> accountInfoList){
        PointInfo pointInfo;
        Point point = MouseInfo.getPointerInfo().getLocation();
        if(keyChar == 49){
            String retStr = "st_silot = [%d,%d]";
            AccountInfo accountInfo = accountInfoList.get(0);
            pointInfo = accountInfo.getSt();
            pointInfo.setX((int)point.getX());
            pointInfo.setY((int)point.getY());
            return String.format(retStr, pointInfo.getX(),pointInfo.getY());
        }else if(keyChar == 50){
            String retStr = "ed_silot = [%d,%d]";
            AccountInfo accountInfo = accountInfoList.get(0);
            pointInfo = accountInfo.getEd();
            pointInfo.setX((int)point.getX());
            pointInfo.setY((int)point.getY());
            return String.format(retStr, pointInfo.getX(),pointInfo.getY());
        }else if(keyChar == 51){
            String retStr = "st_sai = [%d,%d]";
            AccountInfo accountInfo = accountInfoList.get(1);
            pointInfo = accountInfo.getSt();
            pointInfo.setX((int)point.getX());
            pointInfo.setY((int)point.getY());
            return String.format(retStr, pointInfo.getX(),pointInfo.getY());
        }else if(keyChar == 52){
            String retStr = "ed_sai = [%d,%d]";
            AccountInfo accountInfo = accountInfoList.get(1);
            pointInfo = accountInfo.getEd();
            pointInfo.setX((int)point.getX());
            pointInfo.setY((int)point.getY());
            return String.format(retStr, pointInfo.getX(),pointInfo.getY());
        }
        return "";
    }

}

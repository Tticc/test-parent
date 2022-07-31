package com.tester.testerswing.swing;

import com.tester.testerswing.boot.Boot;

import javax.swing.*;

public class EasyScript {
    public static void main(String[] args) {
        // 创建 JFrame 实例
        JFrame frame = new JFrame("Login Example");
        // Setting the width and height of frame
        frame.setSize(350, 200);
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


    }
}

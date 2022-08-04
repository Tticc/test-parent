package com.tester.testerswing.swing;

import javax.swing.*;

public class EasyScript {
    private static JFrame frame;

    public static JFrame getFrame() {
        return frame;
    }


    public void start() {
        // 创建 JFrame 实例
        frame = new JFrame("Script Frame");
        // Setting the width and height of frame
        frame.setSize(470, 430);
        frame.setLocation(625, 355);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        /* 创建面板，这个类似于 HTML 的 div 标签
         * 我们可以创建多个面板并在 JFrame 中指定位置
         * 面板中我们可以添加文本字段，按钮及其他组件。
         */
        EasyScript_UI easyScript_ui = new EasyScript_UI().start();
        JPanel main = easyScript_ui.getMain();
        // 添加面板
        frame.add(main);
        // 设置界面可见
        frame.setVisible(true);
    }

}

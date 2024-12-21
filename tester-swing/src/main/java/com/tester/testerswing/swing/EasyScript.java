package com.tester.testerswing.swing;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class EasyScript {
    private static JFrame frame;

    public static JFrame getFrame() {
        return frame;
    }


    public void start() {
        // 创建 JFrame 实例
        frame = new JFrame("Script Frame");
        // Setting the width and height of frame
        frame.setSize(920, 510);
        frame.setLocation(600, 185);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);



        /* 创建面板，这个类似于 HTML 的 div 标签
         * 我们可以创建多个面板并在 JFrame 中指定位置
         * 面板中我们可以添加文本字段，按钮及其他组件。
         */

        Silot_Input silot_input = new Silot_Input();
        JPanel silot_input_panel = silot_input.getSilot_input_panel();

        Sai_Input sai_input = new Sai_Input();
        JPanel sai_input_panel = sai_input.getSai_input_panel();

        /**
         * ！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！
         * ！！！！！！！！！！！！             ！！！！！！！！！！！！
         * ！！！！！！！！！！！！   启动脚本   ！！！！！！！！！！！！
         * ！！！！！！！！！！！！             ！！！！！！！！！！！！
         * ！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！
         */
        EasyScript_UI_Main easyScript_uiMain = new EasyScript_UI_Main().start(silot_input, sai_input);
        JPanel main = easyScript_uiMain.getMain();
        // 设置按钮鼠标hover颜色
        setupAllButtonsHoverEffect(main);

        JTabbedPane tab = new JTabbedPane();
        tab.addTab("main", main);
        tab.addTab("silot_input", silot_input_panel);
        tab.addTab("sai_input", sai_input_panel);
        // 添加面板
        frame.add(tab);
        // 设置界面可见
        frame.setVisible(true);
    }

    private void setupAllButtonsHoverEffect(JPanel panel) {
        // 遍历面板中的组件
        for (Component comp : panel.getComponents()) {
            if (comp instanceof JButton) { // 检查该组件是否为 Jbutton
                Color originalColor = comp.getBackground(); // 获取原始背景色

                ((JButton) comp).addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseEntered(MouseEvent e) {
                        ((JButton) comp).setBackground(Color.ORANGE); // 鼠标进入时改变颜色
                    }

                    @Override
                    public void mouseExited(MouseEvent e) {
                        ((JButton) comp).setBackground(originalColor); // 鼠标离开时恢复颜色
                    }
                });
            }
        }
    }

}

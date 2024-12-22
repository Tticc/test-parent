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
                JButton button = (JButton) comp;
                button.addMouseListener(new MouseAdapter() {
                    JDialog dialog;

                    @Override
                    public void mouseEntered(MouseEvent e) {
                        button.setBackground(Color.ORANGE); // 鼠标进入时改变颜色
                        // 获取或创建弹窗
                        JDialog dialog = (JDialog) button.getClientProperty("hoverDialog");
                        if (dialog == null) {
                            dialog = createHoverDialog(button, panel);
                            button.putClientProperty("hoverDialog", dialog);
                        }
                        // 更新弹窗位置
                        Point location = button.getLocationOnScreen();
                        dialog.setLocation(location.x - 20, location.y - 50);
                        // 显示弹窗
                        dialog.setVisible(true);
                    }

                    @Override
                    public void mouseExited(MouseEvent e) {
                        button.setBackground(originalColor); // 鼠标离开时恢复颜色
                        // 隐藏弹窗
                        JDialog dialog = (JDialog) button.getClientProperty("hoverDialog");
                        if (dialog != null) {
                            dialog.setVisible(false);
                        }
                    }
                });
            }
        }
    }

    private JDialog createHoverDialog(JButton button, JPanel panel) {
        JDialog dialog = new JDialog((JFrame) SwingUtilities.getWindowAncestor(panel));
        dialog.setUndecorated(true); // 无边框
        dialog.setLayout(new BorderLayout());

        // 放大按钮内容
        JLabel enlargedLabel = new JLabel(button.getText());
        enlargedLabel.setFont(button.getFont().deriveFont(button.getFont().getSize() * 3.0f));
        enlargedLabel.setBackground(button.getBackground());
        enlargedLabel.setOpaque(true); // 确保背景可见
        dialog.add(enlargedLabel);

        dialog.pack();
        return dialog;
    }

}

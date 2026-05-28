package com;

import javax.swing.*;
import java.awt.*;

class CustomButton extends JButton {
    public CustomButton(String icon, String text, Runnable action) {
        super();

        // Tạo icon panel
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 0));

        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font("Arial", Font.PLAIN, 16));

        JLabel textLabel = new JLabel(text);
        textLabel.setFont(new Font("Arial", Font.PLAIN, 14));

        panel.add(iconLabel);
        panel.add(textLabel);

        setLayout(new BorderLayout());
        add(panel, BorderLayout.CENTER);

        setPreferredSize(new Dimension(150, 45));
        addActionListener(e -> action.run());
    }
}

public class Demo {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Custom Button");
        JPanel panel = new JPanel();

        panel.add(new CustomButton("🔍", "Search",
                () -> System.out.println("Searching...")));
        panel.add(new CustomButton("💾", "Save",
                () -> System.out.println("Saving...")));

        frame.add(panel);
        frame.setSize(400, 100);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}

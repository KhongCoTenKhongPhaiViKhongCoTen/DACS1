package com;

import javax.swing.SwingUtilities;

import com.shopapp.ui.frame.Login;

public class Main {
    public static void main(String[] args) {
        IO.println("Hello");

        SwingUtilities.invokeLater(() -> {
            new Login();
        });

    }
}
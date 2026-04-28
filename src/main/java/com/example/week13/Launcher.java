package com.example.week13;

import javax.swing.SwingUtilities;

public class Launcher {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new HashTableVisualizer().setVisible(true);
        });
    }
}

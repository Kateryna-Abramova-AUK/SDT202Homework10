package com.example.week13;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedList;

public class HashTableVisualizer extends JFrame {
    private static final int TABLE_SIZE = 7;
    private ArrayList<LinkedList<Entry>> table;

    private JTextField keyField;
    private JTextField valueField;
    private DrawPanel drawPanel;
    private JTextArea logArea;

    private static class Entry {
        String key;
        String value;
        public Entry(String key, String value) {
            this.key = key; this.value = value;
        }
    }

    public HashTableVisualizer() {
        super("Separate Chaining Hash Table Visualizer");

        table = new ArrayList<>(TABLE_SIZE);
        for (int i = 0; i < TABLE_SIZE; i++) {
            table.add(new LinkedList<>());
        }

        // --- TOP CONTROLS ---
        JPanel controlPanel = new JPanel();
        controlPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        controlPanel.setBackground(new Color(240, 240, 245));

        Font font = new Font("SansSerif", Font.PLAIN, 14);

        controlPanel.add(new JLabel("Key:"));
        keyField = new JTextField(8);
        keyField.setFont(font);
        controlPanel.add(keyField);

        controlPanel.add(new JLabel("  Value:"));
        valueField = new JTextField(8);
        valueField.setFont(font);
        controlPanel.add(valueField);

        JButton insertBtn = new JButton("Insert");
        insertBtn.setFont(font);
        insertBtn.setFocusPainted(false);
        insertBtn.addActionListener(e -> insertData());
        controlPanel.add(Box.createHorizontalStrut(10));
        controlPanel.add(insertBtn);

        // --- DRAWING AREA ---
        drawPanel = new DrawPanel();
        drawPanel.setBackground(new Color(240, 240, 240)); // Match the original slight gray background
        JScrollPane canvasScroll = new JScrollPane(drawPanel);
        canvasScroll.setBorder(null);

        // --- LOGGING AREA ---
        logArea = new JTextArea(6, 40);
        logArea.setEditable(false);
        logArea.setFont(new Font("Monospaced", Font.PLAIN, 13));
        JScrollPane logScroll = new JScrollPane(logArea);
        logScroll.setBorder(BorderFactory.createTitledBorder("Operation Logs"));

        // --- LAYOUT SETUP ---
        setLayout(new BorderLayout());
        add(controlPanel, BorderLayout.NORTH);
        add(canvasScroll, BorderLayout.CENTER);
        add(logScroll, BorderLayout.SOUTH);

        setSize(850, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    private int hash(String key) {
        return (Math.abs(key.hashCode())) % TABLE_SIZE;
    }

    private void insertData() {
        String key = keyField.getText().trim();
        String val = valueField.getText().trim();

        if (!key.isEmpty() && !val.isEmpty()) {
            int index = hash(key);
            LinkedList<Entry> chain = table.get(index);

            boolean updated = false;
            for (Entry entry : chain) {
                if (entry.key.equals(key)) {
                    String oldVal = entry.value;
                    entry.value = val;
                    updated = true;
                    log("Updated: Key ['" + key + "'] from Value '" + oldVal + "' -> '" + val + "' (Bucket " + index + ")");
                    break;
                }
            }
            if (!updated) {
                chain.addFirst(new Entry(key, val));
                log("Inserted: Key ['" + key + "'] with Value ['" + val + "'] -> Hashed to Bucket " + index);
            }

            keyField.setText("");
            valueField.setText("");

            drawPanel.setPreferredSize(new Dimension(800 + (chain.size() * 150), 600));
            drawPanel.revalidate();
            drawPanel.repaint();
        }
    }

    private void log(String message) {
        logArea.append("> " + message + "\n");
        logArea.setCaretPosition(logArea.getDocument().getLength());
    }

    private class DrawPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g2.setFont(new Font("SansSerif", Font.PLAIN, 13));

            int startX = 50;
            int startY = 50;
            int boxWidth = 80;
            int boxHeight = 40;
            int gapY = 55;

            for (int i = 0; i < TABLE_SIZE; i++) {
                int y = startY + (i * gapY);

                // RESTORED: Sharp gray boxes for Buckets
                g2.setColor(Color.LIGHT_GRAY);
                g2.fillRect(startX, y, boxWidth, boxHeight);
                g2.setColor(Color.BLACK);
                g2.drawRect(startX, y, boxWidth, boxHeight);
                g2.drawString("Bucket " + i, startX + 15, y + 25);

                LinkedList<Entry> chain = table.get(i);
                int currentX = startX + boxWidth + 40;

                for (int j = 0; j < chain.size(); j++) {
                    Entry entry = chain.get(j);

                    // RESTORED: Sharp original light blue boxes for Nodes
                    g2.setColor(new Color(173, 216, 230));
                    g2.fillRect(currentX, y, boxWidth + 20, boxHeight);
                    g2.setColor(Color.BLACK);
                    g2.drawRect(currentX, y, boxWidth + 20, boxHeight);
                    g2.drawString(entry.key + " : " + entry.value, currentX + 10, y + 25);

                    int prevEndX = currentX - 40;
                    drawArrow(g2, prevEndX, y + boxHeight / 2, currentX, y + boxHeight / 2);

                    currentX += boxWidth + 20 + 40;
                }

                int prevEndX = currentX - 40;
                drawArrow(g2, prevEndX, y + boxHeight / 2, currentX, y + boxHeight / 2);
                g2.drawString("null", currentX + 5, y + 25);
            }
        }

        private void drawArrow(Graphics2D g2, int x1, int y1, int x2, int y2) {
            // RESTORED: Original thin black arrows
            g2.setColor(Color.BLACK);
            g2.setStroke(new BasicStroke(1));
            g2.drawLine(x1, y1, x2, y2);
            g2.drawLine(x2, y2, x2 - 5, y2 - 5);
            g2.drawLine(x2, y2, x2 - 5, y2 + 5);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            new HashTableVisualizer().setVisible(true);
        });
    }
}
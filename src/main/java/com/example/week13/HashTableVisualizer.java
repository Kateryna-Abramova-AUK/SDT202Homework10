package com.example.week13;


import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.LinkedList;


public class HashTableVisualizer extends Application {

    private static final int TABLE_SIZE = 7;

    // Data structures for the tables
    private ArrayList<LinkedList<Entry>> scTable; // Separate Chaining
    private Entry[] lpTable;                      // Linear Probing
    private int lpSize = 0;

    // UI Components
    private Canvas scCanvas;
    private Canvas lpCanvas;
    private TextArea scLogArea;
    private TextArea lpLogArea;

    private static class Entry {
        String key;
        String value;
        public Entry(String key, String value) {
            this.key = key;
            this.value = value;
        }
    }

    @Override
    public void start(Stage primaryStage) {
        // Initialize underlying data structures
        scTable = new ArrayList<>(TABLE_SIZE);
        for (int i = 0; i < TABLE_SIZE; i++) {
            scTable.add(new LinkedList<>());
        }
        lpTable = new Entry[TABLE_SIZE];

        // Create the left pane (Separate Chaining)
        VBox leftPane = createSeparateChainingPane();

        // Create the right pane (Linear Probing)
        VBox rightPane = createLinearProbingPane();

        // Use a SplitPane to divide the screen into two halves
        SplitPane splitPane = new SplitPane();
        splitPane.getItems().addAll(leftPane, rightPane);
        SplitPane.setResizableWithParent(leftPane, true);
        SplitPane.setResizableWithParent(rightPane, true);

        Scene scene = new Scene(splitPane, 1400, 750);
        primaryStage.setTitle("Hash Table Visualizations (JavaFX)");
        primaryStage.setScene(scene);
        primaryStage.show();

        // Initial draw
        drawSeparateChaining();
        drawLinearProbing();
    }

    // ==========================================
    // UI SETUP FOR LEFT SIDE (Separate Chaining)
    // ==========================================
    private VBox createSeparateChainingPane() {
        VBox pane = new VBox(10);
        pane.setPadding(new Insets(10));

        Label title = new Label("Separate Chaining");
        title.setFont(new Font("System Bold", 18));

        HBox controls = new HBox(10);
        TextField keyField = new TextField();
        keyField.setPromptText("Key");
        TextField valField = new TextField();
        valField.setPromptText("Value");
        Button insertBtn = new Button("Insert");

        controls.getChildren().addAll(new Label("Key:"), keyField, new Label("Val:"), valField, insertBtn);

        scCanvas = new Canvas(800, 500);
        ScrollPane scrollPane = new ScrollPane(scCanvas);
        scrollPane.setPrefHeight(500);

        scLogArea = new TextArea();
        scLogArea.setEditable(false);
        scLogArea.setPrefHeight(100);

        insertBtn.setOnAction(e -> {
            insertSeparateChaining(keyField.getText().trim(), valField.getText().trim());
            keyField.clear();
            valField.clear();
        });

        pane.getChildren().addAll(title, controls, scrollPane, new Label("Logs:"), scLogArea);
        return pane;
    }

    // ==========================================
    // UI SETUP FOR RIGHT SIDE (Linear Probing)
    // ==========================================
    private VBox createLinearProbingPane() {
        VBox pane = new VBox(10);
        pane.setPadding(new Insets(10));

        Label title = new Label("Linear Probing");
        title.setFont(new Font("System Bold", 18));

        HBox controls = new HBox(10);
        TextField keyField = new TextField();
        keyField.setPromptText("Key");
        TextField valField = new TextField();
        valField.setPromptText("Value");
        Button insertBtn = new Button("Insert");

        controls.getChildren().addAll(new Label("Key:"), keyField, new Label("Val:"), valField, insertBtn);

        lpCanvas = new Canvas(600, 500);
        ScrollPane scrollPane = new ScrollPane(lpCanvas);
        scrollPane.setPrefHeight(500);

        lpLogArea = new TextArea();
        lpLogArea.setEditable(false);
        lpLogArea.setPrefHeight(100);

        insertBtn.setOnAction(e -> {
            insertLinearProbing(keyField.getText().trim(), valField.getText().trim());
            keyField.clear();
            valField.clear();
        });

        pane.getChildren().addAll(title, controls, scrollPane, new Label("Logs:"), lpLogArea);
        return pane;
    }

    // ==========================================
    // HASHING & INSERTION LOGIC
    // ==========================================
    private int hash(String key) {
        return (Math.abs(key.hashCode())) % TABLE_SIZE;
    }

    private void insertSeparateChaining(String key, String val) {
        if (key.isEmpty() || val.isEmpty()) return;

        int index = hash(key);
        LinkedList<Entry> chain = scTable.get(index);
        boolean updated = false;

        for (Entry entry : chain) {
            if (entry.key.equals(key)) {
                String old = entry.value;
                entry.value = val;
                updated = true;
                scLogArea.appendText("> Updated Key '" + key + "' from '" + old + "' to '" + val + "' (Bucket " + index + ")\n");
                break;
            }
        }
        if (!updated) {
            chain.addFirst(new Entry(key, val));
            scLogArea.appendText("> Inserted Key '" + key + "' : '" + val + "' -> Bucket " + index + "\n");
        }

        // Adjust canvas width if chains get too long
        scCanvas.setWidth(Math.max(800, 150 + chain.size() * 140));
        drawSeparateChaining();
    }

    private void insertLinearProbing(String key, String val) {
        if (key.isEmpty() || val.isEmpty()) return;

        int index = hash(key);
        int startIndex = index;
        int probes = 0;

        while (lpTable[index] != null) {
            if (lpTable[index].key.equals(key)) {
                String old = lpTable[index].value;
                lpTable[index].value = val;
                lpLogArea.appendText("> Updated Key '" + key + "' from '" + old + "' to '" + val + "' at Index " + index + "\n");
                drawLinearProbing();
                return;
            }
            index = (index + 1) % TABLE_SIZE;
            probes++;

            if (probes >= TABLE_SIZE) {
                lpLogArea.appendText("> Error: Table is Full! Cannot insert '" + key + "'\n");
                return;
            }
        }

        lpTable[index] = new Entry(key, val);
        lpSize++;
        if (probes == 0) {
            lpLogArea.appendText("> Inserted Key '" + key + "' : '" + val + "' -> Index " + index + " (No collisions)\n");
        } else {
            lpLogArea.appendText("> Inserted Key '" + key + "' : '" + val + "' -> Index " + index + " (Probed " + probes + " times from " + startIndex + ")\n");
        }
        drawLinearProbing();
    }

    // ==========================================
    // DRAWING LOGIC (Separate Chaining)
    // ==========================================
    private void drawSeparateChaining() {
        GraphicsContext gc = scCanvas.getGraphicsContext2D();
        gc.clearRect(0, 0, scCanvas.getWidth(), scCanvas.getHeight());
        gc.setFont(new Font("SansSerif", 13));

        int startX = 30;
        int startY = 30;
        int boxW = 80;
        int boxH = 40;
        int gapY = 55;

        for (int i = 0; i < TABLE_SIZE; i++) {
            int y = startY + (i * gapY);

            // Draw Bucket
            gc.setFill(Color.LIGHTGRAY);
            gc.fillRect(startX, y, boxW, boxH);
            gc.setStroke(Color.BLACK);
            gc.strokeRect(startX, y, boxW, boxH);
            gc.setFill(Color.BLACK);
            gc.fillText("Bucket " + i, startX + 15, y + 25);

            LinkedList<Entry> chain = scTable.get(i);
            int currentX = startX + boxW + 40;

            for (int j = 0; j < chain.size(); j++) {
                Entry entry = chain.get(j);

                // Draw Node
                gc.setFill(Color.LIGHTBLUE);
                gc.fillRect(currentX, y, boxW + 20, boxH);
                gc.strokeRect(currentX, y, boxW + 20, boxH);
                gc.setFill(Color.BLACK);
                gc.fillText(entry.key + " : " + entry.value, currentX + 10, y + 25);

                // Draw Arrow
                drawArrow(gc, currentX - 40, y + boxH / 2, currentX, y + boxH / 2);

                currentX += boxW + 60;
            }

            // Draw null
            drawArrow(gc, currentX - 40, y + boxH / 2, currentX, y + boxH / 2);
            gc.fillText("null", currentX + 5, y + 25);
        }
    }

    // ==========================================
    // DRAWING LOGIC (Linear Probing)
    // ==========================================
    private void drawLinearProbing() {
        GraphicsContext gc = lpCanvas.getGraphicsContext2D();
        gc.clearRect(0, 0, lpCanvas.getWidth(), lpCanvas.getHeight());
        gc.setFont(new Font("SansSerif", 13));

        int startX = 150;
        int startY = 30;
        int boxW = 120;
        int boxH = 40;
        int gapY = 55;

        for (int i = 0; i < TABLE_SIZE; i++) {
            int y = startY + (i * gapY);

            // Draw Index Label
            gc.setFill(Color.BLACK);
            gc.fillText("Index " + i, startX - 60, y + 25);

            // Draw Array Slot
            if (lpTable[i] == null) {
                gc.setFill(Color.WHITE);
                gc.fillRect(startX, y, boxW, boxH);
                gc.setStroke(Color.GRAY);
                gc.strokeRect(startX, y, boxW, boxH);
                gc.setFill(Color.GRAY);
                gc.fillText("empty", startX + 35, y + 25);
            } else {
                gc.setFill(Color.LIGHTGREEN);
                gc.fillRect(startX, y, boxW, boxH);
                gc.setStroke(Color.BLACK);
                gc.strokeRect(startX, y, boxW, boxH);
                gc.setFill(Color.BLACK);
                gc.fillText(lpTable[i].key + " : " + lpTable[i].value, startX + 15, y + 25);
            }
        }
    }

    // Helper for drawing arrows in JavaFX
    private void drawArrow(GraphicsContext gc, double x1, double y1, double x2, double y2) {
        gc.setStroke(Color.BLACK);
        gc.strokeLine(x1, y1, x2, y2);
        gc.strokeLine(x2, y2, x2 - 5, y2 - 5);
        gc.strokeLine(x2, y2, x2 - 5, y2 + 5);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
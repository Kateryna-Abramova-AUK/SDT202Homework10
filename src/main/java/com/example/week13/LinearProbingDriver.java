package com.example.week13;

public class LinearProbingDriver {
    public static void main(String[] args) {
        // Start with a small capacity to demonstrate resizing and linear probing
        LinearProbingHashTable<String, Integer> hashTable = new LinearProbingHashTable<>(4);

        System.out.println("Inserting elements (triggers resizing)...");
        hashTable.put("Dog", 1);
        hashTable.put("Cat", 2);
        hashTable.put("Bird", 3);
        hashTable.put("Fish", 4);
        hashTable.put("Lizard", 5);

        hashTable.printTable();

        System.out.println("Testing Retrieval:");
        System.out.println("Value for 'Cat': " + hashTable.get("Cat"));
        System.out.println("Value for 'Fish': " + hashTable.get("Fish"));
        System.out.println("Value for 'Bear' (not in table): " + hashTable.get("Bear"));
    }
}
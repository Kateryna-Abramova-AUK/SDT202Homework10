package com.example.week13;

public class SeparateChainingDriver {
    public static void main(String[] args) {
        SeparateChainingHashTable<String, Integer> hashTable = new SeparateChainingHashTable<>(5);

        System.out.println("Inserting elements...");
        hashTable.put("Apple", 10);
        hashTable.put("Banana", 20);
        hashTable.put("Orange", 30);
        hashTable.put("Grapes", 40);
        hashTable.put("Melon", 50);
        // Force a likely collision update
        hashTable.put("Apple", 15);

        hashTable.printTable();

        System.out.println("Testing Retrieval:");
        System.out.println("Value for 'Banana': " + hashTable.get("Banana"));
        System.out.println("Value for 'Apple' (updated): " + hashTable.get("Apple"));
        System.out.println("Value for 'Mango' (not in table): " + hashTable.get("Mango"));
    }
}
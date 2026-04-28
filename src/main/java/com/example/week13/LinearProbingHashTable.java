package com.example.week13;

public class LinearProbingHashTable<Key, Value> {
    private int n; // Number of key-value pairs
    private int m; // Hash table size
    private Key[] keys;
    private Value[] vals;

    @SuppressWarnings("unchecked")
    public LinearProbingHashTable(int capacity) {
        m = capacity;
        n = 0;
        keys = (Key[]) new Object[m];
        vals = (Value[]) new Object[m];
    }

    private int hash(Key key) {
        return (key.hashCode() & 0x7fffffff) % m;
    }

    public void put(Key key, Value val) {
        if (n >= m / 2) resize(2 * m); // Standard textbook resize logic to keep load factor <= 0.5

        int i;
        for (i = hash(key); keys[i] != null; i = (i + 1) % m) {
            if (keys[i].equals(key)) {
                vals[i] = val;
                return;
            }
        }
        keys[i] = key;
        vals[i] = val;
        n++;
    }

    public Value get(Key key) {
        for (int i = hash(key); keys[i] != null; i = (i + 1) % m) {
            if (keys[i].equals(key)) {
                return vals[i];
            }
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    private void resize(int capacity) {
        LinearProbingHashTable<Key, Value> temp = new LinearProbingHashTable<>(capacity);
        for (int i = 0; i < m; i++) {
            if (keys[i] != null) {
                temp.put(keys[i], vals[i]);
            }
        }
        keys = temp.keys;
        vals = temp.vals;
        m = temp.m;
    }

    public void printTable() {
        System.out.println("--- Linear Probing Hash Table ---");
        for (int i = 0; i < m; i++) {
            if (keys[i] != null) {
                System.out.println("Index " + i + ": [" + keys[i] + "=" + vals[i] + "]");
            } else {
                System.out.println("Index " + i + ": empty");
            }
        }
        System.out.println("---------------------------------\n");
    }
}
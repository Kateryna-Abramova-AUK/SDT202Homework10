package com.example.week13;

public class SeparateChainingHashTable<Key, Value> {
    private int m; // Hash table size
    private Node[] st; // Array of linked-list nodes

    // Textbook standard inner class for linked list nodes
    private static class Node {
        Object key;
        Object val;
        Node next;

        public Node(Object key, Object val, Node next) {
            this.key = key;
            this.val = val;
            this.next = next;
        }
    }

    public SeparateChainingHashTable(int m) {
        this.m = m;
        st = new Node[m];
    }

    // Mask off the sign bit to ensure a positive index
    private int hash(Key key) {
        return (key.hashCode() & 0x7fffffff) % m;
    }

    public void put(Key key, Value val) {
        if (val == null) return;

        int i = hash(key);
        // Check if key already exists, update value if so
        for (Node x = st[i]; x != null; x = x.next) {
            if (key.equals(x.key)) {
                x.val = val;
                return;
            }
        }
        // Otherwise, insert new node at the front of the chain
        st[i] = new Node(key, val, st[i]);
    }

    @SuppressWarnings("unchecked")
    public Value get(Key key) {
        int i = hash(key);
        for (Node x = st[i]; x != null; x = x.next) {
            if (key.equals(x.key)) {
                return (Value) x.val;
            }
        }
        return null;
    }

    // Utility method to visualize the internal structure in the console
    public void printTable() {
        System.out.println("--- Separate Chaining Hash Table ---");
        for (int i = 0; i < m; i++) {
            System.out.print("Bucket " + i + ": ");
            for (Node x = st[i]; x != null; x = x.next) {
                System.out.print("[" + x.key + "=" + x.val + "] -> ");
            }
            System.out.println("null");
        }
        System.out.println("------------------------------------\n");
    }
}
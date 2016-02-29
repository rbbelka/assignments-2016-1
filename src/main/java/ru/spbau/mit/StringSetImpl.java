package ru.spbau.mit;

import java.util.LinkedList;

/**
 * Created by Наталья on 22.02.2016.
 */
public class StringSetImpl implements StringSet {

    private class Node {
        private LinkedList <Node> nodes = new LinkedList<>();
        private boolean id = false;
        private char value;
        private int size = 0;

        public Node addNext(char cur) {
            Node temp = new Node(cur);
            nodes.add(temp);
            return temp;
        }

        public void setId(boolean id) {
            this.id = id;
        }

        public char getValue() {
            return value;
        }

        public Node(char cur) {
            value = cur;
        }

        public Node findValue(char cur) {
            if (nodes == null) {
                return null;
            }
            return nodes.stream().filter(node -> node.getValue() == cur).findFirst().orElse(null);
        }
    }

    private Node head = new Node('0');

    public int size() {
        return head.size;
    }

    public boolean add(String element) {
        if (!contains(element)) {
            if (element == "") {
                head.setId(true);
                head.size++;
                return true;
            }
            Node current = head;
            for (int i = 0; i < element.length(); i++) {
                char symbol = element.charAt(i);
                Node next = current.findValue(symbol);
                current.size++;
                if (i == element.length()-1) {
                        current = current.addNext(symbol);
                        current.setId(true);
                        current.size++;
                        return true;
                }
                if (next != null) {
                    current = next;
                }
                else {
                    current = current.addNext(symbol);
                }
            }
        }
        return false;
    }

    public boolean contains(String element) {
        if (element == "") {
            return head.id;
        }
        Node current = head;
        for (int i = 0; i < element.length(); i++) {
            char symbol = element.charAt(i);
            Node next = current.findValue(symbol);
            if(next == null) {
                return false;
            }
            if (i == element.length()-1 && next.id) {
                    return true;
            }
            else {
                current = next;
            }
        }
        return false;
    }

    public boolean remove(String element) {
        if (contains(element)) {
            if (element == "") {
                head.setId(false);
                head.size--;
                return true;
            }
            Node current = head;
            for (int i = 0; i < element.length(); i++) {
                char symbol = element.charAt(i);
                Node next = current.findValue(symbol);
                current.size--;
                if (i == element.length() - 1 && next.id) {
                    next.setId(false);
                    next.size--;
                    return true;
                }
                else {
                    current = next;
                }
            }
        }
        return false;
    }

    public int howManyStartsWithPrefix(String prefix) {
        if (prefix == "") {
            return head.size;
        }
        Node current = head;
        for (int i = 0; i < prefix.length(); i++) {
            char symbol = prefix.charAt(i);
            Node next = current.findValue(symbol);
            if (next == null) {
                return 0;
            }
            if (i == prefix.length() - 1) {
                return next.size;
            }
            else {
                current = next;
            }
        }
        return 0;
    }
}
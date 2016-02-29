package ru.spbau.mit;

import java.util.LinkedList;
import java.util.ListIterator;

/**
 * Created by Наталья on 22.02.2016.
 */
public class StringSetImpl implements StringSet {

    private final class Node {
        private LinkedList<Node> nodes = new LinkedList<>();
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

        private Node(char cur) {
            value = cur;
        }

        public Node findValue(char cur) {
            if (nodes == null) {
                return null;
            }
            ListIterator<Node> iterator = nodes.listIterator();
            while (iterator.hasNext()) {
                Node node = iterator.next();
                if (node.getValue() == cur) {
                    return node;
                }
            }
            return null;
        }
    }

    private Node head = new Node('0');

    public int size() {
        return head.size;
    }

    public boolean add(String element) {
        if (!contains(element)) {
            if (element.equals("")) {
                head.setId(true);
                head.size++;
                return true;
            }
            Node current = head;
            for (int i = 0; i < element.length(); i++) {
                char symbol = element.charAt(i);
                Node next = current.findValue(symbol);
                current.size++;
                if (next != null) {
                    current = next;
                } else {
                    current = current.addNext(symbol);
                }
                if (i == element.length() - 1) {
                    current.setId(true);
                    current.size++;
                    return true;
                }
            }
        }
        return false;
    }

    public boolean contains(String element) {
        if (element.equals("")) {
            return head.id;
        }
        Node current = head;
        for (int i = 0; i < element.length(); i++) {
            char symbol = element.charAt(i);
            Node next = current.findValue(symbol);
            if (next == null) {
                return false;
            }
            if (i == element.length() - 1 && next.id) {
                    return true;
            } else {
                current = next;
            }
        }
        return false;
    }

    public boolean remove(String element) {
        if (contains(element)) {
            if (element.equals("")) {
                head.setId(false);
                head.size--;
                return true;
            }
            Node current = head;
            for (int i = 0; i < element.length(); i++) {
                char symbol = element.charAt(i);
                Node next = current.findValue(symbol);
                if (next != null) {
                    current.size--;
                    if (i == element.length() - 1 && next.id) {
                        next.setId(false);
                        next.size--;
                        return true;
                    } else {
                        current = next;
                    }
                }
            }
        }
        return false;
    }

    public int howManyStartsWithPrefix(String prefix) {
        if (prefix.equals("")) {
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
            } else {
                current = next;
            }
        }
        return 0;
    }
}

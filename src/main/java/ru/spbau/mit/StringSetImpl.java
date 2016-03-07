package ru.spbau.mit;

/**
 * Created by Наталья on 22.02.2016.
 */
public class StringSetImpl implements StringSet {

    private static final int ALPHABET_SIZE = 58;

    private static final class Node {
        private Node[] nodes = new Node[ALPHABET_SIZE];
        private boolean isTerminal = false;
        private int size = 0;

        private Node addNext(char cur) {
            Node temp = new Node();
            nodes[cur - 'A'] = temp;
            return temp;
        }

        private void setTerminal(boolean terminal) {
            this.isTerminal = terminal;
        }

        private Node findValue(char cur) {
            return nodes[cur - 'A'];
        }
    }

    private Node head = new Node();

    public int size() {
        return head.size;
    }

    private boolean walk(String element, Integer a, boolean flag) {
        Node current = head;
        for (int i = 0; i < element.length(); i++) {
            char symbol = element.charAt(i);
            Node next = current.findValue(symbol);
            current.size += a;

            if (next == null && a == 0) {
                return false;
            }

            if (next != null) {
                current = next;
            } else {
                current = current.addNext(symbol);
            }
        }

        if (a == 0) {
            return current.isTerminal;
        }
        current.setTerminal(flag);
        current.size += a;
        return true;
    }

    public boolean add(String element) {
        if (contains(element)) {
            return false;
        }
        return walk(element, 1, true);
    }

    public boolean contains(String element) {
        return walk(element, 0, false);
    }

    public boolean remove(String element) {
        if (!contains(element)) {
            return false;
        }
        return walk(element, -1, false);
    }

    public int howManyStartsWithPrefix(String prefix) {
        Node current = head;
        for (int i = 0; i < prefix.length(); i++) {
            char symbol = prefix.charAt(i);
            Node next = current.findValue(symbol);
            if (next == null) {
                return 0;
            }
            current = next;
        }
        return current.size;
    }
}

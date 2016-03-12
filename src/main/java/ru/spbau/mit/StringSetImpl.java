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

        private int index(char cur) {
            return cur - 'A';
        }

        private Node addNext(char cur) {
            Node temp = new Node();
            nodes[index(cur)] = temp;
            return temp;
        }

        private void setTerminal(boolean terminal) {
            this.isTerminal = terminal;
        }

        private Node findValue(char cur) {
            return nodes[index(cur)];
        }

        private void delete(char cur) {
            nodes[index(cur)] = null;
        }
    }

    private Node head = new Node();

    private enum WalkType {
        ADD(1, true),
        REMOVE(-1, false),
        FIND(0, false);

        private int increment;
        private boolean terminal;
        WalkType(int increment, boolean terminal) {
            this.increment = increment;
            this.terminal = terminal;
        }
    }

    private Node walk(String element, WalkType walkType) {
        Node current = head;
        for (int i = 0; i < element.length(); i++) {
            char symbol = element.charAt(i);
            Node next = current.findValue(symbol);
            current.size += walkType.increment;

            switch (walkType) {
                case REMOVE:
                    if (next != null && next.size == 1) {
                        current.delete(element.charAt(i));
                        return null;
                    }
                case FIND:
                    if (next == null) {
                        return null;
                    }
                case ADD:
                    if (next != null) {
                        current = next;
                    } else {
                        current = current.addNext(symbol);
                    }
                default:
                    break;
            }
        }
        
        if (walkType != WalkType.FIND) {
                current.setTerminal(walkType.terminal);
                current.size += walkType.increment;
        }
        return current;
    }

    public int size() {
        return head.size;
    }

    public boolean add(String element) {
        if (contains(element)) {
            return false;
        }
        walk(element, WalkType.ADD);
        return true;
    }

    public boolean contains(String element) {
        Node node = walk(element, WalkType.FIND);
        return node != null && node.isTerminal;
    }

    public boolean remove(String element) {
        if (!contains(element)) {
            return false;
        }
        walk(element, WalkType.REMOVE);
        return true;
    }

    public int howManyStartsWithPrefix(String prefix) {
        Node node = walk(prefix, WalkType.FIND);
        if (node == null) {
            return 0;
        } else {
            return node.size;
        }
    }
}
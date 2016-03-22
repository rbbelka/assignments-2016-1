package ru.spbau.mit;

import java.util.*;

/**
 * Created by rbbelka on 22.03.16.
 */

public class HashMultiset<E> extends AbstractSet<E> implements Multiset<E> {

    private LinkedHashMap<E, SetEntry<E>> map = new LinkedHashMap<>();
    private LinkedHashSet<SetEntry<E>> set = new LinkedHashSet<>();
    private LinkedHashSet<E> distinct = new LinkedHashSet<>();
    private int size = 0;

    public int count(Object element) {
        if (map.containsKey(element)) {
            return map.get(element).getCount();
        }
        return 0;
    }

    public Set<E> elementSet() {
        return Collections.unmodifiableSet(distinct);
    }

    public Set<? extends Entry<E>> entrySet() {
        return Collections.unmodifiableSet(set);
    }

    public Iterator<E> iterator() {
        return new SetIterator<>(set);
    }

    public int size() {
        return size;
    }

    public boolean add(E e) {
        size += 1;
        if (distinct.contains(e)) {
            SetEntry<E> entry = map.get(e);
            entry.size += 1;
            return true;
        }
        SetEntry<E> entry = new SetEntry<>(e);
        map.put(e, entry);
        set.add(entry);
        distinct.add(e);
        return true;
    }

    private static class SetEntry<E> implements Entry<E> {

        private E item;
        private int size = 1;

        private SetEntry(E e) {
            item = e;
        }

        @Override
        public E getElement() {
            return item;
        }

        @Override
        public int getCount() {
            return size;
        }
    }

    private static class SetIterator<E> implements Iterator<E> {

        private Iterator<SetEntry<E>> iterator;
        private SetEntry<E> current;
        private int index = 0;

        SetIterator(LinkedHashSet<SetEntry<E>> set) {
            iterator = set.iterator();
        }

        public boolean hasNext() {
            if (!iterator.hasNext()) {
                return current != null && current.getCount() < index - 1;
            }
            return true;
        }

        public E next() {
            if (!hasNext()) {
                return null;
            }
            if (current != null && current.getCount() < index - 1) {
                index++;
            } else {
                current = iterator.next();
                index = 0;
            }
            return current.getElement();
        }

        public void remove() {
        }
    }
}

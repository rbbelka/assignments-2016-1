package ru.spbau.mit;

import java.util.*;

@SuppressWarnings("unchecked")
public class SmartList<E> extends AbstractList<E> implements List<E> {
    private static final int MAX_ARRAY_SIZE = 5;

    private enum ListType {
        none, object, array, list;

        ListType() {
        }

        private static ListType getType(int size) {
            if (size == 0) {
                return none;
            }
            if (size == 1) {
                return object;
            }
            if (size <= MAX_ARRAY_SIZE) {
                return array;
            } else {
                return list;
            }
        }
    }

    private int size;

    private Object object;

    public SmartList() {
        size = 0;
    }

    public SmartList(Collection<E> collection) {
        size = collection.size();
        Iterator<E> iterator = collection.iterator();

        switch (ListType.getType(size)) {
            case none:
                object = null;
                break;

            case object:
                object = iterator.next();
                break;

            case array:
                Object[] objects = new Object[MAX_ARRAY_SIZE];
                int i = 0;
                while (iterator.hasNext()) {
                    objects[i++] = iterator.next();
                }
                object = objects;
                break;

            case list:
                object = new ArrayList<>(collection);
                break;

            default:
                break;
        }
    }


    @Override
    public E get(int index) {
        checkIndex(index);

        switch (ListType.getType(size)) {
            case object:
                return (E) object;

            case array:
                E[] arr = (E[]) object;
                return arr[index];

            case list:
                ArrayList<E> arrayList = (ArrayList<E>) object;
                return arrayList.get(index);

            default:
                return null;
        }
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public E set(int index, E element) {
        checkIndex(index);
        E result;

        switch (ListType.getType(size)) {
            case object:
                result = (E) object;
                object = element;
                break;

            case array:
                E[] arr = (E[]) object;
                result = arr[index];
                arr[index] = element;
                break;

            case list:
                ArrayList<E> arrayList = (ArrayList<E>) object;
                result = arrayList.set(index, element);
                break;

            default:
                return null;
        }
        return result;
    }

    private void checkIndex(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException();
        }
    }

    @Override
    public boolean add(E e) {
        if (ListType.getType(size) != ListType.getType(size + 1)) {
            addSize();
        }
        size++;

        switch (ListType.getType(size)) {
            case object:
                object = e;
                break;

            case array:
                E[] arr = (E[]) object;
                arr[size - 1] = e;
                break;

            case list:
                ArrayList<E> arrayList = (ArrayList<E>) object;
                arrayList.add(e);
                break;

            default:
                return false;
        }
        return true;
    }

    private void addSize() {
        switch (ListType.getType(size)) {
            case none:
                break;

            case object:
                Object[] objects = new Object[MAX_ARRAY_SIZE];
                objects[0] = object;
                object = objects;
                break;

            case array:
                object = new ArrayList<>(
                        Arrays.asList((E[]) object)
                );
                break;

            default:
                break;
        }
    }

    @Override
    public E remove(int index) {
        checkIndex(index);
        E result = null;

        switch (ListType.getType(size)) {
            case object:
                result = (E) object;
                object = null;
                break;

            case array:
                E[] arr = (E[]) object;
                result = arr[index];
                System.arraycopy(arr, index + 1, arr, index, arr.length - 1 - index);
                break;

            case list:
                result = ((ArrayList<E>) object).remove(index);
                break;

            default:
                break;
        }

        if (ListType.getType(size) != ListType.getType(size - 1)) {
            reduceSize();
        }
        size--;
        return result;
    }

    private void reduceSize() {

        switch (ListType.getType(size)) {
            case array:
                object = ((E[]) object)[0];
                break;

            case list:
                object = ((ArrayList<E>) object).toArray();
                break;

            default:
                break;
        }
    }
}

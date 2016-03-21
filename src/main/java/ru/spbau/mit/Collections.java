package ru.spbau.mit;

import java.util.ArrayList;
import java.util.Iterator;

public abstract class Collections {

    public static <T, R> Iterable<R> map(final Function1<? super T, ? extends R> f, final Iterable<T> a) {
        ArrayList<R> b = new ArrayList<>();
        for (T item : a) {
            b.add(f.apply(item));
        }
        return b;
    }

    public static <T> Iterable<T> filter(final Predicate<? super T> p, final Iterable<T> a) {
        ArrayList<T> b = new ArrayList<>();
        for (T item : a) {
            if (p.apply(item)) {
                b.add(item);
            }
        }
        return b;
    }

    public static <T> Iterable<T> takeWhile(final Predicate<? super T> p, final Iterable<T> a) {
        ArrayList<T> b = new ArrayList<>();
        for (T item : a) {
            if (!p.apply(item)) {
                break;
            }
            b.add(item);
        }
        return b;
    }

    public static <T> Iterable<T> takeUnless(final Predicate<? super T> p, final Iterable<T> a) {
        return takeWhile(p.not(), a);
    }

    public static <T, R> R foldl(final Function2<? super R, ? super T, ? extends R> f, R e, final Iterable<T> a) {
        R b = e;
        for (T item : a) {
            b = f.apply(b, item);
        }
        return b;
    }

    public static <T, R> R foldr(final Function2<? super T, ? super R, ? extends R> f, R e, final Iterable<T> a) {
        R b = e;
        final Iterator<T> iter = a.iterator();

        if (!iter.hasNext()) {
            return e;
        }
        final T item = iter.next();
        Iterable<T> it = new Iterable<T>() {
            public Iterator<T> iterator() {
                return iter;
            }
        };
        return f.apply(item, foldr(f, e, it));
    }
}

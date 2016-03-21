package ru.spbau.mit;


public abstract class Predicate<T> {

    public abstract Boolean apply(T arg);

    public Predicate<T> or(final Predicate<? super T> pred) {
        return new Predicate<T>() {
            public Boolean apply(T arg) {
                return Predicate.this.apply(arg) || pred.apply(arg);
            }
        };
    }

    public Predicate<T> and(final Predicate<? super T> pred) {
        return new Predicate<T>() {
            public Boolean apply(T arg) {
                return Predicate.this.apply(arg) && pred.apply(arg);
            }
        };
    }

    public Predicate<T> not() {
        return new Predicate<T>() {
            public Boolean apply(T arg) {
                return !Predicate.this.apply(arg);
            }
        };
    }

    static final Predicate ALWAYS_TRUE = new Predicate() {
        public Boolean apply(Object arg) {
            return true;
        }
    };

    static final Predicate ALWAYS_FALSE = new Predicate() {
        public Boolean apply(Object arg) {
            return false;
        }
    };

}

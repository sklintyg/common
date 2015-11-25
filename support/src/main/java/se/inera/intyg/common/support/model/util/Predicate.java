package se.inera.intyg.common.support.model.util;

public abstract class Predicate<T> {
    public abstract boolean apply(T t);
}

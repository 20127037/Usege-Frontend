package com.group_1.usege.utilities.collection;

import java.util.List;
import java.util.function.Predicate;

public class CollectionUtilities {
    static public <T> T find(List<T> elements, Predicate<T> p) {
        for (T item : elements) if (p.test(item)) return item;
        return null;
    }

    static public <T> T find(T[] elements, Predicate<T> p) {
        for (T item : elements) if (p.test(item)) return item;
        return null;
    }
}

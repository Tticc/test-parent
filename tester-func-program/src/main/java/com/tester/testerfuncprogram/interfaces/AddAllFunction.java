package com.tester.testerfuncprogram.interfaces;


import java.util.Collection;

@FunctionalInterface
public interface AddAllFunction<E> {
    Collection<E> addAll(Collection<E> a, Collection<E> b);
}

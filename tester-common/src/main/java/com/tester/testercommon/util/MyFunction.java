package com.tester.testercommon.util;

import com.tester.testercommon.exception.BusinessException;

import java.util.Objects;

/**
 * 比自带的Supplier多了一个异常抛出声明
 * @Date 2020-6-23
 * @Author 温昌营
 * @see java.util.function.Function
 *
 **/
@FunctionalInterface
public interface MyFunction<T, R> {

    /**
     * Applies this function to the given argument.
     *
     * @param t the function argument
     * @return the function result
     */
    R apply(T t) throws BusinessException;

    /**
     * Returns a composed function that first applies the {@code before}
     * function to its input, and then applies this function to the result.
     * If evaluation of either function throws an exception, it is relayed to
     * the caller of the composed function.
     *
     * @param <V> the type of input to the {@code before} function, and to the
     *           composed function
     * @param before the function to apply before this function is applied
     * @return a composed function that first applies the {@code before}
     * function and then applies this function
     * @throws NullPointerException if before is null
     *
     * @see #andThen(MyFunction)
     */
    default <V> MyFunction<V, R> compose(MyFunction<? super V, ? extends T> before) throws BusinessException {
        Objects.requireNonNull(before);
        return (V v) -> apply(before.apply(v));
    }

    /**
     * Returns a composed function that first applies this function to
     * its input, and then applies the {@code after} function to the result.
     * If evaluation of either function throws an exception, it is relayed to
     * the caller of the composed function.
     *
     * @param <V> the type of output of the {@code after} function, and of the
     *           composed function
     * @param after the function to apply after this function is applied
     * @return a composed function that first applies this function and then
     * applies the {@code after} function
     * @throws NullPointerException if after is null
     *
     * @see #compose(MyFunction)
     */
    default <V> MyFunction<T, V> andThen(MyFunction<? super R, ? extends V> after) throws BusinessException {
        Objects.requireNonNull(after);
        return (T t) -> after.apply(apply(t));
    }

    /**
     * Returns a function that always returns its input argument.
     *
     * @param <T> the type of the input and output objects to the function
     * @return a function that always returns its input argument
     */
    static <T> MyFunction<T, T> identity() throws BusinessException {
        return t -> t;
    }
}

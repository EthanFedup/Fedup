package com.evertech.core.definition

/**
 * 作用同[java.util.function.BiConsumer]，但前者要求sdk最低为24.
 * 该类不同于[io.reactivex.functions.BiConsumer]，不会抛异常.
 */
interface JBiConsumer<T1, T2> {
    /**
     * Performs an operation on the given values.
     *
     * @param t1 the first value
     * @param t2 the second value
     */
    fun accept(t1: T1, t2: T2)
}

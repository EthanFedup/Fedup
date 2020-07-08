package com.evertech.core.definition

/**
 * 作用同[java.util.function.Consumer]，但前者要求sdk最低为24.
 * 该类不同于[io.reactivex.functions.Consumer]，不会抛异常.
 */
interface JConsumer<T> {
    /**
     * Consume the given value.
     *
     * @param t the value
     */
    fun accept(t: T)
}

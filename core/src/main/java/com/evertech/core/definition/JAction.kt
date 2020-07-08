package com.evertech.core.definition

/**
 * 不抛异常版的[io.reactivex.functions.Action].
 *
 *
 * 注：
 * 不同于Consumer，JDK8中没有Action类.
 * 但为了区别RxJava2的Action，延用与[JConsumer]相同的命名方式.
 */
interface JAction {
    /**
     * Runs the action.
     */
    fun run()
}

package com.evertech.core.fetch.strategy


import com.evertech.core.mvp.view.IView

/**
 * author : Ethan
 * e-mail : Ethan@fedup.cn
 * date   : 7/2/2020 3:56 PM
 * desc   :数据请求相关的各种策略
 * * 主要分三大类型：
 * * 1.无网  2.业务逻辑错误  3.其他错误
 * * 注：“空数据”只有列表才有，并且是由[PtrLmHandler]配置的，所以不用处理。
 */
open class BaseFetchStrategy {

    /* 非常重要的IView */
    var view: IView? = null

    /* 显示全局通用的加载页 */
    var enableCommonLoadingView = true

    /* 显示"无网”提示的Toast */
    var enableNoNetToast = true

    /* 显示“其他异常”的Toast */
    var enableErrorToast = true

    /* 显示“业务逻辑错误”的Toast */
    var enableBizErrorToast = true

    /* 可以显示"无网”的状态提示页（前提是当前页没数据） */
    var enableNoNetPage: Boolean = false

    /* 可以显示“其他异常”的状态提示页（前提是当前页没数据） */
    var enableErrorPage: Boolean = false

    companion object {

        const val DEFAULT_ERROR_TOAST = "网络请求异常"

        const val DEFAULT_BIZ_ERROR_TOAST = "服务器异常"

        const val TOKEN_INVALID_TOAST = "登录状态失效，请重新登录"
    }

}

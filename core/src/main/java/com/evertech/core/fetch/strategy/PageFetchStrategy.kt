package com.evertech.core.fetch.strategy

/**
 * author : Ethan
 * e-mail : Ethan@fedup.cn
 * date   : 7/2/2020 3:57 PM
 * desc   :
 */
 class PageFetchStrategy : BaseFetchStrategy() {
    /* 显示全局通用的加载页，但是，不关闭让框架取消；默认不开启 */
    var enableCommonLoadingViewWithoutDismiss: Boolean = false
}

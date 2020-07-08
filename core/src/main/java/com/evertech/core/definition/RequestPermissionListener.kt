package com.evertech.core.definition

/**
 *    author : Ethan
 *    e-mail : Ethan@fedup.cn
 *    date   : 7/2/2020 3:57 PM
 *    desc   :
 */
interface RequestPermissionListener {
    /**
     * 小于[android.os.Build.VERSION_CODES.M]必然调用，
     * 大于或者等于时，只有申请的权限全部同意才会调用
     */
    fun onAcceptAllPermissions()

    /**
     * 该方法只有在大于或者等于[android.os.Build.VERSION_CODES.M]才有可能被调用
     */
    fun onDenySomePermissions(deniedPermissions: List<String>)
}

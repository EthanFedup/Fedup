package com.evertech.core.util

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import androidx.fragment.app.FragmentActivity
import androidx.core.content.ContextCompat

import com.blankj.utilcode.util.ObjectUtils
import com.evertech.core.BaseApp
import com.evertech.core.definition.RequestPermissionListener
import com.tbruyelle.rxpermissions2.Permission
import com.tbruyelle.rxpermissions2.RxPermissions

import java.util.ArrayList

import io.reactivex.functions.Action
import io.reactivex.functions.Consumer

class PermissionHelper {
    private var deniedPermissions: MutableList<String>? = null

    /**
     * 申请权限通用方法.
     *
     * @param activity    Activity级为目标
     * @param listener    权限申请的结果回调
     * @param permissions 待申请权限列表
     */
    @SuppressLint("CheckResult")
    fun requestPermissions(
        activity: FragmentActivity,
        listener: RequestPermissionListener?,
        permissions: Array<String>
    ) {
        if (listener == null) {
            return
        }

        if (ObjectUtils.isEmpty(permissions)) {
            listener.onAcceptAllPermissions()
            return
        }

        deniedPermissions = ArrayList()

        RxPermissions(activity)
            .requestEach(*permissions)
            .doOnComplete {
                if (deniedPermissions!!.size == 0) {
                    deniedPermissions = null
                    listener.onAcceptAllPermissions()
                } else {
                    listener.onDenySomePermissions(deniedPermissions!!)
                }
            }
            .subscribe { permission ->
                if (!permission.granted) {
                    deniedPermissions!!.add(permission.name)
                }
            }
    }

    companion object {

        private val context: Context
            get() = BaseApp.app

        /**
         * 验证某个权限是否已授权
         */
        fun isGranted(permission: String): Boolean {
            return ContextCompat.checkSelfPermission(
                context,
                permission
            ) == PackageManager.PERMISSION_GRANTED
        }
    }

}

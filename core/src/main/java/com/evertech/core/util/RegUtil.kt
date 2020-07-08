package com.evertech.core.util

import android.R.string.no
import java.util.regex.Pattern


/**
 *    author : Ethan
 *    e-mail : Ethan@fedup.cn
 *    date   : 7/8/2020 9:34 AM
 *    desc   :
 */
object RegUtil {

    /**
     * 邮箱验证
     */
    fun isEmail(emailText :String): Boolean {
        val regEmail =   "[A-Z0-9a-z._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,64}"

        val p = Pattern.compile(regEmail)
        val m = p.matcher(emailText)
        return m.matches()
    }

   /**
     * 邮箱密码
     */
    fun isPassword(passText :String): Boolean {
        val regPass =  "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{8,20}$"

        val p = Pattern.compile(regPass)
        val m = p.matcher(passText)
        return m.matches()
    }



}
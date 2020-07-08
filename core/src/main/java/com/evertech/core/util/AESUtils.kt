package com.evertech.core.util

import android.util.Base64
import com.blankj.utilcode.util.LogUtils

import java.nio.charset.StandardCharsets

import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

class AESUtils {


    /*
     * 加密用的Key 可以用26个字母和数字组成 此处使用AES-128-CBC加密模式，key需要为16位。
     */
    private val sKey = "fedup2020fedup20"
    private val ivParameter = "abcdefghijklmnop"


    // 加密
    @Throws(Exception::class)
    fun encrypt2(sSrc: String): String {
        val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
        val raw = sKey.toByteArray()
        val skeySpec = SecretKeySpec(raw, "AES")
        val iv = IvParameterSpec(ivParameter.toByteArray())// 使用CBC模式，需要一个向量iv，可增加加密算法的强度
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv)
        val encrypted = cipher.doFinal(sSrc.toByteArray(StandardCharsets.UTF_8))
        return  Base64.encodeToString(encrypted, Base64.NO_WRAP) // 此处使用BASE64做转码。
    }



    fun encrypt(sSrc: String): String{
       val oldText = encrypt2(sSrc)
        LogUtils.d("encrypt--0000000---"+encrypt2(sSrc))
        LogUtils.d("encrypt---111---"+ StringUtil.unicodeStr2String(oldText))
      return  StringUtil.unicodeStr2String(encrypt2(sSrc))
    }


    // 解密
    @Throws(Exception::class)
    fun decrypt(sSrc: String): String? {
        try {
            val raw = sKey.toByteArray(StandardCharsets.US_ASCII)
            val skeySpec = SecretKeySpec(raw, "AES")
            val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
            val iv = IvParameterSpec(ivParameter.toByteArray())
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv)
            val encrypted1 = Base64.decode(sSrc, Base64.DEFAULT)// 先用base64解密
            val original = cipher.doFinal(encrypted1)
            return String(original, StandardCharsets.UTF_8)
        } catch (ex: Exception) {
            return null
        }

    }

    // 解密
    @Throws(Exception::class)
    fun decrypt(sSrc: String, key: String): String? {
        try {
            val raw = key.toByteArray(StandardCharsets.US_ASCII)
            val skeySpec = SecretKeySpec(raw, "AES")
            val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
            val iv = IvParameterSpec(ivParameter.toByteArray())
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv)
            val encrypted1 = Base64.decode(sSrc, Base64.DEFAULT)// 先用base64解密
            val original = cipher.doFinal(encrypted1)
            return String(original, StandardCharsets.UTF_8)
        } catch (ex: Exception) {
            return null
        }


    }

    companion object {
        private var instance: AESUtils? = null

        //    private AESUtils(String sKey) {
        //        this.sKey = sKey;
        //    }

        fun getInstance(): AESUtils {
            if (instance == null)
                instance = AESUtils()
            return instance as AESUtils
        }

        // 加密
        @Throws(Exception::class)
        fun encrypt(encData: String, secretKey: String?, vector: String): String? {
            if (secretKey == null) {
                return null
            }
            if (secretKey.length != 16) {
                return null
            }
            val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
            val raw = secretKey.toByteArray()
            val skeySpec = SecretKeySpec(raw, "AES")
            val iv = IvParameterSpec(vector.toByteArray())// 使用CBC模式，需要一个向量iv，可增加加密算法的强度
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv)
            val encrypted = cipher.doFinal(encData.toByteArray(StandardCharsets.UTF_8))
            return Base64.encodeToString(
                encrypted,
                Base64.DEFAULT
            )// 此处使用BASE64做转码。（处于android.util包）
        }
    }
}

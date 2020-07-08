package com.evertech.Fedup

import com.blankj.utilcode.util.LogUtils
import com.evertech.core.util.AESUtils
import com.evertech.core.util.StringUtil
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
//        assertEquals(4, 2 + 2)

        val textEqu = "\\u003d"
        val url ="https://minigamma.fedup.cn/h5Html/index.html?name\\u003dservice"

        System.out.println("initViews--222--$url")
        System.out.println("initViews--333--$textEqu")
        if(url.contains(textEqu)){
            System.out.println("initViews--444--$url")
            System.out.println("initViews--555--$textEqu")

//                url =  url!!.replace("\"/","")
           var newUrl = url.replace(textEqu,"=")

            System.out.println("initViews--666--$newUrl")

        }
    }

    @Test
    fun testAes(){
        val phoneNum = "18201772200"
        val encrypt = AESUtils.getInstance().encrypt(phoneNum)
        val decryptString = AESUtils.getInstance().decrypt(encrypt)


        System.out.println("testAes-000--"+encrypt)
        System.out.println("testAes-111--"+decryptString)

//        LogUtils.d("testAes-000--"+encrypt)
//        LogUtils.d("testAes-111--"+decryptString)
    }

    @Test
    fun testUni(){

//        var oldText ="sO6tndMLl8S/vFGRPxCKOw\u003d\u003d\n"
        var oldText ="3vq0ABbdQyjOkGoW3oWBhA\u003d\u003d"

        val newText = StringUtil.unicodeStr2String(oldText)

        System.out.println("testUni=----"+newText)
    }
}

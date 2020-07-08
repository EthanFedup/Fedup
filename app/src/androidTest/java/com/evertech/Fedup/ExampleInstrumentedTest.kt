package com.evertech.Fedup

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.evertech.core.util.AESUtils

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.evertech.fedup", appContext.packageName)
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
}

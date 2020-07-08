package com.evertech.Fedup

import com.evertech.core.BaseApp
import com.alibaba.android.arouter.launcher.ARouter
import me.jessyan.autosize.AutoSizeConfig
import androidx.core.content.ContextCompat.getSystemService
import me.jessyan.autosize.unit.Subunits


/**
 *    author : Ethan
 *    e-mail : Ethan@fedup.cn
 *    date   : 7/3/2020 11:38 AM
 *    desc   :
 */
class MyApp : BaseApp() {

    override fun commonInit() {
        super.commonInit()
        initAutoSize()
        initARouter()
    }

    private fun initAutoSize() {
        AutoSizeConfig.getInstance().getUnitsManager()
            //是否让框架支持自定义 Fragment 的适配参数, 由于这个需求是比较少见的, 所以须要使用者手动开启
            //如果没有这个需求建议不开启
    //            .setCustomFragment(false)
            //是否屏蔽系统字体大小对 AndroidAutoSize 的影响, 如果为 true,
            // App 内的字体的大小将不会跟随系统设置中字体大小的改变
            //如果为 false, 则会跟随系统设置中字体大小的改变, 默认为 false
    //            .setExcludeFontScale(false)
            //是否使用设备的实际尺寸做适配, 默认为 false, 如果设置为 false, 在以屏幕高度为基准进行适配时
            //AutoSize 会将屏幕总高度减去状态栏高度来做适配
            //设置为 true 则使用设备的实际屏幕高度, 不会减去状态栏高度
    //            .setUseDeviceSize(true)
            //是否全局按照宽度进行等比例适配, 默认为 true, 如果设置为 false, AutoSize 会全局按照高度进行适配
    //            .setBaseOnWidth(false)
            .setSupportDP(false)
            .setSupportSP(false)
            .setSupportSubunits(Subunits.MM)
            .supportSubunits = Subunits.PT

//            .setLog(BuildConfig.BUILD_TYPE != "release")

        //        在任何情况下本来适配正常的布局突然出现适配失效，适配异常等问题，只要重写
        //        Activity 的 getResources() 方法即可，
        //        如果是 Dialog、PopupWindow 等控件出现适配失效或适配异常，同样在每次 show()
        //        之前调用 AutoSize#autoConvertDensity() 即可。
    }

    private fun initARouter() {
        if (BuildConfig.DEBUG) {
            ARouter.openLog()
            ARouter.openDebug()
        }
        ARouter.init(this)
    }


    override fun onTerminate() {
        super.onTerminate()
        ARouter.getInstance().destroy()
    }
}
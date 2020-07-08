package com.evertech.core.definition

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

import com.blankj.utilcode.util.ObjectUtils
import com.evertech.core.mvp.view.BaseFragment

import java.util.ArrayList

/**
 *    author : Ethan
 *    e-mail : Ethan@fedup.cn
 *    date   : 7/2/2020 3:10 PM
 *    desc   :
 */
class BasePagerAdapter<T : BaseFragment> : FragmentPagerAdapter {
    private var mData: List<T>? = null

    constructor(fm: FragmentManager) : super(fm) {}

    constructor(fm: FragmentManager, data: List<T>?) : super(fm) {
        mData = data ?:ArrayList()
    }

    fun setData(data: List<T>?) {
        mData = data ?:ArrayList()
        notifyDataSetChanged()
    }

    override fun getItem(position: Int): Fragment? {
        return if (ObjectUtils.isEmpty(mData)) null else mData!![position]
    }

    override fun getCount(): Int {
        return if (ObjectUtils.isEmpty(mData)) 0 else mData!!.size
    }

}

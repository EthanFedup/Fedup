package com.evertech.Fedup.login.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.android.arouter.facade.annotation.Route
import com.blankj.utilcode.util.LogUtils
import com.evertech.Fedup.R
import com.evertech.Fedup.login.Presenter.CountryCodePresenter
import com.evertech.Fedup.login.adapter.CountryCodeAdapter
import com.evertech.Fedup.login.adapter.CountryInnerAdapter
import com.evertech.Fedup.login.contract.CountryCodeContract
import com.evertech.Fedup.login.model.Country
import com.evertech.Fedup.login.model.WrapCountryData
import com.evertech.core.app.Path
import com.evertech.core.mvp.view.BaseActivity
import com.evertech.core.widget.SideBar
import kotlinx.android.synthetic.main.activity_country_code.*


/**
 *    author : Ethan
 *    e-mail : Ethan@fedup.cn
 *    date   : 7/7/2020 10:51 AM
 *    desc   :
 */
@Route(path = Path.Login.COUNTRY_CODE)
class CountryCodeActivity : BaseActivity(), CountryCodeContract.View {


    private var listCountry: MutableList<WrapCountryData> = ArrayList()
    private var allSubListCountry: MutableList<Country> = ArrayList()
    private var searchListCountry: MutableList<Country> = ArrayList()

    private val countryCodePresenter = CountryCodePresenter()

    private val countryCodeAdapter = CountryCodeAdapter(ArrayList(), this)
    private var searchAdapter = CountryInnerAdapter(R.layout.item_contry_code_layout, ArrayList())

    override fun layoutResId() = R.layout.activity_country_code

    override fun initData() {
        super.initData()
        addPresenter(countryCodePresenter)
    }

    override fun initViews() {
        titleBar.setLeftIconType(1).setTitle("选择国家或地区")
        initRv()
        initListener()
    }


    override fun onFinishInit() {
        super.onFinishInit()
        countryCodePresenter.getCountryCodeList()
    }

    private fun initRv() {
        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        rvList.layoutManager = layoutManager
        rvList.adapter = countryCodeAdapter

        val layoutManagerSearch = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        rvSearch.layoutManager = layoutManagerSearch
        rvSearch.adapter = searchAdapter


    }

    private fun initListener() {
        countryCodeAdapter.setOnItemClickListener { country ->
            val intent = Intent().putExtra("codeBean", country)
            LogUtils.d("countryCodeAdapter--00---" + country.number)
            setResult(Activity.RESULT_OK, intent)
            finish()
        }

        sideBar.setOnTouchingLetterChangedListener { s ->
            val position = countryCodeAdapter.getPositionForSection(s[0].toInt())
            if (position != -1) {
                smoothMoveToPosition(rvList, position);
            } else {
                smoothMoveToPosition(rvList, position + 1);
            }
        }

        rvList.addOnScrollListener(object :RecyclerView.OnScrollListener(){
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if(mShouldScroll&&RecyclerView.SCROLL_STATE_IDLE==newState){
                    mShouldScroll = false
                    smoothMoveToPosition(rvList,mToPosition)
                }
            }
        })

        et_search.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable) {}

            override fun beforeTextChanged(p0: CharSequence, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(charSequence: CharSequence, p1: Int, p2: Int, p3: Int) {
                if (charSequence.isNotEmpty()) {
                    searchData(charSequence.toString())
                    rvSearch.visibility = View.VISIBLE
                } else {
                    rvSearch.visibility = View.GONE
                }
            }
        })

    }


    private fun searchData(keyword: String) {
        searchListCountry.clear()
        for ((index, element) in allSubListCountry.withIndex()) {
            println("第${index}种水果是$element")
            if (element.number.contains(keyword) || element.name.contains(keyword)) {
                searchListCountry.add(element)
            }
        }

        searchAdapter.setNewData(searchListCountry)

    }

    override fun onCountryCodeResult(
        countryCodeList: MutableList<WrapCountryData>,
        stateCode: Int
    ) {

        if (countryCodeList.isNotEmpty()) {
            listCountry = countryCodeList
            countryCodeList.sortedBy { it.name }
            if (countryCodeList[0].name == "#") {
                val wrapCountryData = countryCodeList[0]
                countryCodeList.removeAt(0)
                countryCodeList.add(wrapCountryData)
            }

            for ((index, element) in listCountry.withIndex()) {
                println("第${index}种水果是$element")
                for ((index, subElement) in element.countrys.withIndex()) {
                    subElement.mWord = element.name
                    allSubListCountry.add(subElement)
                }
            }
            countryCodeAdapter.setAllData(allSubListCountry)
        }
    }


    /**
     * 目标项是否在最后一个可见项之后
     */
    private var mShouldScroll: Boolean = false
    /**
     * 记录目标项位置
     */
    private var mToPosition: Int = 0

    /**
     * 滑动到指定位置
     */
    private fun smoothMoveToPosition(mRecyclerView: RecyclerView, position: Int) {
        // 第一个可见位置
        val firstItem = mRecyclerView.getChildLayoutPosition(mRecyclerView.getChildAt(0))
        // 最后一个可见位置
        val lastItem = mRecyclerView.getChildLayoutPosition(mRecyclerView.getChildAt(mRecyclerView.childCount - 1))
        if (position < firstItem) {
            // 第一种可能:跳转位置在第一个可见位置之前
            mRecyclerView.smoothScrollToPosition(position)
        } else if (position <= lastItem) {
            // 第二种可能:跳转位置在第一个可见位置之后
            val movePosition = position - firstItem
            if (movePosition >= 0 && movePosition < mRecyclerView.childCount) {
                val top = mRecyclerView.getChildAt(movePosition).top
                mRecyclerView.smoothScrollBy(0, top)
            }
        } else {
            // 第三种可能:跳转位置在最后可见项之后
            mRecyclerView.smoothScrollToPosition(position)
            mToPosition = position
            mShouldScroll = true
        }
    }

}

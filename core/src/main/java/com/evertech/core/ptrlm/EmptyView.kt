package com.evertech.core.ptrlm

import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.StringRes

import com.evertech.core.R
import com.evertech.core.R2

import com.evertech.core.widget.CustomView

import butterknife.BindView

import com.blankj.utilcode.util.StringUtils.getString
import com.blankj.utilcode.util.StringUtils.isTrimEmpty

/**
 * @Author Shuo
 * @Create 2018-12-03
 *
 *
 * @Desc
 */
class EmptyView : CustomView {

    @JvmField
    @BindView(R2.id.iv_hint)
    internal var ivHint: ImageView? = null

    @JvmField
    @BindView(R2.id.tv_hint)
    internal var tvHint: TextView? = null

    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {}

    override fun contentResID(): Int {
        return R.layout.widget_empty_view
    }

    override fun init(attrs: AttributeSet?) {
        super.init(attrs)
        setEmptyType(EMPTY_DATA_COMMON)
    }

    fun setEmptyType(type: Int) {
        when (type) {
            EMPTY_DATA_COMMON -> {
                tvHint!!.setText(R.string.state_empty_data)
//                ivHint!!.setImageResource(R.mipmap.ic_state_empty_data)
            }
            EMPTY_DATA_SEARCH -> {
                tvHint!!.setText(R.string.state_empty_search_data)
//                ivHint!!.setImageResource(R.mipmap.ic_state_empty_search_result)
            }
            EMPTY_HOTEL_FILTER_RESULT -> {
                tvHint!!.setText(R.string.state_empty_hotel_filter_result)
//                ivHint!!.setImageResource(R.mipmap.ic_state_empty_data)
            }
            else -> {
                tvHint!!.setText(R.string.state_empty_data)
//                ivHint!!.setImageResource(R.mipmap.ic_state_empty_data)
            }
        }
    }

    fun setEmptyText(text: String) {
        if (!isTrimEmpty(text)) {
            tvHint!!.text = text
        }
    }

    fun setEmptyText(@StringRes resID: Int) {
        setEmptyText(getString(resID))
    }

    companion object {

        /* 普通空数据页 */
       const val EMPTY_DATA_COMMON = 1

        /* 搜索空数据页 */
        const   val EMPTY_DATA_SEARCH = 2

        /* 筛选空数据页 */
        const val EMPTY_HOTEL_FILTER_RESULT = 3
    }

}

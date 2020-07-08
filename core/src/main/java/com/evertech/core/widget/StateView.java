package com.evertech.core.widget;

import android.content.Context;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.evertech.core.R;
import com.evertech.core.R2;
import com.evertech.core.fetch.BaseFetcher;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @Author Shuo
 * @Create 2018-12-22
 */
public class StateView extends CustomView implements View.OnClickListener {

    @BindView(R2.id.tv_hint) TextView tvHint;

    @BindView(R2.id.iv_hint) ImageView ivHint;


    /**
     * 正在调用的Fetcher
     */
    private BaseFetcher mFetcher;
    /**
     * 当前页的数据是否为空
     */
    private boolean isDataEmpty = true;

    public StateView(Context context) {
        super(context);
    }

    public StateView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected int contentResID() {
        return R.layout.widget_state_view;
    }

    @OnClick(R2.id.tv_retry)
    public void onClick(View v) {
        if (mFetcher != null) {
            mFetcher.start();
        }
    }

    public void showNoNetworkView() {
        this.setVisibility(View.VISIBLE);
        tvHint.setText(R.string.state_no_network);
//        ivHint.setImageResource(R.mipmap.ic_state_no_network);
    }

    public void showErrorView() {
        this.setVisibility(View.VISIBLE);
        tvHint.setText(R.string.state_error_data);
//        ivHint.setImageResource(R.mipmap.ic_state_loading_error);
    }

    public void setFetcher(BaseFetcher fetcher) {
        mFetcher = fetcher;
    }

    public boolean isDataEmpty() {
        return isDataEmpty;
    }

    public void validateData() {
        isDataEmpty = false;
    }

}

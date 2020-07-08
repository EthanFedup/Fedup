package com.evertech.core.widget;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import androidx.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;

import com.blankj.utilcode.util.LogUtils;
import com.evertech.core.R;


/**
 *    author : Ethan
 *    e-mail : Ethan@fedup.cn
 *    date   : 7/6/2020 6:44 PM
 *    desc   :
 */
public class PageLoadingView extends Dialog {

    private Activity activity;
    private volatile static PageLoadingView mSingleInstance = null;


    public static PageLoadingView getInstance(Context context) {
        if (mSingleInstance == null) {
            synchronized (PageLoadingView.class) {
                if (mSingleInstance == null) {
                    mSingleInstance = new PageLoadingView(context);
                }
            }
        }
        return mSingleInstance;

    }

    private RealPageLoadingView rplv;

    public PageLoadingView(@NonNull Context context) {
        super(context, R.style.dialog_dim);
        activity = (Activity) context;
        LogUtils.d("PageLoadingView--0000----");
        initViews();
    }

    private void initViews() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.widget_page_loading_view, null);
        rplv = view.findViewById(R.id.rplv);
        setContentView(view);
        setCancelable(false);
    }

    @Override
    public void setCancelable(boolean cancelable) {
        super.setCancelable(cancelable);
        super.setCanceledOnTouchOutside(cancelable);
    }

    @Override
    public void show() {
        if(activity.isFinishing()){
           return;
        }
        rplv.startAnim();
        super.show();
    }

    @Override
    public void dismiss() {

        LogUtils.d("PageLoadingView--dismiss----000--");

        rplv.stopAnim();
        rplv.stopAnim();
        LogUtils.d("PageLoadingView--dismiss----111--");

        super.dismiss();
    }

}

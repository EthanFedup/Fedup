package com.evertech.core.widget;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.ConvertUtils;
import com.blankj.utilcode.util.ScreenUtils;
import com.evertech.core.R;
import com.evertech.core.definition.JAction;
import com.evertech.core.util.ResourceUtils;
import com.evertech.core.util.ViewHelper;

import androidx.annotation.ColorRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.StringRes;

import static com.evertech.core.util.ViewHelper.WRAP;


/**
 *    author : Ethan
 *    e-mail : Ethan@fedup.cn
 *    date   : 7/2/2020 6:22 PM
 *    desc   :
 */
public class SimpleAlertView {

    private SimpleDialog mDialog;

    private FrameLayout flRoot;

    private LinearLayout llContent, llBothText;

    private TextView tvTitle, tvContent, tvLeftText, tvRightText, tvSingleText;

    private JAction mRightAction, mLeftAction, mSingleAction, mDismissAction;

    /* 主题色 */
    public static final int COLOR_THEME = Color.parseColor("#fdce2f");
    /* 蓝色 */
    public static final int COLOR_BLUE = Color.parseColor("#257cf5");
    /* 黄色 */
    public static final int COLOR_YELLOW = Color.parseColor("#fdce2f");
    /* 红色 */
    public static final int COLOR_RED = Color.parseColor("#ea6226");
    /* 灰色 */
    public static final int COLOR_GRAY = Color.parseColor("#b1b2b2");
    /*白色*/
    public static final int COLOR_WHITE = Color.parseColor("#fffffe");

    private SimpleAlertView(Context context) {
        mDialog = SimpleDialog.Builder.newBuilder(context)
                .layoutResID(getLayoutResID())
                .widthAndHeight(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
                .animation(R.anim.scale_in, R.anim.scale_out)
                .cancelOnTouchOutside(true)
                .duration(200)
                .build();

        initViews();
    }

    private @LayoutRes
    int getLayoutResID() {
        return R.layout.widget_simple_alert_view ;
    }

    private void initViews() {
        View view = mDialog.getView();
        flRoot = view.findViewById(R.id.fl_root);
        llContent = view.findViewById(R.id.ll_alert_content);
        llBothText = view.findViewById(R.id.ll_tv_alert_both_text);
        tvTitle = view.findViewById(R.id.tv_alert_title);
        tvContent = view.findViewById(R.id.tv_alert_content);
        tvSingleText = view.findViewById(R.id.tv_alert_single_text);
        tvLeftText = view.findViewById(R.id.tv_alert_left_text);
        tvRightText = view.findViewById(R.id.tv_alert_right_text);
        showBothText(true);

        float widthRate =  0.9f ;
        int width = (int) (ScreenUtils.getScreenWidth() * widthRate);
        FrameLayout.LayoutParams params = ViewHelper.newFrameParams(width, WRAP);
        params.bottomMargin = ConvertUtils.dp2px(20);
        params.gravity = Gravity.CENTER;
        llContent.setLayoutParams(params);

        llContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        tvSingleText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SimpleAlertView.this.dismiss(true);
                if (mSingleAction != null) mSingleAction.run();
            }
        });
        tvRightText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SimpleAlertView.this.dismiss(true);
                if (mRightAction != null) mRightAction.run();
            }
        });
        tvLeftText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SimpleAlertView.this.dismiss(true);
                if (mLeftAction != null) mLeftAction.run();
            }
        });
    }

    public static SimpleAlertView newInstance(Context context) {
        return new SimpleAlertView(context);
    }

    public SimpleAlertView show() {
        if (ViewHelper.isTrimEmptyText(tvTitle)) {
            tvTitle.setVisibility(View.GONE);
        }
        if (ViewHelper.isTrimEmptyText(tvContent)) {
            tvContent.setVisibility(View.GONE);
        }
        mDialog.show();
        return this;
    }

    public boolean isShowing() {
        return mDialog.isShowing();
    }

    public SimpleAlertView dismiss() {
        dismiss(false);
        return this;
    }

    public SimpleAlertView dismiss(boolean reactDismiss) {
        mDialog.dismiss(reactDismiss);
        return this;
    }

    private void showBothText(boolean tag) {
        llBothText.setVisibility(tag ? View.VISIBLE : View.GONE);
        tvSingleText.setVisibility(tag ? View.GONE : View.VISIBLE);
    }

    /* 开启显示单个按钮，默认不开启 */
    public SimpleAlertView enableSingleText() {
        showBothText(false);
        return this;
    }

    /* 开启背景模糊，默认不开启 */
    public SimpleAlertView enableBlurBackground() {
        mDialog.setBlurBackgroundEnable(true);
        return this;
    }

    /* 开启"禁掉back press键"，默认不开启 */
    public SimpleAlertView disableBackPressed() {
        mDialog.setBackPressEnable(false);
        return this;
    }

    /* 是否点击外面取消，默认是 */
    public SimpleAlertView cancelOnTouchOutside(final boolean mTag) {
        flRoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mTag) SimpleAlertView.this.dismiss(false);
            }
        });
        return this;
    }

    public SimpleAlertView content(String text) {
        tvContent.setText(text);
        return this;
    }

    public SimpleAlertView content(@StringRes int strRes) {
        tvContent.setText(strRes);
        return this;
    }

    public SimpleAlertView title(String text) {
        tvTitle.setText(text);
        return this;
    }

    public SimpleAlertView title(@StringRes int strRes) {
        tvTitle.setText(strRes);
        return this;
    }

    public SimpleAlertView leftText(String text) {
        tvLeftText.setText(text);
        return this;
    }

    public SimpleAlertView leftText(@StringRes int strRes) {
        tvLeftText.setText(strRes);
        return this;
    }

    public SimpleAlertView rightText(String text) {
        tvRightText.setText(text);
        return this;
    }

    public SimpleAlertView rightText(@StringRes int strRes) {
        tvRightText.setText(strRes);
        return this;
    }

    public SimpleAlertView singleText(String text) {
        tvSingleText.setText(text);
        return this;
    }

    public SimpleAlertView singleText(@StringRes int strRes) {
        tvSingleText.setText(strRes);
        return this;
    }

    public SimpleAlertView leftTextColorRes(@ColorRes int colorRes) {
        int color = ResourceUtils.getColor(colorRes);
        return leftTextColor(color);
    }

    public SimpleAlertView leftTextColor(int color) {
        tvLeftText.setTextColor(color);
        return this;
    }

    public SimpleAlertView rightTextColorRes(@ColorRes int colorRes) {
        int color = ResourceUtils.getColor(colorRes);
        return rightTextColor(color);
    }

    public SimpleAlertView rightTextColor(int color) {
        tvRightText.setTextColor(color);
        return this;
    }

    public SimpleAlertView singleTextColorRes(@ColorRes int colorRes) {
        int color = ResourceUtils.getColor(colorRes);
        return singleTextColor(color);
    }

    public SimpleAlertView singleTextColor(int color) {
        tvSingleText.setTextColor(color);
        return this;
    }

    public SimpleAlertView titleColorRes(@ColorRes int colorRes) {
        int color = ResourceUtils.getColor(colorRes);
        return titleTextColor(color);
    }

    public SimpleAlertView titleTextColor(int color) {
        tvTitle.setTextColor(color);
        return this;
    }

    public SimpleAlertView onBeforeDismiss(JAction jAction) {
        mDialog.onBeforeDismissAction(jAction);
        return this;
    }

    public SimpleAlertView singleClick(JAction action) {
        mSingleAction = action;
        return this;
    }

    public SimpleAlertView leftClick(JAction action) {
        mLeftAction = action;
        return this;
    }

    public SimpleAlertView rightClick(JAction action) {
        mRightAction = action;
        return this;
    }

}

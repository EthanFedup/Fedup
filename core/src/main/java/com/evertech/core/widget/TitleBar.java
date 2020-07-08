package com.evertech.core.widget;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;

import androidx.annotation.DrawableRes;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;

import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.ObjectUtils;
import com.evertech.core.R;
import com.evertech.core.definition.JConsumer;
import com.evertech.core.util.ResourceUtils;

public class TitleBar extends CustomView implements View.OnClickListener {

    private TextView tvTitle;
    private ImageView ivLeft;
    private RelativeLayout rlBack;
    private ImageView ivLeftBold;
    private TextView tvRight;

    private JConsumer<View> mLeftClick;
    private JConsumer<View> mRightClick;

    public TitleBar(Context context) {
        super(context);
    }

    public TitleBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected int contentResID() {
        return R.layout.titlebar_mobile;
    }

    @Override
    protected void init(@Nullable AttributeSet attrs) {
        super.init(attrs);

        String title = null;
        String rightText = null;
        int leftIconResID = getDefaultLeftIcon();

        if (attrs != null) {
            TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.TitleBar);
            title = typedArray.getString(R.styleable.TitleBar_title);
            leftIconResID = typedArray.getResourceId(R.styleable.TitleBar_leftIcon, getDefaultLeftIcon());
            rightText = typedArray.getString(R.styleable.TitleBar_rightText);
        }

        title = ObjectUtils.isEmpty(title) ? "" : title;

        tvTitle = getContent().findViewById(R.id.tv_title);
        rlBack = getContent().findViewById(R.id.rl_back);
        ivLeft = getContent().findViewById(R.id.iv_title_bar_left);
        ivLeftBold = getContent().findViewById(R.id.iv_back_bold);
        tvRight = getContent().findViewById(R.id.tv_title_bar_right);

        setTitle(title).setLeftIcon(leftIconResID).setRightText(rightText);

        rlBack.setOnClickListener(this);

        tvRight.setOnClickListener(this);

        mLeftClick = new JConsumer<View>() {
            @Override
            public void accept(View view) {
                if (TitleBar.this.getContext() instanceof Activity) {
                    ((Activity) TitleBar.this.getContext()).onBackPressed();
                }
            }
        };


        fakeStatusBarHanding();
    }

    private @DrawableRes
    int getDefaultLeftIcon() {
        return R.mipmap.icon_back;
    }

    private void fakeStatusBarHanding() {
        View fakeStatusBar = getContent().findViewById(R.id.fake_status_bar);
        ViewGroup.LayoutParams params = fakeStatusBar.getLayoutParams();
        params.height = BarUtils.getStatusBarHeight();
        fakeStatusBar.setLayoutParams(params);
    }

    /**
     * 标题.
     */
    public TitleBar setTitle(@StringRes int resID) {
        return setTitle(ResourceUtils.getString(resID));
    }

    /**
     * 标题.
     */
    public TitleBar setTitle(String title) {
        if (ObjectUtils.isEmpty(title)) title = "";
        tvTitle.setText(title);
        return this;
    }

    /**
     * 左ICON. “-1”表示隐藏该ICON.
     */
    public TitleBar setLeftIcon(int resID) {
        if (resID == -1) {
            ivLeft.setVisibility(View.GONE);
        } else {
            ivLeftBold.setVisibility(View.VISIBLE);
            ivLeftBold.setImageResource(resID);
            ivLeft.setVisibility(View.GONE);
        }
        return this;
    }

    /**
     * 设置backIcon类型
     */
    public TitleBar setLeftIconType(int mType) {
        if (mType == 0) {
            ivLeftBold.setVisibility(View.VISIBLE);
            ivLeft.setVisibility(View.GONE);
        } else {
            ivLeft.setVisibility(View.VISIBLE);
            ivLeftBold.setVisibility(View.GONE);
        }
        return this;
    }

    /**
     * 右文本. “空”表示隐藏该文本.
     */
    public TitleBar setRightText(String text) {
        if (ObjectUtils.isEmpty(text)) {
            tvRight.setVisibility(GONE);
        } else {
            tvRight.setText(text);
            tvRight.setVisibility(View.VISIBLE);
        }
        return this;
    }

    /**
     * 右文本.
     */
    public TitleBar setRightText(@StringRes int resID) {
        setRightText(ResourceUtils.getString(resID));
        return this;
    }


    /**
     * 右边点击事件.
     */
    public TitleBar setRightClick(JConsumer<View> rightClick) {
        mRightClick = rightClick;
        return this;
    }

    /**
     * 左边点击事件.
     */
    public TitleBar setLeftClick(JConsumer<View> leftClick) {
        mLeftClick = leftClick;
        return this;
    }

    @Override
    public void onClick(View v) {
        int vieId = v.getId();
        if (vieId == R.id.rl_back) {
            if (mLeftClick != null) {
                mLeftClick.accept(v);
            } else {
            }
        } else if (vieId == R.id.tv_title_bar_right) {
            if (mRightClick != null) {
                mRightClick.accept(v);
            }
        }
    }

}

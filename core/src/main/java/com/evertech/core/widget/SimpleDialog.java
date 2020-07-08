package com.evertech.core.widget;

import android.content.Context;
import androidx.annotation.AnimRes;
import androidx.annotation.LayoutRes;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;


import com.evertech.core.R;
import com.evertech.core.definition.JAction;

import razerdp.basepopup.BasePopupWindow;
import razerdp.util.SimpleAnimationUtils;

/**
 * @Author Shuo
 * @Create 2018-11-13
 * <p>
 * @Desc
 */
public class SimpleDialog extends BasePopupWindow implements View.OnClickListener {
    private View mView;

    private Builder mBuilder;

    private JAction mBeforeDismissAction;

    /**
     * 是否是用户交互相关的Dismiss,
     * 为防止用户交互相关响应后的Dismiss与{@link #mBeforeDismissAction}默认的Dismiss的事件处理重复.
     */
    private boolean isReactDismiss;

    private SimpleDialog(Context context, Builder builder) {
        super(context);
        initViews(builder);
    }

    private void initViews(Builder builder) {
        FrameLayout flContent = getContentView().findViewById(R.id.fl_content);

        mView = LayoutInflater.from(getContext()).inflate(builder.mLayoutResID, null, false);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(builder.mWidth, builder.mHeight, builder.mGravity);
        flContent.addView(mView, params);
        flContent.setOnClickListener(this);
        setAllowDismissWhenTouchOutside(builder.mCancelOnTouchOutside);
        mBuilder = builder;
    }

    @Override
    public View onCreateContentView() {
        return createPopupById(R.layout.simple_dialog);
    }

    public View getView() {
        return mView;
    }

    public void show() {
        showPopupWindow();
    }

    @Override
    protected Animation onCreateShowAnimation() {
        return null;
    }

    @Override
    protected Animation onCreateDismissAnimation() {
        return null;
    }


    @Override
    public void dismiss() {
        dismiss(false);
    }

    @Override
    public void dismiss(boolean reactDismiss) {
        isReactDismiss = reactDismiss;
        super.dismiss(true);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.fl_content) {
            if (mBuilder.mCancelOnTouchOutside) {
                dismiss();
            }
        }
    }

    @Override
    public boolean onBeforeDismiss() {
        boolean onBeforeDismiss = super.onBeforeDismiss();
        Log.d("CCC", getClass().getSimpleName() +"onBeforeDismiss " + (onBeforeDismiss && mBeforeDismissAction != null && !isReactDismiss));
        if (onBeforeDismiss && mBeforeDismissAction != null && !isReactDismiss) {
            mBeforeDismissAction.run();
            isReactDismiss = false;
        }
        return onBeforeDismiss;
    }

    /*  点击取消前的调用(**前提是可以取消) */
    public void onBeforeDismissAction(JAction action) {
        mBeforeDismissAction = action;
    }

    public static class Builder {
        /* Context */
        private Context mContext;
        /* 布局ID */
        private int mLayoutResID;
        /* 长/宽 */
        private int mHeight, mWidth;
        /* 动画时长 */
        private int mDuration;
        /* 进/出动画 */
        private Animation mEnterAnim, mExitAnim;
        /* Gravity */
        private int mGravity;
        /* 点外面取消 */
        private boolean mCancelOnTouchOutside;

        static final int DEFAULT_DURATION = 300;

        private Builder(Context context) {
            mContext = context;
            mWidth = MATCH_PARENT;
            mHeight = WRAP_CONTENT;
            mDuration = DEFAULT_DURATION;
            mCancelOnTouchOutside = true;
            mGravity = Gravity.BOTTOM;
            mEnterAnim = SimpleAnimationUtils.getTranslateVerticalAnimation(1f, 0f, mDuration);
            mExitAnim = SimpleAnimationUtils.getTranslateVerticalAnimation(0f, 1f, mDuration);
        }

        public static Builder newBuilder(Context context) {
            return new Builder(context);
        }

        public Builder layoutResID(@LayoutRes int layoutResID) {
            mLayoutResID = layoutResID;
            return this;
        }

        public Builder widthAndHeight(int width, int height) {
            mWidth = width;
            mHeight = height;
            return this;
        }

        public Builder animation(@AnimRes int enterAnimResID, @AnimRes int exitAnimResID) {
            mEnterAnim = AnimationUtils.loadAnimation(mContext, enterAnimResID);
            mExitAnim = AnimationUtils.loadAnimation(mContext, exitAnimResID);
            return this;
        }

        public Builder animation(Animation enterAnim, Animation exitAnim) {
            mEnterAnim = enterAnim;
            mExitAnim = exitAnim;
            return this;
        }

        public Builder duration(int duration) {
            mDuration = duration;
            return this;
        }

        public Builder gravity(int gravity) {
            mGravity = gravity;
            return this;
        }

        public Builder cancelOnTouchOutside(boolean canceledOnTouchOutside) {
            mCancelOnTouchOutside = canceledOnTouchOutside;
            return this;
        }

        public SimpleDialog build() {
            mEnterAnim.setDuration(mDuration);
            mExitAnim.setDuration(mDuration);

            return new SimpleDialog(mContext, this) {
                @Override
                protected Animation onCreateShowAnimation() {
                    return mEnterAnim;
                }

                @Override
                protected Animation onCreateDismissAnimation() {
                    return mExitAnim;
                }
            };
        }

    }
}

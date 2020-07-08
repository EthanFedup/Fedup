package com.evertech.core.widget;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

import com.blankj.utilcode.util.LogUtils;
import com.evertech.core.R;
import com.evertech.core.R2;

import butterknife.BindView;

/**
 *    author : Ethan
 *    e-mail : Ethan@fedup.cn
 *    date   : 7/6/2020 6:31 PM
 *    desc   :
 */
public class RealPageLoadingView extends CustomView implements View.OnClickListener {
    @BindView(R2.id.iv_anim)
    public ImageView ivAnim;

    private AnimationDrawable animationDrawable;


    public RealPageLoadingView(Context context) {
        super(context);
    }

    public RealPageLoadingView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setOnClickListener(this);
    }

    @Override
    protected int contentResID() {
        return R.layout.widget_loading_view;
    }

    @Override
    protected void init(@Nullable AttributeSet attrs) {
        super.init(attrs);
        // 把动画资源设置为imageView的背景,也可直接在XML里面设置
        ivAnim.setBackgroundResource(R.drawable.loading_frame_ani);
        animationDrawable = (AnimationDrawable) ivAnim.getBackground();
    }


    public void startAnim() {
        if (animationDrawable != null && !animationDrawable.isRunning()) {
            animationDrawable.start();
        }
    }

    public void stopAnim() {
        LogUtils.d("stopAnim----000---");
        if (animationDrawable != null && animationDrawable.isRunning()) {
            LogUtils.d("stopAnim----111---");

            animationDrawable.stop();
            LogUtils.d("stopAnim----222---");

        }
    }


    @Override
    public void onClick(View v) {

    }

}

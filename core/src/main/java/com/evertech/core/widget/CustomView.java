package com.evertech.core.widget;

import android.content.Context;
import androidx.annotation.CallSuper;
import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import butterknife.ButterKnife;

public abstract class CustomView extends FrameLayout {
    private ViewGroup viewContent;

    protected boolean isDriver = true;

    public CustomView(Context context) {
        super(context);
        if (isInEditMode()) return;
        init(null);
    }

    public CustomView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        if (isInEditMode()) return;
        init(attrs);
    }

    @CallSuper
    protected void init(@Nullable AttributeSet attrs) {
        viewContent = (ViewGroup) View.inflate(getContext(), contentResID(), this);
        ButterKnife.bind(this, viewContent);
    }

    @LayoutRes
    protected abstract int contentResID();

    protected ViewGroup getContent() {
        return viewContent;
    }

}

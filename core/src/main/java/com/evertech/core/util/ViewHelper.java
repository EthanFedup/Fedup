package com.evertech.core.util;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.StringUtils;
import com.evertech.core.definition.JConsumer;

import androidx.annotation.ColorRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.FontRes;
import androidx.annotation.IdRes;
import androidx.annotation.StringRes;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static com.evertech.core.util.ResourceUtils.getString;


public class ViewHelper {

    public static final int MATCH = ViewGroup.LayoutParams.MATCH_PARENT;

    public static final int WRAP = ViewGroup.LayoutParams.WRAP_CONTENT;

    public static final int CLICK_INTERVAL = 1000;

    public static ViewGroup.LayoutParams newCommonParams(int width, int height) {
        return new ViewGroup.LayoutParams(width, height);
    }

    public static FrameLayout.LayoutParams newFrameParams(int width, int height) {
        return new FrameLayout.LayoutParams(width, height);
    }

    public static LinearLayout.LayoutParams newLinearParams(int width, int height) {
        return new LinearLayout.LayoutParams(width, height);
    }

    public static RelativeLayout.LayoutParams newRelativeParams(int width, int height) {
        return new RelativeLayout.LayoutParams(width, height);
    }

    public static String getTvText(TextView tv) {
        if (tv == null) return "";
        return StringHelper.nullToEmpty(tv.getText().toString().trim());
    }

    public static String getTvText(TextView tv, String defaultValue) {
        if (tv == null) return defaultValue;
        return StringHelper.nullToDefault(tv.getText().toString().trim(), defaultValue);
    }

    public static String getTextWithNull(TextView tv) {
        if (tv == null) return null;
        String text = getTvText(tv);
        return StringUtils.isTrimEmpty(text) ? null : text;
    }

    public static boolean isEmptyText(TextView tv) {
        return ObjectUtils.isEmpty(getTvText(tv));
    }

    public static boolean isTrimEmptyText(TextView tv) {
        return StringUtils.isTrimEmpty(getTvText(tv));
    }

    /**
     * 设置字体颜色
     */
    public static void setTextColor(TextView tv, @ColorRes int colorResId) {
        if (tv == null) return;
        tv.setTextColor(ResourceUtils.getColor(colorResId));
    }

    /**
     * 设置字体颜色
     */
    public static void setTextColorInt(TextView tv, int color) {
        if (tv == null) return;
        tv.setTextColor(color);
    }

    /**
     * 设置字体颜色
     */
    public static void setTextColor(TextView tv, String colorString) {
        if (tv == null) return;
        int color = 0;
        try {
            color = Color.parseColor(colorString);
        } catch (Exception e) {
            return;
        }
        tv.setTextColor(color);
    }

    public static void setViewBackground(View tv, @DrawableRes int resID) {
        if (tv == null) return;
        tv.setBackgroundResource(resID);
    }

    /**
     * 设置文字内容，如果text为空，默认赋值空字符串.
     */
    public static void setText(TextView tv, String text) {
        if (tv == null) return;
        text = StringHelper.nullToEmpty(text);
        tv.setText(text);
    }

    /**
     * 设置文字内容，如果text为空，默认赋值空字符串.
     */
    public static void setText(TextView tv, String text, String defaultText) {
        if (tv == null) return;
        defaultText = defaultText == null ? "" : defaultText;
        text = StringHelper.nullToDefault(text, defaultText);
        tv.setText(text);
    }

    /**
     * 设置文字内容，如果text为空，则不赋值.
     */
    public static void setNoneNullText(TextView tv, String text) {
        if (tv == null || text == null) return;
        tv.setText(text);
    }

    /**
     * TextView设置int类型内容.
     */
    public static void setIntText(TextView tv, int intText) {
        if (tv == null) return;
        tv.setText(String.valueOf(intText));
    }

    public static void textFormat(TextView tv, String format, Object... args) {
        if (tv == null || format == null || ObjectUtils.isEmpty(args)) return;
        tv.setText(String.format(format, args));
    }

    public static void textFormat(TextView tv, @StringRes int formatRes, Object... args) {
        if (tv == null || ObjectUtils.isEmpty(args)) return;
        tv.setText(String.format(getString(formatRes), args));
    }

    public static boolean isTrimEmpty(TextView tv) {
        if (tv == null) return false;
        return StringUtils.isTrimEmpty(tv.getText().toString());
    }

    public static void setFont(TextView tv, @FontRes int fontRes) {
        if (tv == null) return;
        Typeface typeface = ResourcesCompat.getFont(tv.getContext(), fontRes);
        tv.setTypeface(typeface);
    }

    /**
     * 设置右图标.
     */
    public static void setDrawableRight(TextView tv, @DrawableRes int resID) {
        if (tv == null) return;
        tv.setCompoundDrawablesWithIntrinsicBounds(0, 0, resID, 0);
    }

    /**
     * 设置左图标.
     */
    public static void setDrawableLeft(TextView tv, @DrawableRes int resID) {
        if (tv == null) {
            return;
        }
        tv.setCompoundDrawablesWithIntrinsicBounds(resID, 0, 0, 0);
    }

    /**
     * 光标移动到最后一个字符.
     */
    public static void moveToLastCursor(EditText et) {
        if (et == null) return;
        String text = ViewHelper.getTvText(et);
        if (!ObjectUtils.isEmpty(text)) {
            et.setSelection(text.length());
        }
    }

    /**
     * 文字变化的快速监听.
     */
    public static void afterTextChanged(TextView tv, final JConsumer<String> consumer) {
        tv.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                consumer.accept(s.toString());
            }
        });
    }

    /**
     * RecyclerView的内容是否铺满当前屏幕.
     */
    public static boolean isItemsFillScreen(RecyclerView rv) {
        if (rv == null) return false;

        RecyclerView.LayoutManager layoutManager = rv.getLayoutManager();
        if (layoutManager instanceof LinearLayoutManager) {
            int firstCompletelyVisibleItemPosition = ((LinearLayoutManager) layoutManager).findFirstCompletelyVisibleItemPosition();
            /* 第一可见的item的index大于0，说明铺满当前屏幕 */
            return firstCompletelyVisibleItemPosition > 0;
        }

        return false;
    }

    /**
     * @param view
     * @param left   -1表示不设置，保留原值.
     * @param top    -1表示不设置，保留原值.
     * @param right  -1表示不设置，保留原值.
     * @param bottom -1表示不设置，保留原值.
     */
    public static void setMargin(View view, int left, int top, int right, int bottom) {
        if (view == null) return;
        ViewGroup.LayoutParams params = view.getLayoutParams();
        if (!(params instanceof ViewGroup.MarginLayoutParams)) return;

        ViewGroup.MarginLayoutParams mlp = (ViewGroup.MarginLayoutParams) params;
        int originLeftMargin = mlp.leftMargin;
        int originTopMargin = mlp.topMargin;
        int originRightMargin = mlp.rightMargin;
        int originBottomMargin = mlp.bottomMargin;

        mlp.setMargins(left == -1 ? originLeftMargin : left,
                top == -1 ? originTopMargin : top,
                right == -1 ? originRightMargin : right,
                bottom == -1 ? originBottomMargin : bottom);

        view.setLayoutParams(mlp);
    }

    public static void alignParentRight(View view) {
        setAlignRule(view, RelativeLayout.ALIGN_PARENT_RIGHT);
    }

    public static void alignParentLeft(View view) {
        setAlignRule(view, RelativeLayout.ALIGN_PARENT_LEFT);
    }

    public static void centerInParent(View view) {
        setAlignRule(view, RelativeLayout.CENTER_IN_PARENT);
    }

    public static void toLeftOf(View view, @IdRes int idRes) {
        toDirectionOf(view, idRes, RelativeLayout.LEFT_OF);
    }

    public static void toRightOf(View view, @IdRes int idRes) {
        toDirectionOf(view, idRes, RelativeLayout.RIGHT_OF);
    }

    public static void toLeftAndRightOf(View view, @IdRes int toLeftIdRes, @IdRes int toRightIdRes) {
        RelativeLayout.LayoutParams params = getRelativeLayoutParams(view);
        if (params == null) return;
        removeDirectionRules(view);
        params.addRule(RelativeLayout.LEFT_OF, toLeftIdRes);
        params.addRule(RelativeLayout.RIGHT_OF, toRightIdRes);
        view.setLayoutParams(params);
    }

    private static void setAlignRule(View view, int alignRule) {
        RelativeLayout.LayoutParams params = getRelativeLayoutParams(view);
        if (params == null) return;
        removeAlignRules(view);
        params.addRule(alignRule);
        view.setLayoutParams(params);
    }

    public static void removeAlignRules(View view) {
        RelativeLayout.LayoutParams params = getRelativeLayoutParams(view);
        if (params == null) return;
        params.removeRule(RelativeLayout.ALIGN_PARENT_LEFT);
        params.removeRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        params.removeRule(RelativeLayout.CENTER_IN_PARENT);
        params.removeRule(RelativeLayout.CENTER_HORIZONTAL);
        params.removeRule(RelativeLayout.CENTER_VERTICAL);
    }

    public static void removeDirectionRules(View view) {
        RelativeLayout.LayoutParams params = getRelativeLayoutParams(view);
        if (params == null) return;
        params.removeRule(RelativeLayout.LEFT_OF);
        params.removeRule(RelativeLayout.RIGHT_OF);
    }

    private static void toDirectionOf(View view, @IdRes int idRes, int direction) {
        RelativeLayout.LayoutParams params = getRelativeLayoutParams(view);
        if (params == null) return;
        params.removeRule(RelativeLayout.LEFT_OF);
        params.removeRule(RelativeLayout.RIGHT_OF);
        params.addRule(direction, idRes);
        view.setLayoutParams(params);
    }

    public static RelativeLayout.LayoutParams getRelativeLayoutParams(View view) {
        if (view == null) return null;
        try {
            return (RelativeLayout.LayoutParams) view.getLayoutParams();
        } catch (Exception e) {
            return null;
        }
    }


    public static void setEditTextLength(EditText editText, int length) {
        editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(length)});
    }

    /**
     * 设置TextView下划线
     */
    public static void setUnderLine(TextView textView){
        textView.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
        textView.getPaint().setAntiAlias(true);//抗锯齿
    }

}

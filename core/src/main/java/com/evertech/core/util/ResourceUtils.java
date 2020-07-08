package com.evertech.core.util;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.DimenRes;
import androidx.annotation.IntegerRes;
import androidx.annotation.StringRes;
import androidx.core.content.ContextCompat;

import com.evertech.core.BaseApp;


public class ResourceUtils {
    private static Context getContext() {
        return BaseApp.Companion.getApp();
    }

    public static Resources getResources() {
        return getContext().getResources();
    }

    public static String getString(@StringRes int resID) {
        return getResources().getString(resID);
    }

    public static @ColorInt
    int getColor(@ColorRes int resID) {
        return ContextCompat.getColor(getContext(), resID);
    }

    public static int getDimension(@DimenRes int resID) {
        return (int) getResources().getDimension(resID);
    }

    public static int getInteger(@IntegerRes int resID) {
        return getResources().getInteger(resID);
    }

    public static int getDrawableIDByName(String resName) {
        return getResources().getIdentifier(resName, "mipmap", getContext().getPackageName());
    }

    public static Drawable getDrawableByName(String resName) {
        return getResources().getDrawable(getDrawableIDByName(resName));
    }

    public static String getResourceName(int resID) {
        try {
            return getResources().getResourceEntryName(resID);
        } catch (Resources.NotFoundException e) {
            return null;
        }
    }

    public static String[] getResourceStringArray(int resID) {
        try {
            return getResources().getStringArray(resID);
        } catch (Resources.NotFoundException e) {
            return null;
        }
    }

    public static Bitmap getBitmap(int resID) {
        return BitmapFactory.decodeResource(getResources(), resID);
    }

}

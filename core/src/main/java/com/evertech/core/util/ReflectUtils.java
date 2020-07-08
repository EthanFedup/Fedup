package com.evertech.core.util;

import com.blankj.utilcode.util.StringUtils;
import com.chad.library.adapter.base.loadmore.LoadMoreView;

import java.lang.reflect.Field;

public class ReflectUtils {
    /**
     * @param target
     * @param filedName
     * @param value
     * @param <T>
     */
    public static <T> void setFiledValue(T target, String filedName, Object value) {
        try {
            Field declaredField = getField(target.getClass(), filedName);
            if (declaredField == null) return;

            boolean accessible = declaredField.isAccessible();
            declaredField.setAccessible(true);

            declaredField.set(target, value);

            declaredField.setAccessible(accessible);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static <T> Field getField(Class<?> clazz, String filedName) {
        if (clazz == null || StringUtils.isTrimEmpty(filedName)) return null;

        Field declaredField = null;
        try {
            declaredField = clazz.getDeclaredField(filedName);
        } catch (Exception e) {
            if (clazz.getSuperclass() != null) {
                return getField(clazz.getSuperclass(), filedName);
            }
        }
        return declaredField;
    }


    /**
     * 拿到{@link LoadMoreView}的私有字段"mLoadMoreStatus".
     */
    public static int getLoadMoreStatus(Object target) {
        Field field = null;
        try {
            field = ReflectUtils.getField(target.getClass(), "mLoadMoreStatus");
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (field == null) {
            return -1;
        }

        field.setAccessible(true);
        try {
            return field.getInt(target);
        } catch (IllegalAccessException e) {
            return -1;
        } finally {
            field.setAccessible(false);
        }
    }

}

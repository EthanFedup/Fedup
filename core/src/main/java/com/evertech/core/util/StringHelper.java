package com.evertech.core.util;

import android.text.TextUtils;
import android.util.Log;

import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.StringUtils;


import java.io.UnsupportedEncodingException;

import me.jessyan.autosize.utils.LogUtils;

public class StringHelper {
    private static final String DEFAULT_CHARSET = "UTF-8";

    public static String nullToEmpty(String target) {
        return StringUtils.isTrimEmpty(target) ? "" : target;
    }

    public static String nullToDefault(String target, String defaultStr) {
        return StringUtils.isTrimEmpty(target) ? defaultStr : target;
    }

    /**
     * 截取一个字符串最后几位.
     *
     * @param str
     * @param retainCount 最后几位
     * @return
     */
    public static String lastCharacters(String str, int retainCount) {
        if (ObjectUtils.isEmpty(str)) {
            return "";
        }
        return str.length() > retainCount ? str.substring(str.length() - retainCount) : str;
    }





    /**
     * 替换到字符串中的空字符串.
     * 别问我为什么会有这种情况.
     */
    public static String replaceEmptyStr(String str) {
        try {
            str = str.replace(new String(new byte[]{-30, -128, -88}, "UTF-8"), "");
        } catch (UnsupportedEncodingException e) {
        }
        return str;
    }


    public static String unicodeToUtf8(String theString) {
        char aChar;
        int len = theString.length();
        StringBuffer outBuffer = new StringBuffer(len);
        for (int x = 0; x < len;) {
            aChar = theString.charAt(x++);
            if (aChar == '\\') {
                aChar = theString.charAt(x++);
                if (aChar == 'u') {
                    // Read the xxxx
                    int value = 0;
                    for (int i = 0; i < 4; i++) {
                        aChar = theString.charAt(x++);
                        switch (aChar) {
                            case '0':
                            case '1':
                            case '2':
                            case '3':
                            case '4':
                            case '5':
                            case '6':
                            case '7':
                            case '8':
                            case '9':
                                value = (value << 4) + aChar - '0';
                                break;
                            case 'a':
                            case 'b':
                            case 'c':
                            case 'd':
                            case 'e':
                            case 'f':
                                value = (value << 4) + 10 + aChar - 'a';
                                break;
                            case 'A':
                            case 'B':
                            case 'C':
                            case 'D':
                            case 'E':
                            case 'F':
                                value = (value << 4) + 10 + aChar - 'A';
                                break;
                            default:
                                throw new IllegalArgumentException(
                                        "Malformed   \\uxxxx   encoding.");
                        }
                    }
                    outBuffer.append((char) value);
                } else {
                    if (aChar == 't')
                        aChar = '\t';
                    else if (aChar == 'r')
                        aChar = '\r';
                    else if (aChar == 'n')
                        aChar = '\n';
                    else if (aChar == 'f')
                        aChar = '\f';
                    outBuffer.append(aChar);
                }
            } else
                outBuffer.append(aChar);
        }
        return outBuffer.toString();
    }

}

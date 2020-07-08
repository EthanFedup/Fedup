package com.evertech.core.util

import android.text.TextUtils
import android.widget.TextView


import com.blankj.utilcode.util.LogUtils

import java.lang.reflect.Array
import java.util.regex.Matcher
import java.util.regex.Pattern

/**
 * 创建日期：2018/9/17 on 16:09
 * 描述:
 * 作者: ethan
 */
object StringUtil {

    private val keyRmb = "￥"

    fun getEmptyString(string: String): String {
        return if (TextUtils.isEmpty(string)) "" else string
    }

    fun getEmptyStringNumber(string: String): String {
        val mZero = "0"
        return if (TextUtils.isEmpty(string)) "" else if (mZero == string) "" else string
    }

    fun getTagEmptyString(string: String): String {
        return if (TextUtils.isEmpty(string)) "全部" else string
    }


    fun replaceString(string: String): String {
        return if (TextUtils.isEmpty(string)) "" else string.substring(
            0,
            3
        ) + "****" + string.substring(7, string.length)
    }

    fun clearString(string: StringBuilder) {
        if (!TextUtils.isEmpty(string) && string.length > 0) {
            string.delete(0, string.length)
        }
    }

    fun clearMap(map: MutableMap<*, *>) {
        if (!map.isEmpty()) {
            map.clear()
        }
    }

    fun clearList(list: MutableList<*>) {
        if (!list.isEmpty()) {
            list.clear()
        }
    }

    fun isEmpty(obj: Any?): Boolean {
        if (obj == null) {
            return true
        } else if (obj is CharSequence) {
            return obj.length == 0
        } else if (obj is Collection<*>) {
            return obj.isEmpty()
        } else if (obj is Map<*, *>) {
            return obj.isEmpty()
        } else if (obj.javaClass.isArray) {
            return Array.getLength(obj) == 0
        }

        return false
    }


    fun getUgcDetailsTitle(oldTitle: String): String {
        var newTitle = oldTitle

        val sbTitle = StringBuilder()

        if (!TextUtils.isEmpty(oldTitle) && oldTitle.length > 10) {
            if (oldTitle.length < 14) {
                newTitle = sbTitle.append(oldTitle.substring(0, 9)).append("...").toString()
            } else {
                newTitle = sbTitle.append(
                    oldTitle.substring(
                        0,
                        if (oldTitle.length >= 20) 20 else oldTitle.length
                    )
                ).append("...").toString()
            }
        } else {
            newTitle = StringUtil.getEmptyString(oldTitle)
        }

        return newTitle
    }


    fun tagsToString(stringList: List<String>?): String {
        if (null == stringList || stringList.isEmpty()) {
            return ""
        }

        val stringBuilder = StringBuilder()

        for (i in stringList.indices) {
            stringBuilder.append(stringList[i])
            if (i != stringList.size) {
                stringBuilder.append(",")
            }
        }
        stringBuilder.delete(stringBuilder.length - 1, stringBuilder.length)

        return stringBuilder.toString()
    }


    fun appendSelectedTag(objectTag: String, specialTag: String): String {

        val keyAll = ""

        return if (keyAll == objectTag) {
            if (specialTag != keyAll) {
                specialTag
            } else {
                ""
            }
        } else {
            if (specialTag != keyAll) {
                "$objectTag,$specialTag"
            } else {
                objectTag
            }
        }
    }

    fun appendRmb(objectText: String, specialTag: String): String {

        LogUtils.d("appendRmb--000----$objectText")
        LogUtils.d("appendRmb--111----$specialTag")

        val stringBuilder = StringBuilder(objectText)

        if (!TextUtils.isEmpty(objectText)) {
            stringBuilder.insert(0, keyRmb)
            if (objectText.contains(specialTag)) {
                stringBuilder.insert(objectText.lastIndexOf(specialTag) + 2, keyRmb)
                val endPrice = stringBuilder.subSequence(
                    stringBuilder.lastIndexOf(keyRmb) + 2,
                    stringBuilder.length
                ).toString()
                if (endPrice.length > 3) {
                    stringBuilder.replace(
                        stringBuilder.lastIndexOf(keyRmb) + 1,
                        stringBuilder.length,
                        "1000+"
                    )
                }
            }
        }

        return stringBuilder.toString()
    }

    fun getCountry(textView: TextView): String {
        var textCode = textView.text.toString()
        if (!TextUtils.isEmpty(textCode) && textCode.contains("+")) {
            textCode = textCode.replace("+", "")
        } else {
            return ""
        }
        return textCode
    }


    /*   public static String ScriptEngine(String oldString){

       String string="{\"target_type\":\"single\",\"msg_type\":\"text\",\"target_name\":\"测试家长\",\"" +
                "target_id\":\"18539282209\",\"from_id\":\"13820165774\",\"from_name\":\"大大\",\"from_type\"" +
                ":\"user\",\"from_platform\":\"web\",\"msg_body\":{\"text\":\"\\u003cdiv style\\u003d" +
                "\\\"display: flex;align-items: center;word-wrap:break-word;\\\"\\u003e飞飞哥\\u003c/div\\u003e\"}" +
                ",\"create_time\":1575364059442,\"version\":1,\"msgid\":5635856903,\"msg_level\":0,\"msg_ctime\":1575364058429}";
        ScriptEngineManager sem = new ScriptEngineManager();
        ScriptEngine engine = sem.getEngineByExtension("js");
        String unUrl;
        try {
            unUrl = (String)engine.eval("unescape('"+string+"')");
            System.out.println(unUrl);
        } catch (ScriptException e) {
            e.printStackTrace();
        }
        return "";
    }
*/

    /**
     * 含有unicode 的字符串转一般字符串
     * @param unicodeStr 混有 Unicode 的字符串
     * @return
     */
    fun unicodeStr2String(unicodeStr: String): String {

        LogUtils.d("unicodeStr2String--0000000---"+unicodeStr)

        val length = unicodeStr.length

        LogUtils.d("unicodeStr2String--111---"+length)

        var count = 0
        //正则匹配条件，可匹配“\\u”1到4位，一般是4位可直接使用 String regex = "\\\\u[a-f0-9A-F]{4}";
        val regex = "\\\\u[a-f0-9A-F]{1,4}"
        val pattern = Pattern.compile(regex)
        val matcher = pattern.matcher(unicodeStr)

        LogUtils.d("unicodeStr2String--222---"+length)

        val stringBuffer = StringBuilder()

        while (matcher.find()) {


            val oldChar = matcher.group()//原本的Unicode字符
            LogUtils.d("unicodeStr2String--333---"+oldChar)
            val newChar = unicode2String(oldChar)//转换为普通字符
            // int index = unicodeStr.indexOf(oldChar);
            // 在遇见重复出现的unicode代码的时候会造成从源字符串获取非unicode编码字符的时候截取索引越界等
            val index = matcher.start()
            LogUtils.d("unicodeStr2String--444---"+index)

            stringBuffer.append(unicodeStr.substring(count, index))//添加前面不是unicode的字符
            stringBuffer.append(newChar)//添加转换后的字符
            LogUtils.d("unicodeStr2String--555---"+newChar)

            count = index + oldChar.length//统计下标移动的位置
        }
        stringBuffer.append(unicodeStr.substring(count, length))//添加末尾不是Unicode的字符
        LogUtils.d("unicodeStr2String--666---"+stringBuffer.toString())

        return stringBuffer.toString()
    }

    /**
     * unicode 转字符串
     * @param unicode 全为 Unicode 的字符串
     * @return
     */
    fun unicode2String(unicode: String): String {
        val string = StringBuffer()
        val hex = unicode.split("\\\\u".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

        for (i in 1 until hex.size) {
            // 转换出每一个代码点
            val data = Integer.parseInt(hex[i], 16)
            // 追加成string
            string.append(data.toChar())
        }

        return string.toString()
    }

   private const val textEqu = "\u003d"
    fun unToString( oldText:String ):String {
        LogUtils.d("initViews--222--$oldText")
        LogUtils.d("initViews--333--$textEqu")

        var newText=""

        if(oldText.contains(textEqu)){
            LogUtils.d("initViews--444--$oldText")
            LogUtils.d("initViews--555--$textEqu")
            newText = oldText.replace(textEqu,"=")

            LogUtils.d("initViews--666--$newText")

        }else{
            newText = oldText
        }

        return newText
    }




}

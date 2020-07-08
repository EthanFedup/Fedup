package com.evertech.Fedup.login.adapter;

import android.widget.TextView;

import androidx.annotation.Nullable;

import com.blankj.utilcode.util.LogUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.evertech.Fedup.R;
import com.evertech.Fedup.login.model.Country;
import com.evertech.core.util.StringUtil;

import java.util.List;
import java.util.Locale;

/**
 *    author : Ethan
 *    e-mail : Ethan@fedup.cn
 *    date   : 7/7/2020 6:02 PM
 *    desc   :
 */
public class CountryInnerAdapter extends BaseQuickAdapter<Country, BaseViewHolder> {



    public CountryInnerAdapter(int layoutResId, @Nullable List<Country> data) {
        super(layoutResId, data);
    }


    /**
     * 根据分类的首字母的Char ascii值获取其第一次出现该首字母的位置
     */
        /*  public int getPositionForSection(int section) {
  for (int i = 0; i < mData.size(); i++) {
            String sortStr = mData.get(i).getTag();
            char firstChar = sortStr.toUpperCase(Locale.CHINESE).charAt(0);

            LogUtils.d("firstChar--  "+firstChar);
            LogUtils.d("int section--  "+ section);

            if (firstChar == section) {
                LogUtils.d("firstChar == section--  "+ i);

                return i+1;
            }
        }

        return -1;
    }*/


    @Override
    protected void convert(BaseViewHolder helper, Country item) {
        int position = helper.getAdapterPosition();
//        int position = helper.getLayoutPosition();
        LogUtils.d("getLayoutPosition--  "+position);



        helper.setText(R.id.tv_key, StringUtil.INSTANCE.getEmptyString( item.getName()));
        helper.setText(R.id.tv_value, "+"+ item.getNumber());



//        String word = mData.get(position-1).getPinyin().substring(0, 1).toLowerCase();
//            String word = item.getPinyin().substring(0, 1).toLowerCase();

//        tvWord.setText(word);

        //将相同字母开头的合并在一起
     /*   if (position == 1) {
            //第一个是一定显示的
            helper.setVisible(R.id.tv_word, true);
        } else {
            //后一个与前一个对比,判断首字母是否相同，相同则隐藏
            String headerWord = mData.get(position - 2).getPinyin().substring(0, 1).toLowerCase();
//                String headerWord = item.getPinyin().substring(0, 1).toLowerCase();
            if (word.equals(headerWord)) {
                helper.setVisible(R.id.tv_word, false);
            } else {
                helper.setVisible(R.id.tv_word, true);
            }
        }
*/

    }
}

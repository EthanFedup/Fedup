package com.evertech.Fedup.login.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

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
public class CountryCodeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Country> data;
    private OnItemClickListener onItemClickListener;
    private Context context;

    private CountryInnerAdapter innerAdapter;

    public CountryCodeAdapter(List<Country> data, Context context) {
        this.data = data;
        this.context = context;

    }


    public void setAllData(List<Country> data) {
        this.data = data;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_contry_code_layout, parent, false);
        return new MyViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {


        final Country item = data.get(position);

        ((MyViewHolder) holder).tvWord.setText(StringUtil.INSTANCE.getEmptyString(item.getName()));

        ((MyViewHolder) holder).tvKey.setText(StringUtil.INSTANCE.getEmptyString(item.getName()));
        ((MyViewHolder) holder).tvValue.setText("+" + item.getNumber());

        String word = data.get(position).getMWord().toLowerCase();
        ((MyViewHolder) holder).tvWord.setText(word);
        //将相同字母开头的合并在一起
        if (position == 0) {
            //第一个是一定显示的
            ((MyViewHolder) holder).tvWord.setVisibility(View.VISIBLE);
        } else {
            //后一个与前一个对比,判断首字母是否相同，相同则隐藏
            String headerWord = data.get(position - 1).getMWord().toLowerCase();
            if (word.equals(headerWord)) {
                ((MyViewHolder) holder).tvWord.setVisibility(View.GONE);
            } else {
                ((MyViewHolder) holder).tvWord.setVisibility(View.VISIBLE);
            }
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(item);
                }
            }
        });
    }


    /**
     * 根据分类的首字母的Char ascii值获取其第一次出现该首字母的位置
     */

    public int getPositionForSection(int section) {
        for (int i = 0; i < data.size(); i++) {
            String sortStr = data.get(i).getMWord();
            char firstChar = sortStr.toUpperCase(Locale.CHINESE).charAt(0);
            if (firstChar == section) {
                return i ;
            }
        }
        return -1;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    protected class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tvWord;
        private TextView tvKey;
        private TextView tvValue;


        MyViewHolder(View itemView) {
            super(itemView);
            this.tvWord = itemView.findViewById(R.id.tv_word);
            this.tvKey = itemView.findViewById(R.id.tv_key);
            this.tvValue = itemView.findViewById(R.id.tv_value);
        }
    }


    public interface OnItemClickListener {
        void onItemClick(Country country);
    }



    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }


}

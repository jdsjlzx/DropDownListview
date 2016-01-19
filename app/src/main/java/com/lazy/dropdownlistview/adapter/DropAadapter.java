package com.lazy.dropdownlistview.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.lazy.dropdownlistview.R;

/**
 * 作者：liujingyuan on 2016/1/19 16:42
 * 邮箱：906514731@qq.com
 */
public class DropAadapter extends BaseAdapter {
    private final int TYPECOUNT = 2;
    private final int ONE = 0;
    private final int TWO = 1;
    private Context mContext;


    public DropAadapter(Context mContext) {
        this.mContext = mContext;
    }


    @Override public int getCount() {
        return 10;
    }


    @Override public Object getItem(int position) {
        return null;
    }


    @Override public long getItemId(int position) {
        return 0;
    }


    @Override public int getViewTypeCount() {
        return TYPECOUNT;
    }


    @Override public int getItemViewType(int position) {
        if (position == ONE) {
            return ONE;
        }
        else {
            return TWO;
        }
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int itemViewType = getItemViewType(position);
        View inflate;
        if (itemViewType == ONE) {
            inflate = View.inflate(mContext, R.layout.list_view_item_title,
                    null);
        }
        else {
            inflate = View.inflate(mContext,
                    android.R.layout.simple_expandable_list_item_1, null);
            TextView mTextView = (TextView) inflate.findViewById(android.R.id
                    .text1);
            mTextView.setText(position+"");
        }
        return inflate;
    }
}

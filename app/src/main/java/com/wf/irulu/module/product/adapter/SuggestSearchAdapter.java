package com.wf.irulu.module.product.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.wf.irulu.R;

import java.util.ArrayList;

/**
 * @描述: TODO
 * @项目名: irulu1.2
 * @包名:com.wf.irulu.module.product.adapter
 * @类名:SuggestWordAdapter
 * @作者: Yuki
 * @创建时间:2015/11/17
 */
public class SuggestSearchAdapter extends BaseAdapter{

    private Context mContext;
    private LayoutInflater mInflater;
    private ArrayList<String> mSearchSuggest;

    public SuggestSearchAdapter(ArrayList<String> searchSuggest, Context context) {
        mSearchSuggest = searchSuggest;
        mInflater = LayoutInflater.from(context);
        mContext = context;
    }

    public void refresh(ArrayList<String> searchSuggest){
        this.mSearchSuggest =  searchSuggest;
//        if (searchSuggest != null && searchSuggest.size() > 0) {
            notifyDataSetChanged();
//        }else{
//            notifyDataSetInvalidated();
//        }
    }

    @Override
    public int getCount() {
        return mSearchSuggest == null ? 0 : mSearchSuggest.size();
    }

    @Override
    public Object getItem(int position) {
        return mSearchSuggest.get(position);
    }

    @Override
    public long getItemId(int position) {
        return mSearchSuggest == null ? 0 : mSearchSuggest.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder mHolder = null;
        if (convertView == null){
            convertView = mInflater.inflate(R.layout.item_search_suggest,null);
            mHolder = new ViewHolder();
            mHolder.mSuggestTvContent = (TextView) convertView.findViewById(R.id.suggest_tv_content);
            convertView.setTag(mHolder);
        }else{
            mHolder = (ViewHolder) convertView.getTag();
        }
        mHolder.mSuggestTvContent.setText(mSearchSuggest.get(position));
        return convertView;
    }

    private class ViewHolder {
        TextView mSuggestTvContent;
    }
}

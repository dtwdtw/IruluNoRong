package com.wf.irulu.module.product.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.wf.irulu.R;
import com.wf.irulu.logic.IruluController;
import com.wf.irulu.logic.db.RecentSearchDbHelper;

import java.util.ArrayList;

/**
 * @描述: TODO
 * @项目名: irulu1.2
 * @包名:com.wf.irulu.module.product.adapter
 * @类名:RecentSearchAdapter
 * @作者: Yuki
 * @创建时间:2015/11/16
 */
public class RecentSearchAdapter extends BaseAdapter {

    private ArrayList<String> mRecentSearch;
    private LayoutInflater mInflater;
    private Context mContext;
    private RecentSearchDbHelper mRecentSearchDbHelper;

    public RecentSearchAdapter(ArrayList<String> recentSearch, Context context) {
        mRecentSearch = recentSearch;
        mInflater = LayoutInflater.from(context);
        mContext = context;
        mRecentSearchDbHelper = new RecentSearchDbHelper(IruluController.getInstance());
    }

    public void refresh(ArrayList<String> recentSearch){
        this.mRecentSearch = recentSearch;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if (mRecentSearch == null){
            return 0;
        }else if(mRecentSearch.size() > 10){
            return 10;
        }
        return mRecentSearch.size();
    }

    @Override
    public Object getItem(int position) {
        return mRecentSearch.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder mHolder = null;
        if (convertView == null){
            convertView = mInflater.inflate(R.layout.item_search_recent,null);
            mHolder = new ViewHolder();
            mHolder.mRecentIvDelete = (ImageView) convertView.findViewById(R.id.recent_iv_delete);
            mHolder.mRecentTvContent = (TextView) convertView.findViewById(R.id.recent_tv_content);
            convertView.setTag(mHolder);
        }else{
            mHolder = (ViewHolder) convertView.getTag();
        }
        String word = mRecentSearch.get(position);
        mHolder.mRecentTvContent.setText(word);
        mHolder.mRecentIvDelete.setOnClickListener(new OnDeleteClickListener(word));
        return convertView;
    }

    private class ViewHolder {
        TextView mRecentTvContent;
        ImageView mRecentIvDelete;
    }

    private class OnDeleteClickListener implements View.OnClickListener{

        private String mKey;

        public OnDeleteClickListener(String key) {
            this.mKey = key;
        }

        @Override
        public void onClick(View v) {
            new RecentSearchDbHelper(IruluController.getInstance()).removeKeyWord(mKey);
            mRecentSearch.remove(mKey);
            notifyDataSetChanged();
        }
    }
}

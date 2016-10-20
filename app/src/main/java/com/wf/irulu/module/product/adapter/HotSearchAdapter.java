package com.wf.irulu.module.product.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wf.irulu.R;
import com.wf.irulu.module.product.listener.SearchWordListener;

import java.util.ArrayList;

/**
 * @描述: TODO
 * @项目名: irulu1.2
 * @包名:com.wf.irulu.module.product.adapter
 * @类名:HotSearchAdapter
 * @作者: Yuki
 * @创建时间:2015/11/14
 */
public class HotSearchAdapter extends RecyclerView.Adapter<HotSearchAdapter.ViewHolder> {

    private LayoutInflater mInflater;
    private Context mContext;
    private ArrayList<String> mSearchHot;
    private SearchWordListener mListener;

    public HotSearchAdapter(Context context, ArrayList<String> searchHot ,SearchWordListener listener) {
        this.mContext = context;
        mInflater = LayoutInflater.from(context);
        mSearchHot = searchHot;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_search_hot,null);
        ViewHolder mHolder = new ViewHolder(view);
        return mHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String keyWord = mSearchHot.get(position);
        holder.mSearchTvHot.setText(keyWord);
        holder.mSearchTvHot.setOnClickListener(new OnHotClickListener(keyWord));
    }

    @Override
    public int getItemCount() {
        return mSearchHot == null ? 0 : mSearchHot.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        TextView mSearchTvHot;

        public ViewHolder(View itemView) {
            super(itemView);
            mSearchTvHot = (TextView) itemView.findViewById(R.id.search_bt_hot);

        }
    }

    private class OnHotClickListener implements View.OnClickListener{

        private String mWord;

        public OnHotClickListener(String word) {
            this.mWord = word;
        }

        @Override
        public void onClick(View v) {
            mListener.searchForSelect(mWord);
        }
    }
}

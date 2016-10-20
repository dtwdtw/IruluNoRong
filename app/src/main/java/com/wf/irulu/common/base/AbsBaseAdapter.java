package com.wf.irulu.common.base;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zzh
 * Create date:2016.3.30
 * BaseAdapter多布局的封装类
 */
public abstract class AbsBaseAdapter<T> extends BaseAdapter {

    public Context context;
    protected List<T> datas;
    private int[] resid;

    public AbsBaseAdapter(Context context, int... resid) {
        this.context = context;
        this.resid = resid;
        this.datas = new ArrayList<>();
    }

    public void setDatas(List<T> datas) {
        this.datas = datas;
        this.notifyDataSetChanged();
    }

    public void addDatas(List<T> datas) {
        this.datas.addAll(datas);
        this.notifyDataSetChanged();
    }

    @Override
    public int getViewTypeCount() {
        return resid.length;
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public Object getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView != null) {
            viewHolder = (ViewHolder) convertView.getTag();
        } else {
            convertView = LayoutInflater.from(context).inflate(resid[getItemViewType(position)], null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }

        //从viewholder中获取控件赋值
        bindDatas(viewHolder, datas,position);

        return convertView;
    }

    /**
     * 提供一个抽象方法，用于子适配器来进行数据绑定
     *
     * @param viewHolder
     * @param datas
     */
    public abstract void bindDatas(ViewHolder viewHolder, List<T> datas,int position);



    class ViewHolder {

        private Map<Integer, View> mapCache = new HashMap<>();
        //item的布局对象
        private View layoutView;

        public ViewHolder(View layoutView) {
            this.layoutView = layoutView;
        }

        //提供一个获得布局中子控件的方法，参数为控件id
        public View getView(int id) {
            if (mapCache.containsKey(id)) {
                return mapCache.get(id);
            } else {
                View view = layoutView.findViewById(id);
                mapCache.put(id, view);
                return view;
            }
        }
    }
}

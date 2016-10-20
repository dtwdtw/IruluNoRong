package com.wf.irulu.common.base;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * @描述: 万能的Adapter
 * @项目名: irulu1.2
 * @包名:com.wf.irulu.common.base
 * @类名:CommonAdapter
 * @作者: 左杰
 * @创建时间:2015/11/20 13:35
 *
 * @deprecated 建议使用新的adapter基类:AbsBaseAdapter
 *
 */
@Deprecated
public abstract class CommonAdapter <T> extends BaseAdapter {

    /**
     * 数据源
     */
    protected List<T> datas = null;

    /**
     * 上下文对象
     */
    protected Context context = null;

    /**
     * item布局文件的资源ID
     */
    protected int itemLayoutResId = 0;

    public CommonAdapter(Context context, List<T> datas, int itemLayoutResId) {
        this.context = context;
        this.datas = datas;
        this.itemLayoutResId = itemLayoutResId;
    }

    @Override
    public int getCount() {
        if(datas != null) return datas.size();
        return 0;
    }

    /**
     * 注意，返回值也要为泛型
     */
    @Override
    public T getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = ViewHolder.getViewHolder(context, itemLayoutResId, position, convertView, parent);

        convert(viewHolder,position, getItem(position));

        return viewHolder.getConvertView();
    }

    /**
     * 开发者实现该方法，进行业务处理
     */
    public abstract void convert(ViewHolder viewHolder, int position,T item);

}

package com.wf.irulu.module.shoppingcart.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wf.irulu.R;
import com.wf.irulu.common.bean.GiftInfo;

import java.util.ArrayList;

/**
 * @描述: 购物车中买一送一的Adapter
 * @项目名: irulu1.2
 * @包名:com.wf.irulu.module.shoppingcart.adapter
 * @类名:AddOnGiftAdapter
 * @作者: 左杰
 * @创建时间:2015/10/30 15:47
 */
public class AddOnGiftAdapter extends BaseAdapter {
    private final String TAG = getClass().getCanonicalName();
    private Context mContext;
    private ArrayList<GiftInfo> mGiftInfo;

    public AddOnGiftAdapter(Context context,ArrayList<GiftInfo> giftInfo){
        this.mContext = context;
        this.mGiftInfo = giftInfo;
    }
    @Override
    public int getCount() {
        if(null != mGiftInfo){
            return mGiftInfo.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        ViewHolder holder = null;
        if(null != convertView){
            holder = (ViewHolder) convertView.getTag();
        }else{
            convertView = View.inflate(mContext, R.layout.item_add_on_gift,null);
            holder = new ViewHolder();
            holder.gift_tag_ll = (LinearLayout) convertView.findViewById(R.id.gift_tag_ll);
            holder.gift_tag = (TextView) convertView.findViewById(R.id.gift_tag);//礼物的标签
            holder.gift_explain = (TextView) convertView.findViewById(R.id.gift_explain);//礼物说明
            holder.gift_quantity = (TextView) convertView.findViewById(R.id.gift_quantity);//礼物数量
            convertView.setTag(holder);
        }

        if(position ==0){
            holder.gift_tag.setVisibility(View.VISIBLE);
        }else if(position >0){
            holder.gift_tag.setVisibility(View.GONE);
        }

        GiftInfo giftInfo = mGiftInfo.get(position);
//        int id = giftInfo.getId();
//        int skuId = giftInfo.getSkuId();
//        int productId = giftInfo.getProductId();
        String name = giftInfo.getName();
        int num = giftInfo.getNum();

        //获取到的是px,120px
        int dimensionPixelSizeLeftMargin = mContext.getResources().getDimensionPixelSize(R.dimen.shopping_cart_gift_leftMargin);
        int dimensionPixelSizeRightMargin = mContext.getResources().getDimensionPixelSize(R.dimen.shopping_cart_gift_rightMargin);
        int dimensionPixelSizeTopBottomMargin = mContext.getResources().getDimensionPixelSize(R.dimen.shopping_cart_gift_top_bottom_Margin);
        //设置控件的宽高
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(dimensionPixelSizeLeftMargin,dimensionPixelSizeTopBottomMargin, dimensionPixelSizeRightMargin,dimensionPixelSizeTopBottomMargin);
        holder.gift_tag_ll.setLayoutParams(layoutParams); // 使设置好的布局参数应用到控件

        holder.gift_explain.setText(name);
        holder.gift_quantity.setText("x"+num);
        return convertView;
    }

    class ViewHolder{
        LinearLayout gift_tag_ll;
        TextView gift_tag;//礼物的标签
        TextView gift_explain;//礼物说明
        TextView gift_quantity;//礼物数量
    }
}

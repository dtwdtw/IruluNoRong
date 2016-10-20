package com.wf.irulu.module.homenew1_3_0.DiscoverFragment;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wf.irulu.IruluApplication;
import com.wf.irulu.R;
import com.wf.irulu.common.utils.ConstantsUtils;
import com.wf.irulu.common.utils.CustomerAnalystic;
import com.wf.irulu.common.utils.CustomerEvent;
import com.wf.irulu.framework.HomeActivity;
import com.wf.irulu.module.category.activity.CategoryPhoneActivity;
import com.wf.irulu.module.category.activity.CategorySmartDeviceActivity;
import com.wf.irulu.common.bean.HomeBean;

import java.util.List;

//import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * Created by dtw on 16/4/26.
 */
public class CataMenuListAdapter extends BaseAdapter {
    List<HomeBean.DiscoverBean.CatalogBean> catalogBeen;
    Activity context;


    public CataMenuListAdapter(Activity context, List<HomeBean.DiscoverBean.CatalogBean> catalogBeen) {
        this.catalogBeen = catalogBeen;
        this.context = context;
    }

    @Override
    public int getCount() {
        return catalogBeen.size();
    }

    @Override
    public Object getItem(int i) {
        return catalogBeen.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder mHolder = null;
        if (view == null) {
            mHolder = new ViewHolder();
            view = LayoutInflater.from(context).inflate(R.layout.catalogitem, null);
            LinearLayout linearLayout= (LinearLayout) view.findViewById(R.id.item);
            LinearLayout.LayoutParams layoutParams= (LinearLayout.LayoutParams) linearLayout.getLayoutParams();
            layoutParams.width=ConstantsUtils.DISPLAYW/4;
            linearLayout.setLayoutParams(layoutParams);
            mHolder.imageView = (ImageView) view.findViewById(R.id.cataicon);
            mHolder.textView = (TextView) view.findViewById(R.id.catatext);
            view.setTag(mHolder);
        } else {
            mHolder = (ViewHolder) view.getTag();
        }


        String temp = catalogBeen.get(i).getApp_icon();
        if (temp.startsWith("https")) {
            temp = temp.replace("https", "http");
        }
        int width = ConstantsUtils.DISPLAYW * 30 / 75;
        IruluApplication.getInstance().getPicasso().load(temp + "?imageView2/0/w/" + width + "/h/" + width + "/interlace/1/q/75").into(mHolder.imageView);
//        Picasso.with(context).load(temp).into(mHolder.imageView);
        Log.e("hellolove", catalogBeen.get(i).getApp_icon());
        mHolder.textView.setText(catalogBeen.get(i).getName());
        mHolder.imageView.setOnClickListener(new CataLogClick(i));
        return view;
    }

    class ViewHolder {
        ImageView imageView;
        TextView textView;
    }

    class CataLogClick implements View.OnClickListener {
        int position;

        public CataLogClick(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            if("Phone".equalsIgnoreCase(catalogBeen.get(position).getName())){
                CustomerAnalystic.analystic(CustomerEvent.num_of_shop_by_category_phone,((HomeActivity)context).mLogger);
                Intent phone = new Intent(context,CategoryPhoneActivity.class);
                phone.putExtra("name",catalogBeen.get(position).getName());
                phone.putExtra("id",catalogBeen.get(position).getId());
                context.startActivity(phone);
            }else{
                switch (catalogBeen.get(position).getName()){
                    case "Tablet":
                        CustomerAnalystic.analystic(CustomerEvent.num_of_shop_by_category_tablet,((HomeActivity)context).mLogger);
                        break;
                    case "Smart Device":
                        CustomerAnalystic.analystic(CustomerEvent.num_of_shop_by_category_smart_device,((HomeActivity)context).mLogger);
                        break;
                    case "Accessory":
                        CustomerAnalystic.analystic(CustomerEvent.num_of_shop_by_category_accessory,((HomeActivity)context).mLogger);
                        break;
                    default:
                        break;
                }
                Intent sd = new Intent(context,CategorySmartDeviceActivity.class);
                sd.putExtra("name",catalogBeen.get(position).getName());
                sd.putExtra("id",catalogBeen.get(position).getId());
                context.startActivity(sd);
            }
        }
    }
}

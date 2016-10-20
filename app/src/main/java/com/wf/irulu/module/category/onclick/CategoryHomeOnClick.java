package com.wf.irulu.module.category.onclick;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.wf.irulu.common.utils.CustomerAnalystic;
import com.wf.irulu.common.utils.CustomerEvent;
import com.wf.irulu.module.category.activity.CategoryActivity;
import com.wf.irulu.module.category.activity.CategoryPhoneActivity;
import com.wf.irulu.module.category.activity.CategorySmartDeviceActivity;

/**
 * Created by daniel on 2015/11/17.
 */
public class CategoryHomeOnClick implements View.OnClickListener {
    private String name;
    private Context mContext;
    private  int id;
    public CategoryHomeOnClick (Context context,String name,int id){
        this.name = name;
        this.mContext = context;
        this.id = id;
    }
    @Override
    public void onClick(View view) {
        if("Phone".equalsIgnoreCase(name)){
            CustomerAnalystic.analystic(CustomerEvent.num_of_shop_by_category_phone,((CategoryActivity)mContext).mLoger);
            Intent phone = new Intent(mContext,CategoryPhoneActivity.class);
            phone.putExtra("name",name);
            phone.putExtra("id",id);
            mContext.startActivity(phone);
        }else{
            switch (name){
                case "Tablet":
                    CustomerAnalystic.analystic(CustomerEvent.num_of_shop_by_category_tablet,((CategoryActivity)mContext).mLoger);
                    break;
                case "Smart Device":
                    CustomerAnalystic.analystic(CustomerEvent.num_of_shop_by_category_smart_device,((CategoryActivity)mContext).mLoger);
                    break;
                 case "Accessory":
                     CustomerAnalystic.analystic(CustomerEvent.num_of_shop_by_category_accessory,((CategoryActivity)mContext).mLoger);
                     break;
                default:
                    break;
            }
            Intent sd = new Intent(mContext,CategorySmartDeviceActivity.class);
            sd.putExtra("name",name);
            sd.putExtra("id",id);
            mContext.startActivity(sd);
        }
    }
}

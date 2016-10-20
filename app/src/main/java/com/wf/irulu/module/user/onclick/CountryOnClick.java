package com.wf.irulu.module.user.onclick;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.wf.irulu.R;
import com.wf.irulu.common.bean.CountryInforBean;
import com.wf.irulu.common.utils.ConstantsUtils;
import com.wf.irulu.module.user.activity.CountryActivity;
import com.wf.irulu.module.user.holder.Countryholder;

import java.util.ArrayList;

/**
 * Created by daniel on 2015/11/16.
 */

/**
 * 重写onclicklistener点击事件  防止出现卡顿现象
 */
public class CountryOnClick implements View.OnClickListener {
    private Countryholder tag;
    private int position;
    private Context context;
    private ArrayList<CountryInforBean> datas;

    public CountryOnClick(Context context, Countryholder tag, int position, ArrayList<CountryInforBean> datas) {
        this.tag = tag;
        this.position = position;
        this.datas = datas;
        this.context = context;

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.item_country_lv_rl) {
            tag.select.setVisibility(View.VISIBLE);
            CountryInforBean countryInforBean = datas.get(position);
            countryInforBean.getCountryName();
            Intent add = new Intent();
            add.putExtra("countryname", countryInforBean.getCountryName());
            add.putExtra("telecode", countryInforBean.getTelcode());
            add.putExtra("twoLetterIso", countryInforBean.getTwoLetterIso());
            ((CountryActivity) context).setResult(ConstantsUtils.SELECT_COUNTRY_RESULT_CODE, add);
            ((CountryActivity) context).finish();
        }
    }
}

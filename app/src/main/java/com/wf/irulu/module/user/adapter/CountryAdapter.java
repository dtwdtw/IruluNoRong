package com.wf.irulu.module.user.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.util.Log;
import com.wf.irulu.IruluApplication;
import com.wf.irulu.R;
import com.wf.irulu.common.bean.CountryInforBean;
import com.wf.irulu.common.utils.UIUtils;
import com.wf.irulu.module.user.holder.Countryholder;
import com.wf.irulu.module.user.onclick.CountryOnClick;

import java.util.ArrayList;

/**
 * Created by daniel on 2015/11/16.展示国家信息的adapter
 */

 public class CountryAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<CountryInforBean> datas;
    private String cname;
    private int flag = -1;
    public CountryAdapter(Context context,ArrayList<CountryInforBean> datas,String name){
        this.datas = datas;
        this.mContext= context;
        this.cname = name;
    }

        @Override
        public int getCount() {
            if (datas != null) {
                return datas.size();
            } else {
                return 0;
            }
        }

        @Override
        public Object getItem(int position) {
            if (datas != null) {
                return datas.get(position);
            } else {
                return null;
            }
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View coverview, ViewGroup viewGroup) {
            Countryholder tag;
            if (coverview == null) {
                tag = new Countryholder();
                coverview = View.inflate(mContext, R.layout.item_country_lv, null);

                tag.v = coverview.findViewById(R.id.item_country_lv_line_v);
                tag.letter = (TextView) coverview.findViewById(R.id.item_country_lv_letter);
                tag.flag = (ImageView) coverview.findViewById(R.id.item_country_lv_flag_iv);
                tag.name = (TextView) coverview.findViewById(R.id.item_coutry_lv_name_tv);
                tag.select = (ImageView) coverview.findViewById(R.id.item_country_lv_select_iv);
                tag.rl = (RelativeLayout) coverview.findViewById(R.id.item_country_lv_rl);
                tag.line1 = coverview.findViewById(R.id.item_country_title_line);
                tag.line2 = coverview.findViewById(R.id.item_country_item_line);
                coverview.setTag(tag);
            } else {
                tag = (Countryholder) coverview.getTag();
            }
            CountryInforBean cib = datas.get(position);
            IruluApplication.getInstance().getPicasso().load(cib.getIcon()+"?imageView2/3/w/" + UIUtils.dip2px(45) + "/h/" + UIUtils.dip2px(30)).error(R.mipmap.bg_picutre_show).into(tag.flag);
            tag.name.setText(cib.getCountryName());
            tag.letter.setText(cib.getFirstLetter());
            if(cname!=null&&cname.equalsIgnoreCase(cib.getCountryName())){
                flag = position;
            }
            if(flag!=-1&&flag==position){
                tag.select.setVisibility(View.VISIBLE);
            }else{
                tag.select.setVisibility(View.GONE);
            }
            if (position != 0) {
                if (cib.getFirstLetter().equals(datas.get(position - 1).getFirstLetter())) {
                    tag.letter.setVisibility(View.GONE);
                    tag.v.setVisibility(View.GONE);
                    tag.line1.setVisibility(View.GONE);
                    tag.line2.setVisibility(View.VISIBLE);
                } else {
                    tag.letter.setVisibility(View.VISIBLE);
                    tag.v.setVisibility(View.VISIBLE);
                    tag.line1.setVisibility(View.VISIBLE);
                    tag.line2.setVisibility(View.GONE);
                }
            } else {
                tag.line1.setVisibility(View.VISIBLE);
                tag.line2.setVisibility(View.GONE);
                tag.v.setVisibility(View.GONE);
                tag.letter.setVisibility(View.VISIBLE);
            }

            tag.rl.setOnClickListener(new CountryOnClick(mContext,tag,position,datas));
            return coverview;
        }
    }

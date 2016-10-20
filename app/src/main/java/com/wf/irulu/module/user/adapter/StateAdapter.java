package com.wf.irulu.module.user.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wf.irulu.R;
import com.wf.irulu.common.bean.StateInfo;
import com.wf.irulu.module.user.holder.Countryholder;
import com.wf.irulu.module.user.onclick.StateOnClick;

import java.util.ArrayList;

/**
 * Created by iRULU on 2016/3/26.
 */
public class StateAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<StateInfo.ListEntity> datas;
    private int flag = -1;

    public StateAdapter(Context context, ArrayList<StateInfo.ListEntity> datas) {
        this.datas = datas;
        this.mContext = context;

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
            coverview = View.inflate(mContext, R.layout.item_state_lv, null);

            tag.v = coverview.findViewById(R.id.item_state_lv_line_v);
            tag.letter = (TextView) coverview.findViewById(R.id.item_state_lv_letter);

            tag.name = (TextView) coverview.findViewById(R.id.item_state_lv_name_tv);
            tag.select = (ImageView) coverview.findViewById(R.id.item_state_lv_select_iv);
            tag.rl = (RelativeLayout) coverview.findViewById(R.id.item_state_lv_rl);
            tag.line1 = coverview.findViewById(R.id.item_state_title_line);
            tag.line2 = coverview.findViewById(R.id.item_state_item_line);
            coverview.setTag(tag);
        } else {
            tag = (Countryholder) coverview.getTag();
        }
        StateInfo.ListEntity cib = datas.get(position);
        tag.name.setText(cib.getName()+"("+cib.getCode()+")");
        tag.letter.setText(cib.getName().substring(0,1).toUpperCase());
        if (flag != -1 && flag == position) {
            tag.select.setVisibility(View.VISIBLE);
        } else {
            tag.select.setVisibility(View.GONE);
        }
        if (position != 0) {
            if (cib.getName().substring(0,1).equals(datas.get(position - 1).getName().substring(0,1))) {
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

        tag.rl.setOnClickListener(new StateOnClick(mContext, tag, position, datas));
        return coverview;
    }

}

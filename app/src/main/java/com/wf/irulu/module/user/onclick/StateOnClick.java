package com.wf.irulu.module.user.onclick;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.wf.irulu.R;
import com.wf.irulu.common.bean.StateInfo;
import com.wf.irulu.common.utils.ConstantsUtils;
import com.wf.irulu.module.user.holder.Countryholder;

import java.util.ArrayList;

/**
 * Created by iRULU on 2016/3/26.
 */
public class StateOnClick implements View.OnClickListener {
    private Countryholder tag;
    private int position;
    private Context context;
    private ArrayList<StateInfo.ListEntity> datas;

    public StateOnClick(Context context, Countryholder tag, int position, ArrayList<StateInfo.ListEntity> datas) {
        this.tag = tag;
        this.position = position;
        this.datas = datas;
        this.context = context;

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.item_state_lv_rl) {
            tag.select.setVisibility(View.VISIBLE);
            StateInfo.ListEntity stateInfo = datas.get(position);

            Intent add = new Intent();
            add.putExtra("code", stateInfo.getCode());
            add.putExtra("stateId", stateInfo.getId());

            ((Activity) context).setResult(ConstantsUtils.SELECR_STATE_RESULT_CODE, add);
            ((Activity) context).finish();
        }
    }
}

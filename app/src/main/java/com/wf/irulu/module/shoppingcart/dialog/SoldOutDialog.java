package com.wf.irulu.module.shoppingcart.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.wf.irulu.IruluApplication;
import com.wf.irulu.R;
import com.wf.irulu.common.bean.ProductCart;
import com.wf.irulu.common.utils.StringUtils;
import com.wf.irulu.module.shoppingcart.adapter.SoldOutAdapter;

import java.util.ArrayList;

/**
 * @描述: 限售(Shipment Restricted)的Dialog
 * @项目名: irulu1.2
 * @包名:com.wf.irulu.module.payment.dialog
 * @类名:ShipmentRestrictedDialog
 * @作者: 左杰
 * @创建时间:2015/11/19 16:22
 */
public class SoldOutDialog extends Dialog {
    private Context mContext;
    /** 按钮的点击监听*/
    private View.OnClickListener mOnClickListener;
    private ArrayList<ProductCart> mProductList;

    public SoldOutDialog(Context context) {
        super(context, R.style.Theme_Dialog_Base);
        this.mContext = context;
    }

    public SoldOutDialog(Context context, int theme) {
        super(context, theme);
        mContext = context;
    }

    /**
     * 设置回调监听
     *
     * @param onClickListener
     */
    public void setListener(ArrayList<ProductCart> productList,View.OnClickListener onClickListener) {
        this.mProductList = productList;
        this.mOnClickListener = onClickListener;
        init();
    }

    private void init() {
        initView();
        Window window = getWindow();
        window.setGravity(Gravity.BOTTOM);
        window.setWindowAnimations(R.style.DialogPopupAnimation);
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(lp);
    }

    /**
     * 初始化界面
     */
    private void initView() {
        View view = View.inflate(mContext,R.layout.dialog_shipment_restricted,null);
        ImageView titleCancel = (ImageView) view.findViewById(R.id.title_cancel);
        ListView mListView = (ListView) view.findViewById(R.id.shipment_restricted_lv);
        Button removeAll = (Button) view.findViewById(R.id.shipment_restricted_remove);
        TextView titleTxt = (TextView) view.findViewById(R.id.title_txt);
        titleTxt.setText("Sold Out");
        View headLayout = View.inflate(mContext, R.layout.item_sold_out_header_layout, null);
        mListView.addHeaderView(headLayout);
        /**
         * 关闭对话框
         */
        titleCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        ArrayList<ProductCart> productList = new ArrayList<ProductCart>();
        int size = mProductList.size();
        for(int i=0;i <size;i++){
            ProductCart productCart = mProductList.get(i);
            String status = productCart.getStatus();
            boolean isenough = productCart.isenough();//如果为false，说明此商品库存不足
            if("0".equals(status) || isenough == false){//如果有下架商品
                productList.add(productCart);
            }
        }
        SoldOutAdapter adapter = new SoldOutAdapter(mContext,productList);
        mListView.setAdapter(adapter);
        removeAll.setOnClickListener(mOnClickListener);
        setContentView(view);
    }

}

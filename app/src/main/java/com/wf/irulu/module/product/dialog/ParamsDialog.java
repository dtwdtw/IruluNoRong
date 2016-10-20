package com.wf.irulu.module.product.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.wf.irulu.IruluApplication;
import com.wf.irulu.R;
import com.wf.irulu.common.bean.ProductDetail;
import com.wf.irulu.common.bean.TypeName;
import com.wf.irulu.common.utils.StringUtils;
import com.wf.irulu.common.utils.UIUtils;
import com.wf.irulu.common.view.ClickButton;
import com.wf.irulu.common.view.FlowLayout;
import com.wf.irulu.module.product.adapter.ParamsCheckBoxAdapter;
import com.wf.irulu.module.product.listener.ParamsChoosedListener;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @描述: TODO
 * @项目名: irulu1.2
 * @包名:com.wf.irulu.module.product.dialog
 * @类名:ParamsDialog
 * @作者: Yuki
 * @创建时间:2015/11/2
 */
public class ParamsDialog extends Dialog {

    public static final int ACTION_ADD_CART = 1;
    public static final int ACTION_BUY_NOW = 2;
    public static final int ACTION_VARIETY = 3;

    private int mAction = 1;
    private Context mContext;
    /**
     * 商品详情信息
     */
    private ProductDetail mDetail;
    /**
     * 详情选择回调监听
     */
    private ParamsChoosedListener mListener;
    /**
     * 图片加载器
     */
    private Picasso mPicasso;
    /**
     * 参数类型名称列表
     */
    private ArrayList<String> mTypeName;
    /**
     * 产品图片
     */
    private ImageView mParamIvImage;
    /**
     * 产品名称
     */
    private TextView mParamTvName;
    /**
     * 产品折扣价
     */
    private TextView mParamTvNewPrice;
    /**
     * 产品库存
     */
    private TextView mParamTvStock;
    /**
     * 数量输入框
     */
    private EditText mParamEtQuantity;
    /**
     * 数量指示器加
     */
    private ClickButton mParamsBtAdd;
    /**
     * 数量指示器减
     */
    private ClickButton mParamsBtDec;

    private Button mContinueBt;

    /**
     * 产品参数详情列表
     */
    private LinearLayout mParamsLlDetail;

    private LinearLayout mProductOptionsLL;
    private Button mBuyBt;
    private Button mAddBt;

    /**
     * 可选择参数队列（false表示不可选择，true表示可选择)
     */
    private ArrayList<HashMap<String, Boolean>> mParamsTags;
    /**
     * 参数数据适配器
     */
    private ArrayList<ParamsCheckBoxAdapter> mParamsAdapters;
    /**
     * 参数类型列表
     */
    private ArrayList<TypeName> mTypeNames;
    /**
     * 按钮的点击监听
     */
    private View.OnClickListener mOnClickListener;
    /**
     * 数量
     */
    private int mQuantity = 1;
    /**
     * 库存
     */
    private int mStock = 0;

    public ParamsDialog(Context context) {
        super(context, R.style.Theme_Hold_Dialog_Base);
        this.mContext = context;
        setCanceledOnTouchOutside(true);//外部点击的时候是否可以取消
        mPicasso = IruluApplication.getInstance().getPicasso();
    }


    public int getAction() {
        return mAction;
    }

    public void show(int action) {
        if (this.mAction != action) {
            mContinueBt.setClickable(true);
        }
        this.mAction = action;
        switchOptionsViewVisibility(action);
        Log.d("TB", "mAddCartBt.eanble=" + mAddBt.isEnabled() + "==>isClickable=" + mAddBt.isClickable() + "==>mAddBt.hasOnClickListeners="+mAddBt.hasOnClickListeners());
        show();

    }


    private void switchOptionsViewVisibility(int action) {

        int continueVisibility = action == ACTION_VARIETY ? View.GONE : View.VISIBLE;
        int optionsVisibility = continueVisibility == View.VISIBLE ? View.GONE : View.VISIBLE;
        mProductOptionsLL.setVisibility(optionsVisibility);
        mContinueBt.setVisibility(continueVisibility);


    }


    @Override
    public void show() {
        if (isShowing()) {
            return;
        }
        super.show();

    }

    /**
     * 设置回调监听
     *
     * @param pListener
     */
    public void setListener(ParamsChoosedListener pListener, ProductDetail pDetail, ArrayList<String> pTypeName, View.OnClickListener pOnClickListener) {
        this.mListener = pListener;
        this.mDetail = pDetail;
        this.mTypeName = pTypeName;
        this.mOnClickListener = pOnClickListener;
        init();
    }

    /**
     * 初始化数据
     */
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
        LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_params_product, null);
        ImageView params_iv_cancel = (ImageView) view.findViewById(R.id.params_iv_cancel);
        params_iv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        mProductOptionsLL = (LinearLayout) view.findViewById(R.id.product_options);
        mBuyBt = (Button) view.findViewById(R.id.product_bt_buy_now);
        mAddBt = (Button) view.findViewById(R.id.product_bt_add_cart);

        mContinueBt = (Button) view.findViewById(R.id.params_continue);
        mParamIvImage = (ImageView) view.findViewById(R.id.params_iv_image);
        mParamTvName = (TextView) view.findViewById(R.id.params_tv_name);
        mParamTvNewPrice = (TextView) view.findViewById(R.id.params_tv_newprice);
        mParamTvStock = (TextView) view.findViewById(R.id.params_tv_stock);
        mParamsLlDetail = (LinearLayout) view.findViewById(R.id.params_ll_detail);
        mParamEtQuantity = (EditText) view.findViewById(R.id.params_et_num);
        mParamsBtDec = (ClickButton) view.findViewById(R.id.params_iv_dec);
        mParamsBtAdd = (ClickButton) view.findViewById(R.id.params_iv_add);


        View itemView = null;
        HashMap<String, Boolean> paramsDetailTag = null;
        mParamsTags = new ArrayList<HashMap<String, Boolean>>();
        mParamsAdapters = new ArrayList<ParamsCheckBoxAdapter>();
        ParamsCheckBoxAdapter adapter = null;
        mTypeNames = mDetail.getTypeName();
        int length = mTypeNames == null ? 0 : mTypeNames.size();

        for (int i = 0; i < length; i++) {
            // 获取当前类型
            TypeName typeName = mTypeNames.get(i);
            paramsDetailTag = new HashMap<String, Boolean>();
            String[] list = typeName.getList();
            int listLength = list.length;
            for (int j = 0; j < listLength; j++) {
                paramsDetailTag.put(list[j], true);
            }
            mParamsTags.add(paramsDetailTag);
            itemView = inflater.inflate(R.layout.item_product_params, null);
            // 类型名称
            TextView paramsDetailTvName = (TextView) itemView.findViewById(R.id.paramsdetail_tv_name);
            paramsDetailTvName.setText(typeName.getName());
            // 类型值列表
            FlowLayout flowLayout = (FlowLayout) itemView.findViewById(R.id.paramsdetail_fl_list);
            // 设置横向间距
            flowLayout.setHorizontalSpacing(mContext.getResources().getDimensionPixelSize(R.dimen.product_margin_box_01));
            // 设置纵向间距
            flowLayout.setVerticalSpacing(mContext.getResources().getDimensionPixelSize(R.dimen.product_margin_box_01));
            // 添加数据
            adapter = new ParamsCheckBoxAdapter(typeName, mContext, mDetail.getType(), mTypeName, flowLayout, mListener);
            flowLayout.setAdapter(adapter);
            mParamsAdapters.add(adapter);
            mParamsLlDetail.addView(itemView);
        }

        String url = null;
        if (mDetail.getImage() != null && mDetail.getImage().size() > 0) {
            url = mDetail.getImage().get(0);
        }
        mParamTvName.setText(mDetail.getName());
        setTitle(mDetail.getDiscountPrice(), mDetail.getStock(), url);
        if (mDetail.getStatus() == 1) {
            mParamEtQuantity.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (TextUtils.isEmpty(s)) {
                        if (mStock <= 0) {
                            mParamEtQuantity.setText(String.valueOf(0));
                        } else {
                            mQuantity = 1;
                            mParamEtQuantity.setText(String.valueOf(mQuantity));
                        }
                    } else {
                        int num = 1;
                        try {
                            num = Integer.parseInt(mParamEtQuantity.getText().toString());
                        } catch (Exception e) {
                            num = 1;
                        }
                        if (num > 200 || num > mStock) {
                            num = Math.min(200, mStock);
                            mQuantity = num;
                            mParamEtQuantity.setText(String.valueOf(mQuantity));
                            mParamsBtAdd.setEnabled(false);
                        } else {
                            if (num == 200 || num == mStock) {
                                mParamsBtAdd.setEnabled(false);
                            } else {
                                mParamsBtAdd.setEnabled(true);
                            }
                        }
                        mQuantity = num;
                        if (s.toString().startsWith("0") && !s.toString().equals("0")) {
                            mParamEtQuantity.setText(String.valueOf(mQuantity));
                        }
                        if (mQuantity <= 1) {
                            mParamsBtDec.setEnabled(false);
                        } else {
                            mParamsBtDec.setEnabled(true);
                        }
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
            mParamsBtDec.setEnabled(false);
            mContinueBt.setEnabled(true);
            mAddBt.setEnabled(true);
            mBuyBt.setEnabled(true);

            mParamsBtDec.setOnClickListener(mOnClickListener);
            mParamsBtAdd.setOnClickListener(mOnClickListener);
            mBuyBt.setOnClickListener(mOnClickListener);
            mAddBt.setOnClickListener(mOnClickListener);
            mContinueBt.setOnClickListener(mOnClickListener);
        } else {

            mParamsBtDec.setEnabled(false);
            mParamsBtAdd.setEnabled(false);
            mContinueBt.setEnabled(false);
            mAddBt.setEnabled(false);
            mBuyBt.setEnabled(false);

            mQuantity = 0;
            mParamEtQuantity.setText(String.valueOf(mQuantity));
        }
        setContentView(view);
    }

    /**
     * 重置标题信息
     *
     * @param pNewPrice 折扣价
     * @param pStock    库存
     * @param pUrl      图片地址
     */
    public void setTitle(float pNewPrice, int pStock, String pUrl) {
        this.mStock = pStock;
        mParamTvNewPrice.setText(StringUtils.getPriceByFormat(pNewPrice));
        if (mDetail.getStatus() == 1) {
            // 判断库存
            if (pStock <= 0) {
                // 如果没有库存
                mContinueBt.setEnabled(false);
                mBuyBt.setEnabled(false);
                mAddBt.setEnabled(false);
                // 设置数量显示
                mParamEtQuantity.setText(String.valueOf(0));
                // 显示无库存
                mParamTvStock.setText(mContext.getString(R.string.out_of_stock));
                mParamTvStock.setTextColor(mContext.getResources().getColor(R.color.blue_line_image));
                mParamsBtDec.setEnabled(false);
                mParamsBtAdd.setEnabled(false);
            } else {
                // 如果有库存
                mContinueBt.setEnabled(true);
                mBuyBt.setEnabled(true);
                mAddBt.setEnabled(true);
                // 如果选择数量大于库存量
                if (pStock < mQuantity) {
                    // 设置数量为库存量
                    mQuantity = pStock;
                }
                if (mQuantity == 0 && pStock > 0) {
                    mQuantity = 1;
                }
                // 设置数量显示
                mParamEtQuantity.setText(String.valueOf(mQuantity));
                // 库存在1～10之间，显示库存数量
                if (pStock <= 10) {
                    mParamTvStock.setText("Only ");
                    mParamTvStock.append(String.valueOf(pStock));
                    mParamTvStock.append(" Left");
                    mParamTvStock.setTextColor(mContext.getResources().getColor(R.color.blue_line_image));
                } else {
                    // 否则显示有库存
                    mParamTvStock.setText(mContext.getString(R.string.in_stock));
                    mParamTvStock.setTextColor(mContext.getResources().getColor(R.color.blue_line_image));
                }
            }
        }
        // 加载图片
        if (!TextUtils.isEmpty(pUrl)) {
            mPicasso.load(StringUtils.getThumbnailImageUrlFormat(pUrl, UIUtils.getSixthWidth())).placeholder(R.mipmap.notify_image_xiaotu).error(R.mipmap.notify_image_xiaotu).into(mParamIvImage);
        }
    }

    public void setChoosed(HashMap<String, String> params, String key) {
        // 获取参数类型个数
        int total = mTypeName.size();
        // 循环比对刷新当前的参数列表
        for (int i = 0; i < total; i++) {
            //如果当前选择的参数类型发生变化，则当前的参数类型列表只需要更新点击状态，不需要更新是否可以选择的状态
            if (mTypeName.get(i).equals(key)) {
                continue;
            }
            // 否则需要更新可选择状态
            mParamsAdapters.get(i).setChoosed(params);
        }
    }

    /**
     * 设置数量
     *
     * @param isAdd 是否点击增加
     */
    public void setQuantity(boolean isAdd) {
        if (isAdd) {
            if (mQuantity < 200 && mQuantity < mStock) {
                mQuantity++;
                if (mQuantity >= 200 || mQuantity <= mStock) {
                    mParamsBtAdd.setEnabled(false);
                }
            }
            mParamsBtDec.setEnabled(true);
        } else {
            if (mQuantity > 1 && mStock > 0) {
                mQuantity--;
                if (mQuantity <= 1) {
                    mParamsBtDec.setEnabled(false);
                }
            }
            mParamsBtAdd.setEnabled(true);
        }
        mParamEtQuantity.setText(String.valueOf(mQuantity));
        if (mStock <= 0) {
            mParamEtQuantity.setText(String.valueOf(0));
        }
    }

    public void setOptionsClickable(boolean isClickable, int action) {
        if (action == ACTION_BUY_NOW) {
            mBuyBt.setClickable(isClickable);
        } else if(action==ACTION_ADD_CART) {
            mAddBt.setClickable(isClickable);
        }
        setContinueClickable(isClickable, action);

    }


    private void setContinueClickable(boolean isClickable, int action) {
        if (!isShowing()) {
            return;
        }

        if (action != mAction) {
            return;
        }
        mContinueBt.setClickable(isClickable);
    }


    /**
     * 获取数量
     *
     * @return 数量
     */
    public int getQuantity() {
        return mQuantity;
    }

}

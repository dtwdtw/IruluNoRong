package com.wf.irulu.module.product.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;

import com.wf.irulu.R;
import com.wf.irulu.common.bean.Type;
import com.wf.irulu.common.bean.TypeName;
import com.wf.irulu.common.view.FlowLayout;
import com.wf.irulu.module.product.listener.ParamsChoosedListener;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by XImoon on 15/11/7.
 */
public class ParamsCheckBoxAdapter implements FlowLayout.FlowLayoutAdapter{

    /**  当前参数类型的详细信息*/
    private TypeName mTypeName;
    /** 上下文*/
    private Context mContext;
    /** 装载信息的布局*/
    private FlowLayout mFlowLayout;
    /** 参数选择监听器*/
    private ParamsChoosedListener mListener;
    /** 参数类型名称列表*/
    private ArrayList<String> mNames;
    /** sku列表*/
    private ArrayList<Type> mSkus;
    /** 当前参数是否可以选择标签*/
    private HashMap<String,Boolean> mParamsTags;
    /** 记录上一个点击的position*/
    private int mCurrentPosition = -1;

    public ParamsCheckBoxAdapter(TypeName pTypeName,Context pContext,ArrayList<Type> pSkus,ArrayList<String> pNames,FlowLayout pFlowLayout,ParamsChoosedListener pListener){
        this.mContext = pContext;
        this.mSkus = pSkus;
        this.mNames = pNames;
        this.mTypeName = pTypeName;
        this.mFlowLayout = pFlowLayout;
        this.mListener = pListener;
        this.mParamsTags = new HashMap<String,Boolean>();

    }

    /**
     * 初始化
     */
    @Override
    public void init(){
        LayoutInflater inflater = LayoutInflater.from(mContext);
        int size = mTypeName.getList().length;
        CheckBox paramsCb = null;
        for (int i = 0; i < size; i++) {
            String param = mTypeName.getList()[i];
            mParamsTags.put(param, true);
            // 获取子view
            paramsCb = (CheckBox) inflater.inflate(R.layout.item_params_radiobutton, null);
            // 设置view显示名称
            paramsCb.setText(param);
            // 设置checkbox选择的监听事件
            paramsCb.setOnClickListener(new OnChoosedParamsChangedListener(i));
            mFlowLayout.addView(paramsCb);
        }
    }

    /**
     * 视图的刷新
     */
    @Override
    public void refresh(){
        int size = mTypeName.getList().length;
        for (int i = 0; i < size; i++){
            CheckBox checkBox = (CheckBox) mFlowLayout.getChildAt(i);
            String text = mTypeName.getList()[i];
            if (mParamsTags.get(text)){
                checkBox.setEnabled(true);
                if (i == mCurrentPosition){
                    checkBox.setChecked(true);
                }else {
                    checkBox.setChecked(false);
                }
            }else{
                checkBox.setEnabled(false);
            }
        }
    }

    /**
     * 回调判断
     */
    @Override
    public void setChoosed(HashMap<String,String> params) {
        int count = mTypeName.getList().length;
        for (int i = 0; i < count; i++) {
            mParamsTags.put(mTypeName.getList()[i], false);
        }
        int size = mSkus.size();
        // 循环比对当前已选择的参数列表和sku列表
        for (int i = 0; i < size; i++) {
            ArrayList<String> sku = mSkus.get(i).getType();
            // 获取参数类型的个数（sku的长度）
            int length = sku.size();
            // 循环判断是否一致的标志
            boolean isContained = true;
            // 循环比对当前参数
            for (int j = 0; j < length; j++) {
                // 当前参数已选择,对可选择的参数列表发生影响，若未选择，对参数无影响
                if (!TextUtils.isEmpty(params.get(mNames.get(j)))) {
                    // 判断出当前参数类型项的剩余params完全匹配（如果为当前项则对参数的可否选择不造成影响，所以放行即选择默认选中，由非当前选项对其进行判断是否可以选中）
                    if (sku.get(j).equals(params.get(mNames.get(j))) || mTypeName.getName().equals(mNames.get(j))) {
                        continue;
                    } else {
                        // 发生参数不匹配
                        isContained = false;
                        break;
                    }
                }
            }
            // 若完全匹配
            if (isContained == true) {
                // 索引出当前的匹配的sku对应的参数类型项值，设置成true
                mParamsTags.put(sku.get(mNames.indexOf(mTypeName.getName())), true);
            }
        }
        // 刷新视图
        refresh();
    }

    private class OnChoosedParamsChangedListener implements View.OnClickListener {

        /** 当前被选中的参数值 */
        private String mValue;
        /** 当前点击的position*/
        private int mParamLocation = -1;
        /** 选择状态*/
        private boolean isChecked;

        public OnChoosedParamsChangedListener(int pParamPosition) {
            this.mValue = mTypeName.getList()[pParamPosition];
            this.mParamLocation = pParamPosition;
        }

        @Override
        public void onClick(View v) {
            // 获取当前选择的状态
            this.isChecked = ((CheckBox)mFlowLayout.getChildAt(mParamLocation)).isChecked();
            // 如果上一次和本次点击的position一致并且为第一次点击状态
            if (mParamLocation != mCurrentPosition && mCurrentPosition != -1){
                // 取消上一次的选择
                ((CheckBox) mFlowLayout.getChildAt(mCurrentPosition)).setChecked(false);
            }
            // 记录当前选择位置
            mCurrentPosition = mParamLocation;
            if (isChecked) {
                // 如果是选择状态，已选择参数对应参数类型的值为当前选择的值
                mListener.changeParams(mTypeName.getName(), mValue);
            } else {
                // 如果是未选择状态，已选择参数对应参数类型的值被重置为null
                mListener.changeParams(mTypeName.getName(), null);
                mCurrentPosition = -1;
            }
        }
    }
}
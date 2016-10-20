package com.wf.irulu.module.payment.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;
import com.wf.irulu.R;
import com.wf.irulu.common.base.CommonTitleBaseActivity;
import com.wf.irulu.common.bean.PaypalSuccess;
import com.wf.irulu.common.utils.ConstantsUtils;
import com.wf.irulu.common.utils.CustomerEvent;
import com.wf.irulu.common.utils.StringUtils;
import com.wf.irulu.framework.HomeActivity;
import com.wf.irulu.module.order.activity.OrderDetailActivity;

import java.util.ArrayList;

/**
 * @描述: 支付成功或者Pending页面
 * @项目名: irulu1.2
 * @包名:com.wf.irulu.module.payment.activity
 * @类名:PaySuccessOrPayPendingActivity
 * @作者: 左杰
 * @创建时间:2015/11/17 19:53
 */
public class PaySuccessOrPayPendingActivity extends CommonTitleBaseActivity{
    /**
     * Title上面的名称
     */
    private String titleName;

    /**
     * 支付总金额控件
     */
    private TextView mPendingPayment;
    /**
     * 订单号控件
     */
    private TextView mOrderId;
    /**
     * 成功或者待定的图片控件
     */
    private ImageView mSuccessImg;
    /**
     * 成功的文本控件
     */
    private TextView mSuccessText;
    /**
     * 待定的文本控件1
     */
    private TextView mPendingText1;
    /**
     * 待定的文本控件2
     */
    private TextView mPendingText2;
    /**
     * 待定的文本控件3
     */
    private TextView mPendingText3;
    /**
     * 支付状态，2支付成功，1待审核，0支付不成功,默认支付成功
     */
    private int payStatus = 2;
    /**
     * 支付总金额
     */
    private float total;
    /**
     * 订单号
     */
    private String orderId = "";
    /**
     * 支付成功或待定的数据对象
     */
    private PaypalSuccess paypalSuccess;
    /**Transaction ID*/
    private TextView mTransactionText;
    /**Transaction ID的值*/
    private TextView mTransactionId;


    @Override
    protected String setWindowTitle() {
        return "Payment Result";
    }

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_pay_success_pending);
    }

    @Override
    public void initView() {
        mPendingPayment = (TextView) findViewById(R.id.pay_success_pending_payment);
        mOrderId = (TextView) findViewById(R.id.pay_success_order_id);
        mSuccessImg = (ImageView) findViewById(R.id.success_img);
        mSuccessText = (TextView) findViewById(R.id.success_txt);
        mPendingText1 = (TextView) findViewById(R.id.pending_txt1);
        mPendingText2 = (TextView) findViewById(R.id.pending_txt2);
        mPendingText3 = (TextView) findViewById(R.id.pending_txt3);
        mTransactionText = (TextView) findViewById(R.id.pay_success_transaction_txt);//Transaction ID
        mTransactionId = (TextView) findViewById(R.id.pay_success_transaction_id);//Transaction ID的值

        paypalSuccess = (PaypalSuccess) getIntent().getParcelableExtra("paypalSuccess");
        payStatus = paypalSuccess.getPayStatus();
        total = paypalSuccess.getTotal();//支付总金额

        // 统计支付完成页面
        MobclickAgent.onEvent(this, String.valueOf(CustomerEvent.filter_enter_pay_complate_view), ConstantsUtils.mVersionAnalystics);
    }

    @Override
    public void initData() {
        ArrayList<String> orderList = paypalSuccess.getOrderList();
        int size = orderList.size();
        for (int i = 0; i < size; i++) {
            orderId += paypalSuccess.getOrderList().get(i);
            orderId += ConstantsUtils.ENTER;//加换行
        }
        orderId = orderId.substring(0, orderId.lastIndexOf(ConstantsUtils.ENTER));//拼接订单号，因为有可能有多个订单
        mPendingPayment.setText(StringUtils.getPriceByFormat(total));//显示支付总金额
        mOrderId.setText(orderId);//显示订单号

        String pendingReason = paypalSuccess.getPendingReason();
        switch (payStatus) {
            case 2://支付成功，跳转到Success to pay
                mSuccessImg.setImageResource(R.mipmap.pay_success_to_pay_icon);
                mSuccessText.setVisibility(View.VISIBLE);
                mPendingText1.setVisibility(View.GONE);
                mPendingText2.setVisibility(View.GONE);
                mPendingText3.setVisibility(View.GONE);
                mTransactionText.setVisibility(View.VISIBLE);
                mTransactionId.setVisibility(View.VISIBLE);
                mTransactionId.setText(paypalSuccess.getTransactionId());
                break;
            case 1://待审核，跳转到Pending
                mSuccessImg.setImageResource(R.mipmap.pay_pending_to_pay_icon);
                if(!TextUtils.isEmpty(pendingReason)){
                    mSuccessText.setVisibility(View.VISIBLE);
                    mPendingText1.setVisibility(View.GONE);
                    mPendingText2.setVisibility(View.GONE);
                    mPendingText3.setVisibility(View.GONE);
                    mSuccessText.setText(pendingReason);
                }else {
                    mSuccessText.setVisibility(View.GONE);
                    mPendingText1.setVisibility(View.VISIBLE);
                    mPendingText2.setVisibility(View.VISIBLE);
                    mPendingText3.setVisibility(View.VISIBLE);
                    mTransactionText.setVisibility(View.GONE);
                    mTransactionId.setVisibility(View.GONE);
                }
            default:
                break;
        }
    }

    /**
     * 点击继续购物，回到首页
     * @param v
     */
    public void goToShoping(View v){
        Intent intent = new Intent(this,HomeActivity.class);
        startActivity(intent);
        finish();
    }

    /**
     * 回到订单详情
     * @param v
     */
    public void orderDetails(View v){
        OrderDetailActivity.startOrderDetailActivity(this,orderId);
        finish();
    }

    @Override
    public void onClick(View v) {

    }
}

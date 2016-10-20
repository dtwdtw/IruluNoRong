package com.wf.irulu.module.payment.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;
import com.umeng.analytics.MobclickAgent;
import com.wf.irulu.R;
import com.wf.irulu.common.base.CommonTitleBaseActivity;
import com.wf.irulu.common.bean.CreditCardPay;
import com.wf.irulu.common.bean.OrderDetail;
import com.wf.irulu.common.bean.PayPalEntity;
import com.wf.irulu.common.bean.PaypalSuccess;
import com.wf.irulu.common.bean.ShippingAddrBean;
import com.wf.irulu.common.utils.CacheUtils;
import com.wf.irulu.common.utils.ConstantsUtils;
import com.wf.irulu.common.utils.CustomerEvent;
import com.wf.irulu.common.utils.ILog;
import com.wf.irulu.common.view.PageLoadDialog;
import com.wf.irulu.common.view.SuperEditText;
import com.wf.irulu.logic.listener.ServiceListener;
import com.wf.irulu.module.order.activity.OrdersActivity;
import com.wf.irulu.module.user.activity.CountryActivity;

import org.json.JSONException;

import java.math.BigDecimal;

/**
 * @描述: Payment的页面
 * @项目名: irulu1.2
 * @包名:com.wf.irulu.module.payment.activity
 * @类名:PaymentMyMethodActivity
 * @作者: 左杰
 * @创建时间:2015/11/17 18:38
 */
public class PaymentMyMethodActivity extends CommonTitleBaseActivity implements ServiceListener {
    private static final int REQUEST_CODE_CHOOSE_COUNTRY = 3;
    private final String TAG = getClass().getCanonicalName();
    /**
     * 选着信用卡方式的布局
     */
    private LinearLayout chooseCreditCard;
    /**
     * 选着paypal方式的布局
     */
    private LinearLayout choosePaypal;
    /**
     * 选着相同的送货地址的布局
     */
    private LinearLayout chooseShippingAddress;
    /**
     * 选着相同的送货地址的单选按钮
     */
    private ImageView chooseShippingAddressImg;
    /**
     * 判断是否选中
     */
    private boolean isChecked = true;
    /**
     * 判断是否是信用卡被选中，默认被选中
     */
    private boolean isCreditCard = true;
    /**
     * 选着信用卡方式的单选按钮
     */
    private ImageView chooseCreditCardImg;
    /**
     * 选着paypal方式的单选按钮
     */
    private ImageView choosePaypalImg;
    /**
     * 行用卡支付的一些信息，当选中paypal支付方式时，隐藏这个
     */
    private LinearLayout paymentInfoLayout;
    /**
     * 信用卡的卡号
     */
    private SuperEditText mCardNumber;
    /**
     * 选着国家的文本名称
     */
    private TextView mChooseCountryText;
    /**
     * 安全代码(CVV)
     */
    private EditText mSecurityCode;
    private EditText mMonth;
    private EditText mYear;
    private EditText mFistName;
    private EditText mLastName;
    private EditText mAddressLine1;
    private EditText mAddressLine2;
    private TextView mCountry;
    private EditText mState;
    private EditText mCity;
    private EditText mPostCode;
    private EditText mPhone;
    /**生成订单Bean*/
//    private CreateOrder createOrderBean;
    /**
     * 订单详情Bean
     */
    private OrderDetail mOrderDetail;
    /**
     * 国家 2个字母缩写
     */
    private String mTwoLetterIso = "";
    /**地址bean*/
//    private ShoppingAddr mShoppingAddr;
    /**
     * 填写信用卡信息的图标，当匹配用户输完信用卡账号后判断是否显示该图标
     */
    private ImageView mCreditCardInfoImg;
    private String token;

    @Override
    protected String setWindowTitle() {
        return "Payment";
    }

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_payment_layout);
    }

    @Override
    public void initView() {
        chooseCreditCard = (LinearLayout) findViewById(R.id.choose_credit_card_ll);//选着信用卡方式的布局
        chooseCreditCardImg = (ImageView) findViewById(R.id.choose_credit_card_img);//选着信用卡方式的单选按钮
        choosePaypal = (LinearLayout) findViewById(R.id.choose_paypal_ll);//选着paypal方式的布局
        choosePaypalImg = (ImageView) findViewById(R.id.choose_paypal_img);//选着paypal方式的单选按钮
        chooseShippingAddress = (LinearLayout) findViewById(R.id.choose_same_shipping_address_ll);//选着相同的送货地址的布局
        chooseShippingAddressImg = (ImageView) findViewById(R.id.choose_shipping_address_img);//选着相同的送货地址的单选按钮
        paymentInfoLayout = (LinearLayout) findViewById(R.id.payment_info_layout);//行用卡支付的一些信息，当选中paypal支付方式时，隐藏这个
        mCreditCardInfoImg = (ImageView) findViewById(R.id.credit_card_info_img);//填写信用卡信息的图标，当匹配用户输完信用卡账号后判断是否显示该图标

        mCardNumber = (SuperEditText) findViewById(R.id.card_number_et);//信用卡的卡号
        mSecurityCode = (EditText) findViewById(R.id.security_code_et);//安全代码(CVV)
        mMonth = (EditText) findViewById(R.id.mm_et);
        mYear = (EditText) findViewById(R.id.yy_et);
        mFistName = (EditText) findViewById(R.id.fist_name_et);
        mLastName = (EditText) findViewById(R.id.last_name_et);
        mAddressLine1 = (EditText) findViewById(R.id.address_line1_et);
        mAddressLine2 = (EditText) findViewById(R.id.address_line2_et);
        mCountry = (TextView) findViewById(R.id.choose_country_txt);
        mState = (EditText) findViewById(R.id.state_province_region_et);
        mCity = (EditText) findViewById(R.id.city_et);
        mPostCode = (EditText) findViewById(R.id.zip_postal_code_et);
        mPhone = (EditText) findViewById(R.id.phone_number_et);

        RelativeLayout chooseCountryLayout = (RelativeLayout) findViewById(R.id.choose_country_rl);//选着国家的布局
        mChooseCountryText = (TextView) findViewById(R.id.choose_country_txt);//选着国家的文本名称

        Button payNow = (Button) findViewById(R.id.payNow);//提交支付按钮
        chooseCreditCard.setOnClickListener(this);
        choosePaypal.setOnClickListener(this);
        chooseShippingAddress.setOnClickListener(this);
        chooseCountryLayout.setOnClickListener(this);
        payNow.setOnClickListener(this);
        MobclickAgent.onEvent(this, String.valueOf(CustomerEvent.filter_enter_payment), ConstantsUtils.mVersionAnalystics);
    }

    @Override
    public void initData() {
        mOrderDetail = (OrderDetail) getIntent().getParcelableExtra("orderDetail");
        //默认信用卡被选中
        chooseCreditCardImg.setImageResource(R.mipmap.round_button_selected);
        mCardNumber.showType = true;//开启银行卡模式
        mCardNumber.setOnAfterTextChangedListener(new SuperEditText.onAfterTextChangedListener() {
            @Override
            public void onAfterTextChanged(Editable s) {
                int length = s.length();
                if (length == 4) {//输入的开好是四个，就开始比较
                    int i = Integer.parseInt(s.toString());
                    ILog.e(TAG, "i=" + i);
                    mCreditCardInfoImg.setVisibility(View.VISIBLE);
                    if (i >= 4000 && i <= 4999) { //威士卡（VISA）
                        mCreditCardInfoImg.setImageResource(R.mipmap.icon_visa);
                    } else if (i >= 5100 && i <= 5599) {//万事达卡（MasterCard）
                        mCreditCardInfoImg.setImageResource(R.mipmap.icon_mastercard);
                    } else if ((i >= 3400 && i <= 3499) || (i >= 3700 && i <= 3799)) {//运通卡（American Express）
                        mCreditCardInfoImg.setImageResource(R.mipmap.icon_american);
                    } else if (i == 5018 || i == 5020 || i == 5038 || i == 6304 || i == 6759 || i == 6761 || i == 6762 || i == 6763) {//maestro
                        mCreditCardInfoImg.setImageResource(R.mipmap.icon_maestro);
                    } else {
                        mCreditCardInfoImg.setVisibility(View.GONE);
                    }
                } else if (length == 0) {//如果输入的卡号是0个，就隐藏信用卡的图片
                    mCreditCardInfoImg.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.choose_credit_card_ll://选着信用卡的点击事件
                chooseCreditCardImg.setImageResource(R.mipmap.round_button_selected);
                choosePaypalImg.setImageResource(R.mipmap.round_button_normal);
                paymentInfoLayout.setVisibility(View.VISIBLE);
                isCreditCard = true;
                break;
            case R.id.choose_paypal_ll://选着paypal的点击事件
                choosePaypalImg.setImageResource(R.mipmap.round_button_selected);
                chooseCreditCardImg.setImageResource(R.mipmap.round_button_normal);
                paymentInfoLayout.setVisibility(View.GONE);
                isCreditCard = false;
                break;
            case R.id.choose_same_shipping_address_ll://选着ShippingAddress的点击事件的布局
                ILog.i(TAG, "choose_same_shipping_address_ll的点击事件");
                if (isChecked) {//选中
                    chooseShippingAddressImg.setImageResource(R.mipmap.round_button_selected);
                    isChecked = !isChecked;
                    /**选中的时候填写好送货地址信息 start */
                    ShippingAddrBean shippingAddrBean = mOrderDetail.getShoppingAddr();
                    mFistName.setText(shippingAddrBean.getFirstName());
                    mLastName.setText(shippingAddrBean.getLastName());
                    mAddressLine1.setText(shippingAddrBean.getAddress1());
                    mAddressLine2.setText(shippingAddrBean.getAddress2());
                    mTwoLetterIso = shippingAddrBean.getAbbreviation();
                    mCountry.setText(shippingAddrBean.getCountry());
                    mState.setText(shippingAddrBean.getState());
                    mCity.setText(shippingAddrBean.getCity());
                    mPostCode.setText(shippingAddrBean.getZipCode());
                    mPhone.setText(shippingAddrBean.getPhone());
                    /**选中的时候填写好送货地址信息 start */
                } else {//未选中
                    chooseShippingAddressImg.setImageResource(R.mipmap.round_button_normal);
                    isChecked = !isChecked;
                    /**未选中的时候取消填写好送货地址信息 start */
                    mFistName.setText("");
                    mLastName.setText("");
                    mAddressLine1.setText("");
                    mAddressLine2.setText("");
                    mTwoLetterIso = "";
                    mCountry.setText("Select country");
                    mState.setText("");
                    mCity.setText("");
                    mPostCode.setText("");
                    mPhone.setText("");
                    /**未选中的时候取消填写好送货地址信息 start */
                }
                break;
            case R.id.choose_country_rl:
                ILog.i(TAG, "选着国家执行了");
                Intent intent = new Intent(PaymentMyMethodActivity.this, CountryActivity.class);
                startActivityForResult(intent, REQUEST_CODE_CHOOSE_COUNTRY);
                break;
            case R.id.payNow:
                if (!isCreditCard) {//paypal支付
                    ILog.i(TAG, "是paypal支付方式");
                    PageLoadDialog.showDialogForLoading(this, false, false);
                    //paypal支付(从Paypal获取支付token)
                    controller.getServiceManager().getPaymentService().getPayToken(this);

                    // 统计支付
                    MobclickAgent.onEvent(this, String.valueOf(CustomerEvent.filter_pay_clicked), ConstantsUtils.mVersionAnalystics);

                } else {//信用卡支付
                    ILog.i(TAG, "是信用卡支付方式");
                    CreditCardPay creditCardPay = new CreditCardPay();

                    String cardNumberStr = mCardNumber.getText().toString().trim();//4392 2600 1992 7209
                    cardNumberStr = cardNumberStr.replace(" ", "");//去掉信用卡的空格
                    ILog.i(TAG, "cardNumberStr===>" + cardNumberStr);//4392260019927209
                    String monthStr = mMonth.getText().toString().trim();
                    String year = mYear.getText().toString().trim();
                    String securityCodeStr = mSecurityCode.getText().toString().trim();
                    String fistNameStr = mFistName.getText().toString().trim();
                    String lastNameStr = mLastName.getText().toString().trim();
                    String addressLine1Str = mAddressLine1.getText().toString().trim();
                    String addressLine2Str = mAddressLine2.getText().toString().trim();
//                    String countryStr = mCountry.getText().toString().trim();
                    String stateStr = mState.getText().toString().trim();
                    String cityStr = mCity.getText().toString().trim();
                    String postCodeStr = mPostCode.getText().toString().trim();
                    String phoneStr = mPhone.getText().toString().trim();

                    if (TextUtils.isEmpty(cardNumberStr)) {
                        showToast("Please enter card number!");
                        return;
                    } else if (TextUtils.isEmpty(securityCodeStr)) {
                        showToast("Please enter security code!");
                        return;
                    } else if (TextUtils.isEmpty(monthStr) || TextUtils.isEmpty(year)) {
                        showToast("Please enter expiration date!");
                        return;
                    } else if (TextUtils.isEmpty(fistNameStr)) {
                        showToast("Please enter first name!");
                        return;
                    } else if (TextUtils.isEmpty(addressLine1Str)) {
                        showToast("Please enter address1!");
                        return;
                    } else if (TextUtils.isEmpty(mTwoLetterIso)) {
                        showToast("Please select country!");
                        return;
                    } else if (TextUtils.isEmpty(stateStr)) {
                        showToast("Please enter state/province/region!");
                        return;
                    } else if (TextUtils.isEmpty(cityStr)) {
                        showToast("Please enter city!");
                        return;
                    } else if (TextUtils.isEmpty(postCodeStr)) {
                        showToast("Please enter zip/postal code!");
                        return;
                    } else if (TextUtils.isEmpty(phoneStr)) {
                        showToast("Please enter phone number!");
                        return;
                    }
//                    creditCardPay.setOrderid("201507153622");//201507153622，201507153573
                    PageLoadDialog.showDialogForLoading(this, false, false);

                    // 统计支付
                    MobclickAgent.onEvent(this, String.valueOf(CustomerEvent.filter_pay_clicked), ConstantsUtils.mVersionAnalystics);

                    if (null != mOrderDetail) {
                        //根据传过来的OrderDetail的Bean获取订单ID
                        String orderId = mOrderDetail.getOrderId();
                        creditCardPay.setOrderid(orderId);
//                      creditCardPay.setCardid("4392260019927209");
                        creditCardPay.setCardid(cardNumberStr);
                        creditCardPay.setMonth(monthStr);
                        creditCardPay.setYear(year);
                        creditCardPay.setCvv(securityCodeStr);
                        creditCardPay.setFirst(fistNameStr);
                        creditCardPay.setLast(lastNameStr);
                        creditCardPay.setCity(cityStr);
                        creditCardPay.setLine1(addressLine1Str);
                        creditCardPay.setLine2(addressLine2Str);
                        creditCardPay.setState(stateStr);
                        creditCardPay.setPostCode(postCodeStr);
                        creditCardPay.setCountry(mTwoLetterIso);
                        creditCardPay.setPhone(phoneStr);

                        ILog.i(TAG, "mOrderId=" + orderId + ",cardNumberStr=" + cardNumberStr + ",monthStr=" + monthStr + ",year=" + year + ",securityCodeStr=" + securityCodeStr +
                                ",NameStr=" + fistNameStr + lastNameStr + ",cityStr=" + cityStr + ",addressLine1Str=" + addressLine1Str + ",addressLine2Str" + addressLine2Str
                                + ",stateStr=" + stateStr + ",postCodeStr=" + postCodeStr + ",mTwoLetterIso=" + mTwoLetterIso + ",phoneStr=" + phoneStr);
                    /**
                     * 联网进行信用卡支付
                     */
                      controller.getServiceManager().getPaymentService().creditCardPay(creditCardPay,this);
                    }
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE_CHOOSE_COUNTRY){
            if(resultCode == ConstantsUtils.SELECT_COUNTRY_RESULT_CODE){
                String countryName = data.getStringExtra("countryname");
                mTwoLetterIso = data.getStringExtra("twoLetterIso");
                mChooseCountryText.setText(countryName);//设置国家的文本名称
                ILog.i(TAG, "countryName="+countryName + "，mTwoLetterIso=" + mTwoLetterIso);
            }
        }
        if(requestCode == ConstantsUtils.REQUEST_CODE_PAYMENT){
            if (resultCode == Activity.RESULT_OK) {
                PaymentConfirmation confirm = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                if (confirm != null) {
                    try {
                        String msg = confirm.toJSONObject().toString(4).trim();
                        Gson gson = new Gson();
                        PayPalEntity entity = gson.fromJson(msg, new TypeToken<PayPalEntity>() {}.getType());
                        final String payid = entity.response.id;
//                        String paynowId = createOrderBean.getPaynowId();
                        String orderId = mOrderDetail.getOrderId();
                        PageLoadDialog.showDialogForLoading(this, true,true);
//                        PageLoadDialog.showDialogForLoading(this, false,false);
                        /**
                         * 从Paypal获取支付后的相关验证信息
                         */
                        controller.getServiceManager().getPaymentService().verifyPayment("", orderId, payid, token, this);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            } else {
                if (resultCode == Activity.RESULT_CANCELED) {
                    ILog.i(TAG, "The user canceled.");
                } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
                    ILog.i(TAG,"An invalid Payment or PayPalConfiguration was submitted. Please see the docs.");
                }
                finish();
            }
        }
    }

    private PayPalPayment getThingToBuy(String paymentIntent) {
        float totalPrice = mOrderDetail.getTotalPrice();// 金额总数
        String orderId = mOrderDetail.getOrderId();//支付标记就是订单ID
        BigDecimal bigDecimal = new BigDecimal(totalPrice);
        PayPalPayment payPalPayment = new PayPalPayment(bigDecimal, // 金额总数
                CacheUtils.getString(this, "currencyCode", "USD"), // 货币
                getString(R.string.purchase_title), // 页面标题
                paymentIntent)//卖东西，支付给商家的意思
                .invoiceNumber(orderId);//支付标记就是订单ID
        return payPalPayment;
    }

    public static void startOrderDetailActivity(Context context,OrderDetail orderDetail){
        Intent intent = new Intent(context,PaymentMyMethodActivity.class);
        intent.putExtra("orderDetail",orderDetail);
        context.startActivity(intent);
    }

    /*@Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
           isFinishing();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected boolean isFinish() {
        // 更新未付款订单的数量的显示
        long unpaidOrderNum = CacheUtils.getLong(this, "unpaidOrderNum");
        unpaidOrderNum++;
        CacheUtils.setLong(this,"unpaidOrderNum",unpaidOrderNum);
        controller.postUnpaidOrderCountCallback((int) unpaidOrderNum);
        return super.isFinish();
    }*/

    @Override
    public void onDestroy() {
        stopService(new Intent(this, PayPalService.class));
        super.onDestroy();
    }

    @Override
    public void serviceSuccess(ServiceListener.ActionTypes action, Object bandObj, Object returnObj) {
        switch (action) {
            case PAY_SUCCESS:
                PaypalSuccess paypalSuccess = (PaypalSuccess)returnObj;
                int payStatus = paypalSuccess.getPayStatus();//支付状态，2支付成功，1待审核，0支付不成功
                switch (payStatus) {
                    case 2://支付成功，跳转到Success to pay
                    case 1://待审核，跳转到Pending
                        Intent intentSuccess = new Intent(this, PaySuccessOrPayPendingActivity.class);
                        intentSuccess.putExtra("paypalSuccess", paypalSuccess);
                        startActivity(intentSuccess);
                        break;
                    case 0://支付不成功,跳转到myOrder
                        Intent intentFailure = new Intent(this, OrdersActivity.class);
                        startActivity(intentFailure);
                        break;
                    default:
                        break;
                }
                PageLoadDialog.hideDialogForLoading();// 关闭对话框
//                controller.postOrderListCallback();//刷新myOrder界面
                controller.postOrderListCallback();
                finish();
                break;
            case PAYMENT_TOKEN:
                token = (String) returnObj;
                PayPalPayment thingToBuy = getThingToBuy(PayPalPayment.PAYMENT_INTENT_SALE);
                Intent intent = new Intent(PaymentMyMethodActivity.this, PaymentActivity.class);
                intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, ConstantsUtils.config);
                intent.putExtra(PaymentActivity.EXTRA_PAYMENT, thingToBuy);
                startActivityForResult(intent, ConstantsUtils.REQUEST_CODE_PAYMENT);
                PageLoadDialog.hideDialogForLoading();// 关闭对话框
                break;
            default:
                break;
        }
    }

    @Override
    public void serviceFailure(ServiceListener.ActionTypes action, Object returnObj, int errorCode) {
        switch (action) {
            case PAY_SUCCESS:
                // 更新未付款订单的数量的显示
                long unpaidOrderNum = CacheUtils.getLong(this, "unpaidOrderNum");
                unpaidOrderNum++;
                controller.postUnpaidOrderCountCallback((int) unpaidOrderNum);
                break;
            default:
                break;
        }
        if(returnObj != null){
            showToast(returnObj.toString());
        }else{
            showToast("Server Error!");
        }
        PageLoadDialog.hideDialogForLoading();// 关闭对话框
    }

    @Override
    public void serviceCallback(ServiceListener.ActionTypes action, int type, Object returnObj) {

    }
}

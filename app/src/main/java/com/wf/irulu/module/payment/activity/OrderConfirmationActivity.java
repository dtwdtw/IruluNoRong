package com.wf.irulu.module.payment.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;
import com.wf.irulu.IruluApplication;
import com.wf.irulu.R;
import com.wf.irulu.common.base.CommonTitleBaseActivity;
import com.wf.irulu.common.bean.CheckoutBean;
import com.wf.irulu.common.bean.CreateOrder;
import com.wf.irulu.common.bean.OrderDetail;
import com.wf.irulu.common.bean.OrderDetailProduct;
import com.wf.irulu.common.bean.ProductCart;
import com.wf.irulu.common.bean.ShippingAddrBean;
import com.wf.irulu.common.bean.ShoppingCart;
import com.wf.irulu.common.utils.CacheUtils;
import com.wf.irulu.common.utils.ConstantsUtils;
import com.wf.irulu.common.utils.CustomerEvent;
import com.wf.irulu.common.utils.StringUtils;
import com.wf.irulu.common.utils.UIUtils;
import com.wf.irulu.common.view.NoScrollGridView;
import com.wf.irulu.common.view.PageLoadDialog;
import com.wf.irulu.logic.listener.ServiceListener;
import com.wf.irulu.module.payment.adapter.OrderConfirmAdapter;
import com.wf.irulu.module.payment.dialog.ShipmentRestrictedDialog;
import com.wf.irulu.module.user.activity.AddNewAddressActivity;

import java.util.ArrayList;

/**
 * @描述: 订单确认
 * @项目名: irulu1.2
 * @包名:com.wf.irulu.module.payment.activity
 * @类名:OrderConfirmationActivity
 * @作者: 左杰
 * @创建时间:2015/11/16 14:35
 */
public class OrderConfirmationActivity extends CommonTitleBaseActivity implements ServiceListener {

    private final String TAG = getClass().getCanonicalName();
    private TextView order_promotional_savings_item;
    private TextView order_total;
    private EditText orderconfirm_et_code;
    private Button orderconfir_bt_apply;
    private RelativeLayout order_new_address;// 添加一个新的地址
    private TextView orderconfirm_tv_message;// 收件人地址
    private TextView orderconfirm_tv_name;// 收件人姓名
    private NoScrollGridView mOrderGridView;
    private String token;
    private String addressId;
    private Button placeOrder;
    private TextView orderconfirm_tv_codetip;
    private TextView product_tv_pricetotal = null;
    private TextView item_shipping_item = null;

    private ShippingAddrBean mShippingAddrBean;// 默认地址bean
    private CheckoutBean mCheckoutBean;// 生成订单Bean
    private ScrollView order_sv_detail;
    private OrderConfirmAdapter mAdapter;
    /**
     * $符号
     */
    public final String priceSymbol = "$ ";
    private ShipmentRestrictedDialog mDialog;
    private String mTag = "";
    /**
     * 手机专享价选项布局
     */
    private LinearLayout mAppExclusiveLayout;
    /**
     * 手机专享价选项图标
     */
    private ImageView mAppExclusiveImg;
    /**
     * 判断是否选中
     */
    private boolean isChecked = false;
    private TextView mAppExclusiveExt;

    @Override
    protected String setWindowTitle() {
        return "Order Confirmation";
    }

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_order_confirmation);
    }

    @Override
    public void initView() {
        order_total = (TextView) findViewById(R.id.order_total);// Order Total
        product_tv_pricetotal = (TextView) findViewById(R.id.product_tv_pricetotal);// Items Total
        placeOrder = (Button) findViewById(R.id.place_order);
        mOrderGridView = (NoScrollGridView) findViewById(R.id.order_gridview);
        order_sv_detail = (ScrollView) findViewById(R.id.order_sv_detail);
        orderconfirm_tv_name = (TextView) findViewById(R.id.orderconfirm_tv_name);
        orderconfirm_tv_codetip = (TextView) findViewById(R.id.orderconfirm_tv_codetip);
        orderconfirm_tv_message = (TextView) findViewById(R.id.orderconfirm_tv_message);
        order_promotional_savings_item = (TextView) findViewById(R.id.order_promotional_savings_item);
        item_shipping_item = (TextView) findViewById(R.id.item_shipping_item);
        order_new_address = (RelativeLayout) findViewById(R.id.order_new_address);
        orderconfirm_et_code = (EditText) findViewById(R.id.orderconfirm_et_code);
        orderconfir_bt_apply = (Button) findViewById(R.id.orderconfir_bt_apply);
        mAppExclusiveLayout = (LinearLayout) findViewById(R.id.app_exclusive_layout);//手机专享价选项布局
        mAppExclusiveImg = (ImageView) findViewById(R.id.app_exclusive_img);//手机专享价选项图标
        mAppExclusiveExt = (TextView) findViewById(R.id.app_exclusive_txt);//手机专享价选项文本
        order_new_address.setOnClickListener(this);
        mAppExclusiveLayout.setOnClickListener(this);
    }

    @Override
    public void initData() {
        Intent intent = getIntent();
        mTag = intent.getStringExtra("TAG");
        mShippingAddrBean = (ShippingAddrBean) intent.getParcelableExtra("shippingAddrBean");
        setAddress(mShippingAddrBean);

        mCheckoutBean = (CheckoutBean) intent.getParcelableExtra("checkoutBean");
        /********是否是限送 start*************/
//        restrictionsDelivery(mCheckoutBean);
        /********是否是限送 end*************/
        if (mCheckoutBean != null) {
            refreshData();
            /**
             * 让程序一进来就定位到(0,0);
             */
            mOrderGridView.post(new Runnable() {
                @Override
                public void run() {
                    order_sv_detail.scrollTo(0, 0);
                }
            });
        } else {
            showToast("failed connecting!");
//            Toast.makeText(this, "failed connecting!", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 设置地址显示信息
     */
    private void setAddress(ShippingAddrBean shippingAddrBean) {
        if (shippingAddrBean == null) {
            orderconfirm_tv_name.setText("Add a New Address");
            orderconfirm_tv_message.setVisibility(View.GONE);
            return;
        }
        addressId = shippingAddrBean.getId();
        if (TextUtils.isEmpty(addressId)) {
            orderconfirm_tv_name.setText("Add a New Address");
            orderconfirm_tv_message.setVisibility(View.GONE);
        } else {
            orderconfirm_tv_name.setText(shippingAddrBean.getFirstName() + " " + shippingAddrBean.getLastName());
            orderconfirm_tv_message.setVisibility(View.VISIBLE);
            StringBuilder sb = new StringBuilder();
            if (!TextUtils.isEmpty(shippingAddrBean.getAddress1())) {
                sb.append(shippingAddrBean.getAddress1());
                sb.append(ConstantsUtils.ENTER);
            }
            if (!TextUtils.isEmpty(shippingAddrBean.getAddress2())) {
                sb.append(shippingAddrBean.getAddress2());
                sb.append(ConstantsUtils.ENTER);
            }
            if (!TextUtils.isEmpty(shippingAddrBean.getCity())) {
                sb.append(shippingAddrBean.getCity());
                sb.append(",");
            }
            if (!TextUtils.isEmpty(shippingAddrBean.getState())) {
                sb.append(shippingAddrBean.getState());
                sb.append(",");
            }

            if (!TextUtils.isEmpty(shippingAddrBean.getCountry())) {
                sb.append(shippingAddrBean.getCountry());
                sb.append(",");
            }
            if (!TextUtils.isEmpty(shippingAddrBean.getZipCode())) {
                sb.append(shippingAddrBean.getZipCode());
            }
            sb.append(ConstantsUtils.ENTER);
            if (!TextUtils.isEmpty(shippingAddrBean.getPhone())) {
                sb.append("Phone : ");
                sb.append(shippingAddrBean.getPhone());
            }
            orderconfirm_tv_message.setText(sb.toString());
        }
    }

    /**
     * 限制送货处理方法
     *
     * @param checkoutBean
     */
    private void restrictionsDelivery(CheckoutBean checkoutBean) {
        if (null != checkoutBean) {
            ArrayList<OrderDetail> orderDetailList = checkoutBean.getList();
            int orderDetailSize = orderDetailList.size();
            boolean isCanNotSendTag = false;
            String msg = "";
            int isCanNotSend = 0;
            for (int i = 0; i < orderDetailSize; i++) {
                ArrayList<OrderDetailProduct> productList = orderDetailList.get(i).getProductList();
                int productListSize = productList.size();
                for (int j = 0; j < productListSize; j++) {
                    isCanNotSend = productList.get(j).getIsCanNotSend();//是否限售
                    if (isCanNotSend == 1) {
                        isCanNotSendTag = true;
                        msg = productList.get(j).getMsg();//限售消息提示
                    }
                }
            }
//            if(isCanNotSendTag){
//                UIUtils.getToastLong(this, msg);
//            }
        }
    }

    /**
     * 获取实体信息刷新布局
     */
    private void refreshData() {
        if (mCheckoutBean == null) {
            return;
        }
        if (mAdapter == null) {
            //Products的Adapter
            mAdapter = new OrderConfirmAdapter(mCheckoutBean.getList(), this);
            mOrderGridView.setAdapter(mAdapter);
        } else {
            mAdapter.setOrders(mCheckoutBean.getList());
        }
        String discountMsg = mCheckoutBean.getDiscountMsg();//手机专享价选项文本
        mAppExclusiveExt.setText(discountMsg);//设置手机专享价选项折文本
        float total = mCheckoutBean.getTotal();

        float shippingFee = 0.0f;
        float productFee = 0.0f;
        float savings = 0.0f;
        ArrayList<OrderDetail> checkoutBeanList = mCheckoutBean.getList();
        for (OrderDetail detail : checkoutBeanList) {
            shippingFee += detail.getShipping();
            productFee += detail.getSumPrice();
            savings += detail.getSavings();
        }
        order_total.setText(StringUtils.getPriceByFormat(total));//Order Total
        product_tv_pricetotal.setText("  " + StringUtils.getPriceByFormat(productFee));//Items Total
        item_shipping_item.setText("+" + StringUtils.getPriceByFormat(shippingFee));//运费
        order_promotional_savings_item.setText("- " + StringUtils.getPriceByFormat(savings));//手机专享价
        order_total.setOnClickListener(this);
        orderconfir_bt_apply.setOnClickListener(this);
        placeOrder.setOnClickListener(this);
        if (mCheckoutBean.getCouponInfo() == null || TextUtils.isEmpty(mCheckoutBean.getCouponInfo().getMsg())) {//不显示优惠码信息
            orderconfirm_tv_codetip.setVisibility(View.GONE);
        } else {//显示优惠码信息
            orderconfirm_tv_codetip.setVisibility(View.VISIBLE);
            orderconfirm_tv_codetip.setText(mCheckoutBean.getCouponInfo().getMsg());
            if (mCheckoutBean.getCouponInfo().getStatus() == 1) {
                orderconfirm_tv_codetip.setTextColor(getResources().getColor(R.color.irulu_reply));//成功
            } else {
                orderconfirm_tv_codetip.setTextColor(getResources().getColor(R.color.tab_selcted));//失败
            }
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.place_order://Place Order按钮
                if (mShippingAddrBean == null || TextUtils.isEmpty(mShippingAddrBean.getId())) {
                    showToast("Please enter shipping address!");
                    return;
                }
                /***************************是否是限送 starts*******************************/
                ArrayList<OrderDetail> orderDetailList = mCheckoutBean.getList();
                int orderDetailSize = orderDetailList.size();
                boolean isCanNotSendTag = false;
                boolean isOnlyOne = false;//是否只有一个
                String msg = "";
                int isCanNotSend = 0;
                ArrayList<OrderDetailProduct> orderDetailProducttList = new ArrayList<OrderDetailProduct>();
                for (int i = 0; i < orderDetailSize; i++) {
                    ArrayList<OrderDetailProduct> productList = orderDetailList.get(i).getProductList();
                    if (productList.size() == 1) {//如果只有一个商品,只Toast一下
                        msg = productList.get(0).getMsg();
                        isOnlyOne = true;
                    }
                    int productListSize = productList.size();
                    for (int j = 0; j < productListSize; j++) {
                        OrderDetailProduct orderDetailProduct = productList.get(j);
                        isCanNotSend = productList.get(j).getIsCanNotSend();//是否限售
                        if (isCanNotSend == 1) {
                            isCanNotSendTag = true;
                            orderDetailProducttList.add(orderDetailProduct);
                        }
                    }
                }
                String coupon = orderconfirm_et_code.getText().toString().trim();//优惠券码
                if (isOnlyOne && isCanNotSendTag) {
                    showToast(msg);
                } else if (isCanNotSendTag) {
                    mDialog = new ShipmentRestrictedDialog(this);
                    mDialog.setListener(orderDetailProducttList, this);
                    mDialog.show();
                } else {
                    // 显示加载进度对话框
                    PageLoadDialog.showDialogForLoading(this, true, false);
                    String mobile = "1";//是否使用促销 0：否、1：是
                    if (isChecked) {
                        mobile = "0";
                    }
                    if (TextUtils.isEmpty(mTag)) {
                        // 请求服务器创建订单
                        controller.getServiceManager().getOrderService().createOrder(null, null, null, coupon, mobile, addressId, this);
                    } else {//是从商品详情页面过来的
                        OrderDetailProduct productInfo = mCheckoutBean.getList().get(0).getProductList().get(0);
                        int productId = productInfo.getProductId();//商品id
                        int skuId = productInfo.getId();//Sku ID
                        int quantity = productInfo.getQuantity();//购买数量
                        // 请求服务器BUY NOW生成订单(调用的是/createorder/buynow接口)
                        controller.getServiceManager().getOrderService().createOrder(String.valueOf(productId), String.valueOf(skuId), String.valueOf(quantity), coupon, mobile, addressId, this);
//                        controller.getServiceManager().getOrderService().buyNow(productId,skuId, String.valueOf(quantity),coupon,addressId, this);
                    }
                    // 统计生成订单的点击量
                    MobclickAgent.onEvent(this, String.valueOf(CustomerEvent.filter_create_order_clicked), ConstantsUtils.mVersionAnalystics);

                }
                /***************************是否是限送 end*******************************/

                break;
            case R.id.order_new_address:
                if (mShippingAddrBean == null || TextUtils.isEmpty(mShippingAddrBean.getId())) {
                    Intent intent = new Intent(OrderConfirmationActivity.this, AddNewAddressActivity.class);
                    intent.putExtra("flags", "OrderConfirmationActivity");
                    startActivityForResult(intent, ConstantsUtils.REQUEST_CODE_ADDRESS);
                } else {
                    Intent intent = new Intent(OrderConfirmationActivity.this, ChooseAddressActivity.class);
                    intent.putExtra("addrId", mShippingAddrBean.getId());
                    startActivityForResult(intent, ConstantsUtils.REQUEST_CODE_ADDRESS);
                }
                break;
            case R.id.orderconfir_bt_apply:
                if (TextUtils.isEmpty(orderconfirm_et_code.getText())) {
                    showToast(R.string.enter_promotional_codes);
                    return;
                }
                if (mShippingAddrBean == null || TextUtils.isEmpty(mShippingAddrBean.getId())) {
                    showToast("Please enter shipping address!");
                    return;
                }
                long last_time = 0;
                if (last_time != 0 && System.currentTimeMillis() - last_time < 1000 * 5) {
                    return;
                }
                String addressId = "";
                if (!TextUtils.isEmpty(mShippingAddrBean.getId())) {
                    addressId = mShippingAddrBean.getId();
                }

                String mobile = "1";//是否使用促销 0：否、1：是
                if (isChecked) {
                    mobile = "0";
                }
                String promotionalCodes = orderconfirm_et_code.getText().toString().trim();//优惠码
                PageLoadDialog.showDialogForLoading(this, true, false);
                if (TextUtils.isEmpty(mTag)) {
                    /**
                     * 联网请求Check Now
                     */
                    controller.getServiceManager().getOrderService().checkout(promotionalCodes, addressId, mobile, this);
                } else {//是从商品详情页面过来的
                    OrderDetailProduct productInfo = mCheckoutBean.getList().get(0).getProductList().get(0);
                    int productId = productInfo.getProductId();//商品id
                    int skuId = productInfo.getId();//Sku ID
                    int quantity = productInfo.getQuantity();//购买数量
                    coupon = orderconfirm_et_code.getText().toString().trim();//优惠券码
                    // 请求服务器buyNow
                    controller.getServiceManager().getOrderService().buyNow(String.valueOf(productId), String.valueOf(skuId), String.valueOf(quantity), coupon, addressId, mobile, this);
                }
                break;
            case R.id.shipment_restricted_remove://删除限购商品
                PageLoadDialog.showDialogForLoading(this, true, false);
                orderDetailList = mCheckoutBean.getList();
                StringBuilder builder = new StringBuilder();
                int size = orderDetailList.size();
                for (int i = 0; i < size; i++) {
                    ArrayList<OrderDetailProduct> productList = orderDetailList.get(i).getProductList();
                    int productListSize = productList.size();
                    for (int j = 0; j < productListSize; j++) {
                        OrderDetailProduct productInfo = productList.get(j);
                        int isCanNotSend1 = productInfo.getIsCanNotSend();//是否限售
                        if (isCanNotSend1 == 1) {
                            int id = productInfo.getId();
                            builder.append("," + id);
                        }
                    }
                }
                StringBuilder replace = builder.replace(0, 1, "");//将第一个逗号给去掉
                /**
                 * 联网请求删除商品
                 */
                controller.getServiceManager().getShoppingService().removeProduct("", replace.toString(), this);
                break;
            case R.id.app_exclusive_layout:
                if (isChecked) {//选中
                    mAppExclusiveImg.setImageResource(R.mipmap.round_button_selected);
//                    refreshData();
                    isChecked = !isChecked;
                } else {//未选中
//                    int discountPrecentInt = mCheckoutBean.getDiscountPrecent();//手机专享价选项折扣比(是个整数)
//                    float discountPrecent = discountPrecentInt/100f;//百分比
//
//                    float shippingFee = 0.0f;
//                    float productFee = 0.0f;
//                    float savings = 0.0f;
//                    ArrayList<OrderDetail> checkoutBeanList = mCheckoutBean.getList();
//                    for (OrderDetail detail : checkoutBeanList) {
//                        shippingFee += detail.getShipping();
//                        productFee += detail.getSumPrice();
//                        savings += detail.getSavings();
//                    }
//
//                    float saving = savings-productFee * discountPrecent;
//                    order_promotional_savings_item.setText("- " + StringUtils.getPriceByFormat(saving));//手机专享价
//
//                    float total = productFee+shippingFee-saving;
//                    order_total.setText(StringUtils.getPriceByFormat(total));//Order Total
                    mAppExclusiveImg.setImageResource(R.mipmap.round_button_normal);
                    isChecked = !isChecked;
                }
                addressId = "";
                if (mShippingAddrBean == null || TextUtils.isEmpty(mShippingAddrBean.getId())) {
                    showToast("Please enter shipping address!");
                    return;
                }
                if (!TextUtils.isEmpty(mShippingAddrBean.getId())) {
                    addressId = mShippingAddrBean.getId();
                }

                PageLoadDialog.showDialogForLoading(this, true, false);
                mobile = "1";//是否使用促销 0：否、1：是
                if (isChecked) {
                    mobile = "0";
                }
                coupon = orderconfirm_et_code.getText().toString().trim();//优惠券码
                if (TextUtils.isEmpty(mTag)) {
                    //请求网络Check NOW,一边根据国家修改运费
                    controller.getServiceManager().getOrderService().checkout(coupon, addressId, mobile, this);
                } else {//是从商品详情页面过来的
                    OrderDetailProduct productInfo = mCheckoutBean.getList().get(0).getProductList().get(0);
                    int productId = productInfo.getProductId();//商品id
                    int skuId = productInfo.getId();//Sku ID
                    int quantity = productInfo.getQuantity();//购买数量
                    //请求网络buyNow,一边根据国家修改运费
                    controller.getServiceManager().getOrderService().buyNow(String.valueOf(productId), String.valueOf(skuId), String.valueOf(quantity), coupon, addressId, mobile, this);

                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case ConstantsUtils.REQUEST_CODE_ADDRESS: {
                if (data == null) {
                    return;
                }
                mShippingAddrBean = (ShippingAddrBean) data.getParcelableExtra("ShippingAddrBean");
                String mobile = "1";//是否使用促销 0：否、1：是
                if (isChecked) {
                    mobile = "0";
                }
                if (null != mShippingAddrBean) {
                    //设置地址显示信息
                    setAddress(mShippingAddrBean);
                    addressId = mShippingAddrBean.getId();
                    PageLoadDialog.showDialogForLoading(this, true, false);
                    String coupon = orderconfirm_et_code.getText().toString().trim();//优惠券码
                    if (TextUtils.isEmpty(mTag)) {
                        //请求网络Check NOW,一边根据国家修改运费
                        controller.getServiceManager().getOrderService().checkout(coupon, addressId, mobile, this);
                    } else {//是从商品详情页面过来的
                        OrderDetailProduct productInfo = mCheckoutBean.getList().get(0).getProductList().get(0);
                        int productId = productInfo.getProductId();//商品id
                        int skuId = productInfo.getId();//Sku ID
                        int quantity = productInfo.getQuantity();//购买数量
                        //请求网络buyNow,一边根据国家修改运费
                        controller.getServiceManager().getOrderService().buyNow(String.valueOf(productId), String.valueOf(skuId), String.valueOf(quantity), coupon, addressId, mobile, this);

                    }
                }
            }
            break;
            default:
                break;
        }
    }

    @Override
    public void serviceSuccess(ActionTypes action, Object bandObj, Object returnObj) {
        switch (action) {
            case PRODUCT_CHECKOUT:
                mCheckoutBean = (CheckoutBean) returnObj;
                /********是否是限送 start*************/
//                restrictionsDelivery(mCheckoutBean);
                /********是否是限送 end*************/
                refreshData();
                if (null != mDialog) {
                    mDialog.dismiss();
                }
                PageLoadDialog.hideDialogForLoading();// 关闭对话框
                break;
            case CREATE_ORDER:
                CreateOrder createOrder = (CreateOrder) returnObj;

                if (TextUtils.isEmpty(mTag)) {//从购物车过来的
                    //清空购物车
                    controller.postShoppingCartEmptyCallback(null);
                    //清空购物车的数量
                    CacheUtils.setLong(OrderConfirmationActivity.this, "cartNum", 0);
                    controller.postShoppongCartCountCallback(0);
                }

                //根据传过来的CreateOrderBean获取订单ID
                ArrayList<OrderDetail> orderDetailList = createOrder.getList();
                OrderDetail orderDetail = orderDetailList.get(0);
                int paystyle = CacheUtils.getInt(IruluApplication.getInstance(), "nativePaymentEnabled");
                if (paystyle == 1) {
                    PaymentMyMethodActivity.startOrderDetailActivity(OrderConfirmationActivity.this, orderDetail);
                } else {
                    WebPaymentActivity.startWebPaymentActivity(OrderConfirmationActivity.this, orderDetail);
                }
                PageLoadDialog.hideDialogForLoading();// 关闭对话框

                controller.postOrderListCallback();//刷新myOrder界面
                finish();
                break;
            case CART_REMOVE_PRODUCT:
                ShoppingCart shoppingCart = (ShoppingCart) returnObj;
                ArrayList<ProductCart> productList = shoppingCart.shoppingList.getProductList();
                String mobile = "1";//是否使用促销 0：否、1：是
                if (isChecked) {
                    mobile = "0";
                }
                //刷新购物车
                controller.postShoppingCartEmptyCallback(productList);
                controller.getServiceManager().getOrderService().checkout("", addressId, mobile, this);
                break;
            case PRODUCT_BUYNOW:
                mCheckoutBean = (CheckoutBean) returnObj;

                refreshData();
                PageLoadDialog.hideDialogForLoading();// 关闭对话框
                break;
            default:
                break;
        }
        /*if(null != mCheckoutBean){
            float savings = 0.0f;
            ArrayList<OrderDetail> checkoutBeanList = mCheckoutBean.getList();
            for (OrderDetail detail : checkoutBeanList) {
                savings += detail.getSavings();
            }
            if(savings > 0){
                mAppExclusiveImg.setImageResource(R.mipmap.round_button_selected);
            }else{
                mAppExclusiveImg.setImageResource(R.mipmap.round_button_normal);
            }
        }*/
    }

    @Override
    public void serviceFailure(ActionTypes action, Object returnObj, int errorCode) {
        switch (action) {
            case CREATE_ORDER:
                PageLoadDialog.hideDialogForLoading();// 关闭对话框
                break;
            case PRODUCT_BUYNOW:
            case PRODUCT_CHECKOUT:
                PageLoadDialog.hideDialogForLoading();// 关闭对话框
                break;
            default:
                break;
        }
        if (returnObj != null) {
            UIUtils.getToastShort(this, returnObj.toString());
        } else {
            UIUtils.getToastShort(this, "Server Error!");
        }
    }

    @Override
    public void serviceCallback(ActionTypes action, int type, Object returnObj) {

    }
}

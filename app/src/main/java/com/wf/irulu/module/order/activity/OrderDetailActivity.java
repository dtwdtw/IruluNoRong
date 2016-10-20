package com.wf.irulu.module.order.activity;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.wf.irulu.IruluApplication;
import com.wf.irulu.R;
import com.wf.irulu.common.base.CommonTitleBaseActivity;
import com.wf.irulu.common.bean.LogisticsInfo;
import com.wf.irulu.common.bean.OrderDetail;
import com.wf.irulu.common.bean.OrderDetailProduct;
import com.wf.irulu.common.bean.Product;
import com.wf.irulu.common.bean.ShippingAddrBean;
import com.wf.irulu.common.utils.CacheUtils;
import com.wf.irulu.common.utils.ConstantsUtils;
import com.wf.irulu.common.utils.DateFormatUtils;
import com.wf.irulu.common.utils.StringUtils;
import com.wf.irulu.common.view.DeleteDialog;
import com.wf.irulu.common.view.NoScrollListView;
import com.wf.irulu.logic.listener.DeleteDialogListener;
import com.wf.irulu.logic.listener.ServiceListener;
import com.wf.irulu.module.comment.activity.MyReviewsActivity;
import com.wf.irulu.module.order.adapter.OrderDetailProductAdapter;
import com.wf.irulu.module.order.dialog.LoadingDialog;
import com.wf.irulu.module.order.listener.IOrderslListener;
import com.wf.irulu.module.payment.activity.PaymentMyMethodActivity;
import com.wf.irulu.module.payment.activity.WebPaymentActivity;
import com.wf.irulu.module.product.activity.ProductDetailActivity;

public class OrderDetailActivity extends CommonTitleBaseActivity implements ServiceListener, IOrderslListener {

    private ScrollView mDetailSvParent;
    private TextView mDetailTvId, mDetailTvDate, mDetailTvStatus;
    private TextView mDetailTvName, mDetailTvAddress;
    private NoScrollListView mDetailNslvProducts;
    private TextView mDetailTvTotal, mDetailTvShipping, mDetailTvPromotion, mDetailTvPrice;
    private Button mDetailBt01, mDetailBt02;
    private RelativeLayout mDetailRlTrackingMore;
    private LinearLayout mDetailLlTracking;
    private TextView mDetailTvTracking, mDetailTvTrackingDate;
    private String mOId;
    private OrderDetail mOrderDetail;
    /**
     * 进度加载框
     */
    private LoadingDialog mLoadingDialog;

    @Override
    public void setContentView() {
//        setContentView(R.layout.activity_order_detail);
        setContentView(R.layout.loading_simple_waiting);
    }

    @Override
    public void initView() {
        mOId = getIntent().getStringExtra("OId");
    }

    @Override
    public void initDataView() {
        controller.registerIOrderList(this);
        mDetailSvParent = bindView(R.id.detail_sv_parent);
        mDetailTvId = bindView(R.id.detail_tv_id);
        mDetailTvDate = bindView(R.id.detail_tv_date);
        mDetailTvStatus = bindView(R.id.detail_tv_status);
        mDetailTvName = bindView(R.id.detail_tv_name);
        mDetailTvAddress = bindView(R.id.detail_tv_address);
        mDetailTvTotal = bindView(R.id.detail_tv_total);
        mDetailTvShipping = bindView(R.id.detail_tv_shipping);
        mDetailTvPromotion = bindView(R.id.detail_tv_promotion);
        mDetailTvTracking = bindView(R.id.detail_tv_tracking);
        mDetailTvTrackingDate = bindView(R.id.detail_tv_tracking_date);
        mDetailTvPrice = bindView(R.id.detail_tv_price);
        mDetailBt01 = bindView(R.id.detail_bt_01);
        mDetailBt02 = bindView(R.id.detail_bt_02);
        mDetailRlTrackingMore = bindView(R.id.detail_rl_tracking_more);
        mDetailLlTracking = bindView(R.id.detail_ll_tracking);
        mDetailNslvProducts = bindView(R.id.detail_nslv_products);
        mLoadingDialog = new LoadingDialog(this);
    }

    @Override
    public void initData() {
        controller.getServiceManager().getOrderService().getOrderDetail(mOId, this);
    }

    @Override
    protected String setWindowTitle() {
        return getString(R.string.order_details);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.detail_bt_01:
                switch (mOrderDetail.getStatus()) {
                    case 1:
                        DeleteDialog.showDeleteDialog(this, getString(R.string.cancel_order), getString(R.string.makesure_cancle_order),
                                new DeleteDialogListener() {
                                    @Override
                                    public void deleteDialog() {
                                        controller.getServiceManager().getOrderService().cancelOrder(mOId, OrderDetailActivity.this);
                                    }
                                });
                        break;
                    case 6:
                    case 7:
                        DeleteDialog.showDeleteDialog(this, getString(R.string.delete_order), getString(R.string.makesure_delete_order),
                                new DeleteDialogListener() {
                                    @Override
                                    public void deleteDialog() {
                                        controller.getServiceManager().getOrderService().deleteOrder(mOId, OrderDetailActivity.this);
                                    }
                                });
                        break;
                    default:
                        break;
                }
                break;
            case R.id.detail_bt_02:
                switch (mOrderDetail.getStatus()) {
                    case 1:
                        // 支付
                        int paystyle = CacheUtils.getInt(IruluApplication.getInstance(), "nativePaymentEnabled");
                        if (paystyle == 1) {
                            PaymentMyMethodActivity.startOrderDetailActivity(this, mOrderDetail);
                        } else {
                            WebPaymentActivity.startWebPaymentActivity(this, mOrderDetail);
                        }
                        break;
                    case 4:
                        DeleteDialog.showDeleteDialog(this, getString(R.string.confirm_goods), getString(R.string.makesure_confirm_goods),
                                new DeleteDialogListener() {
                                    @Override
                                    public void deleteDialog() {
                                        controller.getServiceManager().getOrderService().confirmOrder(mOId, OrderDetailActivity.this);
                                    }
                                });
                        break;
                    case 7:
                        // 写评论
                        if (mOrderDetail.getProductList().size() == 1) {
                            OrderDetailProduct p = mOrderDetail.getProductList().get(0);
                            Product product = new Product();
                            product.setId(String.valueOf(p.getId()));
                            product.setImage(p.getImage());
                            product.setName(p.getName());
                            product.setPrice(p.getPrice());
                            product.setQuantity(p.getQuantity());
                            product.setSku(p.getSku());
                            MyReviewsActivity.startMyReviewsActivity(this, product, "order");
                        } else {
                            Intent intent = new Intent(this, SelectToReivewActivity.class);
                            intent.putExtra("products", mOrderDetail.getProductList());
                            startActivity(intent);
                        }
                        break;
                    default:
                        break;
                }
                break;
            case R.id.detail_rl_tracking_more:
                TrackingDetailsActivity.startTrackingDetailsActivity(this, mOId);
                break;
            default:
                break;
        }
    }

    /**
     * 加载数据显示到界面
     */
    public void addData() {
        mDetailTvId.setText(mOrderDetail.getOrderId());
        mDetailTvDate.setText(DateFormatUtils.DateFormatUrlTime(Long.parseLong(mOrderDetail.getCreateTime())));
        mDetailTvStatus.setText(ConstantsUtils.ORDER_STATUS.get(mOrderDetail.getStatus()));
        ShippingAddrBean addr = mOrderDetail.getShoppingAddr();
        mDetailTvName.setText(addr.getFirstName() + " " + addr.getLastName());
        StringBuilder addressInfo = new StringBuilder();
        if (!TextUtils.isEmpty(addr.getAddress1())) {
            addressInfo.append(addr.getAddress1()).append(ConstantsUtils.ENTER);
        }
        if (!TextUtils.isEmpty(addr.getAddress2())) {
            addressInfo.append(addr.getAddress2()).append(ConstantsUtils.ENTER);
        }
        if (!TextUtils.isEmpty(addr.getState())) {
            addressInfo.append(addr.getState()).append(ConstantsUtils.ENTER);
        }
        if (!TextUtils.isEmpty(addr.getCountry())) {
            addressInfo.append(addr.getCountry()).append(ConstantsUtils.ENTER);
        }
        if (!TextUtils.isEmpty(addr.getPhone())) {
            addressInfo.append("Phone: ").append(addr.getPhone());
        }
        mDetailTvAddress.setText(addressInfo.toString());
        mDetailTvTotal.setText(StringUtils.getPriceByFormat(mOrderDetail.getSumPrice()));
        mDetailTvShipping.setText(StringUtils.getPriceByFormat(mOrderDetail.getShipping()));
        mDetailTvPromotion.setText(StringUtils.getPriceByFormat(mOrderDetail.getSavings()));
        mDetailTvPrice.setText(StringUtils.getPriceByFormat(mOrderDetail.getTotalPrice()));
        if (mOrderDetail.getLogisticsInfo() != null && !StringUtils.isEmpty(mOrderDetail.getLogisticsInfo().getLogisticsId())) {
            LogisticsInfo info = mOrderDetail.getLogisticsInfo();
            mDetailLlTracking.setVisibility(View.VISIBLE);
            mDetailRlTrackingMore.setOnClickListener(this);
            if (info.getLogisticsDetail().size() > 0) {
                mDetailTvTracking.setText(info.getLogisticsDetail().get(info.getLogisticsDetail().size() - 1).getItemValue());
                mDetailTvTrackingDate.setText(info.getLogisticsDetail().get(info.getLogisticsDetail().size() - 1).getTime());
            } else {
                mDetailLlTracking.setVisibility(View.GONE);
            }
        } else {
            mDetailLlTracking.setVisibility(View.GONE);
        }
        mDetailNslvProducts.setAdapter(new OrderDetailProductAdapter(mOrderDetail.getProductList(), this));
        mDetailNslvProducts.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ProductDetailActivity.startProductDetailActivity(OrderDetailActivity.this, String.valueOf(mOrderDetail.getProductList().get(position).getProductId()));
            }
        });
        mDetailSvParent.post(new Runnable() {

            @Override
            public void run() {
                mDetailSvParent.scrollTo(0, 0);
            }
        });

        switch (mOrderDetail.getStatus()) {
            // 1 Unpaid 未付款
            case 1:
                mDetailBt01.setVisibility(View.VISIBLE);
                mDetailBt02.setVisibility(View.VISIBLE);
                mDetailBt01.setText(getString(R.string.cancel_order));
                mDetailBt02.setText(getString(R.string.place_order));
                mDetailBt01.setBackgroundResource(R.drawable.button_01_selector);
                mDetailBt01.setTextColor(getResources().getColor(R.color.blue_line_image));
                mDetailBt02.setBackgroundResource(R.drawable.button_02_selector);
                mDetailBt02.setTextColor(getResources().getColor(R.color.white));
                break;
            // 2 Pending 待审核
            case 2:
                mDetailBt01.setVisibility(View.GONE);
                mDetailBt02.setVisibility(View.GONE);
                break;
            // 3 Paid 已付款
            case 3:
                mDetailBt01.setVisibility(View.GONE);
                mDetailBt02.setVisibility(View.GONE);
                break;
            // 4 Shipped 已发货
            case 4:
                mDetailBt01.setVisibility(View.INVISIBLE);
                mDetailBt02.setVisibility(View.VISIBLE);
                mDetailBt02.setText(getString(R.string.confirm_goods));
                mDetailBt02.setBackgroundResource(R.drawable.button_02_selector);
                mDetailBt02.setTextColor(getResources().getColor(R.color.white));
                break;
            // 5 Frozen 交易冻结
            case 5:
                mDetailBt01.setVisibility(View.GONE);
                mDetailBt02.setVisibility(View.GONE);
                break;
            // 6 Closed 交易关闭
            case 6:
                mDetailBt01.setVisibility(View.VISIBLE);
                mDetailBt02.setVisibility(View.GONE);
                mDetailBt01.setText(getString(R.string.delete_order));
                mDetailBt01.setBackgroundResource(R.drawable.button_01_selector);
                mDetailBt01.setTextColor(getResources().getColor(R.color.blue_line_image));
                break;
            // 7 Completed 完成
            case 7:
                mDetailBt01.setVisibility(View.VISIBLE);
                mDetailBt02.setVisibility(View.VISIBLE);
                mDetailBt01.setText(getString(R.string.delete_order));
                mDetailBt01.setBackgroundResource(R.drawable.button_01_selector);
                mDetailBt01.setTextColor(getResources().getColor(R.color.blue_line_image));
                mDetailBt02.setText(getString(R.string.write_a_review));
                mDetailBt02.setBackgroundResource(R.drawable.button_01_selector);
                mDetailBt02.setTextColor(getResources().getColor(R.color.blue_line_image));
                break;
            default:
                break;
        }
        mDetailBt01.setOnClickListener(this);
        mDetailBt02.setOnClickListener(this);
        if (mLoadingDialog.isShowing() && !isFinishing()) {
            mLoadingDialog.dismiss();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        controller.unRegisterIOrderList(this);
    }

    public static void startOrderDetailActivity(Activity activity, String pOId) {
        Intent intent = new Intent(activity, OrderDetailActivity.class);
        intent.putExtra("OId", pOId);
        activity.startActivity(intent);
    }

    @Override
    public void serviceSuccess(ActionTypes action, Object bandObj, Object returnObj) {
        switch (action) {
            case ORDER_DETAIL:
                mOrderDetail = (OrderDetail) returnObj;

                if (mOrderDetail != null) {
                    refreshDataView(R.layout.activity_order_detail);
                } else {
                    displayNoDataView(R.layout.no_data_simple_title_page);
                }
                break;
            case ORDER_CANCEL:
                mLoadingDialog.dismiss();
                refresh();
                controller.postOrderListCallback();
                break;
            case ORDER_DELETE:
                mLoadingDialog.dismiss();
                controller.unRegisterIOrderList(this);
                controller.postOrderListCallback();
                finish();
                break;
            case ORDER_CONFIRM:
                mLoadingDialog.dismiss();
                refresh();
                controller.postOrderListCallback();
                break;
            default:
                break;
        }
    }

    @Override
    public void serviceFailure(ActionTypes action, Object returnObj, int errorCode) {
        switch (action) {
            case ORDER_DETAIL:
                if (returnObj != null) {
                    showToast(returnObj.toString());
                }
                displayNoDataView(R.layout.no_data_simple_title_page);
                break;
            case ORDER_CANCEL:
                if (returnObj != null) {
                    showToast(returnObj.toString());
                }
                mLoadingDialog.dismiss();
                break;
            case ORDER_DELETE:
                if (returnObj != null) {
                    showToast(returnObj.toString());
                }
                mLoadingDialog.dismiss();
                break;
            case ORDER_CONFIRM:
                if (returnObj != null) {
                    showToast(returnObj.toString());
                }
                mLoadingDialog.dismiss();
                break;
            default:
                break;
        }
    }

    @Override
    public void serviceCallback(ActionTypes action, int type, Object returnObj) {
        if (!isFinishing()) {
            boolean isNeedRefreshing = (Boolean) returnObj;
            switch (action) {
                case ORDER_DETAIL:
                    if (isNeedRefreshing) {
                        mLoadingDialog.show();
                    }
                    break;
                case ORDER_CANCEL:
                    if (isNeedRefreshing) {
                        mLoadingDialog.show();
                    }
                    break;
                case ORDER_DELETE:
                    if (isNeedRefreshing) {
                        mLoadingDialog.show();
                    }
                    break;
                case ORDER_CONFIRM:
                    if (isNeedRefreshing) {
                        mLoadingDialog.show();
                    }
                    break;
                default:
                    break;
            }
        }

    }

    @Override
    public void refresh() {
        setContentView();
        setView();
        initView();
        initData();
    }
}

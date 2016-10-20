package com.wf.irulu.module.order.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wf.irulu.IruluApplication;
import com.wf.irulu.R;
import com.wf.irulu.common.bean.OrderDetail;
import com.wf.irulu.common.bean.OrderDetailProduct;
import com.wf.irulu.common.bean.Product;
import com.wf.irulu.common.utils.CacheUtils;
import com.wf.irulu.common.utils.ConstantsUtils;
import com.wf.irulu.common.utils.DateFormatUtils;
import com.wf.irulu.common.utils.ILog;
import com.wf.irulu.common.utils.StringUtils;
import com.wf.irulu.common.view.DeleteDialog;
import com.wf.irulu.common.view.NoScrollListView;
import com.wf.irulu.logic.IruluController;
import com.wf.irulu.logic.listener.DeleteDialogListener;
import com.wf.irulu.logic.listener.ServiceListener;
import com.wf.irulu.module.comment.activity.MyReviewsActivity;
import com.wf.irulu.module.order.activity.OrdersActivity;
import com.wf.irulu.module.order.activity.SelectToReivewActivity;
import com.wf.irulu.module.payment.activity.PaymentMyMethodActivity;
import com.wf.irulu.module.payment.activity.WebPaymentActivity;

import java.util.ArrayList;

/**
 * @描述: 订单条目适配器
 * @项目名: irulu
 * @包名:com.wf.irulu.module.shopping.adapter
 * @类名:OrdersAdapter
 * @作者: Yuki
 * @创建时间:2015-7-28 下午6:35:59
 */
public class OrdersAdapter extends BaseAdapter {

    private ArrayList<OrderDetail> orders = null;
    private LayoutInflater mInflater = null;
    private Context mContext;
    private IruluController controller = null;
    private String mOrderPlaced = null;
    private ServiceListener service;
    private final int TYPE_BASE = 0;
    private final int TYPE_1 = 1;
    private final int TYPE_2 = 2;
    private final int TYPE_3 = 3;

    ViewHolderBase holderBase;
    ViewHolder1 holder1;
    ViewHolder2 holder2;
    ViewHolder3 holder3;

    public OrdersAdapter(ArrayList<OrderDetail> orders, Context mContext, ServiceListener service) {
        super();
        this.orders = orders;
        this.mContext = mContext;
        this.mInflater = LayoutInflater.from(mContext);
        this.service = service;
//		this.priceSymbol = IruluController.getInstance().getCacheManager().getPriceSymbol();
        controller = IruluController.getInstance();
        mOrderPlaced = mContext.getString(R.string.order_placed);
    }

    public void setOrders(ArrayList<OrderDetail> orders) {
        this.orders = orders;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return orders == null ? 0 : orders.size();
    }

    @Override
    public Object getItem(int position) {
        return orders.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getViewTypeCount() {
        return 4;
    }

    @Override
    public int getItemViewType(int position) {
        final OrderDetail order = (OrderDetail) getItem(position);
        int type = TYPE_BASE;

        switch (order.getStatus()) {
            case 1:
                type = TYPE_1;
                break;
            case 2:
                type = TYPE_BASE;
                break;
            case 3:
                type = TYPE_BASE;
                break;
            case 4:
                type = TYPE_2;
                break;
            case 5:
                type = TYPE_BASE;
                break;
            case 6:
                type = TYPE_2;
                break;
            case 7:
                type = TYPE_3;
                break;
        }

        return type;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final OrderDetail order = (OrderDetail) getItem(position);

        View viewBase = null;
        View view1 = null;
        View view2 = null;
        View view3 = null;

        switch (getItemViewType(position)) {
            case TYPE_BASE:
                if (null == convertView) {
                    holderBase = new ViewHolderBase();
                    viewBase = mInflater.inflate(R.layout.order_item_base, null);
                    holderBase.date = (TextView) viewBase.findViewById(R.id.order_tv_date);
                    holderBase.status = (TextView) viewBase.findViewById(R.id.order_tv_status);
                    holderBase.products = (NoScrollListView) viewBase.findViewById(R.id.order_nslv_products);
                    holderBase.count = (TextView) viewBase.findViewById(R.id.order_tv_count);
                    holderBase.total = (TextView) viewBase.findViewById(R.id.order_tv_total);
                    holderBase.total_view = (RelativeLayout) viewBase.findViewById(R.id.total_view);
                    ((ViewGroup) viewBase).setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
                    viewBase.setTag(holderBase);
                    convertView = viewBase;
                } else
                    holderBase = (ViewHolderBase) convertView.getTag();

                holderBase.date.setText(mOrderPlaced + DateFormatUtils.DateFormatUrlTime(Long.parseLong(order.getUpdateTime())));
                holderBase.status.setText(ConstantsUtils.ORDER_STATUS.get(order.getStatus()));
                holderBase.count.setText(order.getProductList().size() > 1 ? order.getProductList().size() + " items" : order.getProductList().size() + " item");
                holderBase.total.setText(StringUtils.getPriceByFormat(order.getTotalPrice()));

                if (null == holderBase.adapter) {
                    holderBase.adapter = new ProductOrderAdapter(order.getProductList(), mContext, order.getOrderId());
                    holderBase.products.setAdapter(holderBase.adapter);
                } else {
                    holderBase.adapter.setProductsOrderID(order.getProductList(), order.getOrderId());
                }

                holderBase.total_view.setBackgroundResource(R.color.white);
                break;
            case TYPE_1:
                if (null == convertView) {
                    holder1 = new ViewHolder1();
                    view1 = mInflater.inflate(R.layout.order_item1, null);
                    holder1.date = (TextView) view1.findViewById(R.id.order_tv_date);
                    holder1.status = (TextView) view1.findViewById(R.id.order_tv_status);
                    holder1.products = (NoScrollListView) view1.findViewById(R.id.order_nslv_products);
                    holder1.count = (TextView) view1.findViewById(R.id.order_tv_count);
                    holder1.total = (TextView) view1.findViewById(R.id.order_tv_total);
                    holder1.cancel_order = (Button) view1.findViewById(R.id.cancel_order);
                    holder1.pay_now = (Button) view1.findViewById(R.id.pay_now);
                    ((ViewGroup) view1).setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
                    view1.setTag(holder1);
                    convertView = view1;
                } else
                    holder1 = (ViewHolder1) convertView.getTag();

                holder1.date.setText(mOrderPlaced + DateFormatUtils.DateFormatUrlTime(Long.parseLong(order.getUpdateTime())));
                holder1.status.setText(ConstantsUtils.ORDER_STATUS.get(order.getStatus()));
                holder1.count.setText(order.getProductList().size() > 1 ? order.getProductList().size() + " items" : order.getProductList().size() + " item");
                holder1.total.setText(StringUtils.getPriceByFormat(order.getTotalPrice()));
                if (null == holder1.adapter) {
                    holder1.adapter = new ProductOrderAdapter(order.getProductList(), mContext, order.getOrderId());
                    holder1.products.setAdapter(holder1.adapter);
                } else {
                    holder1.adapter.setProductsOrderID(order.getProductList(), order.getOrderId());
                }

                holder1.cancel_order.setOnClickListener(new MyClickLisnter(position));
                holder1.pay_now.setOnClickListener(new MyClickLisnter(position));
                break;
            case TYPE_2:
                if (null == convertView) {
                    holder2 = new ViewHolder2();
                    view2 = mInflater.inflate(R.layout.order_item2, null);
                    holder2.date = (TextView) view2.findViewById(R.id.order_tv_date);
                    holder2.status = (TextView) view2.findViewById(R.id.order_tv_status);
                    holder2.products = (NoScrollListView) view2.findViewById(R.id.order_nslv_products);
                    holder2.count = (TextView) view2.findViewById(R.id.order_tv_count);
                    holder2.total = (TextView) view2.findViewById(R.id.order_tv_total);
                    holder2.btn = (Button) view2.findViewById(R.id.btn);
                    ((ViewGroup) view2).setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
                    view2.setTag(holder2);
                    convertView = view2;
                } else
                    holder2 = (ViewHolder2) convertView.getTag();

                switch (order.getStatus()) {
                    case 4:
                        holder2.btn.setText(mContext.getString(R.string.confirm_goods));
                        break;
                    case 6:
                        holder2.btn.setText(mContext.getString(R.string.delete_order));
                        break;
                }

                holder2.date.setText(mOrderPlaced + DateFormatUtils.DateFormatUrlTime(Long.parseLong(order.getUpdateTime())));
                holder2.status.setText(ConstantsUtils.ORDER_STATUS.get(order.getStatus()));
                holder2.count.setText(order.getProductList().size() > 1 ? order.getProductList().size() + " items" : order.getProductList().size() + " item");
                holder2.total.setText(StringUtils.getPriceByFormat(order.getTotalPrice()));
                if (null == holder2.adapter) {
                    holder2.adapter = new ProductOrderAdapter(order.getProductList(), mContext, order.getOrderId());
                    holder2.products.setAdapter(holder2.adapter);
                } else {
                    holder2.adapter.setProductsOrderID(order.getProductList(), order.getOrderId());
                }

                holder2.btn.setOnClickListener(new MyClickLisnter(position));
                break;
            case TYPE_3:
                if (null == convertView) {
                    holder3 = new ViewHolder3();
                    view3 = mInflater.inflate(R.layout.order_item3, null);
                    holder3.date = (TextView) view3.findViewById(R.id.order_tv_date);
                    holder3.status = (TextView) view3.findViewById(R.id.order_tv_status);
                    holder3.products = (NoScrollListView) view3.findViewById(R.id.order_nslv_products);
                    holder3.count = (TextView) view3.findViewById(R.id.order_tv_count);
                    holder3.total = (TextView) view3.findViewById(R.id.order_tv_total);
                    holder3.delete_order = (Button) view3.findViewById(R.id.delete_order);
                    holder3.write_review = (Button) view3.findViewById(R.id.write_review);
                    ((ViewGroup) view3).setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
                    view3.setTag(holder3);
                    convertView = view3;
                } else {
                    holder3 = (ViewHolder3) convertView.getTag();
                }

                holder3.date.setText(mOrderPlaced + DateFormatUtils.DateFormatUrlTime(Long.parseLong(order.getUpdateTime())));
                holder3.status.setText(ConstantsUtils.ORDER_STATUS.get(order.getStatus()));

                int count = 0;

                for (OrderDetailProduct product : order.getProductList()) {
                    count += product.getQuantity();
                }

                holder3.count.setText(count > 1 ? count + " items" : count + " item");
                holder3.total.setText(StringUtils.getPriceByFormat(order.getTotalPrice()));
                if (null == holder3.adapter) {
                    holder3.adapter = new ProductOrderAdapter(order.getProductList(), mContext, order.getOrderId());
                    holder3.products.setAdapter(holder3.adapter);
                } else {
                    holder3.adapter.setProductsOrderID(order.getProductList(), order.getOrderId());
                }

                holder3.delete_order.setOnClickListener(new MyClickLisnter(position));
                holder3.write_review.setOnClickListener(new MyClickLisnter(position));
                break;
        }

        return convertView;
    }

    class ViewHolderBase {
        protected TextView date;
        protected TextView status;
        protected TextView count;
        protected TextView total;
        protected NoScrollListView products;
        protected RelativeLayout total_view;
        protected ProductOrderAdapter adapter;
    }

    final class ViewHolder1 extends ViewHolderBase {
        private Button cancel_order;
        private Button pay_now;
    }

    final class ViewHolder2 extends ViewHolderBase {
        private Button btn;
    }

    final class ViewHolder3 extends ViewHolderBase {
        private Button delete_order;
        private Button write_review;
    }

    private final class MyClickLisnter implements View.OnClickListener {

        int position;
        Intent intent;

        public MyClickLisnter(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            final OrderDetail order = orders.get(position);
            switch (v.getId()) {
                case R.id.cancel_order:
                    DeleteDialog.showDeleteDialog(mContext, mContext.getString(R.string.cancel_order), mContext.getString(R.string.makesure_cancle_order),
                            new DeleteDialogListener() {
                                @Override
                                public void deleteDialog() {
                                    controller.getServiceManager().getOrderService().cancelOrder(order.getOrderId(), service);
                                }
                            });
                    break;
                case R.id.pay_now:
                    int paystyle = CacheUtils.getInt(IruluApplication.getInstance(), "nativePaymentEnabled");
                    if (paystyle == 1) {
                        PaymentMyMethodActivity.startOrderDetailActivity(mContext, order);
                    } else {
                        WebPaymentActivity.startWebPaymentActivity(mContext, order);
                    }
                    break;
                case R.id.btn:
                    switch (order.getStatus()) {
                        case 4:
                            DeleteDialog.showDeleteDialog(mContext, mContext.getString(R.string.confirm_goods), mContext.getString(R.string.makesure_confirm_goods),
                                    new DeleteDialogListener() {
                                        @Override
                                        public void deleteDialog() {
                                            controller.getServiceManager().getOrderService().confirmOrder(order.getOrderId(), service);
                                        }
                                    });
                            break;
                        case 6:
                            DeleteDialog.showDeleteDialog(mContext, mContext.getString(R.string.delete_order), mContext.getString(R.string.makesure_delete_order),
                                    new DeleteDialogListener() {
                                        @Override
                                        public void deleteDialog() {
                                            controller.getServiceManager().getOrderService().deleteOrder(order.getOrderId(), service);
                                        }
                                    });
                            break;
                    }
                    break;
                case R.id.delete_order:
                    DeleteDialog.showDeleteDialog(mContext, mContext.getString(R.string.delete_order), mContext.getString(R.string.makesure_delete_order),
                            new DeleteDialogListener() {
                                @Override
                                public void deleteDialog() {
                                    controller.getServiceManager().getOrderService().deleteOrder(order.getOrderId(), service);
                                }
                            });
                    break;
                case R.id.write_review:
                    if (1 < order.getProductList().size()) {
                        intent = new Intent(mContext, SelectToReivewActivity.class);
                        intent.putExtra("products", order.getProductList());
                        mContext.startActivity(intent);
                    } else {
                        OrderDetailProduct p = order.getProductList().get(0);
                        Product product = new Product();
                        product.setId(String.valueOf(p.getProductId()));
                        product.setImage(p.getImage());
                        product.setName(p.getName());
                        product.setPrice(p.getPrice());
                        product.setQuantity(p.getQuantity());
                        product.setSku(p.getSku());
                        MyReviewsActivity.startMyReviewsActivity((OrdersActivity) mContext, product, "order");
                    }
                    break;
            }
        }
    }

    @Override
    public boolean isEmpty() {
        return orders == null || orders.size() == 0;
    }
}
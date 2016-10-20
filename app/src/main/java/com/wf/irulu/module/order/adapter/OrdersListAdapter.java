package com.wf.irulu.module.order.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
 * Created by dtw on 16/3/10.
 */
public class OrdersListAdapter extends RecyclerView.Adapter<OrdersListAdapter.ViewHolder> {

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

    public OrdersListAdapter(ArrayList<OrderDetail> orders, Context mContext, ServiceListener service) {
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
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder viewHolder = null;
        switch (viewType) {
            case TYPE_BASE:
                viewHolder = new ViewHolder(mInflater.inflate(R.layout.order_item0, null), viewType);
                break;
            case TYPE_1:
                viewHolder = new ViewHolder(mInflater.inflate(R.layout.order_item1, null), viewType);

                break;
            case TYPE_2:
                viewHolder = new ViewHolder(mInflater.inflate(R.layout.order_item2, null), viewType);
                break;
            case TYPE_3:
                viewHolder = new ViewHolder(mInflater.inflate(R.layout.order_item3, null), viewType);

                break;
        }
        return viewHolder;

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        OrderDetail order = orders.get(position);
        switch (getItemViewType(position)) {
            case TYPE_BASE:
                holder.date.setText(mOrderPlaced + DateFormatUtils.DateFormatUrlTime(Long.parseLong(order.getUpdateTime())));
                holder.status.setText(ConstantsUtils.ORDER_STATUS.get(order.getStatus()));
                holder.count.setText(order.getProductList().size() > 1 ? order.getProductList().size() + " items" : order.getProductList().size() + " item");
                holder.total.setText(StringUtils.getPriceByFormat(order.getTotalPrice()));

                if (null == holder.adapter) {
                    holder.adapter = new ProductOrderAdapter(order.getProductList(), mContext, order.getOrderId());
                    holder.products.setAdapter(holder.adapter);
                } else {
                    holder.adapter.setProductsOrderID(order.getProductList(), order.getOrderId());
                }

                holder.total_view.setBackgroundResource(R.color.white);
                break;
            case TYPE_1:
                holder.date.setText(mOrderPlaced + DateFormatUtils.DateFormatUrlTime(Long.parseLong(order.getUpdateTime())));
                holder.status.setText(ConstantsUtils.ORDER_STATUS.get(order.getStatus()));
                holder.count.setText(order.getProductList().size() > 1 ? order.getProductList().size() + " items" : order.getProductList().size() + " item");
                holder.total.setText(StringUtils.getPriceByFormat(order.getTotalPrice()));
                if (null == holder.adapter) {
                    holder.adapter = new ProductOrderAdapter(order.getProductList(), mContext, order.getOrderId());
                    holder.products.setAdapter(holder.adapter);
                } else {
                    holder.adapter.setProductsOrderID(order.getProductList(), order.getOrderId());
                }

                holder.cancel_order.setOnClickListener(new MyClickLisnter(position));
                holder.pay_now.setOnClickListener(new MyClickLisnter(position));
                break;
            case TYPE_2:
                switch (order.getStatus()) {
                    case 4:
                        holder.btn.setText(mContext.getString(R.string.confirm_goods));
                        break;
                    case 6:
                        holder.btn.setText(mContext.getString(R.string.delete_order));
                        break;
                }

                holder.date.setText(mOrderPlaced + DateFormatUtils.DateFormatUrlTime(Long.parseLong(order.getUpdateTime())));
                holder.status.setText(ConstantsUtils.ORDER_STATUS.get(order.getStatus()));
                holder.count.setText(order.getProductList().size() > 1 ? order.getProductList().size() + " items" : order.getProductList().size() + " item");
                holder.total.setText(StringUtils.getPriceByFormat(order.getTotalPrice()));
                if (null == holder.adapter) {
                    holder.adapter = new ProductOrderAdapter(order.getProductList(), mContext, order.getOrderId());
                    holder.products.setAdapter(holder.adapter);
                } else {
                    holder.adapter.setProductsOrderID(order.getProductList(), order.getOrderId());
                }

                holder.btn.setOnClickListener(new MyClickLisnter(position));
                break;
            case TYPE_3:
                holder.date.setText(mOrderPlaced + DateFormatUtils.DateFormatUrlTime(Long.parseLong(order.getUpdateTime())));
                holder.status.setText(ConstantsUtils.ORDER_STATUS.get(order.getStatus()));

                int count = 0;

                for (OrderDetailProduct product : order.getProductList()) {
                    count += product.getQuantity();
                }

                holder.count.setText(count > 1 ? count + " items" : count + " item");
                holder.total.setText(StringUtils.getPriceByFormat(order.getTotalPrice()));
                if (null == holder.adapter) {
                    holder.adapter = new ProductOrderAdapter(order.getProductList(), mContext, order.getOrderId());
                    holder.products.setAdapter(holder.adapter);
                } else {
                    holder.adapter.setProductsOrderID(order.getProductList(), order.getOrderId());
                }

                holder.delete_order.setOnClickListener(new MyClickLisnter(position));
                holder.write_review.setOnClickListener(new MyClickLisnter(position));
                break;
        }
    }

    @Override
    public int getItemCount() {
        return orders == null ? 0 : orders.size();
    }

    @Override
    public int getItemViewType(int position) {
        final OrderDetail order = orders.get(position);
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

    class ViewHolder extends RecyclerView.ViewHolder {

        protected TextView date;
        protected TextView status;
        protected TextView count;
        protected TextView total;
        protected NoScrollListView products;
        protected RelativeLayout total_view;
        protected ProductOrderAdapter adapter;
        private Button cancel_order;
        private Button pay_now;
        private Button btn;
        private Button delete_order;
        private Button write_review;

        public ViewHolder(View itemView) {
            super(itemView);


        }

        public ViewHolder(View itemView, int viewType) {
            super(itemView);
            switch (viewType) {
                case TYPE_BASE:
                    date = (TextView) itemView.findViewById(R.id.order_tv_date);
                    status = (TextView) itemView.findViewById(R.id.order_tv_status);
                    products = (NoScrollListView) itemView.findViewById(R.id.order_nslv_products);
                    count = (TextView) itemView.findViewById(R.id.order_tv_count);
                    total = (TextView) itemView.findViewById(R.id.order_tv_total);
                    total_view = (RelativeLayout) itemView.findViewById(R.id.total_view);
                    ((ViewGroup) itemView).setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
                    break;
                case TYPE_1:
                    date = (TextView) itemView.findViewById(R.id.order_tv_date);
                    status = (TextView) itemView.findViewById(R.id.order_tv_status);
                    products = (NoScrollListView) itemView.findViewById(R.id.order_nslv_products);
                    count = (TextView) itemView.findViewById(R.id.order_tv_count);
                    total = (TextView) itemView.findViewById(R.id.order_tv_total);
                    cancel_order = (Button) itemView.findViewById(R.id.cancel_order);
                    pay_now = (Button) itemView.findViewById(R.id.pay_now);
                    ((ViewGroup) itemView).setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
                    break;
                case TYPE_2:
                    date = (TextView) itemView.findViewById(R.id.order_tv_date);
                    status = (TextView) itemView.findViewById(R.id.order_tv_status);
                    products = (NoScrollListView) itemView.findViewById(R.id.order_nslv_products);
                    count = (TextView) itemView.findViewById(R.id.order_tv_count);
                    total = (TextView) itemView.findViewById(R.id.order_tv_total);
                    btn = (Button) itemView.findViewById(R.id.btn);
                    ((ViewGroup) itemView).setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
                    break;
                case TYPE_3:
                    date = (TextView) itemView.findViewById(R.id.order_tv_date);
                    status = (TextView) itemView.findViewById(R.id.order_tv_status);
                    products = (NoScrollListView) itemView.findViewById(R.id.order_nslv_products);
                    count = (TextView) itemView.findViewById(R.id.order_tv_count);
                    total = (TextView) itemView.findViewById(R.id.order_tv_total);
                    delete_order = (Button) itemView.findViewById(R.id.delete_order);
                    write_review = (Button) itemView.findViewById(R.id.write_review);
                    ((ViewGroup) itemView).setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
                    break;
            }
        }

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
}

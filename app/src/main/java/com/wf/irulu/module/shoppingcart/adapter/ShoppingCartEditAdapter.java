package com.wf.irulu.module.shoppingcart.adapter;

import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wf.irulu.IruluApplication;
import com.wf.irulu.R;
import com.wf.irulu.common.bean.GiftInfo;
import com.wf.irulu.common.bean.ProductCart;
import com.wf.irulu.common.bean.ShoppingCart;
import com.wf.irulu.common.utils.CacheUtils;
import com.wf.irulu.common.utils.ConstantsUtils;
import com.wf.irulu.common.utils.StringUtils;
import com.wf.irulu.common.view.ClickButton;
import com.wf.irulu.common.view.NoScrollListView;
import com.wf.irulu.common.view.PageLoadDialog;
import com.wf.irulu.logic.IruluController;
import com.wf.irulu.logic.listener.ServiceListener;
import com.wf.irulu.module.home.onclick.AddWishlistOnClick_01;
import com.wf.irulu.module.shoppingcart.activity.ShoppingCartEditActivity;
import com.wf.irulu.module.shoppingcart.activity.ShoppingCartEditActivity.OkOnClick;

import java.util.ArrayList;
import java.util.List;

/**
 * @描述: 购物车可编辑的Adapter
 * @项目名: irulu1.2
 * @包名:com.wf.irulu.module.shoppingcart.adapter
 * @类名:ShoppingCartEditAdapter
 * @作者: 左杰
 * @创建时间:2015/10/29 16:44
 */
public class ShoppingCartEditAdapter extends BaseAdapter implements ServiceListener {
    private IruluController controller;
    private Context mContext;
    private ArrayList<ProductCart> mProductList;
    private OkOnClick okOnClick;
    private int width = 0;
    private int index;

    public ShoppingCartEditAdapter(Context context, ArrayList<ProductCart> productList, OkOnClick okOnClick, IruluController controller) {
        mContext = context;
        mProductList = productList;
        this.okOnClick = okOnClick;
        this.controller = controller;
        this.width = ConstantsUtils.DISPLAYW * 180 / 750;
    }

    @Override
    public int getCount() {
        if (null != mProductList) {
            return mProductList.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int i) {
        return mProductList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    public void setNotifyDataSetChanged(ArrayList<ProductCart> productList) {
        this.mProductList = productList;
        notifyDataSetChanged();
    }

    /**
     * 刷新当前填写的商品数量
     *
     * @param position
     * @param num
     */
    public void refreshProductNum(int position, String num) {
        ProductCart productCart = mProductList.get(position);
        productCart.setQuantity(Integer.parseInt(num));
        mProductList.set(position, productCart);
        notifyDataSetChanged();
    }

    /**
     * 刷新当前填写的商品数量
     *
     * @param position
     * @param stock
     */
    public void refreshProductStock(int position, int stock) {
        ProductCart productCart = mProductList.get(position);
        productCart.setQuantity(stock);
        mProductList.set(position, productCart);
        notifyDataSetChanged();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView != null) {
            holder = (ViewHolder) convertView.getTag();
        } else {
            convertView = View.inflate(mContext, R.layout.item_shopping_cart_edit, null);
            holder = new ViewHolder();
            holder.shopping_image = (ImageView) convertView.findViewById(R.id.shopping_image);
            holder.shopping_name = (TextView) convertView.findViewById(R.id.shopping_name);
            holder.shopping_price = (TextView) convertView.findViewById(R.id.shopping_price);
            holder.shopping_layout = (LinearLayout) convertView.findViewById(R.id.shopping_layout);
            holder.shopping_productSku = (TextView) convertView.findViewById(R.id.shopping_productSku);
            holder.shopping_increase = (ClickButton) convertView.findViewById(R.id.shopping_increase);//商品数量增加按钮
            holder.shopping_quantity = (EditText) convertView.findViewById(R.id.shopping_quantity);
            holder.shopping_reduce = (ClickButton) convertView.findViewById(R.id.shopping_reduce);//商品数量减少按钮
            holder.shopping_no_scroll_lv = (NoScrollListView) convertView.findViewById(R.id.shopping_no_scroll_lv);
            holder.soldOutTag = (TextView) convertView.findViewById(R.id.sold_out_tag);
            holder.horizontal_line_gray = convertView.findViewById(R.id.horizontal_line_gray);
            holder.wisth_iv = (ImageView) convertView.findViewById(R.id.wishlist_icon_iv);
            convertView.setTag(holder);
        }

        final ProductCart productCart = mProductList.get(position);

        int wish = productCart.getIsAddWish() == null ? 0 : Integer.valueOf(productCart.getIsAddWish());
        holder.wisth_iv.setImageResource(wish == 0 ? R.mipmap.wishlist_icon_normal : R.mipmap.wishlist_icon_select);
        final String image = productCart.getImage();
        String product = productCart.getProduct();
        float price = productCart.getPrice();
        List<String> productSku = productCart.getProductSku();
        int quantity = productCart.getQuantity();//购买数量
        final int stock = productCart.getStock();//库存量

        /****************商品数量变化时，加减按钮变化 start******************/
        if (quantity > 1 || quantity < 200) {
            holder.shopping_reduce.setEnabled(true);//减少按钮可点击
            holder.shopping_increase.setEnabled(true);//增加按钮可点击

        }


        String statusStr = productCart.getStatus();
        if ("0".equals(statusStr) || stock == 0) {//商品已下架,或者库存为0
            holder.soldOutTag.setVisibility(View.VISIBLE);
            holder.shopping_increase.setEnabled(false);
            holder.shopping_reduce.setEnabled(false);

        } else {
            holder.soldOutTag.setVisibility(View.GONE);
            if (quantity <= 1) {//商品减少按钮不可点击
                holder.shopping_reduce.setEnabled(false);

            } else if (quantity >= 200 || quantity >= stock) {//商品增加按钮不可点击
                holder.shopping_increase.setEnabled(false);

            } else {
                holder.shopping_reduce.setEnabled(true);//减少按钮可点击

                holder.shopping_increase.setEnabled(true);//增加按钮可点击

            }
            if (quantity == 1 && stock == 1) {//商品增加按钮不可点击
                holder.shopping_increase.setEnabled(false);

            }
        }
        /****************商品数量变化时，加减按钮变化 end******************/

        if (!TextUtils.isEmpty(image)) {
            String str = "?imageView2/0/w/" + width + "/h/" + width + "/format/jpg/interlace/1/q/75";
            IruluApplication.getInstance().getPicasso().load(image + str).placeholder(R.mipmap.notify_image_xiaotu).into(holder.shopping_image);
        }
        holder.shopping_quantity.setText(String.valueOf(quantity));//设置购物车商品的数量
        holder.shopping_name.setText(product);
        holder.shopping_productSku.setText(productSku.toString().replace("[", "").replace("]", ""));
        holder.shopping_price.setText(StringUtils.getPriceByFormat(price)+"*1");

        /**
         * 商品的赠品（买一送N），赠品数量根据主商品数量变化增加
         */
        ArrayList<GiftInfo> giftInfo = productCart.getGiftInfo();
        if (giftInfo.size() > 0) {
            holder.horizontal_line_gray.setVisibility(View.VISIBLE);
            holder.shopping_no_scroll_lv.setVisibility(View.VISIBLE);
            holder.shopping_no_scroll_lv.setAdapter(new AddOnGiftAdapter(mContext, giftInfo));
        } else {
            holder.horizontal_line_gray.setVisibility(View.GONE);
            holder.shopping_no_scroll_lv.setVisibility(View.GONE);
        }

        /**
         * 记录当前位置
         */
        holder.shopping_quantity.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    index = position;
                }
                return false;
            }
        });

        holder.shopping_quantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.shopping_quantity.requestFocus();
            }
        });

        if (index == position) {
            //如果当前的行下标和点击事件中保存的index一致，手动为EditText设置焦点
            holder.shopping_quantity.requestFocus();
        } else {
            holder.shopping_quantity.clearFocus();
        }

        /**
         * 商品数量增加点击事件
         */
        holder.shopping_increase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sumPriceStr = holder.shopping_quantity.getText().toString().trim();
                int id = mProductList.get(position).getId();//购物车id
                int productQuantity = 0;
                if (TextUtils.isEmpty(sumPriceStr)) {
                    productQuantity = 0;
                } else {
                    productQuantity = Integer.parseInt(sumPriceStr);
                }
                ++productQuantity;
                if (productQuantity > 1) {//商品减少按钮可点击
                    holder.shopping_reduce.setEnabled(true);

                }
                if (productQuantity >= 200) {//商品增加按钮不可点击
//                    holder.shopping_increase.setClickable();
                    holder.shopping_increase.setEnabled(false);

                }
                if (productQuantity >= stock) {
                    holder.shopping_increase.setEnabled(false);

                } else {
                    holder.shopping_increase.setEnabled(true);

                }
                String productQuantityStr = String.valueOf(productQuantity);
                holder.shopping_quantity.setText(productQuantityStr);
                PageLoadDialog.showDialogForLoading(mContext, true, false);
                /**
                 * 请求网络修改商品数量
                 */
                controller.getServiceManager().getShoppingService().updataProductNum(String.valueOf(id), productQuantityStr, ShoppingCartEditAdapter.this);
            }
        });

        /**
         * 商品数量减少点击事件
         */
        holder.shopping_reduce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sumPriceStr = holder.shopping_quantity.getText().toString().trim();
                int productQuantity = 0;
                if (TextUtils.isEmpty(sumPriceStr)) {
                    productQuantity = 0;
                } else {
                    productQuantity = Integer.parseInt(sumPriceStr);
                }
                if (productQuantity > 1) {
                    int id = mProductList.get(position).getId();//购物车id
                    productQuantity--;
                    if (productQuantity < 200) {//商品增加按钮可点击
                        holder.shopping_increase.setEnabled(true);

                    }
                    if (productQuantity <= 1) {//商品减少按钮不可点击
                        holder.shopping_reduce.setEnabled(false);

                    }
                    if (productQuantity >= stock) {
                        holder.shopping_increase.setEnabled(false);

                    } else {
                        holder.shopping_increase.setEnabled(true);

                    }
                    String productQuantityStr = String.valueOf(productQuantity);
                    holder.shopping_quantity.setText(productQuantityStr);
                    PageLoadDialog.showDialogForLoading(mContext, true, false);
                    /**
                     * 请求网络修改商品数量
                     */
                    controller.getServiceManager().getShoppingService().updataProductNum(String.valueOf(id), productQuantityStr, ShoppingCartEditAdapter.this);
                }
            }
        });
        /**
         * 监听EditText的变化
         */
        holder.shopping_quantity.addTextChangedListener(new MyTextWatcher(index));
        holder.wisth_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String isAddWish = productCart.getIsAddWish() == null ? "0" : productCart.getIsAddWish();
                new AddWishlistOnClick_01(mContext, IruluController.getInstance()).OnClick(Integer.valueOf(isAddWish), productCart.getProductId() + "");
            }
        });

        return convertView;
    }

    @Override
    public void serviceSuccess(ActionTypes action, Object bandObj, Object returnObj) {
        switch (action) {
            case UPDATA_PRODUCT_NUM:
                ShoppingCart shoppingCart = (ShoppingCart) returnObj;
                mProductList = shoppingCart.shoppingList.getProductList();
                notifyDataSetChanged();
                int quantityItems = 0;
                int size = mProductList.size();
                for (int i = 0; i < size; i++) {
                    int quantity = mProductList.get(i).getQuantity();
                    quantityItems = quantityItems + quantity;
                }
                CacheUtils.setLong(mContext, "cartNum", quantityItems);// 购物车数量设置sp中就是为了在其他页面初始化的时候调用
                controller.postShoppongCartCountCallback(quantityItems);//当删除商品后，购物车没有商品了，就更新购物车数量
                //初始化底部布局的商品数量和商品总价格
                ((ShoppingCartEditActivity) mContext).initProductNumTotal(shoppingCart);
                PageLoadDialog.hideDialogForLoading();
                break;
            default:
                break;
        }
    }

    @Override
    public void serviceFailure(ActionTypes action, Object returnObj, int errorCode) {

    }

    @Override
    public void serviceCallback(ActionTypes action, int type, Object returnObj) {

    }

    class ViewHolder {
        ImageView shopping_image;
        TextView shopping_name;
        TextView shopping_price;
        TextView shopping_productSku;
        LinearLayout shopping_layout;
        ClickButton shopping_increase;//购物车item增加按钮
        EditText shopping_quantity;//购物车item的数量
        ClickButton shopping_reduce;//购物车item减少按钮
        NoScrollListView shopping_no_scroll_lv;
        TextView soldOutTag;
        ImageView wisth_iv;
        View horizontal_line_gray;
    }

    /**
     * 自定义TextWatcher
     */
    class MyTextWatcher implements TextWatcher {
        private int position;

        public MyTextWatcher(int position) {
            this.position = position;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (!TextUtils.isEmpty(s.toString())) {
                okOnClick.setPosition(position);
                okOnClick.setNum(s.toString());
            }
        }
    }

}

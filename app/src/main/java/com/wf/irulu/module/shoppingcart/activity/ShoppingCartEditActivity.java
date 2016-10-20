package com.wf.irulu.module.shoppingcart.activity;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;
import com.wf.irulu.R;
import com.wf.irulu.common.base.CommonTitleBaseActivity;
import com.wf.irulu.common.bean.CartInfo;
import com.wf.irulu.common.bean.CheckoutBean;
import com.wf.irulu.common.bean.ProductCart;
import com.wf.irulu.common.bean.ShippingAddr;
import com.wf.irulu.common.bean.ShippingAddrBean;
import com.wf.irulu.common.bean.ShoppingCart;
import com.wf.irulu.common.bean.WishInfo;
import com.wf.irulu.common.utils.CacheUtils;
import com.wf.irulu.common.utils.ConstantsUtils;
import com.wf.irulu.common.utils.CustomerEvent;
import com.wf.irulu.common.utils.KeyboardUtils;
import com.wf.irulu.common.utils.StringUtils;
import com.wf.irulu.common.utils.UIUtils;
import com.wf.irulu.common.view.KeyboardLayout;
import com.wf.irulu.common.view.PageLoadDialog;
import com.wf.irulu.common.view.pulltorefresh.PullToRefreshBase;
import com.wf.irulu.common.view.swipemenulistview.SwipeMenu;
import com.wf.irulu.common.view.swipemenulistview.SwipeMenuCreator;
import com.wf.irulu.common.view.swipemenulistview.SwipeMenuItem;
import com.wf.irulu.common.view.swipemenulistview.SwipeMenuListView;
import com.wf.irulu.framework.HomeActivity;
import com.wf.irulu.logic.listener.IsAddWishListListener;
import com.wf.irulu.logic.listener.ServiceListener;
import com.wf.irulu.logic.listener.ShoppingCartEmptyListener;
import com.wf.irulu.module.aas.activity.LoginActivity;
import com.wf.irulu.module.payment.activity.OrderConfirmationActivity;
import com.wf.irulu.module.product.activity.ProductDetailActivity;
import com.wf.irulu.module.shoppingcart.adapter.ShoppingCartEditAdapter;
import com.wf.irulu.module.shoppingcart.dialog.SoldOutDialog;

import java.util.ArrayList;
import java.util.List;

/**
 * @描述: 购物车可编辑的Activity
 * @项目名: irulu1.2
 * @包名:com.wf.irulu.module.shoppingcart
 * @类名:ShoppingCartActivity
 * @作者: 左杰
 * @创建时间:2015/10/29 10:36
 */
public class ShoppingCartEditActivity extends CommonTitleBaseActivity implements PullToRefreshBase.OnRefreshListener<ListView>, ServiceListener, ShoppingCartEmptyListener, IsAddWishListListener {
    private final String TAG = getClass().getCanonicalName();
    private static final int CHECKOUT_REQUESTCODE = 0;
    /**
     * 购物车商品集合对象对象
     */
    public ArrayList<ProductCart> mProductList;
    private SwipeMenuListView mPullToRefreshListView;
    private ShoppingCart mShoppingCart;
    private OkOnClick okOnClick;
    private ShoppingCartEditAdapter mAdapter;
    private LinearLayout mShoppingCalculateLayout;
    /**
     * 商品总价格
     */
    private TextView mShoppingTotal;
    /**
     * 空布局
     */
    private RelativeLayout mCartEmptyLayout;
    /**
     * 空布局中的GO Shopping按钮
     */
    private Button mCartEmptyBtn;
    private ShippingAddrBean mShippingAddrBean;
    /**
     * 是否是刷新
     */
    private boolean isRefresh = false;
    private boolean isInitWish = true;
    private SoldOutDialog mDialog;

    @Override
    protected String setWindowTitle() {
        return "Shopping Cart";
    }

    @Override
    public void setContentView() {
        setContentView(R.layout.loading_simple_waiting);
    }

    @Override
    public void initView() {
        controller.registShoppingCartEmptyListener(this);
        controller.registIsAddWishListListener(this);
    }

    @Override
    public void initData() {
        /**
         * 联网请求购物车数据
         */
        controller.getServiceManager().getShoppingService().getCartInfo(this);
        isInitWish = true;
        // 统计进入购物车
        MobclickAgent.onEvent(this, String.valueOf(CustomerEvent.filter_enter_shopping_cart), ConstantsUtils.mVersionAnalystics);
    }

    @Override
    public void initDataView() {
        PageLoadDialog.showDialogForLoading(this, true, true);

        mPullToRefreshListView = (SwipeMenuListView) findViewById(R.id.shoppingcart_edit_lv);
        mCartEmptyLayout = (RelativeLayout) findViewById(R.id.cart_empty_layout);//空布局
        mCartEmptyBtn = (Button) findViewById(R.id.cart_empty_btn);//空布局中的GO Shopping按钮
        Button checkout = (Button) findViewById(R.id.checkout);
        final KeyboardLayout mKeyboardLayout = (KeyboardLayout) findViewById(R.id.main_keyboard_layout);//键盘监听的布局
        mShoppingCalculateLayout = (LinearLayout) findViewById(R.id.shopping_calculate_layout);//底部的Check Out按钮及Total文本的布局
        final RelativeLayout inputConfirmLayout = (RelativeLayout) findViewById(R.id.input_confirm_layout);//商品数量输入确认布局
        mShoppingTotal = (TextView) findViewById(R.id.shopping_total);//商品总价格
        TextView keyboardCancel = (TextView) findViewById(R.id.keyboard_cancel);
        TextView keyboardOk = (TextView) findViewById(R.id.keyboard_ok);


        mPullToRefreshListView.setEmptyView(mCartEmptyLayout);
        keyboardCancel.setOnClickListener(this);
        checkout.setOnClickListener(this);
        okOnClick = new OkOnClick();
        keyboardOk.setOnClickListener(okOnClick);
        mCartEmptyBtn.setOnClickListener(this);

        /**
         * 监听软键盘的显示和隐藏
         */
        mKeyboardLayout.setOnkbdStateListener(new KeyboardLayout.onKybdsChangeListener() {
            @Override
            public void onKeyBoardStateChange(int state) {
                if (state == mKeyboardLayout.KEYBOARD_STATE_SHOW) {//软键盘打开状态-3
                    isVisibleBelowLayout(View.GONE);
                    inputConfirmLayout.setVisibility(View.VISIBLE);
                } else if (state == mKeyboardLayout.KEYBOARD_STATE_HIDE) {//软键盘隐藏状态-2
                    isVisibleBelowLayout(View.VISIBLE);//
                    inputConfirmLayout.setVisibility(View.GONE);
                } else {//软键盘初始化状态，-1

                }
            }
        });
    }


    @Override
    public void addData() {

        //创建一个SwipeMenuCreator供ListView使用
        SwipeMenuCreator creator = new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu) {
                //创建一个侧滑菜单
                SwipeMenuItem delItem = new SwipeMenuItem(getApplicationContext());
                //给该侧滑菜单设置背景
                delItem.setBackground(new ColorDrawable(getResources().getColor(R.color.wish_delete)));
                //设置宽度
                delItem.setWidth(UIUtils.dip2px(70));
                // 设置图片
                delItem.setIcon(R.drawable.wish_delete_selector);
                // 设置标题
                delItem.setTitle("Delete");
                // 设置标题颜色
                delItem.setTitleColor(getResources().getColor(R.color.white));
                // 设置标题字体大小
                delItem.setTitleSize(12);
                //设置图片
//                delItem.setIcon(R.drawable.icon_del);
                //加入到侧滑菜单中
                menu.addMenuItem(delItem);
            }
        };

        // 添加菜单
        mPullToRefreshListView.setMenuCreator(creator);

        //侧滑菜单的相应事件
        mPullToRefreshListView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                int id = mProductList.get(position).getId();
                PageLoadDialog.showDialogForLoading(ShoppingCartEditActivity.this, true, false);
                /**
                 * 联网请求删除商品
                 */
                controller.getServiceManager().getShoppingService().removeProduct(String.valueOf(id), "", ShoppingCartEditActivity.this);
                return false;
            }

            /**
             * 跳转到商品详情页面
             */
            @Override
            public void onSingleClick(int position) {
                int productId = mProductList.get(position).getProductId();//这里的position-1是因为ListView有个头
                ProductDetailActivity.startProductDetailActivity(ShoppingCartEditActivity.this, String.valueOf(productId));
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.checkout:
                if (controller.getCacheManager().getLoginUser().getUid() == 0 || controller.getCacheManager().getLoginUser().getUid() == -1) {
                    Intent intent = new Intent(this, LoginActivity.class);
                    intent.putExtra("shoppingCartActivityTag", "ShoppingCartActivity");
                    startActivityForResult(intent, CHECKOUT_REQUESTCODE);
                    // 统计进入登陆
                    return;
                }
                if (mShoppingCart.shoppingList == null) {
                    return;
                }

                int size = mProductList.size();

                if (size == 1) {//如果只有一个商品
                    ProductCart productCart = mProductList.get(0);
                    String status = productCart.getStatus();
                    boolean isenough = productCart.isenough();//如果为false，说明此商品库存不足
                    if ("0".equals(status) || isenough == false) {//如果商品已经下架,只Toast一下
                        showToast(R.string.sold_out);
                        return;
                    }
                } else {
                    boolean isHasSoldOut = false;
                    for (int i = 0; i < size; i++) {
                        ProductCart productCart = mProductList.get(i);
                        String status = productCart.getStatus();
                        boolean isenough = productCart.isenough();//如果为false，说明此商品库存不足
                        if ("0".equals(status) || isenough == false) {//如果有下架商品
                            isHasSoldOut = true;
                        }
                    }
                    if (isHasSoldOut) {
                        mDialog = new SoldOutDialog(this);
                        mDialog.setListener(mProductList, this);
                        mDialog.show();
                        return;
                    }
                }

                PageLoadDialog.showSingletonDialogForLoading(this, true, false);
                /**
                 * 请求网络获取地址
                 */
                controller.getServiceManager().getAasService().getAllAddress(this);

                // 统计CheckOut
                MobclickAgent.onEvent(this, String.valueOf(CustomerEvent.filter_check_out_click), ConstantsUtils.mVersionAnalystics);
                break;
            case R.id.keyboard_cancel:
                KeyboardUtils.hideSoftInput(this);//隐藏软键盘
                break;
            case R.id.cart_empty_btn:
                Intent intent = new Intent(this, HomeActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.shipment_restricted_remove:
                StringBuilder builder = new StringBuilder();
                int productListSize = mProductList.size();
                for (int i = 0; i < productListSize; i++) {
                    ProductCart productCart = mProductList.get(i);
                    String status = productCart.getStatus();
                    boolean isenough = productCart.isenough();//如果为false，说明此商品库存不足
                    if ("0".equals(status) || isenough == false) {//如果有下架商品
                        int id = productCart.getId();
                        builder.append("," + id);
                    }
                }
                StringBuilder replace = builder.replace(0, 1, "");//将第一个逗号给去掉
                PageLoadDialog.showDialogForLoading(this, true, false);
                /**
                 * 联网请求删除商品
                 */
                controller.getServiceManager().getShoppingService().removeProduct(replace.toString(), "", this);
                if (null != mDialog) {
                    mDialog.dismiss();
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case CHECKOUT_REQUESTCODE:
                if (data != null) {
                    mProductList = (ArrayList<ProductCart>) data.getSerializableExtra("productList");
                    mAdapter.setNotifyDataSetChanged(mProductList);
                }
                break;

            default:
                break;
        }
    }

    /**
     * 是否显示底部(按钮)的布局
     *
     * @param visibility
     */
    public void isVisibleBelowLayout(int visibility) {
        mShoppingCalculateLayout.setVisibility(visibility);
    }

    @Override
    public void onRefresh(PullToRefreshBase<ListView> refreshView) {
        isRefresh = true;
        controller.getServiceManager().getShoppingService().getCartInfo(this);
    }

    @Override
    public void shoppingCartEmpty(ArrayList<ProductCart> productList) {
        mProductList = productList;
        if (mProductList == null) {
            isVisibleBelowLayout(View.GONE);//隐藏底部布局
        }
        if (null != mAdapter) {
            mAdapter.setNotifyDataSetChanged(mProductList);
        }
    }

    @Override
    public void isAddWishList(String productId, String addWishList) {
        if (mProductList != null) {
            int size = mProductList.size();
            int vTempProductId;
            int vDstProductId = Integer.valueOf(productId);

            for (int i = 0; i < size; i++) {
                ProductCart vProductCart = mProductList.get(i);
                vTempProductId = vProductCart.getProductId();
                if (vTempProductId == vDstProductId) {
                    vProductCart.setIsAddWish(addWishList);
                    mAdapter.setNotifyDataSetChanged(mProductList);
                    break;
                }


            }
        }

    }

    /**
     * 自定义OK的点击事件，好让ListView中的item中的EditText输入完后，联网请求
     */
    public class OkOnClick implements View.OnClickListener {
        private int position;
        private String num;

        @Override
        public void onClick(View v) {
            ProductCart productCart = mProductList.get(position);
            int id = productCart.getId();
            int stock = productCart.getStock();//库存量
            if (!TextUtils.isEmpty(num)) {
                int numInt = Integer.parseInt(num);
                if (numInt <= stock) {//如果输入的数量在库存范围内
                    if (numInt >= 200) {//如果输入的数量是最大限制(200),那么就让其变为200
                        num = "200";
                    }
                    if (numInt <= 0) {//如果输入的数量是小与0,那么就让其变为1
                        num = "1";
                    }
                    mAdapter.refreshProductNum(position, num);
                    /**
                     * 请求网络改变购物车商品的数量
                     */
                    controller.getServiceManager().getShoppingService().updataProductNum(String.valueOf(id), num, ShoppingCartEditActivity.this);
                } else {//否则输入的数量大于库存
                    mAdapter.refreshProductStock(position, stock);
                    /**
                     * 请求网络改变购物车商品的数量
                     */
                    controller.getServiceManager().getShoppingService().updataProductNum(String.valueOf(id), String.valueOf(stock), ShoppingCartEditActivity.this);
                    showLongToast(R.string.no_enough);
                }
            }
            KeyboardUtils.hideSoftInput(ShoppingCartEditActivity.this);//隐藏软键盘
        }

        public int getPosition() {
            return position;
        }

        public void setPosition(int position) {
            this.position = position;
        }

        public String getNum() {
            return num;
        }

        public void setNum(String num) {
            this.num = num;
        }
    }

    /**
     * 初始化底部布局的商品数量和商品总价格
     */
    public void initProductNumTotal(ShoppingCart shoppingCart) {
        CartInfo shoppingList = shoppingCart.shoppingList;
        float total = shoppingList.getTotal();
        mShoppingTotal.setText(StringUtils.getPriceByFormat((total)));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        controller.unRegistShoppingCartEmptyListener(this);
        controller.unRegistIsAddWishListListener(this);
    }

    @Override
    public void serviceSuccess(ActionTypes action, Object bandObj, Object returnObj) {
        switch (action) {
            case GET_CART_INFO:
                refreshDataView(R.layout.activity_shopping_cart_edit_layout);
                mShoppingCart = (ShoppingCart) returnObj;
                CartInfo shoppingList = mShoppingCart.shoppingList;
                mProductList = shoppingList.getProductList();

                updateTitleNum(mProductList);
                if (mProductList.size() > 0) {
                    if (isRefresh) {
                        mAdapter.setNotifyDataSetChanged(mProductList);
//                        mPullToRefreshListView.onRefreshComplete();
                    } else {
                        isVisibleBelowLayout(View.VISIBLE);//
                        if (mAdapter == null) {
                            mAdapter = new ShoppingCartEditAdapter(this, mProductList, okOnClick, controller);
                            mPullToRefreshListView.setAdapter(mAdapter);
                        } else {
                            mAdapter.setNotifyDataSetChanged(mProductList);
                        }
                    }
                } else {
//                    mPullToRefreshListView.setMode(Mode.DISABLED);
                }
                //初始化底部布局的商品数量和商品总价格
                initProductNumTotal(mShoppingCart);
                if (isInitWish && mProductList != null && mProductList.size() != 0) {
                    controller.getServiceManager().getProductService().getWishList(this);
                } else {
                    PageLoadDialog.hideDialogForLoading();
                }

                break;
            case WISH_LIST:
                ArrayList<WishInfo> vWishInfos = (ArrayList<WishInfo>) returnObj;
                if (vWishInfos == null || vWishInfos.size() == 0) {
                    return;
                }
                PageLoadDialog.hideDialogForLoading();
                isInitWish = false;
                for (int i = 0; i < mProductList.size(); i++) {
                    for (int j = 0; j < vWishInfos.size(); j++) {
                        if (mProductList.get(i).getProductId() == vWishInfos.get(j).getPid()) {
                            mProductList.get(i).setIsAddWish("1");
                            continue;
                        }
                    }
                }
                mAdapter.notifyDataSetChanged();

                break;
            case UPDATA_PRODUCT_NUM:
                mShoppingCart = (ShoppingCart) returnObj;
                shoppingList = mShoppingCart.shoppingList;
                mProductList = shoppingList.getProductList();

                updateTitleNum(mProductList);
                //初始化底部布局的商品数量和商品总价格
                initProductNumTotal(mShoppingCart);
                break;
            case GET_ADDRESS:
                ShippingAddr shippingAddr = (ShippingAddr) returnObj;
                List<ShippingAddrBean> shippingAddrBeanList = shippingAddr.getList();
                int shippingAddrBeanListSize = shippingAddrBeanList.size();
                for (int i = 0; i < shippingAddrBeanListSize; i++) {
                    int isDefault = shippingAddrBeanList.get(i).getIsDefault();
                    if (isDefault == 1) {//如果有默认地址,就设置地址
                        mShippingAddrBean = shippingAddrBeanList.get(i);
                        break;
                    } else {//否则把第一个设置为地址
                        mShippingAddrBean = shippingAddrBeanList.get(0);
                    }

                }
                String addressId = "";
                if (mShippingAddrBean != null && !TextUtils.isEmpty(mShippingAddrBean.getId())) {
                    addressId = mShippingAddrBean.getId();
                }

                /**
                 * 请求网络Check NOW
                 */
                controller.getServiceManager().getOrderService().checkout("", addressId, "1", this);
                break;
            case PRODUCT_CHECKOUT:
                CheckoutBean checkoutBean = (CheckoutBean) returnObj;
                Intent intent = new Intent(this, OrderConfirmationActivity.class);
                intent.putExtra("checkoutBean", checkoutBean);
                intent.putExtra("shippingAddrBean", mShippingAddrBean);
                startActivity(intent);
                // 关闭对话框
                PageLoadDialog.hideDialogForLoading();
                break;
            case CART_REMOVE_PRODUCT:
                ShoppingCart shoppingCart = (ShoppingCart) returnObj;
                mProductList = shoppingCart.shoppingList.getProductList();
                mAdapter.setNotifyDataSetChanged(mProductList);
                updateTitleNum(mProductList);
                //初始化底部布局的商品数量和商品总价格
                initProductNumTotal(shoppingCart);
                PageLoadDialog.hideDialogForLoading();
                break;
            default:
                break;
        }
    }

    @Override
    public void serviceFailure(ActionTypes action, Object returnObj, int errorCode) {
        switch (action) {
            case GET_CART_INFO:
                PageLoadDialog.hideDialogForLoading();
                displayNoDataView(R.layout.no_data_simple_title_page);
                break;
            case GET_ADDRESS:
            case PRODUCT_CHECKOUT:
            case CART_REMOVE_PRODUCT:
                PageLoadDialog.hideDialogForLoading();
                break;
            default:
                break;
        }
        if (returnObj != null) {
            showToast(returnObj.toString());
        }
    }

    @Override
    public void serviceCallback(ActionTypes action, int type, Object returnObj) {

    }

    /**
     * 更新侧栏购物车数量和Title上面的购物车数量
     *
     * @param productList
     */
    private void updateTitleNum(ArrayList<ProductCart> productList) {

        int quantityItems = 0;
        int productListSize = productList.size();

        if (productListSize <= 0) {
            isVisibleBelowLayout(View.GONE);
            CacheUtils.setLong(this, "cartNum", 0);// 购物车数量设置sp中就是为了在其他页面初始化的时候调用
//          controller.postShoppongCartCountCallback(0);//当删除商品后，购物车没有商品了，就更新购物车数量
        }

        for (int i = 0; i < productListSize; i++) {
            int quantity = mProductList.get(i).getQuantity();
            quantityItems = quantityItems + quantity;
        }
        CacheUtils.setLong(this, "cartNum", quantityItems);// 购物车数量设置sp中就是为了在其他页面初始化的时候调用
        controller.postShoppongCartCountCallback(quantityItems);//当删除商品后，购物车没有商品了，就更新购物车数量
    }
}

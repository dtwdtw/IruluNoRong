package com.wf.irulu.module.payment.activity;

import android.content.Context;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wf.irulu.R;
import com.wf.irulu.common.base.CommonTitleBaseActivity;
import com.wf.irulu.common.bean.ShippingAddr;
import com.wf.irulu.common.bean.ShippingAddrBean;
import com.wf.irulu.common.utils.ConstantsUtils;
import com.wf.irulu.common.view.PageLoadDialog;
import com.wf.irulu.logic.listener.ServiceListener;
import com.wf.irulu.module.user.activity.AddNewAddressActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * @描述: 选着一个地址的Activity
 * @项目名: irulu1.2
 * @包名:com.wf.irulu.module.payment.activity
 * @类名:ChooseAddressActivity
 * @作者: 左杰
 * @创建时间:2015/11/16 19:13
 */
public class ChooseAddressActivity extends CommonTitleBaseActivity implements ServiceListener {

    private ListView mListView;
    private RelativeLayout choose_address_add;
    private ShippingAddr mShippingAddr;
    private List<ShippingAddrBean> mShippingAddrBeanList;
    private ShippingAddrBean mShippingAddrBean;
//    private ChooseAddressAdapter mAdapter;
    private String addrId;
    /** 是否点击了编辑地址，默认没有点击*/
    private boolean isEditClick = false;
    protected static final int EDIT_ADDRESS_REQUESTCODE = 1;
    private ChooseAddressAdapter mAdapter;

    @Override
    protected String setWindowTitle() {
        return "Choose Address";
    }

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_choose_address);
    }

    @Override
    public void initView() {
        addrId = getIntent().getStringExtra("addrId");
        choose_address_add = (RelativeLayout) findViewById(R.id.choose_address_add);
        RelativeLayout addressEmptyLayout = (RelativeLayout) findViewById(R.id.address_empty_layout);
        mListView = (ListView) findViewById(R.id.choose_address);
        mListView.setEmptyView(addressEmptyLayout);
        choose_address_add.setOnClickListener(this);
    }

    @Override
    public void initData() {
        PageLoadDialog.showDialogForLoading(this,true,false);
        controller.getServiceManager().getAasService().getAllAddress(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.choose_address_add:
                Intent intent = new Intent(ChooseAddressActivity.this,AddNewAddressActivity.class);
                startActivityForResult(intent, ConstantsUtils.REQUEST_CODE_ADDRESS);
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            isFinish();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected boolean isFinish() {
        if(isEditClick){
            //访问网络，修改默认地址
            Intent intent = new Intent();
            intent.putExtra("ShippingAddrBean", mShippingAddrBean);
            setResult(RESULT_OK, intent);
        }
        return super.isFinish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(data != null){
            switch (requestCode) {
                case ConstantsUtils.REQUEST_CODE_ADDRESS://添加一个新地址的时候回传的
                    ArrayList<ShippingAddrBean> shoppingAddrList = data.getParcelableArrayListExtra("shippingAddrBeanList");
                    mAdapter.setNotifyDataSetChanged(shoppingAddrList);
                    break;
                case EDIT_ADDRESS_REQUESTCODE://编辑的时候回传的
                    ArrayList<ShippingAddrBean> shoppingAddrList1 = data.getParcelableArrayListExtra("shippingAddrBeanList");
                    mAdapter.setNotifyDataSetChanged(shoppingAddrList1);
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public void serviceSuccess(ActionTypes action, Object bandObj, Object returnObj) {
        switch (action){
            case GET_ADDRESS:
                ShippingAddr shippingAddr = (ShippingAddr) returnObj;
                List<ShippingAddrBean> shippingAddrBeanList = shippingAddr.getList();
                mAdapter = new ChooseAddressAdapter(this,shippingAddrBeanList);
                mListView.setAdapter(mAdapter);
                PageLoadDialog.hideDialogForLoading();
            break;
            default:
                break;
        }
    }

    @Override
    public void serviceFailure(ActionTypes action, Object returnObj, int errorCode) {
        switch (action){
            case GET_ADDRESS:
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

    class ChooseAddressAdapter extends BaseAdapter {
        private List<ShippingAddrBean> mShippingAddrBeanList;
        private Context mContext;

        public ChooseAddressAdapter(Context context, List<ShippingAddrBean> shippingAddrBeanList) {
            this.mContext = context;
            mShippingAddrBeanList = shippingAddrBeanList;
        }

        @Override
        public int getCount() {
            if (mShippingAddrBeanList != null) return mShippingAddrBeanList.size();
            return 0;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final ViewHolder holder;
            if (convertView != null) {
                holder = (ViewHolder) convertView.getTag();
            } else {
                convertView = View.inflate(mContext, R.layout.item_choose_address, null);
                holder = new ViewHolder();
//				holder.choose_address_radio = (RadioButton) convertView.findViewById(R.id.choose_address_radio);//RadioButton
                holder.choose_address_iv = (ImageView) convertView.findViewById(R.id.choose_address_iv);//RadioButton
                holder.choose_address_name = (TextView) convertView.findViewById(R.id.choose_address_name);//收货姓名
                holder.choose_address_edit = (LinearLayout) convertView.findViewById(R.id.choose_address_edit);//edit点击事件
                holder.choose_address_ll = (LinearLayout) convertView.findViewById(R.id.choose_address_ll);//详细地址模块的布局
                holder.choose_address_street = (TextView) convertView.findViewById(R.id.choose_address_street);//地址
                holder.choose_address_city = (TextView) convertView.findViewById(R.id.choose_address_city);//城市
                holder.choose_address_state = (TextView) convertView.findViewById(R.id.choose_address_state);//州
                holder.choose_address_zipCode = (TextView) convertView.findViewById(R.id.choose_address_zipCode);//邮编
                holder.choose_address_country = (TextView) convertView.findViewById(R.id.choose_address_country);//国家
                holder.choose_address_phoneNum = (TextView) convertView.findViewById(R.id.choose_address_phoneNum);//电话号码

                convertView.setTag(holder);
            }

            mShippingAddrBean = mShippingAddrBeanList.get(position);
            String name = mShippingAddrBean.getFirstName() + " " + mShippingAddrBean.getLastName();
            String streetAddress1 = mShippingAddrBean.getAddress1();
            String city = mShippingAddrBean.getCity();
            String state = mShippingAddrBean.getState();
            String zipCode = mShippingAddrBean.getZipCode();
            String country = mShippingAddrBean.getCountry();
            String phoneNumber = mShippingAddrBean.getPhone();

            final String id = mShippingAddrBean.getId();
            if (id.equals(addrId)) {//如果传递过来的地址id等于此时的id，设置为选中
                holder.choose_address_iv.setImageResource(R.mipmap.round_button_selected);
            } else {
                holder.choose_address_iv.setImageResource(R.mipmap.round_button_normal);
            }

            holder.choose_address_name.setText(name);
            holder.choose_address_street.setText(streetAddress1);
            holder.choose_address_city.setText(city);
            holder.choose_address_state.setText(state);
            holder.choose_address_zipCode.setText(zipCode);
            holder.choose_address_country.setText(country);
            holder.choose_address_phoneNum.setText(phoneNumber);

            /**
             * 修改地址的点击事件
             */
            holder.choose_address_iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    chooseAddress(position, holder);
                }
            });

            /**
             * 修改地址的点击事件
             */
            holder.choose_address_ll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    chooseAddress(position, holder);
                }
            });

            /**
             * 编辑地址的点击事件
             */
            holder.choose_address_edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (id.equals(addrId)) {//如果传递过来的地址id等于此时的id，设置为选中
                        isEditClick = true;
                    }
                    mShippingAddrBean = mShippingAddrBeanList.get(position);
                    String id = mShippingAddrBean.getId();
                    // 编辑数据
                    Intent intent = new Intent(ChooseAddressActivity.this, AddNewAddressActivity.class);
                    intent.putExtra("ShippingAddrBean", mShippingAddrBean);
                    intent.putExtra("flags", "SAAEDIT");
                    startActivityForResult(intent, EDIT_ADDRESS_REQUESTCODE);
                }
            });
            return convertView;
        }

        private void chooseAddress(final int position, final ViewHolder holder) {
            holder.choose_address_iv.setImageResource(R.mipmap.round_button_selected);
            notifyDataSetChanged();
            //访问网络，修改默认地址
            Intent intent = new Intent();
            ShippingAddrBean shippingAddrBean = mShippingAddrBeanList.get(position);
            intent.putExtra("ShippingAddrBean", shippingAddrBean);
            setResult(RESULT_OK, intent);
            finish();
        }

        public void setNotifyDataSetChanged(List<ShippingAddrBean> shippingAddrBeanList) {
            mShippingAddrBeanList = shippingAddrBeanList;
            notifyDataSetChanged();
        }

        class ViewHolder {
            ImageView choose_address_iv;
            TextView choose_address_name;//姓名
            LinearLayout choose_address_edit;//点击Edit
            LinearLayout choose_address_ll;//详细地址模块的布局
            TextView choose_address_street;//地址
            TextView choose_address_city;//城市
            TextView choose_address_state;//州
            TextView choose_address_zipCode;//邮编
            TextView choose_address_country;//国家
            TextView choose_address_phoneNum;//电话号码
        }
    }
}

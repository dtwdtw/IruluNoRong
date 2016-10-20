package com.wf.irulu.module.user.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wf.irulu.R;
import com.wf.irulu.common.base.CommonTitleBaseActivity;
import com.wf.irulu.common.bean.ShippingAddr;
import com.wf.irulu.common.bean.ShippingAddrBean;
import com.wf.irulu.common.utils.ErrorCodeUtils;
import com.wf.irulu.common.view.PageLoadDialog;
import com.wf.irulu.logic.listener.ServiceListener;

import java.util.ArrayList;

/**
 * Created by daniel on 2015/11/7. 发货地址的 增 删 改 查
 */
public class ShippingAddressActivity extends CommonTitleBaseActivity implements ServiceListener{

    private TextView no_shipping_tv;
    private ImageView no_shipping_iv;
    private ListView shipping_lv;
    private ArrayList<ShippingAddrBean> datas = new ArrayList<ShippingAddrBean>();
    private ShippingAdapter adapter;
    private int flags = -1;
    private int editPosition = -1;
    @Override
    protected String setWindowTitle() {
        return getString(R.string.shipping_address);
    }

    @Override
    public void setContentView() {
        setContentView(R.layout.loading_simple_waiting);
//        setContentView(R.layout.activity_shipping_address);
    }

    @Override
    public void initView() {


    }

    @Override
    public void initData() {
        controller.getServiceManager().getAasService().getAllAddress(this);
    }

    @Override
    public void initDataView() {
        RelativeLayout add_new_address_rl = (RelativeLayout) findViewById(R.id.shipping_address_add_rl);
        no_shipping_tv = (TextView) findViewById(R.id.shipping_address_tv);
        no_shipping_iv = (ImageView) findViewById(R.id.shipping_address_iv);
        shipping_lv = (ListView) findViewById(R.id.shipping_address_lv);
        add_new_address_rl.setOnClickListener(this);
    }

    @Override
    public void addData() {

    }

    /**
     * 进入添加地址的页面
     * @param view
     */
    @Override
    public void onClick(View view) {
        if (view.getId()==R.id.shipping_address_add_rl){
            Intent add =new Intent(this,AddNewAddressActivity.class);
            startActivityForResult(add,3);
        }
    }

    @Override
    public void serviceSuccess(ActionTypes action, Object bandObj, Object returnObj) {
        switch (action){
                /**
                 * 获取发货地址用来展示数据
                 */
                case GET_ADDRESS:
                    refreshDataView(R.layout.activity_shipping_address);
                    ShippingAddr sa = (ShippingAddr) returnObj;
                    int total = sa.getTotal();
                    if(total>0){
                        for (int i = 0; i < total; i++) {
                            datas.add(sa.getList().get(i));
                        }
                        no_shipping_tv.setVisibility(View.GONE);
                        no_shipping_iv.setVisibility(View.GONE);
                    }
                    adapter = new ShippingAdapter();
                    shipping_lv.setAdapter(adapter);
                    break;
                case DELETE_ADDRESS:
                    /**
                     * 删除发货地址
                     */
                    PageLoadDialog.hideDialogForLoading();
                    datas.remove((Integer) returnObj);
                    adapter.notifyDataSetChanged();
                    if(datas==null||datas.size()==0){
                        no_shipping_iv.setVisibility(View.VISIBLE);
                        no_shipping_tv.setVisibility(View.VISIBLE);
                    }
                    break;
                case DEFAULT_ADDRESS:
                    /**
                     * 设置默认地址
                     */
                    PageLoadDialog.hideDialogForLoading();
                    datas.get((Integer)returnObj).setIsDefault(1);
                    adapter.notifyDataSetChanged();
                    break;
                default:
                    break;
            }
    }

    @Override
    public void serviceFailure(ActionTypes action, Object returnObj, int errorCode) {
        if(errorCode== ErrorCodeUtils.NO_NET_RESPONSE){
            if(action==ActionTypes.GET_ADDRESS){
                displayNoDataView(R.layout.no_data_simple_title_page);
            }else{
                PageLoadDialog.hideDialogForLoading();
            }
            showToast(R.string.no_network);
            return;
        }
        switch (action){
            /**
             * 获取发货地址用来展示数据
             */
            case GET_ADDRESS:
                displayNoDataView(R.layout.no_data_simple_title_page);
                break;
            case DELETE_ADDRESS:
            case DEFAULT_ADDRESS:
                PageLoadDialog.hideDialogForLoading();
            default:
                break;
        }
        showToast((String)returnObj);

    }

    @Override
    public void serviceCallback(ActionTypes action, int type, Object returnObj) {

    }

    /**
     * 显示发货地址item 的adapter
     */
    public class ShippingAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            if (datas == null) {
                return 0;
            } else {
                return datas.size();
            }
        }

        @Override
        public Object getItem(int position) {
            if (datas == null || datas.size() < 1) {
                return null;
            } else {
                return datas.get(position);
            }
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertview, ViewGroup viewGroup) {
            ViewHolder holder = null;
            if (convertview == null) {
                holder = new ViewHolder();
                convertview = View.inflate(ShippingAddressActivity.this, R.layout.item_shipping_address, null);
                holder.nickname_tv = (TextView) convertview.findViewById(R.id.shipping_address_contact_name);
                holder.addr1_tv = (TextView) convertview.findViewById(R.id.shipping_address_street1);
                holder.addr2_tv = (TextView) convertview.findViewById(R.id.shipping_address_street2);
                holder.city_tv = (TextView) convertview.findViewById(R.id.shipping_address_city);
                holder.state_tv = (TextView) convertview.findViewById(R.id.shipping_address_state);
                holder.code_tv = (TextView) convertview.findViewById(R.id.shipping_address_code);
                holder.country_tv = (TextView) convertview.findViewById(R.id.shipping_address_country);
                holder.telephone_tv = (TextView) convertview.findViewById(R.id.shipping_address_phone_number);
                holder.set_default_iv = (ImageView) convertview.findViewById(R.id.shipping_address_item_iv);
                holder.set_default_tv = (TextView) convertview.findViewById(R.id.shipping_address_default);
                holder.edit_tv = (TextView) convertview.findViewById(R.id.add_address_edit);
                holder.delete_tv = (TextView) convertview.findViewById(R.id.shipping_address_delete);


                convertview.setTag(holder);
            } else {
                holder = (ViewHolder) convertview.getTag();
            }
            /**
             * 设置数据
             */
            ShippingAddrBean sb = datas.get(position);
            holder.nickname_tv.setText(sb.getFirstName() + " " + sb.getLastName());
            holder.addr1_tv.setText(sb.getAddress1());
            String address2 = sb.getAddress2();
            if (TextUtils.isEmpty(address2)) {
                holder.addr2_tv.setVisibility(View.GONE);
            } else {
                holder.addr2_tv.setVisibility(View.VISIBLE);
                holder.addr2_tv.setText(address2);
            }
            holder.city_tv.setText(sb.getCity());
            holder.state_tv.setText(sb.getState());
            holder.code_tv.setText(sb.getZipCode());
            holder.country_tv.setText(sb.getCountry());
            holder.telephone_tv.setText(sb.getPhone());
            if(sb.getIsDefault()==1){
                flags=position;
                holder.set_default_iv.setImageResource(R.mipmap.round_button_selected);
                holder.set_default_tv.setText("Default");
            }else{
                holder.set_default_iv.setImageResource(R.mipmap.round_button_normal);
                holder.set_default_tv.setText("Set A Default");
            }
            holder.set_default_iv.setOnClickListener(new MyOnClick(datas,position,this));
            holder.edit_tv.setOnClickListener(new MyOnClick(datas,position,this));
            holder.delete_tv.setOnClickListener(new MyOnClick(datas,position,this));
            return convertview;
        }
    }

    class ViewHolder {
        TextView nickname_tv;
        TextView addr1_tv;
        TextView addr2_tv;
        TextView city_tv;
        TextView state_tv;
        TextView code_tv;
        TextView country_tv;
        TextView telephone_tv;
        TextView set_default_tv;
        ImageView set_default_iv;
        TextView edit_tv;
        TextView delete_tv;
    }

    class MyOnClick implements View.OnClickListener {
        private ArrayList<ShippingAddrBean> datas;
        private int position;
        private ShippingAdapter adapter;
        public MyOnClick(ArrayList<ShippingAddrBean> datas,int position,ShippingAdapter adapter) {
            this.datas = datas;
            this.position= position;
            this.adapter = adapter;
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.shipping_address_item_iv:
                    if(datas.get(position).getIsDefault()==1){
                        return;
                    }
                    if(flags!=-1) {
                        datas.get(flags).setIsDefault(0);
                    }
                    datas.get(position).setIsDefault(1);
                    adapter.notifyDataSetChanged();
                    PageLoadDialog.showDialogForLoading(ShippingAddressActivity.this,true,false);
                    controller.getServiceManager().getAasService().setDefaultAddress(datas.get(position).getId(), position, ShippingAddressActivity.this);
                    flags = position;
                    break;
                case R.id.add_address_edit:
                    editPosition = position;
                    Intent edit = new Intent(ShippingAddressActivity.this, AddNewAddressActivity.class);
                    ShippingAddrBean sb = datas.get(position);
                    edit.putExtra("ShippingAddrBean",sb);
                    edit.putExtra("flags","SAAEDIT");
                    startActivityForResult(edit, 0);

                    break;
                case R.id.shipping_address_delete:
                    PageLoadDialog.showDialogForLoading(ShippingAddressActivity.this,true,false);
                    controller.getServiceManager().getAasService().deleteAddress(datas.get(position).getId(),position,ShippingAddressActivity.this);
                    break;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode==1){
            ShippingAddrBean sa = (ShippingAddrBean) data.getParcelableExtra("ShippingAddrBean");
            if(requestCode==3){
                //添加地址
            if(datas.size()==0){
                datas.add(sa);
                no_shipping_iv.setVisibility(View.GONE);
                no_shipping_tv.setVisibility(View.GONE);
            }else{
                datas.add(0,sa);
            }
        }else if(requestCode==0){
            //编辑
            datas.add(editPosition,sa);
            datas.remove(editPosition+1);
        }
            adapter.notifyDataSetChanged();
    }
}
}
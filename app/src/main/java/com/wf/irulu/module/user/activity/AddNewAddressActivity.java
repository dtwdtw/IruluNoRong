package com.wf.irulu.module.user.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.wf.irulu.IruluApplication;
import com.wf.irulu.R;
import com.wf.irulu.common.base.CommonTitleBaseActivity;
import com.wf.irulu.common.bean.CountryInfor;
import com.wf.irulu.common.bean.CountryInforBean;
import com.wf.irulu.common.bean.ShippingAddrBean;
import com.wf.irulu.common.bean.StateInfo;
import com.wf.irulu.common.utils.CacheUtils;
import com.wf.irulu.common.utils.ConstantsUtils;
import com.wf.irulu.common.utils.ErrorCodeUtils;
import com.wf.irulu.common.utils.ILog;
import com.wf.irulu.logic.listener.ServiceListener;
import com.wf.irulu.module.services.FetchAddressIntentService;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by daniel on 2015/11/9.
 * 用来添加地址 编辑地址
 */
public class AddNewAddressActivity extends CommonTitleBaseActivity implements ServiceListener {

    public static final int SUCCESS_RESULT = 0;

    public static final int FAILURE_RESULT = 1;

    public static final String PACKAGE_NAME = IruluApplication.getInstance().getPackageName();

    public static final String RECEIVER = PACKAGE_NAME + ".RECEIVER";

    public static final String RESULT_DATA_KEY = PACKAGE_NAME + ".RESULT_DATA_KEY";

    public static final String LOCATION_DATA_EXTRA = PACKAGE_NAME + ".LOCATION_DATA_EXTRA";


    private EditText address1_et;
    private EditText address2_et;
    private EditText city_et;
    private EditText state_et;
    private EditText code_et;
    private EditText firstname_et;
    private EditText lastname_et;
    private EditText profix_et;
    private EditText phone_number_et;
    private TextView country_tv;
    private String flags;
    private LocationManager lm;
    private String iso3Country;
    private boolean isInvariability = true;
    private HashMap<String, ArrayList<StateInfo.ListEntity>> stateMaps = null;
    private CountryInfor mCountryInfor = null;
    /**
     * Receiver registered with this activity to get the response from FetchAddressIntentService.
     */
    private AddressResultReceiver mResultReceiver;


    @Override
    protected String setWindowTitle() {
        flags = getIntent().getStringExtra("flags");
        if ("SAAEDIT".equals(flags)) {
            return "Edit Address";
        }
        return getString(R.string.add_new_address);

    }

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_add_new_address);
    }

    @Override
    public void initView() {
        address1_et = (EditText) findViewById(R.id.add_street_address1);
        address2_et = (EditText) findViewById(R.id.add_street_address2);
        city_et = (EditText) findViewById(R.id.add_city);
        state_et = (EditText) findViewById(R.id.add_state_province_country);
        code_et = (EditText) findViewById(R.id.add_zip_postal_code);
        firstname_et = (EditText) findViewById(R.id.add_first_name);
        lastname_et = (EditText) findViewById(R.id.add_last_name);
        profix_et = (EditText) findViewById(R.id.add_profix);
        phone_number_et = (EditText) findViewById(R.id.add_phone_number);
        country_tv = (TextView) findViewById(R.id.add_country);
        Button save_btn = (Button) findViewById(R.id.add_new_address_save_btn);

        country_tv.setOnClickListener(this);
        save_btn.setOnClickListener(this);
    }

    /**
     * 从编辑过来的 就要初始化数据
     */
    @Override
    public void initData() {

        if ("SAAEDIT".equals(flags)) {
            ShippingAddrBean sb = (ShippingAddrBean) getIntent().getParcelableExtra("ShippingAddrBean");
            address1_et.setText(sb.getAddress1());
            address2_et.setText(sb.getAddress2() + "");
            city_et.setText(sb.getCity());
            state_et.setText(sb.getState());
            code_et.setText(sb.getZipCode());
            firstname_et.setText(sb.getFirstName());
            lastname_et.setText(sb.getLastName());
            country_tv.setText(sb.getCountry());
            String[] split = sb.getPhone().split(" ");
            if (split.length > 1) {
                profix_et.setText(sb.getPhone().split(" ")[0]);
                phone_number_et.setText(sb.getPhone().split(" ")[1]);
            } else {
                profix_et.setText("");
                phone_number_et.setText(sb.getPhone());
            }
        }
        mResultReceiver = new AddressResultReceiver(new Handler());
        if (stateMaps == null) {
            controller.getServiceManager().getAasService().getStateInformation(this);
        }
        if (mCountryInfor == null) {
            controller.getServiceManager().getAasService().getCountryInformation(this);
        }
        getLoaction();


    }

    /**
     * 获取最佳的定位方式
     */
    public void getLoaction() {
        lm = (LocationManager) getSystemService(LOCATION_SERVICE);


        Criteria criteria = new Criteria();
        criteria.setCostAllowed(false);
        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);
        criteria.setPowerRequirement(Criteria.POWER_LOW);
        criteria.setAccuracy(Criteria.ACCURACY_COARSE);
        String bestProvider = lm.getBestProvider(criteria, true);

        Location lastKnownLocation = lm.getLastKnownLocation(bestProvider);

        if (lastKnownLocation != null) {
            getCountryLocation(lastKnownLocation);
            return;
        }
        lm.requestLocationUpdates(bestProvider, 0, 0, listener);
    }

    /**
     * 获取经纬度
     */
    private LocationListener listener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            if (location != null) {
                getCountryLocation(location);
                lm.removeUpdates(listener);
            }
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {
        }
    };

    /**
     * 获取地理位置
     */
    private void getCountryLocation(Location location) {
        startIntentService(location);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.add_country:
                Intent ct = new Intent(this, CountryActivity.class);
                ct.putExtra("cname", country_tv.getText());
                startActivityForResult(ct, 100);
                break;
            /**
             * 提交数据 包括 添加新地址   还有编辑地址
             */
            case R.id.add_new_address_save_btn:
                String address1 = address1_et.getText().toString().trim();
                String address2 = address2_et.getText().toString().trim();
                String city = city_et.getText().toString().trim();
                String state = state_et.getText().toString().trim();
                String code = code_et.getText().toString().trim();
                String country = country_tv.getText().toString().trim();
                String firstname = firstname_et.getText().toString().trim();
                String lastname = lastname_et.getText().toString().trim();
                String profix = profix_et.getText().toString().trim();
                String phone_number = phone_number_et.getText().toString().trim();
                if (TextUtils.isEmpty(address1)) {
                    showToast(R.string.street_address1);
                } else if (TextUtils.isEmpty(city)) {
                    showToast(R.string.city);
                } else if (TextUtils.isEmpty(state)) {
                    showToast(R.string.state);
                } else if (TextUtils.isEmpty(code)) {
                    showToast(R.string.zip);
                } else if (TextUtils.isEmpty(firstname)) {
                    showToast(R.string.first_name);
                } else if (TextUtils.isEmpty(phone_number)) {
                    showToast(R.string.phone_number);
                } else {
                    String email = CacheUtils.getString(this, "email");
                    if ("SAAEDIT".equals(flags)) {
                        ShippingAddrBean sb = (ShippingAddrBean) getIntent().getParcelableExtra("ShippingAddrBean");
                        sb.setAddress1(address1);
                        sb.setAddress2(address2);
                        sb.setCity(city);
                        sb.setCountry(country);
                        sb.setZipCode(code);
                        sb.setState(state);
                        sb.setFirstName(firstname);
                        sb.setLastName(lastname);
                        sb.setPhone(profix + " " + phone_number);
                        controller.getServiceManager().getAasService().editAddress(sb.getId(), firstname, lastname, country, state, city, code, profix + " " + phone_number, email, address1, address2, String.valueOf(sb.getIsDefault()), sb, this);
                    } else {
                        controller.getServiceManager().getAasService().addAddress(firstname, lastname, country, state, city, code, profix + " " + phone_number, email, address1, address2, "0", this);
                    }
                }
                break;

        }
    }

    /**
     * 成功添加和编辑地址
     *
     * @param action    当前操作
     * @param bandObj
     * @param returnObj 返回对象
     */
    @Override
    public void serviceSuccess(ActionTypes action, Object bandObj, Object returnObj) {
        switch (action) {
            case ADD_ADDRESS:
            case EDIT_ADDRESS:
                ShippingAddrBean sb = (ShippingAddrBean) returnObj;
                ILog.e("success", sb.getCountry());
                ArrayList<ShippingAddrBean> shippingAddrBeanList = (ArrayList<ShippingAddrBean>) bandObj;
                Intent intent = new Intent();
                intent.putExtra("ShippingAddrBean", sb);
                intent.putParcelableArrayListExtra("shippingAddrBeanList", shippingAddrBeanList);
                setResult(1, intent);
                finish();
                break;
            case COUNTRY_INFORMATION:
                mCountryInfor = (CountryInfor) returnObj;
                updateContryCodeAndState();


                break;
            case STATE_INFORMATION:
                stateMaps = (HashMap<String, ArrayList<StateInfo.ListEntity>>) returnObj;
                restStateInfo();
                break;
        }
    }


    private void updateContryCodeAndState() {
        if (mCountryInfor == null) return;

        int total = mCountryInfor.getTotal();
        if (total > 0) {
            ArrayList<CountryInforBean> datas = mCountryInfor.getList();
            if (datas != null && datas.size() > 0) {
                for (int i = 0; i < datas.size(); i++) {
                    CountryInforBean cb = datas.get(i);
                    String twoLetterIso = cb.getTwoLetterIso();
                    if (iso3Country != null && iso3Country.equalsIgnoreCase(twoLetterIso)) {
                        resetLocationInfo(cb.getCountryName(), twoLetterIso, cb.getTelcode());
                        break;
                    }
                    String countryName = country_tv.getText().toString();
                    if (countryName != null && countryName.equalsIgnoreCase(cb.getCountryName())) {
                        resetLocationInfo(cb.getCountryName(), twoLetterIso, cb.getTelcode());
                        break;
                    }
                }
            }
        }

    }


    private void resetLocationInfo(String countryName, String countryTwoISO, String telcode) {
        isInvariability = country_tv.getText().equals(countryName);
        country_tv.setText(countryName);
        country_tv.setTag(countryTwoISO);
        restStateInfo();
        if (TextUtils.isEmpty(telcode)) {
            profix_et.setText("+");
        } else {
            if (telcode.startsWith("+")) {
                profix_et.setText(telcode);
            } else {
                profix_et.setText("+" + telcode);
            }
        }

    }


    private void restStateInfo() {
        if (!isInvariability) {
            state_et.setText("");
        }
        isInvariability = true;
        final String countryCode = (String) country_tv.getTag();

        if (stateMaps == null || countryCode == null || stateMaps.get(countryCode) == null) {
            state_et.setFocusableInTouchMode(true);
            state_et.setFocusable(true);
            state_et.setCompoundDrawables(null, null, null, null);
            state_et.setOnClickListener(null);
            return;
        }
        Drawable vDrawable = getResources().getDrawable(R.mipmap.icon_arrow);
        vDrawable.setBounds(0, 0, vDrawable.getMinimumHeight(), vDrawable.getMinimumWidth());
        state_et.setFocusable(false);
        state_et.setFocusableInTouchMode(false);
        state_et.setCompoundDrawables(null, null, vDrawable, null);

        state_et.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent vIntent = new Intent(AddNewAddressActivity.this, StateActivity.class);

                vIntent.putParcelableArrayListExtra("stateList", stateMaps.get(countryCode));
                startActivityForResult(vIntent, 100);

            }
        });

    }

    /**
     * 联网失败  或者  添加和编辑失败
     *
     * @param action    当前操作
     * @param returnObj 返回对象
     * @param errorCode
     */
    @Override
    public void serviceFailure(ActionTypes action, Object returnObj, int errorCode) {
        if (errorCode == ErrorCodeUtils.ERROR_NO_RESPONSE) {
            showToast(R.string.no_network);
            return;
        }
        switch (action) {
            case ADD_ADDRESS:
            case EDIT_ADDRESS:
                showToast((String) returnObj);
                break;
            case COUNTRY_INFORMATION:
                break;
        }
    }

    @Override
    public void serviceCallback(ActionTypes action, int type, Object returnObj) {
        switch (action) {
            case COUNTRY_INFORMATION:
                mCountryInfor = (CountryInfor) returnObj;
                updateContryCodeAndState();
                break;
            case STATE_INFORMATION:
                stateMaps = (HashMap<String, ArrayList<StateInfo.ListEntity>>) returnObj;
                restStateInfo();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == ConstantsUtils.SELECT_COUNTRY_RESULT_CODE) {

            String countryname = data.getStringExtra("countryname");
            String telecode = data.getStringExtra("telecode");
            String twoLetterIso = data.getStringExtra("twoLetterIso");
            resetLocationInfo(countryname, twoLetterIso, telecode);
            return;
        }

        if (requestCode == 100 && resultCode == ConstantsUtils.SELECR_STATE_RESULT_CODE) {
            String stateCode = data.getStringExtra("code");
            state_et.setText(stateCode);
        }
    }

    @Override
    protected void onDestroy() {
        lm.removeUpdates(listener);
        super.onDestroy();
    }


    /**
     * Creates an intent, adds location data to it as an extra, and starts the intent service for
     * fetching an address.
     */
    protected void startIntentService(Location location) {
        // Create an intent for passing to the intent service responsible for fetching the address.
        Intent intent = new Intent(this, FetchAddressIntentService.class);

        // Pass the result receiver as an extra to the service.
        intent.putExtra(RECEIVER, mResultReceiver);

        // Pass the location data as an extra to the service.
        intent.putExtra(LOCATION_DATA_EXTRA, location);

        // Start the service. If the service isn't already running, it is instantiated and started
        // (creating a process for it if needed); if it is running then it remains running. The
        // service kills itself automatically once all intents are processed.
        startService(intent);
    }


    /**
     * Receiver for data sent from FetchAddressIntentService.
     */
    class AddressResultReceiver extends ResultReceiver {
        public AddressResultReceiver(Handler handler) {
            super(handler);
        }

        /**
         * Receives data sent from FetchAddressIntentService and updates the UI in MainActivity.
         */
        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            if(isFinishing() || isDestroyed()) {
                return;
            }
            // Display the address string or an error message sent from the intent service.
            String mAddressOutput = resultData.getString(RESULT_DATA_KEY);
            // Show a toast message if an address was found.
            ILog.d("TB","resultCode="+resultCode+"====>mAddressOutput="+mAddressOutput);
            if (resultCode == SUCCESS_RESULT) {
                iso3Country = mAddressOutput;
                updateContryCodeAndState();
            }


        }
    }
}

package com.wf.irulu.module.advertising.activity;

import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.wf.irulu.IruluApplication;
import com.wf.irulu.R;
import com.wf.irulu.common.base.BaseActivity;
import com.wf.irulu.common.bean.AdInfo;
import com.wf.irulu.common.utils.ExitionUtil;
import com.wf.irulu.common.utils.ILog;
import com.wf.irulu.logic.db.DbHelper;

/**
 * @描述: 启动广告信息
 * @项目名: irulu1.2
 * @包名:com.wf.irulu.module.advertising.activity
 * @类名:AdvertisingInfoActivity
 * @作者: 左杰
 * @创建时间:2015/11/25 12:11
 */
public class AdvertisingInfoActivity extends BaseActivity {

    private ImageView mAdvertisingImage;
    private TextView mAdvertisingTime;
    private Button mAdvertisingBtn;
    private AdInfo mAdInfo;
    private int mTime;

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_advertising_info);
    }

    @Override
    public void initView() {
        mAdInfo = getIntent().getParcelableExtra("adInfo");
        mAdvertisingImage = (ImageView) findViewById(R.id.advertising_image);
        mAdvertisingTime = (TextView) findViewById(R.id.advertising_time);
        mAdvertisingBtn = (Button) findViewById(R.id.advertising_btn);
    }

    @Override
    public void initData() {
        String image = mAdInfo.getImage();
        mTime = mAdInfo.getTime();
        IruluApplication.getInstance().getPicasso().load(image).into(mAdvertisingImage);
        mAdvertisingTime.setText(String.valueOf(mTime));
        timer.onTick(4000);
        timer.start();
    }

    @Override
    public void onClick(View v) {

    }

    public static void startAdvertisingInfoActivity(Context context,AdInfo adInfo){
        Intent intent = new Intent(context,AdvertisingInfoActivity.class);
        intent.putExtra("adInfo", adInfo);
        context.startActivity(intent);
    }

    CountDownTimer timer = new CountDownTimer(3000,1000) {
        @Override
        public void onTick(long millisUntilFinished) {
            long l = millisUntilFinished / 1000;
            ILog.i("AdvertisingInfoActivity","=onTick-=-="+l);
            mAdvertisingTime.setText(String.valueOf(l));
        }

        @Override
        public void onFinish() {
            ILog.i("AdvertisingInfoActivity","=onFinish-=-=");
        }
    };

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //捕获按下返回键事件
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            //判断在此页面之前是否有页面
            if (!ExitionUtil.getInstance().isRecoedAble()) { //当前页面为最深度的页面或者前一个被返回的页面已超时无需再记录
                //设置此页为Exit Page
                DbHelper.getInstance().updateExitPage(IruluApplication.getInstance().getNo(), "AdvertisingInfoActivity");
                ExitionUtil.getInstance().setEnable();
            } /** else {
             仍然将之前的页面设置为Exit Page
             } **/
        }

        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onResume() {
        super.onResume();

        DbHelper.getInstance().updateCurrentPage(IruluApplication.getInstance().getNo(), "AdvertisingInfoActivity");
    }
}

package com.wf.irulu;

import android.content.Intent;
import android.view.KeyEvent;
import android.view.View;

import com.wf.irulu.common.base.BaseActivity;
import com.wf.irulu.common.utils.CacheUtils;
import com.wf.irulu.common.utils.ExitionUtil;
import com.wf.irulu.framework.HomeActivity;
import com.wf.irulu.logic.db.DbHelper;

/**
 * 
 * @描述: 引导页
 *
 */
public class RefrenceActivity extends BaseActivity{

	@Override
	public void setContentView() {
		setContentView(R.layout.activity_refrence);
	}

	@Override
	public void initView() {
		findViewById(R.id.refrence_bt_start).setOnClickListener(this);
	}

	@Override
	public void initData() {

	}

	@Override
	public void onClick(View v) {
		CacheUtils.setBoolean(this,getString(R.string.version_name) + "isfirst",false);
		switch (v.getId()){
			case R.id.refrence_bt_start:
				Intent intent = new Intent(this, HomeActivity.class);
				startActivity(intent);
				break;
			default:
				break;
		}
		finish();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		//捕获按下返回键事件
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			//判断在此页面之前是否有页面
			if (!ExitionUtil.getInstance().isRecoedAble()) { //当前页面为最深度的页面或者前一个被返回的页面已超时无需再记录
				//设置此页为Exit Page
				DbHelper.getInstance().updateExitPage(IruluApplication.getInstance().getNo(), "RefrenceActivity");
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
		DbHelper.getInstance().updateCurrentPage(IruluApplication.getInstance().getNo(), "RefrenceActivity");
	}
}

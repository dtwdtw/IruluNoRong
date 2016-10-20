package com.wf.irulu.module.product.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.wf.irulu.IruluApplication;
import com.wf.irulu.R;
import com.wf.irulu.common.base.BaseActivity;
import com.wf.irulu.common.utils.ExitionUtil;
import com.wf.irulu.common.view.MutipleTouchViewPager;
import com.wf.irulu.logic.db.DbHelper;
import com.wf.irulu.module.product.adapter.ImagePagerAdapter;
import com.wf.irulu.module.product.listener.LayoutVisibilityListener;

import java.util.ArrayList;


/***
 * 查看大图UI
 * 
 * @author tony.liu
 *
*/
public class ShowPhotosActivity extends BaseActivity implements LayoutVisibilityListener{
	
	private MutipleTouchViewPager viewPager;
	private ImagePagerAdapter imagePagerAdapter;
	private String type ="";
	private int index;
	private ArrayList<String> photos = new ArrayList<String>();
	private RadioGroup mFullRgIndicator;
	private ImageView mFullIvBack;
	// 是否全屏显示
	private boolean isFull = false;

	@Override
	public void setContentView() {
		setContentView(R.layout.activity_show_photos);
	}

	@Override
	public void initView() {
		photos = (ArrayList<String>) getIntent().getSerializableExtra("photos");
		type = getIntent().getStringExtra("type");
		index = getIntent().getIntExtra("index", 0);
		if ("default".equals(photos.get(photos.size() - 1))) {
			photos.remove(photos.size() - 1);
		}
		mFullIvBack = bindView(R.id.full_iv_back);
		mFullIvBack.setOnClickListener(this);
		viewPager = bindView(R.id.pager);
		mFullRgIndicator = bindView(R.id.full_rg_indicator);
		int size = photos.size();
		RadioButton rb = null;
		for (int i = 0; i < size; i++){
			rb = (RadioButton) getLayoutInflater().inflate(R.layout.view_radiobutton_productdetail, null);
			mFullRgIndicator.addView(rb);
		}
		mFullRgIndicator.check(mFullRgIndicator.getChildAt(0).getId());
	}

	@Override
	public void initData() {
		imagePagerAdapter = new ImagePagerAdapter(this, photos,type);
		imagePagerAdapter.setOnLayoutVisibilityListener(this);
		viewPager.setAdapter(imagePagerAdapter);
		viewPager.addOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageSelected(int arg0) {
				mFullRgIndicator.check(mFullRgIndicator.getChildAt(arg0).getId());
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
			}
		});
		viewPager.setCurrentItem(index);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.full_iv_back:
			finish();
			break;
		default:
			break;
		}
	}


	
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}


	public static void startShowPhotosActivity(Activity activity,ArrayList<String> photos,String type,int index){
		Intent intent = new Intent(activity,ShowPhotosActivity.class);
		intent.putStringArrayListExtra("photos", photos);
		intent.putExtra("type", type);
		intent.putExtra("index", index);
		activity.startActivity(intent);
	}

	@Override
	public void layoutVisibility() {
		// 判断是商品详情页的全屏看图模式
		if (!"type".equals(type)) {
			// 如果处于全屏状态
			if (isFull) {
				mFullRgIndicator.setVisibility(View.VISIBLE);
				mFullIvBack.setVisibility(View.VISIBLE);
			} else {
				mFullRgIndicator.setVisibility(View.INVISIBLE);
				mFullIvBack.setVisibility(View.INVISIBLE);
			}
			isFull = !isFull;
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		//捕获按下返回键事件
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			//判断在此页面之前是否有页面
			if (!ExitionUtil.getInstance().isRecoedAble()) { //当前页面为最深度的页面或者前一个被返回的页面已超时无需再记录
				//设置此页为Exit Page
				DbHelper.getInstance().updateExitPage(IruluApplication.getInstance().getNo(), "ShowPhotosActivity");
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

		DbHelper.getInstance().updateCurrentPage(IruluApplication.getInstance().getNo(), "ShowPhotosActivity");
	}
}

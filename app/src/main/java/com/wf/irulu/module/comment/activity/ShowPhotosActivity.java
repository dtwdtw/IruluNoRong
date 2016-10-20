package com.wf.irulu.module.comment.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.wf.irulu.IruluApplication;
import com.wf.irulu.R;
import com.wf.irulu.common.base.BaseActivity;
import com.wf.irulu.common.utils.ExitionUtil;
import com.wf.irulu.common.view.MutipleTouchViewPager;
import com.wf.irulu.logic.db.DbHelper;
import com.wf.irulu.module.comment.adapter.ImagePagerAdapter;

import java.util.ArrayList;


/***
 * 查看大图UI
 * 
 * @author tony.liu
 *
*/
public class ShowPhotosActivity extends BaseActivity {
	
	private MutipleTouchViewPager viewPager;
	private TextView imageshow_tv_indicatorsum;
	private TextView imageshow_tv_indicatornow;
	private ImagePagerAdapter imagePagerAdapter;
	private String type ="";
	private int index;
	private ArrayList<String> photos = new ArrayList<String>();
	
	public static void startShowPhotosActivity(Activity activity,ArrayList<String> photos,String type,int index){
		Intent intent = new Intent(activity,ShowPhotosActivity.class);
		intent.putStringArrayListExtra("photos", photos);
		intent.putExtra("type", type);
		intent.putExtra("index", index);
		activity.startActivity(intent);
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back_icon_tv:
			finish();
			break;
		default:
			break;
		}
	}

	@Override
	public void setContentView() {
		setContentView(R.layout.activity_show_photos_layout);
	}

	@Override
	public void initData() {
		
		imageshow_tv_indicatornow.setText((index + 1) + "");
		imageshow_tv_indicatorsum.setText(photos.size() + "");
		
		imagePagerAdapter = new ImagePagerAdapter(this, photos,type);
		viewPager.setAdapter(imagePagerAdapter);
		viewPager.setCurrentItem(index);
		
		viewPager.setOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageSelected(int arg0) {
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				imageshow_tv_indicatornow.setText((arg0 + 1) + "");
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
			}
		});
	}

	@Override
	public void initView() {
		photos = (ArrayList<String>) getIntent().getSerializableExtra("photos");
		type = getIntent().getStringExtra("type");
		index = getIntent().getIntExtra("index", 0);
		if ("default".equals(photos.get(photos.size() - 1))) {
			photos.remove(photos.size() - 1);
		}
		imageshow_tv_indicatorsum = (TextView) findViewById(R.id.imageshow_tv_indicatorsum);
		imageshow_tv_indicatornow = (TextView) findViewById(R.id.imageshow_tv_indicatornow);
		viewPager = (MutipleTouchViewPager) findViewById(R.id.pager);
	}
	
	@Override
	protected void onDestroy() {
		viewPager = null;
		imagePagerAdapter = null;
		super.onDestroy();
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

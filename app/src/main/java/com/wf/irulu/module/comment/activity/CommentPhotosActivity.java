package com.wf.irulu.module.comment.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wf.irulu.R;
import com.wf.irulu.common.base.CommonTitleBaseActivity;
import com.wf.irulu.common.utils.DataCleanManager;
import com.wf.irulu.common.utils.ImageCompress;
import com.wf.irulu.common.view.MutipleTouchViewPager;
import com.wf.irulu.module.comment.adapter.ImagePagerAdapter;
import com.wf.irulu.module.comment.listener.LayoutVisibilityListener;

import java.io.File;
import java.util.ArrayList;

/**
 * @描述: 写评论的查看图片Activity
 *
 * @项目名: irulu
 * @包名:com.wf.irulu.module.comment.activity
 * @类名:CommentPhotosActivity
 * @作者: 左西杰
 * @创建时间:2015-9-10 下午6:56:50
 * 
 */
public class CommentPhotosActivity extends CommonTitleBaseActivity implements LayoutVisibilityListener {
	
	private MutipleTouchViewPager viewPager;
	private TextView imageshow_tv_indicatorsum;
	private TextView imageshow_tv_indicatornow;
	private ImagePagerAdapter imagePagerAdapter;
	private String type ="";
	private int index;
	private ArrayList<String> photos = new ArrayList<String>();
	private LinearLayout title_layout;
	private RelativeLayout bottom_layout;
	private int currentIndex = 0;
	public static final int ENSURE_PICS_RESULT = 9090;
	/**
	 * 此标记判断Title布局和底部布局是否显示，true：显示，false：隐藏
	 */
	private Boolean isShow = false;
	
	@Override
	public void setContentView() {
		setContentView(R.layout.activity_comment_photos);

	}
	@Override
	protected String setWindowTitle() {
		return "Picture";
	}
	@Override
	public void initView() {
		mCommonTitleCartNum.setVisibility(View.GONE);
		commonTitleSearch.setVisibility(View.GONE);
		photos = (ArrayList<String>) getIntent().getSerializableExtra("photos");
		type = getIntent().getStringExtra("type");
		index = getIntent().getIntExtra("index", 0);
		if ("default".equals(photos.get(photos.size() - 1))) {
			photos.remove(photos.size() - 1);
		}
		imageshow_tv_indicatorsum = (TextView) findViewById(R.id.imageshow_tv_indicatorsum);
		imageshow_tv_indicatornow = (TextView) findViewById(R.id.imageshow_tv_indicatornow);
		title_layout = (LinearLayout) findViewById(R.id.title_layout);
		bottom_layout = (RelativeLayout) findViewById(R.id.bottom_layout);
		viewPager = (MutipleTouchViewPager) findViewById(R.id.pager);

		commonTitleBack.setOnClickListener(this);
		commonTitleCart.setOnClickListener(this);
	}

	@Override
	public void initData() {
		imageshow_tv_indicatornow.setText((index + 1) + "");
		imageshow_tv_indicatorsum.setText(photos.size() + "");
		
		imagePagerAdapter = new ImagePagerAdapter(this, photos,type);
		viewPager.setAdapter(imagePagerAdapter);
		viewPager.setCurrentItem(index);
		imagePagerAdapter.setOnLayoutVisibilityListener(this);
		
		viewPager.setOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				
			}
			
			@Override
			public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
				currentIndex = position;
				imageshow_tv_indicatornow.setText((position + 1) + "");
				
			}
			
			@Override
			public void onPageScrollStateChanged(int state) {
				
			}
		});
		viewPager.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
			}
		});
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.commontitle_iv_back:
			finish();
			break;
		case R.id.commontitle_bt_cart:
			photos.remove(currentIndex);
			if(photos.size() == 0){
				finish();
			}
			imageshow_tv_indicatorsum.setText(photos.size() + "");
			imagePagerAdapter.setNotifyDataSetChanged(photos,type);
			viewPager.setCurrentItem(currentIndex);
			
			controller.postNotifyPhotoSetChangedCallback(photos);
			break;
		case R.id.back_icon_tv:
			finish();
			break;
		default:
			break;
		}
	}
	
	public static void startCommentPhotosActivity(Activity activity,ArrayList<String> photos,String type,int index){
		Intent intent = new Intent(activity,CommentPhotosActivity.class);
		intent.putStringArrayListExtra("photos", photos);
		intent.putExtra("type", type);
		intent.putExtra("index", index);
		activity.startActivity(intent);
	}

	@Override
	public void layoutVisibility() {
		if(isShow){
			title_layout.setVisibility(View.VISIBLE);
			bottom_layout.setVisibility(View.VISIBLE);
		}else{
			title_layout.setVisibility(View.GONE);
			bottom_layout.setVisibility(View.GONE);
		}
		isShow = !isShow;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		//循环删除本地压缩后的图片
		if(null != photos){
			int size = photos.size();
			if (photos != null && size != 0) {
				for (int i = 0; i < size; i++) {
					File compressedFile = ImageCompress.getDefaultCompressFile(photos.get(i));
					//删除本地压缩后的图片
					DataCleanManager.deleteFolderFile(compressedFile.getAbsolutePath(), true);
				}
			}
		}
	}
}

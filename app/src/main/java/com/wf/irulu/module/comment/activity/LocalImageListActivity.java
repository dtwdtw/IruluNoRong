package com.wf.irulu.module.comment.activity;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore.Images;
import android.provider.MediaStore.Images.ImageColumns;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.wf.irulu.IruluApplication;
import com.wf.irulu.R;
import com.wf.irulu.common.base.BaseActivity;
import com.wf.irulu.common.bean.PhotoDirInfo;
import com.wf.irulu.common.utils.ConstantsUtils;
import com.wf.irulu.common.utils.ExitionUtil;
import com.wf.irulu.logic.db.DbHelper;
import com.wf.irulu.module.comment.adapter.ImageListAdapter;

import java.util.ArrayList;

/**
 * 本地相册列表UI
 * 
 * @author tony.liu
 * 
 */
public class LocalImageListActivity extends BaseActivity {

	private ArrayList<String> selectedDataList = new ArrayList<String>();
	private ListView listView;
	private ImageListAdapter adapter;
	private Context mContext;
	private ArrayList<PhotoDirInfo> mDirInfos;
	private final String IMG_JPG = "image/jpg";
	private final String IMG_JPEG = "image/jpeg";
	private final String IMG_PNG = "image/png";
	private final String IMG_GIF = "image/gif";

	@Override
	public void setContentView() {
		setContentView(R.layout.activity_local_iamgelist_layout);
		mContext = this;
	}

	@Override
	public void initData() {
		final int max = getIntent().getIntExtra("max", 5);
		mDirInfos = getImageDir(mContext);
		adapter = new ImageListAdapter(this, mDirInfos);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
				PhotoDirInfo photoDirInfo = mDirInfos.get(position);
				Intent intent = new Intent(mContext, AlbumActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
				Bundle bundle = new Bundle();
				bundle.putStringArrayList("dataList", selectedDataList);
				bundle.putString("bucketId", photoDirInfo.getDirId());
				bundle.putInt("max", max);
				intent.putExtras(bundle);
				startActivityForResult(intent, 3);
			}
		});
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.back_icon_tv:
			finish();
			break;
		default:
			break;
		}
	}

	private synchronized ArrayList<PhotoDirInfo> getImageDir(Context context) {
		ArrayList<PhotoDirInfo> list = null;
		PhotoDirInfo dirInfo = null;
		ContentResolver mResolver = context.getContentResolver();
		String[] IMAGE_PROJECTION = new String[] { ImageColumns.BUCKET_ID,ImageColumns.BUCKET_DISPLAY_NAME, ImageColumns.DATA,"COUNT(*) AS " + ImageColumns.DATA };
		String selection = " 1=1 AND " + ImageColumns.MIME_TYPE+ " IN (?,?,?)) GROUP BY (" + ImageColumns.BUCKET_ID+ ") ORDER BY (" + ImageColumns.BUCKET_DISPLAY_NAME;
		String[] selectionArgs = new String[] { IMG_JPG, IMG_JPEG, IMG_PNG };
		Cursor cursor = mResolver.query(Images.Media.EXTERNAL_CONTENT_URI,IMAGE_PROJECTION, selection, selectionArgs, null);
		if (null != cursor) {
			if (cursor.getCount() > 0) {
				list = new ArrayList<PhotoDirInfo>();
				while (cursor.moveToNext()) {
					dirInfo = new PhotoDirInfo();
					dirInfo.setDirId(cursor.getString(0));
					dirInfo.setDirName(cursor.getString(1));
					dirInfo.setFirstPicPath(cursor.getString(2));
					dirInfo.setPicCount(cursor.getInt(3));
					dirInfo.setUserOtherPicSoft(false);
					list.add(dirInfo);
				}
			}
			cursor.close();
		}
		return list;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			if (requestCode == 3) {
				Bundle bundle = data.getExtras();
				ArrayList<String> tDataList = (ArrayList<String>) bundle.getSerializable("dataList");
				if (tDataList != null) {
					selectedDataList.clear();
					selectedDataList.addAll(tDataList);
					Intent intent = new Intent();
					intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					intent.putExtra("photos",selectedDataList);
					setResult(ConstantsUtils.RESULT_CODE_PHOTOS, intent);
					finish();
				}
			}
		}
	}

	@Override
	public void onBackPressed() {
		Intent intent = new Intent();
		Bundle bundle = new Bundle();
		bundle.putStringArrayList("dataList", selectedDataList);
		intent.putExtras(bundle);
		setResult(RESULT_OK, intent);
		finish();
	}

	@Override
	public void initView() {
		Intent intent = getIntent();
		selectedDataList = intent.getStringArrayListExtra("list");
		if ("default".equals(selectedDataList.get(selectedDataList.size() - 1))) {
			selectedDataList.remove(selectedDataList.size() - 1);
		}
		
//		mTitleTV = (TextView) findViewById(R.id.title_tv);
//		mMenuTV = (TextView) findViewById(R.id.menu_tv);
//		mMenuTV.setVisibility(View.GONE);
//		mTitleTV.setWindowTitle("本地相册");
		findViewById(R.id.back_icon_tv).setOnClickListener(this);
		listView = (ListView) findViewById(R.id.imageListView);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		//捕获按下返回键事件
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			//判断在此页面之前是否有页面
			if (!ExitionUtil.getInstance().isRecoedAble()) { //当前页面为最深度的页面或者前一个被返回的页面已超时无需再记录
				//设置此页为Exit Page
				DbHelper.getInstance().updateExitPage(IruluApplication.getInstance().getNo(), "LocalImageListActivity");
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

		DbHelper.getInstance().updateCurrentPage(IruluApplication.getInstance().getNo(), "LocalImageListActivity");
	}
}

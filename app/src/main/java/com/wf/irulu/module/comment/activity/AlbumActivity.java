package com.wf.irulu.module.comment.activity;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore.Images;
import android.provider.MediaStore.Images.ImageColumns;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.squareup.picasso.Picasso;
import com.wf.irulu.IruluApplication;
import com.wf.irulu.R;
import com.wf.irulu.common.base.BaseActivity;
import com.wf.irulu.common.utils.ConstantsUtils;
import com.wf.irulu.common.utils.ExitionUtil;
import com.wf.irulu.logic.db.DbHelper;
import com.wf.irulu.module.comment.adapter.AlbumGridViewAdapter;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

/** 
 * @ClassName: AlbumActivity 
 * @Description: TODO() 
 * @author tony.liu
 */

public class AlbumActivity extends BaseActivity {
	
	public static final int SELECT_OK = 0;
	
//	private TextView mTitleTV;
//	private TextView mMenuTV;
	
	private GridView gridView;
	private ArrayList<String> dataList = new ArrayList<String>();
	private HashMap<String, ImageView> hashMap = new HashMap<String, ImageView>();
	private ArrayList<String> selectedDataList = new ArrayList<String>();
	
	private String bucketId = "";
	private final String IMG_JPG = "image/jpg";
	private final String IMG_JPEG = "image/jpeg";
	private final String IMG_PNG = "image/png";
	private final String IMG_GIF = "image/gif";

	private ProgressBar progressBar;
	private AlbumGridViewAdapter gridImageAdapter;
	private LinearLayout selectedImageLayout;
	private Button okButton;
	private HorizontalScrollView scrollview;
	private Context mContext;
	private Picasso mPicasso;

	private int max;
	
	@Override
	public void setContentView() {
		setContentView(R.layout.activity_album_layout);
		mContext = this;
		mPicasso = Picasso.with(mContext);
	}

	@Override
	public void initData() {
		initListener();
	}

	private void initSelectImage() {
		if (selectedDataList == null){
			return;
		}
		for (final String path : selectedDataList) {
			ImageView imageView = (ImageView) LayoutInflater.from(AlbumActivity.this).inflate(R.layout.row_choose_imageview_layout,selectedImageLayout, false);
			selectedImageLayout.addView(imageView);
			hashMap.put(path, imageView);
			mPicasso.load(new File(path)).resize(70, 70).into(imageView);
			imageView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					removePath(path);
					gridImageAdapter.notifyDataSetChanged();
				}
			});
		}
		okButton.setText("(" + selectedDataList.size() + "/" + max + ")");
	}

	private void initListener() {
		final int width_every = (ConstantsUtils.DISPLAYW - okButton.getMeasuredWidth() - 20) / 4;
		gridImageAdapter.setOnItemClickListener(new AlbumGridViewAdapter.OnItemClickListener() {
			@Override
			public void onItemClick(final CheckBox checkBox,int position, final String path, boolean isChecked) {
				if (selectedDataList.size() >= max) {
					checkBox.setChecked(false);
					if (!removePath(path)) {
//						ToastUtils.showToast("只能选择9张图片", ToastStatus.OK);
						showToast("Most "+ max +" pictures you can choose!");
					}
					return;
				}
				if (isChecked) {
					if (!hashMap.containsKey(path)) {
						ImageView imageView = (ImageView) LayoutInflater.from(AlbumActivity.this).inflate(R.layout.row_choose_imageview_layout,selectedImageLayout, false);
						selectedImageLayout.addView(imageView);
						imageView.postDelayed(new Runnable() {
							@Override
							public void run() {
								int off = selectedImageLayout.getMeasuredWidth()- scrollview.getWidth();
								if (off > 0) {
									scrollview.smoothScrollTo(off, 0);
								}
							}
						}, 100);
						hashMap.put(path, imageView);
						selectedDataList.add(path);
						if(width_every >= 0)
							mPicasso.load(new File(path)).resize(width_every, width_every).into(imageView);
						imageView.setOnClickListener(new View.OnClickListener() {
							@Override
							public void onClick(View v) {
								checkBox.setChecked(false);
								removePath(path);
							}
						});
						okButton.setText("Completed ("+ selectedDataList.size() + "/"+max+")");
					}
				} else {
					removePath(path);
				}
			}
		});
		okButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(mContext, LocalImageListActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				Bundle bundle = new Bundle();
				bundle.putStringArrayList("dataList", selectedDataList);
				intent.putExtras(bundle);
				setResult(RESULT_OK, intent);
				finish();
			}
		});
	}

	private boolean removePath(String path) {
		if (hashMap.containsKey(path)) {
			selectedImageLayout.removeView(hashMap.get(path));
			hashMap.remove(path);
			removeOneData(selectedDataList, path);
			okButton.setText("完成(" + selectedDataList.size() + "/"+max+")");
			return true;
		} else {
			return false;
		}
	}

	private void removeOneData(ArrayList<String> arrayList, String s) {
		for (int i = 0; i < arrayList.size(); i++) {
			if (arrayList.get(i).equals(s)) {
				arrayList.remove(i);
				return;
			}
		}
	}

	private void refreshData() {
		new AsyncTask<Void, Void, ArrayList<String>>() {
			@Override
			protected void onPreExecute() {
				progressBar.setVisibility(View.VISIBLE);
				super.onPreExecute();
			}
			@Override
			protected ArrayList<String> doInBackground(Void... params) {
				ArrayList<String> tmpList = new ArrayList<String>();
				ContentResolver mResolver = mContext.getContentResolver();
				String[] IMAGE_PROJECTION = new String[] { ImageColumns.DATA };
				String selection = ImageColumns.BUCKET_ID + "= ?  AND "+ ImageColumns.MIME_TYPE + " IN (?,?,?)";
				String[] selectionArgs = new String[] { bucketId, IMG_JPG,IMG_JPEG, IMG_PNG };
				Cursor cursor = mResolver.query(Images.Media.EXTERNAL_CONTENT_URI, IMAGE_PROJECTION,selection, selectionArgs, null);
				if (null != cursor) {
					if (cursor.getCount() > 0) {
						while (cursor.moveToNext()) {
							tmpList.add(cursor.getString(0));
						}
					}
					cursor.close();
				}
				return tmpList;
			}

			protected void onPostExecute(ArrayList<String> tmpList) {
				if (AlbumActivity.this == null || AlbumActivity.this.isFinishing()) {
					return;
				}
				progressBar.setVisibility(View.GONE);
				dataList.clear();
				dataList.addAll(tmpList);
				gridImageAdapter.notifyDataSetChanged();
				return;
			};
		}.execute();
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
	protected void onDestroy() {
		super.onDestroy();
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
	public void initView() {
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		selectedDataList = (ArrayList<String>) bundle.getSerializable("dataList");
		bucketId = bundle.getString("bucketId");
		max = bundle.getInt("max", 5);
		
//		mTitleTV = (TextView) findViewById(R.id.title_tv);
//		mMenuTV = (TextView) findViewById(R.id.menu_tv);
//		mMenuTV.setVisibility(View.GONE);
//		mTitleTV.setWindowTitle("选择图片");
		findViewById(R.id.back_icon_tv).setOnClickListener(this);
		
		progressBar = (ProgressBar) findViewById(R.id.progressbar);
		progressBar.setVisibility(View.GONE);
		gridView = (GridView) findViewById(R.id.myGrid);
		
		gridImageAdapter = new AlbumGridViewAdapter(this, dataList,selectedDataList);
		gridView.setAdapter(gridImageAdapter);
		refreshData();
		
		selectedImageLayout = (LinearLayout) findViewById(R.id.selected_image_layout);
		okButton = (Button) findViewById(R.id.ok_button);
		scrollview = (HorizontalScrollView) findViewById(R.id.scrollview);

		initSelectImage();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		//捕获按下返回键事件
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			//判断在此页面之前是否有页面
			if (!ExitionUtil.getInstance().isRecoedAble()) { //当前页面为最深度的页面或者前一个被返回的页面已超时无需再记录
				//设置此页为Exit Page
				DbHelper.getInstance().updateExitPage(IruluApplication.getInstance().getNo(), "AlbumActivity");
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

		DbHelper.getInstance().updateCurrentPage(IruluApplication.getInstance().getNo(), "AlbumActivity");
	}
}

package com.wf.irulu.module.comment.activity;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.wf.irulu.R;
import com.wf.irulu.common.base.CommonTitleBaseActivity;
import com.wf.irulu.common.view.MutipleTouchViewPager;
import com.wf.irulu.module.comment.adapter.ShowBigPictureAdapter;

import java.util.ArrayList;

/**
 * Created by daniel on 2015/12/1.
 */
public class WriteCommentShowPhotosActivity extends CommonTitleBaseActivity {

    private MutipleTouchViewPager pager;
    private LinearLayout ll;
    private int flag = -1;
    private ArrayList<String> datas;
    private ShowBigPictureAdapter adapter;
    private ImageView delete;

    @Override
    protected String setWindowTitle() {
        return "";
    }

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_write_comment_show_photos);
    }

    @Override
    public void initView() {
        commonTitleBack.setVisibility(View.GONE);
        pager = (MutipleTouchViewPager) findViewById(R.id.write_comment_show_photos_vp);
        ll = (LinearLayout) findViewById(R.id.write_comment_point_ll);
        ImageView back = (ImageView) findViewById(R.id.show_picture_back_iv);
        delete = (ImageView) findViewById(R.id.show_picture_delete_iv);
        back.setOnClickListener(this);
        delete.setOnClickListener(this);
    }

    @Override
    public void initData() {
        Intent intent = getIntent();
        datas = intent.getStringArrayListExtra("photos");
        int index = intent.getIntExtra("index", 0);
        flag = index;
        if (datas != null && datas.size() > 0) {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(15, 15);
            for (int i = 0; i < datas.size(); i++) {
                if ("default".equals(datas.get(i))) {
                    datas.remove(i);
                    continue;
                }
                ImageView iv = new ImageView(this);
                if (i != 0) {
                    params.leftMargin = 10;
                }
                if (index == i) {
                    iv.setImageResource(R.mipmap.yema_selected);
                } else {
                    iv.setImageResource(R.mipmap.yema_normal);
                }
                ll.addView(iv, params);
            }
        }


        adapter = new ShowBigPictureAdapter(datas, this);
        pager.setAdapter(adapter);
        pager.setCurrentItem(index);

        pager.addOnPageChangeListener(new MutipleTouchViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                ImageView iv1 = (ImageView) ll.getChildAt(position);
                iv1.setImageResource(R.mipmap.yema_selected);
                if (flag != -1 && datas.size() > flag) {
                    ImageView iv2 = (ImageView) ll.getChildAt(flag);
                    iv2.setImageResource(R.mipmap.yema_normal);
                }
                flag = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.show_picture_delete_iv) {


            delete.setClickable(false);
            int currentItem = pager.getCurrentItem();
            if (datas.size() == 1) {
                datas.remove(currentItem);
                Intent intent = getIntent();
                intent.putStringArrayListExtra("image", datas);
                setResult(12, intent);
                finish();
                return;
            }
            datas.remove(currentItem);
            ll.removeViewAt(currentItem);


            flag = -1;
            pager.setAdapter(adapter);
            if (currentItem > 0) {
                pager.setCurrentItem(currentItem - 1);
            } else {
                pager.setCurrentItem(0);
                flag = 0;
            }
            ImageView iv = (ImageView) ll.getChildAt(pager.getCurrentItem());
            iv.setImageResource(R.mipmap.yema_selected);
            delete.setClickable(true);
        } else if (v.getId() == R.id.show_picture_back_iv) {
            Intent intent = getIntent();
            intent.putStringArrayListExtra("image", datas);
            setResult(12, intent);
            finish();

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mCommonTitleCartNum.setVisibility(View.GONE);
        commonTitleCart.setVisibility(View.GONE);
        commonTitleSearch.setVisibility(View.GONE);
    }

    public static void startShowPhotosActivity(Activity activity, ArrayList<String> photos, String type, int index) {
        Intent intent = new Intent(activity, WriteCommentShowPhotosActivity.class);
        intent.putStringArrayListExtra("photos", photos);
        intent.putExtra("type", type);
        intent.putExtra("index", index);
        activity.startActivityForResult(intent, 11);
    }


    @Override
    public void onBackPressed() {
        Intent intent = getIntent();
        intent.putStringArrayListExtra("image", datas);
        setResult(12, intent);
        finish();
    }
}

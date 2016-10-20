package com.wf.irulu.framework;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.RelativeLayout;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;

import com.bigkoo.convenientbanner.listener.OnItemClickListener;

import com.wf.irulu.R;
import com.wf.irulu.common.base.BaseFragment;
import com.wf.irulu.common.utils.ConstantsUtils;
import com.wf.irulu.common.utils.CustomerAnalystic;
import com.wf.irulu.common.utils.CustomerEvent;
import com.wf.irulu.common.utils.HttpConstantUtils;
import com.wf.irulu.framework.holderview.NetworkImageHolderView;
import com.wf.irulu.module.category.activity.CategoryActivity;
import com.wf.irulu.module.home.activity.ShufflingFigureActivity;
import com.wf.irulu.common.bean.HomeBean;
import com.wf.irulu.common.bean.SlideBean;
import com.wf.irulu.module.product.activity.ProductDetailActivity;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * @描述: 首页页面滑动标签带的基类
 * @项目名: irulu1.2
 * @包名:com.wf.irulu.framework
 * @类名:BaseContentFragment
 * @作者: 左杰
 * @创建时间:2015/10/24 12:16
 */
public abstract class BaseContentFragment extends BaseFragment implements OnItemClickListener {

    public static final String TAG = "BaseContentFragment";

    private List<SlideBean> slideDatas;
    /**
     * 数据模拟
     */
    //private int count = 1;
    //private String[] imgs = {"http://img1.3lian.com/2015/a1/124/d/178.jpg", "http://tupian.enterdesk.com/2015/mxy/6/19/2/10.jpg", "http://cdn.duitang.com/uploads/item/201410/03/20141003000112_t8Nes.jpeg"};
    private ConvenientBanner convenientBanner;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return init();
    }

    public HomeBean sendBundleData(HomeBean homeBean) {
        loadData(homeBean);
        return homeBean;
    }

    protected abstract View init();

    public void loadData(HomeBean homeBean) {
    }

    /**
     * 设置轮播图
     *
     * @param context
     * @param cyclicView 轮播图布局
     * @param slideDatas 数据
     */
    protected void setShufflingFigure(Context context, View cyclicView, List<SlideBean> slideDatas) {
        this.slideDatas = slideDatas;
        convenientBanner = (ConvenientBanner) cyclicView.findViewById(R.id.convenientBanner);
        int height = (int) (ConstantsUtils.DISPLAYW * 35 / 75);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, height);
        convenientBanner.setLayoutParams(layoutParams);

        List<String> urls = new ArrayList<String>();

        int size = slideDatas.size();
        for (int i = 0; i < size; i++) {
            // 获取网络图片
            String picUrl = slideDatas.get(i).getImage();
            String str = "?imageView2/0/w/" + ConstantsUtils.DISPLAYW + "/h/" + height + "/interlace/1/q/75";
            urls.add(picUrl + str);
            Log.d(TAG, "setShufflingFigure() returned: " + picUrl + str);
        }

// 测试
//        for (int i = 0; i < count; i++) {
//            urls.add(imgs[i]);
//        }
//
//        count++;
//
//        if (count > imgs.length) {
//            count = 1;
//        }


        convenientBanner.setPages(new CBViewHolderCreator<NetworkImageHolderView>() {
            @Override
            public NetworkImageHolderView createHolder() {
                return new NetworkImageHolderView();
            }
        }, urls).setPageIndicator(new int[]{R.drawable.ic_page_indicator, R.drawable.ic_page_indicator_focused})
                .setOnItemClickListener(this);

        if (urls.size() < 2) {
            convenientBanner.getViewPager().setCanLoop(false);
            convenientBanner.setPointViewVisible(false);
        }else {
            convenientBanner.setPointViewVisible(true);
            convenientBanner.getViewPager().setCanLoop(true);
        }

    }

    @Override
    public void onItemClick(final int position) {
        SlideBean slide = null;
        Intent slideIntent = new Intent();
        if (null != slideDatas) {
            slide = slideDatas.get(position);
            String type = slide.getType();
            String content = slide.getContent();
            switch (type) {
                case "1"://产品列表
                    slideIntent.setClass(mActivity, CategoryActivity.class);
                    startActivity(slideIntent);
                    break;
                case "2"://跳转到产品详情
                    slideIntent.setClass(mActivity, ProductDetailActivity.class);
                    slideIntent.putExtra("pId", content);
                    startActivity(slideIntent);
                    break;
                case "3"://app内部H5
                    String regEx = "^http(s)?://[^\\\\s]*";
                    Pattern pattern = Pattern.compile(regEx);
                    Matcher matcher = pattern.matcher(content);
                    boolean matches = matcher.matches();//是否是以http://或者以https://开头
                    if (matches) {
                        slideIntent.setClass(mActivity, ShufflingFigureActivity.class);
                        slideIntent.putExtra("url", content);
                        slideIntent.putExtra("title", slide.getContent());
                        startActivity(slideIntent);
                    }
                    break;
                case "4"://H5（跳浏览器）
                    try {
                        content = URLDecoder.decode(content, "UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    slideIntent.setAction(Intent.ACTION_VIEW);
                    slideIntent.setData(Uri.parse(content));
                    startActivity(slideIntent);
                    break;
                default:
                    break;
            }

            CustomerAnalystic.analystic(CustomerEvent.num_of_home_banner_ads, ((HomeActivity) mActivity).mLogger, new HashMap<String, String>() {
                {
                    put("version", HttpConstantUtils.VERSION);
                    // 1 position
                    put("position", String.valueOf(position));
                    // 2 type
                    put("type", slideDatas.get(position).getType());
                    // 3 type
                    put("content", slideDatas.get(position).getContent());
                }
            });
        }
    }


    @Override
    public void onStop() {
        super.onStop();
        // To prevent a memory leak on rotation, make sure to call stopAutoCycle() on the slider before activity or fragment is destroyed
        if (convenientBanner != null) {
            convenientBanner.stopTurning();
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        if (convenientBanner != null) {
            convenientBanner.startTurning(5000);
        }
    }

}

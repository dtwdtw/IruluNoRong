package com.wf.irulu.module.home;

import android.support.v4.util.SparseArrayCompat;
import android.text.TextUtils;

import com.wf.irulu.framework.BaseContentFragment;
import com.wf.irulu.module.home.fragment.MostPopularFragment;
import com.wf.irulu.module.home.fragment.NewReleaseFragment;
import com.wf.irulu.module.homenew1_3_0.DiscoverFragment.DiscoverFragment1_3_0;
import com.wf.irulu.module.homenew1_3_0.HomeLoadingFragment.HomeLoadingFragment;

/**
 * @描述: 获得模块的工厂
 * @项目名: irulu1.2.0
 * @包名:com.wf.irulu.module.contentPage
 * @类名:FragmentFactory
 * @作者: 左杰
 * @创建时间:2015/10/19 21:53
 */
public class FragmentFactory {
    // 缓存
	private static SparseArrayCompat<BaseContentFragment> mCaches = new SparseArrayCompat<BaseContentFragment>();

    public static BaseContentFragment getFragment(String type) {
        BaseContentFragment fragment = null;
        if(!TextUtils.isEmpty(type)){
            int typePostion = Integer.parseInt(type);
            // 去缓存中取
            fragment = mCaches.get(typePostion);
        }
        if (null != fragment) {// 说明缓存中有
            return fragment;
        }

        switch (type) {
            case "1":// Special Offers
                fragment = new DiscoverFragment1_3_0();
                break;
            case "2":// Most Popular
                fragment = new MostPopularFragment();
                break;
            case "3":// New Release
                fragment = new NewReleaseFragment();
                break;
            default://加载页面
                fragment = new HomeLoadingFragment();
                break;
        }

        if(!TextUtils.isEmpty(type)){
            int typePostion = Integer.parseInt(type);
            //缓存起来
            mCaches.put(typePostion,fragment);
        }
        return fragment;
    }
}

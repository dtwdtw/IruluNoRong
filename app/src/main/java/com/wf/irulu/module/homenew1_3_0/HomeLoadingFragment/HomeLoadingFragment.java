package com.wf.irulu.module.homenew1_3_0.HomeLoadingFragment;

import android.view.View;

import com.wf.irulu.R;
import com.wf.irulu.framework.BaseContentFragment;

/**
 * @描述: 主页的空页面
 * @项目名: irulu1.2
 * @包名:com.wf.irulu.module.home.fragment
 * @类名:HomeEmptyFragment
 * @作者: 左杰
 * @创建时间:2015/12/5 16:08
 */
public class HomeLoadingFragment extends BaseContentFragment {

    @Override
    protected View init() {
        setContentView(R.layout.fragment_home_loading);
        return mMainView;
    }
}

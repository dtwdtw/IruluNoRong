package com.wf.irulu.module.order.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import com.wf.irulu.R;
import com.wf.irulu.common.utils.UIUtils;

import java.util.ArrayList;

/**
 * @描述: TODO
 *
 * @项目名: irulu
 * @包名:com.wf.irulu.module.myorder.adapter
 * @类名:OrdersPageAdapter
 * @作者: Yuki
 * @创建时间:2015-8-31 下午2:21:38
 * 
 */
public class OrdersPageAdapter extends FragmentStatePagerAdapter {
	
	private ArrayList<Fragment> fragments = null;

	public OrdersPageAdapter(FragmentManager fm, ArrayList<Fragment> fragments) {
		super(fm);
		this.fragments = fragments;
	}

	@Override
	public Fragment getItem(int arg0) {
		return fragments.get(arg0);
	}

	@Override
	public int getCount() {
		if (fragments == null) {
			return 0;
		}
		return fragments.size();
	}

	// 初始化每个页卡选项  
    @Override  
    public Object instantiateItem(ViewGroup arg0, int position) {  
        Fragment fragment = (Fragment)super.instantiateItem(arg0, position);
        Bundle savedFragmentState = fragment.getArguments();
	    if (savedFragmentState != null) {
	       savedFragmentState.setClassLoader(fragment.getClass().getClassLoader());
	    }
        return fragment;  
    }  
      
    @Override
    public int getItemPosition(Object object) {
    	return  POSITION_NONE;
    }
    
    @Override  
    public void destroyItem(ViewGroup container, int position, Object object) {  
        super.destroyItem(container, position, object);  
    }
}

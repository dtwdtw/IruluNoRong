package com.wf.irulu.module.product.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.wf.irulu.common.bean.DailyDealsInit;
import com.wf.irulu.module.product.fragment.DallyDealsFragment;
import java.util.List;

/**
 * Author: zzh
 * Contact: zzh5659@qq.com
 * CreateDate: 16/4/14 下午4:07
 */
public class DailyDealsPagerAdapter extends FragmentPagerAdapter {

    private String[] titles = {"Deals Today", "Deals Tomorrow"};
    private DailyDealsInit dailyDealsInit;

    public DailyDealsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public DailyDealsPagerAdapter(FragmentManager supportFragmentManager, DailyDealsInit dailyDealsInit) {
        super(supportFragmentManager);
        this.dailyDealsInit = dailyDealsInit;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position) {
            case 0:
                DailyDealsInit.DailyTodayBean daily_today = dailyDealsInit.getDaily_today();
                fragment = DallyDealsFragment.newInstance(titles[position], daily_today.getProductList(),daily_today.getLimit_time());
                break;
            case 1:
                DailyDealsInit.DailyTomorrowBean daily_tomorrow = dailyDealsInit.getDaily_tomorrow();
                fragment = DallyDealsFragment.newInstance(titles[position], daily_tomorrow.getProductList(),daily_tomorrow.getLimit_time());
                break;
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return titles.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }
}

package com.wf.irulu.common.base;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;
import com.wf.irulu.IruluApplication;
import com.wf.irulu.R;
import com.wf.irulu.common.bean.AdInfo;
import com.wf.irulu.common.bean.PopupInfo;
import com.wf.irulu.common.bean.StartAdvertising;
import com.wf.irulu.common.utils.AdvertisingServiceListener;
import com.wf.irulu.common.utils.CacheUtils;
import com.wf.irulu.framework.BaseContentFragment;
import com.wf.irulu.framework.MenuFragment;
import com.wf.irulu.logic.IruluController;
import com.wf.irulu.logic.listener.ServiceListener;
import com.wf.irulu.module.advertising.dialog.AdvertisingDialog;

/**
 * @描述: Fragment的基类
 *
 * @项目名: irulu
 * @包名:com.wf.irulu.common.base
 * @类名:BaseFragment
 * @作者: 左西杰
 * @创建时间:2015-5-27 上午9:25:56
 * 
 */

public abstract class BaseFragment extends Fragment{

	private final String mPageName = "BaseFragment";
	public IruluController controller;
	protected View mMainView;
	protected ViewGroup mContainer;
	protected LayoutInflater mInflate;
//	public BaseFragmentActivity context;
	private RelativeLayout rootLayout;

	//Fragment是依赖于Activity生存的，没有Activity，就没有Fragment
	protected Activity mActivity;// 宿主activity

    protected void changeFont(ViewGroup root) {
        Typeface tf = Typeface.createFromAsset(mActivity.getAssets(),
                "fonts/OpenSans-Regular.ttf");
        for(int i = 0; i <root.getChildCount(); i++) {
            View v = root.getChildAt(i);
            if(v instanceof TextView) {
                ((TextView)v).setTypeface(tf);
            } else if(v instanceof Button) {
                ((Button)v).setTypeface(tf);
            } else if(v instanceof EditText) {
                ((EditText)v).setTypeface(tf);
            } else if(v instanceof ViewGroup) {
                changeFont((ViewGroup)v);
            }
        }

    }
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mActivity = getActivity();
		controller = IruluController.getInstance();
		controller.getPageManager().addPage(this);

        changeFont((ViewGroup) mActivity.getWindow().getDecorView());
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater,@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		inflater = getActivity().getLayoutInflater();
		this.mInflate = inflater;
		this.mContainer = container;
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	//	protected abstract View initView();

	/**
	 * 宿主Activity 的onCreate()方法执行完成返回后调用。
	 */
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		//加载数据
//		initData();
	}
	/**
	 * 如果孩子要加载数据，那么就复写此方法
	 */
//	protected void initData() {}

	public void replaceFragment(int resId, Fragment frag) {
		FragmentManager fm = getActivity().getSupportFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();
		String tag = frag.getClass().getName();
		ft.replace(resId, frag,tag);
		ft.setTransition(FragmentTransaction.TRANSIT_NONE);
		ft.commitAllowingStateLoss();
	}
	
	/**
	 * SetContentView
	 * 
	 * @param id
	 */
	public void setContentView(int id) {
		if (null == mMainView) {
			this.mMainView = this.mInflate.inflate(R.layout.base_fragment, mContainer, false);
			rootLayout = (RelativeLayout) mMainView.findViewById(R.id.rootLayout);
		}

		rootLayout.removeAllViews();
		rootLayout.addView(this.mInflate.inflate(id, mContainer, false));
	}
	/**
	 * findViewById
	 * 
	 * @param id
	 * @return
	 */
	public View findViewById(int id) {
		return mMainView.findViewById(id);
	}
	
	/**
	 * getString
	 * 
	 * @param id
	 * @return
	 */
	public String getFragString(int id) {
		return getFragResources().getString(id);
	}

	/**
	 * getResources
	 * 
	 * @return
	 */
	public Resources getFragResources() {
		return IruluApplication.getInstance().getResources();
	}
	
	public Fragment findFragmentByTag(String tag) {
		FragmentManager fm = getActivity().getSupportFragmentManager();
		return fm.findFragmentByTag(tag);
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		controller.getPageManager().removePage(this);
	}
	
	
	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart(mPageName); //统计页面
	}
	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd(mPageName); 
	}

}

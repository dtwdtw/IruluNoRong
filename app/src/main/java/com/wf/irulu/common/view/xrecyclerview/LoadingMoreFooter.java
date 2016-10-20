package com.wf.irulu.common.view.xrecyclerview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.wf.irulu.R;
import com.wf.irulu.common.view.xrecyclerview.progressindicator.AVLoadingIndicatorView;

public class LoadingMoreFooter extends LinearLayout {

    private SimpleViewSwithcer progressCon;
    private Context mContext;
    public final static int STATE_LAODING = 0;
    public final static int STATE_COMPLETE = 1;
    public final static int STATE_NOMORE = 2;
    public TextView mText;
	public LoadingMoreFooter(Context context) {
		super(context);
		initView(context);
	}

	/**
	 * @param context
	 * @param attrs
	 */
	public LoadingMoreFooter(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
	}
    public void initView(Context context ){
        mContext = context;
        setGravity(Gravity.CENTER);
        setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        progressCon = new SimpleViewSwithcer(context);
        progressCon.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        AVLoadingIndicatorView progressView = new AVLoadingIndicatorView(this.getContext());
        progressView.setIndicatorColor(0xffB5B5B5);
        progressView.setIndicatorId(ProgressStyle.BallSpinFadeLoader);
        progressCon.setView(progressView);

        addView(progressCon);
        mText = new TextView(context);
        mText.setPadding(0, 25, mText.getTotalPaddingRight(), 25);
        mText.setGravity(Gravity.CENTER);
        mText.setTextColor(getResources().getColor(R.color.pulltorefresh_blue));
        mText.setText(mContext.getText(R.string.listview_loading));

        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins( (int)getResources().getDimension(R.dimen.textandiconmargin),0,0,0 );

        mText.setLayoutParams(layoutParams);
        addView(mText);
    }

    public void setProgressStyle(int style) {
        if(style == ProgressStyle.SysProgress){
//            progressCon.setView(new ProgressBar(mContext, null, android.R.attr.progressBarStyle));
            /*********************我修改的 start***********************/
            ProgressBar progressBar = new ProgressBar(mContext);
            float dimension = getResources().getDimension(R.dimen.pageLoadDialog_circles);
            ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams((int)dimension,(int)dimension);
            progressBar.setLayoutParams(layoutParams);
            progressBar.setIndeterminate(true);
            progressBar.setIndeterminateDrawable(getResources().getDrawable(R.drawable.dialog_waiting_anim));
            progressCon.setView(progressBar);
            /*********************我修改的 end***********************/
        }else{
            AVLoadingIndicatorView progressView = new AVLoadingIndicatorView(this.getContext());
            progressView.setIndicatorColor(0xffB5B5B5);
            progressView.setIndicatorId(style);
            progressCon.setView(progressView);
        }
    }

    public void  setState(int state) {
        switch(state) {
            case STATE_LAODING:
                progressCon.setVisibility(View.VISIBLE);
                mText.setText(mContext.getText(R.string.listview_loading));
                this.setVisibility(View.VISIBLE);
                    break;
            case STATE_COMPLETE:
                mText.setText(mContext.getText(R.string.listview_loading));
                this.setVisibility(View.GONE);
                break;
            case STATE_NOMORE:
                mText.setText(mContext.getText(R.string.nomore_loading));
                progressCon.setVisibility(View.GONE);
                this.setVisibility(View.VISIBLE);
                break;
        }

    }
}

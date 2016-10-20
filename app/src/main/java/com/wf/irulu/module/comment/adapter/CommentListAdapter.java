package com.wf.irulu.module.comment.adapter;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.MemoryPolicy;
import com.wf.irulu.IruluApplication;
import com.wf.irulu.R;
import com.wf.irulu.common.bean.Comment;
import com.wf.irulu.common.bean.User;
import com.wf.irulu.common.bean.UserInfo;
import com.wf.irulu.common.utils.ConstantsUtils;
import com.wf.irulu.common.utils.DateFormatUtils;
import com.wf.irulu.module.product.service.GridDecoration;

import java.util.ArrayList;

/**
 * Created by XImoon on 15/11/10.
 */
public class CommentListAdapter extends BaseAdapter {

    /**
     * 评论列表
     */
    private ArrayList<Comment> mComments;
    /**
     * 布局加载器
     */
    private LayoutInflater mInflater = null;
    /**
     * 上下文
     */
    private Context mContext;
    /**
     * 图片宽度
     */
    private int mHeadWidth = 0;
    private int mRecycleWidth = 0;
    private int mRecycleItemWidth = 0;
    private int mRecycleItemSpace = 0;

    public CommentListAdapter(ArrayList<Comment> pComments, Context pContext) {
        this.mComments = pComments;
        this.mContext = pContext;
        mInflater = LayoutInflater.from(pContext);
        mRecycleWidth = ConstantsUtils.DISPLAYW - (pContext.getResources().getDimensionPixelSize(R.dimen.product_margin_box_01) * 2);
        mRecycleItemSpace = pContext.getResources().getDimensionPixelSize(R.dimen.indicator_internal_padding);
        mRecycleItemWidth = (mRecycleWidth - mRecycleItemSpace * 4) / 4;
        mHeadWidth = pContext.getResources().getDimensionPixelSize(R.dimen.message_head_img_width_height);

    }

    public void refresh(ArrayList<Comment> pComments) {
        this.mComments = pComments;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mComments == null ? 0 : mComments.size();
    }

    @Override
    public Object getItem(int position) {
        return mComments.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder mHolder = null;
        Comment comment = mComments.get(position);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_reviews_comment, null);
            mHolder = new ViewHolder();
            mHolder.mReiviewRbRate = (RatingBar) convertView.findViewById(R.id.review_rb_rate);
            mHolder.mReviewTvContent = (TextView) convertView.findViewById(R.id.review_tv_content);
            mHolder.mReviewTvName = (TextView) convertView.findViewById(R.id.review_tv_name);
            mHolder.mReviewTvTime = (TextView) convertView.findViewById(R.id.review_tv_time);
            mHolder.mReviewTvReply = (TextView) convertView.findViewById(R.id.review_tv_reply);
            mHolder.mReivewRvImage = (RecyclerView) convertView.findViewById(R.id.review_rv_image);
            mHolder.mHeadImageView = (ImageView) convertView.findViewById(R.id.reviews_head_iv);
            GridLayoutManager manager = new GridLayoutManager(mContext, 4);
            mHolder.mReivewRvImage.setLayoutManager(manager);

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(mRecycleItemWidth, ViewGroup.LayoutParams.WRAP_CONTENT);
            mHolder.mReivewRvImage.setLayoutParams(layoutParams);
            mHolder.mReivewRvImage.addItemDecoration(new GridDecoration(mRecycleItemSpace, mRecycleItemSpace));
            convertView.setTag(mHolder);
        } else {
            mHolder = (ViewHolder) convertView.getTag();
        }

        if (comment.getImage() == null || comment.getImage().size() == 0) {
            mHolder.mReivewRvImage.setVisibility(View.GONE);
        } else {
            mHolder.mReivewRvImage.setVisibility(View.VISIBLE);
            mHolder.mReivewRvImage.setAdapter(new CustomerReviewImageAdapter(comment.getImage(), mContext, mRecycleItemWidth));
        }

        UserInfo vUserInfo = comment.getUserInfo();
        User vUser = comment.getUser();
        String nickname = vUserInfo == null ? vUser.getNickname() : vUserInfo.getNickname();
        String imageURL = vUserInfo == null ? vUser.getHeadjpg() : vUserInfo.getHeadjpg();


        mHolder.mReiviewRbRate.setRating(comment.getStar());
        mHolder.mReviewTvContent.setText(comment.getContent());
        mHolder.mReviewTvTime.setText(DateFormatUtils.DateFormatUrlTime(comment.getTime()));

        mHolder.mReviewTvName.setText(nickname);
        if (comment.getChild() != null && comment.getChild().size() > 0) {
            mHolder.mReviewTvReply.setVisibility(View.VISIBLE);
            SpannableString iruluReply = new SpannableString(mContext.getString(R.string.irulu_respective));
            iruluReply.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.black)), 0, iruluReply.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
            mHolder.mReviewTvReply.setText(iruluReply);
            mHolder.mReviewTvReply.append(comment.getChild().get(0).getContent());
        } else {
            mHolder.mReviewTvReply.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(imageURL)) {
            IruluApplication.getInstance().getPicasso().load(imageURL + "?imageView2/0/w/" + mHeadWidth + "/h/" + mHeadWidth + "/interlace/1/q/75").placeholder(R.mipmap.reviews_head_im).error(R.mipmap.reviews_head_im).memoryPolicy(MemoryPolicy.NO_STORE).into(mHolder.mHeadImageView);
        } else {
            mHolder.mHeadImageView.setImageResource(R.mipmap.reviews_head_im);
        }

        return convertView;
    }

    private class ViewHolder {
        RatingBar mReiviewRbRate;
        TextView mReviewTvContent;
        TextView mReviewTvName;
        TextView mReviewTvTime;
        TextView mReviewTvReply;
        ImageView mHeadImageView;
        RecyclerView mReivewRvImage;
    }
}

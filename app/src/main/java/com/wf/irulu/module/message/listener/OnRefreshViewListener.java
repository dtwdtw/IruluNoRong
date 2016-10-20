package com.wf.irulu.module.message.listener;

import java.util.List;

import io.rong.imlib.model.Conversation;

/**
 * @描述: TODO
 * @项目名: irulu1.2
 * @包名:com.wf.irulu.module.message.listener
 * @类名:OnRefreshViewListener
 * @作者: 左杰
 * @创建时间:2015/11/23 18:53
 */
public interface OnRefreshViewListener {
    void onRefresh(List<Conversation> mConversationList);
}

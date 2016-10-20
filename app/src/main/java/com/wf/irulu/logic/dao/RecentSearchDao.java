package com.wf.irulu.logic.dao;

import java.util.ArrayList;

/**
 * @描述: TODO
 * @项目名: irulu1.2
 * @包名:com.wf.irulu.logic.dao
 * @类名:RecentSearchDao
 * @作者: Yuki
 * @创建时间:2015/11/16
 */
public interface RecentSearchDao {

    /** 查询所有搜索关键词*/
    ArrayList<String> searchAllKey();

    /** 查询是否包含指定关键词*/
    boolean isContainedKey(String key);

    /** 插入指定关键词*/
    boolean insertKey(String key);

    /** 更新指定关键词*/
    boolean updateKey(String key);

    /** 删除指定关键词*/
    boolean deleteKey(String key);
}

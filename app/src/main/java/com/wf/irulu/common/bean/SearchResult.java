package com.wf.irulu.common.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * @描述: TODO
 * @项目名: irulu1.2
 * @包名:com.wf.irulu.common.bean
 * @类名:SearchResult
 * @作者: Yuki
 * @创建时间:2015/11/17
 */
public class SearchResult implements Parcelable {

    private ArrayList<ProductSearch> list;
    private ArrayList<Sort> sort;

    public ArrayList<ProductSearch> getList() {
        return list;
    }

    public void setList(ArrayList<ProductSearch> list) {
        this.list = list;
    }

    public ArrayList<Sort> getSort() {
        return sort;
    }

    public void setSort(ArrayList<Sort> sort) {
        this.sort = sort;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(list);
        dest.writeList(this.sort);
    }

    public SearchResult() {
    }

    protected SearchResult(Parcel in) {
        this.list = in.createTypedArrayList(ProductSearch.CREATOR);
        this.sort = new ArrayList<Sort>();
        in.readList(this.sort, List.class.getClassLoader());
    }

    public static final Parcelable.Creator<SearchResult> CREATOR = new Parcelable.Creator<SearchResult>() {
        public SearchResult createFromParcel(Parcel source) {
            return new SearchResult(source);
        }

        public SearchResult[] newArray(int size) {
            return new SearchResult[size];
        }
    };
}

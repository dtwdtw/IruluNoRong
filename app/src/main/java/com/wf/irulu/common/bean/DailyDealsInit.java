package com.wf.irulu.common.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: zzh
 * Contact: zzh5659@qq.com
 * CreateDate: 16/4/22 下午2:31
 */
public class DailyDealsInit implements Parcelable {


    private DailyTodayBean daily_today;

    private DailyTomorrowBean daily_tomorrow;

    public DailyTodayBean getDaily_today() {
        return daily_today;
    }

    public void setDaily_today(DailyTodayBean daily_today) {
        this.daily_today = daily_today;
    }

    public DailyTomorrowBean getDaily_tomorrow() {
        return daily_tomorrow;
    }

    public void setDaily_tomorrow(DailyTomorrowBean daily_tomorrow) {
        this.daily_tomorrow = daily_tomorrow;
    }

    public static class DailyTodayBean implements Parcelable {
        private int type;
        private String name;
        private long limit_time;
        private List<ProductListBean> productList;

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public long getLimit_time() {
            return limit_time;
        }

        public void setLimit_time(long limit_time) {
            this.limit_time = limit_time;
        }

        public List<ProductListBean> getProductList() {
            return productList;
        }

        public void setProductList(List<ProductListBean> productList) {
            this.productList = productList;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.type);
            dest.writeString(this.name);
            dest.writeLong(this.limit_time);
            dest.writeList(this.productList);
        }

        public DailyTodayBean() {
        }

        protected DailyTodayBean(Parcel in) {
            this.type = in.readInt();
            this.name = in.readString();
            this.limit_time = in.readLong();
            this.productList = new ArrayList<ProductListBean>();
            in.readList(this.productList, ProductListBean.class.getClassLoader());
        }

        public static final Creator<DailyTodayBean> CREATOR = new Creator<DailyTodayBean>() {
            @Override
            public DailyTodayBean createFromParcel(Parcel source) {
                return new DailyTodayBean(source);
            }

            @Override
            public DailyTodayBean[] newArray(int size) {
                return new DailyTodayBean[size];
            }
        };
    }
    public static class DailyTomorrowBean implements Parcelable {
        private int type;
        private String name;
        private long limit_time;
        private List<ProductListBean> productList;

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public long getLimit_time() {
            return limit_time;
        }

        public void setLimit_time(long limit_time) {
            this.limit_time = limit_time;
        }

        public List<ProductListBean> getProductList() {
            return productList;
        }

        public void setProductList(List<ProductListBean> productList) {
            this.productList = productList;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.type);
            dest.writeString(this.name);
            dest.writeLong(this.limit_time);
            dest.writeList(this.productList);
        }

        public DailyTomorrowBean() {
        }

        protected DailyTomorrowBean(Parcel in) {
            this.type = in.readInt();
            this.name = in.readString();
            this.limit_time = in.readLong();
            this.productList = new ArrayList<ProductListBean>();
            in.readList(this.productList, ProductListBean.class.getClassLoader());
        }

        public static final Creator<DailyTomorrowBean> CREATOR = new Creator<DailyTomorrowBean>() {
            @Override
            public DailyTomorrowBean createFromParcel(Parcel source) {
                return new DailyTomorrowBean(source);
            }

            @Override
            public DailyTomorrowBean[] newArray(int size) {
                return new DailyTomorrowBean[size];
            }
        };
    }
    public static class ProductListBean implements Parcelable {
        private String commentNum;
        private String id;
        private int addWishList;
        private String percent;
        private String price;
        private String tag;
        private String percentTag;
        private String star;
        private String image;
        private String discountPrice;
        private String productName;
        private int productId;



        public String getCommentNum() {
            return commentNum;
        }

        public void setCommentNum(String commentNum) {
            this.commentNum = commentNum;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public int getAddWishList() {
            return addWishList;
        }

        public void setAddWishList(int addWishList) {
            this.addWishList = addWishList;
        }

        public String getPercent() {
            return percent;
        }

        public void setPercent(String percent) {
            this.percent = percent;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getTag() {
            return tag;
        }

        public void setTag(String tag) {
            this.tag = tag;
        }

        public String getPercentTag() {
            return percentTag;
        }

        public void setPercentTag(String percentTag) {
            this.percentTag = percentTag;
        }

        public String getStar() {
            return star;
        }

        public void setStar(String star) {
            this.star = star;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getDiscountPrice() {
            return discountPrice;
        }

        public void setDiscountPrice(String discountPrice) {
            this.discountPrice = discountPrice;
        }

        public String getProductName() {
            return productName;
        }

        public void setProductName(String productName) {
            this.productName = productName;
        }

        public int getProductId() {
            return productId;
        }

        public void setProductId(int productId) {
            this.productId = productId;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.commentNum);
            dest.writeString(this.id);
            dest.writeInt(this.addWishList);
            dest.writeString(this.percent);
            dest.writeString(this.price);
            dest.writeString(this.tag);
            dest.writeString(this.percentTag);
            dest.writeString(this.star);
            dest.writeString(this.image);
            dest.writeString(this.discountPrice);
            dest.writeString(this.productName);
            dest.writeInt(this.productId);
        }

        public ProductListBean() {
        }

        protected ProductListBean(Parcel in) {
            this.commentNum = in.readString();
            this.id = in.readString();
            this.addWishList = in.readInt();
            this.percent = in.readString();
            this.price = in.readString();
            this.tag = in.readString();
            this.percentTag = in.readString();
            this.star = in.readString();
            this.image = in.readString();
            this.discountPrice = in.readString();
            this.productName = in.readString();
            this.productId = in.readInt();
        }

        public static final Creator<ProductListBean> CREATOR = new Creator<ProductListBean>() {
            @Override
            public ProductListBean createFromParcel(Parcel source) {
                return new ProductListBean(source);
            }

            @Override
            public ProductListBean[] newArray(int size) {
                return new ProductListBean[size];
            }
        };
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.daily_today, flags);
        dest.writeParcelable(this.daily_tomorrow, flags);
    }

    public DailyDealsInit() {
    }

    protected DailyDealsInit(Parcel in) {
        this.daily_today = in.readParcelable(DailyTodayBean.class.getClassLoader());
        this.daily_tomorrow = in.readParcelable(DailyTomorrowBean.class.getClassLoader());
    }

    public static final Creator<DailyDealsInit> CREATOR = new Creator<DailyDealsInit>() {
        @Override
        public DailyDealsInit createFromParcel(Parcel source) {
            return new DailyDealsInit(source);
        }

        @Override
        public DailyDealsInit[] newArray(int size) {
            return new DailyDealsInit[size];
        }
    };
}

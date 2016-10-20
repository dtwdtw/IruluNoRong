package com.wf.irulu.common.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * @描述: TODO
 * @项目名: irulu1.2
 * @包名:com.wf.irulu.common.bean
 * @类名:OrderDetailProduct
 * @作者: Yuki
 * @创建时间:2015/11/24
 */
public class OrderDetailProduct implements Parcelable {

//    id	int		sku id
    private int id;
//    productId	int		产品id
    private int productId;
//    uniq	string		唯一id
    private String uniq;
//    quantity	int		商品数量
    private int quantity;
//    price	float		商品价格
    private float price;
//    sku	array String		商品sku
    private ArrayList<String> sku;
//    name	string		商品名称
    private String name;
//    image	string		图片
    private String image;
//    servInfo	json		售后信息
    private ServInfo servInfo;
//    isCanNotSend	int		是否限售
    private int isCanNotSend;
//    msg String 限售消息提示
    private String msg;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getIsCanNotSend() {
        return isCanNotSend;
    }

    public void setIsCanNotSend(int isCanNotSend) {
        this.isCanNotSend = isCanNotSend;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getUniq() {
        return uniq;
    }

    public void setUniq(String uniq) {
        this.uniq = uniq;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public ArrayList<String> getSku() {
        return sku;
    }

    public void setSku(ArrayList<String> sku) {
        this.sku = sku;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public ServInfo getServInfo() {
        return servInfo;
    }

    public void setServInfo(ServInfo servInfo) {
        this.servInfo = servInfo;
    }

    public OrderDetailProduct() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeInt(this.productId);
        dest.writeString(this.uniq);
        dest.writeInt(this.quantity);
        dest.writeFloat(this.price);
        dest.writeStringList(this.sku);
        dest.writeString(this.name);
        dest.writeString(this.image);
        dest.writeParcelable(this.servInfo, 0);
        dest.writeInt(this.isCanNotSend);
        dest.writeString(this.msg);
    }

    protected OrderDetailProduct(Parcel in) {
        this.id = in.readInt();
        this.productId = in.readInt();
        this.uniq = in.readString();
        this.quantity = in.readInt();
        this.price = in.readFloat();
        this.sku = in.createStringArrayList();
        this.name = in.readString();
        this.image = in.readString();
        this.servInfo = in.readParcelable(ServInfo.class.getClassLoader());
        this.isCanNotSend = in.readInt();
        this.msg = in.readString();
    }

    public static final Creator<OrderDetailProduct> CREATOR = new Creator<OrderDetailProduct>() {
        public OrderDetailProduct createFromParcel(Parcel source) {
            return new OrderDetailProduct(source);
        }

        public OrderDetailProduct[] newArray(int size) {
            return new OrderDetailProduct[size];
        }
    };
}

package com.wf.irulu.common.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * @描述: 购物车商品信息描述
 * @项目名: irulu1.2
 * @包名:com.wf.irulu.common.bean
 * @类名:ProductCart
 * @作者: Yuki
 * @创建时间:2015/10/24
 */
public class ProductCart implements Parcelable {
    /**购物车id*/
    private int id;
    /*商铺id*/
    private int merchantId;
    /*商品id*/
    private int productId;
    /*商品skuid*/
    private int pskuId;
    /*选购时间（时间戳）*/
    private long addTime;
    /*此商品总价*/
    private float sumPrice;
    /*购买数量*/
    private int quantity;
    /*商铺名称*/
    private String merchant;
    /*商品名称*/
    private String product;
    /*商品SKU*/
    private ArrayList<String> productSku;
    /*商品单价*/
    private float price;
    /*商品图片*/
    private String image;
    /** giftInfo	json array		买一送一信息 */
    private ArrayList<GiftInfo> giftInfo;
    /**是否是下载商品，0是，1否*/
    private String status;
    /**库存是否足够*/
    private boolean isenough;
    /**商品配送价格*/
    private float deliveryFee;
    private float oriPrice;
    private String percent;
    private int skuId;
    /**库存*/
    private int stock;
    /**唯一id*/
    private String uniq;

    private String isAddWish;

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public long getAddTime() {
        return addTime;
    }

    public void setAddTime(long addTime) {
        this.addTime = addTime;
    }

    public float getDeliveryFee() {
        return deliveryFee;
    }

    public void setDeliveryFee(float deliveryFee) {
        this.deliveryFee = deliveryFee;
    }

    public ArrayList<GiftInfo> getGiftInfo() {
        return giftInfo;
    }

    public void setGiftInfo(ArrayList<GiftInfo> giftInfo) {
        this.giftInfo = giftInfo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public boolean isenough() {
        return isenough;
    }

    public void setIsenough(boolean isenough) {
        this.isenough = isenough;
    }

    public String getMerchant() {
        return merchant;
    }

    public void setMerchant(String merchant) {
        this.merchant = merchant;
    }

    public int getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(int merchantId) {
        this.merchantId = merchantId;
    }

    public float getOriPrice() {
        return oriPrice;
    }

    public void setOriPrice(float oriPrice) {
        this.oriPrice = oriPrice;
    }

    public String getPercent() {
        return percent;
    }

    public void setPercent(String percent) {
        this.percent = percent;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public ArrayList<String> getProductSku() {
        return productSku;
    }

    public void setProductSku(ArrayList<String> productSku) {
        this.productSku = productSku;
    }

    public int getPskuId() {
        return pskuId;
    }

    public void setPskuId(int pskuId) {
        this.pskuId = pskuId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getSkuId() {
        return skuId;
    }

    public void setSkuId(int skuId) {
        this.skuId = skuId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public float getSumPrice() {
        return sumPrice;
    }

    public void setSumPrice(float sumPrice) {
        this.sumPrice = sumPrice;
    }

    public String getUniq() {
        return uniq;
    }

    public void setUniq(String uniq) {
        this.uniq = uniq;
    }

    public String getIsAddWish() {
        return isAddWish;
    }

    public void setIsAddWish(String isAddWish) {
        this.isAddWish = isAddWish;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeInt(this.merchantId);
        dest.writeInt(this.productId);
        dest.writeInt(this.pskuId);
        dest.writeLong(this.addTime);
        dest.writeFloat(this.sumPrice);
        dest.writeInt(this.quantity);
        dest.writeString(this.merchant);
        dest.writeString(this.product);
        dest.writeStringList(this.productSku);
        dest.writeFloat(this.price);
        dest.writeString(this.image);
        dest.writeTypedList(giftInfo);
        dest.writeString(this.status);
        dest.writeByte(isenough ? (byte) 1 : (byte) 0);
        dest.writeFloat(this.deliveryFee);
        dest.writeFloat(this.oriPrice);
        dest.writeString(this.percent);
        dest.writeInt(this.skuId);
        dest.writeInt(this.stock);
        dest.writeString(this.uniq);
        dest.writeString(this.isAddWish);
    }

    public ProductCart() {
    }

    protected ProductCart(Parcel in) {
        this.id = in.readInt();
        this.merchantId = in.readInt();
        this.productId = in.readInt();
        this.pskuId = in.readInt();
        this.addTime = in.readLong();
        this.sumPrice = in.readFloat();
        this.quantity = in.readInt();
        this.merchant = in.readString();
        this.product = in.readString();
        this.productSku = in.createStringArrayList();
        this.price = in.readFloat();
        this.image = in.readString();
        this.giftInfo = in.createTypedArrayList(GiftInfo.CREATOR);
        this.status = in.readString();
        this.isenough = in.readByte() != 0;
        this.deliveryFee = in.readFloat();
        this.oriPrice = in.readFloat();
        this.percent = in.readString();
        this.skuId = in.readInt();
        this.stock = in.readInt();
        this.uniq = in.readString();
        this.isAddWish=in.readString();
    }

    public static final Creator<ProductCart> CREATOR = new Creator<ProductCart>() {
        public ProductCart createFromParcel(Parcel source) {
            return new ProductCart(source);
        }

        public ProductCart[] newArray(int size) {
            return new ProductCart[size];
        }
    };
}

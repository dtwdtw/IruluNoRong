package com.wf.irulu.common.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * @描述: 商品详情
 * @项目名: irulu1.2
 * @包名:com.wf.irulu.common.bean
 * @类名:ProductDetail
 * @作者: Yuki
 * @创建时间:2015/10/24
 */
public class ProductDetail implements Parcelable {

    /** 产品id */
    private int id;
    /** 唯一id */
    private String uniq;
    /** 产品名称 */
    private String name;
    /** 图片列表 */
    private ArrayList<String> image;
    /** 价格 */
    private float price;
    /** 配送费用 */
    private float deliveryFee;
    /** SKU列表 */
    private ArrayList<TypeName> typeName;
    /** SKU详情 */
    private ArrayList<Type> type;
    /** 规格 */
    private ArrayList<Standard> standard;
    /** 星级 */
    private float star;
    /** 评论列表 */
    private ArrayList<Comment> commentList;
    /** maybe you like */
    private ArrayList<MaybeLike> maybeLike;
    /** 详情地址 */
    private String description;
    /** 是否已经已经添加到wish*/
    private int isWishList;
    /** */
    private String percentTag;
    /** 评论总数 */
    private int commentNum;
    /** 价格 */
    private float discountPrice;
    /** commentPower	int		评论权限（0：无，1有）*/
    private int commentPower;
    /** shippingInfo	array string		配送费信息 */
    private ArrayList<String> shippingInfo;
    /** giftInfo	json array		买一送一信息 */
    private ArrayList<GiftInfo> giftInfo;
    /** 库存*/
    private int stock;
    /** 标签*/
    private String tag;
    /** 分享信息*/
    private ShareInfo shareInfo;
    /** 是否上架*/
    private int status;

    public ProductDetail() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUniq() {
        return uniq;
    }

    public void setUniq(String uniq) {
        this.uniq = uniq;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<String> getImage() {
        return image;
    }

    public void setImage(ArrayList<String> image) {
        this.image = image;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public float getDeliveryFee() {
        return deliveryFee;
    }

    public void setDeliveryFee(float deliveryFee) {
        this.deliveryFee = deliveryFee;
    }

    public ArrayList<TypeName> getTypeName() {
        return typeName;
    }

    public void setTypeName(ArrayList<TypeName> typeName) {
        this.typeName = typeName;
    }

    public ArrayList<Type> getType() {
        return type;
    }

    public void setType(ArrayList<Type> type) {
        this.type = type;
    }

    public ArrayList<Standard> getStandard() {
        return standard;
    }

    public void setStandard(ArrayList<Standard> standard) {
        this.standard = standard;
    }

    public float getStar() {
        return star;
    }

    public void setStar(float star) {
        this.star = star;
    }

    public ArrayList<Comment> getCommentList() {
        return commentList;
    }

    public void setCommentList(ArrayList<Comment> commentList) {
        this.commentList = commentList;
    }

    public ArrayList<MaybeLike> getMaybeLike() {
        return maybeLike;
    }

    public void setMaybeLike(ArrayList<MaybeLike> maybeLike) {
        this.maybeLike = maybeLike;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getCommentNum() {
        return commentNum;
    }

    public void setCommentNum(int commentNum) {
        this.commentNum = commentNum;
    }

    public float getDiscountPrice() {
        return discountPrice;
    }

    public void setDiscountPrice(float discountPrice) {
        this.discountPrice = discountPrice;
    }

    public int getCommentPower() {
        return commentPower;
    }

    public void setCommentPower(int commentPower) {
        this.commentPower = commentPower;
    }

    public ArrayList<String> getShippingInfo() {
        return shippingInfo;
    }

    public void setShippingInfo(ArrayList<String> shippingInfo) {
        this.shippingInfo = shippingInfo;
    }

    public ArrayList<GiftInfo> getGiftInfo() {
        return giftInfo;
    }

    public void setGiftInfo(ArrayList<GiftInfo> giftInfo) {
        this.giftInfo = giftInfo;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public ShareInfo getShareInfo() {
        return shareInfo;
    }

    public void setShareInfo(ShareInfo shareInfo) {
        this.shareInfo = shareInfo;
    }

    public int isWishList() {
        return isWishList;
    }

    public void setIsWishList(int isWishList) {
        this.isWishList = isWishList;
    }

    public String getPercentTag() {
        return percentTag;
    }

    public void setPercentTag(String percentTag) {
        this.percentTag = percentTag;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.uniq);
        dest.writeString(this.name);
        dest.writeStringList(this.image);
        dest.writeFloat(this.price);
        dest.writeFloat(this.deliveryFee);
        dest.writeTypedList(typeName);
        dest.writeTypedList(type);
        dest.writeTypedList(standard);
        dest.writeFloat(this.star);
        dest.writeTypedList(commentList);
        dest.writeTypedList(maybeLike);
        dest.writeString(this.description);
        dest.writeInt(this.isWishList);
        dest.writeString(this.percentTag);
        dest.writeInt(this.commentNum);
        dest.writeFloat(this.discountPrice);
        dest.writeInt(this.commentPower);
        dest.writeStringList(this.shippingInfo);
        dest.writeTypedList(giftInfo);
        dest.writeInt(this.stock);
        dest.writeString(this.tag);
        dest.writeParcelable(this.shareInfo, 0);
        dest.writeInt(this.status);
    }

    protected ProductDetail(Parcel in) {
        this.id = in.readInt();
        this.uniq = in.readString();
        this.name = in.readString();
        this.image = in.createStringArrayList();
        this.price = in.readFloat();
        this.deliveryFee = in.readFloat();
        this.typeName = in.createTypedArrayList(TypeName.CREATOR);
        this.type = in.createTypedArrayList(Type.CREATOR);
        this.standard = in.createTypedArrayList(Standard.CREATOR);
        this.star = in.readFloat();
        this.commentList = in.createTypedArrayList(Comment.CREATOR);
        this.maybeLike = in.createTypedArrayList(MaybeLike.CREATOR);
        this.description = in.readString();
        this.isWishList = in.readInt();
        this.percentTag = in.readString();
        this.commentNum = in.readInt();
        this.discountPrice = in.readFloat();
        this.commentPower = in.readInt();
        this.shippingInfo = in.createStringArrayList();
        this.giftInfo = in.createTypedArrayList(GiftInfo.CREATOR);
        this.stock = in.readInt();
        this.tag = in.readString();
        this.shareInfo = in.readParcelable(ShareInfo.class.getClassLoader());
        this.status = in.readInt();
    }

    public static final Creator<ProductDetail> CREATOR = new Creator<ProductDetail>() {
        public ProductDetail createFromParcel(Parcel source) {
            return new ProductDetail(source);
        }

        public ProductDetail[] newArray(int size) {
            return new ProductDetail[size];
        }
    };
}

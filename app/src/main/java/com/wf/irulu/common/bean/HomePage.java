package com.wf.irulu.common.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @描述: 首页bean      临时的
 *
 * @项目名: irulu
 * @包名:com.wf.irulu.common.bean
 * @类名:Homepoa
 * @作者: 左西杰
 * @创建时间:2015-7-3 上午11:40:25
 * 
 */
public class HomePage implements Serializable{

	public String priceSymbol;					// 价格符号
	public List<Catalog> catalog;				// 栏目类别
	public List<Product> product;				// 产品列表
	public List<SlideBean> slide;					// slide 广告
	public Promotion promotion;					// 促销

	/** 促销*/
	public class Promotion{
		public String name;						// 显示促销的名字
		public String moreUrl;					//更多的URL
		public String url;						// 促销URL
		public List<PromotionList> list;		//
	}

	/** 栏目类别*/
	public class Catalog implements Serializable{
		public int id;							// id
		public String name;						// 栏目名字
		public String icon;						// 栏目图标
	}

	/** 产品列表*/
	public class Product implements Serializable{
		public int id;
		public String name;						// 显示的名字
		public ArrayList<ProductInfo> list;			// 产品列表
	}

	public class PromotionList{
		public String title;					//标题
		public String image;					//图片地址
		public String content;					//跳转内容
		public int type;						//类型 1：产品列表、2：产品详情、3：app内部H5、
		public int showType;					//图片展示类型(1:大图；2:小图)
	}

	public class ProductInfo{
		// id int sku id
		public int id;
		// productId int 产品id
		public int productId;
		// uniq string 唯一id
		public String uniq;
		// quantity int 商品数量
		public int quantity;
		// price float 商品价格
		public float price;
		// sku array String 商品sku
		public ArrayList<String> sku;
		// name string 商品名称
		public String name;
		// image string 图片
		public String image;
		// servInfo json 售后信息
		public ServInfo servInfo;
		// star float 评论数
		public float star;
		public int isCanNotSend;//是否限售
		public String msg;//限售消息提示
	}

	public class ServInfo{
		// servId string 售后id
		public String servId;
		// status int 状态 0：关闭1：等待客服回复、2：客服已回复、3：拒绝售后
		public int status;
		//	type	int		售后类型 1：退款、2：退货
		public int type;
	}
}
package com.wf.irulu.logic.listener;

/**
 *
 * @描述: 操作监听器,观察动作执行返回
 *
 * @项目名: irulu
 * @包名:com.wf.irulu.logic.service
 * @类名:ServiceListener
 * @作者: 左西杰
 * @创建时间:2015-5-27 下午4:59:10
 *
 */
public interface ServiceListener {

	public static final int TYPE_REFRESH_TIME_VAL = 5000;
	public static final int TYPE_CACHE_DATA = 5001;					// 缓存数据
	public static final int TYPE_LOADINGING = 5002;
	public static final int TYPE_NO_NET = 5003;

	public enum ActionTypes {
		/**----Twitter----**/
		TWITTER,

		/**
		 * APP初始化
		 */
		APP_INIT,

		/**
		 * 启动广告
		 */
		START_ADVERTISING,

		/**
		 * 产品首页
		 */
		HOME,

		/**
		 * 产品首页分页
		 */
		HOME_MORE,
		/***AAS接口 start***/

		/**
		 * 版本检测
		 */
		CHECK_VERSION,

		/**
		 * 注册
		 */
		REGISTER,

		/**
		 * 登录
		 */
		LOGIN,

		/**
		 * 登出
		 */
		LOGOUT,

		/***AAS接口 end***/

		/***个人资料接口 start***/

		/**
		 * 修改密码
		 */
		CHANGE_PASSWORD,

		/**
		 * 找回密码
		 */
		FIND_PASSWORD,

		/**
		 * 获取地址
		 */
		GET_ADDRESS,

		/***个人资料接口 end***/

		/***商品详情 start***/

		/**
		 * 发布评论
		 */
		PUBLISH_COMMENT,

		/***
		 * 添加购物车
		 */
		ADD_CART,

		/**
		 * 获取购物车商品列表
		 */
		GET_CART_INFO,

		/**
		 * 修改商品数量
		 */
		UPDATA_PRODUCT_NUM,

		/**
	     * products'detail
	     */
	    PRO_DETA,

	    /**
	     * BUY NOW
	     */
	    PRODUCT_BUYNOW,

	    /**
	     * Check NOW
	     */
	    PRODUCT_CHECKOUT,

	    /**
	     * 心愿单
	     */
	    WISH_LIST,

		/**
		 * 添加到心愿单
		 */
		ADD_WISH,

		/**
		 * 移除一个心愿单
		 */
		DEL_WISH,

	    /**
	     * 评论列表
	     */
	    COMMENT_LIST,

	    /**
	     * 推荐
	     */
	    PRODUCT_RECOMMENDATION,

	    /**
	     * 上传服务器获取token key
	     */
	    UPLOAD_TOKEN,
		/**
		 * 七牛上传
		 */
		QINIU_UPLOAD,
		/***商品详情 end***/

		/***购物车 start***/

		/***
		 * 生成订单
		 */
		CREATE_ORDER,

		/**
		 * 移动到购物车/保存到以后购买
		 */
		CART_TO_SAVED_OR_CART_TO_SAVED,

		/**
		 * 从购物车移除商品
		 */
		CART_REMOVE_PRODUCT,

		/**
		 * 修改商品数量
		 */
		CART_UPDATE_PRODUCT,

		/***购物车end***/


	    /**
	     * 首页
	     */
	    IRULU_HOME,

	    /***搜索 start***/

	    /**
	     * keyword
	     */
	    PRODUCT_SEARCH,

	    /**
	     * hot search
	     */
	    HOT_SEARCH,

	    /**
	     * suggest search
	     */
	    SUGGEST_SEARCH,

	    /***搜索 end***/

	    /***订单 start***/

	    /**
	     * 订单列表
	     */
	    ORDER_LIST,

	    /**
	     * 删除订单
	     */
	    ORDER_DELETE,

	    /**
	     * 取消订单
	     */
	    ORDER_CANCEL,

	    /**
	     * 确认订单
	     */
	    ORDER_CONFIRM,

	    /**
	     * 订单详情
	     */
	    ORDER_DETAIL,

	    /**
	     * 支付token
	     */
	    PAYMENT_TOKEN,

		/**
		 * 支付成功
		 */
		PAY_SUCCESS,

		/**
		 * 售后详情
		 */
		SERVICE_DETAILS,

	    /**
	     * 交易ID
	     */
	    TRANSACTION_ID,

	    /**
	     * 申请售后
	     */
	    APPLY_SERVER,

	    /***订单 end***/

		/**
		 * 建议 feedback
		 */
	    FEEDBACK,
		/**
		 * 修改昵称
		 */
		UPDATE_NICKNAME,
		/**
		 * 修改生日
		 */
		UPDATE_BIRTHDAY,
		/**
		 * 添加发货地址
		 */
		ADD_ADDRESS,
		/**
		 * 编辑发货地址
		 */
		EDIT_ADDRESS,
		/**
		 * 删除地址
		 */
		DELETE_ADDRESS,
		/**
		 * 设置默认地址
		 */
		DEFAULT_ADDRESS,
		/**
		 * 获取国家信息
		 */
		COUNTRY_INFORMATION,
		/**
		 * 获取 地区信息
		 */
		STATE_INFORMATION,
		/**
		 * 产品分类首页
		 */
		PRODUCT_CATEGORY,
		/**
		 * 产品分类列表
		 */
		PRODUCT_CATEGORY_LIST,
		/**
		 * 获得客户支持原因列表
		 */
		SUPPORT_REASON,

		/**
		 * 添加客户支持信息
		 */
		ADD_SUPPORT_INFO,

		/**
		 * 重新获取rongcloudtoken
		 */
		RESET_RONG_TOKEN,
		/**
		 * 物流信息
		 */
		LOGISTICS_TRACKING,

		/**
		 *  获取新品
		 */
		NEW_ARRICALS,


		/**
		 *
		 */
		DAILY_DEALS_INIT,

		/**
		 * Daily Deals
		 */
		DAILY_DEALS

	}

	/**
	 * 执行动作成功
	 *
	 * @param action 当前操作
	 * @param returnObj 返回对象
	 */
	void serviceSuccess(ActionTypes action, Object bandObj, Object returnObj);

	/**
	 * 执行动作失败
	 *
	 * @param action 当前操作
	 * @param returnObj 返回对象
	 */
	void serviceFailure(ActionTypes action, Object returnObj, int errorCode);

	/**
	 * 处理非网络响应
	 * @param action
	 * @param type
	 * @param returnObj
	 */
	void serviceCallback(ActionTypes action, int type, Object returnObj);

}

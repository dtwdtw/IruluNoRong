package com.wf.irulu.logic.manager;

import com.wf.irulu.logic.IruluController;
import com.wf.irulu.logic.service.AASService;
import com.wf.irulu.logic.service.HomeService;
import com.wf.irulu.logic.service.OrderService;
import com.wf.irulu.logic.service.PaymentService;
import com.wf.irulu.logic.service.ProductService;
import com.wf.irulu.logic.service.ShoppingService;
import com.wf.irulu.logic.service.impl.AASServiceImpl;
import com.wf.irulu.logic.service.impl.HomeServiceImpl;
import com.wf.irulu.logic.service.impl.OrderServiceImpl;
import com.wf.irulu.logic.service.impl.PaymentServiceImpl;
import com.wf.irulu.logic.service.impl.ProductServiceImpl;
import com.wf.irulu.logic.service.impl.SettingServiceImpl;
import com.wf.irulu.logic.service.impl.ShoppingServiceImpl;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @描述: 接口管理类
 *
 * @项目名: irulu
 * @包名:com.wf.irulu.logic.service
 * @类名:ServiceManager
 * @作者: 左西杰
 * @创建时间:2015-5-29 下午2:36:57
 * 
 */

public class ServiceManager {

	private AASService aasService;				// AAS相关接口
	private ProductService productService;		//商品相关接口
	private ShoppingService shoppingService;	//购物车相关接口
	private HomeService homeService;			//首页相关接口
	private OrderService orderService;          //订单相关接口
	private PaymentService paymentService;      //支付相关接口
	private ExecutorService executorService;	// 线程池

	public ServiceManager(IruluController controller) {
		aasService = new AASServiceImpl(controller, this);
		productService = new ProductServiceImpl(controller, this);
		homeService = new HomeServiceImpl(controller, this);
		orderService = new OrderServiceImpl(controller, this);
		shoppingService = new ShoppingServiceImpl(controller, this);
		paymentService = new PaymentServiceImpl(controller,this);
		executorService = Executors.newScheduledThreadPool(10);			// 初始化线程池--10条
	}

	public AASService getAasService() {
		return aasService;
	}

	public ExecutorService getExecutorService() {
		return executorService;
	}

	public HomeService getHomeService() {
		return homeService;
	}

	public OrderService getOrderService() {
		return orderService;
	}

	public ProductService getProductService() {
		return productService;
	}

	public ShoppingService getShoppingService() {
		return shoppingService;
	}

	public PaymentService getPaymentService() {
		return paymentService;
	}
}

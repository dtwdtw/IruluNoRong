package com.wf.irulu.common.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dtw on 16/4/21.
 */
public class HomeBean implements Parcelable {

    /**
     * daily_deals : {"product":{"commentNum":10,"id":"59","addWishList":0,"percent":"59","price":"104.99","tag":"","percentTag":"59%","star":4.8,"image":"https://images.irulu.com/product/cover/20151008/521a2f166b2dd10e4598fd.png","discountPrice":"43.99","productName":"iRULU eXpro X1Pro Quad Core Android 4.4 Kitkat 9 Inch Tablet OTG function G sensor internal 3 point capacitive touch screen","productId":1723431},"limit_time":1461643199,"name":"Daily Deals"}
     * title : Discover
     * events_deals : {"eventInfo":[],"title":"Events Deals"}
     * catalog : [{"id":0,"icon":"","count":0,"app_icon":"https://dn-irulu.qbox.me/app/All-Categories@3x.png","name":"All Categories"},{"id":1313436,"icon":"https://images.irulu.com/cate/20151211/5269a66dc58b0522598bd.png","count":1,"name":"Phone","app_icon":"https://images.irulu.com/cate/20160420/530f68eef98ac1e306b072.png"},{"id":1013446,"icon":"https://images.irulu.com/cate/20151211/5269a660c3fc3a30cacbf.png","count":5,"name":"Tablet","app_icon":"https://images.irulu.com/cate/20160420/530f68e492631163be2f8e.png"},{"id":1713446,"icon":"https://images.irulu.com/cate/20151211/5269a648f2e4921c7536ac.png","count":4,"name":"Smart Hardware","app_icon":"https://images.irulu.com/cate/20160420/530f69cc5dc2d17dfb4861.png"},{"id":1413456,"icon":"https://images.irulu.com/cate/20151211/5269a621e17213e0a59df.png","count":4,"name":"Accessory","app_icon":"https://images.irulu.com/cate/20160420/530f688c1ddf3d7b87752.png"}]
     * priceSymbol : US $
     * recommendation : {"id":"1","title":"Recommend","slide":[{"type":"3","content":"javascript:;","id":"1","image":"https://images.irulu.com/poster/20160127/52a5b4285d2791e97ab682.jpg"},{"type":"2","content":"1123492","id":"2","image":"https://images.irulu.com/poster/20160104/5287e1748320d1a2161e86.jpg"},{"type":"2","content":"1333471","id":"6","image":"https://images.irulu.com/poster/20160107/528b9484bdd466a38074a.jpg"}],"type":"1","productList":[{"commentNum":5,"id":0,"addWishList":0,"percent":"39","price":69.99,"tag":"Mobile","percentTag":"39%","star":4.8,"image":"https://images.irulu.com/product/cover/20150907/51f220ead7d93af59f92d.jpg","discountPrice":42.99,"productName":"iRULU GV08 Bluetooth Touch Screen Smart Phone Watch","productId":1423441},{"commentNum":4,"id":0,"addWishList":0,"percent":"59","price":289.99,"tag":"Mobile","percentTag":"59%","star":5,"image":"https://images.irulu.com/product/cover/20150909/51f4ab5f0e5531fe9c57a7.jpg","discountPrice":119,"productName":"iRULU P2 Smart Phone Tablet Phablet 2GB RAM 16GB ROM 3G GSM WCDMA Android 4.4.0 Octa core mobile Internet multimedia device","productId":1323491},{"commentNum":1,"id":0,"addWishList":0,"percent":"57","price":174.99,"tag":"Mobile","percentTag":"57%","star":5,"image":"https://images.irulu.com/product/cover/20150909/51f4b026140409c20109d.jpg","discountPrice":75.99,"productName":"G2 TPU Band Bluetooth Waterproof Smartwatch","productId":1023402},{"commentNum":2,"id":0,"addWishList":0,"percent":"29","price":16.99,"tag":"Mobile","percentTag":"29%","star":5,"image":"https://images.irulu.com/product/cover/20150909/51f4daf8436a71f2afb071.jpg","discountPrice":11.99,"productName":"iRULU Portable Mini Wireless Bluetooth Speaker","productId":1923442},{"commentNum":1,"id":0,"addWishList":0,"percent":"30","price":19.99,"tag":"Mobile","percentTag":"30%","star":5,"image":"https://images.irulu.com/product/cover/20150910/51f6114e83cb4d52046b3.jpg","discountPrice":13.99,"productName":"Fashionable 3.5mm Hi-Fi Stereo Sports USB Headset","productId":1623452},{"commentNum":0,"id":0,"addWishList":1,"percent":"65","price":45.99,"tag":"Mobile","percentTag":"65%","star":0,"image":"https://images.irulu.com/product/cover/20150910/51f6151df71c910ca28059.jpg","discountPrice":15.99,"productName":"iRULU Ozone Output 2mg/h 500mAh Air Sterilizer","productId":1323462},{"commentNum":6,"id":0,"addWishList":0,"percent":"61","price":131.99,"tag":"Mobile","percentTag":"61%","star":4.5,"image":"https://images.irulu.com/product/cover/20151216/526fde00abad61423efeed.jpg","discountPrice":51.99,"productName":"iRULU eXpro X2 7 inch phablet GPS bluetooth 3G phone tablet PC","productId":1933422},{"commentNum":0,"id":0,"addWishList":0,"percent":"57","price":140.99,"tag":"Mobile","percentTag":"57%","star":0,"image":"https://images.irulu.com/product/cover/20151209/5268205a7e6c120f00ff38.jpg","discountPrice":59.99,"productName":"KW08 cell phone watch bluetooth GSM GPRS MTK6260 SIM card built-in camera","productId":1833402},{"commentNum":1,"id":0,"addWishList":0,"percent":"55","price":201.99,"tag":"Mobile","percentTag":"55%","star":5,"image":"https://images.irulu.com/product/cover/20151208/5266ef72f9429c1ce07fa.jpg","discountPrice":89.99,"productName":"iRULU WalknBook 3Mini Notebook 2-in-1 8 inch Intel Baytrail Quad Core laptop and tablet in one","productId":1133402},{"commentNum":2,"id":0,"addWishList":0,"percent":"51","price":50.99,"tag":"Mobile","percentTag":"51%","star":5,"image":"https://images.irulu.com/product/cover/20151025/522f80eca38e1155eec7e5.jpg","discountPrice":24.99,"productName":"Portable wireless soundbar bluetooth speaker stand","productId":1123499},{"commentNum":1,"id":0,"addWishList":0,"percent":"48","price":399.99,"tag":"Mobile","percentTag":"48%","star":5,"image":"https://images.irulu.com/product/cover/20151218/527269c2eadae17e94c7ce.jpg","discountPrice":209.99,"productName":"iRULU WalknBook 2Pro Notebook Tablet 2-in-1 W2Pro 11.6 inch Intel Baytrail-T Quad Core","productId":1333442},{"commentNum":14,"id":0,"addWishList":0,"percent":"46","price":92.88,"tag":"Mobile","percentTag":"46%","star":4.9,"image":"https://images.irulu.com/product/cover/20151203/525fa488d03491415e5218.jpg","discountPrice":49.99,"productName":"iRULU eXpro X1Pro Quad Core Android 4.4 Kitkat 9 Inch Tablet OTG function G sensor internal 3 point capacitive touch screen with keyboard","productId":1733481},{"commentNum":11,"id":0,"addWishList":0,"percent":"45","price":196.98,"tag":"Mobile","percentTag":"45%","star":4.8,"image":"https://images.irulu.com/product/cover/20151203/52608f0cbe5ab10d6be353.jpg","discountPrice":107.99,"productName":"iRULU eXpro X2Plus series android 4.4 high-speed Octa Core Tablet 16GB ROM 10 points Capacitive IPS touch screen Bluetooth 4.2 with keyboard","productId":1433491},{"commentNum":0,"id":0,"addWishList":0,"percent":"31","price":25.99,"tag":"Mobile","percentTag":"31%","star":0,"image":"https://images.irulu.com/product/cover/20151214/526d57a9c9f437d226249.jpg","discountPrice":17.99,"productName":"Small 1.8W spray painting modern table lamp","productId":1533412},{"commentNum":1,"id":0,"addWishList":0,"percent":"35","price":116.99,"tag":"Mobile","percentTag":"35%","star":5,"image":"https://images.irulu.com/product/cover/20151215/526fb37ca3aee951d215d.jpg","discountPrice":75.99,"productName":"S55 full HD 1080P 170°angle waterproof helmet action camera camcorder","productId":1233422},{"commentNum":0,"id":0,"addWishList":0,"percent":"20","price":99.99,"tag":"Mobile","percentTag":"20%","star":0,"image":"https://images.irulu.com/product/cover/20151216/5270e76410e8a139f13d56.jpg","discountPrice":79.99,"productName":"S20 full HD 1080P waterproof action video camcorder sports camera","productId":1633432},{"commentNum":1,"id":0,"addWishList":0,"percent":"49","price":90.99,"tag":"Mobile","percentTag":"49%","star":1,"image":"https://images.irulu.com/product/cover/20151221/52762bcf71e071a71e26b0.jpg","discountPrice":45.99,"productName":"iRULU eXpro tablet 9 inch 8GB Dual Core Android 4.2","productId":1033452},{"commentNum":1,"id":0,"addWishList":0,"percent":"31","price":144.99,"tag":"Mobile","percentTag":"31%","star":5,"image":"https://images.irulu.com/product/cover/20151126/5257c585f0ba57ae39884.jpg","discountPrice":99.99,"productName":"iRULU intelligent multi color LED light Wifi speaker smart bedroom lamp","productId":1333471},{"commentNum":0,"id":0,"addWishList":0,"percent":"24","price":20.99,"tag":"Mobile","percentTag":"24%","star":0,"image":"https://images.irulu.com/product/cover/20151203/525f80848ade41e6074321.jpg","discountPrice":15.99,"productName":"Universal 5 ports adapter 5V 4.1A 20.5W Multi-port USB charger","productId":1033481},{"commentNum":0,"id":0,"addWishList":0,"percent":"69","price":149.98,"tag":"Mobile","percentTag":"69%","star":0,"image":"https://images.irulu.com/product/cover/20151125/5255757f7b1001ea0a2572.jpg","discountPrice":45.99,"productName":"GBB automatic food packaging machine professional vacuum sealer FB159","productId":1633461}]}
     * new_arrivals : {"id":"3","title":"New Arrivals","slide":[],"type":"2","productList":[{"commentNum":0,"id":0,"is_new":1,"addWishList":0,"percent":"25","price":31.99,"tag":"Mobile","percentTag":"25%","star":0,"image":"https://images.irulu.com/product/cover/20160419/530e1d77c20e81949edde9.jpg","discountPrice":23.99,"productId":1763421},{"commentNum":0,"id":0,"is_new":1,"addWishList":0,"percent":"0","price":27.99,"tag":"Mobile","percentTag":"0%","star":0,"image":"https://images.irulu.com/product/cover/20160419/530e1c0608cb0150cdc50b.jpg","discountPrice":27.99,"productId":1363411},{"commentNum":0,"id":0,"is_new":1,"addWishList":0,"percent":"0","price":27.99,"tag":"Mobile","percentTag":"0%","star":0,"image":"https://images.irulu.com/product/cover/20160419/530e0fe38191118e625e59.jpg","discountPrice":27.99,"productId":1663401},{"commentNum":0,"id":0,"is_new":1,"addWishList":0,"percent":"9","price":21.99,"tag":"Mobile","percentTag":"9%","star":0,"image":"https://images.irulu.com/product/cover/20160419/530d2f69aeb6e15f22be41.jpg","discountPrice":19.99,"productId":1963490}]}
     * slide : [{"type":4,"content":"https://www.youtube.com/watch?v=nzZoWWmgREc","id":1313436,"image":"https://images.irulu.com/poster/20151123/5252fb1a31c42170e3633d.jpg"}]
     * type_id : 5
     */

    private DiscoverBean discover;
    /**
     * id : 2
     * title : Hot Sales
     * slide : []
     * type : 2
     * productList : [{"commentNum":75,"id":0,"addWishList":0,"percent":"73","price":131.99,"tag":"Mobile","percentTag":"73%","star":4.8,"is_hot":1,"image":"https://images.irulu.com/product/cover/20150828/51e5cb1ffb3921530cd3e5.jpg","discountPrice":35.99,"productName":"iRULU eXpro X1 7 inch android 4.4.2 Kitkat quad core 5 Point Capacitive HD Touch Screen tablet pc","productId":1913428},{"commentNum":125,"id":0,"addWishList":0,"percent":"85","price":119.99,"tag":"Mobile","percentTag":"85%","star":4.8,"is_hot":1,"image":"https://images.irulu.com/product/cover/20150828/51e5af769b9f812311154c.png","discountPrice":17.99,"productName":"U8 bluetooth Smartwatch fashionable wearing design","productId":1413456},{"commentNum":201,"id":0,"addWishList":0,"percent":"65","price":614.99,"tag":"Mobile","percentTag":"65%","star":4.8,"is_hot":1,"image":"https://images.irulu.com/product/cover/20150828/51e5ddefe2a14b811229a.png","discountPrice":212.99,"productName":"iRULU WalknBook 1 Notebook Tablet 2-in-1 10.1 inch 32GB laptop tablet detachable keyboard","productId":1423400},{"commentNum":4,"id":0,"addWishList":0,"percent":"23","price":12.99,"tag":"Mobile","percentTag":"23%","star":5,"is_hot":1,"image":"https://images.irulu.com/product/cover/20151022/522bbdcfc49e5e1f4afb9.jpg","discountPrice":9.99,"productName":"6.5 inch tempered glass phone screen protector for iRULU V3 smartphone","productId":1723479},{"commentNum":6,"id":0,"addWishList":1,"percent":"67","price":29.99,"tag":"Mobile","percentTag":"67%","star":5,"is_hot":1,"image":"https://images.irulu.com/product/cover/20150827/51e56f96db8c54b95dc01.jpg","discountPrice":9.99,"productName":"iRULU V3 Leather Back Cover Cell Phone Case","productId":1523403},{"commentNum":20,"id":0,"addWishList":0,"percent":"52","price":330.99,"tag":"Mobile","percentTag":"52%","star":4.9,"is_hot":1,"image":"https://images.irulu.com/product/cover/20151102/5238af35b429823112064d.jpg","discountPrice":159.99,"productName":"iRULU WalknBook 3 Notebook Tablet 2-in-1 10.1 inch 32GB convertible laptop tablet hybrid with detachable keyboard","productId":1533400},{"commentNum":145,"id":0,"addWishList":1,"percent":"44","price":232.99,"tag":"Mobile","percentTag":"44%","star":4.7,"is_hot":1,"image":"https://images.irulu.com/product/cover/20150915/51fc15155c8c310342c001.jpg","discountPrice":129.99,"productName":"iRULU Victory V3 Smartphone 6.5 Inch 4G LTE Unlock Cell Phone Android 5.1 GSM WCDMA IPS touch screen Qualcomm quad core GMS certified","productId":1123492},{"commentNum":50,"id":0,"addWishList":0,"percent":"59","price":179.99,"tag":"Mobile","percentTag":"59%","star":4.8,"is_hot":1,"image":"https://images.irulu.com/product/cover/20160129/52a77552b8f9166b266fa.jpg","discountPrice":72.99,"productName":"iRULU eXpro X1Plus 10.1 inch quad core tablet Android 5.1 Lollipop support Wifi OTG function Bluetooth 4.0","productId":1023484},{"commentNum":6,"id":0,"addWishList":0,"percent":"61","price":131.99,"tag":"Mobile","percentTag":"61%","star":4.5,"is_hot":1,"image":"https://images.irulu.com/product/cover/20151216/526fde00abad61423efeed.jpg","discountPrice":51.99,"productName":"iRULU eXpro X2 7 inch phablet GPS bluetooth 3G phone tablet PC","productId":1933422},{"commentNum":5,"id":0,"addWishList":0,"percent":"68","price":13.29,"tag":"Mobile","percentTag":"68%","star":4.2,"is_hot":1,"image":"https://images.irulu.com/product/cover/20150828/51e5d779003a8f2dcdfd9.png","discountPrice":4.29,"productName":"10.1 inch PET tablet screen protector 3 in 1 pack","productId":1613409},{"commentNum":5,"id":0,"addWishList":1,"percent":"72","price":3.49,"tag":"Mobile","percentTag":"72%","star":5,"is_hot":1,"image":"https://images.irulu.com/product/cover/20150828/51e6a41bdd09d1b6a2f6b7.jpg","discountPrice":0.99,"productName":"High quality universal 3.5mm in-ear stereo headset","productId":1923430},{"commentNum":2,"id":0,"addWishList":1,"percent":"77","price":29.96,"tag":"Mobile","percentTag":"77%","star":4,"is_hot":1,"image":"https://images.irulu.com/product/cover/20150915/51fc5b0d2c05515571dafd.jpg","discountPrice":6.96,"productName":"LY-700 Handfree Wireless Sports in Ear Bluetooth Headset","productId":1223413},{"commentNum":12,"id":0,"addWishList":0,"percent":"35","price":16.99,"tag":"Mobile","percentTag":"35%","star":4.4,"is_hot":1,"image":"https://images.irulu.com/product/cover/20150830/51e91e52fb645224705556.png","discountPrice":10.99,"productName":"Protective PU leather 10.1 inch tablet keyboard case","productId":1323450},{"commentNum":2,"id":0,"addWishList":0,"percent":"60","price":24.89,"tag":"Mobile","percentTag":"60%","star":5,"is_hot":1,"image":"https://images.irulu.com/product/cover/20150830/51e9221087b44bbe5187d.png","discountPrice":9.89,"productName":"Protective PU leather 9 inch tablet keyboard case","productId":1023460},{"commentNum":0,"id":0,"addWishList":0,"percent":"71","price":13.99,"tag":"Mobile","percentTag":"71%","star":0,"is_hot":1,"image":"https://images.irulu.com/product/cover/20150915/51fc2eae367af1dacb7459.jpg","discountPrice":3.99,"productName":"Convenient Stable Cell Phone Car Mount","productId":1823492},{"commentNum":2,"id":0,"addWishList":0,"percent":"61","price":79.99,"tag":"Mobile","percentTag":"61%","star":5,"is_hot":1,"image":"https://images.irulu.com/product/cover/20151010/521bb7f6116bc15b7ed7d6.jpg","discountPrice":31.49,"productName":"Portable high capacity external battery power bank 20000mAh","productId":1123458},{"commentNum":32,"id":0,"addWishList":0,"percent":"58","price":180.99,"tag":"Mobile","percentTag":"58%","star":4.9,"is_hot":1,"image":"https://images.irulu.com/product/cover/20151117/524c7694be7ba1d6918fbf.jpg","discountPrice":75.99,"productName":"iRULU eXpro X1Plus 10.1 inch quad core tablet Android 5.1 Lollipop support Wifi OTG function Bluetooth 4.0 with case","productId":1033411},{"commentNum":2,"id":0,"addWishList":0,"percent":"69","price":15.99,"tag":"Mobile","percentTag":"69%","star":5,"is_hot":1,"image":"https://images.irulu.com/product/cover/20150828/51e5d9742e179178974e1c.jpg","discountPrice":4.99,"productName":"Luxury PU leather flip IRULU V1 leather phone case","productId":1513459},{"commentNum":5,"id":0,"addWishList":0,"percent":"39","price":69.99,"tag":"Mobile","percentTag":"39%","star":4.8,"is_hot":1,"image":"https://images.irulu.com/product/cover/20150907/51f220ead7d93af59f92d.jpg","discountPrice":42.99,"productName":"iRULU GV08 Bluetooth Touch Screen Smart Phone Watch","productId":1423441},{"commentNum":27,"id":0,"addWishList":0,"percent":"61","price":108.99,"tag":"Mobile","percentTag":"61%","star":4.7,"is_hot":1,"image":"https://images.irulu.com/product/cover/20151104/523b3d15c7aab117becf73.jpg","discountPrice":41.99,"productName":"iRULU eXpro X1 7 inch quad core HD Touch Screen tablet with keyboard cartoon leather case","productId":1933410}]
     */

    private HotSalesBean hot_sales;

    public DiscoverBean getDiscover() {
        return discover;
    }

    public void setDiscover(DiscoverBean discover) {
        this.discover = discover;
    }

    public HotSalesBean getHot_sales() {
        return hot_sales;
    }

    public void setHot_sales(HotSalesBean hot_sales) {
        this.hot_sales = hot_sales;
    }

    public static class DiscoverBean implements Parcelable {
        /**
         * product : {"commentNum":10,"id":"59","addWishList":0,"percent":"59","price":"104.99","tag":"","percentTag":"59%","star":4.8,"image":"https://images.irulu.com/product/cover/20151008/521a2f166b2dd10e4598fd.png","discountPrice":"43.99","productName":"iRULU eXpro X1Pro Quad Core Android 4.4 Kitkat 9 Inch Tablet OTG function G sensor internal 3 point capacitive touch screen","productId":1723431}
         * limit_time : 1461643199
         * name : Daily Deals
         */

        private DailyDealsBean daily_deals;
        private String title;
        /**
         * eventInfo : []
         * title : Events Deals
         */

        private EventsDealsBean events_deals;
        private String priceSymbol;
        /**
         * id : 1
         * title : Recommend
         * slide : [{"type":"3","content":"javascript:;","id":"1","image":"https://images.irulu.com/poster/20160127/52a5b4285d2791e97ab682.jpg"},{"type":"2","content":"1123492","id":"2","image":"https://images.irulu.com/poster/20160104/5287e1748320d1a2161e86.jpg"},{"type":"2","content":"1333471","id":"6","image":"https://images.irulu.com/poster/20160107/528b9484bdd466a38074a.jpg"}]
         * type : 1
         * productList : [{"commentNum":5,"id":0,"addWishList":0,"percent":"39","price":69.99,"tag":"Mobile","percentTag":"39%","star":4.8,"image":"https://images.irulu.com/product/cover/20150907/51f220ead7d93af59f92d.jpg","discountPrice":42.99,"productName":"iRULU GV08 Bluetooth Touch Screen Smart Phone Watch","productId":1423441},{"commentNum":4,"id":0,"addWishList":0,"percent":"59","price":289.99,"tag":"Mobile","percentTag":"59%","star":5,"image":"https://images.irulu.com/product/cover/20150909/51f4ab5f0e5531fe9c57a7.jpg","discountPrice":119,"productName":"iRULU P2 Smart Phone Tablet Phablet 2GB RAM 16GB ROM 3G GSM WCDMA Android 4.4.0 Octa core mobile Internet multimedia device","productId":1323491},{"commentNum":1,"id":0,"addWishList":0,"percent":"57","price":174.99,"tag":"Mobile","percentTag":"57%","star":5,"image":"https://images.irulu.com/product/cover/20150909/51f4b026140409c20109d.jpg","discountPrice":75.99,"productName":"G2 TPU Band Bluetooth Waterproof Smartwatch","productId":1023402},{"commentNum":2,"id":0,"addWishList":0,"percent":"29","price":16.99,"tag":"Mobile","percentTag":"29%","star":5,"image":"https://images.irulu.com/product/cover/20150909/51f4daf8436a71f2afb071.jpg","discountPrice":11.99,"productName":"iRULU Portable Mini Wireless Bluetooth Speaker","productId":1923442},{"commentNum":1,"id":0,"addWishList":0,"percent":"30","price":19.99,"tag":"Mobile","percentTag":"30%","star":5,"image":"https://images.irulu.com/product/cover/20150910/51f6114e83cb4d52046b3.jpg","discountPrice":13.99,"productName":"Fashionable 3.5mm Hi-Fi Stereo Sports USB Headset","productId":1623452},{"commentNum":0,"id":0,"addWishList":1,"percent":"65","price":45.99,"tag":"Mobile","percentTag":"65%","star":0,"image":"https://images.irulu.com/product/cover/20150910/51f6151df71c910ca28059.jpg","discountPrice":15.99,"productName":"iRULU Ozone Output 2mg/h 500mAh Air Sterilizer","productId":1323462},{"commentNum":6,"id":0,"addWishList":0,"percent":"61","price":131.99,"tag":"Mobile","percentTag":"61%","star":4.5,"image":"https://images.irulu.com/product/cover/20151216/526fde00abad61423efeed.jpg","discountPrice":51.99,"productName":"iRULU eXpro X2 7 inch phablet GPS bluetooth 3G phone tablet PC","productId":1933422},{"commentNum":0,"id":0,"addWishList":0,"percent":"57","price":140.99,"tag":"Mobile","percentTag":"57%","star":0,"image":"https://images.irulu.com/product/cover/20151209/5268205a7e6c120f00ff38.jpg","discountPrice":59.99,"productName":"KW08 cell phone watch bluetooth GSM GPRS MTK6260 SIM card built-in camera","productId":1833402},{"commentNum":1,"id":0,"addWishList":0,"percent":"55","price":201.99,"tag":"Mobile","percentTag":"55%","star":5,"image":"https://images.irulu.com/product/cover/20151208/5266ef72f9429c1ce07fa.jpg","discountPrice":89.99,"productName":"iRULU WalknBook 3Mini Notebook 2-in-1 8 inch Intel Baytrail Quad Core laptop and tablet in one","productId":1133402},{"commentNum":2,"id":0,"addWishList":0,"percent":"51","price":50.99,"tag":"Mobile","percentTag":"51%","star":5,"image":"https://images.irulu.com/product/cover/20151025/522f80eca38e1155eec7e5.jpg","discountPrice":24.99,"productName":"Portable wireless soundbar bluetooth speaker stand","productId":1123499},{"commentNum":1,"id":0,"addWishList":0,"percent":"48","price":399.99,"tag":"Mobile","percentTag":"48%","star":5,"image":"https://images.irulu.com/product/cover/20151218/527269c2eadae17e94c7ce.jpg","discountPrice":209.99,"productName":"iRULU WalknBook 2Pro Notebook Tablet 2-in-1 W2Pro 11.6 inch Intel Baytrail-T Quad Core","productId":1333442},{"commentNum":14,"id":0,"addWishList":0,"percent":"46","price":92.88,"tag":"Mobile","percentTag":"46%","star":4.9,"image":"https://images.irulu.com/product/cover/20151203/525fa488d03491415e5218.jpg","discountPrice":49.99,"productName":"iRULU eXpro X1Pro Quad Core Android 4.4 Kitkat 9 Inch Tablet OTG function G sensor internal 3 point capacitive touch screen with keyboard","productId":1733481},{"commentNum":11,"id":0,"addWishList":0,"percent":"45","price":196.98,"tag":"Mobile","percentTag":"45%","star":4.8,"image":"https://images.irulu.com/product/cover/20151203/52608f0cbe5ab10d6be353.jpg","discountPrice":107.99,"productName":"iRULU eXpro X2Plus series android 4.4 high-speed Octa Core Tablet 16GB ROM 10 points Capacitive IPS touch screen Bluetooth 4.2 with keyboard","productId":1433491},{"commentNum":0,"id":0,"addWishList":0,"percent":"31","price":25.99,"tag":"Mobile","percentTag":"31%","star":0,"image":"https://images.irulu.com/product/cover/20151214/526d57a9c9f437d226249.jpg","discountPrice":17.99,"productName":"Small 1.8W spray painting modern table lamp","productId":1533412},{"commentNum":1,"id":0,"addWishList":0,"percent":"35","price":116.99,"tag":"Mobile","percentTag":"35%","star":5,"image":"https://images.irulu.com/product/cover/20151215/526fb37ca3aee951d215d.jpg","discountPrice":75.99,"productName":"S55 full HD 1080P 170°angle waterproof helmet action camera camcorder","productId":1233422},{"commentNum":0,"id":0,"addWishList":0,"percent":"20","price":99.99,"tag":"Mobile","percentTag":"20%","star":0,"image":"https://images.irulu.com/product/cover/20151216/5270e76410e8a139f13d56.jpg","discountPrice":79.99,"productName":"S20 full HD 1080P waterproof action video camcorder sports camera","productId":1633432},{"commentNum":1,"id":0,"addWishList":0,"percent":"49","price":90.99,"tag":"Mobile","percentTag":"49%","star":1,"image":"https://images.irulu.com/product/cover/20151221/52762bcf71e071a71e26b0.jpg","discountPrice":45.99,"productName":"iRULU eXpro tablet 9 inch 8GB Dual Core Android 4.2","productId":1033452},{"commentNum":1,"id":0,"addWishList":0,"percent":"31","price":144.99,"tag":"Mobile","percentTag":"31%","star":5,"image":"https://images.irulu.com/product/cover/20151126/5257c585f0ba57ae39884.jpg","discountPrice":99.99,"productName":"iRULU intelligent multi color LED light Wifi speaker smart bedroom lamp","productId":1333471},{"commentNum":0,"id":0,"addWishList":0,"percent":"24","price":20.99,"tag":"Mobile","percentTag":"24%","star":0,"image":"https://images.irulu.com/product/cover/20151203/525f80848ade41e6074321.jpg","discountPrice":15.99,"productName":"Universal 5 ports adapter 5V 4.1A 20.5W Multi-port USB charger","productId":1033481},{"commentNum":0,"id":0,"addWishList":0,"percent":"69","price":149.98,"tag":"Mobile","percentTag":"69%","star":0,"image":"https://images.irulu.com/product/cover/20151125/5255757f7b1001ea0a2572.jpg","discountPrice":45.99,"productName":"GBB automatic food packaging machine professional vacuum sealer FB159","productId":1633461}]
         */

        private RecommendationBean recommendation;
        /**
         * id : 3
         * title : New Arrivals
         * slide : []
         * type : 2
         * productList : [{"commentNum":0,"id":0,"is_new":1,"addWishList":0,"percent":"25","price":31.99,"tag":"Mobile","percentTag":"25%","star":0,"image":"https://images.irulu.com/product/cover/20160419/530e1d77c20e81949edde9.jpg","discountPrice":23.99,"productId":1763421},{"commentNum":0,"id":0,"is_new":1,"addWishList":0,"percent":"0","price":27.99,"tag":"Mobile","percentTag":"0%","star":0,"image":"https://images.irulu.com/product/cover/20160419/530e1c0608cb0150cdc50b.jpg","discountPrice":27.99,"productId":1363411},{"commentNum":0,"id":0,"is_new":1,"addWishList":0,"percent":"0","price":27.99,"tag":"Mobile","percentTag":"0%","star":0,"image":"https://images.irulu.com/product/cover/20160419/530e0fe38191118e625e59.jpg","discountPrice":27.99,"productId":1663401},{"commentNum":0,"id":0,"is_new":1,"addWishList":0,"percent":"9","price":21.99,"tag":"Mobile","percentTag":"9%","star":0,"image":"https://images.irulu.com/product/cover/20160419/530d2f69aeb6e15f22be41.jpg","discountPrice":19.99,"productId":1963490}]
         */

        private NewArrivalsBean new_arrivals;
        private int type_id;
        /**
         * id : 0
         * icon :
         * count : 0
         * app_icon : https://dn-irulu.qbox.me/app/All-Categories@3x.png
         * name : All Categories
         */

        private List<CatalogBean> catalog;
        /**
         * type : 4
         * content : https://www.youtube.com/watch?v=nzZoWWmgREc
         * id : 1313436
         * image : https://images.irulu.com/poster/20151123/5252fb1a31c42170e3633d.jpg
         */

        private List<SlideBean> slide;

        public DailyDealsBean getDaily_deals() {
            return daily_deals;
        }

        public void setDaily_deals(DailyDealsBean daily_deals) {
            this.daily_deals = daily_deals;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public EventsDealsBean getEvents_deals() {
            return events_deals;
        }

        public void setEvents_deals(EventsDealsBean events_deals) {
            this.events_deals = events_deals;
        }

        public String getPriceSymbol() {
            return priceSymbol;
        }

        public void setPriceSymbol(String priceSymbol) {
            this.priceSymbol = priceSymbol;
        }

        public RecommendationBean getRecommendation() {
            return recommendation;
        }

        public void setRecommendation(RecommendationBean recommendation) {
            this.recommendation = recommendation;
        }

        public NewArrivalsBean getNew_arrivals() {
            return new_arrivals;
        }

        public void setNew_arrivals(NewArrivalsBean new_arrivals) {
            this.new_arrivals = new_arrivals;
        }

        public int getType_id() {
            return type_id;
        }

        public void setType_id(int type_id) {
            this.type_id = type_id;
        }

        public List<CatalogBean> getCatalog() {
            return catalog;
        }

        public void setCatalog(List<CatalogBean> catalog) {
            this.catalog = catalog;
        }

        public List<SlideBean> getSlide() {
            return slide;
        }

        public void setSlide(List<SlideBean> slide) {
            this.slide = slide;
        }

        public static class DailyDealsBean implements Parcelable {
            /**
             * commentNum : 10
             * id : 59
             * addWishList : 0
             * percent : 59
             * price : 104.99
             * tag :
             * percentTag : 59%
             * star : 4.8
             * image : https://images.irulu.com/product/cover/20151008/521a2f166b2dd10e4598fd.png
             * discountPrice : 43.99
             * productName : iRULU eXpro X1Pro Quad Core Android 4.4 Kitkat 9 Inch Tablet OTG function G sensor internal 3 point capacitive touch screen
             * productId : 1723431
             */

            private ProductInfo product;
            private long limit_time;
            private String name;

            public ProductInfo getProduct() {
                return product;
            }

            public void setProduct(ProductInfo product) {
                this.product = product;
            }

            public long getLimit_time() {
                return limit_time;
            }

            public void setLimit_time(int limit_time) {
                this.limit_time = limit_time;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeParcelable(this.product,flags);
                dest.writeLong(this.limit_time);
                dest.writeString(this.name);
            }

            public DailyDealsBean() {
            }

            protected DailyDealsBean(Parcel in) {
                this.product = in.readParcelable(ProductInfo.class.getClassLoader());
                this.limit_time = in.readInt();
                this.name = in.readString();
            }

            public static final Creator<DailyDealsBean> CREATOR = new Creator<DailyDealsBean>() {
                @Override
                public DailyDealsBean createFromParcel(Parcel source) {
                    return new DailyDealsBean(source);
                }

                @Override
                public DailyDealsBean[] newArray(int size) {
                    return new DailyDealsBean[size];
                }
            };
        }

        public static class EventsDealsBean implements Parcelable {
            private String title;
            private EventInfoBean eventInfo;

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public EventInfoBean getEventInfo() {
                return eventInfo;
            }

            public void setEventInfo(EventInfoBean eventInfo) {
                this.eventInfo = eventInfo;
            }

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeString(this.title);
                dest.writeParcelable(this.eventInfo,flags);
            }

            public EventsDealsBean() {
            }

            protected EventsDealsBean(Parcel in) {
                this.title = in.readString();
                this.eventInfo = in.readParcelable(EventInfoBean.class.getClassLoader());
            }

            public static final Creator<EventsDealsBean> CREATOR = new Creator<EventsDealsBean>() {
                @Override
                public EventsDealsBean createFromParcel(Parcel source) {
                    return new EventsDealsBean(source);
                }

                @Override
                public EventsDealsBean[] newArray(int size) {
                    return new EventsDealsBean[size];
                }
            };
        }

        public static class EventInfoBean implements Parcelable {
            private int type;
            private String content;
            private String image;

            public int getType() {
                return type;
            }

            public void setType(int type) {
                this.type = type;
            }

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public String getImage() {
                return image;
            }

            public void setImage(String image) {
                this.image = image;
            }

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeInt(this.type);
                dest.writeString(this.content);
                dest.writeString(this.image);
            }

            public EventInfoBean() {
            }

            protected EventInfoBean(Parcel in) {
                this.type = in.readInt();
                this.content = in.readString();
                this.image = in.readString();
            }

            public static final Creator<EventInfoBean> CREATOR = new Creator<EventInfoBean>() {
                @Override
                public EventInfoBean createFromParcel(Parcel source) {
                    return new EventInfoBean(source);
                }

                @Override
                public EventInfoBean[] newArray(int size) {
                    return new EventInfoBean[size];
                }
            };
        }

        public static class RecommendationBean implements Parcelable {
            private String id;
            private String title;
            private String type;
            /**
             * type : 3
             * content : javascript:;
             * id : 1
             * image : https://images.irulu.com/poster/20160127/52a5b4285d2791e97ab682.jpg
             */

            private List<SlideBean> slide;
            /**
             * commentNum : 5
             * id : 0
             * addWishList : 0
             * percent : 39
             * price : 69.99
             * tag : Mobile
             * percentTag : 39%
             * star : 4.8
             * image : https://images.irulu.com/product/cover/20150907/51f220ead7d93af59f92d.jpg
             * discountPrice : 42.99
             * productName : iRULU GV08 Bluetooth Touch Screen Smart Phone Watch
             * productId : 1423441
             */

            private List<ProductInfo> productList;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public List<SlideBean> getSlide() {
                return slide;
            }

            public void setSlide(List<SlideBean> slide) {
                this.slide = slide;
            }

            public List<ProductInfo> getProductList() {
                return productList;
            }

            public void setProductList(List<ProductInfo> productList) {
                this.productList = productList;
            }

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeString(this.id);
                dest.writeString(this.title);
                dest.writeString(this.type);
                dest.writeList(this.slide);
                dest.writeList(this.productList);
            }

            public RecommendationBean() {
            }

            protected RecommendationBean(Parcel in) {
                this.id = in.readString();
                this.title = in.readString();
                this.type = in.readString();
                this.slide = new ArrayList<SlideBean>();
                in.readList(this.slide, SlideBean.class.getClassLoader());
                this.productList = new ArrayList<ProductInfo>();
                in.readList(this.productList, ProductInfo.class.getClassLoader());
            }

            public static final Creator<RecommendationBean> CREATOR = new Creator<RecommendationBean>() {
                @Override
                public RecommendationBean createFromParcel(Parcel source) {
                    return new RecommendationBean(source);
                }

                @Override
                public RecommendationBean[] newArray(int size) {
                    return new RecommendationBean[size];
                }
            };
        }

        public static class NewArrivalsBean implements Parcelable {
            private String id;
            private String title;
            private String type;
            private List<SlideBean> slide;
            /**
             * commentNum : 0
             * id : 0
             * is_new : 1
             * addWishList : 0
             * percent : 25
             * price : 31.99
             * tag : Mobile
             * percentTag : 25%
             * star : 0
             * image : https://images.irulu.com/product/cover/20160419/530e1d77c20e81949edde9.jpg
             * discountPrice : 23.99
             * productId : 1763421
             */

            private List<ProductInfo> productList;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public List<?> getSlide() {
                return slide;
            }

            public void setSlide(List<SlideBean> slide) {
                this.slide = slide;
            }

            public List<ProductInfo> getProductList() {
                return productList;
            }

            public void setProductList(List<ProductInfo> productList) {
                this.productList = productList;
            }

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeString(this.id);
                dest.writeString(this.title);
                dest.writeString(this.type);
                dest.writeList(this.slide);
                dest.writeList(this.productList);
            }

            public NewArrivalsBean() {
            }

            protected NewArrivalsBean(Parcel in) {
                this.id = in.readString();
                this.title = in.readString();
                this.type = in.readString();
                this.slide = new ArrayList<SlideBean>();
                in.readList(this.slide, SlideBean.class.getClassLoader());
                this.productList = new ArrayList<ProductInfo>();
                in.readList(this.productList, ProductInfo.class.getClassLoader());
            }

            public static final Creator<NewArrivalsBean> CREATOR = new Creator<NewArrivalsBean>() {
                @Override
                public NewArrivalsBean createFromParcel(Parcel source) {
                    return new NewArrivalsBean(source);
                }

                @Override
                public NewArrivalsBean[] newArray(int size) {
                    return new NewArrivalsBean[size];
                }
            };
        }

        public static class CatalogBean {
            private int id;
            private String icon;
            private int count;
            private String app_icon;
            private String name;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getIcon() {
                return icon;
            }

            public void setIcon(String icon) {
                this.icon = icon;
            }

            public int getCount() {
                return count;
            }

            public void setCount(int count) {
                this.count = count;
            }

            public String getApp_icon() {
                return app_icon;
            }

            public void setApp_icon(String app_icon) {
                this.app_icon = app_icon;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }
        }



        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeParcelable(this.daily_deals, flags);
            dest.writeString(this.title);
            dest.writeParcelable(this.events_deals, flags);
            dest.writeString(this.priceSymbol);
            dest.writeParcelable(this.recommendation, flags);
            dest.writeParcelable(this.new_arrivals, flags);
            dest.writeInt(this.type_id);
            dest.writeList(this.catalog);
            dest.writeList(this.slide);
        }

        public DiscoverBean() {
        }

        protected DiscoverBean(Parcel in) {
            this.daily_deals = in.readParcelable(DailyDealsBean.class.getClassLoader());
            this.title = in.readString();
            this.events_deals = in.readParcelable(EventsDealsBean.class.getClassLoader());
            this.priceSymbol = in.readString();
            this.recommendation = in.readParcelable(RecommendationBean.class.getClassLoader());
            this.new_arrivals = in.readParcelable(NewArrivalsBean.class.getClassLoader());
            this.type_id = in.readInt();
            this.catalog = new ArrayList<CatalogBean>();
            in.readList(this.catalog, CatalogBean.class.getClassLoader());
            this.slide = new ArrayList<SlideBean>();
            in.readList(this.slide, SlideBean.class.getClassLoader());
        }

        public static final Creator<DiscoverBean> CREATOR = new Creator<DiscoverBean>() {
            @Override
            public DiscoverBean createFromParcel(Parcel source) {
                return new DiscoverBean(source);
            }

            @Override
            public DiscoverBean[] newArray(int size) {
                return new DiscoverBean[size];
            }
        };
    }

    public static class HotSalesBean implements Parcelable {
        private String id;
        private String title;
        private String type;
        private List<SlideBean> slide;
        /**
         * commentNum : 75
         * id : 0
         * addWishList : 0
         * percent : 73
         * price : 131.99
         * tag : Mobile
         * percentTag : 73%
         * star : 4.8
         * is_hot : 1
         * image : https://images.irulu.com/product/cover/20150828/51e5cb1ffb3921530cd3e5.jpg
         * discountPrice : 35.99
         * productName : iRULU eXpro X1 7 inch android 4.4.2 Kitkat quad core 5 Point Capacitive HD Touch Screen tablet pc
         * productId : 1913428
         */

        private List<ProductInfo> productList;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public List<SlideBean> getSlide() {
            return slide;
        }

        public void setSlide(List<SlideBean> slide) {
            this.slide = slide;
        }

        public List<ProductInfo> getProductList() {
            return productList;
        }

        public void setProductList(List<ProductInfo> productList) {
            this.productList = productList;
        }


        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.id);
            dest.writeString(this.title);
            dest.writeString(this.type);
            dest.writeList(this.slide);
            dest.writeList(this.productList);
        }

        public HotSalesBean() {
        }

        protected HotSalesBean(Parcel in) {
            this.id = in.readString();
            this.title = in.readString();
            this.type = in.readString();
            this.slide = new ArrayList<SlideBean>();
            in.readList(this.slide, SlideBean.class.getClassLoader());
            this.productList = new ArrayList<ProductInfo>();
            in.readList(this.productList, ProductInfo.class.getClassLoader());
        }

        public static final Creator<HotSalesBean> CREATOR = new Creator<HotSalesBean>() {
            @Override
            public HotSalesBean createFromParcel(Parcel source) {
                return new HotSalesBean(source);
            }

            @Override
            public HotSalesBean[] newArray(int size) {
                return new HotSalesBean[size];
            }
        };
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.discover, flags);
        dest.writeParcelable(this.hot_sales, flags);
    }

    public HomeBean() {
    }

    protected HomeBean(Parcel in) {
        this.discover = in.readParcelable(DiscoverBean.class.getClassLoader());
        this.hot_sales = in.readParcelable(HotSalesBean.class.getClassLoader());
    }

    public static final Parcelable.Creator<HomeBean> CREATOR = new Parcelable.Creator<HomeBean>() {
        @Override
        public HomeBean createFromParcel(Parcel source) {
            return new HomeBean(source);
        }

        @Override
        public HomeBean[] newArray(int size) {
            return new HomeBean[size];
        }
    };
}

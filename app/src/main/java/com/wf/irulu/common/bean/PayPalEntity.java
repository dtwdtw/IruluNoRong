package com.wf.irulu.common.bean;

import android.os.Parcelable;

import java.io.Serializable;

/**
 * @描述: 支付描述
 * @项目名: irulu1.2
 * @包名:com.wf.irulu.common.bean
 * @类名:PayPalEntity
 * @作者: 左杰
 * @创建时间:2015/11/17 19:03
 */
public class PayPalEntity implements Serializable {
    public Response response;

    public class Response implements Serializable {
        public String id;
    }
}

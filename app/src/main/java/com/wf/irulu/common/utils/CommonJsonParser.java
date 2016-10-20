package com.wf.irulu.common.utils;

import com.wf.irulu.common.bean.CommonServiceBean;

import org.json.JSONObject;

/**
 * @描述: 解析基类
 *
 * @项目名: irulu
 * @包名:com.wf.irulu.logic.parser
 * @类名:CommonJsonParser
 * @作者: 左西杰
 * @创建时间:2015-5-27 下午6:57:39
 * 
 */

public class CommonJsonParser {
	
	/**
	 * 解析服务器数据
	 * @param jsonStr 服务器返回json数据
	 * @return CommonServiceBean
	 */
	public static CommonServiceBean commonParser(String jsonStr){
		
		CommonServiceBean bean = new CommonServiceBean();
		
        if (StringUtils.isEmpty(jsonStr)) {
			
			bean.code = ErrorCodeUtils.ERROR_NO_RESPONSE;
			
			return bean;
		}
        
        try {
        	JSONObject jsonObj = new JSONObject(jsonStr);
     		
        	if (jsonObj.has("code")) {
        		
        		bean.code = jsonObj.getInt("code");
        		
			}
        	
			if (jsonObj.has("msg")) {
				
				bean.msg = jsonObj.getString("msg");
				
			}
			
			if (jsonObj.has("data")) {
				
				bean.data = jsonObj.get("data");
				
			}
		} catch (Exception e) {
			e.printStackTrace();
			bean.code = ErrorCodeUtils.ERROR_PARSER;
		}
        
        // 保证data不为空，以便其他地方调用
        if (bean.data == null) {
        	bean.data = new Object();
		}
        return bean;
	}
}

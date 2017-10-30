/*
 * Copyright (c) 2015, NJXS All Rights Reserved.
 * CookieRequest.java
 * com.xuesi.mos.net.volley
 *
 * Function�� TODO 
 *
 *   ver     date      		author
 * ������������������������������������������������������������������
 *   		 2015-10-13 	lzq
 *
 */

package com.xuesi.mos.net.volley;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import com.xuesi.mos.libs.volley.AuthFailureError;
import com.xuesi.mos.libs.volley.Response.ErrorListener;
import com.xuesi.mos.libs.volley.Response.Listener;
import com.xuesi.mos.libs.volley.toolbox.JsonObjectRequest;

/**
 * ClassName:CookieRequest
 * 
 * Function: TODO ADD FUNCTION
 * 
 * Reason: TODO ADD REASON
 * 
 * @author lzq
 * @version jdk1.8.0_25
 * @since Ver 1.0.0
 * @Date 2015-10-13
 */
public class CookieRequest extends JsonObjectRequest {
	private Map<String, String> mHeaders = new HashMap<String, String>(1);

	public CookieRequest(int method, String url, JSONObject jsonRequest, Listener<JSONObject> listener,
			ErrorListener errorListener) {
		super(method, url, jsonRequest, listener, errorListener);
		// TODO Auto-generated constructor stub
	}

	public CookieRequest(String url, JSONObject jsonRequest, Listener<JSONObject> listener, ErrorListener errorListener) {
		super(url, jsonRequest, listener, errorListener);
	}

	public void setCookie(String cookie) {
		mHeaders.put("Cookie", cookie);
	}

	@Override
	public Map<String, String> getHeaders() throws AuthFailureError {
		return mHeaders;
	}

}

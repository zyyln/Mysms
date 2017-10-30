/*
 * MosVolleyManager.java
 * com.xuesi.mos.net
 *
 * Function�� TODO 
 *
 *   ver     date      		author
 * ──────────────────────────────────
 *   		 2015-10-12 	lzq
 *
 * Copyright (c) 2015, NJXS All Rights Reserved.
 */

package com.xuesi.mos.net.volley;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActivityManager;
import android.content.Context;
import android.util.Log;

import com.xuesi.mos.libs.volley.AuthFailureError;
import com.xuesi.mos.libs.volley.DefaultRetryPolicy;
import com.xuesi.mos.libs.volley.NetworkResponse;
import com.xuesi.mos.libs.volley.ParseError;
import com.xuesi.mos.libs.volley.Request.Method;
import com.xuesi.mos.libs.volley.RequestQueue;
import com.xuesi.mos.libs.volley.Response;
import com.xuesi.mos.libs.volley.VolleyError;
import com.xuesi.mos.libs.volley.toolbox.HttpHeaderParser;
import com.xuesi.mos.libs.volley.toolbox.ImageLoader;
import com.xuesi.mos.libs.volley.toolbox.JsonObjectRequest;
import com.xuesi.mos.libs.volley.toolbox.Volley;
import com.xuesi.mos.util.MosLog;

/**
 * ClassName:MosVolleyManager
 * 
 * Function: TODO ADD FUNCTION
 * 
 * Reason: TODO ADD REASON
 * 
 * @author lzq
 * @version jdk1.8.0_25
 * @since Ver 1.0.0
 * @Date 2015-10-12
 */
public class MosVolleyUtil {
	private final String LOG_TAG = MosVolleyUtil.class.getSimpleName();
	private final MosLog mosLog = MosLog.getInstance(LOG_TAG, "MosHelper");

	private Context mContext;
	private static MosVolleyUtil singleInstance;
	private static RequestQueue mRequestQueue;
	private static ImageLoader mImageLoader;

	private MosVolleyUtil(Context context) {
		this.mContext = context;
		this.mRequestQueue = Volley.newRequestQueue(context);

		int memClass = ((ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE)).getMemoryClass();
		int cacheSize = 1024 * 1024 * memClass / 8;
		// mImageLoader = new ImageLoader(mRequestQueue, new BitmapLruCache(
		// cacheSize));
	}

	public static MosVolleyUtil getInstance(Context context) {
		if (singleInstance == null) {
			singleInstance = new MosVolleyUtil(context);
		}
		return singleInstance;
	}

	public void executePost(String url, JSONObject jsonRequest,
			Map<String, String> headers, RequestResultCallback rrc,
			String requestTag, boolean isCacheData) {
		if (requestTag == null) {
			throw new NullPointerException("requestTag can not be null");
		}
		if (rrc == null) {
			throw new NullPointerException("VooleyCallback can not be null");
		}
		if (url == null) {
			throw new NullPointerException("url can not be null");
		}
		final String mRequestTag = requestTag;

		final RequestResultCallback mRrc = rrc;

		final Map<String, String> mHeaders = headers;

		final String mUrl = url;

		Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject response) {
				mRrc.onRequestSuccess(mRequestTag, response);
			}
		};
		Response.ErrorListener errorListener = new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				mRrc.onRequestFail(mRequestTag, error);
			}
		};
		JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
				Method.POST, mUrl, jsonRequest, listener, errorListener) {
			@Override
			public Map<String, String> getHeaders() throws AuthFailureError {
				if (null != mHeaders) {
					return mHeaders;
				}
				return super.getHeaders();
			}

			@Override
			protected Response<JSONObject> parseNetworkResponse(
					NetworkResponse response) {
				// TODO Auto-generated method stub
				return super.parseNetworkResponse(response);
			}
		};
		jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(50000,
				DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
		jsonObjectRequest.setTag(mRequestTag);
		this.mRequestQueue.add(jsonObjectRequest);
	}

	public void executePostForCookie(String url, JSONObject jsonRequest,
			Map<String, String> headers, RequestResultCallback rrc,
			String requestTag, boolean isCacheData) {
		if (requestTag == null) {
			throw new NullPointerException("requestTag can not be null");
		}
		if (rrc == null) {
			throw new NullPointerException("VooleyCallback can not be null");
		}
		if (url == null) {
			throw new NullPointerException("url can not be null");
		}
		final String mRequestTag = requestTag;

		final RequestResultCallback mRrc = rrc;

		final Map<String, String> mHeaders = headers;

		final String mUrl = url;

		Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject response) {
				mRrc.onRequestSuccess(mRequestTag, response);
			}
		};
		Response.ErrorListener errorListener = new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				mRrc.onRequestFail(mRequestTag, error);
			}
		};
		JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
				Method.POST, mUrl, jsonRequest, listener, errorListener) {

			@Override
			public Map<String, String> getHeaders() throws AuthFailureError {
				if (null != mHeaders) {
					return mHeaders;
				}
				return super.getHeaders();
			}

			@Override
			protected Response<JSONObject> parseNetworkResponse(
					NetworkResponse response) {
				// TODO Auto-generated method stub
				try {
					String jsonString = new String(response.data,
							HttpHeaderParser.parseCharset(response.headers));
					String mHeader = response.headers.toString();
					// 使用正则表达式从reponse的头中提取cookie内容的子串
					Pattern pattern = Pattern.compile("Set-Cookie.*?;");
					Matcher m = pattern.matcher(mHeader);
					JSONObject jsonObject = new JSONObject(jsonString);
					if (m.find()) {
						String cookieFromResponse = m.group();
						// 去掉cookie的头和末尾的分号
						cookieFromResponse = cookieFromResponse.substring(11,
								cookieFromResponse.length() - 1);
						String[] cookieArr = cookieFromResponse.split("=");
						// 将cookie字符串添加到jsonObject中，该jsonObject会被deliverResponse递交，调用请求时则能在onResponse中得到
						jsonObject.put(cookieArr[0], cookieArr[1]);
					}
					return Response.success(jsonObject,
							HttpHeaderParser.parseCacheHeaders(response));
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
					return Response.error(new ParseError(e));
				} catch (JSONException e) {
					e.printStackTrace();
					return Response.error(new ParseError(e));
				}
				// return super.parseNetworkResponse(response);
			}
		};
		jsonObjectRequest.setTag(mRequestTag);
		this.mRequestQueue.add(jsonObjectRequest);
	}

	public void start() {
		this.mRequestQueue.start();
	}

	public void stop() {
		this.mRequestQueue.stop();
	}

	public interface RequestResultCallback {
		public void onRequestSuccess(String paramString, Object paramObject);

		public void onRequestFail(String paramString, Exception paramException);
	}

	/*
	 * AuthFailureError：如果在做一个HTTP的身份验证，可能会发生这个错误。
	 * NetworkError：Socket关闭，服务器宕机，DNS错误都会产生这个错误。
	 * NoConnectionError：和NetworkError类似，这个是客户端没有网络连接。
	 * ParseError：在使用JsonObjectRequest或JsonArrayRequest时，如果接收到的JSON是畸形，会产生异常。
	 * SERVERERROR：服务器的响应的一个错误，最有可能的4xx或5xx HTTP状态代码。
	 * TimeoutError：Socket超时，服务器太忙或网络延迟会产生这个异常
	 * 。默认情况下，Volley的超时时间为2.5秒。如果得到这个错误可以使用RetryPolicy。
	 */

}

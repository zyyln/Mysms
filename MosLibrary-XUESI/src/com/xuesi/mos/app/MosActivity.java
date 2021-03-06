package com.xuesi.mos.app;

import java.util.Map;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import com.xuesi.mos.device.Screen;
import com.xuesi.mos.net.volley.MosVolleyUtil;
import com.xuesi.mos.net.volley.MosVolleyUtil.RequestResultCallback;
import com.xuesi.mos.util.MosLog;
import com.xuesi.mos.util.MosToast;

public abstract class MosActivity extends Activity implements
		View.OnClickListener, MosVolleyUtil.RequestResultCallback {
	private final String LOG_TAG = MosActivity.class.getSimpleName();
	private final MosLog mosLog = MosLog.getInstance(LOG_TAG, "MosHelper");

	private MosVolleyUtil volleyUtil;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		MosApplication.getInstance().addActivity(this);
		volleyUtil = MosVolleyUtil.getInstance( getApplicationContext());
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		volleyUtil.start();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Screen.getInstance().refreshScreenBar(this);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}

	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	public void setContentView(int layoutResID) {
		// TODO Auto-generated method stub
		super.setContentView(layoutResID);
		initContentView();
	}

	protected abstract void initContentView();

	protected void openActivity(Class<?> pClass) {
		openActivity(pClass, null);
	}

	protected void openActivity(Class<?> pClass, Bundle pBundle) {
		Intent intent = new Intent(this, pClass);
		if (pBundle != null) {
			intent.putExtras(pBundle);
		}
		startActivity(intent);
	}

	protected void openActivity(String pAction) {
		openActivity(pAction, null);
	}

	protected void openActivity(String pAction, Bundle pBundle) {
		Intent intent = new Intent(pAction);
		if (pBundle != null) {
			intent.putExtras(pBundle);
		}
		startActivity(intent);
	}

	protected void openActivity(Class<?> pClass, int arg0) {
		openActivity(pClass, null, arg0);
	}

	protected void openActivity(Class<?> pClass, Bundle pBundle, int arg0) {
		Intent intent = new Intent(this, pClass);
		if (pBundle != null) {
			intent.putExtras(pBundle);
		}
		startActivityForResult(intent, arg0);
	}

	public void showShortToast(String pMsg) {
		MosToast.TextToast(this, pMsg, 0);
	}

	public void showShortToast(int pResId) {
		showShortToast(getString(pResId));
	}

	public void showLongToast(String pMsg) {
		MosToast.TextToast(this, pMsg, 1);
	}

	public void showLongToast(int pResId) {
		showLongToast(getString(pResId));
	}

	public void sendPOST(String url, JSONObject jo,
			Map<String, String> headers, RequestResultCallback rrc,
			String requestTag, boolean isCacheData) {
		mosLog.init(url, LOG_TAG);
		mosLog.i(jo.toString());
		this.volleyUtil.executePost(url, jo, headers, rrc, requestTag,
				isCacheData);
	}

	public void sendPostForCookie(String url, JSONObject jo,
			Map<String, String> headers, RequestResultCallback rrc,
			String requestTag, boolean isCacheData) {
		mosLog.init(url, LOG_TAG);
		mosLog.i(jo.toString());
		this.volleyUtil.executePostForCookie(url, jo, headers, rrc, requestTag,
				isCacheData);
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onRequestSuccess(String paramString, Object paramObject) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onRequestFail(String paramString, Exception paramException) {
		// TODO Auto-generated method stub
	}

}

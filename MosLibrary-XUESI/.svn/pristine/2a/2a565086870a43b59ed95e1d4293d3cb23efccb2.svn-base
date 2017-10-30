package com.xuesi.mos.app;

import java.util.Map;

import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xuesi.mos.net.volley.MosVolleyUtil;
import com.xuesi.mos.net.volley.MosVolleyUtil.RequestResultCallback;
import com.xuesi.mos.util.MosLog;
import com.xuesi.mos.util.MosToast;

public abstract class MosFragment extends Fragment implements View.OnClickListener, MosVolleyUtil.RequestResultCallback {
	private final String LOG_TAG = MosFragment.class.getSimpleName();
	private final MosLog mosLog = MosLog.getInstance(LOG_TAG, "MosHelper");

	private MosVolleyUtil volleyUtil;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		volleyUtil = MosVolleyUtil.getInstance(getActivity());
	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		volleyUtil.start();
	}

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	protected abstract void initContentView();

	public void showShortToast(String pMsg) {
		MosToast.TextToast(getActivity(), pMsg, 0);
	}

	public void showShortToast(int pResId) {
		showShortToast(getString(pResId));
	}

	public void showLongToast(String pMsg) {
		MosToast.TextToast(getActivity(), pMsg, 1);
	}

	public void showLongToast(int pResId) {
		showLongToast(getString(pResId));
	}

	public void sendPOST(String url, JSONObject jo, Map<String, String> headers, RequestResultCallback rrc,
			String requestTag, boolean isCacheData) {
		mosLog.init(url, LOG_TAG);
		mosLog.i(jo.toString());
		this.volleyUtil.executePost(url, jo, headers, rrc, requestTag, isCacheData);
	}

	public void sendPostForCookie(String url, JSONObject jo, Map<String, String> headers, RequestResultCallback rrc,
			String requestTag, boolean isCacheData) {
		mosLog.init(url, LOG_TAG);
		mosLog.i(jo.toString());
		this.volleyUtil.executePostForCookie(url, jo, headers, rrc, requestTag, isCacheData);
	}

	@Override
	public void onClick(View v) {
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

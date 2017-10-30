package com.xuesi.sms.app.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xuesi.mos.util.MosLog;

public class SheetFragment extends SmsFragment {
	/** LOG */
	private final String LOG_TAG = SheetFragment.class.getSimpleName();
	private final MosLog mLog = MosLog.getInstance(LOG_TAG, "XUESI-SMS");
	/** progress的次数 */
	private int mProgressNumber = 0;

	protected boolean isVisible;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	@Override
	protected void initContentView() {
		// TODO Auto-generated method stub
	}

	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		// TODO Fragment数据的缓加载
		super.setUserVisibleHint(isVisibleToUser);
		if (getUserVisibleHint()) {
			isVisible = true;

		} else {
			isVisible = false;
		}
	}

}

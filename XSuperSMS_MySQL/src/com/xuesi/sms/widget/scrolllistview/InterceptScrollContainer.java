package com.xuesi.sms.widget.scrolllistview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.LinearLayout;

import com.xuesi.mos.util.MosLog;

/*
 * 
 * 一个视图容器控件
 * 阻止 拦截 ontouch事件传递给其子控件
 * */
public class InterceptScrollContainer extends LinearLayout {
	private final String TAG_LOG = InterceptScrollContainer.class.getName();
	private final MosLog mLog = MosLog.getInstance(TAG_LOG, "XUESI-MES");

	public InterceptScrollContainer(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public InterceptScrollContainer(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	//
	// @Override
	// public boolean dispatchTouchEvent(MotionEvent ev) {
	// // TODO Auto-generated method stub
	// //return super.dispatchTouchEvent(ev);
	// Log.i("pdwy","ScrollContainer dispatchTouchEvent");
	// return true;
	// }

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
		// return super.onInterceptTouchEvent(ev);
		if (ev.getAction() == MotionEvent.ACTION_DOWN) {
			mLog.w("ScrollContainer onInterceptTouchEvent 按下了..");
		}
		if (ev.getAction() == MotionEvent.ACTION_MOVE) {
			mLog.w("ScrollContainer onInterceptTouchEvent 移动了..");

		}
		if (ev.getAction() == MotionEvent.ACTION_UP) {
			mLog.w("ScrollContainer onInterceptTouchEvent 抬起了..");
		}
		mLog.w("ScrollContainer onInterceptTouchEvent");
		return true;

		// return super.onInterceptTouchEvent(ev);
	}

	// @Override
	// public boolean onTouchEvent(MotionEvent event) {
	// // TODO Auto-generated method stub
	// Log.i("pdwy","ScrollContainer onTouchEvent");
	// return true;
	// }
}

package com.xuesi.sms.widget.scrolllistview;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.HorizontalScrollView;

import com.xuesi.mos.util.MosLog;

/*
 * 自定义的 滚动控件
 * 重载了 onScrollChanged（滚动条变化）,监听每次的变化通知给 观察(此变化的)观察者
 * 可使用 AddOnScrollChangedListener 来订阅本控件的 滚动条变化
 * */
public class MyHScrollView extends HorizontalScrollView {
	private final String TAG_LOG = MyHScrollView.class.getName();
	private final MosLog mLog = MosLog.getInstance(TAG_LOG, "XUESI-MES");
	ScrollViewObserver mScrollViewObserver = new ScrollViewObserver();

	public MyHScrollView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	public MyHScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public MyHScrollView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		int move_i = 0;

		if (ev.getAction() == MotionEvent.ACTION_DOWN) {
			mLog.w("MyHScrollView onTouchEvent  ..按下了.." + move_i);
		}
		if (ev.getAction() == MotionEvent.ACTION_MOVE) {
			move_i = 1;
			mLog.w("MyHScrollView onTouchEvent  ..移动了.." + move_i);
			float x = ev.getX();
			float y = ev.getY();
			ev.setLocation(x, y + (x % 3));
		}
		if (ev.getAction() == MotionEvent.ACTION_UP) {
			mLog.w("MyHScrollView onTouchEvent  ..抬起了.." + move_i);
			/*
			 * if (move_i==1) { move_i = 0 ; Log.w("进入if了,处理完了.."); return true;
			 * }
			 */
		}
		mLog.w("MyHScrollView onTouchEvent..");
		return super.onTouchEvent(ev);
	}

	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
		/*
		 * 当滚动条移动后，引发 滚动事件。通知给观察者，观察者会传达给其他的。
		 */
		if (mScrollViewObserver != null /* && (l != oldl || t != oldt) */) {
			mScrollViewObserver.NotifyOnScrollChanged(l, t, oldl, oldt);
		}
		super.onScrollChanged(l, t, oldl, oldt);
	}

	/*
	 * 订阅 本控件 的 滚动条变化事件
	 */
	public void AddOnScrollChangedListener(OnScrollChangedListener listener) {
		mScrollViewObserver.AddOnScrollChangedListener(listener);
	}

	/*
	 * 取消 订阅 本控件 的 滚动条变化事件
	 */
	public void RemoveOnScrollChangedListener(OnScrollChangedListener listener) {
		mScrollViewObserver.RemoveOnScrollChangedListener(listener);
	}

	/*
	 * 当发生了滚动事件时
	 */
	public static interface OnScrollChangedListener {
		public void onScrollChanged(int l, int t, int oldl, int oldt);
	}

	/*
	 * 观察者
	 */
	public static class ScrollViewObserver {
		List<OnScrollChangedListener> mList;

		public ScrollViewObserver() {
			super();
			mList = new ArrayList<OnScrollChangedListener>();
		}

		public void AddOnScrollChangedListener(OnScrollChangedListener listener) {
			mList.add(listener);
		}

		public void RemoveOnScrollChangedListener(
				OnScrollChangedListener listener) {
			mList.remove(listener);
		}

		public void NotifyOnScrollChanged(int l, int t, int oldl, int oldt) {
			if (mList == null || mList.size() == 0) {
				return;
			}
			for (int i = 0; i < mList.size(); i++) {
				if (mList.get(i) != null) {
					mList.get(i).onScrollChanged(l, t, oldl, oldt);
				}
			}
		}
	}
}

package com.xuesi.mos.util;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

public class MosUtil {
	private final String LOG_TAG = MosUtil.class.getName();
	private final MosLog mLog = MosLog.getInstance(LOG_TAG, "MosHelper");

	/**
	 * 
	 * setListViewHeightBasedOnChildren(动态设置ListView的高度)
	 * <p>
	 * (在调用setAdapter()后)
	 * 
	 * @param listView
	 *            void
	 * @exception
	 * @since 1.0.0
	 */
	public static void setListViewHeightBasedOnChildren(ListView listView) {
		// 获取listview对应的adapter
		ListAdapter listAdapter = listView.getAdapter();
		if (null == listAdapter) {
			return;
		}

		int totalHeight = 0;

		for (int i = 0; i < listAdapter.getCount(); i++) {
			View itemView = listAdapter.getView(i, null, listView);
			// 计算子项的View的高度
			itemView.measure(0, 0);
			// 统计所有子项的总高度
			totalHeight += itemView.getMeasuredHeight();
		}

		ViewGroup.LayoutParams params = listView.getLayoutParams();
		// listView.getDividerHeight()获取子项间隔条高度
		// params.height最后得到整个listView完整的显示需要的高度
		params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		listView.setLayoutParams(params);
	}

}

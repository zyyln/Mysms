/**
 * <p>Title: BarcodeInfoAdapter.java</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2007</p>
 * <p>Company: XSuper</p>
 * @author XS-PC011
 * @date 2015-9-9
 *
 */
package com.xuesi.sms.widget.adapter;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xuesi.mos.util.MosLog;
import com.xuesi.sms.R;
import com.xuesi.sms.bean.Common;

/**
 * <p>
 * Title: 条码列表适配器
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Company: XSuper
 * </p>
 * 
 * @author XS-PC011
 * @date 2015-9-9
 */
public class BarcodeAdapter extends BaseAdapter {
	private final String TAG_LOG = BarcodeAdapter.class.getSimpleName();
	private final MosLog mLog = MosLog.getInstance(TAG_LOG, "XUESI-SMS");
	private LayoutInflater mInflater;
	private List<Common> list;
	/** 选中项集合 */
	private List<Common> selectedList = new ArrayList<Common>();

	class ViewHolder {
		TextView barcodeTv;
	}

	public BarcodeAdapter(Context context, List<Common> codeList) {
		this.mInflater = LayoutInflater.from(context);
		this.list = codeList;
	}

	@Override
	public int getCount() {
		return list != null ? list.size() : 0;
	}

	@Override
	public Common getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@SuppressLint({ "NewApi", "ResourceAsColor" })
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		convertView = null;
		ViewHolder holder = new ViewHolder();
		// if (convertView == null) {
		// 当第一次加载ListView控件时 convertView为空
		// 导入布局并赋值给convertview

		convertView = mInflater.inflate(R.layout.item_tv_white, null);
		holder.barcodeTv = (TextView) convertView
				.findViewById(R.id.item_tv_content);
		// 为view设置标签
		convertView.setTag(holder);
		// } else {
		// // 取出holder,对象存储在这个视图的标记
		// holder = (ViewHolder) convertView.getTag();
		// }
		holder.barcodeTv.setText(list.get(position).getTxt());
		if (list.get(position).isSelected()) {
			convertView
					.setBackgroundResource(R.drawable.sheetin_info_bj_violet);
		} else {
			convertView.setBackgroundResource(R.drawable.sheetin_info_bj_gray);
		}

		return convertView;
	}

	public List<Common> getSelectedList() {
		selectedList.clear();
		for (Common cm : list) {
			if (cm.isSelected()) {
				selectedList.add(cm);
			}
		}
		return selectedList;
	}

	public void setSelectedList(List<Common> selectedList) {
		this.selectedList = selectedList;
	}

}

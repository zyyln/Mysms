package com.xuesi.sms.widget.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xuesi.sms.R;
import com.xuesi.sms.bean.GrantOrder;

/**
 * 发料单（出库单）适配器 <br>
 * 数据源：GrantOrder.ListClass单号实体类 <br>
 * 单选
 * 
 * @author Administrator
 * 
 */
public class GrantOrderAdapter extends BaseAdapter {
	private LayoutInflater mInflater;
	private List<GrantOrder.ListClass> list;
	/** 单选项选中项角标 */
	private int selecteP = -1;

	public List<GrantOrder.ListClass> getList() {
		return list;
	}

	public void setList(List<GrantOrder.ListClass> list) {
		this.list = list;
	}

	public int getSelecteP() {
		return selecteP;
	}

	public void setSelecteP(int selecteP) {
		this.selecteP = selecteP;
	}

	/** 获取选中项 */
	public GrantOrder.ListClass getSeleItem() {
		return list.get(selecteP);
	}

	public GrantOrderAdapter(Context context, List<GrantOrder.ListClass> list) {
		this.list = list;
		this.mInflater = LayoutInflater.from(context);
	}

	class ViewHolder {
		TextView orderId;
	}

	@Override
	public int getCount() {
		return list != null ? list.size() : 0;
	}

	@Override
	public GrantOrder.ListClass getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (null == convertView) {
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.item_lv_popwin, null);
			holder.orderId = (TextView) convertView.findViewById(R.id.popwin_tv_content);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		if (null != list && list.size() > 0) {
			// 数据库字段数据类型为32位varchar，因此仅在显示去除空格
			holder.orderId.setText(list.get(position).getID().trim());
		}
		return convertView;
	}

}

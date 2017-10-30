package com.xuesi.sms.widget.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xuesi.sms.R;
import com.xuesi.sms.bean.StoreHouse;

/**
 * 库房适配器 <br>
 * 单选 <br>
 * 
 * @author XS-PC014
 * 
 */
public class StoreAdapter extends BaseAdapter {
	private LayoutInflater mInflater;
	private List<StoreHouse.ListClass> list;
	/** 选中项角标 */
	private int selecteP = -1;

	private class Holder {
		TextView houseName;
	}

	public StoreAdapter(Context context, List<StoreHouse.ListClass> list) {
		this.mInflater = LayoutInflater.from(context);
		this.list = list;
	}

	public int getSelecteP() {
		return selecteP;
	}

	public void setSelecteP(int selecteP) {
		this.selecteP = selecteP;
	}

	/** 获取选中项 */
	public StoreHouse.ListClass getSeleItem(int position) {
		return list.get(selecteP);
	}

	@Override
	public int getCount() {
		return list != null ? list.size() : 0;
	}

	@Override
	public StoreHouse.ListClass getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Holder holder = null;
		if (null == convertView) {
			holder = new Holder();
			convertView = mInflater.inflate(R.layout.item_lv_popwin, null);
			holder.houseName = (TextView) convertView.findViewById(R.id.popwin_tv_content);
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}
		StoreHouse.ListClass house = list.get(position);
		holder.houseName.setText(house.getNAME());
		return convertView;
	}

}
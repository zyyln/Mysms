package com.xuesi.sms.widget.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xuesi.sms.R;
import com.xuesi.sms.bean.Stack;

/**
 * 信息查询画面-查询钢板<br>
 * 
 * @author XS-PC014
 * 
 */
public class SteelResultAdapter extends BaseAdapter {
	private LayoutInflater mInflater;
	private List<Stack.ListClass> list;
	private int textColor;

	private class Holder {
		TextView indexText;
		TextView boundText;
		TextView stackText;
		TextView numText;
	}

	public SteelResultAdapter(Context context, List<Stack.ListClass> list) {
		this.mInflater = LayoutInflater.from(context);
		this.list = list;
		this.textColor = context.getResources().getColor(R.color.black);
	}

	public List<Stack.ListClass> getList() {
		return list;
	}

	@Override
	public int getCount() {
		return list != null ? list.size() : 0;
	}

	@Override
	public Stack.ListClass getItem(int arg0) {
		return list != null ? list.get(arg0) : null;
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Holder holder = null;
		if (null == convertView) {
			holder = new Holder();
			convertView = mInflater.inflate(R.layout.item_lv_info_query_steel, null);
			holder.indexText = (TextView) convertView.findViewById(R.id.info_query_steel_index);
			holder.boundText = (TextView) convertView.findViewById(R.id.info_query_steel_bound);
			holder.numText = (TextView) convertView.findViewById(R.id.info_query_steel_num);
			holder.stackText = (TextView) convertView.findViewById(R.id.info_query_steel_stack);

			// 更换item背景色
			convertView.setBackgroundResource(R.drawable.info_query_item_bg);
			// 更换字体颜色
			holder.indexText.setTextColor(textColor);
			holder.boundText.setTextColor(textColor);
			holder.numText.setTextColor(textColor);
			holder.stackText.setTextColor(textColor);

			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}

		Stack.ListClass stackObj = list.get(position);

		holder.indexText.setText(String.valueOf(position + 1));
		holder.boundText.setText(stackObj.getNAME());// 库房
		holder.stackText.setText(stackObj.getSTACKNAME());// 垛位
		holder.numText.setText(stackObj.getQTYNUM());// 合情钢板数量

		return convertView;
	}

	public View getItemView() {
		View convertView = mInflater.inflate(R.layout.item_lv_info_query_steel, null);
		return convertView;
	}

}

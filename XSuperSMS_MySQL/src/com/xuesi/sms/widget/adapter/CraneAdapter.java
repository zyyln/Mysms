package com.xuesi.sms.widget.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xuesi.sms.R;
import com.xuesi.sms.bean.CraneByType;

/**
 * 行车适配器<br>
 * 数据源：CraneByType.ListClass<br>
 * 
 * @author Administrator
 * 
 */
public class CraneAdapter extends BaseAdapter {
	private LayoutInflater mInflater;
	private List<CraneByType.ListClass> list;

	public List<CraneByType.ListClass> getList() {
		return list;
	}

	public void setList(List<CraneByType.ListClass> list) {
		this.list = list;
	}

	class ViewHolder {
		TextView craneTv;
	}

	public CraneAdapter(Context context, List<CraneByType.ListClass> craneItemList) {
		this.mInflater = LayoutInflater.from(context);
		this.list = craneItemList;
	}

	@Override
	public int getCount() {
		return list != null ? list.size() : 0;
	}

	@Override
	public CraneByType.ListClass getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.item_gv_device, null);
			viewHolder.craneTv = (TextView) convertView.findViewById(R.id.item_tv_device);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.craneTv.setText(list.get(position).getDNAME());

		if (list.get(position).isSelected()) {// 选中
			viewHolder.craneTv.setBackgroundResource(R.drawable.work_panel_button_selected);
		} else {// 非选中
			viewHolder.craneTv.setBackgroundResource(R.drawable.work_panel_button_normal);
		}
		return convertView;
	}

}

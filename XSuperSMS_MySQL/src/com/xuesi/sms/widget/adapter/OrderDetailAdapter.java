package com.xuesi.sms.widget.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.xuesi.sms.R;
import com.xuesi.sms.SmsConfig;
import com.xuesi.sms.bean.OrderDetail;
import com.xuesi.sms.util.SmsUtil;

/**
 * 出库\配版-单号列表适配器<br>
 * 操作数据：<br>
 * 
 * @author Administrator
 * 
 */
public class OrderDetailAdapter extends BaseAdapter {
	private LayoutInflater mInflater;
	private List<OrderDetail.ListClass> list;
	/** 选中项角标 */
	private int selecteP = -1;

	class ViewHolder {
		LinearLayout itemLinear;
		TextView selectNum;
		EditText sheetTxt;
	}

	public OrderDetailAdapter(Context context, List<OrderDetail.ListClass> list) {
		this.list = list;
		this.mInflater = LayoutInflater.from(context);
	}

	public List<OrderDetail.ListClass> getList() {
		return list;
	}

	public void setList(List<OrderDetail.ListClass> list) {
		this.list = list;
	}

	@Override
	public int getCount() {
		return list != null ? list.size() : 0;
	}

	@Override
	public OrderDetail.ListClass getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;

		if (convertView == null) {
			// 当第一次加载ListView控件时 convertView为空
			holder = new ViewHolder();
			// 导入布局并赋值给convertview
			convertView = mInflater.inflate(R.layout.item_lv_tvwhite, null);
			holder.itemLinear = (LinearLayout) convertView
					.findViewById(R.id.item_linear);
			holder.sheetTxt = (EditText) convertView
					.findViewById(R.id.item_tv_content);
			// 为view设置标签
			convertView.setTag(holder);
		} else {
			// 取出holder,对象存储在这个视图的标记
			holder = (ViewHolder) convertView.getTag();
		}

		OrderDetail.ListClass grantDetail = list.get(position);
		holder.sheetTxt.setText(grantDetail.getSHEETTYPE() + "-"
				+ grantDetail.getMATERIAL()
				+ "*" + grantDetail.getTHICKNESS() + "*"
				+ grantDetail.getLENGTH() + "*" + grantDetail.getWIDTH());
		if (grantDetail.isSelected()) {
			holder.itemLinear
					.setBackgroundResource(R.drawable.sheetin_info_bj_violet);
		} else {
			holder.itemLinear
					.setBackgroundResource(R.drawable.sheetin_info_bj_gray);
		}
		return convertView;
	}

	public void selectMehtod() {

	}

	public int getSelecteP() {
		return selecteP;
	}

	public void setSelecteP(int selecteP) {
		this.selecteP = selecteP;
	}

	/** 获取选中项 */
	public OrderDetail.ListClass getSeleItem(int position) {
		return list.get(selecteP);
	}

	public void updateSelecte() {
		// 禁止多选
		for (OrderDetail.ListClass orderDetail : list) {
			orderDetail.setSelected(false);
		}
	}

}

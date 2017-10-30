package com.xuesi.sms.widget.adapter;

import java.util.List;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xuesi.sms.R;
import com.xuesi.sms.bean.Stack;

/**
 * 库存盘点的垛位适配器
 * 
 * @author XS-PC014
 * 
 */
public class StockCheckAdapter extends BaseAdapter {
	/** LOG */
	// private static final String LOG_TAG =
	// StockCheckAdapter.class.getSimpleName();
	// private static final LogUtil mLog = LogUtil.getInstance(LOG_TAG,
	// "XUESI-SMS");

	private List<Stack.ListClass> list;
	private LayoutInflater mInflater;
	private String storeType;// 库房类型
	private Resources res;

	public List<Stack.ListClass> getList() {
		return list;
	}

	private class Holder {
		LinearLayout rLayout;
		TextView stackName;
		TextView heighthTv;
		TextView stackNumTv;
	}

	public StockCheckAdapter(Context context, List<Stack.ListClass> stackList,
			String storeType) {
		this.res = context.getResources();
		this.list = stackList;
		this.mInflater = LayoutInflater.from(context);
		this.storeType = storeType;
	}

	@Override
	public int getCount() {
		return list != null ? list.size() : 0;
	}

	@Override
	public Stack.ListClass getItem(int position) {
		return list != null ? list.get(position) : null;
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
			convertView = mInflater.inflate(R.layout.item_gv_stockcheck, null);
			holder.rLayout = (LinearLayout) convertView
					.findViewById(R.id.stock_check_item_layout);
			holder.stackName = (TextView) convertView
					.findViewById(R.id.stock_check_item_cargo);
			holder.heighthTv = (TextView) convertView
					.findViewById(R.id.stock_check_item_height);
			holder.stackNumTv = (TextView) convertView
					.findViewById(R.id.stock_check_item_num);
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}

		Stack.ListClass stack = list.get(position);
		int drawable = -1;
		if (storeType.equals("RCK")) {
			// 余料库垛位图片变化
			switch (stack.getCheckFlag()) {
			// 盘点无误
			case 1: {
				drawable = R.drawable.stock_oddments_finish;
			}
				break;
			// 盘点有误
			case 2: {
				drawable = R.drawable.stock_oddments_error;
			}
				break;
			// 需要倒垛
			case 3: {
				drawable = R.drawable.stock_oddments_needreindex;
			}
				break;
			// 需要盘点
			default: {
				drawable = R.drawable.stock_oddments_wating;
			}
				break;
			}
		} else {// 一级库、二级库垛位背景图片变化
			switch (stack.getCheckFlag()) {
			// 盘点无误
			case 1: {
				drawable = R.drawable.img_stock_finish;
			}
				break;
			// 盘点有误
			case 2: {
				drawable = R.drawable.img_stock_error;
			}
				break;
			// 需要倒垛
			case 3: {
				drawable = R.drawable.img_stock_need_reindex;
			}
				break;
			// 需要盘点
			default: {
				drawable = R.drawable.img_stock_wating;
			}
				break;
			}
		}
		holder.rLayout.setBackgroundResource(drawable);
		holder.stackName.setText(stack.getSTACKNAME());
		holder.heighthTv.setText(stack.getSumThickness()
				+ res.getString(R.string.millimeter));
		holder.stackNumTv.setText((int) stack.getSumAmount()
				+ res.getString(R.string.steel_unit));
		return convertView;
	}

	@Override
	public void notifyDataSetChanged() {
		// TODO Auto-generated method stub
		super.notifyDataSetChanged();
	}

}

package com.xuesi.sms.widget.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xuesi.mos.util.MosLog;
import com.xuesi.sms.R;
import com.xuesi.sms.app.activity.SheetBaseActivity;
import com.xuesi.sms.bean.Stack;
import com.xuesi.sms.bean.Stack.ListClass;
import com.xuesi.sms.util.BitmapUtil;

/**
 * 垛位适配器(入库|倒垛|出库|总览) <br>
 * 备注：筛选垛位时，要求符合条件高亮靠前显示 <br>
 * 
 * @author lzq
 * 
 */
public class StackAdapter extends BaseAdapter {
	/** LOG */
	private final String LOG_TAG = SheetBaseActivity.class.getSimpleName();
	private final MosLog mLog = MosLog.getInstance(LOG_TAG, "XUESI-SMS");
	private LayoutInflater mInflater;
	private Context context;
	private List<Stack.ListClass> list;
	private String houseType;
	// 0: "/api/pad/GetSheetNumInSTACKRust"; "/api/pad/GetSheetNumInSTACK";
	// 1: "/api/StoreStackMng/getStackInfo";"/api/pad/getStackInfoName";
	private int flag;

	public List<Stack.ListClass> getList() {
		return list;
	}

	public void setList(List<Stack.ListClass> list) {
		this.list = list;
	}

	/**
	 * convertView-ViewHolder缓存技术
	 * 
	 * @author 垛位内部类
	 * 
	 */
	class ViewHolder {
		RelativeLayout stackLayout;
		ImageView recom;
		ImageView selct;
		ImageView changeHeight;
		TextView stackName;
		TextView stackHeight;
		TextView stackNumber;
	}

	public StackAdapter(Context context, List<Stack.ListClass> stackAllList,
			String houseType, int flag) {
		this.context = context;
		this.list = stackAllList;
		this.houseType = houseType;
		this.flag = flag;
		mInflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return list != null ? list.size() : 0;
	}

	@Override
	public Stack.ListClass getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;

		/** 缓存机制导致数据混乱 */
		// if (convertView == null) {
		// holder = new ViewHolder();
		// convertView = mInflater.inflate(R.layout.item_gv_stack, null);
		// holder.stackLayout = (RelativeLayout)
		// convertView.findViewById(R.id.stackItem_relative_stack);
		// holder.changeHeight = (ImageView)
		// convertView.findViewById(R.id.stackItem_iv_change);
		// holder.recom = (ImageView)
		// convertView.findViewById(R.id.stackItem_iv_recom);
		// holder.selct = (ImageView)
		// convertView.findViewById(R.id.stackItem_iv_selected);
		// holder.stackName = (TextView)
		// convertView.findViewById(R.id.stackItem_tv_name);
		// holder.stackHeight = (TextView)
		// convertView.findViewById(R.id.stackItem_tv_height);
		// holder.stackNumber = (TextView)
		// convertView.findViewById(R.id.stackItem_tv_count);
		// convertView.setTag(holder);
		// } else {
		// holder = (ViewHolder) convertView.getTag();
		// }

		convertView = null;
		holder = new ViewHolder();
		convertView = mInflater.inflate(R.layout.item_gv_stack, null);
		holder.stackLayout = (RelativeLayout) convertView
				.findViewById(R.id.stackItem_relative_stack);
		holder.changeHeight = (ImageView) convertView
				.findViewById(R.id.stackItem_iv_change);
		holder.recom = (ImageView) convertView
				.findViewById(R.id.stackItem_iv_recom);
		holder.selct = (ImageView) convertView
				.findViewById(R.id.stackItem_iv_selected);
		holder.stackName = (TextView) convertView
				.findViewById(R.id.stackItem_tv_name);
		holder.stackHeight = (TextView) convertView
				.findViewById(R.id.stackItem_tv_height);
		holder.stackNumber = (TextView) convertView
				.findViewById(R.id.stackItem_tv_count);

		Stack.ListClass stack = (ListClass) getItem(position);

		float maxH = 0;
		float curH = 0;

		if (flag == 0) {
			String maxHeight = stack.getMAXHEIGHT();
			float curHeight = stack.getCURRENTHEIGHT();
			maxH = Float.parseFloat(maxHeight.trim());
			curH = curHeight;
		} else if (flag == 1) {
			String maxHeight = stack.getMAXHEIGHT();
			float curHeight = stack.getSumThickness();
			maxH = Float.parseFloat(maxHeight);
			curH = curHeight;
		}

		/** 处理垛位高度图片变化 */
		if ("RCK".equals(houseType)) {
			// 余料库垛位背景变化
			if (stack.isSelected()) {
				holder.stackLayout
						.setBackgroundResource(R.drawable.oddment_selcted_bg);
			} else {
				holder.stackLayout.setBackgroundResource(R.drawable.oddment_bg2);
			}
			if (maxH > 0 && curH > 0) {
				// 垛位高度缩放比例
				// int bgImgID = stackAllList.get(position).getRecommend() ?
				// R.drawable.oddment_recommend
				// : R.drawable.oddment_height;
				int bgImgID = R.drawable.oddment_height;
				BitmapUtil.getInstance().setZoomPicBg(context,
						holder.changeHeight, bgImgID, curH / maxH);
			} else {
				holder.changeHeight.setBackgroundResource(0);
			}
		} else {// 一级库，二级库垛位图片变化
			// 一级库，二级库垛位背景变化
			if (stack.isSelected()) {
				holder.stackLayout
						.setBackgroundResource(R.drawable.img_stock_purple);
			} else {
				holder.stackLayout
						.setBackgroundResource(R.drawable.img_stock_normal2);
			}
			if (maxH > 0 && curH > 0) {
				// 垛位高度缩放比例
				int bgImgID = R.drawable.stack_height;
				BitmapUtil.getInstance().setZoomPicBg(context,
						holder.changeHeight, bgImgID, curH / maxH);
			} else {
				holder.changeHeight.setBackgroundResource(0);
			}
		}
		// 设置推荐垛位
		if (stack.isRecommend()) {
			holder.recom.setVisibility(View.VISIBLE);
		} else {
			holder.recom.setVisibility(View.GONE);
		}
		// 设置目标(进库)垛位
		if (stack.isToSelect()) {
			holder.selct.setVisibility(View.VISIBLE);
		} else {
			holder.selct.setVisibility(View.GONE);
		}
		holder.stackName.setText(stack.getSTACKNAME());
		if (flag == 0) {
			holder.stackHeight.setText(stack.getCURRENTHEIGHT()
					+ context.getString(R.string.millimeter));
			holder.stackNumber.setText((int) stack.getSumAmount()
					+ context.getString(R.string.steel_unit));
		} else if (flag == 1) {
			holder.stackHeight.setText(stack.getSumThickness()
					+ context.getString(R.string.millimeter));
			holder.stackNumber.setText((int) stack.getSumAmount()
					+ context.getString(R.string.steel_unit));
		}

		return convertView;
	}

	/**
	 * 更新库房类型
	 * 
	 * @param houseType
	 */
	public void updateHouseType(String houseType) {
		this.houseType = houseType;
	}

	public void initAll() {
		for (Stack.ListClass stack : list) {
			stack.setToSelect(false);
			stack.setRecommend(false);
			stack.setSelected(false);
		}
		notifyDataSetChanged();
	}

	public void updateToSelect() {
		for (Stack.ListClass stack : list) {
			stack.setToSelect(false);
		}
		notifyDataSetChanged();
	}

}

package com.xuesi.sms.widget.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xuesi.sms.R;
import com.xuesi.sms.bean.Sheet;

/**
 * 钢板适配器 (入库 || 倒垛)<br>
 * 数据源:钢板(Sheet.ListClass) <br>
 * 可多选
 * 
 * @author lzq
 * 
 */
public class SheetAdapter extends BaseAdapter {
	// private final String TAG_LOG = SheetAdapter.class.getSimpleName();
	// private final MosLog mLog = MosLog.getInstance(TAG_LOG, "XUESI-SMS");
	private LayoutInflater mInflater;
	private Context mContext;
	private List<Sheet.ListClass> list;
	private Sheet.ListClass sheet = null;
	private ViewHolder holder = null;
	/** 选中项的集合 */
	private List<Sheet.ListClass> selectedList = new ArrayList<Sheet.ListClass>();

	public List<Sheet.ListClass> getList() {
		return list;
	}

	public void setList(List<Sheet.ListClass> list) {
		this.list = list;
	}

	class ViewHolder {
		LinearLayout itemLinear;
		TextView selectNum;
		EditText sheetTxt;
	}

	public SheetAdapter(Context context, List<Sheet.ListClass> list) {
		// TODO Auto-generated constructor stub
		this.mInflater = LayoutInflater.from(context);
		this.mContext = context;
		this.list = list;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list != null ? list.size() : 0;
	}

	@Override
	public Sheet.ListClass getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub

		if (convertView == null) {
			// 当第一次加载ListView控件时 convertView为空
			holder = new ViewHolder();
			// 导入布局并赋值给convertview
			convertView = mInflater.inflate(R.layout.item_lv_tvwhite, null);
			holder.itemLinear = (LinearLayout) convertView
					.findViewById(R.id.item_linear);
			holder.sheetTxt = (EditText) convertView
					.findViewById(R.id.item_tv_content);
			holder.selectNum = (TextView) convertView
					.findViewById(R.id.item_tv_selectNum);
			convertView.setTag(holder);
		} else {
			// 取出holder,对象存储在这个视图的标记
			holder = (ViewHolder) convertView.getTag();
		}
		sheet = (Sheet.ListClass) getItem(position);
		if ("1".equals(sheet.getISREM())) {
			holder.sheetTxt.setText("余料-" + sheet.getSHEETNAME() + ":"
					+ sheet.getProjectId() + "*" + sheet.getMaterial() + "*"
					+ sheet.getThickness() + "*" + sheet.getWidth() + "*"
					+ sheet.getLength());
		} else {
			holder.sheetTxt.setText("整板-" + sheet.getSHEETNAME() + ":"
					+ sheet.getProjectId() + "*" + sheet.getMaterial() + "*"
					+ sheet.getThickness() + "*" + sheet.getWidth() + "*"
					+ sheet.getLength());
		}
		if (sheet.isSelected()) {
			holder.itemLinear
					.setBackgroundResource(R.drawable.sheetin_info_bj_violet);
		} else {
			holder.itemLinear
					.setBackgroundResource(R.drawable.sheetin_info_bj_gray);
		}
		holder.selectNum.setText(sheet.getSelectNum());
		return convertView;
	}

	public List<Sheet.ListClass> getSelectedList() {
		selectedList.clear();
		for (Sheet.ListClass sClass : list) {
			if (sClass.isSelected()) {
				selectedList.add(sClass);
			}
		}
		return selectedList;
	}

}

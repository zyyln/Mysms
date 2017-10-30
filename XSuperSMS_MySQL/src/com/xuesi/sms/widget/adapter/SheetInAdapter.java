package com.xuesi.sms.widget.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xuesi.sms.R;
import com.xuesi.sms.bean.SheetByCode;
import com.xuesi.sms.bean.SheetByCode.ListClass;

/**
 * 钢板适配器 (入库)<br>
 * 数据源:钢板 <br>
 * 可多选 --已与SheetAdapter合并-待删除
 * 
 * @author lzq
 * 
 */
public class SheetInAdapter extends BaseAdapter {
	// private final String TAG_LOG = SheetAdapter.class.getSimpleName();
	// private final MosLog mLog = MosLog.getInstance(TAG_LOG, "XUESI-SMS");
	private LayoutInflater mInflater;
	// private Context mContext;
	private List<SheetByCode.ListClass> mList;
	private String mFlag;// "sheet"钢板，"remnant"余料

	public List<SheetByCode.ListClass> getList() {
		return mList;
	}

	public void setList(List<SheetByCode.ListClass> list) {
		this.mList = list;
	}

	class ViewHolder {
		TextView sheetTV;
	}

	public SheetInAdapter(Context context, List<SheetByCode.ListClass> list, String flag) {
		// TODO Auto-generated constructor stub
		// this.mContext = context;
		this.mList = list;
		this.mInflater = LayoutInflater.from(context);
		this.mFlag = flag;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mList != null ? mList.size() : 0;
	}

	@Override
	public SheetByCode.ListClass getItem(int position) {
		// TODO Auto-generated method stub
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder = null;
		if (convertView == null) {
			// 当第一次加载ListView控件时 convertView为空
			holder = new ViewHolder();
			// 导入布局并赋值给convertview
			convertView = mInflater.inflate(R.layout.item_lv_tvwhite, null);
			holder.sheetTV = (TextView) convertView.findViewById(R.id.item_tv_content);
			// 为view设置标签
			convertView.setTag(holder);
		} else {
			// 取出holder,对象存储在这个视图的标记
			holder = (ViewHolder) convertView.getTag();
		}
		SheetByCode.ListClass sheet = (ListClass) getItem(position);

		if (mFlag.equals("sheet")) {
			holder.sheetTV.setText(sheet.getMATERIAL() + "*" + sheet.getTHICKNESS() + "*" + sheet.getWIDTH() + "*"
					+ sheet.getLENGTH());
		} else if (mFlag.equals("remnant")) {
			holder.sheetTV.setText(sheet.getMATERIAL() + "*" + sheet.getTHICKNESS() + "*" + sheet.getPLATEHEIGHT()
					+ "*" + sheet.getPLATELENGTH());
		}

		// 根据钢板的选中属性设置背景
		if (sheet.isSelcted()) {
			holder.sheetTV.setBackgroundResource(R.drawable.sheetin_info_bj_violet);
		} else {
			holder.sheetTV.setBackgroundResource(R.drawable.sheetin_info_bj_gray);
		}
		return convertView;
	}

}

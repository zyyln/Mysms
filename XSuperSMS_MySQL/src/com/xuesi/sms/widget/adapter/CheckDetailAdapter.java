package com.xuesi.sms.widget.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xuesi.sms.R;
import com.xuesi.sms.bean.Sheet;
import com.xuesi.sms.util.SmsUtil;

/**
 * <p>
 * Title: 盘点详情-钢板适配器
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
public class CheckDetailAdapter extends BaseAdapter {
	// private final String LOG_TAG = CheckDetailAdapter.class.getSimpleName();
	// private final MosLog mLog = MosLog.getInstance(LOG_TAG, "XUESI-SMS");
	private LayoutInflater mInflater;
	private Context mContext;
	private List<Sheet.ListClass> list;

	public List<Sheet.ListClass> getList() {
		return list;
	}

	public void removeItem(Sheet.ListClass item) {
		list.remove(item);
	}

	public void removeItem(int position) {
		removeItem(getItem(position));
	}

	public void insertItem(int position, Sheet.ListClass item) {
		list.add(position, item);
	}

	private class Holder {
		LinearLayout layout;
		TextView sheetTv;
		ImageView dropBtn;
	}

	public CheckDetailAdapter(Context context, List<Sheet.ListClass> steelList) {
		this.mContext = context;
		this.list = steelList;
		this.mInflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list != null ? list.size() : 0;
	}

	@Override
	public Sheet.ListClass getItem(int position) {
		// TODO Auto-generated method stub
		return list != null ? list.get(position) : null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		Holder holder = null;
		if (null == convertView) {
			holder = new Holder();
			convertView = mInflater.inflate(R.layout.item_lv_checkdetail, null);
			holder.layout = (LinearLayout) convertView
					.findViewById(R.id.item_lv_checkdetail);
			holder.sheetTv = (TextView) convertView
					.findViewById(R.id.item_tv_sheetinfo);
			holder.dropBtn = (ImageView) convertView
					.findViewById(R.id.drag_handle);
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}

		Sheet.ListClass steel = list.get(position);
		int resource = -1;
		switch (steel.getSheetStatus()) {
		// 信息正常
		case 0:
			resource = R.drawable.checkdetail_status_normal;
			break;
		// 信息有误(钢板不存在)
		case 1:
			resource = R.drawable.checkdetail_status_lost;
			break;
		// 需要倒垛(层号不同)
		case 2:
			resource = R.drawable.checkdetail_status_warn;
			break;
		// 需要倒垛(钢板规格和垛位定义信息不符)
		case 3:
			resource = R.drawable.checkdetail_status_info;
			break;
		}
		holder.layout.setBackgroundResource(resource);
		holder.sheetTv.setText(getSteelString(steel.getItemNo(),
				steel.getBarcode(), steel.getMaterial(), steel.getLength(),
				steel.getWidth(), steel.getThickness(), steel.getProjectId()));
		return convertView;
	}

	private String getSteelString(int itemNo, String code, String material,
			String length, String width, String thickness, String projectId) {
		StringBuffer sb = new StringBuffer();
		if (itemNo >0) {
			sb.append(itemNo);
		}
		if (SmsUtil.checkString(code)) {
			sb.append(": ");
			sb.append(code);
		}
		if (SmsUtil.checkString(material)) {
			sb.append("; ");
			sb.append(material);
		}
		if (SmsUtil.checkString(thickness)) {
			sb.append("*");
			sb.append(thickness);
		}
		if (SmsUtil.checkString(length)) {
			sb.append("; ");
			sb.append(length);
		}
		if (SmsUtil.checkString(width)) {
			sb.append("*");
			sb.append(width);
		}
		if (SmsUtil.checkString(projectId)) {
			sb.append("; ");
			sb.append(projectId);
		}
		return sb.toString();
	}

}

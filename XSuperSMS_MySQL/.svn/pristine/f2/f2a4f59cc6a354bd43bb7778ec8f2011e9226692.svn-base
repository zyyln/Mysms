/**
 * <p>Title: PopuWindowAdapter.java</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2007</p>
 * <p>Company: XSuper</p>
 * @author XS-PC011
 * @date 2015-9-9
 *
 */
package com.xuesi.sms.widget.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xuesi.mos.util.MosLog;
import com.xuesi.sms.R;
import com.xuesi.sms.bean.GetBillNoAndSheetMsg;

/**
 * <p>
 * Title:获取单号PopuWindow适配器
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
public class PopuWindowAdapter extends BaseAdapter {
	private final String TAG_LOG = PopuWindowAdapter.class.getSimpleName();
	private final MosLog mLog = MosLog.getInstance(TAG_LOG, "XUESI-SMS");
	private LayoutInflater mInflater;
	private List<GetBillNoAndSheetMsg.BnAndSm> list;
	/** 获取订单：0；下拉获取钢板信息：1 */
	private int popuFlag;

	public PopuWindowAdapter(Context context,
			List<GetBillNoAndSheetMsg.BnAndSm> list, int popuFlag) {
		this.list = list;
		this.mInflater = LayoutInflater.from(context);
		this.popuFlag = popuFlag;
	}

	public class viewHolder {
		TextView item_msg;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if (list != null) {
			return list.size();
		} else {
			return 0;
		}
	}

	@Override
	public GetBillNoAndSheetMsg.BnAndSm getItem(int position) {
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
		viewHolder holder = null;
		if (null == convertView) {
			holder = new viewHolder();
			convertView = mInflater.inflate(R.layout.item_lv_popwin, null);
			holder.item_msg = (TextView) convertView
					.findViewById(R.id.popwin_tv_content);
			convertView.setTag(holder);
		} else {
			holder = (viewHolder) convertView.getTag();
		}
		if (popuFlag == 0) {
			holder.item_msg.setText(list.get(position).getBillsNo());
		} else if (popuFlag == 1) {
			holder.item_msg.setText(list.get(position).getMactchPara());
		}
		return convertView;
	}

}

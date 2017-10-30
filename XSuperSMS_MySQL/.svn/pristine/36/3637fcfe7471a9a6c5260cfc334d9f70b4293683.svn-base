/**
 * <p>Title: BillNoSheetAdapter.java</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2007</p>
 * <p>Company: XSuper</p>
 * @author XS-PC011
 * @date 2015-9-9
 *
 */
package com.xuesi.sms.widget.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.xuesi.mos.util.MosLog;
import com.xuesi.sms.R;
import com.xuesi.sms.bean.ConfigFieldEntity;
import com.xuesi.sms.bean.Sheet;
import com.xuesi.sms.widget.scrolllistview.MyHScrollView;
import com.xuesi.sms.widget.scrolllistview.MyHScrollView.OnScrollChangedListener;

/**
 * <p>
 * Title: 钢板内部适配器
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
public class BillNoSheetAdapter extends BaseAdapter {
	private final String TAG_LOG = BillNoSheetAdapter.class.getSimpleName();
	private final MosLog mLog = MosLog.getInstance(TAG_LOG, "XUESI-SMS");
	private LayoutInflater mInflater;
	private List<Sheet.ListClass> list;
	private List<ArrayList<ConfigFieldEntity.Segment>> sheetList;
	private Context context;
	private LinearLayout linear;
	/** 选中项集合 */
	private List<Sheet.ListClass> selectedList = new ArrayList<Sheet.ListClass>();

	public BillNoSheetAdapter(Context context,
			List<Sheet.ListClass> sheetBillNoList,
			List<ArrayList<ConfigFieldEntity.Segment>> sheetList,
			LinearLayout linear) {
		this.list = sheetBillNoList;
		this.sheetList = sheetList;
		this.mInflater = LayoutInflater.from(context);
		this.context = context;
		this.linear = linear;
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
		convertView = null;
		convertView = mInflater.inflate(R.layout.item_scrollview_querybill,
				null);
		MyHScrollView scrollView1 = (MyHScrollView) convertView
				.findViewById(R.id.horizontalScrollView1);
		LinearLayout content = (LinearLayout) convertView
				.findViewById(R.id.lv_field);
		addView(content, sheetList.get(position), 1);

		MyHScrollView headSrcrollView = (MyHScrollView) linear
				.findViewById(R.id.horizontalScrollView1);
		headSrcrollView
				.AddOnScrollChangedListener(new OnScrollChangedListenerImp(
						scrollView1));

		Sheet.ListClass sheet = list.get(position);

		if (sheet.isChecked()) {
			convertView
					.setBackgroundResource(R.drawable.sheetin_info_bj_violet);
		} else if (!sheet.isChecked()) {
			convertView
					.setBackgroundResource(R.drawable.sheet_detail_list_item_bg);
		}
		return convertView;
	}

	class OnScrollChangedListenerImp implements OnScrollChangedListener {
		MyHScrollView mScrollViewArg;

		public OnScrollChangedListenerImp(MyHScrollView scrollViewar) {
			mScrollViewArg = scrollViewar;
		}

		@Override
		public void onScrollChanged(int l, int t, int oldl, int oldt) {
			mScrollViewArg.smoothScrollTo(l, t);
		}
	};

	public void addView(final LinearLayout lineLayout,
			List<ConfigFieldEntity.Segment> list, int Flag) {
		lineLayout.removeAllViews();
		for (int i = 0; i < list.size(); i++) {
			TextView showText = new TextView(context);
			showText.setTextColor(Color.BLACK);
			showText.setTextSize(13);
			if (Flag == 0) {
				showText.setText(list.get(i).getVal());
			} else {
				showText.setText(list.get(i).getValue());
			}
			showText.setBackgroundColor(Color.TRANSPARENT);
			showText.setGravity(Gravity.CENTER);
			// set 文本大小
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
					180, LayoutParams.WRAP_CONTENT);
			// set 四周距离
			params.setMargins(3, 3, 3, 3);

			showText.setLayoutParams(params);
			// 添加文本到主布局
			lineLayout.addView(showText);
			// 分割线
			TextView showLine = new TextView(context);
			showLine.setBackgroundColor(R.color.white);
			// set 文本大小
			LinearLayout.LayoutParams pa = new LinearLayout.LayoutParams(2,
					LayoutParams.MATCH_PARENT);
			showLine.setLayoutParams(pa);
			lineLayout.addView(showLine);
		}
	}

	public List<Sheet.ListClass> getSelectedList() {
		selectedList.clear();
		for (Sheet.ListClass sheet : list) {
			if (sheet.isChecked()) {
				selectedList.add(sheet);
			}
		}
		return selectedList;
	}

	public void setSelectedList(List<Sheet.ListClass> selectedList) {
		this.selectedList = selectedList;
	}

}

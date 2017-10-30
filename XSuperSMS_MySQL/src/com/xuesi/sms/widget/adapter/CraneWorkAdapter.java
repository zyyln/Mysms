package com.xuesi.sms.widget.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.xuesi.mos.util.MosUtil;
import com.xuesi.sms.R;
import com.xuesi.sms.SmsConfig;
import com.xuesi.sms.bean.CraneWork;
import com.xuesi.sms.bean.CraneWorkDetail;
import com.xuesi.sms.util.SmsUtil;

/**
 * 行车作业面板-作业列表
 * 
 * @author Administrator
 * 
 */
public class CraneWorkAdapter extends BaseAdapter {
	private LayoutInflater mInflater;
	private Context mContext;
	private List<CraneWork> workList;
	private List<CraneWorkDetail.ListClass> detailList;
	private Resources mRes;
	/** 钢板合计数 */
	private int steelAmount;

	private class Holder {
		// ImageView imgView;
		TextView workIdTxt;
		ListView listView;
	}

	public CraneWorkAdapter(Context context,
			List<CraneWorkDetail.ListClass> workDetailList) {
		this.mInflater = LayoutInflater.from(context);
		this.mRes = context.getResources();
		this.mContext = context;
		initData(workDetailList);
	}

	private void initData(List<CraneWorkDetail.ListClass> workDetailList) {
		String tmpWorkID = null;// 作业号
		CraneWork craneWork = null;// 作业对象
		workList = new ArrayList<CraneWork>();
		if (workDetailList.size() > 0) {
			for (int i = 0; i < workDetailList.size(); i++) {
				String workid = workDetailList.get(i).getWorkId();
				if (null == tmpWorkID || !tmpWorkID.equals(workid)) {
					if (null != tmpWorkID) {
						// 上一个相同流水码的作业对象craneWork完成
						craneWork.setCraneWorkDetail(detailList);
						workList.add(craneWork);
					}
					craneWork = new CraneWork();
					craneWork.setWorkID(workid);
					detailList = new ArrayList<CraneWorkDetail.ListClass>();
				}
				tmpWorkID = workid;
				detailList.add(workDetailList.get(i));
				if (i == workDetailList.size() - 1) {
					// 添加最后一条数据
					craneWork.setCraneWorkDetail(detailList);
					workList.add(craneWork);
				}
			}
		}
	}

	public int getSteelAmount() {
		// 每次归零
		steelAmount = 0;
		for (int i = 0; i < getCount(); i++) {
			steelAmount += getItem(i).getCount();
		}
		return steelAmount;
	}

	public void removeItem(int position) {
		workList.remove(position);
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return workList != null ? workList.size() : 0;
	}

	@Override
	public CraneWork getItem(int position) {
		return workList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Holder subHolder = null;
		if (null == convertView) {
			subHolder = new Holder();
			convertView = mInflater.inflate(R.layout.item_crane_work_panel,
					null);
			subHolder.workIdTxt = (TextView) convertView
					.findViewById(R.id.crane_work_panel_list_workid_txt);
			// subHolder.imgView = (ImageView)
			// convertView.findViewById(R.id.crane_work_panel_list_imageview);
			subHolder.listView = (ListView) convertView
					.findViewById(R.id.crane_work_panel_list_sub_listview);
			convertView.setTag(subHolder);
		} else {
			subHolder = (Holder) convertView.getTag();
		}

		CraneWork craneWork = workList.get(position);

		// 第一条数据，文字设为红色，显示箭头
		if (position == 0) {
			subHolder.workIdTxt.setTextColor(mRes.getColor(R.color.red));
			// subHolder.imgView.setVisibility(View.VISIBLE);
		} else {
			subHolder.workIdTxt.setTextColor(mRes.getColor(R.color.black));
			// subHolder.imgView.setVisibility(View.GONE);
		}

		// holder.rLayout.setBackgroundDrawable(drw);
		subHolder.workIdTxt.setText(craneWork.getWorkID());
		WorkInfoDetailAdapter detailAdapter = new WorkInfoDetailAdapter(
				craneWork.getCraneWorkDetail(), position == 0 ? true : false,
				false);
		subHolder.listView.setAdapter(detailAdapter);
		// if (craneWork.getCraneWorkDetail().size() >= 3) {
		// View itemView = detailAdapter.getView(0, null, subHolder.listView);
		// itemView.measure(0, 0);
		// itemView.getMeasuredHeight();
		// }
		MosUtil.setListViewHeightBasedOnChildren(subHolder.listView);
		if (convertView.getHeight() > subHolder.listView.getHeight()) {
			// ViewGroup.LayoutParams params = subHolder.listView
			// .getLayoutParams();
			// params.height = convertView.getHeight();
			// subHolder.listView.setLayoutParams(params);
			ViewGroup.LayoutParams params = convertView.getLayoutParams();
			params.height = subHolder.listView.getHeight();
			convertView.setLayoutParams(params);
		}
		return convertView;
	}

	/**
	 * 
	 * 设置Listview的高度
	 */
	public void setListViewHeight(ListView listView) {

		WorkInfoDetailAdapter listAdapter = (WorkInfoDetailAdapter) listView
				.getAdapter();

		if (listAdapter == null) {
			return;
		}

		int totalHeight = 0;

		for (int i = 0; i < listAdapter.getCount(); i++) {
			View listItem = listAdapter.getView(i, null, listView);
			listItem.measure(0, 0);
			totalHeight += listItem.getMeasuredHeight();
		}

		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight
				+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		listView.setLayoutParams(params);
	}

	// public View getHeaderView() {
	// View headerView = mInflater.inflate(R.layout.item_crane_work_panel,
	// null);

	// ((LinearLayout)
	// headerView.findViewById(R.id.crane_work_panel_list_layout))
	// .setBackgroundResource(R.drawable.sheet_detail_head_bg);

	// ListView listView = (ListView)
	// headerView.findViewById(R.id.crane_work_panel_list_sub_listview);
	//
	// CraneWorkDetail.ListClass detailData = new CraneWorkDetail().new
	// ListClass();
	// detailData.setFirStackname(mRes.getString(R.string.crane_work_panel_from));
	// detailData.setSecStackname(mRes.getString(R.string.crane_work_panel_to));
	// detailData.setAmountName(mRes.getString(R.string.quantity));
	// detailData.setCreater(mRes.getString(R.string.crane_work_panel_author));
	// detailData.setMaterial(mRes.getString(R.string.material));
	// detailData.setRemark(mRes.getString(R.string.crane_work_panel_remark));
	// detailData.setWeightName(mRes.getString(R.string.crane_work_panel_single_weight));
	//
	// List<CraneWorkDetail.ListClass> listData = new
	// ArrayList<CraneWorkDetail.ListClass>();
	// listData.add(detailData);
	// listView.setAdapter(new WorkInfoDetailAdapter(listData, false,
	// true));
	// return headerView;
	// }

	private class WorkInfoDetailAdapter extends BaseAdapter {
		private List<CraneWorkDetail.ListClass> workDetailList;
		private boolean firstWork = false;
		private boolean titleFlag = false;

		private class Holder {
			EditText formText;
			EditText toText;
			EditText materialText;
			EditText specText;
			EditText amountText;
			EditText weightText;
			EditText createrText;
			EditText remarkText;
		}

		public WorkInfoDetailAdapter(
				List<CraneWorkDetail.ListClass> workDetailList,
				boolean firstWork, boolean titleFlag) {
			this.workDetailList = workDetailList;
			this.firstWork = firstWork;
			this.titleFlag = titleFlag;
		}

		@Override
		public int getCount() {
			return workDetailList != null ? workDetailList.size() : 0;
		}

		@Override
		public CraneWorkDetail.ListClass getItem(int position) {
			return workDetailList != null ? workDetailList.get(position) : null;
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
				convertView = mInflater.inflate(
						R.layout.item_crane_work_panel_sub, null);
				holder.formText = (EditText) convertView
						.findViewById(R.id.crane_work_panel_list_from_txt);
				holder.toText = (EditText) convertView
						.findViewById(R.id.crane_work_panel_list_to_txt);
				holder.materialText = (EditText) convertView
						.findViewById(R.id.crane_work_panel_list_material_txt);
				holder.specText = (EditText) convertView
						.findViewById(R.id.crane_work_panel_list_spec_txt);
				holder.amountText = (EditText) convertView
						.findViewById(R.id.crane_work_panel_list_num_txt);
				holder.weightText = (EditText) convertView
						.findViewById(R.id.crane_work_panel_list_singleweight_txt);
				holder.createrText = (EditText) convertView
						.findViewById(R.id.crane_work_panel_list_author_txt);
				holder.remarkText = (EditText) convertView
						.findViewById(R.id.crane_work_panel_list_remark_txt);
				convertView.setTag(holder);
			} else {
				holder = (Holder) convertView.getTag();
			}

			CraneWorkDetail.ListClass workDetailData = workDetailList
					.get(position);

			int textColor;
			// 第一条数据，文字设为红色
			if (firstWork) {
				textColor = mRes.getColor(R.color.red);
			} else {
				textColor = mRes.getColor(R.color.black);
			}

			holder.formText.setTextColor(textColor);
			holder.formText.setText(workDetailData.getFirStackname().trim());

			holder.toText.setTextColor(textColor);
			holder.toText.setText(workDetailData.getSecStackname().trim());

			holder.materialText.setTextColor(textColor);
			holder.materialText.setText(workDetailData.getMaterial());

			holder.specText.setTextColor(textColor);
			holder.amountText.setTextColor(textColor);
			holder.weightText.setTextColor(textColor);
			if (this.titleFlag) {
				holder.specText.setText(workDetailData.getSpecTitle());
				holder.weightText.setText(workDetailData.getWeightName());
				holder.amountText.setText(workDetailData.getAmountName());
			} else {
				holder.specText.setText(workDetailData.getSpec());
				holder.weightText.setText(workDetailData.getWeight());
				holder.amountText.setText(workDetailData.getAmount() + "");
			}

			holder.createrText.setTextColor(textColor);
			holder.createrText.setText(workDetailData.getCreater());

			holder.remarkText.setTextColor(textColor);
			holder.remarkText.setText(workDetailData.getRemark());
			return convertView;
		}
	}

	private View getSubHeaderView() {
		return mInflater.inflate(R.layout.item_crane_work_panel_sub, null);
	}

}

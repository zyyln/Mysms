package com.xuesi.sms.app.activity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;
import com.xuesi.mos.device.Screen;
import com.xuesi.mos.libs.pulltorefresh.PullToRefreshBase;
import com.xuesi.mos.libs.pulltorefresh.PullToRefreshListView;
import com.xuesi.mos.util.MosLog;
import com.xuesi.sms.R;
import com.xuesi.sms.ServerApi;
import com.xuesi.sms.SmsConfig;
import com.xuesi.sms.bean.ConfigFieldEntity;
import com.xuesi.sms.bean.ConfigFieldEntity.Segment;
import com.xuesi.sms.bean.GetBillNoAndSheetMsg;
import com.xuesi.sms.bean.GetBillNoAndSheetMsg.BnAndSm;
import com.xuesi.sms.bean.SheetBillNo;
import com.xuesi.sms.util.CalendarUtil;
import com.xuesi.sms.util.SmsUtil;
import com.xuesi.sms.widget.adapter.PopuWindowAdapter;
import com.xuesi.sms.widget.scrolllistview.MyHScrollView;
import com.xuesi.sms.widget.scrolllistview.MyHScrollView.OnScrollChangedListener;

public class QueryBillActivity extends SmsActivity implements
		PullToRefreshBase.OnRefreshListener2<ListView>, OnItemClickListener {
	private final String TAG_LOG = QueryBillActivity.class.getName();
	private final MosLog mLog = MosLog.getInstance(TAG_LOG, "XUESI-MES");
	/** progress的次数 */
	private int mProgressNumber = 0;
	private LinearLayout mHead;
	private LinearLayout mField;
	private PullToRefreshListView billPullToRefreshListView;
	private ListView billListView;
	private DefinedAdapter definedAdapter;

	private EditText startDateET, stopDateET;
	private Spinner sheettypeSp;
	/***/
	public List<ConfigFieldEntity.Segment> fieldList = new ArrayList<ConfigFieldEntity.Segment>();
	public List<ArrayList<ConfigFieldEntity.Segment>> field_valueList = new ArrayList<ArrayList<ConfigFieldEntity.Segment>>();
	// 选择采购单号
	/** 选择单号组合框 */
	private LinearLayout selectListnumLinear;
	private EditText ListnumNoTV;
	/** 选择单号弹出框 */
	private View selectBillNoView;
	private PopupWindow selectBillNoWin;
	private ListView selectBillNoListView;
	private List<BnAndSm> popuList = new ArrayList<BnAndSm>();
	/** PopuWindow适配器 */
	private PopuWindowAdapter BillNoAdapter;
	/** 当前页码 */
	private int mCurrentNumber = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_query_bills);
	}

	@SuppressLint("NewApi")
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		billListView.setFriction(ViewConfiguration.getScrollFriction() * 2);
	}

	@Override
	protected void initContentView() {
		// TODO Auto-generated method stub
		super.initContentView();
		setTopTitle(R.string.bill_search);
		setRefreshView(View.INVISIBLE);
		selectListnumLinear = (LinearLayout) findViewById(R.id.codebind_layout_listnum);
		ListnumNoTV = (EditText) findViewById(R.id.codebind_tv_listnumName);
		findViewById(R.id.codebind_btn_listnumArrow).setOnClickListener(this);
		// 选择单号弹出框
		selectBillNoView = getLayoutInflater().inflate(
				R.layout.popuwindow_view, null);
		selectBillNoListView = (ListView) selectBillNoView
				.findViewById(R.id.popup_lv_houseName);
		selectBillNoListView.setOnItemClickListener(this);
		/***/
		sheettypeSp = (Spinner) findViewById(R.id.SP_sheettype);
		startDateET = (EditText) findViewById(R.id.etPickerStart);
		startDateET.setText(CalendarUtil.getTodayDate());
		stopDateET = (EditText) findViewById(R.id.etPickerStop);
		stopDateET.setText(CalendarUtil.getTodayDate());

		mHead = (LinearLayout) findViewById(R.id.head);
		mHead.setFocusable(true);
		mHead.setClickable(true);
		// mHead.setBackgroundColor(Color.parseColor("#b2d235"));
		mHead.setBackgroundResource(R.drawable.sheet_detail_head_bg);
		mHead.setOnTouchListener(new ListViewAndHeadViewTouchLinstener());
		mField = (LinearLayout) findViewById(R.id.field);
		billPullToRefreshListView = (PullToRefreshListView) findViewById(R.id.Lv_Bills);
		billPullToRefreshListView.setOnRefreshListener(this);
		billPullToRefreshListView.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
		billListView = billPullToRefreshListView.getRefreshableView();
		billListView
				.setOnTouchListener(new ListViewAndHeadViewTouchLinstener());
		billListView.setOnItemClickListener(this);
	}

	/**
	 * 在区域上动态添加数据
	 * 
	 * @param lineLayout区域
	 * @param list数据源
	 * @param Flag
	 *            0：字段名称，1：值
	 */
	public void addView(final LinearLayout lineLayout,
			List<ConfigFieldEntity.Segment> list, int Flag) {
		lineLayout.removeAllViews();
		for (int i = 0; i < list.size(); i++) {
			TextView showText = new TextView(this);
			showText.setTextColor(Color.BLACK);
			showText.setTextSize(13);
			showText.getPaint().setFakeBoldText(true);
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
			TextView showLine = new TextView(this);
			showLine.setBackgroundColor(getResources().getColor(R.color.white));
			// set 文本大小
			LinearLayout.LayoutParams pa = new LinearLayout.LayoutParams(2,
					LayoutParams.MATCH_PARENT);
			showLine.setLayoutParams(pa);
			lineLayout.addView(showLine);
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	public void myNetwork(String url, String... txt) {
		// TODO Auto-generated method stub
		super.myNetwork(url, txt);
		mProgressNumber++;
		if (mProgressNumber == 1) {
			showProgressDialog("加载中", false, false);
		}
		JSONObject json;
		if (ServerApi.getInstance().API_SHEETORDERLIST.equals(url)) {
			try {
				json = getRequstJson();
				if ("已入库".equals(sheettypeSp.getSelectedItem().toString())) {
					json.put("flag", 1);
				} else if ("已到货".equals(sheettypeSp.getSelectedItem()
						.toString())) {
					json.put("status", 1);
				} else if ("未到货".equals(sheettypeSp.getSelectedItem()
						.toString())) {
					json.put("status", 0);
				} else if ("全部"
						.equals(sheettypeSp.getSelectedItem().toString())) {
				}
				json.put("startTime", startDateET.getText().toString().trim());
				StringBuffer sb = new StringBuffer();
				sb.append(stopDateET.getText().toString().trim().split("-")[0]);
				sb.append("-");
				sb.append(stopDateET.getText().toString().trim().split("-")[1]);
				sb.append("-");
				sb.append(Integer.parseInt(stopDateET.getText().toString()
						.trim().split("-")[2])
						+ 1 + "");
				json.put("endTime", sb.toString());
				json.put("curPage", mCurrentNumber);
				json.put("pageQty", SmsConfig.pageSize);
				json.put("billNo", ListnumNoTV.getText().toString().trim());
				json.put("orderField", "BILLSNO");
				sendPOST(url, json, null, this, url, false);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		} else if (ServerApi.getInstance().API_GETTABLEHEAD.equals(url)) {
			try {
				json = getRequstJson();
				json.put("pFACTORYCODE", ServerApi.factoryCode);
				json.put("page", ServerApi.CONFIGURE_BILLS);
				json.put("lang", ServerApi.LANG);
				sendPOST(url, json, null, this, url, false);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		} else if (ServerApi.getInstance().ORDER_GETBILLNOS.equals(url)) {// 获取发布单号
			try {
				json = getRequstJson();
				json.put("status", "-1");
				json.put("pbillon", ListnumNoTV.getText().toString().trim());
				json.put("perPage", 9999);
				sendPOST(url, json, null, this, url, false);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void onRequestFail(String url, Exception ex) {
		// TODO Auto-generated method stub
		super.onRequestFail(url, ex);
		if (mProgressNumber > 0) {
			mProgressNumber--;
		}
		if (mProgressNumber == 0) {
			dismissProgressDialog();
		}
	}

	@Override
	public void onRequestSuccess(String url, Object obj) {
		// TODO Auto-generated method stub
		super.onRequestSuccess(url, obj);
		if (mProgressNumber > 0) {
			mProgressNumber--;
		}
		if (mProgressNumber == 0) {
			dismissProgressDialog();
		}
		Gson gson = new Gson();
		mLog.i("打印JSON数据" + obj.toString());
		if (ServerApi.getInstance().API_SHEETORDERLIST.equals(url)) {
			SheetBillNo sheetBillNo = gson.fromJson(obj.toString(),
					SheetBillNo.class);
			if (sheetBillNo.getResultCode() == 0) {
				try {
					for (int i = 0; i < ((JSONObject) obj).getJSONArray("list")
							.length(); i++) {
						field_valueList.add(getValidDate(((JSONObject) obj)
								.getJSONArray("list").get(i), fieldList));
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				++mCurrentNumber;
				definedAdapter = new DefinedAdapter(this, field_valueList);
				billListView.setAdapter(definedAdapter);
				billPullToRefreshListView.onRefreshComplete();
				if (field_valueList.size() == sheetBillNo.getTotal()) {
					showShortToast("数据全部加载完成，没有更多数据");
					billPullToRefreshListView
							.setMode(PullToRefreshBase.Mode.DISABLED);
				}
			}
		} else if (ServerApi.getInstance().API_GETTABLEHEAD.equals(url)) {
			ConfigFieldEntity cfEntity = gson.fromJson(obj.toString(),
					ConfigFieldEntity.class);
			if (cfEntity.getResultCode() == 0) {
				fieldList = cfEntity.getList();
				addView(mField, fieldList, 0);
				myNetwork(ServerApi.getInstance().API_SHEETORDERLIST);
			}
		} else if (ServerApi.getInstance().ORDER_GETBILLNOS.equals(url)) {// 获取单号
			GetBillNoAndSheetMsg gbn = gson.fromJson(obj.toString(),
					GetBillNoAndSheetMsg.class);
			// 加一行主要是解析失败的，加一个提示，不需要闪退
			if (gbn == null) {
				showShortToast("数据解析失败，请联系开发人员！！");
				return;
			}
			if (gbn.getResultCode() == 0) {
				popuList = gbn.getList();
				if (popuList.size() != 0) {
					BillNoAdapter = new PopuWindowAdapter(this, popuList, 0);
					selectBillNoListView.setAdapter(BillNoAdapter);
					showBindNoPopup();
				} else {
					showShortToast(R.string.no_date);
				}
			} else {
				showShortToast("resultCode==" + gbn.getResultCode()
						+ "，请联系开发人员");
			}
		}
	}

	/**
	 * 显示选择单号弹出框
	 */
	/**
	 * 显示选择单号弹出框
	 */
	private void showBindNoPopup() {
		if (null != selectBillNoWin && selectBillNoWin.isShowing()) {
			selectBillNoWin.dismiss();
		}
		int width = selectListnumLinear.getWidth();
		selectBillNoWin = new PopupWindow(selectBillNoView, width,
				ViewGroup.LayoutParams.WRAP_CONTENT);
		selectBillNoWin.setFocusable(true);
		selectBillNoWin.setOutsideTouchable(true);
		selectBillNoWin.setBackgroundDrawable(new BitmapDrawable());
		selectBillNoWin.setAnimationStyle(R.style.AnimTop2);
		if (popuList.size() > 5) {
			selectBillNoWin
					.setHeight(Screen.getInstance().getScreenHeight() * 4 / 10);
		}
		if (selectBillNoWin.isShowing()) {
			selectBillNoWin.dismiss();
		} else {
			int[] position = new int[2];
			selectListnumLinear.getLocationOnScreen(position);
			int height = selectListnumLinear.getHeight();
			int y = position[1] + height;
			// 在指定位置显示
			selectBillNoWin.showAtLocation(selectListnumLinear, Gravity.LEFT
					| Gravity.TOP, position[0], y);
		}
	}

	/**
	 * 将勾选的字段与返回的数据比较后合并成一个有效的实体类
	 * 
	 * @param obj获取的所有数据
	 * @param list有效的字段
	 * @return
	 */
	public static ArrayList<Segment> getValidDate(Object obj,
			List<ConfigFieldEntity.Segment> list) {
		ArrayList<Segment> retList = new ArrayList<ConfigFieldEntity.Segment>();
		List<String> tpList = new ArrayList<String>();
		List<String> tempList = new ArrayList<String>();
		Iterator iterator = ((JSONObject) obj).keys();
		while (iterator.hasNext()) {
			String key = (String) iterator.next();
			tpList.add(key.toLowerCase());
			tempList.add(key);
		}
		for (int i = 0; i < list.size(); i++) {
			if (tpList.contains(list.get(i).getName().toLowerCase())) {
				ConfigFieldEntity.Segment sg = new ConfigFieldEntity().new Segment();
				try {
					int index = tpList.indexOf(list.get(i).getName()
							.toLowerCase());
					if (tempList.get(index).equals("thickness")
							|| tempList.get(index).equals("length")
							|| tempList.get(index).equals("width")
							|| tempList.get(index).equals("weight")) {
						sg.setValue(SmsUtil.DecimalFormat(((JSONObject) obj)
								.getString(tempList.get(index)),
								SmsConfig.dotNum));
					} else {
						sg.setValue(((JSONObject) obj).getString(tempList
								.get(index)));
					}
					sg.setName(list.get(i).getName());
					retList.add(sg);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return retList;
	}

	class ListViewAndHeadViewTouchLinstener implements View.OnTouchListener {

		@Override
		public boolean onTouch(View arg0, MotionEvent arg1) {
			// 当在列头 和 listView控件上touch时，将这个touch的事件分发给 ScrollView
			HorizontalScrollView headSrcrollView = (HorizontalScrollView) mHead
					.findViewById(R.id.horizontalScrollView1);
			headSrcrollView.onTouchEvent(arg1);
			return false;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		if (parent == selectBillNoListView) {
			ListnumNoTV.setText(popuList.get(position).getBillsNo());
			selectBillNoWin.dismiss();
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		super.onClick(v);
		switch (v.getId()) {
		case R.id.codebind_btn_listnumArrow: {
			myNetwork(ServerApi.getInstance().ORDER_GETBILLNOS);
		}
			break;
		}
	}

	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
		// TODO Auto-generated method stub
		myNetwork(ServerApi.getInstance().API_SHEETORDERLIST);
	}

	/**
	 * 显示起始日期选择
	 */
	public void showStartDatePickDlg(View view) {
		Calendar calendar = Calendar.getInstance();
		DatePickerDialog datePickerDialog = new DatePickerDialog(
				QueryBillActivity.this, new OnDateSetListener() {

					@Override
					public void onDateSet(DatePicker view, int year,
							int monthOfYear, int dayOfMonth) {
						startDateET.setText(year + "-" + (monthOfYear + 1)
								+ "-" + dayOfMonth);
					}
				}, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
				calendar.get(Calendar.DAY_OF_MONTH));
		datePickerDialog.show();
	}

	/**
	 * 显示结束日期选择
	 */
	public void showStopDatePickDlg(View view) {
		Calendar calendar = Calendar.getInstance();
		DatePickerDialog datePickerDialog = new DatePickerDialog(
				QueryBillActivity.this, new OnDateSetListener() {

					@Override
					public void onDateSet(DatePicker view, int year,
							int monthOfYear, int dayOfMonth) {
						stopDateET.setText(year + "-" + (monthOfYear + 1) + "-"
								+ dayOfMonth);
					}
				}, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
				calendar.get(Calendar.DAY_OF_MONTH));
		datePickerDialog.show();
	}

	/**
	 * 检索
	 */
	public void searchBill(View view) {
		fieldList.clear();
		field_valueList.clear();
		mCurrentNumber = 1;
		myNetwork(ServerApi.getInstance().API_GETTABLEHEAD);
	}

	public class DefinedAdapter extends BaseAdapter {
		private LayoutInflater inflate;
		private List<ArrayList<ConfigFieldEntity.Segment>> billList;

		public DefinedAdapter(Context context,
				List<ArrayList<ConfigFieldEntity.Segment>> billList) {
			inflate = LayoutInflater.from(context);
			this.billList = billList;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return billList.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return billList.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			synchronized (QueryBillActivity.this) {

				convertView = inflate.inflate(
						R.layout.item_scrollview_querybill, null);
				MyHScrollView scrollView1 = (MyHScrollView) convertView
						.findViewById(R.id.horizontalScrollView1);
				LinearLayout content = (LinearLayout) convertView
						.findViewById(R.id.lv_field);
				addView(content, billList.get(position), 1);

				MyHScrollView headSrcrollView = (MyHScrollView) mHead
						.findViewById(R.id.horizontalScrollView1);
				headSrcrollView
						.AddOnScrollChangedListener(new OnScrollChangedListenerImp(
								scrollView1));
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

	}
}

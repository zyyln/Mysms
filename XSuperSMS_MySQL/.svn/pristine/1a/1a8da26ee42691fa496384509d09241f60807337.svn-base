package com.xuesi.sms.app.activity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.xuesi.mos.libs.pulltorefresh.PullToRefreshBase;
import com.xuesi.mos.libs.pulltorefresh.PullToRefreshListView;
import com.xuesi.mos.util.MosLog;
import com.xuesi.sms.R;
import com.xuesi.sms.ServerApi;
import com.xuesi.sms.SmsApplication;
import com.xuesi.sms.SmsConfig;
import com.xuesi.sms.bean.BaseModel;
import com.xuesi.sms.bean.ConfigFieldEntity;
import com.xuesi.sms.bean.Sheet;
import com.xuesi.sms.bean.Stack;
import com.xuesi.sms.bean.StackExplain;
import com.xuesi.sms.bean.StoreHouse;
import com.xuesi.sms.widget.scrolllistview.MyHScrollView;
import com.xuesi.sms.widget.scrolllistview.MyHScrollView.OnScrollChangedListener;

/**
 * 垛位详情（钢板或者余料）<br>
 * 来源activity：<br>
 * {@link StoreActivity}<br>
 * {@link InputActivity}<br>
 * {@link OutputActivity}<br>
 * 
 * @author XS-PC014
 * 
 */
public class SheetDetailActivity extends SmsActivity implements
		PullToRefreshBase.OnRefreshListener2<ListView> {
	/** LOG */
	private final String TAG_LOG = SheetDetailActivity.class.getSimpleName();
	private final MosLog mLog = MosLog.getInstance(TAG_LOG, "XUESI-SMS");
	/** progress的次数 */
	private int mProgressNumber = 0;

	/** 来源库房 */
	private StoreHouse.ListClass fromStorehouse;
	/** 来源垛位 */
	private Stack.ListClass fromStack;

	/** 钢板列表项 */
	private PullToRefreshListView sheetPullRefreshListView;
	private ListView sheetListView;
	/** 钢板信息适配器 */
	private SheetDetailAdapter sheetDetailAdapter;
	/** 每页显示条数 */
	private final int mPageSize = SmsConfig.pageSize;
	/** 当前页码 */
	private int mCurrentNumber = 1;
	/** 钢板总数 */
	private int mTotal;
	/** 查询字段 */
	private String barCode, material, thickness, length, width, inTime,
			project, assemblyNo;

	/** 底部显示库房号、垛位号、垛位当前高度、厚度、材质、张数 */
	private String sheetStackCodeTv, sheetStockCodeTv, sheetHeightTv,
			sheetNumTv, materialTv, thicknessTv;
	private EditText sheetDetailEt;

	/** 钢板选项和详情 弹出框 */
	private Dialog optionsDialog, detailDialog;
	/** 保存要填写的的垛位信息 */
	protected List<StackExplain.StackMsg> stackMsgList = new ArrayList<StackExplain.StackMsg>();
	/** 列表头部 */
	private LinearLayout mHead;
	private LinearLayout mField;
	/** 存储字段，存储字段对应的数据 */
	public List<ConfigFieldEntity.Segment> fieldList = new ArrayList<ConfigFieldEntity.Segment>();
	public List<ArrayList<ConfigFieldEntity.Segment>> field_valueList = new ArrayList<ArrayList<ConfigFieldEntity.Segment>>();
	private String transferArg;
	List<Sheet.ListClass> allList = new ArrayList<Sheet.ListClass>();
	List<Sheet.ListClass> sList = new ArrayList<Sheet.ListClass>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		SmsApplication.getInstance().addActivity(this);// 添加到Activity堆栈
		setContentView(R.layout.activity_sheetdetail);
	

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		// 加载额外数据
		loadExtras();
	}

	@Override
	protected void initContentView() {
		// TODO Auto-generated method stub
		super.initContentView();
		mHead = (LinearLayout) findViewById(R.id.head);
		mHead.setFocusable(true);
		mHead.setClickable(true);
		// mHead.setBackgroundColor(Color.parseColor("#b2d235"));
		mHead.setBackgroundResource(R.drawable.sheet_detail_head_bg);
		mHead.setOnTouchListener(new ListViewAndHeadViewTouchLinstener());
		mField = (LinearLayout) findViewById(R.id.field);
		TextView tvBtn = (TextView) findViewById(R.id.field_tv_options);
		tvBtn.setVisibility(View.INVISIBLE);
		// 钢板列表
		sheetPullRefreshListView = (PullToRefreshListView) findViewById(R.id.sheet_detail_listview);
		sheetPullRefreshListView.setOnRefreshListener(this);
		sheetPullRefreshListView.setMode(PullToRefreshBase.Mode.BOTH);
		// sheetPullRefreshListView.setMode(PullToRefreshBase.Mode.BOTH);
		sheetListView = sheetPullRefreshListView.getRefreshableView();
		// Need to use the Actual ListView when registering for Context Menu
		registerForContextMenu(sheetListView);
		if (mTotal == 0) {
			TextView tv = new TextView(this);
			tv.setGravity(Gravity.CENTER);
			tv.setText("没有钢板数据，试试下拉刷新");
			sheetPullRefreshListView.setEmptyView(tv);
		} else {
			sheetPullRefreshListView.removeEmptyView();
		}
		// 底边控件显示
		sheetDetailEt = (EditText) findViewById(R.id.sheet_detail_et);
		sheetListView
				.setOnTouchListener(new ListViewAndHeadViewTouchLinstener());
	}

	/**
	 * 加载请求钢板信息数据
	 */
	private void loadExtras() {
		Bundle bundle = getIntent().getExtras();
		if (null != bundle) {
			if ("FSK".equals(bundle.getString("curStoreType"))) {
				setTopTitle(R.string.sheet_detail);
			} else {
				setTopTitle(R.string.remnant_detail);
			}
			fromStorehouse = (StoreHouse.ListClass) bundle
					.getSerializable("curSelctStore");
			fromStack = (Stack.ListClass) bundle
					.getSerializable("curSelctStack");
			barCode = bundle.getString("barCode");
			material = bundle.getString("material");
			thickness = bundle.getString("thickness");
			length = bundle.getString("length");
			width = bundle.getString("width");
			inTime = bundle.getString("time");
			project = bundle.getString("project");
			assemblyNo = bundle.getString("assemblyNo");

			mLog.w(fromStack.getSTACKNAME() + "||" + fromStack.getSTACKNO());

			sheetStackCodeTv = fromStorehouse.getNAME();
			sheetStockCodeTv = fromStack.getSTACKNAME();

			sheetHeightTv = fromStack.getSumThickness() + "mm";
			sheetNumTv = (int) fromStack.getSumAmount()
					+ getString(R.string.steel_unit);

			// 判断是否是高亮的垛位
			if (bundle.getBoolean("isQuery")) {
				// myNetwork(SmsApi.getInstance().API_ALLSHEETLIST_BYSTACKNO,
				// barCode, material, thickness, length,
				// width, project, assemblyNo, inTime);
				transferArg = "isQuery";
				myNetwork(ServerApi.getInstance().API_GETTABLEHEAD);

			} else {
				myNetwork(ServerApi.getInstance().API_GETTABLEHEAD);
			}
			myNetwork(ServerApi.getInstance().API_GET_STACKSETMSG,
					fromStack.getSTACKNO());
		} else {
			mLog.w("getIntent().getExtras() bundle == null");
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		super.onClick(v);
	}

	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
		// TODO Auto-generated method stub
		mLog.w("onPullDownToRefresh");
		sheetDetailAdapter = null;
		mCurrentNumber = 1;
		fieldList.clear();
		field_valueList.clear();
		sList.clear();
		allList.clear();
		myNetwork(ServerApi.getInstance().API_GETTABLEHEAD);
		myNetwork(ServerApi.getInstance().API_GET_STACKSETMSG,
				fromStack.getSTACKNO());
		sheetPullRefreshListView.setMode(PullToRefreshBase.Mode.BOTH);
	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
		// TODO Auto-generated method stub
		mLog.w("onPullUpToRefresh");
		fieldList.clear();
		myNetwork(ServerApi.getInstance().API_GETTABLEHEAD);
	}

	@Override
	public void myNetwork(String tag, String... txt) {
		// TODO Auto-generated method stub
		super.myNetwork(tag, txt);
		mProgressNumber++;
		if (mProgressNumber == 1) {
			showProgressDialog("加载中", false, false);
		}
		JSONObject json = getRequstJson();
		if (ServerApi.getInstance().SHEET_GETSHEETLIST.equals(tag)) {
			// 获取垛位中钢板
			try {
				json.put("StackNo", fromStack.getSTACKNO());// 货位号
				// 排序字段(desc是descend 降序, asc是ascend 升序)
				json.put("pCOLUMN", "ItemNo desc");
				json.put("deleteFlag", "0");// 删除分区(因为是pcpad共用接口需要传值 0:未删除,1:删除)
				json.put("from", txt[0]);// 从符合条件的第from开始的结果
				json.put("perPage", mPageSize);// 每页返回的记录数
				sendPOST(tag, json, null, this, tag, false);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		} else if (ServerApi.getInstance().API_ALLSHEETLIST_BYSTACKNO
				.equals(tag)) {
			// 查询钢板
			try {
				json.put("Flag", "2");
				json.put("StackNo", fromStack.getSTACKNO());
				if (null != barCode && barCode.length() > 0) {
					json.put("Barcode", barCode);
				}
				if (material.length() > 0) {
					json.put("Material", material);
				}
				if (thickness.length() > 0) {
					json.put("Thickness", thickness);
				}
				if (length.length() > 0) {
					json.put("Length", length);
				}
				if (width.length() > 0) {
					json.put("Width", width);
				}
				if (null != inTime && inTime.length() > 0) {
					json.put("InTime", inTime);
				}
				if (project.length() > 0) {
					json.put("Project", project);
				}
				if (null != assemblyNo && assemblyNo.length() > 0) {
					json.put("assemblyNo", assemblyNo);
				}
				// json.put("pSortField", "ItemNo desc");// 按层数降序
				sendPOST(tag, json, null, this, tag, false);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		} else if (ServerApi.getInstance().ORDER_UPDATEBARCODE.equals(tag)) {
			// 更换条形码
			try {
				json.put("sheetId", txt[0]);
				json.put("barCode", txt[1]);
				// json.put("perPage", "9999");// 每页返回的记录数
				sendPOST(tag, json, null, this, tag, false);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		} else if (ServerApi.getInstance().API_UPDATASHEETRUST.equals(tag)) {
			// 修改锈蚀状态0:未锈蚀，1:已锈蚀
			try {
				json.put("pSheetId", txt[0]);
				json.put("pSheetRust", Integer.parseInt(txt[1]));
				// json.put("perPage", "9999");// 每页返回的记录数
				sendPOST(tag, json, null, this, tag, false);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		} else if (ServerApi.getInstance().API_GET_STACKSETMSG.equals(tag)) {
			// 查询垛位自定义信息
			try {
				json.put("pStackId", txt[0]);
				json.put("perPage", "9999");// 每页返回的记录数
				sendPOST(tag, json, null, this, tag, false);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		} else if (ServerApi.getInstance().API_GETTABLEHEAD.equals(tag)) {
			try {
				json = getRequstJson();
				json.put("pFACTORYCODE", ServerApi.factoryCode);
				json.put("page", ServerApi.CONFIGURE_SHEET);
				json.put("lang", ServerApi.LANG);
				sendPOST(tag, json, null, this, tag, false);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		} else {
			// showShortToast("错误的网络请求，请检查");
		}
	}

	@Override
	public void onRequestFail(String str, Exception ex) {
		// TODO Auto-generated method stub
		// super.onRequestFail(str, ex);
		if (mProgressNumber > 0) {
			mProgressNumber--;
		}
		if (mProgressNumber == 0) {
			dismissProgressDialog();
		}
	}

	@Override
	public void onRequestSuccess(String tag, Object obj) {
		// TODO Auto-generated method stub
		// super.onRequestSuccess(tag, obj);
		if (mProgressNumber > 0) {
			mProgressNumber--;
		}
		if (mProgressNumber == 0) {
			dismissProgressDialog();
		}
		Gson gson = new Gson();
		mLog.init(tag, TAG_LOG);
		mLog.i(TAG_LOG + obj.toString());
		if (ServerApi.getInstance().SHEET_GETSHEETLIST.equals(tag)) {
			// 获取垛位中钢板
			Sheet st = gson.fromJson(obj.toString(), Sheet.class);
			if (st.getResultCode() == 0) {
				// if (null == sheetAdapter) {
				try {
					for (int j = 0; j < ((JSONObject) obj).getJSONArray("list")
							.length(); j++) {
						field_valueList.add(QueryBillActivity.getValidDate(
								((JSONObject) obj).getJSONArray("list").get(j),
								fieldList));
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				sList.addAll(st.getList());
				sheetDetailAdapter = new SheetDetailAdapter(this, sList,
						field_valueList);
				sheetListView.setAdapter(sheetDetailAdapter);
				mTotal = st.getTotal();
				++mCurrentNumber;
				sheetPullRefreshListView.onRefreshComplete();
				if (field_valueList.size() == mTotal) {
					showShortToast("数据全部加载完成，没有更多数据");
					sheetPullRefreshListView
							.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
				}
			} else {
				showShortToast("resultCode==" + st.getResultCode() + "，请联系开发人员");
			}
			// Call onRefreshComplete when the list has been refreshed.

		} else if (ServerApi.getInstance().API_ALLSHEETLIST_BYSTACKNO
				.equals(tag)) {
			// 获取合情钢板
			Sheet st = gson.fromJson(obj.toString(), Sheet.class);
			if (st.getResultCode() == 0) {
				for (Sheet.ListClass sheet : st.getList()) {
					// 符合查询条件的钢板
					sheet.setChecked(true);
				}

				allList.addAll(st.getList());
				allList.addAll(st.getAlllist());
				// 按层号降序排序(用Integer类型的对象排序)
				Collections.sort(allList, new Comparator<Sheet.ListClass>() {

					@Override
					public int compare(Sheet.ListClass rhs, Sheet.ListClass lhs) {
						// TODO Auto-generated method stub
						return Integer.valueOf(lhs.getItemNo()).compareTo(
								rhs.getItemNo());
					}
				});
				for (int i = 0; i < allList.size(); i++) {
					try {
						JSONObject jo = new JSONObject(gson.toJson(
								allList.get(i), Sheet.ListClass.class));
						field_valueList.add(QueryBillActivity.getValidDate(jo,
								fieldList));
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				sheetDetailAdapter = new SheetDetailAdapter(this, allList,
						field_valueList);
				sheetListView.setAdapter(sheetDetailAdapter);
				sheetPullRefreshListView.onRefreshComplete();
				sheetPullRefreshListView
						.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
			} else {
				showShortToast("resultCode==" + st.getResultCode() + "，请联系开发人员");
			}
		} else if (ServerApi.getInstance().ORDER_UPDATEBARCODE.equals(tag)) {
			// 更换条码
			BaseModel bm = gson.fromJson(obj.toString(), BaseModel.class);
			if (bm.getResultCode() == 0) {
				myNetwork(ServerApi.getInstance().API_GETTABLEHEAD);
			} else if (bm.getResultCode() == 1001) {
				showShortToast("正在盘点，请勿更改条码");
			} else if (bm.getResultCode() == 1002) {
				showShortToast("条形码已存在!");
			} else {
				showShortToast("resultCode==" + bm.getResultCode() + "，请联系开发人员");
			}
		} else if (ServerApi.getInstance().API_UPDATASHEETRUST.equals(tag)) {
			BaseModel bm = gson.fromJson(obj.toString(), BaseModel.class);
			if (bm.getResultCode() == 0) {
				// 刷新钢板
				sheetDetailAdapter.notifyDataSetChanged();
			} else {
				showShortToast("resultCode==" + bm.getResultCode() + "，请联系开发人员");
			}
		} else if (ServerApi.getInstance().API_GET_STACKSETMSG.equals(tag)) {
			// 获取垛位说明信息
			StackExplain stackExplain = gson.fromJson(obj.toString(),
					StackExplain.class);
			if (stackExplain.getResultCode() == 0) {
				stackMsgList = StackExplain.stackMsg(stackExplain.getListSet(),
						stackExplain.getStackInfo());
				StringBuffer sb = new StringBuffer();
				for (int i = 0; i < stackMsgList.size(); i++) {
					sb.append("【");
					sb.append(stackMsgList.get(i).getDescription());
					sb.append(":");
					sb.append(stackMsgList.get(i).getValue());
					sb.append("】");
				}
				sheetDetailEt.setText(sb.toString());
			}
		} else if (ServerApi.getInstance().API_GETTABLEHEAD.equals(tag)) {
			ConfigFieldEntity cfEntity = gson.fromJson(obj.toString(),
					ConfigFieldEntity.class);
			if (cfEntity.getResultCode() == 0) {
				fieldList = cfEntity.getList();
				addView(mField, fieldList, 0);
				if ("isQuery".equals(transferArg)) {
					myNetwork(ServerApi.getInstance().API_ALLSHEETLIST_BYSTACKNO);
				} else {
					myNetwork(ServerApi.getInstance().SHEET_GETSHEETLIST,
							mCurrentNumber + "");
				}
			}
		}
	}

	public void addView(final LinearLayout lineLayout,
			List<ConfigFieldEntity.Segment> list, int Flag) {
		lineLayout.removeAllViews();
		for (int i = 0; i < list.size(); i++) {
			// 分割线
			TextView showLine = new TextView(this);
			showLine.setBackgroundColor(getResources().getColor(R.color.white));
			// set 文本大小
			LinearLayout.LayoutParams pa = new LinearLayout.LayoutParams(2,
					LayoutParams.MATCH_PARENT);
			showLine.setLayoutParams(pa);
			lineLayout.addView(showLine);
			TextView showText = new TextView(this);
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

		}
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

	/**
	 * 钢板适配器
	 */
	private class SheetDetailAdapter extends BaseAdapter {
		private LayoutInflater mInflater;
		private List<Sheet.ListClass> list;
		private List<ArrayList<ConfigFieldEntity.Segment>> sheetList;
		TextView sheetOptionsBtn;

		public List<Sheet.ListClass> getList() {
			return list;
		}

		public SheetDetailAdapter(Context context, List<Sheet.ListClass> list,
				List<ArrayList<ConfigFieldEntity.Segment>> sheetList) {
			this.list = list;
			this.sheetList = sheetList;
			this.mInflater = LayoutInflater.from(context);
		}

		/** 删除0角标至location角标元素 */
		public void removeToP(int location) {
			for (int i = 0; i < location; i++) {
				list.remove(i);
			}
		}

		@Override
		public int getCount() {
			return sheetList != null ? sheetList.size() : 0;
		}

		@Override
		public Sheet.ListClass getItem(int position) {
			return list.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			synchronized (SheetDetailActivity.this) {

				convertView = mInflater.inflate(
						R.layout.item_scrollview_querybill, null);
				MyHScrollView scrollView1 = (MyHScrollView) convertView
						.findViewById(R.id.horizontalScrollView1);
				LinearLayout content = (LinearLayout) convertView
						.findViewById(R.id.lv_field);
				sheetOptionsBtn = (TextView) convertView
						.findViewById(R.id.sheetitem_tv_options);
				sheetOptionsBtn.setVisibility(View.VISIBLE);
				addView(content, sheetList.get(position), 1);

				MyHScrollView headSrcrollView = (MyHScrollView) mHead
						.findViewById(R.id.horizontalScrollView1);
				headSrcrollView
						.AddOnScrollChangedListener(new OnScrollChangedListenerImp(
								scrollView1));
			}
			Sheet.ListClass sheet = list.get(position);
			if (sheet.isChecked()) {
				convertView
						.setBackgroundResource(R.drawable.sheet_detail_list_item_selected_bg);
			} else if (sheet.getISRUST() == 0) {
				convertView
						.setBackgroundResource(R.drawable.sheet_detail_list_item_bg);
			} else {
				convertView
						.setBackgroundResource(R.drawable.sheet_detail_head_bg);
			}

			// 选项按钮
			sheetOptionsBtn.setOnClickListener(click);
			sheetOptionsBtn.setTag(sheet);
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

		// 按钮点击事件
		private View.OnClickListener click = new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Sheet.ListClass sheet = (Sheet.ListClass) v.getTag();
				Intent intent = new Intent();
				Bundle bundle = new Bundle();
				switch (v.getId()) {
				// 选项按钮
				case R.id.sheetitem_tv_options:
					showOptionsDialog(sheet);
					break;
				// 详情按钮
				case R.id.total_tv_sheetdetail:
					showDetailDialog(sheet);

					closeDialog(optionsDialog);
					break;
				// 倒垛按钮
				case R.id.total_tv_sheettosp:
					// 传递需要倒垛的库房类型、库房编号、库房名称、垛位编号、垛位名称
					int num = list.indexOf(sheet);
					double weight = 0.0;
					for (int i = 0; i < list.indexOf(sheet) + 1; i++) {
						mLog.e("倒垛钢板：" + "i---->" + list.get(i).getBarcode()
								+ "--->层号：->" + list.get(i).getItemNo());
						weight += Double.parseDouble(list.get(i).getWeight());
					}
					bundle.putInt("num", num);// 需要倒垛的钢板张数
					bundle.putDouble("weight", weight);// 需要倒垛的钢板重量
					bundle.putSerializable("curSelctStore", fromStorehouse);
					bundle.putSerializable("curSelctStack", fromStack);
					bundle.putString("fromActivity", TAG_LOG);
					intent.putExtras(bundle);
					intent.setClass(SheetDetailActivity.this,
							ShiftActivity.class);
					startActivityForResult(intent, 1000);
					closeDialog(optionsDialog);
					break;
				// 锈蚀/取消锈蚀按钮
				case R.id.total_tv_sheetrust: {
					if (sheet.getISRUST() == 0) {
						sheet.setISRUST(1);
						myNetwork(ServerApi.getInstance().API_UPDATASHEETRUST,
								sheet.getMetalBillId(),
								Integer.toString(sheet.getISRUST()));
					} else if (sheet.getISRUST() == 1) {
						sheet.setISRUST(0);
						myNetwork(ServerApi.getInstance().API_UPDATASHEETRUST,
								sheet.getMetalBillId(),
								Integer.toString(sheet.getISRUST()));
					}
					closeDialog(optionsDialog);
				}
					break;
				// 特请出库(功能隐藏)
				case R.id.total_tv_outstock:
					break;
				// 更换条码
				case R.id.total_tv_upbarcode:
					closeDialog(optionsDialog);

					bundle.putSerializable("sheet", sheet);
					bundle.putString("FLAG", TAG_LOG);
					bundle.putString("title", "更换条码");
					intent.putExtras(bundle);
					intent.setClass(SheetDetailActivity.this, ScanDialog.class);
					startActivityForResult(intent, 1000);
					break;
				default:
					break;
				}
			}
		};

		/**
		 * 显示选项弹出对话框
		 */
		private void showOptionsDialog(final Sheet.ListClass sheet) {
			View view = mInflater.inflate(R.layout.dialog_option_total, null);
			TextView sheetDetailTv = (TextView) view
					.findViewById(R.id.total_tv_sheetdetail);
			TextView sheetTospTv = (TextView) view
					.findViewById(R.id.total_tv_sheettosp);
			Spinner sheetRustSp = (Spinner) view
					.findViewById(R.id.total_sp_sheetrust);
			sheetRustSp.setSelection(sheet.getISRUST());
			sheetRustSp.setOnItemSelectedListener(new OnItemSelectedListener() {

				@Override
				public void onItemSelected(AdapterView<?> parent, View view,
						int position, long id) {
					// TODO Auto-generated method stub
					String[] sheetrust = getResources().getStringArray(
							R.array.sheetrust);
					sheet.setISRUST(position);
					myNetwork(ServerApi.getInstance().API_UPDATASHEETRUST,
							sheet.getMetalBillId(),
							Integer.toString(sheet.getISRUST()));
				}

				@Override
				public void onNothingSelected(AdapterView<?> parent) {
					// TODO Auto-generated method stub

				}
			});
			TextView sheetRustTv = (TextView) view
					.findViewById(R.id.total_tv_sheetrust);
			TextView pleaseOutStockTv = (TextView) view
					.findViewById(R.id.total_tv_outstock);
			TextView replacebarcodeTv = (TextView) view
					.findViewById(R.id.total_tv_upbarcode);
			// 钢板详情按钮
			sheetDetailTv.setOnClickListener(click);
			sheetDetailTv.setTag(sheet);
			// 倒垛按钮
			sheetTospTv.setOnClickListener(click);
			sheetTospTv.setTag(sheet);
			// 锈蚀/取消锈蚀按钮
			sheetRustTv.setOnClickListener(click);
			sheetRustTv.setTag(sheet);
			if (sheet.getISRUST() == 0) {
				sheetRustTv.setText(R.string.sheet_rust);
			} else if (sheet.getISRUST() == 1) {
				sheetRustTv.setText(R.string.sheet_cancelrust);
			}
			// 特请出库按钮
			pleaseOutStockTv.setOnClickListener(click);
			// 更换条码按钮
			replacebarcodeTv.setOnClickListener(click);
			replacebarcodeTv.setTag(sheet);
			optionsDialog = getDialog(SheetDetailActivity.this, view);
			showDialog(optionsDialog);
		}

		/**
		 * 显示详情弹出对话框
		 */
		private void showDetailDialog(Sheet.ListClass sheet) {
			View view = mInflater.inflate(R.layout.dialog_sheetdetail, null);
			((TextView) view.findViewById(R.id.sheetdetail_tv_stackno))
					.setText(sheet.getStackName());
			((TextView) view.findViewById(R.id.sheetdetail_tv_floorno))
					.setText(sheet.getItemNo() + "");
			((TextView) view.findViewById(R.id.sheetdetail_tv_sheetno))
					.setText(sheet.getMetalBillId());
			((TextView) view.findViewById(R.id.sheetdetail_tv_barcode))
					.setText(sheet.getBarcode());
			((TextView) view.findViewById(R.id.sheetdetail_tv_sheetname))
					.setText(sheet.getSHEETNAME());
			((TextView) view.findViewById(R.id.sheetdetail_tv_material))
					.setText(sheet.getMaterial());
			((TextView) view.findViewById(R.id.sheetdetail_tv_thickness))
					.setText(sheet.getThickness() + "");
			((TextView) view.findViewById(R.id.sheetdetail_tv_length))
					.setText(sheet.getLength() + "");
			((TextView) view.findViewById(R.id.sheetdetail_tv_width))
					.setText(sheet.getWidth() + "");
			// 重量
			((TextView) view.findViewById(R.id.sheetdetail_tv_weight))
					.setText(sheet.getWeight() + "KG");
			// 数量
			((TextView) view.findViewById(R.id.sheetdetail_tv_quantity))
					.setText(sheet.getAmount() + "");
			// 物料编号
			((TextView) view.findViewById(R.id.sheetdetail_tv_materialno))
					.setText(sheet.getMaterialId());
			((TextView) view.findViewById(R.id.sheetdetail_tv_materialname))
					.setText(sheet.getCodeName());
			// 供应商
			((TextView) view.findViewById(R.id.sheetdetail_tv_supplier))
					.setText(sheet.getSUPPLIER());
			// 炉批号
			((TextView) view.findViewById(R.id.sheetdetail_tv_batchnum))
					.setText(sheet.getBatchNo());
			// 提检号
			((TextView) view.findViewById(R.id.sheetdetail_tv_checkoutid))
					.setText(sheet.getCheckOutId());
			// 规格
			((TextView) view.findViewById(R.id.sheetdetail_tv_specifications))
					.setText(sheet.getThickness() + "*" + sheet.getLength()
							+ "*" + sheet.getWidth());
			// 工程号
			((TextView) view.findViewById(R.id.sheetdetail_tv_projectno))
					.setText(sheet.getProjectId());
			// 分段号
			((TextView) view.findViewById(R.id.sheetdetail_tv_AssemblyId))
					.setText(sheet.getAssemblyId());
			// 入库时间
			((TextView) view.findViewById(R.id.sheetdetail_tv_intime))
					.setText(sheet.getInTime());
			detailDialog = getDialog(SheetDetailActivity.this, view);
			showDialog(detailDialog);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		mLog.d(TAG_LOG + "run onActivityResult() !");
		if (requestCode == 1000 && resultCode == 1011) {
			// 获取垛位钢板
			sheetDetailAdapter = null;
			mCurrentNumber = 1;
			fieldList.clear();
			field_valueList.clear();
			sList.clear();
			allList.clear();
			myNetwork(ServerApi.getInstance().API_GETTABLEHEAD);
			myNetwork(ServerApi.getInstance().API_GET_STACKSETMSG,
					fromStack.getSTACKNO());
			closeDialog(optionsDialog);
		} else if (requestCode == 1000 && resultCode == 1012) {
			// 更换条形码
			myNetwork(ServerApi.getInstance().ORDER_UPDATEBARCODE,
					data.getStringExtra("sheetid"),
					data.getStringExtra("barcode"));
		}
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

}

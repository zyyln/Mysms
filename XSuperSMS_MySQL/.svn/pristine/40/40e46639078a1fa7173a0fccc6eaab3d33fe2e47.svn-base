package com.xuesi.sms.app.activity;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.xuesi.mos.device.Screen;
import com.xuesi.mos.libs.pulltorefresh.PullToRefreshBase;
import com.xuesi.mos.libs.pulltorefresh.PullToRefreshGridView;
import com.xuesi.mos.util.MosLog;
import com.xuesi.sms.R;
import com.xuesi.sms.ServerApi;
import com.xuesi.sms.SmsApplication;
import com.xuesi.sms.bean.BaseModel;
import com.xuesi.sms.bean.BaseVo;
import com.xuesi.sms.bean.Stack;
import com.xuesi.sms.bean.StoreHouse;
import com.xuesi.sms.widget.adapter.StockCheckAdapter;
import com.xuesi.sms.widget.adapter.StoreAdapter;

/**
 * <p>
 * Title:库存盘点
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
public class CheckActivity extends SmsActivity implements
		AdapterView.OnItemClickListener, RadioGroup.OnCheckedChangeListener,
		PullToRefreshBase.OnRefreshListener2<GridView> {
	/** LOG */
	private final String LOG_TAG = CheckActivity.class.getSimpleName();
	private final MosLog mLog = MosLog.getInstance(LOG_TAG, "XUESI-SMS");
	/** progress的次数 */
	private int mProgressNumber = 0;

	// 一级库、二级库、余料库单选按钮
	private RadioGroup selctHouseType;
	private String stockType = "FSK";

	// 库房下拉框------
	/** 选择库房弹出框 */
	private PopupWindow storePopWin;
	private LinearLayout storeLinear;
	/** */
	private TextView HouseLeftTv, stockNoTv;
	/** 弹出框视图 */
	private View stockPopView;
	/** 下拉选择库房列表 */
	private ListView storeListView;
	/** 选择仓库适配器 */
	private StoreAdapter storeAdapter;
	/** 当前选中的库房 */
	private StoreHouse.ListClass curSelctStore;

	// 货位组件------
	/** 下拉刷新控件 */
	private PullToRefreshGridView stackPullRefreshGridView;
	/** 货位列表 */
	private GridView stackGridView;
	/** 垛位信息适配器 */
	private StockCheckAdapter stockAdapter;
	/** 当前选中垛位 */
	private Stack.ListClass curSelctStack;

	/** 开始盘点， 结束盘点 */
	private Button beginCheckBtn, endCheckBtn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		SmsApplication.getInstance().addActivity(this);// 添加到Activity堆栈
		setContentView(R.layout.activity_check);
		// 初始化界面
		myNetwork(ServerApi.getInstance().API_GET_STORE_NAME, stockType);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		myNetwork(ServerApi.getInstance().SHEET_GETISLOCK, stockType);
	}

	@Override
	protected void initContentView() {
		// TODO Auto-generated method stub
		super.initContentView();
		setTopTitle(R.string.storage_check);
		setRefreshView(View.INVISIBLE);

		// 选择库房组合框
		storeLinear = (LinearLayout) findViewById(R.id.housestore_linear);
		HouseLeftTv = (TextView) findViewById(R.id.housestore_tv_left);
		stockNoTv = (TextView) findViewById(R.id.housestore_tv_name);
		findViewById(R.id.housestore_btn_arrow).setOnClickListener(this);

		// 选择库房弹出部分
		stockPopView = getLayoutInflater().inflate(R.layout.popup_storehouse,
				null);
		storeListView = (ListView) stockPopView
				.findViewById(R.id.popup_lv_houseName);
		storeListView.setOnItemClickListener(this);

		// 垛位列表项
		stackPullRefreshGridView = (PullToRefreshGridView) findViewById(R.id.include_gv_stack);
		stackPullRefreshGridView.setOnRefreshListener(this);
		// 设置模式为只能下拉刷新
		stackPullRefreshGridView
				.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
		stackGridView = stackPullRefreshGridView.getRefreshableView();
		stackGridView.setOnItemClickListener(this);

		// 开始,结束盘点按钮
		beginCheckBtn = (Button) findViewById(R.id.start_check_btn);
		beginCheckBtn.setOnClickListener(this);
		endCheckBtn = (Button) findViewById(R.id.end_check_btn);
		endCheckBtn.setOnClickListener(this);

		// 库房类项
		selctHouseType = (RadioGroup) findViewById(R.id.stock_bound_group);
		selctHouseType.setOnCheckedChangeListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		super.onClick(v);
		switch (v.getId()) {
		// 选择仓库
		case R.id.housestore_btn_arrow: {
			myNetwork(ServerApi.getInstance().API_GET_STORE_NAME, stockType);
		}
			break;
		// 开始盘点
		case R.id.start_check_btn: {
			if (null != stockAdapter && stockAdapter.getCount() > 0) {
				new AlertDialog.Builder(this)
						.setTitle(R.string.prompt)
						.setMessage("是否开始盘点？")
						.setPositiveButton(R.string.confirm,
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int which) {
										myNetwork(ServerApi.getInstance().API_ADDSHEETDETAILPD);
									}
								}).setNegativeButton(R.string.cancel, null)
						.setCancelable(true).show();
			} else {
				showShortToast("没有库房，无法盘点!");
			}
		}
			break;
		// 结束盘点
		case R.id.end_check_btn: {
			new AlertDialog.Builder(this)
					.setTitle(R.string.prompt)
					.setMessage("是否结束盘点？")
					.setPositiveButton(R.string.confirm,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									myNetwork(ServerApi.getInstance().API_STOCK_CHECK_END);
								}
							}).setNegativeButton(R.string.cancel, null)
					.setCancelable(true).show();
		}
			break;
		default:
			break;
		}
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		// TODO Auto-generated method stub
		if (group == selctHouseType) {
			if (checkedId == R.id.stock_senior_radio) {
				stockType = "FSK";
			} else if (checkedId == R.id.stock_sub_radio) {
				stockType = "SCK";
			} else {
				stockType = "RCK";
			}

			// 切换库房类型后,默认选中顺位一号库房
			curSelctStore = null;
			myNetwork(ServerApi.getInstance().API_GET_STORE_NAME, stockType);
			// 更新当前库的盘点状态
			myNetwork(ServerApi.getInstance().SHEET_GETISLOCK, stockType);
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		// 库房列表
		if (parent == storeListView) {
			curSelctStore = storeAdapter.getItem(position);
			stockNoTv.setText(curSelctStore.getNAME());
			myNetwork(ServerApi.getInstance().API_GET_STOCK_CKECK_LIST);
			if (storePopWin.isShowing()) {
				storePopWin.dismiss();
			}
		}
		// 垛位列表
		if (parent == stackGridView) {
			if (endCheckBtn.getVisibility() == View.VISIBLE) {
				curSelctStack = stockAdapter.getItem(position);
				Intent intent = new Intent();
				// 传递需要的当前库房、当前垛位
				Bundle bundle = new Bundle();
				bundle.putSerializable("curSelctStore", curSelctStore);
				bundle.putSerializable("curSelctStack", curSelctStack);
				intent.putExtras(bundle);
				intent.setClass(CheckActivity.this, CheckDetailActivity.class);
				startActivityForResult(intent, 1000);
			} else {
				showShortToast("请开始盘点");
			}
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onPullDownToRefresh(PullToRefreshBase<GridView> refreshView) {
		// 取消弹出加载对话框
		mProgressNumber = 1;
		myNetwork(ServerApi.getInstance().API_GET_STOCK_CKECK_LIST);
	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase<GridView> refreshView) {
		// TODO Auto-generated method stub
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
		if (ServerApi.getInstance().SHEET_GETISLOCK.equals(tag)) {
			// 判断是否盘点
			try {
				json.put("type", stockType);
				sendPOST(tag, json, null, this, tag, false);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		} else if (ServerApi.getInstance().API_GET_STORE_NAME.equals(tag)) {
			// 获取库房
			try {
				json.put("pStoreType", stockType);
				sendPOST(tag, json, null, this, tag, false);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		} else if (ServerApi.getInstance().API_GET_STOCK_CKECK_LIST.equals(tag)) {
			// 查询货位一览
			try {
				if (null != curSelctStore) {
					json.put("pStorehouseId", curSelctStore.getID());
				} else {
					json.put("pStorehouseId", "-1");
				}
				sendPOST(tag, json, null, this, tag, false);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		} else if (ServerApi.getInstance().API_ADDSHEETDETAILPD.equals(tag)) {
			// 开始盘点
			try {
				json.put("pPDDAYCODE", stockType);
				json.put("pCREATER", ServerApi.account);
				sendPOST(tag, json, null, this, tag, false);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		} else if (ServerApi.getInstance().API_STOCK_CHECK_END.equals(tag)) {
			// 结束盘点
			try {
				json.put("pStoreType", stockType);
				sendPOST(tag, json, null, this, tag, false);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		} else {
			showShortToast("错误的网络请求，请检查");
		}
	}

	@Override
	public void onRequestFail(String str, Exception ex) {
		// TODO Auto-generated method stub
		super.onRequestFail(str, ex);
		if (mProgressNumber > 0) {
			mProgressNumber--;
		}
		if (mProgressNumber == 0) {
			dismissProgressDialog();
		}
	}

	@Override
	public void onRequestSuccess(String str, Object obj) {
		// TODO Auto-generated method stub
		super.onRequestSuccess(str, obj);
		if (mProgressNumber > 0) {
			mProgressNumber--;
		}
		if (mProgressNumber == 0) {
			dismissProgressDialog();
		}
		Gson gson = new Gson();
		mLog.init(str, LOG_TAG);
		mLog.i(LOG_TAG + obj.toString());
		if (ServerApi.getInstance().SHEET_GETISLOCK.equals(str)) {
			// 判断是否盘点
			BaseVo bv = gson.fromJson(obj.toString(), BaseVo.class);
			if (bv.getResultCode() == 0) {
				if (bv.getTotal() > 0) {// 处于盘点状态
					beginCheckBtn.setVisibility(View.GONE);
					endCheckBtn.setVisibility(View.VISIBLE);
				} else {// 不在盘点状态
					beginCheckBtn.setVisibility(View.VISIBLE);
					endCheckBtn.setVisibility(View.GONE);
				}
			} else {
				showShortToast("resultCode==" + bv.getResultCode() + "，请联系开发人员");
			}
		} else if (ServerApi.getInstance().API_GET_STORE_NAME.equals(str)) {
			// 获取库房
			StoreHouse storeHouse = gson.fromJson(obj.toString(),
					StoreHouse.class);
			if (storeHouse.getResultCode() == 0) {
				storeAdapter = new StoreAdapter(this, storeHouse.getList());
				storeListView.setAdapter(storeAdapter);
				if (storeAdapter.getCount() <= 0) {
					stockNoTv.setText("");
					showPromptDialog(this, "prompt", R.string.msg_createstore);
				} else if (null == curSelctStore) {
					// 初始化页面
					curSelctStore = storeAdapter.getItem(0);
					stockNoTv.setText(curSelctStore.getNAME());
					myNetwork(ServerApi.getInstance().API_GET_STOCK_CKECK_LIST);
				} else {
					showSelectHousePopup();
				}
			} else {
				showShortToast("resultCode==" + storeHouse.getResultCode()
						+ "，请联系开发人员");
			}
		} else if (ServerApi.getInstance().API_GET_STOCK_CKECK_LIST.equals(str)) {
			// 垛位一览
			Stack stack = gson.fromJson(obj.toString(), Stack.class);
			stackPullRefreshGridView.onRefreshComplete();
			if (stack.getResultCode() == 0) {
				stockAdapter = new StockCheckAdapter(this, stack.getList(),
						stockType);
				stackGridView.setAdapter(stockAdapter);
				if (stack.getList().size() == 0) {
					TextView tv = new TextView(this);
					tv.setGravity(Gravity.CENTER);
					tv.setText("没有垛位数据，请先查看库房");
					stackPullRefreshGridView.setEmptyView(tv);
				} else {
					stackPullRefreshGridView.removeEmptyView();
				}
			} else {
				showShortToast("resultCode==" + stack.getResultCode()
						+ "，请联系开发人员");
			}
		} else if (ServerApi.getInstance().API_ADDSHEETDETAILPD.equals(str)) {
			// 开始盘点,空库房可以盘点
			BaseModel bm = gson.fromJson(obj.toString(), BaseModel.class);
			if (bm.getResultCode() == 0) {
				beginCheckBtn.setVisibility(View.GONE);
				endCheckBtn.setVisibility(View.VISIBLE);
			} else {
				showShortToast("resultCode==" + bm.getResultCode() + "，请联系开发人员");
			}
		} else if (ServerApi.getInstance().API_STOCK_CHECK_END.equals(str)) {
			// 结束盘点
			BaseModel bm = gson.fromJson(obj.toString(), BaseModel.class);
			if (bm.getResultCode() == 0) {
				beginCheckBtn.setVisibility(View.VISIBLE);
				endCheckBtn.setVisibility(View.GONE);

				finish();
			} else {
				showShortToast("resultCode==" + bm.getResultCode() + "，请联系开发人员");
			}
		}
	}

	/**
	 * 显示选择仓库弹出框
	 */
	private void showSelectHousePopup() {
		if (null != storePopWin && storePopWin.isShowing()) {
			storePopWin.dismiss();
		}
		int width = storeLinear.getWidth();
		storePopWin = new PopupWindow(stockPopView, width,
				ViewGroup.LayoutParams.WRAP_CONTENT);
		storePopWin.setFocusable(true);
		storePopWin.setOutsideTouchable(true);
		// storePopWin.setBackgroundDrawable(new BitmapDrawable());
		storePopWin.setAnimationStyle(R.style.AnimTop2);
		if (storeAdapter.getCount() > 5) {
			storePopWin
					.setHeight(Screen.getInstance().getScreenHeight() * 4 / 10);
		}
		if (storePopWin.isShowing()) {
			storePopWin.dismiss();
		} else {
			int[] position = new int[2];
			HouseLeftTv.getLocationOnScreen(position);
			int height = storeLinear.getHeight();
			int y = position[1] + height;
			// 在指定位置显示
			storePopWin.showAtLocation(stockNoTv, Gravity.LEFT | Gravity.TOP,
					position[0], y);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 1000 && resultCode == 1011) {// 刷新界面
			// mLog.d(TAG_LOG + "run onActivityResult() !");
			if (null != curSelctStore) {
				myNetwork(ServerApi.getInstance().API_GET_STOCK_CKECK_LIST);
			}
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

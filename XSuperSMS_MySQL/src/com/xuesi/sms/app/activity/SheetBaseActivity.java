package com.xuesi.sms.app.activity;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.google.gson.Gson;
import com.xuesi.mos.device.Screen;
import com.xuesi.mos.libs.pulltorefresh.PullToRefreshBase;
import com.xuesi.mos.libs.pulltorefresh.PullToRefreshBase.Mode;
import com.xuesi.mos.libs.pulltorefresh.PullToRefreshGridView;
import com.xuesi.mos.util.MosLog;
import com.xuesi.sms.R;
import com.xuesi.sms.ServerApi;
import com.xuesi.sms.SmsApplication;
import com.xuesi.sms.bean.CraneByType;
import com.xuesi.sms.bean.Stack;
import com.xuesi.sms.bean.StackExplain;
import com.xuesi.sms.bean.StoreHouse;
import com.xuesi.sms.bean.StoreHouse.ListClass;
import com.xuesi.sms.widget.adapter.CraneAdapter;
import com.xuesi.sms.widget.adapter.StackAdapter;
import com.xuesi.sms.widget.adapter.StoreAdapter;

/**
 * 
 * 钢板处理基类
 * 
 * @author XS-PC014
 * 
 */
public abstract class SheetBaseActivity extends ScanBaseActivity implements
		AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener,
		PullToRefreshBase.OnRefreshListener2<GridView> {
	/** LOG */
	private final String LOG_TAG = SheetBaseActivity.class.getSimpleName();
	private final MosLog mLog = MosLog.getInstance(LOG_TAG, "XUESI-SMS");
	/** progress的次数 */
	private int mProgressNumber = 0;

	/** 库房类型,值域{FSK,RCK} */
	protected String curStoreType;
	// 库房下拉框 ------------
	/** 选择库房下拉框 */
	private PopupWindow storePopWin;
	private LinearLayout storeLinear;
	protected TextView HouseLeftTxt, stockNoTxt;
	/** 下拉选择库房列表 */
	protected ListView storeListView;
	/** 选择仓库适配器 */
	protected StoreAdapter storeAdapter;
	/** 当前选中的库房 */
	protected StoreHouse.ListClass curSelctStore;

	// 垛位列表 ---------------
	/** 下拉刷新控件 */
	protected PullToRefreshGridView stackPullRefreshGridView;
	/** 垛位列表项 */
	protected GridView stackGridView;
	/** 所有垛位信息 */
	protected List<Stack.ListClass> stackList;
	/** 垛位适配器 */
	protected StackAdapter stackAdapter;
	/** 垛位数据解析体(申明为全局变量是TotalActivity需要调用) */
	protected Stack stack;
	/** 原垛位(选中垛位) */
	protected Stack.ListClass fromStack;
	/** 目标垛位 */
	protected Stack.ListClass toStack;

	// 钢板列表 ----------------
	/** 钢板列表项 */
	protected ListView sheetListView;

	// 行车部分 ----------------
	/** 选择行车弹出框 */
	protected Dialog craneDialog;
	/** 行车列表 */
	protected ListView craneListView;
	protected CraneAdapter craneAdapter;
	/** 当前行车 */
	protected CraneByType.ListClass curSelctCrane;
	/** 保存要填写的的垛位信息 */
	protected List<StackExplain.StackMsg> stackMsgList = new ArrayList<StackExplain.StackMsg>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		SmsApplication.getInstance().addActivity(this);// 添加到Activity堆栈
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	protected void initContentView() {
		// TODO Auto-generated method stub
		super.initContentView();

		// 选择库房组合框--------
		storeLinear = (LinearLayout) findViewById(R.id.housestore_linear);
		HouseLeftTxt = (TextView) findViewById(R.id.housestore_tv_left);
		stockNoTxt = (TextView) findViewById(R.id.housestore_tv_name);
		findViewById(R.id.housestore_btn_arrow).setOnClickListener(this);
		// 垛位列表项
		stackPullRefreshGridView = (PullToRefreshGridView) findViewById(R.id.include_gv_stack);
		stackPullRefreshGridView.setOnRefreshListener(this);
		// 设置模式为只能下拉刷新
		stackPullRefreshGridView.setMode(Mode.PULL_FROM_START);
		stackGridView = stackPullRefreshGridView.getRefreshableView();
		stackGridView.setOnItemClickListener(this);
		stackGridView.setOnItemLongClickListener(this);
	}

	/**
	 * 下拉回调方法
	 */
	@Override
	public void onPullDownToRefresh(PullToRefreshBase<GridView> refreshView) {
		// TODO Auto-generated method stub
		// 取消弹出加载对话框
		mProgressNumber = 1;
	}

	/**
	 * 上拉回调方法
	 */
	@Override
	public void onPullUpToRefresh(PullToRefreshBase<GridView> refreshView) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		// -------------------
		// 库房列表项(单选库房)
		if (parent == storeListView) {
			curSelctStore = (ListClass) storeAdapter.getItem(position);
			stockNoTxt.setText(curSelctStore.getNAME());

			myNetwork(ServerApi.getInstance().API_GET_STACKINFO);
			if (storePopWin.isShowing()) {
				storePopWin.dismiss();
			}
		}

		// ----------
		// 行车列表
		if (parent == craneListView) {
			curSelctCrane = craneAdapter.getItem(position);
			if (curSelctCrane.isSelected()) {// 如果已经是选中的
				return;
			}
			// 实现单选功能
			for (CraneByType.ListClass crane : craneAdapter.getList()) {
				if (crane.isSelected()) {
					crane.setSelected(false);
					break;
				}
			}
			curSelctCrane.setSelected(true);
			craneAdapter.notifyDataSetChanged();
			mLog.d("curSelctCrane==" + curSelctCrane.getDNAME());
		}
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void myNetwork(String tag, String... txt) {
		// TODO Auto-generated method stub
		// super.myNetwork(tag, txt);
		mProgressNumber++;
		// mLog.w("mProgressNumber== " + LOG_TAG + mProgressNumber);
		if (mProgressNumber == 1) {
			showProgressDialog("加载中", false, false);
		}

		JSONObject json = getRequstJson();
		if (ServerApi.getInstance().SHEET_GETHOUSEID.equals(tag)) {
			try {
				json.put("type", curStoreType);
				json.put("perPage", 9999);// 默认值为5
				sendPOST(tag, json, null, this, tag, false);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		} else if (ServerApi.getInstance().API_GET_STACKINFO.equals(tag)) {
			try {
				json.put("PhouseID", curSelctStore.getID());
				// json.put("material", "");
				// json.put("thickness", "");
				// json.put("length", "");
				// json.put("width", "");
				// json.put("pOrderKey", orderKey);
				sendPOST(tag, json, null, this, tag, false);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		} else if (ServerApi.getInstance().API_GETSTACKINFONAME.equals(tag)) {
			// 推荐空垛位-入库和倒垛
			try {
				json.put("PhouseID", curSelctStore.getID());
				json.put("pSheetId", txt[0]);
				// json.put("pOrderKey", orderKey);// 因为返回两个结果list,无需排序字段
				sendPOST(tag, json, null, this, tag, false);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		} else if (ServerApi.getInstance().API_GET_CRANELIST.equals(tag)) {
			// 获取行车
			try {
				json.put("dcoType", "2");
				json.put("storeType", curStoreType);
				json.put("perPage", "9999");// 每页返回的记录数
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
		// super.onRequestSuccess(str, obj);
		if (mProgressNumber > 0) {
			mProgressNumber--;
		}
		if (mProgressNumber == 0) {
			dismissProgressDialog();
		}
		Gson gson = new Gson();
		mLog.init(tag, LOG_TAG);

		if (ServerApi.getInstance().SHEET_GETHOUSEID.equals(tag)) {
			// 获取库房
			mLog.i(LOG_TAG + obj.toString());
			StoreHouse storeHouse = gson.fromJson(obj.toString(),
					StoreHouse.class);
			if (storeHouse.getResultCode() == 0) {
				storeAdapter = new StoreAdapter(this, storeHouse.getList());
				if (storeHouse.getList().size() > 0) {
					if (null != curSelctStore) {
						showSelectHousePopup();
					} else {
						// 初始化页面
						curSelctStore = storeAdapter.getItem(0);
						stockNoTxt.setText(curSelctStore.getNAME());
						myNetwork(ServerApi.getInstance().API_GET_STACKINFO);
					}
				} else {
					showForceExitDialog(this, "prompt",
							R.string.msg_createstore);
				}
			}
		} else if (ServerApi.getInstance().API_GET_STACKINFO.equals(tag)) {
			// 获取垛位
			mLog.i(LOG_TAG + obj.toString());
			stack = gson.fromJson(obj.toString(), Stack.class);
			stackPullRefreshGridView.onRefreshComplete();
			if (stack.getResultCode() == 0) {
				stackList = new ArrayList<Stack.ListClass>();
				stackList = stack.getList();
				stackAdapter = new StackAdapter(this, stackList, curStoreType,
						1);
				stackGridView.setAdapter(stackAdapter);
				if (stack.getList().size() == 0) {
					TextView tv = new TextView(this);
					tv.setGravity(Gravity.CENTER);
					tv.setText("没有垛位数据，请先查看库房");
					stackPullRefreshGridView.setEmptyView(tv);
				} else {
					stackPullRefreshGridView.removeEmptyView();
				}
			}
		} else if (ServerApi.getInstance().API_GET_CRANELIST.equals(tag)) {
			CraneByType craneByType = gson.fromJson(obj.toString(),
					CraneByType.class);
			mLog.i(LOG_TAG + obj.toString());
			if (craneByType.getResultCode() == 0) {
				if (craneByType.getList().size() > 0) {
					// 行车弹出框
					showSeleCraneDialog(craneByType.getList());
				} else {
					showPromptDialog(this, "prompt", R.string.msg_create_crane);
				}
			}
		} else if (ServerApi.getInstance().API_GETSTACKINFONAME.equals(tag)) {
			// 入库和倒垛推荐垛位
			mLog.i(LOG_TAG + obj.toString());
			stack = gson.fromJson(obj.toString(), Stack.class);
			if (stack.getResultCode() == 0) {
				for (Stack.ListClass stacklist : stack.getList()) {
					stacklist.setRecommend(true);
				}
				List<Stack.ListClass> allList = new ArrayList<Stack.ListClass>();

				if (stack.getList().size() == 0) {
					allList.addAll(stackList);
				} else {
					List<String> temp = new ArrayList<String>();
					allList.addAll(stack.getList());
					for (Stack.ListClass sl : stack.getList()) {
						temp.add(sl.getSTACKNO());
					}
					for (Stack.ListClass slc : stackList) {
						if (!temp.contains(slc.getSTACKNO())) {
							allList.add(slc);
						}
					}
				}
				stackAdapter = new StackAdapter(this, allList, curStoreType, 1);
				stackGridView.setAdapter(stackAdapter);
			}
		}
	}

	/**
	 * 显示选择仓库弹出框
	 */
	public void showSelectHousePopup() {
		if (null != storePopWin && storePopWin.isShowing()) {
			storePopWin.dismiss();
		}
		int width = storeLinear.getWidth();
		// 选择库房弹出部分
		View popView = getLayoutInflater().inflate(R.layout.popup_storehouse,
				null);
		storeListView = (ListView) popView
				.findViewById(R.id.popup_lv_houseName);
		storeListView.setOnItemClickListener(this);
		storeListView.setAdapter(storeAdapter);

		storePopWin = new PopupWindow(popView, width,
				ViewGroup.LayoutParams.WRAP_CONTENT);
		storePopWin.setFocusable(true);
		storePopWin.setOutsideTouchable(true);
		storePopWin.setBackgroundDrawable(new BitmapDrawable());
		storePopWin.setAnimationStyle(R.style.AnimTop2);
		if (storeAdapter.getCount() > 5) {
			storePopWin
					.setHeight(Screen.getInstance().getScreenHeight() * 4 / 10);
		}
		if (storePopWin.isShowing()) {
			storePopWin.dismiss();
		} else {
			int[] position = new int[2];
			HouseLeftTxt.getLocationOnScreen(position);
			int height = storeLinear.getHeight();
			int y = position[1] + height;
			// 在指定位置显示
			storePopWin.showAtLocation(stockNoTxt, Gravity.LEFT | Gravity.TOP,
					position[0], y);
		}
	}

	/**
	 * 显示选择行车对话框
	 */
	protected void showSeleCraneDialog(List<CraneByType.ListClass> list) {
		View dialogView = getLayoutInflater().inflate(R.layout.dialog_crane,
				null);
		craneDialog = new Dialog(this, R.style.dialog);
		craneDialog.addContentView(dialogView, new LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		// 确定按钮事件
		dialogView.findViewById(R.id.seleCrane_btn_confirm).setOnClickListener(
				this);
		craneListView = (ListView) dialogView
				.findViewById(R.id.seleCrane_lv_haulage);
		craneAdapter = new CraneAdapter(this, list);
		craneListView.setAdapter(craneAdapter);
		// 行车列表事件(在子activity中响应)
		craneListView.setOnItemClickListener(this);
		craneDialog.show();
	}

	/**
	 * 提示对话框 <br>
	 * 提示后关闭activity <br>
	 * 
	 * @param context
	 * @param title
	 * @param mes
	 */
	private void showForceExitDialog(Context context, String title, int resId) {
		AlertDialog.Builder builder = new Builder(context);
		if (title.equals("warning")) {
			builder.setTitle(R.string.warning);
		} else if (title.equals("prompt")) {
			builder.setTitle(R.string.prompt);
		} else {
			builder.setTitle(title);
		}
		builder.setMessage(getString(resId));
		builder.setPositiveButton(R.string.confirm,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						finish();
					}
				});
		builder.create().show();
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
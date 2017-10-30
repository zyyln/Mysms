package com.xuesi.sms.app.activity;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.xuesi.mos.libs.pulltorefresh.PullToRefreshBase;
import com.xuesi.mos.util.MosLog;
import com.xuesi.sms.R;
import com.xuesi.sms.ServerApi;
import com.xuesi.sms.SmsApplication;
import com.xuesi.sms.SmsConfig;
import com.xuesi.sms.bean.ResultCode;
import com.xuesi.sms.bean.Sheet;
import com.xuesi.sms.bean.Stack;
import com.xuesi.sms.bean.StoreHouse;
import com.xuesi.sms.util.SmsUtil;
import com.xuesi.sms.widget.adapter.SheetAdapter;

/**
 * 倒垛<br>
 * 
 * @author XS-PC014
 * 
 */
public class ShiftActivity extends SheetBaseActivity {
	/** LOG */
	private final String LOG_TAG = ShiftActivity.class.getSimpleName();
	private final MosLog mLog = MosLog.getInstance(LOG_TAG, "XUESI-SMS");
	/** progress的次数 */
	private int mProgressNumber = 0;

	/** 钢板列表 */
	private List<Sheet.ListClass> selctSheetList;
	private SheetAdapter sheetAdapter;
	private Sheet.ListClass curSelctSheet;
	/** 当前的位置 */
	private int curPosition;

	/** 记录吊取钢板数，重量 */
	private int curSheetNum = 0;
	private double curSheetWeight = 0.0;
	/** 记录从哪个界面跳转过来的 */
	private String fromActivity;
	/** 显示计算吊取钢板数 */
	private EditText countSheetTxt, weightSheetTxt;
	private TextView stackNoTxt;

	/** 行车勾选框 */
	private CheckBox seleCrane;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		SmsApplication.getInstance().addActivity(this);// 添加到Activity堆栈
		setContentView(R.layout.activity_shift);

		loadExtras();
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
		setTopTitle(R.string.sheet_shift);
		setRefreshView(View.INVISIBLE);

		// 减号、加号、倒垛
		findViewById(R.id.shift_btn_shift).setOnClickListener(this);
		stackNoTxt = (TextView) findViewById(R.id.shift_tv_stackName);
		countSheetTxt = (EditText) findViewById(R.id.shift_tv_countSheet);
		weightSheetTxt = (EditText) findViewById(R.id.sheet_tv_weight);
		// 钢板列表
		sheetListView = (ListView) findViewById(R.id.shift_lv_info);
		sheetListView.setOnItemClickListener(this);
		selctSheetList = new ArrayList<Sheet.ListClass>();

		// 选择行车
		seleCrane = (CheckBox) findViewById(R.id.shift_cb_crane);
		seleCrane.setChecked(SmsConfig.isCraneCheck);
	}

	/**
	 * 处理其他页面需要倒垛的钢板
	 */
	private void loadExtras() {
		// mLog.d("run_> " + LOG_TAG + " initLoad()");
		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
			fromActivity = bundle.getString("fromActivity");
			if (fromActivity.equals(StoreTypeActivity.class.getSimpleName())) {
				mLog.d("从导航而来");
				// 初始画面为默认显示一级库下第一个库房下的垛位
				curStoreType = bundle.getString("curStoreType");
				myNetwork(ServerApi.getInstance().SHEET_GETHOUSEID, "");
			} else {
				// 取出数据包中需要倒垛的库房类型、库房编号、库房名称、垛位编号
				curSheetNum = bundle.getInt("num");
				curSheetWeight = bundle.getDouble("weight");
				curSelctStore = (StoreHouse.ListClass) bundle
						.getSerializable("curSelctStore");
				fromStack = (Stack.ListClass) bundle
						.getSerializable("curSelctStack");
				curStoreType = curSelctStore.getTYPE();
				if (SheetDetailActivity.class.getSimpleName().equals(
						fromActivity)) {
					// 钢板明细
					curSheetNum = curSheetNum + 1;
				}
				// mLog.d("取出的库房类型==" + curStoreType);
				// mLog.d("取出的库房名称==" + curSelctStore.getName());
				// mLog.d("取出的垛位名称==" + fromStack.getStackName());

				stockNoTxt.setText(curSelctStore.getNAME());
				// 获取当前库房下的所有垛位
				myNetwork(ServerApi.getInstance().API_GET_STACKINFO);
			}
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		// super.onClick(v);
		switch (v.getId()) {
		case R.id.bartop_img_back:
			if (getIntent().getExtras() != null) {
				// 处理其他页面跳转
				ShiftActivity.this.setResult(1011);
			}
			finish();
			break;
		case R.id.bartop_img_refresh:
			// if (stackAllList.size() > 0 && sheetAllList.size() > 0) {
			// reqStackData();
			// reqSheetItemData(fromStack.getStackNO(), null);
			// }
			break;
		case R.id.housestore_btn_arrow:// 选择库房
			if (curSheetNum == 0) {
				myNetwork(ServerApi.getInstance().SHEET_GETHOUSEID, "");
			} else {
				showShortToast("正在倒垛！");
			}
			break;
		case R.id.seleCrane_btn_confirm:// 行车确定按钮
			if (null != curSelctCrane) {
				craneDialog.cancel();
				// 倒垛请求
				myNetwork(ServerApi.getInstance().SHEET_MOVESTACK);
			} else {
				showShortToast("请选择行车!");
			}
			break;
		case R.id.shift_btn_shift:// 倒垛按钮
			if (selctSheetList.size() > 0) {
				if (null != toStack) {
					// 置空行车
					curSelctCrane = null;
					if (seleCrane.isChecked()) {

						myNetwork(ServerApi.getInstance().API_GET_CRANELIST, "");
					} else {// 行车未勾选则直接倒垛
						myNetwork(ServerApi.getInstance().SHEET_MOVESTACK);
					}
				} else {
					showShortToast("请选择目标垛位！");
				}
			}
			break;
		default:
			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		// 库房列表项和行车事件在父类
		super.onItemClick(parent, view, position, id);
		// 库房列表事件
		if (parent == storeListView) {
			// 置空原垛位,置空钢板列表
			fromStack = null;
			if (null != sheetAdapter) {
				sheetAdapter.getList().clear();
				sheetAdapter.notifyDataSetChanged();
			}
		}

		// 钢板列表事件(可多选入库,需按上下顺序选择钢板)
		if (parent == sheetListView) {
			curSelctSheet = sheetAdapter.getItem(position);
			if (selctSheetList.size() != 0) {
				// 单击条目处理钢板,不用加减号
				if (position == curPosition || position == curPosition + 1) {
					mLog.d("第二或N次选");
					curPosition = position;
					if (curSelctSheet.isSelected()) {
						--curSheetNum;
						curSheetWeight = curSheetWeight
								- Double.parseDouble(curSelctSheet.getWeight());
						--curPosition;

						curSelctSheet.setSelected(false);
						selctSheetList.remove(curSelctSheet);
						curSelctSheet.setSelectNum("");
					} else {
						++curSheetNum;
						curSheetWeight = curSheetWeight
								+ Double.parseDouble(curSelctSheet.getWeight());
						if (!Sheet.equalsSheet(curSelctSheet,
								selctSheetList.get(0))) {
							showPromptDialog(this, "warning",
									R.string.select_sheet_warning);
						}

						curSelctSheet.setSelected(true);
						//
						selctSheetList.add(curSelctSheet);
						mLog.e(selctSheetList.size());
						curSelctSheet.setSelectNum(selctSheetList.size() + "");
					}
				} else {
					// 按上下顺选择钢板
					showPromptDialog(this, "warning",
							R.string.select_steel_in_up_and_down_the_order);
				}
			} else {
				mLog.d("第一次选");
				if (position == 0) {
					curPosition = position;
					++curSheetNum;
					curSheetWeight = curSheetWeight
							+ Double.parseDouble(curSelctSheet.getWeight());
					curSelctSheet.setSelected(true);
					selctSheetList.add(curSelctSheet);
					curSelctSheet.setSelectNum(selctSheetList.size() + "");
					// 当第一次选钢板的时候推荐垛位
					myNetwork(ServerApi.getInstance().API_GETSTACKINFONAME,
							curSelctSheet.getMetalBillId());
				} else {
					// 请选第一张钢板
					showPromptDialog(this, "warning",
							R.string.select_piece_of_steel_plate);
				}
			}
			// 重置数据
			if (curSheetNum == 0) {
				toStack = null;
				selctSheetList.clear();
				for (Stack.ListClass stack : stackAdapter.getList()) {
					stack.setToSelect(false);
					stack.setRecommend(false);
				}
				stackAdapter.notifyDataSetChanged();
			}
			countSheetTxt.setText(curSheetNum + getString(R.string.steel_unit));
			weightSheetTxt.setText(SmsUtil.DecimalFormat(curSheetWeight,
					SmsConfig.dotNum) + "Kg");
			sheetAdapter.notifyDataSetChanged();
		}

		// -----------
		// 垛位列表事件
		if (parent == stackGridView) {
			if (curSheetNum == 0) {// 获取选中垛位的所有钢板
				// mLog.d(TAG + "走获取所有的钢板");
				fromStack = stackAdapter.getItem(position);
				for (int i = 0; i < stackAdapter.getCount(); i++) {
					stackAdapter.getItem(i).setSelected(
							i == position ? true : false);
					stackAdapter.getItem(i).setRecommend(false);
				}
				// mLog.d("fromStack==" + fromStack.getSTACKNO() + "," +
				// fromStack.getSTACKNAME());

				myNetwork(ServerApi.getInstance().SHEET_GETSHEETLIST);
				stackNoTxt.setText(fromStack.getSTACKNAME()
						+ getString(R.string.shift_location));
			}
			if (curSheetNum != 0) {// 选择垛位进行倒垛
				// mLog.d(TAG + "走倒垛");

				Stack.ListClass stack = stackAdapter.getItem(position);

				// mLog.d(TAG, "toStack==" + toStack.getStackNO() + "," +
				// toStack.getStackName());

				if (fromStack.getSTACKNO().equals(stack.getSTACKNO())) {
					showShortToast("不能选择原垛位！");
					return;
				} else {
					// 重置目标垛位属性
					for (Stack.ListClass stacke : stackAdapter.getList()) {
						stacke.setToSelect(false);
					}
					if (curStoreType.equals("SCK")) {
						String stackName = stack.getSTACKNAME();
						if (!stackName.contains("LS")) {
							toStack = stack;
							toStack.setToSelect(true);
						} else {
							showShortToast("不能选临时库入库！");
						}
					} else {
						toStack = stack;
						toStack.setToSelect(true);
					}
				}
			}
			stackAdapter.notifyDataSetChanged();
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			if (getIntent().getExtras() != null) {
				// 处理其他页面跳转
				ShiftActivity.this.setResult(1011);
			}
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}

	/**
	 * 下拉方法
	 */
	@Override
	public void onPullDownToRefresh(PullToRefreshBase<GridView> refreshView) {
		// TODO Auto-generated method stub
		super.onPullDownToRefresh(refreshView);
		if (null == curSelctStore) {
			stackPullRefreshGridView.onRefreshComplete();
			return;
		}
		// 取消弹出加载对话框
		mProgressNumber = 1;
		myNetwork(ServerApi.getInstance().API_GET_STACKINFO);
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
		JSONObject json;
		if (ServerApi.getInstance().SHEET_GETSHEETLIST.equals(tag)) {
			// 获取钢板
			try {
				json = getRequstJson();
				json.put("StackNo", fromStack.getSTACKNO());// 货位号
				json.put("Barcode", "");// 条形码，可以省略
				json.put("perPage", "99999");// 每页返回的记录数
				json.put("deleteFlag", "0");// 删除分区(因为是pcpad共用接口需要传值 0:未删除,1:删除)
				// json.put("from", "1");
				// json.put("segments", segments);
				// 排序字段(desc是descend 降序, asc是ascend 升序)
				json.put("pCOLUMN", "ItemNo desc");
				sendPOST(tag, json, null, this, tag, false);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		} else if (ServerApi.getInstance().SHEET_MOVESTACK.equals(tag)) {
			// 倒垛
			try {
				json = new JSONObject();
				JSONArray jsonArray = new JSONArray();
				for (int i = selctSheetList.size() - 1; i >= 0; i--) {
					JSONObject jo = new JSONObject();
					if (null != curSelctCrane) {// 是否选择行车，是传值1，否不传
						jo.put("flag", 1);
						jo.put("deviceId", curSelctCrane.getDCODEID());
					}
					jo.put("stackNo", fromStack.getSTACKNO());
					jo.put("gloalStackNo", toStack.getSTACKNO());
					jo.put("sheetId", selctSheetList.get(i).getMetalBillId());
					jo.put("user", ServerApi.account);
					jo.put(ServerApi.PARA_FACTORYCODE, ServerApi.factoryCode);
					json.put(ServerApi.COOKIE_MILLCODE, ServerApi.millCode);
					jsonArray.put(jo);
				}
				json.put("SheetList", jsonArray);
				json.put(ServerApi.COOKIE_PASSPORT, ServerApi.passport);
				sendPOST(tag, json, null, this, tag, false);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		} else {
			// toastShort("错误的网络请求，请检查");
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
	public void onRequestSuccess(String tag, Object obj) {
		// TODO Auto-generated method stub
		super.onRequestSuccess(tag, obj);
		if (mProgressNumber > 0) {
			mProgressNumber--;
		}
		if (mProgressNumber == 0) {
			dismissProgressDialog();
		}
		Gson gson = new Gson();

		if (ServerApi.getInstance().API_GET_STACKINFO.equals(tag)) {
			mLog.i(LOG_TAG + obj.toString());
			// 获取垛位
			if (null != stackAdapter && stackAdapter.getCount() > 0) {
				if (null != fromStack) {
					/** 处理其他页面的倒垛需求 **/
					mLog.w("处理其他页面的倒垛需求");
					for (Stack.ListClass stack : stackAdapter.getList()) {// 设置原垛位背景
						if (stack.getSTACKNO().equals(fromStack.getSTACKNO())) {
							stack.setSelected(true);
						}
					}
					stackAdapter.updateHouseType(curStoreType);
					stackAdapter.notifyDataSetChanged();
					stackNoTxt.setText(fromStack.getSTACKNAME()
							+ getString(R.string.shift_location));
					// 获取当前垛位下的所有钢板
					myNetwork(ServerApi.getInstance().SHEET_GETSHEETLIST);
				}
			}
		} else if (ServerApi.getInstance().SHEET_GETSHEETLIST.equals(tag)) {
			mLog.i(LOG_TAG + obj.toString());
			// 获取钢板
			Sheet sheetOut = gson.fromJson(obj.toString(), Sheet.class);
			if (sheetOut.getResultCode() == 0) {
				selctSheetList.clear();
				sheetAdapter = new SheetAdapter(this, sheetOut.getList());
				sheetListView.setAdapter(sheetAdapter);

				// 当是从其他画面跳转过来时
				if (curSheetNum != 0) {
					for (int i = 0; i < curSheetNum; i++) {
						Sheet.ListClass sheet = sheetAdapter.getItem(i);
						sheet.setSelected(true);
						sheet.setSelectNum(i + 1 + "");
						// 钢板实际层号是从底部到顶部的，当多张钢板倒垛需要保持上下层号顺序
						selctSheetList.add(sheet);
					}
					curPosition = curSheetNum - 1;
					// 当第一次选钢板的时候推荐垛位
					myNetwork(ServerApi.getInstance().API_GETSTACKINFONAME,
							sheetAdapter.getItem(0).getMetalBillId());
					sheetAdapter.notifyDataSetChanged();
					countSheetTxt.setText(curSheetNum
							+ getString(R.string.steel_unit));
					weightSheetTxt.setText(SmsUtil.DecimalFormat(
							curSheetWeight, SmsConfig.dotNum)
							+ getString(R.string.ton));
				}
			} else {
				showShortToast("resultCode==" + sheetOut.getResultCode()
						+ "，请联系开发人员");
			}
		} else if (ServerApi.getInstance().API_GETSTACKINFONAME.equals(tag)) {
			mLog.i(LOG_TAG + obj.toString());
			// 获取推荐垛位(父类解析)
			if (null != stackAdapter && stackAdapter.getCount() > 0) {
				// 设置原垛位
				for (Stack.ListClass stack : stackAdapter.getList()) {
					if (stack.getSTACKNO().equals(fromStack.getSTACKNO())) {
						stack.setSelected(true);
						stack.setRecommend(false);
					}
				}
			}
		} else if (ServerApi.getInstance().SHEET_MOVESTACK.equals(tag)) {
			mLog.i(LOG_TAG + obj.toString());
			ResultCode rc = gson.fromJson(obj.toString(), ResultCode.class);
			if (rc.getResultCode() == 0) {
				if (rc.getCodeList().contains(1002)) {
					showShortToast("正在盘点，请勿倒垛");
				} else {
					if (rc.getCodeList().contains(1001)) {
						showShortToast("存在钢板已在行车");
					} else if (rc.getCodeList().contains(1003)) {
						showShortToast("存在钢板已在垛位");
					} else if (rc.getCodeList().contains(1004)) {
						showShortToast("存在钢板已被倒垛");
					} else if (rc.getCodeList().contains(1005)) {
						showShortToast("部分钢板已被出库锁定，请联系仓管人员！");
					} else {
						if (null != curSelctCrane) {// 勾选行车
							showShortToast("钢板已发送到行车!");
						} else {
							showShortToast("倒垛成功");
						}
					}

					// 更新垛位信息,当fromStack不为空值时更新钢板列表
					myNetwork(ServerApi.getInstance().API_GET_STACKINFO);

					// 清空当前选中的钢板List
					selctSheetList.clear();
					// 吊取个数清空
					curSheetNum = 0;
					curSheetWeight = 0.0;
					countSheetTxt.setText(curSheetNum
							+ getString(R.string.steel_unit));
					weightSheetTxt.setText(SmsUtil.DecimalFormat(
							curSheetWeight, SmsConfig.dotNum)
							+ getString(R.string.ton));
					// 置空选中的行车
					curSelctCrane = null;
				}
			} else {
				showShortToast("resultCode==" + rc.getResultCode() + "，请联系开发人员");
			}
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
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

	@Override
	protected void onScanResult(String barcode) {
		// TODO Auto-generated method stub
	}

}

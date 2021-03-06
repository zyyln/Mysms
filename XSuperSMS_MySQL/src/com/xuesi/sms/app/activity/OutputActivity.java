package com.xuesi.sms.app.activity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.xuesi.mos.device.Screen;
import com.xuesi.mos.libs.pulltorefresh.PullToRefreshBase;
import com.xuesi.mos.util.MosLog;
import com.xuesi.sms.R;
import com.xuesi.sms.ServerApi;
import com.xuesi.sms.SmsApplication;
import com.xuesi.sms.SmsConfig;
import com.xuesi.sms.bean.BaseModel;
import com.xuesi.sms.bean.GrantOrder;
import com.xuesi.sms.bean.OrderDetail;
import com.xuesi.sms.bean.ResultCode;
import com.xuesi.sms.bean.Sheet;
import com.xuesi.sms.bean.Stack;
import com.xuesi.sms.util.CalendarUtil;
import com.xuesi.sms.widget.adapter.GrantOrderAdapter;
import com.xuesi.sms.widget.adapter.OrderDetailAdapter;
import com.xuesi.sms.widget.adapter.StackAdapter;

public class OutputActivity extends SheetBaseActivity implements
		RadioGroup.OnCheckedChangeListener {
	/** LOG */
	private final String LOG_TAG = OutputActivity.class.getSimpleName();
	private final MosLog mLog = MosLog.getInstance(LOG_TAG, "XUESI-SMS");
	/** progress的次数 */
	private int mProgressNumber = 0;

	/** 待处理0的发料单号(0:待处理;1:待审批;2待发放;3 已发放) */
	private PopupWindow numPopupWin;
	private LinearLayout orderLinear;
	private TextView orderLeft, orderName;
	private View grantPopView;
	/** 单号列表 */
	private ListView grantOrderListView;
	private GrantOrderAdapter grantOrderAdapter;

	/** 单号详细列表项 */
	private ListView orderDetailListView;
	private OrderDetailAdapter orderDetailAdapter;
	/** 当前选中的单号详细 */
	private OrderDetail.ListClass curOrderDetail;

	/** 垛位布局 */
	private LinearLayout stackLayout;

	/** 钢板布局 */
	protected LinearLayout sheetLayout;
	/** 钢板列表 */
	private SheetAdapter sheetAdapter;
	/** 当前选中的钢板 */
	private Sheet.ListClass curSelctSheet;
	/** 成功交换工程的钢板 */
	private Sheet.ListClass sheetEXpro;
	/** 显示当前垛位 */
	private TextView stackTxt;

	/** 选择库房类型 */
	private RadioGroup selctHouseType;

	/** 出库路径 */
	private RadioGroup selctPathType;
	/** 选中的路径(默认为：最短路径) */
	private String selctPath = "0";

	/** 行车勾选框 */
	private CheckBox selctCrane;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		SmsApplication.getInstance().addActivity(this);// 添加到Activity堆栈
		setContentView(R.layout.activity_output);

		// 初始画面为默认显示一级库下第一个库房下的垛位
		curStoreType = "FSK";
		myNetwork(ServerApi.getInstance().SHEET_GETHOUSEID);
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
		setTopTitle(R.string.sheet_output);
		setRefreshView(View.INVISIBLE);

		// 选择库房类型
		selctHouseType = (RadioGroup) findViewById(R.id.output_radiogroup_warehouse);
		selctHouseType.setOnCheckedChangeListener(this);

		// 选择单号
		findViewById(R.id.output_imgbtn_order).setOnClickListener(this);
		orderLinear = (LinearLayout) findViewById(R.id.output_linear_selctNum);
		orderLeft = (TextView) findViewById(R.id.output_tv_orderleft);
		orderName = (TextView) findViewById(R.id.output_tv_order);
		grantPopView = getLayoutInflater().inflate(R.layout.popup_grant, null);
		grantOrderListView = (ListView) grantPopView
				.findViewById(R.id.popup_lv_grantNo);
		grantOrderListView.setOnItemClickListener(this);

		// 选择出库路径
		selctPathType = (RadioGroup) findViewById(R.id.output_radiogroup_selctPath);
		selctPathType.setOnCheckedChangeListener(this);

		// 单号详细列表项
		orderDetailListView = (ListView) findViewById(R.id.output_lv_grantDetail);
		orderDetailListView.setOnItemClickListener(this);

		// 钢板布局
		sheetLayout = (LinearLayout) findViewById(R.id.output_linear_sheet);
		stackLayout = (LinearLayout) findViewById(R.id.output_linear_stack);

		// 钢板列表
		sheetListView = (ListView) findViewById(R.id.output_lv_sheet);
		sheetListView.setOnItemClickListener(this);

		// 出库、返回垛位按钮
		findViewById(R.id.output_btn_output).setOnClickListener(this);
		stackTxt = (TextView) findViewById(R.id.output_tv_stackno);
		stackTxt.setOnClickListener(this);

		// 选择行车
		selctCrane = (CheckBox) findViewById(R.id.output_cb_crane);
		selctCrane.setChecked(SmsConfig.isCraneCheck);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		super.onClick(v);
		switch (v.getId()) {
		case R.id.output_imgbtn_order:// 选择单号

			myNetwork(ServerApi.getInstance().API_GETGRANTID);
			break;
		case R.id.housestore_btn_arrow: // 选择库房

			myNetwork(ServerApi.getInstance().SHEET_GETHOUSEID, "");
			break;
		case R.id.output_tv_stackno:// 返回垛位
			stackLayout.setVisibility(View.VISIBLE);
			sheetLayout.setVisibility(View.GONE);
			curSelctSheet = null;

			break;
		case R.id.seleCrane_btn_confirm: {// 选择行车确定按钮
			if (curSelctCrane == null) {
				showShortToast("请选择行车");
			} else {
				craneDialog.cancel();
				sheetSelectOutput();
				// output();
			}
		}
			break;
		case R.id.output_btn_output:// 出库按钮
			if (null != sheetAdapter && sheetAdapter.getList().size() > 0) {
				// 有钢板的情况下
				if (curSelctSheet != null) {
					// 当钢板是在头部时，可选
					if (sheetAdapter.getList().get(0).equals(curSelctSheet)) {
						if (sheetAdapter.getList().contains(curSelctSheet)) {
							curSelctCrane = null;
							if (selctCrane.isChecked()) {
								myNetwork(
										ServerApi.getInstance().API_GET_CRANELIST,
										"");
							} else {
								output();
							}
						} else {
							showShortToast("你选择的钢板不符合出库条件!");
						}
					} else {
						showShortToast("请先倒垛上层的钢板！");
					}
				} else {
					showShortToast("请选择钢板进行出库!");
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

		// 发料单号列表项
		if (parent == grantOrderListView) {
			grantOrderAdapter.setSelecteP(position);
			orderName.setText(grantOrderAdapter.getSeleItem().getID().trim());

			myNetwork(ServerApi.getInstance().API_GETGRANT_DETAILINFO, "");
			if (numPopupWin.isShowing()) {
				numPopupWin.dismiss();
			}
		}

		// 单号明细列表项(单选)
		if (parent == orderDetailListView) {
			curOrderDetail = orderDetailAdapter.getItem(position);
			if (curOrderDetail.isSelected()) {
				curOrderDetail.setSelected(false);
			} else {
				orderDetailAdapter.updateSelecte();
				curOrderDetail.setSelected(true);
				myNetwork(ServerApi.getInstance().API_GETSHEETNUMINSTACKRUST);
			}
			orderDetailAdapter.notifyDataSetChanged();
		}

		// 垛位列表项
		if (parent == stackGridView) {
			fromStack = (Stack.ListClass) stackAdapter.getItem(position);
			stackTxt.setText("垛位:" + fromStack.getSTACKNAME());
			if (fromStack.isRecommend()) {
				// 根据钢板同类型获取符合条件的钢板
				myNetwork(ServerApi.getInstance().API_ALLSHEETLIST_BYSTACKNO,
						"");
			}
		}

		// 钢板列表项
		if (parent == sheetListView) {
			curSelctSheet = sheetAdapter.getItem(position);
			if (curSelctSheet.getISRUST() == 0) {
				if (!curSelctSheet.isSelected()) {
					for (int i = 0; i < sheetAdapter.getList().size(); i++) {
						sheetAdapter.getList().get(i)
								.setSelected(position == i ? true : false);
					}
					sheetAdapter.notifyDataSetChanged();
				}
			}
		}
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view,
			int position, long id) {
		// TODO Auto-generated method stub
		if (parent == stackGridView) {
			Stack.ListClass curSelctStack = stackAdapter.getItem(position);
			if (curSelctStack.getSumAmount() > 0) {
				Bundle bundle = new Bundle();
				bundle.putString("curStoreType", curStoreType);
				bundle.putSerializable("curSelctStore", curSelctStore);
				bundle.putSerializable("curSelctStack", curSelctStack);
				bundle.putBoolean("isQuery", false);
				// 打开钢板详情画面
				openActivity(SheetDetailActivity.class, bundle);
			} else {
				mLog.e("2---------");
				showShortToast("当前垛位为空！");
			}
		}
		return super.onItemLongClick(parent, view, position, id);
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		// TODO Auto-generated method stub

		// 选择库房类型
		if (group == selctHouseType) {
			if (checkedId == R.id.output_radioBtn_terminal) {
				curStoreType = "FSK";
			} else {
				curStoreType = "RCK";
			}
			// 切换库房类型后,默认选中顺位一号库房
			curSelctStore = null;
			myNetwork(ServerApi.getInstance().SHEET_GETHOUSEID);
		}

		// 选择出库路径
		if (group == selctPathType && null != sheetAdapter) {
			for (Sheet.ListClass sheet : sheetAdapter.getList()) {
				sheet.setSelected(false);
			}
			if (checkedId == R.id.output_radioBtn_fastest) {
				selctPath = "0";// 最短路径
			} else {
				selctPath = "1";// 先进先出
			}
			findRecommendSteel();
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
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

	private void output() {
		myNetwork(ServerApi.getInstance().API_FINISHSEND);

		// if (curOrderDetail.getTYPE().equals("特情请料")) {
		// mLog.d("特情请料-出库");
		// myNetwork(SmsApi.getInstance().API_UPSPECIALOUT);
		// } else {// 切割请料\借用
		// mLog.d("切割或者借用-出库");
		// myNetwork(SmsApi.getInstance().API_UPDATEPADOUT_STACKSELECTED);
		// }
	}

	private void sheetSelectOutput() {
		myNetwork(ServerApi.getInstance().API_UPDATEOUTSTCKSELECTED);
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
		if (ServerApi.getInstance().API_GETGRANTID.equals(tag)) {
			// 获取待处理的发料单单号
			try {
				json = getRequstJson();
				json.put("pFlag", "0");
				// json.put("pGrantId", pGrantId);//发料单号
				// json.put("pUseProjNo", pUseProjNo);//工程编号
				// json.put("pNcCode", pNcCode);//程序编号
				// json.put("pMaterial", pMaterial);//材质
				sendPOST(tag, json, null, this, tag, false);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		} else if (ServerApi.getInstance().API_GETGRANT_DETAILINFO.equals(tag)) {
			// 获取单号详细
			try {
				json = getRequstJson();
				json.put("pGrantId1", "'"
						+ grantOrderAdapter.getSeleItem().getID() + "'");
				json.put("pGrantId2", "'"
						+ grantOrderAdapter.getSeleItem().getID() + "'");
				json.put("pFlag4", "0");
				// json.put("pCurPage", curPage);
				json.put("pShowTotal", "9999");// 每页显示的总记录数
				json.put("pOrder", "GRANTID desc");// 按单号降序
				sendPOST(tag, json, null, this, tag, false);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		} else if (ServerApi.getInstance().API_GETSHEETNUMINSTACKRUST
				.equals(tag)) {
			// 根据钢板同类型定义推荐符合的垛位
			try {
				json = getRequstJson();
				json.put("houseId", curSelctStore.getID());
				if (curOrderDetail.getSHEETTYPE().equals("余料")) {
					json.put("remName", curOrderDetail.getSHEETCODE());// 余料推荐
				}
				json.put("grantId", curOrderDetail.getGRANTID());
				json.put("flowNo", curOrderDetail.getFLOWNO());
				json.put("perPage", 9999);
				sendPOST(tag, json, null, this, tag, false);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		} else if (ServerApi.getInstance().API_ALLSHEETLIST_BYSTACKNO
				.equals(tag)) {
			// 获取推荐钢板
			try {
				json = getRequstJson();
				json.put("Flag", "1");
				json.put("StackNo", fromStack.getSTACKNO());
				if (curOrderDetail.getSHEETTYPE().equals("余料")) {
					json.put("sheetcode", curOrderDetail.getSHEETCODE());// 余料推荐
				} else {
					json.put("Material", curOrderDetail.getMATERIAL());
					json.put("Thickness", curOrderDetail.getTHICKNESS());
					json.put("Length", curOrderDetail.getLENGTH());
					json.put("Width", curOrderDetail.getWIDTH());
					json.put("Project", curOrderDetail.getUSEPROJNO());// 实际发料单的工程号
					json.put("assemblyNo", curOrderDetail.getUSEBLOCKNO());// 实际发料单的分段号
				}
				json.put("grantId", curOrderDetail.getGRANTID());
				json.put("flowNo", curOrderDetail.getFLOWNO());
				// json.put("pSortField", "ItemNo desc");// 按层号降序排列
				sendPOST(tag, json, null, this, tag, false);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		} else if (ServerApi.getInstance().API_UPDATASHEETRUST.equals(tag)) {
			// 修改锈蚀状态
			try {
				json = getRequstJson();
				json.put("pSheetId", txt[0]);
				json.put("pSheetRust", txt[1]);
				// json.put("perPage", "9999");// 每页返回的记录数
				sendPOST(tag, json, null, this, tag, false);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		} else if (ServerApi.getInstance().API_SWAPPROJECTNO.equals(tag)) {
			// 交换工程
			try {
				json = getRequstJson();
				json.put("pSheetId", txt[0]);// 原钢板
				json.put("pSwapSheetId", txt[1]);// 目标钢板
				json.put("pStackNo", txt[2]);
				sendPOST(tag, json, null, this, tag, false);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		} else if (ServerApi.getInstance().API_UPDATEOUTSTCKSELECTED
				.equals(tag)) {
			// 选定钢板出库
			try {
				json = getRequstJson();
				json.put("pGrantID", curOrderDetail.getGRANTID());
				json.put("pFlowNo", curOrderDetail.getFLOWNO());
				json.put("pUsedFlag", 2);
				json.put("pUSESTACKITEM", curSelctSheet.getItemNo());
				json.put("pUSESTACKNO", curSelctSheet.getStackNo());
				json.put("pMetalBillId", curSelctSheet.getMetalBillId());
				sendPOST(tag, json, null, this, tag, false);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if (ServerApi.getInstance().API_FINISHSEND.equals(tag)) {
			// 出库接口-批量
			JSONArray jsonArray = new JSONArray();
			json = new JSONObject();
			try {
				JSONObject jo = new JSONObject();
				jo.put("pGrantId", curOrderDetail.getGRANTID());// 发料单号
				jo.put("pFlowNo", curOrderDetail.getFLOWNO());// 流水码
				jo.put("pMetalBillId", curSelctSheet.getMetalBillId());// 出库钢板ID
				jo.put("pSecHouse", curOrderDetail.getGRANTSTACK());// 二级库库房
				jo.put("PType", curOrderDetail.getTYPE());// 请料类型
				jo.put("pStatus", "0");// 状态
				jo.put("pFirstFlag", 0);// 出库
				jo.put("pFlag", 1);// 未传给ERP
				jo.put("pGrantFlag", 3);//
				if (curSelctCrane != null) {
					jo.put("pDcodeId", curSelctCrane.getDCODEID());// 行车
				}
				jo.put("pCreater", ServerApi.account);
				jo.put(ServerApi.PARA_FACTORYCODE, ServerApi.factoryCode);
				json.put(ServerApi.COOKIE_MILLCODE, ServerApi.millCode);
				jsonArray.put(jo);
				json.put("GrantList", jsonArray);
				json.put(ServerApi.COOKIE_PASSPORT, ServerApi.passport);
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
		mLog.init(tag, LOG_TAG);
		mLog.i(LOG_TAG + obj.toString());
		Gson gson = new Gson();
		if (ServerApi.getInstance().API_GETGRANTID.equals(tag)) {
			GrantOrder grantOrder = gson.fromJson(obj.toString(),
					GrantOrder.class);
			if (grantOrder.getResultCode() == 0) {
				grantOrderAdapter = new GrantOrderAdapter(this,
						grantOrder.getList());
				grantOrderListView.setAdapter(grantOrderAdapter);
				if (grantOrder.getList().size() != 0) {
					showSelectNumberPopup();
				}
			} else {
				showShortToast("resultCode==" + grantOrder.getResultCode()
						+ "，请联系开发人员");
			}
		} else if (ServerApi.getInstance().API_GETGRANT_DETAILINFO.equals(tag)) {
			OrderDetail orderDetail = gson.fromJson(obj.toString(),
					OrderDetail.class);
			if (orderDetail.getResultCode() == 0) {
				orderDetailAdapter = new OrderDetailAdapter(this,
						orderDetail.getGrantDetailInfoList());
				orderDetailListView.setAdapter(orderDetailAdapter);
				if (orderDetailAdapter.getCount() == 0) {
					orderName.setText("");
				}
			} else {
				showShortToast("resultCode==" + orderDetail.getResultCode()
						+ "，请联系开发人员");
			}
		} else if (ServerApi.getInstance().API_GET_STACKINFO.equals(tag)) {
			// 获取垛位
			stackLayout.setVisibility(View.VISIBLE);
			sheetLayout.setVisibility(View.GONE);

			if (null != orderDetailAdapter && orderDetailAdapter.getCount() > 0) {
				orderDetailAdapter.updateSelecte();
				orderDetailAdapter.notifyDataSetChanged();
			}
		} else if (ServerApi.getInstance().API_GETSHEETNUMINSTACKRUST
				.equals(tag)) {
			// 获取推荐垛位
			stackLayout.setVisibility(View.VISIBLE);
			sheetLayout.setVisibility(View.GONE);
			Stack stack = gson.fromJson(obj.toString(), Stack.class);
			if (stack.getResultCode() == 0) {
				boolean recom = false;
				List<Stack.ListClass> allList = new ArrayList<Stack.ListClass>();
				for (Stack.ListClass stacklist : stack.getList()) {
					if (("1").equals(stacklist.getFuhe())) {
						// 高亮显示
						stacklist.setRecommend(true);
						recom = true;
					} else {
						stacklist.setRecommend(false);
					}
					allList.add(stacklist);
				}
				stackAdapter = new StackAdapter(this, allList, curStoreType, 0);
				stackGridView.setAdapter(stackAdapter);
				stackAdapter.notifyDataSetChanged();
				if (recom) {
					fromStack = stack.getList().get(0);
					stackTxt.setText("垛位:" + fromStack.getSTACKNAME());
					myNetwork(ServerApi.getInstance().API_ALLSHEETLIST_BYSTACKNO);
				} else {
					showShortToast("未找到符合条件的钢板！");
				}
			} else {
				showShortToast("resultCode==" + stack.getResultCode()
						+ "，请联系开发人员");
			}
		} else if (ServerApi.getInstance().API_ALLSHEETLIST_BYSTACKNO
				.equals(tag)) {
			// 推荐钢板
			int temp = 0;
			Sheet sheetOut = gson.fromJson(obj.toString(), Sheet.class);
			if (sheetOut.getResultCode() == 0) {
				for (Sheet.ListClass sheet : sheetOut.getList()) {
					// 符合查询条件的钢板
					sheet.setChecked(true);
				}
				List<Sheet.ListClass> allList = new ArrayList<Sheet.ListClass>();
				allList.addAll(sheetOut.getList());
				allList.addAll(sheetOut.getAlllist());
				// 按层号降序排序(用Integer类型的对象排序)
				Collections.sort(allList, new Comparator<Sheet.ListClass>() {

					@Override
					public int compare(Sheet.ListClass rhs, Sheet.ListClass lhs) {
						// TODO Auto-generated method stub
						return Integer.valueOf(lhs.getItemNo()).compareTo(
								rhs.getItemNo());
					}
				});
				sheetAdapter = new SheetAdapter(this, allList);
				sheetListView.setAdapter(sheetAdapter);
				for (int i = 0; i < allList.size(); i++) {
					if (allList.get(i).isChecked()) {
						temp = i;
						break;
					}
				}
				final int selection = temp;
				sheetListView.post(new Runnable() {
					@Override
					public void run() {
						// TODO Auto-generated method stub
						sheetListView.setSelection(selection);
					}
				});
				stackLayout.setVisibility(View.GONE);
				sheetLayout.setVisibility(View.VISIBLE);
				findRecommendSteel();
			} else {
				showShortToast("resultCode==" + sheetOut.getResultCode()
						+ "，请联系开发人员");
			}
		} else if (ServerApi.getInstance().API_UPDATEOUTSTCKSELECTED
				.equals(tag)) {
			BaseModel bm = gson.fromJson(obj.toString(), BaseModel.class);
			if (bm.getResultCode() == 0) {
				output();
			} else {
				showShortToast("选板出错，未出库成功");
			}
		} else if (ServerApi.getInstance().API_FINISHSEND.equals(tag)) {
			// SmsApi.getInstance().API_UPSPECIALOUT.equals(tag)
			// SmsApi.getInstance().API_UPDATEPADOUT_STACKSELECTED.equals(tag)
			ResultCode rc = gson.fromJson(obj.toString(), ResultCode.class);
			if (rc.getResultCode() == 0) {
				/**
				 * 返回值： 1、 判断是否在盘点状态 ------1002 <br>
				 * 2、判断行车上是否有此批钢板---------1004 <br>
				 * 3、判断此批钢板是否已入库----------1003<br>
				 */
				if (rc.getCodeList().contains(1002)) {// 正在盘点
					showShortToast("正在盘点，请勿出库");
				} else {
					if (rc.getCodeList().contains(1004)) {
						showShortToast("存在钢板已在行车");
					} else if (rc.getCodeList().contains(1003)) {
						showShortToast("此发料单明细已被出库");
					} else if (rc.getCodeList().contains(1000)) {
						showShortToast("未创建临时库房");
					} else {
						if (null != curSelctCrane) {// 勾选行车
							showShortToast("钢板已发送到行车");
						} else {
							showShortToast("出库成功!");
						}
					}
					myNetwork(ServerApi.getInstance().API_GET_STACKINFO);
					myNetwork(ServerApi.getInstance().API_GETGRANT_DETAILINFO);
					stackLayout.setVisibility(View.VISIBLE);
					sheetLayout.setVisibility(View.GONE);
					// 置空选中的单号详细
					curOrderDetail = null;
					curSelctSheet = null;

					// 置空选中行车
					curSelctCrane = null;
				}
			} else {
				showShortToast("resultCode==" + rc.getResultCode() + "，请联系开发人员");
			}
		} else if (ServerApi.getInstance().API_UPDATASHEETRUST.equals(tag)) {
			// 锈蚀
			BaseModel baseModel = gson
					.fromJson(obj.toString(), BaseModel.class);
			if (baseModel.getResultCode() == 0) {
				// 刷新钢板
				sheetAdapter.notifyDataSetChanged();
				myNetwork(ServerApi.getInstance().API_ALLSHEETLIST_BYSTACKNO);
			} else {
				showShortToast("resultCode==" + baseModel.getResultCode()
						+ "，请联系开发人员");
			}
		} else if (ServerApi.getInstance().API_SWAPPROJECTNO.equals(tag)) {
			// 交换工程
			BaseModel bm = gson.fromJson(obj.toString(), BaseModel.class);
			if (bm.getResultCode() == 0) {
				for (Sheet.ListClass sheet : sheetAdapter.getList()) {
					sheet.setSelected(false);
					sheet.setRecommend(false);
				}
				curSelctSheet = sheetEXpro;
				curSelctSheet.setRecommend(true);
				curSelctSheet.setSelected(true);
				sheetAdapter.notifyDataSetChanged();
				showShortToast("交换成功!");
			} else if (bm.getResultCode() == 1001) {
				showShortToast("钢板已锁定，不能交换工程！");
			} else if (bm.getResultCode() == 1002) {
				showShortToast("请选择规格一致的钢板！");
			} else {
				showShortToast("resultCode==" + bm.getResultCode() + "，请联系开发人员");
			}
		}
	}

	/**
	 * 推荐最优钢板
	 */
	private void findRecommendSteel() {
		// 获取推荐原则
		boolean nearestPath = selctPath.endsWith("0") ? true : false;

		int recommendIndex = -1;
		int tmpItemNo = -1;
		String tmpDataStr = null;
		for (int i = 0; i < sheetAdapter.getCount(); i++) {
			Sheet.ListClass sheet = sheetAdapter.getItem(i);

			// mLog.d("sheet.getSheetrust()==" + sheet.getSheetrust());

			if (sheet.getISRUST() == 0) {// 判断是否为锈蚀的钢板
				if (sheet.isChecked()) {
					// 最短路径
					if (nearestPath) {
						// 获取层号
						int itemNO = sheet.getItemNo() != 0 ? sheet.getItemNo()
								: -1;
						// 获取满足条件的最大层号
						if (itemNO > tmpItemNo) {
							tmpItemNo = itemNO;
							recommendIndex = i;
						}
						// 先进先出
					} else {
						// 获取满足条件的最早时间
						if (CalendarUtil.compareDataString(tmpDataStr,
								sheet.getInTime())) {
							// tmpDataStr比~早返回true
							tmpDataStr = sheet.getInTime();
							recommendIndex = i;
						}
					}
				}
			}
		}
		// 设置推荐钢板
		if (recommendIndex != -1) {
			curSelctSheet = sheetAdapter.getItem(recommendIndex);
			curSelctSheet.setRecommend(true);// 最优的钢板
			curSelctSheet.setSelected(true);// 同时也是选中的钢板
		}
		sheetAdapter.notifyDataSetChanged();
	}

	/**
	 * 显示选择单号下拉框
	 */
	private void showSelectNumberPopup() {
		if (null != numPopupWin && numPopupWin.isShowing()) {
			numPopupWin.dismiss();
		}
		int width = orderLinear.getWidth();
		numPopupWin = new PopupWindow(grantPopView, width,
				ViewGroup.LayoutParams.WRAP_CONTENT);
		numPopupWin.setFocusable(true);
		numPopupWin.setOutsideTouchable(true);
		numPopupWin.setBackgroundDrawable(new BitmapDrawable());
		numPopupWin.setAnimationStyle(R.style.AnimTop2);
		if (grantOrderAdapter.getCount() > 5) {
			numPopupWin
					.setHeight(Screen.getInstance().getScreenHeight() * 4 / 10);
		}
		if (numPopupWin.isShowing()) {
			numPopupWin.dismiss();
		} else {
			int[] position = new int[2];
			orderLeft.getLocationOnScreen(position);
			int height = orderLinear.getHeight();
			int y = position[1] + height;
			numPopupWin.showAtLocation(orderName, Gravity.LEFT | Gravity.TOP,
					position[0], y);
		}
	}

	/**
	 * 钢板信息适配器
	 * 
	 * @author Administrator
	 * 
	 */
	private class SheetAdapter extends BaseAdapter {
		private List<Sheet.ListClass> list;
		private LayoutInflater mInflater;
		// 钢板选项和详情弹出框
		private Dialog sheetOptionDialog, sheetDetailDialog;

		class ViewHolder {
			LinearLayout layout;
			TextView startTxt, endTxt, materialTxt, sizeTxt, checkoutTxt,
					itemnoTxt;
			Button optionBtn;
		}

		public SheetAdapter(Context context, List<Sheet.ListClass> list) {
			this.mInflater = LayoutInflater.from(context);
			this.list = list;
		}

		public List<Sheet.ListClass> getList() {
			return list;
		}

		@Override
		public int getCount() {
			return list != null ? list.size() : 0;
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
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			ViewHolder holder = null;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = mInflater
						.inflate(R.layout.item_lv_outsheet, null);
				holder.layout = (LinearLayout) convertView
						.findViewById(R.id.outsheet_layout);
				holder.startTxt = (TextView) convertView
						.findViewById(R.id.start_txt);
				holder.endTxt = (TextView) convertView
						.findViewById(R.id.end_txt);
				holder.materialTxt = (TextView) convertView
						.findViewById(R.id.outsheet_txt_material);
				holder.sizeTxt = (TextView) convertView
						.findViewById(R.id.outsheet_txt_size);
				holder.checkoutTxt = (TextView) convertView
						.findViewById(R.id.outsheet_txt_checkout);
				holder.itemnoTxt = (TextView) convertView
						.findViewById(R.id.outsheet_txt_itemno);

				holder.optionBtn = (Button) convertView
						.findViewById(R.id.option_btn);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			Sheet.ListClass sheet = list.get(position);
			holder.materialTxt.setText(getString(R.string.material_thickness_)
					+ sheet.getMaterial() + "*" + sheet.getThickness());
			holder.sizeTxt.setText(getString(R.string.length_width_)
					+ sheet.getLength() + "*" + sheet.getWidth());
			holder.checkoutTxt.setText(getString(R.string.checkoutid_)
					+ sheet.getCheckOutId());
			holder.itemnoTxt.setText(getString(R.string.layer_)
					+ sheet.getItemNo());

			if (sheet.isSelected()) {
				// holder.optionTxt.setVisibility(View.VISIBLE);
				holder.startTxt.setVisibility(View.VISIBLE);
				holder.endTxt.setVisibility(View.GONE);
				holder.layout
						.setBackgroundResource(R.drawable.sheet_detail_list_item_selected_bg);
				Animation translateAnimation = new TranslateAnimation(-100.0f,
						0f, 0f, 0f);
				// 设置动画时间
				translateAnimation.setDuration(500);
				holder.layout.startAnimation(translateAnimation);
			} else if (sheet.getISRUST() == 1) {
				holder.layout
						.setBackgroundResource(R.drawable.sheet_detail_head_bg);
				holder.startTxt.setVisibility(View.GONE);
				holder.endTxt.setVisibility(View.VISIBLE);
				// holder.startTxt.setVisibility(View.VISIBLE);
			} else {
				holder.layout
						.setBackgroundResource(R.drawable.sheet_detail_list_item_bg);
				holder.startTxt.setVisibility(View.GONE);
				holder.endTxt.setVisibility(View.VISIBLE);
				// holder.startTxt.setVisibility(View.GONE);
			}
			holder.optionBtn.setOnClickListener(click);
			holder.optionBtn.setTag(sheet);

			return convertView;
		}

		/**
		 * 显示选项按钮弹出框
		 */
		private void showOptionsDialog(Sheet.ListClass sheet) {
			View view = getLayoutInflater().inflate(
					R.layout.dialog_option_output, null);
			// 详情
			view.findViewById(R.id.option_out_btn_detail).setOnClickListener(
					click);
			view.findViewById(R.id.option_out_btn_detail).setTag(sheet);
			// 倒垛
			view.findViewById(R.id.option_out_btn_shift).setOnClickListener(
					click);
			view.findViewById(R.id.option_out_btn_shift).setTag(sheet);
			// 交换工程
			if ("FSK".equals(curStoreType)) {
				view.findViewById(R.id.option_out_btn_project)
						.setOnClickListener(click);
				view.findViewById(R.id.option_out_btn_project).setTag(sheet);
			} else {
				view.findViewById(R.id.option_out_btn_project).setPressed(true);
				view.findViewById(R.id.option_out_btn_project).setClickable(
						false);
			}
			// 锈蚀
			Button rustBtn = (Button) view
					.findViewById(R.id.option_out_btn_rust);
			rustBtn.setOnClickListener(click);
			rustBtn.setTag(sheet);
			if (sheet.getISRUST() == 0) {
				// 若钢板是未锈蚀
				rustBtn.setText(R.string.sheet_rust);
			} else if (sheet.getISRUST() == 1) {
				rustBtn.setText(R.string.sheet_cancelrust);
			}
			// 扫描确认
			view.findViewById(R.id.option_out_btn_scan).setOnClickListener(
					click);
			view.findViewById(R.id.option_out_btn_scan).setTag(sheet);

			sheetOptionDialog = getDialog(OutputActivity.this, view);
			showDialog(sheetOptionDialog);
		}

		/**
		 * 选项弹出框中按钮事件
		 */
		private View.OnClickListener click = new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Sheet.ListClass sheet = (Sheet.ListClass) v.getTag();
				Intent intent = new Intent();
				Bundle bundle = new Bundle();
				switch (v.getId()) {
				case R.id.option_btn: {
					showOptionsDialog(sheet);
				}
					break;
				case R.id.option_out_btn_detail:// 详情按钮
					closeDialog(sheetOptionDialog);
					showDetailDialog(sheet);
					break;
				case R.id.option_out_btn_shift: {
					closeDialog(sheetOptionDialog);
					// 传递需要倒垛的库房类型、库房编号、库房名称、垛位编号、垛位名称
					int num = sheetAdapter.getList().indexOf(sheet);
					double weight = 0.0;
					for (int i = 0; i < num; i++) {
						mLog.e("倒垛钢板：" + "i---->"
								+ sheetAdapter.getList().get(i).getBarcode()
								+ "--->层号：->"
								+ sheetAdapter.getList().get(i).getItemNo());
						weight += Double.parseDouble(sheetAdapter.getList()
								.get(i).getWeight());
					}
					bundle.putInt("num", num);// 需要倒垛的钢板张数
					bundle.putDouble("weight", weight);// 需要倒垛的钢板重量
					bundle.putSerializable("curSelctStore", curSelctStore);
					bundle.putSerializable("curSelctStack", fromStack);
					bundle.putString("fromActivity", LOG_TAG);
					intent.putExtras(bundle);
					intent.setClass(OutputActivity.this, ShiftActivity.class);
					startActivityForResult(intent, 1000);
				}
					break;
				case R.id.option_out_btn_project:// 交换工程按钮
					if (null != curSelctSheet && !sheet.equals(curSelctSheet)) {
						if (sheet.getISRUST() == 1) {
							showShortToast("钢板锈蚀不能交换工程！");
						} else {
							sheetEXpro = sheet;
							myNetwork(
									ServerApi.getInstance().API_SWAPPROJECTNO,
									curSelctSheet.getMetalBillId(),
									sheet.getMetalBillId(),
									fromStack.getSTACKNO());
						}
					}
					closeDialog(sheetOptionDialog);
					break;
				// 锈蚀或者取消锈蚀按钮
				case R.id.option_out_btn_rust:
					if (sheet.getISRUST() == 0) {
						sheet.setISRUST(1);
						sheet.setSelected(false);
						myNetwork(ServerApi.getInstance().API_UPDATASHEETRUST,
								sheet.getMetalBillId(), "1");
					} else if (sheet.getISRUST() == 1) {
						if (!(sheet.isSelected() && sheet.getISRUST() == 0)) {
							sheet.setISRUST(0);
							sheet.setSelected(false);
						}
						myNetwork(ServerApi.getInstance().API_UPDATASHEETRUST,
								sheet.getMetalBillId(), "0");
					}
					closeDialog(sheetOptionDialog);
					break;
				case R.id.option_out_btn_scan:// 扫描确认按钮
					bundle.putSerializable("sheet", sheet);
					bundle.putString("title", "扫描确认");
					bundle.putString("FLAG", LOG_TAG);
					intent.putExtras(bundle);
					intent.setClass(OutputActivity.this, ScanDialog.class);
					startActivityForResult(intent, 1000);

					closeDialog(sheetOptionDialog);
					break;
				default:
					break;
				}
			}
		};

		/**
		 * 显示钢板详情弹出框
		 */
		private void showDetailDialog(Sheet.ListClass sheet) {
			View view = mInflater.inflate(R.layout.dialog_sheetdetail, null);
			((TextView) view.findViewById(R.id.sheetdetail_tv_stackno))
					.setText(sheet.getStackNo());
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
					.setText(sheet.getMaterial());
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
			((TextView) view.findViewById(R.id.sheetdetail_tv_AssemblyId))
					.setText(sheet.getAssemblyId());
			// 入库时间
			((TextView) view.findViewById(R.id.sheetdetail_tv_intime))
					.setText(sheet.getInTime());
			sheetDetailDialog = getDialog(OutputActivity.this, view);
			showDialog(sheetDetailDialog);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		mLog.d("run " + LOG_TAG + " onActivityResult() !");
		if (requestCode == 1000 && resultCode == 1011) {
			// 处理倒垛返回的结果
			myNetwork(ServerApi.getInstance().API_GETSHEETNUMINSTACKRUST);
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

	@Override
	protected void onScanResult(String barcode) {
		// TODO Auto-generated method stub
	}

}

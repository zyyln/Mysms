package com.xuesi.sms.app.activity;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.xuesi.mos.device.Screen;
import com.xuesi.mos.libs.pulltorefresh.PullToRefreshBase;
import com.xuesi.mos.libs.pulltorefresh.PullToRefreshBase.Mode;
import com.xuesi.mos.libs.pulltorefresh.PullToRefreshListView;
import com.xuesi.mos.util.MosLog;
import com.xuesi.sms.R;
import com.xuesi.sms.ServerApi;
import com.xuesi.sms.SmsApplication;
import com.xuesi.sms.SmsConfig;
import com.xuesi.sms.bean.Common;
import com.xuesi.sms.bean.ConfigFieldEntity;
import com.xuesi.sms.bean.FiledSetting;
import com.xuesi.sms.bean.FiledSetting.FiledEntity;
import com.xuesi.sms.bean.GetBillNoAndSheetMsg;
import com.xuesi.sms.bean.GetBillNoAndSheetMsg.BnAndSm;
import com.xuesi.sms.bean.ResultCode;
import com.xuesi.sms.bean.Sheet;
import com.xuesi.sms.util.SmsUtil;
import com.xuesi.sms.widget.adapter.BarcodeAdapter;
import com.xuesi.sms.widget.adapter.BillNoSheetAdapter;
import com.xuesi.sms.widget.adapter.PopuWindowAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * <p>
 * Title: BarcodeBindActivity-条码绑定
 * <p>
 * Description:
 * </p>
 * <p>
 * Company: XSuper
 * </p>
 * 
 * @author XS-PC011
 * @date 2015-9-6
 */
public class BarcodeBindActivity extends ScanBaseActivity implements
		AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener,
		RadioGroup.OnCheckedChangeListener,
		PullToRefreshBase.OnRefreshListener2<ListView> {
	/** LOG */
	private final String TAG_LOG = BarcodeBindActivity.class.getSimpleName();
	private final MosLog mLog = MosLog.getInstance(TAG_LOG, "XUESI-SMS");
	private static final String NO_DATA = "no_data";
	private static final String NO_KEY = "no_key";
	/** progress的次数 */
	private int mProgressNumber = 0;
	/** 选择绑定方式 */
	private RadioGroup BindWayradg;
	/** 记录绑定方式 */
	private String bindWay;
	/** ----获取导入EXCEL的字段设定的信息---- */
	public List<FiledEntity> FiledList = new ArrayList<FiledSetting.FiledEntity>();
	private List<String> segList = new ArrayList<String>();

	/** 条码输入框 */
	private EditText barcodeET;
	/** 选中钢板数(累计数：totalSelectCount) */
	private TextView selectCount;
	/** 选中钢板个数 */
	private int totalSelectCount = 0;
	/** 扫描的条形码列表 */
	private ListView codeListView;
	/** 扫描出的条码列表适配器 */
	private BarcodeAdapter codeAdapter;
	/** 条码数据集合 */
	private List<Common> codeList = new ArrayList<Common>();
	/** 条码操作 */
	private PopupWindow barcodeWindow;
	private View barcodeView;

	// --------------
	// 选择填写信息
	/** 填写信息TITLE */
	private TextView title_tv;
	/** 输入钢板信息布局 */
	private LinearLayout inMsgLayout;
	/** 信息输入 */
	private ListView lvBindMsg;
	private Map<String, String> msgTemp = new HashMap<String, String>();
	private BarcodeMsgAdapter bmAdapter;
	/** PopuWindow适配器 */
	private PopuWindowAdapter BillNoAdapter;

	// -------------
	// 选择采购单号
	/** 选择单号组合框 */
	private LinearLayout billNoLayout;
	/** 选择单号组合框 */
	private LinearLayout selectListnumLinear;
	private TextView ListnumNoTV;
	/** 选择单号弹出框 */
	private View selectBillNoView;
	private PopupWindow selectBillNoWin;
	private ListView selectBillNoListView;
	private List<BnAndSm> popuList = new ArrayList<BnAndSm>();

	/** 钢板信息适配器 */
	private PullToRefreshListView sheetPullToRefreshListView;
	private ListView SheetListView;
	private BillNoSheetAdapter billNoSheetAdapter;
	private List<Sheet.ListClass> sheetBillNoList = new ArrayList<Sheet.ListClass>();

	private View ArrowPopView;
	private PopupWindow ArrowPopWin;
	/** PopuWindow布局中ListView */
	private ListView ArrowListView;
	/** PopuWindow适配器 */
	private PopuWindowAdapter MsgAdapter;
	private int clickId;

	/** 每页显示条数 */
	private final int mPageSize = SmsConfig.pageSize;
	/** 钢板总数 */
	private int mTotal;
	/** 发布钢板数据 */
	private int mRemainData;

	/** 判断是否为滑动加载状态 */
	// private boolean loadStatus = false;
	/** true:绑定条码，false:修改钢板信息 */
	boolean bindOrModify = true;

	/** 钢板详细信息对话框 */
	private Dialog bindWayDialog, sheetDetailDialog;
	private ListView dialogLv;
	/** Dialog确认按钮 */
	private Button dialogBtn;
	/** 钢板选择数量 */
	private TextView SheetSelected;

	/** 列表头部 */
	private LinearLayout mHead;
	private LinearLayout mField;
	/** 存储字段，存储字段对应的数据 */
	public List<ConfigFieldEntity.Segment> fieldList = new ArrayList<ConfigFieldEntity.Segment>();
	public List<ArrayList<ConfigFieldEntity.Segment>> field_valueList = new ArrayList<ArrayList<ConfigFieldEntity.Segment>>();
	/** 当前页码 */
	private int mCurrentNumber = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		SmsApplication.getInstance().addActivity(this);// 添加到Activity堆栈
		setContentView(R.layout.copy_activity_barcode_bind);
		showBindWayDialog();
		loadMsgData();
	}


	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}


	/**
	 * 选择绑定方式
	 */
	private void showBindWayDialog() {
		View view = getLayoutInflater().inflate(R.layout.dialog_bindway, null);
		// 选择绑定方式弹出框
		BindWayradg = (RadioGroup) view.findViewById(R.id.codebind_rg_bindway);
		BindWayradg.setOnCheckedChangeListener(this);
		bindWayDialog = getDialog(BarcodeBindActivity.this, view);
		bindWayDialog.setCanceledOnTouchOutside(false);
		bindWayDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
			@Override
			public boolean onKey(DialogInterface paramDialogInterface,
					int keycode, KeyEvent event) {
				// TODO Auto-generated method stub
				if (keycode == KeyEvent.KEYCODE_BACK) {
					bindWayDialog.dismiss();
					BarcodeBindActivity.this.finish();
					return true;
				} else {
					return false;
				}
			}
		});
		// showDialog(bindWayDialog);
		bindWayDialog.show();
	}

	@Override
	protected void initContentView() {
		// TODO Auto-generated method stub
		super.initContentView();
		// 设置标题
		setTopTitle(R.string.barcodeBinding);
		setRefreshView(View.INVISIBLE);
		mHead = (LinearLayout) findViewById(R.id.head);
		mHead.setFocusable(true);
		mHead.setClickable(true);
		// mHead.setBackgroundColor(Color.parseColor("#b2d235"));
		mHead.setBackgroundResource(R.drawable.sheet_detail_head_bg);
		mHead.setOnTouchListener(new ListViewAndHeadViewTouchLinstener());
		mField = (LinearLayout) findViewById(R.id.field);

		bmAdapter = new BarcodeMsgAdapter(this, FiledList);
		// 填写信息View
		lvBindMsg = (ListView) findViewById(R.id.lv_bindmsg);
		lvBindMsg.setAdapter(bmAdapter);

		// 条形码显示框
		barcodeET = (EditText) findViewById(R.id.codebind_et_bar);
		// 填写信息Title
		title_tv = (TextView) findViewById(R.id.title_tv);
		// 扫描&&确认绑定按钮
		findViewById(R.id.codebind_btn_scan).setOnClickListener(this);
		findViewById(R.id.codebind_btn_confirm).setOnClickListener(this);
		// 显示已经选中的条形码的个数
		selectCount = (TextView) findViewById(R.id.codebing_tv_totalCount);
		selectCount.setText(getString(R.string.accumulative_total_)
				+ totalSelectCount);
		// 扫描的钢板条形码列表
		codeListView = (ListView) findViewById(R.id.codebind_lv_info);
		codeListView.setOnItemClickListener(this);
		codeListView.setOnItemLongClickListener(this);
		codeAdapter = new BarcodeAdapter(this, codeList);
		codeListView.setAdapter(codeAdapter);

		/******************* 选择单号&&钢板信息&&已选中钢板数 ****************/
		billNoLayout = (LinearLayout) findViewById(R.id.codebind_layout_billno);
		inMsgLayout = (LinearLayout) findViewById(R.id.codebind_layout_inMsg);
		/*************************** 单号绑定初始化 *******************************/
		selectListnumLinear = (LinearLayout) findViewById(R.id.codebind_layout_listnum);
		ListnumNoTV = (TextView) findViewById(R.id.codebind_tv_listnumName);
		findViewById(R.id.codebind_btn_listnumArrow).setOnClickListener(this);
		// 选择单号弹出框
		selectBillNoView = getLayoutInflater().inflate(
				R.layout.popuwindow_view, null);
		selectBillNoListView = (ListView) selectBillNoView
				.findViewById(R.id.popup_lv_houseName);
		selectBillNoListView.setOnItemClickListener(this);
		// 根据单号获取的钢板信息
		sheetPullToRefreshListView = (PullToRefreshListView) findViewById(R.id.codebind_lv_sheet);
		sheetPullToRefreshListView.setOnRefreshListener(this);
		sheetPullToRefreshListView.setMode(Mode.DISABLED);
		// sheetPullToRefreshListView.setReleaseLabel("释放开始加载...");
		// sheetPullToRefreshListView.setLastUpdatedLabel("最后加载时间："
		// + new SimpleDateFormat("EEEE yyyy-MM-dd", Locale.CHINA)
		// .format(new Date()));
		// sheetPullToRefreshListView.setPullLabel("上拉加载...");
		// sheetPullToRefreshListView.setRefreshingLabel("加载更多数据...");
		SheetListView = sheetPullToRefreshListView.getRefreshableView();
		billNoSheetAdapter = new BillNoSheetAdapter(this, sheetBillNoList,
				field_valueList, mHead);
		SheetListView.setAdapter(billNoSheetAdapter);
		SheetListView.setOnItemClickListener(this);
		SheetListView.setOnItemLongClickListener(this);
		SheetListView
				.setOnTouchListener(new ListViewAndHeadViewTouchLinstener());

		// 记录选中的钢板数
		SheetSelected = (TextView) findViewById(R.id.codebind_tv_count);
		SheetSelected
				.setText(getString(R.string.billno_already_selected) + "0");
		// 发布单号
		findViewById(R.id.codebind_btn_release).setOnClickListener(this);
		/*************************** 条码绑定初始化 *********************************/
		// 下拉框中列表
		ArrowPopView = getLayoutInflater().inflate(R.layout.popuwindow_view,
				null);
		ArrowListView = (ListView) ArrowPopView
				.findViewById(R.id.popup_lv_houseName);
		ArrowListView.setOnItemClickListener(this);
	}

	public void loadMsgData() {
		myNetwork(ServerApi.getInstance().SEGMENTCONFIG_GETFILEDS);
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		// TODO Auto-generated method stub
		if (group == BindWayradg) {
			((LinearLayout) findViewById(R.id.codebind_layout_middleContent))
					.setVisibility(View.VISIBLE);
			if (checkedId == R.id.codebind_rdbtn_billno) {
				bindWay = "BillNoWay";
				selectListnumLinear.setVisibility(View.VISIBLE);
				billNoLayout.setVisibility(View.VISIBLE);
				inMsgLayout.setVisibility(View.GONE);
			} else if (checkedId == R.id.codebind_rdbtn_inmsg) {
				bindWay = "FillInMsgWay";
				selectListnumLinear.setVisibility(View.GONE);
				billNoLayout.setVisibility(View.GONE);
				inMsgLayout.setVisibility(View.VISIBLE);
			}
			bindWayDialog.dismiss();
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		// super.onClick(v);
		switch (v.getId()) {
		case R.id.bartop_img_back: {
			if (codeList.isEmpty()) {
				finish();
			} else {
				showAlertDialog(this, "提示", "尚有未绑定的条形码，退出后将要重新扫描，是否退出?",
						"finish");
			}
		}
			break;
		case R.id.bartop_img_refresh: {
			loadSheet(0);

			codeList.clear();
			codeAdapter.notifyDataSetChanged();
			totalSelectCount = 0;// 累计数从0开始
			selectCount.setText(getString(R.string.accumulative_total_)
					+ totalSelectCount);
			// 清空各个EditText
			barcodeET.getText().clear();
			// 代码清空界面所有信息
		}
			break;
		// 获取单号
		case R.id.codebind_btn_listnumArrow: {
			myNetwork(ServerApi.getInstance().ORDER_GETBILLNOS,
					Integer.toString(0));
		}
			break;
		// 扫描确定
		case R.id.codebind_btn_scan: {
			String barcode = barcodeET.getText().toString().trim();
			if (SmsUtil.checkString(barcode)) {
				if (judgeBarcode(barcode)) {
					showShortToast("条码已在列表中");
				} else {
					Common cm = new Common();
					cm.setTxt(barcode);
					codeList.add(cm);
					codeAdapter.notifyDataSetChanged();
					barcodeET.getText().clear();
				}
			} else {
				showShortToast("条形码为空！请扫描条形码或手动输入");
			}
		}
			break;
		// 确认绑定
		case R.id.codebind_btn_confirm: {
			// 代码
			if ("FillInMsgWay".equals(bindWay)) {
				if (segList.contains("MATERIAL") || segList.contains("ISREM")) {
					if (getMapValues(msgTemp, "MATERIAL", 1).equals(NO_DATA)) {
						showShortToast("请将绑定信息填写完整，'材质'为必填项！");
					} else if (getMapValues(msgTemp, "ISREM", 1)
							.equals(NO_DATA)) {
						showShortToast("请填写板材类型！");
					}
				}
				if (segList.contains("LENGTH")) {
					if (getMapValues(msgTemp, "LENGTH", 1).equals(NO_DATA)) {
						showShortToast("请将绑定信息填写完整，'长度'为必填项！");
					} else if (!SmsUtil.isPositiveFloatOrInt(getMapValues(
							msgTemp, "LENGTH", 0))) {
						showShortToast("长度无效！");
					}
				}
				if (segList.contains("WIDTH")) {
					if (getMapValues(msgTemp, "WIDTH", 1).equals(NO_DATA)) {
						showShortToast("请将绑定信息填写完整，'宽度'为必填项！");
					} else if (!SmsUtil.isPositiveFloatOrInt(getMapValues(
							msgTemp, "WIDTH", 0))) {
						showShortToast("宽度无效！");
					}
				}
				if (segList.contains("THICKNESS")) {
					if (getMapValues(msgTemp, "THICKNESS", 1).equals(NO_DATA)) {
						showShortToast("请将绑定信息填写完整，'厚度'为必填项！");
					} else if (!SmsUtil.isPositiveFloatOrInt(getMapValues(
							msgTemp, "THICKNESS", 0))) {
						showShortToast("厚度无效！");
					}
				}
				if (segList.contains("ISREM") && segList.contains("SHEETNAME")) {
					if (("1").equals(getMapValues(msgTemp, "ISREM", 1))
							&& getMapValues(msgTemp, "SHEETNAME", 2).equals("")) {
						showShortToast("请填写余料名称！");
					}
				}
				if (segList.contains("AMOUNT")) {
					if (!SmsUtil.isPositiveInt(getMapValues(msgTemp, "AMOUNT",
							0))) {
						showShortToast("钢板数量为正整数!");
					} else if (totalSelectCount != Integer
							.parseInt(getMapValues(msgTemp, "AMOUNT", 0))) {
						showShortToast("条码数量和钢板数量不等！");
					}
					if (segList.contains("ISREM")) {
						if (("1").equals(getMapValues(msgTemp, "ISREM", 1))) {
							if (totalSelectCount > 1
									|| 1 != Integer.parseInt(getMapValues(
											msgTemp, "AMOUNT", 0))) {
								showShortToast("余料只能单张绑定！");
							}
						}
					}
				}
				if (segList.contains("AMOUNT")) {
					if (totalSelectCount == Integer.parseInt(getMapValues(
							msgTemp, "AMOUNT", 0))) {
						// 确定绑定-调用绑定接口
						myNetwork(ServerApi.getInstance().API_INSERTSHEETANDREMNANT);
					}
				} else {
					if (totalSelectCount > 1) {
						showShortToast("因为属性中没有数量字段，条码只能选一张！");

					} else if (totalSelectCount == 1) {
						// 确定绑定-调用绑定接口
						myNetwork(ServerApi.getInstance().API_INSERTSHEETANDREMNANT);
					}
				}
			} else if ("BillNoWay".equals(bindWay)) {
				int size = billNoSheetAdapter.getSelectedList().size();
				if (totalSelectCount == 0) {
					showShortToast("选择要绑定的条形码！");
				} else if (totalSelectCount < size) {
					showShortToast("条形码数量不足！");
				} else if (totalSelectCount > size) {
					showShortToast("条形码数量超额！");
				} else if (totalSelectCount == size) {
					// 单号绑定——确认绑定上传绑定信息
					bindOrModify = true;
					myNetwork(ServerApi.getInstance().API_UPDATABARCODEBYSHEETID);
				}
			}
		}
			break;
		// 发布单号
		case R.id.codebind_btn_release: {
			if (ListnumNoTV.getText().toString().length() > 0) {
				AlertDialog.Builder builder = new Builder(this);
				builder.setTitle("提示");
				builder.setMessage("钢板条码绑定完成，发布此单号？");
				builder.setPositiveButton("全部发布",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.dismiss();
								for (int i = 0; i < sheetBillNoList.size(); i++) {
									sheetBillNoList.get(i).setChecked(true);
								}

								myNetwork(ServerApi.getInstance().API_RELEASEBILLNO);
							}
						});
				builder.setNeutralButton("部分发布",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
								dialog.dismiss();
								if (billNoSheetAdapter.getSelectedList().size() == 0) {
									showShortToast("选择发布的钢板！");
								} else {
									myNetwork(ServerApi.getInstance().API_RELEASEBILLNO);
								}
							}
						});
				builder.setNegativeButton(R.string.cancel,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.dismiss();
							}
						});
				builder.create().show();
			}
		}
			break;
		default:
			break;
		}
	}

	/**
	 * 列表点击事件处理
	 */
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long Id) {
		// TODO Auto-generated method stub
		if (parent == selectBillNoListView) {
			ListnumNoTV.setText(popuList.get(position).getBillsNo());
			loadSheet(0);
			selectBillNoWin.dismiss();
		} else if (parent == codeListView) {// 条形码列表
			if (codeListView.getChoiceMode() == ListView.CHOICE_MODE_MULTIPLE) {
				if (codeList.get(position).isSelected()) {
					totalSelectCount--;
					codeList.get(position).setSelected(false);
				} else {
					totalSelectCount++;
					codeList.get(position).setSelected(true);
				}
			} else if (codeListView.getChoiceMode() == ListView.CHOICE_MODE_SINGLE) {

				if (codeList.get(position).isSelected()) {
					codeList.get(position).setSelected(false);
				} else {
					for (Common cm : codeList) {
						cm.setSelected(false);
					}
					codeList.get(position).setSelected(true);
				}
				totalSelectCount = codeAdapter.getSelectedList().size();

			}
			codeAdapter.notifyDataSetChanged();
			selectCount.setText(getString(R.string.accumulative_total_)
					+ Integer.toString(totalSelectCount));
		} else if (parent == SheetListView) {// 钢板信息表
			if (sheetBillNoList.get(position - 1).isChecked()) {
				sheetBillNoList.get(position - 1).setChecked(false);
			} else {
				sheetBillNoList.get(position - 1).setChecked(true);
			}
			billNoSheetAdapter.notifyDataSetChanged();
			SheetSelected.setText(getString(R.string.billno_already_selected)
					+ billNoSheetAdapter.getSelectedList().size());
		} else if (parent == ArrowListView) {
			FiledList.get(clickId).setEtTxt(
					popuList.get(position).getMactchPara());
			bmAdapter.notifyDataSetChanged();
			ArrowPopWin.dismiss();
		}
	}

	/**
	 * 列表长按
	 */
	@Override
	public boolean onItemLongClick(AdapterView<?> view, View arg1,
			final int position, long id) {
		// TODO Auto-generated method stub
		if (view.equals(codeListView)) {
			showBarcodePop(position, arg1);
			// final int ll = position;

			// Boolean positonState = ListSelected.get(position);
			// if (positonState) {
			// toastShort("请取消选中");
			// } else {
			// ListSelected.remove(position);
			// barcodeScanList.remove(position);
			// codeAdapter.notifyDataSetChanged();
			// }
		} else if (view.equals(SheetListView)) {
			showSheetDetailDialog(sheetBillNoList.get(position - 1));
		}
		return false;
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			if (codeList.isEmpty()) {
				finish();
			} else {
				showAlertDialog(this, "提示", "尚有未绑定的条形码，退出后将要重新扫描，是否退出?",
						"finish");
			}
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
		mLog.w("onPullDownToRefresh");
		loadSheet(1);
	}

	@Override
	protected void onScanResult(String barcode) {
		// TODO Auto-generated method stub
		// update UI to reflect the data
		barcodeET.setText(barcode);

		if (SmsUtil.checkString(barcode)) {
			if (judgeBarcode(barcode)) {
				showShortToast("条码已在列表中");
			} else {
				Common cm = new Common();
				cm.setTxt(barcode);
				codeList.add(cm);
				codeAdapter.notifyDataSetChanged();
				barcodeET.getText().clear();
			}
		}

	}

	/**
	 * 钢板信息下拉显示PopuWindow
	 */
	private void showPopWindow(int clickId, ListView lv) {
		MsgAdapter = new PopuWindowAdapter(this, popuList, 1);
		ArrowListView.setAdapter(MsgAdapter);
		int tem = clickId - lv.getFirstVisiblePosition();
		if (null != ArrowPopWin && ArrowPopWin.isShowing()) {
			ArrowPopWin.dismiss();
		}
		if (popuList.size() == 0) {
			//
			// Toast.makeText(context,
			// context.getResources().getString(R.string.no_date),
			// Toast.LENGTH_SHORT).show();
		} else {
			ArrowPopWin = new PopupWindow(ArrowPopView, lv.getChildAt(tem)
					.getWidth(), ViewGroup.LayoutParams.WRAP_CONTENT);
			if (popuList.size() > 4) {
				ArrowPopWin
						.setHeight(Screen.getInstance().getScreenHeight() * 2 / 10);
			}
			ArrowPopWin.setFocusable(true);
			ArrowPopWin.setOutsideTouchable(true);
			ArrowPopWin.setBackgroundDrawable(new BitmapDrawable());
			ArrowPopWin.setAnimationStyle(R.style.AnimTop2);
			if (ArrowPopWin.isShowing()) {
				ArrowPopWin.dismiss();
			} else {
				int[] position = new int[2];
				if ("BillNoWay".equals(bindWay)) {
					lv.getChildAt(tem).getLocationInWindow(position);
				} else if ("FillInMsgWay".equals(bindWay)) {
					lv.getChildAt(tem).getLocationOnScreen(position);
				}
				int height = lv.getChildAt(tem).getHeight();
				int y = position[1] + height;
				ArrowPopWin.showAtLocation(lv.getChildAt(tem), Gravity.LEFT
						| Gravity.TOP, position[0], y);
			}
		}
	}

	/**
	 * 
	 * judgeBarcode(判断条码是否已存在)
	 * 
	 * @param barcode
	 * @return 存在return true
	 * @exception
	 * @since 1.0.0
	 */
	private boolean judgeBarcode(String barcode) {
		for (int i = 0; i < codeList.size(); i++) {
			if (codeList.get(i).getTxt().equals(barcode)) {
				return true;
			}
		}
		return false;
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
		if (ServerApi.getInstance().SEGMENTCONFIG_GETFILEDS.equals(tag)) {// 获取导入EXCEL的字段设定的信息
			try {
				json = getRequstJson();
				json.put("pMillCode", ServerApi.factoryCode);
				json.put("pTableName", "sheet");
				json.put("ISME", 1);
				sendPOST(tag, json, null, this, tag, false);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		} else if (ServerApi.getInstance().ORDER_GETBILLNOS.equals(tag)) {// 获取发布单号
			try {
				json = getRequstJson();
				json.put("status", Integer.parseInt(txt[0]));
				json.put("perPage", 9999);
				sendPOST(tag, json, null, this, tag, false);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		} else if (ServerApi.getInstance().ORDER_GETORDERLIST.equals(tag)) {// 获取单号下的钢板
			try {
				json = getRequstJson();
				json.put("billNo", txt[0]);
				json.put("flag", Integer.toString(0));
				json.put("status", Integer.toString(0));
				json.put("from", mCurrentNumber);
				json.put("perPage", mPageSize);
				json.put("field", "MATERIAL " + "," + "THICKNESS " + ","
						+ "LENGTH " + "," + "WIDTH ");
				sendPOST(tag, json, null, this, tag, false);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		} else if (ServerApi.getInstance().API_GETSHEETMSG.equals(tag)) {// 获取钢板绑定信息
			try {
				json = getRequstJson();
				json.put("pParamater", txt[0]);
				json.put("pFlag", txt[1]);
				sendPOST(tag, json, null, this, tag, false);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		} else if (ServerApi.getInstance().API_UPDATABARCODEBYSHEETID
				.equals(tag)) {
			// 单号绑定——确认绑定上传绑定信息
			JSONArray jsonArray = new JSONArray();
			json = new JSONObject();

			// 选中的条形码
			List<Common> seleCodeList = codeAdapter.getSelectedList();
			// 选中的钢板
			List<Sheet.ListClass> seleSheetList = billNoSheetAdapter
					.getSelectedList();
			try {
				if (bindOrModify) {
					for (int i = 0; i < seleCodeList.size(); i++) {
						JSONObject jo = new JSONObject();
						jo.put("pSheetId", seleSheetList.get(i)
								.getMetalBillId());
						jo.put("pBarCode", seleCodeList.get(i).getTxt());
						jo.put("pSupplier", seleSheetList.get(i).getSupplier());
						jo.put("pBatchNo", seleSheetList.get(i).getBatchNo());
						jo.put("pProjectId", seleSheetList.get(i)
								.getProjectId());
						jo.put("pAssemblyId", seleSheetList.get(i)
								.getAssemblyId());
						jo.put("pCheckoutId", seleSheetList.get(i)
								.getCheckOutId());
						jo.put("pCertificateNo", seleSheetList.get(i)
								.getCertificateNo());
						jo.put("pData1", seleSheetList.get(i).getDATA1());
						jo.put("pData2", seleSheetList.get(i).getDATA2());
						jo.put("pData3", seleSheetList.get(i).getDATA3());
						jo.put("pData4", seleSheetList.get(i).getDATA4());
						jo.put("pData5", seleSheetList.get(i).getDATA5());
						jo.put("pData6", seleSheetList.get(i).getDATA6());
						jo.put("pRemark", seleSheetList.get(i).getRemark());
						jo.put("pSheetName", seleSheetList.get(i)
								.getSHEETNAME());
						jo.put("pMaterial", seleSheetList.get(i).getMaterial());
						jo.put("pThickNess", seleSheetList.get(i)
								.getThickness());
						jo.put("pLength", seleSheetList.get(i).getLength());
						jo.put("pWidth", seleSheetList.get(i).getWidth());
						jo.put("pMaterialId", seleSheetList.get(i)
								.getMaterialId());
						jo.put("pMaterialName", seleSheetList.get(i)
								.getCodeName());
						jo.put("pUnit", seleSheetList.get(i).getUnit());
						jo.put("pPlatFlow", seleSheetList.get(i).getPLATFLOW());
						jo.put("pShipName", seleSheetList.get(i).getSHIPNAME());
						jo.put("pTranFlag", seleSheetList.get(i).getTRANFLAG());
						jo.put("pPlanNo", seleSheetList.get(i).getPLANNO());
						jo.put("pIsRem", seleSheetList.get(i).getISREM());
						jo.put("pCreater", ServerApi.account);
						jo.put(ServerApi.PARA_FACTORYCODE,
								ServerApi.factoryCode);
						json.put(ServerApi.COOKIE_MILLCODE, ServerApi.millCode);
						jsonArray.put(jo);
					}
				} else {
					JSONObject jo = new JSONObject();
					jo.put("pSheetId", txt[0]);
					jo.put("pBarCode", "");
					jo.put("pSupplier", txt[1]);
					jo.put("pBatchNo", txt[2]);
					jo.put("pProjectId", txt[3]);
					jo.put("pAssemblyId", txt[4]);
					jo.put("pCheckoutId", txt[5]);
					jo.put("pCertificateNo", txt[6]);
					jo.put("pData1", txt[7]);
					jo.put("pData2", txt[8]);
					jo.put("pData3", txt[9]);
					jo.put("pData4", txt[10]);
					jo.put("pData5", txt[11]);
					jo.put("pData6", txt[12]);
					jo.put("pRemark", txt[13]);
					jo.put("pSheetName", txt[14]);
					jo.put("pMaterial", txt[15]);
					jo.put("pThickNess", txt[16]);
					jo.put("pLength", txt[17]);
					jo.put("pWidth", txt[18]);
					jo.put("pMaterialId", txt[19]);
					jo.put("pMaterialName", txt[20]);
					jo.put("pUnit", txt[21]);
					jo.put("pPlatFlow", txt[22]);
					jo.put("pShipName", txt[23]);
					jo.put("pTranFlag", txt[24]);
					jo.put("pPlanNo", txt[25]);
					jo.put("pIsRem", txt[26]);
					jo.put("pCreater", ServerApi.account);
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
		} else if (ServerApi.getInstance().API_INSERTSHEETANDREMNANT
				.equals(tag)) {
			// 填写信息绑定——确认绑定上传绑定信息
			JSONArray jsonArray = new JSONArray();
			json = new JSONObject();
			// 生成采购单号
			String billdNoString = new SimpleDateFormat("yyyyMMddhhmmssS",
					Locale.CHINA).format(new Date());
			try {
				// 选中的条形码
				List<Common> seleCodeList = codeAdapter.getSelectedList();
				for (int i = 0; i < seleCodeList.size(); i++) {
					JSONObject jsonObj = new JSONObject();
					if (segList.contains("ISREM")) {
						jsonObj.put("PISREM", getMapValues(msgTemp, "ISREM", 2));// 是否余料ISREM
					} else {
						jsonObj.put("PISREM", 0);// 是否余料ISREM
					}
					jsonObj.put("PBARCODE", seleCodeList.get(i).getTxt());
					jsonObj.put("PBILLSNO", billdNoString);
					if (("1").equals(getMapValues(msgTemp, "ISREM", 2))) {
						jsonObj.put("PREMAINNAME",
								getMapValues(msgTemp, "SHEETNAME", 2));// 如果选择是余料，则传入的是余料名称
					} else {// 传入的钢板名称
						jsonObj.put("PSHEETNAME",
								getMapValues(msgTemp, "SHEETNAME", 2));
					}
					jsonObj.put(
							"PMATERIALID",
							getMapValues(msgTemp, "MATERIALID", 2).split("\\(")[0]);// 物料编码MATERIALID
					jsonObj.put("PMATERIAL",
							getMapValues(msgTemp, "MATERIAL", 2));// 材质MATERIAL
					jsonObj.put("PLENGTH", getMapValues(msgTemp, "LENGTH", 2));// 长度LENGTH
					jsonObj.put("PWIDTH", getMapValues(msgTemp, "WIDTH", 2));// 宽度WIDTH
					jsonObj.put("PTHICKNESS",
							getMapValues(msgTemp, "THICKNESS", 2));// 厚度THICKNESS
					jsonObj.put("PSUPPLIERID",
							getMapValues(msgTemp, "SUPPLIERID", 2));// 供应商SUPPLIERID
					jsonObj.put("PCERTIFICATENO",
							getMapValues(msgTemp, "CERTIFICATENO", 2));// 船级社证书编号CERTIFICATENO
					jsonObj.put("PPROJECTID",
							getMapValues(msgTemp, "PROJECTID", 2));// 项目号PROJECTID
					jsonObj.put("PASSEMBLYID",
							getMapValues(msgTemp, "ASSEMBLYID", 2));// 部套号ASSEMBLYID
					jsonObj.put("PBATCHNO", getMapValues(msgTemp, "BATCHNO", 2));// 炉批号BATCHNO
					jsonObj.put("PCHECKNO",
							getMapValues(msgTemp, "CHECKOUTID", 2));// 提捡号CHECKOUTID
					jsonObj.put("PPLANNO", getMapValues(msgTemp, "PLANNO", 2));// 计划编号PLANNO
					jsonObj.put("PSHIPNAME",
							getMapValues(msgTemp, "SHIPNAME", 2));// 船级社SHIPNAME
					jsonObj.put("PMATERIALNAME",
							getMapValues(msgTemp, "MATERIALNAME", 2));// 物料名称MATERIALNAME
					jsonObj.put("PTRANFLAG",
							getMapValues(msgTemp, "TRANFLAG", 2));// 转库标志TRANFLAG
					jsonObj.put("PPLATFLOW",
							getMapValues(msgTemp, "PLATFLOW", 2));// 钢板批次PLATFLOW
					jsonObj.put("PSAMPLEINFO",
							getMapValues(msgTemp, "SAMPLEINFO", 2));// 取样信息SAMPLEINFO
					jsonObj.put("PSTEELSPECIFICATION",
							getMapValues(msgTemp, "STEELSPECIFICATION", 2));// 型材规格STEELSPECIFICATION
					jsonObj.put("PDATA1", getMapValues(msgTemp, "DATA1", 2));// DATA1
					jsonObj.put("PDATA2", getMapValues(msgTemp, "DATA2", 2));// DATA2
					jsonObj.put("PDATA3", getMapValues(msgTemp, "DATA3", 2));// DATA3
					jsonObj.put("PDATA4", getMapValues(msgTemp, "DATA4", 2));// DATA4
					jsonObj.put("PDATA5", getMapValues(msgTemp, "DATA5", 2));// DATA5
					jsonObj.put("PDATA6", getMapValues(msgTemp, "DATA6", 2));// DATA6
					jsonObj.put("PREMARK", getMapValues(msgTemp, "REMARK", 2));// REMARK
					jsonObj.put("PCREATER", ServerApi.account);
					jsonObj.put(ServerApi.PARA_FACTORYCODE,
							ServerApi.factoryCode);
					json.put(ServerApi.COOKIE_MILLCODE, ServerApi.millCode);
					jsonArray.put(jsonObj);
				}
				json.put("SheetList", jsonArray);
				json.put(ServerApi.COOKIE_PASSPORT, ServerApi.passport);
				sendPOST(tag, json, null, this, tag, false);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		} else if (ServerApi.getInstance().API_RELEASEBILLNO.equals(tag)) {
			// 发布采购单号
			JSONArray jsonArray = new JSONArray();
			json = new JSONObject();
			try {
				for (int i = 0; i < sheetBillNoList.size(); i++) {
					if (sheetBillNoList.get(i).isChecked()) {
						JSONObject jo = new JSONObject();
						jo.put("billsNo", ListnumNoTV.getText().toString()
								.trim());
						jo.put("status", 1);
						jo.put("metalBillId", sheetBillNoList.get(i)
								.getMetalBillId());
						jo.put(ServerApi.PARA_FACTORYCODE,
								ServerApi.factoryCode);
						json.put(ServerApi.COOKIE_MILLCODE, ServerApi.millCode);
						jsonArray.put(jo);
					}
				}
				json.put("orderList", jsonArray);
				json.put(ServerApi.COOKIE_PASSPORT, ServerApi.passport);
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
		mLog.i("打印JSON数据" + obj.toString());
		if (ServerApi.getInstance().SEGMENTCONFIG_GETFILEDS.equals(str)) {// 获取导入EXCEL的字段设定的信息
			FiledSetting fs = gson.fromJson(obj.toString(), FiledSetting.class);
			if (fs == null) {
				showShortToast("数据解析失败，请联系开发人员！！");
				return;
			}
			if (fs.getResultCode() == 0) {
				FiledList.clear();
				FiledList.addAll(fs.getList());
				msgTemp.clear();
				for (int i = 0; i < FiledList.size(); i++) {
					FiledList.get(i).setPosId(i);
					FiledList.get(i).setEtTxt("");
					msgTemp.put(FiledList.get(i).getSEGMENT(), FiledList.get(i)
							.getEtTxt());
					segList.add(FiledList.get(i).getSEGMENT());
				}
				bmAdapter.notifyDataSetChanged();
			}
		} else if (ServerApi.getInstance().ORDER_GETBILLNOS.equals(str)) {// 获取单号
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
					ListnumNoTV.setText("");
				}
			} else {
				showShortToast("resultCode==" + gbn.getResultCode()
						+ "，请联系开发人员");
			}
		} else if (ServerApi.getInstance().ORDER_GETORDERLIST.equals(str)) {// 根据单号获取钢板
			Sheet gsbn = gson.fromJson(obj.toString(), Sheet.class);
			// 加一行主要是解析失败的，加一个提示，不需要闪退
			if (gsbn == null) {
				showShortToast("数据解析失败，请联系开发人员！！");
				return;
			}
			if (gsbn.getResultCode() == 0) {
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
				++mCurrentNumber;
				sheetBillNoList.addAll(gsbn.getList());
				mLog.e("sheetBillNoList.size()....." + sheetBillNoList.size());

				mTotal = gsbn.getTotal();
				mRemainData = mTotal;

				mLog.e("total....." + mTotal);

				SheetListView.clearChoices();
				billNoSheetAdapter.notifyDataSetChanged();

				SheetSelected
						.setText(getString(R.string.billno_already_selected)
								+ billNoSheetAdapter.getSelectedList().size());
				sheetPullToRefreshListView.onRefreshComplete();
				if (sheetBillNoList.size() == mTotal) {
					showShortToast("数据全部加载完成，没有更多数据");
					sheetPullToRefreshListView
							.setMode(PullToRefreshBase.Mode.DISABLED);
				}

				if (gsbn.getList().size() == 0) {
					ListnumNoTV.setText("");
				}
			} else {
				showShortToast("resultCode==" + gsbn.getResultCode()
						+ "，请联系开发人员");
			}
		} else if (ServerApi.getInstance().API_GETSHEETMSG.equals(str)) {// 下拉获取钢板信息
			GetBillNoAndSheetMsg gbn = gson.fromJson(obj.toString(),
					GetBillNoAndSheetMsg.class);
			// 加一行主要是解析失败的，加一个提示，不需要闪退
			if (gbn == null) {
				showShortToast("数据解析失败，请联系开发人员！！");
				return;
			}
			if (gbn.getResultCode() == 0) {
				popuList.clear();
				popuList.addAll(gbn.getList());

			} else if (gbn.getResultCode() == 1010) {
				popuList.clear();
			} else {
				showShortToast("resultCode==" + gbn.getResultCode()
						+ "，请联系开发人员");
			}
			if ("BillNoWay".equals(bindWay)) {
				showPopWindow(clickId, dialogLv);
			} else if ("FillInMsgWay".equals(bindWay)) {
				showPopWindow(clickId, lvBindMsg);
			}
		} else if (ServerApi.getInstance().API_UPDATABARCODEBYSHEETID
				.equals(str)) {
			// 确认绑定 - 单号
			ResultCode rc = gson.fromJson(obj.toString(), ResultCode.class);
			// 加一行主要是解析失败的，加一个提示，不需要闪退
			if (rc == null) {
				showShortToast("数据解析失败，请联系开发人员！！");
				return;
			}
			if (rc.getResultCode() == 0) {
				int sheetPosition;

				if (rc.getCodeList().contains(1003)) {
					showShortToast("存在已发布的钢板");
				} else {
					if (bindOrModify) {

						// 选中的条形码
						List<Common> seleCodeList = codeAdapter
								.getSelectedList();
						// 选中的钢板
						List<Sheet.ListClass> seleSheetList = billNoSheetAdapter
								.getSelectedList();

						for (int i = rc.getCodeList().size() - 1; i >= 0; i--) {
							if (rc.getCodeList().get(i) == 0) {
								// 删除成功绑定条码
								codeList.remove(seleCodeList.get(i));

								// 清除scanSheetListView中所有之前的选择
								codeListView.clearChoices();
								totalSelectCount--;
								selectCount
										.setText(getString(R.string.accumulative_total_)
												+ totalSelectCount);

								// 钢板
								sheetPosition = sheetBillNoList
										.indexOf(seleSheetList.get(i));
								sheetBillNoList.get(sheetPosition).setBarcode(
										seleCodeList.get(i).getTxt());
								sheetBillNoList.get(sheetPosition).setChecked(
										false);
								sheetBillNoList.get(sheetPosition)
										.setIsBarcode(1);// 只要isBarcode>0,条码就会显示在界面上
							}
						}
						codeAdapter.notifyDataSetChanged();
						billNoSheetAdapter.notifyDataSetChanged();
						SheetSelected
								.setText(getString(R.string.billno_already_selected)
										+ billNoSheetAdapter.getSelectedList()
												.size());

						if (rc.getCodeList().contains(1002)) {
							showShortToast("条形码已存在！");
						}
					} else {
						mLog.w("修改成功");
					}
				}
			} else {
				showShortToast("绑定失败\n" + "resultCode==" + rc.getResultCode()
						+ "，请联系开发人员");
			}
		} else if (ServerApi.getInstance().API_INSERTSHEETANDREMNANT
				.equals(str)) {
			// 确认绑定 - 填写信息
			ResultCode rc = gson.fromJson(obj.toString(), ResultCode.class);
			// 加一行主要是解析失败的，加一个提示，不需要闪退
			if (rc == null) {
				showShortToast("数据解析失败，请联系开发人员！！");
				return;
			}
			if (rc.getResultCode() == 0) {
				// 选中的条形码
				List<Common> seleCodeList = codeAdapter.getSelectedList();

				if (rc.getCodeList().contains(1002)) {
					showShortToast("条形码已存在！");
					for (int i = rc.getCodeList().size() - 1; i >= 0; i--) {
						if (rc.getCodeList().get(i) == 0) {
							codeList.remove(seleCodeList.get(i));// 删除绑定成功的条码
							codeListView.clearChoices();
							// 清除scanSheetListView中所有之前的选择
							totalSelectCount--;
							selectCount
									.setText(getString(R.string.accumulative_total_)
											+ totalSelectCount);
						}
					}
				} else if (rc.getCodeList().contains(1008)) {
					showShortToast("余料名称已存在");
				} else {
					// 删除绑定成功的条形码
					codeList.removeAll(seleCodeList);
					for (int i = 0; i < FiledList.size(); i++) {
						FiledList.get(i).setEtTxt("");
					}
					bmAdapter.notifyDataSetChanged();
					codeListView.clearChoices();// 清除scanSheetListView中所有之前的选择
					totalSelectCount = 0;// 累计数从0开始
					selectCount.setText(getString(R.string.accumulative_total_)
							+ totalSelectCount);
				}
				codeAdapter.notifyDataSetChanged();
			} else {
				showShortToast("绑定失败\n" + "resultCode==" + rc.getResultCode()
						+ "，请联系开发人员");
			}
		} else if (ServerApi.getInstance().API_RELEASEBILLNO.equals(str)) {
			// 发布单号后更新
			ResultCode rc = gson.fromJson(obj.toString(), ResultCode.class);
			if (rc.getResultCode() == 0) {
				// if (rc.getCodeList().contains(2003)) {
				// showShortToast("存在已发布钢板");
				// } else {
				if (rc.getCodeList().contains(3001)) {
					showShortToast("存在余料名称相同的板材，请检查！");
					// 重新获取钢板，以达到刷新的目的
					loadSheet(0);
				} else {
					// 选中的钢板
					List<Sheet.ListClass> choiceSheet = billNoSheetAdapter
							.getSelectedList();
					// 记录数据还剩下多少
					mRemainData = mRemainData - choiceSheet.size();
					showShortToast("本次成功发布:" + choiceSheet.size() + "条数据，"
							+ "剩余：" + mRemainData + "条数据");

					// 钢板集合和返回集合大小一致，则说明发布的内容是当前全部加载的钢板
					// if (sheetBillNoList.size() == rc.getCodeList().size()) {
					loadSheet(0);

					sheetBillNoList.removeAll(choiceSheet);
					billNoSheetAdapter.notifyDataSetChanged();
					SheetSelected
							.setText(getString(R.string.billno_already_selected)
									+ "0");
				}
			} else {
				showShortToast("resultCode==" + rc.getResultCode() + "，请联系开发人员");
			}
		} else if (ServerApi.getInstance().API_GETTABLEHEAD.equals(str)) {
			ConfigFieldEntity cfEntity = gson.fromJson(obj.toString(),
					ConfigFieldEntity.class);
			if (cfEntity.getResultCode() == 0) {
				fieldList = cfEntity.getList();
				addView(mField, fieldList, 0);
				myNetwork(ServerApi.getInstance().ORDER_GETORDERLIST,
						ListnumNoTV.getText().toString().trim());
			}
		}
	}

	private void loadSheet(int status) {
		if (status == 0) {
			mCurrentNumber = 1;
			sheetBillNoList.clear();
			fieldList.clear();
			field_valueList.clear();
		}
		myNetwork(ServerApi.getInstance().API_GETTABLEHEAD);
	}

	/**
	 * 入库时间
	 */
	public String putDataMethod(String putdate) {
		String putData = null;
		if (putdate.length() == 0) {
			putData = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA)
					.format(new Date());
		} else {
			putData = putdate;
		}
		return putData;
	}

	/**
	 * @param map
	 * @param key
	 * @param model
	 *            0：判断有值，1：判断有值且不为空,2:有值但为空
	 * @return
	 */
	public String getMapValues(Map<String, String> map, String key, int model) {
		if (map.get(key) != null && model == 0) {
			return map.get(key);
		} else if (map.get(key) != null && model == 1) {
			if (map.get(key).equals("")) {
				return NO_DATA;
			} else {
				return map.get(key);
			}
		} else if (map.get(key) != null && model == 2) {
			if (map.get(key).equals("")) {
				return "";
			} else {
				return map.get(key);
			}
		} else {
			return "";
		}
	}

	/**
	 * @param map
	 * @param key
	 * @param value
	 */
	public void addMapValues(Map<String, String> map, String key, String value) {
		if (map != null) {
			if (map.containsKey(key)) {
				map.put(key, value);
				for (int i = 0; i < FiledList.size(); i++) {
					if (FiledList.get(i).getSEGMENT().equals(key)) {
						if (value == null) {
							FiledList.get(i).setEtTxt("");
						} else {
							FiledList.get(i).setEtTxt(value);
						}
					}
				}
			}
		}
	}

	/**
	 * 长按删除条码
	 * 
	 * @param position
	 * @param view
	 */
	private void showBarcodePop(final int position, View view) {
		if (barcodeWindow != null && barcodeWindow.isShowing()) {
			barcodeWindow.dismiss();
		}
		barcodeView = getLayoutInflater().inflate(R.layout.popup_barcode, null);
		TextView ctView = (TextView) barcodeView.findViewById(R.id.item_btn);
		ctView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View paramView) {
				// TODO Auto-generated method stub
				if (codeList.get(position).isSelected()) {
					totalSelectCount--;
					selectCount.setText(getString(R.string.accumulative_total_)
							+ Integer.toString(totalSelectCount));
				}
				codeList.remove(position);
				codeAdapter.notifyDataSetChanged();
				barcodeWindow.dismiss();
			}
		});
		TextView xtView = (TextView) barcodeView.findViewById(R.id.item_choice);
		xtView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View paramView) {
				// TODO Auto-generated method stub
				for (int i = 0; i < codeList.size(); i++) {
					codeList.get(i).setSelected(true);
				}
				codeAdapter.notifyDataSetChanged();
				totalSelectCount = codeList.size();
				selectCount.setText(getString(R.string.accumulative_total_)
						+ totalSelectCount);
				barcodeWindow.dismiss();
			}
		});
		TextView nxtView = (TextView) barcodeView
				.findViewById(R.id.item_notChoice);
		nxtView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View paramView) {
				// TODO Auto-generated method stub
				for (int i = 0; i < codeList.size(); i++) {
					codeList.get(i).setSelected(false);
				}
				codeAdapter.notifyDataSetChanged();
				totalSelectCount = 0;
				selectCount.setText(getString(R.string.accumulative_total_)
						+ totalSelectCount);
				barcodeWindow.dismiss();
			}
		});
		barcodeWindow = new PopupWindow(barcodeView,
				ViewGroup.LayoutParams.WRAP_CONTENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);
		barcodeWindow.setFocusable(true);
		barcodeWindow.setOutsideTouchable(true);
		// 不加这行点击空白处POP不消失
		barcodeWindow.setBackgroundDrawable(new BitmapDrawable());
		if (barcodeWindow.isShowing()) {
			barcodeWindow.dismiss();
		} else {
			int[] location = new int[2];
			barcodeView.measure(0, 0);
			// 获得当前item的位置x与y
			view.getLocationOnScreen(location);
			barcodeWindow.showAtLocation(
					codeAdapter.getView(position, null, codeListView),
					Gravity.LEFT | Gravity.TOP, location[0], location[1]
							- barcodeView.getMeasuredHeight());
		}
	}

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
	 * 长按显示钢板详细信息
	 */
	private void showSheetDetailDialog(final Sheet.ListClass sheet) {
		View view = getLayoutInflater().inflate(R.layout.dialog_bindmsg, null);
		dialogLv = (ListView) view.findViewById(R.id.lv_dialogBindmsg);
		dialogLv.setAdapter(bmAdapter);
		addMapValues(msgTemp, "SHEETNAME", sheet.getSHEETNAME());
		addMapValues(msgTemp, "MATERIAL", sheet.getMaterial());
		addMapValues(msgTemp, "THICKNESS", sheet.getThickness());
		addMapValues(msgTemp, "LENGTH", sheet.getLength());
		addMapValues(msgTemp, "WIDTH", sheet.getWidth());
		addMapValues(msgTemp, "SHIPNAME", sheet.getSHIPNAME());
		addMapValues(msgTemp, "CERTIFICATENO", sheet.getCertificateNo());
		addMapValues(msgTemp, "MATERIALID", sheet.getMaterialId());
		addMapValues(msgTemp, "MATERIALNAME", sheet.getCodeName());
		addMapValues(msgTemp, "PROJECTID", sheet.getProjectId());
		addMapValues(msgTemp, "ASSEMBLYID", sheet.getAssemblyId());
		addMapValues(msgTemp, "PLANNO", sheet.getPLANNO());
		addMapValues(msgTemp, "TRANFLAG", sheet.getTRANFLAG());
		addMapValues(msgTemp, "SUPPLIERID", sheet.getSupplier());
		addMapValues(msgTemp, "BATCHNO", sheet.getBatchNo());
		addMapValues(msgTemp, "PLATFLOW", sheet.getPLATFLOW());
		addMapValues(msgTemp, "SAMPLEINFO", sheet.getSAMPLEINFO());
		addMapValues(msgTemp, "CHECKOUTID", sheet.getCheckOutId());
		addMapValues(msgTemp, "STEELSPECIFICATION",
				sheet.getSTEELSPECIFICATION());
		addMapValues(msgTemp, "ISREM", sheet.getISREM());
		addMapValues(msgTemp, "AMOUNT", "1");
		addMapValues(msgTemp, "DATA1", sheet.getDATA1());
		addMapValues(msgTemp, "DATA2", sheet.getDATA2());
		addMapValues(msgTemp, "DATA3", sheet.getDATA3());
		addMapValues(msgTemp, "DATA4", sheet.getDATA4());
		addMapValues(msgTemp, "DATA5", sheet.getDATA5());
		addMapValues(msgTemp, "DATA6", sheet.getDATA6());
		addMapValues(msgTemp, "REMARK", sheet.getRemark());
		bmAdapter.notifyDataSetChanged();
		dialogBtn = (Button) view.findViewById(R.id.billNo_confirm);
		dialogBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				if (segList.contains("MATERIAL") || segList.contains("ISREM")) {
					if (getMapValues(msgTemp, "MATERIAL", 1).equals(NO_DATA)) {
						showShortToast("请将绑定信息填写完整，'材质'为必填项！");
					} else if (getMapValues(msgTemp, "ISREM", 1)
							.equals(NO_DATA)) {
						showShortToast("请填写板材类型！");
					}
				}
				if (segList.contains("LENGTH")) {
					if (getMapValues(msgTemp, "LENGTH", 1).equals(NO_DATA)) {
						showShortToast("请将绑定信息填写完整，'长度'为必填项！");
					} else if (!SmsUtil.isPositiveFloatOrInt(getMapValues(
							msgTemp, "LENGTH", 0))) {
						showShortToast("长度无效！");
					}
				}
				if (segList.contains("WIDTH")) {
					if (getMapValues(msgTemp, "WIDTH", 1).equals(NO_DATA)) {
						showShortToast("请将绑定信息填写完整，'宽度'为必填项！");
					} else if (!SmsUtil.isPositiveFloatOrInt(getMapValues(
							msgTemp, "WIDTH", 0))) {
						showShortToast("宽度无效！");
					}
				}
				if (segList.contains("THICKNESS")) {
					if (getMapValues(msgTemp, "THICKNESS", 1).equals(NO_DATA)) {
						showShortToast("请将绑定信息填写完整，'厚度'为必填项！");
					} else if (!SmsUtil.isPositiveFloatOrInt(getMapValues(
							msgTemp, "THICKNESS", 0))) {
						showShortToast("厚度无效！");
					}
				}
				if (segList.contains("ISREM") && segList.contains("SHEETNAME")) {
					if (("1").equals(getMapValues(msgTemp, "ISREM", 1))
							&& getMapValues(msgTemp, "SHEETNAME", 2).equals("")) {
						showShortToast("请填写余料名称！");
					}
				}
				if (segList.contains("AMOUNT")) {
					if (!SmsUtil.isPositiveInt(getMapValues(msgTemp, "AMOUNT",
							0))) {
						showShortToast("钢板数量为正整数!");
					} else if (1 != Integer.parseInt(getMapValues(msgTemp,
							"AMOUNT", 0))) {
						showShortToast("钢板数量为1");
					}
				}
				if (segList.contains("SHEETNAME")) {
					sheet.setSHEETNAME(getMapValues(msgTemp, "SHEETNAME", 2));
				}
				if (segList.contains("MATERIAL")) {
					sheet.setMaterial(getMapValues(msgTemp, "MATERIAL", 2));
				}
				if (segList.contains("THICKNESS")) {
					sheet.setThickness(getMapValues(msgTemp, "THICKNESS", 2));
				}
				if (segList.contains("LENGTH")) {
					sheet.setLength(getMapValues(msgTemp, "LENGTH", 2));
				}
				if (segList.contains("WIDTH")) {
					sheet.setWidth(getMapValues(msgTemp, "WIDTH", 2));
				}
				if (segList.contains("SHIPNAME")) {
					sheet.setSHIPNAME(getMapValues(msgTemp, "SHIPNAME", 2));
				}
				if (segList.contains("CERTIFICATENO")) {
					sheet.setCertificateNo(getMapValues(msgTemp,
							"CERTIFICATENO", 2));
				}
				if (segList.contains("MATERIALID")) {
					sheet.setMaterialId(getMapValues(msgTemp, "MATERIALID", 2));
				}
				if (segList.contains("MATERIALNAME")) {
					sheet.setCodeName(getMapValues(msgTemp, "MATERIALNAME", 2));
				}
				if (segList.contains("PROJECTID")) {
					sheet.setProjectId(getMapValues(msgTemp, "PROJECTID", 2));
				}
				if (segList.contains("ASSEMBLYID")) {
					sheet.setAssemblyId(getMapValues(msgTemp, "ASSEMBLYID", 2));
				}
				if (segList.contains("PLANNO")) {
					sheet.setPLANNO(getMapValues(msgTemp, "PLANNO", 2));
				}
				if (segList.contains("TRANFLAG")) {
					sheet.setTRANFLAG(getMapValues(msgTemp, "TRANFLAG", 2));
				}
				if (segList.contains("SUPPLIERID")) {
					sheet.setSupplier(getMapValues(msgTemp, "SUPPLIERID", 2));
				}
				if (segList.contains("BATCHNO")) {
					sheet.setBatchNo(getMapValues(msgTemp, "BATCHNO", 2));
				}
				if (segList.contains("PLATFLOW")) {
					sheet.setPLATFLOW(getMapValues(msgTemp, "PLATFLOW", 2));
				}
				if (segList.contains("SAMPLEINFO")) {
					sheet.setSAMPLEINFO(getMapValues(msgTemp, "SAMPLEINFO", 2));
				}
				if (segList.contains("CHECKOUTID")) {
					sheet.setCheckOutId(getMapValues(msgTemp, "CHECKOUTID", 2));
				}
				if (segList.contains("STEELSPECIFICATION")) {
					sheet.setSTEELSPECIFICATION(getMapValues(msgTemp,
							"STEELSPECIFICATION", 2));
				}
				if (segList.contains("ISREM")) {
					sheet.setISREM(getMapValues(msgTemp, "ISREM", 2));
				}
				if (segList.contains("DATA1")) {
					sheet.setDATA1(getMapValues(msgTemp, "DATA1", 2));
				}
				if (segList.contains("DATA2")) {
					sheet.setDATA2(getMapValues(msgTemp, "DATA2", 2));
				}
				if (segList.contains("DATA3")) {
					sheet.setDATA3(getMapValues(msgTemp, "DATA3", 2));
				}
				if (segList.contains("DATA4")) {
					sheet.setDATA4(getMapValues(msgTemp, "DATA4", 2));
				}
				if (segList.contains("DATA5")) {
					sheet.setDATA5(getMapValues(msgTemp, "DATA5", 2));
				}
				if (segList.contains("DATA6")) {
					sheet.setDATA6(getMapValues(msgTemp, "DATA6", 2));
				}
				if (segList.contains("REMARK")) {
					sheet.setRemark(getMapValues(msgTemp, "REMARK", 2));
				}
				billNoSheetAdapter.notifyDataSetChanged();
				bindOrModify = false;
				mLog.e(sheet.getRefreshTime());
				myNetwork(ServerApi.getInstance().API_UPDATABARCODEBYSHEETID,
						sheet.getMetalBillId(), sheet.getSupplier(),
						sheet.getBatchNo(), sheet.getProjectId(),
						sheet.getAssemblyId(), sheet.getCheckOutId(),
						sheet.getCertificateNo(), sheet.getDATA1(),
						sheet.getDATA2(), sheet.getDATA3(), sheet.getDATA4(),
						sheet.getDATA5(), sheet.getDATA6(), sheet.getRemark(),
						sheet.getSHEETNAME(), sheet.getMaterial(),
						sheet.getThickness(), sheet.getLength(),
						sheet.getWidth(), sheet.getMaterialId(),
						sheet.getCodeName(), sheet.getUnit(),
						sheet.getPLATFLOW(), sheet.getSHIPNAME(),
						sheet.getTRANFLAG(), sheet.getPLANNO(),
						sheet.getISREM());
				sheetDetailDialog.dismiss();
			}

		});
		sheetDetailDialog = getDialog(BarcodeBindActivity.this, view);
		showDialog(sheetDetailDialog);
	}

	public void addView(final LinearLayout lineLayout,
			List<ConfigFieldEntity.Segment> list, int Flag) {
		lineLayout.removeAllViews();
		for (int i = 0; i < list.size(); i++) {
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

	class BarcodeMsgAdapter extends BaseAdapter {
		Context context;
		private LayoutInflater mInflater;
		private List<FiledEntity> list;
		private List<BnAndSm> zeroOne = new ArrayList<BnAndSm>();
		viewHolder holder = null;
		private int index = -1;

		public class viewHolder {
			TextView tv_bindmsg;
			EditText et_bindmsg;
			ImageButton imgbtn_bindmsg;
		}

		public BarcodeMsgAdapter(Context context, List<FiledEntity> FiledList) {
			// TODO Auto-generated constructor stub
			this.context = context;
			this.mInflater = LayoutInflater.from(context);
			this.list = FiledList;
			zeroOne.add(new GetBillNoAndSheetMsg().new BnAndSm("0"));
			zeroOne.add(new GetBillNoAndSheetMsg().new BnAndSm("1"));
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return list != null ? list.size() : 0;
		}

		@Override
		public Object getItem(int position) {
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
			if (null == convertView) {
				holder = new viewHolder();
				convertView = mInflater.inflate(R.layout.item_bindmsg, null);
				holder.tv_bindmsg = (TextView) convertView
						.findViewById(R.id.tv_bindmsg);
				holder.et_bindmsg = (EditText) convertView
						.findViewById(R.id.et_bindmsg);
				holder.et_bindmsg.setTag(position);

				holder.imgbtn_bindmsg = (ImageButton) convertView
						.findViewById(R.id.imgbtn_bindmsg);
				holder.imgbtn_bindmsg.setOnClickListener(click);
				convertView.setTag(holder);
			} else {
				holder = (viewHolder) convertView.getTag();
				holder.et_bindmsg.setTag(position);
			}
			holder.tv_bindmsg.setText(list.get(position).getSEGMENTNAME());
			holder.et_bindmsg.setText(list.get(position).getEtTxt());
			FiledEntity be = list.get(position);
			holder.imgbtn_bindmsg.setTag(be);
			int tag = (Integer) holder.et_bindmsg.getTag();
			if (list.get(tag) != null
					&& !list.get(tag).getEtTxt().toString().equals("")) {
				holder.et_bindmsg.setText(list.get(tag).getEtTxt().toString());
			} else {
				holder.et_bindmsg.setText(list.get(position).getEtTxt()
						.toString());
			}
			holder.et_bindmsg.addTextChangedListener(new myWatcher(holder));
			holder.et_bindmsg.setOnTouchListener(new View.OnTouchListener() {
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					if (event.getAction() == MotionEvent.ACTION_UP) {
						index = (Integer) v.getTag();
					}
					return false;
				}
			});
			holder.et_bindmsg.clearFocus();
			if (index != -1 && index == position) {
				holder.et_bindmsg.requestFocus();
			}
			return convertView;
		}

		private class myWatcher implements TextWatcher {
			private BarcodeMsgAdapter.viewHolder vHolder;

			public myWatcher(BarcodeMsgAdapter.viewHolder holder) {
				this.vHolder = holder;
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
				int pos = (Integer) vHolder.et_bindmsg.getTag();
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start,
					int before, int count) {
				// TODO Auto-generated method stub
			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub5
				int pos = (Integer) vHolder.et_bindmsg.getTag();
				list.get(pos).setEtTxt(s.toString());
				msgTemp.put(list.get(pos).getSEGMENT(), list.get(pos)
						.getEtTxt());
				// map.put(pos, s.toString());
			}
		}

		// 按钮点击事件
		private View.OnClickListener click = new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				FiledEntity bme = (FiledEntity) v.getTag();
				switch (v.getId()) {
				case R.id.imgbtn_bindmsg:
					// if (list.get(bme.getId()).getStatus() == 100) {
					//
					// } else {
					clickId = bme.getPosId();
					if (bme.getSEGMENT().equals("TRANFLAG")
							|| bme.getSEGMENT().equals("ISREM")) {
						popuList.clear();
						popuList.addAll(zeroOne);
						if ("BillNoWay".equals(bindWay)) {
							showPopWindow(clickId, dialogLv);
						} else if ("FillInMsgWay".equals(bindWay)) {
							showPopWindow(clickId, lvBindMsg);
						}
					} else {
						myNetwork(ServerApi.getInstance().API_GETSHEETMSG, list
								.get(bme.getPosId()).getEtTxt(),
								bme.getSEGMENT());
					}

					// }
					break;
				default:
					break;
				}
			}

		};

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

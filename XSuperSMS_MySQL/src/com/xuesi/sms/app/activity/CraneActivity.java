package com.xuesi.sms.app.activity;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.xuesi.mos.util.MosLog;
import com.xuesi.sms.R;
import com.xuesi.sms.ServerApi;
import com.xuesi.sms.SmsApplication;
import com.xuesi.sms.bean.BaseModel;
import com.xuesi.sms.bean.CraneByType;
import com.xuesi.sms.bean.CraneWorkDetail;
import com.xuesi.sms.widget.adapter.CraneAdapter;
import com.xuesi.sms.widget.adapter.CraneWorkAdapter;

/**
 * 行车作业面板
 * 
 * @author XS-PC014
 * 
 */
public class CraneActivity extends SmsActivity implements
		AdapterView.OnItemClickListener {
	/** LOG */
	private final String LOG_TAG = CraneActivity.class.getSimpleName();
	private final MosLog mLog = MosLog.getInstance(LOG_TAG, "XUESI-SMS");
	/** progress的次数 */
	private int mProgressNumber = 0;

	// 行车列表弹出框----
	/** 行车弹出框布局 */
	private RelativeLayout craneLayout;
	private GridView craneGridView;
	/** 行车适配器 */
	private CraneAdapter craneAdapter;
	/** 当前行车 */
	private CraneByType.ListClass currentCrane;

	// 作业列表----
	/** 作业列表视图 */
	private ListView workListView;
	/** 作业数据适配器 */
	private CraneWorkAdapter workAdapter;
	/** 来源地,携带信息,目的地 */
	private TextView currentFromTxt, amountAndWeightTxt, currentToTxt;
	/** 作业数和钢板数合计 */
	private TextView amountTxt;

	/** 红色箭头 */
	private ImageView arrows;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		SmsApplication.getInstance().addActivity(this);// 添加到Activity堆栈
		setContentView(R.layout.activity_crane_panel);

		myNetwork(ServerApi.getInstance().API_GET_WORKPANEL_CRANES);
	}

	@Override
	protected void initContentView() {
		// TODO Auto-generated method stub
		super.initContentView();
		setTopTitle(R.string.crane_work_panel);
		// 行车选择
		((TextView) findViewById(R.id.selectcrane_tv_title))
				.setText(getString(R.string.msg_selecte_crane));
		craneLayout = (RelativeLayout) findViewById(R.id.selectcrane_layout);

		craneGridView = (GridView) findViewById(R.id.selectcrane_gv_crane);
		craneGridView.setOnItemClickListener(this);

		// 作业面板
		workListView = (ListView) findViewById(R.id.crane_work_panel_listview);

		// 行车确定按钮、完成按钮
		findViewById(R.id.selectcrane_btn_confirm).setOnClickListener(this);
		findViewById(R.id.cranepanel_btn_finish).setOnClickListener(this);

		currentFromTxt = (TextView) findViewById(R.id.crane_work_panel_tv_from);
		currentToTxt = (TextView) findViewById(R.id.crane_work_panel_tv_to);
		amountAndWeightTxt = (TextView) findViewById(R.id.crane_work_panel_tv_qtyandweight);
		// 作业数、钢板数
		amountTxt = (TextView) findViewById(R.id.crane_work_panel_tv_amount);

		arrows = (ImageView) findViewById(R.id.crane_work_panel_imageview);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.bartop_img_back: {// 返回
			backPrompt();
		}
			break;
		case R.id.bartop_img_refresh: {// 刷新
			if (!(craneLayout.getVisibility() == View.VISIBLE)) {
				myNetwork(ServerApi.getInstance().API_GET_BRICRANE_WORKPANEL);
			}
		}
			break;
		case R.id.selectcrane_btn_confirm: {// 行车选择确认按钮
			if (null != currentCrane) {
				craneLayout.setVisibility(View.GONE);
				setTopTitle(currentCrane.getDNAME()
						+ getString(R.string.work_panel));
				myNetwork(ServerApi.getInstance().API_GET_BRICRANE_WORKPANEL);

				// new AlertDialog.Builder(this).setTitle(R.string.prompt)
				// .setMessage("是否确认选择" + currentCrane.getDCODEID())
				// .setPositiveButton(getString(R.string.confirm), new
				// OnClickListener() {
				// public void onClick(DialogInterface dialog, int which) {
				// setTopTitle(currentCrane.getDNAME() +
				// getString(R.string.work_panel));
				// myNetwork(SmsApi.getInstance().API_GET_BRICRANE_WORKPANEL);
				// }
				// }).setNegativeButton(getString(R.string.cancel), new
				// OnClickListener() {
				// public void onClick(DialogInterface dialog, int which) {
				// craneLayout.setVisibility(View.VISIBLE);
				// }
				// }).setCancelable(false).show();
			} else {
				showShortToast("对不起，请选择行车");
			}
		}
			break;
		case R.id.cranepanel_btn_finish: {// 完成按钮
			if (null != workAdapter && workAdapter.getCount() > 0) {
				new AlertDialog.Builder(this)
						.setTitle(R.string.prompt)
						.setMessage(R.string.msg_crane_finish)
						.setPositiveButton(R.string.confirm,
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int which) {
										myNetwork(ServerApi.getInstance().API_UPDATEBRICRANE_WORKPANEL);
									}
								}).setNegativeButton(R.string.cancel, null)
						.show();
			} else {
				showPromptDialog(this, "prompt", R.string.msg_crane_nowork);
			}
		}
			break;
		default:
			break;
		}
	}

	/**
	 * 退出提示
	 */
	private void backPrompt() {
		if (null != workAdapter && workAdapter.getCount() > 0) {
			new AlertDialog.Builder(this)
					.setTitle(R.string.prompt)
					.setMessage(R.string.msg_crane_exit)
					.setPositiveButton(R.string.confirm,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									finish();
								}
							}).setNegativeButton(R.string.cancel, null).show();
		} else {
			finish();
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		if (parent == craneGridView) {// 行车列表
			if (craneAdapter.getList().get(position).isSelected()) {
				return;
			}
			// if (null == currentCrane) {
			// currentCrane = new CraneByType.ListClass();
			// }
			for (CraneByType.ListClass data : craneAdapter.getList()) {
				if (data.isSelected()) {
					data.setSelected(false);
					break;
				}
			}
			craneAdapter.getList().get(position).setSelected(true);
			currentCrane = craneAdapter.getItem(position);
			craneAdapter.notifyDataSetChanged();
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			backPrompt();
		}
		return super.onKeyDown(keyCode, event);
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
		if (ServerApi.getInstance().API_GET_WORKPANEL_CRANES.equals(tag)) {
			// 行车作业面板请选择行车
			try {
				// json.put("pSortField", "");
				json.put("pCurPage", "1");
				json.put("pPageQty", "9999");
				sendPOST(tag, json, null, this, tag, false);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		} else if (ServerApi.getInstance().API_GET_BRICRANE_WORKPANEL
				.equals(tag)) {
			// 获取行车作业列表信息
			try {
				json.put("pDcodeId", currentCrane.getDCODEID());
				json.put("pCurPage", "1");
				json.put("pPageQty", "9999");
				json.put("pSortField", "WORKID");
				sendPOST(tag, json, null, this, tag, false);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if (ServerApi.getInstance().API_UPDATEBRICRANE_WORKPANEL
				.equals(tag)) {
			// 修改行车作业面板信息的状态
			try {
				json.put("pWorkId", workAdapter.getItem(0).getWorkID());
				json.put("pUser", ServerApi.account);
				sendPOST(tag, json, null, this, tag, false);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
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

		mLog.i(obj.toString());
		if (ServerApi.getInstance().API_GET_WORKPANEL_CRANES.equals(str)) {
			// 获取行车数据
			CraneByType cbt = gson.fromJson(obj.toString(), CraneByType.class);
			if (cbt.getResultCode() == 0) {
				craneAdapter = new CraneAdapter(this, cbt.getList());
				craneGridView.setAdapter(craneAdapter);
				if (craneAdapter.getCount() > 0) {
					craneLayout.setVisibility(View.VISIBLE);
				} else {
					new AlertDialog.Builder(this)
							.setTitle(R.string.prompt)
							.setMessage(R.string.msg_create_crane)
							.setNegativeButton(R.string.confirm,
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog,
												int which) {
											finish();
										}
									}).create().show();
				}
			} else {
				showShortToast("resultCode==" + cbt.getResultCode()
						+ "，请联系开发人员");
			}
		} else if (ServerApi.getInstance().API_GET_BRICRANE_WORKPANEL
				.equals(str)) {
			// 获取作业信息
			CraneWorkDetail cwif = gson.fromJson(obj.toString(),
					CraneWorkDetail.class);
			if (cwif.getResultCode() == 0) {
				workAdapter = new CraneWorkAdapter(this, cwif.getList());
				workListView.setAdapter(workAdapter);
				updateText();
			} else {
				showShortToast("resultCode==" + cwif.getResultCode()
						+ "，请联系开发人员");
			}
		} else if (ServerApi.getInstance().API_UPDATEBRICRANE_WORKPANEL
				.equals(str)) {
			// 行车完成更新
			BaseModel bm = gson.fromJson(obj.toString(), BaseModel.class);
			if (bm.getResultCode() == 0) {
				workAdapter.removeItem(0);
				updateText();
			} else if (bm.getResultCode() == 3005) {
				workAdapter.removeItem(0);
				updateText();
				showShortToast("此条作业号已被完工");
			} else if (bm.getResultCode() == 1001) {
				showShortToast("正在盘点，请勿操作");
			} else {
				showShortToast("resultCode==" + bm.getResultCode() + "，请联系开发人员");
			}
		}
	}

	/**
	 * 更新界面文本内容
	 * */
	private void updateText() {
		if (workAdapter.getCount() > 0) {
			currentFromTxt.setText(workAdapter.getItem(0).getFromString()
					.trim());
			currentToTxt.setText(workAdapter.getItem(0).getToString().trim());
			amountAndWeightTxt.setText(workAdapter.getItem(0).getCount()
					+ getString(R.string.steel_unit) + "\n"
					+ workAdapter.getItem(0).getWeight());
			// 作业和钢板合计
			amountTxt.setText(getString(R.string.work_amount_)
					+ workAdapter.getCount()
					+ getString(R.string.steel_amount_)
					+ workAdapter.getSteelAmount());
		} else {
			currentFromTxt.setText("");
			currentToTxt.setText("");
			amountAndWeightTxt.setText("");
			amountTxt.setText(getString(R.string.work_amount_) + "0"
					+ getString(R.string.steel_amount_) + "0");
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

}

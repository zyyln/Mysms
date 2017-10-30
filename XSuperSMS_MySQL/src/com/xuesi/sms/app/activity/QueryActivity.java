package com.xuesi.sms.app.activity;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.xuesi.mos.util.MosLog;
import com.xuesi.sms.R;
import com.xuesi.sms.ServerApi;
import com.xuesi.sms.SmsApplication;
import com.xuesi.sms.bean.Stack;
import com.xuesi.sms.util.SmsUtil;
import com.xuesi.sms.widget.adapter.SteelResultAdapter;

/**
 * 
 * 信息查询
 * 
 * @author XS-PC014
 */
public class QueryActivity extends ScanBaseActivity {
	/** LOG */
	private final String LOG_TAG = QueryActivity.class.getSimpleName();
	private final MosLog mLog = MosLog.getInstance(LOG_TAG, "XUESI-SMS");
	/** progress的次数 */
	private int mProgressNumber = 0;

	/** 查询垛位、库房数据的条件输入框 */
	private EditText barcodeET, et_thickness, et_steelMaterial, et_steelLength,
			et_steelWidth, et_steelProject, et_steelSection, et_steelTime;
	private SteelResultAdapter steelAdapter;

	private ListView steelListView;

	/** 涉及的值, 钢板值 */
	private TextView totalText, steelText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		SmsApplication.getInstance().addActivity(this);// 添加到Activity堆栈
		setContentView(R.layout.activity_info_query);
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
		setTopTitle(R.string.infoquery);
		setRefreshView(View.INVISIBLE);

		barcodeET = (EditText) findViewById(R.id.info_query_steel_code);
		et_thickness = (EditText) findViewById(R.id.info_query_steel_thickness);
		et_steelMaterial = (EditText) findViewById(R.id.info_query_steel_material);
		et_steelLength = (EditText) findViewById(R.id.info_query_steel_length);
		et_steelWidth = (EditText) findViewById(R.id.info_query_steel_width);
		et_steelProject = (EditText) findViewById(R.id.info_query_steel_project);
		et_steelSection = (EditText) findViewById(R.id.info_query_steel_section);
		et_steelTime = (EditText) findViewById(R.id.info_query_steel_time);

		// 查询按钮
		findViewById(R.id.info_query_steel_btn).setOnClickListener(this);

		steelListView = (ListView) findViewById(R.id.info_query_result_list);

		// 底部数据
		totalText = (TextView) findViewById(R.id.infoquery_tv_total);
		steelText = (TextView) findViewById(R.id.infoquery_tv_value);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		super.onClick(v);
		switch (v.getId()) {
		// 查询钢板
		case R.id.info_query_steel_btn: {
			if (checkConditionEmty()) {
				String time = et_steelTime.getText().toString()
						.replaceAll(" ", "");
				if (SmsUtil.checkString(time) && !SmsUtil.checkTimeString(time)) {
					showShortToast("查询时间输入有误!");
					et_steelTime.requestFocus();
				} else {
					myNetwork(ServerApi.getInstance().API_GETSHEETNUM_INSTACK);
				}
			} else {
				barcodeET.setFocusable(true);
				barcodeET.setFocusableInTouchMode(true);
				barcodeET.requestFocus();
				barcodeET.requestFocusFromTouch();
			}
		}
			break;
		default:
			break;
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onScanResult(String barcode) {
		barcodeET.setText(barcode);
	}

	private boolean checkConditionEmty() {
		if (SmsUtil.checkString(barcodeET.getText().toString().trim())) {
			return true;
		}
		if (SmsUtil.checkString(et_steelMaterial.getText().toString().trim())) {
			return true;
		}
		if (SmsUtil.checkString(et_thickness.getText().toString().trim())) {
			return true;
		}
		if (SmsUtil.checkString(et_steelLength.getText().toString().trim())) {
			return true;
		}
		if (SmsUtil.checkString(et_steelWidth.getText().toString().trim())) {
			return true;
		}
		if (SmsUtil.checkString(et_steelProject.getText().toString().trim())) {
			return true;
		}
		if (SmsUtil.checkString(et_steelSection.getText().toString().trim())) {
			return true;
		}
		if (SmsUtil.checkString(et_steelTime.getText().toString().trim())) {
			return true;
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

		if (ServerApi.getInstance().API_GETSHEETNUM_INSTACK.equals(tag)) {
			try {
				JSONObject json = getRequstJson();
				// 因为接口那边问题，所以空值不传json
				if (SmsUtil.checkString(barcodeET.getText().toString().trim())) {
					json.put("barcode", barcodeET.getText().toString().trim());
				}
				if (SmsUtil.checkString(et_steelMaterial.getText().toString()
						.trim())) {
					json.put("material", et_steelMaterial.getText().toString()
							.trim());
				}
				if (SmsUtil.checkString(et_thickness.getText().toString()
						.trim())) {
					json.put("thickness", et_thickness.getText().toString()
							.trim());
				}
				if (SmsUtil.checkString(et_steelLength.getText().toString()
						.trim())) {
					json.put("length", et_steelLength.getText().toString()
							.trim());
				}
				if (SmsUtil.checkString(et_steelWidth.getText().toString()
						.trim())) {
					json.put("width", et_steelWidth.getText().toString().trim());
				}
				if (SmsUtil.checkString(et_steelTime.getText().toString()
						.trim())) {
					json.put("InTime", et_steelTime.getText().toString().trim());
				}
				if (SmsUtil.checkString(et_steelProject.getText().toString()
						.trim())) {
					json.put("project", et_steelProject.getText().toString()
							.trim());
				}
				if (SmsUtil.checkString(et_steelSection.getText().toString()
						.trim())) {
					json.put("assemblyNo", et_steelSection.getText().toString()
							.trim());
				}
				json.put("perPage", "9999");// 每页返回的记录数
				sendPOST(tag, json, null, this, tag, false);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		} else {
			showShortToast("错误的网络请求，请检查!");
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
		if (ServerApi.getInstance().API_GETSHEETNUM_INSTACK.equals(tag)) {
			// 获取筛选后的钢板
			mLog.i(LOG_TAG + obj.toString());
			Stack stack = gson.fromJson(obj.toString(), Stack.class);
			if (stack.getResultCode() == 0) {
				steelAdapter = new SteelResultAdapter(this, stack.getList());
				steelListView.setAdapter(steelAdapter);
				// 共涉及
				totalText.setText(stack.getTotalList().get(0).getHousenum()
						+ getString(R.string.loaf_storehouse)
						+ stack.getTotalList().get(0).getStacknum()
						+ getString(R.string.loaf_stock));
				// 显示合情的钢板数量
				steelText.setText(stack.getTotalList().get(0).getTotal()
						+ getString(R.string.steel_unit));
			} else {
				showShortToast("resultCode==" + stack.getResultCode()
						+ "，请联系开发人员");
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

}

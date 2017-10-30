package com.xuesi.sms.app.activity;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.xuesi.mos.util.MosLog;
import com.xuesi.sms.R;
import com.xuesi.sms.ServerApi;
import com.xuesi.sms.SmsApplication;
import com.xuesi.sms.bean.Sheet;
import com.xuesi.sms.util.SmsUtil;

public class ScanDialog extends ScanBaseActivity {
	/** LOG */
	private final String LOG_TAG = ScanDialog.class.getSimpleName();
	private final MosLog mLog = MosLog.getInstance(LOG_TAG, "XUESI-SMS");
	/** progress的次数 */
	private int mProgressNumber = 0;

	private Sheet.ListClass curSelectSheet;
	private TextView barcodeTv, TitleTv;
	private String FLAG;
	private EditText barcodeET;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		SmsApplication.getInstance().addActivity(this);// 添加到Activity堆栈
		setContentView(R.layout.dialog_replacebrcode);
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
		barcodeET = (EditText) findViewById(R.id.repalce_barcode_et);
		findViewById(R.id.replace_barcode_confirm).setOnClickListener(this);
		barcodeTv = ((TextView) findViewById(R.id.repalce_barcode_tv));
		TitleTv = (TextView) findViewById(R.id.repalce_barcode_title);

		findViewById(R.id.replacebarcode_root).setOnClickListener(this);
	}

	private void loadExtras() {
		Bundle bundle = getIntent().getExtras();
		if (null != bundle) {
			curSelectSheet = (Sheet.ListClass) bundle.getSerializable("sheet");
			barcodeTv.setText(curSelectSheet.getBarcode());
			FLAG = bundle.getString("FLAG");
			TitleTv.setText(bundle.getString("title"));
			mLog.d("---->" + FLAG);
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		super.onClick(v);
		switch (v.getId()) {
		case R.id.replace_barcode_confirm:
			String barcode = barcodeET.getText().toString().replaceAll(" ", "");
			if (SmsUtil.checkString(barcode)) {
				if (FLAG.equals(SheetDetailActivity.class.getSimpleName())) {
					Intent intent = new Intent();
					intent.putExtra("barcode", barcodeET.getText().toString());
					intent.putExtra("sheetid", curSelectSheet.getMetalBillId());
					this.setResult(1012, intent);
					this.finish();
				} else if (FLAG.equals(OutputActivity.class.getSimpleName())) {
					// 出库画面是扫描确认
					myNetwork(ServerApi.getInstance().SHEET_GETSHEETLIST,
							barcode);
				}
			} else {
				showShortToast("条形码不能为空");
			}
			break;
		case R.id.replacebarcode_root:
			// 排除触摸屏方法onTouchEvent()
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
	public boolean onTouchEvent(MotionEvent event) {
		this.finish();
		return true;
	}

	@Override
	protected void onScanResult(String barcode) {
		// TODO Auto-generated method stub
		barcodeET.setText(barcode);
	}

	@Override
	public void myNetwork(String tag, String... txt) {
		// TODO Auto-generated method stub
		super.myNetwork(tag, txt);
		mProgressNumber++;
		if (mProgressNumber == 1) {
			showProgressDialog("加载中", false, false);
		}
		if (ServerApi.getInstance().SHEET_GETSHEETLIST.equals(tag)) {
			// 根据条形码获取钢板详情
			try {
				JSONObject json = getRequstJson();
				json.put("StackNo", "");// 货位号,可以省略
				json.put("Barcode", txt[0]);// 条形码,可以省略
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
		} else {
			showShortToast("错误的网络请求，请检查");
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

		mLog.i(LOG_TAG + obj.toString());
		if (ServerApi.getInstance().SHEET_GETSHEETLIST.equals(tag)) {
			// 获取钢板
			Sheet sheetOut = gson.fromJson(obj.toString(), Sheet.class);
			if (sheetOut.getResultCode() == 0 && sheetOut.getList().size() > 0) {
				Sheet.ListClass sheet = sheetOut.getList().get(0);
				if (Sheet.equalsSheet(curSelectSheet, sheet)) {
					showShortToast("钢板一致!");
				} else {
					showShortToast("钢板不一致!");
				}
				this.finish();
			} else {
				showShortToast("没有这张钢板!");
			}
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

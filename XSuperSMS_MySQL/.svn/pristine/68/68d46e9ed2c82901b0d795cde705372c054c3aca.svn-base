package com.xuesi.sms.app.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.RadioGroup;

import com.xuesi.mos.util.MosLog;
import com.xuesi.sms.R;
import com.xuesi.sms.SmsApplication;

/**
 * 选择库房类型<br>
 * 添加原因:入库增加功能
 * 
 * @author XS-PC014
 * 
 */
public class StoreTypeActivity extends SmsActivity implements
		RadioGroup.OnCheckedChangeListener {
	/** LOG */
	private final String LOG_TAG = StoreTypeActivity.class.getSimpleName();
	private final MosLog mLog = MosLog.getInstance(LOG_TAG, "XUESI-SMS");

	/** 目标activity */
	private String toActivity;

	// 一级库、二级库、余料库单选按钮
	private RadioGroup selctStoreType;
	/** 记录库房类型,值域{一级库FSK,二级库SCK,余料库RCK} */
	private String curStoreType;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		SmsApplication.getInstance().addActivity(this);// 添加到Activity堆栈
		setContentView(R.layout.activity_storehousetype);
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
		setTopTitle(R.string.selctStoreType);
		setRefreshView(View.INVISIBLE);
		// 选择库房类型
		selctStoreType = (RadioGroup) findViewById(R.id.storehousetype_radiog);
		selctStoreType.setOnCheckedChangeListener(this);
		// 确定按钮事件
		// findViewById(R.id.storehousetype_btn_confirm).setOnClickListener(this);

		Bundle bundle = getIntent().getExtras();
		toActivity = bundle.getString("toActivity");
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		super.onClick(v);
		switch (v.getId()) {
		case R.id.storehousetype_btn_confirm: {// 选择库房类型的确定按钮
			if (null != curStoreType) {
				Bundle bundle = new Bundle();
				bundle.putString("curStoreType", curStoreType);
				if (InputActivity.class.getSimpleName().equals(toActivity)) {
					openActivity(InputActivity.class, bundle);
				} else if (ShiftActivity.class.getSimpleName().equals(
						toActivity)) {
					bundle.putString("fromActivity", LOG_TAG);
					openActivity(ShiftActivity.class, bundle);
				} else if (StoreActivity.class.getSimpleName().equals(
						toActivity)) {
					openActivity(StoreActivity.class, bundle);
				}
				finish();
			} else {
				showShortToast("请选择库房类型！");
			}
		}
			break;
		default:
			break;
		}
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		// TODO Auto-generated method stub
		// 一级库、二级库、余料库选择事件处理
		if (group == selctStoreType) {
			if (checkedId == R.id.storehousetype_radioBtn_fsk) {
				curStoreType = "FSK";
			} else if (checkedId == R.id.storehousetype_radioBtn_sck) {
				curStoreType = "SCK";
			} else {
				curStoreType = "RCK";
			}

			Bundle bundle = new Bundle();
			bundle.putString("curStoreType", curStoreType);
			if (InputActivity.class.getSimpleName().equals(toActivity)) {
				openActivity(InputActivity.class, bundle);
			} else if (ShiftActivity.class.getSimpleName().equals(toActivity)) {
				bundle.putString("fromActivity", LOG_TAG);
				openActivity(ShiftActivity.class, bundle);
			} else if (StoreActivity.class.getSimpleName().equals(toActivity)) {
				openActivity(StoreActivity.class, bundle);
			}
			finish();
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

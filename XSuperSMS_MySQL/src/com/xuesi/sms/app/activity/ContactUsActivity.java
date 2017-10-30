/**
 * <p>Title: ContactUsActivity.java</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2007</p>
 * <p>Company: XSuper</p>
 * @author XS-PC011
 * @date 2015-9-2
 *
 */
package com.xuesi.sms.app.activity;

import android.os.Bundle;
import android.view.View;

import com.xuesi.mos.util.MosLog;
import com.xuesi.sms.R;
import com.xuesi.sms.SmsApplication;

/**
 * <p>
 * Title: ContactUsActivity
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Company: XSuper
 * </p>
 * 
 * @author XS-PC011
 * @date 2015-9-2
 */
public class ContactUsActivity extends SmsActivity {
	/** LOG */
	private final String LOG_TAG = ContactUsActivity.class.getSimpleName();
	private final MosLog mLog = MosLog.getInstance(LOG_TAG, "XUESI-SMS");

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SmsApplication.getInstance().addActivity(this);// 添加到Activity堆栈
		setContentView(R.layout.activity_contact_us);
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

		setTopTitle(R.string.contactUs);
		setRefreshView(View.INVISIBLE);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		super.onClick(v);
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

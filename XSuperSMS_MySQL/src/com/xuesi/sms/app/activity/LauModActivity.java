package com.xuesi.sms.app.activity;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;

import com.google.gson.Gson;
import com.xuesi.mos.device.Screen;
import com.xuesi.mos.util.MosLog;
import com.xuesi.sms.R;
import com.xuesi.sms.ServerApi;
import com.xuesi.sms.SmsApplication;
import com.xuesi.sms.SmsConfig;
import com.xuesi.sms.bean.BaseModel;
import com.xuesi.sms.util.SmsDir;
import com.xuesi.sms.util.ViewFactory;

/**
 * 加载模式选择<br>
 * 
 * @author XS-PC014
 * 
 */
public class LauModActivity extends SmsActivity {
	/** LOG */
	private final String LOG_TAG = LauModActivity.class.getSimpleName();
	private final MosLog mLog = MosLog.getInstance(LOG_TAG, "XUESI-SMS");
	/** progress的次数 */
	private int mProgressNumber = 0;
	private Dialog adDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		SmsApplication.getInstance().addActivity(this);// 添加到Activity堆栈
		setContentView(R.layout.activity_laumod);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}

	@Override
	protected void initContentView() {
		// TODO Auto-generated method stub
		super.initContentView();
		findViewById(R.id.launchmodel_btn_alpha).setOnClickListener(this);
		findViewById(R.id.launchmodel_btn_standard).setOnClickListener(this);
		 checkURL(ServerApi.imageUri);
		// showAdDialog();
	}


	// 检查连接的有效性
	public void checkURL(final String url) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				Message msg = Message.obtain();
				try {
					HttpURLConnection conn = (HttpURLConnection) new URL(url)
							.openConnection();
					int code = conn.getResponseCode();
					mLog.d("CODE=="+url+"-----"+code);
					if (code == 200) {
						msg.what = 1;
					} else {
						msg.what = 0;
					}
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				upHandler.sendMessage(msg);
			}
		}).start();
	}

	private Handler upHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {

			switch (msg.what) {
			case 1:
				showAdDialog();
				break;
			case 0:
				
				break;
			default:
				break;
			}
		}
	};

	private void showAdDialog() {
		View view = getLayoutInflater().inflate(R.layout.dialog_advert, null);

		view.findViewById(R.id.imgbtn_close).setOnClickListener(this);
		FrameLayout vw = (FrameLayout) view.findViewById(R.id.view_ad);
		vw.addView(ViewFactory.getImageView(this, ServerApi.imageUri,
				R.layout.view_banner));
		vw.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				openActivity(AdWebActivity.class);
			}
		});
		adDialog = getDialog(LauModActivity.this, view);
		adDialog.setCanceledOnTouchOutside(false);
		adDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
			@Override
			public boolean onKey(DialogInterface paramDialogInterface,
					int keycode, KeyEvent event) {
				// TODO Auto-generated method stub
				if (keycode == KeyEvent.KEYCODE_BACK) {
					closeDialog(adDialog);
					return true;
				} else {
					return false;
				}
			}
		});
		adDialog.getWindow().setLayout(
				(int) (Screen.getInstance().getScreenWidth() * 0.75),
				(int) (Screen.getInstance().getScreenHeight() * 0.75));
		adDialog.show();

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		super.onClick(v);
		switch (v.getId()) {
		case R.id.launchmodel_btn_alpha:
			new AlertDialog.Builder(this)
					.setTitle(getString(R.string.prompt))
					.setMessage(getString(R.string.msg_test_reminder))
					.setPositiveButton(R.string.confirm,
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {

									// openActivity(WorkInfoActivity.class);
									// finish();
									myNetwork(ServerApi.getInstance().API_INSERTEXPERIENCE);
								}
							}).create().show();
			break;
		case R.id.launchmodel_btn_standard:
			ServerApi.setNull();
			SmsConfig.isTest = false;
			openActivity(WorkInfoActivity.class);
			finish();
			break;
		case R.id.imgbtn_close:
			adDialog.dismiss();
			break;
		default:
			break;
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			exitApplication();
		}
		return false;
	}

	@Override
	public void myNetwork(String url, String... txt) {
		// TODO Auto-generated method stub
		super.myNetwork(url, txt);
		mProgressNumber++;
		if (mProgressNumber == 1) {
			showProgressDialog("", false, false);
		}
		if (ServerApi.getInstance().API_INSERTEXPERIENCE.equals(url)) {

			JSONObject jo = getRequstJson();
			sendPOST(url, jo, null, this, url, false);
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
		if (mProgressNumber > 0) {
			mProgressNumber--;
		}
		if (mProgressNumber == 0) {
			dismissProgressDialog();
		}
		Gson gson = new Gson();
		mLog.i(obj.toString());
		if (ServerApi.getInstance().API_INSERTEXPERIENCE.equals(str)) {
			BaseModel bm = gson.fromJson(obj.toString(), BaseModel.class);
			if (bm.getResultCode() != 1) {
				ServerApi.setNull();
				SmsConfig.isTest = true;
				openActivity(WorkInfoActivity.class);
				finish();
			} else {
				showShortToast("resultCode==" + bm.getResultCode() + "，请联系开发人员");
			}
		}
	}

	private void exitApplication() {
		// 退出应用，finish所有活动的activity
		new AlertDialog.Builder(this)
				.setTitle(getString(R.string.prompt))
				.setMessage(
						getString(R.string.exit)
								+ getString(R.string.app_name_zh))
				.setPositiveButton(R.string.confirm,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								SmsApplication.getInstance().exit();
							}
						})
				.setNegativeButton(R.string.cancel,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.dismiss();
							}
						}).create().show();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// TODO Auto-generated method stub
		super.onConfigurationChanged(newConfig);
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

package com.xuesi.sms.app.activity;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xuesi.mos.app.MosActivity;
import com.xuesi.mos.device.Screen;
import com.xuesi.mos.libs.volley.NetworkResponse;
import com.xuesi.mos.libs.volley.VolleyError;
import com.xuesi.mos.util.MosLog;
import com.xuesi.sms.R;
import com.xuesi.sms.ServerApi;
import com.xuesi.sms.SmsApplication;
import com.xuesi.sms.SmsConfig;

public class SmsActivity extends MosActivity {
	/** LOG */
	private final String LOG_TAG = SmsActivity.class.getSimpleName();
	private final MosLog mLog = MosLog.getInstance(LOG_TAG, "XUESI-SMS");

	/** 标题栏 ,用户,日期 */
	public TextView titleTv, accountTv, compTv, dateTv, testCount;
	/** 返回,刷新 */
	public ImageView backImg, refreshImg;
	/** 显示器高度 */
	public int screenHeight = Screen.getInstance().getScreenHeight();
	/** 显示器宽度 */
	public int screenWidth = Screen.getInstance().getScreenWidth();

	private IntentFilter intentFilter;
	private NetworkChangeReceiver networkChangeReceiver;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		SmsApplication.getInstance().addActivity(this);// 添加到Activity堆栈

	}

	/**
	 * 
	 * NetworkChangeReceiver 2016-1-26 下午5:36:47 监听网络广播接收器
	 * 
	 * @version 1.0.0
	 * 
	 */
	class NetworkChangeReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo networkInfo = connectivityManager
					.getActiveNetworkInfo();
			if (networkInfo != null && networkInfo.isAvailable()) {
				// toastShort("network is available");
			} else {
				showShortToast("网络无法连接，请检查网络！");
			}
		}

	}

	@Override
	protected void onStart() {
		super.onStart();
	}

	@Override
	protected void onRestart() {
		super.onRestart();
	}

	@Override
	protected void onResume() {
		super.onResume();
		String nowClass = this.getClass().getSimpleName();
		if (nowClass.equals("LoginActivity")) {

		} else {
			if (ServerApi.factoryCode == null || ServerApi.millCode == null
					|| ServerApi.passport == null) {
				openActivity(LoginActivity.class);
			}
		}

	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onStop() {
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(networkChangeReceiver);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.bartop_img_back:
			finish();
			break;
		case R.id.bartop_img_refresh:
			break;
		default:
			break;
		}
	}

	@Override
	protected void initContentView() {
		// TODO 设置返回按钮、刷新按钮、标题栏、用户、日期
		backImg = (ImageView) findViewById(R.id.bartop_img_back);
		if (null != backImg) {
			backImg.setOnClickListener(this);
		}
		refreshImg = (ImageView) findViewById(R.id.bartop_img_refresh);
		if (null != refreshImg) {
			refreshImg.setOnClickListener(this);
		}
		titleTv = (TextView) findViewById(R.id.bartop_tv_title);
		accountTv = (TextView) findViewById(R.id.barbottom_tv_account);
		if (null != accountTv) {
			accountTv.setText(ServerApi.account);
		}
		compTv = (TextView) findViewById(R.id.barbottom_tv_comp);
		if (null != compTv) {
			compTv.setText(ServerApi.comp);
		}
		dateTv = (TextView) findViewById(R.id.barbottom_tv_date);
		if (null != dateTv) {
			dateTv.setText(ServerApi.date);
		}
		testCount = (TextView) findViewById(R.id.tv_test_count);
		if (null != testCount && SmsConfig.isTest) {
			testCount.setText(getString(R.string.test_count));
		}
		// 注册网络广播
		intentFilter = new IntentFilter();
		intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
		networkChangeReceiver = new NetworkChangeReceiver();
		registerReceiver(networkChangeReceiver, intentFilter);
	}

	/**
	 * 添加画面标题
	 */
	public void setTopTitle(int resid) {
		if (null != titleTv && resid != 0) {
			// titleTv已公有，此方法可删除
			titleTv.setVisibility(View.VISIBLE);
			titleTv.setText(resid);
		}
	}

	public void setTopTitle(String title) {
		if (null != titleTv && null != title) {
			// titleTv已公有，此方法可删除
			titleTv.setVisibility(View.VISIBLE);
			titleTv.setText(title);
		}
	}

	public void setRefreshView(int visible) {
		if (null != refreshImg) {
			// refreshImg已公有，此方法可删除
			refreshImg.setVisibility(visible);
		}
	}

	/**
	 * 获取通用JSON<br>
	 * 待修改
	 * 
	 * @return
	 * @throws JSONException
	 */
	protected JSONObject getRequstJson() {
		JSONObject json = new JSONObject();
		try {
			json.put(ServerApi.PARA_FACTORYCODE, ServerApi.factoryCode);
			json.put(ServerApi.COOKIE_MILLCODE, ServerApi.millCode);
			json.put(ServerApi.COOKIE_PASSPORT, ServerApi.passport);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return json;
	}

	/**
	 * 本页面所有的网络请求。
	 * 
	 * @param txt
	 *            [?] 对应的需要发送的单号
	 * @param tag
	 *            对应的tag
	 */
	public void myNetwork(String url, String... txt) {

	}

	@Override
	public void onRequestFail(String url, Exception ex) {
		// TODO Auto-generated method stub
		mLog.d("请求连接失败，请稍候再试！");
		showShortToast("请求连接失败，请稍候再试！");
	}

	@Override
	public void onRequestSuccess(String url, Object obj) {
		// TODO Auto-generated method stub
	}

	private Dialog loadingDialog = null;

	/**
	 * showProgressDialog(创建等待框) (这里描述这个方法适用条件 – 可选)
	 * 
	 * @param 提示内容
	 * @param isCancel
	 * @param 布局方向
	 * @return void
	 * @exception
	 * @since 1.0.0
	 */
	public void showProgressDialog(String msg, boolean isCancel, boolean isRight) {
		if (this.loadingDialog == null) {
			this.loadingDialog = creatProgressDialog(msg, isCancel, isRight);
			this.loadingDialog.show();
		}
	}

	private Dialog creatProgressDialog(String msg, boolean isCancel,
			boolean isRight) {
		LinearLayout.LayoutParams wrap_content = new LinearLayout.LayoutParams(
				-2, -2);
		LinearLayout.LayoutParams wrap_content0 = new LinearLayout.LayoutParams(
				-2, -2);
		LinearLayout main = new LinearLayout(this);
		main.setBackgroundColor(Color.WHITE);
		if (isRight) {
			main.setOrientation(LinearLayout.HORIZONTAL);
			wrap_content.setMargins(10, 0, 35, 0);
			wrap_content0.setMargins(35, 25, 0, 25);
		} else {
			main.setOrientation(LinearLayout.VERTICAL);
			wrap_content.setMargins(10, 5, 10, 15);
			wrap_content0.setMargins(35, 25, 35, 0);
		}
		main.setGravity(Gravity.CENTER);
		ImageView spaceshipImage = new ImageView(this);
		spaceshipImage.setImageResource(R.drawable.publicloading);
		TextView tipTextView = new TextView(this);
		tipTextView.setText("请稍候...");

		Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(this,
				R.anim.loading_animation);

		spaceshipImage.startAnimation(hyperspaceJumpAnimation);
		if ((msg != null) && (!"".equals(msg))) {
			tipTextView.setText(msg);
		}
		Dialog loadingDialog = new Dialog(this, R.style.loading_dialog);
		loadingDialog.setCancelable(isCancel);
		// this.cancelable = Boolean.valueOf(isCancel);
		main.addView(spaceshipImage, wrap_content0);
		main.addView(tipTextView, wrap_content);
		LinearLayout.LayoutParams fill_parent = new LinearLayout.LayoutParams(
				-1, -1);
		loadingDialog.setContentView(main, fill_parent);

		return loadingDialog;
	}

	public void dismissProgressDialog() {
		if ((this.loadingDialog != null) && (this.loadingDialog.isShowing())) {
			this.loadingDialog.dismiss();
			this.loadingDialog = null;
		}
	}

	/**
	 * 显示toast信息
	 * 
	 * @param msg
	 *            //
	 */
	// public void toastShort(String msg) {
	// Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
	// }
	//
	// public void toastLong(String msg) {
	// Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
	// }

	/**
	 * 提示对话框
	 * 
	 * @param context
	 * @param title
	 * @param mes
	 */
	protected void showPromptDialog(Context context, String title, int resId) {
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
					}
				});
		builder.create().show();
	}

	/**
	 * 提示对话框,跳转界面
	 * 
	 * @param context
	 * @param title
	 * @param mes
	 * @param pClass
	 */
	protected void showPromptDialog(Context context, String title, int resId,
			final Class<?> pClass) {
		// TODO Auto-generated method stub
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
						// 跳转导航
						openActivity(pClass);
					}
				});
		builder.create().show();
	}

	/**
	 * 
	 * 可选对话框
	 * 
	 * @param context
	 * @param title
	 * @param mes
	 * @param active
	 *            "finish":退出;"active":执行具体操作
	 */
	protected void showAlertDialog(Context context, String title, String mes,
			final String active) {
		AlertDialog.Builder builder = new Builder(context);
		builder.setTitle(title);
		builder.setMessage(mes);
		builder.setPositiveButton(R.string.confirm,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						if ("finish".equals(active)) {
							finish();
						} else if ("active".equals(active)) {
							_do();
						}
					}
				});
		builder.setNegativeButton(R.string.cancel,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						if ("active".equals(active)) {
							_cancle();
						}
					}
				});
		builder.create().show();
	}

	protected void _do() {
	}

	protected void _cancle() {
	}

	/**
	 * 设置对话框
	 */
	public void showDialog(Dialog dialog) {
		dialog.getWindow().setLayout(
				(int) (Screen.getInstance().getScreenWidth() * 0.57),
				(int) (Screen.getInstance().getScreenHeight() * 0.75));
		dialog.show();
	}

	/**
	 * 获取对话框
	 */
	public Dialog getDialog(Context context, View view) {
		Dialog dialog = new Dialog(context, R.style.dialog);
		dialog.setContentView(view);
		dialog.setCanceledOnTouchOutside(true);
		return dialog;
	}

	/**
	 * 关闭对话框
	 */
	public void closeDialog(Dialog dialog) {
		if (null != dialog && dialog.isShowing()) {
			dialog.cancel();
			dialog.dismiss();
		}
	}

}

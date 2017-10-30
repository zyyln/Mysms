package com.xuesi.sms.app.activity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.os.Message;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.xuesi.mos.util.MosLog;
import com.xuesi.sms.R;
import com.xuesi.sms.ServerApi;
import com.xuesi.sms.SmsApplication;
import com.xuesi.sms.SmsConfig;
import com.xuesi.sms.bean.BaseModel;
import com.xuesi.sms.util.DataCleanManager;
import com.xuesi.sms.util.FileUtil;
import com.xuesi.sms.util.SPHelper;
import com.xuesi.sms.util.SmsDir;
import com.xuesi.sms.util.SmsUtil;
import com.xuesi.sms.util.UpdateManager;

/**
 * <p>
 * Title: SettingActivity-系统设定页面
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
public class SettingActivity extends SmsActivity {
	/** LOG */
	private final String LOG_TAG = SettingActivity.class.getSimpleName();
	private final MosLog mLog = MosLog.getInstance(LOG_TAG, "XUESI-SMS");
	/** progress的次数 */
	private int mProgressNumber = 0;

	private EditText orgEditText, newEditText, confirmEditText;
	private TextView localDataSizeTv;
	private AlertDialog changePswDialog;
	private ImageButton swtBtn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		SmsApplication.getInstance().addActivity(this);// 添加到Activity堆栈
		setContentView(R.layout.activity_setting);
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
		// 设置标题
		setTopTitle(R.string.systemSetting);
		// 隐藏刷新按钮
		setRefreshView(View.INVISIBLE);
		localDataSizeTv = (TextView) findViewById(R.id.localDataSize);
		localDataSizeTv.setText(FileUtil.getFormatSize(getLoaclDataSize()));
		LinearLayout changePswLayout = (LinearLayout) findViewById(R.id.setting_change_password);
		changePswLayout.setOnClickListener(this);
		swtBtn = (ImageButton) findViewById(R.id.img_swt);
		swtBtn.setOnClickListener(this);
		if (SmsConfig.isCraneCheck) {
			swtBtn.setBackground(getResources().getDrawable(
					R.drawable.switch_on));
		} else {
			swtBtn.setBackground(getResources().getDrawable(
					R.drawable.switch_off));
		}
		LinearLayout cleanDataLayout = (LinearLayout) findViewById(R.id.setting_clean_localData);
		cleanDataLayout.setOnClickListener(this);
		LinearLayout useHelpLayout = (LinearLayout) findViewById(R.id.setting_use_help);
		useHelpLayout.setOnClickListener(this);

		LinearLayout aboutUsLayout = (LinearLayout) findViewById(R.id.setting_about_us);
		aboutUsLayout.setOnClickListener(this);

		LinearLayout checkVersionLayout = (LinearLayout) findViewById(R.id.setting_check_version);
		checkVersionLayout.setOnClickListener(this);
		TextView verNameTxt = (TextView) findViewById(R.id.setting_tv_vername);
		LinearLayout bluetoothLayout = (LinearLayout) findViewById(R.id.setting_Bluetooth);
		bluetoothLayout.setOnClickListener(this);

		// 进入配置界面
		LinearLayout configureFieldsLayout = (LinearLayout) findViewById(R.id.setting_configure_fields);
		configureFieldsLayout.setOnClickListener(this);
		try {
			String versionName = getPackageManager().getPackageInfo(
					getPackageName(), 0).versionName;
			if (SmsConfig.isDebug) {
				verNameTxt.setText("当前测试版本:" + versionName);
			} else {
				verNameTxt.setText("当前版本:" + versionName);
			}
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获取本应用文件大小
	 * 
	 * @return
	 */
	private double getLoaclDataSize() {
		double temSize = FileUtil.getDirSize(SmsDir.getInstance()
				.getCache_dir())
				+ FileUtil.getDirSize(SmsDir.getInstance().getCrash_dir())
				+ FileUtil.getDirSize(SmsDir.getInstance().getDownload_dir())
				+ FileUtil.getDirSize(SmsDir.getInstance().getTemp_dir());
		return temSize;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		super.onClick(v);
		switch (v.getId()) {
		case R.id.setting_change_password: {// 修改密码
			if (null != changePswDialog && changePswDialog.isShowing()) {
				changePswDialog.dismiss();
			}
			changePswDialog = new AlertDialog.Builder(this).create();
			changePswDialog.setCanceledOnTouchOutside(true);
			View changePswLayout = LayoutInflater.from(this).inflate(
					R.layout.dialog_change_password, null);
			orgEditText = (EditText) changePswLayout
					.findViewById(R.id.change_psw_org_text);
			newEditText = (EditText) changePswLayout
					.findViewById(R.id.change_psw_new_text);
			confirmEditText = (EditText) changePswLayout
					.findViewById(R.id.change_psw_confirm_text);
			changePswLayout.findViewById(R.id.change_psw_confirm_btn)
					.setOnClickListener(this);

			changePswDialog.setView(changePswLayout, 0, 0, 0, 0);
			changePswDialog.show();
		}
			break;
		case R.id.img_swt: {// 是否勾选行车
			if (SmsConfig.isCraneCheck) {
				SmsConfig.isCraneCheck = false;
				swtBtn.setBackground(getResources().getDrawable(
						R.drawable.switch_off));
			} else {
				SmsConfig.isCraneCheck = true;
				swtBtn.setBackground(getResources().getDrawable(
						R.drawable.switch_on));
			}
		}
			break;
		case R.id.setting_use_help: {// 查看帮助
			openActivity(HelpActivity.class);
		}
			break;
		case R.id.setting_about_us: {// 关于我们
			openActivity(ContactUsActivity.class);
		}
			break;
		case R.id.setting_check_version: {// 检查版本
			UpdateManager upMng = new UpdateManager(this,
					UpdateManager.STATUS_1);
			upMng.checkVersion();
		}
			break;
		case R.id.setting_Bluetooth: {// 蓝牙连接/断开
			openActivity(ConnectBluetooth.class);
		}
			break;
		case R.id.change_psw_confirm_btn: {// 修改密码确认按钮
			String orgStr = orgEditText.getText().toString().trim();
			String newStr = newEditText.getText().toString().trim();
			String confirmStr = confirmEditText.getText().toString().trim();
			Message msg = Message.obtain();
			msg.what = 0;
			if (SmsUtil.checkString(orgStr)) {
				if (SmsUtil.checkString(newStr)) {
					if (SmsUtil.checkString(confirmStr)) {
						if (newStr.equals(confirmStr)) {
							myNetwork(ServerApi.getInstance().USER_CHANGE_PSD,
									orgStr, newStr, confirmStr);
						} else {
							showShortToast("新密码与确认密码不一致!");
						}
					} else {
						showShortToast("确认密码不能为空!");
					}
				} else {
					showShortToast("新密码不能为空!");
				}
			} else {
				showShortToast("原始密码不能为空!");
			}
		}
			break;
		case R.id.setting_clean_localData: {// 清除缓存
			showAlertDialog(this, getString(R.string.prompt), "确定清除本应用缓存?",
					"active");
		}
			break;
		case R.id.setting_configure_fields: {// 配置列表字段
			openActivity(ConfigureFieldsActivity.class);
			// showShortToast("敬请期待");
		}
			break;
		default:
			break;
		}
	}

	@Override
	protected void _do() {
		// TODO Auto-generated method stub
		super._do();
		showProgressDialog("清除中", false, false);
		DataCleanManager.cleanApplicationData(this, SmsDir.getInstance()
				.getCache_dir(), SmsDir.getInstance().getCrash_dir(), SmsDir
				.getInstance().getDownload_dir(), SmsDir.getInstance()
				.getTemp_dir());
		if (getLoaclDataSize() == 0) {
			dismissProgressDialog();
			localDataSizeTv.setText(FileUtil.getFormatSize(getLoaclDataSize()));
			showShortToast("清除完成");
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		return super.onKeyDown(keyCode, event);
	}

	/**
	 * 本页面所有的网络请求。
	 * 
	 * @param txt
	 *            [?] 对应的需要发送的单号
	 * @param tag
	 *            对应的tag
	 */
	@Override
	public void myNetwork(String tag, String... txt) {
		// TODO Auto-generated method stub
		// super.myNetwork(tag, txt);
		mProgressNumber++;
		if (mProgressNumber == 1) {
			showProgressDialog("加载中", false, false);
		}
		if (ServerApi.getInstance().USER_CHANGE_PSD.equals(tag)) {
			try {
				JSONObject json = new JSONObject();
				json.put("operator", ServerApi.account);// 操作人帐号
				json.put("account", ServerApi.account);
				json.put("oldPassword", txt[0].toString());
				json.put("newPassword", txt[1].toString());
				json.put("confirmPassword", txt[2].toString());
				json.put(ServerApi.PARA_FACTORYCODE, ServerApi.factoryCode);
				json.put(ServerApi.COOKIE_MILLCODE, ServerApi.millCode);
				json.put(ServerApi.COOKIE_PASSPORT, ServerApi.passport);
				sendPOST(tag, json, null, this, tag, false);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		} else {
			showShortToast("错误的网络请求，请检查");
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
		// 修改密码接口
		if (ServerApi.getInstance().USER_CHANGE_PSD.equals(str)) {
			mLog.i(LOG_TAG + obj.toString());
			BaseModel bm = gson.fromJson(obj.toString(), BaseModel.class);
			if (bm.getResultCode() == 0) {
				changePswDialog.dismiss();
				// 清空保存的密码
				SPHelper.getInstance().saveValueToSp(SPHelper.KEY_PASSWORD, "");

				showShortToast("修改成功，请重新登录！");
				openActivity(LoginActivity.class);
			} else if (bm.getResultCode() == 6003) {
				showShortToast("原始密码错误!");
				orgEditText.setText("");
				orgEditText.requestFocus();
			} else {
				showShortToast("resultCode==" + bm.getResultCode() + "，请联系开发人员");
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
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

}

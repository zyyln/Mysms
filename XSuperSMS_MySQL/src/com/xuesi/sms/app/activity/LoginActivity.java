package com.xuesi.sms.app.activity;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.CheckBox;
import android.widget.EditText;

import com.google.gson.Gson;
import com.xuesi.mos.util.MosLog;
import com.xuesi.sms.R;
import com.xuesi.sms.ServerApi;
import com.xuesi.sms.SmsApplication;
import com.xuesi.sms.SmsConfig;
import com.xuesi.sms.app.fragmentActivity.InputFragmentActivity;
import com.xuesi.sms.bean.LoginJudge;
import com.xuesi.sms.util.SPHelper;
import com.xuesi.sms.util.SmsUtil;
import com.xuesi.sms.util.UpdateManager;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends SmsActivity {
    /**
     * LOG
     */
    private final String LOG_TAG = LoginActivity.class.getName();
    private final MosLog mLog = MosLog.getInstance(LOG_TAG, "XUESI-SMS");

    private EditText m_account, m_password;
    private CheckBox m_savepsd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        SmsApplication.getInstance().addActivity(this);// 添加到Activity堆栈
        setContentView(R.layout.activity_login);

        // 检查版本
        UpdateManager upMng = new UpdateManager(this, UpdateManager.STATUS_0);
        upMng.checkVersion();
    }

    @Override
    protected void initContentView() {
        // TODO Auto-generated method stub
        super.initContentView();
        // 用户名
        m_account = (EditText) findViewById(R.id.login_et_account);
        // 密码
        m_password = (EditText) findViewById(R.id.login_et_psd);
        // 记住密码
        m_savepsd = (CheckBox) findViewById(R.id.login_remember);

        // 登录按钮
        findViewById(R.id.login_btn_launch).setOnClickListener(this);
        // 重置按钮
        findViewById(R.id.login_btn_reset).setOnClickListener(this);
        findViewById(R.id.register_btn_launch).setOnClickListener(this);
        // 关于我们
        findViewById(R.id.login_imgbtn_contactus).setOnClickListener(this);
        if (SmsConfig.isDebug) {
            // 设置IP
            findViewById(R.id.login_imgbtn_ipset).setVisibility(View.VISIBLE);
            findViewById(R.id.login_imgbtn_ipset).setOnClickListener(this);
        }

        boolean isSave = SPHelper.getInstance().getBooleanFromSp(
                SPHelper.KEY_IS_REMPSD);
        m_savepsd.setChecked(isSave);
        if (isSave) {
            m_account.setText(SPHelper.getInstance().getStringFromSp(
                    SPHelper.KEY_ACCOUNT));
            m_password.setText(SPHelper.getInstance().getStringFromSp(
                    SPHelper.KEY_PASSWORD));
        }
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
            case R.id.login_btn_launch:
                String user = m_account.getText().toString().trim();
                String password = m_password.getText().toString().trim();
                if (SmsUtil.checkString(user) && SmsUtil.checkString(password)) {
                    login(user, password);
                } else {
                    m_account.setText("");
                    m_account.requestFocus();
                    showShortToast("用户名或密码不能为空");
                }
                break;
            // 重置按钮
            case R.id.login_btn_reset: {
                m_account.setText("");
                m_password.setText("");
                m_account.requestFocus();
            }
            break;
            // 关于我们
            case R.id.login_imgbtn_contactus: {
                openActivity(ContactUsActivity.class);
            }
            break;
            // 设置IP
            case R.id.login_imgbtn_ipset: {
                showIPSettingDialog();
            }
            break;
            // 注册
            case R.id.register_btn_launch: {
                Intent intent = new Intent();
                intent.setAction("android.intent.action.VIEW");
                Uri content_url = Uri.parse(ServerApi.REGISTER_URL);
                intent.setData(content_url);
                startActivity(intent);
            }
            break;
            default:
                break;
        }
    }

    /*
     * IP设置Dialog
     */
    private void showIPSettingDialog() {
        View ipSetView = getLayoutInflater().inflate(
                R.layout.dialog_ip_setting, null);
        final Dialog dialog = new Dialog(this, R.style.dialog);
        dialog.addContentView(ipSetView, new LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        final EditText ipET = (EditText) ipSetView
                .findViewById(R.id.ipSeting_address);
        final EditText portET = (EditText) ipSetView
                .findViewById(R.id.ipSetting_port);
        String[] infos = getIPSeetingInfo();
        ipET.setText(infos[0]);
        portET.setText(infos[1]);
        dialog.show();
        ipSetView.findViewById(R.id.ipSet_btn_sure).setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // 确定按钮 需要判断字符串的书写规范
                        // 保存IP信息
                        saveIPSettingInfo(ipET.getText().toString().trim(),
                                portET.getText().toString().trim());
                        dialog.cancel();
                    }
                });
        ipSetView.findViewById(R.id.ipSet_btn_cancel).setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        dialog.cancel();
                    }
                });
    }

    /**
     * 把IP信息保存在本地
     *
     * @param ip
     * @param port
     */
    private void saveIPSettingInfo(String ip, String port) {
        // 由于修改了ip和端口，SmsApi需要重新创建对象
        ServerApi.setNull();
        SPHelper.getInstance().saveValueToSp(SPHelper.KEY_IP, ip);
        SPHelper.getInstance().saveValueToSp(SPHelper.KEY_PORT, port);
    }

    /**
     * 获取IP信息 <br>
     */
    private String[] getIPSeetingInfo() {
        String[] infos = new String[2];
        infos[0] = ServerApi.mPortalAddress;
        infos[1] = ServerApi.mPortalPort;

        String ip = SPHelper.getInstance().getStringFromSp(SPHelper.KEY_IP);
        String port = SPHelper.getInstance().getStringFromSp(SPHelper.KEY_PORT);
        if (SmsUtil.checkString(ip)) {
            infos[0] = ip;
        }
        if (SmsUtil.checkString(port)) {
            infos[1] = port;
        }
        return infos;
    }

    /**
     * 登录
     *
     * @param user
     * @param pass
     */
    private void login(String user, String pass) {
        showProgressDialog("正在登录...", false, false);
        try {
            JSONObject jo = new JSONObject();
            jo.put("account", user);
            jo.put("password", pass);
            // jo.put("from", 1);
            // jo.put("perPage", 5);
            // jo.put("segments","userId,userName,password,deptId,remark");
            sendPostForCookie(ServerApi.getInstance().USER_LOGIN, jo, null,
                    this, ServerApi.getInstance().USER_LOGIN, false);
        } catch (JSONException e) {
            e.printStackTrace();
            dismissProgressDialog();
        }
    }

    @Override
    public void onRequestFail(String str, Exception ex) {
        // TODO Auto-generated method stub
        super.onRequestFail(str, ex);
        dismissProgressDialog();
        showShortToast("请求连接失败，请稍候再试！");
    }

    @Override
    public void onRequestSuccess(String str, Object obj) {
        // TODO Auto-generated method stub
        Gson gson = new Gson();
        if (ServerApi.getInstance().USER_LOGIN.equals(str)) {
            mLog.i(obj.toString());
            LoginJudge login = gson.fromJson(obj.toString(), LoginJudge.class);
            if (login.getResultCode() == 0) {
                ServerApi.passport = login.getPassport();
                ServerApi.millCode = login.getMillCode();
                ServerApi.factoryCode = login.getFactoryCode();
                ServerApi.account = login.get__account();
                ServerApi.comp = login.getCOMP();
                if (m_savepsd.isChecked()) {
                    String account = m_account.getText().toString().trim();
                    String password = m_password.getText().toString().trim();
                    // save name & password
                    // 已经勾选记住密码，保存用户名和密码到本地的SP文件中
                    SPHelper.getInstance().saveValueToSp(SPHelper.KEY_ACCOUNT,
                            account);
                    SPHelper.getInstance().saveValueToSp(SPHelper.KEY_PASSWORD,
                            password);
                }
                SPHelper.getInstance().saveValueToSp(SPHelper.KEY_IS_REMPSD,
                        m_savepsd.isChecked());
                // 打开indexActivity
                // openActivity(WorkInfoActivity.class);
                openActivity(InputFragmentActivity.class);

                this.finish();
            } else {
                if (login.getResultCode() == 2001) {
                    showShortToast("用户名或密码错误");
                }
                if (login.getResultCode() == 1001) {
                    showShortToast("密码错误");
                }
                m_account.setText("");
                m_password.setText("");
                m_account.setFocusable(true);
                m_account.setFocusableInTouchMode(true);
                m_account.requestFocus();
                m_account.requestFocusFromTouch();
            }
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

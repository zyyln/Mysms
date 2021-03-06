package com.xuesi.sms.app.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;

import com.google.gson.Gson;
import com.xuesi.mos.util.MosLog;
import com.xuesi.sms.R;
import com.xuesi.sms.ServerApi;
import com.xuesi.sms.SmsApplication;
import com.xuesi.sms.bean.BaseVo;

import org.json.JSONObject;

/**
 * <p>
 * Title: SMS-ArrayNaviActivity-阵列导航画面
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
public class ArrayNaviActivity extends SmsActivity {
    /**
     * LOG
     */
    private final String TAG_LOG = ArrayNaviActivity.class.getSimpleName();
    private final MosLog mLog = MosLog.getInstance(TAG_LOG, "XUESI-SMS");
    /**
     * progress的次数
     */
    private int mProgressNumber = 0;

    /**
     * 目标activity 值域{入库、出库、倒垛、库房、行车}
     */
    private String toActivity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        SmsApplication.getInstance().addActivity(this);// 添加到Activity堆栈
        setContentView(R.layout.activity_navi_array);
    }

    @Override
    protected void onResume() {
        super.onResume();
        myNetwork(ServerApi.getInstance().SHEET_GETISLOCK);
    }


    @Override
    protected void initContentView() {
        // TODO Auto-generated method stub
        super.initContentView();
        backImg.setVisibility(View.GONE);
        titleTv.setVisibility(View.GONE);
        // 标题栏控件初始化(删除,layout_header_navi文件可删)
        // findViewById(R.id.navi_btn_back).setOnClickListener(this);
        // findViewById(R.id.navi_btn_refresh).setOnClickListener(this);

        // 导航图标控件初始化
        findViewById(R.id.arrayNavi_linear_worklook).setOnClickListener(this);
        findViewById(R.id.arrayNavi_linear_barocde).setOnClickListener(this);
        findViewById(R.id.arrayNavi_linear_input).setOnClickListener(this);
        findViewById(R.id.arrayNavi_linear_output).setOnClickListener(this);
        findViewById(R.id.arrayNavi_linear_shift).setOnClickListener(this);
        findViewById(R.id.arrayNavi_linear_pandect).setOnClickListener(this);
        findViewById(R.id.arrayNavi_linear_check).setOnClickListener(this);
        findViewById(R.id.arrayNavi_linear_crane).setOnClickListener(this);
        findViewById(R.id.arrayNavi_linear_infoQuery).setOnClickListener(this);
        findViewById(R.id.arrayNavi_linear_systemSetting).setOnClickListener(
                this);
        findViewById(R.id.arrayNavi_linear_billSearch).setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.bartop_img_refresh:
                // 刷新按钮
                myNetwork(ServerApi.getInstance().SHEET_GETISLOCK);
                break;
            case R.id.arrayNavi_linear_worklook:
                // 作业看板按钮
                openActivity(WorkInfoActivity.class);
                break;
            case R.id.arrayNavi_linear_barocde:
                // 条码绑定按钮
                openActivity(BarcodeBindActivity.class);
                break;
            case R.id.arrayNavi_linear_input:
                // 钢板入库按钮
                myNetwork(ServerApi.getInstance().SHEET_GETISLOCK);
                toActivity = InputActivity.class.getSimpleName();
                break;
            case R.id.arrayNavi_linear_output:
                // 钢板出库按钮
                myNetwork(ServerApi.getInstance().SHEET_GETISLOCK);
                toActivity = OutputActivity.class.getSimpleName();
                break;
            case R.id.arrayNavi_linear_shift:
                // 钢板倒垛按钮
                myNetwork(ServerApi.getInstance().SHEET_GETISLOCK);
                toActivity = ShiftActivity.class.getSimpleName();
                break;
            case R.id.arrayNavi_linear_pandect:
                // 库位总览按钮
                myNetwork(ServerApi.getInstance().SHEET_GETISLOCK);
                toActivity = StoreActivity.class.getSimpleName();
                break;
            case R.id.arrayNavi_linear_crane:
                // 行车面板按钮
                myNetwork(ServerApi.getInstance().SHEET_GETISLOCK);
                toActivity = CraneActivity.class.getSimpleName();
                break;
            case R.id.arrayNavi_linear_check:
                // 库存盘点按钮
                openActivity(CheckActivity.class);
                break;
            case R.id.arrayNavi_linear_infoQuery:
                // 信息查询按钮
                openActivity(QueryActivity.class);
                break;
            case R.id.arrayNavi_linear_systemSetting:
                // 系统设定按钮
                openActivity(SettingActivity.class);
                break;
            case R.id.arrayNavi_linear_billSearch:
                // 采购单查询
                openActivity(QueryBillActivity.class);
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
            showProgressDialog("加载中", false, false);
        }
        if (ServerApi.getInstance().SHEET_GETISLOCK.equals(url)) {
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
        if (ServerApi.getInstance().SHEET_GETISLOCK.equals(str)) {
            BaseVo bv = gson.fromJson(obj.toString(), BaseVo.class);
            if (bv.getResultCode() == 0) {
                if (bv.getTotal() > 0) {// 在盘点
                    showShortToast("盘点中...");
                    findViewById(R.id.arrayNavi_tv_input).setVisibility(
                            View.VISIBLE);
                    findViewById(R.id.arrayNavi_tv_total).setVisibility(
                            View.VISIBLE);
                    findViewById(R.id.arrayNavi_tv_output).setVisibility(
                            View.VISIBLE);
                    findViewById(R.id.arrayNavi_tv_shifting).setVisibility(
                            View.VISIBLE);
                    findViewById(R.id.arrayNavi_tv_panelHc).setVisibility(
                            View.VISIBLE);
                } else {// 不在盘点
                    findViewById(R.id.arrayNavi_tv_input).setVisibility(
                            View.GONE);
                    findViewById(R.id.arrayNavi_tv_output).setVisibility(
                            View.GONE);
                    findViewById(R.id.arrayNavi_tv_total).setVisibility(
                            View.GONE);
                    findViewById(R.id.arrayNavi_tv_shifting).setVisibility(
                            View.GONE);
                    findViewById(R.id.arrayNavi_tv_panelHc).setVisibility(
                            View.GONE);

                    if (OutputActivity.class.getSimpleName().equals(toActivity)) {
                        openActivity(OutputActivity.class);
                    } else if (CraneActivity.class.getSimpleName().equals(
                            toActivity)) {
                        openActivity(CraneActivity.class);
                    } else if (null != toActivity) {
                        // 备注：入库、倒垛、库位总览的目标activity一致
                        Bundle bundle = new Bundle();
                        bundle.putString("toActivity", toActivity);
                        openActivity(StoreTypeActivity.class, bundle);
                    }
                    toActivity = null;
                }
            } else {
                showShortToast("resultCode==" + bv.getResultCode() + "，请联系开发人员");
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
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

}

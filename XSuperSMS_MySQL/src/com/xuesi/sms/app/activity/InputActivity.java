package com.xuesi.sms.app.activity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.xuesi.mos.device.Screen;
import com.xuesi.mos.libs.pulltorefresh.PullToRefreshBase;
import com.xuesi.mos.util.MosLog;
import com.xuesi.sms.R;
import com.xuesi.sms.ServerApi;
import com.xuesi.sms.SmsApplication;
import com.xuesi.sms.SmsConfig;
import com.xuesi.sms.bean.GetBillNoAndSheetMsg;
import com.xuesi.sms.bean.ResultCode;
import com.xuesi.sms.bean.Sheet;
import com.xuesi.sms.bean.Sheet.ListClass;
import com.xuesi.sms.bean.Stack;
import com.xuesi.sms.util.SmsUtil;
import com.xuesi.sms.widget.adapter.PopuWindowAdapter;
import com.xuesi.sms.widget.adapter.SheetAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 入库方式:<br>
 * 一级库:扫描入库、采购单入库<br>
 * (需求改动)二级库:扫描入库<br>
 * 余料库:扫描入库、余料列表入库<br>
 *
 * @author XS-PC014
 */
public class InputActivity extends SheetBaseActivity {
    /**
     * LOG
     */
    private final String LOG_TAG = InputActivity.class.getSimpleName();
    private final MosLog mLog = MosLog.getInstance(LOG_TAG, "XUESI-SMS");
    /**
     * progress的次数
     */
    private int mProgressNumber = 0;

    // 选择入库方式-------
    /**
     * 记录入库方式,值域{normal(扫描入库),other(其他方式)}
     */
    private String curInputType;

    // 选择单号下拉框------
    private PopupWindow billsNOPopWin;
    private LinearLayout billsNOLinear;
    protected TextView billsNOLeftTv, billsNOTv;
    /**
     * 下拉选择单号列表
     */
    protected ListView billsNOListView;
    /**
     * 选择单号适配器
     */
    protected PopuWindowAdapter billsNoAdapter;
    /**
     * 当前选中的单号
     */
    protected GetBillNoAndSheetMsg.BnAndSm curBillsNO;

    private EditText barcodeET;

    /**
     * 扫描钢板数
     */
    private TextView scanCountTv;
    /**
     * 记录扫描的钢板个数
     */
    private int scanSheetCount = 0;

    // 钢板列表
    /**
     * 数据源：SheetByCode.ListClass
     */
    // private SheetAdapter sheetAdapter;
    private Dialog detailDialog;
    /**
     * 数据源：Sheet.ListClass
     */
    private SheetAdapter sheetSAdapter;
    /**
     * 选中钢板的集合
     */
    private List<Sheet.ListClass> selctSheetList = new ArrayList<Sheet.ListClass>();
    /**
     * 当前的位置
     */
    private int curPosition;
    /**
     * 客户设置的垛位信息
     */
    private List<String> stackSetList;

    /**
     * 行车勾选框
     */
    private CheckBox seleCrane;
    /**
     * 记录吊取钢板数，重量
     */
    private int curSheetNum = 0;
    private double curSheetWeight = 0.0;
    /**
     * 显示计算吊取钢板数
     */
    private EditText countSheetTxt, weightSheetTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        SmsApplication.getInstance().addActivity(this);// 添加到Activity堆栈
        setContentView(R.layout.activity_input);

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
        setTopTitle(R.string.sheet_input);
        setRefreshView(View.INVISIBLE);

        barcodeET = (EditText) findViewById(R.id.input_et_barcode);
        // 显示已经扫描的个数
        scanCountTv = (TextView) findViewById(R.id.input_tv_scanCount);
        scanCountTv.setText(getString(R.string.accumulative_total_)
                + scanSheetCount);

        // 扫描按钮
        findViewById(R.id.input_btn_scan).setOnClickListener(this);
        // 入库按钮
        findViewById(R.id.input_btn_input).setOnClickListener(this);
        // 退出按钮
        // findViewById(R.id.sheetIn_btn_exit).setOnClickListener(this);

        // 扫描的钢板列表
        sheetListView = (ListView) findViewById(R.id.input_lv_info);
        sheetListView.setOnItemClickListener(this);

        countSheetTxt = (EditText) findViewById(R.id.shift_tv_countSheet);
        weightSheetTxt = (EditText) findViewById(R.id.sheet_tv_weight);
        // 勾选行车
        seleCrane = (CheckBox) findViewById(R.id.input_cb_crane);
        seleCrane.setChecked(SmsConfig.isCraneCheck);
    }

    private void loadExtras() {
        // TODO Auto-generated method stub
        Bundle bundle = getIntent().getExtras();
        curStoreType = bundle.getString("curStoreType");

        // 入库方式选择弹出框------
        final Dialog dialog = new Dialog(this, R.style.dialog_storetype);
        View view = getLayoutInflater()
                .inflate(R.layout.dialog_inputtype, null);
        dialog.addContentView(view, new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT));
        dialog.setCanceledOnTouchOutside(false);
        if (curStoreType.equals("RCK")) {
            ((RadioButton) view.findViewById(R.id.input_radioBtn_other))
                    .setText(getString(R.string.rck_remaining));
        }
        ((RadioGroup) view.findViewById(R.id.input_radg_inputtype))
                .setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        // 一级库、二级库、余料库选择事件处理
                        if (checkedId == R.id.input_radioBtn_normal) {
                            curInputType = "normal";
                            findViewById(R.id.input_linear_barcode)
                                    .setVisibility(View.VISIBLE);
                            findViewById(R.id.input_relative_scan)
                                    .setVisibility(View.VISIBLE);
                        } else if (checkedId == R.id.input_radioBtn_other) {
                            curInputType = "other";
                            if (curStoreType.equals("FSK")) {
                                // 采购单号入库
                                // 选择单号下拉框-------
                                findViewById(R.id.input_linear_selctbill)
                                        .setVisibility(View.VISIBLE);
                                billsNOLinear = (LinearLayout) findViewById(R.id.input_linear_selctbill);
                                billsNOTv = (TextView) findViewById(R.id.input_tv_billsno);
                                billsNOLeftTv = (TextView) findViewById(R.id.input_tv_billsnoLeft);
                                // 单号下拉按钮
                                findViewById(R.id.input_imgbtn_selctbill)
                                        .setOnClickListener(InputActivity.this);
                            } else if (curStoreType.equals("RCK")) {
                                // 余料列表入库
                                myNetwork(ServerApi.getInstance().API_GETREMLIST);
                            }
                            // if (curStoreType.equals("SCK")) {
                            // findViewById(R.id.input_linear_selctbill).setVisibility(View.GONE);
                            // findViewById(R.id.input_linear_inputtype).setVisibility(View.GONE);
                            // }
                        }
                        // 初始画面为默认显示一级库下第一个库房下的垛位
                        myNetwork(ServerApi.getInstance().SHEET_GETHOUSEID);
                        dialog.dismiss();
                    }
                });
        // 确定按钮
        view.findViewById(R.id.input_btn_confirm).setOnClickListener(this);
        // 覆盖对话框的按键事件
        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {

            @Override
            public boolean onKey(DialogInterface dialogInterface, int keyCode,
                                 KeyEvent keyEvent) {
                // TODO Auto-generated method stub
                if (keyCode == KeyEvent.KEYCODE_BACK
                        && keyEvent.getRepeatCount() == 0) {
                    dialog.dismiss();
                    InputActivity.this.finish();
                }
                return false;
            }
        });
        dialog.show();
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        // super.onClick(v);
        switch (v.getId()) {
            case R.id.bartop_img_back:
                if ("normal".equals(curInputType) && null != sheetSAdapter
                        && sheetSAdapter.getCount() > 0) {
                    showAlertDialog(this, getString(R.string.prompt),
                            getString(R.string.msg_input_exit), "finish");
                } else {
                    finish();
                }
                break;
            case R.id.input_btn_confirm: {
                // if (null != curInputType) {
                // } else {
                // toastShort("请选择入库方式！");
                // }
            }
            break;
            // case R.id.storehousetype_btn_confirm: {// 选择库房类型的确定按钮
            // if (null != curStoreType) {
            // storeTypeLayout.setVisibility(View.GONE);
            // // 初始画面为默认显示一级库下第一个库房下的垛位
            // myNetwork(SmsApi.getInstance().SHEET_GETHOUSEID, "");
            // } else {
            // toastShort("请选择库房类型！");
            // }
            // }
            // break;
            case R.id.housestore_btn_arrow: {// 选择仓库
                if (selctSheetList.size() == 0) {
                    myNetwork(ServerApi.getInstance().SHEET_GETHOUSEID, "");
                } else {
                    showShortToast("正在入库中");
                }
            }
            break;
            case R.id.input_imgbtn_selctbill: {// 选择单号
                myNetwork(ServerApi.getInstance().ORDER_GETBILLNOS);
            }
            break;
            case R.id.input_btn_scan: {// 确定按钮
                String barcode = barcodeET.getText().toString().replaceAll(" ", "");
                if (SmsUtil.checkString(barcode)) {
                    if (null != sheetSAdapter && judgeBarcode(barcode)) {
                        showShortToast("钢板已在列表中！");
                    } else {
                        myNetwork(ServerApi.getInstance().API_GETBARCODE, barcode);
                    }
                }
            }
            break;
            // case R.id.sheetIn_btn_exit: {// 退出按钮
            // findViewById(R.id.sheetIn_linear_sheet).setVisibility(View.GONE);
            // // stackGridView.setVisibility(View.VISIBLE);
            // // stackGridView.getVisibility();
            // }
            // break;
            case R.id.seleCrane_btn_confirm: {// 选择行车确定按钮
                craneDialog.cancel();
                if (null != curSelctCrane) {
                    craneDialog.cancel();
                    input();
                } else {
                    showShortToast("请选择行车!");
                }
            }
            break;
            case R.id.input_btn_input: {// 入库按钮
                if (selctSheetList.size() > 0) {
                    if (null != toStack) {
                        mLog.d("选中的钢板数量==" + selctSheetList.size());
                        curSelctCrane = null;
                        if (seleCrane.isChecked()) {// 行车勾选则查询行车信息
                            myNetwork(ServerApi.getInstance().API_GET_CRANELIST, "");
                        } else {// 行车未勾选则入库
                            input();
                        }
                    } else {
                        showShortToast("请选择目标垛位！");
                    }
                }
            }
            break;
            default:
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        // TODO Auto-generated method stub
        // 库房列表项\行车事件在父类----
        super.onItemClick(parent, view, position, id);

        // 选择采购单号------
        if (parent == billsNOListView) {
            curBillsNO = billsNoAdapter.getItem(position);
            billsNOTv.setText(curBillsNO.getBillsNo());
            // 获取钢板信息
            myNetwork(ServerApi.getInstance().ORDER_GETORDERLIST,
                    curBillsNO.getBillsNo());
            if (billsNOPopWin.isShowing()) {
                billsNOPopWin.dismiss();
            }
        }

        // --------------
        // 钢板列表项
        // 扫描入库：(可多选入库,需按上下顺序选择钢板)
        // 采购单号：(可多选入库,任意选择钢板)
        // 余料列表：(可多选入库,任意选择钢板)
        // --------------
        if (parent == sheetListView) {

            // 1、只要是在列表中的数据，点击选择先弹出一个对话框，弹出框中显示的就是列表中的数据
            // 当前选中的钢板

            // if ("normal".equals(curInputType)) {
            // 扫描入库
            showSheetDialog(sheetSAdapter.getList());
            // }
            // else if ("other".equals(curInputType)) {
            // // 其他方式(可多选入库,任意选择钢板)
            // if (curSheet.isSelected()) {// 状态为选中
            // curSheet.setSelected(false);
            // selctSheetList.remove(curSheet);
            // curSheet.setSelectNum("");
            // --curSheetNum;
            // curSheetWeight = curSheetWeight
            // - Double.parseDouble(curSheet.getWeight());
            // for (int i = selctSheetList.size() - 1; i >= 0; i--) {
            // selctSheetList.get(i).setSelectNum(i + 1 + "");
            // }
            // } else {
            // curSheet.setSelected(true);
            // // 钢板实际层号是从底部到顶部的
            // selctSheetList.add(curSheet);
            // for (int i = selctSheetList.size() - 1; i >= 0; i--) {
            // selctSheetList.get(i).setSelectNum(i + 1 + "");
            // }
            // ++curSheetNum;
            // curSheetWeight = curSheetWeight
            // + Double.parseDouble(curSheet.getWeight());
            // }
            // if (selctSheetList.size() == 1) {
            // // 当第一次选钢板的时候推荐垛位
            // if (curStoreType.equals("RCK")) {
            // // 余料列表数据
            // myNetwork(ServerApi.getInstance().API_GETSTACKINFONAME,
            // curSheet.getMetalBillId());
            // } else {
            // // 其他钢板数据
            // myNetwork(ServerApi.getInstance().API_GETSTACKINFONAME,
            // curSheet.getMetalBillId());
            // }
            // }
            // }
            //
            // // 更新垛位
            // if (selctSheetList.size() == 0) {
            // toStack = null;
            // for (Stack.ListClass stack : stackAdapter.getList()) {
            // stack.setToSelect(false);
            // stack.setRecommend(false);
            // }
            // stackAdapter.notifyDataSetChanged();
            // curSheetNum = 0;
            // curSheetWeight = 0.00;
            // }
            // sheetSAdapter.notifyDataSetChanged();
            // countSheetTxt.setText(curSheetNum +
            // getString(R.string.steel_unit));
            // weightSheetTxt.setText(SmsUtil.DecimalFormat(curSheetWeight,
            // SmsConfig.dotNum) + "Kg");
            //
        }

        // ------------
        // 垛位列表项,二级库临时库位要排除
        // ------------
        if (parent == stackGridView) {
            if (selctSheetList.size() == 0) {
                // 单击垛位进入钢板列表
            } else {
                Stack.ListClass stack = (Stack.ListClass) stackAdapter
                        .getItem(position);
                mLog.d("curSheetNum==" + selctSheetList.size());
                // 单击垛位进行入库(任意垛位都可以进行入库(二级库入库时排除临时库位))
                for (Stack.ListClass stackList : stackAdapter.getList()) {
                    stackList.setToSelect(false);
                }
                if (curStoreType.equals("SCK")) {
                    String stackName = stack.getSTACKNAME();
                    // !stackName.substring(0, 2).equals("LS")
                    if (!stackName.contains("LS")) {
                        stack.setToSelect(true);
                        toStack = stack;
                    } else {
                        showShortToast("不能选临时库入库！");
                    }
                } else {
                    stack.setToSelect(true);
                    toStack = stack;
                }
                stackAdapter.notifyDataSetChanged();
                // if (stackItem.getRecommend()) {
                // toStack = stackConfList.get(position);
                // } else {
                // showToast("请选择推荐垛位！");
                // }
            }
        }
    }

    private void showSheetDialog(final List<ListClass> list) {
        View view = getLayoutInflater().inflate(R.layout.dialog_input_detail,
                null);
        ListView lv = (ListView) view.findViewById(R.id.lv_msg);
        Button btn_confirm = (Button) view.findViewById(R.id.btn_confirm);
        detailDialog = getDialog(this, view);
        detailDialog.setCanceledOnTouchOutside(false);
        lv.setAdapter(sheetSAdapter);
        btn_confirm.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                detailDialog.dismiss();
            }
        });
        lv.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub
                Sheet.ListClass curSheet = sheetSAdapter.getItem(position);
                if (selctSheetList.size() != 0) {
                    mLog.d("第二或N次选");
                    curPosition = position;
                    if (curSheet.isSelected()) {// 状态为选中
                        --curSheetNum;
                        curSheetWeight = curSheetWeight
                                - Double.parseDouble(curSheet.getWeight());
                        --curPosition;

                        curSheet.setSelected(false);
                        selctSheetList.remove(curSheet);
                        curSheet.setSelectNum("");
                        for (int i = selctSheetList.size() - 1; i >= 0; i--) {
                            selctSheetList.get(i).setSelectNum(i + 1 + "");
                        }
                    } else {
                        ++curSheetNum;
                        curSheetWeight = curSheetWeight
                                + Double.parseDouble(curSheet.getWeight());
                        if (!Sheet.equalsSheet(curSheet, selctSheetList.get(0))) {
                            // 警告用户，但是仍然让选
                        }

                        curSheet.setSelected(true);
                        // 规则：一批选择的钢板入库原则是平移
                        selctSheetList.add(curSheet);
                        for (int i = selctSheetList.size() - 1; i >= 0; i--) {
                            selctSheetList.get(i).setSelectNum(i + 1 + "");
                        }
                    }
                } else {
                    mLog.d("第一次选");
                    curPosition = position;
                    ++curSheetNum;
                    curSheetWeight = curSheetWeight
                            + Double.parseDouble(curSheet.getWeight());
                    selctSheetList.add(curSheet);
                    curSheet.setSelected(true);
                    curSheet.setSelectNum(selctSheetList.size() + "");

                    // 当第一次选钢板的时候推荐垛位
                    myNetwork(ServerApi.getInstance().API_GETSTACKINFONAME,
                            curSheet.getMetalBillId());
                }

                // 更新垛位
                loadSheetlist();
            }
        });
        showDialog(detailDialog);
        // detailDialog.show();
    }

    private void loadSheetlist() {
        if (selctSheetList.size() == 0) {
            toStack = null;
            for (Stack.ListClass stack : stackAdapter.getList()) {
                stack.setToSelect(false);
                stack.setRecommend(false);
            }
            stackAdapter.notifyDataSetChanged();
            curSheetNum = 0;
            curSheetWeight = 0.00;
        }
        sheetSAdapter.notifyDataSetChanged();
        countSheetTxt.setText(curSheetNum + getString(R.string.steel_unit));
        weightSheetTxt.setText(SmsUtil.DecimalFormat(curSheetWeight,
                SmsConfig.dotNum) + "Kg");
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view,
                                   int position, long id) {
        // TODO Auto-generated method stub
        if (parent == stackGridView) {
            Stack.ListClass curSelctStack = stackAdapter.getItem(position);
            if (curSelctStack.getSumAmount() > 0) {
                Bundle bundle = new Bundle();
                bundle.putString("curStoreType", curStoreType);
                bundle.putSerializable("curSelctStore", curSelctStore);
                bundle.putSerializable("curSelctStack", curSelctStack);
                bundle.putBoolean("isQuery", false);
                // 打开钢板详情画面
                openActivity(SheetDetailActivity.class, bundle);
            } else {
                showShortToast("当前垛位为空！");
            }
        }
        return super.onItemLongClick(parent, view, position, id);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            if ("normal".equals(curInputType) && null != sheetSAdapter
                    && sheetSAdapter.getCount() > 0) {
                showAlertDialog(this, getString(R.string.prompt),
                        getString(R.string.msg_input_exit), "finish");
            } else {
                finish();
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase<GridView> refreshView) {
        // TODO Auto-generated method stub
        super.onPullDownToRefresh(refreshView);
        if (null == curSelctStore) {
            stackPullRefreshGridView.onRefreshComplete();
            return;
        }
        // 取消弹出加载对话框
        mProgressNumber = 1;
        myNetwork(ServerApi.getInstance().API_GET_STACKINFO);
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase<GridView> refreshView) {
        // TODO Auto-generated method stub
    }

    private void input() {
        if (curStoreType.equals("FSK")) {// 一级库入库
            myNetwork(ServerApi.getInstance().SHEET_INSTACK, "");
        }
        if (curStoreType.equals("RCK")) {// 余料库入库
            myNetwork(ServerApi.getInstance().API_ADDREMNANTSTACK, "");
        }
    }

    @Override
    protected void onScanResult(final String barcode) {
        // TODO Auto-generated method stub
        barcodeET.setText(barcode);
        if (SmsUtil.checkString(barcode)) {
            if (null != sheetSAdapter && judgeBarcode(barcode)) {
                showShortToast("钢板已在列表中！");
            } else {
                myNetwork(ServerApi.getInstance().API_GETBARCODE,
                        barcode);
            }
        }
    }

    private boolean judgeBarcode(String barcode) {
        for (int i = 0; i < sheetSAdapter.getCount(); i++) {
            if (sheetSAdapter.getItem(i).getBarcode().equals(barcode)) {
                return true;
            }
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
        JSONObject json;
        if (ServerApi.getInstance().API_GETBARCODE.equals(tag)) {
            // 根据条形码获取钢板数据
            try {
                json = getRequstJson();
                json.put("pBarcode", txt[0].toString());
                json.put("pType", curStoreType);
                sendPOST(tag, json, null, this, tag, false);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (ServerApi.getInstance().ORDER_GETBILLNOS.equals(tag)) {
            // 获取已发布的采购单号
            try {
                json = getRequstJson();
                json.put("status", 1);
                json.put("pISREM", 0);// 0:整板
                json.put("perPage", 9999);
                sendPOST(tag, json, null, this, tag, false);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (ServerApi.getInstance().ORDER_GETORDERLIST.equals(tag)) {
            // 根据采购单号获取钢板数据
            try {
                json = getRequstJson();
                json.put("billNo", txt[0]);
                json.put("flag", 0);// 是否入库 状态( 0 未入库; 1已入库 ;2:未入库但已在行车上)
                json.put("perPage", 9999);
                json.put("status", 1);// 0:未发布，1：已发布
                json.put("pISREM", 0);// 0：整板
                json.put("field", "MATERIAL desc");
                sendPOST(tag, json, null, this, tag, false);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (ServerApi.getInstance().API_GETREMLIST.equals(tag)) {
            // 获取余料列表数据
            try {
                json = getRequstJson();
                json.put("status", 1);// 状态 1:真实余料 ;2:已入库
                sendPOST(tag, json, null, this, tag, false);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (ServerApi.getInstance().SHEET_INSTACK.equals(tag)) {
            // 一级库入库
            JSONArray jsonArray = new JSONArray();
            try {
                json = new JSONObject();
                // 对于采购单号，入库后的物理位置要和选择的单号顺序一致，先选的在上面
                for (int i = selctSheetList.size() - 1; i >= 0; i--) {
                    JSONObject jsonObj = new JSONObject();
                    if (curSelctCrane != null) {
                        jsonObj.put("flag", 1);
                        jsonObj.put("deviceId", curSelctCrane.getDCODEID());
                    }
                    jsonObj.put("stackNo", toStack.getSTACKNO());
                    // if ("normal".equals(curInputType)) {
                    // jsonObj.put("sheetId", selctSheetList.get(i).getId());
                    // } else if ("other".equals(curInputType)) {
                    jsonObj.put("sheetId", selctSheetList.get(i)
                            .getMetalBillId());
                    // }
                    jsonObj.put("user", ServerApi.account);
                    jsonObj.put(ServerApi.PARA_FACTORYCODE,
                            ServerApi.factoryCode);
                    jsonArray.put(jsonObj);
                }
                json.put(ServerApi.COOKIE_MILLCODE, ServerApi.millCode);
                json.put("SheetList", jsonArray);
                json.put(ServerApi.COOKIE_PASSPORT, ServerApi.passport);
                sendPOST(tag, json, null, this, tag, false);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (ServerApi.getInstance().API_ADDREMNANTSTACK.equals(tag)) {
            // 余料库入库
            JSONArray jsonArray = new JSONArray();
            try {
                json = new JSONObject();
                // 对于采购单号，入库后的物理位置要和选择的单号顺序一致，先选的在上面
                for (int i = selctSheetList.size() - 1; i >= 0; i--) {
                    JSONObject jsonObj = new JSONObject();
                    if (curSelctCrane != null) {
                        jsonObj.put("flag", 1);
                        jsonObj.put("deviceId", curSelctCrane.getDCODEID());
                    }
                    jsonObj.put("stackNo", toStack.getSTACKNO());
                    jsonObj.put("remnantId", selctSheetList.get(i)
                            .getMetalBillId());
                    jsonObj.put("remnantname", selctSheetList.get(i)
                            .getSHEETNAME());
                    jsonObj.put("creater", ServerApi.account);
                    jsonObj.put(ServerApi.PARA_FACTORYCODE,
                            ServerApi.factoryCode);
                    jsonArray.put(jsonObj);
                }
                json.put(ServerApi.COOKIE_MILLCODE, ServerApi.millCode);
                json.put("RemInfo", jsonArray);
                json.put(ServerApi.COOKIE_PASSPORT, ServerApi.passport);
                sendPOST(tag, json, null, this, tag, false);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            // showShortToast("错误的网络请求，请检查");
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
        mLog.init(tag, LOG_TAG);
        mLog.i(LOG_TAG + obj.toString());
        Gson gson = new Gson();
        if (ServerApi.getInstance().API_GETBARCODE.equals(tag)) {
            // 根据条形码获取钢板数据(未入库钢板)

            Sheet sheet = gson.fromJson(obj.toString(), Sheet.class);
            if (sheet.getResultCode() == 0) {
                if (sheet.getList().size() > 0) {
                    barcodeET.setText("");
                    if (null == sheetSAdapter) {
                        // 首次加载
                        sheetSAdapter = new SheetAdapter(this, sheet.getList());
                        sheetListView.setAdapter(sheetSAdapter);
                    } else {
                        sheetSAdapter.getList().addAll(sheet.getList());
                        sheetSAdapter.notifyDataSetChanged();
                    }
                    scanCountTv.setText(getString(R.string.accumulative_total_)
                            + (++scanSheetCount));
                } else {
                    showShortToast("待入库钢板中没有这张钢板！");
                }
            } else if (sheet.getResultCode() == 1001) {
                showShortToast("钢板已发送行车！");
            } else {
                showShortToast("resultCode==" + sheet.getResultCode()
                        + "，请联系开发人员");
            }
        } else if (ServerApi.getInstance().ORDER_GETBILLNOS.equals(tag)) {
            // 获取采购单号
            GetBillNoAndSheetMsg gbn = gson.fromJson(obj.toString(),
                    GetBillNoAndSheetMsg.class);
            if (gbn.getResultCode() == 0) {
                billsNoAdapter = new PopuWindowAdapter(this, gbn.getList(), 0);
                showBillNoPopup();
            } else {
                showShortToast("resultCode==" + gbn.getResultCode()
                        + "，请联系开发人员");
            }
        } else if (ServerApi.getInstance().ORDER_GETORDERLIST.equals(tag)) {
            // 获取采购单明细
            selctSheetList.clear();
            Sheet sheet = gson.fromJson(obj.toString(), Sheet.class);
            if (sheet.getResultCode() == 0) {
                if (sheet.getList().size() > 0) {
                    sheetSAdapter = new SheetAdapter(this, sheet.getList());
                    sheetListView.setAdapter(sheetSAdapter);
                    loadSheetlist();
                } else {
                    showShortToast("采购单号没有数据！");
                }
            } else {
                showShortToast("resultCode==" + sheet.getResultCode()
                        + "，请联系开发人员");
            }
        } else if (ServerApi.getInstance().API_GETREMLIST.equals(tag)) {
            Sheet sheet = gson.fromJson(obj.toString(), Sheet.class);
            if (sheet.getResultCode() == 0) {
                if (sheet.getList().size() > 0) {
                    // sheetAdapter = new SheetAdapter(this,
                    // sheetByCode.getList(), "remnant");
                    sheetSAdapter = new SheetAdapter(this, sheet.getList());
                    sheetListView.setAdapter(sheetSAdapter);
                } else {
                    showShortToast("余料列表没有数据！");
                }
            } else {
                showShortToast("resultCode==" + sheet.getResultCode()
                        + "，请联系开发人员");
            }
        } else if (ServerApi.getInstance().SHEET_INSTACK.equals(tag)
                || ServerApi.getInstance().API_ADDREMNANTSTACK.equals(tag)) {
            ResultCode rc = gson.fromJson(obj.toString(), ResultCode.class);
            if (rc.getResultCode() == 0) {
                if (rc.getCodeList().contains(1002)) {// 正在盘点
                    showShortToast("正在盘点，请勿入库");
                } else {
                    if (rc.getCodeList().contains(1004)) {
                        showShortToast("存在钢板已在行车");
                    } else if (rc.getCodeList().contains(1003)) {
                        showShortToast("存在钢板已在垛位");
                    } else {
                        if (null != curSelctCrane) {// 勾选行车
                            showShortToast("成功发送行车");
                        } else {// 未勾选行车
                            showShortToast("入库成功");
                        }
                    }
                    // 更新垛位信息
                    myNetwork(ServerApi.getInstance().API_GET_STACKINFO);
                    // 更新钢板列表
                    sheetSAdapter.getList().removeAll(selctSheetList);
                    sheetSAdapter.notifyDataSetChanged();

                    // 清空存储选中钢板List
                    selctSheetList.clear();
                    // 置空选中的垛位
                    toStack = null;
                    // 置空选中的行车
                    curSelctCrane = null;
                }
            } else {
                showShortToast("resultCode==" + rc.getResultCode() + "，请联系开发人员");
            }
        }
    }

    /**
     * 显示选择单号弹出框
     */
    private void showBillNoPopup() {
        if (null != billsNOPopWin && billsNOPopWin.isShowing()) {
            billsNOPopWin.dismiss();
        }
        int width = billsNOLinear.getWidth();
        // 选择库房弹出部分
        View popView = getLayoutInflater().inflate(R.layout.popup_storehouse,
                null);
        billsNOListView = (ListView) popView
                .findViewById(R.id.popup_lv_houseName);
        billsNOListView.setOnItemClickListener(this);
        billsNOListView.setAdapter(billsNoAdapter);

        billsNOPopWin = new PopupWindow(popView, width,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        billsNOPopWin.setFocusable(true);
        billsNOPopWin.setOutsideTouchable(true);
        billsNOPopWin.setBackgroundDrawable(new BitmapDrawable());
        billsNOPopWin.setAnimationStyle(R.style.AnimTop2);
        if (billsNoAdapter.getCount() > 5) {
            billsNOPopWin
                    .setHeight(Screen.getInstance().getScreenHeight() * 4 / 10);
        }
        if (billsNOPopWin.isShowing()) {
            billsNOPopWin.dismiss();
        } else {
            int[] position = new int[2];
            billsNOLinear.getLocationOnScreen(position);
            int height = billsNOLinear.getHeight();
            int y = position[1] + height;
            // 在指定位置显示
            billsNOPopWin.showAtLocation(billsNOLinear, Gravity.LEFT
                    | Gravity.TOP, position[0], y);
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

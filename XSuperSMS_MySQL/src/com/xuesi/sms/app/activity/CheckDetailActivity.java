package com.xuesi.sms.app.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.device.ScanManager;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import com.android.uhflibs.As3992demoISO6C;
import com.google.gson.Gson;
import com.motorolasolutions.adc.decoder.SE4500_DEMO;
import com.xuesi.mos.libs.dragsortlistview.DragSortListView;
import com.xuesi.mos.util.MosLog;
import com.xuesi.sms.R;
import com.xuesi.sms.ServerApi;
import com.xuesi.sms.SmsApplication;
import com.xuesi.sms.SmsConfig;
import com.xuesi.sms.bean.BaseModel;
import com.xuesi.sms.bean.Sheet;
import com.xuesi.sms.bean.Stack;
import com.xuesi.sms.bean.StackExplain;
import com.xuesi.sms.bean.StoreHouse;
import com.xuesi.sms.util.SmsUtil;
import com.xuesi.sms.widget.adapter.CheckDetailAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 盘点明细
 *
 * @author XS-PC014
 */
public class CheckDetailActivity extends SmsActivity implements
        SE4500_DEMO.Callback, As3992demoISO6C.Callback,
        DragSortListView.DropListener, DragSortListView.RemoveListener {
    /**
     * LOG
     */
    private final String LOG_TAG = CheckDetailActivity.class.getSimpleName();
    private final MosLog mLog = MosLog.getInstance(LOG_TAG, "XUESI-SMS");
    /**
     * progress的次数
     */
    private int mProgressNumber = 0;

    // 条形码显示框
    // private EditText barcodeET;

    // --UROVO PAD扫描部分
    private SoundPool soundPool = null;
    private int soundid;
    /**
     * 广播
     */
    private BroadcastReceiver receiver;

    /**
     * kt45 and tt35设备条形码扫描类
     */
    private SE4500_DEMO se4500;

    /**
     * kt45 and tt35设备超高频扫描类
     */
    private As3992demoISO6C aIso6c;

    /**
     * 条形码
     */
    String code = null;

    // 钢板列表项------
    /**
     * 库存钢板列表
     */
    private ListView serverListView, liveListVie;
    /**
     * 实况钢板列表
     */
    private DragSortListView liveListView;
    /**
     * 库存钢板，实况钢板适配器
     */
    private CheckDetailAdapter serverAdapter, liveAdapter;
    /**
     * 虚拟钢板
     */
    private Sheet.ListClass virSheet = new Sheet().new ListClass();
    ;

    /**
     * 当前库房
     */
    private StoreHouse.ListClass curHouse;
    /**
     * 当前垛位
     */
    private Stack.ListClass formStack;
    /**
     * 当前垛位(不同的返回字段)
     */
    private StackExplain.StackClass curStackExplain;

    /**
     * Stack.ListClass 垛位的仓库号, 垛位号, 高度, 张数,材质, 厚度
     */
    private EditText stackTxt;
    /**
     * 确认按钮,清除按钮
     */
    private Button confirmBtn;

    /**
     * 垛位盘点结果-0：未盘点，1：盘点无误,2：盘点有误 3：需要倒垛
     */
    private int checkResult = -1;

    /**
     * 保存要填写的的垛位信息
     */
    protected List<StackExplain.StackMsg> stackMsgList = new ArrayList<StackExplain.StackMsg>();
    /**
     * 符合当前钢板存放的垛位ID
     */
    private List<String> stacknoList;
    private Sheet.ListClass curLiveSheet;

    /**
     * HoneyWell
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        SmsApplication.getInstance().addActivity(this);// 添加到Activity堆栈
        setContentView(R.layout.activity_check_detail);
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
        setTopTitle(R.string.stock_check_detail);
        setRefreshView(View.INVISIBLE);

        stackTxt = (EditText) findViewById(R.id.checkdetail_tv_stack);
        // 提交、清除盘点按钮
        confirmBtn = (Button) findViewById(R.id.checkdetail_ok_btn);
        confirmBtn.setOnClickListener(this);
        // cleanBtn = (Button) findViewById(R.id.checkdetail_clean_btn);
        // cleanBtn.setOnClickListener(this);

        // 库存钢板列表
        serverListView = (ListView) findViewById(R.id.checkdetail_lv_server);
        // 实况钢板列表
        // liveListVie = (ListView) findViewById(R.id.checkdetail_lv_local);
        liveListView = (DragSortListView) findViewById(R.id.checkdetail_dslv_live);
        liveListView.setDropListener(this);
        liveListView.setRemoveListener(this);

        // 条形码按钮
        findViewById(R.id.checkdetail_btn_).setOnClickListener(this);
        // 手机条形码按钮
        findViewById(R.id.img_scan).setOnClickListener(this);

    }

    private void loadExtras() {
        // TODO Auto-generated method stub
        Bundle bundle = getIntent().getExtras();
        curHouse = (StoreHouse.ListClass) bundle
                .getSerializable("curSelctStore");
        formStack = (Stack.ListClass) bundle.getSerializable("curSelctStack");

        // if (formStack.getCKECK() != 0) {
        // cleanBtn.setVisibility(View.VISIBLE);
        // confirmBtn.setVisibility(View.GONE);
        // } else {
        initScan();
        // }
        //
        myNetwork(ServerApi.getInstance().API_GET_WORKLIST_ANDINSTACK);
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        // super.onClick(v);
        switch (v.getId()) {
            case R.id.bartop_img_back: {
                CheckDetailActivity.this.setResult(1011);
                this.finish();
            }
            break;
            // 提交盘点按钮
            case R.id.checkdetail_ok_btn: {
                new AlertDialog.Builder(this)
                        .setTitle(R.string.prompt)
                        .setMessage("是否完成盘点？")
                        .setPositiveButton(getString(R.string.confirm),
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        // 全部重新比对结果
                                        if (null != serverAdapter
                                                && null != liveAdapter) {
                                            for (int i = serverAdapter.getCount() - 1; i >= 0; i--) {
                                                if (serverAdapter.getItem(i)
                                                        .equals(virSheet)
                                                        || liveAdapter.getItem(i)
                                                        .equals(virSheet)) {
                                                    continue;
                                                }
                                                curLiveSheet = liveAdapter
                                                        .getItem(i);
                                                comparisonSheet(serverAdapter
                                                        .getItem(i));
                                            }

                                            // 清除虚拟钢板
                                            for (int i = 0; i < serverAdapter
                                                    .getCount(); i++) {
                                                if (serverAdapter.getItem(i)
                                                        .equals(virSheet)) {
                                                    serverAdapter.removeItem(i);
                                                }
                                            }
                                            for (int i = 0; i < liveAdapter
                                                    .getCount(); i++) {
                                                if (liveAdapter.getItem(i).equals(
                                                        virSheet)) {
                                                    liveAdapter.removeItem(i);
                                                }
                                            }

                                            // 库存钢板大于实际钢板(两边不相等则盘点有误)
                                            if (serverAdapter.getCount() != liveAdapter
                                                    .getCount()) {
                                                checkResult = 2;

                                            } else if (serverAdapter.getCount() == 0) {
                                                // 库存和实际都为空(盘点无误)
                                                checkResult = 1;
                                            } else {
                                                // 库存和实际钢板数量一致
                                                for (Sheet.ListClass sheet : liveAdapter
                                                        .getList()) {
                                                    if (sheet.getSheetStatus() == 1) {
                                                        checkResult = 2;
                                                        break;
                                                    } else if (sheet
                                                            .getSheetStatus() == 2
                                                            || sheet.getSheetStatus() == 3) {// 需要倒垛
                                                        checkResult = 3;
                                                    }
                                                }
                                            }
                                        }
                                        if (checkResult == -1) {// 盘点无误
                                            checkResult = 1;
                                        }
                                        if (formStack.getCheckFlag() != 0) {
                                            // 更改盘点结果需要清除后再提交
                                            myNetwork(ServerApi.getInstance().API_CLEANGATHER);
                                        } else {
                                            myNetwork(ServerApi.getInstance().API_ADDGATHER);
                                        }
                                    }
                                })
                        .setNegativeButton(getString(R.string.cancel), null)
                        .setCancelable(true).show();
            }
            break;
            // 清除盘点信息
            case R.id.checkdetail_clean_btn: {
                new AlertDialog.Builder(this)
                        .setTitle(R.string.prompt)
                        .setMessage("是否清除盘点？")
                        .setPositiveButton(getString(R.string.confirm),
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        myNetwork(ServerApi.getInstance().API_CLEANGATHER);
                                    }
                                })
                        .setNegativeButton(getString(R.string.cancel), null)
                        .setCancelable(true).show();
            }
            break;
            case R.id.checkdetail_btn_:// 弹框
                editDialog();
                break;
            case R.id.img_scan:// 手机扫描
                openActivity(CaptureActivity.class, 0);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        code = data.getStringExtra("SCAN_RESULT");

        if ("".equals(code) || code == null) {

        } else {

            myNetwork(ServerApi.getInstance().SHEET_GETSHEETLIST, code);

        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (null != se4500 && keyCode == SE4500_DEMO.KEYCODE_TT35
                || keyCode == SE4500_DEMO.KEYCODE_KT45) {
            se4500.startScan();
        }
        if (null != aIso6c && keyCode == As3992demoISO6C.KEYCODE_TT35
                || keyCode == As3992demoISO6C.KEYCODE_KT45) {
            // 防止在初始化之前按F1键
            aIso6c.startScan();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onScanSuccess(String code) {
        // TODO 条形码回调方法
        // 根据条形码获取钢板列表
        myNetwork(ServerApi.getInstance().SHEET_GETSHEETLIST, code);
    }

    @Override
    public void onUHFSuccess(String code) {
        // TODO 超高频回调方法
        code = code.toUpperCase();// 字母全部转大写
        myNetwork(ServerApi.getInstance().SHEET_GETSHEETLIST, code);
    }

    @Override
    public void remove(int which) {
        // TODO Auto-generated method stub
        if (liveAdapter.getItem(which).equals(virSheet)) {
            liveAdapter.notifyDataSetChanged();
        } else {
            int itemNo = liveAdapter.getItem(which).getItemNo();
            mLog.w("itemNum: " + itemNo);
            itemNo = 1;

            liveAdapter.removeItem(liveAdapter.getItem(which));

            // 确保库存和实况数量一致
            if (serverAdapter.getItem(which).equals(virSheet)) {
                serverAdapter.removeItem(serverAdapter.getItem(which));
            } else {
                // 在角标0处添加虚拟钢板
                liveAdapter.insertItem(0, virSheet);
            }
            mLog.w("liveAdapter.getCount()==" + liveAdapter.getCount()
                    + ",serverAdapter.getCount()==" + serverAdapter.getCount());

            // 重新比对结果和重新设置层号
            for (int i = serverAdapter.getCount() - 1; i >= 0; i--) {
                if (!liveAdapter.getItem(i).equals(virSheet)) {
                    liveAdapter.getItem(i).setItemNo(itemNo++);

                    mLog.w("liveAdapter.getItem(i)=="
                            + liveAdapter.getItem(i).getItemNo());
                }
                if (serverAdapter.getItem(i).equals(virSheet)
                        || liveAdapter.getItem(i).equals(virSheet)) {
                    continue;
                }
                curLiveSheet = liveAdapter.getItem(i);
                comparisonSheet(serverAdapter.getItem(i));
            }
            liveAdapter.notifyDataSetChanged();
            serverAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void drop(int from, int to) {
        // TODO Auto-generated method stub
        mLog.w("from: " + from + ",to: " + to);
        // 排除单击拖动按钮和目的地是虚拟钢板的拖动
        if (from != to && !liveAdapter.getItem(to).equals(virSheet)) {
            int itemNo = 0;
            if (to > from) {
                itemNo = liveAdapter.getItem(to).getItemNo();
            } else if (to < from) {
                itemNo = liveAdapter.getItem(from).getItemNo();
            }
            mLog.w("itemNum : " + itemNo);

            Sheet.ListClass sheet = liveAdapter.getItem(from);
            liveAdapter.removeItem(sheet);
            liveAdapter.insertItem(to, sheet);

            // 重新比对结果和重新设置层号
            for (int i = serverAdapter.getCount() - 1; i >= 0; i--) {
                if (!liveAdapter.getItem(i).equals(virSheet)) {
                    if (to > from && i >= from && i <= to) {
                        liveAdapter.getItem(i).setItemNo(itemNo++);
                        mLog.w("itemNum1: " + itemNo);
                    } else if (to < from && i >= to && i <= from) {
                        liveAdapter.getItem(i).setItemNo(itemNo++);
                        mLog.w("itemNum2: " + itemNo);
                    }
                }
                if (serverAdapter.getItem(i).equals(virSheet)
                        || liveAdapter.getItem(i).equals(virSheet)) {
                    continue;
                }
                curLiveSheet = liveAdapter.getItem(i);
                comparisonSheet(serverAdapter.getItem(i));
            }
            liveAdapter.notifyDataSetChanged();
        }
    }

    /**
     * judgeBarcode(判断条码是否存在)
     *
     * @param barcode
     * @return boolean
     * @throws
     * @since 1.0.0
     */
    private boolean judgeBarcode(String barcode) {
        if (liveAdapter != null) {
            for (int i = 0; i < liveAdapter.getCount(); i++) {
                if (!liveAdapter.getItem(i).equals(virSheet)
                        && liveAdapter.getItem(i).getBarcode().equals(barcode)) {
                    return true;
                }
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
        if (ServerApi.getInstance().API_GET_STACKSETMSG.equals(tag)) {
            // 获取垛位声明信息
            try {
                json = getRequstJson();
                // json.put("pStoreId", txt[0]);// 库房ID
                json.put("pStackId", formStack.getSTACKNO());// 库位ID
                sendPOST(tag, json, null, this, tag, false);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (ServerApi.getInstance().API_GET_WORKLIST_ANDINSTACK
                .equals(tag)) {
            // 库位下的所有钢板信息(库存钢板)
            try {
                json = getRequstJson();
                json.put("pStackNo", formStack.getSTACKNO());// 货位编号
                // json.put("pCurPage", 1);// 从符合条件的第from开始的结果
                // json.put("pPageQty", 9999);// 每页返回的记录数
                json.put("pSortField", "ITEMNO desc");// 按层号降序
                sendPOST(tag, json, null, this, tag, false);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (ServerApi.getInstance().API_GETGATHERLIST.equals(tag)) {
            // 获取已经盘点信息
            try {
                json = getRequstJson();
                json.put("pStackNo", formStack.getSTACKNO());// 货位编号
                sendPOST(tag, json, null, this, tag, false);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (ServerApi.getInstance().SHEET_GETSHEETLIST.equals(tag)) {
            // 根据条形码获取钢板列表
            if (SmsUtil.checkString(txt[0])) {
                if (judgeBarcode(txt[0])) {
                    // txt[0]==barCode
                    showShortToast("条形码已扫描！");
                    dismissProgressDialog();
                } else {
                    try {
                        json = getRequstJson();
                        json.put("Barcode", txt[0].replaceAll(" ", ""));
                        // json.put("StackNo", value)//货位号
                        // json.put("status", value)//状态
                        // json.put("pCOLUMN", value)//排序字段
                        json.put("deleteFlag", "0");
                        json.put("from", "1");
                        json.put("perPage", 99999);
                        sendPOST(tag, json, null, this, tag, false);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        } else if (ServerApi.getInstance().API_CLEANGATHER.equals(tag)) {
            // 清除垛位的盘点信息
            try {
                json = getRequstJson();
                json.put("pStackNo", formStack.getSTACKNO());
                sendPOST(tag, json, null, this, tag, false);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (ServerApi.getInstance().API_ADDGATHER.equals(tag)) {
            // 确认按钮-插入盘点信息
            json = new JSONObject();
            JSONArray jsonArray = new JSONArray();
            try {
                if (null == liveAdapter) {
                    JSONObject jObj = new JSONObject();
                    jObj.put("pGatherId", curHouse.getTYPE());
                    jObj.put("pStackNo", formStack.getSTACKNO());
                    jObj.put("pCheck", checkResult);
                    jObj.put(ServerApi.PARA_FACTORYCODE, ServerApi.factoryCode);
                    json.put(ServerApi.COOKIE_MILLCODE, ServerApi.millCode);
                    jsonArray.put(jObj);
                } else {
                    if (liveAdapter.getCount() == 0) {
                        JSONObject jObj = new JSONObject();
                        jObj.put("pGatherId", curHouse.getTYPE());
                        jObj.put("pStackNo", formStack.getSTACKNO());
                        jObj.put("pCheck", checkResult);
                        jObj.put(ServerApi.PARA_FACTORYCODE,
                                ServerApi.factoryCode);
                        json.put(ServerApi.COOKIE_MILLCODE, ServerApi.millCode);
                        jsonArray.put(jObj);
                    } else {
                        for (int i = 0; i < liveAdapter.getCount(); i++) {
                            mLog.e("liveAdapter.getCount()-----"
                                    + liveAdapter.getCount());
                            JSONObject jObj = new JSONObject();
                            jObj.put("pGatherId", curHouse.getTYPE());
                            jObj.put("pStackNo", formStack.getSTACKNO());
                            jObj.put("pMetalbillId", liveAdapter.getItem(i)
                                    .getMetalBillId());
                            jObj.put("pRemark", liveAdapter.getItem(i)
                                    .getRemark());
                            jObj.put("pBarcode", liveAdapter.getItem(i)
                                    .getBarcode());
                            jObj.put("pCheck", checkResult);// 垛位盘点结果
                            jObj.put("pSheetStatus", liveAdapter.getItem(i)
                                    .getSheetStatus());// 钢板盘点结果
                            jObj.put("pItemNo", liveAdapter.getItem(i)
                                    .getItemNo());// 钢板实况层号
                            jObj.put(ServerApi.PARA_FACTORYCODE,
                                    ServerApi.factoryCode);
                            json.put(ServerApi.COOKIE_MILLCODE,
                                    ServerApi.millCode);
                            jsonArray.put(jObj);
                        }
                    }
                }
                json.put("SheetList", jsonArray);
                json.put(ServerApi.COOKIE_PASSPORT, ServerApi.passport);
                sendPOST(tag, json, null, this, tag, false);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (ServerApi.getInstance().API_GETSTACKINFONAME.equals(tag)) {
            // 通过推荐钢板的接口获取符合的垛位，判断当前垛位是否符合
            json = getRequstJson();
            try {
                json.put("PhouseID", curHouse.getID());
                json.put("pSheetId", txt[0].toString());
                // json.put("pOrderKey", orderKey);// 因为返回两个结果list,无需排序字段
                sendPOST(tag, json, null, this, tag, false);
            } catch (JSONException e) {
                e.printStackTrace();
            }
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
        super.onRequestSuccess(str, obj);
        if (mProgressNumber > 0) {
            mProgressNumber--;
        }
        if (mProgressNumber == 0) {
            dismissProgressDialog();
        }

        Gson gson = new Gson();
        mLog.i(LOG_TAG + obj.toString());
        if (ServerApi.getInstance().API_GET_WORKLIST_ANDINSTACK.equals(str)) {
            // 库位下的所有钢板信息(库存钢板)
            Sheet sheetEn = gson.fromJson(obj.toString(), Sheet.class);
            if (sheetEn.getResultCode() == 0) {
                serverAdapter = new CheckDetailAdapter(this, sheetEn.getList());
                serverListView.setAdapter(serverAdapter);
                if (formStack.getCheckFlag() == 0) {
                    List<Sheet.ListClass> liveList = new ArrayList<Sheet.ListClass>();
                    // 未盘点的垛位
                    for (int i = 0; i < serverAdapter.getCount(); i++) {
                        // 为了让界面中显示的虚拟钢板为空的

                        virSheet.setMaterial("");
                        virSheet.setLength("");
                        virSheet.setWidth("");
                        virSheet.setThickness("");
                        virSheet.setProjectId("");
                        liveList.add(virSheet);
                    }
                    liveAdapter = new CheckDetailAdapter(this, liveList);
                    liveListView.setAdapter(liveAdapter);
                    // 注册拖动监听
                    // liveListView.setOnDragListener(this,
                    // liveAdapter.getList());
                } else {
                    // 盘点过垛位
                    myNetwork(ServerApi.getInstance().API_GETGATHERLIST);
                }
                myNetwork(ServerApi.getInstance().API_GET_STACKSETMSG);
                setListViewOnTouchAndScrollListener(serverListView,
                        liveListView);
            } else {
                showShortToast("resultCode==" + sheetEn.getResultCode()
                        + "，请联系开发人员");
            }
        } else if (ServerApi.getInstance().API_GETGATHERLIST.equals(str)) {
            // 获取已经盘点信息
            Sheet sheetEn = gson.fromJson(obj.toString(), Sheet.class);
            if (sheetEn.getResultCode() == 0) {
                liveAdapter = new CheckDetailAdapter(this, sheetEn.getList());
                liveListView.setAdapter(liveAdapter);
                // 添加相等项
                int ratio = serverAdapter.getCount() - liveAdapter.getCount();
                if (ratio > 0) {
                    for (int i = 0; i < ratio; i++) {
                        // 添加在前头
                        liveAdapter.insertItem(0, virSheet);
                    }
                    liveAdapter.notifyDataSetChanged();
                } else if (ratio < 0) {
                    for (int i = 0; i < -ratio; i++) {
                        serverAdapter.insertItem(0, virSheet);
                    }
                    serverAdapter.notifyDataSetChanged();
                }
                // 注册拖动监听
                // liveListView.setOnDragListener(this, liveAdapter.getList());
            } else {
                showShortToast("resultCode==" + sheetEn.getResultCode()
                        + "，请联系开发人员");
            }
        } else if (ServerApi.getInstance().SHEET_GETSHEETLIST.equals(str)) {
            // 根据条形码获取钢板列表
            Sheet sOut = gson.fromJson(obj.toString(), Sheet.class);
            if (sOut.getResultCode() == 0) {
                if (sOut.getList().size() > 0) {
                    Sheet.ListClass sheet = sOut.getList().get(0);
                    /**
                     * 界面显示按钢板层号降序<br>
                     * 扫描从下往上(按层号升序)
                     */
                    // 获取virSheet位置最末的角标
                    int index = liveAdapter.getList().lastIndexOf(virSheet);
                    if (index != -1) {
                        // 设置实况层号
                        sheet.setItemNo(serverAdapter.getCount() - index);
                        curLiveSheet = sheet;
                        comparisonSheet(serverAdapter.getItem(index));
                        // 替换指定位置的元素
                        liveAdapter.getList().set(index, sheet);
                    } else {// 库存钢板小于实际钢板
                        if (liveAdapter.getCount() == 0) {
                            // 空垛位首张钢板，设置序号
                            sheet.setItemNo(1);
                        } else {
                            // 设置层号
                            sheet.setItemNo(liveAdapter.getItem(0).getItemNo() + 1);
                        }
                        // 钢板状态为有误
                        sheet.setSheetStatus(1);
                        liveAdapter.getList().add(0, sheet);
                        // 同时库存列表添加数据，以便滑动
                        serverAdapter.insertItem(0, virSheet);
                        serverAdapter.notifyDataSetChanged();
                    }
                    liveAdapter.notifyDataSetChanged();
                } else {
                    // toastShort("未找到钢板");
                    Sheet.ListClass sheet = new Sheet().new ListClass();
                    // 获取virSheet位置最末的角标
                    int index = liveAdapter.getList().lastIndexOf(virSheet);
                    mLog.e("index.." + index);
                    if (index != -1) {
                        // 设置实况层号
                        sheet.setItemNo(serverAdapter.getCount() - index);
                        sheet.setBarcode(code);
                        sheet.setSheetStatus(1);
                        sheet.setMetalBillId("");
                        sheet.setRemark("");
                        // 替换指定位置的元素
                        liveAdapter.getList().set(index, sheet);
                    } else {// 库存钢板小于实际钢板
                        if (liveAdapter.getCount() == 0) {
                            // 空垛位首张钢板，设置序号
                            sheet.setItemNo(1);
                        } else {
                            // 设置层号
                            sheet.setItemNo(liveAdapter.getItem(0).getItemNo() + 1);
                        }
                        // 钢板状态为有误
                        sheet.setSheetStatus(1);
                        sheet.setBarcode(code);
                        sheet.setMetalBillId("");
                        sheet.setRemark("");
                        liveAdapter.getList().add(0, sheet);
                        // 同时库存列表添加数据，以便滑动
                        serverAdapter.insertItem(0, virSheet);
                        serverAdapter.notifyDataSetChanged();
                    }
                    liveAdapter.notifyDataSetChanged();
                }
            } else {
                showShortToast("resultCode==" + sOut.getResultCode()
                        + "，请联系开发人员");
            }
        } else if (ServerApi.getInstance().API_CLEANGATHER.equals(str)) {
            // 清除垛位的盘点信息
            BaseModel baseModel = gson
                    .fromJson(obj.toString(), BaseModel.class);
            if (baseModel.getResultCode() == 0) {
                // liveAdapter.getList().clear();
                // for (int i = 0; i < serverAdapter.getList().size(); i++) {
                // liveAdapter.getList().add(virSheet);
                // }
                // liveAdapter.notifyDataSetChanged();
                formStack.setCheckFlag(0);
                // 更改盘点结果需要清除后再提交
                myNetwork(ServerApi.getInstance().API_ADDGATHER);
            } else {
                showShortToast("resultCode==" + baseModel.getResultCode()
                        + "，请联系开发人员");
            }
        } else if (ServerApi.getInstance().API_ADDGATHER.equals(str)) {
            // 确认按钮-插入盘点信息
            BaseModel baseModel = gson
                    .fromJson(obj.toString(), BaseModel.class);
            if (baseModel.getResultCode() == 0) {
                CheckDetailActivity.this.setResult(1011);
                finish();
            } else {
                showShortToast("resultCode==" + baseModel.getResultCode()
                        + "，请联系开发人员");
            }
        } else if (ServerApi.getInstance().API_GET_STACKSETMSG.equals(str)) {
            // 获取垛位说明信息
            mLog.i(LOG_TAG + obj.toString());
            StackExplain stackExplain = gson.fromJson(obj.toString(),
                    StackExplain.class);
            if (stackExplain.getResultCode() == 0) {
                stackMsgList = StackExplain.stackMsg(stackExplain.getListSet(),
                        stackExplain.getStackInfo());
                StringBuffer sb = new StringBuffer();
                for (int i = 0; i < stackMsgList.size(); i++) {
                    sb.append("【");
                    sb.append(stackMsgList.get(i).getDescription());
                    sb.append(":");
                    sb.append(stackMsgList.get(i).getValue());
                    sb.append("】");
                }
                stackTxt.setText(sb.toString());
            }
        } else if (ServerApi.getInstance().API_GETSTACKINFONAME.equals(str)) {
            // 通过推荐钢板的接口获取符合的垛位，判断当前垛位是否符合
            mLog.i(LOG_TAG + obj.toString());
            Stack stack = gson.fromJson(obj.toString(), Stack.class);
            if (stack.getResultCode() == 0) {
                stacknoList = new ArrayList<String>();
                for (int i = 0; i < stack.getList().size(); i++) {
                    stacknoList.add(stack.getList().get(i).getSTACKNO());
                }
                if (stacknoList != null
                        && stacknoList.contains(formStack.getSTACKNO())) {
                    curLiveSheet.setSheetStatus(0);
                } else {
                    curLiveSheet.setSheetStatus(2);
                }
                liveAdapter.notifyDataSetChanged();
            }
        }

    }

    /**
     * 比对库存和实际钢板属性
     *
     * @param serSheet
     * @param
     * @return 0:正常(排除1、2、3), 1：有误(钢板不存在), 2：倒垛(层号不符),3:倒垛(钢板规格和垛位定义规则不一致)
     */
    private void comparisonSheet(Sheet.ListClass serSheet) {
        // 核查扫描钢板编号是否在库存中存在
        boolean isPresence = false;
        for (int i = 0; i < serverAdapter.getCount(); i++) {
            if (curLiveSheet.getMetalBillId().equals(
                    serverAdapter.getList().get(i).getMetalBillId())) {
                isPresence = true;
                break;
            }
        }
        if (isPresence) {
            if (serSheet.getMetalBillId().equals(curLiveSheet.getMetalBillId())) {
                myNetwork(ServerApi.getInstance().API_GETSTACKINFONAME,
                        curLiveSheet.getMetalBillId());
            } else {
                curLiveSheet.setSheetStatus(2);
            }
        } else {
            curLiveSheet.setSheetStatus(1);
        }
    }

    /**
     * @param listView1
     * @param listView2
     */
    private void setListViewOnTouchAndScrollListener(final ListView listView1,
                                                     final ListView listView2) {

        // 设置listview2列表的scroll监听，用于滑动过程中左右不同步时校正
        listView2.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                // 如果停止滑动
                if (scrollState == 0 || scrollState == 1) {
                    // 获得第一个子view
                    View subView = view.getChildAt(0);

                    if (subView != null) {
                        final int top = subView.getTop();
                        final int top1 = listView1.getChildAt(0).getTop();
                        final int position = view.getFirstVisiblePosition();

                        // 如果两个首个显示的子view高度不等
                        if (top != top1) {
                            listView1.setSelectionFromTop(position, top);
                        }
                    }
                }
            }

            public void onScroll(AbsListView view, final int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
                View subView = view.getChildAt(0);
                if (subView != null) {
                    final int top = subView.getTop();

                    // 如果两个首个显示的子view高度不等
                    int top1 = listView1.getChildAt(0).getTop();
                    if (!(top1 - 7 < top && top < top1 + 7)) {
                        listView1.setSelectionFromTop(firstVisibleItem, top);
                        listView2.setSelectionFromTop(firstVisibleItem, top);
                    }
                }
            }
        });

        // 设置listview1列表的scroll监听，用于滑动过程中左右不同步时校正
        listView1.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == 0 || scrollState == 1) {
                    // 获得第一个子view
                    View subView = view.getChildAt(0);

                    if (subView != null) {
                        final int top = subView.getTop();
                        final int top1 = listView2.getChildAt(0).getTop();
                        final int position = view.getFirstVisiblePosition();

                        // 如果两个首个显示的子view高度不等
                        if (top != top1) {
                            listView1.setSelectionFromTop(position, top);
                            listView2.setSelectionFromTop(position, top);
                        }
                    }
                }
            }

            @Override
            public void onScroll(AbsListView view, final int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
                View subView = view.getChildAt(0);
                if (subView != null) {
                    final int top = subView.getTop();
                    listView1.setSelectionFromTop(firstVisibleItem, top);
                    listView2.setSelectionFromTop(firstVisibleItem, top);
                }
            }
        });
        // 滑动到末尾
        serverListView.setSelection(serverAdapter.getCount() - 1);
    }

    /**
     * 初始化扫描头相关
     */
    private void initurovo() {
        try {
            ScanManager scanManager = new ScanManager();
            receiver = new scanReceiver(); // 动态注册BroadcastReceiver
            IntentFilter filter = new IntentFilter();
            // private String ACTION = "urovo.rcv.message";
            filter.addAction("urovo.rcv.message");
            registerReceiver(receiver, filter);
            soundPool = new SoundPool(1, AudioManager.STREAM_NOTIFICATION, 100);
            soundid = soundPool.load("/etc/Scan_new.ogg", 1);
            boolean powerState = scanManager.getScannerState();
            if (!powerState) {
                scanManager.openScanner();
            }
            scanManager.resetScannerParameters();
            scanManager.switchOutputMode(0);
        } catch (ExceptionInInitializerError e) {
            e.printStackTrace();
        } catch (NoClassDefFoundError e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class scanReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            soundPool.play(soundid, 1, 1, 0, 0, 1);
            if (action.equals("urovo.rcv.message")) {
                byte[] barocode = intent.getByteArrayExtra("barocode");
                int barCodeLen = intent.getIntExtra("length", 0);
                byte temp = intent.getByteExtra("barcodeType", (byte) 0);
                code = new String(barocode, 0, barCodeLen);
                // 根据条形码查找钢板
                myNetwork(ServerApi.getInstance().SHEET_GETSHEETLIST, code);
            }
        }
    }

    private void initScan() {
        if (Build.MODEL.equals(SmsConfig.TAG_UROVO)) {
            // mLog.d("---->UROVO");
            initurovo();
        } else if (Build.MODEL.equals(SmsConfig.TAG_KT45)
                || Build.DEVICE.equals(SmsConfig.TAG_TT35)) {
            // mLog.d("---->KT45 or TT35");
            // 条形码
            se4500 = SE4500_DEMO.getInstance(this);
            // 超高频
            aIso6c = As3992demoISO6C.getInstance(this);
        } else {// 其他设备
            findViewById(R.id.checkdetail_btn_).setVisibility(View.VISIBLE);
            mLog.d("run otherDevice");
        }
    }

    /**
     * 设置honeywell扫描
     */
    private Dialog HwUhfDialog;
    private Spinner SpinnerQ;
    private RadioGroup RgInventory;
    private RadioButton RbInventorySingle;
    private RadioButton RbInventoryLoop;
    private RadioButton RbInventoryAnti;
    private Button BtInventory;


    /**
     * 弹出编辑框
     */
    private void editDialog() {
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_edit,
                null);
        final EditText editText = (EditText) view
                .findViewById(R.id.edit_dialog);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("请输入条形码");
        builder.setView(view);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                code = editText.getText().toString().trim();
                if (SmsUtil.checkString(code)) {
                    myNetwork(ServerApi.getInstance().SHEET_GETSHEETLIST, code);
                }
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.create().show();
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
        if (Build.MODEL.equals(SmsConfig.TAG_KT45)
                || Build.DEVICE.equals(SmsConfig.TAG_TT35)) {
            if (null != se4500) {
                se4500.onDestroy();
            }
            if (null != aIso6c) {
                aIso6c.onDestroy();
            }
        }
        if (Build.MODEL.equals(SmsConfig.TAG_UROVO)) {
            // 关闭广播
            if (receiver != null) {
                unregisterReceiver(receiver);
            }
        }
    }


}

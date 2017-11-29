package com.xuesi.sms.app.fragment;

import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.google.gson.Gson;
import com.xuesi.mos.device.Screen;
import com.xuesi.sms.R;
import com.xuesi.sms.ServerApi;
import com.xuesi.sms.bean.StoreHouse;
import com.xuesi.sms.widget.adapter.StoreAdapter;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by XS-PC014 on 2017/11/8.
 */

public class TopFragment extends SmsFragment {
    private TextView titleTv;
    //是否显示选择库房，默认显示
    private boolean storeShow = true;
    private String curStoreType;
    private StoreAdapter storeAdapter;
    /**
     * 选择库房下拉框
     */
    private PopupWindow storePopWin;
    private LinearLayout storeLinear;
    // 下拉选择库房列表
    protected ListView storeListView;
    protected TextView HouseLeftTxt, stockNoTxt;
    private ImageButton arrowBtn;
    /**
     * 当前选中的库房
     */
    protected StoreHouse.ListClass curSelctStore;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_top, container, false);
        titleTv = (TextView) view.findViewById(R.id.top_title);
        storeLinear = (LinearLayout) view.findViewById(R.id.housestore_linear);
        HouseLeftTxt = (TextView) view.findViewById(R.id.housestore_tv_left);
        stockNoTxt = (TextView) view.findViewById(R.id.housestore_tv_name);
        arrowBtn = (ImageButton) view.findViewById(R.id.housestore_btn_arrow);
        return view;
    }

    /**
     * 传入需要的参数，设置给arguments
     *
     * @param bundle
     * @return
     */
    public static TopFragment newInstance(Bundle bundle) {
        TopFragment topFragment = new TopFragment();
        topFragment.setArguments(bundle);
        return topFragment;
    }

    public void setContent(boolean storeShow, String title) {
        titleTv.setText(title);
        if (storeShow) {
            storeLinear.setVisibility(View.VISIBLE);
            arrowBtn.setOnClickListener(this);
            myNetwork(ServerApi.getInstance().SHEET_GETHOUSEID, "");
        } else {
            storeLinear.setVisibility(View.GONE);
        }
    }

    public void setCurStoreType(String curStoreType) {
        this.curStoreType = curStoreType;
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.housestore_btn_arrow: {// 选择仓库
                myNetwork(ServerApi.getInstance().SHEET_GETHOUSEID, "");
            }
            break;
            default:
                break;
        }
    }

    @Override
    public void myNetwork(String url, String... txt) {
        super.myNetwork(url, txt);
        JSONObject json = getRequstJson();
        if (ServerApi.getInstance().SHEET_GETHOUSEID.equals(url)) {
            try {
                json.put("type", curStoreType);
                json.put("perPage", 9999);// 默认值为5
                sendPOST(url, json, null, this, url, false);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onRequestFail(String url, Exception ex) {
        super.onRequestFail(url, ex);
    }

    @Override
    public void onRequestSuccess(String url, Object obj) {
        super.onRequestSuccess(url, obj);
        Gson gson = new Gson();

        if (ServerApi.getInstance().SHEET_GETHOUSEID.equals(url)) {
            // 获取库房
            StoreHouse storeHouse = gson.fromJson(obj.toString(),
                    StoreHouse.class);
            if (storeHouse.getResultCode() == 0) {
                storeAdapter = new StoreAdapter(getActivity(), storeHouse.getList());
                if (storeHouse.getList().size() > 0) {
                    if (null != curSelctStore) {
                        showSelectHousePopup();
                    } else {
                        // 初始化页面
                        curSelctStore = storeAdapter.getItem(0);
                        storeOnItemClick.storeOnItemClick(curSelctStore);
                        stockNoTxt.setText(curSelctStore.getNAME());
                    }
                } else {
                    showShortToast(R.string.msg_createstore);
//                    showForceExitDialog(this, "prompt",
//                            R.string.msg_createstore);
                }
            }
        }
    }

    /**
     * 显示选择仓库弹出框
     */

    public void showSelectHousePopup() {
        if (null != storePopWin && storePopWin.isShowing()) {
            storePopWin.dismiss();
        }
        int width = storeLinear.getWidth();
        // 选择库房弹出部分
        View popView = getActivity().getLayoutInflater().inflate(R.layout.popup_storehouse,
                null);
        storeListView = (ListView) popView
                .findViewById(R.id.popup_lv_houseName);
        storeListView.setOnItemClickListener(this);
        storeListView.setAdapter(storeAdapter);

        storePopWin = new PopupWindow(popView, width,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        storePopWin.setFocusable(true);
        storePopWin.setOutsideTouchable(true);
        storePopWin.setBackgroundDrawable(new BitmapDrawable());
        storePopWin.setAnimationStyle(R.style.AnimTop2);
        if (storeAdapter.getCount() > 5) {
            storePopWin
                    .setHeight(Screen.getInstance().getScreenHeight() * 4 / 10);
        }
        if (storePopWin.isShowing()) {
            storePopWin.dismiss();
        } else {
            int[] position = new int[2];
            HouseLeftTxt.getLocationOnScreen(position);
            int height = storeLinear.getHeight();
            int y = position[1] + height;
            // 在指定位置显示
            storePopWin.showAtLocation(stockNoTxt, Gravity.LEFT | Gravity.TOP,
                    position[0], y);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        super.onItemClick(parent, view, position, id);
        // 库房列表项(单选库房)
        if (parent == storeListView) {
            curSelctStore = (StoreHouse.ListClass) storeAdapter.getItem(position);
            stockNoTxt.setText(curSelctStore.getNAME());
            storeOnItemClick.storeOnItemClick(curSelctStore);
            if (storePopWin.isShowing()) {
                storePopWin.dismiss();
            }
        }
    }

    private StoreOnItemClick storeOnItemClick;

    //点击库房接口
    public interface StoreOnItemClick {
        void storeOnItemClick(StoreHouse.ListClass curSelctStore);
    }

    public void setStoreOnItemClick(StoreOnItemClick storeOnItemClick) {
        this.storeOnItemClick = storeOnItemClick;
    }
}

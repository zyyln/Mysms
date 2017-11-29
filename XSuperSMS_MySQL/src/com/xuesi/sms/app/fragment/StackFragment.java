package com.xuesi.sms.app.fragment;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.xuesi.mos.libs.pulltorefresh.PullToRefreshBase;
import com.xuesi.mos.libs.pulltorefresh.PullToRefreshGridView;
import com.xuesi.mos.util.MosLog;
import com.xuesi.sms.R;
import com.xuesi.sms.ServerApi;
import com.xuesi.sms.bean.Stack;
import com.xuesi.sms.bean.StoreHouse;
import com.xuesi.sms.widget.adapter.StackAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class StackFragment extends SmsFragment implements View.OnClickListener, PullToRefreshBase.OnRefreshListener2, AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {
    /**
     * LOG
     */
    private final String LOG_TAG = StackFragment.class.getSimpleName();
    private final MosLog mLog = MosLog.getInstance(LOG_TAG, "XUESI-SMS");

    /**
     * ServerApi实例对象
     */
    private static StackFragment instance;

    //获取库房
    public static final String CURSELCTSTORE = "curSelctStore";
    public static final String CURSTORETYPE = "curStoreType";
    //入库推荐库房
    private View view;
    /**
     * 当前选中的库房
     */
    protected StoreHouse.ListClass curSelctStore;
    //大库房类型
    private String curStoreType;
    /**
     * 下拉刷新控件
     */
    protected PullToRefreshGridView stackPullRefreshGridView;
    /**
     * 垛位列表项
     */
    protected GridView stackGridView;
    /**
     * 垛位数据解析体
     */
    private Stack stack;
    /**
     * 所有垛位信息
     */
    protected List<Stack.ListClass> stackList;
    /**
     * 垛位适配器
     */
    protected StackAdapter stackAdapter;

    /**
     * 单例模式中获取唯一的ServerApi实例
     */
    public static StackFragment getInstance() {
        if (null == instance) {
            instance = new StackFragment();
        }
        return instance;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            curStoreType = bundle.getString(CURSTORETYPE);
            curSelctStore = (StoreHouse.ListClass) bundle.getSerializable(CURSELCTSTORE);
        }
        myNetwork(ServerApi.getInstance().API_GET_STACKINFO);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        view = inflater.inflate(R.layout.include_gv_stack, container, false);
        super.onCreateView(inflater, container, savedInstanceState);
        return view;
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
    }

    @Override
    protected void initContentView() {
        // TODO Auto-generated method stub
        // 垛位列表项
        stackPullRefreshGridView = (PullToRefreshGridView) view.findViewById(R.id.include_gv_stack);
        stackPullRefreshGridView.setOnRefreshListener(this);
        // 设置模式为只能下拉刷新
        stackPullRefreshGridView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        stackGridView = stackPullRefreshGridView.getRefreshableView();
        stackGridView.setOnItemClickListener(this);
        stackGridView.setOnItemLongClickListener(this);
    }

    @Override
    public void myNetwork(String url, String... txt) {
        super.myNetwork(url, txt);
        JSONObject json = getRequstJson();
        if (ServerApi.getInstance().API_GET_STACKINFO.equals(url)) {
            try {
                json.put("PhouseID", curSelctStore.getID());
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
        if (ServerApi.getInstance().API_GET_STACKINFO.equals(url)) {
            //获取垛位
            stack = gson.fromJson(obj.toString(), Stack.class);
            stackPullRefreshGridView.onRefreshComplete();
            if (stack.getResultCode() == 0) {
                stackList = new ArrayList<Stack.ListClass>();
                stackList = stack.getList();
                stackAdapter = new StackAdapter(getActivity(), stackList, curStoreType, 1);
                stackGridView.setAdapter(stackAdapter);
                if (stack.getList().size() == 0) {
                    TextView tv = new TextView(getActivity());
                    tv.setGravity(Gravity.CENTER);
                    tv.setText("没有垛位数据，请先查看库房");
                    stackPullRefreshGridView.setEmptyView(tv);
                } else {
                    stackPullRefreshGridView.removeEmptyView();
                }
            }
        }
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase refreshView) {

    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase refreshView) {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (girdViewClickListener != null) {
            girdViewClickListener.gvOnItemClick(parent, view, position, id);
        }
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        if (girdViewLongClickListener != null) {
            girdViewLongClickListener.gvOnItemLongClick(parent, view, position, id);
        }
        return false;
    }

    /**
     * 传入需要的参数，设置给arguments
     *
     * @param
     * @return
     */
    public static StackFragment newInstance(String curStoreT, StoreHouse.ListClass curStore) {
        Bundle bundle = new Bundle();
        bundle.putString(CURSTORETYPE, curStoreT);
        bundle.putSerializable(CURSELCTSTORE, curStore);
        StackFragment stackFragment = getInstance();
        stackFragment.setArguments(bundle);
        return stackFragment;
    }

    /**
     * 更新垛位信息
     */
    public void updateStackNo(String curStoreT, StoreHouse.ListClass curStore) {
        curStoreType = curStoreT;
        curSelctStore = curStore;
        myNetwork(ServerApi.getInstance().API_GET_STACKINFO);
    }

    //GirdView 点击接口
    private GirdViewClickListener girdViewClickListener;
    //GirdView 长按接口
    private GirdViewLongClickListener girdViewLongClickListener;

    public interface GirdViewClickListener {
        void gvOnItemClick(AdapterView<?> parent, View view, int position, long id);
    }

    public interface GirdViewLongClickListener {
        void gvOnItemLongClick(AdapterView<?> parent, View view, int position, long id);
    }

    public void setGirdViewClickListener(GirdViewClickListener girdViewClickListener) {
        this.girdViewClickListener = girdViewClickListener;
    }

    public void setGirdViewLongClickListener(GirdViewLongClickListener girdViewLongClickListener) {
        this.girdViewLongClickListener = girdViewLongClickListener;
    }
}

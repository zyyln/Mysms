package com.xuesi.sms.app.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.xuesi.mos.libs.pulltorefresh.PullToRefreshBase;
import com.xuesi.mos.util.MosLog;
import com.xuesi.sms.R;
import com.xuesi.sms.ServerApi;
import com.xuesi.sms.SmsApplication;
import com.xuesi.sms.bean.Stack;
import com.xuesi.sms.bean.StackExplain;
import com.xuesi.sms.bean.StackExplain.StackMsg;
import com.xuesi.sms.widget.adapter.StackAdapter;

/**
 * 库存总览
 * 
 * @author XS-PC014
 * 
 */
public class StoreActivity extends SheetBaseActivity implements
		AdapterView.OnItemLongClickListener {
	/** LOG */
	private final String LOG_TAG = StoreActivity.class.getSimpleName();
	private final MosLog mLog = MosLog.getInstance(LOG_TAG, "XUESI-SMS");
	/** progress的次数 */
	private int mProgressNumber = 0;

	// private TextView materialTv, thicknessTwv, lengthTv, widthTv,
	// maxHeightTv,
	// earlistStorageTimeTv, remarkTv;
	// 库房信息
	/** 垛位信息 */
	private EditText barcodeET, stockThicknessEt, stockMaterialEt,
			stockLengthEt, stockWidthEt;
	private EditText TimeEt, projectEt, assemblyNoEt, suitcutEt;

	/** 垛位总数\钢板总数\钢板数(和情的钢板数量) */
	private TextView stackCountTv, sheetCountTv, sheetNumTv;
	private int curPosition;
	private ListView lvStackMsg;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		SmsApplication.getInstance().addActivity(this);// 添加到Activity堆栈
		setContentView(R.layout.activity_total);

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
		setTopTitle(R.string.stock_pandect);
		setRefreshView(View.INVISIBLE);

		barcodeET = (EditText) findViewById(R.id.bar_code_tv);

		// 查询条件输入框
		suitcutEt = (EditText) findViewById(R.id.et_suit_cut);
		stockThicknessEt = (EditText) findViewById(R.id.stock_thick_tv);
		stockMaterialEt = (EditText) findViewById(R.id.stock_material_tv);
		stockLengthEt = (EditText) findViewById(R.id.stock_length_tv);
		stockWidthEt = (EditText) findViewById(R.id.stock_width_tv);
		TimeEt = (EditText) findViewById(R.id.time_tv);
		projectEt = (EditText) findViewById(R.id.project_tv);
		assemblyNoEt = (EditText) findViewById(R.id.assemblyNo_tv);

		sheetNumTv = (TextView) findViewById(R.id.total_tv_sheetnum);
		stackCountTv = (TextView) findViewById(R.id.total_tv_stackcount);
		sheetCountTv = (TextView) findViewById(R.id.total_tv_sheetcount);
		// 查询按钮
		findViewById(R.id.query_btn).setOnClickListener(this);
	}

	private void loadExtras() {
		Bundle bundle = getIntent().getExtras();
		curStoreType = bundle.getString("curStoreType");
		// 初始画面为默认显示一级库下第一个库房下的垛位
		myNetwork(ServerApi.getInstance().SHEET_GETHOUSEID);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		super.onClick(v);
		switch (v.getId()) {
		// case R.id.bartop_img_back:
		// finish();
		// break;
		case R.id.bartop_img_refresh:
			// if (isQuery) {
			// if (null == curSelctStore.getId() || stackAllList.size() == 0) {
			// return;
			// }
			// queryStackInfo();
			// } else {
			// if ("".equals(curSelctStore.getId()) || null ==
			// curSelctStore.getId()) {
			// toastShort("请选择库房号，再刷新");
			// return;
			// }
			// reqStackData();
			// }
			break;
		case R.id.housestore_btn_arrow:// 选择仓库下拉按钮
			myNetwork(ServerApi.getInstance().SHEET_GETHOUSEID);
			break;
		case R.id.query_btn:// 查询钢板信息
			if (null != curSelctStore.getID()) {
				if (null != stackAdapter && stackAdapter.getCount() > 0) {
					startQuery();
				} else {
					// toastShort("请选择有垛位的库房进行查询！");
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
		// 库房列表项和行车事件在父类
		super.onItemClick(parent, view, position, id);
		// **** 垛位列表项() ****
		if (parent == stackGridView) {
			curPosition = position;
			for (int i = 0; i < stackAdapter.getCount(); i++) {
				stackAdapter.getItem(i).setSelected(
						i == position ? true : false);
			}

			myNetwork(ServerApi.getInstance().API_GET_STACKSETMSG, stackAdapter
					.getItem(curPosition).getSTACKNO());
		}
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view,
			int position, long id) {
		// TODO Auto-generated method stub
		// **** 垛位列表项 ****
		if (parent == stackGridView) {
			Stack.ListClass curSelctStack = stackAdapter.getItem(position);
			if (curSelctStack.getSumAmount() > 0
					|| curSelctStack.getSumAmount() > 0) {
				Bundle bundle = new Bundle();
				bundle.putSerializable("curSelctStore", curSelctStore);
				bundle.putSerializable("curSelctStack", curSelctStack);
				bundle.putString("curStoreType", curStoreType);
				if (curSelctStack.isRecommend()) {
					bundle.putString("barCode", barcodeET.getText().toString()
							.replaceAll(" ", ""));
					bundle.putString("material", stockMaterialEt.getText()
							.toString().trim());
					bundle.putString("thickness", stockThicknessEt.getText()
							.toString().trim());
					bundle.putString("length", stockLengthEt.getText()
							.toString().trim());
					bundle.putString("width", stockWidthEt.getText().toString()
							.trim());
					bundle.putString("time", TimeEt.getText().toString().trim());
					bundle.putString("project", projectEt.getText().toString()
							.trim());
					bundle.putString("assemblyNo", assemblyNoEt.getText()
							.toString().trim());
					bundle.putBoolean("isQuery", true);
				} else {
					bundle.putBoolean("isQuery", false);
				}
				// 打开钢板详情画面
				openActivity(SheetDetailActivity.class, bundle);
			} else {
				showShortToast("当前垛位为空！");
			}
		}
		return false;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		return super.onKeyDown(keyCode, event);
	}

	/**
	 * 下拉方法
	 */
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

	@Override
	protected void onScanResult(String barcode) {
		// TODO Auto-generated method stub
		barcodeET.setText(barcode);
	}

	/**
	 * 查询垛位信息
	 */
	private void startQuery() {
		String barCode = barcodeET.getText().toString().replaceAll(" ", "");
		String suitcutNo = suitcutEt.getText().toString().replaceAll(" ", "");
		String stockThickness = stockThicknessEt.getText().toString().trim();
		String stockMaterial = stockMaterialEt.getText().toString().trim();
		String stockLength = stockLengthEt.getText().toString().trim();
		String stockWidth = stockWidthEt.getText().toString().trim();
		String Time = TimeEt.getText().toString().trim();
		String project = projectEt.getText().toString().trim();
		String assemblyNo = assemblyNoEt.getText().toString().trim();
		if ("".equals(barCode) && "".equals(suitcutNo)
				&& "".equals(stockThickness) && "".equals(stockMaterial)
				&& "".equals(stockLength) && "".equals(stockWidth)
				&& "".equals(Time) && "".equals(project)
				&& "".equals(assemblyNo)) {
			// toastShort("请先输入查询条件，再查询!");
			return;
		}
		if (suitcutNo.length() > 0 && !curStoreType.equals("SCK")) {
			showShortToast("只能在二级库进行套裁图号查询!");
			return;
		}
		Map<String, String> map = new HashMap<String, String>();
		map.put("barCode", barCode);
		myNetwork(ServerApi.getInstance().API_GETSHEETNUM_INSTACK, barCode,
				suitcutNo, stockMaterial, stockThickness, stockLength,
				stockWidth, Time, project, assemblyNo);
	}

	@Override
	public void myNetwork(String tag, String... txt) {
		// TODO Auto-generated method stub
		super.myNetwork(tag, txt);
		mProgressNumber++;
		if (mProgressNumber == 1) {
			showProgressDialog("加载中", false, false);
		}

		JSONObject json = getRequstJson();
		if (ServerApi.getInstance().API_GETSHEETNUM_INSTACK.equals(tag)) {
			// 根据输入条件筛选垛位
			try {
				json.put("houseId", curSelctStore.getID());
				// 因为接口那边问题，所以空值不传json
				if (null != txt[0] && txt[0].length() > 0) {
					json.put("barcode", txt[0]);
				}
				if (null != txt[1] && txt[1].length() > 0) {
					json.put("ncCode", txt[1]);
				}
				if (txt[2].length() > 0) {
					json.put("material", txt[2]);
				}
				if (txt[3].length() > 0) {
					json.put("thickness", txt[3]);
				}
				if (txt[4].length() > 0) {
					json.put("length", txt[4]);
				}
				if (txt[5].length() > 0) {
					json.put("width", txt[5]);
				}
				if (null != txt[6] && txt[6].length() > 0) {
					json.put("InTime", txt[6]);
				}
				if (null != txt[7] && txt[7].length() > 0) {
					json.put("project", txt[7]);
				}
				if (null != txt[8] && txt[8].length() > 0) {
					json.put("assemblyNo", txt[8]);
				}
				json.put("perPage", "9999");// 每页返回的记录数
				sendPOST(tag, json, null, this, tag, false);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		} else {
			// toastShort("错误的网络请求，请检查");
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
		Gson gson = new Gson();
		if (ServerApi.getInstance().API_GET_STACKINFO.equals(tag)) {
			// 获取所有垛位，父类解析
			mLog.i(LOG_TAG + obj.toString());
			if (null != stack && stack.getResultCode() == 0) {
				// 垛位总数、钢板总数
				stackCountTv.setText(stack.getTotalList().get(0).getStacknum()
						+ getString(R.string.loaf));
				sheetCountTv.setText(stack.getTotalList().get(0).getSheetnum()
						+ getString(R.string.steel_unit));
			} else {
				showShortToast("resultCode==" + stack.getResultCode()
						+ "，请联系开发人员");
			}
		} else if (ServerApi.getInstance().API_GETSHEETNUM_INSTACK.equals(tag)) {
			// 获取筛选后的钢板
			mLog.i(LOG_TAG + obj.toString());
			Stack stack = gson.fromJson(obj.toString(), Stack.class);
			if (stack.getResultCode() == 0) {
				List<Stack.ListClass> alllist = new ArrayList<Stack.ListClass>();
				alllist.addAll(stack.getList());
				alllist.addAll(stack.getUnlist());
				stackAdapter = new StackAdapter(this, alllist, curStoreType, 0);
				if (stack.getList().size() == 0) {
					showShortToast("未查询到符合条件的钢板！");
				} else {
					for (Stack.ListClass stacklist : stack.getList()) {
						// 设置符合条件属性
						stacklist.setRecommend(true);
					}
				}
				stackGridView.setAdapter(stackAdapter);
				// 显示合情的钢板数量
				sheetNumTv.setText(stack.getTotalList().get(0).getTotal()
						+ getString(R.string.steel_unit));
			} else {
				showShortToast("resultCode==" + stack.getResultCode()
						+ "，请联系开发人员");
			}
		} else if (ServerApi.getInstance().API_GET_STACKSETMSG.equals(tag)) {
			// 获取垛位说明信息
			mLog.i(LOG_TAG + obj.toString());
			StackExplain stackExplain = gson.fromJson(obj.toString(),
					StackExplain.class);
			if (stackExplain.getResultCode() == 0) {
				stackMsgList = StackExplain.stackMsg(stackExplain.getListSet(),
						stackExplain.getStackInfo());
				// 把垛位信息填充到Dialog
				View dialogview = getLayoutInflater().inflate(
						R.layout.dialog_stackdetails, null);
				// 垛位定义信息显示
				lvStackMsg = (ListView) dialogview
						.findViewById(R.id.lv_stackmsg);
				lvStackMsg.setAdapter(new StackMsgAdapter(this, stackMsgList));
				Dialog stackDetailDialog = getDialog(StoreActivity.this,
						dialogview);
				showDialog(stackDetailDialog);
				stackAdapter.notifyDataSetChanged();
			}
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

	class StackMsgAdapter extends BaseAdapter {
		List<StackExplain.StackMsg> list;
		private LayoutInflater mInflater;

		class ViewHolder {
			TextView stackMsgTv;
			EditText stackMsgEt;
		}

		public StackMsgAdapter(Context context, List<StackExplain.StackMsg> list) {
			this.mInflater = LayoutInflater.from(context);
			this.list = list;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return list != null ? list.size() : 0;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return list.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			convertView = null;
			ViewHolder holder = new ViewHolder();
			if (convertView == null) {
				convertView = mInflater.inflate(R.layout.item_stack_msg, null);
				holder.stackMsgTv = (TextView) convertView
						.findViewById(R.id.tv_msg);
				holder.stackMsgEt = (EditText) convertView
						.findViewById(R.id.et_msg);
				convertView.setTag(holder);

			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.stackMsgTv.setText(list.get(position).getDescription());
			holder.stackMsgEt.setText(list.get(position).getValue());
			return convertView;
		}
	}

}

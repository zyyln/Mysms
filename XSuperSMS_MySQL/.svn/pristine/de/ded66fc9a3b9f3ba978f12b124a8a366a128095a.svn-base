package com.xuesi.sms.app.activity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.xuesi.mos.util.MosLog;
import com.xuesi.sms.R;
import com.xuesi.sms.ServerApi;
import com.xuesi.sms.SmsApplication;
import com.xuesi.sms.SmsConfig;
import com.xuesi.sms.bean.GetServerTime;
import com.xuesi.sms.bean.WbTotalData;
import com.xuesi.sms.util.CalendarUtil;
import com.xuesi.sms.util.SmsUtil;
import com.xuesi.sms.widget.DashBoardView;
import com.xuesi.sms.widget.PercentView;

/**
 * 作业看板
 * 
 * @author XS-PC014
 * 
 */
public class WorkInfoActivity extends WorkInfoBaseActivity {
	/** LOG */
	private final String LOG_TAG = WorkInfoActivity.class.getSimpleName();
	private final MosLog mLog = MosLog.getInstance(LOG_TAG, "XUESI-SMS");
	/** progress的次数 */
	private int mProgressNumber = 0;

	/** 总数，钢板请料数的张和吨，切割废料量的吨，产量中的切割钢板(张)和零件重量 */
	private TextView sum, material, material2, waste, incision, part;

	/** 库存中的钢板(张和吨)、余料、在制品 */
	private TextView sheet, sheet2, oddments, oddments2, processed, processed2;

	/** 利用率，钢板报损率，在制品报损率(都为百分比) */
	private DashBoardView useRatioBoard, injureRatioBoard,
			destoryRatioDashBoard;

	/** 计划进展 */
	private PercentView m_planProgressInfo;

	/** 钢板报损率(张和吨),在制品报损率(张和吨) */
	private TextView sheetBreakage, sheetBreakage2, processedBreakage;

	/** 切割工时(H)，钢板代用(P) */
	private TextView workTime, replace;

	/** 产量、库存、利用率 */
	private LinearLayout yield, reserve, useRatio;

	/** 计划进度 */
	private RelativeLayout planProgress;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		SmsApplication.getInstance().addActivity(this);// 添加到Activity堆栈
		setContentView(R.layout.activity_work_info);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		// pauseThread = true;
		myNetwork(ServerApi.getInstance().API_GETSYSDATE);
	}

	@Override
	protected void initContentView() {
		// TODO Auto-generated method stub
		super.initContentView();
		setTopTitle(R.string.work_panel);

		// 更换返回键为主页图标
		backImg.setImageResource(R.drawable.workinfo_home_select);

		// 计划进展情况
		planProgress = (RelativeLayout) findViewById(R.id.workInfo_relative_planProgress);
		planProgress.setOnClickListener(this);
		m_planProgressInfo = (PercentView) findViewById(R.id.workInfo_plan_percent);
		// 进行中的计划总数
		sum = (TextView) findViewById(R.id.workInfo_tv_summation);

		// 钢板请料数(张)
		material = (TextView) findViewById(R.id.workInfo_tv_material);
		// 钢板请料数(吨)
		material2 = (TextView) findViewById(R.id.workInfo_tv_material2);

		// 切割废料量
		waste = (TextView) findViewById(R.id.workInfo_tv_waste);

		// 产量中的切割钢板(张)和零件重量
		yield = (LinearLayout) findViewById(R.id.workInfo_linear_yield);
		yield.setOnClickListener(this);
		incision = (TextView) findViewById(R.id.workInfo_tv_incision);
		part = (TextView) findViewById(R.id.workInfo_tv_part);

		// 库存中的钢板(张和吨)、余料、在制品
		reserve = (LinearLayout) findViewById(R.id.workInfo_linear_reserve);
		reserve.setOnClickListener(this);
		sheet = (TextView) findViewById(R.id.workInfo_tv_sheet);
		sheet2 = (TextView) findViewById(R.id.workInfo_tv_sheet2);
		oddments = (TextView) findViewById(R.id.workInfo_tv_oddments);
		oddments2 = (TextView) findViewById(R.id.workInfo_tv_oddments2);
		processed = (TextView) findViewById(R.id.workInfo_tv_processed);
		processed2 = (TextView) findViewById(R.id.workInfo_tv_processed2);

		// 利用率
		useRatio = (LinearLayout) findViewById(R.id.workInfo_linear_useRatio);
		useRatio.setOnClickListener(this);
		useRatioBoard = (DashBoardView) findViewById(R.id.workInfo_meter);
		useRatioBoard.setPanelView(R.drawable.worklook_meter);

		// 钢板报损率
		injureRatioBoard = (DashBoardView) findViewById(R.id.wb_injure_dashboard);
		injureRatioBoard.setPanelView(R.drawable.worklook_meter1);
		// injureRatioTxt = (TextView) findViewById(R.id.wb_injure_percent_txt);
		sheetBreakage = (TextView) findViewById(R.id.wb_injure_count_txt);
		sheetBreakage2 = (TextView) findViewById(R.id.wb_injure_weight_txt);

		// 在制品报损率
		destoryRatioDashBoard = (DashBoardView) findViewById(R.id.wb_destory_ratio_dashboard);
		destoryRatioDashBoard.setPanelView(R.drawable.worklook_meter1);
		processedBreakage = (TextView) findViewById(R.id.wb_destory_count);

		// 切割工时(H)，钢板代用(P)
		workTime = (TextView) findViewById(R.id.workInfo_tv_time);
		replace = (TextView) findViewById(R.id.workInfo_tv_replace);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.bartop_img_back: {// 主页按钮
			openActivity(ArrayNaviActivity.class);
		}
			break;
		case R.id.bartop_img_refresh: {// 刷新按钮
			myNetwork(ServerApi.getInstance().API_GETSYSDATE, "");
		}
			break;
		case R.id.workInfo_relative_planProgress: {// 计划进度
			// openActivity(WorkInfoPlanActivity.class);
			showPromptDialog(this, "prompt", R.string.open_new_request,
					WorkInfoPlanActivity.class);
		}
			break;
		case R.id.workInfo_linear_yield: {// 产量布局按钮
			// openActivity(WorkInfoYieldActivity.class);
			showPromptDialog(this, "prompt", R.string.open_new_request,
					WorkInfoYieldActivity.class);
		}
			break;
		case R.id.workInfo_linear_reserve: {// 库存布局按钮
			openActivity(WorkInfoReserveActivity.class);
		}
			break;
		case R.id.workInfo_linear_useRatio: {// 利用率布局按钮
			// openActivity(WorkInfoUseRatioActivity.class);
			showPromptDialog(this, "prompt", R.string.open_new_request,
					WorkInfoUseRatioActivity.class);
		}
			break;
		default:
			break;
		}
		// R.id.bartop_img_back的事件区别
		// 父类是finish,子类是startActivity,有先后执行顺序
		super.onClick(v);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			// 跳转导航
			openActivity(ArrayNaviActivity.class);
		}
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
		mProgressNumber++;
		if (mProgressNumber == 1) {
			showProgressDialog("加载中", false, false);
		}

		JSONObject jo = getRequstJson();
		if (API_WORK.equals(tag)) {
			getDateParam();
			try {
				jo.put("pStartDate", startDate);// 检索开始时间
				jo.put("pEndDate", endDate);// 检索结束时间
				jo.put("pType", "0");// 请料类型 0:切割请料;1:其他请料
				jo.put("pFlag", "1");// 转储类型
				// 获取利用率的分类字段 0:总利用率，1：套料员，2：厚度，3：材质，4：设备类型
				jo.put("pUsedFieldFlg", "0");
				// 分类字段 0:总数，1：天的新增件数，2：周的新增件数，3：月的新增件数，4：年的新增件数
				jo.put("pFieldFlg", "0");

				sendPOST(ServerApi.getInstance().API_WB_DATA_TOTAL, jo, null,
						this, ServerApi.getInstance().API_WB_DATA_TOTAL, false);

			} catch (JSONException e) {
				e.printStackTrace();
			}
		} else if (ServerApi.getInstance().API_GETSYSDATE.equals(tag)) {
			sendPOST(tag, jo, null, this, tag, false);
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
		// 暂停更新
		handler.removeCallbacks(runnable);

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
		mLog.i(obj.toString());
		if (ServerApi.getInstance().API_WB_DATA_TOTAL.equals(str)) {
			handler.postDelayed(runnable, TIME);
			WbTotalData getWbDataTotal = gson.fromJson(obj.toString(),
					WbTotalData.class);
			if (getWbDataTotal.getResultCode() == 0) {
				updateViewData(getWbDataTotal);
			} else {
				showShortToast("resultCode==" + getWbDataTotal.getResultCode()
						+ "，请联系开发人员");
			}
		} else if (ServerApi.getInstance().API_GETSYSDATE.equals(str)) {
			GetServerTime serverTime = gson.fromJson(obj.toString(),
					GetServerTime.class);
			if (serverTime.getResultCode() == 0) {
				ServerApi.date = CalendarUtil.getChineseDate(serverTime
						.getList().get(0).getSysdate());
				dateTv.setText(ServerApi.date);
				myNetwork(API_WORK, "");
			} else {
				showShortToast("resultCode==" + serverTime.getResultCode()
						+ "，请联系开发人员");
			}
		}
	}

	private void updateViewData(WbTotalData getWbDataTotal) {

		// 计划进展情况
		if (getWbDataTotal.getPlanQtyList() == null) {// if (
														// getWbDataTotal.getPlanQtyList().size()
			// <= 0)

			sum.setText("0");
			m_planProgressInfo.setPercent(0);
			m_planProgressInfo.setValueForWhat("0", "0", "0", "0");
		} else {
			int finishCount = 0;
			int unFinishCount = 0;
			int planTotalCount = 0;

			for (WbTotalData.PlanQtyList planQtyItem : getWbDataTotal
					.getPlanQtyList()) {
				if (planQtyItem.getFinishQty().length() > 0
						&& planQtyItem.getPlanQty().length() > 0) {
					finishCount += Integer.valueOf(planQtyItem.getFinishQty());
					planTotalCount += Integer.valueOf(planQtyItem.getPlanQty());
					unFinishCount += Integer.valueOf(planQtyItem
							.getUnFinishQTY());
				}
			}
			double finishPrecent = SmsUtil.getPercentValue(finishCount,
					planTotalCount);
			double unFinishPrecent = SmsUtil.getPercentValue(unFinishCount,
					planTotalCount);
			sum.setText(String.valueOf(planTotalCount));
			m_planProgressInfo.setPercent(finishPrecent);
			m_planProgressInfo.setValueForWhat(String.valueOf(finishCount),
					String.valueOf(unFinishCount),
					SmsUtil.getPercentString(finishPrecent),
					SmsUtil.getPercentString(unFinishPrecent));
		}

		// 钢板请料数
		if (getWbDataTotal.getReqQtyList() == null) {
			material2.setText("0.0" + getString(R.string.ton));
			material.setText(0 + getString(R.string.steel_unit));
		} else {
			if (getWbDataTotal.getReqQtyList().get(0).getSheetWeight().length() > 0) {
				material2.setText(getWbDataTotal.getReqQtyList().get(0)
						.getSheetWeight()
						+ getString(R.string.ton));
				material.setText(getWbDataTotal.getReqQtyList().get(0)
						.getRIDCount()
						+ getString(R.string.steel_unit));
			} else {
				material2.setText(0 + getString(R.string.ton));
				material.setText(0 + getString(R.string.steel_unit));
			}
		}

		// 切割废料量和钢板报损率
		if (getWbDataTotal.getpMbidcws() == null) {
			waste.setText("0.0" + getString(R.string.ton));
			injureRatioBoard.setPercentageTV(SmsUtil.getPercentString(0));
			injureRatioBoard.setDashBoradValue(0);
			sheetBreakage.setText(0 + getString(R.string.steel_unit));
			sheetBreakage2.setText(0 + getString(R.string.ton));
		} else {
			if (getWbDataTotal.getpMbidcws().size() > 0) {
				// 切割废料量
				waste.setText(getWbDataTotal.getpMbidcws().get(0)
						.getPreWeight()
						+ getString(R.string.ton));

				// 钢板报损率
				double percent = SmsUtil.getPercentValue(getWbDataTotal
						.getpMbidcws().get(0).getMBIDWeight(), getWbDataTotal
						.getpTotalWeiLists().get(0).getTotalWeight());
				injureRatioBoard.setDashBoradValue(percent);
				injureRatioBoard.setPercentageTV(SmsUtil
						.getPercentString(percent));

				sheetBreakage.setText(getWbDataTotal.getpMbidcws().get(0)
						.getMBIDCount()
						+ getString(R.string.steel_unit));
				sheetBreakage2.setText(getWbDataTotal.getpMbidcws().get(0)
						.getMBIDWeight()
						+ getString(R.string.ton));
			} else {
				waste.setText("0" + getString(R.string.ton));
				injureRatioBoard.setPercentageTV(SmsUtil.getPercentString(0));
				injureRatioBoard.setDashBoradValue(0);
				sheetBreakage.setText(0 + getString(R.string.steel_unit));
				sheetBreakage2.setText(0 + getString(R.string.ton));
			}
		}

		// 产量
		if (getWbDataTotal.getYieldInfoLists() == null) {
			incision.setText(0 + getString(R.string.steel_unit));
			part.setText(0 + getString(R.string.ton));
		} else {
			if (getWbDataTotal.getYieldInfoLists().size() > 0) {
				incision.setText(getWbDataTotal.getYieldInfoLists().get(0)
						.getAmount()
						+ getString(R.string.steel_unit));
				part.setText(getWbDataTotal.getYieldInfoLists().get(0)
						.getWeight()
						+ getString(R.string.ton));
			} else {
				incision.setText(0 + getString(R.string.steel_unit));
				part.setText(0 + getString(R.string.ton));
			}
		}

		// 库存-钢板
		if (getWbDataTotal.getSheetInfoLists() == null) {
			sheet.setText(0 + getString(R.string.steel_unit));
			sheet2.setText(0 + getString(R.string.ton));
		} else {
			if (getWbDataTotal.getSheetInfoLists().get(0).getAmount() > 0) {
				sheet.setText(getWbDataTotal.getSheetInfoLists().get(0)
						.getAmount()
						+ getString(R.string.steel_unit));
				sheet2.setText(getWbDataTotal.getSheetInfoLists().get(0)
						.getWeight()
						+ getString(R.string.ton));
			} else {
				sheet.setText(0 + getString(R.string.steel_unit));
				sheet2.setText(0 + getString(R.string.ton));
			}
		}
		// 库存-余料
		if (getWbDataTotal.getRemInfoLists() == null) {
			oddments.setText(0 + getString(R.string.steel_unit));
			oddments2.setText(0 + getString(R.string.ton));
		} else {
			if (getWbDataTotal.getRemInfoLists().get(0).getAmount() > 0) {
				oddments.setText(getWbDataTotal.getRemInfoLists().get(0)
						.getAmount()
						+ getString(R.string.steel_unit));
				oddments2.setText(getWbDataTotal.getRemInfoLists().get(0)
						.getWeight()
						+ getString(R.string.ton));
			} else {
				oddments.setText(0 + getString(R.string.steel_unit));
				oddments2.setText(0 + getString(R.string.ton));
			}
		}
		// 库存-在制品
		if (getWbDataTotal.getShopPartInfoLists() == null) {
			processed.setText(0 + getString(R.string.piece));
			processed2.setText(0 + getString(R.string.ton));
		} else {
			if (getWbDataTotal.getShopPartInfoLists().get(0).getAmount() > 0) {
				processed.setText(getWbDataTotal.getShopPartInfoLists().get(0)
						.getAmount()
						+ getString(R.string.piece));
				processed2.setText(getWbDataTotal.getShopPartInfoLists().get(0)
						.getWeight()
						+ getString(R.string.ton));
			} else {
				processed.setText(0 + getString(R.string.piece));
				processed2.setText(0 + getString(R.string.ton));
			}
		}

		// 利用率
		if (getWbDataTotal.getRateLists() == null) {
			useRatioBoard.setDashBoradValue(0);
			useRatioBoard.setPercentageTV("0.0%");
		} else {
			if (getWbDataTotal.getRateLists().size() > 0) {
				useRatioBoard.setDashBoradValue(getWbDataTotal.getRateLists()
						.get(0).getUsedRate() / 100);
				useRatioBoard.setPercentageTV(SmsUtil
						.getPercentString(getWbDataTotal.getRateLists().get(0)
								.getUsedRate() / 100));
			} else {
				useRatioBoard.setDashBoradValue(0);
				useRatioBoard.setPercentageTV("0.0%");
			}
		}

		// 在制品报损率
		// 返回值是百分比的前面值

		if (getWbDataTotal.getpFsRates() == null) {
			destoryRatioDashBoard.setDashBoradValue(0);
			destoryRatioDashBoard.setPercentageTV(0.0 + "%");
			processedBreakage.setText(0 + getString(R.string.piece));
		} else {
			if (getWbDataTotal.getpFsRates().get(0).getFSRate().length() > 0) {
				destoryRatioDashBoard.setDashBoradValue(Double
						.valueOf(getWbDataTotal.getpFsRates().get(0)
								.getFSRate()) * 0.01);
				String fsRate = getWbDataTotal.getpFsRates().get(0).getFSRate();
				destoryRatioDashBoard.setPercentageTV(fsRate + "%");
			} else {
				destoryRatioDashBoard.setDashBoradValue(0);
			}
			if (getWbDataTotal.getpFsRates().get(0).getsFSQty().length() > 0) {
				processedBreakage.setText(getWbDataTotal.getpFsRates().get(0)
						.getsFSQty()
						+ getString(R.string.piece));
			} else {
				processedBreakage.setText(0 + getString(R.string.piece));
			}
		}

		// 切割工时(H)
		if (getWbDataTotal.getpCTimes() == null) {
			workTime.setText("0.00H");
		} else {
			if (getWbDataTotal.getpCTimes().get(0).getCutTime().length() > 0) {
				String pcTime = getWbDataTotal.getpCTimes().get(0).getCutTime();
				workTime.setText(pcTime);
			} else {
				workTime.setText("0.00H");
			}

		}

		// 钢板代用(P)
		if (getWbDataTotal.getpOmbCounts() == null) {
			replace.setText("0");
		} else {
			if (getWbDataTotal.getpOmbCounts().get(0).getOMBCount().length() > 0) {
				replace.setText(getWbDataTotal.getpOmbCounts().get(0)
						.getOMBCount());
			} else {
				replace.setText("0");
			}
		}

		updateDataTypeStatus();
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

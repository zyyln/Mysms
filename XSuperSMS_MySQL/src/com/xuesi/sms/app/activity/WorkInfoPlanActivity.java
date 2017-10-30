package com.xuesi.sms.app.activity;

import java.util.ArrayList;
import java.util.List;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.BarChart.Type;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.SimpleSeriesRenderer;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;
import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.Color;
import android.graphics.Paint.Align;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.google.gson.Gson;
import com.xuesi.mos.util.MosLog;
import com.xuesi.sms.R;
import com.xuesi.sms.ServerApi;
import com.xuesi.sms.SmsApplication;
import com.xuesi.sms.bean.WorkInfoPlan;
import com.xuesi.sms.util.SmsUtil;
import com.xuesi.sms.widget.DashBoardView;

/**
 * 作业看板-计划进度页面
 * 
 * @author XS-PC014
 * 
 */
public class WorkInfoPlanActivity extends WorkInfoBaseActivity {
	/** LOG */
	private final String LOG_TAG = WorkInfoPlanActivity.class.getSimpleName();
	private final MosLog mLog = MosLog.getInstance(LOG_TAG, "XUESI-SMS");
	/** progress的次数 */
	private int mProgressNumber = 0;

	// 左侧控件
	private TextView planFinishPrecentTxt, planFinishCountTxt1,
			planFinishCountTxt2, planUnfinishPercentTxt, planUnfinishCountTxt1,
			planUnfinishCountTxt2, planOverDueTxt, planOverDueCountTxt;

	// 仪表盘
	private DashBoardView planOverDueBoard;

	// 右侧控件
	private TextView planNewWorkTxt, planPreWorkTxt, planWorkingTxt,
			planWorkFinishTxt;
	private TextView planNewWorkPercentTxt, planPreWorkPercentTxt,
			planWorkingPercentTxt, planWorkFinishPercentTxt;
	private LinearLayout chartLayout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		SmsApplication.getInstance().addActivity(this);// 添加到Activity堆栈
		setContentView(R.layout.activity_workinfo_plan);
		// MySQL版本没有更改
		myNetwork(API_WORK);
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
		setTopTitle(R.string.workInfo_plan);

		// 已完工
		planFinishPrecentTxt = (TextView) findViewById(R.id.planprogress_finish_txt);
		planFinishCountTxt1 = (TextView) findViewById(R.id.planprogress_finish_count);
		planFinishCountTxt2 = (TextView) findViewById(R.id.planprogress_finish_count2);

		// 未完工
		planUnfinishPercentTxt = (TextView) findViewById(R.id.planprogress_unfinish_txt);
		planUnfinishCountTxt1 = (TextView) findViewById(R.id.planprogress_unfinish_count);
		planUnfinishCountTxt2 = (TextView) findViewById(R.id.planprogress_unfinish_count2);

		// 逾期率
		planOverDueTxt = (TextView) findViewById(R.id.planprogress_overdue_ratio);
		planOverDueCountTxt = (TextView) findViewById(R.id.planprogress_overdue_txt);
		planOverDueBoard = (DashBoardView) findViewById(R.id.plan_overdue_dashboard);
		planOverDueBoard.setPanelView(R.drawable.worklook_meter1);

		// 未派工, 未完成, 加工中, 已完工
		planNewWorkTxt = (TextView) findViewById(R.id.plan_new_work_count_txt);
		planPreWorkTxt = (TextView) findViewById(R.id.plan_pre_work_count_txt);
		planWorkingTxt = (TextView) findViewById(R.id.plan_working_count_txt);
		planWorkFinishTxt = (TextView) findViewById(R.id.plan_work_finish_count_txt);
		planNewWorkPercentTxt = (TextView) findViewById(R.id.plan_new_work_percent_txt);
		planPreWorkPercentTxt = (TextView) findViewById(R.id.plan_pre_work_percent_txt);
		planWorkingPercentTxt = (TextView) findViewById(R.id.plan_working_percent_txt);
		planWorkFinishPercentTxt = (TextView) findViewById(R.id.plan_work_finish_percent_txt);

		// 图表
		chartLayout = (LinearLayout) findViewById(R.id.plan_chart_layout);

		// 初始化
		updateViewData();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		super.onClick(v);
	}

	@Override
	public void myNetwork(String url, String... txt) {
		// TODO Auto-generated method stub
		super.myNetwork(url, txt);
		// mProgressNumber++;
		// if (mProgressNumber == 1) {
		// showProgressDialog("加载中", false, false);
		// }
		// JSONObject jo = getRequstJson();
		// if (API_WORK.equals(url)) {
		// // 第一个接口
		// try {
		// // 检索开始时间
		// jo.put("pStartDate", startDate);
		// // 检索结束时间
		// jo.put("pEndDate", endDate);
		// sendPOST(ServerApi.getInstance().API_PLAN_LIST_FINISH, jo, null,
		// this,
		// ServerApi.getInstance().API_PLAN_LIST_FINISH, false);
		// } catch (JSONException e) {
		// e.printStackTrace();
		// }
		// } else if (ServerApi.getInstance().API_PLAN_LIST_PLAN.equals(url)
		// || ServerApi.getInstance().API_PLAN_LIST_NUMNESTED.equals(url)
		// || ServerApi.getInstance().API_PLAN_LIST_OVER_DUE.equals(url)
		// || ServerApi.getInstance().API_ASSEMBLY_N.equals(url)) {
		// try {
		// jo.put("pStartDate", startDate);
		// jo.put("pEndDate", endDate);
		// sendPOST(url, jo, null, this, url, false);
		// } catch (JSONException e) {
		// e.printStackTrace();
		// }
		// } else {
		// toastShort("错误的网络请求，请检查");
		// }
	}

	/** 解析值 */
	private int FinishQty = 0;
	private int PlanQty = 0;
	private List<WorkInfoPlan.TotalClass> numNestedList = new ArrayList<WorkInfoPlan.TotalClass>();
	private List<WorkInfoPlan.TotalClass> assemblyList = new ArrayList<WorkInfoPlan.TotalClass>();
	private int OverDueQty = 0;

	@Override
	public void onRequestSuccess(String tag, Object obj) {
		// TODO Auto-generated method stub
		super.onRequestSuccess(tag, obj);
		//
		// Gson gson = new Gson();
		// if (ServerApi.getInstance().API_PLAN_LIST_FINISH.equals(tag)) {
		// mLog.i(obj.toString());
		// WorkInfoPlan workInfoPlan = gson.fromJson(obj.toString(),
		// WorkInfoPlan.class);
		// if (workInfoPlan.getResultCode() == 0) {
		// // 计划已完成数
		// FinishQty =
		// Integer.parseInt(workInfoPlan.getList().get(0).getFinishQty().length()
		// == 0 ? "0"
		// : workInfoPlan.getList().get(0).getFinishQty());
		// } else {
		// toastShort("resultCode==" + workInfoPlan.getResultCode() +
		// "，请联系开发人员");
		// }
		// myNetwork(ServerApi.getInstance().API_PLAN_LIST_PLAN);
		// } else if (ServerApi.getInstance().API_PLAN_LIST_PLAN.equals(tag)) {
		// mLog.i(obj.toString());
		// WorkInfoPlan workInfoPlan = gson.fromJson(obj.toString(),
		// WorkInfoPlan.class);
		// if (workInfoPlan.getResultCode() == 0) {
		// // 计划总数
		// PlanQty =
		// Integer.parseInt(workInfoPlan.getList().get(0).getPlanQty().length()
		// == 0 ? "0"
		// : workInfoPlan.getList().get(0).getPlanQty());
		// }
		// myNetwork(ServerApi.getInstance().API_PLAN_LIST_NUMNESTED);
		// } else if
		// (ServerApi.getInstance().API_PLAN_LIST_NUMNESTED.equals(tag)) {
		// mLog.i(obj.toString());
		// WorkInfoPlan workInfoPlan = gson.fromJson(obj.toString(),
		// WorkInfoPlan.class);
		// if (workInfoPlan.getResultCode() == 0) {
		// numNestedList.addAll(workInfoPlan.getList());
		// }
		// myNetwork(ServerApi.getInstance().API_PLAN_LIST_OVER_DUE);
		// } else if
		// (ServerApi.getInstance().API_PLAN_LIST_OVER_DUE.equals(tag)) {
		// mLog.i(obj.toString());
		// WorkInfoPlan workInfoPlan = gson.fromJson(obj.toString(),
		// WorkInfoPlan.class);
		// if (workInfoPlan.getResultCode() == 0) {
		// // 逾期零件数
		// OverDueQty =
		// Integer.parseInt(workInfoPlan.getList().get(0).getOverDueQty().length()
		// == 0 ? "0"
		// : workInfoPlan.getList().get(0).getOverDueQty());
		// }
		// myNetwork(ServerApi.getInstance().API_ASSEMBLY_N);
		// } else if (ServerApi.getInstance().API_ASSEMBLY_N.equals(tag)) {
		// mLog.i(obj.toString());
		// WorkInfoPlan workInfoPlan = gson.fromJson(obj.toString(),
		// WorkInfoPlan.class);
		// if (workInfoPlan.getResultCode() == 0) {
		// assemblyList.addAll(workInfoPlan.getList());
		// }
		// // 刷新界面
		// updateViewData();
		// handler.postDelayed(runnable, TIME);
		// }
		// if (mProgressNumber > 0) {
		// mProgressNumber--;
		// }
		// if (mProgressNumber == 0) {
		// dismissProgressDialog();
		// }
	}

	private void updateViewData() {
		// TODO Auto-generated method stub
		double planFinishPercent = SmsUtil.getPercentValue(FinishQty, PlanQty);
		// 已完工
		planFinishPrecentTxt.setText(getString(R.string.workInfo_finish)
				+ SmsUtil.getPercentString(planFinishPercent));
		planFinishCountTxt1.setText(FinishQty + getString(R.string.piece));
		planFinishCountTxt1.setLayoutParams(new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.MATCH_PARENT,
				(float) planFinishPercent));
		planFinishCountTxt2.setLayoutParams(new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.MATCH_PARENT,
				(float) (1.0 - planFinishPercent)));

		// 未完工
		planUnfinishPercentTxt.setText(getString(R.string.workInfo_unfinish)
				+ SmsUtil.getPercentString(1.0f - planFinishPercent));
		planUnfinishCountTxt1.setText((PlanQty - FinishQty)
				+ getString(R.string.piece));
		planUnfinishCountTxt1.setLayoutParams(new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.MATCH_PARENT,
				(float) (1.0 - planFinishPercent)));
		planUnfinishCountTxt2.setLayoutParams(new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.MATCH_PARENT,
				(float) planFinishPercent));

		// 逾期率
		double planOverDuePercent = SmsUtil
				.getPercentValue(OverDueQty, PlanQty);
		planOverDueBoard.setDashBoradValue(planOverDuePercent);
		planOverDueTxt.setText(SmsUtil.getPercentString(planOverDuePercent));
		planOverDueCountTxt.setText(OverDueQty + getString(R.string.piece));

		// 未派工, 未加工, 加工中, 已完工
		int tmpPerWorkCount = 0;
		int tmpWorkingCount = 0;
		if (numNestedList.size() != 0) {
			for (WorkInfoPlan.TotalClass entity : numNestedList) {
				if (entity.getFlag().equals("0")) {
					tmpPerWorkCount += Integer.parseInt(entity.getNumNested()
							.length() == 0 ? "0" : entity.getNumNested());
				} else {
					tmpWorkingCount += Integer.parseInt(entity.getNumNested()
							.length() == 0 ? "0" : entity.getNumNested());
				}
			}
		}

		int newWorkCount = PlanQty - tmpPerWorkCount - tmpWorkingCount
				- FinishQty;
		int perWorkCount = tmpPerWorkCount;
		int workingCount = tmpWorkingCount;
		int workFinishCount = FinishQty;

		planNewWorkTxt.setText(newWorkCount + "");
		planPreWorkTxt.setText(perWorkCount + "");
		planWorkingTxt.setText(workingCount + "");
		planWorkFinishTxt.setText(FinishQty + "");

		double tmpPercent = SmsUtil.getPercentValue(newWorkCount, PlanQty);
		planNewWorkTxt.setLayoutParams(new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.MATCH_PARENT, (float) tmpPercent));
		planNewWorkPercentTxt.setText(getString(R.string.workInfo_newWork)
				+ SmsUtil.getPercentString(tmpPercent));

		tmpPercent = SmsUtil.getPercentValue(perWorkCount, PlanQty);
		planPreWorkPercentTxt.setText(getString(R.string.workInfo_preWork)
				+ SmsUtil.getPercentString(SmsUtil.getPercentValue(
						perWorkCount, PlanQty)));
		planPreWorkTxt.setLayoutParams(new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.MATCH_PARENT, (float) tmpPercent));

		tmpPercent = SmsUtil.getPercentValue(workingCount, PlanQty);
		planWorkingPercentTxt.setText(getString(R.string.workInfo_working)
				+ SmsUtil.getPercentString(SmsUtil.getPercentValue(
						workingCount, PlanQty)));
		planWorkingTxt.setLayoutParams(new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.MATCH_PARENT, (float) tmpPercent));

		tmpPercent = SmsUtil.getPercentValue(workFinishCount, PlanQty);
		planWorkFinishPercentTxt.setText(getString(R.string.workInfo_finish)
				+ SmsUtil.getPercentString(SmsUtil.getPercentValue(
						workFinishCount, PlanQty)));
		planWorkFinishTxt.setLayoutParams(new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.MATCH_PARENT, (float) tmpPercent));

		// 柱状图的两个序列的名字
		String[] titles = new String[] { getString(R.string.workInfo_unfinish),
				getString(R.string.workInfo_finish) };

		// 存放柱状图两个序列的值
		ArrayList<double[]> value = new ArrayList<double[]>();
		int size = assemblyList.size();
		double[] d1 = new double[size];
		double[] d2 = new double[size];
		String[] xAxisLabel = new String[size];

		int[] xLable = new int[size];

		WorkInfoPlan.TotalClass entity;
		for (int i = 0; i < size; i++) {
			entity = assemblyList.get(i);
			d1[i] = Double
					.parseDouble(entity.getFinishQty().length() == 0 ? "0"
							: entity.getFinishQty());
			d2[i] = Double
					.parseDouble(entity.getUnFinishQty().length() == 0 ? "0"
							: entity.getUnFinishQty());
			xAxisLabel[i] = entity.getAssemblyName();
			xLable[i] = i + 1;
		}

		double maxY = 0;
		for (double tmp : d1) {
			if (maxY < tmp) {
				maxY = tmp;
			}
		}
		for (double tmp : d2) {
			if (maxY < tmp) {
				maxY = tmp;
			}
		}
		maxY += 10;

		value.add(d1);
		value.add(d2);

		// 两个状的颜色
		int[] colors = { 0xFF84AC46, 0xFFF0603A };

		chartLayout.removeAllViews();

		// 为chartLayout添加柱状图
		chartLayout.addView(
				getChartView(titles, value, colors, 4, 6, new double[] { 0,
						4 + 0.5, 0, maxY }, xLable, xAxisLabel),
				new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
						LayoutParams.MATCH_PARENT));

		updateDataTypeStatus();

		FinishQty = 0;
		PlanQty = 0;
		numNestedList.clear();
		assemblyList.clear();
		OverDueQty = 0;
	}

	private GraphicalView getChartView(String[] titles,
			ArrayList<double[]> value, int[] colors, int x, int y,
			double[] range, int[] xLable, String[] xAxisLabel) {
		// 多个渲染
		XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();
		// 多个序列的数据集
		XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();

		// 构建数据集以及渲染
		for (int i = 0; i < titles.length; i++) {

			XYSeries series = new XYSeries(titles[i]);
			double[] yLable = value.get(i);

			for (int j = 0; j < yLable.length; j++) {
				series.add(xLable[j], yLable[j]);// 角标越界
				renderer.addXTextLabel(xLable[j], xAxisLabel[j]);
			}

			dataset.addSeries(series);
			XYSeriesRenderer xyRenderer = new XYSeriesRenderer();
			// 设置颜色
			xyRenderer.setColor(colors[i]);
			// 设置点的样式 //
			xyRenderer.setPointStyle(PointStyle.SQUARE);
			// 将要绘制的点添加到坐标绘制中

			renderer.addSeriesRenderer(xyRenderer);
		}
		// X轴方向显示多少个标签,这里显示0，因为我们要自定义X轴的标签
		renderer.setXLabels(0);
		// 设置Y轴标签数
		renderer.setYLabels(y);
		// 设置x轴的最大值
		renderer.setXAxisMax(x - 0.5);
		// 设置轴的颜色
		renderer.setAxesColor(Color.BLACK);

		// 设置现实网格
		renderer.setShowGrid(true);

		renderer.setShowAxes(true);
		// 设置条形图之间的距离
		renderer.setBarSpacing(0.2);
		renderer.setInScroll(false);
		renderer.setPanEnabled(true, false);
		renderer.setZoomEnabled(false, false);
		renderer.setClickEnabled(false);
		// 设置x轴和y轴标签的颜色
		renderer.setXLabelsColor(Color.BLACK);
		renderer.setYLabelsColor(0, Color.BLACK);
		// 设置x轴和y轴的标签对齐方式
		renderer.setXLabelsAlign(Align.CENTER);
		renderer.setYLabelsAlign(Align.RIGHT);
		// 设置滑动的限制
		renderer.setPanLimits(new double[] { 0, xLable.length + 1, -10, 40 });
		// renderer.setZoomLimits(new double[] { 2004, 2013, -10, 40 });
		int length = renderer.getSeriesRendererCount();
		// 设置图标的标题
		// renderer.setChartTitle(xtitle);
		// renderer.setLabelsColor(Color.RED);

		// 设置显示网格
		renderer.setShowGrid(true);
		renderer.setGridColor(Color.BLACK);

		// 设置图例的字体大小
		// renderer.setLegendTextSize(18);

		renderer.setLabelsColor(Color.BLACK); // 图上说明文字的颜色

		// 不显示图例
		renderer.setShowLegend(false);
		// 设置坐标轴标签字体大小
		renderer.setAxisTitleTextSize(20);

		// 设置x轴和y轴的最大最小值
		renderer.setRange(range);
		renderer.setMarginsColor(0x00888888);
		for (int i = 0; i < length; i++) {
			SimpleSeriesRenderer ssr = renderer.getSeriesRendererAt(i);
			ssr.setChartValuesTextAlign(Align.RIGHT);
			ssr.setChartValuesTextSize(12);
			ssr.setDisplayChartValues(true);
		}
		GraphicalView mChartView = ChartFactory.getBarChartView(
				getApplicationContext(), dataset, renderer, Type.DEFAULT);

		return mChartView;
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
		// handler.removeCallbacks(runnable);
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

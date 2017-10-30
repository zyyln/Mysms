package com.xuesi.sms.app.activity;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.Paint.Align;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.xuesi.mos.util.MosLog;
import com.xuesi.sms.R;
import com.xuesi.sms.SmsApplication;
import com.xuesi.sms.bean.WbTotalData;
import com.xuesi.sms.chart.MyChartFactory;
import com.xuesi.sms.util.SmsUtil;
import com.xuesi.sms.widget.DashBoardView;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.BarChart.Type;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.CategorySeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.DialRenderer;
import org.achartengine.renderer.SimpleSeriesRenderer;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import java.util.ArrayList;
import java.util.List;

/**
 * 作业看板-利用率页面
 * 
 * @author XS-PC014
 * 
 */
public class WorkInfoUseRatioActivity extends WorkInfoBaseActivity {
	/** LOG */
	private final String LOG_TAG = WorkInfoUseRatioActivity.class
			.getSimpleName();
	private final MosLog mLog = MosLog.getInstance(LOG_TAG, "XUESI-SMS");
	/** progress的次数 */
	private int mProgressNumber = 0;

	// 套料员，材质， 厚度， 设备类型
	private Button userBtn, typeBtn, deepBtn, deviceBtn;
	private DashBoardView useRatioBoard;
	private LinearLayout chartLayout;
	// private LinearLayout dialLayout;
	private TextView useRatioTxt;
	private List<Button> btnList = new ArrayList<Button>();
	private int tmpBtnIndex = 0;
	WbTotalData wTotalData;
	int num = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		SmsApplication.getInstance().addActivity(this);// 添加到Activity堆栈
		setContentView(R.layout.activity_workinfo_useratio);

		// myNetwork(ServerApi.getInstance().API_USED_TOTAL_RATE);
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
		setTopTitle(R.string.workInfo_useratio);

		// 套料员
		userBtn = (Button) findViewById(R.id.workinfo_useratio_users_btn);
		userBtn.setOnClickListener(this);
		userBtn.setEnabled(false);
		btnList.add(userBtn);

		// 厚度
		deepBtn = (Button) findViewById(R.id.workinfo_useratio_deep_btn);
		deepBtn.setOnClickListener(this);
		btnList.add(deepBtn);

		// 材质
		typeBtn = (Button) findViewById(R.id.workinfo_useratio_type_btn);
		typeBtn.setOnClickListener(this);
		btnList.add(typeBtn);

		// 设备类型
		deviceBtn = (Button) findViewById(R.id.workinfo_useratio_device_btn);
		deviceBtn.setOnClickListener(this);
		btnList.add(deviceBtn);

		chartLayout = (LinearLayout) findViewById(R.id.workinfo_useratio_chart);
		// 为chartLayout添加柱状图
		chartLayout.addView(
				getChartView(new double[] {}, new double[] { 0, 0, 0, 0 },
						new String[] {}, 0xFF008AC6),
				new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
						LayoutParams.MATCH_PARENT));
		// dialLayout = (LinearLayout)
		// findViewById(R.id.workinfo_useratio_dial);
		useRatioBoard = (DashBoardView) findViewById(R.id.useratio_dashboard);
		useRatioBoard.setDashBoradValue(0);
		useRatioTxt = (TextView) findViewById(R.id.workinfo_useratio_percent);
		useRatioTxt.setText(getString(R.string.useRatio)
				+ SmsUtil.getPercentString(1));
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		super.onClick(v);
		switch (v.getId()) {
		case R.id.bartop_img_refresh: {
			myNetwork(API_WORK, "");
		}
			break;
		// 套料员
		case R.id.workinfo_useratio_users_btn: {
			tmpBtnIndex = 0;
			// handler.removeCallbacks(runnable);
			// myNetwork(ServerApi.getInstance().API_USED_TOTAL_RATE);
		}
			break;
		// 厚度
		case R.id.workinfo_useratio_deep_btn: {
			tmpBtnIndex = 1;
			// handler.removeCallbacks(runnable);
			// myNetwork(ServerApi.getInstance().API_USED_TOTAL_RATE);
		}
			break;
		// 材质
		case R.id.workinfo_useratio_type_btn: {
			tmpBtnIndex = 2;
			// handler.removeCallbacks(runnable);
			// myNetwork(ServerApi.getInstance().API_USED_TOTAL_RATE);
		}
			break;
		// 设备类型
		case R.id.workinfo_useratio_device_btn: {
			tmpBtnIndex = 3;
			// handler.removeCallbacks(runnable);
			// myNetwork(ServerApi.getInstance().API_USED_TOTAL_RATE);
		}
			break;
		default:
			break;
		}
	}

	private void updateBtnSelectState() {
		for (int i = 0; i < btnList.size(); i++) {
			if (i == tmpBtnIndex) {
				((Button) btnList.get(i)).setEnabled(false);
			} else {
				((Button) btnList.get(i)).setEnabled(true);
			}
		}
	}

	@Override
	public void myNetwork(String tag, String... txt) {
		super.myNetwork(tag, txt);
		// mProgressNumber++;
		// if (mProgressNumber == 1) {
		// showProgressDialog("加载中", false, false);
		// }
		// JSONObject json = getRequstJson();
		// if (API_WORK.equals(tag)) {
		// try {
		// json.put("pFieldFlg", 0);
		// json.put("pBeginDate", startDate);
		// json.put("pEndDate", endDate);
		// sendPOST(ServerApi.getInstance().API_USED_TOTAL_RATE, json, null,
		// this,
		// ServerApi.getInstance().API_USED_TOTAL_RATE, false);
		// } catch (JSONException e) {
		// e.printStackTrace();
		// }
		// } else if (ServerApi.getInstance().API_USED_TOTAL_RATE.equals(tag) &&
		// num == 0) {// 总利用率请求数据
		// try {
		// json.put("pFieldFlg", 0);
		// json.put("pBeginDate", startDate);
		// json.put("pEndDate", endDate);
		// sendPOST(tag, json, null, this, tag, false);
		// } catch (JSONException e) {
		// e.printStackTrace();
		// }
		// } else if (ServerApi.getInstance().API_USED_TOTAL_RATE.equals(tag) &&
		// num == 1) {// 具体利用率请求数据
		// try {
		// json.put("pFieldFlg", tmpBtnIndex + 1);
		// json.put("pBeginDate", startDate);
		// json.put("pEndDate", endDate);
		// sendPOST(tag, json, null, this, tag, false);
		// } catch (JSONException e) {
		// e.printStackTrace();
		// }
		// } else {
		// toastShort("错误的网络请求，请检查");
		// }
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
		//
		// if (mProgressNumber > 0) {
		// mProgressNumber--;
		// }
		// if (mProgressNumber == 0) {
		// dismissProgressDialog();
		// }
		// Gson gson = new Gson();
		// if (ServerApi.getInstance().API_USED_TOTAL_RATE.equals(tag)) {
		// mLog.i(LOG_TAG + obj.toString());
		//
		// wTotalData = gson.fromJson(obj.toString(), WbTotalData.class);
		// if (wTotalData.getResultCode() == 0) {
		//
		// if (num == 0) {
		// switch (tmpBtnIndex) {
		// case 0:
		// // 套料员
		// num++;
		// myNetwork(ServerApi.getInstance().API_USED_TOTAL_RATE);
		// break;
		// case 1:
		// // 厚度
		// num++;
		// myNetwork(ServerApi.getInstance().API_USED_TOTAL_RATE);
		// break;
		// case 2:
		// // 材质
		// num++;
		// myNetwork(ServerApi.getInstance().API_USED_TOTAL_RATE);
		// break;
		// case 3:
		// // 设备类型
		// num++;
		// myNetwork(ServerApi.getInstance().API_USED_TOTAL_RATE);
		// break;
		// default:
		// break;
		// }
		// } else if (num == 1) {
		// /**
		// * 更新服务器数据
		// */
		// if (!wTotalData.getRateLists().isEmpty()) {
		// // dialLayout.removeAllViews();
		// // dialLayout.addView(getDialView(wTotalData.getRateLists().get(0)
		// // .getUsedRate()), new LinearLayout.LayoutParams(
		// // LayoutParams.WRAP_CONTENT,
		// // LayoutParams.WRAP_CONTENT));
		// // mLog.e("加载仪表真实数据。。。");
		// useRatioBoard.setDashBoradValue(wTotalData.getRateLists().get(0).getUsedRate()
		// / 100);
		// useRatioTxt.setText(getString(R.string.useRatio)
		// +
		// SmsUtil.getPercentString(wTotalData.getRateLists().get(0).getUsedRate()
		// / 100));
		// } else {
		// // dialLayout.removeAllViews();
		// // dialLayout.addView(getDialView(50), new
		// // LinearLayout.LayoutParams(
		// // LayoutParams.WRAP_CONTENT,
		// // LayoutParams.WRAP_CONTENT));
		// // mLog.e("加载仪表。。。");
		// useRatioBoard.setDashBoradValue(0);
		// useRatioTxt.setText(getString(R.string.useRatio) +
		// SmsUtil.getPercentString(0));
		// }
		// // 存放柱状图两个序列的值
		// double[] d1 = new double[wTotalData.getRateLists().size()];
		// String[] d2 = new String[wTotalData.getRateLists().size()];
		//
		// for (int i = 0; i < wTotalData.getRateLists().size(); i++) {
		// d1[i] = wTotalData.getRateLists().get(i).getUsedRate();
		// d2[i] = wTotalData.getRateLists().get(i).getGroupField();
		// }
		//
		// double maxY = 0;
		// for (double tmp : d1) {
		// if (maxY < tmp) {
		// maxY = tmp;
		// }
		// }
		// maxY += 1;
		//
		// chartLayout.removeAllViews();
		//
		// int color;
		// if (tmpBtnIndex == 0) {
		// color = 0xFF008AC6;
		// } else if (tmpBtnIndex == 1) {
		// color = 0xFF8DA954;
		// } else if (tmpBtnIndex == 2) {
		// color = 0xFF995B93;
		// } else {
		// color = 0xFFFF8F00;
		// }
		// // 为chartLayout添加柱状图
		// chartLayout.addView(getChartView(d1, new double[] { 0, d1.length +
		// 0.5, 0, maxY }, d2, color),
		// new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
		// LayoutParams.MATCH_PARENT));
		//
		// wTotalData = null;
		// num = 0;
		//
		// updateDataTypeStatus();
		// updateBtnSelectState();
		//
		// handler.postDelayed(runnable, TIME);
		// }
		// }
		//
		// }
	}

	private GraphicalView getChartView(double[] valueY, double[] range,
			String[] xAxisLabel, int color) {
		// 多个渲染
		XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();
		// 多个序列的数据集
		XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();

		// 构建数据集以及渲染

		XYSeries series = new XYSeries(getString(R.string.workInfo_useratio));
		// double[] yLable = value.get(i);

		for (int i = 0; i < valueY.length; i++) {
			series.add(i + 1, valueY[i]);

			renderer.addXTextLabel(i + 1, xAxisLabel[i]);
		}

		for (int i = 0; i < 6; i++) {
			renderer.addYTextLabel(i * 10 * 2, i * 10 * 2 + "%");
		}

		dataset.addSeries(series);

		XYSeriesRenderer xyRenderer = new XYSeriesRenderer();
		// 设置颜色
		xyRenderer.setColor(color);
		xyRenderer.setPointStyle(PointStyle.SQUARE);
		// 将要绘制的点添加到坐标绘制中

		renderer.addSeriesRenderer(xyRenderer);

		// X轴方向显示多少个标签,这里显示0，因为我们要自定义X轴的标签
		renderer.setXLabels(0);
		// 设置Y轴标签数
		renderer.setYLabels(6);
		renderer.setYAxisMax(100);

		// 设置x轴的最大值
		renderer.setXAxisMax(valueY.length);

		// 设置轴的颜色
		renderer.setAxesColor(Color.BLACK);

		renderer.setShowAxes(true);
		// 设置条形图之间的距离
		renderer.setBarSpacing(0.5);
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
		renderer.setPanLimits(new double[] { 0, valueY.length + 1, -10, 40 });
		int length = renderer.getSeriesRendererCount();

		// 设置显示网格
		renderer.setShowGrid(true);
		renderer.setGridColor(Color.BLACK);

		// 设置图例的字体大小
		// renderer.setLegendTextSize(18);

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

	@SuppressLint("NewApi")
	public GraphicalView getDialView(double valueDial) {
		DialRenderer renderer = new DialRenderer();
		// 构建数据集以及渲染
		CategorySeries dataset = new CategorySeries(
				getString(R.string.workInfo_useratio));
		// 添加值
		dataset.add(valueDial);
		renderer.setAngleMax(60);
		renderer.setAngleMin(300);
		renderer.setMinValue(0);
		renderer.setMaxValue(100);
		renderer.setMajorTicksSpacing(10);

		XYSeriesRenderer xyRenderer = new XYSeriesRenderer();
		// xyRenderer.setColor(getResources().getColor(R.color.orangered));
		xyRenderer.setPointStyle(PointStyle.CIRCLE);
		xyRenderer.setChartValuesTextAlign(Align.CENTER);
		// 将要绘制的点添加到坐标绘制中
		renderer.addSeriesRenderer(xyRenderer);
		renderer.setLabelsColor(getResources().getColor(R.color.black));// 设置标签颜色，包括Title
		// renderer.setLegendHeight(-50);
		/** 设置Lable字体大小 */
		renderer.setLabelsTextSize(getResources().getDimension(
				R.dimen.dimen_8_dip));
		renderer.setZoomButtonsVisible(true);// 放大缩小按钮

		renderer.setApplyBackgroundColor(true);
		renderer.setBackgroundColor(getResources().getColor(R.color.silver));
		// renderer.setChartTitle("利用率：" + valueDial + "%");
		renderer.setChartTitle("NJXS");
		renderer.setChartTitleTextSize(getResources().getDimension(
				R.dimen.dimen_16_dip));//
		// 设置Title字体大小
		renderer.setPanEnabled(true);// 设置仪表盘是否能拖动FALSE：不能拖动
		// /**初始化仪表盘大小*/
		// renderer.setScale(0.5F);
		renderer.setDisplayValues(true);
		renderer.setZoomRate((float) 1.1);// 一次放大倍数
		renderer.setSelectableBuffer(30);
		GraphicalView mChartView = MyChartFactory.getDialView(
				getApplicationContext(), dataset, renderer);
		return mChartView;
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

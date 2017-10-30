package com.xuesi.sms.app.activity;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;
import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.Color;
import android.graphics.Paint.Align;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.xuesi.mos.util.MosLog;
import com.xuesi.sms.R;
import com.xuesi.sms.ServerApi;
import com.xuesi.sms.SmsApplication;
import com.xuesi.sms.bean.WbTotalData;
import com.xuesi.sms.util.SmsUtil;

/**
 * 作业看板-产量页面
 * 
 * @author XS-PC014
 * 
 */
public class WorkInfoYieldActivity extends WorkInfoBaseActivity {
	/** LOG */
	private final String LOG_TAG = WorkInfoYieldActivity.class.getSimpleName();
	private final MosLog mLog = MosLog.getInstance(LOG_TAG, "XUESI-SMS");
	/** progress的次数 */
	private int mProgressNumber = 0;

	/** 零件重量、 切割钢板数 */
	private TextView yieldWeightTv, yieldCountTv;

	/** 图标 （零件重量、 切割钢板数） */
	private LinearLayout yieldWeightChart, yieldCountChart;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		SmsApplication.getInstance().addActivity(this);// 添加到Activity堆栈
		setContentView(R.layout.activity_workinfo_yield);

		myNetwork(API_WORK, "");
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
		setTopTitle(R.string.workInfo_yield);
		// 零件重量
		yieldWeightTv = (TextView) findViewById(R.id.workInfo_yield_weight_title);
		yieldWeightChart = (LinearLayout) findViewById(R.id.workinfo_yield_weight_chart);

		// 切割钢板数
		yieldCountTv = (TextView) findViewById(R.id.workInfo_yield_count_title);
		yieldCountChart = (LinearLayout) findViewById(R.id.workinfo_yield_count_chart);
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
		//
		// mProgressNumber++;
		// if (mProgressNumber == 1) {
		// showProgressDialog("加载中", false, false);
		// }
		// if (API_WORK.equals(url)) {
		// try {
		// JSONObject json = getRequstJson();
		// // 分类字段 0:总数，1：天的新增件数，2：周的新增件数，3：月的新增件数，4：年的新增件数
		// json.put("pFieldFlg", tmpSelectIndex + 1);
		// // 检索开始时间,传递的时候yyyy-mm-dd格式，查询总数的时候传空
		// json.put("pBeginDate", startDate);
		// // 检索结束时间,传递的时候yyyy-mm-dd格式，查询总数的时候传空
		// json.put("pEndDate", endDate);
		// sendPOST(ServerApi.getInstance().API_YIELD_INFO, json, null, this,
		// ServerApi.getInstance().API_YIELD_INFO, false);
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
		// 暂停更新
		handler.removeCallbacks(runnable);
	}

	@Override
	public void onRequestSuccess(String url, Object obj) {
		// TODO Auto-generated method stub
		super.onRequestSuccess(url, obj);
		// Gson gson = new Gson();
		// if (ServerApi.getInstance().API_YIELD_INFO.equals(url)) {
		// WbTotalData wbTotalData = gson.fromJson(obj.toString(),
		// WbTotalData.class);
		// if (wbTotalData.getResultCode() == 0) {
		// double weightTotal = 0;
		// int countTotal = 0;
		// int length = wbTotalData.getSheetInfoLists().size();
		// double[] ys1 = new double[length];
		// double[] ys2 = new double[length];
		// for (int i = 0; i < length; i++) {
		// weightTotal += wbTotalData.getSheetInfoLists().get(i).getWeight();
		// countTotal += wbTotalData.getSheetInfoLists().get(i).getAmount();
		// ys1[i] = wbTotalData.getSheetInfoLists().get(i).getWeight();
		// ys2[i] = wbTotalData.getSheetInfoLists().get(i).getAmount();
		// }
		// yieldWeightTv.setText(SmsUtil.DecimalFormat(weightTotal, 3)
		// + getString(R.string.ton));
		// yieldCountTv.setText(countTotal + getString(R.string.steel_unit));
		//
		// double MaxY = 0;
		// for (double tmpY : ys1) {
		// if (MaxY < tmpY) {
		// MaxY = tmpY;
		// }
		// }
		//
		// yieldWeightChart.removeAllViews();
		// yieldWeightChart.addView(getLineView(ys1, MaxY + 1, 0xFFF0603A));
		//
		// MaxY = 0;
		// for (double tmpY : ys2) {
		// if (MaxY < tmpY) {
		// MaxY = tmpY;
		// }
		// }
		// yieldCountChart.removeAllViews();
		// yieldCountChart.addView(getLineView(ys2, MaxY + 1, 0xFF84AC46));
		//
		// updateDataTypeStatus();
		// }
		// // 开启更新
		// handler.postDelayed(runnable, TIME);
		// }
		// if (mProgressNumber > 0) {
		// mProgressNumber--;
		// }
		// if (mProgressNumber == 0) {
		// dismissProgressDialog();
		// }
	}

	/**
	 * 绘制图表
	 * 
	 * @param ys
	 * @param maxY
	 * @param color
	 * @return
	 */
	private GraphicalView getLineView(double[] ys, double maxY, int color) {
		XYMultipleSeriesDataset seriesDataset = new XYMultipleSeriesDataset();
		XYSeries xySeries = new XYSeries(null);

		// 天按钮时需要两个数据
		double[] ys1 = null;
		boolean isOne = false;
		if (ys.length == 1) {
			isOne = true;
			ys1 = new double[] { 0, ys[0] };
		} else {
			ys1 = ys;
		}
		for (int i = 0; i < ys1.length; i++) {
			xySeries.add(i + 1, ys1[i]);
		}
		seriesDataset.addSeries(xySeries);

		/* 描绘器，设置图表整体效果，比如x,y轴效果，缩放比例，颜色设置 */
		XYMultipleSeriesRenderer seriesRenderer = new XYMultipleSeriesRenderer();

		seriesRenderer.setMargins(new int[] { 20, 40, 20, 10 });// 设置外边距，顺序为：上左下右

		// 坐标轴设置
		seriesRenderer.setAxisTitleTextSize(16);// 设置坐标轴标题字体的大小
		seriesRenderer.setYAxisMin(0);// 设置y轴的起始值
		seriesRenderer.setYAxisMax(maxY + 0.5);// 设置y轴的最大值
		seriesRenderer.setXAxisMin(0);// 设置x轴起始值
		seriesRenderer.setXAxisMax(ys1.length + 1);// 设置x轴最大值

		// 缩放设置
		seriesRenderer.setZoomButtonsVisible(false);// 设置缩放按钮是否可见
		seriesRenderer.setExternalZoomEnabled(false);// 图表是否可以缩放设置
		seriesRenderer.setZoomEnabled(false, false); // 图表是否可以缩放设置

		// 图表移动设置
		seriesRenderer.setPanEnabled(true, false);// 图表是否可以移动

		// 坐标轴标签设置
		seriesRenderer.setLabelsTextSize(14);// 设置标签字体大小
		seriesRenderer.setXLabelsAlign(Align.CENTER);
		seriesRenderer.setYLabelsAlign(Align.RIGHT);
		// 设置坐标轴,轴的颜色
		seriesRenderer.setAxesColor(Color.BLACK);

		// 设置x轴和y轴标签的颜色
		seriesRenderer.setXLabelsColor(Color.BLACK);
		seriesRenderer.setYLabelsColor(0, Color.BLACK);

		seriesRenderer.setXLabels(0);// 显示的x轴标签的个数

		// 针对特定的x轴值增加文本标签
		for (int i = 0; i < ys1.length; i++) {
			seriesRenderer.addXTextLabel(i + 1, getTypeString(i, isOne));
		}
		seriesRenderer.setPointSize(3);// 设置坐标点大小

		seriesRenderer.setMarginsColor(0x00FFFFFF);// 设置外边距空间的颜色
		seriesRenderer.setClickEnabled(false);
		seriesRenderer.setShowGrid(true);
		seriesRenderer.setGridColor(Color.BLACK);

		// 不显示图例
		seriesRenderer.setShowLegend(false);

		// 某一组数据的描绘器
		XYSeriesRenderer xySeriesRenderer = new XYSeriesRenderer();
		xySeriesRenderer.setPointStyle(PointStyle.POINT);// 坐标点的显示风格
		xySeriesRenderer.setColor(color);// 表示该组数据的图或线的颜色
		xySeriesRenderer.setDisplayChartValues(true);// 设置是否显示坐标点的y轴坐标值
		xySeriesRenderer.setChartValuesTextSize(14);// 设置显示的坐标点值的字体大小
		xySeriesRenderer.setDisplayChartValuesDistance(30);
		xySeriesRenderer.setLineWidth(2);

		seriesRenderer.addSeriesRenderer(xySeriesRenderer);

		GraphicalView view = ChartFactory.getLineChartView(this, seriesDataset,
				seriesRenderer);
		return view;
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

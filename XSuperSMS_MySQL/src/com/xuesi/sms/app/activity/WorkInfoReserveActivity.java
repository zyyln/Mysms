package com.xuesi.sms.app.activity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

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
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.xuesi.mos.util.MosLog;
import com.xuesi.sms.R;
import com.xuesi.sms.ServerApi;
import com.xuesi.sms.SmsApplication;
import com.xuesi.sms.SmsConfig;
import com.xuesi.sms.bean.WbTotalData;
import com.xuesi.sms.bean.WbTotalData.sheetInfoList;
import com.xuesi.sms.util.SmsUtil;

/**
 * 作业看板-库存页面
 * 
 * @author XS-PC014
 * 
 */
public class WorkInfoReserveActivity extends WorkInfoBaseActivity {
	/** LOG */
	private final String LOG_TAG = WorkInfoReserveActivity.class
			.getSimpleName();
	private final MosLog mLog = MosLog.getInstance(LOG_TAG, "XUESI-SMS");
	/** progress的次数 */
	private int mProgressNumber = 0;

	// 左侧控件
	/** 显示钢板数据 */
	private TextView steelCountText, steelWeightText, steelCountNewText,
			steelWeightNewText;
	/** 显示余料数据 */
	private TextView marginCountText, marginWeightText, marginCountNewText,
			marginWeightNewText;
	/** 显示在制品数据 */
	private TextView makingCountText, makingWeightText, makingCountNewText,
			makingWeightNewText;

	// 右侧控件
	/** 钢板、余料、在制品按钮 */
	private Button steelButton, marginButton, makingButton;
	/** 右侧重量、数量图表 */
	private LinearLayout weightChart, countChart;
	/** */
	private TextView sheetWeightText, sheetCountText;
	/** 区别钢板、余料、在制品的标识符 */
	private int tmpBtnIndex = 0;
	/** */
	private List<Button> buttonList = new ArrayList<Button>();
	/** 钢板、余料、在制品张数和重量 */
	private double[] steelCounts, steelRckCounts;
	private double[] steelWeights, steelRckWeights;
	/** 获取总数或者新增数据的标识符 */
	int num = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		SmsApplication.getInstance().addActivity(this);// 添加到Activity堆栈
		setContentView(R.layout.activity_workinfo_reserve);

		myNetwork(ServerApi.getInstance().API_SHEET_INFO);
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
		setTopTitle(R.string.workInfo_reserve);
		// 钢板
		steelCountText = (TextView) findViewById(R.id.workinfo_reserve_steel_count);
		steelWeightText = (TextView) findViewById(R.id.workinfo_reserve_steel_weight);
		steelCountNewText = (TextView) findViewById(R.id.workinfo_reserve_steel_count_new);
		steelWeightNewText = (TextView) findViewById(R.id.workinfo_reserve_steel_weight_new);

		// 余料
		marginCountText = (TextView) findViewById(R.id.workinfo_reserve_margin_count);
		marginWeightText = (TextView) findViewById(R.id.workinfo_reserve_margin_weight);
		marginCountNewText = (TextView) findViewById(R.id.workinfo_reserve_margin_count_new);
		marginWeightNewText = (TextView) findViewById(R.id.workinfo_reserve_margin_weight_new);

		// 在制品
		makingCountText = (TextView) findViewById(R.id.workinfo_reserve_making_count);
		makingWeightText = (TextView) findViewById(R.id.workinfo_reserve_making_weight);
		makingCountNewText = (TextView) findViewById(R.id.workinfo_reserve_making_count_new);
		makingWeightNewText = (TextView) findViewById(R.id.workinfo_reserve_making_weight_new);
		makingCountText.setText(0 + getString(R.string.piece));
		makingWeightText.setText(0.0 + getString(R.string.ton));
		makingCountNewText.setText(getString(R.string.increase) + 0
				+ getString(R.string.piece));
		makingWeightNewText.setText(getString(R.string.increase) + 0.0
				+ getString(R.string.ton));
		// 按钮 -> 钢板、余料、在制品
		steelButton = (Button) findViewById(R.id.workinfo_reserve_steel_btn);
		steelButton.setOnClickListener(this);
		steelButton.setEnabled(false);
		buttonList.add(steelButton);

		marginButton = (Button) findViewById(R.id.workinfo_reserve_margin_btn);
		marginButton.setOnClickListener(this);
		buttonList.add(marginButton);

		makingButton = (Button) findViewById(R.id.workinfo_reserve_making_btn);
		makingButton.setOnClickListener(this);
		// buttonList.add(makingButton);

		// 库存重量图标、库存件数图标
		weightChart = (LinearLayout) findViewById(R.id.workinfo_reserve_weight_chart);
		countChart = (LinearLayout) findViewById(R.id.workinfo_reserve_count_chart);

		// 图表实例文字
		sheetCountText = (TextView) findViewById(R.id.workinfo_reserve_sheet_count);
		sheetWeightText = (TextView) findViewById(R.id.workinfo_reserve_sheet_weight);
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
		case R.id.workinfo_reserve_steel_btn: {// 钢板
			tmpBtnIndex = 0;
			handler.removeCallbacks(runnable);
			myNetwork(ServerApi.getInstance().API_SHEET_INFO);
		}
			break;
		case R.id.workinfo_reserve_margin_btn: {// 余料
			tmpBtnIndex = 1;
			handler.removeCallbacks(runnable);
			myNetwork(ServerApi.getInstance().API_SHEET_INFO);
		}
			break;
		case R.id.workinfo_reserve_making_btn: {// 在制品
			// tmpBtnIndex = 2;
			// handler.removeCallbacks(runnable);
			// myNetwork(ServerApi.getInstance().API_SHEET_INFO);
			showPromptDialog(this, "prompt", R.string.open_new_request);
		}
			break;
		default:
			break;
		}
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
		if (API_WORK.endsWith(tag)) {
			try {
				json.put("pFieldFlg", 0);
				json.put("pBeginDate", "");
				json.put("pEndDate", "");
				sendPOST(ServerApi.getInstance().API_SHEET_INFO, json, null,
						this, ServerApi.getInstance().API_SHEET_INFO, false);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		} else if ((ServerApi.getInstance().API_SHEET_INFO.equals(tag) && num == 0)
				|| (ServerApi.getInstance().API_SHEET_RCK_INFO.equals(tag) && num == 1)) {
			try {
				json.put("pFieldFlg", 0);
				json.put("pBeginDate", "");
				json.put("pEndDate", "");
				sendPOST(tag, json, null, this, tag, false);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		} else if ((ServerApi.getInstance().API_SHEET_INFO.equals(tag) && num == 2)
				|| (ServerApi.getInstance().API_SHEET_RCK_INFO.equals(tag) && num == 3)) {
			try {
				json.put("pFieldFlg", tmpSelectIndex + 1);
				json.put("pBeginDate", startDate);
				json.put("pEndDate", endDate);
				sendPOST(tag, json, null, this, tag, false);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		} else {
			showShortToast("错误的网络请求，请检查");
		}
	}

	@Override
	public void onRequestSuccess(String tag, Object obj) {
		// TODO Auto-generated method stub
		super.onRequestSuccess(tag, obj);

		float tmpCount = 0;
		float tmpWeight = 0;
		int tmpIndex = 0;
		Gson gson = new Gson();
		if (ServerApi.getInstance().API_SHEET_INFO.equals(tag)) {
			mLog.i(LOG_TAG + obj.toString());
			WbTotalData wbTotalData = gson.fromJson(obj.toString(),
					WbTotalData.class);
			if (wbTotalData.getResultCode() == 0) {
				if (num == 0) {
					mLog.d("显示钢板总数据----"
							+ wbTotalData.getSheetInfoLists().size());
					// 显示钢板总数据
					steelCountText.setText((int) (wbTotalData
							.getSheetInfoLists().get(0).getAmount())
							+ getString(R.string.steel_unit));
					steelWeightText.setText(wbTotalData.getSheetInfoLists()
							.get(0).getWeight()
							+ getString(R.string.ton));
					num++;
				} else if (num == 2) {
					mLog.d("新增显示钢板数据----"
							+ wbTotalData.getSheetInfoLists().size());
					// 显示钢板新增数据
					steelCounts = new double[wbTotalData.getSheetInfoLists()
							.size()];
					steelWeights = new double[wbTotalData.getSheetInfoLists()
							.size()];
					for (sheetInfoList data : wbTotalData.getSheetInfoLists()) {
						tmpCount += data.getAmount();
						tmpWeight += data.getWeight();
						steelCounts[tmpIndex] = (int) data.getAmount();
						steelWeights[tmpIndex] = Double.parseDouble(Float
								.toString(data.getWeight()));
						tmpIndex++;
					}
					steelCountNewText.setText(getString(R.string.increase)
							+ (int) tmpCount + getString(R.string.steel_unit));
					steelWeightNewText.setText(getString(R.string.increase)
							+ tmpWeight + getString(R.string.ton));
					num++;
				}
			} else {
				showShortToast("resultCode==" + wbTotalData.getResultCode()
						+ "，请联系开发人员");
				dismissProgressDialog();
			}
			myNetwork(ServerApi.getInstance().API_SHEET_RCK_INFO);
		} else if (ServerApi.getInstance().API_SHEET_RCK_INFO.equals(tag)) {
			mLog.i(LOG_TAG + obj.toString());
			WbTotalData wbTotalDataRCK = gson.fromJson(obj.toString(),
					WbTotalData.class);
			if (wbTotalDataRCK.getResultCode() == 0) {
				if (num == 1) {
					mLog.d(" 显示余料总数据----"
							+ wbTotalDataRCK.getSheetInfoLists().size());
					// 显示余料总数据
					marginCountText.setText((int) (wbTotalDataRCK
							.getSheetInfoLists().get(0).getAmount())
							+ getString(R.string.steel_unit));
					marginWeightText.setText(wbTotalDataRCK.getSheetInfoLists()
							.get(0).getWeight()
							+ getString(R.string.ton));
					num++;
					myNetwork(ServerApi.getInstance().API_SHEET_INFO);
				} else if (num == 3) {
					mLog.d(" 新增显示余料总数据----"
							+ wbTotalDataRCK.getSheetInfoLists().size());
					// 显示新增余料数据
					steelRckCounts = new double[wbTotalDataRCK
							.getSheetInfoLists().size()];
					steelRckWeights = new double[wbTotalDataRCK
							.getSheetInfoLists().size()];
					for (sheetInfoList data : wbTotalDataRCK
							.getSheetInfoLists()) {
						tmpCount += data.getAmount();
						tmpWeight += data.getWeight();
						steelRckCounts[tmpIndex] = (int) data.getAmount();
						steelRckWeights[tmpIndex] = Double.parseDouble(Float
								.toString(data.getWeight()));
						tmpIndex++;
					}
					marginCountNewText.setText(getString(R.string.increase)
							+ (int) tmpCount + getString(R.string.steel_unit));
					marginWeightNewText.setText(getString(R.string.increase)
							+ tmpWeight + getString(R.string.ton));
					// myNetwork(ServerApi.getInstance().API_SHOP_PART_INFO);
					num = 0;

					updataChartView();
					updateDataTypeStatus();
					updateBtnSelectState();
					// 开启
					// handler.postDelayed(runnable, TIME);
				}
			}
		}
		// else if (ServerApi.getInstance().API_SHOP_PART_INFO.equals(tag)) {
		// mLog.i(LOG_TAG + obj.toString());
		// WbTotalData wbTotalDataPART = gson.fromJson(obj.toString(),
		// WbTotalData.class);
		// if (wbTotalDataPART.getResultCode() == 0) {
		// if (num == 2) {
		// mLog.d(" 显示在制品总数据----"
		// + wbTotalDataPART.getSheetInfoLists().size());
		// // 显示在制品总数据
		// makingCountText.setText((int) (wbTotalDataPART
		// .getSheetInfoLists().get(0).getAmount())
		// + getString(R.string.piece));
		// makingWeightText.setText(wbTotalDataPART
		// .getSheetInfoLists().get(0).getWeight()
		// + getString(R.string.ton));
		// num++;
		// myNetwork(ServerApi.getInstance().API_SHEET_INFO);
		// } else if (num == 5) {
		// mLog.d("新增 显示在制品总数据----"
		// + wbTotalDataPART.getSheetInfoLists().size());
		// // 显示新增在制品数据
		// shopPartCounts = new double[wbTotalDataPART
		// .getSheetInfoLists().size()];
		// shopPartWeights = new double[wbTotalDataPART
		// .getSheetInfoLists().size()];
		// for (sheetInfoList data : wbTotalDataPART
		// .getSheetInfoLists()) {
		// tmpCount += data.getAmount();
		// tmpWeight += data.getWeight();
		// shopPartCounts[tmpIndex] = (int) data.getAmount();
		// shopPartWeights[tmpIndex] = SmsUtil.DecimalFormat(
		// data.getWeight(), 4);
		// tmpIndex++;
		// }
		// makingCountNewText.setText(getString(R.string.increase)
		// + (int) tmpCount + getString(R.string.piece));
		// makingWeightNewText.setText(getString(R.string.increase)
		// + SmsUtil.DecimalFormat(tmpWeight, 4)
		// + getString(R.string.ton));
		// num = 0;
		//
		// updataChartView();
		// updateDataTypeStatus();
		// updateBtnSelectState();
		// // 开启
		// handler.postDelayed(runnable, TIME);
		// }
		// }
		// }
		if (mProgressNumber > 0) {
			mProgressNumber--;
		}
		if (mProgressNumber == 0) {
			dismissProgressDialog();
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
		handler.removeCallbacks(runnable);
	}

	/**
	 * 切换仓库选项，更新UI
	 */
	private void updateBtnSelectState() {
		for (int i = 0; i < buttonList.size(); i++) {
			if (i == tmpBtnIndex) {
				((Button) buttonList.get(i)).setEnabled(false);
			} else {
				((Button) buttonList.get(i)).setEnabled(true);
			}
		}
	}

	private void updataChartView() {
		double[] CountsY1;// 张数数组
		double[] weightY2;// 重量数组
		// 设置图例文字
		if (tmpBtnIndex == 0) {
			sheetCountText.setText(R.string.steel_count);
			sheetWeightText.setText(R.string.steel_weight);
			CountsY1 = steelCounts;
			weightY2 = steelWeights;

		} else {
			sheetCountText.setText(R.string.margin_count);
			sheetWeightText.setText(R.string.margin_weight);
			CountsY1 = steelRckCounts;
			weightY2 = steelRckWeights;
		}
		// 取得重量Y轴最大值
		double MaxY = 0;
		for (double tmpY : weightY2) {
			if (MaxY < tmpY) {
				MaxY = tmpY;
			}
		}
		weightChart.removeAllViews();
		weightChart.addView(getLineView(weightY2, MaxY + 1, getResources()
				.getColor(R.color.orange)));

		// 取得张数Y轴最大值
		MaxY = 0;
		for (double tmpY : CountsY1) {
			if (MaxY < tmpY) {
				MaxY = tmpY;
			}
		}
		countChart.removeAllViews();
		countChart.addView(getLineView(CountsY1, MaxY + 1, getResources()
				.getColor(R.color.green)));
	}

	/**
	 * 返回折线图视图
	 * 
	 * @param ys
	 * @param maxY
	 *            设置y轴的最大值
	 * @param color
	 *            表示该组数据的图或线的颜色
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
		seriesRenderer.setYAxisMax(maxY + 1);// 设置y轴的最大值
		seriesRenderer.setXAxisMin(0);// 设置x轴起始值
		seriesRenderer.setXAxisMax(ys1.length + 1);// 设置x轴最大值

		// 缩放设置
		seriesRenderer.setZoomButtonsVisible(false);// 设置缩放按钮是否可见
		seriesRenderer.setExternalZoomEnabled(false);// 图表是否可以缩放设置
		seriesRenderer.setZoomEnabled(false, false);// 图表是否可以缩放设置

		// 图表移动设置
		seriesRenderer.setPanEnabled(true, false);// 图表是否可以移动
		// 图表手动放大缩小
		seriesRenderer.setInScroll(false);

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
			// seriesRenderer.addXTextLabel(i + 1, (i + 1) + "\n" +
			// getTypeString(i));
			// mLog.d("ys1.length== " + ys1.length);
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
		handler.removeCallbacks(runnable);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		handler.removeCallbacks(runnable);
	}

}

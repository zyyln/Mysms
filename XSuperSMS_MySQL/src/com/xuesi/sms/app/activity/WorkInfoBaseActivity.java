package com.xuesi.sms.app.activity;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;

import com.xuesi.mos.util.MosLog;
import com.xuesi.sms.R;
import com.xuesi.sms.SmsApplication;
import com.xuesi.sms.util.CalendarUtil;

public class WorkInfoBaseActivity extends SmsActivity {
	/** LOG */
	private final String LOG_TAG = WorkInfoBaseActivity.class.getSimpleName();
	private final MosLog mLog = MosLog.getInstance(LOG_TAG, "XUESI-SMS");

	/** 天周月年按钮 */
	public Button dayBtn, weekBtn, monthBtn, yearBtn;
	public List<Button> tabBtnList;

	public int currentSelectIndex = 0;
	public int tmpSelectIndex = 0;

	/** 开始时间,结束时间 */
	public String startDate, endDate;
	/**
	 * 作业面板通用接口<br>
	 * 具体url则在相应Activity方法中
	 */
	public static final String API_WORK = "API_WORK";
	/** 30秒刷新界面 */
	public final int TIME = 30 * 1000;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		SmsApplication.getInstance().addActivity(this);// 添加到Activity堆栈

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		// 进入界面首先开启更新
		handler.postDelayed(runnable, TIME);
	}

	@Override
	protected void initContentView() {
		// TODO Auto-generated method stub
		super.initContentView();

		dayBtn = (Button) findViewById(R.id.timebtn_btn_day);
		dayBtn.setOnClickListener(this);
		weekBtn = (Button) findViewById(R.id.timebtn_btn_week);
		weekBtn.setOnClickListener(this);
		monthBtn = (Button) findViewById(R.id.timebtn_btn_month);
		monthBtn.setOnClickListener(this);
		yearBtn = (Button) findViewById(R.id.timebtn_btn_year);
		yearBtn.setOnClickListener(this);

		tabBtnList = new ArrayList<Button>();
		tabBtnList.add(dayBtn);
		tabBtnList.add(weekBtn);
		tabBtnList.add(monthBtn);
		tabBtnList.add(yearBtn);

		updateDataTypeStatus();
	}

	Handler handler = new Handler();
	Runnable runnable = new Runnable() {
		@Override
		public void run() {
			// TODO Auto-generated method stub
			myNetwork(API_WORK);
		}
	};

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		super.onClick(v);
		switch (v.getId()) {
		case R.id.timebtn_btn_day:// 天按钮
			tmpSelectIndex = 0;
			if (tmpSelectIndex != currentSelectIndex) {
				handler.removeCallbacks(runnable);
				myNetwork(API_WORK, "");
			}
			break;
		case R.id.timebtn_btn_week:// 周按钮
			tmpSelectIndex = 1;
			if (tmpSelectIndex != currentSelectIndex) {
				handler.removeCallbacks(runnable);
				myNetwork(API_WORK, "");
			}
			break;
		case R.id.timebtn_btn_month:// 月按钮
			tmpSelectIndex = 2;
			if (tmpSelectIndex != currentSelectIndex) {
				handler.removeCallbacks(runnable);
				myNetwork(API_WORK, "");
			}
			break;
		case R.id.timebtn_btn_year:// 年按钮
			tmpSelectIndex = 3;
			if (tmpSelectIndex != currentSelectIndex) {
				handler.removeCallbacks(runnable);
				myNetwork(API_WORK, "");
			}
			break;
		default:
			break;
		}
	}

	@Override
	public void myNetwork(String url, String... txt) {
		// TODO Auto-generated method stub
		getDateParam();
	}

	/**
	 * 更新数据的状态(天，周，月，年)
	 */
	public void updateDataTypeStatus() {
		currentSelectIndex = tmpSelectIndex;
		for (int i = 0; i < tabBtnList.size(); i++) {
			if (i == currentSelectIndex) {
				tabBtnList.get(i).setEnabled(false);
			} else {
				tabBtnList.get(i).setEnabled(true);
			}
		}
	}

	/**
	 * 获取开始时间和结束时间
	 */
	public void getDateParam() {
		// 结束时间是此刻
		endDate = CalendarUtil.getTodayDate();
		// 开始时间是
		if (tmpSelectIndex == 0) {
			startDate = CalendarUtil.getTodayDate();
		} else if (tmpSelectIndex == 1) {
			startDate = CalendarUtil.getFirstDayOfWeek();
		} else if (tmpSelectIndex == 2) {
			startDate = CalendarUtil.getFirstDayOfMonth();
		} else {
			startDate = CalendarUtil.getFirstDayOfYear();
		}
	}

	public String getTypeString(int index, boolean isOne) {
		String string = null;
		String[] array = null;
		switch (tmpSelectIndex) {
		case 0: {
			array = new String[] { "初始值", "今日" };

			// 数组越界bug-0804

			string = array[index];
			// string = getString(R.string.today);
		}
			break;
		case 1: {
			if (isOne) {
				array = new String[] { "初始值", "周日" };
			} else {
				array = new String[] { "周\n日", "周\n一", "周\n二", "周\n三", "周\n四",
						"周\n五", "周\n六" };
			}
			string = array[index];
			// string = getString(R.string.dayBtn);
			// string = getString(R.string.weekBtn);
		}
			break;
		case 2: {
			if (isOne) {
				array = new String[] { "初始值", "一周", };
			} else {
				array = new String[] { "一\n\n周", "二\n周", "三\n周", "四\n周",
						"五\n周", "六\n周" };
			}
			string = array[index];
			// string = getString(R.string.weekBtn);
			// string = getString(R.string.monthBtn);
		}
			break;
		case 3: {
			if (isOne) {
				array = new String[] { "初始值", "一月", };
			} else {
				array = new String[] { "一\n\n月", "二\n月", "三\n月", "四\n月",
						"五\n月", "六\n月", "七\n月", "八\n月", "九\n月", "十\n月",
						"十\n一\n月", "十\n二\n月" };
			}
			string = array[index];
			// string = getString(R.string.monthBtn);
			// string = getString(R.string.yearBtn);
		}
			break;
		}
		return string;
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

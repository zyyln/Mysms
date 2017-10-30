package com.xuesi.sms.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xuesi.sms.R;

public class PercentView extends FrameLayout {

	/** 比例图 */
	private TextView percent_1_view;
	private TextView percent_2_view;
	/** 完工图例和值 */
	private TextView type_1_view;
	private TextView type_1_value;
	private TextView type_1_value1;
	/** 未完工图例和值 */
	private TextView type_2_view;
	private TextView type_2_value;
	private TextView type_2_value1;
	/** 上下文 */
	private Context mContext;

	public PercentView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.mContext = context;
		LayoutInflater.from(context).inflate(R.layout.layout_percent, this);

		percent_1_view = (TextView) findViewById(R.id.prcent_tv_fin);
		percent_2_view = (TextView) findViewById(R.id.prcent_tv_unf);

		type_1_view = (TextView) findViewById(R.id.percent_type_1_view);
		type_1_value = (TextView) findViewById(R.id.percent_type_1_value);
		type_1_value1 = (TextView) findViewById(R.id.percent_type_1_value1);

		type_2_view = (TextView) findViewById(R.id.percent_type_2_view);
		type_2_value = (TextView) findViewById(R.id.percent_type_2_value);
		type_2_value1 = (TextView) findViewById(R.id.percent_type_2_value1);
	}

	public void setTypeStyle(int color1, int str1, int color2, int str2) {
		type_1_view.setBackgroundResource(color1);
		type_1_value.setText(str1);
		type_2_view.setBackgroundResource(color2);
		type_2_value.setText(str2);
	}

	public void setTypeStyle(int color1, String str1, int color2, String str2) {
		type_1_view.setBackgroundResource(color1);
		type_1_value.setText(str1);
		type_2_view.setBackgroundResource(color2);
		type_2_value.setText(str2);
	}

	public void setPercent(double percent) {
		percent_1_view.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.MATCH_PARENT, (float) (1.0 - percent)));
		percent_2_view.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.MATCH_PARENT, (float) percent));
	}

	public void setValue(String str1_1, String str1_2, String str2_1, String str2_2) {
		percent_1_view.setText(str1_1);
		percent_2_view.setText(str1_2);
		type_1_value.setText(mContext.getString(R.string.workInfo_finish) + str2_1);
		type_2_value.setText(mContext.getString(R.string.workInfo_unfinish) + str2_2);
	}

	public void setValueForWhat(String str1_1, String str1_2, String str2_1, String str2_2) {
		type_1_value.setText(mContext.getString(R.string.workInfo_finish) + ": " + str1_1);
		type_2_value.setText(mContext.getString(R.string.workInfo_unfinish) + ": " + str1_2);
		type_1_value1.setText(str2_1);
		type_2_value1.setText(str2_2);
	}

}

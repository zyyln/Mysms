package com.xuesi.sms.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.xuesi.sms.R;

/**
 * 自定义仪表盘
 * 
 * @author chen_zhipeng
 * 
 */
public class DashBoardView extends FrameLayout {

	private ImageView dashPanelView; // 面板
	private ImageView needleView; // 指针
	private TextView percentageTV;// 百分比数值

	public DashBoardView(Context context, AttributeSet attrs) {
		super(context, attrs);
		LayoutInflater.from(context).inflate(R.layout.layout_dashboard, this);
		needleView = (ImageView) findViewById(R.id.dash_iv_needle);
		dashPanelView = (ImageView) findViewById(R.id.dash_iv_panel);
		percentageTV = (TextView) findViewById(R.id.dash_tv_percentage);
	}

	public void setPanelView(int imageID) {
		dashPanelView.setImageResource(imageID);
	}

	public void setNeedleView(int imageID) {
		needleView.setImageResource(imageID);
	}

	public void setPercentageTV(String msg) {
		percentageTV.setText(msg);
	}

	public void setDashBoradValue(double value) {

		// 默认旋转角度为112度
		float degree = (float) value * 112.0f;
		RotateAnimation animation = new RotateAnimation(0, degree, Animation.RELATIVE_TO_SELF, 0.5f,
				Animation.RELATIVE_TO_SELF, 0.5f);
		animation.setDuration(1000);
		animation.setFillAfter(true);
		needleView.startAnimation(animation);
	}

}

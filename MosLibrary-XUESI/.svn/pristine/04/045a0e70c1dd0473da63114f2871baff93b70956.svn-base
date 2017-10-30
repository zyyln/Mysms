/*
 * Screen.java
 * com.xuesi.mos.device
 *
 * Function： TODO 
 *
 *   ver     date      		author
 * ──────────────────────────────────
 *   		 2015-10-15 		lzq
 *
 * Copyright (c) 2015, NJXS All Rights Reserved.
 */

package com.xuesi.mos.device;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Window;
import android.view.WindowManager;

/**
 * ClassName:Screen
 * 
 * Function: TODO ADD FUNCTION
 * 
 * Reason: TODO ADD REASON
 * 
 * @author lzq
 * @version
 * @since Ver 1.1
 * @Date 2015-10-15
 */
public class Screen {
	private static Screen instance;
	/** 显示器高度(unit:Pixels) */
	private int screenHeight;
	/** 显示器宽度(unit:Pixels) */
	private int screenWidth;
	private int statusBarHeight;
	private int titleBarHeight;
	private float density;
	private int densityDpi;

	public static Screen getInstance() {
		if (instance == null)
			instance = new Screen();

		return instance;
	}

	public void init(Context mContext) {
		DisplayMetrics dm = new DisplayMetrics();
		WindowManager manager = (WindowManager) mContext.getSystemService("window");
		Display display = manager.getDefaultDisplay();
		display.getMetrics(dm);

		this.screenHeight = dm.heightPixels;
		this.screenWidth = dm.widthPixels;

		this.density = dm.density;
		this.densityDpi = dm.densityDpi;

		Log.i("Screen", this.screenWidth + " * " + this.screenHeight + " " + this.density + " " + this.densityDpi);
	}

	public void refreshScreenBar(Context mContext) {
		Rect rect = new Rect();
		Window win = ((Activity) mContext).getWindow();
		win.getDecorView().getWindowVisibleDisplayFrame(rect);
		this.statusBarHeight = rect.top;
		int contentViewTop = win.findViewById(16908290).getTop();
		this.titleBarHeight = (contentViewTop - this.statusBarHeight);
	}

	public int getScreenHeight() {
		return this.screenHeight;
	}

	public void setScreenHeight(int screenHeight) {
		this.screenHeight = screenHeight;
	}

	public int getScreenWidth() {
		return this.screenWidth;
	}

	public void setScreenWidth(int screenWidth) {
		this.screenWidth = screenWidth;
	}

	public int getStatusBarHeight() {
		return this.statusBarHeight;
	}

	public void setStatusBarHeight(int statusBarHeight) {
		this.statusBarHeight = statusBarHeight;
	}

	public int getTitleBarHeight() {
		return this.titleBarHeight;
	}

	public void setTitleBarHeight(int titleBarHeight) {
		this.titleBarHeight = titleBarHeight;
	}

	public float getDensity() {
		return this.density;
	}

	public void setDensity(float density) {
		this.density = density;
	}

	public int getDensityDpi() {
		return this.densityDpi;
	}

	public void setDensityDpi(int densityDpi) {
		this.densityDpi = densityDpi;
	}
}

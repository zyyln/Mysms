package com.xuesi.mos.app;

import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import android.app.Application;
import android.content.res.Configuration;

import com.xuesi.mos.util.MosLog;

/**
 * MosApplication 2015-10-15
 * 
 * @version 1.0.0
 * 
 */
public class MosApplication extends Application {
	private final String LOG_TAG = MosApplication.class.getSimpleName();
	private final MosLog mosLog = MosLog.getInstance(LOG_TAG, "MosHelper");

	private static MosApplication instance;

	public static MosApplication getInstance() {
		if (instance == null) {
			instance = new MosApplication();
		}
		return instance;
	}

	@Override
	public void onCreate() {
		super.onCreate();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// TODO Auto-generated method stub
		super.onConfigurationChanged(newConfig);
	}

	private List<Activity> activityList = new LinkedList<Activity>();

	public void addActivity(Activity activity) {
		activityList.add(activity);
	}

	public void exit() {
		for (Activity activity : activityList) {
			activity.finish();
		}
		System.exit(0);
	}

}

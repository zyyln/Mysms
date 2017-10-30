/*
 * MosDevice.java
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

import java.lang.reflect.Method;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Build;

/**
 * ClassName:MosDevice
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
public class MosDevice {

	private static MosDevice instance;
	private static final int PHONE_DEVICE = 0;
	private static final int TABLET_DEVICE = 1;
	private int deviceType;
	private String deviceId;
	private String deviceModel;
	private String deviceOsVersion;

	public static MosDevice getInstance() {
		if (instance == null)
			instance = new MosDevice();

		return instance;
	}

	public void init(Context mContext) {

	}

	private int getDeviceType(Context context) {
		int deviceType = 0;

		Boolean bIsTabletDevice = Boolean.valueOf(false);

		if (11 <= Build.VERSION.SDK_INT) {
			Configuration objCon = context.getResources().getConfiguration();
			try {
				Method objMethod = objCon.getClass().getMethod("isLayoutSizeAtLeast", new Class[] { Integer.TYPE });

				bIsTabletDevice = (Boolean) objMethod.invoke(objCon, new Object[] { Integer.valueOf(4) });

				if (!(bIsTabletDevice.booleanValue())) {
					deviceType = 1;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return deviceType;
	}

	public int getDeviceType() {
		return this.deviceType;
	}

	public void setDeviceType(int deviceType) {
		this.deviceType = deviceType;
	}

	public String getDeviceId() {
		return this.deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getDeviceModel() {
		return this.deviceModel;
	}

	public void setDeviceModel(String deviceModel) {
		this.deviceModel = deviceModel;
	}

	public String getDeviceOsVersion() {
		return this.deviceOsVersion;
	}

	public void setDeviceOsVersion(String deviceOsVersion) {
		this.deviceOsVersion = deviceOsVersion;
	}

}

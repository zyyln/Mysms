/*
 * MosPhone.java
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

import java.util.List;

import android.content.Context;
import android.telephony.CellLocation;

/**
 * ClassName:MosPhone
 * 
 * Function: TODO device info
 * 
 * Reason: TODO ADD REASON
 * 
 * @author lzq
 * @version
 * @since Ver 1.1
 * @Date 2015-10-15
 */
public class MosPhone {
	private static MosPhone instance;
	private int callState;
	private CellLocation cellLocation;
	private String phoneNum;
	private List neighboringCellInfo;
	private String networkCountryIso;
	private String networkOperator;
	private String networkOperatorName;
	private int networkType;
	private int phoneType;
	private String simCountryIso;
	private String simOperator;
	private String simOperatorName;
	private String simSerialNumber;
	private int simState;
	private String subscriberId;

	public static MosPhone getInstance() {
		if (instance == null)
			instance = new MosPhone();
		return instance;
	}

	public void init(Context mContext) {
	}

	public int getCallState() {
		return this.callState;
	}

	public void setCallState(int callState) {
		this.callState = callState;
	}

	public CellLocation getCellLocation() {
		return this.cellLocation;
	}

	public void setCellLocation(CellLocation cellLocation) {
		this.cellLocation = cellLocation;
	}

	public String getPhoneNum() {
		return this.phoneNum;
	}

	public void setPhoneNum(String phoneNum) {
		this.phoneNum = phoneNum;
	}

	public List getNeighboringCellInfo() {
		return this.neighboringCellInfo;
	}

	public void setNeighboringCellInfo(List neighboringCellInfo) {
		this.neighboringCellInfo = neighboringCellInfo;
	}

	public String getNetworkCountryIso() {
		return this.networkCountryIso;
	}

	public void setNetworkCountryIso(String networkCountryIso) {
		this.networkCountryIso = networkCountryIso;
	}

	public String getNetworkOperator() {
		return this.networkOperator;
	}

	public void setNetworkOperator(String networkOperator) {
		this.networkOperator = networkOperator;
	}

	public String getNetworkOperatorName() {
		return this.networkOperatorName;
	}

	public void setNetworkOperatorName(String networkOperatorName) {
		this.networkOperatorName = networkOperatorName;
	}

	public int getNetworkType() {
		return this.networkType;
	}

	public void setNetworkType(int networkType) {
		this.networkType = networkType;
	}

	public int getPhoneType() {
		return this.phoneType;
	}

	public void setPhoneType(int phoneType) {
		this.phoneType = phoneType;
	}

	public String getSimCountryIso() {
		return this.simCountryIso;
	}

	public void setSimCountryIso(String simCountryIso) {
		this.simCountryIso = simCountryIso;
	}

	public String getSimOperator() {
		return this.simOperator;
	}

	public void setSimOperator(String simOperator) {
		this.simOperator = simOperator;
	}

	public String getSimOperatorName() {
		return this.simOperatorName;
	}

	public void setSimOperatorName(String simOperatorName) {
		this.simOperatorName = simOperatorName;
	}

	public String getSimSerialNumber() {
		return this.simSerialNumber;
	}

	public void setSimSerialNumber(String simSerialNumber) {
		this.simSerialNumber = simSerialNumber;
	}

	public int getSimState() {
		return this.simState;
	}

	public void setSimState(int simState) {
		this.simState = simState;
	}

	public String getSubscriberId() {
		return this.subscriberId;
	}

	public void setSubscriberId(String subscriberId) {
		this.subscriberId = subscriberId;
	}

}

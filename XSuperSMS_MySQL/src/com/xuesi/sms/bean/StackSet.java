package com.xuesi.sms.bean;

import java.util.ArrayList;
import java.util.List;

public class StackSet extends BaseModel {
	public class StackSetSegments {
		private int id;// 序号
		private String SEGMENT;// 字段名
		private String DESCRIPTION;// 字段说明
		private int ISSELECT;// 0:没选；1：选择
		private int ISMUST;// 0：否；1：是
		private String REMARK;// 字段备注
		private String MySELECT;// 我的选择

		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}

		public String getSEGMENT() {
			return SEGMENT;
		}

		public void setSEGMENT(String sEGMENT) {
			SEGMENT = sEGMENT;
		}

		public String getDESCRIPTION() {
			return DESCRIPTION;
		}

		public void setDESCRIPTION(String dESCRIPTION) {
			DESCRIPTION = dESCRIPTION;
		}

		public int getISSELECT() {
			return ISSELECT;
		}

		public void setISSELECT(int iSSELECT) {
			ISSELECT = iSSELECT;
		}

		public int getISMUST() {
			return ISMUST;
		}

		public void setISMUST(int iSMUST) {
			ISMUST = iSMUST;
		}

		public String getREMARK() {
			return REMARK;
		}

		public void setREMARK(String rEMARK) {
			REMARK = rEMARK;
		}

		public String getMySELECT() {
			return MySELECT;
		}

		public void setMySELECT(String mySELECT) {
			MySELECT = mySELECT;
		}
	}

	private List<StackSetSegments> list;
	private StackSetSegments setInfo;

	public List<StackSetSegments> getStackSetSegments() {
		return list;
	}

	public List<String> getSelectSegments() {
		List<String> setList = new ArrayList<String>();
		for (int i = 0; i < list.size(); i++) {
			if ("1".equals(list.get(i).getMySELECT())) {
				setList.add(list.get(i).getMySELECT());
			}
		}
		return setList;
	}
}

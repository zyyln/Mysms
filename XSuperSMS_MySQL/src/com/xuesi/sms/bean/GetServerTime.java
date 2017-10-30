package com.xuesi.sms.bean;

import java.util.List;

public class GetServerTime extends BaseModel {

	public class ListClass {
		private String sysdate;// 系统时间

		public String getSysdate() {
			return sysdate;
		}
	}

	private List<ListClass> list;

	public List<ListClass> getList() {
		return list;
	}

}

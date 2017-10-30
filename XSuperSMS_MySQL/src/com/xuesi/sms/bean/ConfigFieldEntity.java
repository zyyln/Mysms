package com.xuesi.sms.bean;

import java.util.List;

public class ConfigFieldEntity extends BaseModel {

	private List<Segment> List;

	public List<Segment> getList() {
		return List;
	}

	public class Segment {
		private String name;
		private String val;
		private String Dtime;
		private String value;

		public String getValue() {
			return value;
		}

		public void setValue(String value) {
			this.value = value;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getVal() {
			return val;
		}

		public void setVal(String val) {
			this.val = val;
		}

		public String getDtime() {
			return Dtime;
		}

		public void setDtime(String dtime) {
			Dtime = dtime;
		}

	}
}

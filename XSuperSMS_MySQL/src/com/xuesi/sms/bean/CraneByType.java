package com.xuesi.sms.bean;

import java.util.List;

/**
 * 获取行车信息解析体
 * 
 * @author XS-PC014
 * 
 */
public class CraneByType extends BaseModel {

	public class ListClass {
		private String DCODEID;
		private String DNAME;

		public String getDCODEID() {
			return DCODEID;
		}

		public void setDCODEID(String dCODEID) {
			DCODEID = dCODEID;
		}

		public String getDNAME() {
			return DNAME;
		}

		public void setDNAME(String dNAME) {
			DNAME = dNAME;
		}

		// ----
		private boolean selected = false;// 是否选中

		public boolean isSelected() {
			return selected;
		}

		public void setSelected(boolean selected) {
			this.selected = selected;
		}
	}

	private List<ListClass> list;

	public List<ListClass> getList() {
		return list;
	}

}

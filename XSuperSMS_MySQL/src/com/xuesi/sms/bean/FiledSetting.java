package com.xuesi.sms.bean;

import java.util.List;

public class FiledSetting extends BaseVo {
	private List<FiledEntity> list;

	public List<FiledEntity> getList() {
		return list;
	}

	public class FiledEntity {
		private String ID;// 序号（唯一性）
		private String SEGMENT;// 字段
		private String SEGMENTNAME;// 字段名称
		private String DESCRIPTION;// 字段说明
		private String LISTNO;// 表中的列序号
		private String ISMUST;// 是否必须
		private String REMARK;// 字段备注
		private String MyLISTNO;// 本厂的导出字段配置序号

		private int posId;// 位置
		private String etTxt;// EditText

		public int getPosId() {
			return posId;
		}

		public void setPosId(int posId) {
			this.posId = posId;
		}

		public String getEtTxt() {
			return etTxt;
		}

		public void setEtTxt(String etTxt) {
			this.etTxt = etTxt;
		}

		public String getID() {
			return ID;
		}

		public void setID(String iD) {
			ID = iD;
		}

		public String getSEGMENT() {
			return SEGMENT;
		}

		public void setSEGMENT(String sEGMENT) {
			SEGMENT = sEGMENT;
		}

		public String getSEGMENTNAME() {
			return SEGMENTNAME;
		}

		public void setSEGMENTNAME(String sEGMENTNAME) {
			SEGMENTNAME = sEGMENTNAME;
		}

		public String getDESCRIPTION() {
			return DESCRIPTION;
		}

		public void setDESCRIPTION(String dESCRIPTION) {
			DESCRIPTION = dESCRIPTION;
		}

		public String getLISTNO() {
			return LISTNO;
		}

		public void setLISTNO(String lISTNO) {
			LISTNO = lISTNO;
		}

		public String getISMUST() {
			return ISMUST;
		}

		public void setISMUST(String iSMUST) {
			ISMUST = iSMUST;
		}

		public String getREMARK() {
			return REMARK;
		}

		public void setREMARK(String rEMARK) {
			REMARK = rEMARK;
		}

		public String getMyLISTNO() {
			return MyLISTNO;
		}

		public void setMyLISTNO(String myLISTNO) {
			MyLISTNO = myLISTNO;
		}
	}
}

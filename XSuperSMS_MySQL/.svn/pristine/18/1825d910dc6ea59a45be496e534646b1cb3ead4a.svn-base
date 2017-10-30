package com.xuesi.sms.bean;

import java.util.List;

import com.xuesi.sms.SmsConfig;
import com.xuesi.sms.util.SmsUtil;

/**
 * 根据条形码获取钢板信息-入库<br>
 * 本类字段合并到{@link Sheet}<br>
 * 待删除
 * 
 * @author XS-PC014
 * 
 */
public class SheetByCode extends BaseModel {

	public class ListClass {
		private String ID;// 钢板编码
		private float LENGTH;
		private float WIDTH;
		private String MATERIAL;
		private float THICKNESS;
		private String PROJECTID;

		public String getID() {
			return ID;
		}

		public void setID(String iD) {
			ID = iD;
		}

		public float getLENGTH() {
			if (LENGTH != 0) {

				return Float.parseFloat(SmsUtil.DecimalFormat(LENGTH,
						SmsConfig.dotNum));
			} else {
				return LENGTH;
			}

		}

		public void setLENGTH(float lENGTH) {
			LENGTH = lENGTH;
		}

		public float getWIDTH() {
			return Float.parseFloat(SmsUtil.DecimalFormat(WIDTH,
					SmsConfig.dotNum));
		}

		public void setWIDTH(float wIDTH) {
			WIDTH = wIDTH;
		}

		public String getMATERIAL() {
			return MATERIAL;
		}

		public void setMATERIAL(String mATERIAL) {
			MATERIAL = mATERIAL;
		}

		public float getTHICKNESS() {
			if (THICKNESS != 0) {
				return Float.parseFloat(SmsUtil.DecimalFormat(THICKNESS,
						SmsConfig.dotNum));
			} else {
				return THICKNESS;
			}
		}

		public void setTHICKNESS(float tHICKNESS) {
			THICKNESS = tHICKNESS;
		}

		public String getPROJECTID() {
			return PROJECTID;
		}

		public void setPROJECTID(String pROJECTID) {
			PROJECTID = pROJECTID;
		}

		// /api/order/GetOrderList--
		private String BILLSNO;// 采购单号
		private String METALBILLID;// 钢板编号
		private String BARCODE;// 条形码

		public String getBILLSNO() {
			return BILLSNO;
		}

		public String getMETALBILLID() {
			return METALBILLID;
		}

		public String getBARCODE() {
			return BARCODE;
		}

		// /api/pad/GetPADRemnantList----余料列表
		private String REMNAME;// 余料名称
		private String PLATELENGTH;// 长"},
		private String PLATEHEIGHT;// 宽"},

		public String getREMNAME() {
			return REMNAME;
		}

		public String getPLATELENGTH() {
			if (PLATELENGTH != null && !PLATELENGTH.equals("0")) {

				return SmsUtil.DecimalFormat(PLATELENGTH, SmsConfig.dotNum);
			} else {
				return PLATELENGTH;
			}
		}

		public String getPLATEHEIGHT() {
			if (PLATEHEIGHT != null && !PLATEHEIGHT.equals("0")) {
				return SmsUtil.DecimalFormat(PLATEHEIGHT, SmsConfig.dotNum);
			} else {
				return PLATEHEIGHT;
			}
		}

		private boolean recommend = false; // 符合条件的钢板(true:是, false:否)
		private boolean selcted = false;// 选中的钢板(true:是, false:否)
		private boolean checked = false;// 库位总览中钢板详细的多选属性

		public boolean isRecommend() {
			return recommend;
		}

		public void setRecommend(boolean recommend) {
			this.recommend = recommend;
		}

		public boolean isSelcted() {
			return selcted;
		}

		public void setSelcted(boolean selcted) {
			this.selcted = selcted;
		}

		public boolean isChecked() {
			return checked;
		}

		public void setChecked(boolean checked) {
			this.checked = checked;
		}
	}

	private List<ListClass> list;

	public List<ListClass> getList() {
		return list;
	}

	/**
	 * 判断两个对象材质、长宽厚属性是否一致
	 */
	public static boolean equalsSheetInfo(ListClass curSheet, ListClass preSheet) {
		if (curSheet.getMATERIAL().equals(preSheet.getMATERIAL())) {
			if (curSheet.getTHICKNESS() == preSheet.getTHICKNESS()) {
				if (curSheet.getLENGTH() == preSheet.getLENGTH()) {
					if (curSheet.getWIDTH() == preSheet.getWIDTH()) {
						return true;
					}
				}
			}
		}
		return false;
	}

}

package com.xuesi.sms.bean;

import java.io.Serializable;
import java.util.List;

import com.xuesi.sms.ServerApi;
import com.xuesi.sms.SmsConfig;
import com.xuesi.sms.util.SmsUtil;

/**
 * {@link ServerApi}<br>
 * 所有返回垛位数据的接口: <br>
 * API_GET_STACKINFO = /api/StoreStackMng/getStackInfo <br>
 * API_GETSTACKINFONAME = /api/pad/getStackInfoName <br>
 * API_GET_STOCK_CKECK_LIST = "/api/pad/getStackNoPD" <br>
 * API_GETSHEETNUM_INSTACK = /api/pad/GetSheetNumInSTACK <br>
 * API_GETSHEETNUMINSTACKRUST = /api/pad/GetSheetNumInSTACKRust <br>
 * 备注： 接口字段重复，待整合优化 <br>
 * 
 * @author XS-PC014
 * 
 */
public class Stack extends BaseVo {

	public class ListClass implements Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		private String STACKNO;// 货位ID
		private String STACKNAME;// 货位名称
		private String MAXHEIGHT;// 最大高度
		private float sumAmount;// 当前张数
		private float sumThickness;// 当前厚度(垛位当前高度)
		private String REMARK;// 备注

		public String getSTACKNO() {
			return STACKNO;
		}

		public void setSTACKNO(String sTACKNO) {
			STACKNO = sTACKNO;
		}

		public String getSTACKNAME() {
			return STACKNAME;
		}

		public void setSTACKNAME(String sTACKNAME) {
			STACKNAME = sTACKNAME;
		}

		public String getMAXHEIGHT() {
			if (MAXHEIGHT != null && !MAXHEIGHT.equals("0")) {
				return SmsUtil.DecimalFormat(MAXHEIGHT, SmsConfig.dotNum);
			} else {
				return MAXHEIGHT;
			}
		}

		public void setMAXHEIGHT(String mAXHEIGHT) {
			MAXHEIGHT = mAXHEIGHT;
		}

		public float getSumAmount() {
			return sumAmount;
		}

		public void setSumAmount(float sumAmount) {
			this.sumAmount = sumAmount;
		}

		public float getSumThickness() {
			if (sumThickness != 0) {
				return Float.parseFloat(SmsUtil.DecimalFormat(sumThickness,
						SmsConfig.dotNum));
			} else {
				return sumThickness;
			}
		}

		public void setSumThickness(float sumThickness) {
			this.sumThickness = sumThickness;
		}

		public String getREMARK() {
			return REMARK;
		}

		public void setREMARK(String rEMARK) {
			REMARK = rEMARK;
		}

		// API_GETSHEETNUMINSTACKRUST = getUrl() +
		// "/api/pad/GetSheetNumInSTACKRust";
		// API_GETSHEETNUM_INSTACK = getUrl() +
		// "/api/pad/GetSheetNumInSTACK";
		private String STOREHOUSEID;// 库房ID
		private String NAME;// 库房名称
		private float CURRENTHEIGHT;// 当前高度
		private String TYPE;// 库房类型
		private String QTYNUM;// 符合数量
		private String fuhe;// 符合

		public String getFuhe() {
			return fuhe;
		}

		public void setFuhe(String fuhe) {
			this.fuhe = fuhe;
		}

		public String getSTOREHOUSEID() {
			return STOREHOUSEID;
		}

		public String getNAME() {
			return NAME;
		}

		public float getCURRENTHEIGHT() {
			if (CURRENTHEIGHT != 0) {
				return Float.parseFloat(SmsUtil.DecimalFormat(CURRENTHEIGHT,
						SmsConfig.dotNum));
			} else {
				return CURRENTHEIGHT;
			}
		}

		public String getTYPE() {
			return TYPE;
		}

		public String getQTYNUM() {
			return QTYNUM;
		}

		/**
		 * 盘点画面"/api/pad/getStackNoPD"<br>
		 * 
		 */
		/** 库房ID */
		private String ID;
		// private String STACKNO;// 货位ID
		// private String sumThickness;// 当前高度
		// private String sumAmount;// 当前钢板数量
		// private String STACKNAME;// 货位名称
		/** desc:"盘点状态，-1：未开始盘点,0：未盘点，1：盘点无误,2：盘点有误 3：需要倒垛" */
		private int checkFlag;

		public String getID() {
			return ID;
		}

		public void setID(String iD) {
			ID = iD;
		}

		public int getCheckFlag() {
			return checkFlag;
		}

		public void setCheckFlag(int checkFlag) {
			this.checkFlag = checkFlag;
		}

		// 可选属性
		/** 是否为选中(true:选中, false:非选中) */
		private boolean selected = false;
		/** 是否选中为垛位推荐(true:推荐, false:非推荐) */
		private boolean recommend = false;
		/** 是否选中为目标垛位 */
		private boolean toSelect = false;

		public boolean isSelected() {
			return selected;
		}

		public void setSelected(boolean selected) {
			this.selected = selected;
		}

		public boolean isRecommend() {
			return recommend;
		}

		public void setRecommend(boolean recommend) {
			this.recommend = recommend;
		}

		public boolean isToSelect() {
			return toSelect;
		}

		public void setToSelect(boolean toSelect) {
			this.toSelect = toSelect;
		}

	}

	private List<ListClass> list;

	public List<ListClass> getList() {
		return list;
	}

	// /api/pad/getStackInfoName
	// /api/StoreStackMng/getStackInfo
	// /api/pad/GetSheetNumInSTACKRust
	// /api/pad/GetSheetNumInSTACK
	// 入库推荐垛位时返回两个集合
	private List<ListClass> unlist;

	public List<ListClass> getUnlist() {
		return unlist;
	}

	// -----数量数组
	public class TotalClass {

		// /api/StoreStackMng/getStackInfo
		// /api/pad/getStackInfoName
		private int stacknum;// 货位数量
		private int sheetnum;// 钢板数量

		public int getStacknum() {
			return stacknum;
		}

		public int getSheetnum() {
			return sheetnum;
		}

		// /api/pad/GetSheetNumInSTACKRust
		// /api/pad/GetSheetNumInSTACK
		// private int stacknum;// 符合查询条件的货位数
		private int housenum;// 符合查询条件的库房数
		private int total;// 符合查询条件的钢板总数

		public int getTotal() {
			return total;
		}

		public int getHousenum() {
			return housenum;
		}
	}

	private List<TotalClass> totalList;

	public List<TotalClass> getTotalList() {
		return totalList;
	}
	
	
	

}

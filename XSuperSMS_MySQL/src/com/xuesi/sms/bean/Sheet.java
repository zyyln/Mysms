package com.xuesi.sms.bean;

import java.io.Serializable;
import java.util.List;

import com.xuesi.sms.ServerApi;
import com.xuesi.sms.SmsConfig;
import com.xuesi.sms.util.SmsUtil;

/**
 * {@link ServerApi}<br>
 * 所有涉及钢板数据(包括整板和余料)的接口： <br>
 * API_GETBARCODE = /api/pad/getBarCode<br>
 * API_GETREMLIST = /api/pad/GetPADRemnantList<br>
 * API_GETGATHERLIST = "/api/pad/getGatherList"<br>
 * API_ALLSHEETLIST_BYSTACKNO = /api/pad/allSheetListByStackNo<br>
 * API_GET_WORKLIST_ANDINSTACK = /api/pad/getWorkListAndInStack<br>
 * API_GETORDERLIST = /api/order/GetOrderList<br>
 * API_GET_SHEETLIST_BYSTACKNO = /api/sheet/GetSheetListByStackNo<br>
 * 
 * @author XS-PC014
 * 
 */
public class Sheet extends BaseVo {
	/**
	 * 通用字段(钢板属性)
	 */
	public class ListClass implements Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private String billsNo;// 采购单号
		private String metalBillId;// 钢板编号
		private String SHEETNAME;// 钢板名称
		private String barcode;// 条形码
		private String length;// 长度
		private String width;// 宽度
		private String weight;// 重量
		private String thickness;// 厚度
		private String material;// 材质
		private String materialId;// 物料编码
		private String materialName;// 物料名称
		private String codeName;// 物料名称api/order/GetOrderList
		private String unit;// 单位
		private int amount;// 数量
		private int ItemNo;// 层数"},
		private int usedFlag;// 状态"},
		private String projectId;// 工程号"},
		private String assemblyId;// 分段号,部套号},
		private String ERPBatchNo;// 批次号"},
		private String certificateNo;// 船级社},
		private String checkOutId;// 提检号"},
		private String batchNo;// 炉批号"},
		private String type;// 应用类别"},
		private String inTime;// 入库时间"},
		private String refreshTime;// 更新时间"},
		private String remark;// 备注"},
		private String stackNo;// 货位号"},
		private String stackName;// 货位名称"},
		private String supplier;// 生产钢厂,供应商},
		private String sheetStack;// 原货位},
		private int isBarcode;// 钢板是否绑定}
		private String TRANFLAG;// 转库标志
		private String SHIPNAME;// 船级社名称
		private String PLANNO;// 计划编号
		private String PLATFLOW;// 钢板批次
		private String SAMPLEINFO;// 取样信息
		private String ISREM;// 是否余料
		private String STEELSPECIFICATION;// 型材规格
		private String DATA3;
		private String DATA4;
		private String DATA5;
		private String DATA6;
		private String DATA1;// 数据1
		private String DATA2;// 数据2
		private String ERP;
		private String millCode;// 分厂},

		private String selectNum;// 勾选顺序

		public String getSelectNum() {
			return selectNum;
		}

		public void setSelectNum(String selectNum) {
			this.selectNum = selectNum;
		}

		public String getERP() {
			return ERP;
		}

		public void setERP(String eRP) {
			ERP = eRP;
		}

		public String getSUPPLIERID() {
			return SUPPLIERID;
		}

		public void setSUPPLIERID(String sUPPLIERID) {
			SUPPLIERID = sUPPLIERID;
		}

		public void setHOUSETYPE(String hOUSETYPE) {
			HOUSETYPE = hOUSETYPE;
		}

		public void setNCID(String nCID) {
			NCID = nCID;
		}

		public void setORIGMETALBILLID(String oRIGMETALBILLID) {
			ORIGMETALBILLID = oRIGMETALBILLID;
		}

		public void setSUPPLIER(String sUPPLIER) {
			SUPPLIER = sUPPLIER;
		}

		public String getBillsNo() {
			return billsNo;
		}

		public void setBillsNo(String billsNo) {
			this.billsNo = billsNo;
		}

		public String getMetalBillId() {
			return metalBillId;
		}

		public void setMetalBillId(String metalBillId) {
			this.metalBillId = metalBillId;
		}

		public String getBarcode() {
			return barcode;
		}

		public void setBarcode(String barcode) {
			this.barcode = barcode;
		}

		public String getLength() {
			if (SmsUtil.checkString(length)) {
				return SmsUtil.DecimalFormat(length, SmsConfig.dotNum);
			} else {
				return length;
			}
		}

		public void setLength(String length) {
			this.length = length;
		}

		public String getWidth() {
			if (SmsUtil.checkString(width)) {
				return SmsUtil.DecimalFormat(width, SmsConfig.dotNum);
			} else {
				return width;
			}
		}

		public void setWidth(String width) {
			this.width = width;
		}

		public String getWeight() {
			if (SmsUtil.checkString(weight)) {
				return SmsUtil.DecimalFormat(weight, SmsConfig.dotNum);
			} else {
				weight="0";
				return weight;
			}
		}

		public void setWeight(String weight) {
			this.weight = weight;
		}

		public String getThickness() {
			if (SmsUtil.checkString(thickness)) {
				return SmsUtil.DecimalFormat(thickness, SmsConfig.dotNum);
			} else {
				return thickness;
			}
		}

		public void setThickness(String thickness) {
			this.thickness = thickness;
		}

		public String getMaterial() {
			return material;
		}

		public void setMaterial(String material) {
			this.material = material;
		}

		public String getMaterialId() {
			return materialId;
		}

		public void setMaterialId(String materialId) {
			this.materialId = materialId;
		}

		public String getMaterialName() {
			return materialName;
		}

		public void setMaterialName(String materialName) {
			this.materialName = materialName;
		}

		public String getCodeName() {
			return codeName;
		}

		public void setCodeName(String codeName) {
			this.codeName = codeName;
		}

		public String getUnit() {
			return unit;
		}

		public void setUnit(String unit) {
			this.unit = unit;
		}

		public int getAmount() {
			return amount;
		}

		public void setAmount(int amount) {
			this.amount = amount;
		}

		public String getStackNo() {
			return stackNo;
		}

		public void setStackNo(String stackNo) {
			this.stackNo = stackNo;
		}

		public String getStackName() {
			return stackName;
		}

		public void setStackName(String stackName) {
			this.stackName = stackName;
		}

		public int getItemNo() {
			return ItemNo;
		}

		public void setItemNo(int itemNo) {
			ItemNo = itemNo;
		}

		public int getUsedFlag() {
			return usedFlag;
		}

		public void setUsedFlag(int usedFlag) {
			this.usedFlag = usedFlag;
		}

		public String getProjectId() {
			return projectId;
		}

		public void setProjectId(String projectId) {
			this.projectId = projectId;
		}

		public String getAssemblyId() {
			return assemblyId;
		}

		public void setAssemblyId(String assemblyId) {
			this.assemblyId = assemblyId;
		}

		public String getERPBatchNo() {
			return ERPBatchNo;
		}

		public void setERPBatchNo(String eRPBatchNo) {
			ERPBatchNo = eRPBatchNo;
		}

		public String getCertificateNo() {
			return certificateNo;
		}

		public void setCertificateNo(String certificateNo) {
			this.certificateNo = certificateNo;
		}

		public String getCheckOutId() {
			return checkOutId;
		}

		public void setCheckOutId(String checkOutId) {
			this.checkOutId = checkOutId;
		}

		public String getBatchNo() {
			return batchNo;
		}

		public void setBatchNo(String batchNo) {
			this.batchNo = batchNo;
		}

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public String getInTime() {
			return inTime;
		}

		public void setInTime(String inTime) {
			this.inTime = inTime;
		}

		public String getRefreshTime() {
			return refreshTime;
		}

		public void setRefreshTime(String refreshTime) {
			this.refreshTime = refreshTime;
		}

		public String getRemark() {
			return remark;
		}

		public void setRemark(String remark) {
			this.remark = remark;
		}

		public String getSupplier() {
			return supplier;
		}

		public void setSupplier(String supplier) {
			this.supplier = supplier;
		}

		public String getSheetStack() {
			return sheetStack;
		}

		public void setSheetStack(String sheetStack) {
			this.sheetStack = sheetStack;
		}

		public int getIsBarcode() {
			return isBarcode;
		}

		public void setIsBarcode(int isBarcode) {
			this.isBarcode = isBarcode;
		}

		public String getMillCode() {
			return millCode;
		}

		public void setMillCode(String millCode) {
			this.millCode = millCode;
		}

		public String getTRANFLAG() {
			return TRANFLAG;
		}

		public void setTRANFLAG(String tRANFLAG) {
			TRANFLAG = tRANFLAG;
		}

		public String getSHIPNAME() {
			return SHIPNAME;
		}

		public void setSHIPNAME(String sHIPNAME) {
			SHIPNAME = sHIPNAME;
		}

		public String getPLANNO() {
			return PLANNO;
		}

		public void setPLANNO(String pLANNO) {
			PLANNO = pLANNO;
		}

		public String getPLATFLOW() {
			return PLATFLOW;
		}

		public void setPLATFLOW(String pLATFLOW) {
			PLATFLOW = pLATFLOW;
		}

		public String getSAMPLEINFO() {
			return SAMPLEINFO;
		}

		public void setSAMPLEINFO(String sAMPLEINFO) {
			SAMPLEINFO = sAMPLEINFO;
		}

		public String getDATA1() {
			return DATA1;
		}

		public void setDATA1(String dATA1) {
			DATA1 = dATA1;
		}

		public String getDATA2() {
			return DATA2;
		}

		public void setDATA2(String dATA2) {
			DATA2 = dATA2;
		}

		public String getSHEETNAME() {
			return SHEETNAME;
		}

		public void setSHEETNAME(String sHEETNAME) {
			SHEETNAME = sHEETNAME;
		}

		public String getISREM() {
			return ISREM;
		}

		public void setISREM(String iSREM) {
			ISREM = iSREM;
		}

		public String getSTEELSPECIFICATION() {
			return STEELSPECIFICATION;
		}

		public void setSTEELSPECIFICATION(String sTEELSPECIFICATION) {
			STEELSPECIFICATION = sTEELSPECIFICATION;
		}

		public String getDATA3() {
			return DATA3;
		}

		public void setDATA3(String dATA3) {
			DATA3 = dATA3;
		}

		public String getDATA4() {
			return DATA4;
		}

		public void setDATA4(String dATA4) {
			DATA4 = dATA4;
		}

		public String getDATA5() {
			return DATA5;
		}

		public void setDATA5(String dATA5) {
			DATA5 = dATA5;
		}

		public String getDATA6() {
			return DATA6;
		}

		public void setDATA6(String dATA6) {
			DATA6 = dATA6;
		}

		// API_GET_SHEETLIST_BYSTACKNO = /api/sheet/GetSheetListByStackNo 单独字段
		// API_ALLSHEETLIST_BYSTACKNO = /api/pad/allSheetListByStackNo 单独字段
		private int ISRUST;// 锈蚀"},
		private String HOUSETYPE;// 库房类型"},
		private String SUPPLIERID;// 生产钢厂

		public int getISRUST() {
			return ISRUST;
		}

		public void setISRUST(int iSRUST) {
			ISRUST = iSRUST;
		}

		public String getHOUSETYPE() {
			return HOUSETYPE;
		}

		// API_GETREMLIST = /api/pad/GetPADRemnantList 单独字段
		private float plateLength;// 余料长"},
		private float plateHeight;// 余料宽"},

		public float getPlateLength() {
			return plateLength;
		}

		public void setPlateLength(float plateLength) {
			this.plateLength = plateLength;
		}

		public float getPlateHeight() {
			return plateHeight;
		}

		public void setPlateHeight(float plateHeight) {
			this.plateHeight = plateHeight;
		}

		// /api/pad/getWorkListAndInStack 字段
		// private int serialNo;// 序号，用于盘点明细显示和排序(本地添加属性,先延用ItemNo)
		// "/api/pad/getGatherList" 单独字段
		/** 0:正常, 1：有误(钢板不存在), 2：倒垛(层号不符),3:倒垛(钢板规格和垛位定义规则不一致) */
		private int sheetStatus;

		public int getSheetStatus() {
			return sheetStatus;
		}

		public void setSheetStatus(int sheetStatus) {
			this.sheetStatus = sheetStatus;
		}

		// API_GETGATHERLIST = "/api/pad/getGatherList" 单独字段
		private String NCID;// 程序号"},
		private String ORIGMETALBILLID;// 母版ID"},
		private String SUPPLIER;// 生产钢厂,供应商},

		public String getNCID() {
			return NCID;
		}

		public String getORIGMETALBILLID() {
			return ORIGMETALBILLID;
		}

		public String getSUPPLIER() {
			return SUPPLIER;
		}

		// 本地添加属性
		/** 是否为单选选中 */
		private boolean selected = false;
		/** 是否为多选选中(库位总览中钢板详细_多余可用selected代替) */
		private boolean checked = false;
		/** 是否为推荐属性 */
		private boolean recommend = false;

		public boolean isSelected() {
			return selected;
		}

		public void setSelected(boolean selected) {
			this.selected = selected;
		}

		public boolean isChecked() {
			return checked;
		}

		public void setChecked(boolean checked) {
			this.checked = checked;
		}

		public boolean isRecommend() {
			return recommend;
		}

		public void setRecommend(boolean recommend) {
			this.recommend = recommend;
		}

	}

	private List<ListClass> list;// 符合条件的钢板
	private List<ListClass> alllist;// 不符合条件的钢板

	public List<ListClass> getList() {
		return list;
	}

	public List<ListClass> getAlllist() {
		return alllist;
	}

	/**
	 * 判断两个对象材质、长宽厚属性是否一致(现根据钢板同类型来判断)
	 */
	public static boolean equalsSheet(Sheet.ListClass curSheet,
			Sheet.ListClass preSheet) {
		if (curSheet.getMaterial().equals(preSheet.getMaterial())) {
			if (curSheet.getThickness() == preSheet.getThickness()) {
				if (curSheet.getLength() == preSheet.getLength()) {
					if (curSheet.getWidth() == preSheet.getWidth()) {
						return true;
					}
				}
			}
		}
		return false;
	}

}
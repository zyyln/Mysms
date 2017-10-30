package com.xuesi.sms.bean;

import java.util.List;

import com.xuesi.sms.ServerApi;
import com.xuesi.sms.SmsConfig;
import com.xuesi.sms.util.SmsUtil;

/**
 * {@link ServerApi}<br>
 * 所有涉及接口:<br>
 * /api/Grant/getGrantDetailInfo<br>
 * 
 * 获取发料单详细
 * 
 * @author XS-PC014
 * 
 */
public class OrderDetail extends BaseModel {

	public class ListClass {
		private String GRANTID;// 发料单号
		private String TYPE;// 请料类型
		private String PROJECTID;// [实请]工程
		private String ASSEMBLYID;// [实请]分段
		private String USEPROJNO;// [实发]工程
		private String USEBLOCKNO;// [实发]分段
		private String USESTACKNO;// 堆垛编号(发)
		private String USESTACKITEM;// 堆垛序号(发)
		private String NCCODE;// 程序编号
		private String USEFLOWNO;// 钢板编号(发)
		private String BARCODE;// 条形码
		private String SHEETNAME;// 钢板名称
		private String MATERIAL;// 钢板材质
		private String THICKNESS;// 钢板厚度
		private String LENGTH;// 钢板长度
		private String WIDTH;// 钢板宽度
		private String SHIPNAME;// 船级社
		private String SUPPLIERID;// 供应商
		private String PLANNO;// 计划编号
		private String BATCHNO;// 炉批号
		private String DATA1;// 数据1
		private String DATA2;// 数据2
		private String DATA3;// 数据3
		private String DATA4;// 数据4
		private String STEELSPECIFICATION;// 型材规格,
		private String SHEETWEIGHT;// 钢板重量
		private String REMARK;// 用料备注
		private String WRITEREMARK;// 请料备注
		private String FLAG;// 发料状态
		private String FLOWNO;// 流水号
		private String SHEETTYPE;// 钢板类型
		private String SHEETCODE;// 套料的钢板/余料名
		private int PRIORITY;// 优先级
		private String ERPBATCHNO;// 批次号
		private String MATERIALID;// 物料编码
		private String GRANTSTACK;// 二级库仓库
		private String REFRESHTIME;// 发放发料时间

		public String getGRANTID() {
			return GRANTID;
		}

		public void setGRANTID(String gRANTID) {
			GRANTID = gRANTID;
		}

		public String getTYPE() {
			return TYPE;
		}

		public void setTYPE(String tYPE) {
			TYPE = tYPE;
		}

		public String getPROJECTID() {
			return PROJECTID;
		}

		public void setPROJECTID(String pROJECTID) {
			PROJECTID = pROJECTID;
		}

		public String getASSEMBLYID() {
			return ASSEMBLYID;
		}

		public void setASSEMBLYID(String aSSEMBLYID) {
			ASSEMBLYID = aSSEMBLYID;
		}

		public String getUSEPROJNO() {
			return USEPROJNO;
		}

		public void setUSEPROJNO(String uSEPROJNO) {
			USEPROJNO = uSEPROJNO;
		}

		public String getUSEBLOCKNO() {
			return USEBLOCKNO;
		}

		public void setUSEBLOCKNO(String uSEBLOCKNO) {
			USEBLOCKNO = uSEBLOCKNO;
		}

		public String getUSESTACKNO() {
			return USESTACKNO;
		}

		public String getBARCODE() {
			return BARCODE;
		}

		public String getSHEETNAME() {
			return SHEETNAME;
		}

		public void setSHEETNAME(String sHEETNAME) {
			SHEETNAME = sHEETNAME;
		}

		public void setBARCODE(String bARCODE) {
			BARCODE = bARCODE;
		}

		public void setUSESTACKNO(String uSESTACKNO) {
			USESTACKNO = uSESTACKNO;
		}

		public String getUSESTACKITEM() {
			return USESTACKITEM;
		}

		public void setUSESTACKITEM(String uSESTACKITEM) {
			USESTACKITEM = uSESTACKITEM;
		}

		public String getNCCODE() {
			return NCCODE;
		}

		public void setNCCODE(String nCCODE) {
			NCCODE = nCCODE;
		}

		public String getUSEFLOWNO() {
			return USEFLOWNO;
		}

		public void setUSEFLOWNO(String uSEFLOWNO) {
			USEFLOWNO = uSEFLOWNO;
		}

		public String getMATERIAL() {
			return MATERIAL;
		}

		public void setMATERIAL(String mATERIAL) {
			MATERIAL = mATERIAL;
		}

		public String getTHICKNESS() {
			if (THICKNESS != null && !THICKNESS.equals("0")) {
				return SmsUtil.DecimalFormat(THICKNESS, SmsConfig.dotNum);
			} else {
				return THICKNESS;
			}
		}

		public void setTHICKNESS(String tHICKNESS) {
			THICKNESS = tHICKNESS;
		}

		public String getLENGTH() {
			if (LENGTH != null && !LENGTH.equals("0")) {
				return SmsUtil.DecimalFormat(LENGTH, SmsConfig.dotNum);
			} else {
				return LENGTH;
			}
		}

		public void setLENGTH(String lENGTH) {
			LENGTH = lENGTH;
		}

		public String getWIDTH() {
			if (WIDTH != null && !WIDTH.equals("0")) {
				return SmsUtil.DecimalFormat(WIDTH, SmsConfig.dotNum);
			} else {
				return WIDTH;
			}
		}

		public void setWIDTH(String wIDTH) {
			WIDTH = wIDTH;
		}

		public String getSHIPNAME() {
			return SHIPNAME;
		}

		public void setSHIPNAME(String sHIPNAME) {
			SHIPNAME = sHIPNAME;
		}

		public String getSUPPLIERID() {
			return SUPPLIERID;
		}

		public void setSUPPLIERID(String sUPPLIERID) {
			SUPPLIERID = sUPPLIERID;
		}

		public String getPLANNO() {
			return PLANNO;
		}

		public void setPLANNO(String pLANNO) {
			PLANNO = pLANNO;
		}

		public String getBATCHNO() {
			return BATCHNO;
		}

		public void setBATCHNO(String bATCHNO) {
			BATCHNO = bATCHNO;
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

		public String getSTEELSPECIFICATION() {
			return STEELSPECIFICATION;
		}

		public void setSTEELSPECIFICATION(String sTEELSPECIFICATION) {
			STEELSPECIFICATION = sTEELSPECIFICATION;
		}

		public String getSHEETWEIGHT() {
			return SHEETWEIGHT;
		}

		public void setSHEETWEIGHT(String sHEETWEIGHT) {
			SHEETWEIGHT = sHEETWEIGHT;
		}

		public String getREMARK() {
			return REMARK;
		}

		public void setREMARK(String rEMARK) {
			REMARK = rEMARK;
		}

		public String getWRITEREMARK() {
			return WRITEREMARK;
		}

		public void setWRITEREMARK(String wRITEREMARK) {
			WRITEREMARK = wRITEREMARK;
		}

		public String getSHEETTYPE() {
			return SHEETTYPE;
		}

		public void setSHEETTYPE(String sHEETTYPE) {
			SHEETTYPE = sHEETTYPE;
		}

		public String getFLAG() {
			return FLAG;
		}

		public void setFLAG(String fLAG) {
			FLAG = fLAG;
		}

		public String getFLOWNO() {
			return FLOWNO;
		}

		public void setFLOWNO(String fLOWNO) {
			FLOWNO = fLOWNO;
		}

		public String getSHEETCODE() {
			return SHEETCODE;
		}

		public void setSHEETCODE(String sHEETCODE) {
			SHEETCODE = sHEETCODE;
		}

		public int getPRIORITY() {
			return PRIORITY;
		}

		public void setPRIORITY(int pRIORITY) {
			PRIORITY = pRIORITY;
		}

		public String getERPBATCHNO() {
			return ERPBATCHNO;
		}

		public void setERPBATCHNO(String eRPBATCHNO) {
			ERPBATCHNO = eRPBATCHNO;
		}

		public String getMATERIALID() {
			return MATERIALID;
		}

		public void setMATERIALID(String mATERIALID) {
			MATERIALID = mATERIALID;
		}

		public String getGRANTSTACK() {
			return GRANTSTACK;
		}

		public void setGRANTSTACK(String gRANTSTACK) {
			GRANTSTACK = gRANTSTACK;
		}

		public String getREFRESHTIME() {
			return REFRESHTIME;
		}

		public void setREFRESHTIME(String rEFRESHTIME) {
			REFRESHTIME = rEFRESHTIME;
		}

		// 本地添加属性
		/** 是否选中 */
		private boolean selected = false;

		public boolean isSelected() {
			return selected;
		}

		public void setSelected(boolean selected) {
			this.selected = selected;
		}

	}

	private List<ListClass> grantDetailInfoList;

	public List<ListClass> getGrantDetailInfoList() {
		return grantDetailInfoList;
	}

	public class Count {
		private String recordCount;// 发料单号详细数量

		public String getRecordCount() {
			return recordCount;
		}
	}

	private List<Count> grantDetailInfoCount;

	public List<Count> getGrantDetailInfoCount() {
		return grantDetailInfoCount;
	}
}

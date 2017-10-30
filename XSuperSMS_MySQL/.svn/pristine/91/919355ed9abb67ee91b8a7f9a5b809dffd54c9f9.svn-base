package com.xuesi.sms.bean;

import java.util.List;

import com.xuesi.sms.SmsConfig;
import com.xuesi.sms.util.SmsUtil;

/**
 * 作业看板总数据接口解析体<br>
 * --/api/WorkBoard/getWbDataTotal 整合接口
 * 
 * @author XS-PC014
 * 
 */
public class WbTotalData extends BaseModel {

	/**
	 * 计划进展情况
	 */
	public class PlanQtyList {
		private String FinishQty;// 完成数量
		private String PlanQty;// 计划数据
		private String OverDueQty;// 逾期零件数
		private String unFinishQTY;// 未完工数量

		public String getFinishQty() {
			return FinishQty;
		}

		public String getPlanQty() {
			return PlanQty;
		}

		public String getOverDueQty() {
			return OverDueQty;
		}

		public String getUnFinishQTY() {
			return unFinishQTY;
		}

	}

	private List<PlanQtyList> pPlanQtyList;

	public List<PlanQtyList> getPlanQtyList() {
		return pPlanQtyList;
	}

	/**
	 * 钢板请料数
	 */
	public class ReqQtyList {
		private String RIDCount;// 钢板请料数
		private String SheetWeight;// 钢板请料重量

		public String getRIDCount() {
			return RIDCount;
		}

		public String getSheetWeight() {
			if (SheetWeight != null && !SheetWeight.equals("0")) {
				return SmsUtil.DecimalFormat(SheetWeight, SmsConfig.dotNum);
			} else {
				return SheetWeight;
			}
		}
	}

	private List<ReqQtyList> pReqQtyList;

	public List<ReqQtyList> getReqQtyList() {
		return pReqQtyList;
	}

	/**
	 * 钢板报损
	 */
	public class MBIDCW {
		private String PreWeight;
		private String MBIDCount;
		private String MBIDWeight;

		public String getPreWeight() {
			if (PreWeight != null && !PreWeight.equals("0")) {
				return SmsUtil.DecimalFormat(PreWeight, SmsConfig.dotNum);
			} else {
				return PreWeight;
			}
		}

		public String getMBIDCount() {
			return MBIDCount;
		}

		public String getMBIDWeight() {
			if (PreWeight != null && !PreWeight.equals("0")) {
				return SmsUtil.DecimalFormat(MBIDWeight, SmsConfig.dotNum);
			} else {
				return PreWeight;
			}
		}
	}

	private List<MBIDCW> pMBIDCW;

	public List<MBIDCW> getpMbidcws() {
		return pMBIDCW;
	}

	/**
	 * 钢板当前总重量
	 */
	public class TotalWeiList {
		private String TotalWeight;// 钢板当前总重量

		public String getTotalWeight() {
			if (TotalWeight != null && !TotalWeight.equals("0")) {
				return SmsUtil.DecimalFormat(TotalWeight, SmsConfig.dotNum);
			} else {
				return TotalWeight;
			}
		}
	}

	private List<TotalWeiList> pTotalWeiList;

	public List<TotalWeiList> getpTotalWeiLists() {
		return pTotalWeiList;
	}

	/**
	 * 零件报废
	 */
	public class pFSRate {
		private String FSRate;// 加工零件报废率
		private String sFSQty;// 加工零件报废数量

		public String getFSRate() {
			if (FSRate != null && !FSRate.equals("0")) {
				return SmsUtil.DecimalFormat(FSRate, SmsConfig.dotNum);
			} else {
				return FSRate;
			}
		}

		public String getsFSQty() {
			return sFSQty;
		}
	}

	private List<pFSRate> pFSRate;

	public List<pFSRate> getpFsRates() {
		return pFSRate;
	}

	/**
	 * 切割工时(小时)
	 */
	public class pCTime {
		private String CutTime;// 切割工时(小时)

		public String getCutTime() {
			if (CutTime != null && !CutTime.equals("0")) {
				return SmsUtil.DecimalFormat(CutTime, SmsConfig.dotNum);
			} else {
				return CutTime;
			}
		}
	}

	private List<pCTime> pCTime;

	public List<pCTime> getpCTimes() {
		return pCTime;
	}

	/**
	 * 钢板代用
	 */
	public class pOMBCount {
		private String OMBCount;// 钢板代用记录数

		public String getOMBCount() {
			return OMBCount;
		}
	}

	private List<pOMBCount> pOMBCount;

	public List<pOMBCount> getpOmbCounts() {
		return pOMBCount;
	}

	/**
	 * 套料情况
	 */
	public class pNumNested {
		private String Flag;// 未加工
		private String NumNested;// 套料数量

		public String getFlag() {
			return Flag;
		}

		public String getNumNested() {
			return NumNested;
		}
	}

	private List<pNumNested> pNumNested;

	public List<pNumNested> getpNUMNesteds() {
		return pNumNested;
	}

	/**
	 * 产品生产情况
	 */
	public class pAssembly {
		private String FinishQty;//
		private String UnFinishQty;//
		private String AssemblyName;//

		public String getFinishQty() {
			return FinishQty;
		}

		public String getUnFinishQty() {
			return UnFinishQty;
		}

		public String getAssemblyName() {
			return AssemblyName;
		}
	}

	private List<pAssembly> pAssembly;

	public List<pAssembly> getpAssemblies() {
		return pAssembly;
	}

	/**
	 * 利用率
	 */
	public class rateList {
		private String groupField;// 分组关键字 总利用率：空， 其他都是关键字字段值
		private float usedRate;// 利用率

		public String getGroupField() {
			return groupField;
		}

		public float getUsedRate() {
			return usedRate;
		}
	}

	private List<rateList> rateList;

	public List<rateList> getRateLists() {
		return rateList;
	}

	/**
	 * 库存-钢板<br>
	 * --/api/WorkBoard/getYieldInfo 产量画面的接口 <br>
	 * --/api/WorkBoard/getSheetInfo 库存画面钢板数据<br>
	 * --/api/WorkBoard/getSheetRckInfo 库存画面余料数据<br>
	 * --/api/WorkBoard/getShopPartInfo 库存画面在制品数据<br>
	 */
	public class sheetInfoList {
		private String dayNum;//
		private int amount;//
		private float weight;//

		public String getDayNum() {
			return dayNum;
		}

		public int getAmount() {
			return amount;
		}

		public float getWeight() {
			if (weight != 0) {

				return Float.parseFloat(SmsUtil.DecimalFormat(weight,
						SmsConfig.dotNum));
			} else {
				return weight;
			}
		}
	}

	private List<sheetInfoList> sheetInfoList;

	public List<sheetInfoList> getSheetInfoLists() {
		return sheetInfoList;
	}

	/**
	 * 库存-余料
	 */
	public class remInfoList {
		private String dayNum;//
		private int amount;//
		private float weight;//

		public String getDayNum() {
			return dayNum;
		}

		public int getAmount() {
			return amount;
		}

		public float getWeight() {
			if (weight != 0) {
				return Float.parseFloat(SmsUtil.DecimalFormat(weight,
						SmsConfig.dotNum));
			} else {
				return weight;
			}
		}
	}

	private List<remInfoList> remInfoList;

	public List<remInfoList> getRemInfoLists() {
		return remInfoList;
	}

	/**
	 * 库存-在制品
	 */
	public class shopPartInfoList {
		private String dayNum;//
		private int amount;//
		private float weight;//

		public String getDayNum() {
			return dayNum;
		}

		public int getAmount() {
			return amount;
		}

		public float getWeight() {
			if (weight != 0) {
				return Float.parseFloat(SmsUtil.DecimalFormat(weight,
						SmsConfig.dotNum));
			} else {
				return weight;
			}
		}
	}

	private List<shopPartInfoList> shopPartInfoList;

	public List<shopPartInfoList> getShopPartInfoLists() {
		return shopPartInfoList;
	}

	/**
	 * 产量
	 */
	public class YieldInfoList {
		private String dayNum;//
		private int amount;//
		private float weight;//

		public String getDayNum() {
			return dayNum;
		}

		public int getAmount() {
			return amount;
		}

		public float getWeight() {
			if (weight != 0) {
				return Float.parseFloat(SmsUtil.DecimalFormat(weight,
						SmsConfig.dotNum));
			} else {
				return weight;
			}
		}
	}

	private List<YieldInfoList> YieldInfoList;

	public List<YieldInfoList> getYieldInfoLists() {
		return YieldInfoList;
	}

	/**
	 * 未完工数量
	 */
	private class UnFinishQty {
		private String unFinishQTY;

		public String getUnFinishQTY() {
			return unFinishQTY;
		}
	}

	private List<UnFinishQty> unFinishQtyList;

	public List<UnFinishQty> getUnFinishQtyList() {
		return unFinishQtyList;
	}

}

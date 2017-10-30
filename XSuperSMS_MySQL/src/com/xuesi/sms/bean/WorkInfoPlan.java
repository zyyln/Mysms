package com.xuesi.sms.bean;

import java.util.ArrayList;

/**
 * /api/WorkBoard/getPlanListFinishQty <br>
 * /api/WorkBoard/getPlanListPlanQty <br>
 * /api/WorkBoard/getPlanListNumNested <br>
 * /api/WorkBoard/getPlanListOverDueQty <br>
 * /api/WorkBoard/getAssemblyNQty <br>
 * --计划进展数据解析体整合
 * 
 * @author XS-PC014
 * 
 */
public class WorkInfoPlan extends BaseModel {

	public class TotalClass {

		// "/api/WorkBoard/getPlanListFinishQty";
		// private String FinishQty;// "完工数量"}

		// "/api/WorkBoard/getPlanListFinishQty"(一条数据)
		private String PlanQty;// "零件图号"}

		// "/api/WorkBoard/getPlanListNumNested"
		private String Flag; // "一次报工状态,1:加工中,0::未加工"}
		private String NumNested;// "套料数量"}

		// /api/WorkBoard/getPlanListOverDueQty(一条数据)
		private String OverDueQty;// "逾期零件数"}

		// /api/WorkBoard/getAssemblyNQty
		private String FinishQty;// "完工数量"}
		private String UnFinishQty;// "未完工数量"}
		private String AssemblyName;// "产品名称"}

		public String getPlanQty() {
			return PlanQty;
		}

		public String getFlag() {
			return Flag;
		}

		public String getNumNested() {
			return NumNested;
		}

		public String getOverDueQty() {
			return OverDueQty;
		}

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

	private ArrayList<TotalClass> list;

	public ArrayList<TotalClass> getList() {
		return list;
	}
}

package com.xuesi.sms.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * 批处理返回结果解析<br>
 * 入库 || 倒垛 || 发布单号 || 出库 || 条码绑定
 * 
 * @author XS-PC014
 * 
 */
public class ResultCode extends BaseModel {

	public class resultListClass {
		private int resultCode;

		public int getResultCode() {
			return resultCode;
		}
	}

	private List<resultListClass> resultList;

	public List<resultListClass> getResultList() {
		return resultList;
	}

	/** resultCode集合 */
	private List<Integer> codeList;

	public List<Integer> getCodeList() {
		codeList = new ArrayList<Integer>();
		for (resultListClass codeClass : resultList) {
			codeList.add(codeClass.getResultCode());
		}
		return codeList;
	}

}

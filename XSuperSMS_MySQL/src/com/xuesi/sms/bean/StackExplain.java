package com.xuesi.sms.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.xuesi.sms.SmsConfig;
import com.xuesi.sms.util.SmsUtil;

/**
 * API_GET_STACKINFO_BYID = getUrl() + "/api/StoreStackMng/getStackInfoById";<br>
 * 画面：库存总览,盘点明细<br>
 * 优化方案：合并到{@link Stack}<br>
 * 
 * @author XS-PC014
 * 
 */
public class StackExplain extends BaseModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public class StackClass {
		private String stackName;// 货位名称
		private String maxQty;// 最大数量
		private String maxHeight;// 最大高度
		private String material;// 材质
		private String minThickNess;// 最小厚度
		private String maxThickNess;// 最大厚度
		private String minWidth;// 最小宽度
		private String maxWidth;// 最大宽度
		private String minLength;// 最小长度
		private String maxLength;// 最大长度
		private String maxWeight;// 最大重量
		private String projectId;// 工程ID"},
		private String assemblyId;// 部套ID"},
		private String supplierId;// 生产钢厂ID"},
		private String remark;// 备注"},
		private String minIntime;// 最早入库时间"}
		private String BATCHNO;// 炉批号
		private String SHIPNAME;// 船级社
		private String PLANNO;// 计划编号
		private String data1;
		private String data2;
		private String data3;
		private String data4;
		private String data5;
		private String data6;
		private Map<String, Object> setToMethod = null;

		public void setStackMap() {
			setToMethod.put("STACKNAME", stackName);
			setToMethod.put("MAXQTY", maxQty);
			setToMethod.put("MAXHEIGHT",
					SmsUtil.DecimalFormat(maxHeight, SmsConfig.dotNum));
			setToMethod.put("MATERIAL", material);
			setToMethod.put("MINTHICKNESS",
					SmsUtil.DecimalFormat(minThickNess, SmsConfig.dotNum));
			setToMethod.put("MAXTHICKNESS",
					SmsUtil.DecimalFormat(maxThickNess, SmsConfig.dotNum));
			setToMethod.put("MINWIDTH",
					SmsUtil.DecimalFormat(minWidth, SmsConfig.dotNum));
			setToMethod.put("MAXWIDTH",
					SmsUtil.DecimalFormat(maxWidth, SmsConfig.dotNum));
			setToMethod.put("MAXLENGTH",
					SmsUtil.DecimalFormat(maxLength, SmsConfig.dotNum));
			setToMethod.put("MINLENGTH",
					SmsUtil.DecimalFormat(minLength, SmsConfig.dotNum));
			setToMethod.put("MAXWEIGHT",
					SmsUtil.DecimalFormat(maxLength, SmsConfig.dotNum));
			setToMethod.put("PROJECTID", projectId);
			setToMethod.put("ASSEMBLYID", assemblyId);
			setToMethod.put("SUPPLIERID", supplierId);
			setToMethod.put("REMARK", remark);
			setToMethod.put("MININTIME", minIntime);
			setToMethod.put("BATCHNO", BATCHNO);
			setToMethod.put("SHIPNAME", SHIPNAME);
			setToMethod.put("PLANNO", PLANNO);
			setToMethod.put("DATA1", data1);
			setToMethod.put("DATA2 ", data2);
			setToMethod.put("DATA3", data3);
			setToMethod.put("DATA4", data4);
			setToMethod.put("DATA5", data5);
			setToMethod.put("DATA6", data6);
		}

		public Map<String, Object> getSetToMethod() {
			return setToMethod;
		}

		public String getStackName() {
			return stackName;
		}

		public void setStackName(String stackName) {
			this.stackName = stackName;
		}

		public String getMaxQty() {
			return maxQty;
		}

		public void setMaxQty(String maxQty) {
			this.maxQty = maxQty;
		}

		public String getMaxHeight() {
			return SmsUtil.DecimalFormat(maxHeight, SmsConfig.dotNum);
		}

		public void setMaxHeight(String maxHeight) {
			this.maxHeight = maxHeight;
		}

		public String getMaterial() {
			return material;
		}

		public void setMaterial(String material) {
			this.material = material;
		}

		public String getMinThickNess() {
			return SmsUtil.DecimalFormat(minThickNess, SmsConfig.dotNum);
		}

		public void setMinThickNess(String minThickNess) {
			this.minThickNess = minThickNess;
		}

		public String getMaxThickNess() {
			return SmsUtil.DecimalFormat(maxThickNess, SmsConfig.dotNum);
		}

		public void setMaxThickNess(String maxThickNess) {
			this.maxThickNess = maxThickNess;
		}

		public String getMinWidth() {
			return SmsUtil.DecimalFormat(minWidth, SmsConfig.dotNum);
		}

		public void setMinWidth(String minWidth) {
			this.minWidth = minWidth;
		}

		public String getMaxWidth() {
			return SmsUtil.DecimalFormat(maxWidth, SmsConfig.dotNum);
		}

		public void setMaxWidth(String maxWidth) {
			this.maxWidth = maxWidth;
		}

		public String getMinLength() {
			return SmsUtil.DecimalFormat(minLength, SmsConfig.dotNum);
		}

		public void setMinLength(String minLength) {
			this.minLength = minLength;
		}

		public String getMaxLength() {
			return SmsUtil.DecimalFormat(maxLength, SmsConfig.dotNum);
		}

		public void setMaxLength(String maxLength) {
			this.maxLength = maxLength;
		}

		public String getMaxWeight() {
			return SmsUtil.DecimalFormat(maxWeight, SmsConfig.dotNum);
		}

		public void setMaxWeight(String maxWeight) {
			this.maxWeight = maxWeight;
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

		public String getSupplierId() {
			return supplierId;
		}

		public void setSupplierId(String supplierId) {
			this.supplierId = supplierId;
		}

		public String getRemark() {
			return remark;
		}

		public void setRemark(String remark) {
			this.remark = remark;
		}

		public String getMinIntime() {
			return minIntime;
		}

		public void setMinIntime(String minIntime) {
			this.minIntime = minIntime;
		}

		public String getBATCHNO() {
			return BATCHNO;
		}

		public void setBATCHNO(String bATCHNO) {
			BATCHNO = bATCHNO;
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

		public String getData1() {
			return data1;
		}

		public void setData1(String data1) {
			this.data1 = data1;
		}

		public String getData2() {
			return data2;
		}

		public void setData2(String data2) {
			this.data2 = data2;
		}

		public String getData3() {
			return data3;
		}

		public void setData3(String data3) {
			this.data3 = data3;
		}

		public String getData4() {
			return data4;
		}

		public void setData4(String data4) {
			this.data4 = data4;
		}

		public String getData5() {
			return data5;
		}

		public void setData5(String data5) {
			this.data5 = data5;
		}

		public String getData6() {
			return data6;
		}

		public void setData6(String data6) {
			this.data6 = data6;
		}
	}

	private StackClass stackInfo;

	public StackClass getStackInfo() {
		this.stackInfo.setToMethod = new HashMap<String, Object>();
		this.stackInfo.setStackMap();
		return stackInfo;
	}

	private List<StackClass> list;

	public List<StackClass> getStackList() {
		return list;
	}

	class StackSet {
		private String segment;
		private String description;

		public String getSegment() {
			return segment;
		}

		public void setSegment(String segment) {
			this.segment = segment;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}
	}

	private List<StackSet> listSet;

	public List<StackSet> getListSet() {
		return listSet;
	}

	public static List<StackMsg> stackMsg(List<StackSet> set, StackClass info) {
		List<StackMsg> list = new ArrayList<StackMsg>();
		StackMsg sm;
		for (int i = 0; i < set.size(); i++) {
			sm = new StackExplain().new StackMsg();
			sm.setSegment(set.get(i).getSegment());
			sm.setDescription(set.get(i).getDescription());
			sm.setValue((String) getMapKey(set.get(i).getSegment()
					.toUpperCase(), info));
			list.add(sm);
		}
		return list;
	}

	public static Object getMapKey(String str, StackClass info) {
		List<String> li = new ArrayList<String>();
		Map map = info.getSetToMethod();
		Iterator iter = map.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry entry = (Map.Entry) iter.next();
			if (entry.getKey().equals(str)) {
				return entry.getValue();
			}
		}
		return null;
	}

	public class StackMsg {
		private String segment;
		private String description;
		private String value;

		public String getSegment() {
			return segment;
		}

		public void setSegment(String segment) {
			this.segment = segment;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}

		public String getValue() {
			return value;
		}

		public void setValue(String value) {
			this.value = value;
		}
	}

}

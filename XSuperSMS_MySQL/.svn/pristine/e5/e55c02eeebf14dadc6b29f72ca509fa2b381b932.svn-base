package com.xuesi.sms.bean;

import java.util.List;

import com.xuesi.sms.SmsConfig;
import com.xuesi.sms.util.SmsUtil;

/**
 * 行车作业面板信息
 * 
 * @author Administrator
 * 
 */
public class CraneWork {
	private String workID; // 流水号
	private List<CraneWorkDetail.ListClass> craneWorkDetail; // 流水号详细

	public String getWorkID() {
		if (null == workID) {
			return "";
		}
		return workID;
	}

	public void setWorkID(String workID) {
		this.workID = workID;
	}

	public List<CraneWorkDetail.ListClass> getCraneWorkDetail() {
		return craneWorkDetail;
	}

	public void setCraneWorkDetail(
			List<CraneWorkDetail.ListClass> craneWorkDetail) {
		this.craneWorkDetail = craneWorkDetail;
	}

	/**
	 * 获取总张数
	 * 
	 * @return
	 */
	public int getCount() {
		int count = 0;
		for (int i = 0; i < this.craneWorkDetail.size(); i++) {
			count += this.craneWorkDetail.get(i).getAmount();
		}
		return count;
	}

	/**
	 * 获取总重量
	 * 
	 * @return
	 */
	public String getWeight() {
		float weight = 0;
		for (int i = 0; i < this.craneWorkDetail.size(); i++) {
			if (this.craneWorkDetail.get(i).getWeight().length() > 0) {
				weight += Float.parseFloat(this.craneWorkDetail.get(i)
						.getWeight());
			} else {
				weight += 0;
			}
		}
		if (weight != 0) {
			return SmsUtil.DecimalFormat(weight, SmsConfig.dotNum) + "KG";
		} else {
			return Float.toString(weight);
		}
	}

	/**
	 * 获取源垛位
	 * 
	 * @return 源垛位
	 */
	public String getFromString() {
		if (craneWorkDetail.size() > 0) {
			return craneWorkDetail.get(0).getFirStackname();
		}
		return "";
	}

	/**
	 * 获取目标垛位
	 * 
	 * @return 目标垛位
	 */
	public String getToString() {
		if (craneWorkDetail.size() > 0) {
			return craneWorkDetail.get(0).getSecStackname();
		}
		return "";
	}

}

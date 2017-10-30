/**
 * <p>Title: CraneWorkInfo.java</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2007</p>
 * <p>Company: XSuper</p>
 * @author XS-PC011
 * @date 2015-9-8
 *
 */
package com.xuesi.sms.bean;

import java.util.List;

import com.xuesi.sms.R;
import com.xuesi.sms.SmsApplication;
import com.xuesi.sms.SmsConfig;
import com.xuesi.sms.util.SmsUtil;

/**
 * <p>
 * Title: CraneWorkInfo
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Company: XSuper
 * </p>
 * 
 * @author XS-PC011
 * @date 2015-9-8
 */
public class CraneWorkDetail extends BaseModel {

	public class ListClass {
		private String WORKID;// 流水码
		private int AMOUNT;// 数量
		private String WEIGHT;// 单重
		private String CREATER;// 创建人
		private String REMARK;// 备注
		private String LENGTH;// 物质规格长
		private String WIDTH;// 物质规格宽
		private String firSTACKNAME;// FORM
		private String MATERIAL;// 材质
		private String THICKNESS;// 厚度
		private String secSTACKNAME;// TO

		public String getWorkId() {
			return WORKID;
		}

		public void setWorkId(String WorkId) {
			this.WORKID = WorkId;
		}

		public int getAmount() {
			return AMOUNT;
		}

		public void setAmount(int Amount) {
			this.AMOUNT = Amount;
		}

		public String getWeight() {
			if (WEIGHT != null && !WEIGHT.equals("0")) {
				return SmsUtil.DecimalFormat(WEIGHT, SmsConfig.dotNum);
			} else {
				return WEIGHT;
			}
		}

		// public void setWeight(float Weight) {
		// this.WEIGHT = Weight;
		// }

		public String getCreater() {
			return CREATER;
		}

		public void setCreater(String Creater) {
			this.CREATER = Creater;
		}

		public String getRemark() {
			return REMARK;
		}

		public void setRemark(String Remark) {
			this.REMARK = Remark;
		}

		public String getLength() {
			return LENGTH;
		}

		// public void setLength(float Length) {
		// this.LENGTH = Length;
		// }

		public String getWidth() {
			return WIDTH;
		}

		// public void setWidth(float Width) {
		// this.WIDTH = Width;
		// }

		public String getFirStackname() {
			return firSTACKNAME;
		}

		public void setFirStackname(String FirStackname) {
			this.firSTACKNAME = FirStackname;
		}

		public String getMaterial() {
			return MATERIAL;
		}

		public void setMaterial(String Material) {
			this.MATERIAL = Material;
		}

		public String getThickness() {
			return THICKNESS;
		}

		// public void setThickness(float Thickness) {
		// this.THICKNESS = Thickness;
		// }

		public String getSecStackname() {
			return secSTACKNAME;
		}

		public void setSecStackname(String SecStackname) {
			this.secSTACKNAME = SecStackname;
		}

		public String getSpecTitle() {
			return SmsApplication.appContext
					.getString(R.string.crane_work_panel_spec);
		}

		public String getSpec() {
			StringBuffer sb = new StringBuffer();
			if (THICKNESS != null && !THICKNESS.equals("0")) {
				sb.append(SmsUtil.DecimalFormat(THICKNESS, SmsConfig.dotNum))
						.append("*");

			} else {
				sb.append(THICKNESS).append("*");
			}
			if (LENGTH != null && !LENGTH.equals("0")) {
				sb.append(SmsUtil.DecimalFormat(LENGTH, SmsConfig.dotNum))
						.append("*");
			} else {
				sb.append(LENGTH).append("*");
			}
			if (WIDTH != null && !WIDTH.equals("0")) {
				sb.append(SmsUtil.DecimalFormat(WIDTH, SmsConfig.dotNum))
						.append("*");
			} else {
				sb.append(WIDTH).append("*");
			}
			return sb.substring(0, sb.length() - 1);
		}

		// ---------
		private String AmountName;
		private String WeightName;

		public String getAmountName() {
			return AmountName;
		}

		public void setAmountName(String amountName) {
			this.AmountName = amountName;
		}

		public String getWeightName() {
			return WeightName;
		}

		public void setWeightName(String weightName) {
			this.WeightName = weightName;
		}

	}

	private List<ListClass> list;

	public List<ListClass> getList() {
		return list;
	}

}

package com.xuesi.sms.bean;

import java.io.Serializable;

import com.xuesi.sms.SmsConfig;
import com.xuesi.sms.util.SmsUtil;

/**
 * 余料
 * 
 * @author XS-PC014
 * 
 */
public class Remnant extends Sheet {

	public class ListCalss implements Serializable {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		// API_GETBARCODE = "/api/pad/getBarCode" 单独字段
		// private String storeHouseId;// 库房ID
		// private String name;// 库房名称
		private String id;// 钢板编号
		private String remname;// 余料名称，查询余料时返回

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getRemname() {
			return remname;
		}

		public void setRemname(String remname) {
			this.remname = remname;
		}

		// API_GETREMLIST = /api/pad/GetPADRemnantList 单独字段
		private float plateLength;// 余料长"},
		private float plateHeight;// 余料宽"},

		public float getPlateLength() {
			if (plateLength != 0) {
				return Float.parseFloat(SmsUtil.DecimalFormat(plateLength,
						SmsConfig.dotNum));
			} else {
				return plateLength;
			}
		}

		public void setPlateLength(float plateLength) {
			this.plateLength = plateLength;
		}

		public float getPlateHeight() {
			if (plateHeight != 0) {
				return Float.parseFloat(SmsUtil.DecimalFormat(plateHeight,
						SmsConfig.dotNum));
			} else {
				return plateHeight;
			}
		}

		public void setPlateHeight(float plateHeight) {
			this.plateHeight = plateHeight;
		}

	}

}

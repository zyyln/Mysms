package com.xuesi.sms.bean;

import java.io.Serializable;
import java.util.List;

import com.xuesi.sms.ServerApi;
import com.xuesi.sms.SmsConfig;
import com.xuesi.sms.util.SmsUtil;

/**
 * 获取库房信息<br>
 * {@link ServerApi}<br>
 * 
 * @author XS-PC014
 * 
 */
public class StoreHouse extends BaseVo {

	public class ListClass implements Serializable {
		private static final long serialVersionUID = 1L;
		private String ID;// 库房ID
		private String NAME;// 库房名称
		private String TYPE;// 库房类型
		private String LENGHT;
		private String WIDTH;

		public String getID() {
			return ID;
		}

		public void setID(String iD) {
			ID = iD;
		}

		public String getNAME() {
			return NAME;
		}

		public void setNAME(String nAME) {
			NAME = nAME;
		}

		public String getTYPE() {
			return TYPE;
		}

		public void setTYPE(String tYPE) {
			TYPE = tYPE;
		}

		public String getLENGHT() {
			if (LENGHT != null && !LENGHT.equals("0")) {
				return SmsUtil.DecimalFormat(LENGHT, SmsConfig.dotNum);
			} else {
				return LENGHT;
			}
		}

		public void setLENGHT(String lENGHT) {
			LENGHT = lENGHT;
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
	}

	private List<ListClass> list;

	public List<ListClass> getList() {
		return list;
	}

}

package com.xuesi.sms.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Android Calendar的运用
 * 
 * @author chen_zhipeng
 * 
 */
public class CalendarUtil {

	// private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd",
	// Locale.CHINA);
	//
	// private static SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMdd",
	// Locale.CHINA);

	/**
	 * 获取今天的中文日期
	 * 
	 * @return string
	 * */
	public static String getChineseDate() {
		TimeZone.setDefault(TimeZone.getTimeZone("Asia/Shanghai"));
		return new SimpleDateFormat("EEEE yyyy年MM月dd日", Locale.CHINA)
				.format(new Date());
	}

	/**
	 * 返回一个年月日的日期 yyyy-MM-dd hh:mm:ss.S
	 * 
	 * @param dataStr
	 * @return
	 */
	public static String getChineseDate(String dataStr) {
		TimeZone.setDefault(TimeZone.getTimeZone("Asia/Shanghai"));
		Date currentDate = new Date();
		try {
			SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.S");
			currentDate = dt.parse(dataStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return new SimpleDateFormat("EEEE yyyy年MM月dd日", Locale.CHINA)
				.format(currentDate);
	}

	/**
	 * 返回 yyyy-MM-dd hh:mm:ss.S
	 * 
	 * @param dataStr
	 * @return
	 */
	public static String getChineseDate2(String dataStr) {
		TimeZone.setDefault(TimeZone.getTimeZone("Asia/Shanghai"));
		Date currentDate = new Date();
		try {
			SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.S");
			currentDate = dt.parse(dataStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA)
				.format(currentDate);
	}

	/**
	 * 获取服务器时间
	 * 
	 * @return
	 */
	public static String getServerData() {
		return "";
	}

	/**
	 * 获取当前的日期yyyy-MM-dd
	 * 
	 * @return string
	 */
	public static String getTodayDate() {
		// TimeZone.setDefault()必须在new SimpleDateFormat()之前才能有效
		TimeZone.setDefault(TimeZone.getTimeZone("Asia/Shanghai"));
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
		Date now = new Date();
		String hehe = sdf.format(now);
		return hehe;
	}
	/**
	 * 获取当前的日期yyyyMMddHHmmss
	 * 
	 * @return string
	 */
	public static String getTodayDateL() {
		// TimeZone.setDefault()必须在new SimpleDateFormat()之前才能有效
		TimeZone.setDefault(TimeZone.getTimeZone("Asia/Shanghai"));
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss", Locale.CHINA);
		Date now = new Date();
		String hehe = sdf.format(now);
		return hehe;
	}

	/**
	 * 获取当前的日期2
	 * 
	 * @return string
	 */
	public static String getTodayDate2() {
		TimeZone.setDefault(TimeZone.getTimeZone("Asia/Shanghai"));
		SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMdd", Locale.CHINA);
		Date now = new Date();
		String hehe = sdf2.format(now);
		return hehe;
	}

	/**
	 * 获取本周的第一天的日期
	 * 
	 * @return string
	 */
	public static String getFirstDayOfWeek() {
		TimeZone.setDefault(TimeZone.getTimeZone("Asia/Shanghai"));
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
		Calendar lastDate = Calendar.getInstance();
		// 减去一天，变为当月最后一天
		lastDate.add(Calendar.DATE, -lastDate.get(Calendar.DAY_OF_WEEK) + 1);

		return sdf.format(lastDate.getTime());
	}

	/**
	 * 获取本月第一天的日期
	 * 
	 * @return string
	 */
	public static String getFirstDayOfMonth() {
		TimeZone.setDefault(TimeZone.getTimeZone("Asia/Shanghai"));
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
		Calendar lastDate = Calendar.getInstance();
		// 设为当前月的1号
		lastDate.set(Calendar.DATE, 1);
		return sdf.format(lastDate.getTime());
	}

	/**
	 * 获取本年的第一天的日期
	 * 
	 * @return string
	 */
	public static String getFirstDayOfYear() {
		TimeZone.setDefault(TimeZone.getTimeZone("Asia/Shanghai"));
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
		Calendar cd = Calendar.getInstance();
		int yearOfNumber = cd.get(Calendar.DAY_OF_YEAR) - 1;
		cd.add(Calendar.DATE, -yearOfNumber);
		return sdf.format(cd.getTime());
	}

	/**
	 * 比较两个日期
	 * 
	 * @return boolean
	 */
	public static boolean compareDataString(String dateStr1, String dateStr2) {
		TimeZone.setDefault(TimeZone.getTimeZone("Asia/Shanghai"));
		if (!SmsUtil.checkString(dateStr1)) {// 第一次比较
			return true;
		}
		if (!SmsUtil.checkString(dateStr2) || dateStr2.length() < 21) {
			return false;
		}

		dateStr1 = dateStr1.substring(0, 21);
		dateStr2 = dateStr2.substring(0, 21);

		SimpleDateFormat tmpSdf = new SimpleDateFormat("yyyy-MM-dd HH:MM:SS.S",
				Locale.CHINA);
		try {
			Date data1 = tmpSdf.parse(dateStr1);
			Date data2 = tmpSdf.parse(dateStr2);
			// 当且仅当此 data1 对象表示的瞬间比 data2 表示的瞬间早，才返回 true；否则返回 false
			return data1.before(data2);
		} catch (ParseException e) {
			e.printStackTrace();
			return false;
		}
	}

}

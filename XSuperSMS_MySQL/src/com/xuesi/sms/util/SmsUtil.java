package com.xuesi.sms.util;

import java.text.DecimalFormat;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.OvershootInterpolator;
import android.view.animation.TranslateAnimation;

import com.xuesi.mos.util.MosLog;

/**
 * 工具类
 * 
 * @author XS-PC014
 * 
 */
public class SmsUtil {
	private final String TAG_LOG = SmsUtil.class.getSimpleName();
	private final MosLog mLog = MosLog.getInstance(TAG_LOG, "XUESI-SMS");

	/**
	 * @function: check string null and check string size
	 * @parameter: string
	 * @return: boolean
	 */
	public static boolean checkString(String str) {
		if (null != str && str.trim().length() > 0) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 匹配时间字符串合法性
	 * 
	 * @param time
	 * @return
	 */
	public static boolean checkTimeString(String time) {
		String result = time.replaceAll("[//d]", "");
		if (result.length() < 8) {
			return false;
		} else if (result.length() > 8) {
			result = result.substring(0, 8);
		}

		Pattern p = Pattern
				.compile("([0-9]{3}[1-9]|[0-9]{2}[1-9][0-9]{1}|[0-9]{1}[1-9][0-9]{2}|[1-9][0-9]{3})(((0[13578]|1[02])(0[1-9]|[12][0-9]|3[01]))|((0[469]|11)(0[1-9]|[12][0-9]|30))|(02(0[1-9]|[1][0-9]|2[0-8])))");
		return p.matcher(result).matches();
	}

	public static double getPercentValue(double part, double total) {
		if (total == 0) {
			return 0;
		}
		return part / total;
	}

	public static double getPercentValue(String part, String total) {
		if (total.equals("0") || part.length() == 0 || total.length() == 0) {
			return 0;
		}
		Double part1 = Double.parseDouble(part);
		Double total1 = Double.parseDouble(total);
		return part1 / total1;
	}

	public static String getPercentString(double percent) {
		// DecimalFormat df = new DecimalFormat("#.00");
		// return df.format(percent * 100) + "%";
		int per = (int) (percent * 10000);
		return (per / 100.0) + "%";
	}

	/**
	 * 比较一个数是否在另外两个数之间
	 * 
	 * @param num
	 * @param minNum
	 * @param maxNum
	 * @return
	 */
	public static boolean compareNum(float num, String minNum, String maxNum) {
		float y1 = Float.valueOf(minNum);
		float y2 = Float.valueOf(maxNum);

		if (num >= y1 && num <= y2) {
			return true;
		} else {
			return false;
		}
	}

	public static void slideview(final View view, final float offsetX,
			final int viewHight) {
		TranslateAnimation animation = new TranslateAnimation(-offsetX, 0, 0, 0);
		animation.setInterpolator(new OvershootInterpolator());
		// 设置动画执行时间
		animation.setDuration(1000);
		// 设置动画延迟执行
		// animation.setStartOffset(500);
		animation.setAnimationListener(new Animation.AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				// int left = view.getLeft()+(int)(offsetX);
				// int top = view.getTop();
				int width = view.getWidth();
				int height = viewHight;
				view.clearAnimation();
				view.layout(0, 0, width, height);
			}
		});
		view.startAnimation(animation);
	}

	/**
	 * 随机生成字符串
	 */
	public static String getRandomString(int length) { // length表示生成字符串的长度
		String base = "abcdefghijklmnopqrstuvwxyz0123456789";
		Random random = new Random();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < length; i++) {
			int number = random.nextInt(base.length());
			sb.append(base.charAt(number));
		}
		return sb.toString();
	}

	/**
	 * 随机数字符串
	 */
	public static String getRandomInt(int min, int max) {
		String randomQ = String.valueOf((int) (Math.random() * max + min));
		return randomQ;
	}

	/**
	 * double保留小数的方法
	 * 
	 * @param vaule
	 *            需要截取保留小数的原数据
	 * @param arg0
	 *            需要保留几位小数
	 * @return
	 */
	public static String DecimalFormat(Double vaule, int arg0) {
		if (vaule != null) {
			StringBuffer buffer = new StringBuffer("#.");
			for (int i = 0; i < arg0; i++) {
				buffer.append("#");
			}
			String str = buffer.toString();
			DecimalFormat df = new DecimalFormat(str);
			return df.format(vaule);
		} else {
			return "0";
		}
	}

	public static String DecimalFormat(Float vaule, int arg0) {
		if (vaule != null) {
			StringBuffer buffer = new StringBuffer("#.");
			for (int i = 0; i < arg0; i++) {
				buffer.append("#");
			}
			String str = buffer.toString();
			DecimalFormat df = new DecimalFormat(str);
			return df.format(vaule);
		} else {
			return "0";
		}
	}

	public static String DecimalFormat(String vaule, int arg0) {
		if (vaule != null && vaule.length() > 0) {
			float vaule_ = Float.parseFloat(vaule);
			StringBuffer buffer = new StringBuffer("#.");
			for (int i = 0; i < arg0; i++) {
				buffer.append("#");
			}
			String str = buffer.toString();
			DecimalFormat df = new DecimalFormat(str);
			return df.format(vaule_);
		} else {
			return "0";
		}
	}

	/**
	 * 去除所有空格
	 * 
	 * @param str
	 * @return
	 */
	public static String removerAllSpace(String str) {
		return str.replaceAll(" ", "");
	}

	/**
	 * 验证ipV4字符串的合法性
	 */
	public static boolean isIP(String strIp) {
		if (strIp.length() < 7 || strIp.length() > 15 || "".equals(strIp)) {
			return false;
		}
		String regex = "([1-9]|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(strIp);
		return matcher.matches();
	}

	public static boolean isPort(String strPort) {
		int port = Integer.parseInt(strPort);
		if (port >= 0 && port < 65535) {
			return true;
		}
		return false;
	}

	/**
	 * 匹配正整数
	 */
	public static boolean isPositiveInt(String str) {
		if (str.length() > 0) {
			String regex = "^\\+?[1-9][0-9]*$";
			// String regex =
			// "^[0-9]*[1-9][0-9]*+\\*[0-9]*[1-9][0-9]*+\\*[0-9]*[1-9][0-9]*+$";
			Pattern p = Pattern.compile(regex);
			Matcher m = p.matcher(str);
			return m.find();
		} else {
			return false;
		}
	}

	/**
	 * 匹配正浮点数
	 */
	public static boolean isPositiveFloat(String str) {
		if (str.length() > 0) {
			String regexFloat = "^(([0-9]+\\.[0-9]*[1-9][0-9]*)|([0-9]*[1-9][0-9]*\\.[0-9]+)|([0-9]*[1-9][0-9]*))$";
			// String regex =
			// "^[0-9]*[1-9][0-9]*+\\*[0-9]*[1-9][0-9]*+\\*[0-9]*[1-9][0-9]*+$";
			Pattern p = Pattern.compile(regexFloat);
			Matcher m = p.matcher(str);
			return m.find();
		} else {
			return false;
		}
	}

	/**
	 * 匹配正浮点数
	 */
	public static boolean isPositiveFloatOrInt(String str) {
		if (str.length() > 0) {
			String regex = "^(\\+?[1-9][0-9]*)|(([0-9]+\\.[0-9]*[1-9][0-9]*)|([0-9]*[1-9][0-9]*\\.[0-9]+)|([0-9]*[1-9][0-9]*))$";
			// String regex =
			// "^[0-9]*[1-9][0-9]*+\\*[0-9]*[1-9][0-9]*+\\*[0-9]*[1-9][0-9]*+$";
			Pattern p = Pattern.compile(regex);
			Matcher m = p.matcher(str);
			return m.find();
		} else {
			return false;
		}
	}

	/**
	 * 判断物料编码是否正确
	 */
	// public static boolean isMaterialCode(String str) {
	// if (str.length() > 0) {
	// String regex = "^[A-Za-z0-9]+\\*[0-9]*[1-9][0-9]*+$";
	// // String regex =
	// // "^[0-9]*[1-9][0-9]*+\\*[0-9]*[1-9][0-9]*+\\*[0-9]*[1-9][0-9]*+$";
	// Pattern p = Pattern.compile(regex);
	// Matcher m = p.matcher(str);
	// return m.find();
	// } else {
	// return false;
	// }
	// }
	/**
	 * 判断物料条码格式是否正确
	 */
	public static boolean isMaterialCode(String str) {
		if (str.length() > 0) {
			// 整数
			String regexInt = "^[A-Za-z0-9]+\\*[0-9]*[1-9][0-9]*+$";
			String regexFloat = "^[A-Za-z0-9]+\\*(([0-9]+\\.[0-9]*[1-9][0-9]*)|([0-9]*[1-9][0-9]*\\.[0-9]+)|([0-9]*[1-9][0-9]*))+$";
			// 浮点数x*x*x
			// String regexFloat =
			// "^((([0-9]+\\.[0-9]*[1-9][0-9]*)|([0-9]*[1-9][0-9]*\\.[0-9]+)|([0-9]*[1-9][0-9]*))"
			// +
			// "+\\*(([0-9]+\\.[0-9]*[1-9][0-9]*)|([0-9]*[1-9][0-9]*\\.[0-9]+)|([0-9]*[1-9][0-9]*))"
			// +
			// "+\\*(([0-9]+\\.[0-9]*[1-9][0-9]*)|([0-9]*[1-9][0-9]*\\.[0-9]+)|([0-9]*[1-9][0-9]*)))+$";
			Pattern pInt = Pattern.compile(regexInt);
			Pattern pFloat = Pattern.compile(regexFloat);
			Matcher mInt = pInt.matcher(str);
			Matcher mFloat = pFloat.matcher(str);
			return mInt.find() | mFloat.find();
		} else {
			return false;
		}
	}

}

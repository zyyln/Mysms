/*
 * SE4500_DEMO.java
 * com.motorolasolutions.adc.decoder
 *
 * Function： TODO 
 *
 *   ver     date      		author
 * ──────────────────────────────────
 *   		 2015-11-5 		lzq
 *
 * Copyright (c) 2015, NJXS All Rights Reserved.
 */

package com.motorolasolutions.adc.decoder;

import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Build;
import android.os.SystemClock;

import com.xuesi.mos.util.MosLog;
import com.xuesi.sms.SmsApplication;
import com.xuesi.sms.SmsConfig;

/**
 * ClassName:SE4500_DEMO
 * <p>
 * Function: 本类是TT35的SE4500_DEMO和KT45的SE4500_DEMO合成的一个类
 * <p>
 * Reason: ADD REASON
 * 
 * @author lzq
 * @version
 * @since Ver 1.1
 * @Date 2015-11-5
 */
public class SE4500_DEMO implements BarCodeReader.DecodeCallback {
	/** LOG */
	private final String TAG_LOG = SE4500_DEMO.class.getSimpleName();
	private final MosLog mLog = MosLog.getInstance(TAG_LOG, "XUESI-SMS");

	// BarCodeReader specifics
	private BarCodeReader bcr = null;

	// system
	/** sound(音频发生器) */
	private ToneGenerator tg = null;
	private int trigMode = BarCodeReader.ParamVal.AUTO_AIM;

	// decode beep enable
	private boolean beepMode = true;
	static long decode_start = 0;
	static long decode_end = 0;
	private boolean isnotdecode = true;

	private int state = STATE_IDLE;
	/** states:闲置 */
	static final int STATE_IDLE = 0;
	/** states:解码 */
	static final int STATE_DECODE = 1;
	/** states:免提 */
	// static final int STATE_HANDSFREE = 2;
	/** states:预览snapshot preview mode */
	// static final int STATE_PREVIEW = 3;
	/** states:快照 */
	static final int STATE_SNAPSHOT = 4;
	/** states:视频 */
	// static final int STATE_VIDEO = 5;

	// KT45
	private boolean STATE_ISDECODING = false;

	// TT35
	private Callback mCallback;

	/** KT45条码扫描按键 */
	public static final int KEYCODE_KT45 = 134;
	/** TT35条码扫描按键 */
	public static final int KEYCODE_TT35 = 135;

	/** 加载libs中的so包 */
	static {
		if (Build.MODEL.equals(SmsConfig.TAG_KT45)) {
			System.loadLibrary("IAL");
			System.loadLibrary("SDL");
			System.loadLibrary("barcodereader44");
		} else if (Build.DEVICE.equals(SmsConfig.TAG_TT35)) {
			// SE45SR Symbol SE4500SR扫头
			System.loadLibrary("IAL_TT35");// 已删除
			System.loadLibrary("SDL_TT35");// 已删除
			System.loadLibrary("barcodereader");// 已删除
		}
	}

	/** 获取实例 */
	public static SE4500_DEMO instance;

	/** 单例模式中获取唯一的SE4500_DEMO实例 */
	public static SE4500_DEMO getInstance(Callback callback) {
		if (null == instance) {
			instance = new SE4500_DEMO(callback);
		}
		return instance;
	}

	// 可用单例
	private SE4500_DEMO(Callback callback) {
		this.mCallback = callback;

		// sound(音频发生器)
		tg = new ToneGenerator(AudioManager.STREAM_MUSIC, ToneGenerator.MAX_VOLUME);

		state = STATE_IDLE;
		try {
			if (Build.MODEL.equals(SmsConfig.TAG_KT45)) {
				bcr = BarCodeReader.open(SmsApplication.appContext);
			} else if (Build.DEVICE.equals(SmsConfig.TAG_TT35)) {
				bcr = BarCodeReader.open(1);
			}
			if (bcr == null) {
				return;
			}
			bcr.setDecodeCallback(this);
		} catch (Exception e) {
			e.printStackTrace();
		}
		doSetParam(22, 2);
		doSetParam(23, 40);
		doSetParam(BarCodeReader.ParamNum.QR_INVERSE, BarCodeReader.ParamVal.INVERSE_AUTOD);
		doSetParam(BarCodeReader.ParamNum.DATAMATRIX_INVERSE, BarCodeReader.ParamVal.INVERSE_AUTOD);
	}

	public void onResume() {
		state = STATE_IDLE;

		try {
			if (Build.MODEL.equals(SmsConfig.TAG_KT45)) {
				bcr = BarCodeReader.open(SmsApplication.appContext);
			} else if (Build.DEVICE.equals(SmsConfig.TAG_TT35)) {
				bcr = BarCodeReader.open(1);
			}
			if (bcr == null) {
				return;
			}
			bcr.setDecodeCallback(this);
		} catch (Exception e) {
			e.printStackTrace();
		}

		doSetParam(22, 2);
		doSetParam(23, 40);
		doSetParam(BarCodeReader.ParamNum.QR_INVERSE, BarCodeReader.ParamVal.INVERSE_AUTOD);
		doSetParam(BarCodeReader.ParamNum.DATAMATRIX_INVERSE, BarCodeReader.ParamVal.INVERSE_AUTOD);
	}

	// set param
	private int doSetParam(int num, int val) {
		String s = "";
		int ret = bcr.setParameter(num, val);// 报空指针-0826
		if (ret != BarCodeReader.BCR_ERROR) {
			if (num == BarCodeReader.ParamNum.PRIM_TRIG_MODE) {
				trigMode = val;
				if (val == BarCodeReader.ParamVal.HANDSFREE) {
					s = "HandsFree";
				} else if (val == BarCodeReader.ParamVal.AUTO_AIM) {
					s = "AutoAim";
					ret = bcr.startHandsFreeDecode(BarCodeReader.ParamVal.AUTO_AIM);
					if (ret != BarCodeReader.BCR_SUCCESS) {
					}
				} else if (val == BarCodeReader.ParamVal.LEVEL) {
					s = "Level";
				}
			}
		} else {
			s = " FAILED (" + ret + ")";
			mLog.d("SE4500_DEMO.doSetParam.s==" + s);
		}
		return ret;
	}

	/**
	 * 实体按键调用开始扫描方法
	 */
	public void startScan() {
		if (STATE_ISDECODING == false) {
			decode_start = SystemClock.elapsedRealtime();
			doDecode();
			STATE_ISDECODING = true;
		}
	}

	// start a decode session
	private void doDecode() {
		if (setIdle() != STATE_IDLE)
			return;

		state = STATE_DECODE;
		isnotdecode = true;
		bcr.startDecode(); // start decode (callback gets results)
	}

	/**
	 * 是否闲置
	 */
	private int setIdle() {
		int prevState = state;
		int ret = prevState; // for states taking time to chg/end

		state = STATE_IDLE;
		switch (prevState) {
		case STATE_DECODE:
			bcr.stopDecode();
			break;
		case STATE_SNAPSHOT:
			ret = STATE_IDLE;
			break;
		default:
			ret = STATE_IDLE;
		}
		return ret;
	}

	// display status string
	private void dspData(String s) {
		mCallback.onScanSuccess(s);
		// tvData.setText("");
		// tvData.setText(s);
	}

	private boolean isHandsFree() {
		return (trigMode == BarCodeReader.ParamVal.HANDSFREE);
	}

	private boolean isAutoAim() {
		return (trigMode == BarCodeReader.ParamVal.AUTO_AIM);
	}

	private void beep() {
		if (tg != null)
			tg.startTone(ToneGenerator.TONE_CDMA_NETWORK_BUSY_ONE_SHOT);
	}

	@Override
	public void onDecodeComplete(int symbology, int length, byte[] data, BarCodeReader reader) {
		// TODO BarCodeReader.DecodeCallback
		if (state == STATE_DECODE)
			state = STATE_IDLE;

		if (length > 0) {
			decode_end = SystemClock.elapsedRealtime();
			if (isHandsFree() == false && isAutoAim() == false)
				bcr.stopDecode();

			if (beepMode)
				beep();
			isnotdecode = false;

			if (symbology == 0x99) // type 99?
			{
				symbology = data[0];
				int n = data[1];
				int s = 2;
				int d = 0;
				int len = 0;
				byte d99[] = new byte[data.length];
				for (int i = 0; i < n; ++i) {
					s += 2;
					len = data[s++];
					System.arraycopy(data, s, d99, d, len);
					s += len;
					d += len;
				}
				d99[d] = 0;
				data = d99;
			}

			dspData(new String(data, 0, length));
		} else {
			// no-decode
			switch (length) {
			case BarCodeReader.DECODE_STATUS_TIMEOUT:
				break;

			case BarCodeReader.DECODE_STATUS_CANCELED:
				break;

			case BarCodeReader.DECODE_STATUS_ERROR:
			default:
				break;
			}
		}
		STATE_ISDECODING = false;
	}

	@Override
	public void onEvent(int event, int info, byte[] data, BarCodeReader reader) {
		// TODO BarCodeReader.DecodeCallback
	}

	public void onPause() {
		if (bcr != null) {
			setIdle();
			bcr.release();
			bcr = null;
		}
	}

	public void onDestroy() {
		if (bcr != null) {
			setIdle();
			bcr.release();
			bcr = null;
		}
	}

	public interface Callback {
		public void onScanSuccess(String code);

		// public abstract void onScanFail(String code);
	}

}

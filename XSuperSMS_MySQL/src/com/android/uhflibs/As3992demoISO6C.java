/*
 * As3992demoISO6C.java
 * com.android.uhflibs
 *
 * Function： 
 *
 *   ver     date      		author
 * ──────────────────────────────────
 *   		 2015-11-5 		lzq
 *
 * Copyright (c) 2015, NJXS All Rights Reserved.
 */

package com.android.uhflibs;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Handler;
import android.os.Message;

import com.xuesi.mos.util.MosLog;
import com.xuesi.sms.R;
import com.xuesi.sms.SmsConfig;

/**
 * ClassName:As3992demoISO6C
 * <p>
 * Function: 本类是TT35的As3992demoISO6C和KT45的As3992demoISO6C合成的一个类
 * <p>
 * Reason:
 * 
 * @author lzq
 * @version
 * @since Ver 1.1
 * @Date 2015-11-5
 */
public class As3992demoISO6C {
	private final String TAG_LOG = As3992demoISO6C.class.getSimpleName();
	private final MosLog mLog = MosLog.getInstance(TAG_LOG, "XUESI-SMS");

	// t45
	private as3992_native native_method;
	private Context context;
	private DeviceControl DevCtrl;
	private ReadThread rthread;

	// private PowerManager pM = null;
	// private WakeLock wK = null;

	private int init_progress = 0;

	/** 正在超高频扫描 */
	private boolean inSearch = false;
	private String epcs[] = null;
	private SearchThread st;
	private SoundPool soundPool;
	private int soundId;

	// tt35
	private File device_path;
	private BufferedWriter proc;

	/** KT45实体按键F2-超高频 */
	public static final int KEYCODE_KT45 = 67;
	/** TT35实体按键F1-超高频 */
	public static final int KEYCODE_TT35 = 131;
	/** 存储扫描过的条形码 */
	private List<String> sendEpcs = new ArrayList<String>();
	private Callback mCallback;

	/** 获取实例 */
	public static As3992demoISO6C instance;

	/** 单例模式中获取唯一的As3992demoISO6C实例 */
	public static As3992demoISO6C getInstance(Callback callback) {
		if (null == instance) {
			instance = new As3992demoISO6C(callback);
		}
		return instance;
	}

	// 可用单例
	private As3992demoISO6C(Callback callback) {
		this.mCallback = callback;

		native_method = new as3992_native();
		if (native_method.OpenComPort("/dev/ttyMT2") != 0) {
			return;
		}
		init_progress++;
		try {
			if (Build.MODEL.equals(SmsConfig.TAG_KT45)) {
				DevCtrl = new DeviceControl("/sys/class/misc/mtgpio/pin");
			} else if (Build.DEVICE.equals(SmsConfig.TAG_TT35)) {
				device_path = new File("/proc/driver/as3992");
				proc = new BufferedWriter(new FileWriter(device_path, false));
				proc.write("on");
				proc.flush();
				mLog.i("write power on cmd to file");
			}
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IOException e) {
			new AlertDialog.Builder(context).setTitle(R.string.warning).setMessage(R.string.msg_open_serialport_fail)
					.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					}).show();
			return;
		}
		try {
			DevCtrl.PowerOnDevice();
			mLog.i("as3992_destroy---poweron");
		} catch (IOException e) {

		}
		init_progress++;

		rthread = new ReadThread();
		rthread.start();

		// pM = (PowerManager) getSystemService(POWER_SERVICE);
		// if (pM != null) {
		// wK = pM.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK |
		// PowerManager.ON_AFTER_RELEASE, "lock3992");
		// if (wK != null) {
		// wK.acquire();
		// init_progress++;
		// }
		// }

		// if (init_progress == 2) {
		// mLog.w("3992_6C---wake lock init failed");
		// }

		try {
			Thread.sleep(300);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		byte[] soft_ver = native_method.get_version(0);
		if (soft_ver == null) {
			// Cur_Tag_Info.setText("Communication Error");
		} else {
			// Cur_Tag_Info.setText("Firmware: ");
			// Cur_Tag_Info.append(new String(soft_ver, 0, soft_ver.length));
			//
			// Set_Tag.setEnabled(true);
			// Search_Tag.setEnabled(true);
			// Read_Tag.setEnabled(true);
			// Write_Tag.setEnabled(true);
			// Set_EPC.setEnabled(true);
			// Set_Password.setEnabled(true);
			// Lock_Tag.setEnabled(true);
			// Area_Select.setEnabled(true);
		}
		native_method.set_gen2(0, (byte) 6);
		native_method.set_gen2(1, (byte) 1);
		native_method.set_gen2(3, (byte) 1);
		set_sensitivity(-72);
		power_antenna(true);
		set_hopping_freq(920625, 924375, 750, -40);
		native_method.set_alloc_param(1, 10000, 0);
	}

	class ReadThread extends Thread {

		@Override
		public void run() {
			super.run();
			while (!isInterrupted()) {
				native_method.read_thread();
			}
		}
	}

	int set_sensitivity(int sens) {
		int retry = 0;
		mLog.d("" + sens);
		// while(set_sens(sens) != 0)
		while (native_method.set_gen2(5, (byte) sens) != 0) {
			mLog.d("setsens retry");
			if ((++retry) >= 5) {
				return -1;
			}
		}
		return 0;
	}

	int power_antenna(boolean power) {
		int retry = 0;
		while (native_method.set_antenna_power(power) != 0) {
			mLog.d("set antenna power " + power + " retry");
			if ((++retry) >= 5) {
				return -1;
			}
		}
		return 0;
	}

	// , int id in my gui program don't use profile id Just set it to zero.
	int set_hopping_freq(int start, int stop, int increment, int rssi) {
		int retry = 0, reval;
		while ((reval = native_method.set_freq(start, stop, increment, rssi, 0)) != 0) {
			if (reval == -2) {
				return -2;
			}
			mLog.e("setfreq retry");
			if ((++retry) >= 5) {
				return -1;
			}
		}
		return 0;
	}

	public void startScan() {
		if (inSearch) {
			onStop();
		} else {
			soundPool = new SoundPool(2, AudioManager.STREAM_MUSIC, 0);
			if (soundPool == null) {
				mLog.d("Open sound failed");
			}

			soundId = soundPool.load("/system/media/audio/ui/VideoRecord.ogg", 0);
			// mLog.d("id is " + soundId);

			st = new SearchThread();
			inSearch = true;
			st.start();
		}
	}

	class SearchThread extends Thread {

		@Override
		public void run() {
			super.run();
			while (inSearch) {
				Message msg = new Message();
				// epcs = native_method.search_UHF();
				epcs = search_UHF();
				if (epcs != null) {
					msg.what = 1;
					handler.sendMessage(msg);
				} else {
					msg.what = 0;
					handler.sendMessage(msg);
				}
				try {
					sleep(200);
				} catch (InterruptedException e) {
					this.interrupt();
				}
			}
		}
	}

	String[] search_UHF() {
		List<as3992_native.Tag_Data_Rssi> cnt;
		int retry = 0;
		do {
			mLog.d("search retry");
			cnt = native_method.search_card_rssi();
			if ((++retry) >= 10) {
				return null;
			}
		} while (cnt == null);
		int noc = cnt.size();
		String[] out = new String[noc];
		for (int i = 0; i < noc; i++) {
			out[i] = new String();
			int loe = cnt.get(i).tdata.epc.length;
			for (int j = 0; j < loe; j++) {
				out[i] += String.format("%02x ", cnt.get(i).tdata.epc[j]);
			}
		}
		return out;
	}

	Handler handler = new Handler() {

		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.what == 1) {
				soundPool.play(soundId, 1, 1, 0, 0, 1);

				for (int p = 0; p < epcs.length; p++) {
					if (sendEpcs.contains(epcs[p])) {
						continue;
					} else {
						sendEpcs.add(epcs[p]);

						// 只取前四位(临时)
						String uhfcode = epcs[p].substring(0, 5);
						mCallback.onUHFSuccess(uhfcode);
						return;
					}
				}
			}
		};
	};

	private void onStop() {
		if (null != soundPool) {
			soundPool.release();
		}
		if (inSearch) {
			inSearch = false;
		}
	}

	public void onDestroy() {
		onStop();
		switch (init_progress) {
		// case 3:
		// wK.release();
		case 2:
			rthread.interrupt();
			try {
				if (Build.MODEL.equals(SmsConfig.TAG_KT45)) {
					DevCtrl.PowerOffDevice();
					mLog.i("write poweroff cmd to file");
				} else if (Build.DEVICE.equals(SmsConfig.TAG_TT35)) {
					proc.write("off"); // powersave
					proc.flush(); // powersave
					mLog.i("write poweroff cmd to file");
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		case 1:
			native_method.CloseComPort();
		case 0:
		default:
			init_progress = 0;
		}
	}

	public interface Callback {

		public void onUHFSuccess(String code);

		// public abstract void onUHFFail(String code);
	}

}

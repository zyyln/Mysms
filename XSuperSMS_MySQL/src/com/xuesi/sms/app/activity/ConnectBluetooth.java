package com.xuesi.sms.app.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.raylinks.ModuleControl;
import com.xuesi.sms.R;
import com.xuesi.sms.SmsConfig;

public class ConnectBluetooth extends SmsActivity {
	private static ModuleControl moduleControl = new ModuleControl();
	private byte flagCrc;

	private static String deviceMAC;
	private static String sConn;
	private static String sPower;
	private static String sFrequency;

	Button BtDisconnect;
	RadioGroup RgSearchDev;
	RadioButton RbPairedDev;
	RadioButton RbNewDev;
	ListView LvDevList;
	ProgressDialog progressDialog;

	private BluetoothAdapter myAdapter;
	private static SimpleAdapter simpleadapter;
	private ArrayList<String> tempList = new ArrayList<String>();
	private static List<HashMap<String, String>> deviceList = new ArrayList<HashMap<String, String>>();
	MyBroadCastReceiver myBroadCastReceiver = new MyBroadCastReceiver();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bluetooth);
		setTitle("蓝牙连接/断开");
		RgSearchDev = (RadioGroup) findViewById(R.id.RgSearchDev);
		RbPairedDev = (RadioButton) findViewById(R.id.RbPairedDev);
		RbNewDev = (RadioButton) findViewById(R.id.RbNewDev);
		LvDevList = (ListView) findViewById(R.id.LvDevList);
		BtDisconnect = (Button) findViewById(R.id.BtDisconnect);
		myAdapter = BluetoothAdapter.getDefaultAdapter();
		BtDisconnect.setOnClickListener(new BtDisconnectClickListener());
		RgSearchDev
				.setOnCheckedChangeListener(new RgSearchDevCheckedChangeListener());
		LvDevList.setOnItemClickListener(new LvDevListItemClickListener());
		IntentFilter foundFilter = new IntentFilter(
				BluetoothDevice.ACTION_FOUND);
		this.registerReceiver(myBroadCastReceiver, foundFilter);

		deviceList.clear();
		simpleadapter = new SimpleAdapter(this, deviceList,// 数据源
				R.layout.devlist_items,// ListItem的XML实现
				new String[] { "deviceInfo" },// 动态数组与Item对应的子项
				new int[] { R.id.TvDevItem }// 子项的id定义
		);

		LvDevList.setAdapter(simpleadapter);
		PairedDevList();
		for (Iterator<String> iterator = tempList.iterator(); iterator
				.hasNext();) {
			HashMap<String, String> item = new HashMap<String, String>();
			item.put("deviceInfo", iterator.next());
			deviceList.add(item);

			simpleadapter.notifyDataSetChanged();
		}

		LvDevList.setAdapter(simpleadapter);
		if (myAdapter == null) {
			// Device does not support Bluetooth
			showShortToast("该设备没有蓝牙");
			return;
		}

		if (!myAdapter.isEnabled()) {
			Intent enableBtIntent = new Intent(
					BluetoothAdapter.ACTION_REQUEST_ENABLE);
			startActivityForResult(enableBtIntent, 10);
		}
	}

	private void PairedDevList() {
		tempList.clear();

		if (!myAdapter.isEnabled()) {
			// 请求打开蓝牙设备
			Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
			startActivity(intent);
		}

		Set<BluetoothDevice> devices = myAdapter.getBondedDevices();

		String deviceStr = "";
		if (devices.size() > 0) {
			for (Iterator<BluetoothDevice> iterator = devices.iterator(); iterator
					.hasNext();) {
				BluetoothDevice bluetootdevice = (BluetoothDevice) iterator
						.next();

				deviceStr = bluetootdevice.getName() + "\n"
						+ bluetootdevice.getAddress();
				tempList.add(deviceStr);
			}
		}
	}

	class BtDisconnectClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {

			if (!SmsConfig.connFlag) {
				showShortToast("尚未连接蓝牙设备");
				return;
			}

			if (moduleControl.UhfReaderDisconnect()) {
				SmsConfig.connFlag = false;
				showShortToast("断开成功");
			} else {
				showShortToast("断开失败");
			}
		}
	}

	public class RgSearchDevCheckedChangeListener implements
			OnCheckedChangeListener {

		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId) {

			if (checkedId == RbPairedDev.getId()) {
				deviceList.clear();
				simpleadapter.notifyDataSetChanged();
				AddPairedDevList();
			} else {
				deviceList.clear();
				simpleadapter.notifyDataSetChanged();
				myAdapter.startDiscovery();

				progress("正在搜索", "请稍等……");
			}
		}
	}

	Handler handler = new Handler() {

		public void handleMessage(Message msg) {
			if (msg.what > 12) {
				progressDialog.cancel();
			}
			progressDialog.setProgress(msg.what);
			super.handleMessage(msg);
		}
	};

	class UpdateThread extends Thread {
		public void run() {
			handler.sendEmptyMessage(0);
			for (int i = 0; i <= 13; i++) {

				try {
					UpdateThread.sleep(1000);
				} catch (InterruptedException e) {

					e.printStackTrace();
				}

				handler.sendEmptyMessage(i);
			}
		}
	}

	public void progress(String title, String message) {
		progressDialog = new ProgressDialog(this);
		UpdateThread thread = new UpdateThread();
		progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progressDialog.setMessage(message);
		progressDialog.setTitle(title);
		progressDialog.setProgress(0);

		progressDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "取消搜索",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {

						myAdapter.cancelDiscovery();
						progressDialog.cancel();
					}
				});

		progressDialog.show();
		thread.start();
	}

	private void AddPairedDevList() {
		deviceList.clear();

		if (!myAdapter.isEnabled()) {
			// 请求打开蓝牙设备
			Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
			startActivity(intent);
		}

		Set<BluetoothDevice> devices = myAdapter.getBondedDevices();

		String deviceStr = "";
		if (devices.size() > 0) {
			for (Iterator<BluetoothDevice> iterator = devices.iterator(); iterator
					.hasNext();) {
				BluetoothDevice bluetootdevice = (BluetoothDevice) iterator
						.next();

				deviceStr = bluetootdevice.getName() + "\n"
						+ bluetootdevice.getAddress();

				HashMap<String, String> item = new HashMap<String, String>();
				item.put("deviceInfo", deviceStr);
				deviceList.add(item);

				simpleadapter.notifyDataSetChanged();
			}
		}
	}

	class LvDevListItemClickListener implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {

			if (SmsConfig.connFlag) {
				Toast.makeText(getApplicationContext(), "已连接", 0).show();
				return;
			}

			HashMap<String, String> item = (HashMap<String, String>) LvDevList
					.getItemAtPosition(position);
			String devInfo = item.get("deviceInfo");

			String tempStr[] = devInfo.split("\n");
			deviceMAC = tempStr[1];

			if (deviceMAC == "")
				return;
			else {

				AlertDialog.Builder builder = new Builder(ConnectBluetooth.this);
				builder.setMessage("确认连接：\n\n" + devInfo);
				builder.setTitle("提示");
				builder.setPositiveButton("确定",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {

								// 打开蓝牙连接
								byte[] bStatus = new byte[1];
								if (moduleControl.UhfReaderConnect(deviceMAC,
										bStatus, flagCrc)) {
									SmsConfig.connFlag = true;
									sConn = "连接成功";
									Log.v("BreakPoint", "connected");
									byte[] bPower = new byte[1];
									if (moduleControl.UhfGetPower(bPower,
											flagCrc)) {

										byte power = (byte) (bPower[0] & 0x7F);

										sPower = "功率：" + String.valueOf(power)
												+ "dBm";
									} else {
										sPower = "读取功率失败";
									}

									try {
										Thread.sleep(100);
									} catch (InterruptedException e) {

										e.printStackTrace();
									}
									byte[] bFreMode = new byte[1];
									byte[] bFreBase = new byte[1];
									byte[] bBaseFre = new byte[2];
									byte[] bChannNum = new byte[1];
									byte[] bChannSpc = new byte[1];
									byte[] bFreHop = new byte[1];
									if (moduleControl.UhfGetFrequency(bFreMode,
											bFreBase, bBaseFre, bChannNum,
											bChannSpc, bFreHop, flagCrc)) {

										// String freBase0 =
										// Integer.toHexString((int)bBaseFre[0]);
										// String freBase1 =
										// Integer.toHexString((int)bBaseFre[1]);
										//
										// short iFreBase0 =
										// Short.parseShort(freBase0, 16);
										// short iFreBase1 =
										// Short.parseShort(freBase1, 16);

										int iFreBase0 = bBaseFre[0] & 0xFF;
										int iFreBase1 = bBaseFre[1] & 0xFF;

										int freI = (iFreBase0 << 3)
												+ (iFreBase1 >> 5);
										int freD = 0;
										int eFreD = 0;
										int eFreI = 0;

										if (bFreBase[0] == 0) {
											// freD = bBaseFre[1]*50;
											freD = (iFreBase1 & 0x1F) * 50;
											eFreD = (freD + bChannSpc[0] * 50
													* (bChannNum[0] - 1)) % 1000;
											eFreI = freI
													+ (freD + bChannSpc[0]
															* 50
															* (bChannNum[0] - 1))
													/ 1000;

										} else {
											// freD = bBaseFre[1]*125;
											freD = (iFreBase1 & 0x1F) * 125;
											eFreD = (freD + bChannSpc[0] * 125
													* (bChannNum[0] - 1)) % 1000;
											eFreI = freI
													+ (freD + bChannSpc[0]
															* 125
															* (bChannNum[0] - 1))
													/ 1000;
										}

										sFrequency = "频率："
												+ String.valueOf(freI) + "."
												+ String.valueOf(freD) + "~"
												+ String.valueOf(eFreI) + "."
												+ String.valueOf(eFreD) + "MHz";

									} else {
										sFrequency = "读取频率失败";
									}

								} else {

									sConn = "连接失败";
								}
								if (SmsConfig.connFlag) {
									showShortToast(sConn + "\n" + sPower + "\n"
											+ sFrequency);
								} else {
									showShortToast(sConn);
								}

							}
						});

				builder.setNegativeButton("取消",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {

							}
						});

				builder.create().show();
			}
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if (myAdapter == null) {
			finish();
		} else {
			myAdapter.cancelDiscovery();
		}
		this.unregisterReceiver(myBroadCastReceiver);
	}

	public class MyBroadCastReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {

			// deviceList.clear();
			String action = intent.getAction();
			String deviceStr = "";
			if (BluetoothDevice.ACTION_FOUND.equals(action)) {
				BluetoothDevice device = intent
						.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

				if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
					deviceStr = device.getName() + "\n" + device.getAddress();

					HashMap<String, String> item = new HashMap<String, String>();
					item.put("deviceInfo", deviceStr);
					deviceList.add(item);

					// 逐项添加，并发送消息更新listview列表
					simpleadapter.notifyDataSetChanged();
					LvDevList.setSelection(deviceList.size() - 1);
				}
			}
		}
	}
}

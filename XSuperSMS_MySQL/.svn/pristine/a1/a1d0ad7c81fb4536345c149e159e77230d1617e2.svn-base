package com.xuesi.sms.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.Xml;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.xuesi.mos.util.MosLog;
import com.xuesi.sms.R;
import com.xuesi.sms.ServerApi;
import com.xuesi.sms.SmsApplication;
import com.xuesi.sms.bean.UpdateXml;

/**
 * 应用更新类<br>
 * 下载类
 * 
 * @author XS-PC014
 * 
 */
public class UpdateManager {
	/** LOG */
	private final String LOG_TAG = UpdateManager.class.getSimpleName();
	private final MosLog mLog = MosLog.getInstance(LOG_TAG, "XUESI-SMS");
	/** progress的次数 */
	private int mProgressNumber = 0;

	// url== http://192.168.1.232:8088/Update/UpdateInfo.xml
	// url== http://192.168.1.232:8088/Update/XSuperSMS_v2.0.0.0.apk
	private Context mContext;

	private static final int GET_INFO_SUCCESS = 10;
	private static final int SERVER_ERROR = 11;
	private static final int SERVER_URL_ERROR = 12;
	private static final int IO_ERROR = 13;
	private static final int XML_PARSER_ERROR = 14;

	/** 进度条与通知ui刷新的handler和msg常量 */
	private ProgressBar mProgress;
	private TextView mProText;
	/** 下载进度对话框 */
	private AlertDialog downloadDialog;

	public static final int STATUS_0 = 0;
	public static final int STATUS_1 = 1;
	/** 区别是否显示一个对话框 */
	private int mStatus;

	private String versionCheckUrl;
	private UpdateXml upXml;

	public UpdateManager(Context context, int status) {
		// TODO Auto-generated constructor stub
		this.mContext = context;
		this.mStatus = status;
	}

	/**
	 * 
	 * <p>
	 * Title: beginCheckAppVersion
	 * </p>
	 * <p>
	 * Description: 开始检查版本信息
	 * </p>
	 */
	public void checkVersion() {
		new Thread(new CheckVersionTask()) {
		}.start(); // 启动一个线程进行服务器连接判断版本号是否相同
	}

	private void toastLong(String msg) {
		Toast.makeText(mContext, msg, Toast.LENGTH_LONG).show();
	}

	private static class UpdateInfoParser {

		public static UpdateXml getUpdateInfo(InputStream is)
				throws XmlPullParserException, IOException {
			XmlPullParser parser = Xml.newPullParser(); // 取得XmlPullParser解析器，准备对XML进行解析
			parser.setInput(is, "UTF-8");
			UpdateXml upXml = new UpdateXml();
			int type = parser.getEventType();
			while (type != XmlPullParser.END_DOCUMENT) {
				if (type == XmlPullParser.START_TAG) {
					if ("fileName".equals(parser.getName())) {
						upXml.setFileName(parser.nextText());
					}
					if ("versionCode".equals(parser.getName())) {
						upXml.setVersionCode(Integer.parseInt(parser.nextText()));
					}
					if ("versionName".equals(parser.getName())) {
						upXml.setVersionName(parser.nextText());
					}
					if ("updateTime".equals(parser.getName())) {
						upXml.setUpdateTime(parser.nextText());
					}
					if ("description".equals(parser.getName())) {
						upXml.setDescription(parser.nextText());
					}
				}
				type = parser.next();
			}
			return upXml;
		}
	}

	private class CheckVersionTask implements Runnable {

		@Override
		public void run() {
			Message msg = Message.obtain();
			// 1、连接服务器
			try {
				versionCheckUrl = ServerApi.getInstance().getUpdateUrl();
				URL url = new URL(versionCheckUrl + "/UpdateInfo.xml");

				mLog.d("url== " + url.toString());

				HttpURLConnection conn = (HttpURLConnection) url
						.openConnection();
				conn.setConnectTimeout(5000);
				conn.setRequestMethod("GET");
				int code = conn.getResponseCode();
				if (code == 200) {
					InputStream is = conn.getInputStream(); // 取得服务器返回的内容
					upXml = UpdateInfoParser.getUpdateInfo(is); // 调用自己编写的方法，将输入流转换为appEntity对象
					msg.what = GET_INFO_SUCCESS;
				} else {
					msg.what = SERVER_ERROR;
				}
			} catch (MalformedURLException e) {
				msg.what = SERVER_URL_ERROR;
				e.printStackTrace();
			} catch (IOException e) {
				msg.what = IO_ERROR;
				e.printStackTrace();
			} catch (XmlPullParserException e) {
				msg.what = XML_PARSER_ERROR;
				e.printStackTrace();
			}
			upHandler.sendMessage(msg);
		}
	}

	private Handler upHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {

			switch (msg.what) {
			case XML_PARSER_ERROR:
				toastLong("XML解析异常");
				break;
			case IO_ERROR:
				toastLong("IO异常");
				break;
			case SERVER_URL_ERROR:
				toastLong("服务器URL出错");
				break;
			case SERVER_ERROR:
				// toastLong("服务器异常");
				break;
			case GET_INFO_SUCCESS:
				String pageName = mContext.getPackageName();
				try {
					int localVerCode = mContext.getPackageManager()
							.getPackageInfo(pageName, 0).versionCode;
					int serverVerCode = upXml.getVersionCode(); // 取得服务器上的版本号
					if (serverVerCode > localVerCode) {
						// if (mStatus == STATUS_0) {
						// toastLong("有新版本，请到系统设定中更新");
						// } else {
						File file = new File(SmsDir.getInstance()
								.getDownload_dir() + "/" + upXml.getFileName());
						if (file.exists()) {// 判断路径下是否有此名称文件
							file.delete();
						}
						afterCheckAppVersion(true);
						// }
					} else if (mStatus == STATUS_1) {
						afterCheckAppVersion(false);
					}
				} catch (NameNotFoundException e) {
					e.printStackTrace();
				}
				break;
			default:
				break;
			}
		}
	};

	public void afterCheckAppVersion(boolean flag) {
		AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
		builder.setTitle(mContext.getString(R.string.prompt));
		if (flag) {
			builder.setMessage(mContext.getString(R.string.msg_version_success));
			builder.setPositiveButton(R.string.update,
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
							showDownloadDialog();
						}
					});
			builder.setOnKeyListener(new OnKeyListener() {

				@Override
				public boolean onKey(DialogInterface paramDialogInterface,
						int keyCode, KeyEvent event) {
					// TODO Auto-generated method stub
					if (keyCode == KeyEvent.KEYCODE_BACK
							&& event.getRepeatCount() == 0) {
						SmsApplication.getInstance().exit();
					}
					return false;
				}
			});
			/*
			 * builder.setNegativeButton(R.string.update_cancel, new
			 * DialogInterface.OnClickListener() {
			 * 
			 * @Override public void onClick(DialogInterface dialog, int which)
			 * { dialog.dismiss(); } });
			 */
		} else {
			builder.setMessage(R.string.msg_version_failure);
			builder.setPositiveButton(R.string.confirm,
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					});
		}
		builder.create().show();
	}

	private void showDownloadDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
		builder.setTitle(mContext.getString(R.string.msg_downloding));
		final LayoutInflater inflater = LayoutInflater.from(mContext);
		View v = inflater.inflate(R.layout.progress_download, null);
		mProgress = (ProgressBar) v.findViewById(R.id.prodown_pb_);
		mProText = (TextView) v.findViewById(R.id.prodown_tv_);
		builder.setView(v);
		// builder.setNegativeButton("取消", new OnClickListener() {
		// @Override
		// public void onClick(DialogInterface dialog, int which) {
		// dialog.dismiss();
		// }
		// });
		downloadDialog = builder.create();
		downloadDialog.setCanceledOnTouchOutside(false);
		downloadDialog.show();

		upAsync asyncTask = new upAsync();
		asyncTask.execute();
	}

	class upAsync extends AsyncTask<Object, Integer, Object> {

		@Override
		protected Object doInBackground(Object... params) {
			// TODO Auto-generated method stub
			// 创建Http请求
			HttpClient client = new DefaultHttpClient();
			HttpGet get = new HttpGet(versionCheckUrl + "/"
					+ upXml.getFileName());

			mLog.d("HttpGet.getURI()==" + get.getURI());

			HttpResponse response;
			try {
				response = client.execute(get);
				// 获取返回的数据
				HttpEntity entity = response.getEntity();

				int fileSize = (int) entity.getContentLength();
				mLog.e("fileSize" + fileSize);
				InputStream is = entity.getContent();

				if (is == null) {
					throw new RuntimeException("stream is null");
				} else {
					// 创建本地文件
					File file = new File(
							SmsDir.getInstance().getDownload_dir(),
							upXml.getFileName());
					// 解析数据流到本地
					FileOutputStream fos = new FileOutputStream(file);
					byte[] buf = new byte[1024];
					int read = -1;
					int curSize = 0;

					do {
						read = is.read(buf);
						mLog.e("read" + read);
						if (read <= 0)
							break;
						fos.write(buf, 0, read);
						curSize = curSize + read;

						// 当调用这个方法的时候会自动去调用onProgressUpdate方法，传递下载进度
						publishProgress((int) (curSize * 100.0f / fileSize));
						Thread.sleep(10);
					} while (true);
					// 关闭数据流
					is.close();
					fos.close();
				}
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			// 这里的返回值将会被作为onPostExecute方法的传入参数
			return null;
		}

		/**
		 * 当一个下载任务成功下载完成的时候回来调用这个方法，这里的result参数就是doInBackground方法的返回值
		 */
		@Override
		protected void onPostExecute(Object result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);

			downloadDialog.dismiss();
			// 下载完成安装apk
			installNewApk();
		}

		/**
		 * 更新下载进度，当publishProgress方法被调用的时候就会自动来调用这个方法
		 */
		@Override
		protected void onProgressUpdate(Integer... values) {
			// TODO Auto-generated method stub
			mProgress.setProgress(values[0]);
			mProText.setText(values[0] + "%");
			super.onProgressUpdate(values);
		}
	}

	/**
	 * 
	 * <p>
	 * Title: installNewApk
	 * </p>
	 * <p>
	 * Description: 调系统API，执行程序安装
	 * </p>
	 * 
	 * @param appName
	 */
	private void installNewApk() {
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setDataAndType(Uri.fromFile(new File(SmsDir.getInstance()
				.getDownload_dir(), upXml.getFileName())),
				"application/vnd.android.package-archive");
		mContext.startActivity(intent);
		// if (Build.VERSION.SDK_INT >= 14) {
		// API 等级
		android.os.Process.killProcess(android.os.Process.myPid());
		// }
	}

}

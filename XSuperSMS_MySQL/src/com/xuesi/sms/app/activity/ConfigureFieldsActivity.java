package com.xuesi.sms.app.activity;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Spinner;

import com.xuesi.sms.R;
import com.xuesi.sms.ServerApi;

public class ConfigureFieldsActivity extends SmsActivity {

	private StringBuffer sb;
	private static final String PAGEPATH = "?pagepath=";
	private static final String FACTORYCODE = "&factorycode=";
	private Spinner sp_configure;
	private WebView webView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_configurefields);
	}

	@Override
	protected void initContentView() {
		// TODO Auto-generated method stub
		super.initContentView();
		setTopTitle(R.string.configure_list_fields);
		setRefreshView(View.INVISIBLE);
		sb = new StringBuffer();
		sp_configure = (Spinner) findViewById(R.id.more_configurefields);
		webView = (WebView) findViewById(R.id.setting_configurefields);
		sp_configure.setOnItemSelectedListener(new mySelectItemListener());
		initWebView();
	}

	/**
	 * 初始化WebView
	 */
	private void initWebView() {
		WebSettings webSettings = webView.getSettings();
		webSettings.setJavaScriptEnabled(true);
		webSettings.setSupportZoom(false);
		webSettings.setBuiltInZoomControls(false);
		webSettings.setUseWideViewPort(true);
		webView.setInitialScale(10);
		webView.setBackgroundColor(0);
		webView.getBackground().setAlpha(0);
		webView.loadUrl(getPage());
		webView.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				// TODO Auto-generated method stub
				// 返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
				view.loadUrl(url);
				return true;
			}
		});
		webView.setHorizontalScrollbarOverlay(true);
	}

	/**
	 * @return 配置的页面
	 */
	private String getPage() {
		sb.setLength(0);
		sb.append(ServerApi.CONFIGURE_FIELDS);
		sb.append(PAGEPATH);
		if ("钢板".equals(sp_configure.getSelectedItem().toString())) {
			sb.append(ServerApi.CONFIGURE_SHEET);

		} else if ("采购单号".equals(sp_configure.getSelectedItem().toString())) {
			sb.append(ServerApi.CONFIGURE_BILLS);
		}
		sb.append(FACTORYCODE);
		sb.append(ServerApi.factoryCode);
		return sb.toString();
	}

	/**
	 * 更新webview
	 */
	private void notifySetChange() {
		webView.loadUrl(getPage());
	}

	class mySelectItemListener implements OnItemSelectedListener {

		@Override
		public void onItemSelected(AdapterView<?> parent, View view,
				int position, long id) {
			// TODO Auto-generated method stub
			notifySetChange();
		}

		@Override
		public void onNothingSelected(AdapterView<?> parent) {
			// TODO Auto-generated method stub

		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

}

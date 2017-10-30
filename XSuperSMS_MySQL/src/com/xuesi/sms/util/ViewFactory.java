package com.xuesi.sms.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.ImageView;

import com.xuesi.mos.libs.volley.RequestQueue;
import com.xuesi.mos.libs.volley.toolbox.ImageLoader;
import com.xuesi.mos.libs.volley.toolbox.ImageLoader.ImageListener;
import com.xuesi.mos.libs.volley.toolbox.Volley;
import com.xuesi.sms.R;
import com.xuesi.sms.cache.BitmapCache;

public class ViewFactory {
	/**
	 * 获取ImageView视图的同时加载显示url
	 * 
	 * @param text
	 * @return
	 */
	public static ImageView getImageView(Context context, String url,
			int layoutId) {
		// ImageView imageView =
		// (ImageView)LayoutInflater.from(context).inflate(
		// R.layout.view_banner, null);
		// ImageLoader.getInstance().displayImage(url, imageView);
		// return imageView;
		ImageView imageView = (ImageView) LayoutInflater.from(context).inflate(
				layoutId, null);
		RequestQueue requestQueue = Volley.newRequestQueue(context);
		/**
		 * ImageLoader的构造函数接收两个参数，第一个参数就是RequestQueue对象，第二个参数是一个ImageCache对象，
		 * 这里我们先new出一个空的ImageCache的实现即可
		 */
		ImageLoader imageLoader = new ImageLoader(requestQueue,
				new BitmapCache());
		/**
		 * 通过调用ImageLoader的getImageListener()方法能够获取到一个ImageListener对象，
		 * getImageListener
		 * ()方法接收三个参数，第一个参数指定用于显示图片的ImageView控件，第二个参数指定加载图片的过程中显示的图片
		 * ，第三个参数指定加载图片失败的情况下显示的图片
		 * */
		ImageListener listener = ImageLoader.getImageListener(imageView,
				R.drawable.publicloading, R.drawable.xsupercutting);
		// 3.获取图片
		imageLoader.get(url, listener);
		return imageView;
	}
}

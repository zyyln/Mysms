package com.xuesi.sms.util;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.RelativeLayout;

/**
 * 图片处理工具类
 * 
 * @author dingyu
 * 
 */
public class BitmapUtil {
	private final String TAG_LOG = BitmapUtil.class.getSimpleName();
	private static BitmapUtil bitmapUtil;

	public static BitmapUtil getInstance() {
		if (null == bitmapUtil) {
			bitmapUtil = new BitmapUtil();
		}
		return bitmapUtil;
	}

	/**
	 * 设置缩放图片背景
	 * 
	 * @param view
	 * @param scale
	 */
	public void setZoomPicBg(Context context, View view, int imgId, float scale) {
		Resources res = context.getResources();
		Bitmap mBitmap = BitmapFactory.decodeResource(res, imgId);
		Drawable drawable = new BitmapDrawable(mBitmap);
		RelativeLayout.LayoutParams rLayoutParams = (RelativeLayout.LayoutParams) view.getLayoutParams();
		int height = mBitmap.getHeight();
		int newHeight = 0;
		int tempScale = (int) scale;
		// if(tempScale>1){
		//
		// //newHeight = height;
		// view.setBackgroundResource(imgId);
		// }
		// else{
		// newHeight = (int) (scale * height);
		// }
		// rLayoutParams.height = newHeight;
		// view.setLayoutParams(rLayoutParams);
		// view.setBackgroundDrawable(drawable);

		if (tempScale < 1) {
			newHeight = (int) (scale * height);
			rLayoutParams.height = newHeight;
			view.setLayoutParams(rLayoutParams);
			view.setBackgroundDrawable(drawable);
		} else {
			view.setBackgroundResource(imgId);
		}
	}

}

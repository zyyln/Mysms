/*
 * MosToast.java
 * com.xuesi.mos.util
 *
 * Function： TODO 
 *
 *   ver     date      		author
 * ──────────────────────────────────
 *   		 2015-10-15 		lzq
 *
 * Copyright (c) 2015, NJXS All Rights Reserved.
 */

package com.xuesi.mos.util;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

/**
 * ClassName:MosToast
 * 
 * Function: TODO ADD FUNCTION
 * 
 * Reason: TODO ADD REASON
 * 
 * @author lzq
 * @version
 * @since Ver 1.1
 * @Date 2015-10-15
 */
public class MosToast {

	private static Toast toast = null;

	public static void TextToast(Context context, CharSequence text, int duration) {
		toast = Toast.makeText(context, text, duration);

		toast.show();
	}

	public static void ImageToast(Context context, int ImageResourceId, CharSequence text, int duration) {
		toast = Toast.makeText(context, text, duration);

		toast.setGravity(17, 0, 0);

		View toastView = toast.getView();

		ImageView img = new ImageView(context);
		img.setImageResource(ImageResourceId);

		LinearLayout ll = new LinearLayout(context);

		ll.addView(img);
		ll.addView(toastView);

		toast.setView(ll);

		toast.show();
	}

}

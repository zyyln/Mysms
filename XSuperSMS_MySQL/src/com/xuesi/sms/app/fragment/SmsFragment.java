package com.xuesi.sms.app.fragment;

import android.app.Dialog;
import android.graphics.Color;
import android.view.Gravity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xuesi.mos.app.MosFragment;
import com.xuesi.mos.util.MosLog;
import com.xuesi.sms.R;
import com.xuesi.sms.ServerApi;
import com.xuesi.sms.app.activity.SmsActivity;

import org.json.JSONException;
import org.json.JSONObject;

public class SmsFragment extends MosFragment {
    /**
     * LOG
     */
    private final String LOG_TAG = SmsActivity.class.getSimpleName();
    private final MosLog mLog = MosLog.getInstance(LOG_TAG, "XUESI-SMS");
    /**
     * progress的次数
     */
    private int mProgressNumber = 0;

    @Override
    protected void initContentView() {
        // TODO Auto-generated method stub

    }

    /**
     * 获取通用JSON<br>
     * 待修改
     *
     * @return
     * @throws JSONException
     */
    protected JSONObject getRequstJson() {
        JSONObject json = new JSONObject();
        try {
            json.put(ServerApi.PARA_FACTORYCODE, ServerApi.factoryCode);
            json.put(ServerApi.COOKIE_MILLCODE, ServerApi.millCode);
            json.put(ServerApi.COOKIE_PASSPORT, ServerApi.passport);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return json;
    }

    /**
     * 本页面所有的网络请求。
     *
     * @param txt [?] 对应的需要发送的单号
     * @param url 对应的tag
     */
    public void myNetwork(String url, String... txt) {
        mProgressNumber++;
        if (mProgressNumber == 1) {
            showProgressDialog("加载中", false, false);
        }
    }

    @Override
    public void onRequestFail(String url, Exception ex) {
        // TODO Auto-generated method stub
        mLog.d("请求连接失败，请稍候再试！");
        showShortToast("请求连接失败，请稍候再试！");
        if (mProgressNumber > 0) {
            mProgressNumber--;
        }
        if (mProgressNumber == 0) {
            dismissProgressDialog();
        }
    }

    @Override
    public void onRequestSuccess(String url, Object obj) {
        // TODO Auto-generated method stub
        if (mProgressNumber > 0) {
            mProgressNumber--;
        }
        if (mProgressNumber == 0) {
            dismissProgressDialog();
        }
        mLog.init(url, LOG_TAG);
        mLog.i(LOG_TAG + obj.toString());
    }

    private Dialog loadingDialog = null;

    /**
     * showProgressDialog(创建等待框) (这里描述这个方法适用条件 – 可选)
     *
     * @param msg
     * @param isCancel
     * @param isRight
     * @return void
     * @throws
     * @since 1.0.0
     */
    public void showProgressDialog(String msg, boolean isCancel, boolean isRight) {
        if (this.loadingDialog == null) {
            this.loadingDialog = creatProgressDialog(msg, isCancel, isRight);
            this.loadingDialog.show();
        }
    }

    private Dialog creatProgressDialog(String msg, boolean isCancel,
                                       boolean isRight) {
        LinearLayout.LayoutParams wrap_content = new LinearLayout.LayoutParams(
                -2, -2);
        LinearLayout.LayoutParams wrap_content0 = new LinearLayout.LayoutParams(
                -2, -2);
        LinearLayout main = new LinearLayout(getActivity());
        main.setBackgroundColor(Color.WHITE);
        if (isRight) {
            main.setOrientation(LinearLayout.HORIZONTAL);
            wrap_content.setMargins(10, 0, 35, 0);
            wrap_content0.setMargins(35, 25, 0, 25);
        } else {
            main.setOrientation(LinearLayout.VERTICAL);
            wrap_content.setMargins(10, 5, 10, 15);
            wrap_content0.setMargins(35, 25, 35, 0);
        }
        main.setGravity(Gravity.CENTER);
        ImageView spaceshipImage = new ImageView(getActivity());
        spaceshipImage.setImageResource(R.drawable.publicloading);
        TextView tipTextView = new TextView(getActivity());
        tipTextView.setText("请稍候...");

        Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(getActivity(),
                R.anim.loading_animation);

        spaceshipImage.startAnimation(hyperspaceJumpAnimation);
        if ((msg != null) && (!"".equals(msg))) {
            tipTextView.setText(msg);
        }
        Dialog loadingDialog = new Dialog(getActivity(), R.style.loading_dialog);
        loadingDialog.setCancelable(isCancel);
        // this.cancelable = Boolean.valueOf(isCancel);
        main.addView(spaceshipImage, wrap_content0);
        main.addView(tipTextView, wrap_content);
        LinearLayout.LayoutParams fill_parent = new LinearLayout.LayoutParams(
                -1, -1);
        loadingDialog.setContentView(main, fill_parent);

        return loadingDialog;
    }

    public void dismissProgressDialog() {
        if ((this.loadingDialog != null) && (this.loadingDialog.isShowing())) {
            this.loadingDialog.dismiss();
            this.loadingDialog = null;
        }
    }
}

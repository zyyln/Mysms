package com.xuesi.sms.app.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.device.ScanManager;
import android.media.AudioManager;
import android.media.SoundPool;
import android.media.ToneGenerator;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.motorolasolutions.adc.decoder.BarCodeReader;
import com.motorolasolutions.adc.decoder.SE4500_DEMO;
import com.xuesi.mos.util.MosLog;
import com.xuesi.sms.R;
import com.xuesi.sms.SmsApplication;
import com.xuesi.sms.SmsConfig;

/**
 * 需要条形码扫描功能的activity继承此基类<br>
 * 包含条形码扫描的设备有UROVO、KT45、TT35<br>
 *
 * @author XS-PC014
 */
public abstract class ScanBaseActivity extends SmsActivity implements
        BarCodeReader.DecodeCallback {
    /**
     * LOG
     */
    private final String TAG_LOG = ScanBaseActivity.class.getName();
    private final MosLog mLog = MosLog.getInstance(TAG_LOG, "XUESI-MES");

    /**
     * BarCodeReader specifics
     */
    private BarCodeReader bcr = null;

    /** decode beep enable */
    // private boolean beepMode = true;
    /**
     * system
     */
    private ToneGenerator tg = null;
    private int trigMode = BarCodeReader.ParamVal.AUTO_AIM;

    static long decode_start = 0;
    static long decode_end = 0;

    private boolean STATE_ISDECODING = false;
    // states
    private int state = STATE_IDLE;
    /**
     * 闲置
     */
    static final int STATE_IDLE = 0;
    /**
     * 解码
     */
    static final int STATE_DECODE = 1;
    /** 免提 */
    // static final int STATE_HANDSFREE = 2;
    /** 预览snapshot preview mode */
    // static final int STATE_PREVIEW = 3;
    /** 快照 */
    // static final int STATE_SNAPSHOT = 4;
    /** 视频 */
    // static final int STATE_VIDEO = 5;

    /** 加载libs中的so包 */
    static {
        if (Build.MODEL.equals(SmsConfig.TAG_KT45)) {
            System.loadLibrary("IAL");
            System.loadLibrary("SDL");
            System.loadLibrary("barcodereader44");
        } else if (Build.DEVICE.equals(SmsConfig.TAG_TT35)) {
            System.loadLibrary("IAL_TT35");// 已删除
            System.loadLibrary("SDL_TT35");// 已删除
            System.loadLibrary("barcodereader");// 已删除
        }
    }

    // UROVO_PAD扫描部分
    /**
     * 扫描头管理类
     */
    private ScanManager scanManager;
    private SoundPool soundPool = null;
    private int soundid;
    private BroadcastReceiver receiver;

    // 手机条形码扫描
    /**
     * 手机条形码扫描按钮
     */
    private ImageView img_scan;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SmsApplication.getInstance().addActivity(this);// 添加到Activity堆栈
        if (Build.MODEL.equals(SmsConfig.TAG_KT45)
                || Build.DEVICE.equals(SmsConfig.TAG_TT35)) {
            // sound
            tg = new ToneGenerator(AudioManager.STREAM_MUSIC,
                    ToneGenerator.MAX_VOLUME);
        }
        if (Build.MODEL.equals(SmsConfig.TAG_UROVO)) {
            initUROVO();
        }
    }

    @Override
    protected void initContentView() {
        // TODO Auto-generated method stub
        super.initContentView();
        img_scan = (ImageView) findViewById(R.id.img_scan);
        if (img_scan != null) {
            img_scan.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View view) {
                    // TODO Auto-generated method stub
                    openActivity(CaptureActivity.class, 0);
                }
            });
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 && resultCode == RESULT_OK) {
            String barcode = null;
            if (data != null) {

                barcode = data.getStringExtra("SCAN_RESULT");
            }

            if ("".equals(barcode) || barcode == null) {

            } else {

                onScanResult(barcode);

            }
        }
    }

    /**
     * 初始化扫描头相关
     */
    private void initUROVO() {
        try {
            scanManager = new ScanManager();
            receiver = new scanReceiver(); // 动态注册BroadcastReceiver
            IntentFilter filter = new IntentFilter();
            // 接收扫描头的action
            filter.addAction("urovo.rcv.message");
            registerReceiver(receiver, filter);
            soundPool = new SoundPool(1, AudioManager.STREAM_NOTIFICATION, 100);
            soundid = soundPool.load("/etc/Scan_new.ogg", 1);
            boolean powerState = scanManager.getScannerState();
            if (!powerState) {
                scanManager.openScanner();
            }
            scanManager.resetScannerParameters();
            scanManager.switchOutputMode(0);
        } catch (ExceptionInInitializerError e) {
            e.printStackTrace();
        } catch (NoClassDefFoundError e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 初始化HeneyWell
     */

    /**
     * 获取条形码的广播
     */
    class scanReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            soundPool.play(soundid, 1, 1, 0, 0, 1);
            if (action.equals("urovo.rcv.message")) {
                byte[] barocode = intent.getByteArrayExtra("barocode");
                int barCodeLen = intent.getIntExtra("length", 0);
                byte temp = intent.getByteExtra("barcodeType", (byte) 0);
                String barcode = new String(barocode, 0, barCodeLen);
                dspData(barcode);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        state = STATE_IDLE;
        try {
            if (Build.MODEL.equals(SmsConfig.TAG_KT45)) {
                bcr = BarCodeReader.open(getApplicationContext());
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
        doSetParam(BarCodeReader.ParamNum.QR_INVERSE,
                BarCodeReader.ParamVal.INVERSE_AUTOD);
        doSetParam(BarCodeReader.ParamNum.DATAMATRIX_INVERSE,
                BarCodeReader.ParamVal.INVERSE_AUTOD);
    }

    /**
     * set param
     *
     * @param num
     * @param val
     * @return
     */
    private int doSetParam(int num, int val) {
        String s = "";
        int ret = bcr.setParameter(num, val);
        if (ret != BarCodeReader.BCR_ERROR) {
            if (num == BarCodeReader.ParamNum.PRIM_TRIG_MODE) {
                trigMode = val;
                if (val == BarCodeReader.ParamVal.HANDSFREE) {
                    s = "HandsFree";
                } else if (val == BarCodeReader.ParamVal.AUTO_AIM) {
                    s = "AutoAim";
                    ret = bcr
                            .startHandsFreeDecode(BarCodeReader.ParamVal.AUTO_AIM);
                    if (ret != BarCodeReader.BCR_SUCCESS) {
                    }
                } else if (val == BarCodeReader.ParamVal.LEVEL) {
                    s = "Level";
                }
            }
        } else {
            s = " FAILED (" + ret + ")";
        }
        return ret;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case 0:

                break;
            case SE4500_DEMO.KEYCODE_KT45:// 134
                if (Build.MODEL.equals(SmsConfig.TAG_KT45)) {
                    if (STATE_ISDECODING == false) {
                        decode_start = SystemClock.elapsedRealtime();
                        doDecode();
                        STATE_ISDECODING = true;
                    }
                }
                break;
            case SE4500_DEMO.KEYCODE_TT35:// 135
                if (Build.DEVICE.equals(SmsConfig.TAG_TT35)) {
                    if (STATE_ISDECODING == false) {
                        decode_start = SystemClock.elapsedRealtime();
                        doDecode();
                        STATE_ISDECODING = true;
                    }
                }
                break;
            default:
                break;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 开始扫描 start a decode session
     */
    private void doDecode() {
        if (setIdle() != STATE_IDLE)
            return;

        state = STATE_DECODE;
        bcr.startDecode(); // start decode (callback gets results)
    }

    /**
     * 设置状态
     */
    private int setIdle() {
        int prevState = state;
        // for states taking time to chg/end
        int ret = prevState;

        state = STATE_IDLE;
        switch (prevState) {
            case STATE_DECODE:
                bcr.stopDecode();
                break;
            default:
                ret = STATE_IDLE;
        }
        return ret;
    }

    /**
     * 扫描后，响"嘟"
     */
    private void beep() {
        if (tg != null)
            tg.startTone(ToneGenerator.TONE_CDMA_NETWORK_BUSY_ONE_SHOT);
    }

    /**
     * BarCodeReader回调方法
     */
    @Override
    public void onDecodeComplete(int symbology, int length, byte[] data,
                                 BarCodeReader reader) {
        // BarCodeReader回调方法
        if (state == STATE_DECODE)
            state = STATE_IDLE;

        if (length > 0) {
            decode_end = SystemClock.elapsedRealtime();
            mLog.d("#######time:" + (decode_end - decode_start));
            beep();

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
            switch (length) {
                case BarCodeReader.DECODE_STATUS_TIMEOUT:
                    break;

                case BarCodeReader.DECODE_STATUS_CANCELED:
                    break;

                case BarCodeReader.DECODE_STATUS_ERROR:
                    break;

                default:
                    break;
            }
        }
        STATE_ISDECODING = false;
    }

    @Override
    public void onEvent(int event, int info, byte[] data, BarCodeReader reader) {
        // BarCodeReader回调方法
    }

    /**
     * dspData(接收扫描结果)
     *
     * @param s void
     * @throws
     * @since 1.0.0
     */
    private void dspData(String s) {
        // if (null != barcodeET) {
        // barcodeET.setText(s);
        // }
        this.onScanResult(s);
    }

    protected abstract void onScanResult(String barcode);


    @Override
    protected void onPause() {
        super.onPause();
        if (bcr != null) {
            setIdle();
            bcr.release();
            bcr = null;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 关闭广播
        if (receiver != null) {
            unregisterReceiver(receiver);
        }
    }

}

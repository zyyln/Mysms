package sms.xuesi.com.myview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.EmbossMaskFilter;
import android.graphics.Paint;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;

import sms.xuesi.com.myapplication.R;

/**
 * Created by XS-PC014 on 2017/11/20.
 */

public class CanvasView extends View {
    Paint paint;

    public CanvasView(Context context) {
        super(context);
    }

    public CanvasView(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();
    }

    public CanvasView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        paint.setColor(Color.parseColor("#FF9800"));
        /**
         *线性
         * x0 y0:起始点坐标
         * x1 y1:终点坐标
         * int colors[]：渐变的颜色数组
         */
//        Shader shader = new LinearGradient(200, 150, 250, 250, Color.parseColor("#FF9800"),
//                Color.parseColor("#ffffff"), Shader.TileMode.CLAMP);
//        Shader shader = new LinearGradient(200, 150, 200, 250, new int[]{Color.RED, Color.YELLOW, Color.BLACK, Color.WHITE,
//                Color.GREEN}, null, Shader.TileMode.CLAMP);
        /**
         *辐射渐变
         * x Y:圆心坐标
         * radius :第一个颜色的半径
         *然后一直扩散到canvas的外边界
         */
//        Shader shader = new RadialGradient(200, 200, 10, Color.RED,
//                Color.parseColor("#FF9800"), Shader.TileMode.REPEAT);
        /**
         * 扫描渐变
         * cx cy ：扫描的中心
         * color0：扫描的起始颜色
         * color1：扫描的终止颜色
         */
//        Shader shader = new SweepGradient(200, 200, Color.parseColor("#FF9800"),
//                Color.parseColor("#E91E63"));
        /**
         * BitmapShader
         * bitmap：用来做模板的 Bitmap 对象
         * tileX：横向的 TileMode
         * tileY：纵向的 TileMode。
         */
        BitmapFactory.Options options = new BitmapFactory.Options();
//        options.inSampleSize = 5;//直接设置它的压缩率，表示1/2
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.img);
        Shader shader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        // 第一个 Shader：头像的 Bitmap

        Bitmap bitmap1 = BitmapFactory.decodeResource(getResources(), R.mipmap.img);
        Shader shader1 = new BitmapShader(bitmap1, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);

// 第二个 Shader：从上到下的线性渐变（由透明到黑色）
//        BitmapFactory.Options options = new BitmapFactory.Options();
//        options.inSampleSize = 3;//直接设置它的压缩率，表示1/2
//        Bitmap bitmap2 = BitmapFactory.decodeResource(getResources(), R.mipmap.img21);
//        Shader shader2 = new BitmapShader(bitmap2, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);

// ComposeShader：结合两个 Shader
//        Shader shader = new ComposeShader(shader1, shader2, PorterDuff.Mode.DST_IN);
//        paint.setShader(shader1);
        /**
         * color 合成
         */

//        ColorFilter lightingColorFilter = new LightingColorFilter(0xffffff, 0x000000);
//        ColorFilter porterDuffColorFilter = new PorterDuffColorFilter(Color.RED, MULTIPLY);
//        paint.setColorFilter(porterDuffColorFilter);
        paint.setShader(shader);
        //模糊效果
//        paint.setMaskFilter(new BlurMaskFilter(50, BlurMaskFilter.Blur.INNER));
        paint.setMaskFilter(new EmbossMaskFilter(new float[]{0, 1, 1}, 0.2f, 8, 10));
        canvas.drawCircle(getWidth() /    2, getHeight() / 2, getWidth() / 4, paint);


//        paint.getFillPath();

////        canvas.drawBitmap();
//        canvas.drawCircle(180, 180, 10, paint);
//        canvas.drawCircle(220, 180, 10, paint);
//        paint.setColor(Color.RED);
//        paint.setStyle(Paint.Style.STROKE);
//        RectF oval = new RectF(180, 220, 220, 240);
//        canvas.drawArc(oval, 135, -90, false, paint);    paint
    }

    //计算图片的缩放值
    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;//获取图片的高
        final int width = options.outWidth;//获取图片的框
        int inSampleSize = 4;
        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;//求出缩放值
    }
}

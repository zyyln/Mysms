package sms.xuesi.com.myview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import sms.xuesi.com.myapplication.R;

import static java.lang.Math.abs;
import static java.lang.Math.floor;


/**
 * Created by XS-PC014 on 2017/10/24.
 */

public class ScrollingImageView extends View {
    private int speed = 0;
    private Bitmap bitmap = null;

    private Rect clipBounds = new Rect();
    private int offset = 0;

    private boolean isStarted;

    public ScrollingImageView(Context context) {
        super(context);
    }

    public ScrollingImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.ParallaxView, 0, 0);
        try {
            speed = ta.getDimensionPixelSize(R.styleable.ParallaxView_speed, 10);
            bitmap = BitmapFactory.decodeResource(getResources(), ta.getResourceId(R.styleable.ParallaxView_src, 0));
        } finally {
            ta.recycle();
        }
        start();
    }

    public void start() {
        if (!isStarted) {
            isStarted = true;
            postInvalidateOnAnimation();
        }
    }

    /**
     * Stop the animation
     */
    public void stop() {
        if (isStarted) {
            isStarted = false;
            invalidate();
        }
    }

    public ScrollingImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), bitmap.getHeight());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (canvas == null) {
            return;
        }

        canvas.getClipBounds(clipBounds);

        int normalizedOffset = offset;
        int layerWidth = bitmap.getWidth();
        if (offset < -layerWidth) {
            offset += (int) (floor(abs(normalizedOffset) / (float) layerWidth) * layerWidth);
        }

        int left = offset;
        while (left < clipBounds.width()) {
            canvas.drawBitmap(bitmap, getBitmapLeft(layerWidth, left), 0, null);
            left += layerWidth;
        }

        if (isStarted) {
            offset -= speed;
            postInvalidateOnAnimation();
        }
    }

    private float getBitmapLeft(int layerWidth, int left) {
        float bitmapLeft = left;
        if (speed < 0) {
            bitmapLeft = clipBounds.width() - layerWidth - left;
        }
        return bitmapLeft;
    }
}

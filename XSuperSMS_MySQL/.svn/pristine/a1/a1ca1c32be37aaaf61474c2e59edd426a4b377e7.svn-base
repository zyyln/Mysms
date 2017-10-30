/**
 * MyDialChart.java
 * com.xuesi.sms.drawdial
 *
 * Function： TODO 
 *
 *   ver     date      		author
 * ──────────────────────────────────
 *   		 2015-10-22 		ZYY
 *
 * Copyright (c) 2015, NJXS All Rights Reserved.
 */

/**
 * 系统项目名称
 * com.xuesi.sms.drawdial
 * MyDialChart.java
 * 
 * 2015-10-22
 */

package com.xuesi.sms.chart;

import org.achartengine.chart.DialChart;
import org.achartengine.model.CategorySeries;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.DialRenderer;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RadialGradient;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.opengl.GLSurfaceView.Renderer;
import android.util.Log;

public class MyDialChart extends DialChart {

    private static final long serialVersionUID = 1L;
    protected CategorySeries mDataset;
    protected DialRenderer mRenderer;

    public MyDialChart(CategorySeries dataset, DialRenderer renderer) {
        super(dataset, renderer);
        mDataset = dataset;
        mRenderer = renderer;
    }

    @Override
    public void draw(Canvas canvas, int x, int y, int width, int height, Paint paint) {
        paint.setAntiAlias(mRenderer.isAntialiasing());
        paint.setStyle(Paint.Style.FILL);
        paint.setTextSize(mRenderer.getLabelsTextSize());
        /** 设置legendSize */
        // int legendSize = getLegendSize(mRenderer, height / 5, 0.0F);
        int left = x;
        int top = y;
        int right = x + width;

        int sLength = mDataset.getItemCount();
        String[] titles = new String[sLength];
        for (int i = 0; i < sLength; i++) {
            titles[i] = mDataset.getCategory(i);
        }

        // if (mRenderer.isFitLegend()) {
        // legendSize = drawLegend(canvas, mRenderer, titles, left, right, y,
        // width, height,
        // legendSize, paint, true);
        // }
        int bottom = y + height;

        int mRadius = Math.min(Math.abs(right - left), Math.abs(bottom - top));
        int radius = (int) (mRadius * 0.35D * mRenderer.getScale());
        if (mCenterX == Integer.MAX_VALUE) {
            mCenterX = ((left + right) / 2);
        }
        if (mCenterY == Integer.MAX_VALUE) {
            mCenterY = ((bottom + top) / 2);
        }
        // float shortRadius = radius * 0.9F;
        float shortRadius = radius * 1.1F;
        float longRadius = radius * 1.2F;

        double min = mRenderer.getMinValue();
        double max = mRenderer.getMaxValue();
        double angleMin = mRenderer.getAngleMin();
        double angleMax = mRenderer.getAngleMax();
        drawMyBackground(mRenderer, canvas, mCenterX, mCenterY, (int) longRadius, paint);
        if ((!mRenderer.isMinValueSet()) || (!mRenderer.isMaxValueSet())) {
            int count = mRenderer.getSeriesRendererCount();
            for (int i = 0; i < count; i++) {
                double value = mDataset.getValue(i);
                if (!mRenderer.isMinValueSet()) {
                    min = Math.min(min, value);
                }
                if (!mRenderer.isMaxValueSet()) {
                    max = Math.max(max, value);
                }
            }
        }
        if (min == max) {
            min *= 0.5D;
            max *= 1.5D;
        }

        paint.setColor(mRenderer.getLabelsColor());
        double minorTicks = mRenderer.getMinorTicksSpacing();
        double majorTicks = mRenderer.getMajorTicksSpacing();
        if (minorTicks == Double.MAX_VALUE) {
            minorTicks = (max - min) / 30.0D;
        }
        if (majorTicks == Double.MAX_VALUE) {
            majorTicks = (max - min) / 10.0D;
        }
        drawTicks(canvas, min, max, angleMin, angleMax, mCenterX, mCenterY, longRadius, radius,
                minorTicks, paint, false);
        drawTicks(canvas, min, max, angleMin, angleMax, mCenterX, mCenterY, longRadius,
                shortRadius, majorTicks, paint, true);

        int count = mRenderer.getSeriesRendererCount();
        for (int i = 0; i < count; i++) {
            double angle = getAngleForValue(mDataset.getValue(i), angleMin, angleMax, min, max);
            // paint.setColor(mRenderer.getSeriesRendererAt(i).getColor());
            paint.setColor(Color.RED);
            boolean type = mRenderer.getVisualTypeForIndex(i) == DialRenderer.Type.ARROW;
            drawNeedle(canvas, angle, mCenterX, mCenterY, shortRadius, type, paint);
        }
        // drawLegend(canvas, mRenderer, titles, left, right, y, width, height,
        // legendSize, paint,
        // false);
        drawTitle(canvas, mCenterX, mCenterY, (int) longRadius, paint);
    }

    public double getAngleForValue(double value, double minAngle, double maxAngle, double min,
            double max) {
        double angleDiff = maxAngle - minAngle;
        double diff = max - min;
        return Math.toRadians(minAngle + (value - min) * angleDiff / diff);
    }

    public void drawTicks(Canvas canvas, double min, double max, double minAngle, double maxAngle,
            int centerX, int centerY, double longRadius, double shortRadius, double ticks,
            Paint paint, boolean labels) {
        for (double i = min; i <= max; i += ticks) {
            double angle = getAngleForValue(i, minAngle, maxAngle, min, max);
            double sinValue = Math.sin(angle);
            double cosValue = Math.cos(angle);
            int x1 = Math.round(centerX + (float) (shortRadius * sinValue));
            int y1 = Math.round(centerY + (float) (shortRadius * cosValue));
            int x2 = Math.round(centerX + (float) (longRadius * sinValue));
            int y2 = Math.round(centerY + (float) (longRadius * cosValue));
            canvas.drawLine(x1, y1, x2, y2, paint);

            if (labels) {
                paint.setTextAlign(Paint.Align.LEFT);
                if (x1 < x2) {// 原x1<=x2
                    paint.setTextAlign(Paint.Align.RIGHT);
                }
                if (x1 == x2) {
                    paint.setTextAlign(Paint.Align.CENTER);
                }
                String text = i + "";
                if (Math.round(i) == i) {
                    text = i + "";
                }
                // 绘制表码
                canvas.drawText(text, x1 - (float) (10 * sinValue), y1 - (float) (10 * cosValue),
                        paint);
            }
        }

    }

    public void drawNeedle(Canvas canvas, double angle, int centerX, int centerY, double radius,
            boolean arrow, Paint paint) {
        double diff = Math.toRadians(90.0D);
        int needleSinValue = (int) (10.0D * Math.sin(angle - diff));
        int needleCosValues = (int) (10.0D * Math.cos(angle - diff));
        int needleX = (int) (radius * Math.sin(angle));
        int needleY = (int) (radius * Math.cos(angle));
        int needleCenterX = centerX + needleX;
        int needleCentetY = centerY + needleY;
        float[] points;
        if (arrow) {
            int arrowBaseX = centerX + (int) (radius * 0.85D * Math.sin(angle));
            int arrowBaseY = centerY + (int) (radius * 0.85D * Math.cos(angle));
            points = new float[] { arrowBaseX - needleSinValue, arrowBaseY - needleCosValues - 4,
                    needleCenterX, needleCentetY, arrowBaseX + needleCenterX,
                    arrowBaseY + needleCentetY };
            paint.setStrokeWidth(2.0F);
            canvas.drawLine(centerX, centerY, needleCenterX, needleCentetY, paint);

        } else {
            points = new float[] { centerX - needleSinValue, centerY - needleCosValues,
                    needleCenterX, needleCentetY, centerX + needleSinValue,
                    centerY + needleCosValues };
        }
        drawPath(canvas, points, paint, true);
    }

    /**
     * 绘制指针
     */
    protected void drawPath(Canvas canvas, float[] points, Paint paint, boolean circular) {
        Path path = new Path();
        int height = canvas.getHeight();
        int width = canvas.getWidth();
        if (points.length < 4) {
            return;
        }
        float[] tempDrawPoints = calculateDrawPoints(points[0], points[1], points[2], points[3],
                height, width);
        path.moveTo(tempDrawPoints[0], tempDrawPoints[1]);
        path.lineTo(tempDrawPoints[2], tempDrawPoints[3]);

        int length = points.length;
        for (int i = 4; i < length; i += 2)
            if (((points[(i - 1)] >= 0.0F) || (points[(i + 1)] >= 0.0F))
                    && ((points[(i - 1)] <= height) || (points[(i + 1)] <= height))) {
                tempDrawPoints = calculateDrawPoints(points[(i - 2)], points[(i - 1)], points[i],
                        points[(i + 1)], height, width);

                if (!circular) {
                    path.moveTo(tempDrawPoints[0], tempDrawPoints[1]);
                }
                path.lineTo(tempDrawPoints[2], tempDrawPoints[3]);
            }
        if (circular) {
            path.lineTo(points[0], points[1]);
        }
        canvas.drawPath(path, paint);
    }

    private static float[] calculateDrawPoints(float p1x, float p1y, float p2x, float p2y,
            int screenHeight, int screenWidth) {
        float drawP1x;

        float drawP1y;

        if (p1y > screenHeight) {
            float m = (p2y - p1y) / (p2x - p1x);
            drawP1x = (screenHeight - p1y + m * p1x) / m;
            drawP1y = screenHeight;

            if (drawP1x < 0.0F) {

                drawP1x = 0.0F;
                drawP1y = p1y - m * p1x;
            } else if (drawP1x > screenWidth) {

                drawP1x = screenWidth;
                drawP1y = m * screenWidth + p1y - m * p1x;
            }
        } else if (p1y < 0.0F) {
            float m = (p2y - p1y) / (p2x - p1x);
            drawP1x = (-p1y + m * p1x) / m;
            drawP1y = 0.0F;
            if (drawP1x < 0.0F) {
                drawP1x = 0.0F;
                drawP1y = p1y - m * p1x;
            } else if (drawP1x > screenWidth) {
                drawP1x = screenWidth;
                drawP1y = m * screenWidth + p1y - m * p1x;
            }
        } else {
            drawP1x = p1x;
            drawP1y = p1y;
        }
        float drawP2x;
        float drawP2y;
        if (p2y > screenHeight) {
            float m = (p2y - p1y) / (p2x - p1x);
            drawP2x = (screenHeight - p1y + m * p1x) / m;
            drawP2y = screenHeight;
            if (drawP2x < 0.0F) {
                drawP2x = 0.0F;
                drawP2y = p1y - m * p1x;
            } else if (drawP2x > screenWidth) {
                drawP2x = screenWidth;
                drawP2y = m * screenWidth + p1y - m * p1x;
            }
        } else if (p2y < 0.0F) {
            float m = (p2y - p1y) / (p2x - p1x);
            drawP2x = (-p1y + m * p1x) / m;
            drawP2y = 0.0F;
            if (drawP2x < 0.0F) {
                drawP2x = 0.0F;
                drawP2y = p1y - m * p1x;
            } else if (drawP2x > screenWidth) {
                drawP2x = screenWidth;
                drawP2y = m * screenWidth + p1y - m * p1x;
            }
        } else {
            drawP2x = p2x;
            drawP2y = p2y;
        }

        return new float[] { drawP1x, drawP1y, drawP2x, drawP2y };
    }

    protected void drawMyBackground(DefaultRenderer renderer, Canvas canvas, int centerX,
            int centerY, float d, Paint paint) {
        if (renderer.isApplyBackgroundColor()) {
            paint.setStyle(Paint.Style.FILL);//
            paint.setColor(Color.parseColor("#708090"));
            canvas.drawCircle(centerX, centerY, d + 10, paint);
            paint.setColor(Color.WHITE);
            canvas.drawCircle(centerX, centerY, d + 6, paint);
            paint.setColor(Color.parseColor("#708090"));
            canvas.drawCircle(centerX, centerY, d + 4, paint);
            paint.setColor(renderer.getBackgroundColor());
            canvas.drawCircle(centerX, centerY, d, paint);

        }
    }

    @Override
    protected void drawBackground(DefaultRenderer renderer, Canvas canvas, int x, int y, int width,
            int height, Paint paint, boolean newColor, int color) {

        if ((renderer.isApplyBackgroundColor()) || (newColor)) {
            if (newColor) {
                paint.setColor(color);
            } else {
                Shader mShader = new LinearGradient(0, 0, 100, 100, new int[] { Color.RED,
                        Color.GREEN, Color.BLUE, Color.YELLOW, Color.LTGRAY }, null,
                        Shader.TileMode.REPEAT);// 线性渐变
                Shader mShader2 = new SweepGradient(0, 0, new int[] { Color.GRAY, Color.BLACK },
                        null);// 角度渐变
                Shader mShader3 = new RadialGradient(0, 0, 10, Color.GRAY, Color.BLACK,
                        Shader.TileMode.MIRROR);// 镜像渐变3
                paint.setStyle(Paint.Style.FILL);//
                paint.setColor(Color.parseColor("#708090"));
                canvas.drawCircle(x, y, width, paint);

                paint.setStyle(Paint.Style.FILL);//
                paint.setColor(Color.parseColor("#708090"));
                canvas.drawCircle(x + width / 2, y + height / 2, width / 2, paint);
                paint.setStyle(Paint.Style.FILL);
                paint.setColor(Color.WHITE);
                canvas.drawCircle(x + width / 2, y + height / 2, width / 2 - 6, paint);
                paint.setStyle(Paint.Style.FILL);
                paint.setColor(Color.parseColor("#708090"));
                canvas.drawCircle(x + width / 2, y + height / 2, width / 2 - 8, paint);
                paint.setStyle(Paint.Style.FILL);// 内部
                paint.setColor(renderer.getBackgroundColor());
                canvas.drawCircle(x + width / 2, y + height / 2, width / 2 - 15, paint);
                // /** 中心点 */
                // paint.setStyle(Paint.Style.FILL);
                // // paint.setShader(mShader3);
                // paint.setColor(Color.CYAN);
                // canvas.drawCircle(x + width / 2, y + height / 2, 10, paint);
            }
            // canvas.drawCircle(x + width / 2, y + height / 2, width / 2,
            // paint);
        }
    }

    public void drawTitle(Canvas canvas, int x, int y, int d, Paint paint) {
        if (mRenderer.isShowLabels()) {
            paint.setColor(mRenderer.getLabelsColor());
            paint.setTextAlign(Paint.Align.CENTER);
            paint.setTextSize(mRenderer.getChartTitleTextSize());
            drawString(canvas, mRenderer.getChartTitle(), x,
                    y + d / 2 + mRenderer.getChartTitleTextSize(), paint);
        }
    }
}

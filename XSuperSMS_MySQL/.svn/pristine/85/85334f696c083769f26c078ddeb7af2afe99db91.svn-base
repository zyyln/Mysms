/**
 * MyChartFactory.java
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
 * MyChartFactory.java
 * 
 * 2015-10-22
 */

package com.xuesi.sms.chart;

import org.achartengine.GraphicalView;
import org.achartengine.model.CategorySeries;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.DialRenderer;

import android.content.Context;

public class MyChartFactory {
    public static final GraphicalView getDialView(Context context, CategorySeries dataset,
            DialRenderer renderer) {
        checkParam(dataset, renderer);
        MyDialChart dialChart = new MyDialChart(dataset, renderer);

        return new GraphicalView(context, dialChart);
    }

    private static void checkParam(CategorySeries dataset, DefaultRenderer renderer) {
        if ((dataset == null) || (renderer == null)
                || (dataset.getItemCount() != renderer.getSeriesRendererCount())) {
            throw new IllegalArgumentException("Dataset and renderer!===");
        }
    }
}

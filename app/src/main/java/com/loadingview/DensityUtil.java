package com.loadingview;

import android.content.Context;

/**
 * Created by tianshutong on 2017/1/19.
 */

public class DensityUtil {

    /**
     * 得到设备的密度
     */
    public static float getScreenDensity(Context context) {
        return context.getResources().getDisplayMetrics().density;
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = getScreenDensity(context);
        return (int) (dpValue * scale + 0.5f);
    }
}

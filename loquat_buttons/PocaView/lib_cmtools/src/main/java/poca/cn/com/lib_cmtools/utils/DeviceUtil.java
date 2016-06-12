package poca.cn.com.lib_cmtools.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

/**
 * @author WilliamJiang
 * @date 16/6/7:下午3:18
 * @desc
 */
public class DeviceUtil {

    private static boolean deviceDataInitialize = false;

    /** 屏幕密度 */
    private static float density;
    /** 屏幕密度 */
    private static int densityDpi;
    /**  屏幕宽度 */
    private static int screenWidth;
    /** 屏幕高度 */
    private static int screenHeight;

    public static void initDeviceData(@NonNull Context context) {
        if (deviceDataInitialize) {
            return;
        }
        deviceDataInitialize = true;

        if (context == null) {
            throw new NullPointerException("Context should not be null");
        }

        DisplayMetrics displayMetrics = null;
        if (context.getResources() != null && (displayMetrics = context.getResources().getDisplayMetrics()) != null) {
            density = displayMetrics.density;
            densityDpi = displayMetrics.densityDpi;
            screenWidth = displayMetrics.widthPixels;
            screenHeight = displayMetrics.heightPixels;
        }
    }

    /**
     * px to sp
     *
     * @param context 上下文对象,建议使用Application的context
     * @param pxValue 需要转换的px值
     * @return int sp值
     */
    public static int px2sp(Context context, float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    /**
     * dip to px
     *
     * @param context 上下文对象,建议使用Application的context
     * @param dpValue 需要转换的dip值
     * @return int px值
     */
    public static int dip2px(Context context, float dpValue) {
        if (deviceDataInitialize) {
            return (int) (dpValue * density + 0.5f);
        }

        if (context != null && context.getResources() != null) {
            float scale = context.getResources().getDisplayMetrics().density;
            return (int) (dpValue * scale + 0.5f);
        } else {
            return -1;
        }
    }


    /**
     * 获取屏幕宽度
     *
     * @param context 上下文对象,建议使用Application的context
     * @return int 屏幕宽度
     */
    public static int getScreenWidth(Context context) {
        if (deviceDataInitialize) {
            return screenWidth;
        }

        if (context != null && context.getResources() != null) {
            DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
            return displayMetrics.widthPixels;
        }
        return -1;
    }

    /**
     * 获取屏幕高度
     *
     * @param context 上下文对象,建议使用Application的context
     * @return int 屏幕高度
     */
    public static int getScreenHeight(Context context) {
        if (deviceDataInitialize) {
            return screenHeight;
        }

        if (context != null && context.getResources() != null) {
            DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
            return displayMetrics.heightPixels;
        }
        return -1;
    }

    public static int getStatusBarHeight(@NonNull Activity activity) {
        if (activity == null) {
            throw new NullPointerException("activity is null");
        }

        Rect frame = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        return frame.top;
    }

    /**
     * 获取屏幕密度
     *
     * @param context 上下文对象,建议使用Application的context
     * @return float 屏幕密度
     */
    public static float getScreenDensity(Context context) {
        if (deviceDataInitialize) {
            return density;
        }

        if (context != null && context.getResources() != null) {
            DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
            return displayMetrics.density;
        }
        return 0f;
    }

    /**
     * 获取屏幕密度dpi
     *
     * @param context 上下文对象,建议使用Application的context
     * @return int  DENSITY_LOW:120
     * DENSITY_MEDIUM:160
     * DENSITY_HIGH:240
     * DENSITY_XHIGH:320
     */
    public static int getScreenDPI(Context context) {
        if (deviceDataInitialize) {
            return densityDpi;
        }

        if (context != null && context.getResources() != null) {
            DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
            return displayMetrics.densityDpi;
        }
        return 0;
    }


    /**
     * 设置屏幕亮度
     *
     * @param activity
     * @param percentage 设置屏幕 percentage% 的屏幕密度
     */
    public static void setScreenBrightness(Activity activity, float percentage) {
        setScreenBrightness(activity, (int) (255 * percentage));
    }

    /**
     * 设置屏幕亮度
     *
     * @param activity
     * @param value    亮度值 (暗)0~(亮)255(不在0~255范围内时,系统将设置为刚进入时的亮度)
     */
    public static void setScreenBrightness(Activity activity, int value) {
        if (activity == null) {
            return;
        }
        //设置亮度,使生效
        android.view.Window localWindow = activity.getWindow();
        WindowManager.LayoutParams localLayoutParams = localWindow.getAttributes();
        float percentage = value / 255f;
        localLayoutParams.screenBrightness = percentage;
        localWindow.setAttributes(localLayoutParams);
    }

    /**
     * 获取屏幕亮度
     *
     * @param activity
     * @return int -255:activity为空; 0~255:屏幕亮度(不在0~255范围内时,系统将设置为刚进入时的亮度)
     */
    public static int getScreenBrightness(Activity activity) {
        if (activity == null) {
            return -255;
        }

        android.view.Window localWindow = activity.getWindow();
        WindowManager.LayoutParams localLayoutParams = localWindow.getAttributes();
        int screenBrightness = (int) (localLayoutParams.screenBrightness * 255);

        return screenBrightness;
    }

    /**
     * 获取屏幕尺寸
     *
     * @param context 上下文对象,建议使用Application的context
     * @return
     */
    public static int[] getScreenDimensions(Context context) {
        int[] dimensions = new int[2];
        Display display = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        dimensions[0] = display.getWidth();
        dimensions[1] = display.getHeight();
        return dimensions;
    }

    /**
     * 根据屏幕宽度作为100%，根据比例来计算出相应距离
     *
     * @param context 上下文对象,建议使用Application的context
     * @param scale   缩放比例
     * @return long 缩放后的屏幕宽度
     */
    public static long getPxByScreenWidth(Context context, double scale) {
        return Math.round(getScreenWidth(context) * scale);
    }

    /**
     * 根据屏幕高度作为100%，根据比例来计算出相应距离
     *
     * @param context 上下文对象,建议使用Application的context
     * @param scale   比例
     * @return long 缩放后的屏幕高度
     */
    public static long getPxByEquipHeight(Context context, double scale) {
        return Math.round(getScreenHeight(context) * scale);
    }

    /**
     * 获取资源绝对尺寸
     *
     * @param context 上下文对象,建议使用Application的context
     * @param dimenId 资源id
     * @return int
     */
    public static int getDimensionPixelSize(Context context, int dimenId) {
        if (context == null) {
            return 0;
        }
        return context.getResources().getDimensionPixelSize(dimenId);

    }

    /**
     * 设置窗口透明度
     *
     * @param activity 窗口实例
     * @param alpha    透明度
     */
    public static void setWindowAlpha(Activity activity, float alpha) {
        if (activity == null) {
            return;
        }

        WindowManager.LayoutParams params = activity.getWindow().getAttributes();
        // params.alpha = alpha;
        params.screenBrightness = alpha;
        activity.getWindow().setAttributes(params);
    }

}

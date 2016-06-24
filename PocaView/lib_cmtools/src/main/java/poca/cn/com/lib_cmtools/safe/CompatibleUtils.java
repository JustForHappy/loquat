package poca.cn.com.lib_cmtools.safe;

import java.util.HashMap;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.graphics.Point;
import android.hardware.Camera;
import android.net.TrafficStats;
import android.net.Uri;
import android.os.Build;
import android.os.Looper;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.Settings;
import android.provider.Settings.SettingNotFoundException;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;

@SuppressLint("NewApi")
public class CompatibleUtils {

    public static final int NETWORK_TYPE_EHRPD = TelephonyManager.NETWORK_TYPE_EHRPD;
    public static final int NETWORK_TYPE_HSPAP = TelephonyManager.NETWORK_TYPE_HSPAP;
    public static final int NETWORK_TYPE_LTE = TelephonyManager.NETWORK_TYPE_LTE;
    private static CompatibleUtils mCompatibleUtile = null;
    private static Object5 mObject5 = null;
    private static Object7 mObject7 = null;
    private static Object8 mObject8 = null;
    private static Object9 mObject9 = null;
    private static Object11 mObject11 = null;
    private static Object14 mObject14 = null;

    static public CompatibleUtils getInstance() {
        if (mCompatibleUtile == null) {
            mCompatibleUtile = new CompatibleUtils();
        }
        return mCompatibleUtile;
    }

    private Object5 getObject5() {
        if (android.os.Build.VERSION.SDK_INT >= 5 && mObject5 == null) {
            mObject5 = new Object5();
        }
        return mObject5;
    }

    private Object7 getObject7() {
        if (android.os.Build.VERSION.SDK_INT >= 7 && mObject7 == null) {
            mObject7 = new Object7();
        }
        return mObject7;
    }

    private Object8 getObject8() {
        if (android.os.Build.VERSION.SDK_INT >= 8 && mObject8 == null) {
            mObject8 = new Object8();
        }
        return mObject8;
    }

    private Object9 getObject9() {
        if (android.os.Build.VERSION.SDK_INT >= 9 && mObject9 == null) {
            mObject9 = new Object9();
        }
        return mObject9;
    }

    private Object11 getObject11() {
        if (android.os.Build.VERSION.SDK_INT >= 11 && mObject11 == null) {
            mObject11 = new Object11();
        }
        return mObject11;
    }

    private Object14 getObject14() {
        if (android.os.Build.VERSION.SDK_INT >= 14 && mObject14 == null) {
            mObject14 = new Object14();
        }
        return mObject14;
    }

    public int getMemoryClass(Context context) {
        if (getObject5() != null) {
            return getObject5().getMemoryClass(context);
        } else {
            return 16;
        }
    }

    public boolean isAutoBrightness(Context context) {
        if (getObject8() != null) {
            return getObject8().isAutoBrightness(context);
        } else {
            return false;
        }
    }

    public long getUidRxBytes(int uid) {
        if (getObject8() != null) {
            return getObject8().getUidRxBytes(uid);
        }
        return 0L;
    }

    public long getUidTxBytes(int uid) {
        if (getObject8() != null) {
            return getObject8().getUidTxBytes(uid);
        }
        return 0L;
    }

    public int getBitmapMaxMemory(Context context) {
        int max = getMemoryClass(context);
        if (max <= 0) {
            max = 16;
        }
        max = max * 1024 * 1024 / 2;
        return max;
    }

    public static int getActionMask() {
        return 0xff;
    }

    public int getActionPointerUp() {
        if (getObject5() != null) {
            return getObject5().getActionPointerUp();
        } else {
            return 6;
        }
    }

    public int getActionPointerDown() {
        if (getObject5() != null) {
            return getObject5().getActionPointerDown();
        } else {
            return 5;
        }
    }

    public float getSpacing(final MotionEvent event) {
        if (getObject5() != null) {
            return getObject5().getSpacing(event);
        } else {
            return -1;
        }
    }

    public Camera getBackCamera() {
        if (getObject9() != null) {
            return getObject9().getBackCamera();
        } else {
            return Camera.open();
        }
    }

    public void setCameraDisplayOrientation(Camera camera, int degrees) {
        if (getObject8() != null) {
            getObject8().setCameraDisplayOrientation(camera, degrees);
        }
    }

    public void WebViewNoDataBase(WebSettings settings) {
        if (getObject5() != null) {
            getObject5().WebViewNoDataBase(settings);
        }
    }

    public boolean supportMultiTouch(Context context) {
        if (getObject7() != null) {
            return getObject7().supportMultiTouch(context);
        } else {
            return false;
        }
    }

    public void removeJavascriptInterface(WebView webView) {
        if (getObject11() != null) {
            getObject11().removeJavascriptInterface(webView);
        }
    }

    public void openGpuHardwareAccelerated(final Activity context) {
        if (getObject11() != null) {
            getObject11().openGpuHardwareAccelerated(context);
        }
    }

    public int getStatusBarColor(Context context) {
        try {
            if (getObject11() != null) {
                return getObject11().getStatusBarColor(context);
            }
        } catch (Exception ex) {
            return 0;
        }
        return 0;
    }

    public boolean isUseHw(View view) {
        if (getObject11() != null) {
            return getObject11().isUseHw(view);
        }
        return false;
    }

    public int getViewLayer(View view) {
        if (getObject11() != null) {
            return getObject11().getViewLayer(view);
        }
        return 0;
    }

    public void noneViewGpu(View view) {
        if (getObject11() != null) {
            getObject11().noneViewGpu(view);
        }
    }

    public void closeViewGpu(View view) {
        if (getObject11() != null) {
            getObject11().closeViewGpu(view);
        }
    }

    public void openViewGpu(View view) {
        if (getObject11() != null) {
            getObject11().openViewGpu(view);
        }
    }

    public WebChromeClient getWebChromeClient(Activity activity) {
        if (getObject14() != null) {
            return getObject14().getWebChromeClient(activity);
        } else {
            return new WebChromeClient();
        }
    }

    public void loadUrl(WebView wv, String url) {
        if (getObject8() != null) {
            getObject8().loadUrl(wv, url);
        } else {
            wv.loadUrl(url);
        }
    }

    public static class FullscreenableChromeClient extends WebChromeClient {
        protected Activity mActivity = null;

        private View mCustomView;
        private WebChromeClient.CustomViewCallback mCustomViewCallback;
        private int mOriginalOrientation;

        private FrameLayout mContentView;
        private FrameLayout mFullscreenContainer;

        private final FrameLayout.LayoutParams COVER_SCREEN_PARAMS = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);

        public FullscreenableChromeClient(Activity activity) {
            this.mActivity = activity;
        }

        @Override
        public void onShowCustomView(View view, int requestedOrientation, WebChromeClient.CustomViewCallback callback) {
            if (mCustomView != null) {
                callback.onCustomViewHidden();
                return;
            }
            mOriginalOrientation = mActivity.getRequestedOrientation();
            FrameLayout decor = (FrameLayout) mActivity.getWindow().getDecorView();
            mFullscreenContainer = new FullscreenHolder(mActivity);
            mFullscreenContainer.addView(view, COVER_SCREEN_PARAMS);
            decor.addView(mFullscreenContainer, COVER_SCREEN_PARAMS);
            mCustomView = view;
            setFullscreen(true);
            mCustomViewCallback = callback;
            mActivity.setRequestedOrientation(requestedOrientation);
            super.onShowCustomView(view, requestedOrientation, callback);
        }

        @Override
        public void onHideCustomView() {
            if (mCustomView == null) {
                return;
            }
            setFullscreen(false);
            FrameLayout decor = (FrameLayout) mActivity.getWindow().getDecorView();
            decor.removeView(mFullscreenContainer);
            mFullscreenContainer = null;
            mCustomView = null;
            mCustomViewCallback.onCustomViewHidden();
            mActivity.setRequestedOrientation(mOriginalOrientation);
        }

        public void hideCustomView() {
            onHideCustomView();
        }

        private void setFullscreen(boolean enabled) {
            Window win = mActivity.getWindow();
            WindowManager.LayoutParams winParams = win.getAttributes();
            final int bits = WindowManager.LayoutParams.FLAG_FULLSCREEN;
            if (enabled) {
                winParams.flags |= bits;
            } else {
                winParams.flags &= ~bits;
                if (mCustomView != null) {
                    mCustomView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
                } else {
                    mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
                }
            }
            win.setAttributes(winParams);
        }

        private class FullscreenHolder extends FrameLayout {
            public FullscreenHolder(Context ctx) {
                super(ctx);
                setBackgroundColor(ctx.getResources().getColor(android.R.color.black));
            }

            @Override
            public boolean onTouchEvent(MotionEvent evt) {
                return true;
            }
        }
    }

    private class Object5 extends Object {
        public int getActionPointerUp() {
            return MotionEvent.ACTION_POINTER_UP;
        }

        public int getActionPointerDown() {
            return MotionEvent.ACTION_POINTER_DOWN;
        }

        public float getSpacing(MotionEvent event) {
            int pointer = event.getPointerCount();
            if (pointer < 2) {
                return -1;
            }
            float x = event.getX(0) - event.getX(1);
            float y = event.getY(0) - event.getY(1);
            return (float) java.lang.Math.sqrt(x * x + y * y);
        }

        public int getMemoryClass(Context context) {
            ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            return am.getMemoryClass();
        }

        public void WebViewNoDataBase(WebSettings settings) {
            if (settings != null) {
                settings.setDatabaseEnabled(false);
            }
        }
    }

    private class Object7 extends Object {
        public boolean supportMultiTouch(Context context) {
            if (context == null) {
                return false;
            }
            return context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_TOUCHSCREEN_MULTITOUCH);

        }
    }

    private class Object8 extends Object {
        public boolean isAutoBrightness(Context context) {
            boolean automicBrightness = false;
            try {
                automicBrightness = Settings.System.getInt(context.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE) == Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC;
            } catch (SettingNotFoundException e) {
                e.printStackTrace();
            }
            return automicBrightness;
        }

        public void setCameraDisplayOrientation(Camera camera, int degrees) {
            if (camera == null) {
                return;
            }
            camera.setDisplayOrientation(degrees);
        }

        public long getUidTxBytes(int uid) {
            return TrafficStats.getUidTxBytes(uid);
        }

        public long getUidRxBytes(int uid) {
            return TrafficStats.getUidRxBytes(uid);
        }

        public void loadUrl(WebView wv, String url) {
            if (wv != null && !TextUtils.isEmpty(url)) {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("referer", "http://c.tieba.baidu.com/");
                wv.loadUrl(url, headers);
            }
        }
    }

    private class Object9 extends Object {

        public int getCameraNumber() {
            try {
                return Camera.getNumberOfCameras();
            } catch (Throwable t) {
                return 0;
            }
        }

        public Camera getBackCamera() {
            int numCameras = Camera.getNumberOfCameras();
            if (numCameras == 0) {
                return null;
            }

            int index = 0;
            while (index < numCameras) {
                Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
                Camera.getCameraInfo(index, cameraInfo);
                if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
                    break;
                }
                index++;
            }

            Camera camera;
            if (index < numCameras && index != 0) {
                try {
                    camera = Camera.open(index);
                } catch (Throwable t) {
                    Log.e("jdb", "failed open camera:" + index);
                    camera = Camera.open(0);
                }
            } else {
                camera = Camera.open(0);
            }

            return camera;
        }
    }

    private class Object11 extends Object {

        private final int[] STATUSBAR_ATTRS = new int[] { android.R.attr.textColor };

        public int getStatusBarColor(Context context) {
            TypedArray appearance = context.obtainStyledAttributes(android.R.style.TextAppearance_StatusBar_EventContent_Title,
                    STATUSBAR_ATTRS);
            return appearance.getColor(0, 0);
        }

        public void openGpuHardwareAccelerated(Activity context) {
            context.getWindow().setFlags(WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
                    WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
        }

        public boolean isUseHw(View view) {
            return view != null && (view.isHardwareAccelerated());
        }

        public int getViewLayer(View view) {
            if (view != null) {
                // Log.d("GPU_TYPE", view.getLayerType()+"");
                return view.getLayerType();
            }
            return View.LAYER_TYPE_NONE;
        }

        public void noneViewGpu(View view) {
            if (view != null) {
                // Log.d("GPU_TYPE", view.getLayerType()+"");
                view.setLayerType(View.LAYER_TYPE_NONE, null);
            }
        }

        public void closeViewGpu(View view) {
            if (view != null && view.isHardwareAccelerated()) {
                // Log.d("GPU_TYPE", view.getLayerType()+"");
                // view.setLayerType(View.LAYER_TYPE_NONE, null);
                view.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
            }
        }

        public void openViewGpu(View view) {
            if (view != null) {
                view.setLayerType(View.LAYER_TYPE_HARDWARE, null);
            }
        }

        public void removeJavascriptInterface(WebView webview) {
            if (webview != null) {
                webview.removeJavascriptInterface("searchBoxJavaBridge_");
            }
        }
    }

    private class Object14 extends Object {
        public WebChromeClient getWebChromeClient(Activity activity) {
            return new FullscreenableChromeClient(activity);
        }
    }

    public static void setAnim(Activity activity, int enter, int exit) {
        if (android.os.Build.VERSION.SDK_INT >= 5) {
            activity.overridePendingTransition(enter, exit);
        }
    }

    public static void scrollListViewBy(ListView listView, int distance, int duration) {
        if (android.os.Build.VERSION.SDK_INT >= 8) {
            listView.smoothScrollBy(distance, duration);
        } else {
        }
    }

    public static void scrollListViewTo(ListView listView, int position) {
        if (android.os.Build.VERSION.SDK_INT >= 8) {
            listView.smoothScrollToPosition(position);
        } else {
            listView.setSelection(position);
        }
    }

    public static Point getSizePoint(WindowManager wmManager) {
        Point p = new Point();
        if (android.os.Build.VERSION.SDK_INT < 13) {
            p.x = wmManager.getDefaultDisplay().getWidth();
            p.y = wmManager.getDefaultDisplay().getHeight();
        } else {
            wmManager.getDefaultDisplay().getSize(p);
        }

        return p;
    }

    public static String uri2filePath(Uri uri, Activity activity) {
        String path = "";
        Cursor cursor = null;

        try {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT && DocumentsContract.isDocumentUri(activity, uri)) {
                String wholeID = DocumentsContract.getDocumentId(uri);
                String id = wholeID.split(":")[1];
                String[] column = { MediaStore.Images.Media.DATA };
                String sel = MediaStore.Images.Media._ID + "=?";
                cursor = activity.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, column, sel,
                        new String[] { id }, null);
                int columnIndex = cursor.getColumnIndex(column[0]);
                if (cursor.moveToFirst()) {
                    path = cursor.getString(columnIndex);
                }
            } else {
                String[] projection = { MediaStore.Images.Media.DATA };
                cursor = activity.getContentResolver().query(uri, projection, null, null, null);
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                if (cursor.moveToFirst()) {
                    path = cursor.getString(column_index);
                }
            }
        } catch (Exception e) {
            Log.e("jdb", e.getMessage());
        } finally {
            if (cursor != null) {
                try {
                    cursor.close();
                } catch (Exception e) {

                }
            }
        }

        return path;
    }

    public static void setY(LinearLayout layout) {
        if (layout != null && android.os.Build.VERSION.SDK_INT >= 11) {
            layout.setY(-200);
        }
    }

    public static void setNoDividers(LinearLayout layout) {
        if (android.os.Build.VERSION.SDK_INT >= 11) {
            layout.setShowDividers(LinearLayout.SHOW_DIVIDER_NONE);
        }
    }

    public static void setThreadStatsTag(int trafficStatsTag) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            TrafficStats.setThreadStatsTag(trafficStatsTag);
        }
    }

    public static void removeViewTreeObserver(ViewTreeObserver observer, OnGlobalLayoutListener listener) {
        if (null == observer || listener == null) {
            return;
        }

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            observer.removeGlobalOnLayoutListener(listener);
        } else {
            observer.removeOnGlobalLayoutListener(listener);
        }
    }

    public static void commitSharedPreferences(android.content.SharedPreferences.Editor editor) {
        if (editor == null) {
            return;
        }

        if (Looper.getMainLooper() == Looper.myLooper()) {
            try {
                editor.apply();
            } catch (NoSuchMethodError e) {
                editor.commit();
            }
        } else {
            editor.commit();
        }
    }

    public boolean hasCameraPermission(Context context) {
        if (android.os.Build.VERSION.SDK_INT >= 17) {
            return context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY);
        } else if (getObject9() != null) {
            return getObject9().getCameraNumber() > 0;
        }

        return true;
    }

    public static boolean removeDefaultJavascriptInterface(WebView mWebView) {
        if (android.os.Build.VERSION.SDK_INT >= 11) {
            mWebView.removeJavascriptInterface("searchBoxJavaBridge_");
        }

        return true;
    }

    public AlertDialog.Builder buildHoloThemeDialog(Activity activity) {
        if (getObject11() != null) {
            return new AlertDialog.Builder(activity, AlertDialog.THEME_HOLO_LIGHT);
        } else {
            return new AlertDialog.Builder(activity);
        }
    }

}

package poca.cn.com.pocaview;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;

/**
 * @author WilliamJiang
 * @date 16/6/13:上午10:43
 * @desc
 */
public class BaseApplication extends Application {
    private static BaseApplication _instance;

    public static BaseApplication getApplicationInstance() {
        return _instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        _instance = this;
    }

    protected Handler handler = new Handler(Looper.getMainLooper());

    public void postTaskInUIThread(Runnable runnable) {
        this.handler.post(runnable);
    }

    public void postTaskInUIThread(Runnable runnable, int delayMillis) {
        this.handler.postDelayed(runnable, delayMillis);
    }

    public Handler getMainLoopHandler() {
        return this.handler;
    }

    public void onAppMemoryLow() {
        System.gc();
    }
    @Override
    public void onLowMemory() {
        onAppMemoryLow();

        super.onLowMemory();
    }
}

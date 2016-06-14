package poca.cn.com.lib_cmtools.safe;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.IBinder;
import android.view.View;
import android.widget.PopupWindow;

/**
 * @author WilliamJiang
 * @date 16/6/8:下午2:15
 * @desc
 */
public class ShowUtils {

    public static final boolean showDialog(Activity activity, Dialog dialog) {
        if (dialog != null && activity != null) {
            if (activity.isFinishing()) {
                return false;
            }
            // 当window没有激活的时候调用dialog.show，是没有问题的，此时如果之后调用finish，只会出现windowleaked(出现window leak是dialog在activity被kill前没有dismiss)
            if (activity.getWindow() != null && !activity.getWindow().isActive()) {
                try {
                    dialog.show();
                    return true;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (activity.getWindow() != null && isTokenValid(activity.getWindow().getDecorView())) {
                try {
                    dialog.show();
                    return true;
                } catch (Exception e) {
                   e.printStackTrace();
                }
            }
        }
        return false;
    }

    public static final boolean dismissDialog(Activity activity, Dialog dialog) {
        if (dialog != null && activity != null) {
            if (activity.isFinishing()) {
                return false;
            }
            if (activity.getWindow() != null && isTokenValid(activity.getWindow().getDecorView())) {
                dialog.dismiss();
                return true;
            }
        }
        return false;
    }

    public static final boolean dismissPopupWindow(PopupWindow window) {
        if (window != null) {
            if (isActivityFinishing(window.getContentView().getContext())) {
                return false;
            }
            if (isTokenValid(window.getContentView())) {
                window.dismiss();
                return true;
            }
        }
        return false;
    }

    public static final boolean dismissPopupWindow(Activity activity, PopupWindow window) {
        if (window != null && activity != null) {
            if (isActivityFinishing(activity)) {
                return false;
            }
            if (isTokenValid(activity.getWindow().getDecorView())) {
                window.dismiss();
                return true;
            }
            return false;
        } else {
            return dismissPopupWindow(window);
        }
    }


    private static final boolean isTokenValid(View view) {
        if (view != null) {
            IBinder binder = view.getWindowToken();
            if (binder != null) {
                try {
                    if (binder.isBinderAlive() && binder.pingBinder()) {
                        return true;
                    }
                } catch (Exception e) {

                }
            }
        }
        return false;
    }

    private static final boolean isActivityFinishing(Context context) {
        if (context instanceof Activity) {
            return ((Activity) context).isFinishing();
        }
        return true;
    }
}

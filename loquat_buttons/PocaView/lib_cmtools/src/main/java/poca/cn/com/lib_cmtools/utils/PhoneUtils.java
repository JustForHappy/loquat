package poca.cn.com.lib_cmtools.utils;

import android.content.Context;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import java.util.UUID;

/**
 * @author WilliamJiang
 * @date 16/6/8:下午1:27
 * @desc
 */
public class PhoneUtils {
    private static String IMEI;
    private static String UDID = null;

    /**
     * 返回11位长度的标准手机号码，去掉时区等冗余信息。
     *
     * @param phoneNum
     *            电话号码
     * @return
     */
    public static String getCleanPhoneNumber(String phoneNum) {
        if (phoneNum == null) {
            throw new NullPointerException("The phone number is null");
        }

        // 处理掉时区等信息
        StringBuilder sb = new StringBuilder(phoneNum.length() + 1);
        for (char c : phoneNum.toCharArray()) {
            if (c >= '0' && c <= '9') {
                sb.append(c);
            }
        }

        if (sb.length() > 11) {
            phoneNum = sb.substring(sb.length() - 11);
        } else {
            phoneNum = sb.toString();
        }

        return phoneNum;
    }

    /**
     * 判断是11位否是手机号码
     *
     * @param phoneNum
     *            电话号码
     * @return true 是标准手机号码 false 不是手机号码
     */
    public static boolean isMobilePhoneNum(String phoneNum) {
        String cleanNum = getCleanPhoneNumber(phoneNum);

        if (cleanNum.length() != 11) {
            return false;
        }

        char[] cs = phoneNum.toCharArray();
        return (cs[0] == '1');
    }

    /**
     * 返回手机IMEI
     *
     * @param context
     * @return
     */
    public static String getIMEI(Context context) {
        if (!TextUtils.isEmpty(IMEI)) {
            return IMEI;
        }
        if (context == null) {
            return "";
        }
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        IMEI = telephonyManager.getDeviceId();
        if (IMEI == null) {
            IMEI = "";
        }
        return IMEI;
    }


    /**
     * 返回手机UUID
     *
     * @param mContext
     * @return
     */
    public static String getUUID(final Context mContext) {
        if (UDID == null) {
            try {
                // 可能会出现没有权限的异常。
                final TelephonyManager tm = (TelephonyManager) mContext.getApplicationContext().getSystemService(
                        Context.TELEPHONY_SERVICE);
                final String tmDevice, tmSerial, androidId;
                tmDevice = "" + tm.getDeviceId();
                tmSerial = "" + tm.getSimSerialNumber();
                androidId = ""
                        + android.provider.Settings.Secure.getString(mContext.getContentResolver(),
                        android.provider.Settings.Secure.ANDROID_ID);
                UUID deviceUuid = new UUID(androidId.hashCode(), ((long) tmDevice.hashCode() << 32)
                        | tmSerial.hashCode());
                String uuid = deviceUuid.toString();
                if (uuid != null) {
                    UDID = deviceUuid.toString().replace("-", "");
                }
            } catch (Throwable t) {

            }
        }
        return UDID;
    }

}

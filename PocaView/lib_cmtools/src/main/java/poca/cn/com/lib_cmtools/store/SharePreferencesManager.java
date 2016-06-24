package poca.cn.com.lib_cmtools.store;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Looper;
import android.util.Log;

import poca.cn.com.lib_cmtools.safe.CompatibleUtils;

public class SharePreferencesManager {
    private static SharePreferencesManager INSTANCE;
    private SharedPreferences mPreferences;

    public static synchronized SharePreferencesManager getInstance() {
        if (INSTANCE == null) {
            if (Looper.myLooper() == null) {
                Looper.getMainLooper();
                Looper.prepare();
            }
            INSTANCE = new SharePreferencesManager();
        }
        return INSTANCE;
    }

    public void appStart(Context context, String fileName) {
        mPreferences = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
    }

    /*
     * boolean 值存取
     */
    public void setBoolean(String key, boolean value) {
        if (mPreferences != null) {
            Editor editor = mPreferences.edit();
            editor.putBoolean(key, value);
            CompatibleUtils.commitSharedPreferences(editor);
        }
    }

    public boolean getBoolean(String key) {
        return getBoolean(key, false);
    }

    public boolean getBoolean(String key, boolean defaultValue) {
        boolean ret = defaultValue;
        if (mPreferences != null && key != null) {
            ret = mPreferences.getBoolean(key, ret);
        }
        return ret;
    }

    public void setBooleanWithMemberId(String memberId, String key, boolean value) {
        setBoolean(key + memberId, value);
    }

    public boolean getBooleanWithMemberId(String memberId, String key) {
        return getBoolean(key + memberId);
    }

    public boolean getBooleanWithMemberId(String memberId, String key,boolean defaultValue) {
        return getBoolean(key + memberId, defaultValue);
    }

    /*
     * String 值存取
     */
    public void setString(String key, String value) {
        if (mPreferences != null) {
            Editor editor = mPreferences.edit();
            editor.putString(key, value);
            CompatibleUtils.commitSharedPreferences(editor);
        }
    }

    public String getString(String key) {
        return getString(key, "");
    }

    public String getString(String key, String defaultValue) {
        String ret = defaultValue;
        if (mPreferences != null && key != null) {
            try {
                ret = mPreferences.getString(key, ret);
            } catch (Exception e) {
                //
            }

        }
        return ret;
    }

    public void setStringWithMemberId(String memberId, String key,String value) {
        setString(key + memberId, value);
    }

    public String getStringWithMemberId(String memberId, String key) {
        return getString(key + memberId);
    }

    public String getStringWithMemberId(String memberId, String key, String defaultValue) {
        return getString(key + memberId, defaultValue);
    }

    /**
     * long 值类型
     */
    public void setLong(String key, long value) {
        if (mPreferences != null) {
            Editor editor = mPreferences.edit();
            editor.putLong(key, value);
            CompatibleUtils.commitSharedPreferences(editor);
        }
    }

    public long getLong(String key) {
        return getLong(key, 0l);
    }

    public long getLong(String key, long defaultValue) {
        long ret = defaultValue;
        if (mPreferences != null && key != null) {
            try {
                ret = mPreferences.getLong(key, ret);
            } catch (Exception e) {
                Log.e("TAG", "type changed:" + key, e);
            }
        }
        return ret;
    }

    public void setLongWithMemberId(String memberId, String key, long value) {
        setLong(key + memberId, value);
    }

    public long getLongWithMemberId(String memberId, String key) {
        return getLong(key + memberId);
    }

    public long getLongWithMemberId(String memberId, String key, long defaultValue) {
        return getLong(key + memberId, defaultValue);
    }


    /**
     * int 值类型
     */
    public void setInt(String key, int value) {
        if (mPreferences != null) {
            Editor editor = mPreferences.edit();
            editor.putInt(key, value);
            CompatibleUtils.commitSharedPreferences(editor);
        }
    }

    public int getInt(String key) {
        return getInt(key, -1);
    }

    public int getInt(String key, int defaultValue) {
        int ret = defaultValue;
        if (mPreferences != null && key != null) {
            ret = mPreferences.getInt(key, ret);
        }
        return ret;
    }

    public void setIntWithMemberId(String memberId, String key,  int value) {
        setInt(key + memberId, value);
    }

    public int getIntWithMemberId(String memberId, String key) {
        return getInt(key + memberId);
    }

    public int getIntWithMemberId(String memberId, String key, int defaultValue) {
        return getInt(key + memberId, defaultValue);
    }

    public void remove(String key) {
        if (mPreferences != null) {
            Editor editor = mPreferences.edit();
            editor.remove(key);
            CompatibleUtils.commitSharedPreferences(editor);
        }
    }
}

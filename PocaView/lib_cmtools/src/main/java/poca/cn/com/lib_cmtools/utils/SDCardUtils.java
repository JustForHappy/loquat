package poca.cn.com.lib_cmtools.utils;

import android.os.Environment;

import java.io.File;

/**
 * @author WilliamJiang
 * @date 16/6/13:下午1:08
 * @desc
 */
public class SDCardUtils {
    /**
     * 检查SD卡是否存在
     *
     * @return true：存在； false：不存在
     */
    public static boolean isSDCardAvailable() {
        String status = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(status);
    }

    /**
     * 获取SD根目录
     *
     * @return
     */
    public static File getExternalStorage() {
        return Environment.getExternalStorageDirectory();
    }

    /**
     * 获取外存储器剩余存储空间
     *
     * @return long(bytes)
     */
    public static long getExternalStorageFreeSpace() {
        return Environment.getExternalStorageDirectory().getFreeSpace();
    }
}

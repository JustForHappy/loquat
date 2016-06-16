package poca.cn.com.pocaview.imgloader;

import poca.cn.com.lib_cmtools.utils.SDCardUtils;

/**
 * @author WilliamJiang
 * @date 16/6/13:上午11:55
 * @desc
 */
public class ImageLoaderUtil {

    /** Image Disk Cache Size */
    public static final long DISK_CACHE_SIZE = 5 * 1024 * 1024;
    public static final String POCA_ROOT_PATH = SDCardUtils.getExternalStorage() + "/Poca/";
    public static final String IMAGE_LOADER_PATH = POCA_ROOT_PATH + "cache/";
}

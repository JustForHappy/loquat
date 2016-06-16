package poca.cn.com.pocaview;

import com.nostra13.universalimageloader.cache.disc.impl.ext.LruDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LRULimitedMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import java.io.File;
import java.io.IOException;

import poca.cn.com.lib_cmtools.utils.DeviceUtils;
import poca.cn.com.lib_cmtools.utils.SDCardUtils;
import poca.cn.com.pocaview.imgloader.ImageLoaderUtil;

/**
 * @author WilliamJiang
 * @date 16/6/13:上午10:43
 * @desc
 */
public class PocaApplication extends BaseApplication{
    @Override
    public void onCreate() {
        super.onCreate();

        DeviceUtils.initDeviceData(getApplicationContext());
        initImageLoader();
    }


    private void initImageLoader() {
        if (ImageLoader.getInstance().isInited()) {
            return;
        }

        int cpuNum = Runtime.getRuntime().availableProcessors();
        int threadNum = (cpuNum > 2 ? 4 : 3);
        int maxMemorySize = (int) (Runtime.getRuntime().maxMemory());
        int maxMemoryCacheSize = maxMemorySize / 16;

        ImageLoaderConfiguration.Builder mBuilder = new ImageLoaderConfiguration.Builder(getApplicationContext())
                .threadPoolSize(threadNum)
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .memoryCache(new LRULimitedMemoryCache(maxMemoryCacheSize))
                .tasksProcessingOrder(QueueProcessingType.LIFO);
        if (ImageLoaderUtil.DISK_CACHE_SIZE < SDCardUtils.getExternalStorageFreeSpace()) {
            try {
                mBuilder.diskCache(new LruDiskCache(new File(ImageLoaderUtil.IMAGE_LOADER_PATH),
                        new HashCodeFileNameGenerator(), ImageLoaderUtil.DISK_CACHE_SIZE));// 设置磁盘缓存
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        ImageLoaderConfiguration config = mBuilder.build();
        ImageLoader.getInstance().init(config);
    }

    @Override
    public void onAppMemoryLow() {
        if (ImageLoader.getInstance().isInited()) {
            ImageLoader.getInstance().clearMemoryCache();
        }

        super.onAppMemoryLow();
    }
}

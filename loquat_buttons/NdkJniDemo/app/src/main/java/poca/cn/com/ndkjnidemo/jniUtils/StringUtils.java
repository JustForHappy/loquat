package poca.cn.com.ndkjnidemo.jniUtils;

/**
 * @author WilliamJiang
 * @date
 * @desc
 */
public class StringUtils {
    public static native String getStringFromC();
    static {
        System.loadLibrary("ndkJniDemo");
    }
}

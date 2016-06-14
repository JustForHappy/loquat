package poca.cn.com.lib_cmtools.utils.sio;

import java.io.File;

/**
 * @author WilliamJiang
 * @date 16/6/15:上午1:05
 * @desc
 */
public class FilenameUtils {
    /**
     * The Unix separator character.
     */
    private static final char UNIX_SEPARATOR = '/';

    /**
     * The Windows separator character.
     */
    private static final char WINDOWS_SEPARATOR = '\\';

    /**
     * The system separator character.
     */
    private static final char SYSTEM_SEPARATOR = File.separatorChar;

    /**
     * The separator character that is the opposite of the system separator.
     */
    private static final char OTHER_SEPARATOR;
    static {
        if (isSystemWindows()) {
            OTHER_SEPARATOR = UNIX_SEPARATOR;
        } else {
            OTHER_SEPARATOR = WINDOWS_SEPARATOR;
        }
    }

    /**
     * Instances should NOT be constructed in standard programming.
     */
    public FilenameUtils() {
        super();
    }

    // -----------------------------------------------------------------------
    /**
     * Determines if Windows file system is in use.
     *
     * @return true if the system is Windows
     */
    public static boolean isSystemWindows() {
        return SYSTEM_SEPARATOR == WINDOWS_SEPARATOR;
    }

}

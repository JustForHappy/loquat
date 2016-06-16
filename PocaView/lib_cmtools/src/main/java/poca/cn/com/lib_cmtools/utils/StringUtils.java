package poca.cn.com.lib_cmtools.utils;

import java.util.regex.Pattern;

/**
 * @author WilliamJiang
 * @date 16/6/7:下午3:18
 * @desc
 */
public class StringUtils {

    /**
     * 字符是否为空，有"null"的判断
     *
     * @param s
     *            待测字符串
     * @return 如果字符串为空或者它的长度为零或者仅由空白字符(whitespace)组成时, 返回true;否则返回false
     */
    public static boolean isEmpty(String s) {
        if (s == null) {
            return true;
        }
        s = s.trim();
        return ((s.length() == 0) || s.equals("null"));
    }

    /**
     * 字符是否为空，有"null"的判断
     *
     * @param s
     * @return
     */
    public static boolean notEmpty(String s) {
        return !isEmpty(s);
    }

    /**
     * 判断是否为纯数字
     *
     * @param str
     * @return
     */
    public static boolean isDigit(String str) {

        if (str == null || str.equals("")) {
            return false;
        }

        Pattern pat = Pattern.compile("\\d*");
        return pat.matcher(str).matches();
    }

    /**
     * 是否包含数字
     * @param str
     * @return
     */
    public static boolean isContainsDigit(String str) {
        if (str == null || str.equals("")) {
            return false;
        }
        str = str.replaceAll("\\s*", "");
        Pattern pat = Pattern.compile(".*\\d+.*");
        return pat.matcher(str.replaceAll(" ", "")).matches();
    }

    /**
     * 验证是否是一个邮箱
     *
     * @param email
     *            待验证邮箱号
     * @return boolean
     */
    public static boolean isEmail(String email) {
        Pattern emailPattern = Pattern.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");
        if (StringUtils.isEmpty(email)) {
            return false;
        }
        return emailPattern.matcher(email).matches();
    }
}

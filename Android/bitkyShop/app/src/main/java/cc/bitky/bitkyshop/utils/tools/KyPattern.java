package cc.bitky.bitkyshop.utils.tools;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by bitky on 2016/12/7.
 */

public class KyPattern {
  /**
   * 验证手机号码
   *
   * @param phoneNumber 手机号码
   * @return boolean
   */
  public static boolean checkPhoneNumber(String phoneNumber) {
    Pattern pattern = Pattern.compile("^1[0-9]{10}$");
    Matcher matcher = pattern.matcher(phoneNumber);
    return matcher.matches();
  }

  /**
   * 验证数字字母和中文字符
   *
   * @param phoneNumber 用户名
   * @return boolean
   */
  public static boolean checkUserName(String phoneNumber) {
    Pattern pattern = Pattern.compile("([a-zA-Z0-9\\u4e00-\\u9fa5]{2,24})");
    Matcher matcher = pattern.matcher(phoneNumber);
    return matcher.matches();
  }

  /**
   * 验证数字字母
   *
   * @param phoneNumber 密码
   * @return boolean
   */
  public static boolean checkNumStr(String phoneNumber) {
    Pattern pattern = Pattern.compile("([a-zA-Z0-9]{2,24})");
    Matcher matcher = pattern.matcher(phoneNumber);
    return matcher.matches();
  }
}

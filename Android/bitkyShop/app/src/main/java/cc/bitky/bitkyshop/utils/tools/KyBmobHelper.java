package cc.bitky.bitkyshop.utils.tools;

import cc.bitky.bitkyshop.bean.cart.KyUser;
import cn.bmob.v3.BmobUser;

public class KyBmobHelper {

  /**
   * 获取本地保存的当前用户
   */
  public static KyUser getCurrentUser() {
    return BmobUser.getCurrentUser(KyUser.class);
  }


}

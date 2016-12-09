package cc.bitky.bitkyshop.bean.cart;

import cn.bmob.v3.BmobObject;
import java.io.Serializable;
import java.util.List;

/**
 * Created by bitky on 2016/12/4.
 */

public class Order extends BmobObject implements Serializable {
  String userObjectId;
  String username;
  Boolean isCompleted = false;

  private ReceiveAddress receiveAddress;

  private List<CommodityOrder> commodityList;

  public Order(List<CommodityOrder> commodityList) {
    this.commodityList = commodityList;
  }

  /**
   * 设置收货地址以及当前用户信息
   */
  public void setAddressAndUserInfo(ReceiveAddress receiveAddress) {
    this.receiveAddress = receiveAddress;
    userObjectId = receiveAddress.getUserObjectId();
    username = receiveAddress.getUsername();
  }

  /**
   * JavaBean是否填写完整
   *
   * @return 填写完整的状态
   */
  public Boolean isDone() {
    if (userObjectId != null
        && username != null
        && receiveAddress != null
        && commodityList != null) {
      return true;
    }
    return false;
  }

  public ReceiveAddress getReceiveAddress() {
    return receiveAddress;
  }

  public void setReceiveAddress(ReceiveAddress receiveAddress) {
    this.receiveAddress = receiveAddress;
  }

  public String getUserObjectId() {
    return userObjectId;
  }

  public void setUserObjectId(String userObjectId) {
    this.userObjectId = userObjectId;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public List<CommodityOrder> getCommodityList() {
    return commodityList;
  }

  public void setCommodityList(List<CommodityOrder> commodityList) {
    this.commodityList = commodityList;
  }
}

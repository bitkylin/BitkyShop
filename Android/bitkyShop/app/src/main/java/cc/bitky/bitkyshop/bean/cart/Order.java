package cc.bitky.bitkyshop.bean.cart;

import cn.bmob.v3.BmobObject;
import java.io.Serializable;
import java.util.List;

/**
 * Created by bitky on 2016/12/4.
 */

public class Order extends BmobObject implements Serializable {
  public static final int NONE = 0;
  public static final int POSTED = 100;
  public static final int CONFIRMED = 110;

  public static final int COMPLETED = 200;

  public static final int CANCELLED = 500;
  public static final int DELETED = 600;
  private String userObjectId;
  private String username;
  private Integer status;

  private ReceiveAddress receiveAddress;
  private List<CommodityOrder> commodityList;

  private String name;
  private String phone;
  private String address;

  public Order() {
    status = POSTED;
  }

  public Order(List<CommodityOrder> commodityList) {
    this.commodityList = commodityList;
    status = POSTED;
  }

  /**
   * 设置收货地址以及当前用户信息
   */
  public void setAddressAndUserInfo(ReceiveAddress receiveAddress) {
    this.receiveAddress = receiveAddress;
    userObjectId = receiveAddress.getUserObjectId();
    username = receiveAddress.getUsername();

    name = receiveAddress.getName();
    phone = receiveAddress.getPhone();
    address = receiveAddress.getAddress();
    status = POSTED;
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

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
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

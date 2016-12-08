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
  String name;
  String phone;
  String address;
  Boolean isDefault;
  private List<CommodityOrder> commodityList;

  public Order(List<CommodityOrder> commodityList) {
    this.commodityList = commodityList;
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

  public Boolean getDefault() {
    return isDefault;
  }

  public void setDefault(Boolean aDefault) {
    isDefault = aDefault;
  }

  public List<CommodityOrder> getCommodityList() {
    return commodityList;
  }

  public void setCommodityList(List<CommodityOrder> commodityList) {
    this.commodityList = commodityList;
  }
}

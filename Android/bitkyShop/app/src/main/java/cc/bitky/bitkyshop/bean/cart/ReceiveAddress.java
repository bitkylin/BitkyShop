package cc.bitky.bitkyshop.bean.cart;

import cn.bmob.v3.BmobObject;

public class ReceiveAddress extends BmobObject {

  String userObjectId;
  String username;
  String name;
  String phone;
  String address;
  Boolean isDefault;

  public ReceiveAddress() {
  }

  public ReceiveAddress(String userObjectId, String username, String name, String phone,
      String address) {
    this.userObjectId = userObjectId;
    this.username = username;
    this.name = name;
    this.phone = phone;
    this.address = address;
    isDefault = false;
  }

  public ReceiveAddress(String userObjectId, String username, String name, String phone,
      String address, Boolean isDefault) {
    this.userObjectId = userObjectId;
    this.username = username;
    this.name = name;
    this.phone = phone;
    this.address = address;
    this.isDefault = isDefault;
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
}

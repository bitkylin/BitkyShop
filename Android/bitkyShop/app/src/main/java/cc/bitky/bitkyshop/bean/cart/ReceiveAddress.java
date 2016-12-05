package cc.bitky.bitkyshop.bean.cart;

import java.io.Serializable;

/**
 * Created by bitky on 2016/12/2.
 */

public class ReceiveAddress implements Serializable {

  String Name;
  String Phone;
  String Address;

  public ReceiveAddress(String name, String phone, String address) {
    Name = name;
    Phone = phone;
    Address = address;
  }

  public String getAddress() {
    return Address;
  }

  public void setAddress(String address) {
    Address = address;
  }

  public String getPhone() {
    return Phone;
  }

  public void setPhone(String phone) {
    Phone = phone;
  }

  public String getName() {
    return Name;
  }

  public void setName(String name) {
    Name = name;
  }
}

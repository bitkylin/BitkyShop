package cc.bitky.bitkyshop.bean.cart;

import java.io.Serializable;

/**
 * Created by bitky on 2016/11/24.
 */

public class CommodityOrder implements Serializable {

  private String objectId;
  private Double price;
  private Integer count;

  public CommodityOrder(String objectId, Double price, Integer count) {
    this.objectId = objectId;
    this.price = price;
    this.count = count;
  }

  public String getObjectId() {
    return objectId;
  }

  public void setObjectId(String objectId) {
    this.objectId = objectId;
  }

  public Double getPrice() {
    return price;
  }

  public void setPrice(Double price) {
    this.price = price;
  }

  public Integer getCount() {
    return count;
  }

  public void setCount(Integer count) {
    this.count = count;
  }
}

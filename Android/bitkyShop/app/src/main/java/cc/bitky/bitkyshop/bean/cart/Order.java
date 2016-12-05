package cc.bitky.bitkyshop.bean.cart;

import cn.bmob.v3.BmobObject;
import java.io.Serializable;
import java.util.List;

/**
 * Created by bitky on 2016/12/4.
 */

public class Order extends BmobObject implements Serializable {

  private ReceiveAddress receiveAddress;
  private List<CommodityOrder> commodityList;

  public ReceiveAddress getReceiveAddress() {
    return receiveAddress;
  }

  public void setReceiveAddress(ReceiveAddress receiveAddress) {
    this.receiveAddress = receiveAddress;
  }

  public List<CommodityOrder> getCommodityList() {
    return commodityList;
  }

  public void setCommodityList(List<CommodityOrder> commodityList) {
    this.commodityList = commodityList;
  }
}

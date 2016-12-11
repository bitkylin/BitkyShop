package cc.bitky.bitkyshop.fragment.userfragment.orderactivity;

import cc.bitky.bitkyshop.bean.cart.Order;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;
import java.util.List;

public class OrderManagerPresenter {

  OrderManagerActivity activity;
  String userObjectId;
  int status = Order.NONE;

  public OrderManagerPresenter(OrderManagerActivity activity) {
    this.activity = activity;
  }

  public void queryOrderFormBmob(String objectId, int status) {
    userObjectId = objectId;
    this.status = status;

    BmobQuery<Order> bmobQuery = new BmobQuery<>();
    bmobQuery.addWhereEqualTo("userObjectId", objectId);
    if (status != Order.NONE) {
      bmobQuery.addWhereEqualTo("status", status);
    }
    bmobQuery.order("-createdAt");
    bmobQuery.setLimit(10);
    bmobQuery.findObjects(new FindListener<Order>() {
      @Override public void done(List<Order> list, BmobException e) {
        if (e != null) {
          activity.showMessage(e.getMessage());
          return;
        }
        activity.initCloudOrder(list);
      }
    });
  }

  /**
   * 在服务器中更新订单状态
   *
   * @param orderOrigin 当前本地订单条目
   * @param updateStatus 欲更新到的状态
   */
  public void updateOrderStatus(Order orderOrigin, final int updateStatus) {
    Order order = new Order();
    order.setObjectId(orderOrigin.getObjectId());
    order.setStatus(updateStatus);
    order.update(new UpdateListener() {
      @Override public void done(BmobException e) {
        if (e != null) {
          activity.showMessage(e.getMessage());
        } else {
          switch (updateStatus) {
            case Order.COMPLETED:
              activity.showMessage("确认收货成功");
              break;
            case Order.CANCELLED:
              activity.showMessage("该订单已被取消");
              break;
          }
          if (userObjectId != null) queryOrderFormBmob(userObjectId, status);
        }
      }
    });
  }
}

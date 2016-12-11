package cc.bitky.bitkyshop.fragment.userfragment.orderactivity;

import cc.bitky.bitkyshop.bean.cart.Order;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by bitky on 2016/12/11.
 */

public class OrderActivityPresenter {
  private final OrderActivity activity;

  public OrderActivityPresenter(OrderActivity activity) {
    this.activity = activity;
  }

  /**
   * 在服务器中更新订单状态
   *
   * @param orderOrigin 当前本地订单条目
   * @param updateStatus 欲更新到的状态
   */
  public void updateOrderStatus(final Order orderOrigin, final int updateStatus) {
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
          orderOrigin.setStatus(updateStatus);
          activity.changeOrderStatus(orderOrigin);
        }
      }
    });
  }
}

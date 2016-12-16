package cc.bitky.bitkyshop.fragment.userfragment.orderactivity;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import cc.bitky.bitkyshop.R;
import cc.bitky.bitkyshop.bean.Commodity;
import cc.bitky.bitkyshop.bean.cart.CommodityOrder;
import cc.bitky.bitkyshop.bean.cart.Order;
import cc.bitky.bitkyshop.utils.recyclerview.KyBaseRecyclerAdapter;
import cc.bitky.bitkyshop.utils.recyclerview.KyBaseViewHolder;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import com.lzy.ninegrid.ImageInfo;
import com.lzy.ninegrid.NineGridView;
import com.lzy.ninegrid.NineGridViewAdapter;
import com.socks.library.KLog;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class OrderManagerRecyclerAdapter extends KyBaseRecyclerAdapter<Order> {

  private final Context mContext;
  private OnButtonCompletedClickListener completedClickListener;
  private OnButtonCancelledClickListener cancelledClickListener;
  private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

  /**
   * 初始化 RecyclerView 适配器
   *
   * @param mDatas 绑定的数据
   */
  public OrderManagerRecyclerAdapter(List<Order> mDatas, Context context) {
    super(mDatas, R.layout.recycler_order_manager_activity_show);
    this.mContext = context;
  }

  @Override public void setDataToViewHolder(final Order dataItem, KyBaseViewHolder holder) {
    //设置订单编号
    holder.getTextView(R.id.recycler_orderManagerActivity_orderId).setText(dataItem.getObjectId());

    //设置订单创建时间
    holder.getTextView(R.id.recycler_orderManagerActivity_orderCreatedTime)
        .setText(dataItem.getCreatedAt());

    //设置订单中商品的数量和总价
    //设置自定义九宫格控件
    final ArrayList<ImageInfo> imageInfo = new ArrayList<>();
    List<CommodityOrder> localList = dataItem.getCommodityList();
    int count = 0;
    double price = 0;
    for (final CommodityOrder commodityOrder : localList) {
      count = count + commodityOrder.getCount();
      price = price + commodityOrder.getPrice();
      BmobQuery<Commodity> bmobQuery = new BmobQuery<>();
      bmobQuery.getObject(commodityOrder.getObjectId(), new QueryListener<Commodity>() {
        @Override public void done(Commodity commodity, BmobException e) {
          if (e != null) {
            KLog.d("有异常：" + e.getMessage());
            return;
          }
          ImageInfo info = new ImageInfo();
          info.setThumbnailUrl(commodity.getCoverPhotoUrl());
          info.setBigImageUrl(commodity.getCoverPhotoUrl());
          imageInfo.add(info);
        }
      });
    }
    //NineGridView nineGridView=holder.getView(R.id.recycler_orderManagerActivity_nineGridView);
    //
    //nineGridView.setAdapter(new NineGridViewAdapter(mContext, imageInfo) {
    //});

    holder.getTextView(R.id.recycler_orderManagerActivity_orderItemCount)
        .setText("共" + count + "件");
    holder.getTextView(R.id.recycler_orderManagerActivity_orderPrice).setText(price + "元");

    //设置订单的当前状态
    Button btncancel = holder.getButton(R.id.recycler_orderManagerActivity_cancelOrder);
    Button btnCompleted = holder.getButton(R.id.recycler_orderManagerActivity_confirmCompleted);

    TextView orderStatus = holder.getTextView(R.id.recycler_orderManagerActivity_orderStatus);
    switch (dataItem.getStatus()) {
      case Order.POSTED:
        orderStatus.setText("订单已确认");
        orderStatus.setTextColor(mContext.getResources().getColor(R.color.red));
        btnCompleted.setVisibility(View.VISIBLE);
        btncancel.setVisibility(View.VISIBLE);
        break;
      case Order.CONFIRMED:
        orderStatus.setText("待收货");
        orderStatus.setTextColor(mContext.getResources().getColor(R.color.red));
        btnCompleted.setVisibility(View.VISIBLE);
        btncancel.setVisibility(View.VISIBLE);
        break;
      case Order.COMPLETED:
        orderStatus.setText("订单已完成");
        orderStatus.setTextColor(mContext.getResources().getColor(R.color.green));
        btnCompleted.setVisibility(View.INVISIBLE);
        btncancel.setVisibility(View.INVISIBLE);
        break;
      case Order.CANCELLED:
        orderStatus.setText("订单已取消");
        orderStatus.setTextColor(mContext.getResources().getColor(R.color.gray));
        btnCompleted.setVisibility(View.INVISIBLE);
        btncancel.setVisibility(View.INVISIBLE);
        break;
    }

    //设置按钮的点击监听器
    btnCompleted.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        if (completedClickListener != null) completedClickListener.onClick(dataItem);
      }
    });
    btncancel.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        if (cancelledClickListener != null) cancelledClickListener.onClick(dataItem);
      }
    });
  }

  public void setCompletedClickListener(OnButtonCompletedClickListener completedClickListener) {
    this.completedClickListener = completedClickListener;
  }

  public void setCancelledClickListener(OnButtonCancelledClickListener cancelledClickListener) {
    this.cancelledClickListener = cancelledClickListener;
  }

  interface OnButtonCompletedClickListener {
    void onClick(Order order);
  }

  interface OnButtonCancelledClickListener {
    void onClick(Order order);
  }
}

package cc.bitky.bitkyshop.fragment.userfragment.orderactivity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import cc.bitky.bitkyshop.R;
import cc.bitky.bitkyshop.bean.Commodity;
import cc.bitky.bitkyshop.bean.cart.CommodityOrder;
import cc.bitky.bitkyshop.bean.cart.Order;
import cc.bitky.bitkyshop.bean.cart.ReceiveAddress;
import cc.bitky.bitkyshop.fragment.userfragment.addressactivity.AddressOptionActivity;
import cc.bitky.bitkyshop.utils.KyToolBar;
import cc.bitky.bitkyshop.utils.ToastUtil;
import cc.bitky.bitkyshop.utils.recyclerview.DividerItemDecoration;
import cc.bitky.bitkyshop.utils.recyclerview.KyBaseRecyclerAdapter;
import cc.bitky.bitkyshop.utils.recyclerview.KyBaseViewHolder;
import cc.bitky.bitkyshop.utils.tools.KySet;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;
import com.socks.library.KLog;
import java.util.ArrayList;
import java.util.List;

public class OrderActivity extends AppCompatActivity implements View.OnClickListener {
  private Context mContext;
  private ReceiveAddress receiveAddress;

  private Order order;

  private TextView name;
  private TextView phone;
  private TextView address;

  private View addressLayout;
  private RecyclerView recyclerView;
  private KyBaseRecyclerAdapter recyclerAdapter;
  private TextView tvPriceTotal;

  private Button btnOrderGeneration;
  private ToastUtil toastUtil;
  private List<Commodity> commodities;
  private Button btnCompleted;
  private Button btncancel;
  private View historyOrderInfoLayout;
  private View bottomNavigation;
  private OrderActivityPresenter presenter;
  private TextView orderStatus;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_order);
    mContext = this;
    presenter = new OrderActivityPresenter(this);
    toastUtil = new ToastUtil(mContext);

    historyOrderInfoLayout = findViewById(R.id.orderActivity_relativeLayout_historyOrderInfo);
    TextView orderId = (TextView) findViewById(R.id.orderActivity_historyOrderInfo_OrderId);
    TextView orderCreatedTime =
        (TextView) findViewById(R.id.orderActivity_historyOrderInfo_OrderCreatedTime);
    orderStatus = (TextView) findViewById(R.id.orderActivity_historyOrderInfo_orderStatus);

    addressLayout = findViewById(R.id.orderActivity_addressCardView);
    ImageView imageViewArrowRight =
        (ImageView) findViewById(R.id.orderActivity_addressCardView_ArrowRight);
    name = (TextView) findViewById(R.id.orderActivity_userName);
    phone = (TextView) findViewById(R.id.orderActivity_phone);
    address = (TextView) findViewById(R.id.orderActivity_address);

    recyclerView = (RecyclerView) findViewById(R.id.orderActivity_recycler_commodity);

    bottomNavigation = findViewById(R.id.orderActivity_bottomNavigation);
    tvPriceTotal = (TextView) findViewById(R.id.orderActivity_textview_total);
    btnOrderGeneration = (Button) findViewById(R.id.orderActivity_btn_orderGeneration);
    btnCompleted = (Button) findViewById(R.id.orderActivity_confirmCompleted);
    btncancel = (Button) findViewById(R.id.orderActivity_cancelOrder);

    KyToolBar kyToolBar = (KyToolBar) findViewById(R.id.orderActivity_kyToolbar);
    kyToolBar.setNavigationOnClickListener(this);
    btnOrderGeneration.setOnClickListener(this);
    btnCompleted.setOnClickListener(this);
    btncancel.setOnClickListener(this);

    switchOrderLocation(OrderLocation.local);
    initRecyclerView();

    int requestCode = getIntent().getIntExtra("requestCode", -1);
    KLog.d("requestCode" + requestCode);
    switch (requestCode) {
      case KySet.CART_REQUEST_SUBMIT_ORDER:
        bottomNavigation.setVisibility(View.VISIBLE);
        btnOrderGeneration.setVisibility(View.VISIBLE);
        imageViewArrowRight.setVisibility(View.VISIBLE);
        addressLayout.setVisibility(View.VISIBLE);
        addressLayout.setOnClickListener(this);
        queryDefaultAddressFromBmob();
        break;

      case KySet.USER_REQUEST_HISTORY_ORDER:
        historyOrderInfoLayout.setVisibility(View.VISIBLE);
        bottomNavigation.setVisibility(View.VISIBLE);
        orderId.setText(order.getObjectId());
        orderCreatedTime.setText(order.getCreatedAt());
        changeOrderStatus(order);
        name.setText(order.getName());
        phone.setText(order.getPhone());
        address.setText(order.getAddress());
        break;
    }
    initRecyclerOrderData(order.getCommodityList());
  }

  /**
   * 根据不同的订单状态改变界面显示
   */
  public void changeOrderStatus(Order order) {
    this.order = order;

    switch (order.getStatus()) {
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
  }

  /**
   * 选择 Order 对象所处的位置并获取所需的 Order 对象
   *
   * @param orderLocation Order 对象所处的位置
   */
  private void switchOrderLocation(OrderLocation orderLocation) {
    switch (orderLocation) {
      case local:
        Bundle bundle = getIntent().getBundleExtra("bundle");
        Order order = (Order) bundle.getSerializable("order");
        initOrder(order);
        break;
      case bmob:
        queryCurrentOrderFromBmob();
        break;
    }
  }

  /**
   * 分析获得的Order对象，进行必要的初始化
   *
   * @param order Order对象
   */
  private void initOrder(Order order) {
    String userObjectId = order.getUserObjectId();
    String userName = order.getUsername();

    if (userObjectId != null && userName != null) {
      this.order = order;
    } else {
      Intent intent = new Intent();
      intent.putExtra("message", "请求的JavaBean(Order)内容不完整");
      setResult(KySet.RESULT_ERROR, intent);
      finish();
    }
  }

  private void queryDefaultAddressFromBmob() {
    BmobQuery<ReceiveAddress> bmobQuery = new BmobQuery<>();
    bmobQuery.addWhereEqualTo("userObjectId", order.getUserObjectId());
    bmobQuery.addWhereEqualTo("isDefault", true);
    bmobQuery.setLimit(50);
    bmobQuery.findObjects(new FindListener<ReceiveAddress>() {

      @Override public void done(List<ReceiveAddress> list, BmobException e) {
        if (e != null) {
          toastUtil.show(e.getMessage());
          return;
        }
        if (list.size() > 0) {
          receiveAddress = list.get(0);
          name.setText(receiveAddress.getName());
          phone.setText(receiveAddress.getPhone());
          address.setText(receiveAddress.getAddress());
        }
      }
    });
  }

  /**
   *
   */
  private void queryCurrentOrderFromBmob() {
    BmobQuery<Order> bmobQuery = new BmobQuery<>();
    bmobQuery.addWhereEqualTo("objectId", order.getObjectId());
    bmobQuery.findObjects(new FindListener<Order>() {

      @Override public void done(List<Order> list, BmobException e) {
        if (e != null) {
          toastUtil.show(e.getMessage());
          return;
        }
        if (list.size() > 0) {
          order = list.get(0);
          initOrder(order);
        }
      }
    });
  }

  private void initRecyclerView() {
    commodities = new ArrayList<>();
    if (recyclerAdapter == null) {
      initRecyclerAdapter(new ArrayList<Commodity>());
    }
    recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
    recyclerView.setItemAnimator(new DefaultItemAnimator());
    recyclerView.addItemDecoration(
        new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
    recyclerView.setAdapter(recyclerAdapter);
  }

  private void initRecyclerOrderData(List<CommodityOrder> commodityOrders) {
    for (final CommodityOrder commodityOrder : commodityOrders) {
      BmobQuery<Commodity> bmobQuery = new BmobQuery<>();
      bmobQuery.getObject(commodityOrder.getObjectId(), new QueryListener<Commodity>() {
        @Override public void done(Commodity commodity, BmobException e) {
          if (e != null) {
            KLog.d("有异常：" + e.getMessage());
            return;
          }
          commodity.setCount(commodityOrder.getCount());
          commodities.add(commodity);
          recyclerAdapter.reloadData(commodities);
          countTotalPrices();
        }
      });
    }
  }

  /**
   * 计算并显示已勾选商品的总价
   */
  public void countTotalPrices() {
    double priceTotal = 0;
    if (commodities != null && commodities.size() > 0) {
      for (Commodity commodityLocal : commodities) {
        int count = commodityLocal.getCount();
        double price = commodityLocal.getPrice();
        priceTotal = priceTotal + price * count;
      }
    }
    tvPriceTotal.setText("合计： " + priceTotal + "元");
  }

  public void initRecyclerAdapter(List<Commodity> list) {
    recyclerAdapter =
        new KyBaseRecyclerAdapter<Commodity>(list, R.layout.recycler_orderactivity_show) {
          @Override
          public void setDataToViewHolder(final Commodity dataItem, KyBaseViewHolder holder) {
            holder.getSimpleDraweeView(R.id.recycler_orderActivity_draweeview)
                .setImageURI(Uri.parse(dataItem.getCoverPhotoUrl()));
            holder.getTextView(R.id.recycler_orderActivity_text_title).setText(dataItem.getName());
            holder.getTextView(R.id.recycler_orderActivity_text_price)
                .setText(dataItem.getPrice().toString() + " 元");
            holder.getTextView(R.id.recycler_orderActivity_text_count)
                .setText("X " + dataItem.getCount());
          }
        };
  }

  @Override public void onClick(View v) {
    switch (v.getId()) {

      //导航栏返回
      case R.id.kytoolbar_navigation:
        finish();
        break;

      //收货地址信息CardView，设置收货地址
      case R.id.orderActivity_addressCardView:
        KLog.d("激活");
        Intent intent = new Intent(mContext, AddressOptionActivity.class);
        intent.putExtra("requestCode", KySet.CART_REQUEST_SELECT_RECEIVE_ADDRESS);
        intent.putExtra("userName", order.getUsername());
        intent.putExtra("objectId", order.getUserObjectId());
        startActivityForResult(intent, KySet.CART_REQUEST_SELECT_RECEIVE_ADDRESS);
        break;

      //生成订单按钮
      case R.id.orderActivity_btn_orderGeneration:
        if (receiveAddress != null) {
          order.setAddressAndUserInfo(receiveAddress);
          if (order.isDone()) {
            order.save(new SaveListener<String>() {
              @Override public void done(String objectId, BmobException e) {
                if (e == null) {
                  Intent intentResult = new Intent();
                  Bundle bundle = new Bundle();
                  order.setObjectId(objectId);
                  bundle.putSerializable("order", order);
                  intentResult.putExtra("bundle", bundle);
                  setResult(KySet.CART_RESULT_SUBMIT_ORDER, intentResult);
                  finish();
                }
              }
            });
          }
        }
        break;
      case R.id.orderActivity_confirmCompleted:
        setResult(KySet.USER_RESULT_REFRESH_ORDER);
        presenter.updateOrderStatus(order, Order.COMPLETED);
        break;
      case R.id.orderActivity_cancelOrder:
        setResult(KySet.USER_RESULT_REFRESH_ORDER);
        presenter.updateOrderStatus(order, Order.CANCELLED);
        break;
    }
  }

  public void showMessage(String message) {
    toastUtil.show(message);
  }

  @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    switch (resultCode) {
      case KySet.CART_RESULT_SELECT_RECEIVE_ADDRESS:
        ReceiveAddress receiveAddress = (ReceiveAddress) data.getSerializableExtra("address");
        if (receiveAddress != null) {
          this.receiveAddress = receiveAddress;
          name.setText(receiveAddress.getName());
          phone.setText(receiveAddress.getPhone());
          address.setText(receiveAddress.getAddress());
        }
        break;
    }
  }

  enum OrderLocation {
    local,
    bmob
  }
}

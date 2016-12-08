package cc.bitky.bitkyshop.fragment.userfragment.orderactivity;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import cc.bitky.bitkyshop.R;
import cc.bitky.bitkyshop.bean.Commodity;
import cc.bitky.bitkyshop.bean.cart.CommodityOrder;
import cc.bitky.bitkyshop.bean.cart.Order;
import cc.bitky.bitkyshop.bean.cart.ReceiveAddress;
import cc.bitky.bitkyshop.utils.KyToolBar;
import cc.bitky.bitkyshop.utils.ToastUtil;
import cc.bitky.bitkyshop.utils.recyclerview.DividerItemDecoration;
import cc.bitky.bitkyshop.utils.recyclerview.KyBaseRecyclerAdapter;
import cc.bitky.bitkyshop.utils.recyclerview.KyBaseViewHolder;
import cc.bitky.bitkyshop.utils.recyclerview.KyRecyclerViewDivider;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;
import com.socks.library.KLog;
import java.util.ArrayList;
import java.util.List;

public class OrderActivity extends AppCompatActivity {

  private TextView userName;
  private TextView phone;
  private TextView address;
  private RecyclerView recyclerView;
  private KyBaseRecyclerAdapter recyclerAdapter;
  private TextView tvPriceTotal;
  private Button btnOrder;
  private Context mContext;
  private ToastUtil toastUtil;
  private List<Commodity> commodities;
  private Order order;
  private String userObjectId;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_order);
    mContext = this;
    toastUtil = new ToastUtil(mContext);

    userName = (TextView) findViewById(R.id.orderActivity_userName);
    phone = (TextView) findViewById(R.id.orderActivity_phone);
    address = (TextView) findViewById(R.id.orderActivity_address);
    recyclerView = (RecyclerView) findViewById(R.id.orderActivity_recycler_commodity);
    tvPriceTotal = (TextView) findViewById(R.id.orderActivity_textview_total);
    btnOrder = (Button) findViewById(R.id.orderActivity_btn_order);
    KyToolBar kyToolBar = (KyToolBar) findViewById(R.id.orderActivity_kyToolbar);
    kyToolBar.setNavigationOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        finish();
      }
    });

    initOrder();
    initDefaultAddress();
    initRecyclerView(order.getCommodityList());
  }

  private void initDefaultAddress() {
    BmobQuery<ReceiveAddress> bmobQuery = new BmobQuery<>();
    bmobQuery.addWhereEqualTo("userObjectId", userObjectId);
    bmobQuery.addWhereEqualTo("isDefault", true);
    bmobQuery.setLimit(50);
    bmobQuery.findObjects(new FindListener<ReceiveAddress>() {

      private ReceiveAddress receiveAddress;

      @Override public void done(List<ReceiveAddress> list, BmobException e) {
        if (e != null) {
          toastUtil.show(e.getMessage());
          return;
        }
        if (list.size() > 0) {
          receiveAddress = list.get(0);
          userName.setText(receiveAddress.getName());
          phone.setText(receiveAddress.getPhone());
          address.setText(receiveAddress.getAddress());
        }
      }
    });
  }

  private void initOrder() {
    Bundle bundle = getIntent().getBundleExtra("bundle");
    String userObjectId = bundle.getString("userObjectId");
    Order order = (Order) bundle.getSerializable("order");

    if (order != null && userObjectId != null) {
      this.order = order;
      this.userObjectId = userObjectId;
    } else {
      finish();
    }
  }

  private void initRecyclerView(List<CommodityOrder> commodityOrders) {
    commodities = new ArrayList<>();
    if (recyclerAdapter == null) {
      initRecyclerViewData(new ArrayList<Commodity>());
    }
    recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
    recyclerView.setItemAnimator(new DefaultItemAnimator());
    recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL_LIST));
    recyclerView.setAdapter(recyclerAdapter);
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

  public void initRecyclerViewData(List<Commodity> list) {
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
}

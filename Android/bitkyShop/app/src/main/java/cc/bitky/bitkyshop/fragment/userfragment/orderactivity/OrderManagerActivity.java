package cc.bitky.bitkyshop.fragment.userfragment.orderactivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import cc.bitky.bitkyshop.R;
import cc.bitky.bitkyshop.bean.cart.Order;
import cc.bitky.bitkyshop.utils.ToastUtil;
import cc.bitky.bitkyshop.utils.recyclerview.KyBaseRecyclerAdapter;
import cc.bitky.bitkyshop.utils.tools.KySet;
import com.socks.library.KLog;
import java.util.ArrayList;
import java.util.List;

public class OrderManagerActivity extends AppCompatActivity {
  Context mContext;
  private ToastUtil toastUtil;
  private OrderManagerRecyclerAdapter recyclerAdapter;
  private OrderManagerPresenter presenter;
  private String objectId;
  private String username;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_order_manager);
    mContext = this;
    toastUtil = new ToastUtil(mContext);
    presenter = new OrderManagerPresenter(this);

    initRecyclerView(new ArrayList<Order>());

    //初始化本地User对象并获取远端Order
    String objectId = getIntent().getStringExtra("objectId");
    String username = getIntent().getStringExtra("userName");
    if (objectId != null && username != null) {
      this.objectId = objectId;
      this.username = username;
      presenter.queryOrderFormBmob(objectId, Order.NONE);
    } else {
      KLog.d("未知错误");
      toastUtil.show("未知错误");
      finish();
    }
  }

  private void initRecyclerView(List<Order> list) {
    RecyclerView recyclerView = (RecyclerView) findViewById(R.id.orderManagerActivity_recycler_All);
    if (recyclerAdapter == null) {
      recyclerAdapter = new OrderManagerRecyclerAdapter(list, mContext);
      recyclerAdapter.setOnClickListener(
          new KyBaseRecyclerAdapter.KyRecyclerViewItemOnClickListener<Order>() {
            @Override public void Onclick(View v, int adapterPosition, Order data) {
              Bundle bundle = new Bundle();
              bundle.putSerializable("order", data);
              Intent intent = new Intent(mContext, OrderActivity.class);
              intent.putExtra("bundle", bundle);
              intent.putExtra("requestCode", KySet.USER_REQUEST_HISTORY_ORDER);
              startActivityForResult(intent, KySet.USER_REQUEST_HISTORY_ORDER);
            }
          });
      recyclerAdapter.setCompletedClickListener(
          new OrderManagerRecyclerAdapter.OnButtonCompletedClickListener() {
            @Override public void onClick(Order order) {
              presenter.updateOrderStatus(order, Order.COMPLETED);
            }
          });
      recyclerAdapter.setCancelledClickListener(
          new OrderManagerRecyclerAdapter.OnButtonCancelledClickListener() {
            @Override public void onClick(Order order) {
              presenter.updateOrderStatus(order, Order.CANCELLED);
            }
          });
    }
    recyclerView.setLayoutManager(new LinearLayoutManager(this));
    recyclerView.setItemAnimator(new DefaultItemAnimator());
    recyclerView.setAdapter(recyclerAdapter);
  }

  @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (resultCode == KySet.USER_RESULT_REFRESH_ORDER) {
      presenter.queryOrderFormBmob(objectId, Order.NONE);
    }
  }

  public void showMessage(String message) {
    toastUtil.show(message);
  }

  public void initCloudOrder(List<Order> list) {
    recyclerAdapter.reloadData(list);
  }
}

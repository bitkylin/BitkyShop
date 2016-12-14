package cc.bitky.bitkyshop.fragment.cartfragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import cc.bitky.bitkyshop.R;
import cc.bitky.bitkyshop.bean.cart.CommodityLocal;
import cc.bitky.bitkyshop.bean.cart.CommodityOrder;
import cc.bitky.bitkyshop.bean.cart.KyUser;
import cc.bitky.bitkyshop.bean.cart.Order;
import cc.bitky.bitkyshop.fragment.userfragment.orderactivity.OrderActivity;
import cc.bitky.bitkyshop.globalDeploy.GreenDaoKyHelper;
import cc.bitky.bitkyshop.utils.ToastUtil;
import cc.bitky.bitkyshop.utils.recyclerview.DividerItemDecoration;
import cc.bitky.bitkyshop.utils.tools.KyBmobHelper;
import cc.bitky.bitkyshop.utils.tools.KySet;
import cc.bitky.bitkyshop.utils.widget.ItemCounter;
import com.socks.library.KLog;
import java.util.ArrayList;
import java.util.List;

public class CartFragment extends Fragment implements View.OnClickListener {
  Context mContext;
  private View view;
  ToastUtil toastUtil;
  private List<CommodityLocal> commodityLocals;
  private CartRecyclerAdapter recyclerAdapter;
  private RecyclerView recyclerViewCategray;
  private TextView textViewTotalPrice;
  private CheckBox checkBoxAll;
  private Button btnDeleteAll;
  private Button btnOrderAll;

  @Override public void onAttach(Context context) {
    super.onAttach(context);
    mContext = context;
  }

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    toastUtil = new ToastUtil(mContext);
  }

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    super.onCreateView(inflater, container, savedInstanceState);
    KLog.d("onCreateView");
    view = inflater.inflate(R.layout.fragment_cart, container, false);
    initToolbar();
    initTallyBar();
    initRecyclerView();

    countTotalPrices();
    updateCheckedAll();
    return view;
  }

  private void initToolbar() {
    CartToolbar cartToolbar = (CartToolbar) view.findViewById(R.id.cartfragment_toolbar);
    cartToolbar.setTextViewEditClickListener(new CartToolbar.OnTextViewEditClickListener() {
      @Override public void OnClick(CartToolbar.TextViewEditType type) {
        if (recyclerAdapter == null) return;
        switch (type) {
          case edit:
            recyclerAdapter.showItemDeleteButton(true);
            btnDeleteAll.setVisibility(View.VISIBLE);
            btnOrderAll.setVisibility(View.GONE);
            break;
          case completed:
            recyclerAdapter.showItemDeleteButton(false);
            btnDeleteAll.setVisibility(View.GONE);
            btnOrderAll.setVisibility(View.VISIBLE);
            break;
        }
      }
    });
  }

  private void initTallyBar() {
    textViewTotalPrice = (TextView) view.findViewById(R.id.cartfragment_textview_total);
    checkBoxAll = (CheckBox) view.findViewById(R.id.cartfragment_checkbox_all);
    checkBoxAll.setOnClickListener(this);
    btnDeleteAll = (Button) view.findViewById(R.id.cartfragment_btn_deleteAll);
    btnDeleteAll.setOnClickListener(this);
    btnOrderAll = (Button) view.findViewById(R.id.cartfragment_btn_order);
    btnOrderAll.setOnClickListener(this);
  }

  /**
   * 从本地数据库中读取数据并初始化RecyclerView
   */
  private void initRecyclerView() {
    commodityLocals = GreenDaoKyHelper.queryAll();
    KLog.d("本地数据库，commodityLocals:" + commodityLocals.size());
    if (recyclerAdapter != null) {
      recyclerAdapter.showItemDeleteButton(false);
      recyclerAdapter.reloadData(commodityLocals);
    } else {
      recyclerAdapter = new CartRecyclerAdapter(commodityLocals);
      recyclerAdapter.setOnCheckboxListener(new CartRecyclerAdapter.CheckboxListener() {
        @Override public void OnClick(CheckBox v) {
          countTotalPrices();
          updateCheckedAll();
        }
      });
      recyclerAdapter.setOnItemCounterListener(new CartRecyclerAdapter.ItemCounterListener() {
        @Override public void addOverflow() {
          KLog.d("addOverflow()");
        }

        @Override public void minusOverflow() {
          KLog.d("minusOverflow()");
        }

        @Override public void OnButtonClick(int currentCount, ItemCounter.ButtonType type) {
          countTotalPrices();
        }
      });
      recyclerAdapter.setOnDeleteItemListener(new CartRecyclerAdapter.DeleteItemListener() {
        @Override public void OnClick(CommodityLocal dataItem) {
          GreenDaoKyHelper.deleteItem(dataItem);
          countTotalPrices();
          updateCheckedAll();
        }
      });
    }
    recyclerViewCategray = (RecyclerView) view.findViewById(R.id.recyclerView_cartFragment);
    recyclerViewCategray.setAdapter(recyclerAdapter);
    recyclerViewCategray.setLayoutManager(new LinearLayoutManager(mContext));
    recyclerViewCategray.setItemAnimator(new DefaultItemAnimator());
    recyclerViewCategray.addItemDecoration(
        new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL_LIST));
  }

  private void itemSetIsChecked(Boolean isChecked) {
    if (commodityLocals != null) {
      for (CommodityLocal commodityLocal : commodityLocals) {
        commodityLocal.setCartIsChecked(isChecked);
        GreenDaoKyHelper.updateChecked(commodityLocal);
      }
    }
    if (recyclerAdapter != null) recyclerAdapter.notifyDataSetChanged();
  }

  /**
   * 更新全选复选框的状态
   */
  private void updateCheckedAll() {
    Boolean isCheckedAll = true;
    if (commodityLocals == null || commodityLocals.size() == 0) {
      isCheckedAll = false;
    } else {
      for (CommodityLocal commodityLocal : commodityLocals) {
        if (!commodityLocal.getCartIsChecked()) {
          isCheckedAll = false;
          break;
        }
      }
    }
    KLog.d("isCheckedAll+" + isCheckedAll);
    checkBoxAll.setChecked(isCheckedAll);
  }

  /**
   * 计算并显示已勾选商品的总价
   */
  public void countTotalPrices() {
    double priceTotal = 0;
    if (commodityLocals != null && commodityLocals.size() > 0) {
      for (CommodityLocal commodityLocal : commodityLocals) {
        if (!commodityLocal.getCartIsChecked()) continue;
        int count = commodityLocal.getCartCount();
        double price = commodityLocal.getPrice();
        priceTotal = priceTotal + price * count;
      }
    }
    textViewTotalPrice.setText("合计： " + priceTotal + "元");
  }

  @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    switch (resultCode) {
      case KySet.CART_RESULT_SUBMIT_ORDER:
        toastUtil.show("添加订单成功");
        btnDeleteAll.performClick();
        break;
    }
  }

  @Override public void onClick(View v) {
    switch (v.getId()) {
      case R.id.cartfragment_checkbox_all:
        itemSetIsChecked(checkBoxAll.isChecked());
        countTotalPrices();
        break;
      case R.id.cartfragment_btn_deleteAll:
        GreenDaoKyHelper.deleteItems(recyclerAdapter.deleteCheckedItem());
        checkBoxAll.setChecked(false);
        countTotalPrices();
        break;
      case R.id.cartfragment_btn_order:
        KyUser kyUser = KyBmobHelper.getCurrentUser();
        if (kyUser == null) {
          toastUtil.show("请登录后再提交订单");
          return;
        }

        List<CommodityOrder> commodityOrders = new ArrayList<>();
        for (CommodityLocal commodityLocal : commodityLocals) {
          if (commodityLocal.getCartIsChecked()) {
            CommodityOrder commodityOrder =
                new CommodityOrder(commodityLocal.getObjectId(), commodityLocal.getPrice(),
                    commodityLocal.getCartCount());
            commodityOrders.add(commodityOrder);
          }
        }
        if (commodityOrders.size()>=50)
        {
          toastUtil.show("您勾选的商品太多,请勾选少于50个");
          return;
        }
        if (commodityOrders.size() > 0) {
          Order order = new Order(commodityOrders);
          order.setUserObjectId(kyUser.getObjectId());
          order.setUsername(kyUser.getUsername());

          Bundle bundle = new Bundle();
          bundle.putSerializable("order", order);
          Intent intent = new Intent(mContext, OrderActivity.class);
          intent.putExtra("bundle", bundle);
          intent.putExtra("requestCode", KySet.CART_REQUEST_SUBMIT_ORDER);
          startActivityForResult(intent, KySet.CART_REQUEST_SUBMIT_ORDER);
        } else {
          toastUtil.show("未勾选商品");
        }
        break;
    }
  }
}
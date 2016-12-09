package cc.bitky.bitkyshop.fragment.userfragment.addressactivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import cc.bitky.bitkyshop.R;
import cc.bitky.bitkyshop.bean.cart.ReceiveAddress;
import cc.bitky.bitkyshop.fragment.userfragment.addressactivity.AddressOptionPresenter.Type;
import cc.bitky.bitkyshop.utils.KyToolBar;
import cc.bitky.bitkyshop.utils.ToastUtil;
import cc.bitky.bitkyshop.utils.recyclerview.DividerItemDecoration;
import cc.bitky.bitkyshop.utils.recyclerview.KyBaseRecyclerAdapter;
import cc.bitky.bitkyshop.utils.recyclerview.KyBaseViewHolder;
import cc.bitky.bitkyshop.utils.tools.KySet;
import com.socks.library.KLog;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class AddressOptionActivity extends AppCompatActivity implements View.OnClickListener {

  private AddressOptionPresenter presenter;
  private ToastUtil toastUtil;
  private RecyclerView recyclerView;
  private KyBaseRecyclerAdapter<ReceiveAddress> recyclerAdapter;
  private String objectId;
  private String username;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_address_option);
    presenter = new AddressOptionPresenter(this);
    toastUtil = new ToastUtil(this);

    //初始化Toolbar
    KyToolBar kyToolBar = (KyToolBar) findViewById(R.id.addressOptionActivity_kyToolbar);
    kyToolBar.setNavigationOnClickListener(this);
    kyToolBar.setRightButtonOnClickListener(this);

    initRecyclerView();

    //由订单确认页面进入时的初始化
    int requestCode = getIntent().getIntExtra("requestCode", -1);
    if (requestCode == KySet.CART_REQUEST_SELECT_RECEIVE_ADDRESS) {
      kyToolBar.setTitle("点击选择收货地址");
      recyclerAdapter.setOnClickListener(
          new KyBaseRecyclerAdapter.KyRecyclerViewItemOnClickListener<ReceiveAddress>() {
            @Override public void Onclick(View v, int adapterPosition, ReceiveAddress data) {
              Intent intent = getIntent();
              intent.putExtra("address", data);
              setResult(KySet.CART_RESULT_SELECT_RECEIVE_ADDRESS, intent);
              finish();
            }
          });
    }

    //初始化本地User对象并获取远端ReceivedAddress
    String objectId = getIntent().getStringExtra("objectId");
    String username = getIntent().getStringExtra("userName");
    if (objectId != null && username != null) {
      this.objectId = objectId;
      this.username = username;
      presenter.getCurrentUserAddress(objectId);
    } else {
      KLog.d("未知错误");
      toastUtil.show("未知错误");
      finish();
    }
  }

  private void initRecyclerView() {
    recyclerView = (RecyclerView) findViewById(R.id.addressOptionActivity_RecyclerView);
    if (recyclerAdapter == null) {
      initRecyclerViewData(new ArrayList<ReceiveAddress>());
    }
    recyclerView.setLayoutManager(new LinearLayoutManager(this));
    recyclerView.setItemAnimator(new DefaultItemAnimator());
    recyclerView.addItemDecoration(
        new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
    recyclerView.setAdapter(recyclerAdapter);
  }

  public void initRecyclerViewData(List<ReceiveAddress> list) {
    recyclerAdapter = new KyBaseRecyclerAdapter<ReceiveAddress>(list,
        R.layout.recycler_userfragment_receive_address) {

      @Override
      public void setDataToViewHolder(final ReceiveAddress dataItem, KyBaseViewHolder holder) {
        holder.getTextView(R.id.recycler_userfragment_receiveAddress_name)
            .setText(dataItem.getName());
        holder.getTextView(R.id.recycler_userfragment_receiveAddress_phone)
            .setText(dataItem.getPhone());
        holder.getTextView(R.id.recycler_userfragment_receiveAddress_address)
            .setText(dataItem.getAddress());
        holder.getCheckBox(R.id.recycler_userfragment_receiveAddress_is_default)
            .setChecked(dataItem.getDefault());
        holder.getCheckBox(R.id.recycler_userfragment_receiveAddress_is_default)
            .setOnClickListener(new View.OnClickListener() {
              @Override public void onClick(View v) {
                updateCheckedStatus((CheckBox) v, dataItem);
              }
            });
        holder.getTextView(R.id.recycler_userfragment_receiveAddress_delete)
            .setOnClickListener(new View.OnClickListener() {
              @Override public void onClick(View v) {
                deleteItem(dataItem);
              }
            });
      }
    };
  }

  /**
   * 删除指定的Item
   *
   * @param dataItem 指定的Item
   */
  private void deleteItem(ReceiveAddress dataItem) {
    if (dataItem.getDefault()) {
      showMessage("不能删除默认地址", null);
    } else {
      presenter.deleteUserAddress(dataItem.getObjectId());
    }
  }

  /**
   * 升级地址列表的"被设为默认的"状态
   *
   * @param c 当前被点击的 Checkbox
   * @param dataItem 被点击的view所对应的Item
   */
  private void updateCheckedStatus(CheckBox c, ReceiveAddress dataItem) {
    if (!c.isChecked()) {
      c.setChecked(true);
    } else {
      for (ReceiveAddress receiveAddress : recyclerAdapter.getDataItems()) {
        if (receiveAddress.getDefault()) {
          receiveAddress.setDefault(false);
          presenter.updateUserAddressDefault(receiveAddress.getObjectId(), false);
        }
      }

      dataItem.setDefault(true);
      presenter.updateUserAddressDefault(dataItem.getObjectId(), true);
      recyclerAdapter.notifyDataSetChanged();
    }
  }

  @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    switch (resultCode) {
      case KySet.USER_RESULT_CREATE_ADDRESS:
        String name = data.getStringExtra("name");
        String phone = data.getStringExtra("phone");
        String address = data.getStringExtra("address");

        if (name != null && phone != null && address != null) {
          ReceiveAddress createdAddress =
              new ReceiveAddress(objectId, username, name, phone, address);
          if (recyclerAdapter.getDataItems().size() == 0) createdAddress.setDefault(true);
          presenter.insertUserAddress(createdAddress);
        }
    }
  }

  @Override public void onClick(View v) {
    switch (v.getId()) {
      case R.id.kytoolbar_navigation:
        finish();
        break;
      case R.id.kytoolbar_rightButton:
        Intent intent = new Intent(this, CreateAddressActivity.class);
        startActivityForResult(intent, KySet.USER_REQUEST_CREATE_ADDRESS);
        break;
    }
  }

  /**
   * 从云端读入的数据
   *
   * @param receiveList 完全正确的数据
   */
  public void initReceiveAddress(List<ReceiveAddress> receiveList) {
    if (receiveList != null && receiveList.size() > 0) {
      recyclerAdapter.reloadData(receiveList);
    }
  }

  public void showMessage(String message, Type type) {
    if (type != null) {
      switch (type) {
        case deleteAddressSuccess:
          //本地删除被选定的Item
          Iterator<ReceiveAddress> iterator = recyclerAdapter.getDataItems().iterator();
          while (iterator.hasNext()) {
            ReceiveAddress address = iterator.next();
            if (address.getObjectId().equals(message)) {
              int index = recyclerAdapter.getDataItems().indexOf(address);
              iterator.remove();
              recyclerAdapter.notifyItemRemoved(index);
            }
          }
          break;
      }
      return;
    }
    if (message != null) {
      toastUtil.show(message);
      return;
    }
  }
}

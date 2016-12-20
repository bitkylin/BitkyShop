package cc.bitky.bitkyshop.fragment.userfragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import cc.bitky.bitkyshop.CommodityDetailActivity;
import cc.bitky.bitkyshop.R;
import cc.bitky.bitkyshop.bean.cart.KyUser;
import cc.bitky.bitkyshop.fragment.userfragment.addressactivity.AddressOptionActivity;
import cc.bitky.bitkyshop.fragment.userfragment.loginfragment.LoginActivity;
import cc.bitky.bitkyshop.fragment.userfragment.orderactivity.OrderManagerActivity;
import cc.bitky.bitkyshop.utils.tools.KyBmobHelper;
import cc.bitky.bitkyshop.utils.tools.KyPattern;
import cc.bitky.bitkyshop.utils.tools.KySet;
import cn.bmob.v3.BmobUser;

public class UserFragment extends Fragment implements View.OnClickListener {

  private TextView textViewUserNameShow;
  private TextView textViewUserLogOut;

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    super.onCreateView(inflater, container, savedInstanceState);
    View view = inflater.inflate(R.layout.fragment_user, container, false);
    textViewUserNameShow = (TextView) view.findViewById(R.id.userFragment_userName_show);
    view.findViewById(R.id.userfragment_button_logout).setOnClickListener(this);
    textViewUserLogOut = (TextView) view.findViewById(R.id.userFragment_userName_logout);
    LinearLayout cardViewAddress =
        (LinearLayout) view.findViewById(R.id.userFragment_addressCardView);
    LinearLayout cardViewOrder = (LinearLayout) view.findViewById(R.id.userFragment_orderCardView);

    textViewUserNameShow.setOnClickListener(this);
    textViewUserLogOut.setOnClickListener(this);
    cardViewAddress.setOnClickListener(this);
    cardViewOrder.setOnClickListener(this);
    initCurrentUser();
    return view;
  }

  /**
   * 读取本地缓存的已登录用户
   */
  private void initCurrentUser() {
    KyUser kyUser = KyBmobHelper.getCurrentUser();
    if (kyUser != null) {
      String username = kyUser.getUsername();
      if (KyPattern.checkPhoneNumber(username)) {
        textViewUserNameShow.setText(username.substring(0, 3) + "****" + username.substring(7, 11));
      } else {
        textViewUserNameShow.setText(username);
      }
      textViewUserLogOut.setVisibility(View.VISIBLE);
    } else {
      textViewUserNameShow.setText("点击登录");
      textViewUserLogOut.setVisibility(View.GONE);
    }
  }

  @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    switch (resultCode) {
      case KySet.USER_RESULT_LOG_IN:
        //Bundle bundle = data.getBundleExtra("bundle");
        //String objectId = bundle.getString("objectId");
        //String username = bundle.getString("username");
        initCurrentUser();
        break;
    }
  }

  @Override public void onClick(View v) {
    if (textViewUserNameShow.getText().toString().trim().equals("点击登录")
        || KyBmobHelper.getCurrentUser() == null) {
      Intent intent = new Intent(getContext(), LoginActivity.class);
      startActivityForResult(intent, KySet.USER_REQUEST_LOG_IN);
      return;
    }
    switch (v.getId()) {
      case R.id.userFragment_userName_show:
      case R.id.userFragment_userName_logout:
        textViewUserNameShow.setText("点击登录");
        BmobUser.logOut();
        textViewUserLogOut.setVisibility(View.GONE);
        break;
      case R.id.userFragment_addressCardView:
        Intent intentAddress = new Intent(getContext(), AddressOptionActivity.class);
        intentAddress.putExtra("objectId", KyBmobHelper.getCurrentUser().getObjectId());
        intentAddress.putExtra("userName", KyBmobHelper.getCurrentUser().getUsername());
        startActivity(intentAddress);
        break;
      case R.id.userFragment_orderCardView:
        Intent intentOrder = new Intent(getContext(), OrderManagerActivity.class);
        intentOrder.putExtra("objectId", KyBmobHelper.getCurrentUser().getObjectId());
        intentOrder.putExtra("userName", KyBmobHelper.getCurrentUser().getUsername());
        startActivity(intentOrder);
        break;
      case R.id.userfragment_button_logout:
        Intent intent = new Intent(getContext(), CommodityDetailActivity.class);
        startActivity(intent);
        break;
    }
  }
}

package cc.bitky.bitkyshop.fragment.userfragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import cc.bitky.bitkyshop.R;
import cc.bitky.bitkyshop.bean.cart.KyUser;
import cc.bitky.bitkyshop.fragment.userfragment.util.KySet;
import cn.bmob.v3.BmobUser;

public class UserFragment extends Fragment implements View.OnClickListener {
  KyUser kyUser;
  private TextView textViewUserNameShow;
  private TextView textViewUserLogOut;

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    super.onCreateView(inflater, container, savedInstanceState);
    View view = inflater.inflate(R.layout.fragment_user, container, false);
    textViewUserNameShow = (TextView) view.findViewById(R.id.userFragment_userName_show);
    textViewUserLogOut = (TextView) view.findViewById(R.id.userFragment_userName_logout);

    textViewUserNameShow.setOnClickListener(this);
    textViewUserLogOut.setOnClickListener(this);
    KyUser kyUser = BmobUser.getCurrentUser(KyUser.class);
    if (kyUser != null) {
      textViewUserNameShow.setText(kyUser.getUsername());
      textViewUserLogOut.setVisibility(View.VISIBLE);
    }
    return view;
  }

  @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    switch (resultCode) {
      case KySet.USER_RESULT_LOG_IN:
        Bundle bundle = data.getBundleExtra("bundle");
        String objectId = bundle.getString("objectId");
        String username = bundle.getString("username");
        textViewUserNameShow.setText(username);
        textViewUserLogOut.setVisibility(View.VISIBLE);
        break;
    }
  }

  @Override public void onClick(View v) {
    switch (v.getId()){
      case R.id.userFragment_userName_show:
      case R.id.userFragment_userName_logout:
        if (textViewUserNameShow.getText().toString().trim().equals("点击登录")) {
          Intent intent = new Intent(getContext(), LoginActivity.class);
          startActivityForResult(intent, KySet.USER_REQUEST_LOG_IN);
        } else {
          textViewUserNameShow.setText("点击登录");
          BmobUser.logOut();
          textViewUserLogOut.setVisibility(View.GONE);
        }
        break;
    }
  }
}

//KyUser kyUser = new KyUser();
//kyUser.setUsername("lml");
//kyUser.setPassword("123");
//
//ReceiveAddress receiveAddress = new ReceiveAddress("lml", "18593275591", "桂林电子科技大学金鸡岭校区");
//ReceiveAddress receiveAddress2 =
//    new ReceiveAddress("lml2", "18593275592", "桂林电子科技大学金鸡岭校区2");
//ReceiveAddress receiveAddress3 =
//    new ReceiveAddress("lml3", "18593275593", "桂林电子科技大学金鸡岭校区3");
//List<ReceiveAddress> receiveAddresses = new ArrayList<>();
//receiveAddresses.add(receiveAddress);
//receiveAddresses.add(receiveAddress2);
//receiveAddresses.add(receiveAddress3);
//kyUser.setAddressList(receiveAddresses);
//kyUser.setDefaultAddress(receiveAddress2);
//kyUser.signUp(new SaveListener<KyUser>() {
//  @Override public void done(KyUser kyUser, BmobException e) {
//    if (e == null) {
//      KLog.d("注册成功");
//    } else {
//      KLog.d("错误信息：" + e.getMessage());
//    }
//  }
//});

//Button button2 = (Button) view.findViewById(R.id.userfragment_button_login);
//button2.setOnClickListener(new View.OnClickListener() {
//  @Override public void onClick(View v) {
//    KyUser kyUser = new KyUser();
//    kyUser.setUsername("lml");
//    kyUser.setPassword("123");
//    kyUser.loginObservable(KyUser.class).subscribe(new Subscriber<KyUser>() {
//      @Override public void onCompleted() {
//        KLog.d("onCompleted");
//      }
//
//      @Override public void onError(Throwable throwable) {
//        KLog.d("onCompleted");
//      }
//
//      @Override public void onNext(KyUser kyUser) {
//
//        Gson gson = new Gson();
//        KLog.json(gson.toJson(kyUser));
//      }
//    });
//  }
//});
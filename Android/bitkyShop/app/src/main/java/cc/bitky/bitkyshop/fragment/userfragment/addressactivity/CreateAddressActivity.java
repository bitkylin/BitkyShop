package cc.bitky.bitkyshop.fragment.userfragment.addressactivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import cc.bitky.bitkyshop.R;
import cc.bitky.bitkyshop.utils.KyToolBar;
import cc.bitky.bitkyshop.utils.ToastUtil;
import cc.bitky.bitkyshop.utils.tools.KyPattern;
import cc.bitky.bitkyshop.utils.tools.KySet;

public class CreateAddressActivity extends AppCompatActivity implements View.OnClickListener {

  private TextView textViewUserName;
  private TextView textViewPhone;
  private TextView textViewAddress;
  private ToastUtil toastUtil;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_create_address);
    toastUtil = new ToastUtil(this);
    KyToolBar kyToolBar = (KyToolBar) findViewById(R.id.createAddressActivity_kyToolbar);
    kyToolBar.setNavigationOnClickListener(this);
    kyToolBar.setRightTextViewOnClickListener(this);

    textViewUserName = (TextView) findViewById(R.id.createAddressActivity_userName);
    textViewPhone = (TextView) findViewById(R.id.createAddressActivity_phone);
    textViewAddress = (TextView) findViewById(R.id.createAddressActivity_address);
  }

  @Override public void onClick(View v) {
    switch (v.getId()) {
      case R.id.kytoolbar_navigation:
        finish();
        break;
      case R.id.kytoolbar_rightTextView:
        pushNewAddress();
        break;
    }
  }

  private void pushNewAddress() {
    InputMethodManager imm =
        (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
    imm.hideSoftInputFromWindow(textViewUserName.getWindowToken(), 0);

    String name = textViewUserName.getText().toString().trim();
    String phone = textViewPhone.getText().toString().trim();
    String address = textViewAddress.getText().toString().trim();

    if (name.length() == 0 || address.length() == 0) {
      toastUtil.show("请将上面的编辑框填写完整");
      return;
    }
    if (!KyPattern.checkPhoneNumber(phone)) {
      toastUtil.show("请输入正确的手机号码");
      return;
    }
    Intent intent = new Intent();
    intent.putExtra("name", name);
    intent.putExtra("phone", phone);
    intent.putExtra("address", address);
    setResult(KySet.USER_RESULT_CREATE_ADDRESS, intent);
    finish();
  }
}

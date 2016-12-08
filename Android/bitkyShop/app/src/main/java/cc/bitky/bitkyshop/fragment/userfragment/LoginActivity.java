package cc.bitky.bitkyshop.fragment.userfragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;
import cc.bitky.bitkyshop.R;
import cc.bitky.bitkyshop.bean.cart.KyUser;
import cc.bitky.bitkyshop.utils.tools.KySet;
import cc.bitky.bitkyshop.utils.KyToolBar;
import cc.bitky.bitkyshop.utils.ToastUtil;
import cc.bitky.bitkyshop.utils.tools.KyPattern;
import rx.Subscriber;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
  ToastUtil toastUtil;
  private TextView textViewUserName;
  private TextView textViewPassword;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_login);
    toastUtil = new ToastUtil(this);
    KyToolBar kyToolBar = (KyToolBar) findViewById(R.id.loginActivity_toolbar);
    kyToolBar.setNavigationOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        finish();
      }
    });
    TextView textViewSignup = (TextView) findViewById(R.id.loginActivity_TextView_signup);
    TextView textViewForgetPassword =
        (TextView) findViewById(R.id.loginActivity_TextView_forgetPassword);
    Button btnLogin = (Button) findViewById(R.id.loginActivity_btn_login);
    textViewSignup.setOnClickListener(this);
    textViewForgetPassword.setOnClickListener(this);
    btnLogin.setOnClickListener(this);
    textViewUserName = (TextView) findViewById(R.id.loginActivity_TextView_userName);
    textViewPassword = (TextView) findViewById(R.id.loginActivity_TextView_password);
  }

  @Override public void onClick(View v) {
    switch (v.getId()) {
      case R.id.loginActivity_TextView_signup:
        Intent intent = new Intent(this, SignupActivity.class);
        startActivityForResult(intent, KySet.USER_REQUEST_SIGN_UP);
        break;
      case R.id.loginActivity_TextView_forgetPassword:
        break;
      case R.id.loginActivity_btn_login:
        textViewUserName.clearFocus();
        textViewPassword.clearFocus();
        InputMethodManager imm =
            (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(textViewUserName.getWindowToken(), 0);
        String userName = textViewUserName.getText().toString().trim();
        String password = textViewPassword.getText().toString().trim();
        userLogin(userName, password);
        break;
    }
  }

  private void userLogin(String userName, String password) {
    if (userName.length() == 0 || password.length() == 0) {
      toastUtil.show("请输入用户名和密码");
      return;
    }
    //验证语法规则
    if (!KyPattern.checkUserName(userName)) {
      toastUtil.show("用户名只能使用中文、英文和数字");
      return;
    }
    if (!KyPattern.checkNumStr(password)) {
      toastUtil.show("密码只能使用英文和数字");
      return;
    }

    KyUser kyUser = new KyUser();
    kyUser.setUsername(userName);
    kyUser.setPassword(password);
    kyUser.loginObservable(KyUser.class).subscribe(new Subscriber<KyUser>() {
      @Override public void onCompleted() {
        toastUtil.show("onCompleted");
      }

      @Override public void onError(Throwable throwable) {
        toastUtil.show(throwable.getMessage());
      }

      @Override public void onNext(KyUser kyUser) {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putString("objectId", kyUser.getObjectId());
        bundle.putString("username", kyUser.getUsername());
        intent.putExtra("bundle", bundle);
        setResult(KySet.USER_RESULT_LOG_IN, intent);
        finish();
      }
    });
  }

  @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    switch (resultCode) {
      case KySet.USER_RESULT_SIGN_UP:
        Bundle bundle = data.getBundleExtra("bundle");
        String objectId = bundle.getString("objectId");
        String username = bundle.getString("username");
        String password = bundle.getString("password");
        if (objectId != null && username != null && password != null) {
          userLogin(username, password);
        }
        break;
    }
  }
}

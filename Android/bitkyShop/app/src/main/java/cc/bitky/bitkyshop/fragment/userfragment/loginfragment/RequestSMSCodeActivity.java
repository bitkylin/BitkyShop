package cc.bitky.bitkyshop.fragment.userfragment.loginfragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import cc.bitky.bitkyshop.R;
import cc.bitky.bitkyshop.bean.cart.KyUser;
import cc.bitky.bitkyshop.utils.KyToolBar;
import cc.bitky.bitkyshop.utils.ToastUtil;
import cc.bitky.bitkyshop.utils.tools.KyBmobHelper;
import cc.bitky.bitkyshop.utils.tools.KySet;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.LogInListener;
import com.socks.library.KLog;

public class RequestSMSCodeActivity extends AppCompatActivity implements View.OnClickListener {

  private ToastUtil toastUtil;
  private EditText editTextSMSCode;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_request_smscode);
    toastUtil = new ToastUtil(this);
    KyToolBar kyToolBar = (KyToolBar) findViewById(R.id.requestSMSCodeActivity_toolbar);
    kyToolBar.setNavigationOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        finish();
      }
    });
    editTextSMSCode = (EditText) findViewById(R.id.requestSMSCodeActivity_textView_SMSCode);
    Button btnLoginOrSignUp = (Button) findViewById(R.id.requestSMSCodeActivity_btn_loginOrSignUp);
    btnLoginOrSignUp.setOnClickListener(this);
  }

  @Override public void onClick(View v) {
    switch (v.getId()) {
      case R.id.requestSMSCodeActivity_btn_loginOrSignUp:
        InputMethodManager imm =
            (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editTextSMSCode.getWindowToken(), 0);
        String SMSCode = editTextSMSCode.getText().toString().trim();
        if (SMSCode.length() == 0) {
          toastUtil.show("请输入手机验证码");
          return;
        }
        String phone = getIntent().getStringExtra("phone");
        if (phone == null) {
          toastUtil.show("未知错误");
          finish();
          return;
        }
        KLog.d("phone:" + phone + ",SMSCode:" + SMSCode);

        BmobUser.signOrLoginByMobilePhone(phone, SMSCode, new LogInListener<KyUser>() {

          @Override public void done(final KyUser kyUser, BmobException e) {
            if (kyUser == null) KLog.d("kyUser是null");
            KyUser kyCurrentUser = KyBmobHelper.getCurrentUser();
            if (kyCurrentUser == null) {
              KLog.d("kyCurrentUser是null");
            } else {
              Intent intent = new Intent();
              intent.putExtra("userObjectId", kyCurrentUser.getObjectId());
              intent.putExtra("username", kyCurrentUser.getUsername());
              intent.putExtra("phone", kyCurrentUser.getMobilePhoneNumber());
              if (kyCurrentUser.getHaveDetailInfo() != null && kyCurrentUser.getHaveDetailInfo()) {
                intent.putExtra("haveDetailInfo", true);
              } else {
                intent.putExtra("haveDetailInfo", false);
              }
              setResult(KySet.USER_RESULT_PHONE_SIGNUP_OR_LOGIN, intent);
              finish();
            }
          }
        });
    }
  }
}

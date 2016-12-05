package cc.bitky.bitkyshop.fragment.userfragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import cc.bitky.bitkyshop.R;
import cc.bitky.bitkyshop.bean.cart.KyUser;
import cc.bitky.bitkyshop.fragment.userfragment.util.KySet;
import cc.bitky.bitkyshop.utils.KyToolBar;
import cc.bitky.bitkyshop.utils.ToastUtil;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignupActivity extends AppCompatActivity implements View.OnClickListener {
  ToastUtil toastUtil;

  private EditText confirmPwd;
  private EditText inputPwd;
  private EditText pwdResumeAnswer;
  private EditText pwdResumeQuestion;
  private EditText userName;

  private String userNameStr;
  private String inputPwdStr;
  private String confirmPwdStr;
  private String pwdResumeQuestionStr;
  private String pwdResumeAnswerStr;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_signup);
    toastUtil = new ToastUtil(this);
    KyToolBar kyToolBar = (KyToolBar) findViewById(R.id.signupActivity_toolbar);
    kyToolBar.setNavigationOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        finish();
      }
    });
    userName = (EditText) findViewById(R.id.signupActivity_editText_userName);
    inputPwd = (EditText) findViewById(R.id.signupActivity_editText_inputPwd);
    confirmPwd = (EditText) findViewById(R.id.signupActivity_editText_confirmPwd);
    pwdResumeQuestion = (EditText) findViewById(R.id.signupActivity_editText_pwdResumeQuestion);
    pwdResumeAnswer = (EditText) findViewById(R.id.signupActivity_editText_pwdResumeAnswer);
    Button btnSignup = (Button) findViewById(R.id.signupActivity_btn_signup);
    btnSignup.setOnClickListener(this);
  }

  @Override public void onClick(View v) {
    switch (v.getId()) {
      case R.id.signupActivity_btn_signup:
        userNameStr = userName.getText().toString().trim();
        inputPwdStr = inputPwd.getText().toString().trim();
        confirmPwdStr = confirmPwd.getText().toString().trim();
        pwdResumeQuestionStr = pwdResumeQuestion.getText().toString().trim();
        pwdResumeAnswerStr = pwdResumeQuestion.getText().toString().trim();

        //验证是否填写完整
        if (userNameStr.length() == 0
            || inputPwdStr.length() == 0
            || confirmPwdStr.length() == 0
            || pwdResumeQuestionStr.length() == 0
            || pwdResumeAnswerStr.length() == 0) {
          toastUtil.show("请将上面的编辑框填写完整");
          break;
        }
        //验证密码是否一致
        if (!inputPwdStr.equals(confirmPwdStr)) {
          toastUtil.show("密码框中的密码不一致，请重新输入");
          inputPwd.setText("");
          confirmPwd.setText("");
          break;
        }
        //验证语法规则
        if (!checkUserName(userNameStr)) {
          toastUtil.show("用户名只能使用中文、英文和数字");
          break;
        }
        if (!checkNumStr(inputPwdStr)) {
          toastUtil.show("密码只能使用英文和数字");
          break;
        }
        if (!checkUserName(pwdResumeQuestionStr) || !checkUserName(pwdResumeAnswerStr)) {
          toastUtil.show("密码找回问题和答案只能使用中文、英文和数字");
          break;
        }

        KyUser kyUser = new KyUser();
        kyUser.setUsername(userNameStr);
        kyUser.setPassword(inputPwdStr);

        kyUser.signUp(new SaveListener<KyUser>() {
          @Override public void done(KyUser kyUser, BmobException e) {
            if (e == null) {
              signUpSuccessful(kyUser);
            } else {
              switch (e.getErrorCode()) {
                case 202:
                  toastUtil.show("用户名已存在");
                  break;
                case 203:
                  toastUtil.show("邮箱已存在");
                  break;
              }
            }
          }
        });
        break;
    }
  }

  /**
   * 注册成功调用
   */
  private void signUpSuccessful(KyUser kyUser) {
    if (inputPwdStr == null || inputPwdStr.length() == 0) {
      toastUtil.show("注册时遇到未知错误");
      return;
    }
    Intent intent = new Intent();
    Bundle bundle = new Bundle();
    bundle.putString("objectId", kyUser.getObjectId());
    bundle.putString("username", kyUser.getUsername());
    bundle.putString("password", inputPwdStr);
    intent.putExtra("bundle", bundle);
    setResult(KySet.USER_RESULT_SIGN_UP, intent);
    finish();
  }

  /**
   * 验证手机号码
   *
   * @param phoneNumber 手机号码
   * @return boolean
   */
  public static boolean checkPhoneNumber(String phoneNumber) {
    Pattern pattern = Pattern.compile("^1[0-9]{10}$");
    Matcher matcher = pattern.matcher(phoneNumber);
    return matcher.matches();
  }

  /**
   * 验证数字字母和中文字符
   *
   * @param phoneNumber 用户名
   * @return boolean
   */
  public static boolean checkUserName(String phoneNumber) {
    Pattern pattern = Pattern.compile("([a-zA-Z0-9\\u4e00-\\u9fa5]{2,24})");
    Matcher matcher = pattern.matcher(phoneNumber);
    return matcher.matches();
  }

  /**
   * 验证数字字母
   *
   * @param phoneNumber 密码
   * @return boolean
   */
  public static boolean checkNumStr(String phoneNumber) {
    Pattern pattern = Pattern.compile("([a-zA-Z0-9]{2,24})");
    Matcher matcher = pattern.matcher(phoneNumber);
    return matcher.matches();
  }
}

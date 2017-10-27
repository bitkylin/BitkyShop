package cc.bitky.bitkyshop.fragment.userfragment.loginfragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import cc.bitky.bitkyshop.R;
import cc.bitky.bitkyshop.bean.cart.KyUser;
import cc.bitky.bitkyshop.utils.KyToolBar;
import cc.bitky.bitkyshop.utils.ToastUtil;
import cc.bitky.bitkyshop.utils.tools.KyPattern;
import cc.bitky.bitkyshop.utils.tools.KySet;
import cn.bmob.v3.BmobSMS;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import com.socks.library.KLog;
import java.util.ArrayList;
import java.util.List;
import rx.Subscriber;

public class LoginActivity extends AppCompatActivity implements ILoginActivity {
  ToastUtil toastUtil;

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

    setTabLayout();
  }

  void setTabLayout() {
    TabLayout tabLayout = (TabLayout) findViewById(R.id.loginActivity_tabLayout);
    ViewPager viewPager = (ViewPager) findViewById(R.id.loginActivity_viewPager);
    //初始化各fragment
    LoginPhoneFragment loginPhoneFragment = new LoginPhoneFragment();
    //   LoginLegacyFragment loginLegacyFragment = new LoginLegacyFragment();
    //   loginLegacyFragment.setActivity(this);
    loginPhoneFragment.setActivity(this);
    //将fragment装进列表中
    List<Fragment> fragmentList = new ArrayList<>();
    fragmentList.add(loginPhoneFragment);
    //   fragmentList.add(loginLegacyFragment);
    //将名称加载tab名字列表，正常情况下，我们应该在values/arrays.xml中进行定义然后调用
    List<String> titleList = new ArrayList<>();
    titleList.add("手机号一键登录");
    //   titleList.add("传统登录");
    //设置TabLayout的模式
    tabLayout.setTabMode(TabLayout.MODE_FIXED);
    //为TabLayout添加tab名称
    tabLayout.addTab(tabLayout.newTab().setText(titleList.get(0)));
    //  tabLayout.addTab(tabLayout.newTab().setText(titleList.get(1)));
    FragmentManager fragmentManager = getSupportFragmentManager();
    FragmentPagerAdapter fAdapter = new FindTabAdapter(fragmentManager, fragmentList, titleList);
    //viewpager加载adapter
    viewPager.setAdapter(fAdapter);
    //TabLayout加载viewpager
    tabLayout.setupWithViewPager(viewPager);
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
          userLoginByLegacy(username, password);
        }
        break;
      case KySet.USER_RESULT_PHONE_SIGNUP_OR_LOGIN:
        String userObjectIdGet = data.getStringExtra("userObjectId");
        String usernameGet = data.getStringExtra("username");
        String phoneGet = data.getStringExtra("phone");
        Boolean haveDetailInfoGet = data.getBooleanExtra("haveDetailInfo", true);
        Intent intent = new Intent();
        setResult(KySet.USER_RESULT_LOG_IN, intent);
        finish();
        break;
    }
  }

  @Override public void userLoginByLegacy(String userName, String password) {
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

  @Override public void userLoginByPhone(final String phone) {
    if (!KyPattern.checkPhoneNumber(phone)) {
      toastUtil.show("请输入正确的手机号码");
      return;
    }

    final Intent intent = new Intent(this, RequestSMSCodeActivity.class);
    intent.putExtra("phone", phone);

    BmobSMS.requestSMSCode(phone, "生活服务超市模板", new QueryListener<Integer>() {

      @Override public void done(Integer smsId, BmobException ex) {
        if (ex != null) {
          toastUtil.show("出现未知错误，请重启该应用");
          KLog.d(ex.getErrorCode() + ":" + ex.getMessage());
          return;
        }
        startActivityForResult(intent, KySet.USER_REQUEST_PHONE_SIGNUP_OR_LOGIN);
      }
    });
  }

  @Override public void signUp() {
    Intent intent = new Intent(this, SignupActivity.class);
    startActivityForResult(intent, KySet.USER_REQUEST_SIGN_UP);
  }
}

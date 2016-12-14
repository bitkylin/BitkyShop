package cc.bitky.bitkyshop.fragment.userfragment.loginfragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;
import cc.bitky.bitkyshop.R;
import cc.bitky.bitkyshop.utils.tools.KySet;

public class LoginLegacyFragment extends Fragment implements View.OnClickListener {
  private ILoginActivity activity;
  Context context;
  private TextView textViewUserName;
  private TextView textViewPassword;

  public void setActivity(ILoginActivity activity) {
    this.activity = activity;
  }

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    context = getContext();
  }

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_login_legacy, container, false);
    TextView textViewSignup =
        (TextView) view.findViewById(R.id.loginLegacyFragment_TextView_signup);
    TextView textViewForgetPassword =
        (TextView) view.findViewById(R.id.loginLegacyFragment_TextView_forgetPassword);
    Button btnLogin = (Button) view.findViewById(R.id.loginLegacyFragment_btn_login);
    textViewSignup.setOnClickListener(this);
    textViewForgetPassword.setOnClickListener(this);
    btnLogin.setOnClickListener(this);
    textViewUserName = (TextView) view.findViewById(R.id.loginLegacyFragment_TextView_userName);
    textViewPassword = (TextView) view.findViewById(R.id.loginLegacyFragment_TextView_password);

    return view;
  }

  @Override public void onAttach(Context context) {
    super.onAttach(context);
  }

  @Override public void onDetach() {
    super.onDetach();
  }

  @Override public void onClick(View v) {
    switch (v.getId()) {
      case R.id.loginLegacyFragment_TextView_signup:
        activity.signUp();
        break;
      case R.id.loginLegacyFragment_TextView_forgetPassword:
        break;
      case R.id.loginLegacyFragment_btn_login:
        textViewUserName.clearFocus();
        textViewPassword.clearFocus();
        InputMethodManager imm =
            (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(textViewUserName.getWindowToken(), 0);
        String userName = textViewUserName.getText().toString().trim();
        String password = textViewPassword.getText().toString().trim();
        if (activity != null) activity.userLoginByLegacy(userName, password);
        break;
    }
  }
}

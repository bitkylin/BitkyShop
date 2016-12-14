package cc.bitky.bitkyshop.fragment.userfragment.loginfragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import cc.bitky.bitkyshop.R;

public class LoginPhoneFragment extends Fragment implements View.OnClickListener {
  private ILoginActivity activity;
  private EditText editTextPhone;
  private Context context;

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    context = getContext();
  }

  public void setActivity(ILoginActivity activity) {
    this.activity = activity;
  }

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_login_phone, container, false);
    Button btnLogin = (Button) view.findViewById(R.id.loginPhoneFragment_btn_login);
    editTextPhone = (EditText) view.findViewById(R.id.loginPhoneFragment_TextView_phone);
    btnLogin.setOnClickListener(this);
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
      case R.id.loginPhoneFragment_btn_login:
        InputMethodManager imm =
            (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editTextPhone.getWindowToken(), 0);
        String phone = editTextPhone.getText().toString().trim();
        activity.userLoginByPhone(phone);
        break;
    }
  }
}

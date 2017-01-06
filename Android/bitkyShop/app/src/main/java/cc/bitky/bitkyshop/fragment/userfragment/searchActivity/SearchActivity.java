package cc.bitky.bitkyshop.fragment.userfragment.searchActivity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import cc.bitky.bitkyshop.R;
import cc.bitky.bitkyshop.bean.Commodity;
import cc.bitky.bitkyshop.utils.KyToolBar;
import cc.bitky.bitkyshop.utils.ToastUtil;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import com.socks.library.KLog;
import java.util.List;

public class SearchActivity extends AppCompatActivity implements View.OnClickListener {

  private Context context;
  private EditText searchEdittext;
  private ToastUtil toastUtil;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_search);
    context = this;
    toastUtil = new ToastUtil(this);
    KyToolBar kyToolBar = (KyToolBar) findViewById(R.id.searchActivity_kyToolbar);
    searchEdittext = (EditText) findViewById(R.id.kytoolbar_editText);
    kyToolBar.setNavigationOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        finish();
      }
    });
    kyToolBar.setRightButtonOnClickListener(this);
    kyToolBar.setOnSearchListener(new KyToolBar.OnSearchListener() {
      @Override public void searchListener(String msg) {
        searchCommodity();
      }
    });
  }

  @Override public void onClick(View v) {
    switch (v.getId()) {
      case R.id.kytoolbar_rightButton:
        searchCommodity();
        break;
    }
  }

  private void searchCommodity() {
    ((InputMethodManager) context.getSystemService(
        Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(searchEdittext.getWindowToken(),
        InputMethodManager.HIDE_NOT_ALWAYS);
    String msg = searchEdittext.getText().toString().trim();
    if (msg.equals("")) {
      toastUtil.show("请输入要搜索的商品");
      return;
    }
    BmobQuery<Commodity> bmobQuery = new BmobQuery<>();
    bmobQuery.addWhereEqualTo("categorySub", msg);
    KLog.d(msg);
    bmobQuery.findObjects(new FindListener<Commodity>() {
      @Override public void done(List<Commodity> list, BmobException e) {
        if (e != null) {
          KLog.d(e.getMessage());
        }
        if (list != null) {
          KLog.d(list.size());
        }
      }
    });
  }
}

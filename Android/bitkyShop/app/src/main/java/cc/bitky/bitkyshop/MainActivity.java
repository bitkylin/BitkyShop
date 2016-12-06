package cc.bitky.bitkyshop;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentTabHost;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;
import cc.bitky.bitkyshop.bean.KyTab;
import cc.bitky.bitkyshop.fragment.cartfragment.CartFragment;
import cc.bitky.bitkyshop.fragment.categrayfragment.CategrayFragment;
import cc.bitky.bitkyshop.fragment.homefragment.HomeFragment;
import cc.bitky.bitkyshop.fragment.hotfragment.HotFragment;
import cc.bitky.bitkyshop.fragment.userfragment.UserFragment;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

  Context mContext;
  List<KyTab> kyTabs = new ArrayList<>(3);
  FragmentTabHost fragmentTabHost;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    mContext = this;
    initTab();
  }

  private void initTab() {
    KyTab tab_home = new KyTab(HomeFragment.class, "主页", R.drawable.navigationbar_selector_home);
    KyTab tab_hot = new KyTab(HotFragment.class, "便民", R.drawable.navigationbar_selector_hot);
    KyTab tab_category =
        new KyTab(CategrayFragment.class, "分类", R.drawable.navigationbar_selector_category);
    KyTab tab_cart = new KyTab(CartFragment.class, "购物车", R.drawable.navigationbar_selector_cart);
    KyTab tab_user = new KyTab(UserFragment.class, "个人", R.drawable.navigationbar_selector_user);
    kyTabs.add(tab_home);
    kyTabs.add(tab_hot);
    kyTabs.add(tab_category);
    kyTabs.add(tab_cart);
    kyTabs.add(tab_user);

    fragmentTabHost = (FragmentTabHost) findViewById(R.id.fragmentTabHost);
    fragmentTabHost.setup(mContext, getSupportFragmentManager(), R.id.fragment);

    for (KyTab tab : kyTabs) {
      TabHost.TabSpec tabSpec =
          fragmentTabHost.newTabSpec(tab.getTitle()).setIndicator(buildIndicator(tab));
      fragmentTabHost.addTab(tabSpec, tab.getFragment(), null);
    }

    fragmentTabHost.getTabWidget().setShowDividers(LinearLayout.SHOW_DIVIDER_NONE);
    fragmentTabHost.setCurrentTab(0);
  }

  private View buildIndicator(KyTab tab) {
    View view = LayoutInflater.from(mContext).inflate(R.layout.tab_indicator, null);
    ImageView img = (ImageView) view.findViewById(R.id.icon_tab);
    TextView text = (TextView) view.findViewById(R.id.txt_indicator);

    img.setBackgroundResource(tab.getIcon());
    text.setText(tab.getTitle());

    return view;
  }
}

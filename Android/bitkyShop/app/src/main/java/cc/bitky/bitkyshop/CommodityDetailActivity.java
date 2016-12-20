package cc.bitky.bitkyshop;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import cc.bitky.bitkyshop.bean.Commodity;
import cc.bitky.bitkyshop.globalDeploy.GreenDaoKyHelper;
import cc.bitky.bitkyshop.utils.KyToolBar;
import cc.bitky.bitkyshop.utils.ToastUtil;
import com.facebook.drawee.view.SimpleDraweeView;

public class CommodityDetailActivity extends AppCompatActivity {

  private TextView textTitle;
  private TextView textPrice;
  private TextView textDetail;
  private SimpleDraweeView draweeView;
  private TextView textFirstCategory;
  private TextView textSecondCategory;
  private Commodity commodity;
  private ToastUtil toastUtil;
  private TextView textSecondCategoryTip;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_commodity_detail);
    toastUtil = new ToastUtil(this);

    KyToolBar kyToolBar = (KyToolBar) findViewById(R.id.commodityDetailActivity_toolbar);
    kyToolBar.setNavigationOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        finish();
      }
    });
    Button btnAddCart = (Button) findViewById(R.id.commodityDetailActivity_btnAddCart);
    btnAddCart.setEnabled(false);
    textFirstCategory = (TextView) findViewById(R.id.commodityDetailActivity_firstCategory);
    textSecondCategory = (TextView) findViewById(R.id.commodityDetailActivity_secondCategory);
    textSecondCategoryTip = (TextView) findViewById(R.id.commodityDetailActivity_secondCategoryTip);
    textTitle = (TextView) findViewById(R.id.commodityDetailActivity_textTitle);
    textPrice = (TextView) findViewById(R.id.commodityDetailActivity_textPrice);
    textDetail = (TextView) findViewById(R.id.commodityDetailActivity_textDetail);
    draweeView = (SimpleDraweeView) findViewById(R.id.commodityDetailActivity_draweeview);
    commodity = getCommodity();
    if (commodity == null) {
      finish();
    } else {
      btnAddCart.setEnabled(true);
      initDetailShow(commodity);
    }
    btnAddCart.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        GreenDaoKyHelper.insertOrIncrease(commodity);
        toastUtil.show("已添加到购物车");
      }
    });
  }

  private Commodity getCommodity() {
    Intent intent = getIntent();
    if (intent == null) {
      return null;
    }
    Bundle bundle = intent.getBundleExtra("bundle");
    if (bundle == null) {
      return null;
    }
    Commodity commodity = (Commodity) bundle.getSerializable("commodity");
    if (commodity == null) {
      return null;
    }
    return commodity;
  }

  private void initDetailShow(Commodity commodity) {
    String name = commodity.getName();
    double price = commodity.getPrice();
    String firstCategory = commodity.getCategory();
    String secondCategory = commodity.getCategorySub();
    String details = commodity.getDetails();
    String coverPhotoUrl = commodity.getCoverPhotoUrl();

    textTitle.setText(name);
    textPrice.setText(price + " 元");
    if (firstCategory == null || firstCategory.equals("")) firstCategory = "未设置";
    if (secondCategory == null || secondCategory.equals("")) {
      secondCategory = "未设置";
      textSecondCategoryTip.setVisibility(View.INVISIBLE);
      textSecondCategory.setVisibility(View.INVISIBLE);
    }
    if (details == null || details.equals("")) details = "无详细介绍";

    textFirstCategory.setText(firstCategory);
    textSecondCategory.setText(secondCategory);
    textDetail.setText(details);
    if (coverPhotoUrl == null || coverPhotoUrl.equals("")) {
      draweeView.setImageURI(
          Uri.parse("http://ohm7wg7jb.bkt.clouddn.com/specific/cannotfindphoto.png"));
    } else {
      draweeView.setImageURI(Uri.parse(coverPhotoUrl));
    }
  }
}

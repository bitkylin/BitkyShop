package cc.bitky.bitkyshop.fragment.categrayfragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import cc.bitky.bitkyshop.CommodityDetailActivity;
import cc.bitky.bitkyshop.R;
import cc.bitky.bitkyshop.bean.Commodity;
import cc.bitky.bitkyshop.fragment.categrayfragment.SubCategrayActivityPresenter.RefreshType;
import cc.bitky.bitkyshop.globalDeploy.GreenDaoKyHelper;
import cc.bitky.bitkyshop.utils.KyToolBar;
import cc.bitky.bitkyshop.utils.ToastUtil;
import cc.bitky.bitkyshop.utils.recyclerview.KyBaseRecyclerAdapter;
import cc.bitky.bitkyshop.utils.recyclerview.KyBaseViewHolder;
import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import java.util.ArrayList;
import java.util.List;

public class SubCategoryActivity extends AppCompatActivity {
  Context mContext;
  private ToastUtil toastUtil;
  private SubCategrayActivityPresenter presenter;
  private MaterialRefreshLayout swipeRefreshLayout;
  private KyBaseRecyclerAdapter recyclerAdapter;
  private String subCategoryStr;
  private RecyclerView recyclerView;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_sub_category);
    mContext = this;
    toastUtil = new ToastUtil(mContext);
    presenter = new SubCategrayActivityPresenter(this, null);

    String subCategoryStr = getSubcategoryStr();
    if (subCategoryStr == null) {
      finish();
      return;
    } else {
      this.subCategoryStr = subCategoryStr;
    }

    KyToolBar kyToolBar = (KyToolBar) findViewById(R.id.subCategoryActivity_kyToolbar);
    kyToolBar.setNavigationOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        finish();
      }
    });
    kyToolBar.setTitle("当前类别: "+subCategoryStr);
    initSwipeRefreshLayout();
    initRecyclerView(new ArrayList<Commodity>());
  }

  @Nullable private String getSubcategoryStr() {
    Intent intent = getIntent();
    if (intent == null) {
      return null;
    }
    String subCategoryStr = intent.getStringExtra("subCategory");
    if (subCategoryStr == null) {
      return null;
    }
    return subCategoryStr;
  }

  private void initSwipeRefreshLayout() {
    swipeRefreshLayout =
        (MaterialRefreshLayout) findViewById(R.id.subCategoryActivity_swiperefreshlayout);
    swipeRefreshLayout.setMaterialRefreshListener(new MaterialRefreshListener() {
      @Override public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
        presenter.refreshRecyclerAdapterData(null, RefreshType.Refresh);
      }

      @Override public void onRefreshLoadMore(MaterialRefreshLayout materialRefreshLayout) {
        super.onRefreshLoadMore(materialRefreshLayout);
        presenter.refreshRecyclerAdapterData(null, RefreshType.LoadMore);
      }
    });
  }

  /**
   * 初始化RecyclerView
   */
  private void initRecyclerView(List<Commodity> list) {
    //商品RecyclerView显示
    recyclerView = (RecyclerView) findViewById(R.id.subCategoryActivity_recyclerView);
    if (recyclerAdapter == null) initRecyclerCommodityAdapter(new ArrayList<Commodity>());
    recyclerView.setAdapter(recyclerAdapter);
    recyclerView.setLayoutManager(new GridLayoutManager(mContext, 2));
    recyclerView.setItemAnimator(new DefaultItemAnimator());
    if (recyclerAdapter.getItemCount() == 0) {
      presenter.refreshRecyclerAdapterData(subCategoryStr, RefreshType.Refresh);
    }
  }

  public void initRecyclerCommodityAdapter(List<Commodity> list) {
    recyclerAdapter =
        new KyBaseRecyclerAdapter<Commodity>(list, R.layout.recycler_homefragment_show) {
          @Override
          public void setDataToViewHolder(final Commodity dataItem, KyBaseViewHolder holder) {
            holder.getSimpleDraweeView(R.id.recycler_homeshow_draweeview)
                .setImageURI(Uri.parse(dataItem.getCoverPhotoUrl()));
            holder.getTextView(R.id.recycler_homeshow_text_title).setText(dataItem.getName());
            holder.getTextView(R.id.recycler_homeshow_text_price)
                .setText(dataItem.getPrice().toString() + " 元");
            holder.getButton(R.id.recycler_homeshow_btn_addCart)
                .setOnClickListener(new View.OnClickListener() {
                  @Override public void onClick(View v) {
                    GreenDaoKyHelper.insertOrIncrease(dataItem);
                    toastUtil.show("已添加到购物车");
                  }
                });
          }
        };
    recyclerAdapter.setOnClickListener(
        new KyBaseRecyclerAdapter.KyRecyclerViewItemOnClickListener<Commodity>() {
          @Override public void Onclick(View v, int adapterPosition, Commodity data) {
            Bundle bundle = new Bundle();
            bundle.putSerializable("commodity", data);
            Intent intent = new Intent(mContext, CommodityDetailActivity.class);
            intent.putExtra("bundle", bundle);
            startActivity(intent);
          }
        });
  }
 public void refreshRecyclerViewData(List<Commodity> list, RefreshType type) {
    switch (type) {
      case Refresh:
        swipeRefreshLayout.finishRefresh();
        if (recyclerAdapter == null) {
          initRecyclerCommodityAdapter(list);
        } else {
          recyclerAdapter.reloadData(list);
        }
        recyclerView.scrollToPosition(0);
        break;

      case LoadMore:
        swipeRefreshLayout.finishRefreshLoadMore();
        if (recyclerAdapter != null) recyclerAdapter.loadMoreData(list);
        break;
    }
  }

   public void CanNotRefreshData(RefreshType type) {
    toastUtil.show("没有更多的商品了！");
    swipeRefreshLayout.finishRefreshLoadMore();
  }
}

package cc.bitky.bitkyshop.fragment.categrayfragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import cc.bitky.bitkyshop.R;
import cc.bitky.bitkyshop.bean.Commodity;
import cc.bitky.bitkyshop.fragment.categrayfragment.CategrayFragmentPresenter.RefreshType;
import cc.bitky.bitkyshop.globalDeploy.GreenDaoKyHelper;
import cc.bitky.bitkyshop.utils.ToastUtil;
import cc.bitky.bitkyshop.utils.recyclerview.KyBaseRecyclerAdapter;
import cc.bitky.bitkyshop.utils.recyclerview.KyBaseViewHolder;
import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.socks.library.KLog;
import java.util.ArrayList;
import java.util.List;

public class CategrayFragment extends Fragment implements ICategrayFragment {
  Context mContext;
  CategrayFragmentPresenter presenter;
  private KyBaseRecyclerAdapter recyclerAdapterCategoryStr;
  private KyBaseRecyclerAdapter recyclerAdapterCommodity;
  private RecyclerView recyclerViewCommodity;
  private MaterialRefreshLayout swipeRefreshLayout;
  private View view;
  ToastUtil toastUtil;

  @Override public void onAttach(Context context) {
    super.onAttach(context);
    mContext = context;
    toastUtil = new ToastUtil(context);
    presenter = new CategrayFragmentPresenter(this);
  }

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    super.onCreateView(inflater, container, savedInstanceState);
    view = inflater.inflate(R.layout.fragment_categray, container, false);
    initRecyclerView();
    initSwipeRefreshLayout();
    return view;
  }

  private void initSwipeRefreshLayout() {
    swipeRefreshLayout =
        (MaterialRefreshLayout) view.findViewById(R.id.categrayfragment_refreshlayout);
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
  private void initRecyclerView() {
    //分类RecyclerView显示
    RecyclerView recyclerViewCategory =
        (RecyclerView) view.findViewById(R.id.categrayfragment_recyclerview_category);
    recyclerViewCategory.setAdapter(getRecyclerAdapterCategoryStr());
    recyclerViewCategory.setLayoutManager(new LinearLayoutManager(mContext));
    recyclerViewCategory.setItemAnimator(new DefaultItemAnimator());

    //商品RecyclerView显示
    recyclerViewCommodity =
        (RecyclerView) view.findViewById(R.id.categrayfragment_recyclerview_commodity);
    if (recyclerAdapterCommodity == null) initRecyclerCommodityAdapter(new ArrayList<Commodity>());
    recyclerViewCommodity.setAdapter(recyclerAdapterCommodity);
    recyclerViewCommodity.setLayoutManager(new GridLayoutManager(mContext, 2));
    recyclerViewCommodity.setItemAnimator(new DefaultItemAnimator());
    if (recyclerAdapterCommodity.getItemCount() == 0) {
      presenter.refreshRecyclerAdapterData(null, RefreshType.Refresh);
    }
  }

  private KyBaseRecyclerAdapter getRecyclerAdapterCategoryStr() {
    if (recyclerAdapterCategoryStr == null) {
      List<String> categrayNames = presenter.getCategrayNames();
      recyclerAdapterCategoryStr = new KyBaseRecyclerAdapter<String>(categrayNames,
          R.layout.recycler_categryfragment_single_text) {
        @Override public void setDataToViewHolder(String dataItem, KyBaseViewHolder holder) {
          holder.getTextView(R.id.recycler_categry_single_textView).setText(dataItem);
        }
      };
      recyclerAdapterCategoryStr.setOnClickListener(
          new KyBaseRecyclerAdapter.KyRecyclerViewItemOnClickListener<String>() {
            @Override public void Onclick(View v, int adapterPosition, String data) {
              presenter.refreshRecyclerAdapterData(data, RefreshType.Refresh);
            }
          });
    }
    return recyclerAdapterCategoryStr;
  }

public void initRecyclerCommodityAdapter(List<Commodity> list) {
    recyclerAdapterCommodity =
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
    recyclerAdapterCommodity.setOnClickListener(
        new KyBaseRecyclerAdapter.KyRecyclerViewItemOnClickListener<Commodity>() {
          @Override public void Onclick(View v, int adapterPosition, Commodity data) {
            KLog.d("位置:" + adapterPosition + ",data:" + data.getName());
          }
        });
  }

  @Override public void refreshRecyclerViewData(List<Commodity> list, RefreshType type) {
    switch (type) {
      case Refresh:
        swipeRefreshLayout.finishRefresh();
        if (recyclerAdapterCommodity == null) {
          initRecyclerCommodityAdapter(list);
        } else {
          recyclerAdapterCommodity.reloadData(list);
        }
        recyclerViewCommodity.scrollToPosition(0);
        break;

      case LoadMore:
        swipeRefreshLayout.finishRefreshLoadMore();
        if (recyclerAdapterCommodity != null) recyclerAdapterCommodity.loadMoreData(list);
        break;
    }
  }

  @Override public void CanNotRefreshData(RefreshType type) {
    toastUtil.show("没有更多的商品了！");
    swipeRefreshLayout.finishRefreshLoadMore();
  }
}

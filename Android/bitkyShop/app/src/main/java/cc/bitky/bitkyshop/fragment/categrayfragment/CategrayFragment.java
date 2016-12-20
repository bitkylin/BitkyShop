package cc.bitky.bitkyshop.fragment.categrayfragment;

import android.content.Context;
import android.content.Intent;
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
import cc.bitky.bitkyshop.bean.SubCategory;
import cc.bitky.bitkyshop.fragment.categrayfragment.CategrayFragmentPresenter.RefreshType;
import cc.bitky.bitkyshop.utils.ToastUtil;
import cc.bitky.bitkyshop.utils.recyclerview.KyBaseRecyclerAdapter;
import cc.bitky.bitkyshop.utils.recyclerview.KyBaseViewHolder;
import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import java.util.ArrayList;
import java.util.List;

public class CategrayFragment extends Fragment implements ICategrayFragment {
  Context mContext;
  CategrayFragmentPresenter presenter;
  private KyBaseRecyclerAdapter<String> recyclerAdapterCategoryStr;
  private KyBaseRecyclerAdapter recyclerAdapterSubCategory;
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
    initSwipeRefreshLayout();
    initRecyclerView();
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
    if (recyclerAdapterSubCategory == null) {
      initRecyclerSubCategoryAdapter(new ArrayList<SubCategory>());
    }
    recyclerViewCommodity.setAdapter(recyclerAdapterSubCategory);
    recyclerViewCommodity.setLayoutManager(new GridLayoutManager(mContext, 2));
    recyclerViewCommodity.setItemAnimator(new DefaultItemAnimator());
    if (recyclerAdapterSubCategory.getItemCount() == 0) {
      presenter.refreshRecyclerAdapterData(recyclerAdapterCategoryStr.getDataItems().get(0),
          RefreshType.Refresh);
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

  public void initRecyclerSubCategoryAdapter(List<SubCategory> list) {
    recyclerAdapterSubCategory =
        new KyBaseRecyclerAdapter<SubCategory>(list, R.layout.recycler_subcategory_show) {
          @Override
          public void setDataToViewHolder(final SubCategory dataItem, KyBaseViewHolder holder) {
            holder.getSimpleDraweeView(R.id.recycler_subcategory_draweeview)
                .setImageURI(Uri.parse(dataItem.getPhotoUrl()));
            holder.getTextView(R.id.recycler_subcategory_textName).setText(dataItem.getName());
          }
        };
    recyclerAdapterSubCategory.setOnClickListener(
        new KyBaseRecyclerAdapter.KyRecyclerViewItemOnClickListener<SubCategory>() {
          @Override public void Onclick(View v, int adapterPosition, SubCategory data) {
            Intent intent = new Intent(mContext, SubCategoryActivity.class);

            intent.putExtra("subCategory", data.getName());
            startActivity(intent);
          }
        });
  }

  @Override public void refreshRecyclerViewData(List<SubCategory> list, RefreshType type) {
    switch (type) {
      case Refresh:
        swipeRefreshLayout.finishRefresh();
        if (recyclerAdapterSubCategory == null) {
          initRecyclerSubCategoryAdapter(list);
        } else {
          recyclerAdapterSubCategory.reloadData(list);
        }
        recyclerViewCommodity.scrollToPosition(0);
        break;

      case LoadMore:
        swipeRefreshLayout.finishRefreshLoadMore();
        if (recyclerAdapterSubCategory != null) recyclerAdapterSubCategory.loadMoreData(list);
        break;
    }
  }

  @Override public void CanNotRefreshData(RefreshType type) {
    toastUtil.show("没有更多的商品了！");
    swipeRefreshLayout.finishRefreshLoadMore();
  }
}

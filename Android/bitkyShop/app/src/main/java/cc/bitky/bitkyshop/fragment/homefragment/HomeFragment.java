package cc.bitky.bitkyshop.fragment.homefragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import cc.bitky.bitkyshop.R;
import cc.bitky.bitkyshop.bean.Commodity;
import cc.bitky.bitkyshop.fragment.homefragment.HomeFragmentPresenter.RefreshType;
import cc.bitky.bitkyshop.globalDeploy.GreenDaoKyHelper;
import cc.bitky.bitkyshop.utils.KyLog;
import cc.bitky.bitkyshop.utils.ToastUtil;
import cc.bitky.bitkyshop.utils.recyclerview.KyBaseRecyclerAdapter;
import cc.bitky.bitkyshop.utils.recyclerview.KyBaseViewHolder;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.Indicators.PagerIndicator;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.google.gson.Gson;
import com.socks.library.KLog;
import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment
    implements IHomeFragment, BaseSliderView.OnSliderClickListener {
  Context mContext;
  RecyclerView recyclerView;
  private MaterialRefreshLayout swipeRefreshLayout;
  private KyBaseRecyclerAdapter recyclerAdapter;
  private SliderLayout mSliderLayout;
  private List<TextSliderView> mTextSliderViews;
  final String TAG = KyLog.getTAG("HomeFragment");
  private HomeFragmentPresenter presenter;
  private View view;
  private ToastUtil toastUtil;

  @Override public void onAttach(Context context) {
    super.onAttach(context);
    mContext = context;
    toastUtil = new ToastUtil(mContext);
    this.presenter = new HomeFragmentPresenter(mContext, this);
  }

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    super.onCreateView(inflater, container, savedInstanceState);
    view = inflater.inflate(R.layout.fragment_home, container, false);
    initSliderLayout();
    initSlider();
    initSwipeRefreshLayout();
    initRecyclerView();
    return view;
  }

  private void initRecyclerView() {
    recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView_home_fragment);
    if (recyclerAdapter == null) {
      initRecyclerViewData(new ArrayList<Commodity>());
    }
    recyclerView.setAdapter(recyclerAdapter);
    recyclerView.setLayoutManager(
        new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
    recyclerView.setItemAnimator(new DefaultItemAnimator());
    presenter.refreshRecyclerAdapterData(RefreshType.Refresh);
  }

  /**
   * 初始化SliderLayout
   */
  private void initSliderLayout() {
    mSliderLayout = (SliderLayout) view.findViewById(R.id.slider);
    PagerIndicator indicator = (PagerIndicator) view.findViewById(R.id.custom_indicator);
    mSliderLayout.setCustomIndicator(indicator);
    mSliderLayout.setCustomAnimation(new DescriptionAnimation());
    mSliderLayout.setPresetTransformer(SliderLayout.Transformer.RotateUp);
    mSliderLayout.setDuration(3000);
  }

  /**
   * 初始化slider中的内容
   */
  private void initSlider() {

    if (mTextSliderViews == null) {
      mSliderLayout.addSlider(buildTextSlider(null, "欢迎使用", 0));
      getBmobSliderData();
    } else {
      mSliderLayout.removeAllSliders();
      for (TextSliderView textSliderView : mTextSliderViews) {
        mSliderLayout.addSlider(textSliderView);
      }
    }
  }

  private void initSwipeRefreshLayout() {
    swipeRefreshLayout =
        (MaterialRefreshLayout) view.findViewById(R.id.swiperefreshlayout_home_fragment);
    swipeRefreshLayout.setMaterialRefreshListener(new MaterialRefreshListener() {
      @Override public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
        presenter.refreshRecyclerAdapterData(RefreshType.Refresh);
      }

      @Override public void onRefreshLoadMore(MaterialRefreshLayout materialRefreshLayout) {
        super.onRefreshLoadMore(materialRefreshLayout);
        presenter.refreshRecyclerAdapterData(RefreshType.LoadMore);
      }
    });
  }

  /**
   * 广告栏Slider数据初始化
   */
  private void getBmobSliderData() {
    BmobQuery<Commodity> bmobQuery = new BmobQuery<>();
    bmobQuery.addWhereEqualTo("BitkyId", "default");
    bmobQuery.setLimit(3);
    bmobQuery.findObjects(new FindListener<Commodity>() {
      @Override public void done(List<Commodity> list, BmobException e) {
        if (e != null) {
          KLog.d(TAG, "异常内容：" + e.getMessage());
        } else if (list.size() > 0) {
          KLog.d(TAG, "list.size()：" + list.size());
          mTextSliderViews = new ArrayList<>();
          for (Commodity commodity : list) {
            mTextSliderViews.add(
                buildTextSlider(commodity.getCoverPhotoUrl(), commodity.getName(), 0));
          }
          KLog.d(TAG, "mTextSliderViews.size()：" + mTextSliderViews.size());

          if (mSliderLayout != null) initSlider();
        }
      }
    });
  }

  private TextSliderView buildTextSlider(String url, String description, int key) {
    TextSliderView textSliderView = new TextSliderView(mContext);
    if (url == null) {
      textSliderView.image(R.mipmap.sliderlayout_default);
    } else {
      textSliderView.image(url);
    }
    textSliderView.description(description);
    Bundle bundle = new Bundle();
    bundle.putInt("msg", key);
    textSliderView.bundle(bundle);
    textSliderView.setOnSliderClickListener(this);
    return textSliderView;
  }

  @Override public void onSliderClick(BaseSliderView slider) {
    switch (slider.getBundle().getInt("msg", -1)) {
      case 0:
        KLog.d(TAG, "case 0");
        break;
      case 1:
        KLog.d(TAG, "case 1");
        break;
      case 2:
        KLog.d(TAG, "case 2");
        break;
    }
  }

  @Override public void initRecyclerViewData(List<Commodity> list) {
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
                    KLog.json(new Gson().toJson(GreenDaoKyHelper.queryAll()));
                  }
                });
          }
        };
    recyclerAdapter.setOnClickListener(
        new KyBaseRecyclerAdapter.KyRecyclerViewItemOnClickListener<Commodity>() {
          @Override public void Onclick(View v, int adapterPosition, Commodity data) {
            KLog.d("点击:位置:" + adapterPosition + "; Name:" + data.getName());
          }
        });
    //
    //if (recyclerView != null) {
    //  recyclerView.setAdapter(recyclerAdapter);
    //  recyclerView.setLayoutManager(
    //      new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
    //  recyclerView.setItemAnimator(new DefaultItemAnimator());
    //}
  }

  @Override public void refleshRecyclerViewData(List<Commodity> list, RefreshType type) {
    switch (type) {
      case Refresh:
        swipeRefreshLayout.finishRefresh();
        if (recyclerAdapter != null) recyclerAdapter.reloadData(list);
        recyclerView.scrollToPosition(0);
        break;

      case LoadMore:
        swipeRefreshLayout.finishRefreshLoadMore();
        if (recyclerAdapter != null) recyclerAdapter.loadMoreData(list);
        break;
    }
  }

  @Override public void CanNotRefreshData(RefreshType type) {
    toastUtil.show("没有更多的商品了！");
    swipeRefreshLayout.finishRefreshLoadMore();
  }
}

package cc.bitky.bitkyshop.fragment.hotfragment;

import static cc.bitky.bitkyshop.fragment.hotfragment.HotFragmentPresenter.RefreshType;

import java.util.List;

import cc.bitky.bitkyshop.bean.Commodity;

public interface IHotFragment {

  void refreshRecyclerViewData(List<Commodity> list, RefreshType type);

  void CanNotRefreshData(RefreshType type);

}

package cc.bitky.bitkyshop.fragment.hotfragment;

import cc.bitky.bitkyshop.bean.Commodity;
import java.util.List;

import static cc.bitky.bitkyshop.fragment.hotfragment.HotFragmentPresenter.RefreshType;



public interface IHotFragment {

  void refreshRecyclerViewData(List<Commodity> list, RefreshType type);

  void CanNotRefreshData(RefreshType type);

}

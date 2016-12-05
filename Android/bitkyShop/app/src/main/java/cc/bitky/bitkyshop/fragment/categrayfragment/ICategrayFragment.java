package cc.bitky.bitkyshop.fragment.categrayfragment;

import cc.bitky.bitkyshop.bean.Commodity;
import cc.bitky.bitkyshop.fragment.categrayfragment.CategrayFragmentPresenter.RefreshType;
import java.util.List;

public interface ICategrayFragment {

  void refreshRecyclerViewData(List<Commodity> list, RefreshType loadMore);

  void CanNotRefreshData(RefreshType loadMore);
}

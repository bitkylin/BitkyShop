package cc.bitky.bitkyshop.fragment.categrayfragment;

import cc.bitky.bitkyshop.bean.SubCategory;
import cc.bitky.bitkyshop.fragment.categrayfragment.CategrayFragmentPresenter.RefreshType;
import java.util.List;

public interface ICategrayFragment {

  void refreshRecyclerViewData(List<SubCategory> list, RefreshType loadMore);

  void CanNotRefreshData(RefreshType loadMore);
}

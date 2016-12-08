package cc.bitky.bitkyshop.fragment.homefragment;

import cc.bitky.bitkyshop.bean.Commodity;
import java.util.List;

/**
 * Created by bitky on 2016/11/18.
 */

public interface IHomeFragment {

  /**
   * 初始化获取数据
   *
   * @param list 获取的数据
   */
  void initRecyclerViewData(List<Commodity> list);

  void refleshRecyclerViewData(List<Commodity> list, HomeFragmentPresenter.RefreshType type);

  void CanNotRefreshData(HomeFragmentPresenter.RefreshType type);
}

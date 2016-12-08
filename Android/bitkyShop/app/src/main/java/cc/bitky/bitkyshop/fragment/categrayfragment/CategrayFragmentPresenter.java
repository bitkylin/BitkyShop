package cc.bitky.bitkyshop.fragment.categrayfragment;

import cc.bitky.bitkyshop.bean.Commodity;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import com.socks.library.KLog;
import java.util.ArrayList;
import java.util.List;

public class CategrayFragmentPresenter {
  ICategrayFragment mView;
  private List<String> categrayNames;
  private int currentPosition = 0;
  private int countLimit = 10;
  private String currentCategoryStr = "洗浴用品";

  public CategrayFragmentPresenter(ICategrayFragment view) {
    mView = view;
  }

  void refreshRecyclerAdapterData(String category, RefreshType type) {
    switch (type) {
      case Refresh:
        if (category != null) {
          currentCategoryStr = category;
        }
        new Thread(new Runnable() {
          @Override public void run() {
            currentPosition = 0;
            BmobQuery<Commodity> bmobQuery = new BmobQuery<>();
            bmobQuery.addWhereEqualTo("Category", currentCategoryStr)
                .setLimit(countLimit)
                .setSkip(currentPosition)
                .order("createdAt");
            bmobQuery.findObjects(new FindListener<Commodity>() {
              @Override public void done(List<Commodity> list, BmobException e) {
                if (e != null) {
                  KLog.d("异常内容：" + e.getMessage());
                } else if (list.size() >= 0) {
                  KLog.d("list.size()=" + list.size());
                  mView.refreshRecyclerViewData(list, RefreshType.Refresh);
                }
                if (list.size() == 0) {
                  mView.CanNotRefreshData(RefreshType.Refresh);
                }
              }
            });
          }
        }).start();

        break;
      case LoadMore:
        new Thread(new Runnable() {
          @Override public void run() {
            currentPosition = currentPosition + 10;
            BmobQuery<Commodity> bmobQuery = new BmobQuery<>();
            bmobQuery.addWhereEqualTo("Category", currentCategoryStr)
                .setLimit(countLimit)
                .setSkip(currentPosition)
                .order("createdAt");
            bmobQuery.findObjects(new FindListener<Commodity>() {
              @Override public void done(List<Commodity> list, BmobException e) {
                if (e != null) {
                  KLog.d("异常内容：" + e.getMessage());
                } else if (list.size() > 0) {
                  mView.refreshRecyclerViewData(list, RefreshType.LoadMore);
                } else if (list.size() == 0) {
                  mView.CanNotRefreshData(RefreshType.LoadMore);
                }
              }
            });
          }
        }).start();
        break;
    }
  }

  public List<String> getCategrayNames() {
    if (categrayNames == null || categrayNames.size() > 0) {
      List<String> strings = new ArrayList<>();
      strings.add("洗浴用品");
      strings.add("书籍");
      strings.add("零食速食");
      strings.add("饮品酒水");
      strings.add("生活用品");
      strings.add("学习用品");
      return strings;
    }
    return categrayNames;
  }

  enum RefreshType {
    Refresh,
    LoadMore
  }
}

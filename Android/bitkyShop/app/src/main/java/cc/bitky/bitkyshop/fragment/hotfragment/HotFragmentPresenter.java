package cc.bitky.bitkyshop.fragment.hotfragment;

import android.content.Context;
import cc.bitky.bitkyshop.bean.Commodity;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import com.socks.library.KLog;
import java.util.ArrayList;
import java.util.List;

class HotFragmentPresenter {
  private IHotFragment mView;
  private int currentPosition = 0;
  private int countLimit = 10;
  private Context mContext;
  private List<String> categrayNames;
  private String currentCategoryStr="水果";

  HotFragmentPresenter(Context context, IHotFragment view) {
    mContext = context;
    mView = view;
  }

  public List<String> getCategrayNames() {
    if (categrayNames == null || categrayNames.size() == 0) {
      List<String> strings = new ArrayList<>();
      strings.add("水果");
      strings.add("烧烤");
      strings.add("旅游");
      return strings;
    }
    return categrayNames;
  }

  public void refreshRecyclerAdapterData(String category, RefreshType type) {
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

  enum RefreshType {
    Refresh,
    LoadMore
  }
}

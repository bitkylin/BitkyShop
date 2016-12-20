package cc.bitky.bitkyshop.fragment.categrayfragment;

import cc.bitky.bitkyshop.bean.Commodity;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import com.socks.library.KLog;
import java.util.List;

public class SubCategrayActivityPresenter {
  private SubCategoryActivity activity;
  private int currentPosition = 0;
  private int countLimit = 10;
  private String currentSubCategoryStr;

  SubCategrayActivityPresenter(SubCategoryActivity activity) {
    this.activity = activity;
  }

  void refreshRecyclerAdapterData(String subCategory, RefreshType type) {
    switch (type) {
      case Refresh:
        if (subCategory != null) {
          currentSubCategoryStr = subCategory;
        }
        new Thread(new Runnable() {
          @Override public void run() {
            currentPosition = 0;
            BmobQuery<Commodity> bmobQuery = new BmobQuery<>();
            bmobQuery.addWhereEqualTo("CategorySub", currentSubCategoryStr)
                .addWhereGreaterThan("Count", 0)
                .setLimit(countLimit)
                .setSkip(currentPosition)
                .order("createdAt");
            bmobQuery.findObjects(new FindListener<Commodity>() {
              @Override public void done(List<Commodity> list, BmobException e) {
                if (e != null) {
                  KLog.d("异常内容：" + e.getMessage());
                } else if (list.size() >= 0) {
                  KLog.d("list.size()=" + list.size());
                  activity.refreshRecyclerViewData(list, RefreshType.Refresh);
                }
                if (list.size() == 0) {
                  activity.CanNotRefreshData(RefreshType.Refresh);
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
            bmobQuery.addWhereEqualTo("CategorySub", currentSubCategoryStr)
                .addWhereGreaterThan("Count", 0)
                .setLimit(countLimit)
                .setSkip(currentPosition)
                .order("createdAt");
            bmobQuery.findObjects(new FindListener<Commodity>() {
              @Override public void done(List<Commodity> list, BmobException e) {
                if (e != null) {
                  KLog.d("异常内容：" + e.getMessage());
                } else if (list.size() > 0) {
                  activity.refreshRecyclerViewData(list, RefreshType.LoadMore);
                } else if (list.size() == 0) {
                  activity.CanNotRefreshData(RefreshType.LoadMore);
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

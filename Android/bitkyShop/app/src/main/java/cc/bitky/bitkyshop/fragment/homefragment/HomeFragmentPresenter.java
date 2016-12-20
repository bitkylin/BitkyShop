package cc.bitky.bitkyshop.fragment.homefragment;

import android.content.Context;
import cc.bitky.bitkyshop.bean.Commodity;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import com.socks.library.KLog;
import java.util.List;

class HomeFragmentPresenter {
  private IHomeFragment mView;
  private int currentPosition = 0;
  private int countLimit = 10;
  private Context mContext;

  HomeFragmentPresenter(Context context, IHomeFragment view) {
    mContext = context;
    mView = view;
  }

  void refreshRecyclerAdapterData(RefreshType type) {
    switch (type) {
      case Refresh:
        new Thread(new Runnable() {
          @Override public void run() {
            currentPosition = 0;
            BmobQuery<Commodity> bmobQuery = new BmobQuery<>();
            bmobQuery.addWhereEqualTo("Promotion", "true")
                .addWhereGreaterThan("Count", 0)
                .setLimit(countLimit)
                .setSkip(currentPosition)
                .order("createdAt");
            bmobQuery.findObjects(new FindListener<Commodity>() {
              @Override public void done(List<Commodity> list, BmobException e) {
                if (e != null) {
                  KLog.d("异常内容：" + e.getMessage());
                } else if (list.size() > 0) {
                  KLog.d("list.size()=" + list.size());
                  mView.refleshRecyclerViewData(list, RefreshType.Refresh);
                } else if (list.size() == 0) {
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
            bmobQuery.addWhereEqualTo("Promotion", "true")
                .addWhereGreaterThan("Count", 0)
                .setLimit(countLimit)
                .setSkip(currentPosition)
                .order("createdAt");
            bmobQuery.findObjects(new FindListener<Commodity>() {
              @Override public void done(List<Commodity> list, BmobException e) {
                if (e != null) {
                  KLog.d("异常内容：" + e.getMessage());
                } else if (list.size() > 0) {
                  mView.refleshRecyclerViewData(list, RefreshType.LoadMore);
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

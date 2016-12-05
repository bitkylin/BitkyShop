package cc.bitky.bitkyshop.globalDeploy;

import android.database.sqlite.SQLiteDatabase;
import cc.bitky.bitkyshop.BuildConfig;
import cc.bitky.bitkyshop.bean.Commodity;
import cc.bitky.bitkyshop.bean.cart.CommodityLocal;
import cc.bitky.bitkyshop.bean.cart.CommodityLocalDao;
import cc.bitky.bitkyshop.bean.cart.DaoMaster;
import cc.bitky.bitkyshop.bean.cart.DaoSession;
import com.socks.library.KLog;
import java.util.List;
import org.greenrobot.greendao.query.QueryBuilder;

public class GreenDaoKyHelper {

  private static DaoSession daoSession;

  static void init(DaoMaster.DevOpenHelper helper) {
    // do this once, for example in your Application class
    QueryBuilder.LOG_SQL = BuildConfig.BITKY_LOG_DEBUG;
    QueryBuilder.LOG_VALUES = BuildConfig.BITKY_LOG_DEBUG;

    SQLiteDatabase db = helper.getWritableDatabase();
    DaoMaster daoMaster = new DaoMaster(db);
    daoSession = daoMaster.newSession();
  }

  public static DaoSession getDaoSession() {
    if (daoSession != null) {
      return daoSession;
    } else {
      throw new NullPointerException("未初始化DaoSession");
    }
  }

  /**
   * 更新数据库中商品被选中的状态
   * @param c 数据库中的商品
   */
  public static void updateChecked(CommodityLocal c) {
    CommodityLocalDao commodityLocalDao = GreenDaoKyHelper.getDaoSession().getCommodityLocalDao();
    commodityLocalDao.update(c);
  }

  /**
   * 在[本地数据库]购物车中插入商品或增加其数量
   *
   * @param c 商品bean
   */
  public static void insertOrIncrease(Commodity c) {
    CommodityLocalDao commodityLocalDao = GreenDaoKyHelper.getDaoSession().getCommodityLocalDao();
    QueryBuilder<CommodityLocal> queryBuilder = commodityLocalDao.queryBuilder();
    queryBuilder.where(CommodityLocalDao.Properties.ObjectId.eq(c.getObjectId()));
    List<CommodityLocal> localList = queryBuilder.list();
    KLog.d("查询到的结果数:" + localList.size());
    CommodityLocal commodityLocal =
        new CommodityLocal(null, c.getObjectId(), c.getCategory(), c.getName(), c.getDetails(), 1,
            c.getPrice(), c.getCoverPhotoUrl(), false);
    if (localList.size() >= 1) {
      commodityLocal.setId(localList.get(0).getId());
      commodityLocal.setCartCount(localList.get(0).getCartCount() + 1);
      commodityLocalDao.update(commodityLocal);
    } else {
      commodityLocalDao.insert(commodityLocal);
    }
  }

  /**
   * 查询[本地数据库]购物车中的所有商品
   *
   * @return 查询到的所有商品
   */
  public static List<CommodityLocal> queryAll() {
    CommodityLocalDao commodityLocalDao = GreenDaoKyHelper.getDaoSession().getCommodityLocalDao();
    QueryBuilder<CommodityLocal> queryBuilder = commodityLocalDao.queryBuilder();
    return queryBuilder.list();
  }

  public static void deleteItem(CommodityLocal dataItem) {
    CommodityLocalDao commodityLocalDao = GreenDaoKyHelper.getDaoSession().getCommodityLocalDao();
    commodityLocalDao.delete(dataItem);
  }

  public static void deleteItems(List<CommodityLocal> dataItems) {
    if (dataItems == null || dataItems.size() == 0) return;
    CommodityLocalDao commodityLocalDao = GreenDaoKyHelper.getDaoSession().getCommodityLocalDao();
    commodityLocalDao.deleteInTx(dataItems);
  }
}

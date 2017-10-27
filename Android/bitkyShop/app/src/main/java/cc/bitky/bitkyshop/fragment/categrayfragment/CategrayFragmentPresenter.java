package cc.bitky.bitkyshop.fragment.categrayfragment;

import com.socks.library.KLog;

import java.util.ArrayList;
import java.util.List;

import cc.bitky.bitkyshop.bean.SubCategory;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class CategrayFragmentPresenter {
    ICategrayFragment mView;
    private List<String> categrayNames;
    private int currentPosition = 0;
    private int countLimit = 50;
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
                    @Override
                    public void run() {
                        currentPosition = 0;
                        BmobQuery<SubCategory> bmobQuery = new BmobQuery<>();
                        bmobQuery.addWhereEqualTo("mainCategory", currentCategoryStr)
                                .setLimit(countLimit)
                                .setSkip(currentPosition)
                                .order("createdAt");
                        bmobQuery.findObjects(new FindListener<SubCategory>() {
                            @Override
                            public void done(List<SubCategory> list, BmobException e) {
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
                    @Override
                    public void run() {
                        currentPosition = currentPosition + 10;
                        BmobQuery<SubCategory> bmobQuery = new BmobQuery<>();
                        bmobQuery.addWhereEqualTo("mainCategory", currentCategoryStr)
                                .setLimit(countLimit)
                                .setSkip(currentPosition)
                                .order("createdAt");
                        bmobQuery.findObjects(new FindListener<SubCategory>() {
                            @Override
                            public void done(List<SubCategory> list, BmobException e) {
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
            //  strings.add("烟、酒");
            strings.add("水、饮料");
            strings.add("牛奶、面包");
            strings.add("休闲食品");
            strings.add("饼干糕点");
            strings.add("冲调、粥");
            strings.add("火腿、面条");
            strings.add("洗漱用品");
            strings.add("冰棒、冷冻");
            strings.add("文体用品");
            strings.add("生活日用");
            strings.add("纸品湿巾");
            strings.add("粮油调料");
            strings.add("礼品、饰品");
            strings.add("手机配件");
            strings.add("电脑配件");

//      strings.add("烟酒饮料");
//      strings.add("美容洗护");
//      strings.add("家居用品");
//      strings.add("方便速食");
//      strings.add("粮油米类");
//      strings.add("厨具");
//      strings.add("学习用品");
            return strings;
        }
        return categrayNames;
    }

    enum RefreshType {
        Refresh,
        LoadMore
    }
}

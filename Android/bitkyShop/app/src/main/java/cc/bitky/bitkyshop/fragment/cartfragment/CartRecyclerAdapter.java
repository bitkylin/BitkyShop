package cc.bitky.bitkyshop.fragment.cartfragment;

import android.net.Uri;
import android.view.View;
import android.widget.CheckBox;
import cc.bitky.bitkyshop.R;
import cc.bitky.bitkyshop.bean.cart.CommodityLocal;
import cc.bitky.bitkyshop.utils.recyclerview.KyBaseRecyclerAdapter;
import cc.bitky.bitkyshop.utils.recyclerview.KyBaseViewHolder;
import cc.bitky.bitkyshop.utils.widget.ItemCounter;
import com.socks.library.KLog;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CartRecyclerAdapter extends KyBaseRecyclerAdapter<CommodityLocal> {
  private ItemCounterListener itemCounterListener;
  private CheckboxListener checkboxListener;
  private DeleteItemListener deleteItemListener;
  private Boolean isShowItemDeleteButton = false;

  /**
   * 初始化 RecyclerView 适配器
   *
   * @param mDatas 绑定的数据
   */
  public CartRecyclerAdapter(List<CommodityLocal> mDatas) {
    super(mDatas, R.layout.recycler_cartfragment_show);
    listener = new KyRecyclerViewItemOnClickListener<CommodityLocal>() {
      @Override public void Onclick(View v, int adapterPosition, CommodityLocal data) {
        KLog.d("点击：" + data.getName() + "位置:" + adapterPosition);
      }
    };
  }

  @Override
  public void setDataToViewHolder(final CommodityLocal dataItem, KyBaseViewHolder holder) {
    //图像显示
    holder.getSimpleDraweeView(R.id.recycler_cartfragment_draweeview)
        .setImageURI(Uri.parse(dataItem.getCoverPhotoUrl()));
    //条目介绍
    holder.getTextView(R.id.recycler_cartfragment_text_title).setText(dataItem.getName());
    //价格
    holder.getTextView(R.id.recycler_cartfragment_text_price)
        .setText(dataItem.getPrice().toString() + " 元");
    //条目的复选框
    holder.getCheckBox(R.id.cartfragment_checkbox_item).setChecked(dataItem.getCartIsChecked());
    holder.getCheckBox(R.id.cartfragment_checkbox_item)
        .setOnClickListener(new View.OnClickListener() {
          @Override public void onClick(View v) {
            CheckBox c = ((CheckBox) v);
            dataItem.setCartIsChecked(c.isChecked());
            if (checkboxListener != null) checkboxListener.OnClick(c);
          }
        });
    //条目的删除按钮
    holder.getButton(R.id.recycler_cartfragment_btnRemoveItem)
        .setOnClickListener(new View.OnClickListener() {
          @Override public void onClick(View v) {
            int index = mDatas.indexOf(dataItem);
            mDatas.remove(dataItem);
            notifyItemRemoved(index);
            if (deleteItemListener != null) deleteItemListener.OnClick(dataItem);
          }
        });

    //是否显示删除按钮
    if (isShowItemDeleteButton) {
      holder.getButton(R.id.recycler_cartfragment_btnRemoveItem).setVisibility(View.VISIBLE);
      holder.getView(R.id.recycler_cartfragment_ItemCounter).setVisibility(View.GONE);
    } else {
      holder.getButton(R.id.recycler_cartfragment_btnRemoveItem).setVisibility(View.GONE);
      holder.getView(R.id.recycler_cartfragment_ItemCounter).setVisibility(View.VISIBLE);
    }
    //商品数量改变自定义控件
    ItemCounter itemCounter = holder.getView(R.id.recycler_cartfragment_ItemCounter);
    itemCounter.setValue(dataItem.getCartCount());
    itemCounter.setOnCounterOverflowListener(new ItemCounter.OnCounterOverflowListener() {
      @Override public void addOverflow() {
        if (itemCounterListener != null) itemCounterListener.addOverflow();
      }

      @Override public void minusOverflow() {
        if (itemCounterListener != null) itemCounterListener.minusOverflow();
      }
    });
    itemCounter.setOnCounterClickListener(new ItemCounter.OnCounterClickListener() {
      @Override public void OnButtonClick(int currentCount, ItemCounter.ButtonType type) {

        dataItem.setCartCount(currentCount);
        if (itemCounterListener != null) itemCounterListener.OnButtonClick(currentCount, type);
      }
    });
  }

  /**
   * 删除被选中的所有条目
   */
  public List<CommodityLocal> deleteCheckedItem() {
    List<CommodityLocal> deletedList = new ArrayList<>();
    Iterator<CommodityLocal> iterator = mDatas.iterator();
    while (iterator.hasNext()) {
      CommodityLocal commodityLocal = iterator.next();
      if (commodityLocal.getCartIsChecked()) {
        deletedList.add(commodityLocal);
        int index = mDatas.indexOf(commodityLocal);
        iterator.remove();
        notifyItemRemoved(index);
      }
    }
    return deletedList;
  }

  public void setOnItemCounterListener(ItemCounterListener itemCounterListener) {
    this.itemCounterListener = itemCounterListener;
  }

  public void setOnCheckboxListener(CheckboxListener checkboxListener) {
    this.checkboxListener = checkboxListener;
  }

  public void setOnDeleteItemListener(DeleteItemListener deleteItemListener) {
    this.deleteItemListener = deleteItemListener;
  }

  interface ItemCounterListener {
    void addOverflow();

    void minusOverflow();

    void OnButtonClick(int currentCount, ItemCounter.ButtonType type);
  }

  interface CheckboxListener {
    void OnClick(CheckBox v);
  }

  interface DeleteItemListener {
    void OnClick(CommodityLocal dataItem);
  }

  public void showItemDeleteButton(Boolean isShowItemDeleteButton) {
    this.isShowItemDeleteButton = isShowItemDeleteButton;
    for (CommodityLocal mData : mDatas) {
      mData.setCartIsChecked(false);
    }
    notifyItemRangeChanged(0, mDatas.size());
  }
}

package cc.bitky.bitkyshop.fragment.userfragment.addressactivity;

import cc.bitky.bitkyshop.bean.cart.ReceiveAddress;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import com.socks.library.KLog;
import java.util.List;

public class AddressOptionPresenter {
  private AddressOptionActivity activity;

  public AddressOptionPresenter(AddressOptionActivity activity) {
    this.activity = activity;
  }

  public void getCurrentUserAddress(String userObjectId) {
    BmobQuery<ReceiveAddress> bmobQuery = new BmobQuery<>();
    bmobQuery.addWhereEqualTo("userObjectId", userObjectId);
    bmobQuery.setLimit(50);
    bmobQuery.findObjects(new FindListener<ReceiveAddress>() {
      @Override public void done(List<ReceiveAddress> list, BmobException e) {
        if (e != null) {
          activity.showMessage("您还没有添加收货地址哦！", null);
          return;
        }
        activity.initReceiveAddress(list);
      }
    });
  }

  /**
   * 插入指定的Item
   *
   * @param createdAddress 待插入的Item
   */
  public void insertUserAddress(final ReceiveAddress createdAddress) {
    createdAddress.save(new SaveListener<String>() {
      @Override public void done(String s, BmobException e) {
        if (e != null) {
          activity.showMessage(e.getMessage(), null);
          return;
        }
        KLog.d("insertUserAddress:" + s);
        activity.showMessage("新建收货地址成功", null);
        getCurrentUserAddress(createdAddress.getUserObjectId());
      }
    });
  }

  /**
   * 更新指定的Item
   *
   * @param addressObjectId Item的objectId
   * @param isDefault Item的默认状态
   */
  public void updateUserAddressDefault(final String addressObjectId, final Boolean isDefault) {
    ReceiveAddress receiveAddress = new ReceiveAddress();
    receiveAddress.setObjectId(addressObjectId);
    receiveAddress.setDefault(isDefault);
    receiveAddress.update(new UpdateListener() {
      @Override public void done(BmobException e) {
        if (e != null) {
          activity.showMessage(e.getMessage(), null);
        }
      }
    });
  }

  /**
   * 删除指定的Item
   *
   * @param addressObjectId 指定的Item的ObjectId
   */
  public void deleteUserAddress(final String addressObjectId) {
    ReceiveAddress receiveAddress = new ReceiveAddress();
    receiveAddress.setObjectId(addressObjectId);
    receiveAddress.delete(new UpdateListener() {
      @Override public void done(BmobException e) {
        if (e != null) {
          activity.showMessage(e.getMessage(), null);
        } else {
          activity.showMessage(addressObjectId, Type.deleteAddressSuccess);
        }
      }
    });
  }

  enum Type {
    insertCreatedAddressSuccessful,
    insertCreatedAddressfailed,
    deleteAddressSuccess

  }
}

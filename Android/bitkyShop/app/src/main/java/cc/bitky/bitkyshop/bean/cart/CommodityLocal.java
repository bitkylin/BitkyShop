package cc.bitky.bitkyshop.bean.cart;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Unique;

@Entity public class CommodityLocal implements Cloneable {
  @Id Long id;
  @Unique String objectId;
  String category;
  String name;
  String details;
  Integer cartCount;
  Double price;
  String CoverPhotoUrl;
  Boolean CartIsChecked;

  @Generated(hash = 449135961)
  public CommodityLocal(Long id, String objectId, String category, String name, String details,
      Integer cartCount, Double price, String CoverPhotoUrl, Boolean CartIsChecked) {
    this.id = id;
    this.objectId = objectId;
    this.category = category;
    this.name = name;
    this.details = details;
    this.cartCount = cartCount;
    this.price = price;
    this.CoverPhotoUrl = CoverPhotoUrl;
    this.CartIsChecked = CartIsChecked;
  }

  @Generated(hash = 252057792) public CommodityLocal() {
  }

  public Long getId() {
    return this.id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getObjectId() {
    return this.objectId;
  }

  public void setObjectId(String objectId) {
    this.objectId = objectId;
  }

  public String getCategory() {
    return this.category;
  }

  public void setCategory(String category) {
    this.category = category;
  }

  public String getName() {
    return this.name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDetails() {
    return this.details;
  }

  public void setDetails(String details) {
    this.details = details;
  }

  public Integer getCartCount() {
    return this.cartCount;
  }

  public void setCartCount(Integer cartCount) {
    this.cartCount = cartCount;
  }

  public Double getPrice() {
    return this.price;
  }

  public void setPrice(Double price) {
    this.price = price;
  }

  public String getCoverPhotoUrl() {
    return this.CoverPhotoUrl;
  }

  public void setCoverPhotoUrl(String CoverPhotoUrl) {
    this.CoverPhotoUrl = CoverPhotoUrl;
  }

  public Boolean getCartIsChecked() {
    return this.CartIsChecked;
  }

  public void setCartIsChecked(Boolean CartIsChecked) {
    this.CartIsChecked = CartIsChecked;
  }
}

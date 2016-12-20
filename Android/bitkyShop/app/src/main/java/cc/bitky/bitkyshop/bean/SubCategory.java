package cc.bitky.bitkyshop.bean;

import cn.bmob.v3.BmobObject;

public class SubCategory extends BmobObject {

  private String mainCategory;
  private String photoUrl;
  public String photoName;
  private String name;

  public String getMainCategory() {
    return mainCategory;
  }

  public void setMainCategory(String mainCategory) {
    this.mainCategory = mainCategory;
  }

  public String getPhotoUrl() {
    return photoUrl;
  }

  public void setPhotoUrl(String photoUrl) {
    this.photoUrl = photoUrl;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getPhotoName() {
    return photoName;
  }

  public void setPhotoName(String photoName) {
    this.photoName = photoName;
  }
}

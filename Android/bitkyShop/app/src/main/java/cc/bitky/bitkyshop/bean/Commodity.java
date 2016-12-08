package cc.bitky.bitkyshop.bean;

import cn.bmob.v3.BmobObject;
import java.util.List;

public class Commodity extends BmobObject {
  private String BitkyId;
  private Integer BitkyMode;
  private String BitkyModeStr;
  private String Promotion;
  private String AD;
  private String Category;
  private String CategorySub;
  private String Name;
  private Double Price;
  private Integer Count;
  private String Details;
  private String CoverPhotoUrl;
  private List<String> CoverPhotoUrlSet;
  private String CoverPhotoName;
  private List<String> CoverPhotoNameSet;

  public String getPromotion() {
    return Promotion;
  }

  public void setPromotion(String promotion) {
    Promotion = promotion;
  }

  public String getAD() {
    return AD;
  }

  public void setAD(String AD) {
    this.AD = AD;
  }

  public String getCategorySub() {
    return CategorySub;
  }

  public void setCategorySub(String categorySub) {
    CategorySub = categorySub;
  }

  public String getBitkyModeStr() {
    return BitkyModeStr;
  }

  public void setBitkyModeStr(String bitkyModeStr) {
    BitkyModeStr = bitkyModeStr;
  }

  public String getBitkyId() {
    return BitkyId;
  }

  public void setBitkyId(String bitkyId) {
    BitkyId = bitkyId;
  }

  public Integer getBitkyMode() {
    return BitkyMode;
  }

  public void setBitkyMode(Integer bitkyMode) {
    BitkyMode = bitkyMode;
  }

  public String getCategory() {
    return Category;
  }

  public void setCategory(String category) {
    Category = category;
  }

  public String getName() {
    return Name;
  }

  public void setName(String name) {
    Name = name;
  }

  public Double getPrice() {
    return Price;
  }

  public void setPrice(Double price) {
    Price = price;
  }

  public Integer getCount() {
    return Count;
  }

  public void setCount(Integer count) {
    Count = count;
  }

  public String getDetails() {
    return Details;
  }

  public void setDetails(String details) {
    Details = details;
  }

  public String getCoverPhotoUrl() {
    return CoverPhotoUrl;
  }

  public void setCoverPhotoUrl(String coverPhotoUrl) {
    CoverPhotoUrl = coverPhotoUrl;
  }

  public List<String> getCoverPhotoUrlSet() {
    return CoverPhotoUrlSet;
  }

  public void setCoverPhotoUrlSet(List<String> coverPhotoUrlSet) {
    CoverPhotoUrlSet = coverPhotoUrlSet;
  }

  public String getCoverPhotoName() {
    return CoverPhotoName;
  }

  public void setCoverPhotoName(String coverPhotoName) {
    CoverPhotoName = coverPhotoName;
  }

  public List<String> getCoverPhotoNameSet() {
    return CoverPhotoNameSet;
  }

  public void setCoverPhotoNameSet(List<String> coverPhotoNameSet) {
    CoverPhotoNameSet = coverPhotoNameSet;
  }
}

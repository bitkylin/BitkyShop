package cc.bitky.bitkyshop.bean.cart;

import cn.bmob.v3.BmobUser;
import java.util.List;

public class KyUser extends BmobUser {

  List<ReceiveAddress> addressList;
  ReceiveAddress defaultAddress;

  String pwdResumeQuestion;
  String pwdResumeAnswer;

  public String getPwdResumeQuestion() {
    return pwdResumeQuestion;
  }

  public void setPwdResumeQuestion(String pwdResumeQuestion) {
    this.pwdResumeQuestion = pwdResumeQuestion;
  }

  public String getPwdResumeAnswer() {
    return pwdResumeAnswer;
  }

  public void setPwdResumeAnswer(String pwdResumeAnswer) {
    this.pwdResumeAnswer = pwdResumeAnswer;
  }

  public List<ReceiveAddress> getAddressList() {
    return addressList;
  }

  public void setAddressList(List<ReceiveAddress> addressList) {
    this.addressList = addressList;
  }

  public ReceiveAddress getDefaultAddress() {
    return defaultAddress;
  }

  public void setDefaultAddress(ReceiveAddress defaultAddress) {
    this.defaultAddress = defaultAddress;
  }
}

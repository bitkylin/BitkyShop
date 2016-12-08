package cc.bitky.bitkyshop.bean.cart;

import cn.bmob.v3.BmobUser;

public class KyUser extends BmobUser {
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
}


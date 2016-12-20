package cc.bitky.bitkyshop.fragment.userfragment.loginfragment;

interface ILoginActivity {
  void userLoginByLegacy(String userName, String password);

  void userLoginByPhone(String phone);

  void signUp();
}

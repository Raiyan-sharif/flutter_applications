class LoginModel{
  String _mobileno;
  String _password;
  bool _login_status;
  LoginModel(this._mobileno, this._password, this._login_status);

  String get password => _password;

  set login_status(bool value){
    _login_status = value;
  }
  bool get login_status => _login_status;
  set password(String value) {
    _password = value;
  }

  String get mobileno => _mobileno;

  set mobileno(String value) {
    _mobileno = value;
  }


}
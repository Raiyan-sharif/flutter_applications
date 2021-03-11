import 'dart:ffi';

import 'package:flutter/material.dart';
import 'package:flutter_login/flutter_login.dart';
import 'package:shared_preferences/shared_preferences.dart';

import 'models/login_model.dart';

class LoginWidget extends StatefulWidget {
  @override
  _LoginWidgetState createState() => _LoginWidgetState();
}

class _LoginWidgetState extends State<LoginWidget> {
  bool login_status;

  Future<LoginModel> _getAllFromSharedPreference() async{
    final prefs = await SharedPreferences.getInstance();
    final pf_mobileNumber = prefs.getString('mobileno');
    final pf_password = prefs.getString('password');
    login_status = prefs.getBool('login_status');
    if(pf_mobileNumber == null || pf_password == null || login_status == null){
      return null;
    }
    final loginObject = LoginModel(pf_mobileNumber, pf_password, login_status);
    return loginObject;
  }

  Future<Void> _setLoginInformationToSharedPreference(String mobileno, String password, bool status) async{
    final prefs = await SharedPreferences.getInstance();
    await prefs.setString('mobileno',mobileno);
    await prefs.setString('password',password);
    await prefs.setBool('login_status', status);
  }


  @override
  Widget build(BuildContext context) {
    return Column(
      children: [
        Expanded(
          flex: 3,
          child: Column(
              mainAxisAlignment: MainAxisAlignment.center,
              children: [
            Image.asset('ic_launcher_foreground.png'),
            Text('YRC-BD'),
          ]),
        ),
        Expanded(
          flex: 4,
          child: Container(
            padding: EdgeInsets.only(left: 40, top: 30,right: 40,bottom: 30),
            color: Colors.indigo[900],
            width: double.infinity,
            child: ListView(
//                crossAxisAlignment: CrossAxisAlignment.stretch,
//                mainAxisAlignment: MainAxisAlignment.spaceBetween,
              children: [
                TextField(
                  decoration: InputDecoration(
                      border: OutlineInputBorder(
                        borderRadius: BorderRadius.circular(5.0),
                        borderSide: BorderSide(
                          color: Colors.amber,
                          style: BorderStyle.solid,
                        ),
                      ),
                      labelText: 'Mobile No',
                      labelStyle: TextStyle(
                        color: Colors.white,
                      )),
                  style: TextStyle(
                    color: Colors.white,
                  ),
                ),
                SizedBox(
                  height: 30,
                ),
                TextField(
                  obscureText: true,
                  decoration: InputDecoration(
                    labelText: 'Password',
                    border: OutlineInputBorder(
                      borderRadius: BorderRadius.circular(5.0),
                      borderSide: BorderSide(
                        color: Colors.amber,
                        style: BorderStyle.solid,
                      ),
                    ),
                    labelStyle: TextStyle(
                      color: Colors.white,
                    ),
                  ),
                  style: TextStyle(
                    color: Colors.white,
                  ),
                ),
                SizedBox(
                  height: 30,
                ),
                RaisedButton(
                  shape: RoundedRectangleBorder(
                      borderRadius: BorderRadius.circular(18.0),
                      side: BorderSide(color: Colors.white)),
                  onPressed: null,
                  child: Text(
                    'Login',
                    style: TextStyle(color: Colors.white),
                  ),
                ),
                SizedBox(
                  height: 30,
                ),
                Center(
                  child: Text(
                    'Don\'t have an accounnt? Sign Up.',
                    style: TextStyle(color: Colors.white),
                  ),
                ),
                SizedBox(
                  height: 30,
                ),
                Center(
                    child: Text(
                  'Forgot password? Go to recovery.',
                  style: TextStyle(color: Colors.white),
                )),
              ],
            ),
          ),
        )
      ],
    );
  }
}

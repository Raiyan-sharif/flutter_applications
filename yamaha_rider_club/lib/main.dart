import 'package:flutter/material.dart';
import 'package:yamaha_rider_club/string_resources/constants.dart';

import 'Login.dart';
import 'LoginView.dart';

void main() {
  runApp(MyApp());
}

class MyApp extends StatelessWidget {
  // This widget is the root of your application.
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'YRC',
      theme: ThemeData(
        primarySwatch: Colors.blue,
      ),
      home: MyHomePage(),

    );
  }
}

class MyHomePage extends StatefulWidget {


  @override
  _MyHomePageState createState() => _MyHomePageState();
}

class _MyHomePageState extends State<MyHomePage> {



  @override
  Widget build(BuildContext context) {

    return SafeArea(
      child: Scaffold(


        body: Center(
          child:LoginWidget(),
        )

      ),


    );
  }
}

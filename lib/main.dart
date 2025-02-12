import 'package:flutter/material.dart';
import 'package:test2/callRecord_service.dart';
import 'package:test2/native_code.dart';

void main() {
  runApp(const MyApp());
}

class MyApp extends StatelessWidget {
  const MyApp({super.key});

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Flutter Demo',
      theme: ThemeData(
        colorScheme: ColorScheme.fromSeed(seedColor: Colors.deepPurple),
        useMaterial3: true,
      ),
      home: Scaffold(
        body: Center(
          child: Column(
            children: [
              ElevatedButton(
                  onPressed: () {
                    Navigator.push(
                      context,
                      MaterialPageRoute(
                        builder: (context) => NativeAndroid(),
                      ),
                    );
                  },
                  child: Text("screen time ")),
              ElevatedButton(
                  onPressed: () {
                    Navigator.push(
                      context,
                      MaterialPageRoute(
                        builder: (context) => CallrecordService(),
                      ),
                    );
                  },
                  child: Text("call service"))
            ],
          ),
        ),
      ),
    );
  }
}

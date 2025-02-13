import 'package:flutter/material.dart';
import 'package:test2/callRecord_service.dart';

import 'native_code.dart';

class SwitchPage extends StatelessWidget {
  const SwitchPage({super.key});

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: Center(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
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
              child: Text('Battery & Screen Usage'),
            ),
            const SizedBox(
              height: 10,
            ),
            ElevatedButton(
              onPressed: () {
                Navigator.push(
                  context,
                  MaterialPageRoute(
                    builder: (context) => CallrecordService(),
                  ),
                );
              },
              child: Text("enable Call blocking"),
            ),
          ],
        ),
      ),
    );
  }
}

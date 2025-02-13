import 'package:flutter/material.dart';
import 'package:flutter/services.dart';


import 'call_helper.dart';

class CallrecordService extends StatefulWidget {
  const CallrecordService({super.key});

  @override
  State<CallrecordService> createState() => _CallrecordServiceState();
}

class _CallrecordServiceState extends State<CallrecordService> {
  bool _isDefault = false;

  void checkDefaultDialer() async {
    bool result = await CallHelper.isDefaultDialer();
    setState(() {
      _isDefault = result;
    });
  }

  @override
  void initState()  {
    super.initState();
    checkDefaultDialer();
  }
  // Future<void> checkCallScreeningStatus() async {
  //   try {
  //     final bool status = await platform.invokeMethod('isCallScreeningEnabled');
  //     setState(() {
  //       isCallScreeningEnabled = status;
  //     });
  //   } on PlatformException catch (e) {
  //     print("Failed to check status: ${e.message}");
  //   }
  // }

  // Future<void> openCallScreeningSettings() async {
  //   if (!isCallScreeningEnabled) {
  //     try {
  //       await platform.invokeMethod('openCallScreeningSettings');
  //     } on PlatformException catch (e) {
  //       print("Failed to open settings: ${e.message}");
  //     }
  //   }
  // }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: Text("Call Control")),
      body: Center(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            Text(_isDefault
                ? "App is Default Dialer"
                : "App is NOT Default Dialer"),
            ElevatedButton(
              onPressed: () {
                CallHelper.requestDefaultDialer();
                Future.delayed(Duration(seconds: 2),
                    checkDefaultDialer); // Recheck after setting
              },
              child: Text("Set as Default Dialer"),
            ),
            ElevatedButton(
              onPressed: checkDefaultDialer,
              child: Text("Check Default Dialer"),
            ),
          ],
        ),
      ),
    );
  }
}


class CallHelper {
  static const platform = MethodChannel('test/native');

  static Future<void> requestDefaultDialer() async {
    try {
      await platform.invokeMethod('setDefaultDialer');
    } catch (e) {
      print("Error setting default dialer: $e");
    }
  }

  static Future<bool> isDefaultDialer() async {
    try {
      final bool isDefault = await platform.invokeMethod('isDefaultDialer');
      return isDefault;
    } catch (e) {
      print("Error checking default dialer status: $e");
      return false;
    }
  }
}

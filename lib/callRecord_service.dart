import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:url_launcher/url_launcher.dart';

class CallrecordService extends StatefulWidget {
  const CallrecordService({super.key});

  @override
  State<CallrecordService> createState() => _CallrecordServiceState();
}

class _CallrecordServiceState extends State<CallrecordService> {
  bool isCallScreeningEnabled = false;
  static const platform = MethodChannel('test/native');
  @override
  void initState() {
    super.initState();
    checkCallScreeningStatus();
  }
  Future<void> checkCallScreeningStatus() async {
    try {
      final bool status = await platform.invokeMethod('isCallScreeningEnabled');
      setState(() {
        isCallScreeningEnabled = status;
      });
    } on PlatformException catch (e) {
      print("Failed to check status: ${e.message}");
    }
  }

  Future<void> openCallScreeningSettings() async {
    if (!isCallScreeningEnabled) {
      try {
        await platform.invokeMethod('openCallScreeningSettings');
      } on PlatformException catch (e) {
        print("Failed to open settings: ${e.message}");
      }
    }
  }


  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: Text("Call Control")),
      body: Center(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            Text(isCallScreeningEnabled ? "Call Screening is Enabled" : "Call Screening is Disabled"),
            const SizedBox(height: 20),
            ElevatedButton(
              onPressed: openCallScreeningSettings,
              child: const Text("Enable Call Screening"),
            ),
          ],
        ),
      ),
    );
  }
}

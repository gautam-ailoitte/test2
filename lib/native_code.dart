import 'dart:developer';

import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

class NativeAndroid extends StatefulWidget {
  const NativeAndroid({super.key});

  @override
  State<NativeAndroid> createState() => _NativeAndroidState();
}

class _NativeAndroidState extends State<NativeAndroid> {
  static const platform = MethodChannel('test/native');
  String _batteryLevel = "unknown level";
  bool hasUsagePermission = false;
  Map<String, double> screenTimeData = {};

  Future<bool> isUsageAccessGranted() async {
    try {
      final bool granted = await platform.invokeMethod('isUsageAccessGranted');
      return granted;
    } catch (e) {
      print("Error checking permission: $e");
      return false;
    }
  }

  Future<void> openUsageAccessSettings() async {
    try {
      await platform.invokeMethod('openUsageAccessSettings');
    } catch (e) {
      print("Error opening settings: $e");
    }
  }

  Future<void> checkPermission() async {
    bool granted = await isUsageAccessGranted();
    setState(() {
      hasUsagePermission = granted;
    });
  }

  Future<void> getBatteryLevel() async {
    String batteryLevel;
    try {
      final int result = await platform.invokeMethod('getBatteryLevel');
      batteryLevel = 'battery level at $result %';
    } on PlatformException catch (e) {
      batteryLevel = 'error $e';
      print(e.toString());
    }
    setState(() {
      _batteryLevel = batteryLevel;
    });
  }
  String formatDuration(int milliseconds) {
    int seconds = milliseconds ~/ 1000;
    int minutes = (seconds ~/ 60) % 60;
    int hours = seconds ~/ 3600;

    return '${hours}h ${minutes}m';
  }
  Future<void> getScreenTimeData() async {
    Map<String, double> screenTime = {};
    print("screen time called 1");
    try {
      final Map<Object?, Object?> result =
          await platform.invokeMethod('getScreenTimeData');
      final screenTime = result.map(
        (key, value) => MapEntry(key.toString(), (value as int)/(1000 * 60 * 60),),
      );
      print("screen time called 2");
      log(screenTime.toString());
    } catch (e) {
      print("Failed to get screen time data: $e");
    }
    setState(() {
      screenTimeData = screenTime;
    });
  }

  @override
  void initState() {
    super.initState();
    checkPermission();
    getBatteryLevel();
    getScreenTimeData();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(),
      body: Column(
        mainAxisAlignment: MainAxisAlignment.center,
        children: [
          hasUsagePermission
              ? Text("Usage Access Granted ✅")
              : Column(
                  mainAxisAlignment: MainAxisAlignment.center,
                  children: [
                    Text("Usage Access Required ❌"),
                    SizedBox(height: 10),
                    ElevatedButton(
                      onPressed: () async {
                        await openUsageAccessSettings();
                        await Future.delayed(Duration(
                            seconds: 2)); // Allow time for settings to open
                        checkPermission(); // Recheck permission status
                      },
                      child: Text("Grant Usage Access"),
                    ),
                  ],
                ),
          ElevatedButton(
            onPressed: getBatteryLevel,
            child: Text('Get Battery native'),
          ),
          Text(_batteryLevel),
          SizedBox(
            height: 300,
            child: ListView.builder(
              itemCount: screenTimeData.length,
              itemBuilder: (context, index) {
                String packageName = screenTimeData.keys.elementAt(index);
                double usageTime = screenTimeData.values.elementAt(index);

                return ListTile(
                  title: Text(packageName),
                  subtitle: Text('Screen Time: ${usageTime}'),
                );
              },
            ),
          ),
        ],
      ),
    );
  }
}

import 'package:flutter/services.dart';

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

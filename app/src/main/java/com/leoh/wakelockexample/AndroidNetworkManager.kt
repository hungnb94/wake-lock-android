package com.leoh.wakelockexample

import android.content.Context
import android.net.wifi.WifiManager
import android.net.wifi.WifiManager.WifiLock
import android.os.Build

class AndroidNetworkManager {
	private var wifiLock: WifiLock? = null

	fun acquireWifiLock(context: Context) {
		val wifiManager: WifiManager = context.getSystemService(WifiManager::class.java)
		val lockType =
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
				WifiManager.WIFI_MODE_FULL_LOW_LATENCY
			} else {
				WifiManager.WIFI_MODE_FULL_HIGH_PERF
			}
		wifiLock =
			wifiManager.createWifiLock(lockType, "MyApp::WifiLock")
		wifiLock?.acquire()
	}

	fun releaseWifiLock() {
		if (wifiLock != null && wifiLock!!.isHeld) {
			wifiLock!!.release()
		}
	}
}

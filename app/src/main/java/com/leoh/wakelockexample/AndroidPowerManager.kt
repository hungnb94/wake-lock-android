package com.leoh.wakelockexample

import android.content.Context
import android.os.PowerManager
import android.os.PowerManager.WakeLock

class AndroidPowerManager {
	private var wakeLock: WakeLock? = null

	fun acquireWakeLock(
		context: Context,
		duration: Long,
	) {
		val powerManager: PowerManager = context.getSystemService(PowerManager::class.java)
		wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "MyApp::NetworkWakeLock")
		wakeLock?.acquire(duration)
	}

	fun releaseWakeLock() {
		wakeLock?.let {
			if (it.isHeld) {
				it.release()
			}
		}
	}
}

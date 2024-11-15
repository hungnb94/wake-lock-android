package com.leoh.wakelockexample

import android.content.Context
import android.os.PowerManager
import android.os.PowerManager.WakeLock

class PowerLockManager {
	private var wakeLock: WakeLock? = null

	fun acquireWakeLock(
		context: Context,
		timeoutMillis: Long,
	) {
		val powerManager: PowerManager = context.getSystemService(PowerManager::class.java)
		wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "femom::PowerWakeLock")
		wakeLock?.acquire(timeoutMillis)
	}

	fun acquireWakeLock(context: Context) {
		val powerManager: PowerManager = context.getSystemService(PowerManager::class.java)
		wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "femom::PowerWakeLock")
		wakeLock?.acquire()
	}

	fun isHeld(): Boolean = wakeLock?.isHeld == true

	fun releaseWakeLock() {
		wakeLock?.let { lock ->
			if (lock.isHeld) {
				lock.release()
			}
		}
	}
}

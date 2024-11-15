package com.leoh.wakelockexample

import android.content.Context
import android.os.SystemClock
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import java.util.concurrent.TimeUnit

@RunWith(AndroidJUnit4::class)
@Config(maxSdk = 34)
class PowerLockManagerTest {
	private val context: Context = ApplicationProvider.getApplicationContext()

	@Test
	fun default_isHeld_returnFalse() {
		val powerManager = PowerLockManager()
		assertThat(powerManager.isHeld()).isFalse()
	}

	@Test
	fun acquireWakeLock5Min_isHeld_returnTrue() {
		val powerManager = PowerLockManager()
		powerManager.acquireWakeLock(context)
		SystemClock.sleep(TimeUnit.MINUTES.toMillis(5))
		assertThat(powerManager.isHeld()).isTrue()
	}

	@Test
	fun acquireWakeLock9Hour_isHeld_returnTrue() {
		val powerManager = PowerLockManager()
		powerManager.acquireWakeLock(context)
		SystemClock.sleep(TimeUnit.HOURS.toMillis(9))
		assertThat(powerManager.isHeld()).isTrue()
	}

	@Test
	fun acquireWakeLock10Min_then_releaseWifiLock_isHeld_returnFalse() {
		val powerManager = PowerLockManager()
		powerManager.acquireWakeLock(context)
		SystemClock.sleep(TimeUnit.MINUTES.toMillis(10))
		powerManager.releaseWakeLock()
		assertThat(powerManager.isHeld()).isFalse()
	}

	@Test
	fun acquireWakeLock3Hour_then_releaseWifiLock_isHeld_returnFalse() {
		val powerManager = PowerLockManager()
		powerManager.acquireWakeLock(context)
		SystemClock.sleep(TimeUnit.HOURS.toMillis(3))
		powerManager.releaseWakeLock()
		assertThat(powerManager.isHeld()).isFalse()
	}

	@Test
	fun acquireWakeLock_timeout120Seconds() {
		val powerManager = PowerLockManager()
		powerManager.acquireWakeLock(context, TimeUnit.SECONDS.toMillis(120))
		SystemClock.sleep(TimeUnit.SECONDS.toMillis(100))
		assertThat(powerManager.isHeld()).isTrue()
		SystemClock.sleep(TimeUnit.SECONDS.toMillis(21))
		assertThat(powerManager.isHeld()).isFalse()
	}

	@Test
	fun acquireWakeLock_timeout2Hours() {
		val powerManager = PowerLockManager()
		powerManager.acquireWakeLock(context, TimeUnit.HOURS.toMillis(2))
		SystemClock.sleep(TimeUnit.MINUTES.toMillis(90))
		assertThat(powerManager.isHeld()).isTrue()
		SystemClock.sleep(TimeUnit.MINUTES.toMillis(31))
		assertThat(powerManager.isHeld()).isFalse()
	}
}

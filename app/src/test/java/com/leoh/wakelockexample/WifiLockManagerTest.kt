package digital.load.femom.library.wakelock

import android.content.Context
import android.os.SystemClock
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import com.leoh.wakelockexample.WifiLockManager
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import java.util.concurrent.TimeUnit

@RunWith(AndroidJUnit4::class)
@Config(maxSdk = 34)
class WifiLockManagerTest {
	private val context: Context = ApplicationProvider.getApplicationContext()

	@Test
	fun default_isHeld_returnFalse() {
		val wifiManager = WifiLockManager()
		assertThat(wifiManager.isHeld()).isFalse()
	}

	@Test
	fun acquireWifiLock5Min_isHeld_returnTrue() {
		val wifiManager = WifiLockManager()
		wifiManager.acquireWifiLock(context)
		SystemClock.sleep(TimeUnit.MINUTES.toMillis(5))
		assertThat(wifiManager.isHeld()).isTrue()
	}

	@Test
	fun acquireWifiLock9Hour_isHeld_returnTrue() {
		val wifiManager = WifiLockManager()
		wifiManager.acquireWifiLock(context)
		SystemClock.sleep(TimeUnit.HOURS.toMillis(9))
		assertThat(wifiManager.isHeld()).isTrue()
	}

	@Test
	fun acquireWifiLock10Min_then_releaseWifiLock_isHeld_returnFalse() {
		val wifiManager = WifiLockManager()
		wifiManager.acquireWifiLock(context)
		SystemClock.sleep(TimeUnit.MINUTES.toMillis(10))
		wifiManager.releaseWifiLock()
		assertThat(wifiManager.isHeld()).isFalse()
	}

	@Test
	fun acquireWifiLock3Hour_then_releaseWifiLock_isHeld_returnFalse() {
		val wifiManager = WifiLockManager()
		wifiManager.acquireWifiLock(context)
		SystemClock.sleep(TimeUnit.HOURS.toMillis(3))
		wifiManager.releaseWifiLock()
		assertThat(wifiManager.isHeld()).isFalse()
	}
}

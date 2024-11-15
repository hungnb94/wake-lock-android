package digital.load.femom.library.wakelock

import android.content.Context
import android.os.SystemClock
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.TimeUnit

@RunWith(AndroidJUnit4::class)
class WifiLockManagerTest {
	private val context: Context = ApplicationProvider.getApplicationContext()

	@Test
	fun default_isHeld_returnFalse() {
		val wifiManager = AndroidWifiManager()
		assertThat(wifiManager.isHeld()).isFalse()
	}

	@Test
	fun acquireWifiLock5Min_isHeld_returnTrue() {
		val wifiManager = AndroidWifiManager()
		wifiManager.acquireWifiLock(context)
		SystemClock.sleep(TimeUnit.MINUTES.toMillis(5))
		assertThat(wifiManager.isHeld()).isTrue()
	}

	@Test
	fun acquireWifiLock9Hour_isHeld_returnTrue() {
		val wifiManager = AndroidWifiManager()
		wifiManager.acquireWifiLock(context)
		SystemClock.sleep(TimeUnit.HOURS.toMillis(9))
		assertThat(wifiManager.isHeld()).isTrue()
	}

	@Test
	fun acquireWifiLock10Min_then_releaseWifiLock_isHeld_returnFalse() {
		val wifiManager = AndroidWifiManager()
		wifiManager.acquireWifiLock(context)
		SystemClock.sleep(TimeUnit.MINUTES.toMillis(10))
		wifiManager.releaseWifiLock()
		assertThat(wifiManager.isHeld()).isFalse()
	}

	@Test
	fun acquireWifiLock3Hour_then_releaseWifiLock_isHeld_returnFalse() {
		val wifiManager = AndroidWifiManager()
		wifiManager.acquireWifiLock(context)
		SystemClock.sleep(TimeUnit.HOURS.toMillis(3))
		wifiManager.releaseWifiLock()
		assertThat(wifiManager.isHeld()).isFalse()
	}
}

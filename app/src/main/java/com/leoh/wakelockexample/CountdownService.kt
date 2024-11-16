package com.leoh.wakelockexample

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.elvishew.xlog.XLog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import kotlin.math.abs

private const val CHANNEL_ID = "CountdownChannel"
private const val NOTIFICATION_ID = 1
private const val TAG = "CountdownService"

private const val DURATION = 300L

class CountdownService : Service() {
	private val retrofitService by lazy { createRetrofitService() }
	private val coroutineScope = CoroutineScope(Dispatchers.Default + SupervisorJob())
	private val logger = XLog.tag(TAG)
	private val powerManager = PowerLockManager()
	private val wifiManager = WifiLockManager()

	override fun onStartCommand(
		intent: Intent?,
		flags: Int,
		startId: Int,
	): Int {
		logger.d("onStartCommand")
		createNotificationChannel()
		startForeground(NOTIFICATION_ID, buildNotification(DURATION.toInt()))
		powerManager.acquireWakeLock(this)
		wifiManager.acquireWifiLock(this)
		startCountdown()
		return super.onStartCommand(intent, flags, startId)
	}

	private fun createNotificationChannel() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
			val channel =
				NotificationChannel(
					CHANNEL_ID,
					"Countdown Service Channel",
					NotificationManager.IMPORTANCE_HIGH,
				)
			channel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC
			val manager = getSystemService(NotificationManager::class.java)
			manager.createNotificationChannel(channel)
		}
	}

	private fun buildNotification(count: Int): Notification =
		NotificationCompat
			.Builder(this, CHANNEL_ID)
			.setContentTitle("Countdown in progress")
			.setContentText("Counting down: $count")
			.setSmallIcon(android.R.drawable.ic_dialog_info)
			.setOngoing(true)
			.setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
			.setSilent(true)
			.build()

	private fun expensiveTask(duration: Int) {
		val start = System.currentTimeMillis()
		var number = "0.0"
		while (System.currentTimeMillis() - start < duration) {
			number = abs(number.toDouble() * 11.1).toString()
		}
	}

	private fun startCountdown() {
		coroutineScope.launch {
			for (i in 300 downTo 1) {
				logger.d("countdown before: $i")
				expensiveTask(1000)
				logger.d("countdown after : $i")
				withContext(Dispatchers.Main) {
					updateNotification(i)
				}
			}
			expensiveTask(3000)
			performNetworkRequest()
		}
	}

	private fun updateNotification(count: Int) {
		val notification = buildNotification(count)
		val manager = getSystemService(NotificationManager::class.java)
		manager.notify(NOTIFICATION_ID, notification)
	}

	private fun performNetworkRequest() {
		XLog.d("Check connected: ${NetworkUtils.isNetworkConnected(this)}")
		XLog.d("Check connecting: ${NetworkUtils.isNetworkConnectedOrConnecting(this)}")
		retrofitService.getData().enqueue(
			object : Callback<Any> {
				override fun onResponse(
					call: Call<Any>,
					response: Response<Any>,
				) {
					updateFinalNotification(response.isSuccessful)
					powerManager.releaseWakeLock()
					wifiManager.releaseWifiLock()
				}

				override fun onFailure(
					call: Call<Any>,
					throwable: Throwable,
				) {
					updateFinalNotification(false)
					powerManager.releaseWakeLock()
					wifiManager.releaseWifiLock()
				}
			},
		)
	}

	private fun updateFinalNotification(success: Boolean) {
		val notificationText = if (success) "Network request success" else "Network request failed"
		logger.d(notificationText)
		val notification =
			NotificationCompat
				.Builder(this, CHANNEL_ID)
				.setContentTitle("Countdown Complete")
				.setContentText(notificationText)
				.setSmallIcon(android.R.drawable.ic_dialog_info)
				.setOngoing(false)
				.setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
				.setSilent(false)
				.build()

		val manager = getSystemService(NotificationManager::class.java)
		manager.notify(NOTIFICATION_ID, notification)
	}

	override fun onBind(intent: Intent?): IBinder? = null

	private fun createRetrofitService(): ApiService {
		val retrofit =
			Retrofit
				.Builder()
				.baseUrl("https://postman-echo.com/")
				.addConverterFactory(GsonConverterFactory.create())
				.client(
					OkHttpClient
						.Builder()
						.addInterceptor(
							HttpLoggingInterceptor { message ->
								XLog.tag("OkHttp").d(message)
							}.setLevel(HttpLoggingInterceptor.Level.BODY),
						).build(),
				).build()
		return retrofit.create(ApiService::class.java)
	}

	interface ApiService {
		@GET("cookies")
		fun getData(): Call<Any>
	}
}

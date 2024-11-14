package com.leoh.wakelockexample

import android.Manifest
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
	private lateinit var button: Button

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		enableEdgeToEdge()
		setContentView(R.layout.activity_main)
		ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
			val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
			v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
			insets
		}
		button = findViewById(R.id.button)
		button.setOnClickListener {
			val serviceIntent = Intent(this, CountdownService::class.java)
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
				startForegroundService(serviceIntent)
			} else {
				startService(serviceIntent)
			}
		}
		requestPermissions(
			arrayOf(
				Manifest.permission.POST_NOTIFICATIONS,
				Manifest.permission.FOREGROUND_SERVICE_CONNECTED_DEVICE,
				Manifest.permission.BLUETOOTH_CONNECT,
				Manifest.permission.BLUETOOTH_SCAN,
				Manifest.permission.WAKE_LOCK,
				Manifest.permission.CHANGE_WIFI_STATE,
				Manifest.permission.ACCESS_NETWORK_STATE,
			),
			1,
		)
	}
}

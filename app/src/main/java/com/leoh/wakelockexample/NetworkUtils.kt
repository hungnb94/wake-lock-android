package com.leoh.wakelockexample

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.elvishew.xlog.XLog

object NetworkUtils {
	fun isNetworkConnected(context: Context): Boolean {
		val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
		val networkCapabilities = connectivityManager.activeNetwork ?: return false
		val activeNetwork = connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false

		return when {
			activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
			activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
			activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
			else -> false
		}
	}

	fun isNetworkConnectedOrConnecting(context: Context): Boolean {
		val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
		val networkInfo = connectivityManager.activeNetworkInfo ?: return false
		if (networkInfo.isConnected) {
			XLog.d("Network is connected")
			return true
		}
		if (networkInfo.isConnectedOrConnecting) {
			XLog.d("Network is connecting")
			return true
		}
		if (networkInfo.isAvailable) {
			XLog.d("Network is available")
			return true
		}
		return false
	}
}

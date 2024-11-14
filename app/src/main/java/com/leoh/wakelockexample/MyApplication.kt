package com.leoh.wakelockexample

import android.app.Application
import android.util.Log
import com.elvishew.xlog.LogConfiguration
import com.elvishew.xlog.LogLevel
import com.elvishew.xlog.XLog
import com.elvishew.xlog.flattener.ClassicFlattener
import com.elvishew.xlog.printer.AndroidPrinter
import com.elvishew.xlog.printer.ConsolePrinter
import com.elvishew.xlog.printer.file.FilePrinter
import com.elvishew.xlog.printer.file.backup.NeverBackupStrategy
import java.io.File

class MyApplication : Application() {
	override fun onCreate() {
		super.onCreate()
		initializeLogger()
	}

	private fun initializeLogger() {
		val config =
			LogConfiguration
				.Builder()
				.logLevel(LogLevel.ALL)
				.enableThreadInfo()
				.enableStackTrace(2)
				.enableBorder()
				.build()

		val androidPrinter = AndroidPrinter(true)
		val consolePrinter = ConsolePrinter()
		val dir = obbDir ?: externalCacheDir ?: filesDir
		Log.d("App", "Log directory: $dir")
		val filePrinter =
			FilePrinter
				.Builder(File(dir, "log").absolutePath)
				.fileNameGenerator(DateTimeFileNameGenerator())
				.backupStrategy(NeverBackupStrategy())
				.flattener(ClassicFlattener())
				.build()
		XLog.init(config, androidPrinter, consolePrinter, filePrinter)
	}
}

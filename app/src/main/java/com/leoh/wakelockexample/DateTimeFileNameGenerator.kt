package com.leoh.wakelockexample

import com.elvishew.xlog.printer.file.naming.DateFileNameGenerator

class DateTimeFileNameGenerator : DateFileNameGenerator() {
	override fun generateFileName(
		logLevel: Int,
		timestamp: Long,
	): String = "${super.generateFileName(logLevel, timestamp)}.txt"
}

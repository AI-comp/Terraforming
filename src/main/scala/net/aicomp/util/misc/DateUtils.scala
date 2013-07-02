package net.aicomp.util.misc

import java.util.Calendar

object DateUtils {
  def dateStringForFileName(calendar: Calendar = Calendar.getInstance) = {
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH) + 1
    val day = calendar.get(Calendar.DAY_OF_MONTH)
    val hour = calendar.get(Calendar.HOUR_OF_DAY)
    val minute = calendar.get(Calendar.MINUTE)
    val second = calendar.get(Calendar.SECOND)
    List(year, month, day, hour, minute, second).mkString("_")
  }
}
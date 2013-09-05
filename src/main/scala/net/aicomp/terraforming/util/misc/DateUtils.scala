package net.aicomp.terraforming.util.misc

import java.util.Calendar

object DateUtils {
  def dateStringForFileName(calendar: Calendar = Calendar.getInstance) = {
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH) + 1
    val day = calendar.get(Calendar.DAY_OF_MONTH)
    val hour = calendar.get(Calendar.HOUR_OF_DAY)
    val minute = calendar.get(Calendar.MINUTE)
    val second = calendar.get(Calendar.SECOND)
    val f = "%02d"
    List(year, f.format(month), f.format(day), f.format(hour), f.format(minute), f.format(second)).mkString("_")
  }
}
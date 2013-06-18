package net.aicomp.util.misc

import java.io.File
import java.nio.charset.Charset

import com.google.common.io.Files

object Logger {
  val logDirectory = new File("log")
  val logFile = new File("log/log_" + DateUtils.dateStringForFileName + ".txt")

  logDirectory.mkdir()
  val writer = Files.newWriter(logFile, Charset.defaultCharset)

  def writeAndFlush(text: String) {
    writer.write(text)
    writer.flush()
  }
}
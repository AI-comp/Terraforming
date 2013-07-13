package net.aicomp.terraforming.util.misc

import java.io.BufferedInputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.io.PrintStream
import java.util.Calendar
import java.util.Random
import net.exkazuu.gameaiarena.io.InputStreams
import java.io.ByteArrayOutputStream

object StreamUtils {
  def initializeReplay(filePath: String) = {
    val stream = InputStreams.openFileOrResource(filePath);
    if (stream == null) {
      throw new IOException("Cant open the replay file:\n" + filePath);
    }
    var version = 0;
    if (stream.read() == 'V') {
      version = stream.read() - '0';
    }
    val bis = new BufferedInputStream(stream);
    val ois = new ObjectInputStream(bis);
    val rand = version match {
      case 0 => {
        ois.readObject().asInstanceOf[Random]
      }
      case _ =>
        throw new IOException("Unsupported replay file.")
    }
    (ois, rand)
  }

  def openStreamForJava(calendar: Calendar, random: Random) = {
    try {
      new File("replay").mkdir()
      val dateForName = DateUtils.dateStringForFileName(calendar)
      val fileName = "replay/" + dateForName + ".rep"
      val fos = new FileOutputStream(fileName);
      fos.write('V');
      fos.write('0');
      val oos = new ObjectOutputStream(fos);
      oos.writeObject(random)
      oos
    } catch {
      case _ => new ObjectOutputStream(new ByteArrayOutputStream())
    }
  }

  def openStreamForJavaScript(calendar: Calendar) = {
    try {
      new File("replay").mkdir()
      val dateForName = DateUtils.dateStringForFileName(calendar)
      val fileName = "replay/" + dateForName + ".js"
      new PrintStream(fileName)
    } catch {
      case _ => new PrintStream(new ByteArrayOutputStream())
    }
  }

  def openStreamForLogging(calendar: Calendar, prefix: String) = {
    try {
      new File("log").mkdir()
      val dateForName = DateUtils.dateStringForFileName(calendar)
      val fileName = "log/" + prefix + "_" + dateForName + ".txt"
      new PrintStream(fileName)
    } catch {
      case _ => new PrintStream(new ByteArrayOutputStream())
    }
  }
}
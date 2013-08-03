package net.aicomp.terraforming

import java.awt.Dimension
import java.awt.Font
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import java.awt.event.KeyEvent
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import java.util.Calendar
import java.util.Random
import java.util.Scanner

import scala.Array.canBuildFrom
import scala.collection.mutable.ListBuffer
import scala.util.control.Exception.allCatch

import org.apache.commons.cli.BasicParser
import org.apache.commons.cli.CommandLine
import org.apache.commons.cli.HelpFormatter
import org.apache.commons.cli.OptionBuilder
import org.apache.commons.cli.Options
import org.apache.commons.cli.ParseException

import javax.swing.JFrame
import javax.swing.JPanel
import javax.swing.JScrollPane
import javax.swing.JTextArea
import javax.swing.JTextField
import javax.swing.SpringLayout
import net.aicomp.terraforming.ai.SampleInternalManipulator
import net.aicomp.terraforming.entity.Field
import net.aicomp.terraforming.entity.Game
import net.aicomp.terraforming.entity.GameEnvironment
import net.aicomp.terraforming.entity.OrthogonalPoint
import net.aicomp.terraforming.entity.Player
import net.aicomp.terraforming.manipulator.AIPlayerGameManipulator
import net.aicomp.terraforming.manipulator.AIPlayerStartManipulator
import net.aicomp.terraforming.manipulator.ConsoleUserGameManipulator
import net.aicomp.terraforming.manipulator.ConsoleUserStartManipulator
import net.aicomp.terraforming.manipulator.GameManipulator
import net.aicomp.terraforming.manipulator.GraphicalUserGameManipulator
import net.aicomp.terraforming.manipulator.GraphicalUserStartManipulator
import net.aicomp.terraforming.manipulator.InternalAIPlayerGameManipulator
import net.aicomp.terraforming.manipulator.InternalAIPlayerStartManipulator
import net.aicomp.terraforming.manipulator.InternalManipulator
import net.aicomp.terraforming.manipulator.StartManipulator
import net.aicomp.terraforming.scene.AbstractScene
import net.aicomp.terraforming.scene.MainScene
import net.aicomp.terraforming.scene.ManipultorScene
import net.aicomp.terraforming.scene.PlayerScene
import net.aicomp.terraforming.scene.ResultScene
import net.aicomp.terraforming.scene.WaitingScene
import net.aicomp.terraforming.scene.graphic.GraphicalScene
import net.aicomp.terraforming.scene.graphic.TextBoxUtils
import net.aicomp.terraforming.scene.graphic.TitleScene
import net.aicomp.terraforming.util.misc.ImageLoader
import net.aicomp.terraforming.util.misc.StreamUtils
import net.aicomp.terraforming.util.settings.Defaults
import net.exkazuu.gameaiarena.gui.JGamePanel
import net.exkazuu.gameaiarena.gui.builder.GameGuiBuilder
import net.exkazuu.gameaiarena.gui.builder.WindowCreator
import net.exkazuu.gameaiarena.key.AwtKeyMemorizer
import net.exkazuu.gameaiarena.manipulator.Manipulator
import net.exkazuu.gameaiarena.player.ExternalComputerPlayer

object Main {

  val HELP = "h"
  val FPS = "f"
  val CUI_MODE = "c"
  val RESULT_MODE = "r"
  val SILENT = "s"
  val USER_PLAYERS = "u"
  val LIGHT_GUI_MODE = "l"
  val EXTERNAL_AI_PROGRAM = "a"
  val INTERNAL_AI_PROGRAM = "i"
  val NOT_SHOWING_LOG = "n"
  val calendar = Calendar.getInstance

  def main(args: Array[String]) {
    val options = buildOptions()
    try {
      val parser = new BasicParser()
      val cl = parser.parse(options, args)
      if (cl.hasOption(HELP)) {
        printHelp(options)
      } else {
        try {
          startGame(cl)
        } catch {
          case e: Throwable => {
            if (!cl.hasOption(SILENT)) {
              val errStream = StreamUtils.openStreamForLogging(calendar, "err")
              e.printStackTrace(errStream)
              System.err.println("Saved an occuerd error in the file of 'log/err_xxxx.txt'");
            }
            throw e
          }
        }
      }
    } catch {
      case e: ParseException => {
        System.err.println("Error: " + e.getMessage())
        printHelp(options)
        System.exit(-1)
      }
    }
  }

  def printHelp(options: Options) {
    val help = new HelpFormatter()
    help.printHelp(
      "java -jar Terraforming.jar [OPTIONS]\n"
        + "[OPTIONS]: ", "", options, "", true)
  }

  def buildOptions() = {
    OptionBuilder.hasArg()
    OptionBuilder.withDescription("Set 0-3 user players. When specifying no player option (-u, -a, -i), a game is provided for 1 user player and 2 default internal AI players")
    val userOption = OptionBuilder.create(USER_PLAYERS)

    OptionBuilder.hasArgs()
    OptionBuilder.withDescription("Set 1-3 AI players with external programs.")
    val externalAIOption = OptionBuilder.create(EXTERNAL_AI_PROGRAM)

    OptionBuilder.hasArgs()
    OptionBuilder.withDescription("Set 1-3 AI players with internal classes for debugging puropose.")
    val internalAIOption = OptionBuilder.create(INTERNAL_AI_PROGRAM)

    OptionBuilder.withDescription(
      "FPS to adjust game speed. Default value is 30 for user mode or 1000 for ai mode.")
    OptionBuilder.hasArg()
    OptionBuilder.withArgName("fps")
    val fpsOption = OptionBuilder.create(FPS)

    val options = new Options()
      .addOption(HELP, false, "Print this help.")
      .addOption(FPS, false, "Enable CUI mode.")
      .addOption(CUI_MODE, false, "Enable CUI mode.")
      .addOption(RESULT_MODE, false, "Enable result mode which show only a screen of a result.")
      .addOption(LIGHT_GUI_MODE, false, "Enable light and fast GUI mode by reducing rendering frequency.")
      .addOption(NOT_SHOWING_LOG, false, "Disable showing logs in the scree.")
      .addOption(SILENT, false, "Disable writing log files in the log directory.")
      .addOption(userOption)
      .addOption(externalAIOption)
      .addOption(internalAIOption)
      .addOption(fpsOption)
    options
  }

  def getOptionsValuesWithoutNull(cl: CommandLine, option: String) = {
    if (cl.hasOption(option))
      cl.getOptionValues(option)
    else
      Array[String]()
  }

  def startGame(cl: CommandLine) {
    val (env, startScene) = initializeEnvironmentAndScenes(cl)

    if (!cl.hasOption(SILENT)) {
      val logStream = StreamUtils.openStreamForLogging(calendar, "log")
      if (cl.hasOption(NOT_SHOWING_LOG)) {
        AbstractScene.display = logStream.print
      } else {
        val display = AbstractScene.display
        AbstractScene.display = { text =>
          logStream.print(text)
          logStream.flush()
          display(text)
        }
      }
    }

    if (cl.hasOption(FPS)) {
      val m = env.getSceneManager()
      val fps = allCatch opt cl.getOptionValue(FPS).toDouble getOrElse (m.getFps())
      m.setFps(fps)
    }

    if (cl.hasOption(LIGHT_GUI_MODE)) {
      ManipultorScene.lightMode = true
    }

    if (env.getRenderer() != null) {
      env.getRenderer().waitLoadImage()
    }
    env.start(startScene)
  }

  def initializeEnvironmentAndScenes(cl: CommandLine) = {
    val jss = StreamUtils.openStreamForJavaScript(calendar)
    if (cl.hasOption(CUI_MODE)) {
      val env = GameEnvironment()
      val scanner = new Scanner(System.in)
      AbstractScene.display = println
      val (startManipulators, gameManipulators) =
        initializeManipulators(cl, env, new ConsoleUserStartManipulator(scanner),
          new ConsoleUserGameManipulator(scanner))

      val resultScene = new ResultScene(null)
      val mainScene = new MainScene(resultScene, gameManipulators, jss)
      (env, new PlayerScene(mainScene, startManipulators))
    } else {
      val (window, env) = initializeComponents()
      val (startManipulators, gameManipulators) =
        initializeManipulators(cl, env, new GraphicalUserStartManipulator(),
          new GraphicalUserGameManipulator())

      val waitScene = new WaitingScene(null) with GraphicalScene
      val resultScene = new ResultScene(waitScene) with GraphicalScene
      if (cl.hasOption(RESULT_MODE)) {
        val mainScene = new MainScene(resultScene, gameManipulators, jss)
        (env, new PlayerScene(mainScene, startManipulators) with GraphicalScene)
      } else {
        val mainScene = new MainScene(resultScene, gameManipulators, jss) with GraphicalScene
        (env, new PlayerScene(mainScene, startManipulators) with TitleScene)
      }
    }
  }

  def initializeManipulators(cl: CommandLine, env: GameEnvironment, userStartManipulator: StartManipulator, userGameManipulator: GameManipulator) = {
    val nums = Vector(0 to 2: _*)
    val players = nums.map(Player(_))
    val nUsers = allCatch opt math.max(0, cl.getOptionValue(USER_PLAYERS).toInt) getOrElse (0)
    val externalCmds = getOptionsValuesWithoutNull(cl, EXTERNAL_AI_PROGRAM)
    val internalNames = getOptionsValuesWithoutNull(cl, INTERNAL_AI_PROGRAM)
    val defaultNames = nums.map(_ => classOf[SampleInternalManipulator].getName())
    val userIndices = if (nUsers + externalCmds.size + internalNames.size == 0) {
      Vector(0)
    } else {
      Vector(0 until nUsers: _*)
    }

    var iPlayers = 0
    val startMans = ListBuffer[Manipulator[Game, Array[String], String]]()
    val gameMans = ListBuffer[Manipulator[Game, Array[String], String]]()

    for (n <- userIndices.take(3 - iPlayers)) {
      startMans += userStartManipulator
      gameMans += userGameManipulator
      iPlayers += 1
    }
    for (cmd <- externalCmds.take(3 - iPlayers)) {
      val com = new ExternalComputerPlayer(cmd.split(" "))
      if (!cl.hasOption(SILENT)) {
        val out = StreamUtils.openStreamForLogging(calendar, "stdout_player" + iPlayers)
        val err = StreamUtils.openStreamForLogging(calendar, "stderr_player" + iPlayers)
        com.addOuputLogStream(out)
        com.addErrorLogStream(err)
      }
      com.addErrorLogStream(System.err)
      startMans += (new AIPlayerStartManipulator(players(iPlayers), com)).limittingTime(10000)
      gameMans += new AIPlayerGameManipulator(players(iPlayers), com)
        .limittingSumTime(1000, 5000)
      iPlayers += 1
    }
    for (name <- (internalNames ++ defaultNames).take(3 - iPlayers)) {
      val man = try {
        val clazz = Class.forName(name)
        clazz.newInstance().asInstanceOf[InternalManipulator]
      } catch {
        case _: Throwable => new SampleInternalManipulator()
      }
      startMans += (new InternalAIPlayerStartManipulator(players(iPlayers), man)
        .limittingTime(10000))
      gameMans += new InternalAIPlayerGameManipulator(players(iPlayers), man)
        .limittingSumTime(1000, 5000)
      iPlayers += 1
    }

    val random = new Random()
    val field = Field(6, players, random)
    env.game = new Game(field, players, 200)
    if (userIndices.size == 0) {
      env.getSceneManager().setFps(1000)
    } else {
      env.getSceneManager().setFps(30)
    }

    val oos = StreamUtils.openStreamForJava(calendar, random)
    (Vector(startMans.map(_.recordingStream(oos)).map(_.threading()): _*),
      Vector(gameMans.map(_.recordingStream(oos)).map(_.threading()): _*))
  }

  def initializeComponents() = {
    val builder = new GameGuiBuilder()
    val window = new JFrame()
    val mainPanel = new JPanel()
    val layout = new SpringLayout()
    val logArea = new JTextArea()
    val logScrollPane = new JScrollPane(logArea)
    val commandField = new JTextField()

    val ret = builder.setTitle("Terraforming version 1.0.5")
      .setWindowSize(1024, 740)
      .setPanelSize(1024, 495)
      .setWindowCreator(new WindowCreator() {
        override def createWindow(gamePanel: JGamePanel) = {
          logArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12))
          logArea.setEditable(false)
          AbstractScene.display = (text) => {
            logArea.append(text)
            logArea.setCaretPosition(logArea.getText().length())
          }

          logScrollPane.setPreferredSize(new Dimension(0, 0))

          commandField.setPreferredSize(new Dimension(0, 20))
          commandField.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12))
          commandField.addActionListener(new ActionListener() {
            def actionPerformed(e: ActionEvent) = {
              val command = commandField.getText()
              commandField.setText("")
              TextBoxUtils.addCommand(command)
            }
          })

          mainPanel.setLayout(layout)
          mainPanel.add(gamePanel)
          mainPanel.add(logScrollPane)
          mainPanel.add(commandField)

          // Layout components
          layout.putConstraint(SpringLayout.NORTH, gamePanel, 0, SpringLayout.NORTH, mainPanel)
          layout.putConstraint(SpringLayout.NORTH, logScrollPane, 0, SpringLayout.SOUTH, gamePanel)
          layout.putConstraint(SpringLayout.SOUTH, logScrollPane, 0, SpringLayout.NORTH, commandField)
          layout.putConstraint(SpringLayout.SOUTH, commandField, 0, SpringLayout.SOUTH, mainPanel)
          layout.putConstraint(SpringLayout.WEST, logScrollPane, 0, SpringLayout.WEST, mainPanel)
          layout.putConstraint(SpringLayout.WEST, commandField, 0, SpringLayout.WEST, mainPanel)
          layout.putConstraint(SpringLayout.EAST, logScrollPane, 0, SpringLayout.EAST, mainPanel)
          layout.putConstraint(SpringLayout.EAST, commandField, 0, SpringLayout.EAST, mainPanel)

          window.getContentPane().add(mainPanel)
          window
        }
      })
      .buildForGui(classOf[GameEnvironment])

    val env = ret.getEnvironment()
    val gamePanel = ret.getPanel()
    ImageLoader.prefetch(env.getRenderer)
    initializeListener(gamePanel, env, commandField, logArea)
    commandField.requestFocus()

    (window, env)
  }

  def initializeListener(gamePanel: JPanel, env: GameEnvironment, commandField: JTextField, logArea: JTextArea) {
    gamePanel.addMouseListener(new MouseAdapter() {
      override def mouseClicked(e: MouseEvent) = {
        if (env.game != null) {
          val cp = new OrthogonalPoint(e.getPoint().x, e.getPoint().y)
          val square = OrthogonalPoint.orthogonalPointToPoints(cp, env.game.field)
          square.headOption match {
            case Some(p) =>
              AbstractScene.display("Your clicked location is ( " + p.x + ", " + p.y + " )")
              AbstractScene.display(Defaults.NEW_LINE)
              if (e.getButton() == MouseEvent.BUTTON3) { // BUTTON3 indicates the right button
                commandField.setText(commandField.getText + " " + p.x + " " + p.y + " ")
              }
            case None =>
          }
        }
        commandField.requestFocus()
      }
    })
    val memorizer = new AwtKeyMemorizer()
    gamePanel.addKeyListener(memorizer)
    logArea.addKeyListener(memorizer)
    commandField.addKeyListener(memorizer)
    env.getInputer().add(0, memorizer.getKeyPressChecker(KeyEvent.VK_ENTER))
  }
}

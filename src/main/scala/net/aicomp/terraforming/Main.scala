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
import net.exkazuu.gameaiarena.player.ExternalComputerPlayer

object Main {

  val HELP = "h"
  val CUI_MODE = "c"
  val AI_PROGRAM = "a"
  val INTERNAL_AI_PROGRAM = "i"
  val NOT_SHOWING_LOG = "n"
  var logFunction: String => Unit = null

  def log(text: String) = logFunction(text)

  def printHelp(options: Options) {
    val help = new HelpFormatter()
    help.printHelp(
      "java -jar Terraforming.jar [OPTIONS]\n"
        + "[OPTIONS]: ", "", options, "", true)
  }

  def main(args: Array[String]) {
    OptionBuilder.hasArgs(3)
    OptionBuilder.withDescription("Set three AI programs.")
    val opt1 = OptionBuilder.create(AI_PROGRAM)

    OptionBuilder.hasArgs(3)
    OptionBuilder.withDescription("Set three internal AI programs.")
    val opt2 = OptionBuilder.create(INTERNAL_AI_PROGRAM)

    val options = new Options()
      .addOption(HELP, false, "Print this help.")
      .addOption(CUI_MODE, false, "Enable CUI mode.")
      .addOption(NOT_SHOWING_LOG, false, "Disable showing logs in the scree.")
      .addOption(opt1)
      .addOption(opt2)

    try {
      val parser = new BasicParser()
      val cl = parser.parse(options, args)
      if (cl.hasOption(HELP)) {
        printHelp(options)
      } else {
        startGame(options, cl)
      }
    } catch {
      case e: ParseException => {
        System.err.println("Error: " + e.getMessage())
        printHelp(options)
        System.exit(-1)
      }
    }
  }

  def startGame(options: Options, cl: CommandLine) {
    // Must not apply limittingTime/limittingSumTime to user manipulators
    val random = new Random()
    val calendar = Calendar.getInstance
    val oos = StreamUtils.openStreamForJava(calendar, random)
    val logStream = StreamUtils.openStreamForLogging(calendar, "log")

    val nums = Vector((0 to 2): _*)
    val players = nums.map(new Player(_))

    val startAndGameMans = if (cl.hasOption(AI_PROGRAM) && cl.getOptionValues(AI_PROGRAM).length == 3) {
      Some(nums.map(i => {
        val cmds = cl.getOptionValues(AI_PROGRAM)
        val com = new ExternalComputerPlayer(cmds(i).split(" "))
        val out = StreamUtils.openStreamForLogging(calendar, "stdout_player" + i)
        val err = StreamUtils.openStreamForLogging(calendar, "stderr_player" + i)
        com.addStreamForLoggingStdoutOfExternalProgram(out)
        com.addStreamForLoggingErrorOfExternalProgram(err)
        com.addStreamForLoggingErrorOfExternalProgram(System.err)
        (new AIPlayerStartManipulator(players(i), com),
          new AIPlayerGameManipulator(players(i), com))
      }))
    } else if (cl.hasOption(INTERNAL_AI_PROGRAM) && cl.getOptionValues(INTERNAL_AI_PROGRAM).length == 3) {
      Some(nums.map(i => {
        val cmds = cl.getOptionValues(INTERNAL_AI_PROGRAM)
        val clazz = Class.forName(cmds(i))
        val man = clazz.newInstance().asInstanceOf[InternalManipulator]
        (new InternalAIPlayerStartManipulator(players(i), man),
          new InternalAIPlayerGameManipulator(players(i), man))
      }))
    } else {
      None
    }

    def initializeEnvironment(env: GameEnvironment, userStartManipulator: StartManipulator, userGameManipulator: GameManipulator) = {
      val startManipulators = (startAndGameMans match {
        case Some(startAndGameMans) => startAndGameMans.map { case (man, _) => man }
          .map(_.limittingTime(10000))
        case None => nums.map(_ => userStartManipulator)
      }).map(_.recordingStream(oos))
        .map(_.threading())
      val gameManipulators = (startAndGameMans match {
        case Some(startAndGameMans) => startAndGameMans.map { case (_, man) => man }
          .map(_.limittingSumTime(1000, 5000))
        case None => nums.map(_ => userGameManipulator)
      }).map(_.recordingStream(oos))
        .map(_.threading())

      val field = Field(6, players, random)
      env.game = new Game(field, players, 200)
      if (startAndGameMans.isDefined) {
        env.getSceneManager().setFps(1000)
      }

      (startManipulators, gameManipulators)
    }

    val (env, startScene) = if (cl.hasOption(CUI_MODE)) {
      val env = GameEnvironment()
      env.getSceneManager().setFps(1000)
      val scanner = new Scanner(System.in)
      AbstractScene.display = println
      val (startManipulators, gameManipulators) =
        initializeEnvironment(env, new ConsoleUserStartManipulator(scanner), new ConsoleUserGameManipulator(scanner))

      val resultScene = new ResultScene(null)
      val mainScene = new MainScene(resultScene, gameManipulators)
      (env, new PlayerScene(mainScene, startManipulators))
    } else {
      val (window, env) = initializeComponents()
      val (startManipulators, gameManipulators) =
        initializeEnvironment(env, new GraphicalUserStartManipulator(), new GraphicalUserGameManipulator())

      val waitScene = new WaitingScene(null) with GraphicalScene
      val resultScene = new ResultScene(waitScene) with GraphicalScene
      val mainScene = new MainScene(resultScene, gameManipulators) with GraphicalScene
      (env, new PlayerScene(mainScene, startManipulators) with TitleScene)
    }
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
    env.start(startScene)
  }

  def initializeComponents() = {
    val builder = new GameGuiBuilder()
    val window = new JFrame()
    val mainPanel = new JPanel()
    val layout = new SpringLayout()
    val logArea = new JTextArea()
    val logScrollPane = new JScrollPane(logArea)
    val commandField = new JTextField()

    val ret = builder.setTitle("Terraforming")
      .setWindowSize(1024, 740)
      .setPanelSize(1024, 495)
      .setFps(5)
      .setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE)
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

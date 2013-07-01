package net.aicomp

import java.awt.Dimension
import java.awt.Font
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import java.awt.event.KeyEvent
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import java.util.Scanner
import scala.Array.canBuildFrom
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
import net.aicomp.entity.Field
import net.aicomp.entity.Game
import net.aicomp.entity.GameEnvironment
import net.aicomp.entity.OrthogonalPoint
import net.aicomp.entity.Player
import net.aicomp.scene.MainScene
import net.aicomp.scene.PlayerScene
import net.aicomp.scene.console.ConsoleScene
import net.aicomp.scene.graphic.GraphicalScene
import net.aicomp.scene.graphic.TextBoxScene
import net.aicomp.scene.graphic.TitleScene
import net.aicomp.util.misc.ImageLoader
import net.aicomp.util.settings.Defaults
import net.exkazuu.gameaiarena.gui.JGamePanel
import net.exkazuu.gameaiarena.gui.builder.GameGuiBuilder
import net.exkazuu.gameaiarena.gui.builder.WindowCreator
import net.exkazuu.gameaiarena.key.AwtKeyMemorizer
import net.exkazuu.gameaiarena.player.ExternalComputerPlayer
import net.aicomp.manipulator.AIPlayerStartManipulator
import net.aicomp.manipulator.GraphicalUserStartManipulator
import net.aicomp.manipulator.StartManipulator
import net.aicomp.manipulator.AIPlayerGameManipulator
import net.aicomp.manipulator.ConsoleUserGameManipulator
import net.aicomp.manipulator.GameManipulator
import net.aicomp.manipulator.ConsoleUserStartManipulator
import net.aicomp.manipulator.GraphicalUserGameManipulator

object Main {

  val HELP = "h"
  val CUI_MODE = "c"
  val AI_PROGRAM = "a"
  var logFunction: String => Unit = null

  def log(text: String) = logFunction(text)

  def printHelp(options: Options) {
    val help = new HelpFormatter();
    help.printHelp(
      "java -jar Terraforming-1.0.0.jar [OPTIONS]\n"
        + "[OPTIONS]: ", "", options, "", true);
  }

  def main(args: Array[String]) {
    OptionBuilder.hasArgs(3)
    OptionBuilder.withDescription("Set three AI programs.")
    val opt = OptionBuilder.create(AI_PROGRAM)

    val options = new Options()
      .addOption(HELP, false, "Print this help.")
      .addOption(CUI_MODE, false, "Enable CUI mode.")
      .addOption(opt)

    try {
      val parser = new BasicParser();
      val cl = parser.parse(options, args);
      if (cl.hasOption(HELP)) {
        printHelp(options);
      } else {
        startGame(options, cl);
      }
    } catch {
      case e: ParseException => {
        System.err.println("Error: " + e.getMessage());
        printHelp(options);
        System.exit(-1);
      }
    }
  }

  def startGame(options: Options, cl: CommandLine) {
    def initializeEnvironment(env: GameEnvironment, userStartManipulator: StartManipulator, userGameManipulator: GameManipulator) = {
      val cmds = cl.getOptionValues(AI_PROGRAM)
      val nums = Range(0, 3).toArray
      val coms = if (cl.hasOption(AI_PROGRAM) && cmds.length == 3) {
        Some(nums.map(i => new ExternalComputerPlayer(cmds(i).split(" "))))
      } else {
        None
      }

      val startManipulators = (coms match {
        case Some(coms) => nums.map(i => new AIPlayerStartManipulator(i, coms(i)))
          .map(_.limittingTime(10000))
        case None => nums.map(_ => userStartManipulator)
      })
      val gameManipulators = (coms match {
        case Some(coms) => nums.map(i => new AIPlayerGameManipulator(i, coms(i)))
          .map(_.limittingSumTime(1000, 5000))
        case None => nums.map(_ => userGameManipulator)
      })

      val players = Vector(nums.map(i => new Player(i, startManipulators(i), gameManipulators(i))): _*)
      val field = Field(7, players)
      env.game = new Game(field, players, 2 * 3)
    }

    if (cl.hasOption(CUI_MODE)) {
      val env = GameEnvironment()
      env.getSceneManager().setFps(1000)
      val scanner = new Scanner(System.in)
      initializeEnvironment(env, new ConsoleUserStartManipulator(scanner), new ConsoleUserGameManipulator(scanner))

      val mainScene = new MainScene(null) with ConsoleScene
      val playerScene = new PlayerScene(mainScene) with ConsoleScene
      env.start(playerScene)
    } else {
      val (window, env) = initializeComponents()
      initializeEnvironment(env, new GraphicalUserStartManipulator(), new GraphicalUserGameManipulator())

      val mainScene = new MainScene(null) with GraphicalScene with TextBoxScene
      val playerScene = new PlayerScene(mainScene) with TitleScene with TextBoxScene
      env.start(playerScene)

      window.dispose();
    }
  }

  def initializeComponents() = {
    val builder = new GameGuiBuilder()
    val window = new JFrame()
    val mainPanel = new JPanel()
    val layout = new SpringLayout();
    val logArea = new JTextArea();
    val logScrollPane = new JScrollPane(logArea)
    val commandField = new JTextField();

    val ret = builder.setTitle("Terraforming")
      .setWindowSize(1024, 740)
      .setPanelSize(1024, 495)
      .setFps(5)
      .setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE)
      .setWindowCreator(new WindowCreator() {
        override def createWindow(gamePanel: JGamePanel) = {
          logArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
          logArea.setEditable(false)
          TextBoxScene.display = (text) => {
            logArea.append(text)
            logArea.setCaretPosition(logArea.getText().length())
          }

          logScrollPane.setPreferredSize(new Dimension(0, 0))

          commandField.setPreferredSize(new Dimension(0, 20))
          commandField.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
          commandField.addActionListener(new ActionListener() {
            def actionPerformed(e: ActionEvent) = {
              val command = commandField.getText()
              commandField.setText("")
              TextBoxScene.addCommand(command)
            }
          })

          mainPanel.setLayout(layout)
          mainPanel.add(gamePanel);
          mainPanel.add(logScrollPane);
          mainPanel.add(commandField);

          // Layout components
          layout.putConstraint(SpringLayout.NORTH, gamePanel, 0, SpringLayout.NORTH, mainPanel);
          layout.putConstraint(SpringLayout.NORTH, logScrollPane, 0, SpringLayout.SOUTH, gamePanel);
          layout.putConstraint(SpringLayout.SOUTH, logScrollPane, 0, SpringLayout.NORTH, commandField);
          layout.putConstraint(SpringLayout.SOUTH, commandField, 0, SpringLayout.SOUTH, mainPanel);
          layout.putConstraint(SpringLayout.WEST, logScrollPane, 0, SpringLayout.WEST, mainPanel);
          layout.putConstraint(SpringLayout.WEST, commandField, 0, SpringLayout.WEST, mainPanel);
          layout.putConstraint(SpringLayout.EAST, logScrollPane, 0, SpringLayout.EAST, mainPanel);
          layout.putConstraint(SpringLayout.EAST, commandField, 0, SpringLayout.EAST, mainPanel);

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
          if (square.nonEmpty) {
            TextBoxScene.display("Your clicked location is ( " + square.head.x + ", " + square.head.y + " )")
            TextBoxScene.display(Defaults.NEW_LINE)
          }
        }
      }
    });
    val memorizer = new AwtKeyMemorizer()
    gamePanel.addKeyListener(memorizer)
    logArea.addKeyListener(memorizer);
    commandField.addKeyListener(memorizer)
    env.getInputer().add(0, memorizer.getKeyPressChecker(KeyEvent.VK_ENTER))
  }
}

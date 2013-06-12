package net.aicomp

import java.awt.Dimension
import java.awt.Font
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import java.awt.event.KeyEvent
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import org.apache.commons.cli.BasicParser
import org.apache.commons.cli.CommandLine
import org.apache.commons.cli.HelpFormatter
import org.apache.commons.cli.Options
import org.apache.commons.cli.ParseException
import javax.swing.JFrame
import javax.swing.JPanel
import javax.swing.JScrollPane
import javax.swing.JTextArea
import javax.swing.JTextField
import javax.swing.SpringLayout
import jp.ac.waseda.cs.washi.gameaiarena.gui.GamePanels
import jp.ac.waseda.cs.washi.gameaiarena.key.AwtKeyMemorizer
import net.aicomp.entity.GameEnvironment
import net.aicomp.entity.GameSetting
import net.aicomp.scene.MainScene
import net.aicomp.scene.PlayerScene
import net.aicomp.scene.console.ConsoleScene
import net.aicomp.scene.graphic.TextBoxScene
import net.aicomp.scene.graphic.TitleScene
import net.aicomp.util.misc.ImageLoader
import net.aicomp.scene.graphic.WhiteScene
import net.aicomp.entity.Point
import net.aicomp.entity.Field
import net.aicomp.entity.OrthogonalPoint
import net.aicomp.util.settings.Defaults

object Main {

  val HELP = "h"
  val CUI_MODE = "c"
  val LARGE_MODE = "l"
  var logFunction: String => Unit = null

  def log(text: String) = logFunction(text)

  def printHelp(options: Options) {
    val help = new HelpFormatter();
    help.printHelp(
      "java -jar JavaChallenge2012-X.X.X.jar [OPTIONS]\n"
        + "[OPTIONS]: ", "", options, "", true);
  }

  def main(args: Array[String]) {
    val options = new Options()
      .addOption(HELP, false, "Print this help.")
      .addOption(CUI_MODE, false, "Enable CUI mode instead of GUI mode.")
      .addOption(LARGE_MODE, false, "Enable GUI mode with large images.")
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

  def startConsoleGame() = {
    val env = GameEnvironment()
    env.getSceneManager().setFps(1000)
    val mainScene = new MainScene(null) with ConsoleScene
    val playerScene = new PlayerScene(mainScene) with ConsoleScene
    env.start(playerScene)
  }

  def startGame(options: Options, cl: CommandLine) {
    if (cl.hasOption(CUI_MODE)) {
      startConsoleGame()
    } else {
      val (window, env) = initializeComponents(cl.hasOption(LARGE_MODE))
      val setting = GameSetting()

      val mainScene = new MainScene(null) with WhiteScene with TextBoxScene
      val playerScene = new PlayerScene(mainScene, setting) with TitleScene with TextBoxScene

      val f: Function1[String, Unit] = mainScene.displayCore(_)

      env.start(playerScene)

      window.dispose()
    }
  }

  def initializeComponents(isLargeMode: Boolean) = {
    // TODO: Use scala.swing package instead of javax.swing package

    // Initialize layout components
    val mainPanel = new JPanel()
    val layout = new SpringLayout();
    mainPanel.setLayout(layout)

    // Initialize each component
    val window = new JFrame()
    window.setTitle("JavaChallenge2012")
    val gamePanel = GamePanels.newWithDefaultImage()
    if (isLargeMode) {
      window.setSize(1280, 1000)
      gamePanel.setPreferredSize(new Dimension(1280, 720))
    } else {
      window.setSize(1024, 740)
      gamePanel.setPreferredSize(new Dimension(1024, 495))
    }
    mainPanel.add(gamePanel);
    val logArea = new JTextArea();
    val logScrollPane = new JScrollPane(logArea)
    logScrollPane.setPreferredSize(new Dimension(0, 0))
    mainPanel.add(logScrollPane);
    logArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
    logArea.setEditable(false)
    val commandField = new JTextField();
    commandField.setPreferredSize(new Dimension(0, 20))
    commandField.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
    commandField.addActionListener(new ActionListener() {
      def actionPerformed(e: ActionEvent) = {
        val command = commandField.getText()
        commandField.setText("")
        TextBoxScene.addCommand(command)
      }
    })
    TextBoxScene.display = (text) => {
      logArea.append(text)
      logArea.setCaretPosition(logArea.getText().length())
    }
    mainPanel.add(commandField);

    // Layout compo6nents
    layout.putConstraint(SpringLayout.NORTH, gamePanel, 0, SpringLayout.NORTH, mainPanel);
    layout.putConstraint(SpringLayout.NORTH, logScrollPane, 0, SpringLayout.SOUTH, gamePanel);
    layout.putConstraint(SpringLayout.SOUTH, logScrollPane, 0, SpringLayout.NORTH, commandField);
    layout.putConstraint(SpringLayout.SOUTH, commandField, 0, SpringLayout.SOUTH, mainPanel);
    layout.putConstraint(SpringLayout.WEST, logScrollPane, 0, SpringLayout.WEST, mainPanel);
    layout.putConstraint(SpringLayout.WEST, commandField, 0, SpringLayout.WEST, mainPanel);
    layout.putConstraint(SpringLayout.EAST, logScrollPane, 0, SpringLayout.EAST, mainPanel);
    layout.putConstraint(SpringLayout.EAST, commandField, 0, SpringLayout.EAST, mainPanel);

    window.getContentPane().add(mainPanel)
    //window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE)

    // Show the window
    window.setVisible(true);
    gamePanel.initializeAfterShowing()

    val env = GameEnvironment(gamePanel)
    ImageLoader.prefetch(env.getRenderer)
    env.getSceneManager.addWindowListenerForTerminating(window);
    env.getSceneManager().setFps(5)
      .addWindowListenerForTerminating(window)
    initializeListener(gamePanel, env, commandField, logArea)

    commandField.requestFocus()

    (window, env)
  }

  def initializeListener(gamePanel: JPanel, env: GameEnvironment, commandField: JTextField, logArea: JTextArea) {
    gamePanel.addMouseListener(new MouseAdapter() {
      override def mouseClicked(e: MouseEvent) = {
        if (env.game != null) {
          val pointSize = 32
          val cp = new Point(e.getPoint().x, e.getPoint().y)
          val square = OrthogonalPoint.getClickedPoint(cp.x, cp.y)
          TextBoxScene.display("Your clicked location is ( " + square.head.x + ", " + square.head.y + " )")
          TextBoxScene.display(Defaults.NEW_LINE)
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

package net.aicomp.util.misc;

import java.awt.Image

import scala.collection.Seq
import scala.collection.mutable

import jp.ac.waseda.cs.washi.gameaiarena.gui.Renderer

/**
 * 画像の読み込み処理を行うクラスです。
 */
object ImageLoader {
  /**
   * 全ての画像を読み込みます。
   *
   * @param render {@link Renderer}
   */
  def prefetch(render: Renderer) = {
    val fetchedImages = mutable.ListBuffer[Image]()
    for (val method <- ImageLoader.getClass().getMethods()) {
      if (method.getName().startsWith("load")) {
        fetchedImages ++= dig(method.invoke(this, render))
      }
    }
    fetchedImages.toList
  }

  def dig(obj: Any): Seq[Image] = {
    obj match {
      case m: Map[_, _] => m.values.toSeq.flatten(dig(_))
      case s: Seq[_] => s.flatten(dig(_))
      case i: Image => Seq(i)
    }
  }

  private var _backgrounds: Map[Int, Image] = null

  def loadBackgrounds(render: Renderer) = {
    if (_backgrounds == null) {
      _backgrounds = Map(
        32 -> render.loadImage("img/map32.png"),
        48 -> render.loadImage("img/map48.png"))
    }
    _backgrounds
  }

  private var _tiles: Map[Int, Image] = null

  def loadTiles(render: Renderer) = {
    if (_tiles == null) {
      _tiles = Map(
        32 -> render.loadImage("img/hex32.png"),
        48 -> render.loadImage("img/hex48.png"))
    }
    _tiles
  }

  private var _roundTitle: Image = null

  def loadRoundTitle(renderer: Renderer) = {
    if (_roundTitle == null) {
      _roundTitle = renderer.loadImage("img/round/round.png")
    }
    _roundTitle
  }

  private var _roundSlash: Image = null

  def loadRoundSlash(renderer: Renderer) = {
    if (_roundSlash == null) {
      _roundSlash = renderer.loadImage("img/round/rnslash.png")
    }
    _roundSlash
  }

  private var _roundNumber: Map[Int, Image] = null

  def loadRoundNumber(render: Renderer) = {
    if (_roundNumber == null) {
      _roundNumber = Range(0, 10).map(num =>
        (num, render.loadImage(
          "img/round/rn" + num.toString + ".png"))).toMap
    }
    _roundNumber
  }

  private var _buyIcon: Image = null
  def loadBuyIcon(render: Renderer) = {
    if (_buyIcon == null) {
      _buyIcon = render.loadImage("img/playerInfo/buy.png")
    }
    _buyIcon
  }

  private var _sellIcon: Image = null
  def loadSellIcon(render: Renderer) = {
    if (_sellIcon == null) {
      _sellIcon = render.loadImage("img/playerInfo/sell.png")
    }
    _sellIcon
  }

  /** プレイヤーインデックス（中立は-1） => 画像の辞書 */
  private val _playerIndices = (-1 to 5)
  private var _veins: Map[Int, Image] = null

  def loadVeins(render: Renderer) = {
    if (_veins == null) {
      _veins =
        _playerIndices
          .map { t => (t, render.loadImage("img/vein" + t + ".png")) }
          .toMap
    }
    _veins
  }

  private var _plusMarks: Map[Int, Image] = null
  def loadPlusMarks(render: Renderer) = {
    if (_plusMarks == null) {
      _plusMarks =
        _playerIndices
          .map { t => (t, render.loadImage("img/plus" + t + ".png")) }
          .toMap
    }
    _plusMarks
  }

  private var _robots: Map[Int, Image] = null
  def loadRobots(render: Renderer) = {
    if (_robots == null) {
      _robots =
        _playerIndices
          .filter { _ >= 0 }
          .map { t => (t, render.loadImage("img/robot" + t + ".png")) }
          .toMap
    }
    _robots
  }

  private var _numbers: Map[(Int, Int), Image] = null
  def loadNumbers(render: Renderer) = {
    if (_numbers == null) {
      _numbers =
        _playerIndices
          .flatMap {
            p =>
              (0 to 9).map {
                n => ((p, n), render.loadImage("img/number/num" + p + n + ".png"))
              }
          }
          .toMap
    }
    _numbers
  }

  private var _playerInformationBackgrounds: Map[Int, Image] = null
  def loadPlayerInformationBackgrounds(render: Renderer) = {
    if (_playerInformationBackgrounds == null) {
      _playerInformationBackgrounds =
        _playerIndices
          .filter { _ >= 0 }
          .map { t => (t, render.loadImage("img/playerInfo/p" + t + ".png")) }
          .toMap
    }
    _playerInformationBackgrounds
  }

  private var _bankInformationBackground: Image = null
  def loadBankInformationBackground(render: Renderer) = {
    if (_bankInformationBackground == null) {
      _bankInformationBackground = render.loadImage("img/bank.png")
    }
    _bankInformationBackground
  }

  private var _playerHighlightFrame: Image = null
  def loadPlayerHighlightFrame(render: Renderer) = {
    if (_playerHighlightFrame == null) {
      _playerHighlightFrame = render.loadImage("img/pactive.png")
    }
    _playerHighlightFrame
  }

  private var _activePlayerStar: Image = null
  def loadActivePlayerStar(render: Renderer) = {
    if (_activePlayerStar == null) {
      _activePlayerStar = render.loadImage("img/star.png")
    }
    _activePlayerStar
  }

  private var _veinSample: Image = null
  def loadVeinSample(render: Renderer) = {
    if (_veinSample == null) {
      _veinSample = render.loadImage("img/veinSample.png")
    }
    _veinSample
  }

  private var _resultBackground: Image = null
  def loadResultBackground(render: Renderer) = {
    if (_resultBackground == null) {
      _resultBackground = render.loadImage("img/result/result_BG.png")
    }
    _resultBackground
  }

  private var _resultNumber: Map[Int, Map[Int, Image]] = null
  def loadResultNumber(render: Renderer) = {
    if (_resultNumber == null) {
      _resultNumber = Map(
        48 ->
          Range(0, 10).map(num =>
            (num, render.loadImage("img/result/48_" + num.toString + ".png"))).toMap,
        64 ->
          Range(0, 10).map(num =>
            (num, render.loadImage("img/result/64_" + num.toString + ".png"))).toMap)
    }
    _resultNumber
  }

  private var _lamp: Map[Int, Image] = null
  def loadLamp(render: Renderer) = {
    if (_lamp == null) {
      _lamp = Range(0, 6).map(num =>
        (num, render.loadImage("img/result/lamp_" + num.toString + ".png"))).toMap
    }
    _lamp
  }

  private var _title: Image = null
  def loadTitle(render: Renderer) = {
    if (_title == null) {
      _title = render.loadImage("img/start/title.png")
    }
    _title
  }

}

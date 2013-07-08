package net.aicomp.terraforming.util.misc;

import java.awt.Image

import scala.collection.Seq
import scala.collection.mutable

import net.exkazuu.gameaiarena.gui.Renderer

object ImageLoader {
  def prefetch(render: Renderer) = {
    val fetchedImages = mutable.ListBuffer[Image]()
    for (method <- ImageLoader.getClass().getMethods()) {
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

  private var _background: Image = null

  def loadBackground(render: Renderer) = {
    if (_background == null) {
      _background = render.loadImage("img/map.png")
    }
    _background
  }

  private var _tiles: Map[String, Image] = null

  def loadTiles(render: Renderer) = {
    if (_tiles == null) {
      _tiles = Map(
        "32" -> render.loadImage("img/hex/hex32.png"),
        "32_0" -> render.loadImage("img/hex/hex32_0.png"),
        "32_1" -> render.loadImage("img/hex/hex32_1.png"),
        "32_2" -> render.loadImage("img/hex/hex32_2.png"),
        "48" -> render.loadImage("img/hex/hex48.png"),
        "48_0" -> render.loadImage("img/hex/hex48_0.png"),
        "48_1" -> render.loadImage("img/hex/hex48_1.png"),
        "48_2" -> render.loadImage("img/hex/hex48_2.png"))
    }
    _tiles
  }
  
  private var _installations: Map[String, Image] = null
  def loadInstallations(render: Renderer) = {
    if (_installations == null){
      _installations = Map(
          "hole" -> render.loadImage("img/installation/hole.png"),
          "installation_0" -> render.loadImage("img/installation/installation0.png"),
          "installation_1" -> render.loadImage("img/installation/installation1.png"),
          "installation_2" -> render.loadImage("img/installation/installation2.png"),
          "initial_0" -> render.loadImage("img/installation/largefactory0.png"),
          "initial_1" -> render.loadImage("img/installation/largefactory1.png"),
          "initial_2" -> render.loadImage("img/installation/largefactory2.png"),
          "factory_0" -> render.loadImage("img/installation/factory0.png"),
          "factory_1" -> render.loadImage("img/installation/factory1.png"),
          "factory_2" -> render.loadImage("img/installation/factory2.png"),
          "pit_0" -> render.loadImage("img/installation/pit0.png"),
          "pit_1" -> render.loadImage("img/installation/pit1.png"),
          "pit_2" -> render.loadImage("img/installation/pit2.png"),
          "attack_0" -> render.loadImage("img/installation/attack0.png"),
          "attack_1" -> render.loadImage("img/installation/attack1.png"),
          "attack_2" -> render.loadImage("img/installation/attack2.png"),
          "shield_0" -> render.loadImage("img/installation/shield0.png"),
          "shield_1" -> render.loadImage("img/installation/shield1.png"),
          "shield_2" -> render.loadImage("img/installation/shield2.png"),
          "bridge_0" -> render.loadImage("img/installation/bridge0.png"),
          "bridge_1" -> render.loadImage("img/installation/bridge1.png"),
          "bridge_2" -> render.loadImage("img/installation/bridge2.png"),
          "house_0" -> render.loadImage("img/installation/house0.png"),
          "house_1" -> render.loadImage("img/installation/house1.png"),
          "house_2" -> render.loadImage("img/installation/house2.png"),
          "town_0" -> render.loadImage("img/installation/town0.png"),
          "town_1" -> render.loadImage("img/installation/town1.png"),
          "town_2" -> render.loadImage("img/installation/town2.png"),
          "city_0" -> render.loadImage("img/installation/city0.png"),
          "city_1" -> render.loadImage("img/installation/city1.png"),
          "city_2" -> render.loadImage("img/installation/city2.png")
          )
    }
    _installations
  }

    private var _statusInstallations: Map[String, Image] = null
  def loadStatusInstallations(render: Renderer) = {
    if (_statusInstallations == null){
      _statusInstallations = Map(
          "town_0" -> render.loadImage("img/playerInfo/town0.png"),
          "town_1" -> render.loadImage("img/playerInfo/town1.png"),
          "town_2" -> render.loadImage("img/playerInfo/town2.png")
          )
    }
    _statusInstallations
  }
  
  private var _roundParts: Map[String, Image] = null

  def loadRoundParts(renderer: Renderer) = {
    if (_roundParts == null) {
      _roundParts = Map("Title" -> renderer.loadImage("img/round/round.png"),
        "Slash" -> renderer.loadImage("img/round/rnslash.png"))
    }
    _roundParts
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

  private val _playerIndices = (-1 to 2)

  private var _robots: Map[Int, Image] = null
  def loadRobots(render: Renderer) = {
    if (_robots == null) {
      _robots =
        _playerIndices
          .filter { _ >= 0 }
          .map { t => (t, render.loadImage("img/robot/robot" + t + ".png")) }
          .toMap
    }
    _robots
  }

  private var _largeRobots: Map[Int, Image] = null
  def loadLargeRobots(render: Renderer) = {
    if (_largeRobots == null) {
      _largeRobots =
        _playerIndices
          .filter { _ >= 0 }
          .map { t => (t, render.loadImage("img/robot/lrobot" + t + ".png")) }
          .toMap
    }
    _largeRobots
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

  private var _statusNumbers: Map[(Int, Int), Image] = null
  def loadStatusNumbers(render: Renderer) = {
    if (_statusNumbers == null) {
      _statusNumbers =
        _playerIndices
          .flatMap {
            p =>
              (0 to 9).map {
                n => ((p, n), render.loadImage("img/lnumber/num" + p + n + ".png"))
              }
          }
          .toMap
    }
    _statusNumbers
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

  private var _playerHighlightFrame: Image = null
  def loadPlayerHighlightFrame(render: Renderer) = {
    if (_playerHighlightFrame == null) {
      _playerHighlightFrame = render.loadImage("img/pactive.png")
    }
    _playerHighlightFrame
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

  private var _title: Image = null
  def loadTitle(render: Renderer) = {
    if (_title == null) {
      _title = render.loadImage("img/start/title.jpg")
    }
    _title
  }
}

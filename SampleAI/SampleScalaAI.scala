import util.parsing.combinator._

case class GameInformation(currentTurn: Int, maxTurn: Int, playerId: Int)
case class FieldInformation(radius: Int, tileSize: Int)
case class TileInformation(x: Int, y: Int, ownerId: Int, robots: Int, obj: String)

case class Informations(gameInformation: GameInformation,
                        fieldInformation: FieldInformation,
                        tileInformations: List[TileInformation])

object StringifyParser extends RegexParsers {
  val num = "-?[0-9]+".r ^^ { _.toInt }
  val str = "[a-zA-Z]+".r
  val eol = "\n"

  val tileStringify = num ~ num ~ num ~ num ~ str ^^ {
    case x ~ y ~ ownerId ~ robots ~ obj =>
      new TileInformation(x, y, ownerId, robots, obj)
  }

  val gameStringify = num ~ num ~ num ^^ {
    case currentTurn ~ maxTurn ~ playerId =>
      new GameInformation(currentTurn, maxTurn, playerId)
  }

  val fieldStringify = num ~ num ^^ {
    case radius ~ tileSize =>
      new FieldInformation(radius, tileSize)
  }

  val commands = gameStringify ~
                 fieldStringify ~
                 rep(tileStringify) ^^ {
    case game ~ field ~ tiles =>
      new Informations(game, field, tiles)
  }

  def parse(input: String) = parseAll(commands, input)
}

object SampleScalaAI {
  def main(args: Array[String]) {
    var line = ""

    System.out.println("Scala")
    while ({ line = readLine(); line ne null }) {
      if (line != "START") Iterator.continually(readLine()).takeWhile(_ != "START")
      val commands = Iterator.continually(readLine()).takeWhile(_ != "EOS").toList
      val informations = StringifyParser.parse(commands.mkString("\n"))

      System.out.println("finish")
    }
  }
}

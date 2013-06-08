package net.aicomp.entity

sealed trait Command

case class MoveCommand(val from: Point, val direction: Direction)
  extends Command

case class BuildCommand(val at: Point, val building: String) extends Command

case class FinishCommand() extends Command

/**
 * Command object only do validation of format.
 * Game.processCommand must validate the semantics of commands.
 */
object Command extends FormatValidation {
  // TODO: def parse(string: String): Command
  // TODO: def parse(elems: List[String]): Command

  def moveCommand(args: List[String]): MoveCommand = {
    describe("move") {
      args.shouldHaveLength(3)
      args(0).shouldBeInt
      args(1).shouldBeInt
      args(2).shouldBeIn("r", "ur", "dr", "l", "ul", "dl")
    }

    val x = args(0).toInt
    val y = args(1).toInt
    val d = args(2)
    MoveCommand(new Point(x, y), Direction.fromString(d))
  }

  //TODO
  def buildCommand(args: List[String]): BuildCommand = {
    describe("build") {
      args.shouldHaveLength(3)
      args(0).shouldBeInt
      args(1).shouldBeInt
      args(2).shouldBeIn("br", "sh", "at", "mt", "pk", "sq", "pl")
    }

    val x = args(0).toInt
    val y = args(1).toInt
    val t = args(2)
    BuildCommand(new Point(x, y), t)
  }

  def finishCommand(args: List[String]): FinishCommand = {
    describe("finish") {
      args.shouldHaveLength(0)
    }
    FinishCommand()
  }
}

sealed trait FormatValidation {
  case class LengthException(msg: String) extends Exception(msg)
  class LengthValidator(args: List[String]) {
    def shouldHaveLength(n: Int) {
      if (args.length != n) throw new Exception("have " + n + " arguments")
    }
  }
  implicit def lengthValidator(args: List[String]): LengthValidator = new LengthValidator(args)

  case class ArgumentException(msg: String) extends Exception(msg)
  class ArgumentValidator(arg: String) {
    def shouldBeInt() {
      try {
        arg.toInt
      } catch {
        case e: java.lang.NumberFormatException => throw new ArgumentException("\"" + arg + "\" should be Int")
      }
    }
    def shouldBeString() {}
    def shouldBeIn(args: String*) {
      if (!args.contains(arg)) {
        throw new ArgumentException("\"" + arg + "\" should be in " + args.mkString("|"))
      }
    }
  }
  implicit def argumentValidator(arg: String): ArgumentValidator = new ArgumentValidator(arg)

  def describe(cmd: String)(f: => Unit) {
    try {
      f
    } catch {
      case LengthException(s) => throw new CommandException(cmd + " should " + s)
      case ArgumentException(s) => throw new CommandException("In " + cmd + " command, " + s)
    }
  }
}

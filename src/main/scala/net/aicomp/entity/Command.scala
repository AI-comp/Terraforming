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
  
  def moveCommand(args: List[String]) = mkCommand("move"){ c =>
    for {
      _ <- c.validateLength(args, 3);
      x <- c.validateInt(args(0));
      y <- c.validateInt(args(1));
      d <- c.validateInRange(args(2), "r", "ur", "dr", "l", "ul", "dl")
    } yield MoveCommand(new Point(x, y), Direction.fromString(d))
  }

  //TODO
  
  def buildCommand(args: List[String]) = mkCommand("build"){ c =>
    for {
      _ <- c.validateLength(args, 3);
      x <- c.validateInt(args(0));
      y <- c.validateInt(args(1));
      t <- c.validateInRange(args(2), "br", "sh", "at", "mt", "pk", "sq", "pl")
    } yield BuildCommand(new Point(x, y), t)
  }

  def finishCommand(args: List[String]) = mkCommand("finish"){ c =>
    for {
      _ <- c.validateLength(args, 0)
    } yield FinishCommand()
  }
}

sealed trait FormatValidation {
  def mkCommand(name: String)(f: CommandBuilder => Either[String, Command]) = {
    f(new CommandBuilder(name)) match {
      case Left(msg) => throw new CommandException(msg)
      case Right(cmd) => cmd
    }
  }

  class CommandBuilder(val name: String) {
    def validateLength(args: List[String], n: Int) = {
      if (args.length == n)
        Right(())
      else
        Left(name + " should have " + n + " arguments")
    }.right
   
    def validateInt(arg: String) = {
      try {
        Right(arg.toInt)
      } catch {
        case e: java.lang.NumberFormatException => Left("In " + name + " command, \"" + arg + "\" should be Int")
      }
    }.right
  
    def validateInRange(arg: String, range: String*) = {
      if (range.contains(arg))
        Right(arg)
      else
        Left("In " + name + " command, \"" + arg + "\" should be in " + range.mkString("|"))
    }.right
  }
}

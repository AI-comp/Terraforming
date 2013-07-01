package net.aicomp.entity

sealed trait Command

case class MoveCommand(val from: Point, val direction: Direction, val amount: Int)
  extends Command

case class BuildCommand(val at: Point, val instllation: Installation) extends Command

case class FinishCommand() extends Command

/**
 * Command object only do validation of format.
 * Game.acceptCommand must validate the semantics of commands.
 */
object Command extends FormatValidation {
  def moveCommand = mkCommand("move") { c =>
    for {
      _ <- c.validateLength(c.args, 4)
      x <- c.validateInt(c.args(0))
      y <- c.validateInt(c.args(1))
      d <- c.validateDirection(c.args(2))
      amount <- c.validateInt(c.args(3))
    } yield MoveCommand(new Point(x, y), d, amount)
  }

  def buildCommand = mkCommand("build") { c =>
    for {
      _ <- c.validateLength(c.args, 3)
      x <- c.validateInt(c.args(0))
      y <- c.validateInt(c.args(1))
      t <- c.validateInstallation(c.args(2))
    } yield BuildCommand(new Point(x, y), t)
  }

  def finishCommand = mkCommand("finish") { c =>
    for {
      _ <- c.validateLength(c.args, 0)
    } yield FinishCommand()
  }
}

sealed trait FormatValidation {
  case class ArgumentException(msg: String) extends Exception(msg)

  def mkCommand[T <: Command](name: String)(f: CommandBuilder => Either[String, T]) = { (args: List[String]) =>
    f(new CommandBuilder(name, args)) match {
      case Left(msg) => throw new CommandException(msg)
      case Right(cmd) => cmd
    }
  }

  class CommandBuilder(val name: String, val args: List[String]) {
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
        failInRangeMsg(arg, range)
    }.right

    def validateDirection(arg: String) = {
      Direction.find(arg) match {
        case Some(x) => Right(x)
        case None => failInRangeMsg(arg, Direction.keys)
      }
    }.right

    def validateInstallation(arg: String) = {
      val candidates = Installation.buildables.filter(ins => ins.name.startsWith(arg))
      candidates match {
        case Nil => failInRangeMsg(arg, Installation.buildables)
        case x :: Nil => Right(x)
        case _ => Left("Ambiguous installation: " + candidates.mkString(", "))
      }
    }.right

    private def failInRangeMsg[T](arg: String, iter: Iterable[T]) = {
      Left("In " + name + " command, \"" + arg + "\" should be in " + iter.mkString("|"))
    }
  }
}

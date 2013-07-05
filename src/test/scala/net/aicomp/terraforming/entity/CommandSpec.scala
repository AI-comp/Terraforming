package net.aicomp.terraforming.entity

import net.aicomp.terraforming.entity.Direction;
import net.aicomp.terraforming.entity.Installation;

import org.specs2.mutable._

class CommandSpec extends SpecificationWithJUnit {
  "Command" should {
    "return MoveCommand" in {
      Command.moveCommand(List("2", "4", "ur", "1")) must_==
        MoveCommand(Point(2, 4), Direction.ur, 1)
      Command.moveCommand(List("0x2", "4", "ur", "1")) must throwA[CommandException]
    }
    "return BuildCommand" in {
      Command.buildCommand(List("2", "3", "br")) must_==
        BuildCommand(Point(2, 3), Installation.bridge)
      Command.buildCommand(List("2", "3", "ur")) must throwA[CommandException]
    }
  }
}

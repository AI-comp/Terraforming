package net.aicomp.terraforming.manipulator;

import java.util.List;

import net.aicomp.terraforming.entity.Game;
import net.aicomp.terraforming.entity.Player;

public interface InternalManipulator {
	public List<String> run(String stringfied, Game scalaGame,
			Player scalaPlayer);
}

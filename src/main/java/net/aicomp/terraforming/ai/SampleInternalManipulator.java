package net.aicomp.terraforming.ai;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import net.aicomp.terraforming.ai.entity.Direction;
import net.aicomp.terraforming.ai.entity.Field;
import net.aicomp.terraforming.ai.entity.Game;
import net.aicomp.terraforming.ai.entity.Installation;
import net.aicomp.terraforming.ai.entity.Point;
import net.aicomp.terraforming.ai.entity.Tile;
import net.aicomp.terraforming.manipulator.InternalManipulator;

public class SampleInternalManipulator implements InternalManipulator {
	public static void main(String[] args) {
		String name = SampleInternalManipulator.class.getName();
		net.aicomp.terraforming.Main
				.main(new String[] { "-n", "-i", name, name, name });
	}

	private Random rand = new Random("aicomp".hashCode());

	@Override
	public List<String> run(String stringfied,
			net.aicomp.terraforming.entity.Game scalaGame,
			net.aicomp.terraforming.entity.Player scalaPlayer) {
		ArrayList<String> result = new ArrayList<String>();

		Scanner scanner = new Scanner(new StringReader(stringfied));
		Game game = parseGame(scanner);

		// Can conduct only one kind of commands, 'move' or 'build'
		if (rand.nextInt(1) == 0) {
			List<Point> points = game.field.getPointsWithRobots(game.myId);
			for (Point point : points) {
				if (rand.nextInt(1) == 0) {
					Tile tile = game.field.tiles.get(point);
					int dirIndex = rand.nextInt(Direction.values().length);
					result.add(move(point, Direction.values()[dirIndex],
							rand.nextInt(tile.robot) + 1));
				}
			}
		} else if (rand.nextInt(3) == 0) {
			List<Point> points = game.field.getPointsWithRobots(game.myId);
			for (Point point : points) {
				int instIndex = rand.nextInt(Installation.values().length - 1) + 1;
				// Can conduct only one command of 'build'
				if (rand.nextInt(3) == 0) {
					result.add(build(point, Installation.values()[instIndex]));
					// Avoid to conduct two or more than 'build' commands
					break;
				}
			}
		}
		result.add("finish");
		return result;
	}

	private static String move(Point point, Direction dir, int robot) {
		return "move " + point.x + " " + point.y + " " + dir.command + " "
				+ robot;
	}

	private static String build(Point point, Installation inst) {
		return "build " + point.x + " " + point.y + " "
				+ inst.name().toLowerCase();
	}

	private static Game parseGame(Scanner scanner) {
		if (!scanner.next().equals("START")) {
			throw new RuntimeException("START should be retrieved.");
		}

		int turn = scanner.nextInt();
		int maxTurn = scanner.nextInt();
		int myId = scanner.nextInt();
		int radius = scanner.nextInt();
		Game game = new Game(turn, maxTurn, myId, new Field(radius));
		Field field = game.field;

		int nTiles = scanner.nextInt();
		for (int i = 0; i < nTiles; i++) {
			Point point = new Point(scanner.nextInt(), scanner.nextInt());

			int playerId = scanner.nextInt();
			int robot = scanner.nextInt();
			int resource = scanner.nextInt();
			String instName = scanner.next();
			String capitalizedName = instName.substring(0, 1).toUpperCase()
					+ instName.substring(1);
			boolean isHole = false;
			Installation inst = null;
			if (capitalizedName.equals("Hole")) {
				isHole = true;
			} else if (!capitalizedName.equals("None")) {
				inst = Installation.valueOf(capitalizedName);
			}
			Tile tile = new Tile(playerId, robot, resource, isHole, inst);
			field.tiles.put(point, tile);
		}
		if (!scanner.next().equals("EOS")) {
			throw new RuntimeException("EOS should be retrieved.");
		}
		return game;
	}
}

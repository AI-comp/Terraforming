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
import net.aicomp.terraforming.ai.entity.Landform;
import net.aicomp.terraforming.ai.entity.Point;
import net.aicomp.terraforming.ai.entity.Tile;
import net.aicomp.terraforming.manipulator.InternalManipulator;

public class SampleInternalManipulator implements InternalManipulator {
	public static void main(String[] args) {
		long currentTimeMillis = System.currentTimeMillis();
		String name = SampleInternalManipulator.class.getName();
		net.aicomp.terraforming.Main.main(new String[] { "-l", "-i", name });
		System.out.println((System.currentTimeMillis() - currentTimeMillis));
	}

	private Random rand = new Random("aicomp".hashCode());

	public List<String> run(String stringfied,
			net.aicomp.terraforming.entity.Game scalaGame,
			net.aicomp.terraforming.entity.Player scalaPlayer) {
		ArrayList<String> result = new ArrayList<String>();

		Scanner scanner = new Scanner(new StringReader(stringfied));
		Game game = parseGame(scanner);

		// Can conduct only one kind of commands, 'move' or 'build'
		if (rand.nextInt(6) != 0) {
			List<Point> points = game.field.getPointsWithRobots(game.myId);
			for (Point point : points) {
				if (rand.nextInt(3) == 0) {
					Tile tile = game.field.tiles.get(point);
					int dirIndex = rand.nextInt(Direction.values().length);
					Direction dir = Direction.values()[dirIndex];
					int robot = rand.nextInt(tile.robot) + 1;
					if (isMove(game, point, dir, robot)) {
						result.add(move(point, dir, robot));
					}
				}
			}
		} else {
			List<Point> points = game.field.getPointsWithRobots(game.myId);
			for (Point point : points) {
				int instIndex = rand.nextInt(Installation.values().length - 2) + 1;
				// Can conduct only one command of 'build'
				if (rand.nextInt(3) == 0) {
					Installation inst = Installation.values()[instIndex];
					if (isBuild(game, point, inst)) {
						result.add(build(point, inst));
					}
					// Avoid to conduct two or more than 'build' commands
					break;
				}
			}
		}
		result.add("finish");
		return result;
	}

	private boolean isMove(Game game, Point src, Direction dir, int robot) {
		Field field = game.field;
		Point dst = new Point(src.x + dir.x, src.y + dir.y);
		if (!field.tiles.containsKey(dst))
			return false;

		Tile srcTile = field.tiles.get(src);
		Tile dstTile = field.tiles.get(dst);

		if (srcTile.robot < robot)
			return false;

		if (srcTile.isHole && srcTile.installation == Installation.None)
			return false;

		if (dstTile.playerId != game.myId
				&& dstTile.installation != Installation.None)
			return false;

		return true;
	}

	private int aroundResources(Game game, Point point) {
		Tile tile = game.field.tiles.get(point);
		if (tile == null)
			return 0;

		int resources = game.field.tiles.get(point).resource;
		for (Direction direction : Direction.values()) {
			Point aroundPoint = new Point(point.x + direction.x, point.y
					+ direction.y);
			Tile aroundTile = game.field.tiles.get(aroundPoint);
			if (aroundTile != null && aroundTile.playerId == tile.playerId) {
				resources += aroundTile.resource;
			}
		}
		return resources;
	}

	private boolean isBuild(Game game, Point point, Installation inst) {
		Tile tile = game.field.tiles.get(point);
		if (tile.playerId != game.myId)
			return false;
		if (tile.installation != Installation.None)
			return false;
		if (tile.robot < inst.robotCost)
			return false;
		if (tile.isHole && (inst != Installation.Bridge)) {
			return false;
		}
		if (!tile.isHole && (inst == Installation.Bridge)) {
			return false;
		}

		if (aroundResources(game, point) < inst.materialCost) {
			return false;
		}

		return true;
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
			boolean isHole = false;

			String landformName = scanner.next();
			String capitalizedlandformName = landformName.substring(0, 1)
					.toUpperCase() + landformName.substring(1);
			Landform landform = Landform.valueOf(capitalizedlandformName);

			String instName = scanner.next();
			String capitalizedInstName = instName.substring(0, 1).toUpperCase()
					+ instName.substring(1);
			Installation inst = null;
			if (capitalizedInstName.equals("Hole")) {
				isHole = true;
			} else {
				inst = Installation.valueOf(capitalizedInstName);
			}

			Tile tile = new Tile(playerId, robot, resource, isHole, landform,
					inst);
			field.tiles.put(point, tile);
		}
		if (!scanner.next().equals("EOS")) {
			throw new RuntimeException("EOS should be retrieved.");
		}
		return game;
	}
}

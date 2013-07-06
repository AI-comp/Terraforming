import java.util.List;
import java.util.Random;
import java.util.Scanner;

import aicmop.net.terraforming.sample.Direction;
import aicmop.net.terraforming.sample.Field;
import aicmop.net.terraforming.sample.Game;
import aicmop.net.terraforming.sample.Installation;
import aicmop.net.terraforming.sample.Point;
import aicmop.net.terraforming.sample.Tile;

public class SampleJavaAI {
  public static void main(String[] args) {
    Random rand = new Random("aicomp".hashCode());

    Scanner scanner = new Scanner(System.in);
    System.out.println("Your player name");

    while (scanner.hasNext()) {
      Game game = parseGame(scanner);
      if (rand.nextInt(3) == 0) {
        List<Point> points = game.field.getPointsWithRobots(game.myId);
        for (Point point : points) {
          Tile tile = game.field.tiles.get(point);
          int dirIndex = rand.nextInt(Direction.values().length);
          move(point, Direction.values()[dirIndex], rand.nextInt(tile.robot + 1));
        }
      } else if (rand.nextInt(3) == 0) {
        List<Point> points = game.field.getPointsWithRobots(game.myId);
        for (Point point : points) {
          int instIndex = rand.nextInt(Installation.values().length);
          build(point, Installation.values()[instIndex]);
        }
      }
      System.out.println("finish");
    }
    scanner.close();
  }

  private static String move(Point point, Direction dir, int robot) {
    return "move " + point.x + " " + point.y + " " + dir.command + " " + robot;
  }

  private static String build(Point point, Installation inst) {
    return "move " + point.x + " " + point.y + " " + inst.name().toLowerCase();
  }

  private static Game parseGame(Scanner scanner) {
    assert (scanner.next().equals("START"));

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
      String instName = scanner.next();
      String capitalizedName = instName.substring(0, 1).toUpperCase() + instName.substring(1);
      Installation inst = Installation.valueOf(capitalizedName);

      Tile tile = new Tile(playerId, robot, inst);
      field.tiles.put(point, tile);
    }
    assert (scanner.next().equals("EOS"));
    return game;
  }
}

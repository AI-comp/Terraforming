package aicmop.net.terraforming.sample;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

public class Field {
  public final int radius;
  public final HashMap<Point, Tile> tiles; // NOTE: it is a mutable collection

  public Field(int radius) {
    this.radius = radius;
    this.tiles = new HashMap<Point, Tile>();
  }

  public List<Point> getPointsWithRobots(int targetPlayerId) {
    List<Point> result = new ArrayList<Point>();
    for (Entry<Point, Tile> pointAndTile : tiles.entrySet()) {
      Point point = pointAndTile.getKey();
      Tile tile = pointAndTile.getValue();
      if (tile.playerId == targetPlayerId && tile.robot > 0) {
        result.add(point);
      }
    }
    return result;
  }

  public List<Point> getPointsWithoutInstallations() {
    List<Point> result = new ArrayList<Point>();
    for (Entry<Point, Tile> pointAndTile : tiles.entrySet()) {
      Point point = pointAndTile.getKey();
      Tile tile = pointAndTile.getValue();
      if (tile.installation == null) {
        result.add(point);
      }
    }
    return result;
  }

  public int getScore(int targetPlayerId) {
    int score = 0;
    for (Tile tile : tiles.values()) {
      score += tile.getScore(targetPlayerId);
    }
    return score;
  }
}

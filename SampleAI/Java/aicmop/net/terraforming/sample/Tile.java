package aicmop.net.terraforming.sample;

public class Tile {
  public final int playerId;
  public final int robot;
  public final Installation installation;

  public Tile(int playerId, int robot, Installation installation) {
    this.playerId = playerId;
    this.robot = robot;
    this.installation = installation;
  }

  public int getScore(int targetPlayerId) {
    if (playerId != targetPlayerId) {
      return 0;
    }
    if (installation == null) {
      return 1;
    }
    return installation.score;
  }
}

package aicomp.net.terraforming.sample;

public class Tile {
  public final int playerId;
  public final int robot;
  public final int resource;
  public final boolean isHole;
  public final Installation installation;

  public Tile(int playerId, int robot, int resource, boolean isHole, Installation installation) {
    this.playerId = playerId;
    this.robot = robot;
    this.resource = resource;
    this.isHole = isHole;
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

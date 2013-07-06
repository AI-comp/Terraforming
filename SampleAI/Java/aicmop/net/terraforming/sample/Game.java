package aicmop.net.terraforming.sample;

public class Game {
  public final int turn;
  public final int maxTurn;
  public final int myId;
  public final Field field;

  public Game(int turn, int maxTurn, int playerId, Field field) {
    this.turn = turn;
    this.maxTurn = maxTurn;
    this.myId = playerId;
    this.field = field;
  }
}

package aicmop.net.terraforming.sample;

public enum Installation {
  Initial(0, 0, 2),
  Factory(25, 4, 2),
  Bridge(10, 4, 2),
  Shield(25, 6, 2),
  Attack(25, 5, 2),
  Pit(20, 4, 2),
  House(1, 4, 3),
  Town(1, 9, 3),
  City(1, 19, 10);

  public final int robotCost;
  public final int materialCost;
  public final int score;

  private Installation(int robotCost, int materialCost, int score) {
    this.robotCost = robotCost;
    this.materialCost = materialCost;
    this.score = score;
  }
}

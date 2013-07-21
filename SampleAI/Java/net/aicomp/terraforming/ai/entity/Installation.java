package net.aicomp.terraforming.ai.entity;

public enum Installation {
  Initial(0, 0, 3),
  Robotmaker(50, 4, 3),
  Excavator(20, 4, 3),
  Tower(25, 5, 3),
  Bridge(15, 4, 3),
  House(10, 4, 3),
  Town(10, 9, 3),
  None(0, 0, 0);

  public final int robotCost;
  public final int materialCost;
  public final int score;

  private Installation(int robotCost, int materialCost, int score) {
    this.robotCost = robotCost;
    this.materialCost = materialCost;
    this.score = score;
  }
}

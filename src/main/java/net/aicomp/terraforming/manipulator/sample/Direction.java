package net.aicomp.terraforming.manipulator.sample;

public enum Direction {
  Left(-1, 0, "l"),
  Right(0, 1, "r"),
  UpLeft(0, -1, "ul"),
  UpRight(1, -1, "ur"),
  DownLeft(-1, 1, "dl"),
  DownRight(0, 1, "dr");
  
  public final int x, y;
  public final String command;

  private Direction(int x, int y, String command) {
    this.x = x;
    this.y = y;
    this.command = command;
  }
}

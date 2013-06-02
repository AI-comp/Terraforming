package net.aicomp.entity

/*
＿人人人人人人人人
＞                ＜
＞   突然のMap    ＜
＞       __       ＜
＞    __/  \__    ＜
＞   /  \__/  \   ＜
＞   \__/  \__/   ＜
＞   /  \__/  \   ＜
＞   \__/  \__/   ＜
＞      \__/      ＜
＞                ＜
 Y^Y^Y^Y^Y^Y^Y^Y^Y
*/

class Map(radius: Int) {
  require(radius > 0, "radius should be positive integer")

  val tiles: Array[Array[Tile]] = Array.tabulate(radius*2-1, radius*2-1) {
    (y: Int, x: Int) => new Tile(Tile.Kind.Vacant)
  }

  def apply(x: Int, y: Int): Tile = tiles(y)(x)
  def apply(p: Point): Tile = this(p.x, p.y)

}

package net.aicomp.terraforming.entity

import net.aicomp.terraforming.entity.Direction;
import net.aicomp.terraforming.entity.Installation;
import net.aicomp.terraforming.entity.Player;
import net.aicomp.terraforming.entity.Point;

import org.specs2.mutable._
import org.specs2.specification.Scope

class InstallationSpec extends SpecificationWithJUnit {
  trait installations extends Scope {
    val players = Vector(new Player(1), new Player(2), new Player(3))
    val origin = Point(0, 0)
    val radius = 7
    val field = Field(radius, players)
    val tiles = field.tiles
    def apply(x: Int, y: Int): Tile = tiles(new Point(x, y))
    def apply(p: Point): Tile = tiles(p)

    def aroundPoints(origin: Point): List[Point] =
      Direction.all.map(_.p + origin).filter(_.within(radius))

    def initTile(field: Field, p: Point, player: Player) {
      field(p).owner = Some(player)
      field(p).robots = 0
      field(p).movedRobots = 0
      field(p).installation = None
      field(p).isHole = false
    }
  }

  "Installation" should {

    "decline to accept building a \"inisial\"" in new installations {
      initTile(field, origin, players(0))
      val aroundPoints = Direction.all.map(_.p + origin).filter(_.within(radius))
      aroundPoints(origin).foreach(initTile(field, _, players(0)))

      field.build(players(0), origin, Installation.initial) must throwA[CommandException]
    }
    "allow to accept building a \"factory\"" in new installations {
      initTile(field, origin, players(0))
      field(origin).robots = 50

      aroundPoints(origin).foreach(initTile(field, _, players(0)))

      field.build(players(0), origin, Installation.factory) must_== ()
    }
    "decline to accept building a \"factory\" in hole" in new installations {
      initTile(field, origin, players(0))
      field(origin).robots = 50
      field(origin).isHole = true

      aroundPoints(origin).foreach(initTile(field, _, players(0)))

      field.build(players(0), origin, Installation.factory) must throwA[CommandException]
    }
    "decline to accept building a \"factory\" in settlement of few robots" in new installations {
      initTile(field, origin, players(0))
      field(origin).robots = 1

      aroundPoints(origin).foreach(initTile(field, _, players(0)))

      field.build(players(0), origin, Installation.factory) must throwA[CommandException]
    }
    "decline to accept building a \"factory\" in settlement of no robots" in new installations {
      initTile(field, origin, players(0))
      field(origin).robots = 0

      aroundPoints(origin).foreach(initTile(field, _, players(0)))

      field.build(players(0), origin, Installation.factory) must throwA[CommandException]
    }
    "decline to accept building a \"factory\" in not own settlement" in new installations {
      initTile(field, origin, players(1))
      field(origin).robots = 1

      aroundPoints(origin).foreach(initTile(field, _, players(0)))

      field.build(players(0), origin, Installation.factory) must throwA[CommandException]
    }
    "decline to accept building a \"factory\" in wasteland" in new installations {
      initTile(field, origin, null)
      field(origin).robots = 1

      aroundPoints(origin).foreach(initTile(field, _, players(0)))

      field.build(players(0), origin, Installation.factory) must throwA[CommandException]
    }
    "allow to accept building a \"bridge\"" in new installations {
      initTile(field, origin, players(0))
      field(origin).robots = 50
      field(origin).isHole = true

      aroundPoints(origin).foreach(initTile(field, _, players(0)))

      field.build(players(0), origin, Installation.bridge) must_== ()
    }
    "decline to accept build a \"bridge\" in settlement" in new installations {
      initTile(field, origin, players(0))
      field(origin).robots = 50
      field(origin).isHole = false

      aroundPoints(origin).foreach(initTile(field, _, players(0)))

      field.build(players(0), origin, Installation.bridge) must throwA[CommandException]
    }
    "allow to accept building a \"shield\"" in new installations {
      initTile(field, origin, players(0))
      field(origin).robots = 50

      aroundPoints(origin).foreach(initTile(field, _, players(0)))

      field.build(players(0), origin, Installation.shield) must_== ()
    }
    "decline to accept building a \"shield\" in hole" in new installations {
      initTile(field, origin, players(0))
      field(origin).robots = 50
      field(origin).isHole = true

      aroundPoints(origin).foreach(initTile(field, _, players(0)))

      field.build(players(0), origin, Installation.shield) must throwA[CommandException]
    }
    "decline to accept building a \"shield\" in settlment of few robots" in new installations {
      initTile(field, origin, players(0))
      field(origin).robots = 1

      aroundPoints(origin).foreach(initTile(field, _, players(0)))

      field.build(players(0), origin, Installation.shield) must throwA[CommandException]
    }
    "allow to accept building a \"attack\"" in new installations {
      initTile(field, origin, players(0))
      field(origin).robots = 50

      val aroundPoints = Direction.all.map(_.p + origin).filter(_.within(radius))
      for (p <- aroundPoints) {
        initTile(field, p, players(0))
      }

      field.build(players(0), origin, Installation.attack) must_== ()
    }
    "decline to accept building a \"attack\" in hole" in new installations {
      initTile(field, origin, players(0))
      field(origin).robots = 50
      field(origin).isHole = true

      val aroundPoints = Direction.all.map(_.p + origin).filter(_.within(radius))
      for (p <- aroundPoints) {
        initTile(field, p, players(0))
      }

      field.build(players(0), origin, Installation.attack) must throwA[CommandException]
    }
    "decline to accept building a \"attack\" in settlment of few robots" in new installations {
      initTile(field, origin, players(0))
      field(origin).robots = 1

      val aroundPoints = Direction.all.map(_.p + origin).filter(_.within(radius))
      for (p <- aroundPoints) {
        initTile(field, p, players(0))
      }

      field.build(players(0), origin, Installation.attack) must throwA[CommandException]
    }
    "allow to accept building a \"pit\"" in new installations {
      initTile(field, origin, players(0))
      field(origin).robots = 50

      aroundPoints(origin).foreach(initTile(field, _, players(0)))

      field.build(players(0), origin, Installation.pit) must_== ()
    }
    "decline to accept building a \"pit\" in hole" in new installations {
      initTile(field, origin, players(0))
      field(origin).robots = 50
      field(origin).isHole = true
      aroundPoints(origin).foreach(initTile(field, _, players(0)))

      field.build(players(0), origin, Installation.pit) must throwA[CommandException]
    }
    "decline to accept building a \"pit\" in settlment of few robots" in new installations {
      initTile(field, origin, players(0))
      field(origin).robots = 1
      aroundPoints(origin).foreach(initTile(field, _, players(0)))

      field.build(players(0), origin, Installation.pit) must throwA[CommandException]
    }
    "allow to accept building a \"park\"" in new installations {
      initTile(field, origin, players(0))
      field(origin).robots = 1

      aroundPoints(origin).foreach(initTile(field, _, players(0)))

      field.build(players(0), origin, Installation.park) must_== ()
    }
    "decline to accept building a \"park\" in hole" in new installations {
      initTile(field, origin, players(0))
      field(origin).robots = 1
      field(origin).isHole = true

      aroundPoints(origin).foreach(initTile(field, _, players(0)))

      field.build(players(0), origin, Installation.park) must throwA[CommandException]
    }
    "decline to accept building a \"park\" in settlment of no robots" in new installations {
      initTile(field, origin, players(0))
      field(origin).robots = 0

      aroundPoints(origin).foreach(initTile(field, _, players(0)))

      field.build(players(0), origin, Installation.park) must throwA[CommandException]
    }
    "decline to accept building a \"park\" in city" in new installations {
      initTile(field, origin, players(0))
      field(origin).robots = 10

      aroundPoints(origin).foreach(initTile(field, _, players(0)))

      field.build(players(0), origin, Installation.park) must_== ()
      field.build(players(0), origin, Installation.park) must throwA[CommandException]
    }
    /*
 ***********************************
 *                                 *
 *         ,*,   ,*,   ,*,         *
 *       *'   '*'   '*'   '*       *
 *       |     |     |     |       *
 *      ,*,   .*,   .*,   .*,      *
 *    *'   '*'   '*'   '*'   '*    *
 *    |     |     |     |     |    *
 *   ,*,   .*,   .*,   .*,   .*,   *
 * *'   '*'   '*'   '*'   '*'   '* *
 * |     |     | sq  |     |     | *
 * *,   ,*,   .*,   .*,   .*,   .* *
 *   '*'   '*'   '*'   '*'   '*'   *
 *    |     |     |     | pit |    *
 *    *,   ,*,   .*,   .*,   ,*    *
 *      '*'   '*'   '*'   '*'      *
 *       |     |     |     |       *
 *       *,   .*,   .*,   .*       *
 *         '*'   '*'   '*'         *
 *                                 *
 ***********************************
 */
    trait originAndPit extends installations {
      val pitPlace = Point(1, 1)
      initTile(field, origin, players(0))
      initTile(field, pitPlace, players(0))
      aroundPoints(origin).foreach(initTile(field, _, players(0)))
      aroundPoints(pitPlace).foreach(initTile(field, _, players(0)))
      field(origin).robots = 1
      field(pitPlace).robots = 20
    }

    "allow to accept building a \"square\"" in new installations with originAndPit {
      field.build(players(0), pitPlace, Installation.pit) must_== ()
      field.build(players(0), origin, Installation.square) must_== ()
    }

    "decline to accept building a \"square\" in hole" in new installations with originAndPit {
      field(origin).isHole = true

      field.build(players(0), pitPlace, Installation.pit) must_== ()
      field.build(players(0), origin, Installation.square) must throwA[CommandException]
    }

    "decline to accept building a \"square\" in settlement which is not enough material" in new installations {
      initTile(field, origin, players(0))
      aroundPoints(origin).foreach(initTile(field, _, players(0)))
      field(origin).robots = 1

      field.build(players(0), origin, Installation.square) must throwA[CommandException]
    }
    /*
 ***********************************
 *                                 *
 *         ,*,   ,*,   ,*,         *
 *       *'   '*'   '*'   '*       *
 *       |     | pit |     |       *
 *      ,*,   .*,   .*,   .*,      *
 *    *'   '*'   '*'   '*'   '*    *
 *    | pit |     |     | pit |    *
 *   ,*,   .*,   .*,   .*,   .*,   *
 * *'   '*'   '*'   '*'   '*'   '* *
 * |     |     | pbl |     |     | *
 * *,   ,*,   .*,   .*,   .*,   .* *
 *   '*'   '*'   '*'   '*'   '*'   *
 *    | pit |     |     | pit |    *
 *    *,   ,*,   .*,   .*,   ,*    *
 *      '*'   '*'   '*'   '*'      *
 *       |     | pit |     |       *
 *       *,   .*,   .*,   .*       *
 *         '*'   '*'   '*'         *
 *                                 *
 ***********************************
 */
    trait originAndPits extends installations {
      val pitPlaces = List(Point(1, 1), Point(2, -1), Point(1, -2), Point(-1, -1), Point(-2, 1), Point(-1, 2))
      initTile(field, origin, players(0))
      aroundPoints(origin).foreach(initTile(field, _, players(0)))
      pitPlaces.foreach(_p => {
        initTile(field, _p, players(0))
        aroundPoints(_p).foreach(initTile(field, _, players(0)))
      })

      field(origin).robots = 1
      pitPlaces.foreach(_p => field(_p).robots = 20)
    }

    "allow to accept building a \"public\"" in new installations with originAndPits {
      pitPlaces.foreach(_p =>
        field.build(players(0), _p, Installation.pit) must_== ())
      field.build(players(0), origin, Installation.public) must_== ()
    }

    "decline to accept building a \"public\" in hole" in new installations with originAndPits {
      field(origin).isHole = true

      pitPlaces.foreach(_p =>
        field.build(players(0), _p, Installation.pit) must_== ())
      field.build(players(0), origin, Installation.public) must throwA[CommandException]
    }

    "decline to accept building a \"public\" in settlement which is not enough material" in new installations {
      val pitPlaces = List(Point(1, 1), Point(2, -1), Point(1, -2), Point(-1, -1), Point(-2, 1))

      initTile(field, origin, players(0))
      aroundPoints(origin).foreach(initTile(field, _, players(0)))
      pitPlaces.foreach(_p => {
        initTile(field, _p, players(0))
        aroundPoints(_p).foreach(initTile(field, _, players(0)))
      })

      field(origin).robots = 1
      pitPlaces.foreach(_p => field(_p).robots = 20)

      pitPlaces.foreach(_p =>
        field.build(players(0), _p, Installation.pit) must_== ())
      field.build(players(0), origin, Installation.public) must throwA[CommandException]
    }
  }

}

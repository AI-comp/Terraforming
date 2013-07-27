package net.aicomp.terraforming.entity

import org.specs2.mutable.SpecificationWithJUnit
import org.specs2.specification.Scope
import java.util.Random

class InstallationSpec extends SpecificationWithJUnit {
  trait installations extends Scope {
    val players = Vector(Player(1), Player(2), Player(3))
    val origin = Point(0, 0)
    val radius = 7
    val field = Field(radius, players, new Random(0))
    val tiles = field.tiles
    def apply(x: Int, y: Int): Tile = tiles(new Point(x, y))
    def apply(p: Point): Tile = tiles(p)

    def initTile(field: Field, p: Point, player: Player) {
      field(p).owner = if (player != null) Some(player) else None
      field(p).robots = 0
      field(p).movedRobots = 0
      field(p).installation = None
      field(p).isHole = false
    }

    initTile(field, origin, players(0))
    field.availableAroundPoints(origin).foreach(initTile(field, _, players(0)))
  }

  "Installation" should {
    "decline to accept building a 'initial'" in new installations {
      field.build(players(0), origin, Installation.initial) must throwA[CommandException]
    }
    "allow to accept building a 'factory'" in new installations {
      field(origin).robots = 50

      field.build(players(0), origin, Installation.robotmaker) must_== ()
    }
    "decline to accept building a 'factory' in hole" in new installations {
      field(origin).robots = 50
      field(origin).isHole = true

      field.build(players(0), origin, Installation.robotmaker) must throwA[CommandException]
    }
    "decline to accept building a 'factory' in settlement of few robots" in new installations {
      field(origin).robots = 1

      field.build(players(0), origin, Installation.robotmaker) must throwA[CommandException]
    }
    "decline to accept building a 'factory' in settlement of no robots" in new installations {
      field(origin).robots = 0

      field.build(players(0), origin, Installation.robotmaker) must throwA[CommandException]
    }
    "decline to accept building a 'factory' in not own settlement" in new installations {
      field(origin).robots = 1

      field.build(players(0), origin, Installation.robotmaker) must throwA[CommandException]
    }
    "decline to accept building a 'factory' in wasteland" in new installations {
      initTile(field, origin, null)
      field(origin).robots = 1

      field.build(players(0), origin, Installation.robotmaker) must throwA[CommandException]
    }
    "allow to accept building a 'bridge'" in new installations {
      field(origin).robots = 50
      field(origin).isHole = true

      field.build(players(0), origin, Installation.bridge) must_== ()
    }
    "decline to accept build a 'bridge' in settlement" in new installations {
      field(origin).robots = 50
      field(origin).isHole = false

      field.build(players(0), origin, Installation.bridge) must throwA[CommandException]
    }
    "allow to accept building a 'tower'" in new installations {
      field(origin).robots = 50

      field.build(players(0), origin, Installation.tower) must_== ()
    }
    "decline to accept building a 'tower' in hole" in new installations {
      field(origin).robots = 50
      field(origin).isHole = true

      field.build(players(0), origin, Installation.tower) must throwA[CommandException]
    }
    "decline to accept building a 'tower' in settlment of few robots" in new installations {
      field(origin).robots = 1

      field.build(players(0), origin, Installation.tower) must throwA[CommandException]
    }
    "allow to accept building a 'pit'" in new installations {
      field(origin).robots = 50

      field.build(players(0), origin, Installation.excavator) must_== ()
    }
    "decline to accept building a 'pit' in hole" in new installations {
      field(origin).robots = 50
      field(origin).isHole = true

      field.build(players(0), origin, Installation.excavator) must throwA[CommandException]
    }
    "decline to accept building a 'pit' in settlment of few robots" in new installations {
      field(origin).robots = 1

      field.build(players(0), origin, Installation.excavator) must throwA[CommandException]
    }
    "allow to accept building a 'house'" in new installations {
      field(origin).robots = 10

      field.build(players(0), origin, Installation.house) must_== ()
    }
    "decline to accept building a 'house' in hole" in new installations {
      field(origin).robots = 1
      field(origin).isHole = true

      field.build(players(0), origin, Installation.house) must throwA[CommandException]
    }
    "decline to accept building a 'house' in settlment of no robots" in new installations {
      field(origin).robots = 0

      field.build(players(0), origin, Installation.house) must throwA[CommandException]
    }
    "decline to accept building two 'house' on the same place" in new installations {
      field(origin).robots = 10

      field.build(players(0), origin, Installation.house) must_== ()
      field.build(players(0), origin, Installation.house) must throwA[CommandException]
    }
    "decline to accept building a 'town' in settlement which is not enough material" in new installations {
      field(origin).robots = 1

      field.build(players(0), origin, Installation.town) must throwA[CommandException]
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
      initTile(field, pitPlace, players(0))
      field.availableAroundPoints(pitPlace).foreach(initTile(field, _, players(0)))
      field(origin).robots = 10
      field(pitPlace).robots = 25
    }

    "allow to accept building a 'town'" in new originAndPit {
      field.build(players(0), pitPlace, Installation.excavator) must_== ()
      field.build(players(0), origin, Installation.town) must_== ()
    }

    "decline to accept building a 'town' in hole" in new originAndPit {
      field(origin).isHole = true

      field.build(players(0), pitPlace, Installation.excavator) must_== ()
      field.build(players(0), origin, Installation.town) must throwA[CommandException]
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
      pitPlaces.foreach(_p => {
        initTile(field, _p, players(0))
        field.availableAroundPoints(_p).foreach(initTile(field, _, players(0)))
      })

      field(origin).robots = 10
      pitPlaces.foreach(_p => field(_p).robots = 25)
    }

    "allow to accept building a 'town'" in new originAndPits {
      pitPlaces.foreach(_p =>
        field.build(players(0), _p, Installation.excavator) must_== ())
      field.build(players(0), origin, Installation.town) must_== ()
      field(origin).additionalScore must_== 10

    }

    "allow to accept building a 'house' in vertex" in new installations {
      val settlementPlaces = List(
        Point(5, 0), Point(5, 1), Point(6, -1),
        Point(5, -5), Point(6, -5), Point(5, -6),
        Point(0, -5), Point(1, -6), Point(-1, -5),
        Point(-5, 0), Point(-5, -1), Point(-6, 1),
        Point(-5, 5), Point(-6, 5), Point(-5, 6),
        Point(0, 5), Point(-1, 6), Point(1, 5))

      val housePlaces = List(
        Point(-6, 6), Point(-6, 0), Point(0, -6),
        Point(0, 6), Point(6, -6), Point(6, 0))

      settlementPlaces.foreach(_p =>
        initTile(field, _p, players(0)))
        
      housePlaces.foreach(_p => {
        initTile(field, _p, players(0))
        field(_p).robots = 10
        field.build(players(0), _p, Installation.house)
      })
    }
    
        "allow to accept building a 'house' in vertex" in new installations {
      val settlementPlaces = List(
        Point(5, 0), Point(5, 1), Point(6, -1),
        Point(5, -5), Point(6, -5), Point(5, -6),
        Point(0, -5), Point(1, -6), Point(-1, -5),
        Point(-5, 0), Point(-5, -1), Point(-6, 1),
        Point(-5, 5), Point(-6, 5), Point(-5, 6),
        Point(0, 5), Point(-1, 6), Point(1, 5))

      val housePlaces = List(
        Point(-6, 6), Point(-6, 0), Point(0, -6),
        Point(0, 6), Point(6, -6), Point(6, 0))

      settlementPlaces.foreach(_p =>
        initTile(field, _p, players(0)))
        
      housePlaces.foreach(_p => {
        initTile(field, _p, players(0))
        field(_p).robots = 10
        field.build(players(0), _p, Installation.house)
      })
    }

  }

}

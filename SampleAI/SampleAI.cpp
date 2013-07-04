#include <cassert>
#include <iostream>
#include <map>
#include <string>
#include <vector>

////////////////////////////////////////
// entity definitions

class Point {
    int _x;
    int _y;

public:
    Point(int x, int y) : _x(x), _y(y) {}
    int x() const { return _x; }
    int y() const { return _y; }
};

bool operator<(const Point& a, const Point& b) {
    return a.x() != b.x() ? a.x() < b.x() : a.y() < b.y();
}

class Tile {
    int _playerId;
    int _robots;
    std::string _building;

public:
    Tile() {}
    Tile(int playerId, int robots, const std::string& building)
        : _playerId(playerId), _robots(robots), _building(building) {}
    int playerId() const { return _playerId; };
    int robots() const { return _robots; };
    std::string building() const { return _building; };
};

class Field {
    int _radius;
    std::map<Point, Tile> _tiles;

public:
    Field(int radius, const std::map<Point, Tile>& tiles)
        : _radius(radius), _tiles(tiles) {}
    int radius() const { return _radius; }
    Tile at(int x, int y) const { return _tiles.find(Point(x, y))->second; }
};

class Game {
    int _turn;
    int _maxTurn;
    int _myId;
    Field _field;

public:
    Game(int turn, int maxTurn, int myId, const Field& field)
        : _turn(turn), _maxTurn(maxTurn), _myId(myId), _field(field) {}
    int turn() const { return _turn; }
    int maxTurn() const { return _maxTurn; }
    int myId() const { return _myId; }
    const Field &field() const { return _field; }
};


////////////////////////////////////////
// runtime

Game *readGame() {
    std::string start, eos;
    if (!(std::cin >> start)) return 0;
    assert(start == "START");
    int turn, maxTurn, myId;
    std::cin >> turn >> maxTurn >> myId;
    int radius, n;
    std::cin >> radius >> n;
    std::map<Point, Tile> tiles;
    for (int i = 0; i < n; i++) {
        int x, y, id, robots;
        std::string building;
        std::cin >> x >> y >> id >> robots >> building;
        tiles[Point(x, y)] = Tile(id, robots, building);
    }
    std::cin >> eos;
    assert(eos == "EOS");
    return new Game(turn, maxTurn, myId, Field(radius, tiles));
}

std::string name();
std::vector<std::string> command(const Game* game);

int main() {
    std::cin.rdbuf()->pubsetbuf(0, 0);
    Game *initial = readGame();
    std::cout << name() << std::endl;
    std::cout << std::endl;
    std::cout.flush();
    for (;;) {
        std::cout << "finish" << std::endl;
        std::cout << std::endl;
        std::cout.flush();
    }
    std::cout << name() << std::endl;
    std::cout << std::endl;
    std::cout.flush();
    delete initial;
    for (;;) {
        Game *game = readGame();
        if (game == 0) break;
        std::vector<std::string> cmds = command(game);
        for (size_t i = 0; i < cmds.size(); i++) {
            std::cout << cmds[i] << std::endl;
        }
        std::cout << std::endl;
        std::cout.flush();
        delete game;
    }
    return 0;
}


////////////////////////////////////////
// put your player name here
std::string name() {
    return "cpp";
}

////////////////////////////////////////
// put your command-decision algorithm here
std::vector<std::string> command(const Game* game) {
    std::vector<std::string> cmds;
    cmds.push_back("finish");
    return cmds;
}

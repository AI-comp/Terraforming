-- this is a haskell sample client for Terraforming
import Control.Applicative
import Control.Arrow
import Control.Monad
import System.IO

-- entity definitions
type Point = (Int, Int) -- x * y
data Tile = Tile Int Int String -- player_id * num_robots * building
data Field = Field Int [(Point, Tile)] -- radius * tiles
data Game = Game Int Field -- turn * field


-- runtime
parsePointAndTile :: String -> (Point, Tile)
parsePointAndTile s = ((x, y), Tile n i b)
  where ([x, y, n, i], [b]) = map read `first` splitAt 4 (words s)

parseField :: [String] -> (Field, [String])
parseField (h:ls) = (Field r ts, rs)
  where [r, n] = map read $ words h
        (ts, rs) = map parsePointAndTile `first` splitAt n ls

parseGame :: [String] -> (Game, [String])
parseGame (start:t:ls) = eos `seq` (Game (read t) f, rs)
  where (f, (eos:rs)) = parseField ls

writeCommands cs = mapM_ putStrLn cs >> putStrLn ""

main = do
    hSetBuffering stdin LineBuffering
    hSetBuffering stdout LineBuffering
    (g, next) <- parseGame . lines <$> getContents
    g `seq` writeCommands [name]
    run next
  where run [] = return ()
        run ls = do
            let (g, next) = parseGame ls
            g `seq` writeCommands (command g)
            run next


-- put your player name here
name = "haskell"

-- put your command-decision algorithm here
command :: Game -> [String]
command g = ["finish"]

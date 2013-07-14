-- this is a haskell sample client for Terraforming
import Control.Applicative
import Control.Arrow
import Control.Monad
import System.IO

-- entity definitions
type Point = (Int, Int) -- x * y
data Tile = Tile { playerId :: Int
                 , robotsNumber :: Int
                 , resourcesNumber :: Int
                 , landForm :: String
                 , building :: String
                 } deriving (Show)

data Field = Field { radius :: Int
                   , tiles :: [(Point, Tile)]
                   } deriving (Show)

data Game = Game { turn :: Int
                 , maxTurn :: Int
                 , ownId :: Int
                 , field :: Field
                 } deriving (Show)

-- runtime
parsePointAndTile :: String -> (Point, Tile)
parsePointAndTile s = ((x, y), Tile n i r l b)
  where ([x, y, n, i, r], [l, b]) = map read `first` splitAt 5 (words s)

parseField :: [String] -> (Field, [String])
parseField (h:ls) = (Field r ts, rs)
  where [r, n] = map read $ words h
        (ts, rs) = map parsePointAndTile `first` splitAt n ls

parseGame :: [String] -> (Game, [String])
parseGame (start:l:ls) = eos `seq` (Game t mt mi f, rs)
  where [t, mt, mi] = map read $ words l
        (f, (eos:rs)) = parseField ls

writeCommands cs = mapM_ putStrLn cs

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
name = "Haskell"

-- put your command-decision algorithm here
command :: Game -> [String]
command g = ["finish"]

#!/bin/sh -v

cd Java
javac *.java

cd ../Scala
scalac *.scala

cd ../Cpp
g++ *.cpp -o a.exe

cd ../Haskell
ghc Main.hs -o Main.exe

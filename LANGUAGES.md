## C
- Environment

        gcc version 4.6.3 (Ubuntu/Linaro 4.6.3-1ubuntu5)

- Requirements
  - main.cにmain関数が記述されている
- Running Process
  1. Fnid a directory which contains main.c
  2. gcc *.c
  3. java -jar Terraforming.jar -a "./a.out"

## Makefile
- Environment

        GNU Make 3.81

- Requirements
  - Makefileが存在していてmakeでビルドできる
- Running Process
  1. Fnid a directory which contains Makefile
  2. make
  3. java -jar Terraforming.jar -a "./a.out"

## Java
- Environment

        java version "1.7.0_25"
        Java(TM) SE Runtime Environment (build 1.7.0_25-b15)
        Java HotSpot(TM) Server VM (build 23.25-b01, mixed mode)

- Requirements
  - Main.javaにmainメソッドが記述されていて，デフォルトパッケージ（パッケージ宣言なし）を使っている
- Running Process
  1. Fnid a directory which contains Main.java
  2. javac Main.java
  3. java -jar Terraforming.jar -a "java Main"

## Maven
- Environment

        Apache Maven 3.0.4

- Requirements
  - pom.xmlが存在していてMaven3でビルドできる
  - Main.javaにmainメソッドが記述されていて，デフォルトパッケージ（パッケージ宣言なし）を使っている
1. Fnid a directory which contains pom.xml
2. mvn package; mv target/*.jar ai.jar
3. java -jar Terraforming.jar -a "java -jar ai.jar Main"

## Scala

## Ruby

## Haskell

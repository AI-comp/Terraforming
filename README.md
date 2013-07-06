Terraforming for CEDEC 2013 [![Build Status](https://api.travis-ci.org/AI-comp/Terraforming.png?branch=master)](https://travis-ci.org/AI-comp/Terraforming) [![Coverage Status](https://coveralls.io/repos/AI-comp/Terraforming/badge.png?branch=master)](https://coveralls.io/r/AI-comp/Terraforming)
========================

## Setup Development Environment

### Prepare Eclipse Environment with Maven
1. Install Eclipse
  * Eclipse IDE for Java Developers Juno (4.2)  
http://www.eclipse.org/downloads/
1. Run Eclipse
1. Install Scala IDE 3.0 (For Scala 2.9.x) on Eclipse Juno (4.2)  
  1. See the following web page: http://scala-ide.org/download/current.html
  1. Copy a URL of suitable version (the *4th* URL) on the above site  
  1. Menu > Help > Install new software
  1. Enter the copied URL on the textbox of "Enter with" then push enter key
1. Install m2e-scala connector on Eclipse Juno (4.2)  
  1. Copy the following URL: http://alchim31.free.fr/m2e-scala/update-site/
  1. Menu > Help > Install new software
  1. Enter the copied URL on the textbox of "Enter with" then push enter key
1. Edit eclipse.ini ("eclipse/eclipse.ini" on Windows, "Eclipse.app/Contents/MacOS/eclipse.ini" on Mac OS)  
-Xmx???m => -Xmx2048m

### Import the Maven Project into Your Eclipse Workspace
You can import maven projects with the following steps:

1. Import > Existing Maven Projects
2. Set Root Directory containing pom.xml
3. Select Projects
4. Push Finish
5. Right click the imported project > Maven > Update Project Configuration > OK

### Build tools
Please install the following tools selecting the following choices.
- sbt ( http://www.scala-sbt.org/ )
- Maven 3 ( http://www.scala-sbt.org/ )
- Zinc ( https://github.com/typesafehub/zinc )

### Test with sbt (Choice 1)
1. sbt  
Start a build server.
2. test  
Conduct testing.

### Test with Maven 3 and Zinc (Choice 2)
1. zinc -start  
Start a build server.  
1. mvn test  
Conduct testing.

### Test with Maven 3 (Choice 3)
1. mvn test  
Conduct testing.

## Documents
- Rule book  
https://docs.google.com/document/d/1bcgHpgDxDQbm94-BdOw0gEG08Wfums8nlGv-kNHlxmE/edit?usp=sharing
- Sample code for rendering a game screen
https://github.com/AI-comp/JavaChallenge2012/blob/master/src/main/scala/net/javachallenge/scene/graphic/GraphicalScene.scala

## Contributors
- __Project Leader__
  - Kazunori Sakamoto (National Institute of Informatics)
- __Developers of Game Platform__
  - Kosuke Yatoh (University of Tokyo)
  - Hiroaki Hosono (Tokyo Institute of Technology)
  - Naoki Yaguchi (Waseda University)
  - Koji Tumura (Waseda University)
  - Ryu Yasuda (Waseda University)
  - Daichi Ota (ACCESS CO., LTD.)
  - Junichi Kobayashi (Waseda University)
  - Masataka Kido (Waseda University)
  - Fumiya Kato (Waseda University)
  - Shogo Kishimoto (Tokyo Institute of Technology)
- __Game Designers__
  - Hiroaki Hosono (Tokyo Institute of Technology)
  - Junichi Kobayashi (Waseda University)
  - Seiji Sato (Waseda University)
- __Developers of AI Execution Server__
  - Fumiya Kato (Waseda University)
  - Genki Sugimoto (Waseda University)
- __Developers of Web Server__
  - Daniel Perez (Waseda University)
  - Masataka Kido (Waseda University)
- __Developers of JavaScript Replayer__
  - Ryohei Takasawa (Waseda University)
  - Naohiko Tuda (Waseda University)
  - Dai Hamada (Waseda University)
- __Developers of Sample AI Programs__
  - __C++, Haskell__: Kosuke Yatoh (University of Tokyo)
  - __Scala__: Ryu Yasuda (Waseda University)
- __Graphic Designers__
  - Masayuki Asakura (Waseda University)
  - Masahiko Wada
- __Web Masters__
  - Kimiyasu Morikawa (Waseda University)
  - Hiroaki Hosono (Tokyo Institute of Technology)

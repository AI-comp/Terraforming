# Project Status
[![Build Status](https://api.travis-ci.org/AI-comp/Terraforming.png?branch=master)]
(https://travis-ci.org/AI-comp/Terraforming)
[![Coverage Status](https://coveralls.io/repos/AI-comp/Terraforming/badge.png?branch=master)]
(https://coveralls.io/r/AI-comp/Terraforming)

# Prepare Eclipse environment
1. Install Eclipse
  * Eclipse IDE for Java Developers Juno (4.2)  
http://www.eclipse.org/downloads/
2. Run Eclipse
3. Menu > Help > Install new software
4. Install Scala IDE 3.0 (For Scala 2.9.x) on Eclipse Juno (4.2)  
  * See the following web page and install suitable version
  * http://scala-ide.org/download/current.html
5. Install m2e-scala connector on Eclipse (the following URL indicates a update site instead of a web page)  
http://alchim31.free.fr/m2e-scala/update-site/
6. Edit eclipse.ini ("eclipse/eclipse.ini" on Windows, "Eclipse.app/Contents/MacOS/eclipse.ini" on Mac OS)  
-Xmx???m => -Xmx2048m

# Import the maven project into your Eclipse workspace
You can import maven projects with the following steps:

1. Import > Existing Maven Projects
2. Set Root Directory containing pom.xml
3. Select Projects
4. Push Finish
5. Right click the imported project > Maven > Update Project Configuration > OK

# Build with Maven 3
1. mvn clean package

# Dcouments
* https://docs.google.com/document/d/1bcgHpgDxDQbm94-BdOw0gEG08Wfums8nlGv-kNHlxmE/edit?usp=sharing

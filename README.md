# Prepare Eclipse environment
1. Install Eclipse
  * For Juno (4.2)  
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
* https://github.com/JavaChallenge2012/JavaChllaenge2012
* http://www.slideshare.net/exKAZUu/javachallenge-2012-result
* http://www.slideshare.net/exKAZUu/javachallenge-2012-special-league

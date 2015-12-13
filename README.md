# PoliticalHierarchyGraph

This project is intended to be a test how to setup a hierarchical system
(in this case all districts, cities of Bavaria-Germany) in Neo4j.

It is also intended to test which queries you can do on this data.

For legal issues:
Daten von <a href="http://www.openstreetmap.org/">OpenStreetMap</a> - 
Veröffentlicht unter <a href="http://opendatacommons.org/licenses/odbl/">ODbL</a>

This application uses interfaces to OSM to parse data.

## Installation Database
* get Neo4j - version 3.2.1, Community Edition (http://neo4j.com/download/)
* start it according to installation instructions
* open your browser and type http://localhost:7474 to start neo4j console
* when you are asked for a password, set it. Don't forget it, you'll need it

## Start this application
* install gradle
* go to the root of this directory
* gradle build
* go to build/libs
* do java -jar geographicalHierarchy-0.0.1-SNAPSHOT.jar
* got to http://localhost:8080/ to initialize db
* go to your neo4j browser for furhter information
  
 ## Links
 * https://de.wikipedia.org/wiki/Land_%28Deutschland%29
 * https://de.wikipedia.org/wiki/Liste_der_Kreiszugeh%C3%B6rigkeit_bayerischer_Gemeinden
 * https://de.wikipedia.org/wiki/Liste_der_Landkreise_und_kreisfreien_St%C3%A4dte_in_Bayern
 
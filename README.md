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
* go to your neo4j browser for further information

## Start in a docker environment
Connection to the database is done via container link. The link to the databse is 
then given by a environment var called NEO4J_PORT_7474_TCP_ADDR. The correct adress
to the database is set up during start. See Dockerfile for more information.

This is how to start the neo4j container (all commands are done by root):
```
useradd -U -m neo4j
sudo su neo4j mkdir ~/geohierarchy
docker run -d --publish=7474:7474 --name neo4j --volume=/home/neo4j/geohierarchy:/data neo4j
```

To set credentials we need another way: create directory (i.e. /opt/geohierarchy) and put an
application.properties file in it. This is the content (adapt data!!):
```
neo4j.user=neo4j
neo4j.password=yourverysecretpassword
```

Now start the container: 
```
docker run -p 8080:8080 --link=neo4j --rm --volume=/opt/geohierarchy:/config --name=web ollihoo/geohierarchy
```  
  
## Links
 * https://de.wikipedia.org/wiki/Land_%28Deutschland%29
 * https://de.wikipedia.org/wiki/Liste_der_Kreiszugeh%C3%B6rigkeit_bayerischer_Gemeinden
 * https://de.wikipedia.org/wiki/Liste_der_Landkreise_und_kreisfreien_St%C3%A4dte_in_Bayern
 
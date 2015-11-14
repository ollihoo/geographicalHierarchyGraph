# PoliticalHierarchyGraph

This project is intended to be a test how to setup a hierarchical system
(in this case all districts, cities of Bavaria-Germany) in Neo4j.

It is also intended to test which queries you can do on this data.

## Installation
* get Neo4j - version 3.2.0, Community Edition (http://neo4j.com/download/)
* start it according to installation instructions
* open your browser and type http://localhost:7474 to start neo4j console
* go to directory src/main/resources/cypher-scripts in this project
* copy every file in the given order into console and press Ctrl-Return

System is ready for requests.

## Do Requests on data

### Get all districts of Bavaria:
  MATCH (d:Districts)-[:BELONGS_TO]->(s:State{name:"Bayern"}) return d.name
  
### Get all communities within rural district 'Traunstein'
       
  MATCH (c:Community)-[:BELONGS_TO]->(:RuralDistrict{name:"Traunstein"}) RETURN c.name
  
### Is there a unit called "Tutzing"? Where is it located within the hierarchy?
 
  MATCH path=(n)-[:BELONGS_TO*1..]->(:State{name:"Bayern"}) 
   WHERE n.name = "Tutzing"
   RETURN extract(node in nodes(path) | node.name)
   
 ## Links
 * https://de.wikipedia.org/wiki/Land_%28Deutschland%29
 * https://de.wikipedia.org/wiki/Liste_der_Kreiszugeh%C3%B6rigkeit_bayerischer_Gemeinden
 * https://de.wikipedia.org/wiki/Liste_der_Landkreise_und_kreisfreien_St%C3%A4dte_in_Bayern
 
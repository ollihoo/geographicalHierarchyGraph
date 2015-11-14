LOAD CSV WITH HEADERS FROM "https://raw.githubusercontent.com/ollihoo/geographicalHierarchyGraph/master/src/installation/csv/states.csv" AS line FIELDTERMINATOR '|'
MATCH (c:Country{name:"Deutschland"})
MERGE (s:State{name:line.Land,squareFootage:line.Flaeche,noInhabitants:line.Einwohner,inhabitantsPerSKm:line.EinwJeQkm,capital:line.Hauptstadt})
MERGE (s)-[:BELONGS_TO]->(c)
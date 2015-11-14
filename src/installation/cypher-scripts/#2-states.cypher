LOAD CSV WITH HEADERS FROM "..." AS line FIELDTERMINATOR '|'
MATCH (c:Country{name:"Deutschland"})
MERGE (s:State{name:line.Land,squareFootage:line.Flaeche,noInhabitants:line.Einwohner,inhabitantsPerSKm:line.EinwJeQkm,capital:line.Hauptstadt})
MERGE (s)-[:BELONGS_TO]->(c)
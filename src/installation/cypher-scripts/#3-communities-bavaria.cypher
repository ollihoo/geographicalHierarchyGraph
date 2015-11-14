USING PERIODIC COMMIT 10
LOAD CSV WITH HEADERS FROM "..." AS line FIELDTERMINATOR '|'
MATCH (s:State{name:"Bayern"})
MERGE (d:District{name:line.Bezirk})
MERGE (rd:RuralDistrict{name:line.Landkreis})
MERGE (co:Community{name:line.Gemeinde})
MERGE (co)-[:BELONGS_TO]->(rd)
MERGE (rd)-[:BELONGS_TO]->(d)
MERGE (d)-[:BELONGS_TO]->(s)

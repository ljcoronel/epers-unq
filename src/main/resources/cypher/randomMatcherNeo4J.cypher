UNWIND range(1,200) as x
MATCH(a:UBICACION{id:toInteger(rand() * 99) + 1}) ,
     (u:UBICACION{id:toInteger(rand() * 99) + 1 })
      MERGE (a)-[:CONECTADA]->(u)

MATCH(u1:UBICACION{id:1}) ,
     (u2:UBICACION{id:2}),
     (u3:UBICACION{id:3}),
     (u4:UBICACION{id:4}),
     (u5:UBICACION{id:5})

      MERGE (u1)-[:CONECTADA]->(u2)
      MERGE (u1)-[:CONECTADA]->(u3)

      MERGE (u2)-[:CONECTADA]->(u1)
      MERGE (u2)-[:CONECTADA]->(u3)
      MERGE (u2)-[:CONECTADA]->(u4)
      MERGE (u2)-[:CONECTADA]->(u5)

      MERGE (u3)-[:CONECTADA]->(u1)
      MERGE (u3)-[:CONECTADA]->(u2)
      MERGE (u3)-[:CONECTADA]->(u4)

      MERGE (u4)-[:CONECTADA]->(u2)
      MERGE (u4)-[:CONECTADA]->(u3)
      MERGE (u4)-[:CONECTADA]->(u5)

      MERGE (u5)-[:CONECTADA]->(u2)
      MERGE (u5)-[:CONECTADA]->(u4)

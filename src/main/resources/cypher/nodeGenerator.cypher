UNWIND range(1,100) AS i
CREATE (:UBICACION {id: i, nombre: "ubicacion" + i, nivelDeConexion: toInteger(rand() * 90) + 1  })
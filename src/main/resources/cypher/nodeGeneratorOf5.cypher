UNWIND range(1,5) AS i
CREATE (:UBICACION {id: i, nombre: "ubicacion" + i, nivelDeConexion: toInteger(rand() * 90) + 1  })
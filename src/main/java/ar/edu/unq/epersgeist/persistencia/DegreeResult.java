package ar.edu.unq.epersgeist.persistencia;

import ar.edu.unq.epersgeist.modelo.ubicacion.Ubicacion;
import ar.edu.unq.epersgeist.persistencia.repositories.dto.neo4j.ClosenessResultDTO;
import ar.edu.unq.epersgeist.persistencia.repositories.dto.neo4j.DegreeResultDTO;

public record DegreeResult(Ubicacion ubicacion, Double closeness) {

    public static DegreeResult registrar(DegreeResultDTO dto) {
        return new DegreeResult(dto.origen().aModelo(), dto.degree());
    }
}

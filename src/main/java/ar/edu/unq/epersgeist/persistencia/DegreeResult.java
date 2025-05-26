package ar.edu.unq.epersgeist.persistencia;

import ar.edu.unq.epersgeist.modelo.ubicacion.Ubicacion;
import ar.edu.unq.epersgeist.persistencia.repositories.dto.neo4j.CentralityResultDTO;

public record DegreeResult(Ubicacion ubicacion, Double degree) {
    public static DegreeResult registrar(CentralityResultDTO dto) {
        return new DegreeResult(dto.origen().aModelo(), dto.centrality());
    }
}

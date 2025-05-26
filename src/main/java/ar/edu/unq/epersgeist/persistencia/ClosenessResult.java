package ar.edu.unq.epersgeist.persistencia;

import ar.edu.unq.epersgeist.modelo.ubicacion.Ubicacion;
import ar.edu.unq.epersgeist.persistencia.repositories.dto.neo4j.CentralityResultDTO;

public record ClosenessResult(Ubicacion ubicacion, Double closeness) {
    public static ClosenessResult registrar(CentralityResultDTO dto) {
        return new ClosenessResult(dto.origen().aModelo(), dto.centrality());
    }
}

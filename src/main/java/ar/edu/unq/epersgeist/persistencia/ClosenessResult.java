package ar.edu.unq.epersgeist.persistencia;

import ar.edu.unq.epersgeist.modelo.ubicacion.Ubicacion;
import ar.edu.unq.epersgeist.persistencia.repositories.dto.neo4j.ClosenessResultDTO;
import ar.edu.unq.epersgeist.persistencia.repositories.dto.neo4j.UbicacionNEODTO;

public record ClosenessResult(Ubicacion ubicacion, Double closeness) {
    public static ClosenessResult registrar(ClosenessResultDTO dto) {
        return new ClosenessResult(dto.getOrigen().aModelo(), dto.getCloseness());
    }
}

package ar.edu.unq.epersgeist.controller.dto.UbicacionDTO;

import ar.edu.unq.epersgeist.persistencia.ClosenessResult;
import ar.edu.unq.epersgeist.persistencia.DegreeResult;

public record CentralityResponseDTO(UbicacionDTOResponse ubicacion, Double centrality) {

    public static CentralityResponseDTO resultadoCloseness(ClosenessResult resultado) {
        return new CentralityResponseDTO(
                UbicacionDTOResponse.desdeModelo(resultado.ubicacion()),
                resultado.closeness()
        );
    }

    public static CentralityResponseDTO resultadoDegree(DegreeResult resultado) {
        return new CentralityResponseDTO(
                UbicacionDTOResponse.desdeModelo(resultado.ubicacion()),
                resultado.degree()
        );
    }
}

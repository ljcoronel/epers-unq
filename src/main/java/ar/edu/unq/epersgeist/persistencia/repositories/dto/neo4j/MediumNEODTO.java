package ar.edu.unq.epersgeist.persistencia.repositories.dto.neo4j;

import ar.edu.unq.epersgeist.modelo.Medium;
import ar.edu.unq.epersgeist.persistencia.repositories.dto.neo4j.espiritu.AngelNEODTO;
import ar.edu.unq.epersgeist.persistencia.repositories.dto.neo4j.espiritu.DemonioNEODTO;
import ar.edu.unq.epersgeist.persistencia.repositories.dto.neo4j.espiritu.EspirituNEODTO;
import ar.edu.unq.epersgeist.persistencia.repositories.dto.neo4j.ubicacion.UbicacionNEODTO;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.util.List;
import java.util.Objects;

@Node(labels = "MEDIUM")
public class MediumNEODTO {
    @Id
    private Long id;

    @Relationship(type = "TIENE_UBICACION")
    private UbicacionNEODTO ubicacion;

    @Relationship(type = "CONECTADO_CON")
    private List<AngelNEODTO> angelesConectados;

    @Relationship(type = "CONECTADO_CON")
    private List<DemonioNEODTO> demoniosConectados;

    public MediumNEODTO(Medium medium) {

        this.id = medium.getId();
        this.ubicacion = UbicacionNEODTO.desdeModelo(medium.getUbicacion());
        this.angelesConectados = medium
                .getAngeles()
                .stream()
                .map(dto -> (AngelNEODTO) AngelNEODTO.desdeModelo(dto,this)).toList();

        this.demoniosConectados = medium
                .getDemonios()
                .stream()
                .map(dto -> (DemonioNEODTO) DemonioNEODTO.desdeModelo(dto,this)).toList();
    }

    public MediumNEODTO(Medium medium,AngelNEODTO angeldto) {

        this.id = medium.getId();
        this.ubicacion = UbicacionNEODTO.desdeModelo(medium.getUbicacion());
        this.angelesConectados = medium
                .getAngeles()
                .stream()
                .map(dto -> {if(Objects.equals(dto.getId(), angeldto.getId()))return angeldto;
                    return (AngelNEODTO) AngelNEODTO.desdeModelo(dto,this);
                }).toList();

        this.demoniosConectados = medium
                .getDemonios()
                .stream()
                .map(dto -> (DemonioNEODTO) DemonioNEODTO.desdeModelo(dto,this)).toList();
    }

    public MediumNEODTO(Medium medium,DemonioNEODTO demoniodto) {

        this.id = medium.getId();
        this.ubicacion = UbicacionNEODTO.desdeModelo(medium.getUbicacion());
        this.angelesConectados = medium
                .getAngeles()
                .stream()
                .map(dto -> (AngelNEODTO) AngelNEODTO.desdeModelo(dto,this)).toList();

        this.demoniosConectados = medium
                .getDemonios()
                .stream()
                .map(dto -> {if(Objects.equals(dto.getId(), demoniodto.getId()))return demoniodto;
                    return (DemonioNEODTO) DemonioNEODTO.desdeModelo(dto,this);
                }).toList();
    }

    public static MediumNEODTO desdeModelo(Medium medium) {
        if(medium == null)return null;
        return new MediumNEODTO(medium);
    }

    public static MediumNEODTO desdeModelo(Medium medium,AngelNEODTO angelNEODTO) {
        if(medium == null)return null;
        return new MediumNEODTO(medium,angelNEODTO);
    }

    public static MediumNEODTO desdeModelo(Medium medium,DemonioNEODTO demonioNEODTO) {
        if(medium == null)return null;
        return new MediumNEODTO(medium,demonioNEODTO);
    }

}

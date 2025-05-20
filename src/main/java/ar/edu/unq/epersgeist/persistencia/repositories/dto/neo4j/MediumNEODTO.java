package ar.edu.unq.epersgeist.persistencia.repositories.dto.neo4j;


import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.stereotype.Repository;

@Node("Medium")
public class MediumNEODTO {
    @Id
    private Long id;
}

package ar.edu.unq.epersgeist.persistencia;

import ar.edu.unq.epersgeist.modelo.espiritu.Espiritu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EspirituDAO extends JpaRepository<Espiritu, Long> {
//    Espiritu crear(Espiritu espiritu);
//    Optional<Espiritu> recuperar(Long idDelEspiritu);
//    List<Espiritu> recuperarTodos();
//    void eliminar(Long idDelEspiritu);
//    void actualizar(Espiritu espiritu);

    // ^ ya no es necesario porque Spring con el JpaRepository ya maneja crud, también recuperar todos

//    List<Espiritu> todosLosEspiritusDeMedium(Long mediumId);
//    ^ te dejo este para que hagas


//    <T extends Espiritu> List<T> espiritusDeTipo(@Param("clase") Sort.Direction direccion, int pagina, int cantidadPorPagina, Class<T> clase);

//    ^ esto ya no sirve, en el service ya hay un método que hace lo mismo. (get all permite sorting y pagination)

}
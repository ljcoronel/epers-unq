package ar.edu.unq.epersgeist.modelo.exception;

public class RecursoNoEncontradoException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public RecursoNoEncontradoException(String className, Long id) {

        super("No se encontro el recurso "+className+" con Id: "+id+".");
    }
}

package ar.edu.unq.epersgeist.modelo;

public enum Direccion {
    ASCENDENTE("asc"),
    DESCENDENTE("desc");

    public final String value;

    Direccion(String direccion) {
        this.value = direccion;
    }
}

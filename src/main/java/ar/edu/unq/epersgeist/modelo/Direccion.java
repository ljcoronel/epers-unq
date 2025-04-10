package ar.edu.unq.epersgeist.modelo;

import org.hibernate.grammars.hql.HqlParser;
import org.hibernate.query.SortDirection;

//public enum Direccion {
//    ASCENDENTE(SortDirection.ASCENDING),
//    DESCENDENTE(SortDirection.DESCENDING);
//
//    public final SortDirection value;
//
//    Direccion(SortDirection direccion) {
//        this.value = direccion;
//    }
//}

public enum Direccion {
    ASCENDENTE("asc"),
    DESCENDENTE("desc");

    public final String value;

    Direccion(String direccion) {
        this.value = direccion;
    }
}

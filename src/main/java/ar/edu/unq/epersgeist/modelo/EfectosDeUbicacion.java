package ar.edu.unq.epersgeist.modelo;

import ar.edu.unq.epersgeist.modelo.ubicacion.Cementerio;
import ar.edu.unq.epersgeist.modelo.ubicacion.Santuario;

public interface EfectosDeUbicacion {
    public void recibirEfectoDe(Cementerio ubicacion);
    public void recibirEfectoDe(Santuario ubicacion);
}


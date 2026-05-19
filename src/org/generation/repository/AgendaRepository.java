package org.generation.repository;

import org.generation.model.Contacto;
import java.util.Set;

// Interface que expone los métodos públicos de la agenda
public interface AgendaRepository {
    void aniadirContacto(Contacto c);
    boolean existeContacto(Contacto c);
    Set<Contacto> listarContactos();
    Contacto buscaContacto(String nombre);
    void eliminarContacto(Contacto c);
    boolean agendaLlena();
    int espaciosLibres();
    void modificarContacto(String nombreBuscado, String apellidoBuscado, String nuevoNombre, String nuevoApellido, String nuevoTelefono);
}

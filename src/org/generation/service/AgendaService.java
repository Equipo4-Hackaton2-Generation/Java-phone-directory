package org.generation.service;

import org.generation.exceptions.CampoInvalidoException;
import org.generation.exceptions.ContactoNotFoundException;
import org.generation.model.Contacto;
import org.generation.repository.AgendaRepository;

import java.util.*;

public class AgendaService implements AgendaRepository {

    private static final int CAPACIDAD_MAXIMA = 10;
    private final Set<Contacto> contactos = new LinkedHashSet<>();

    // -------------------------------------------------------
    // Validaciones de entrada
    // -------------------------------------------------------

    /**
     * Valida que un campo de texto no sea nulo, vacío ni contenga caracteres inválidos.
     * Solo letras y espacios permitidos para nombre/apellido.
     */
    public static void validarTexto(String campo, String nombreCampo) {
        if (campo == null || campo.trim().isEmpty()) {
            throw new CampoInvalidoException("El campo '" + nombreCampo + "' no puede estar vacío.");
        }
        if (!campo.trim().matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ]+")) {
            throw new CampoInvalidoException("El campo '" + nombreCampo + "' contiene caracteres inválidos. Solo se permiten letras.");
        }
    }

    /**
     * Valida que el teléfono sea un número entero de exactamente 10 dígitos.
     * Como int en Java puede tener hasta 10 dígitos, validamos con String.valueOf.
     */

    public static void validarTelefono(String telefono) {
        if (telefono == null || telefono.trim().isEmpty()) {
            throw new CampoInvalidoException("El campo teléfono no puede estar vacío.");
        }
        if (telefono.length() != 10) {
            throw new CampoInvalidoException("El teléfono debe tener exactamente 10 dígitos. Se ingresaron: " + telefono.length());
        }
        if (!telefono.trim().matches("[0-9]+")) {
            throw new CampoInvalidoException("El teléfono contiene caracteres inválidos. Solo se permiten números.");
        }
    }

    // -------------------------------------------------------
    // Implementación de AgendaRepository
    // -------------------------------------------------------

    /**
     * Añade un contacto a la agenda.
     * Lanza excepción si la agenda está llena o si el contacto ya existe.
     */
    @Override
    public void aniadirContacto(Contacto c) {
        validarTexto(c.getNombre(), "Nombre");
        validarTexto(c.getApellido(), "Apellido");
        validarTelefono(c.getTelefono());

        if (agendaLlena()) {
            throw new IllegalStateException("La agenda está llena. No se pueden agregar más contactos.");
        }
        if (existeContacto(c)) {
            throw new IllegalArgumentException("El contacto '" + c.getNombre() + " " + c.getApellido() + "' ya existe en la agenda. Se puede repetir nombre o apellido por separado, pero no ambos iguales.");
        }
        contactos.add(c);
        int libres = espaciosLibres();
        if (libres == 0) {
            System.out.println("Contacto agregado. La agenda está LLENA.");
        } else {
            System.out.println("Contacto agregado. Puedes agregar " + libres + " contacto(s) más.");
        }
    }

    /**
     * Indica si un contacto existe en la agenda (comparación por nombre + apellido).
     */
    @Override
    public boolean existeContacto(Contacto c) {
        return contactos.contains(c);
    }

    /**
     * Retorna todos los contactos de la agenda.
     */
    @Override
    public Set<Contacto> listarContactos() {
        Set<Contacto> ordenados = new TreeSet<>(
                Comparator.comparing((Contacto c) -> c.getNombre().toLowerCase())
                        .thenComparing(c -> c.getApellido().toLowerCase())
        );
        ordenados.addAll(contactos);
        return ordenados;
    }

    /**
     * Busca un contacto por nombre y lo retorna.
     * Lanza ContactoNotFoundException si no existe.
     */
    @Override
    public Contacto buscaContacto(String nombre) {
        validarTexto(nombre, "Nombre de búsqueda");
        for (Contacto c : contactos) {
            if (c.getNombre().equalsIgnoreCase(nombre.trim())) {
                return c;
            }
        }
        throw new ContactoNotFoundException("No se encontró ningún contacto con el nombre: '" + nombre + "'");
    }

    /**
     * Elimina un contacto de la agenda.
     * Indica por pantalla si fue eliminado o no.
     */
    @Override
    public void eliminarContacto(Contacto c) {
        if (contactos.remove(c)) {
            System.out.println("Contacto '" + c.getNombre() + "' eliminado correctamente.");
        } else {
            System.out.println("No se encontró el contacto '" + c.getNombre() + "' para eliminar.");
        }
    }

    /**
     * Indica si la agenda está llena (10 contactos máximo).
     */
    @Override
    public boolean agendaLlena() {
        return contactos.size() >= CAPACIDAD_MAXIMA;
    }

    /**
     * Retorna cuántos contactos más se pueden agregar.
     */
    @Override
    public int espaciosLibres() {
        return CAPACIDAD_MAXIMA - contactos.size();
    }

    /**
     * Modifica nombre, apellido o teléfono de un contacto existente.
     */
    @Override
    public void modificarContacto(String nombreBuscado, String apellidoBuscado, String nuevoNombre, String nuevoApellido, String nuevoTelefono) {
        validarTexto(nuevoNombre, "Nuevo Nombre");
        validarTexto(nuevoApellido, "Nuevo Apellido");
        validarTelefono(nuevoTelefono);

        // Verificar que la combinación nueva nombre+apellido no exista ya (excepto el propio contacto)
        Contacto posibleDuplicado = new Contacto(nuevoNombre.trim(), nuevoApellido.trim(), nuevoTelefono.trim());
        // Buscar si ya existe ESA combinación y NO es el mismo contacto que estamos modificando
        for (Contacto c : contactos) {
            if (c.equals(posibleDuplicado) && !c.getNombre().equalsIgnoreCase(nombreBuscado)) {
                throw new IllegalArgumentException("Ya existe un contacto con nombre '" + nuevoNombre + "' y apellido '" + nuevoApellido + "'.");
            }
        }

        // Buscar el contacto y modificarlo (remover + reinsertar para actualizar el Set)
        Iterator<Contacto> it = contactos.iterator();
        while (it.hasNext()) {
            Contacto c = it.next();
            if (c.getNombre().equalsIgnoreCase(nombreBuscado) && c.getApellido().equalsIgnoreCase(apellidoBuscado)) {
                it.remove();
                c.setNombre(nuevoNombre.trim());
                c.setApellido(nuevoApellido.trim());
                c.setTelefono(nuevoTelefono.trim());
                contactos.add(c);
                System.out.println("Contacto modificado correctamente.");
                return;
            }
        }
        throw new ContactoNotFoundException("No se encontró el contacto '" + nombreBuscado + " " + apellidoBuscado + "' para modificar.");
    }

    public int getCapacidadMaxima() {
        return CAPACIDAD_MAXIMA;
    }

    public int getTotalContactos() {
        return contactos.size();
    }
}

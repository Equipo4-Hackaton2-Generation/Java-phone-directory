package org.generation.exceptions;

// Excepción lanzada cuando no se encuentra un contacto
public class ContactoNotFoundException extends RuntimeException {
    public ContactoNotFoundException(String mensaje) {
        super(mensaje);
    }
}

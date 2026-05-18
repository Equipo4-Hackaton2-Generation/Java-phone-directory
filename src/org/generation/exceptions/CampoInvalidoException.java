package org.generation.exceptions;

// Excepción lanzada cuando hay campos vacíos o con caracteres inválidos
public class CampoInvalidoException extends RuntimeException {
    public CampoInvalidoException(String mensaje) {
        super(mensaje);
    }
}

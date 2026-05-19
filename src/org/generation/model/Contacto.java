package org.generation.model;

public class Contacto {

    private String nombre;
    private String apellido;
    private String telefono;

    public Contacto(String nombre, String apellido, String telefono) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.telefono = telefono;
    }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    // Dos contactos son iguales solo si tienen el mismo nombre Y apellido (ambos deben coincidir)
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Contacto)) return false;
        Contacto otro = (Contacto) obj;
        return this.nombre.equalsIgnoreCase(otro.nombre)
            && this.apellido.equalsIgnoreCase(otro.apellido);
    }

    @Override
    public int hashCode() {
        return (nombre.toLowerCase() + "|" + apellido.toLowerCase()).hashCode();
    }

    @Override
    public String toString() {
        return "Contacto{nombre='" + nombre + "', apellido='" + apellido + "', telefono=" + telefono + "}";
    }
}

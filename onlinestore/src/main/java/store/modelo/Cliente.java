package store.modelo;

public abstract class Cliente {

    // Variables
    private final String nombre;
    private final String domicilio;
    private final String nif;
    private final String email;

    // Constructor
    public Cliente(String nombre, String domicilio, String nif, String email) {
        this.nombre = nombre;
        this.domicilio = domicilio;
        this.nif = nif;
        this.email = email;
    }

    // Getters
    public String getNombre() {
        return nombre;
    }

    public String getDomicilio() {
        return domicilio;
    }

    public String getNif() {
        return nif;
    }

    public String getEmail() {
        return email;
    }

    // toString
    @Override
    public String toString() {
        return nombre + " (Email: " + email + ")";
    }
}
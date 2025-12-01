package store.modelo;

public class ClienteEstandar extends Cliente {

    // Constructores
    public ClienteEstandar(String nombre, String domicilio, String nif, String email) {
        super(nombre, domicilio, nif, email);
    }

    // toString
    @Override
    public String toString() {
        return super.toString() + " - Estándar";
    }
}

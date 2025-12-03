package store.modelo;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "cliente_estandar")
public class ClienteEstandar extends Cliente {

    // Constructor vacío requerido por JPA
    public ClienteEstandar() {}

    public ClienteEstandar(String nombre, String domicilio, String nif, String email) {
        super(nombre, domicilio, nif, email);
    }

    @Override
    public String toString() {
        return super.toString() + " - Estándar";
    }
}
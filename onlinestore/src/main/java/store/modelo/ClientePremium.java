package store.modelo;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "cliente_premium")
public class ClientePremium extends Cliente {

    // Variables (NO se guardan en BD porque son static final)
    private static final double CUOTA_ANUAL = 30.0;
    private static final double DESCUENTO_ENVIO = 0.2; // 20%

    // ⚠️ Constructor vacío obligatorio para Hibernate
    public ClientePremium() {}

    // Constructor normal
    public ClientePremium(String nombre, String domicilio, String nif, String email) {
        super(nombre, domicilio, nif, email);
    }

    // Getters
    public static double getCuotaAnual() {
        return CUOTA_ANUAL;
    }

    public static double getDescuentoEnvio() {
        return DESCUENTO_ENVIO;
    }

    @Override
    public String toString() {
        return super.toString() + " - Premium";
    }
}
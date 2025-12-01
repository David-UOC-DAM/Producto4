package store.modelo;

public class ClientePremium extends Cliente {

    // Variables
    private static final double CUOTA_ANUAL = 30.0;
    private static final double DESCUENTO_ENVIO = 0.2; // 20%

    // Constructores
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

    // toString
    @Override
    public String toString() {
        return super.toString() + " - Premium";
    }
}

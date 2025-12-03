package store.modelo;

public class PedidoNoCancelableException extends Exception {
    public PedidoNoCancelableException(String mensaje) {
        super(mensaje);
    }
}
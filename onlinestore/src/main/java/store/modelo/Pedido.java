package store.modelo;

import java.time.LocalDateTime;
import java.time.Duration;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class Pedido {

    // Variables
    private final int numeroPedido;
    private final Cliente cliente;
    private final Articulo articulo;
    private final int cantidad;
    private final LocalDateTime fechaHora;

    // Formateador para mostrar fecha y hora: dd/MM/yyyy HH:mm:ss
    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    // Constructores
    public Pedido(int numeroPedido, Cliente cliente, Articulo articulo, int cantidad, LocalDateTime fechaHora) {
        this.numeroPedido = numeroPedido;
        this.cliente = cliente;
        this.articulo = articulo;
        this.cantidad = cantidad;
        // si fechaHora es null, usar ahora; si se pasa una fecha, usarla
        this.fechaHora = (fechaHora != null) ? fechaHora : LocalDateTime.now();
    }

    // Getters
    public int getNumeroPedido() {
        return numeroPedido;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public Articulo getArticulo() {
        return articulo;
    }

    public int getCantidad() {
        return cantidad;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    // Métodos

    /**
     * Calcula el precio del pedido en función de la cantidad, los gastos de envío y el descuento aplicado a los cliente premium
     */
    public double calcularPrecioPedido() {
        double precioArticulos = articulo.getPrecioVenta() * cantidad;
        double gastosEnvioOriginal = articulo.getGastosEnvio();
        double gastosEnvioFinal = gastosEnvioOriginal;

        if (cliente instanceof ClientePremium) {
            gastosEnvioFinal = gastosEnvioOriginal * (1 - ClientePremium.getDescuentoEnvio());
        }

        return precioArticulos + gastosEnvioFinal;
    }

    /**
     * Comprueba si el pedido se puede cancelar
     * Solo es cancelable si no ha transcurrido el tiempo de preparación
     */
    public boolean puedeCancelar() {
        LocalDateTime ahora = LocalDateTime.now();
        long minutosTranscurridos = Duration.between(fechaHora, ahora).toMinutes();
        return minutosTranscurridos < articulo.getTiempoPreparacion();
    }

    // toString formateado
    @Override
    public String toString() {
        String fechaFormateada = fechaHora.format(FORMATTER);
        // Formateo del total con 2 decimales (usa Locale para separador decimal correcto si quieres)
        String totalFormateado = String.format(Locale.getDefault(), "%.2f", calcularPrecioPedido());

        return "Pedido #" + numeroPedido +
                " | Cliente: " + cliente.getEmail() +
                " | Artículo: " + articulo.getCodigo() +
                " | Cantidad: " + cantidad +
                " | Fecha: " + fechaFormateada +
                " | Total: " + totalFormateado + " €";
    }
}
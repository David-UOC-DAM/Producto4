package store.modelo;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.time.Duration;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Entity
@Table(name = "pedido")
public class Pedido {

    // ---------- CAMPOS ----------
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "numero")
    private int numeroPedido;

    @ManyToOne
    @JoinColumn(name = "email_cliente", nullable = false)
    private Cliente cliente;

    @ManyToOne
    @JoinColumn(name = "codigo_articulo", nullable = false)
    private Articulo articulo;

    @Column(nullable = false)
    private int cantidad;

    @Column(name = "fecha", nullable = false)
    private LocalDateTime fechaHora;

    @Column(nullable = false)
    private boolean enviado = false;

    // Formateador
    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    // ---------- CONSTRUCTORES ----------
    public Pedido() {}

    public Pedido(int numeroPedido, Cliente cliente, Articulo articulo,
                  int cantidad, LocalDateTime fechaHora) {
        this.numeroPedido = numeroPedido;
        this.cliente = cliente;
        this.articulo = articulo;
        this.cantidad = cantidad;
        this.fechaHora = (fechaHora != null) ? fechaHora : LocalDateTime.now();
    }

    // ---------- GETTERS Y SETTERS ----------
    public int getNumeroPedido() {
        return numeroPedido;
    }

    public void setNumeroPedido(int numeroPedido) {
        this.numeroPedido = numeroPedido;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Articulo getArticulo() {
        return articulo;
    }

    public void setArticulo(Articulo articulo) {
        this.articulo = articulo;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(LocalDateTime fechaHora) {
        this.fechaHora = fechaHora;
    }

    public boolean isEnviado() {
        return enviado;
    }

    public void setEnviado(boolean enviado) {
        this.enviado = enviado;
    }

    // ---------- LÓGICA ----------
    public double calcularPrecioPedido() {
        double precioArticulos = articulo.getPrecioVenta() * cantidad;
        double gastosEnvio = articulo.getGastosEnvio();

        if (cliente instanceof ClientePremium) {
            gastosEnvio *= (1 - ClientePremium.getDescuentoEnvio());
        }

        return precioArticulos + gastosEnvio;
    }

    public boolean puedeCancelar() {
        long minutos = Duration.between(fechaHora, LocalDateTime.now()).toMinutes();
        return minutos < articulo.getTiempoPreparacion();
    }

    // ---------- toString ----------
    @Override
    public String toString() {
        String fechaFormateada = fechaHora.format(FORMATTER);
        String totalFormateado = String.format(Locale.getDefault(), "%.2f", calcularPrecioPedido());

        return "Pedido #" + numeroPedido +
                " | Cliente: " + cliente.getEmail() +
                " | Artículo: " + articulo.getCodigo() +
                " | Cantidad: " + cantidad +
                " | Fecha: " + fechaFormateada +
                " | Enviado: " + enviado +
                " | Total: " + totalFormateado + " €";
    }
}
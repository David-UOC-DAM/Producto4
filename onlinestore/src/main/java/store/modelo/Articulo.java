package store.modelo;

import jakarta.persistence.*;

@Entity
@Table(name = "articulo")
public class Articulo {

    @Id
    private String codigo;

    @Column(name = "descripcion")
    private String descripcion;

    @Column(name = "precio")
    private double precioVenta;

    @Column(name = "envio")
    private double gastosEnvio;

    @Column(name = "tiempo_preparacion")
    private int tiempoPreparacion;


    // Constructor vacío obligatorio JPA
    public Articulo() {}

    public Articulo(String codigo, String descripcion, double precioVenta,
                    double gastosEnvio, int tiempoPreparacion) {
        this.codigo = codigo;
        this.descripcion = descripcion;
        this.precioVenta = precioVenta;
        this.gastosEnvio = gastosEnvio;
        this.tiempoPreparacion = tiempoPreparacion;
    }

    public String getCodigo() { return codigo; }
    public String getDescripcion() { return descripcion; }
    public double getPrecioVenta() { return precioVenta; }
    public double getGastosEnvio() { return gastosEnvio; }
    public int getTiempoPreparacion() { return tiempoPreparacion; }

    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public void setPrecioVenta(double precioVenta) { this.precioVenta = precioVenta; }
    public void setGastosEnvio(double gastosEnvio) { this.gastosEnvio = gastosEnvio; }
    public void setTiempoPreparacion(int tiempoPreparacion) { this.tiempoPreparacion = tiempoPreparacion; }

    @Override
    public String toString() {
        return "Código: " + codigo +
                " | Descripción: " + descripcion +
                " | Precio: " + precioVenta + " €" +
                " | Envío: " + gastosEnvio + " €" +
                " | Preparación: " + tiempoPreparacion + " min";
    }
}

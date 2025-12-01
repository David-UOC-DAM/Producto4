package store;

import store.modelo.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class PedidoTest {

    @Test
    @DisplayName("Calcular precio de pedido para cliente estándar")
    void testCalcularPrecioPedidoClienteEstandar() {
        Articulo articulo = new Articulo("A1", "Ratón", 10.0, 5.0, 60);
        Cliente cliente = new ClienteEstandar("Ana", "Calle A", "123", "ana@mail.com");
        Pedido pedido = new Pedido(1, cliente, articulo, 2, LocalDateTime.now());

        // 2 artículos a 10 € + 5 € envío = 25 €
        double totalEsperado = 25.0;
        assertEquals(totalEsperado, pedido.calcularPrecioPedido(), 0.01,
                "El precio calculado para cliente estándar es incorrecto");
    }

    @Test
    @DisplayName("Calcular precio de pedido para cliente premium")
    void testCalcularPrecioPedidoClientePremium() {
        Articulo articulo = new Articulo("A2", "Teclado", 20.0, 10.0, 60);
        Cliente cliente = new ClientePremium("Luis", "Calle B", "456", "luis@mail.com");
        Pedido pedido = new Pedido(2, cliente, articulo, 3, LocalDateTime.now());

        // 3 artículos a 20 € = 60 €
        // envío 10 € con 20% descuento = 8 €
        double totalEsperado = 68.0;
        assertEquals(totalEsperado, pedido.calcularPrecioPedido(), 0.01,
                "El precio calculado para cliente premium es incorrecto");
    }

    @Test
    @DisplayName("Verificar si un pedido puede cancelarse según el tiempo de preparación")
    void testPuedeCancelar() {
        Articulo articulo = new Articulo("A3", "Monitor", 100.0, 15.0, 60); // 60 min preparación
        Cliente cliente = new ClienteEstandar("Marta", "Calle C", "789", "marta@mail.com");

        // main.java.store.modelo.Pedido creado ahora
        Pedido pedidoReciente = new Pedido(3, cliente, articulo, 1, LocalDateTime.now());
        assertTrue(pedidoReciente.puedeCancelar(), "main.java.store.modelo.Pedido recién creado debería ser cancelable");

        // main.java.store.modelo.Pedido creado hace 2 horas (120 min)
        LocalDateTime hace2Horas = LocalDateTime.now().minusHours(2);
        Pedido pedidoAntiguo = new Pedido(4, cliente, articulo, 1, hace2Horas);
        assertFalse(pedidoAntiguo.puedeCancelar(), "main.java.store.modelo.Pedido con tiempo de preparación superado NO debería ser cancelable");
    }
}
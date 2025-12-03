package store.dao;

import store.modelo.Pedido;
import java.util.List;

public interface PedidoDAO {
    void insertarPedido(Pedido pedido);
    Pedido obtenerPedidoPorNumero(int numero);
    List<Pedido> obtenerTodosLosPedidos();
    void eliminarPedido(int numero);
    List<Pedido> obtenerPedidosPendientes(String email);
    List<Pedido> obtenerPedidosEnviados(String email);
    void actualizarEstadoPedidos();
}
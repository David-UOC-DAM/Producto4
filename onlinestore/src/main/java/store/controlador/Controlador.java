package store.controlador;

import store.dao.ArticuloDAO;
import store.dao.ClienteDAO;
import store.dao.PedidoDAO;
import store.dao.jpa.ArticuloDAOJPA;
import store.dao.jpa.ClienteDAOJPA;
import store.dao.jpa.PedidoDAOJPA;
import store.modelo.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Controlador {

    // DAOs basados en JPA
    private final ArticuloDAO articuloDAO = new ArticuloDAOJPA();
    private final ClienteDAO clienteDAO = new ClienteDAOJPA();
    private final PedidoDAO pedidoDAO = new PedidoDAOJPA();

    // ---------- ARTÍCULOS ----------
    public void anadirArticulo(String codigo, String descripcion, double precio, double envio, int tiempo) {
        Articulo articulo = new Articulo(codigo, descripcion, precio, envio, tiempo);
        articuloDAO.insertarArticulo(articulo);
    }

    public List<Articulo> getArticulos() {
        return articuloDAO.obtenerTodosLosArticulos();
    }

    // ---------- CLIENTES ----------
    public void anadirCliente(String nombre, String domicilio, String nif, String email, boolean premium) {
        Cliente cliente = premium
                ? new ClientePremium(nombre, domicilio, nif, email)
                : new ClienteEstandar(nombre, domicilio, nif, email);

        clienteDAO.insertarCliente(cliente);
    }

    public Map<String, Cliente> getClientes() {
        Map<String, Cliente> mapa = new HashMap<>();
        for (Cliente c : clienteDAO.obtenerTodosLosClientes()) {
            mapa.put(c.getEmail(), c);
        }
        return mapa;
    }

    public List<Cliente> getClientesEstandar() {
        return clienteDAO.obtenerClientesEstandar();
    }

    public List<Cliente> getClientesPremium() {
        return clienteDAO.obtenerClientesPremium();
    }

    // ---------- PEDIDOS ----------
    public void anadirPedido(String emailCliente, String codigoArticulo, int cantidad)
            throws ClienteNoEncontradoException, ArticuloNoEncontradoException {

        Cliente cliente = clienteDAO.obtenerClientePorEmail(emailCliente);
        if (cliente == null)
            throw new ClienteNoEncontradoException("Cliente no encontrado: " + emailCliente);

        Articulo articulo = articuloDAO.obtenerArticuloPorCodigo(codigoArticulo);
        if (articulo == null)
            throw new ArticuloNoEncontradoException("Artículo no encontrado: " + codigoArticulo);

        Pedido pedido = new Pedido(0, cliente, articulo, cantidad, LocalDateTime.now());
        pedidoDAO.insertarPedido(pedido);
    }

    public void eliminarPedido(int numPedido) throws PedidoNoCancelableException {

        Pedido pedido = pedidoDAO.obtenerPedidoPorNumero(numPedido);
        if (pedido == null) return;

        // JPA devuelve el Pedido como entidad completa → podemos usar su método de negocio
        if (!pedido.puedeCancelar()) {
            throw new PedidoNoCancelableException("No se puede eliminar. Pedido enviado o en preparación.");
        }

        pedidoDAO.eliminarPedido(numPedido);
    }

    public List<Pedido> getPedidosPendientes(String email) {
        return pedidoDAO.obtenerPedidosPendientes(email);
    }

    public List<Pedido> getPedidosEnviados(String email) {
        return pedidoDAO.obtenerPedidosEnviados(email);
    }

    public boolean existeCliente(String email) {
        return clienteDAO.obtenerClientePorEmail(email) != null;
    }

}

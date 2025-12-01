package store.dao.mysql;

import store.dao.PedidoDAO;
import store.modelo.*;
import store.util.ConexionBD;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class MySQLPedidoDAO implements PedidoDAO {

    @Override
    public void insertarPedido(Pedido pedido) {
        String sql = "INSERT INTO pedido (email_cliente, codigo_articulo, cantidad, fecha) VALUES (?, ?, ?, ?)";

        try (Connection con = ConexionBD.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, pedido.getCliente().getEmail());
            ps.setString(2, pedido.getArticulo().getCodigo());
            ps.setInt(3, pedido.getCantidad());
            ps.setTimestamp(4, Timestamp.valueOf(pedido.getFechaHora()));

            ps.executeUpdate();
            System.out.println("✅ Pedido insertado correctamente.");

        } catch (SQLException e) {
            System.err.println("❌ Error al insertar pedido: " + e.getMessage());
        }
    }

    @Override
    public Pedido obtenerPedidoPorNumero(int numero) {
        String sql = "SELECT * FROM pedido WHERE numero = ?";
        Pedido pedido = null;

        try (Connection con = ConexionBD.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, numero);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Cliente cliente = new ClienteEstandar("Desconocido", "", "", rs.getString("email_cliente"));
                Articulo articulo = new Articulo(
                        rs.getString("codigo_articulo"),
                        "Desconocido", 0.0, 0.0, 0
                );

                pedido = new Pedido(
                        rs.getInt("numero"),
                        cliente,
                        articulo,
                        rs.getInt("cantidad"),
                        rs.getTimestamp("fecha").toLocalDateTime()
                );
            }

        } catch (SQLException e) {
            System.err.println("❌ Error al obtener pedido: " + e.getMessage());
        }

        return pedido;
    }

    @Override
    public List<Pedido> obtenerTodosLosPedidos() {
        String sql = "SELECT * FROM pedido";
        List<Pedido> lista = new ArrayList<>();

        try (Connection con = ConexionBD.getConexion();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                Cliente cliente = new ClienteEstandar("Desconocido", "", "", rs.getString("email_cliente"));
                Articulo articulo = new Articulo(
                        rs.getString("codigo_articulo"),
                        "Desconocido", 0.0, 0.0, 0
                );

                Pedido pedido = new Pedido(
                        rs.getInt("numero"),
                        cliente,
                        articulo,
                        rs.getInt("cantidad"),
                        rs.getTimestamp("fecha").toLocalDateTime()
                );
                lista.add(pedido);
            }

        } catch (SQLException e) {
            System.err.println("❌ Error al obtener pedidos: " + e.getMessage());
        }

        return lista;
    }

    @Override
    public void eliminarPedido(int numero) {
        String sql = "DELETE FROM pedido WHERE numero = ?";

        try (Connection con = ConexionBD.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, numero);
            int filas = ps.executeUpdate();

            if (filas > 0) {
                System.out.println("✅ Pedido eliminado correctamente.");
            }

        } catch (SQLException e) {
            System.err.println("❌ Error al eliminar pedido: " + e.getMessage());
        }

    }

    @Override
    public List<Pedido> obtenerPedidosPendientes(String email) {
        return obtenerPedidosPorEstado(email, true);
    }

    @Override
    public List<Pedido> obtenerPedidosEnviados(String email) {
        return obtenerPedidosPorEstado(email, false);
    }

    private List<Pedido> obtenerPedidosPorEstado(String email, boolean pendientes) {
        List<Pedido> lista = new ArrayList<>();

        String sql = """
                SELECT p.numero, p.email_cliente, p.codigo_articulo, p.cantidad, p.fecha,
                       a.descripcion, a.precio, a.envio, a.tiempo_preparacion
                FROM pedido p
                JOIN articulo a ON p.codigo_articulo = a.codigo
                """ + (email != null && !email.isEmpty() ? "WHERE p.email_cliente = ?" : "");

        try (Connection con = ConexionBD.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            if (email != null && !email.isEmpty()) {
                ps.setString(1, email);
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Cliente cliente = new ClienteEstandar("Desconocido", "", "", rs.getString("email_cliente"));
                    Articulo articulo = new Articulo(
                            rs.getString("codigo_articulo"),
                            rs.getString("descripcion"),
                            rs.getDouble("precio"),
                            rs.getDouble("envio"),
                            rs.getInt("tiempo_preparacion")
                    );

                    LocalDateTime fecha = rs.getTimestamp("fecha").toLocalDateTime();
                    Pedido pedido = new Pedido(
                            rs.getInt("numero"),
                            cliente,
                            articulo,
                            rs.getInt("cantidad"),
                            fecha
                    );

                    // Determinamos si es pendiente o enviado
                    boolean aunPendiente = fecha.plusMinutes(articulo.getTiempoPreparacion()).isAfter(LocalDateTime.now());

                    if ((pendientes && aunPendiente) || (!pendientes && !aunPendiente)) {
                        lista.add(pedido);
                    }
                }
            }

        } catch (SQLException e) {
            System.err.println("❌ Error al obtener pedidos (" + (pendientes ? "pendientes" : "enviados") + "): " + e.getMessage());
        }

        return lista;
    }
}
package store.dao.mysql;

import store.dao.ClienteDAO;
import store.modelo.Cliente;
import store.modelo.ClienteEstandar;
import store.modelo.ClientePremium;
import store.util.ConexionBD;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MySQLClienteDAO implements ClienteDAO {

    @Override
    public void insertarCliente(Cliente cliente) {
        String sql = "INSERT INTO cliente (email, nombre, domicilio, nif, tipo) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, cliente.getEmail());
            ps.setString(2, cliente.getNombre());
            ps.setString(3, cliente.getDomicilio());
            ps.setString(4, cliente.getNif());
            ps.setString(5, (cliente instanceof ClientePremium) ? "PREMIUM" : "ESTANDAR");

            ps.executeUpdate();
            System.out.println("✅ Cliente insertado correctamente.");

        } catch (SQLException e) {
            System.err.println("❌ Error al insertar cliente: " + e.getMessage());
        }
    }

    @Override
    public Cliente obtenerClientePorEmail(String email) {
        String sql = "SELECT * FROM cliente WHERE email = ?";
        Cliente cliente = null;

        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String tipo = rs.getString("tipo");
                    if ("PREMIUM".equalsIgnoreCase(tipo)) {
                        cliente = new ClientePremium(
                                rs.getString("nombre"),
                                rs.getString("domicilio"),
                                rs.getString("nif"),
                                rs.getString("email")
                        );
                    } else {
                        cliente = new ClienteEstandar(
                                rs.getString("nombre"),
                                rs.getString("domicilio"),
                                rs.getString("nif"),
                                rs.getString("email")
                        );
                    }
                }
            }

        } catch (SQLException e) {
            System.err.println("❌ Error al obtener cliente: " + e.getMessage());
        }

        return cliente;
    }

    @Override
    public List<Cliente> obtenerTodosLosClientes() {
        List<Cliente> lista = new ArrayList<>();
        String sql = "SELECT * FROM cliente";

        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                String tipo = rs.getString("tipo");
                Cliente cliente;
                if ("PREMIUM".equalsIgnoreCase(tipo)) {
                    cliente = new ClientePremium(
                            rs.getString("nombre"),
                            rs.getString("domicilio"),
                            rs.getString("nif"),
                            rs.getString("email")
                    );
                } else {
                    cliente = new ClienteEstandar(
                            rs.getString("nombre"),
                            rs.getString("domicilio"),
                            rs.getString("nif"),
                            rs.getString("email")
                    );
                }
                lista.add(cliente);
            }

        } catch (SQLException e) {
            System.err.println("❌ Error al listar clientes: " + e.getMessage());
        }

        return lista;
    }

    @Override
    public void eliminarCliente(String email) {
        String sql = "DELETE FROM cliente WHERE email = ?";

        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, email);
            int filas = ps.executeUpdate();

            if (filas > 0) {
                System.out.println("✅ Cliente eliminado correctamente.");
            } else {
                System.out.println("⚠️ No se encontró ningún cliente con ese email.");
            }

        } catch (SQLException e) {
            System.err.println("❌ Error al eliminar cliente: " + e.getMessage());
        }
    }

    @Override
    public List<Cliente> obtenerClientesEstandar() {
        return obtenerClientesPorTipo("ESTANDAR");
    }

    @Override
    public List<Cliente> obtenerClientesPremium() {
        return obtenerClientesPorTipo("PREMIUM");
    }

    // Método auxiliar privado
    private List<Cliente> obtenerClientesPorTipo(String tipoBuscado) {
        List<Cliente> lista = new ArrayList<>();
        String sql = "SELECT * FROM cliente WHERE tipo = ?";

        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, tipoBuscado);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Cliente cliente;
                    if ("PREMIUM".equalsIgnoreCase(tipoBuscado)) {
                        cliente = new ClientePremium(
                                rs.getString("nombre"),
                                rs.getString("domicilio"),
                                rs.getString("nif"),
                                rs.getString("email")
                        );
                    } else {
                        cliente = new ClienteEstandar(
                                rs.getString("nombre"),
                                rs.getString("domicilio"),
                                rs.getString("nif"),
                                rs.getString("email")
                        );
                    }
                    lista.add(cliente);
                }
            }

        } catch (SQLException e) {
            System.err.println("❌ Error al obtener clientes " + tipoBuscado + ": " + e.getMessage());
        }

        return lista;
    }
}

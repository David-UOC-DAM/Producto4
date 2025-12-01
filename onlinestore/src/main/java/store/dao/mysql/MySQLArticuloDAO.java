package store.dao.mysql;

import store.dao.ArticuloDAO;
import store.modelo.Articulo;
import store.util.ConexionBD;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MySQLArticuloDAO implements ArticuloDAO {

    @Override
    public void insertarArticulo(Articulo articulo) {
        String sql = "INSERT INTO articulo (codigo, descripcion, precio, envio, tiempo_preparacion) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, articulo.getCodigo());
            stmt.setString(2, articulo.getDescripcion());
            stmt.setDouble(3, articulo.getPrecioVenta());
            stmt.setDouble(4, articulo.getGastosEnvio());
            stmt.setInt(5, articulo.getTiempoPreparacion());

            stmt.executeUpdate();
            System.out.println("✅ Artículo insertado correctamente.");
        } catch (SQLException e) {
            System.err.println("❌ Error al insertar artículo: " + e.getMessage());
        }
    }

    @Override
    public Articulo obtenerArticuloPorCodigo(String codigo) {
        String sql = "SELECT * FROM articulo WHERE codigo = ?";
        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, codigo);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Articulo(
                            rs.getString("codigo"),
                            rs.getString("descripcion"),
                            rs.getDouble("precio"),
                            rs.getDouble("envio"),
                            rs.getInt("tiempo_preparacion")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("❌ Error al obtener artículo: " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<Articulo> obtenerTodosLosArticulos() {
        List<Articulo> articulos = new ArrayList<>();
        String sql = "SELECT * FROM articulo";

        try (Connection conn = ConexionBD.getConexion();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                articulos.add(new Articulo(
                        rs.getString("codigo"),
                        rs.getString("descripcion"),
                        rs.getDouble("precio"),
                        rs.getDouble("envio"),
                        rs.getInt("tiempo_preparacion")
                ));
            }
        } catch (SQLException e) {
            System.err.println("❌ Error al obtener lista de artículos: " + e.getMessage());
        }
        return articulos;
    }

    @Override
    public void actualizarArticulo(Articulo articulo) {
        String sql = "UPDATE articulo SET descripcion=?, precio=?, envio=?, tiempo_preparacion=? WHERE codigo=?";
        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, articulo.getDescripcion());
            stmt.setDouble(2, articulo.getPrecioVenta());
            stmt.setDouble(3, articulo.getGastosEnvio());
            stmt.setInt(4, articulo.getTiempoPreparacion());
            stmt.setString(5, articulo.getCodigo());

            stmt.executeUpdate();
            System.out.println("✅ Artículo actualizado correctamente.");
        } catch (SQLException e) {
            System.err.println("❌ Error al actualizar artículo: " + e.getMessage());
        }
    }

    @Override
    public void eliminarArticulo(String codigo) {
        String sql = "DELETE FROM articulo WHERE codigo = ?";
        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, codigo);
            int filas = stmt.executeUpdate();
            if (filas > 0)
                System.out.println("✅ Artículo eliminado correctamente.");
            else
                System.out.println("⚠️ No se encontró artículo con código: " + codigo);
        } catch (SQLException e) {
            System.err.println("❌ Error al eliminar artículo: " + e.getMessage());
        }
    }
}
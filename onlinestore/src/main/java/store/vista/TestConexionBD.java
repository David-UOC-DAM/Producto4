package store.vista;

import store.util.ConexionBD;
import java.sql.Connection;
import java.sql.SQLException;

public class TestConexionBD {
    public static void main(String[] args) {
        try {
            Connection conexion = ConexionBD.getConexion();
            if (conexion != null) {
                System.out.println("Conexión exitosa con la base de datos MySQL!");
            }
            ConexionBD.cerrarConexion();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
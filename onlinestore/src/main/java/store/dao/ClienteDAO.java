package store.dao;

import store.modelo.Cliente;
import java.util.List;

public interface ClienteDAO {
    void insertarCliente(Cliente cliente);
    Cliente obtenerClientePorEmail(String email);
    List<Cliente> obtenerTodosLosClientes();
    void eliminarCliente(String email);
    List<Cliente> obtenerClientesEstandar();
    List<Cliente> obtenerClientesPremium();
}

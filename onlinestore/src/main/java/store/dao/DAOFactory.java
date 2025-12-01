package store.dao;

import store.dao.mysql.MySQLClienteDAO;

public class DAOFactory {
    public static ClienteDAO getClienteDAO() {
        return new MySQLClienteDAO();
    }
}
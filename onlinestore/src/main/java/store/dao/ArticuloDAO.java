package store.dao;

import store.modelo.Articulo;
import java.util.List;

public interface ArticuloDAO {
    void insertarArticulo(Articulo articulo);
    Articulo obtenerArticuloPorCodigo(String codigo);
    List<Articulo> obtenerTodosLosArticulos();
    void actualizarArticulo(Articulo articulo);
    void eliminarArticulo(String codigo);
}
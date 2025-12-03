package store.dao.jpa;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import store.dao.ArticuloDAO;
import store.modelo.Articulo;
import store.util.JPAUtil;

import java.util.List;

public class ArticuloDAOJPA implements ArticuloDAO {

    @Override
    public void insertarArticulo(Articulo articulo) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(articulo);
            tx.commit();
        } catch (RuntimeException e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    @Override
    public Articulo obtenerArticuloPorCodigo(String codigo) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.find(Articulo.class, codigo);
        } finally {
            em.close();
        }
    }

    @Override
    public List<Articulo> obtenerTodosLosArticulos() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Articulo> q = em.createQuery("SELECT a FROM Articulo a", Articulo.class);
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public void actualizarArticulo(Articulo articulo) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(articulo); // merge actualiza la entidad en la BD
            tx.commit();
        } catch (RuntimeException e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    @Override
    public void eliminarArticulo(String codigo) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            Articulo a = em.find(Articulo.class, codigo);
            if (a == null) return;
            tx.begin();
            em.remove(a);
            tx.commit();
        } catch (RuntimeException e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }
}

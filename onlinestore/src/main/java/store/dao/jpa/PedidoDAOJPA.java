package store.dao.jpa;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import store.dao.PedidoDAO;
import store.modelo.Pedido;
import store.util.JPAUtil;

import java.util.List;

public class PedidoDAOJPA implements PedidoDAO {

    @Override
    public void insertarPedido(Pedido pedido) {
        EntityManager em = JPAUtil.getEntityManager();
        em.getTransaction().begin();
        em.persist(pedido);
        em.getTransaction().commit();
        em.close();
    }

    @Override
    public Pedido obtenerPedidoPorNumero(int numero) {
        EntityManager em = JPAUtil.getEntityManager();
        Pedido p = em.find(Pedido.class, numero);
        em.close();
        return p;
    }

    @Override
    public List<Pedido> obtenerTodosLosPedidos() {
        EntityManager em = JPAUtil.getEntityManager();
        TypedQuery<Pedido> q = em.createQuery("SELECT p FROM Pedido p", Pedido.class);
        List<Pedido> lista = q.getResultList();
        em.close();
        return lista;
    }

    @Override
    public void eliminarPedido(int numero) {
        EntityManager em = JPAUtil.getEntityManager();
        Pedido p = em.find(Pedido.class, numero);

        if (p == null) {
            em.close();
            return;
        }

        em.getTransaction().begin();
        em.remove(p);
        em.getTransaction().commit();
        em.close();
    }

    @Override
    public List<Pedido> obtenerPedidosPendientes(String email) {
        EntityManager em = JPAUtil.getEntityManager();

        TypedQuery<Pedido> q = em.createQuery(
                "SELECT p FROM Pedido p " +
                        "WHERE p.enviado = false " +
                        "AND (:email = '' OR p.cliente.email = :email)",
                Pedido.class
        );

        q.setParameter("email", email);

        List<Pedido> lista = q.getResultList();
        em.close();
        return lista;
    }

    @Override
    public List<Pedido> obtenerPedidosEnviados(String email) {
        EntityManager em = JPAUtil.getEntityManager();

        TypedQuery<Pedido> q = em.createQuery(
                "SELECT p FROM Pedido p " +
                        "WHERE p.enviado = true " +
                        "AND (:email = '' OR p.cliente.email = :email)",
                Pedido.class
        );

        q.setParameter("email", email);

        List<Pedido> lista = q.getResultList();
        em.close();
        return lista;
    }
}

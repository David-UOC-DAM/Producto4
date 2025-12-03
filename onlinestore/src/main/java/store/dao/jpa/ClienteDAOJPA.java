package store.dao.jpa;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import store.dao.ClienteDAO;
import store.modelo.Cliente;
import store.modelo.ClienteEstandar;
import store.modelo.ClientePremium;
import store.util.JPAUtil;

import java.util.List;

public class ClienteDAOJPA implements ClienteDAO {

    @Override
    public void insertarCliente(Cliente cliente) {
        EntityManager em = JPAUtil.getEntityManager();
        em.getTransaction().begin();
        em.persist(cliente);
        em.getTransaction().commit();
        em.close();
    }

    @Override
    public Cliente obtenerClientePorEmail(String email) {
        EntityManager em = JPAUtil.getEntityManager();
        Cliente cliente = em.find(Cliente.class, email);
        em.close();
        return cliente;
    }

    @Override
    public List<Cliente> obtenerTodosLosClientes() {
        EntityManager em = JPAUtil.getEntityManager();
        TypedQuery<Cliente> q = em.createQuery("SELECT c FROM Cliente c", Cliente.class);
        List<Cliente> lista = q.getResultList();
        em.close();
        return lista;
    }

    @Override
    public void eliminarCliente(String email) {
        EntityManager em = JPAUtil.getEntityManager();
        Cliente cliente = em.find(Cliente.class, email);

        if (cliente == null) {
            em.close();
            return;
        }

        em.getTransaction().begin();
        em.remove(cliente);
        em.getTransaction().commit();
        em.close();
    }

    @Override
    public List<Cliente> obtenerClientesEstandar() {
        EntityManager em = JPAUtil.getEntityManager();
        TypedQuery<Cliente> q =
                em.createQuery("SELECT c FROM ClienteEstandar c", Cliente.class);
        List<Cliente> lista = q.getResultList();
        em.close();
        return lista;
    }

    @Override
    public List<Cliente> obtenerClientesPremium() {
        EntityManager em = JPAUtil.getEntityManager();
        TypedQuery<Cliente> q =
                em.createQuery("SELECT c FROM ClientePremium c", Cliente.class);
        List<Cliente> lista = q.getResultList();
        em.close();
        return lista;
    }
}
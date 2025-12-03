package store.pruebas;

import jakarta.persistence.EntityManager;
import store.util.JPAUtil;

public class TestJPA {
    public static void main(String[] args) {

        System.out.println("CLASSPATH contains persistence.xml? -> " +
                TestJPA.class.getClassLoader().getResource("META-INF/persistence.xml"));

        System.out.println("🔍 Intentando abrir EntityManager...");

        try {
            EntityManager em = JPAUtil.getEntityManager();
            System.out.println("✅ Conexión JPA/Hibernate correcta: " + em);
            em.close();
        } catch (Exception e) {
            System.out.println("❌ Error JPA: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
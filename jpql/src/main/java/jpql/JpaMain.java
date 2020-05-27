package jpql;

import javax.persistence.*;

public class JpaMain {
    public static void main(String[] args) {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("jpql");
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction entityTransaction = entityManager.getTransaction();
        entityTransaction.begin();
        try {
            Member member = new Member();
            member.setUsername("myName");
            entityManager.persist(member);

            TypedQuery<String> query1 = entityManager.createQuery("select m.username from Member m where m.username = :username", String.class);
            query1.setParameter("username", "myName");
            Query query2 = entityManager.createQuery("select m.username from Member m");
            entityTransaction.commit();
        } catch (Exception e) {
            entityTransaction.rollback();
        } finally {
            entityManager.close();
        }
        entityManagerFactory.close();
    }

}

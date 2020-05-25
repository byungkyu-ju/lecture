package hellojpa;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.time.LocalDateTime;

public class JpaMain {
    public static void main(String[] args) {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("hello");
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction entityTransaction = entityManager.getTransaction();
        entityTransaction.begin();
        try {
            Member member1 = new Member();
            member1.setName("mem1");

            Member member2 = new Member();
            member2.setName("mem2");
            entityManager.flush();
            entityManager.clear();

            Member m1 = entityManager.find(Member.class,member1.getId());
            Member m2 = entityManager.getReference(Member.class,member2.getId());

            System.out.println(m1.getClass());
            System.out.println(m2.getClass());

            entityTransaction.commit();
        } catch (Exception e) {
            entityTransaction.rollback();
        } finally {
            entityManager.close();
        }
    }
}

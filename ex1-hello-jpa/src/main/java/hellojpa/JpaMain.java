package hellojpa;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

public class JpaMain {
    public static void main(String[] args) {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("hello");
        // 1.기본 트랜잭션
        basicTransactionTutorial(entityManagerFactory);
        // 2.JPQL
        //jpqlTutorial(entityManagerFactory);
    }

    private static void basicTransactionTutorial(EntityManagerFactory entityManagerFactory) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction entityTransaction = entityManager.getTransaction();
        entityTransaction.begin();
        try {

            // insert
            Member member = new Member();
            //member.setId("A");
            member.setUsername("HelloB");
            entityManager.persist(member);
            entityTransaction.commit();
            /*
            //update
            Member findMember = entityManager.find(Member.class, 1L);
            findMember.setUsername("HelloJPA");
            entityTransaction.commit();
            //delete
            entityManager.remove(findMember);
            entityTransaction.commit();
            */
        } catch (Exception e) {
            entityTransaction.rollback();
        } finally {
            entityManager.close();
        }
        entityManagerFactory.close();
    }

    private static void jpqlTutorial(EntityManagerFactory entityManagerFactory) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction entityTransaction = entityManager.getTransaction();
        entityTransaction.begin();
        try {
            List<Member> result = entityManager.createQuery("select m from Member as m", Member.class)
                    .setFirstResult(5)
                    .setMaxResults(100)
                    .getResultList();
            for (Member member : result){
                System.out.println("member.name = " + member.getUsername());
            }
            entityTransaction.commit();
        } catch (Exception e) {
            entityTransaction.rollback();
        } finally {
            entityManager.close();
        }
        entityManagerFactory.close();
    }

}

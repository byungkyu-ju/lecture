package hellojpa;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

public class JpaMain {
    public static void main(String[] args) {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("hello");
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction entityTransaction = entityManager.getTransaction();
        entityTransaction.begin();
        try {
            Member member = new Member();
            member.setName("name");
            member.setHomeAddress(new Address("city", "street", "zipcode"));
            member.getFavoriteFoods().add("chicken");
            member.getFavoriteFoods().add("pizza");
            member.getAddressHistory().add(new Address("oldCity1", "street", "zipcode"));
            member.getAddressHistory().add(new Address("oldCity2", "street", "zipcode"));

            entityManager.persist(member);

            entityManager.flush();
            entityManager.clear();
            Member findMember =entityManager.find(Member.class, member.getId());
            List<Address> addressHistory = findMember.getAddressHistory();
            for(Address address : addressHistory){
                System.out.println("address = " + address.getCity());
            }

            entityTransaction.commit();
        } catch (Exception e) {
            entityTransaction.rollback();
        } finally {
            entityManager.close();
        }
    }
}

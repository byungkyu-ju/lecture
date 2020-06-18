package study.jpadata.repository;

import org.springframework.stereotype.Repository;
import org.springframework.test.annotation.Rollback;
import study.jpadata.entity.Member;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
@Rollback(value = false)
public class MemberJpaRepository {

    @PersistenceContext
    private EntityManager em;

    public Member save(Member member) {
        em.persist(member);
        return member;
    }

    public Member find(Long id) {
        return em.find(Member.class, id);
    }
}

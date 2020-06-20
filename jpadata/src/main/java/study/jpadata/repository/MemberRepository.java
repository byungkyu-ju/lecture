package study.jpadata.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import study.jpadata.entity.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {
}
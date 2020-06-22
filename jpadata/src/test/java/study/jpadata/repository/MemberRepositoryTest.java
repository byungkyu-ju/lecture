package study.jpadata.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import study.jpadata.dto.MemberDto;
import study.jpadata.entity.Member;
import study.jpadata.entity.Team;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;
    @Autowired
    TeamRepository teamRepository;
    @PersistenceContext
    EntityManager em;

    @Test
    void testMember() {
        Member member = new Member("bk");
        Member savedMember = memberRepository.save(member);

        Member findMember = memberRepository.findById(savedMember.getId()).get();
        assertThat(findMember.getId()).isEqualTo(member.getId());
        assertThat(findMember.getUsername()).isEqualTo(member.getUsername());
    }

    @Test
    public void basicCRUD() {
        Member member1 = new Member("member1");
        Member member2 = new Member("member2");
        memberRepository.save(member1);
        memberRepository.save(member2);

        Member findMember1 = memberRepository.findById(member1.getId()).get();
        Member findMember2 = memberRepository.findById(member2.getId()).get();

        List<Member> all = memberRepository.findAll();
        assertThat(all.size()).isEqualTo(2);

        memberRepository.delete(member1);
        memberRepository.delete(member2);

        long deletedCount = memberRepository.count();
        assertThat(deletedCount).isEqualTo(0);
    }

    @Test
    void findByUsernameAndAgeGreaterThen() {
        Member member1 = new Member("aaa", 10);
        Member member2 = new Member("aaa", 20);
        memberRepository.save(member1);
        memberRepository.save(member2);

        List<Member> result = memberRepository.findByUsernameAndAgeGreaterThan("aaa", 15);
        assertThat(result.get(0).getUsername()).isEqualTo("aaa");
        assertThat(result.get(0).getAge()).isEqualTo(20);
        assertThat(result.size()).isEqualTo(1);
    }

    @Test
    void testQuery() {
        Member member1 = new Member("aaa", 10);
        Member member2 = new Member("aaa", 20);
        memberRepository.save(member1);
        memberRepository.save(member2);

        List<Member> result = memberRepository.findUser("aaa", 10);
        assertThat(result.get(0).getAge()).isEqualTo(member1.getAge());
    }

    @Test
    void testMemberDto() {
        Team team = new Team("teamA");
        teamRepository.save(team);

        Member member1 = new Member("aaa", 10);
        memberRepository.save(member1);
        member1.setTeam(team);

        List<MemberDto> result = memberRepository.findMemberDto();
        for (MemberDto dto : result){
            System.out.println(dto.getUsername());
        }
    }

    @Test
    void findByNames() {
        Team team = new Team("teamA");
        teamRepository.save(team);

        Member member1 = new Member("aaa", 10);
        memberRepository.save(member1);
        member1.setTeam(team);

        List<Member> result = memberRepository.findByNames(Arrays.asList("aaa","bbb"));
        for (Member member : result){
            System.out.println(member.getUsername());
        }
    }

    @Test
    void returnType(){
        Member member1 = new Member("aaa", 10);
        Member member2 = new Member("aaa", 20);
        memberRepository.save(member1);
        memberRepository.save(member2);

        Optional<Member> findMembers = memberRepository.findOptionalByUsername("aaa");

    }

    @Test
    void paging() {
        Member member1 = new Member("aaa", 10);
        Member member2 = new Member("aaa", 20);
        Member member3 = new Member("aaa", 10);
        Member member4 = new Member("aaa", 20);
        Member member5= new Member("aaa", 10);
        Member member6 = new Member("aaa", 20);

        memberRepository.save(member1);
        memberRepository.save(member2);
        memberRepository.save(member3);
        memberRepository.save(member4);
        memberRepository.save(member5);
        memberRepository.save(member6);

        int age = 10;
        PageRequest pageRequest = PageRequest.of(0, 3, Sort.Direction.DESC, "username");

        Page<Member> page = memberRepository.findByAge(age, pageRequest);

        List<Member> content = page.getContent();
        long totalElements = page.getTotalElements();

        for (Member member : content) {
            System.out.println("member = " + member);
        }
        System.out.println("totalElements = " + totalElements);

        assertThat(content.size()).isEqualTo(3);
        assertThat(page.getTotalElements()).isEqualTo(3);
        assertThat(page.getNumber()).isEqualTo(0);
        assertThat(page.isFirst()).isTrue();
        assertThat(page.hasNext()).isFalse();
    }

    @Test
    public void bulkUpdate() {
        memberRepository.save(new Member("member1", 10));
        memberRepository.save(new Member("member2", 19));
        memberRepository.save(new Member("member3", 20));
        memberRepository.save(new Member("member4", 21));
        memberRepository.save(new Member("member5", 40));

        int resultCount = memberRepository.bulkAgePlus(20);

        assertThat(resultCount).isEqualTo(3);

    }

}
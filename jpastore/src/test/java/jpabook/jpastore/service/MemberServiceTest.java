package jpabook.jpastore.service;

import jpabook.jpastore.domain.Member;
import jpabook.jpastore.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatIllegalStateException;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class MemberServiceTest {

    @Autowired
    MemberService memberService;
    @Autowired
    MemberRepository memberRepository;

    @Test
    void 회원가입() throws Exception{
        //given
        Member member = new Member();
        member.setName("myName");

        //when
        Long savedId = memberService.join(member);

        //then
        assertEquals(member, memberRepository.findOne(savedId));
    }

    @Test()
    void 중복_회원_예외() throws Exception{
        //given
        Member member1 = new Member();
        member1.setName("user1");

        Member member2 = new Member();
        member2.setName("user1");
        //when + then
        assertThatIllegalStateException().isThrownBy(() -> {
            memberService.join(member1);
            memberService.join(member2);
        });

    }

}
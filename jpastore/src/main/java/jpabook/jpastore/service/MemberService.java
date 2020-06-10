package jpabook.jpastore.service;

import jpabook.jpastore.domain.Member;
import jpabook.jpastore.repository.MemberRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor //3.final이 있 필드만 가지고 생성자를 만들어줌
public class MemberService {

    //final 컴파일시점에 체크를 할 수 있기 때문에 추천함
    @Autowired
    private final MemberRepository memberRepository;

    //1.setter injection
    //injection이 전부 일어난 다음에 추가로 변경하기 때문에 효율이 좋지 않음. 그래서 생성자 injection을 최근에 더 사용함
    /*
    @Autowired
    public void setMemberRepository(MemberRepository memberRepository){
        this.memberRepository = memberRepository;
    }*/

    //2.Constructor injection
    //test시 의존관계를 명확하게 주입할 수 있음
    //하지만 @RequiredArgsConstructor을 가장 선호함
    /*
    @Autowired
    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }*/

    //회원가입
    @Transactional
    public Long join(Member member){
        validateDuplicateMember(member);
        memberRepository.save(member);
        return member.getId();
    }

    private void validateDuplicateMember(Member member) {
        List<Member> findMembers = memberRepository.findByName(member.getName());
        if( !findMembers.isEmpty()){
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }

    //회원전체조회
    @Transactional(readOnly = true) //읽기에는 가급적 readonly true
    public List<Member> findMembers() {
        return memberRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Member findOne(Long memberId){
        return memberRepository.findOne(memberId);
    }
}

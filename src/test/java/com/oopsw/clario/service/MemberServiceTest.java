package com.oopsw.clario.service;

import com.oopsw.clario.domain.member.Member;
import com.oopsw.clario.domain.member.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class MemberServiceTest {

    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberRepository memberRepository;

    @Test
    void 구글_회원가입_정보_저장_테스트() {
        // given
        String email = "testuser@gmail.com";
        String name = "홍길동";
        String phone = "01012345678";
        String password = "암호화된비밀번호";

        // when
        memberService.saveMember(email, name, phone, password);

        // then
        Optional<Member> saved = memberRepository.findByEmail(email);
        assertThat(saved).isPresent();
        Member member = saved.get();
        assertThat(member.getName()).isEqualTo("홍길동");

    }
}

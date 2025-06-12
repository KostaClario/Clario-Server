package com.oopsw.clario.domain.member;



import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
public class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @Test
    void testFindAllMembers(){

//        Member member = new Member();
//        System.out.println("CLASS: " + member.getClass());
//
        List<Member> members = memberRepository.findAll();

        assertThat(members).isNotNull();
        System.out.println("전체 사용자 수 : " + members.size() );

        for (Member member : members) {
            System.out.println(member.getEmail());
        }
    }
}

package com.oopsw.clario.domain.member;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface MemberRepository extends JpaRepository<Member, Integer> {
    boolean existsByEmail(String email);
    Optional<Member> findByEmail(String email);
}

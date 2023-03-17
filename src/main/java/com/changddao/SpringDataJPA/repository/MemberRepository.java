package com.changddao.SpringDataJPA.repository;

import com.changddao.SpringDataJPA.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
}

package com.changddao.SpringDataJPA.repository;

import com.changddao.SpringDataJPA.entity.Member;

import java.util.List;

public interface MemberRepositoryCustom {
    List<Member> findMemberCustom();
}

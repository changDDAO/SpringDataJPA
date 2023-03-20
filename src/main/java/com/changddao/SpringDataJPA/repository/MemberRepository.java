package com.changddao.SpringDataJPA.repository;

import com.changddao.SpringDataJPA.dto.MemberDto;
import com.changddao.SpringDataJPA.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Long>, MemberRepositoryCustom {
    List<Member> findByUsernameAndAgeGreaterThan(String username, int age);

    @Query("select m from Member m where m.username = :username and m.age = :age")
    List<Member> findUser(@Param("username") String username, @Param("age") int age);

    @Query("select m.username from Member m")
    public List<String> usernameList();
    @Query("select new com.changddao.SpringDataJPA.dto.MemberDto(m.id, m.username,t.name) from Member m join m.team t")
    List<MemberDto> findMemberDto();

    @Query("select m from Member m where m.username in :names")
    List<Member> findBynames(@Param("names")Collection<String> names);

    Page<Member> findByAge(int age, Pageable pageable);

    //변경할때는 @Modifying annotation 필수
    @Modifying
    @Query("update Member m set m.age = m.age+1 where m.age >= :age")
    int bulkAgePlus(@Param("age") int age);
    @Query("select m from Member m left join fetch m.team")
    List<Member> findMemberFetchJoin();

}

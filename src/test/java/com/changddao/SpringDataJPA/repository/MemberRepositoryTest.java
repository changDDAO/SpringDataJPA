package com.changddao.SpringDataJPA.repository;

import com.changddao.SpringDataJPA.dto.MemberDto;
import com.changddao.SpringDataJPA.entity.Member;
import com.changddao.SpringDataJPA.entity.Team;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
class MemberRepositoryTest {
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    TeamRepository teamRepository;
    @PersistenceContext
    EntityManager em;


    @Test
    public void testMember() {
        Member member = new Member("changho");
        Member savedMember = memberRepository.save(member);

        Member findMember = memberRepository.findById(savedMember.getId()).get();
        assertThat(findMember.getId()).isEqualTo(member.getId());
        assertThat(findMember.getUsername()).isEqualTo(member.getUsername());
        assertThat(findMember).isEqualTo(member);
    }

    @Test
    public void basicCRUD() {
        Member member1 = new Member("changho");
        Member member2 = new Member("jina");
        memberRepository.save(member1);
        memberRepository.save(member2);

        //단건조회 검증
        Member findMember1 = memberRepository.findById(member1.getId()).get();
        Member findMember2 = memberRepository.findById(member2.getId()).get();
        assertThat(findMember1).isEqualTo(member1);
        assertThat(findMember2).isEqualTo(member2);

        //리스트 조회 검증
        List<Member> allMember = memberRepository.findAll();
        assertThat(allMember.size()).isEqualTo(2);

        //카운트 검증
        long count = memberRepository.count();
        assertThat(count).isEqualTo(2);

        memberRepository.delete(member1);
        memberRepository.delete(member2);

        long afterDeleteCnt = memberRepository.count();
        assertThat(afterDeleteCnt).isEqualTo(0);
    }

    @Test
    public void findByUsernameAndAgeGreaterThen() {
        Member member1 = new Member("AAA", 10);
        Member member2 = new Member("AAA", 20);
        memberRepository.save(member1);
        memberRepository.save(member2);

        List<Member> result = memberRepository.findByUsernameAndAgeGreaterThan("AAA", 15);
        System.out.println("result = " + result);
        assertThat(result.get(0).getUsername()).isEqualTo("AAA");
        assertThat(result.get(0).getAge()).isEqualTo(20);

    }
    @Test
    public void findUser() {
        Member member1 = new Member("changho", 29);
        Member member2 = new Member("jina", 29);

        memberRepository.save(member1);
        memberRepository.save(member2);

        List<Member> users = memberRepository.findUser("changho", 29);
        assertThat(users.get(0).getUsername()).isEqualTo("changho");
        assertThat(users.get(0).getAge()).isEqualTo(29);

    }

    @Test
    public void findUsernameList() {
        Member member1 = new Member("changho", 29);
        Member member2 = new Member("jina", 29);

        memberRepository.save(member1);
        memberRepository.save(member2);

        List<String> usernames = memberRepository.usernameList();

        assertThat(usernames.get(0)).isEqualTo("changho");
        assertThat(usernames.get(1)).isEqualTo("jina");

    }

    @Test
    public void memberDtoTest() {
        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");
        teamRepository.save(teamA);
        teamRepository.save(teamB);

        Member member1 = new Member("changho", 29);
        Member member2 = new Member("jina", 29);
        member1.setTeam(teamA);
        member2.setTeam(teamB);

        memberRepository.save(member1);
        memberRepository.save(member2);

        List<MemberDto> memberDto = memberRepository.findMemberDto();
        assertThat(memberDto.get(0).getUsername()).isEqualTo("changho");
        assertThat(memberDto.get(0).getTeamName()).isEqualTo("teamA");
        for (MemberDto dto : memberDto) {
            System.out.println("dto = " + dto);
        }


    }

    @Test
    public void findByNames() {
        Member member1 = new Member("changho", 29);
        Member member2 = new Member("jina", 29);

        memberRepository.save(member1);
        memberRepository.save(member2);

        List<String> usernames = memberRepository.usernameList();

        List<Member> bynames = memberRepository.findBynames(usernames);
        for (Member byname : bynames) {
            System.out.println("byname = " + byname);
        }
    }

    @Test
    public void paging(){
        //given
        memberRepository.save(new Member("member1", 10));
        memberRepository.save(new Member("member2", 10));
        memberRepository.save(new Member("member3", 10));
        memberRepository.save(new Member("member4", 10));
        memberRepository.save(new Member("member5", 10));
        memberRepository.save(new Member("member6", 10));

        PageRequest pageRequest = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC,"username"));
        int age = 10;
        //when

        Page<Member> page = memberRepository.findByAge(age, pageRequest);


        //then
        List<Member> content = page.getContent();
        long totalElements = page.getTotalElements();

        for (Member member : content) {
            System.out.println("member = " + member);
        }
        assertThat(content.size()).isEqualTo(3);
        assertThat(totalElements).isEqualTo(6);


    }
    @Test
    public void bulkUpdate() {
        memberRepository.save(new Member("member1", 10));
        memberRepository.save(new Member("member2", 20));
        memberRepository.save(new Member("member3", 11));
        memberRepository.save(new Member("member4", 13));
        memberRepository.save(new Member("member5", 7));
        memberRepository.save(new Member("member6", 17));

        int updateCnt = memberRepository.bulkAgePlus(10);
        em.flush();
        em.clear();
        assertThat(updateCnt).isEqualTo(5);

    }

    @Test
    public void findMemberLazy() {
        //given
        //member1 ->teamA
        //member2 ->teamB

        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");
        teamRepository.save(teamA);
        teamRepository.save(teamB);
        Member member1 = new Member("changho");
        Member member2 = new Member("jongho");
        member1.setTeam(teamA);
        member2.setTeam(teamB);
        memberRepository.save(member1);
        memberRepository.save(member2);

        em.flush();
        em.clear();

        //when
        List<Member> members = memberRepository.findMemberFetchJoin();
        for (Member member : members) {
            System.out.println("member = " + member.getUsername());
            System.out.println("member.getTeam().getName() = " + member.getTeam().getName());
        }
    }

    @Test
    public void queryHint() {
        //given
        Member member1 = new Member("member1", 10);
        memberRepository.save(member1);
        em.flush();
        em.clear();

        //when
        Member findMember = memberRepository.findById(member1.getId()).get();
        findMember.setUsername("member2");

        em.flush();
    }

    @Test
    public void callCustom() {
        List<Member> result = memberRepository.findMemberCustom();
    }


}
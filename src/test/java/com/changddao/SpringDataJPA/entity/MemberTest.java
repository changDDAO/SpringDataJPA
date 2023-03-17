package com.changddao.SpringDataJPA.entity;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional

class MemberTest {

    @PersistenceContext
    private EntityManager em;

    @Test
    public void testEntity() {
        Team teamA = new Team("TeamA");
        Team teamB = new Team("TeamB");

        em.persist(teamA);
        em.persist(teamB);

        Member member1 = new Member("member1", 10, teamA);
        Member member2 = new Member("member2", 14, teamA);
        Member member3 = new Member("member3", 17, teamB);
        Member member4 = new Member("member4", 21, teamB);

        em.persist(member1);
        em.persist(member2);
        em.persist(member3);
        em.persist(member4);

        //initialize
        em.flush();
        em.clear();

        List<Member> members = em.createQuery("select m from Member m", Member.class)
                .getResultList();

        members.stream().forEach(m-> {
            System.out.println("member = "+ m);
            System.out.println("->member.team = "+ m.getTeam());

        });

    }

}
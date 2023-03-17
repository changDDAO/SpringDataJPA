package com.changddao.SpringDataJPA.repository;

import com.changddao.SpringDataJPA.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamRepository extends JpaRepository<Team, Long> {
}

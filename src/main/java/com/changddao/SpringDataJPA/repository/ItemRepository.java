package com.changddao.SpringDataJPA.repository;

import com.changddao.SpringDataJPA.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item, Long> {

}

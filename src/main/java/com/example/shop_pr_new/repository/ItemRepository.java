package com.example.shop_pr_new.repository;

import com.example.shop_pr_new.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface ItemRepository extends JpaRepository<Item, Long> {

}

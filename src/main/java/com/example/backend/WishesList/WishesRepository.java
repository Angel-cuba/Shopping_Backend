package com.example.backend.WishesList;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface WishesRepository extends JpaRepository<Wishes, UUID>{
    List<Wishes> findWishedProductsByUserId(UUID userId);
}

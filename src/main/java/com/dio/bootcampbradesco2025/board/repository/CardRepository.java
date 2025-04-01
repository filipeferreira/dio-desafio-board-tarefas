package com.dio.bootcampbradesco2025.board.repository;

import com.dio.bootcampbradesco2025.board.entity.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CardRepository extends JpaRepository<Card, Long> {
}

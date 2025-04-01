package com.dio.bootcampbradesco2025.board.repository;

import com.dio.bootcampbradesco2025.board.entity.BoardColumn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardColumnRepository extends JpaRepository<BoardColumn, Long> {
}

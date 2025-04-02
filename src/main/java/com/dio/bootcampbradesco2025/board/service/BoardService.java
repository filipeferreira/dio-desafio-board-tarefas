package com.dio.bootcampbradesco2025.board.service;

import com.dio.bootcampbradesco2025.board.entity.Board;
import com.dio.bootcampbradesco2025.board.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class BoardService {

    private final BoardRepository boardRepository;

    public Board create(Board board) {
        return boardRepository.save(board);
    }

    public void delete(long id) {
        if (!boardRepository.existsById(id)) {
            throw new IllegalArgumentException("Board não encontrado para o id: " + id);
        }
        boardRepository.deleteById(id);
    }

    public Optional<Board> findById(long id) {
        return boardRepository.findById(id);
    }
}

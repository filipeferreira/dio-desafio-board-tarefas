package com.dio.bootcampbradesco2025.board.service;

import com.dio.bootcampbradesco2025.board.dto.BoardColumnDTO;
import com.dio.bootcampbradesco2025.board.dto.BoardDetailsDTO;
import com.dio.bootcampbradesco2025.board.entity.Board;
import com.dio.bootcampbradesco2025.board.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.stream.Collectors;

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

    public Board findById(long id) {
        return boardRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Board não encontrado para o id: " + id));
    }

    @Transactional
    public BoardDetailsDTO getBoardDetails(long boardId) {
        var board = this.findById(boardId);
        var columns = board
                .getColumns()
                .stream()
                .map(c -> new BoardColumnDTO(c.getId(), c.getName(), c.getType(), c.getCards().size()))
                            .collect(Collectors.toSet());
        return new BoardDetailsDTO(board.getId(), board.getName(), columns);
    }
}

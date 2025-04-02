package com.dio.bootcampbradesco2025.board.dto;

import java.util.Set;

public record BoardDetailsDTO(Long id,
                              String name,
                              Set<BoardColumnDTO> columns) {
}

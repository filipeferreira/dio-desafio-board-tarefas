package com.dio.bootcampbradesco2025.board.dto;

import com.dio.bootcampbradesco2025.board.enums.BoardColumnTypeEnum;

public record BoardColumnDTO(Long id,
                             String name,
                             BoardColumnTypeEnum type,
                             int cardsAmount) {
}

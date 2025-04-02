package com.dio.bootcampbradesco2025.board.dto;

import com.dio.bootcampbradesco2025.board.enums.BoardColumnTypeEnum;

public record BoardColumnInfoDTO(Long id,
                                 int order,
                                 BoardColumnTypeEnum type) {
}

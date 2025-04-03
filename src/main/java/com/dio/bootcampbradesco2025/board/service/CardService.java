package com.dio.bootcampbradesco2025.board.service;

import com.dio.bootcampbradesco2025.board.dto.CardDetailsDTO;
import com.dio.bootcampbradesco2025.board.entity.Card;
import com.dio.bootcampbradesco2025.board.enums.BoardColumnTypeEnum;
import com.dio.bootcampbradesco2025.board.repository.CardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CardService {

    private final CardRepository cardRepository;

    public CardDetailsDTO getCardDetails(long id) {
        return null;
    }

    public void cancelCard(long cardId) {
        var card = cardRepository.findById(cardId)
                .orElseThrow(() -> new IllegalArgumentException("Card não encontrado para o id: " + cardId));
        validateCardIsNotBlocked(card);
        validateCardIsNotFinalized(card);
        validateCardIsNotCanceled(card);

        card.setBoardColumn(card.getBoardColumn().getBoard().getColumns().stream()
                .filter(c -> c.getType().equals(BoardColumnTypeEnum.CANCEL))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Coluna de cancelamento não encontrada.")));

        cardRepository.save(card);
    }

    private static void validateCardIsNotCanceled(Card card) {
        if (card.getBoardColumn().getType().equals(BoardColumnTypeEnum.CANCEL)) {
            throw new IllegalArgumentException("O card está cancelado.");
        }
    }

    private static void validateCardIsNotFinalized(Card card) {
        if (card.getBoardColumn().getType().equals(BoardColumnTypeEnum.FINAL)) {
            throw new IllegalArgumentException("O card já encontra-se finalizado. Não é possível movê-lo.");
        }
    }

    private static void validateCardIsNotBlocked(Card card) {
        if (card.isBlocked()) {
            throw new IllegalArgumentException("O card está bloqueado. É necessário desbloqueá-lo para movê-lo para outra coluna.");
        }
    }
}

package com.dio.bootcampbradesco2025.board.service;

import com.dio.bootcampbradesco2025.board.dto.CardDetailsDTO;
import com.dio.bootcampbradesco2025.board.entity.Block;
import com.dio.bootcampbradesco2025.board.entity.Card;
import com.dio.bootcampbradesco2025.board.enums.BoardColumnTypeEnum;
import com.dio.bootcampbradesco2025.board.repository.CardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class CardService {

    private final CardRepository cardRepository;
    private final BoardService boardService;

    public CardDetailsDTO getCardDetails(long id) {
        return null;
    }

    public void cancelCard(long cardId) {
        var card = getCardById(cardId);
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
            throw new IllegalStateException("O card está cancelado.");
        }
    }

    private static void validateCardIsNotFinalized(Card card) {
        if (card.getBoardColumn().getType().equals(BoardColumnTypeEnum.FINAL)) {
            throw new IllegalStateException("O card já encontra-se finalizado.");
        }
    }

    private static void validateCardIsNotBlocked(Card card) {
        if (card.isBlocked()) {
            throw new IllegalStateException("O card está bloqueado.");
        }
    }

    private static void validateCardIsBlocked(Card card) {
        if (!card.isBlocked()) {
            throw new IllegalStateException("O card não está bloqueado.");
        }
    }

    @Transactional
    public void create(long boardId, String title, String description) {
        var board = boardService.findById(boardId);
        Card card = new Card();
        card.setTitle(title);
        card.setDescription(description);
        card.setBoardColumn(board.getInitialColumn());
        cardRepository.save(card);
    }

    @Transactional
    public void moveCardToNextColumn(long cardId) {
        var card = getCardById(cardId);
        validateCardIsNotBlocked(card);
        validateCardIsNotFinalized(card);
        validateCardIsNotCanceled(card);

        var board = card.getBoardColumn().getBoard();
        var boardColumns = board.getColumns();
        var currentColumn = card.getBoardColumn();

        var nextColumn = boardColumns.stream()
                .filter(c -> c.getOrder() == currentColumn.getOrder() + 1)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Não há coluna seguinte."));

        card.setBoardColumn(nextColumn);
        cardRepository.save(card);
    }

    private Card getCardById(long cardId) {
        var card = cardRepository.findById(cardId)
                .orElseThrow(() -> new IllegalArgumentException("Card não encontrado para o id: " + cardId));
        return card;
    }

    @Transactional
    public void createBlock(long cardId, String reason, boolean blockCard) {
        var card = getCardById(cardId);

        Block block = new Block();
        block.setCard(card);
        if (blockCard) {
            validateCardIsNotBlocked(card);
            block.setBlockedAt(LocalDateTime.now());
            block.setBlockReason(reason);
        } else {
            validateCardIsBlocked(card);
            block.setUnblockedAt(LocalDateTime.now());
            block.setUnblockReason(reason);
        }
        validateCardIsNotFinalized(card);
        validateCardIsNotCanceled(card);

        card.getBlocks().add(block);
        cardRepository.save(card);


    }
}

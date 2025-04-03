package com.dio.bootcampbradesco2025.board.ui;

import com.dio.bootcampbradesco2025.board.entity.Board;
import com.dio.bootcampbradesco2025.board.entity.BoardColumn;
import com.dio.bootcampbradesco2025.board.service.BoardService;
import com.dio.bootcampbradesco2025.board.service.CardService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.Scanner;

@Slf4j
@Component
public class BoardMenu {

    private final Scanner scanner = new Scanner(System.in).useDelimiter("\n");
    private final BoardService boardService;
    private final CardService cardService;
    private final TransactionTemplate transactionTemplate;

    private Board board;

    public BoardMenu(BoardService boardService, CardService cardService, PlatformTransactionManager transactionManager) {
        this.boardService = boardService;
        this.cardService = cardService;
        this.transactionTemplate = new TransactionTemplate(transactionManager);
    }

    public void execute(Board entity) {
        try {
            this.board = entity;
            System.out.printf("Bem vindo ao board %s, selecione a operação desejada\n", board.getId());
            var option = -1;
            while (option != 9) {
                System.out.println("1 - Criar um card");
                System.out.println("2 - Mover um card");
                System.out.println("3 - Bloquear um card");
                System.out.println("4 - Desbloquear um card");
                System.out.println("5 - Cancelar um card");
                System.out.println("6 - Ver board");
                System.out.println("7 - Ver coluna com cards");
                System.out.println("8 - Ver card");
                System.out.println("9 - Voltar para o menu anterior um card");
                System.out.println("10 - Sair");
                option = scanner.nextInt();
                switch (option) {
                    case 1 -> createCard();
                    case 2 -> moveCardToNextColumn();
                    case 3 -> blockCard();
                    case 4 -> unblockCard();
                    case 5 -> cancelCard();
                    case 6 -> showBoard();
                    case 7 -> showCardsInColumn();
                    case 8 -> showCard();
                    case 9 -> System.out.println("Voltando para o menu anterior");
                    case 10 -> System.exit(0);
                    default -> System.out.println("Opção inválida, informe uma opção do menu");
                }
            }
        }catch (Exception ex){
            log.error("Erro ao executar o menu do board", ex);
            System.exit(0);
        }
    }

    private void showCard() {
        System.out.println("Informe o id do card que deseja visualizar");
        var selectedCardId = scanner.nextLong();

        var c = cardService.getCardDetails(selectedCardId);

        System.out.printf("Card %s - %s.\n", c.id(), c.title());
        System.out.printf("Descrição: %s\n", c.description());
        System.out.println(c.blocked() ?
                "Está bloqueado. Motivo: " + c.blockReason() :
                "Não está bloqueado");
        System.out.printf("Já foi bloqueado %s vezes\n", c.blocksAmount());
        System.out.printf("Está no momento na coluna %s - %s\n", c.columnId(), c.columnName());
        System.out.println();
        System.out.println();

    }

    private void showCardsInColumn() {
        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus status) {
                var currentBoard = boardService.findById(board.getId());
                var columns = currentBoard.getColumns();
                var columnsIds = columns.stream().map(BoardColumn::getId).toList();
                var selectedColumnId = -1L;
                while (!columnsIds.contains(selectedColumnId)){
                    System.out.printf("Escolha uma coluna do board %s pelo id\n", currentBoard.getName());
                    columns.forEach(c -> System.out.printf("%s - %s [%s]\n", c.getId(), c.getName(), c.getType()));
                    selectedColumnId = scanner.nextLong();
                }
                long finalSelectedColumnId = selectedColumnId;
                var column = columns.stream().filter(c -> finalSelectedColumnId == c.getId()).findFirst().orElseThrow(() -> new IllegalArgumentException("Coluna não encontrada"));
                System.out.printf("Coluna %s tipo %s\n", column.getName(), column.getType());
                column.getCards().forEach(ca -> System.out.printf("Card %s - %s\nDescrição: %s",
                        ca.getId(), ca.getTitle(), ca.getDescription()));
                System.out.println();
                System.out.println();
            }
        });
    }

    private void showBoard() {
        var boardDetails = boardService.getBoardDetails(board.getId());
        System.out.printf("Board [%s,%s]\n", boardDetails.id(), boardDetails.name());
        boardDetails.columns().forEach(c ->
                System.out.printf("Coluna [%s] tipo: [%s] tem %s cards\n",
                        c.name(), c.type(), c.cardsAmount())
        );
        System.out.println();
        System.out.println();
    }

    private void cancelCard() {
        System.out.println("Informe o id do card que deseja mover para a coluna de cancelamento");
        var cardId = scanner.nextLong();
        try {
            cardService.cancelCard(cardId);
        } catch (IllegalStateException e) {
            log.error(e.getMessage());
        }
    }

    private void unblockCard() {
        System.out.println("Informe o id do card que será desbloqueado");
        var cardId = scanner.nextLong();
        System.out.println("Informe o motivo do desbloqueio do card");
        var reason = scanner.next();
        try {
            cardService.createBlock(cardId, reason, false);
        } catch (IllegalStateException e) {
            log.error(e.getMessage());
        }
    }

    private void blockCard() {
        System.out.println("Informe o id do card que será bloqueado");
        var cardId = scanner.nextLong();
        System.out.println("Informe o motivo do bloqueio do card");
        var reason = scanner.next();
        try {
            cardService.createBlock(cardId, reason, true);
        } catch (IllegalStateException e) {
            log.error(e.getMessage());
        }
    }

    private void moveCardToNextColumn() {
        System.out.println("Informe o id do card que deseja mover para a próxima coluna");
        var cardId = scanner.nextLong();
        try {
            cardService.moveCardToNextColumn(cardId);
        } catch (IllegalStateException ex){
            log.error(ex.getMessage());
        }
    }

    private void createCard() {
        System.out.println("Informe o título do card");
        var title = scanner.next();
        System.out.println("Informe a descrição do card");
        var description = scanner.next();
        cardService.create(board.getId(), title, description);
    }
}

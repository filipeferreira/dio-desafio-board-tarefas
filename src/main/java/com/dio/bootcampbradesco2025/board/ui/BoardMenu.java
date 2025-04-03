package com.dio.bootcampbradesco2025.board.ui;

import com.dio.bootcampbradesco2025.board.entity.Board;
import com.dio.bootcampbradesco2025.board.service.BoardService;
import com.dio.bootcampbradesco2025.board.service.CardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Scanner;

@Slf4j
@RequiredArgsConstructor
@Component
public class BoardMenu {

    private final Scanner scanner = new Scanner(System.in).useDelimiter("\n");
    private final BoardService boardService;
    private final CardService cardService;
    private Board board;

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
                    case 7 -> showColumn();
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

    }

    private void showColumn() {
    }

    private void showBoard() {
        var boardDetails = boardService.getBoardDetails(board);
        System.out.printf("Board [%s,%s]\n", boardDetails.id(), boardDetails.name());
        boardDetails.columns().forEach(c ->
                System.out.printf("Coluna [%s] tipo: [%s] tem %s cards\n",
                        c.name(), c.type(), c.cardsAmount())
        );
    }

    private void cancelCard() {
        System.out.println("Informe o id do card que deseja mover para a coluna de cancelamento");
        var cardId = scanner.nextLong();
        cardService.cancelCard(cardId);
    }

    private void unblockCard() {
    }

    private void blockCard() {
    }

    private void moveCardToNextColumn() {

    }

    private void createCard() {

    }
}

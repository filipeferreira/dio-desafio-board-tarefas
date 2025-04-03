package com.dio.bootcampbradesco2025.board.ui;

import com.dio.bootcampbradesco2025.board.entity.Board;
import com.dio.bootcampbradesco2025.board.entity.BoardColumn;
import com.dio.bootcampbradesco2025.board.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

import static com.dio.bootcampbradesco2025.board.enums.BoardColumnTypeEnum.CANCEL;
import static com.dio.bootcampbradesco2025.board.enums.BoardColumnTypeEnum.FINAL;
import static com.dio.bootcampbradesco2025.board.enums.BoardColumnTypeEnum.INITIAL;
import static com.dio.bootcampbradesco2025.board.enums.BoardColumnTypeEnum.PENDING;

@RequiredArgsConstructor
@Component
public class MainMenu {

    private final Scanner scanner = new Scanner(System.in).useDelimiter("\n");
    private final BoardService boardService;
    private final BoardMenu boardMenu;

    public void execute() {
        System.out.println("Bem vindo ao gerenciador de boards, escolha a opção desejada");
        var option = -1;
        while (true){
            System.out.println("1 - Criar um novo board");
            System.out.println("2 - Selecionar um board existente");
            System.out.println("3 - Excluir um board");
            System.out.println("4 - Sair");
            option = scanner.nextInt();
            switch (option){
                case 1 -> createBoard();
                case 2 -> selectBoard();
                case 3 -> deleteBoard();
                case 4 -> System.exit(0);
                default -> System.out.println("Opção inválida, informe uma opção do menu");
            }
        }
    }

    private void deleteBoard() {
        System.out.println("Informe o id do board que será excluído");
        var id = scanner.nextLong();
        try {
            boardService.delete(id);
            System.out.println("Board excluído com sucesso");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    private void selectBoard() {
        System.out.println("Informe o id do board que deseja selecionar");
        var id = scanner.nextLong();
        boardService.findById(id).ifPresentOrElse(
                boardMenu::execute,
                () -> System.out.printf("Não foi encontrado um board com id %s\n", id)
        );
    }

    private void createBoard() {
        var entity = new Board();
        System.out.println("Informe o nome do seu board");
        entity.setName(scanner.next());

        System.out.println("Seu board terá colunas além das 3 padrões? Se sim informe quantas, senão digite '0'");
        var additionalColumns = scanner.nextInt();

        Set<BoardColumn> columns = new HashSet<>();

        System.out.println("Informe o nome da coluna inicial do board");
        var initialColumnName = scanner.next();
        var initialColumn = new BoardColumn(initialColumnName, INITIAL, 0);
        columns.add(initialColumn);

        for (int i = 0; i < additionalColumns; i++) {
            System.out.println("Informe o nome da coluna de tarefa pendente do board");
            var pendingColumnName = scanner.next();
            var pendingColumn = new BoardColumn(pendingColumnName, PENDING, i + 1);
            columns.add(pendingColumn);
        }

        System.out.println("Informe o nome da coluna final");
        var finalColumnName = scanner.next();
        var finalColumn = new BoardColumn(finalColumnName, FINAL, additionalColumns + 1);
        columns.add(finalColumn);

        System.out.println("Informe o nome da coluna de cancelamento do baord");
        var cancelColumnName = scanner.next();
        var cancelColumn = new BoardColumn(cancelColumnName, CANCEL, additionalColumns + 2);
        columns.add(cancelColumn);

        entity.setColumns(columns);
        boardService.create(entity);

    }
}

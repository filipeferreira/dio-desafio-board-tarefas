package com.dio.bootcampbradesco2025.board.entity;

import com.dio.bootcampbradesco2025.board.enums.BoardColumnTypeEnum;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Set;

@Data
@NoArgsConstructor
@Entity
@Table(name = "BOARDS_COLUMNS", uniqueConstraints = @UniqueConstraint(columnNames = {"board_id", "order"}))
public class BoardColumn {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(name = "\"order\"", nullable = false)
    private int order;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 7)
    private BoardColumnTypeEnum type;

    @EqualsAndHashCode.Exclude
    @ManyToOne
    @JoinColumn(name = "board_id", nullable = false)
    private Board board;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @OneToMany(mappedBy = "boardColumn", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Card> cards;

    public BoardColumn(String name, BoardColumnTypeEnum type, int order, Board board) {
        this.name = name;
        this.type = type;
        this.order = order;
        this.board = board;
    }
}

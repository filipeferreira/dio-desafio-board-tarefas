package com.dio.bootcampbradesco2025.board.entities;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Data;
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

    @Column(nullable = false, length = 255)
    private String name;

    @Column(name = "\"order\"", nullable = false)
    private int order;

    @Column(nullable = false, length = 7)
    private String kind;

    @ManyToOne
    @JoinColumn(name = "board_id", nullable = false)
    private Board board;

    @ToString.Exclude
    @OneToMany(mappedBy = "boardColumn", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Card> cards;

}

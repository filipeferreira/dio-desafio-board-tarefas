package com.dio.bootcampbradesco2025.board.entity;

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
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Data
@NoArgsConstructor
@Entity
@Table(name = "CARDS")
public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String description;

    @ManyToOne
    @JoinColumn(name = "board_column_id", nullable = false)
    private BoardColumn boardColumn;

    @ToString.Exclude
    @OneToMany(mappedBy = "card", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Block> blocks;

    public boolean isBlocked() {
        return Optional.ofNullable(getBlocksOrderedByDate().getLast())
                .map(Block::isBlocked)
                .orElse(false);
    }

    public List<Block> getBlocksOrderedByDate() {
        return Optional.ofNullable(blocks)
                .map(b -> b.stream()
                        .sorted((o1, o2) -> {
                            var eventDate1 = o1.getBlockedAt() != null ? o1.getBlockedAt() : o1.getUnblockedAt();
                            var eventDate2 = o2.getBlockedAt() != null ? o2.getBlockedAt() : o2.getUnblockedAt();
                            return eventDate1.compareTo(eventDate2);
                        })
                        .toList())
                .orElse(Collections.emptyList());
    }

}

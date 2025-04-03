package com.dio.bootcampbradesco2025.board.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Entity
@Table(name = "BLOCKS")
public class Block {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "blocked_at", nullable = false, updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime blockedAt;

    @Column(name = "block_reason", nullable = false)
    private String blockReason;

    @Column(name = "unblocked_at")
    private LocalDateTime unblockedAt;

    @Column(name = "unblock_reason")
    private String unblockReason;

    @EqualsAndHashCode.Exclude
    @ManyToOne
    @JoinColumn(name = "card_id", nullable = false)
    private Card card;

    public boolean isBlocked() {
        return blockedAt != null;
    }

}

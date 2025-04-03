CREATE TABLE BLOCKS(
    id BIGSERIAL PRIMARY KEY,
    blocked_at TIMESTAMP,
    block_reason VARCHAR(255),
    unblocked_at TIMESTAMP NULL,
    unblock_reason VARCHAR(255) NULL,
    card_id BIGINT NOT NULL,
    CONSTRAINT cards__blocks_fk FOREIGN KEY (card_id) REFERENCES CARDS(id) ON DELETE CASCADE
);

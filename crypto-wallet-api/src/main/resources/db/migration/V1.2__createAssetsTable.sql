CREATE TABLE assets (
    id UUID PRIMARY KEY,
    symbol VARCHAR(255) NOT NULL,
    quantity NUMERIC NOT NULL,
    price NUMERIC NOT NULL,
    wallet_id UUID NOT NULL,

    CONSTRAINT fk_wallet_asset
        FOREIGN KEY (wallet_id)
        REFERENCES wallets(id)
        ON DELETE CASCADE
);

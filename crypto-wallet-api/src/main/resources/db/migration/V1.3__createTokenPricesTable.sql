CREATE TABLE token_prices (
    id UUID PRIMARY KEY,
    symbol VARCHAR(50) NOT NULL,
    price NUMERIC(19, 4) NOT NULL,
    timestamp TIMESTAMP NOT NULL
);

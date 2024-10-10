ALTER TABLE cartoes_credito
    ADD COLUMN limite_disponivel DECIMAL(10, 2) NOT NULL DEFAULT 0.00,
ADD COLUMN total_compras_abertas DECIMAL(10, 2) NOT NULL DEFAULT 0.00;
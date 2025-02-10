CREATE TABLE status_parcela_compra (
                                       id SERIAL PRIMARY KEY,
                                       nome VARCHAR(50) NOT NULL UNIQUE CHECK (nome IN ('PENDENTE', 'PAGA', 'ATRASADA'))
);

-- Inserção dos status padrão
INSERT INTO status_parcela_compra (nome) VALUES ('PENDENTE'), ('PAGA'), ('ATRASADA');
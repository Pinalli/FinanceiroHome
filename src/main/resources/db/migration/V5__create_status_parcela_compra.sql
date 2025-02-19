-- V13_create_table_status_parcela_compra.sql
CREATE TABLE status_parcela_compra (
                                       id SERIAL PRIMARY KEY,
                                       nome VARCHAR(50) NOT NULL UNIQUE
);

-- Inserção de status padrão
INSERT INTO status_parcela_compra (nome) VALUES ('PENDENTE'), ('PAGA'), ('ATRASADA');
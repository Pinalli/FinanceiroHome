
-- Define the enum type first
CREATE TYPE status_parcela_compra_enum AS ENUM ('pendente', 'paga', 'cancelada');

CREATE TABLE status_parcela_compra (
    id SERIAL PRIMARY KEY,
    parcela_id INT NOT NULL,
    status status_parcela_compra_enum NOT NULL DEFAULT 'pendente',
    data_alteracao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (parcela_id) REFERENCES parcela_compra(id)
);

-- Adiciona comentários para documentação
COMMENT ON TABLE status_parcela_compra IS 'Tabela que armazena o histórico de status das parcelas.';
COMMENT ON COLUMN status_parcela_compra .parcela_id IS 'Vincula o status à parcela correspondente.';
COMMENT ON COLUMN status_parcela_compra .status IS 'Status da parcela (pendente, paga, cancelada).';
COMMENT ON COLUMN status_parcela_compra .data_alteracao IS 'Data e hora em que o status foi alterado.';

-- Adiciona um índice na coluna parcela_id para melhorar o desempenho das consultas
CREATE INDEX idx_status_parcela_compra_parcela_id ON status_parcela_compra (parcela_id);
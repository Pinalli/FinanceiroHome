-- Cria o tipo enumerado se ele não existir
DO $$
BEGIN
    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'status_parcelas_enum') THEN
CREATE TYPE status_parcelas_enum AS ENUM ('pendente', 'paga', 'cancelada');
END IF;
END $$;

-- Cria a tabela status_parcelas com melhorias
CREATE TABLE status_parcelas (
                                 id SERIAL PRIMARY KEY,
                                 parcela_id INT NOT NULL,
                                 status status_parcelas_enum NOT NULL DEFAULT 'pendente',
                                 data_alteracao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                 usuario_id INT NOT NULL, -- Quem alterou o status
                                 FOREIGN KEY (parcela_id) REFERENCES parcelas(id),
                                 FOREIGN KEY (usuario_id) REFERENCES usuarios(id)
);

-- Adiciona um índice para melhorar consultas por parcela_id
CREATE INDEX idx_status_parcelas_parcela_id ON status_parcelas(parcela_id);

-- Adiciona comentários para documentação
COMMENT ON TABLE status_parcelas IS 'Tabela que armazena o histórico de alterações de status das parcelas.';
COMMENT ON COLUMN status_parcelas.parcela_id IS 'Vincula o status à parcela correspondente.';
COMMENT ON COLUMN status_parcelas.status IS 'Status da parcela (pendente, paga, cancelada).';
COMMENT ON COLUMN status_parcelas.data_alteracao IS 'Data e hora em que o status foi alterado.';
COMMENT ON COLUMN status_parcelas.usuario_id IS 'Usuário que alterou o status da parcela.';

-- Adiciona uma constraint para garantir transições válidas de status
ALTER TABLE status_parcelas
    ADD CONSTRAINT chk_status_valido
        CHECK (status IN ('pendente', 'paga', 'cancelada'));
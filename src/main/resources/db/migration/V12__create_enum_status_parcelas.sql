-- Cria o tipo enumerado se ele não existir
DO $$
BEGIN
    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'status_parcelas_enum') THEN
CREATE TYPE status_parcelas_enum AS ENUM ('PENDENTE', 'PAGO', 'CANCELADA');
END IF;
END $$;

-- Criação da tabela status_parcelas
CREATE TABLE status_parcelas (
                                 id SERIAL PRIMARY KEY,
                                 parcela_id INT NOT NULL,
                                 status status_parcelas_enum NOT NULL DEFAULT 'PENDENTE',
                                 data_alteracao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                 usuario_id INT NOT NULL, -- Quem alterou o status
                                 FOREIGN KEY (parcela_id) REFERENCES parcela_compra(id),
                                 FOREIGN KEY (usuario_id) REFERENCES usuario(id)
);

-- Adiciona um índice para melhorar consultas por parcela_id
CREATE INDEX idx_status_parcelas_parcela_id ON status_parcelas(parcela_id);

-- Adiciona comentários para documentação
COMMENT ON TABLE status_parcelas IS 'Tabela que armazena o histórico de alterações de status das parcelas.';
COMMENT ON COLUMN status_parcelas.parcela_id IS 'Vincula o status à parcela correspondente.';
COMMENT ON COLUMN status_parcelas.status IS 'Status da parcela (pendente, paga, cancelada).';
COMMENT ON COLUMN status_parcelas.data_alteracao IS 'Data e hora em que o status foi alterado.';
COMMENT ON COLUMN status_parcelas.usuario_id IS 'Usuário que alterou o status da parcela.';

-- Criação da função de validação
CREATE OR REPLACE FUNCTION validar_transicao_status()
RETURNS TRIGGER AS $$
BEGIN
    -- Valida transição para 'paga'
    IF NEW.status = 'PAGO' AND
       (SELECT status FROM parcela_compra WHERE id = NEW.parcela_id) <> 'PENDENTE' THEN
        RAISE EXCEPTION 'A parcela só pode ser marcada como paga se estiver pendente.';
END IF;

    -- Valida transição para 'cancelada'
    IF NEW.status = 'CANCELADA' AND
       (SELECT status FROM parcela_compra WHERE id = NEW.parcela_id) <> 'pendente' THEN
        RAISE EXCEPTION 'A parcela só pode ser cancelada se estiver pendente.';
END IF;

RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Criação do trigger
CREATE TRIGGER trigger_validar_status
    BEFORE INSERT OR UPDATE ON status_parcelas
                         FOR EACH ROW EXECUTE FUNCTION validar_transicao_status();

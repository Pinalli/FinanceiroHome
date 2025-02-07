CREATE TABLE contas (
                        id SERIAL PRIMARY KEY,
                        descricao VARCHAR(50) NOT NULL,
                        valor DECIMAL(10, 2) NOT NULL,
                        tipo BOOLEAN NOT NULL, -- TRUE para contas a pagar, FALSE para contas a receber
                        data_vencimento DATE, -- Data de vencimento (para contas a pagar)
                        data_recebimento DATE, -- Data de recebimento (para contas a receber)
                        status VARCHAR(50) NOT NULL, -- Status da conta (ex: "pendente", "paga", "recebida")
                        categoria VARCHAR(100), -- Categoria da conta (ex: "Alimentação", "Moradia")
                        usuario_id INTEGER NOT NULL,
                        FOREIGN KEY (usuario_id) REFERENCES usuario(id)
);

-- Comentários adicionais para documentação
COMMENT ON COLUMN contas.tipo IS 'TRUE para contas a pagar, FALSE para contas a receber';
COMMENT ON COLUMN contas.data_vencimento IS 'Data de vencimento (para contas a pagar)';
COMMENT ON COLUMN contas.data_recebimento IS 'Data de recebimento (para contas a receber)';
COMMENT ON COLUMN contas.status IS 'Status da conta (ex: "pendente", "paga", "recebida")';
COMMENT ON COLUMN contas.categoria IS 'Categoria da conta (ex: "Alimentação", "Moradia")';
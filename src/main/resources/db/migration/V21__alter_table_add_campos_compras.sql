
ALTER TABLE contas
    ADD COLUMN data_vencimento DATE;
ALTER TABLE contas
    ADD COLUMN data_recebimento DATE;
ALTER TABLE contas
    ADD COLUMN status VARCHAR(50) NOT NULL DEFAULT 'pendente';
ALTER TABLE contas
    ADD COLUMN categoria VARCHAR(100);

-- Comentários adicionais para documentação
COMMENT ON COLUMN contas.data_vencimento IS 'Data de vencimento (para contas a pagar)';
COMMENT ON COLUMN contas.data_recebimento IS 'Data de recebimento (para contas a receber)';
COMMENT ON COLUMN contas.status IS 'Status da conta (ex: "pendente", "paga", "recebida")';
COMMENT ON COLUMN contas.categoria IS 'Categoria da conta (ex: "Alimentação", "Moradia")';
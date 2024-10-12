-- Renomear a coluna 'data' para 'data_compra' para maior clareza
ALTER TABLE compras RENAME COLUMN data TO data_compra;

-- Adicionar uma coluna para o status da compra
ALTER TABLE compras ADD COLUMN status VARCHAR(20) NOT NULL DEFAULT 'ABERTO';

-- Adicionar uma coluna para o valor de cada parcela
ALTER TABLE compras ADD COLUMN valor_parcela DECIMAL(10, 2);

-- Adicionar uma coluna para o limite disponível no momento da compra (opcional, mas pode ser útil para relatórios)
ALTER TABLE compras ADD COLUMN limite_disponivel_momento_compra DECIMAL(10, 2);

-- Garantir que parcelas e parcelas_pagas não sejam nulos
ALTER TABLE compras ALTER COLUMN parcelas SET NOT NULL;
ALTER TABLE compras ALTER COLUMN parcelas_pagas SET NOT NULL;

-- Adicionar uma restrição para garantir que parcelas_pagas não exceda parcelas
ALTER TABLE compras ADD CONSTRAINT check_parcelas_pagas CHECK (parcelas_pagas <= parcelas);

-- Adicionar índices para melhorar o desempenho de consultas frequentes
CREATE INDEX idx_compras_cartao_credito ON compras(cartao_credito_id);
CREATE INDEX idx_compras_usuario ON compras(usuario_id);
CREATE INDEX idx_compras_data ON compras(data_compra);
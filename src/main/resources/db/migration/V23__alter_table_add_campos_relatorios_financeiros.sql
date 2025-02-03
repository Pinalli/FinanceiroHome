-- Adiciona o campo tipo_relatorio
ALTER TABLE relatorios_financeiros
    ADD COLUMN tipo_relatorio VARCHAR(100) NOT NULL DEFAULT 'Despesas Mensais';

-- Adiciona o campo status
ALTER TABLE relatorios_financeiros
    ADD COLUMN status VARCHAR(50) NOT NULL DEFAULT 'gerado';

-- Adiciona o campo caminho_arquivo
ALTER TABLE relatorios_financeiros
    ADD COLUMN caminho_arquivo VARCHAR(255);

-- Adiciona o campo data_criacao
ALTER TABLE relatorios_financeiros
    ADD COLUMN data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP;

-- Comentários adicionais para documentação
COMMENT ON COLUMN relatorios_financeiros.tipo_relatorio IS 'Tipo de relatório gerado (ex: "Despesas Mensais", "Saldo entre Contas").';
COMMENT ON COLUMN relatorios_financeiros.status IS 'Status do relatório (ex: "gerado", "pendente", "exportado").';
COMMENT ON COLUMN relatorios_financeiros.caminho_arquivo IS 'Caminho do arquivo gerado (se exportado para PDF ou Excel).';
COMMENT ON COLUMN relatorios_financeiros.data_criacao IS 'Data e hora em que o relatório foi gerado.';
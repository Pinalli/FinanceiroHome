
-- Adiciona a constraint para garantir que pelo menos um dos campos (conta_id ou cartao_credito_id) esteja preenchido
ALTER TABLE relatorios_financeiros_itens
    ADD CONSTRAINT chk_conta_or_cartao
        CHECK (conta_id IS NOT NULL OR cartao_credito_id IS NOT NULL);

-- Adiciona comentários para documentação
COMMENT ON TABLE relatorios_financeiros_itens IS 'Tabela que armazena os itens detalhados de um relatório financeiro.';
COMMENT ON COLUMN relatorios_financeiros_itens.relatorio_financeiro_id IS 'Vincula o item a um relatório financeiro.';
COMMENT ON COLUMN relatorios_financeiros_itens.conta_id IS 'Vincula o item a uma conta (a pagar ou a receber).';
COMMENT ON COLUMN relatorios_financeiros_itens.cartao_credito_id IS 'Vincula o item a uma compra com cartão de crédito.';
COMMENT ON COLUMN relatorios_financeiros_itens.tipo IS 'Define se o item é uma receita (true) ou despesa (false).';
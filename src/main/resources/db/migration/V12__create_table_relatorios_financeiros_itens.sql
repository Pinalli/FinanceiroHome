-- Certifique-se de que o nome do arquivo está correto (sem erros de digitação)
CREATE TABLE relatorios_financeiros_itens (
                                              id BIGSERIAL PRIMARY KEY,
                                              relatorio_financeiro_id BIGINT NOT NULL REFERENCES relatorios_financeiros(id),
                                              conta_id INTEGER,
                                              cartao_credito_id INTEGER,
                                              tipo BOOLEAN NOT NULL, -- Coluna adicionada
                                              FOREIGN KEY (relatorio_financeiro_id) REFERENCES relatorios_financeiros(id),
                                              FOREIGN KEY (conta_id) REFERENCES contas(id),
                                              FOREIGN KEY (cartao_credito_id) REFERENCES cartao_credito(id),
                                              CONSTRAINT chk_conta_or_cartao CHECK (conta_id IS NOT NULL OR cartao_credito_id IS NOT NULL)
);

COMMENT ON TABLE relatorios_financeiros_itens IS 'Tabela que armazena os itens detalhados de um relatório financeiro.';
COMMENT ON COLUMN relatorios_financeiros_itens.relatorio_financeiro_id IS 'Vincula o item a um relatório financeiro.';
COMMENT ON COLUMN relatorios_financeiros_itens.conta_id IS 'Vincula o item a uma conta (a pagar ou a receber).';
COMMENT ON COLUMN relatorios_financeiros_itens.cartao_credito_id IS 'Vincula o item a uma compra com cartão de crédito.';
COMMENT ON COLUMN relatorios_financeiros_itens.tipo IS 'Define se o item é uma receita (true) ou despesa (false).';



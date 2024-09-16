CREATE TABLE relatorios_financeiros_itens (
                                              id SERIAL PRIMARY KEY,
                                              relatorio_financeiro_id INTEGER NOT NULL,
                                              conta_id INTEGER,
                                              cartao_credito_id INTEGER,
                                              data DATE NOT NULL,
                                              valor DECIMAL(10, 2) NOT NULL,
                                              tipo BOOLEAN NOT NULL,  -- true = receita, false = despesa
                                              FOREIGN KEY (relatorio_financeiro_id) REFERENCES relatorios_financeiros(id),
                                              FOREIGN KEY (conta_id) REFERENCES contas(id),
                                              FOREIGN KEY (cartao_credito_id) REFERENCES cartoes_credito(id)
);
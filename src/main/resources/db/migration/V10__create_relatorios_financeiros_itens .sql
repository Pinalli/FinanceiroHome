CREATE TABLE relatorios_financeiros_itens (
                                              id SERIAL PRIMARY KEY,
                                              relatorio_id INT NOT NULL REFERENCES relatorios_financeiros(id) ON DELETE CASCADE,
                                              descricao VARCHAR(255) NOT NULL,
                                              valor DECIMAL(10,2) NOT NULL CHECK (valor > 0),
                                              categoria_id INT REFERENCES categoria(id), -- FK já na criação
                                              metadata JSONB
);

CREATE INDEX idx_relatorios_itens_relatorio ON relatorios_financeiros_itens(relatorio_id);

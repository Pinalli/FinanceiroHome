
CREATE TABLE relatorios_financeiros_itens (
                                              id SERIAL PRIMARY KEY,
                                              relatorio_id INT NOT NULL REFERENCES relatorios_financeiros(id) ON DELETE CASCADE,
                                              descricao VARCHAR(255) NOT NULL,
                                              valor DECIMAL(10,2) NOT NULL,
                                              categoria_id INT REFERENCES categories(id),
                                              metadata JSONB
);

ALTER TABLE relatorios_financeiros_itens
    RENAME COLUMN categoria_id TO categoria_id;

ALTER TABLE relatorios_financeiros_itens
    ADD CONSTRAINT fk_categoria FOREIGN KEY (categoria_id) REFERENCES categoria(id);

CREATE INDEX idx_relatorios_itens_relatorio ON relatorios_financeiros_itens(relatorio_id);
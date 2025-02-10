CREATE TABLE relatorios_financeiros (
                                        id SERIAL PRIMARY KEY,
                                        tipo VARCHAR(50) NOT NULL CHECK (tipo IN ('DESPESAS_CATEGORIA', 'RECEITAS_CATEGORIA', 'SALDO_MENSAL')),
                                        periodo_inicio DATE NOT NULL,
                                        periodo_fim DATE NOT NULL,
                                        usuario_id INT NOT NULL REFERENCES usuario(id) ON DELETE CASCADE,
                                        generated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE relatorios_financeiros_itens (
                                              id SERIAL PRIMARY KEY,
                                              relatorio_id INT NOT NULL REFERENCES relatorios_financeiros(id) ON DELETE CASCADE,
                                              descricao VARCHAR(255) NOT NULL,
                                              valor DECIMAL(10,2) NOT NULL,
                                              categoria_id INT REFERENCES categories(id),
                                              metadata JSONB
);
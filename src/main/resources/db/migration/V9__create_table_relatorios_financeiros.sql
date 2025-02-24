CREATE TABLE relatorios_financeiros (
                                        id SERIAL PRIMARY KEY,
                                        tipo VARCHAR(50) NOT NULL CHECK (tipo IN ('DESPESAS_CATEGORIA', 'RECEITAS_CATEGORIA', 'SALDO_MENSAL')),
                                        periodo_inicio DATE NOT NULL CHECK (periodo_fim >= periodo_inicio),
                                        periodo_fim DATE NOT NULL CHECK (periodo_fim >= periodo_inicio),
                                        usuario_id INT NOT NULL REFERENCES usuario(id) ON DELETE CASCADE,
                                        generated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE categories (
                            id SERIAL PRIMARY KEY,
                            nome VARCHAR(50) NOT NULL,
                            tipo VARCHAR(10) NOT NULL CHECK (tipo IN ('DESPESA', 'RECEITA', 'CARTAO')),
                            usuario_id INT REFERENCES usuario(id) ON DELETE CASCADE
);
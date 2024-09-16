CREATE TABLE contas (
                        id SERIAL PRIMARY KEY,
                        descricao VARCHAR(50) NOT NULL,
                        valor DECIMAL(10, 2) NOT NULL,
                        tipo BOOLEAN NOT NULL,
                        usuario_id INTEGER NOT NULL,
                        FOREIGN KEY (usuario_id) REFERENCES usuarios(id)
);
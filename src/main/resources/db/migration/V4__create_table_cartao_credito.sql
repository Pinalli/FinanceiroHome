CREATE TABLE cartao_credito (
                               id SERIAL PRIMARY KEY,
                               nome VARCHAR(100) NOT NULL,
                               limite DECIMAL(10, 2) NOT NULL,
                               limite_disponivel DECIMAL(10, 2) NOT NULL,
                               usuario_id INT REFERENCES usuario(id) ON DELETE CASCADE
);